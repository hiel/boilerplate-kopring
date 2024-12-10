package com.hiel.boilerplatekopring.redis.signuptoken

import org.springframework.data.repository.CrudRepository

interface SignupTokenRedisRepository : CrudRepository<SignupTokenRedisEntity, Long> {
    fun findBySignupToken(signupToken: String): SignupTokenRedisEntity?
}
