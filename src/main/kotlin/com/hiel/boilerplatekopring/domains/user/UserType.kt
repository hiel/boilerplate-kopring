package com.hiel.boilerplatekopring.domains.user

enum class UserType(val description: String) {
    MASTER("운영자"),
    ADMIN("관리자"),
    USER("사용자"),
}
