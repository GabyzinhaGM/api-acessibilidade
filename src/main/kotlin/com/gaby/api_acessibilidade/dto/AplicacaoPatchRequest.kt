package com.gaby.api_acessibilidade.dto

import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(description = "Dados para atualização parcial de uma aplicação")
data class AplicacaoPatchRequest(

        @field:Size(max = 150, message = "O nome da aplicação deve ter no máximo 150 caracteres")
        @field:Schema(
                description = "Nome da aplicação",
                example = "Portal da Prefeitura"
        )
        val nome: String? = null,

        @field:Size(max = 300, message = "O link deve ter no máximo 300 caracteres")
        @field:Pattern(
                regexp = "^(https?://).+",
                message = "O link deve começar com http:// ou https://"
        )
        @field:Schema(
                description = "Link de acesso da aplicação",
                example = "https://www.exemplo.com.br"
        )
        val link: String? = null,

        @field:Schema(
                description = "Tipo da aplicação",
                example = "WEB"
        )
        val tipo: TipoAplicacao? = null,

        @field:Min(value = 1, message = "O nível de acessibilidade deve ser no mínimo 1")
        @field:Max(value = 5, message = "O nível de acessibilidade deve ser no máximo 5")
        @field:Schema(
                description = "Nível de acessibilidade da aplicação, de 1 a 5",
                example = "4"
        )
        val nivelAcessibilidade: Int? = null,

        @field:Size(max = 1000, message = "As observações devem ter no máximo 1000 caracteres")
        @field:Schema(
                description = "Observações adicionais sobre acessibilidade",
                example = "Boa navegação por teclado e compatibilidade com leitor de tela"
        )
        val observacoes: String? = null
)