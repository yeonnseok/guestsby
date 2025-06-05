package com.guestsby.auth.security

import com.guestsby.auth.domain.CustomUserDetailsService
import com.guestsby.auth.domain.UserPrincipal
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenProvider(
    @Value("\${app.jwt.token.secret-key:sample}")
    val secretKey: String,

    @Value("\${app.jwt.token.expire-length:3000000}")
    val validityInMilliseconds: Long,
) {
    fun createToken(authentication: Authentication?): String =
        Jwts.builder().let {
            val now = Date()
            val userPrincipal = authentication?.principal as UserPrincipal

            it.setClaims(
                Jwts.claims()
                    .setSubject(userPrincipal.getId().toString())
                    .also { claims ->
                        claims["role"] = userPrincipal.authorities.first()
                    }
            )
                .setIssuedAt(now)
                .setExpiration(Date(now.time + validityInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
        }!!

    fun getUserIdFromToken(token: String?): Long =
        Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
            .subject
            .toLong()

    fun validateToken(token: String?): Boolean {
        return try {
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .let { !it.body.expiration.before(Date()) }
        } catch (e: Exception) {
            false
        }
    }
}
