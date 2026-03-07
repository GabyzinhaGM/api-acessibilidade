package com.gaby.api_acessibilidade.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.gaby.api_acessibilidade.dto.AplicacaoPatchRequest
import com.gaby.api_acessibilidade.dto.AplicacaoRequest
import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class AplicacaoIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        private var idAplicacaoCriada: Long = 0
    }

    @Test
    @Order(1)
    fun `deve criar aplicacao com sucesso`() {
        val request = AplicacaoRequest(
                nome = "Portal da Prefeitura",
                link = "https://www.prefeitura.gov.br",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 4,
                observacoes = "Boa navegação por teclado"
        )

        val response = mockMvc.perform(
                post("/aplicacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Portal da Prefeitura"))
                .andExpect(jsonPath("$.tipo").value("WEB"))
                .andReturn()

        val json = response.response.contentAsString
        val responseMap = objectMapper.readValue(json, Map::class.java)

        idAplicacaoCriada = (responseMap["id"] as Number).toLong()

        assertNotNull(idAplicacaoCriada)
    }

    @Test
    @Order(2)
    fun `deve buscar aplicacao por id com sucesso`() {
        mockMvc.perform(get("/aplicacoes/$idAplicacaoCriada"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(idAplicacaoCriada))
                .andExpect(jsonPath("$.nome").value("Portal da Prefeitura"))
                .andExpect(jsonPath("$.link").value("https://www.prefeitura.gov.br"))
                .andExpect(jsonPath("$.tipo").value("WEB"))
                .andExpect(jsonPath("$.nivelAcessibilidade").value(4))
    }

    @Test
    @Order(3)
    fun `deve listar aplicacoes com sucesso`() {
        mockMvc.perform(get("/aplicacoes"))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.conteudo").isArray)
                .andExpect(jsonPath("$.totalElementos").value(1))
                .andExpect(jsonPath("$.conteudo[0].id").value(idAplicacaoCriada))
    }

    @Test
    @Order(4)
    fun `deve atualizar aplicacao com put`() {
        val request = AplicacaoRequest(
                nome = "Portal da Prefeitura Atualizado",
                link = "https://www.prefeitura.gov.br/novo",
                tipo = TipoAplicacao.WEB,
                nivelAcessibilidade = 5,
                observacoes = "Acessibilidade melhorada"
        )

        mockMvc.perform(
                put("/aplicacoes/$idAplicacaoCriada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(idAplicacaoCriada))
                .andExpect(jsonPath("$.nome").value("Portal da Prefeitura Atualizado"))
                .andExpect(jsonPath("$.nivelAcessibilidade").value(5))
                .andExpect(jsonPath("$.observacoes").value("Acessibilidade melhorada"))
    }

    @Test
    @Order(5)
    fun `deve atualizar aplicacao com patch`() {
        val request = AplicacaoPatchRequest(
                nivelAcessibilidade = 3,
                observacoes = "Patch aplicado com sucesso"
        )

        mockMvc.perform(
                patch("/aplicacoes/$idAplicacaoCriada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(idAplicacaoCriada))
                .andExpect(jsonPath("$.nivelAcessibilidade").value(3))
                .andExpect(jsonPath("$.observacoes").value("Patch aplicado com sucesso"))
    }

    @Test
    @Order(6)
    fun `deve deletar aplicacao com sucesso`() {
        mockMvc.perform(delete("/aplicacoes/$idAplicacaoCriada"))
                .andExpect(status().isNoContent)
    }

    @Test
    @Order(7)
    fun `deve retornar 404 ao buscar aplicacao deletada`() {
        mockMvc.perform(get("/aplicacoes/$idAplicacaoCriada"))
                .andExpect(status().isNotFound)
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.mensagem").value("Aplicação não encontrada"))
    }
}