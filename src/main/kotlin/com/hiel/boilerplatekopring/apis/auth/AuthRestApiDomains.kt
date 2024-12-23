package com.hiel.boilerplatekopring.apis.auth

import com.hiel.boilerplatekopring.domains.ResultCode
import com.hiel.boilerplatekopring.domains.auth.AuthToken
import com.hiel.boilerplatekopring.domains.user.UserType
import com.hiel.boilerplatekopring.exceptions.ServiceException
import com.hiel.boilerplatekopring.jpa.user.UserEntity
import com.hiel.boilerplatekopring.utilities.Regex
import com.hiel.boilerplatekopring.utilities.isNotNullValidLengthTrimmed
import com.hiel.boilerplatekopring.utilities.regexMatches

data class SignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val userType: UserType,
) {
    fun validate() {
        if (!email.regexMatches(Regex.EMAIL)) {
            throw ServiceException(ResultCode.Auth.INVALID_FORMAT_EMAIL)
        }
        if (!password.isNotNullValidLengthTrimmed(min = UserEntity.PASSWORD_MINIMUM_LENGTH)) {
            throw ServiceException(
                resultCode = ResultCode.Auth.LENGTH_TOO_SHORT_PASSWORD,
                args = arrayOf(UserEntity.PASSWORD_MINIMUM_LENGTH),
            )
        }
        if (!name.isNotNullValidLengthTrimmed(min = UserEntity.USERNAME_MINIMUM_LENGTH)) {
            throw ServiceException(
                resultCode = ResultCode.Auth.LENGTH_TOO_SHORT_NAME,
                args = arrayOf(UserEntity.USERNAME_MINIMUM_LENGTH),
            )
        }
    }
}

data class CertificateSignupRequest(
    val signupToken: String,
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class IssueTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val id: Long,
    val email: String,
    val name: String,
    val userType: UserType,
) {
    companion object {
        fun build(authToken: AuthToken, userEntity: UserEntity) = IssueTokenResponse(
            accessToken = authToken.accessToken,
            refreshToken = authToken.refreshToken,
            id = userEntity.id,
            email = userEntity.email,
            name = userEntity.name,
            userType = userEntity.userType,
        )
    }
}

data class RefreshTokenRequest(
    val refreshToken: String,
)

data class RequestPasswordResetRequest(
    val email: String,
)

data class ResetPasswordRequest(
    val resetPasswordToken: String,
)

data class ResetPasswordResponse(
    val password: String,
)
