package com.gaby.api_acessibilidade.service

import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoResponse
import com.gaby.api_acessibilidade.entity.Aplicacao
import com.gaby.api_acessibilidade.exception.RecursoNaoEncontradoException
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
                .orElseThrow { RecursoNaoEncontradoException("Aplicação não encontrada") }

        return AplicacaoResponse(
                id = aplicacao.id!!,
                nome = aplicacao.nome,
                link = aplicacao.link,
                tipo = aplicacao.tipo,
                nivelAcessibilidade = aplicacao.nivelAcessibilidade,
                observacoes = aplicacao.observacoes
        )
    }

    fun atualizar(id: Long, request: AplicacaoRequest): AplicacaoResponse {
        val aplicacaoExistente = repository.findById(id)
                .orElseThrow { RecursoNaoEncontradoException("Aplicação não encontrada") }

        val aplicacaoAtualizada = Aplicacao(
                id = aplicacaoExistente.id,
                nome = request.nome,
                link = request.link,
                tipo = request.tipo,
                nivelAcessibilidade = request.nivelAcessibilidade,
                observacoes = request.observacoes
        )

        val salva = repository.save(aplicacaoAtualizada)

        return AplicacaoResponse(
                id = salva.id!!,
                nome = salva.nome,
                link = salva.link,
                tipo = salva.tipo,
                nivelAcessibilidade = salva.nivelAcessibilidade,
                observacoes = salva.observacoes
        )
    }


    fun atualizarParcialmente(id: Long, request: AplicacaoPatchRequest): AplicacaoResponse {
        val aplicacaoExistente = repository.findById(id)
                .orElseThrow { RecursoNaoEncontradoException("Aplicação não encontrada") }

        val aplicacaoAtualizada = Aplicacao(
                id = aplicacaoExistente.id,
                nome = request.nome ?: aplicacaoExistente.nome,
                link = request.link ?: aplicacaoExistente.link,
                tipo = request.tipo ?: aplicacaoExistente.tipo,
                nivelAcessibilidade = request.nivelAcessibilidade ?: aplicacaoExistente.nivelAcessibilidade,
                observacoes = request.observacoes ?: aplicacaoExistente.observacoes
        )

        val salva = repository.save(aplicacaoAtualizada)

        return AplicacaoResponse(
                id = salva.id!!,
                nome = salva.nome,
                link = salva.link,
                tipo = salva.tipo,
                nivelAcessibilidade = salva.nivelAcessibilidade,
                observacoes = salva.observacoes
        )
    }

    fun deletar(id: Long) {
        val aplicacao = repository.findById(id)
                .orElseThrow { RecursoNaoEncontradoException("Aplicação não encontrada") }

        repository.delete(aplicacao)
    }
}