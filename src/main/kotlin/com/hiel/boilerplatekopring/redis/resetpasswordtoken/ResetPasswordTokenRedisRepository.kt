package com.hiel.boilerplatekopring.redis.resetpasswordtoken

import org.springframework.data.repository.CrudRepository

interface ResetPasswordTokenRedisRepository : CrudRepository<ResetPasswordTokenRedisEntity, Long> {
    fun findByResetPasswordToken(resetPasswordToken: String): ResetPasswordTokenRedisEntity?
}
