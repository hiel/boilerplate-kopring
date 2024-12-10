package com.hiel.boilerplatekopring.filters

import com.hiel.boilerplatekopring.configurations.security.JwtAuthenticationEntryPoint
import com.hiel.boilerplatekopring.configurations.security.SecurityConfiguration
import com.hiel.boilerplatekopring.domains.ResultCode
import com.hiel.boilerplatekopring.domains.auth.TokenType
import com.hiel.boilerplatekopring.exceptions.ServiceException
import com.hiel.boilerplatekopring.utilities.BLANK_CHAR
import com.hiel.boilerplatekopring.utilities.JwtTokenUtility
import com.hiel.boilerplatekopring.utilities.LogUtility
import com.hiel.boilerplatekopring.utilities.substringOrNull
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenUtility: JwtTokenUtility,
) : OncePerRequestFilter() {
    companion object : LogUtility {
        private const val HEADER_PREFIX = "Bearer$BLANK_CHAR"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val isOptionalAuth = isOptionalAuth(request)
            val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
            if (isOptionalAuth) {
                filterChain.doFilter(request, response)
                return
            }
            if (authorizationHeader == null || !authorizationHeader.startsWith(HEADER_PREFIX)) {
                throw ServiceException(ResultCode.Auth.INVALID_TOKEN)
            }
            val token = authorizationHeader.substringOrNull(HEADER_PREFIX.length)
                ?: throw ServiceException(ResultCode.Auth.INVALID_TOKEN)
            val user = jwtTokenUtility.parseToken(token = token, tokenType = TokenType.ACCESS_TOKEN)
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(user, token, user.authorities)
        } catch (e: ServiceException) {
            log.error(e.toString())
            request.setAttribute(JwtAuthenticationEntryPoint.EXCEPTION_ATTRIBUTE_NAME, e)
        } catch (e: Exception) {
            log.error(e.toString())
            request.setAttribute(
                JwtAuthenticationEntryPoint.EXCEPTION_ATTRIBUTE_NAME,
                ServiceException(resultCode = ResultCode.Auth.AUTHENTICATION_FAIL),
            )
        }

        filterChain.doFilter(request, response)
    }

    private fun isOptionalAuth(request: HttpServletRequest): Boolean {
        val antPathMatcher = AntPathMatcher()
        return SecurityConfiguration.PERMIT_URLS.any {
            it.optional &&
                (it.method == null || HttpMethod.valueOf(request.method) == it.method) &&
                antPathMatcher.match(it.url, request.requestURI)
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val antPathMatcher = AntPathMatcher()
        return SecurityConfiguration.PERMIT_URLS.any {
            !it.optional &&
                (it.method == null || HttpMethod.valueOf(request.method) == it.method) &&
                antPathMatcher.match(it.url, request.requestURI)
        }
    }
}
