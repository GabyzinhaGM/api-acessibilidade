package com.gaby.api_acessibilidade.auth

import com.gaby.api_acessibilidade.security.JwtService
import jakarta.validation.Valid
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
        private val authenticationManager: AuthenticationManager,
        private val jwtService: JwtService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): LoginResponse {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        request.username,
                        request.password
                )
        )

        val userDetails = authentication.principal as UserDetails
        val token = jwtService.gerarToken(userDetails)

        return LoginResponse(token = token)
    }
}