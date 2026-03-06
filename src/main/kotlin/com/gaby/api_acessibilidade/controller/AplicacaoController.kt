package com.gaby.api_acessibilidade.controller

import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.dto.AplicacaoResponse
import com.gaby.api_acessibilidade.service.AplicacaoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/aplicacoes")
class AplicacaoController(
        private val service: AplicacaoService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun criar(@Valid @RequestBody request: AplicacaoRequest): AplicacaoResponse {
        return service.criar(request)
    }

    @GetMapping
    fun listar(): List<AplicacaoResponse> {
        return service.listar()
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): AplicacaoResponse {
        return service.buscarPorId(id)
    }

    @PutMapping("/{id}")
    fun atualizar(
            @PathVariable id: Long,
            @Valid @RequestBody request: AplicacaoRequest
    ): AplicacaoResponse {
        return service.atualizar(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deletar(@PathVariable id: Long) {
        service.deletar(id)
    }
}