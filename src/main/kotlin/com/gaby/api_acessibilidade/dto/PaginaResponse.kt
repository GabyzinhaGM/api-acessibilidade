package com.gaby.api_acessibilidade.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Resposta paginada")
data class PaginaResponse<T>(

        @field:Schema(description = "Lista de itens da página atual")
        val conteudo: List<T>,

        @field:Schema(description = "Número da página atual", example = "0")
        val pagina: Int,

        @field:Schema(description = "Quantidade de elementos por página", example = "10")
        val tamanho: Int,

        @field:Schema(description = "Quantidade total de elementos", example = "25")
        val totalElementos: Long,

        @field:Schema(description = "Quantidade total de páginas", example = "3")
        val totalPaginas: Int,

        @field:Schema(description = "Indica se é a última página", example = "false")
        val ultima: Boolean
)