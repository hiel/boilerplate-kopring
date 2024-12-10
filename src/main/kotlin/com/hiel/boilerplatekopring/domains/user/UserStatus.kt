package com.hiel.boilerplatekopring.domains.user

enum class UserStatus(val description: String) {
    NOT_CERTIFICATED("미인증"),
    AVAILABLE("이용"),
    WITHDRAWAL("탈퇴"),
}