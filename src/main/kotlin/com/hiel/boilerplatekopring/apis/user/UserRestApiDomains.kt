package com.hiel.boilerplatekopring.apis.user

import com.hiel.boilerplatekopring.domains.ResultCode
import com.hiel.boilerplatekopring.exceptions.ServiceException
import com.hiel.boilerplatekopring.jpa.user.UserEntity
import com.hiel.boilerplatekopring.utilities.isNotNullValidLengthTrimmed

data class UpdatePasswordRequest(
    val currentPassword: String,
    val updatePassword: String,
) {
    fun validate() {
        if (!updatePassword.isNotNullValidLengthTrimmed(min = UserEntity.PASSWORD_MINIMUM_LENGTH)) {
            throw ServiceException(
                resultCode = ResultCode.Auth.LENGTH_TOO_SHORT_PASSWORD,
                args = arrayOf(UserEntity.PASSWORD_MINIMUM_LENGTH),
            )
        }
    }
}
