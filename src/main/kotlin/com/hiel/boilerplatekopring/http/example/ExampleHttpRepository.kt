package com.hiel.boilerplatekopring.http.example

import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface ExampleHttpRepository {
    @GetExchange("/")
    fun getExample(
        @RequestParam("name") name: String,
    ): ExampleHttpResponses
}
