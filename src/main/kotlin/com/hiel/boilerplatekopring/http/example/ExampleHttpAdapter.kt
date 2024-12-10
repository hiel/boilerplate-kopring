package com.hiel.boilerplatekopring.http.example

import org.springframework.stereotype.Component

@Component
class ExampleHttpAdapter(
    private val exampleHttpRepository: ExampleHttpRepository,
) {
    fun getExample(name: String): ExampleHttpResponses {
        return exampleHttpRepository.getExample(name)
    }
}
