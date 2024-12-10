package com.hiel.boilerplatekopring.redis.signuptoken

import com.hiel.boilerplatekopring.jpa.user.UserEntity
import com.hiel.boilerplatekopring.redis.BaseRedisEntity
import com.hiel.boilerplatekopring.utilities.minuteToSecond
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.TimeToLive
import org.springframework.data.redis.core.index.Indexed
import java.util.UUID

@RedisHash("SignupToken")
class SignupTokenRedisEntity(
    @Id
    var userId: Long,
    @Indexed
    var signupToken: String,
) : BaseRedisEntity {
    @TimeToLive
    override fun getTtlSecond() = 10.minuteToSecond()

    companion object {
        fun build(user: UserEntity) = SignupTokenRedisEntity(
            userId = user.id,
            signupToken = UUID.randomUUID().toString(),
        )
    }
}
