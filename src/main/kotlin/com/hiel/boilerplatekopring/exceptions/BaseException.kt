package com.hiel.boilerplatekopring.exceptions

import com.hiel.boilerplatekopring.domains.ResultCode

open class BaseException(
    open val resultCode: ResultCode,
    open val data: Any? = null,
    vararg args: Any,
) : RuntimeException(resultCode.getMessage(*args))
