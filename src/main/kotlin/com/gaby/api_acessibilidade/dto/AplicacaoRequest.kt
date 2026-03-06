package com.gaby.api_acessibilidade.dto

import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import jakarta.validation.constraints.*

data class AplicacaoRequest(

        @field:NotBlank
        val nome: String,

        @field:NotBlank
        val link: String,

        @field:NotNull
        val tipo: TipoAplicacao,

        @field:Min(1)
        @field:Max(5)
        val nivelAcessibilidade: Int,

        val observacoes: String?
)