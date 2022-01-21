package com.crystallake.base.net

import retrofit2.Response

abstract class BaseRequest {

    suspend fun <T> call(
        remote: suspend () -> Response<T>,
        save: ((T) -> Unit)? = null
    ): T = remote().let {
        val code = it.code()
        if (code in 200..299) {
            it.body()?.also { t ->
                save?.invoke(t)
            } ?: kotlin.run {
                throw ApiException(ERROR.UNKNOWN)
            }
        } else {
            //报错
            throw ApiException(code.toString(), it.message())
        }
    }

}