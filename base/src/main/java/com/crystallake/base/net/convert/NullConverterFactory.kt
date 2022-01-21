package com.crystallake.base.net.convert

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class NullConverterFactory private constructor() : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val delegate: Converter<ResponseBody, Any> =
            retrofit.nextResponseBodyConverter(this, type, annotations)
        return Converter {
            if (it == null || it.contentLength() == 0L) {
                null
            } else {
                delegate.convert(it)
            }
        }
    }

    companion object {
        fun create(): NullConverterFactory {
            return NullConverterFactory()
        }
    }
}