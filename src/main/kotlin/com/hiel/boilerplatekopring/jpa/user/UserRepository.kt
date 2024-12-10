package com.hiel.boilerplatekopring.jpa.user

import com.hiel.boilerplatekopring.domains.user.UserStatus
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findFirstByEmail(email: String): UserEntity?

    fun findFirstByIdAndUserStatus(id: Long, userStatus: UserStatus): UserEntity?

    fun findFirstByEmailAndUserStatus(email: String, userStatus: UserStatus): UserEntity?
}
