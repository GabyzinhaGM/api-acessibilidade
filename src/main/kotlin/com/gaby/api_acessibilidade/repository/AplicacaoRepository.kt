package com.gaby.api_acessibilidade.repository

import com.gaby.api_acessibilidade.entity.Aplicacao
import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface AplicacaoRepository : JpaRepository<Aplicacao, Long> {

    fun findByTipo(tipo: TipoAplicacao, pageable: Pageable): Page<Aplicacao>

    fun findByNivelAcessibilidade(nivelAcessibilidade: Int, pageable: Pageable): Page<Aplicacao>

    fun findByTipoAndNivelAcessibilidade(
            tipo: TipoAplicacao,
            nivelAcessibilidade: Int,
            pageable: Pageable
    ): Page<Aplicacao>
}