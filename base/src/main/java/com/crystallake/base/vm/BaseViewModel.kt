package com.crystallake.base.vm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crystallake.base.net.config.NetConfig
import com.crystallake.base.net.ApiException
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

open class BaseViewModel : ViewModel(), LifecycleObserver {

    /**
     * 失败的数量
     */
    val failureRequest = AtomicInteger()

    /**
     * 正在请求的接口数量
     */
    val requestNum = AtomicInteger()

    //请求数量，当为0时没有正在请求的接口，如果数量不为0，表示有正在请求的接口
    val requestLiveData = MutableLiveData<Int>()

    private fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(Dispatchers.Main) {
            block()
        }

    private suspend fun handleException(
        block: suspend CoroutineScope.() -> Unit,
        cancel: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ApiException) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                val increment = requestNum.incrementAndGet()
                requestLiveData.postValue(increment)
                block()
            } catch (e: Throwable) {
                failureRequest.incrementAndGet()
                e.printStackTrace()
                if (e is CancellationException) {
                    cancel()
                } else {
                    error(NetConfig.getConfig().globalExceptionHandle(e))
                }
            } finally {
                val decrement = requestNum.decrementAndGet()
                requestLiveData.postValue(decrement)
                complete()
            }
        }
    }

    fun <T> request(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        cancel: suspend CoroutineScope.() -> Unit,
        error: (ApiException) -> Unit = {},
        complete: () -> Unit = {}
    ) {
        launch {
            handleException(
                block = {
                    withContext(Dispatchers.IO) {
                        block()
                    }.also {
                        success(it)
                    } ?: kotlin.run { }
                },
                cancel = {
                    cancel()
                },
                error = {
                    error(it)
                },
                complete = {
                    complete()
                }
            )
        }
    }
}