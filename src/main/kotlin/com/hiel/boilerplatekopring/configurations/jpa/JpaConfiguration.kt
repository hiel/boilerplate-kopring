package com.hiel.boilerplatekopring.configurations.jpa

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(basePackages = ["com.hiel.boilerplatekopring"])
@EnableJpaRepositories(
    basePackages = ["com.hiel.boilerplatekopring"],
    repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean::class,
)
@EnableJpaAuditing(
    auditorAwareRef = "auditorAwareImpl",
    dateTimeProviderRef = "offsetDateTimeProvider",
)
@Configuration
class JpaConfiguration