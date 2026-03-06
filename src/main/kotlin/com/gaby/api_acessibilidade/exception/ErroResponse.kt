package com.gaby.api_acessibilidade.exception

import java.time.LocalDateTime

data class ErroResponse(
        val timestamp: LocalDateTime,
        val status: Int,
        val erro: String,
        val mensagem: String,
        val caminho: String
)