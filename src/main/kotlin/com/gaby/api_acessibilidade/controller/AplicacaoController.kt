package com.gaby.api_acessibilidade.controller

import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.dto.AplicacaoResponse
import com.gaby.api_acessibilidade.dto.PaginaResponse
import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import com.gaby.api_acessibilidade.service.AplicacaoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Parameter
import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.validation.annotation.Validated

@RestController
@RequestMapping("/aplicacoes")
@Tag(name = "Aplicações", description = "Endpoints para gerenciamento de aplicações acessíveis")
@Validated
class AplicacaoController(
        private val service: AplicacaoService
) {

    @Operation(summary = "Cadastrar aplicação", description = "Cadastra uma nova aplicação com informações de acessibilidade")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "201", description = "Aplicação cadastrada com sucesso"),
                ApiResponse(responseCode = "400", description = "Dados inválidos")
            ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criar(@Valid @RequestBody request: AplicacaoRequest): AplicacaoResponse {
        return service.criar(request)
    }

    @Operation(
            summary = "Listar aplicações",
            description = "Lista aplicações cadastradas com suporte a paginação, filtros por tipo e nível de acessibilidade, e ordenação"
    )
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    fun listar(
            @Parameter(description = "Tipo da aplicação para filtro", example = "WEB")
            @RequestParam(required = false) tipo: TipoAplicacao?,

            @Parameter(description = "Nível de acessibilidade para filtro", example = "4")
            @RequestParam(required = false)
            @Min(value = 1, message = "O nível de acessibilidade deve ser no mínimo 1")
            @Max(value = 5, message = "O nível de acessibilidade deve ser no máximo 5")
            nivelAcessibilidade: Int?,

            @Parameter(description = "Número da página", example = "0")
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "A página deve ser maior ou igual a 0")
            page: Int,

            @Parameter(description = "Quantidade de elementos por página", example = "10")
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "O tamanho da página deve ser no mínimo 1")
            @Max(value = 50, message = "O tamanho da página deve ser no máximo 50")
            size: Int,

            @Parameter(description = "Campo para ordenação", example = "nome")
            @RequestParam(defaultValue = "id")
            sort: String,

            @Parameter(description = "Direção da ordenação: asc ou desc", example = "asc")
            @RequestParam(defaultValue = "asc")
            @Pattern(
                    regexp = "^(asc|desc)$",
                    flags = [Pattern.Flag.CASE_INSENSITIVE],
                    message = "A direção deve ser 'asc' ou 'desc'"
            )
            direction: String
    ): PaginaResponse<AplicacaoResponse> {
        return service.listar(
                tipo = tipo,
                nivelAcessibilidade = nivelAcessibilidade,
                pagina = page,
                tamanho = size,
                ordenarPor = sort,
                direcao = direction
        )
    }

    @Operation(summary = "Buscar aplicação por ID", description = "Busca uma aplicação cadastrada pelo identificador")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "Aplicação encontrada"),
                ApiResponse(responseCode = "404", description = "Aplicação não encontrada")
            ]
    )
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): AplicacaoResponse {
        return service.buscarPorId(id)
    }

    @Operation(summary = "Atualizar aplicação", description = "Atualiza os dados de uma aplicação cadastrada")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "Aplicação atualizada com sucesso"),
                ApiResponse(responseCode = "400", description = "Dados inválidos"),
                ApiResponse(responseCode = "404", description = "Aplicação não encontrada")
            ]
    )
    @PutMapping("/{id}")
    fun atualizar(
            @PathVariable id: Long,
            @Valid @RequestBody request: AplicacaoRequest
    ): AplicacaoResponse {
        return service.atualizar(id, request)
    }

    @Operation(summary = "Atualizar parcialmente uma aplicação", description = "Atualiza apenas os campos informados de uma aplicação cadastrada")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "200", description = "Aplicação atualizada parcialmente com sucesso"),
                ApiResponse(responseCode = "400", description = "Dados inválidos"),
                ApiResponse(responseCode = "404", description = "Aplicação não encontrada")
            ]
    )
    @PatchMapping("/{id}")
    fun atualizarParcialmente(
            @PathVariable id: Long,
            @Valid @RequestBody request: AplicacaoPatchRequest
    ): AplicacaoResponse {
        return service.atualizarParcialmente(id, request)
    }

    @Operation(summary = "Remover aplicação", description = "Remove uma aplicação cadastrada")
    @ApiResponses(
            value = [
                ApiResponse(responseCode = "204", description = "Aplicação removida com sucesso"),
                ApiResponse(responseCode = "404", description = "Aplicação não encontrada")
            ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletar(@PathVariable id: Long) {
        service.deletar(id)
    }
}