package com.gaby.api_acessibilidade.service

import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoResponse
import com.gaby.api_acessibilidade.dto.PaginaResponse
import com.gaby.api_acessibilidade.entity.Aplicacao
import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import com.gaby.api_acessibilidade.exception.RecursoNaoEncontradoException
import com.gaby.api_acessibilidade.exception.ParametroInvalidoException
import com.gaby.api_acessibilidade.repository.AplicacaoRepository
import org.springframework.stereotype.Service
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

@Service
class AplicacaoService(
        private val repository: AplicacaoRepository
) {
    private val camposOrdenacaoPermitidos = setOf("id", "nome", "link", "tipo", "nivelAcessibilidade")

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

    fun listar(
            tipo: TipoAplicacao?,
            nivelAcessibilidade: Int?,
            pagina: Int,
            tamanho: Int,
            ordenarPor: String,
            direcao: String
    ): PaginaResponse<AplicacaoResponse> {

        if (ordenarPor !in camposOrdenacaoPermitidos) {
            throw ParametroInvalidoException(
                    "Campo de ordenação inválido. Valores permitidos: ${camposOrdenacaoPermitidos.joinToString(", ")}"
            )
        }

        val sortDirection = if (direcao.equals("desc", ignoreCase = true)) {
            Sort.Direction.DESC
        } else {
            Sort.Direction.ASC
        }

        val pageable = PageRequest.of(
                pagina,
                tamanho,
                Sort.by(sortDirection, ordenarPor)
        )

        val resultado = when {
            tipo != null && nivelAcessibilidade != null ->
                repository.findByTipoAndNivelAcessibilidade(tipo, nivelAcessibilidade, pageable)

            tipo != null ->
                repository.findByTipo(tipo, pageable)

            nivelAcessibilidade != null ->
                repository.findByNivelAcessibilidade(nivelAcessibilidade, pageable)

            else ->
                repository.findAll(pageable)
        }

        val conteudo = resultado.content.map {
            AplicacaoResponse(
                    id = it.id!!,
                    nome = it.nome,
                    link = it.link,
                    tipo = it.tipo,
                    nivelAcessibilidade = it.nivelAcessibilidade,
                    observacoes = it.observacoes
            )
        }

        return PaginaResponse(
                conteudo = conteudo,
                pagina = resultado.number,
                tamanho = resultado.size,
                totalElementos = resultado.totalElements,
                totalPaginas = resultado.totalPages,
                ultima = resultado.isLast
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