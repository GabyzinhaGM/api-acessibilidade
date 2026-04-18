package com.gaby.api_acessibilidade.auth

import com.gaby.api_acessibilidade.security.JwtService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirements
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação da API")
class AuthController(
        private val authenticationManager: AuthenticationManager,
        private val jwtService: JwtService
) {

    @Operation(
            summary = "Realizar login",
            description = "Autentica o usuário e retorna um token JWT"
    )
    @SecurityRequirements
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
                ApiResponse(responseCode = "400", description = "Dados inválidos"),
                ApiResponse(responseCode = "401", description = "Credenciais inválidas")
            ]
    )
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