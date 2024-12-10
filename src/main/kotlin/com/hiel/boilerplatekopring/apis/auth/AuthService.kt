package com.hiel.boilerplatekopring.apis.auth

import com.hiel.boilerplatekopring.domains.ResultCode
import com.hiel.boilerplatekopring.domains.auth.TokenType
import com.hiel.boilerplatekopring.domains.user.UserStatus
import com.hiel.boilerplatekopring.domains.user.UserType
import com.hiel.boilerplatekopring.exceptions.ServiceException
import com.hiel.boilerplatekopring.jpa.user.UserEntity
import com.hiel.boilerplatekopring.jpa.user.UserRepository
import com.hiel.boilerplatekopring.redis.refreshtoken.RefreshTokenRedisEntity
import com.hiel.boilerplatekopring.redis.refreshtoken.RefreshTokenRedisRepository
import com.hiel.boilerplatekopring.redis.resetpasswordtoken.ResetPasswordTokenRedisEntity
import com.hiel.boilerplatekopring.redis.resetpasswordtoken.ResetPasswordTokenRedisRepository
import com.hiel.boilerplatekopring.redis.signuptoken.SignupTokenRedisEntity
import com.hiel.boilerplatekopring.redis.signuptoken.SignupTokenRedisRepository
import com.hiel.boilerplatekopring.utilities.JwtTokenUtility
import com.hiel.boilerplatekopring.utilities.getRandomString
import com.hiel.boilerplatekopring.utilities.mail.MailUtility
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val signupTokenRedisRepository: SignupTokenRedisRepository,
    private val refreshTokenRedisRepository: RefreshTokenRedisRepository,
    private val resetPasswordTokenRedisRepository: ResetPasswordTokenRedisRepository,
    private val mailUtility: MailUtility,
    private val jwtTokenUtility: JwtTokenUtility,
    private val passwordEncoder: PasswordEncoder,

    @Value("\${web-client-url}")
    private val webClientUrl: String,
) {
    fun signup(
        email: String,
        password: String,
        name: String,
        userType: UserType,
    ) {
        userRepository.findFirstByEmail(email)?.let {
            throw ServiceException(ResultCode.Auth.DUPLICATED_EMAIL)
        }

        val user = userRepository.save(
            UserEntity(
                email = email,
                encryptPassword = password,
                name = name,
                userType = userType,
                userStatus = UserStatus.NOT_CERTIFICATED,
            ),
        )

        val signupTokenRedis = signupTokenRedisRepository.save(SignupTokenRedisEntity.build(user))
//        mailUtility.sendMail(
//            to = email,
//            template = MailTemplate.SignupCertificate(MailTemplate.SignupCertificate.Params(
//                webClientUrl = webClientUrl,
//                token = signupTokenRedis.signupToken,
//            )),
//        )
    }

    @Transactional
    fun certificateSignup(signupToken: String) {
        val signupTokenRedis = signupTokenRedisRepository.findBySignupToken(signupToken = signupToken)
            ?: throw ServiceException(ResultCode.Auth.EXPIRED_TOKEN)

        userRepository.findByIdOrNull(signupTokenRedis.userId).let {
            it ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
            it.userStatus = UserStatus.AVAILABLE
        }

        signupTokenRedisRepository.delete(signupTokenRedis)
    }

    fun login(
        email: String,
        password: String,
    ): IssueTokenResponse {
        val user = userRepository.findFirstByEmailAndUserStatus(email = email, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.INVALID_EMAIL_OR_PASSWORD)
        if (!passwordEncoder.matches(password, user.encryptPassword)) {
            throw ServiceException(ResultCode.Auth.INVALID_EMAIL_OR_PASSWORD)
        }

        refreshTokenRedisRepository.deleteByUserId(user.id)

        val authToken = jwtTokenUtility.generateAuthToken(user)
        refreshTokenRedisRepository.save(RefreshTokenRedisEntity(userId = user.id, refreshToken = authToken.refreshToken))
        return IssueTokenResponse.build(authToken = authToken, userEntity = user)
    }

    fun refreshToken(refreshToken: String): IssueTokenResponse {
        try {
            val userDetails = jwtTokenUtility.parseToken(token = refreshToken, tokenType = TokenType.REFRESH_TOKEN)
            refreshTokenRedisRepository.findFirstByUserId(userDetails.id)
                ?: throw ServiceException(ResultCode.Auth.INVALID_TOKEN)

            refreshTokenRedisRepository.deleteByUserId(userDetails.id)
            val userEntity = userRepository.findFirstByEmailAndUserStatus(email = userDetails.email, userStatus = UserStatus.AVAILABLE)
                ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)

            val authToken = jwtTokenUtility.generateAuthToken(userEntity)
            refreshTokenRedisRepository.save(RefreshTokenRedisEntity(userId = userEntity.id, refreshToken = authToken.refreshToken))
            return IssueTokenResponse.build(authToken = authToken, userEntity = userEntity)
        } catch (e: ServiceException) {
            if (e.resultCode == ResultCode.Auth.EXPIRED_ACCESS_TOKEN) {
                throw ServiceException(ResultCode.Auth.EXPIRED_REFRESH_TOKEN)
            }
            throw e
        }
    }

    fun requestPasswordReset(email: String) {
        val user = userRepository.findFirstByEmailAndUserStatus(email = email, userStatus = UserStatus.AVAILABLE)
            ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)

        val resetPasswordToken = UUID.randomUUID().toString()
        resetPasswordTokenRedisRepository.save(ResetPasswordTokenRedisEntity(userId = user.id, resetPasswordToken = resetPasswordToken))
//        mailUtility.sendMail(
//            to = email,
//            template = MailTemplate.PasswordReset(MailTemplate.PasswordReset.Params(
//                webClientUrl = webClientUrl,
//                token = resetPasswordToken,
//            ))
//        )
    }

    @Transactional
    fun resetPassword(resetPasswordToken: String): String {
        val resetPasswordTokenRedis = resetPasswordTokenRedisRepository.findByResetPasswordToken(resetPasswordToken)
            ?: throw ServiceException(ResultCode.Auth.EXPIRED_TOKEN)

        val password = getRandomString(UserEntity.PASSWORD_MINIMUM_LENGTH)
        userRepository.findByIdOrNull(resetPasswordTokenRedis.userId).let {
            it ?: throw ServiceException(ResultCode.Auth.NOT_EXIST_USER)
            it.encryptPassword = passwordEncoder.encode(password)
        }
        resetPasswordTokenRedisRepository.delete(resetPasswordTokenRedis)

        return password
    }
}
