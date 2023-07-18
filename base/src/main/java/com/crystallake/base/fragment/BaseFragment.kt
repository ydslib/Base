package com.crystallake.base.fragment

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.crystallake.base.app.BaseApplication
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<VM : ViewModel> : Fragment() {

    val mViewModel: VM by lazy {
        ViewModelProvider(getOwner()).get(getViewModelClass())
    }

    //是否第一次加载
    private var isFirst: Boolean = true

    var mFragmentProvider: ViewModelProvider? = null
    var mActivityProvider: ViewModelProvider? = null
    var mApplicationProvider: ViewModelProvider? = null

    fun <T : ViewModel> getFragmentScopeViewModel(modelClass: Class<T>): T? {
        if (mFragmentProvider == null) {
            mFragmentProvider = ViewModelProvider(this)
        }
        return mFragmentProvider?.get(modelClass)
    }

    fun <T : ViewModel> getActivityScopeViewModel(modelClass: Class<T>): T? {
        if (mActivityProvider == null) {
            activity?.let {
                mActivityProvider = ViewModelProvider(it)
            }
        }
        return mActivityProvider?.get(modelClass)
    }

    fun <T : ViewModel> getApplicationScopeViewModel(modelClass: Class<T>): T? {
        if (mApplicationProvider == null) {
            activity?.let {
                mApplicationProvider = ViewModelProvider(
                    (context?.applicationContext as BaseApplication),
                    getApplicationFactory(it)
                )
            }
        }
        return mApplicationProvider?.get(modelClass)
    }

    protected open fun getOwner(): ViewModelStoreOwner = this

    private fun getViewModelClass(): Class<VM> {
        val actualTypeArguments =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        val first = actualTypeArguments.first {
            isVMClass(it as Class<*>)
        }
        return first as Class<VM>
    }

    private fun isVMClass(type: Class<*>): Boolean {
        return type.superclass?.let {
            if (it.name == "androidx.lifecycle.ViewModel" || it.name == "com.crystallake.base.vm.BaseViewModel"
                ||it.name == "androidx.lifecycle.AndroidViewModel") {
                true
            } else {
                isVMClass(it)
            }
        } ?: kotlin.run {
            false
        }
    }

    open fun getApplicationFactory(activity: Activity): ViewModelProvider.Factory {
        checkActivity(this)
        val application: Application = checkApplication(activity)
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    open fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }


    open fun checkActivity(fragment: Fragment) {
        val activity = fragment.activity
            ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(savedInstanceState)
        createObserver()
        onVisible()
        loadDataAfterCreate()
    }

    /**
     * 每次都加载
     */
    open fun loadDataAfterCreate() {

    }

    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 懒加载，只加载一次
     */
    abstract fun lazyLoadData()

    abstract fun createObserver()

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onResume() {
        super.onResume()
        onVisible()
    }
}