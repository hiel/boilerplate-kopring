package com.hiel.boilerplatekopring.exceptions

import com.hiel.boilerplatekopring.domains.ApiResponse
import com.hiel.boilerplatekopring.domains.ResultCode
import com.hiel.boilerplatekopring.utilities.ApiResponseFactory
import com.hiel.boilerplatekopring.utilities.LogUtility
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
    companion object : LogUtility

    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(e: ServiceException): ApiResponse<*> {
        log.error(e.stackTraceToString())
        return ApiResponseFactory.failure(resultCode = e.resultCode, message = e.message, data = e.data)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ApiResponse<Unit> {
        log.error(e.stackTraceToString())
        return ApiResponseFactory.failure(resultCode = ResultCode.Common.FAIL, message = ResultCode.Common.FAIL.getMessage())
    }
}
