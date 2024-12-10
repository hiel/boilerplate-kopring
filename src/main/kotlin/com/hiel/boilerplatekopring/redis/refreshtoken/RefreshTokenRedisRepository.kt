package com.hiel.boilerplatekopring.redis.refreshtoken

import org.springframework.data.repository.CrudRepository

interface RefreshTokenRedisRepository : CrudRepository<RefreshTokenRedisEntity, Long> {
    fun findFirstByUserId(userId: Long): RefreshTokenRedisEntity?

    fun deleteByUserId(userId: Long)
}
