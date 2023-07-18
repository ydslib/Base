package com.crystallake.base.activity

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crystallake.base.app.BaseApplication
import java.lang.reflect.ParameterizedType

abstract class BaseActivity<VM : ViewModel> : AppCompatActivity() {

    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null

    val mViewModel: VM by lazy {
        ViewModelProvider(this).get(getViewModelClass())
    }


    private fun getViewModelClass(): Class<VM> {
        //获取父类真实范型类型
        val actualTypeArguments =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        //取出第一个ViewModel类型的范型
        val first = actualTypeArguments.first {
            isVMClass(it as Class<*>)
        }
        return first as Class<VM>
    }

    fun isVMClass(type: Class<*>): Boolean {
        return type.superclass?.let {
            if (it?.name == "androidx.lifecycle.ViewModel" || it?.name == "com.crystallake.base.vm.BaseViewModel"
                || it.name == "androidx.lifecycle.AndroidViewModel"
            ) {
                true
            } else {
                isVMClass(it)
            }
        } ?: false
    }

    /**
     * activity级别的viewmodel
     */
    protected open fun <T : ViewModel?> getActivityScopeViewModel(modelClass: Class<T>): T? {
        if (mActivityProvider == null) {
            mActivityProvider = ViewModelProvider(this)
        }
        return mActivityProvider?.get(modelClass)
    }

    /**
     * application级别的viewmodel
     */
    protected open fun <T : ViewModel?> getApplicationScopeViewModel(modelClass: Class<T>): T? {
        if (mApplicationProvider == null) {
            mApplicationProvider = ViewModelProvider(
                applicationContext as BaseApplication,
                getAppFactory(this)
            )
        }
        return mApplicationProvider?.get(modelClass)
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory {
        val application = checkApplication(activity)
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }

}