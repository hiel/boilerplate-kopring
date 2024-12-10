package com.hiel.boilerplatekopring.jpa.user

import com.hiel.boilerplatekopring.domains.user.UserStatus
import com.hiel.boilerplatekopring.domains.user.UserType
import com.hiel.boilerplatekopring.jpa.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.envers.Audited

@Audited
@Entity
@Table(name = "users", schema = "boilerplate_kopring")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "email", updatable = false, nullable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    var encryptPassword: String,

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "user_type", updatable = false, nullable = false)
    @Enumerated(value = EnumType.STRING)
    val userType: UserType,

    @Column(name = "user_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    var userStatus: UserStatus,
) : BaseEntity() {
    companion object {
        const val PASSWORD_MINIMUM_LENGTH = 8
        const val USERNAME_MINIMUM_LENGTH = 2
    }
}
