package com.gaby.api_acessibilidade.mapper

import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.dto.AplicacaoResponse
import com.gaby.api_acessibilidade.entity.Aplicacao
import org.springframework.stereotype.Component

@Component
class AplicacaoMapper {

    fun paraEntidade(request: AplicacaoRequest): Aplicacao {
        return Aplicacao(
                nome = request.nome,
                link = request.link,
                tipo = request.tipo,
                nivelAcessibilidade = request.nivelAcessibilidade,
                observacoes = request.observacoes
        )
    }

    fun paraResponse(aplicacao: Aplicacao): AplicacaoResponse {
        return AplicacaoResponse(
                id = aplicacao.id!!,
                nome = aplicacao.nome,
                link = aplicacao.link,
                tipo = aplicacao.tipo,
                nivelAcessibilidade = aplicacao.nivelAcessibilidade,
                observacoes = aplicacao.observacoes
        )
    }

    fun paraEntidadeAtualizada(id: Long, request: AplicacaoRequest): Aplicacao {
        return Aplicacao(
                id = id,
                nome = request.nome,
                link = request.link,
                tipo = request.tipo,
                nivelAcessibilidade = request.nivelAcessibilidade,
                observacoes = request.observacoes
        )
    }

    fun aplicarPatch(aplicacaoExistente: Aplicacao, request: AplicacaoPatchRequest): Aplicacao {
        return Aplicacao(
                id = aplicacaoExistente.id,
                nome = request.nome ?: aplicacaoExistente.nome,
                link = request.link ?: aplicacaoExistente.link,
                tipo = request.tipo ?: aplicacaoExistente.tipo,
                nivelAcessibilidade = request.nivelAcessibilidade ?: aplicacaoExistente.nivelAcessibilidade,
                observacoes = request.observacoes ?: aplicacaoExistente.observacoes
        )
    }
}