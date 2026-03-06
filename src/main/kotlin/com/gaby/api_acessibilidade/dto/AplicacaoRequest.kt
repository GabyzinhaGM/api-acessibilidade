package com.gaby.api_acessibilidade.dto

import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@Schema(description = "Dados para cadastro ou atualização de uma aplicação")
data class AplicacaoRequest(

        @field:NotBlank(message = "O nome da aplicação é obrigatório")
        @field:Size(max = 150, message = "O nome da aplicação deve ter no máximo 150 caracteres")
        @field:Schema(
                description = "Nome da aplicação",
                example = "Portal da Prefeitura"
        )
        val nome: String,

        @field:NotBlank(message = "O link da aplicação é obrigatório")
        @field:Size(max = 300, message = "O link deve ter no máximo 300 caracteres")
        @field:Pattern(
                regexp = "^(https?://).+",
                message = "O link deve começar com http:// ou https://"
        )
        @field:Schema(
                description = "Link de acesso da aplicação",
                example = "https://www.exemplo.com.br"
        )
        val link: String,

        @field:NotNull(message = "O tipo da aplicação é obrigatório")
        @field:Schema(
                description = "Tipo da aplicação",
                example = "WEB"
        )
        val tipo: TipoAplicacao,

        @field:Min(value = 1, message = "O nível de acessibilidade deve ser no mínimo 1")
        @field:Max(value = 5, message = "O nível de acessibilidade deve ser no máximo 5")
        @field:Schema(
                description = "Nível de acessibilidade da aplicação, de 1 a 5",
                example = "4"
        )
        val nivelAcessibilidade: Int,

        @field:Size(max = 1000, message = "As observações devem ter no máximo 1000 caracteres")
        @field:Schema(
                description = "Observações adicionais sobre acessibilidade",
                example = "Boa navegação por teclado e compatibilidade com leitor de tela",
                nullable = true
        )
        val observacoes: String?
)