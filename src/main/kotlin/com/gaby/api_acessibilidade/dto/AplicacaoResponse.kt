package com.gaby.api_acessibilidade.dto

import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao

data class AplicacaoResponse(

        val id: Long,
        val nome: String,
        val link: String,
        val tipo: TipoAplicacao,
        val nivelAcessibilidade: Int,
        val observacoes: String?
)