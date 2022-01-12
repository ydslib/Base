package com.crystallake.base.net

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 *   @auther : Aleyn
 *   time   : 2019/08/12
 */
object ExceptionHandle {

    fun handleException(e: Throwable): ApiException {
        val ex: ApiException
        if (e is ApiException) {
            ex = e
        } else if (e is HttpException) {
            ex = ApiException(ERROR.HTTP_ERROR, e)
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException || e is MalformedJsonException
        ) {
            ex = ApiException(ERROR.PARSE_ERROR, e)
        } else if (e is ConnectException) {
            ex = ApiException(ERROR.NETWORD_ERROR, e)
        } else if (e is javax.net.ssl.SSLException) {
            ex = ApiException(ERROR.SSL_ERROR, e)
        } else if (e is java.net.SocketTimeoutException) {
            ex = ApiException(ERROR.TIMEOUT_ERROR, e)
        } else if (e is java.net.UnknownHostException) {
            ex = ApiException(ERROR.TIMEOUT_ERROR, e)
        } else {
            ex = if (!e.message.isNullOrEmpty()) ApiException("1000", e.message!!, e)
            else ApiException(ERROR.UNKNOWN, e)
        }
        return ex
    }
}