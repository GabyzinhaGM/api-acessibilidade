package com.gaby.api_acessibilidade.service

import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.entity.Aplicacao
import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import com.gaby.api_acessibilidade.exception.ParametroInvalidoException
import com.gaby.api_acessibilidade.exception.RecursoNaoEncontradoException
import com.gaby.api_acessibilidade.mapper.AplicacaoMapper
import com.gaby.api_acessibilidade.repository.AplicacaoRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class AplicacaoServiceTest {

    @Mock
    private lateinit var repository: AplicacaoRepository

    private lateinit var mapper: AplicacaoMapper
    private lateinit var service: AplicacaoService

    @BeforeEach
    fun setUp() {
        mapper = AplicacaoMapper()
        service = AplicacaoService(repository, mapper)
    }

    @Test
    fun `deve criar aplicacao com sucesso`() {
        val request = AplicacaoRequest(
                nome = "Portal da Prefeitura",
                link = "https://www.prefeitura.gov.br",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 4,
                observacoes = "Boa navegação por teclado"
        )

        val aplicacaoSalva = Aplicacao(
                id = 1L,
                nome = request.nome,
                link = request.link,
                tipo = request.tipo,
                nivelAcessibilidade = request.nivelAcessibilidade,
                observacoes = request.observacoes
        )

        `when`(repository.save(any(Aplicacao::class.java))).thenReturn(aplicacaoSalva)

        val response = service.criar(request)

        assertNotNull(response)
        assertEquals(1L, response.id)
        assertEquals("Portal da Prefeitura", response.nome)
        assertEquals("https://www.prefeitura.gov.br", response.link)
        assertEquals(TipoAplicacao.WEB, response.tipo)
        assertEquals(4, response.nivelAcessibilidade)
        assertEquals("Boa navegação por teclado", response.observacoes)

        verify(repository, times(1)).save(any(Aplicacao::class.java))
    }

    @Test
    fun `deve buscar aplicacao por id com sucesso`() {
        val aplicacao = Aplicacao(
                id = 1L,
                nome = "App Acessível",
                link = "https://www.app.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Compatível com leitor de tela"
        )

        `when`(repository.findById(1L)).thenReturn(Optional.of(aplicacao))

        val response = service.buscarPorId(1L)

        assertEquals(1L, response.id)
        assertEquals("App Acessível", response.nome)
        assertEquals(TipoAplicacao.WEB, response.tipo)

        verify(repository, times(1)).findById(1L)
    }

    @Test
    fun `deve lançar excecao ao buscar aplicacao inexistente`() {
        `when`(repository.findById(99L)).thenReturn(Optional.empty())

        val exception = assertThrows(RecursoNaoEncontradoException::class.java) {
            service.buscarPorId(99L)
        }

        assertEquals("Aplicação não encontrada", exception.message)
        verify(repository, times(1)).findById(99L)
    }

    @Test
    fun `deve atualizar aplicacao com sucesso`() {
        val request = AplicacaoRequest(
                nome = "Portal Atualizado",
                link = "https://www.portal-atualizado.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Melhorou bastante"
        )

        val aplicacaoExistente = Aplicacao(
                id = 1L,
                nome = "Portal Antigo",
                link = "https://www.portal-antigo.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 3,
                observacoes = "Versão antiga"
        )

        val aplicacaoAtualizada = Aplicacao(
                id = 1L,
                nome = request.nome,
                link = request.link,
                tipo = request.tipo,
                nivelAcessibilidade = request.nivelAcessibilidade,
                observacoes = request.observacoes
        )

        `when`(repository.findById(1L)).thenReturn(Optional.of(aplicacaoExistente))
        `when`(repository.save(any(Aplicacao::class.java))).thenReturn(aplicacaoAtualizada)

        val response = service.atualizar(1L, request)

        assertEquals(1L, response.id)
        assertEquals("Portal Atualizado", response.nome)
        assertEquals(5, response.nivelAcessibilidade)

        verify(repository, times(1)).findById(1L)
        verify(repository, times(1)).save(any(Aplicacao::class.java))
    }

    @Test
    fun `deve atualizar parcialmente uma aplicacao com sucesso`() {
        val aplicacaoExistente = Aplicacao(
                id = 1L,
                nome = "Sistema X",
                link = "https://www.sistemax.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 2,
                observacoes = "Precisa melhorar"
        )

        val patchRequest = AplicacaoPatchRequest(
                nivelAcessibilidade = 4,
                observacoes = "Melhorou suporte ao teclado"
        )

        val aplicacaoAtualizada = Aplicacao(
                id = 1L,
                nome = "Sistema X",
                link = "https://www.sistemax.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 4,
                observacoes = "Melhorou suporte ao teclado"
        )

        `when`(repository.findById(1L)).thenReturn(Optional.of(aplicacaoExistente))
        `when`(repository.save(any(Aplicacao::class.java))).thenReturn(aplicacaoAtualizada)

        val response = service.atualizarParcialmente(1L, patchRequest)

        assertEquals(1L, response.id)
        assertEquals("Sistema X", response.nome)
        assertEquals(4, response.nivelAcessibilidade)
        assertEquals("Melhorou suporte ao teclado", response.observacoes)

        verify(repository, times(1)).findById(1L)
        verify(repository, times(1)).save(any(Aplicacao::class.java))
    }

    @Test
    fun `deve deletar aplicacao com sucesso`() {
        val aplicacao = Aplicacao(
                id = 1L,
                nome = "Sistema Y",
                link = "https://www.sistemay.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 4,
                observacoes = null
        )

        `when`(repository.findById(1L)).thenReturn(Optional.of(aplicacao))
        doNothing().`when`(repository).delete(aplicacao)

        service.deletar(1L)

        verify(repository, times(1)).findById(1L)
        verify(repository, times(1)).delete(aplicacao)
    }

    @Test
    fun `deve listar aplicacoes paginadas com sucesso`() {
        val aplicacao = Aplicacao(
                id = 1L,
                nome = "Portal A",
                link = "https://www.portala.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Muito boa"
        )

        val sortDirection = "asc"
        val ordenarPor = "id"

        val pageRequest = PageRequest.of(0, 10,
                Sort.by(Sort.Direction.ASC, ordenarPor)
        )

        val page = PageImpl(listOf(aplicacao), pageRequest, 1)

        `when`(repository.findAll(pageRequest)).thenReturn(page)

        val response = service.listar(
                tipo = null,
                nivelAcessibilidade = null,
                pagina = 0,
                tamanho = 10,
                ordenarPor = ordenarPor,
                direcao = sortDirection
        )

        assertEquals(1, response.conteudo.size)
        assertEquals(1L, response.totalElementos)
        assertEquals(1, response.totalPaginas)
        assertEquals(true, response.ultima)
        assertEquals("Portal A", response.conteudo[0].nome)

        verify(repository, times(1)).findAll(pageRequest)
    }

    @Test
    fun `deve lançar excecao quando campo de ordenacao for invalido`() {
        val exception = assertThrows(ParametroInvalidoException::class.java) {
            service.listar(
                    tipo = null,
                    nivelAcessibilidade = null,
                    pagina = 0,
                    tamanho = 10,
                    ordenarPor = "banana",
                    direcao = "asc"
            )
        }

        assertEquals(
                "Campo de ordenação inválido. Valores permitidos: id, nome, link, tipo, nivelAcessibilidade",
                exception.message
        )

        verify(repository, never()).findAll(any(org.springframework.data.domain.Pageable::class.java))
    }
}