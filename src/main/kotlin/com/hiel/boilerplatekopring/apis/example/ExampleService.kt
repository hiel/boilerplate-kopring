package com.hiel.boilerplatekopring.apis.example

import com.hiel.boilerplatekopring.domains.ResultCode
import com.hiel.boilerplatekopring.exceptions.ServiceException
import com.hiel.boilerplatekopring.utilities.aws.S3Utility
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ExampleService(
    private val s3Utility: S3Utility,
) {
    fun uploadFile(file: MultipartFile): String {
        return s3Utility.uploadFile(file)
            ?: throw ServiceException(ResultCode.File.FAILED_TO_UPLOAD_FILE)
    }
}
