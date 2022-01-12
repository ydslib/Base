package com.crystallake.base.net.config

import com.crystallake.base.net.ExceptionHandle


/**
 * <pre>
 *     author : yangliu
 *     e-mail : yangliu@codemao.cn
 *     time   : 2021/05/24
 *     desc   :
 * </pre>
 */
interface GlobalConfig {
    fun globalExceptionHandle(e: Throwable) = ExceptionHandle.handleException(e)
}