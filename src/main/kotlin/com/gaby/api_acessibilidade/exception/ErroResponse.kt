package com.gaby.api_acessibilidade.exception

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "Estrutura padrão de resposta de erro")
data class ErroResponse(

        @field:Schema(description = "Data e hora do erro")
        val timestamp: LocalDateTime,

        @field:Schema(description = "Código HTTP do erro", example = "404")
        val status: Int,

        @field:Schema(description = "Descrição resumida do erro", example = "Not Found")
        val erro: String,

        @field:Schema(description = "Mensagem detalhada do erro", example = "Aplicação não encontrada")
        val mensagem: String,

        @field:Schema(description = "Caminho da requisição que gerou o erro", example = "/aplicacoes/999")
        val caminho: String
)