package com.hiel.boilerplatekopring.configurations.jpa

import com.hiel.boilerplatekopring.utilities.getNowKst
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.auditing.DateTimeProvider
import java.util.Optional

@Configuration
class JpaAuditConfiguration {
    @Bean
    fun offsetDateTimeProvider() = DateTimeProvider { Optional.of(getNowKst()) }
}
