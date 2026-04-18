package com.gaby.api_acessibilidade.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.gaby.api_acessibilidade.config.SecurityConfig
import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.dto.AplicacaoResponse
import com.gaby.api_acessibilidade.dto.PaginaResponse
import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import com.gaby.api_acessibilidade.exception.RecursoNaoEncontradoException
import com.gaby.api_acessibilidade.security.JwtService
import com.gaby.api_acessibilidade.service.AplicacaoService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AplicacaoController::class)
@Import(SecurityConfig::class)
class AplicacaoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var service: AplicacaoService

    @MockitoBean
    private lateinit var jwtService: JwtService

    @Test
    fun `deve criar aplicacao com sucesso quando autenticado`() {
        val request = AplicacaoRequest(
                nome = "Portal da Prefeitura",
                link = "https://www.prefeitura.gov.br",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 4,
                observacoes = "Boa navegação por teclado"
        )

        val response = AplicacaoResponse(
                id = 1L,
                nome = "Portal da Prefeitura",
                link = "https://www.prefeitura.gov.br",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 4,
                observacoes = "Boa navegação por teclado"
        )

        given(service.criar(request)).willReturn(response)

        mockMvc.perform(
                post("/aplicacoes")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated)
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Portal da Prefeitura"))
                .andExpect(jsonPath("$.tipo").value("WEB"))
                .andExpect(jsonPath("$.nivelAcessibilidade").value(4))
    }

    @Test
    fun `deve retornar proibido ao criar aplicacao sem autenticacao`() {
        val request = AplicacaoRequest(
                nome = "Portal da Prefeitura",
                link = "https://www.prefeitura.gov.br",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 4,
                observacoes = "Boa navegação por teclado"
        )

        mockMvc.perform(
                post("/aplicacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isForbidden)
    }

    @Test
    fun `deve retornar 400 ao criar aplicacao com dados invalidos quando autenticado`() {
        val jsonInvalido = """
            {
              "nome": "",
              "link": "site-sem-http.com",
              "tipo": "WEB",
              "nivelAcessibilidade": 9,
              "observacoes": "teste"
            }
        """.trimIndent()

        mockMvc.perform(
                post("/aplicacoes")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInvalido)
        )
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.caminho").value("/aplicacoes"))
    }

    @Test
    fun `deve buscar aplicacao por id com sucesso sem autenticacao`() {
        val response = AplicacaoResponse(
                id = 1L,
                nome = "Sistema Acessível",
                link = "https://www.sistema.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Compatível com leitor de tela"
        )

        given(service.buscarPorId(1L)).willReturn(response)

        mockMvc.perform(get("/aplicacoes/1"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Sistema Acessível"))
                .andExpect(jsonPath("$.tipo").value("WEB"))
                .andExpect(jsonPath("$.nivelAcessibilidade").value(5))
    }

    @Test
    fun `deve retornar 404 ao buscar aplicacao inexistente`() {
        given(service.buscarPorId(99L))
                .willThrow(RecursoNaoEncontradoException("Aplicação não encontrada"))

        mockMvc.perform(get("/aplicacoes/99"))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Not Found"))
                .andExpect(jsonPath("$.mensagem").value("Aplicação não encontrada"))
                .andExpect(jsonPath("$.caminho").value("/aplicacoes/99"))
    }

    @Test
    fun `deve listar aplicacoes com paginacao sem autenticacao`() {
        val response = PaginaResponse(
                conteudo = listOf(
                        AplicacaoResponse(
                                id = 1L,
                                nome = "Portal A",
                                link = "https://www.portala.com",
                                tipo = TipoAplicacao.WEB,
                                nivelAcessibilidade = 4,
                                observacoes = "Boa"
                        )
                ),
                pagina = 0,
                tamanho = 10,
                totalElementos = 1,
                totalPaginas = 1,
                ultima = true
        )

        given(
                service.listar(
                        tipo = null,
                        nivelAcessibilidade = null,
                        pagina = 0,
                        tamanho = 10,
                        ordenarPor = "id",
                        direcao = "asc"
                )
        ).willReturn(response)

        mockMvc.perform(get("/aplicacoes"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.conteudo[0].id").value(1))
                .andExpect(jsonPath("$.conteudo[0].nome").value("Portal A"))
                .andExpect(jsonPath("$.pagina").value(0))
                .andExpect(jsonPath("$.tamanho").value(10))
                .andExpect(jsonPath("$.totalElementos").value(1))
                .andExpect(jsonPath("$.ultima").value(true))
    }

    @Test
    fun `deve atualizar aplicacao com put quando autenticado`() {
        val request = AplicacaoRequest(
                nome = "Portal Atualizado",
                link = "https://www.portal-atualizado.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Melhorou"
        )

        val response = AplicacaoResponse(
                id = 1L,
                nome = "Portal Atualizado",
                link = "https://www.portal-atualizado.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Melhorou"
        )

        given(service.atualizar(1L, request)).willReturn(response)

        mockMvc.perform(
                put("/aplicacoes/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Portal Atualizado"))
                .andExpect(jsonPath("$.nivelAcessibilidade").value(5))
    }

    @Test
    fun `deve retornar proibido ao atualizar aplicacao com put sem autenticacao`() {
        val request = AplicacaoRequest(
                nome = "Portal Atualizado",
                link = "https://www.portal-atualizado.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Melhorou"
        )

        mockMvc.perform(
                put("/aplicacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isForbidden)
    }

    @Test
    fun `deve atualizar parcialmente aplicacao com patch quando autenticado`() {
        val request = AplicacaoPatchRequest(
                nivelAcessibilidade = 5,
                observacoes = "Agora muito melhor"
        )

        val response = AplicacaoResponse(
                id = 1L,
                nome = "Portal X",
                link = "https://www.portalx.com",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Agora muito melhor"
        )

        given(service.atualizarParcialmente(1L, request)).willReturn(response)

        mockMvc.perform(
                patch("/aplicacoes/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nivelAcessibilidade").value(5))
                .andExpect(jsonPath("$.observacoes").value("Agora muito melhor"))
    }

    @Test
    fun `deve retornar proibido ao atualizar parcialmente sem autenticacao`() {
        val request = AplicacaoPatchRequest(
                nivelAcessibilidade = 5,
                observacoes = "Agora muito melhor"
        )

        mockMvc.perform(
                patch("/aplicacoes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isForbidden)
    }

    @Test
    fun `deve deletar aplicacao com sucesso quando autenticado`() {
        mockMvc.perform(
                delete("/aplicacoes/1")
                        .with(user("admin").roles("ADMIN"))
        )
                .andExpect(status().isNoContent)
    }

    @Test
    fun `deve retornar proibido ao deletar aplicacao sem autenticacao`() {
        mockMvc.perform(delete("/aplicacoes/1"))
                .andExpect(status().isForbidden)
    }

    @Test
    fun `deve retornar 400 ao listar com query param invalido`() {
        mockMvc.perform(get("/aplicacoes?page=-1"))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Bad Request"))
                .andExpect(jsonPath("$.caminho").value("/aplicacoes"))
    }
}