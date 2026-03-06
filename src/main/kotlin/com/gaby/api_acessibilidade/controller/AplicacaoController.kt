package com.gaby.api_acessibilidade.controller

import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.dto.AplicacaoResponse
import com.gaby.api_acessibilidade.service.AplicacaoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/aplicacoes")
@Tag(name = "Aplicações", description = "Endpoints para gerenciamento de aplicações acessíveis")
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

    @Operation(summary = "Listar aplicações", description = "Lista todas as aplicações cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    fun listar(): List<AplicacaoResponse> {
        return service.listar()
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