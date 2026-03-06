package com.gaby.api_acessibilidade.service

import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.dto.AplicacaoResponse
import com.gaby.api_acessibilidade.entity.Aplicacao
import com.gaby.api_acessibilidade.repository.AplicacaoRepository
import org.springframework.stereotype.Service

@Service
class AplicacaoService(
        private val repository: AplicacaoRepository
) {

    fun criar(request: AplicacaoRequest): AplicacaoResponse {

        val aplicacao = Aplicacao(
                nome = request.nome,
                link = request.link,
                tipo = request.tipo,
                nivelAcessibilidade = request.nivelAcessibilidade,
                observacoes = request.observacoes
        )

        val salva = repository.save(aplicacao)

        return AplicacaoResponse(
                id = salva.id!!,
                nome = salva.nome,
                link = salva.link,
                tipo = salva.tipo,
                nivelAcessibilidade = salva.nivelAcessibilidade,
                observacoes = salva.observacoes
        )
    }

    fun listar(): List<AplicacaoResponse> =
            repository.findAll().map {
                AplicacaoResponse(
                        id = it.id!!,
                        nome = it.nome,
                        link = it.link,
                        tipo = it.tipo,
                        nivelAcessibilidade = it.nivelAcessibilidade,
                        observacoes = it.observacoes
                )
            }

    fun buscarPorId(id: Long): AplicacaoResponse {

        val aplicacao = repository.findById(id)
                .orElseThrow { RuntimeException("Aplicação não encontrada") }

        return AplicacaoResponse(
                id = aplicacao.id!!,
                nome = aplicacao.nome,
                link = aplicacao.link,
                tipo = aplicacao.tipo,
                nivelAcessibilidade = aplicacao.nivelAcessibilidade,
                observacoes = aplicacao.observacoes
        )
    }

    fun deletar(id: Long) {
        repository.deleteById(id)
    }
}