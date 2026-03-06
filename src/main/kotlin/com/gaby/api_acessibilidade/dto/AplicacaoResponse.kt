package com.gaby.api_acessibilidade.dto

import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Dados retornados da aplicação cadastrada")
data class AplicacaoResponse(

        @field:Schema(description = "Identificador da aplicação", example = "1")
        val id: Long,

        @field:Schema(description = "Nome da aplicação", example = "Portal da Prefeitura")
        val nome: String,

        @field:Schema(description = "Link de acesso da aplicação", example = "https://www.exemplo.com.br")
        val link: String,

        @field:Schema(description = "Tipo da aplicação", example = "WEB")
        val tipo: TipoAplicacao,

        @field:Schema(description = "Nível de acessibilidade da aplicação, de 1 a 5", example = "4")
        val nivelAcessibilidade: Int,

        @field:Schema(
                description = "Observações adicionais sobre acessibilidade",
                example = "Boa compatibilidade com leitores de tela",
                nullable = true
        )
        val observacoes: String?
)