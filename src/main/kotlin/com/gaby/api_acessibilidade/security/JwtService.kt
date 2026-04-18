package com.gaby.api_acessibilidade.security

import com.gaby.api_acessibilidade.config.SecurityProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Date

@Service
class JwtService(
        private val securityProperties: SecurityProperties
) {

    private fun getSigningKey() =
            Keys.hmacShaKeyFor(securityProperties.jwtSecret.toByteArray(StandardCharsets.UTF_8))

    fun gerarToken(userDetails: UserDetails): String {
        val agora = Date()
        val expiracao = Date(agora.time + securityProperties.jwtExpirationMs)

        return Jwts.builder()
                .subject(userDetails.username)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getSigningKey())
                .compact()
    }

    fun extrairUsername(token: String): String {
        return extrairClaims(token).subject
    }

    fun tokenValido(token: String, userDetails: UserDetails): Boolean {
        val username = extrairUsername(token)
        return username == userDetails.username && !tokenExpirado(token)
    }

    private fun tokenExpirado(token: String): Boolean {
        return extrairClaims(token).expiration.before(Date())
    }

    private fun extrairClaims(token: String): Claims {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .payload
    }
}