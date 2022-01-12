package com.crystallake.base.net

import com.crystallake.base.R
import com.crystallake.base.utils.Util


/**
 *   @auther : Aleyn
 *   time   : 2019/08/12
 */
enum class ERROR(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, Util.getApp().getString(R.string.unknow_error)),
    /**
     * 解析错误
     */
    PARSE_ERROR(1001, Util.getApp().getString(R.string.parsing_error)),
    /**
     * 网络错误
     */
    NETWORD_ERROR(1002, Util.getApp().getString(R.string.network_error)),
    /**
     * 协议出错
     */
    HTTP_ERROR(1003, Util.getApp().getString(R.string.protocol_error)),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, Util.getApp().getString(R.string.certificate_error)),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, Util.getApp().getString(R.string.connection_timed_out));

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}