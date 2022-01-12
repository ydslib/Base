package com.crystallake.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import java.util.*

object Util {

    private var sApplication: Application? = null
    private val ACTIVITY_LIFECYCLE by lazy { ActivityLifecycleImpl() }

    fun setUp(context: Context?) {
        context?.let {
            setUp((it as Application).applicationContext)
        } ?: kotlin.run {
            setUp(getApplicationByReflect())
        }
    }

    fun setUp(app: Application?) {
        if (sApplication == null) {
            sApplication = app ?: getApplicationByReflect()
            sApplication?.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE)
        } else {
            if (app != null && app.javaClass != sApplication?.javaClass) {
                sApplication?.unregisterActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE)
                ACTIVITY_LIFECYCLE.mActivityList.clear()
                sApplication = app
                sApplication?.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE)
            }
        }
    }

    fun getApp(): Application {
        if (sApplication != null) {
            return sApplication!!
        }
        val app = getApplicationByReflect()
        setUp(app)
        return app
    }

    fun getCurrentActivity(): Activity {
        return ACTIVITY_LIFECYCLE.getTopActivity()
    }


    private fun getApplicationByReflect(): Application {
        try {
            @SuppressLint("PrivateApi") val activityThread =
                Class.forName("android.app.ActivityThread")
            val thread = activityThread.getMethod("currentActivityThread").invoke(null)
            val app = activityThread.getMethod("getApplication").invoke(thread)
                ?: throw NullPointerException("u should init first")
            return app as Application
        } catch (e: Exception) {
            e.printStackTrace()
        }
        throw NullPointerException("u should init first")
    }


    class ActivityLifecycleImpl : ActivityLifecycleCallbacks {

        val mActivityList: LinkedList<Activity> = LinkedList()
        val mStatusListenerMap: Map<Any, OnAppStatusChangedListener?> =
            HashMap<Any, OnAppStatusChangedListener?>()

        /**
         * 前台Activity个数
         */
        private var mForegroundCount = 0

        private var mConfigCount = 0

        /**
         * 是否处于后台
         */
        private var mIsBackground = false

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            setTopActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            if (!mIsBackground) {
                setTopActivity(activity)
            }
            if (mConfigCount < 0) {
                mConfigCount++
            } else {
                mForegroundCount++
            }
        }

        override fun onActivityResumed(activity: Activity) {
            setTopActivity(activity)
            if (mIsBackground) {
                mIsBackground = false
                postStatus(true)
            }
        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {
            if (activity.isChangingConfigurations) {
                --mConfigCount
            } else {
                --mForegroundCount
                if (mForegroundCount <= 0) {
                    mIsBackground = true
                    postStatus(false)
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            mActivityList.remove(activity)

        }


        /**
         * 设置顶部Activity
         */
        private fun setTopActivity(activity: Activity) {
            if (mActivityList.contains(activity)) {
                if (!mActivityList.last.equals(activity)) {
                    mActivityList.remove(activity)
                    mActivityList.addLast(activity)
                }
            } else {
                mActivityList.addLast(activity)
            }
        }

        fun getTopActivity(): Activity {
            if (!mActivityList.isEmpty()) {
                val topActivity = mActivityList.last
                if (topActivity != null) {
                    return topActivity
                }
            }
            val topActivityByReflect = getTopActivityByReflect()
            if (topActivityByReflect != null) {
                setTopActivity(topActivityByReflect)
            }
            return topActivityByReflect!!
        }

        private fun getTopActivityByReflect(): Activity? {
            try {
                @SuppressLint("PrivateApi") val activityThreadClass =
                    Class.forName("android.app.ActivityThread")
                val currentActivityThreadMethod =
                    activityThreadClass.getMethod("currentActivityThread").invoke(null)
                val mActivityListField = activityThreadClass.getDeclaredField("mActivityList")
                mActivityListField.isAccessible = true
                val activities = mActivityListField[currentActivityThreadMethod] as? Map<*, *>
                    ?: return null
                for (activityRecord in activities.values) {
                    val activityRecordClass: Class<*>? = activityRecord?.javaClass
                    val pausedField = activityRecordClass?.getDeclaredField("paused")
                    pausedField?.isAccessible = true
                    if (pausedField?.getBoolean(activityRecord)==false) {
                        val activityField = activityRecordClass.getDeclaredField("activity")
                        activityField.isAccessible = true
                        return activityField[activityRecord] as? Activity
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        private fun postStatus(isForeground: Boolean) {
            if (mStatusListenerMap.isEmpty()) {
                return
            }
            mStatusListenerMap.values.forEach {
                it?.let {
                    if (isForeground) {
                        it.onForeground()
                    } else {
                        it.onBackground()
                    }
                }
            }
        }

    }

    interface OnAppStatusChangedListener {
        fun onForeground()
        fun onBackground()
    }
}