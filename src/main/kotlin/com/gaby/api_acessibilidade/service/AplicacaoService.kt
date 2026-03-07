package com.gaby.api_acessibilidade.service

import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.dto.AplicacaoResponse
import com.gaby.api_acessibilidade.dto.PaginaResponse
import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import com.gaby.api_acessibilidade.exception.ParametroInvalidoException
import com.gaby.api_acessibilidade.exception.RecursoNaoEncontradoException
import com.gaby.api_acessibilidade.mapper.AplicacaoMapper
import com.gaby.api_acessibilidade.repository.AplicacaoRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class AplicacaoService(
        private val repository: AplicacaoRepository,
        private val mapper: AplicacaoMapper
) {

    private val camposOrdenacaoPermitidos = setOf("id", "nome", "link", "tipo", "nivelAcessibilidade")

    fun criar(request: AplicacaoRequest): AplicacaoResponse {
        val aplicacao = mapper.paraEntidade(request)
        val salva = repository.save(aplicacao)
        return mapper.paraResponse(salva)
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

        return PaginaResponse(
                conteudo = resultado.content.map(mapper::paraResponse),
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

        return mapper.paraResponse(aplicacao)
    }

    fun atualizar(id: Long, request: AplicacaoRequest): AplicacaoResponse {
        repository.findById(id)
                .orElseThrow { RecursoNaoEncontradoException("Aplicação não encontrada") }

        val aplicacaoAtualizada = mapper.paraEntidadeAtualizada(id, request)
        val salva = repository.save(aplicacaoAtualizada)

        return mapper.paraResponse(salva)
    }

    fun atualizarParcialmente(id: Long, request: AplicacaoPatchRequest): AplicacaoResponse {
        val aplicacaoExistente = repository.findById(id)
                .orElseThrow { RecursoNaoEncontradoException("Aplicação não encontrada") }

        val aplicacaoAtualizada = mapper.aplicarPatch(aplicacaoExistente, request)
        val salva = repository.save(aplicacaoAtualizada)

        return mapper.paraResponse(salva)
    }

    fun deletar(id: Long) {
        val aplicacao = repository.findById(id)
                .orElseThrow { RecursoNaoEncontradoException("Aplicação não encontrada") }

        repository.delete(aplicacao)
    }
}