package com.gaby.api_acessibilidade.auth

import jakarta.validation.constraints.NotBlank

data class LoginRequest(

        @field:NotBlank(message = "O username é obrigatório")
        val username: String,

        @field:NotBlank(message = "A password é obrigatória")
        val password: String
)