package com.hiel.boilerplatekopring.apis.example

import com.hiel.boilerplatekopring.domains.ApiResponse
import com.hiel.boilerplatekopring.utilities.ApiResponseFactory
import com.hiel.boilerplatekopring.utilities.validate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/examples")
@RestController
class ExampleRestApiController(
    private val exampleService: ExampleService,
) {
    @PostMapping("/upload-file")
    fun uploadFile(
        file: MultipartFile,
    ): ApiResponse<String> {
        file.validate(
            maxSizeByte = 5,
            allowExtensions = listOf("png", "jpg", "jpeg"),
        )
        return ApiResponseFactory.success(exampleService.uploadFile(file))
    }
}
