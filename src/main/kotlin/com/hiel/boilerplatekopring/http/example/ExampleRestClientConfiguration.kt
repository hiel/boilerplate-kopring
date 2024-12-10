package com.hiel.boilerplatekopring.http.example

import com.hiel.boilerplatekopring.interceptors.RestClientLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.util.DefaultUriBuilderFactory
import java.nio.charset.StandardCharsets

@Configuration
class ExampleRestClientConfiguration(
    @Value("\${open-api-url}")
    private var url: String,
) {
    @Bean
    fun exampleHttpRepository(): ExampleHttpRepository {
        val uriBuilderFactory = DefaultUriBuilderFactory(url)
        uriBuilderFactory.encodingMode = DefaultUriBuilderFactory.EncodingMode.NONE
        val restClient = RestClient.builder()
            .requestInterceptor(RestClientLoggingInterceptor())
            .defaultHeaders {
                it.contentType = MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8)
            }
            .uriBuilderFactory(uriBuilderFactory)
            .baseUrl(url)
            .build()
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(ExampleHttpRepository::class.java)
    }
}
