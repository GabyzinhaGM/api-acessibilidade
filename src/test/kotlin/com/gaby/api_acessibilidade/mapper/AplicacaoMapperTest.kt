package com.gaby.api_acessibilidade.mapper

import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.entity.Aplicacao
import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AplicacaoMapperTest {

    private lateinit var mapper: AplicacaoMapper

    @BeforeEach
    fun setUp() {
        mapper = AplicacaoMapper()
    }

    @Test
    fun `deve converter AplicacaoRequest para entidade`() {
        val request = AplicacaoRequest(
                nome = "Portal da Prefeitura",
                link = "https://www.prefeitura.gov.br",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 4,
                observacoes = "Boa navegação por teclado"
        )

        val entidade = mapper.paraEntidade(request)

        assertNull(entidade.id)
        assertEquals("Portal da Prefeitura", entidade.nome)
        assertEquals("https://www.prefeitura.gov.br", entidade.link)
        assertEquals(TipoAplicacao.WEB, entidade.tipo)
        assertEquals(4, entidade.nivelAcessibilidade)
        assertEquals("Boa navegação por teclado", entidade.observacoes)
    }

    @Test
    fun `deve converter entidade para AplicacaoResponse`() {
        val aplicacao = Aplicacao(
                id = 1L,
                nome = "Sistema Acessível",
                link = "https://www.sistema.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Compatível com leitor de tela"
        )

        val response = mapper.paraResponse(aplicacao)

        assertEquals(1L, response.id)
        assertEquals("Sistema Acessível", response.nome)
        assertEquals("https://www.sistema.com", response.link)
        assertEquals(TipoAplicacao.WEB, response.tipo)
        assertEquals(5, response.nivelAcessibilidade)
        assertEquals("Compatível com leitor de tela", response.observacoes)
    }

    @Test
    fun `deve converter request e id para entidade atualizada`() {
        val request = AplicacaoRequest(
                nome = "Portal Atualizado",
                link = "https://www.portal-atualizado.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Melhorou bastante"
        )

        val entidade = mapper.paraEntidadeAtualizada(10L, request)

        assertEquals(10L, entidade.id)
        assertEquals("Portal Atualizado", entidade.nome)
        assertEquals("https://www.portal-atualizado.com", entidade.link)
        assertEquals(TipoAplicacao.WEB, entidade.tipo)
        assertEquals(5, entidade.nivelAcessibilidade)
        assertEquals("Melhorou bastante", entidade.observacoes)
    }

    @Test
    fun `deve aplicar patch mantendo campos nao informados`() {
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

        val aplicacaoAtualizada = mapper.aplicarPatch(aplicacaoExistente, patchRequest)

        assertEquals(1L, aplicacaoAtualizada.id)
        assertEquals("Sistema X", aplicacaoAtualizada.nome)
        assertEquals("https://www.sistemax.com", aplicacaoAtualizada.link)
        assertEquals(TipoAplicacao.WEB, aplicacaoAtualizada.tipo)
        assertEquals(4, aplicacaoAtualizada.nivelAcessibilidade)
        assertEquals("Melhorou suporte ao teclado", aplicacaoAtualizada.observacoes)
    }

    @Test
    fun `deve aplicar patch alterando todos os campos informados`() {
        val aplicacaoExistente = Aplicacao(
                id = 2L,
                nome = "Sistema Antigo",
                link = "https://www.antigo.com",
                tipo = TipoAplicacao.DESKTOP,
                nivelAcessibilidade = 1,
                observacoes = "Versão inicial"
        )

        val patchRequest = AplicacaoPatchRequest(
                nome = "Sistema Novo",
                link = "https://www.novo.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Totalmente reformulado"
        )

        val aplicacaoAtualizada = mapper.aplicarPatch(aplicacaoExistente, patchRequest)

        assertEquals(2L, aplicacaoAtualizada.id)
        assertEquals("Sistema Novo", aplicacaoAtualizada.nome)
        assertEquals("https://www.novo.com", aplicacaoAtualizada.link)
        assertEquals(TipoAplicacao.WEB, aplicacaoAtualizada.tipo)
        assertEquals(5, aplicacaoAtualizada.nivelAcessibilidade)
        assertEquals("Totalmente reformulado", aplicacaoAtualizada.observacoes)
    }
}