package com.hiel.boilerplatekopring.configurations.redis

import com.hiel.boilerplatekopring.utilities.EMPTY_STRING
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@EnableRedisRepositories(
    basePackages = ["com.hiel.boilerplatekopring.redis"],
    enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP,
    keyspaceNotificationsConfigParameter = EMPTY_STRING,
)
@Configuration
class RedisConfiguration(
    @Value("\${spring.data.redis.host}")
    private var host: String,

    @Value("\${spring.data.redis.port}")
    private var port: String,
) {
    @Bean
    fun redisConnectionFactory() = LettuceConnectionFactory(host, port.toInt())

    @Bean
    fun redisTemplate() = StringRedisTemplate(redisConnectionFactory())
}
