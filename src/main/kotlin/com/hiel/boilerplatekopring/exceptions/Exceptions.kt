package com.hiel.boilerplatekopring.exceptions

import com.hiel.boilerplatekopring.domains.ResultCode

class ServiceException(
    override val resultCode: ResultCode = ResultCode.Common.FAIL,
    override val data: Any? = null,
    vararg args: Any,
) : BaseException(resultCode = resultCode, data = data, args = args)
