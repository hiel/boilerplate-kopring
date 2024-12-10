package com.hiel.boilerplatekopring.utilities

import com.hiel.boilerplatekopring.domains.ApiResponse
import com.hiel.boilerplatekopring.domains.ResultCode

object ApiResponseFactory {
    fun <D> success(
        data: D? = null,
    ): ApiResponse<D> = ApiResponse(resultCode = ResultCode.Common.SUCCESS, data = data)

    fun <D> failure(
        resultCode: ResultCode,
        message: String? = null,
        data: D? = null,
    ): ApiResponse<D> = ApiResponse(resultCode = resultCode, message = message, data = data)
}
