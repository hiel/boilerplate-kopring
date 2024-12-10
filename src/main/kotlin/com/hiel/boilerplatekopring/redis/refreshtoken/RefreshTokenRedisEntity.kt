package com.hiel.boilerplatekopring.redis.refreshtoken

import com.hiel.boilerplatekopring.domains.auth.AuthToken
import com.hiel.boilerplatekopring.redis.BaseRedisEntity
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive

@RedisHash("RefreshToken")
class RefreshTokenRedisEntity(
    @Id
    var userId: Long,
    var refreshToken: String,
) : BaseRedisEntity {
    @TimeToLive
    override fun getTtlSecond() = AuthToken.REFRESH_TOKEN_EXPIRE_DURATION.seconds
}
