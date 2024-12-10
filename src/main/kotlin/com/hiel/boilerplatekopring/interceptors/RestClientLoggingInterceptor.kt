package com.hiel.boilerplatekopring.interceptors

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.hiel.boilerplatekopring.utilities.LogUtility
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.util.StreamUtils
import java.io.ByteArrayInputStream
import java.io.InputStream

class RestClientLoggingInterceptor : ClientHttpRequestInterceptor {
    companion object : LogUtility

    override fun intercept(req: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution): ClientHttpResponse {
        val objectMapper = jacksonObjectMapper()
        val res = execution.execute(req, body)
        val responseBody = StreamUtils.copyToByteArray(res.body)

        try {
            log.info(
                objectMapper.writeValueAsString(
                    mapOf(
                        "request" to mapOf(
                            "method" to req.method.toString(),
                            "url" to req.uri,
                            "body" to String(body, Charsets.UTF_8),
                            "headers" to req.headers,
                        ),
                    ),
                ),
            )
            log.info(
                objectMapper.writeValueAsString(
                    mapOf(
                        "response" to mapOf(
                            "statusCode" to res.statusCode,
                            "body" to String(responseBody, Charsets.UTF_8),
                            "headers" to res.headers,
                        ),
                    ),
                ),
            )
        } catch (e: Exception) {
            log.error(e.toString())
        }

        return object : ClientHttpResponse by res {
            override fun getBody(): InputStream = ByteArrayInputStream(responseBody)
        }
    }
}
