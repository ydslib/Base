package com.crystallake.base.net


class ApiException : Exception {

    var code: String
    var errMsg: String

    constructor(error: ERROR, e: Throwable? = null) : super(e) {
        code = error.getKey().toString()
        errMsg = error.getValue()
    }

    constructor(code: String, msg: String, e: Throwable? = null) : super(e) {
        this.code = code
        this.errMsg = msg
    }

    override fun toString(): String {
        return "ApiException(code='$code', errMsg='$errMsg')"
    }


}