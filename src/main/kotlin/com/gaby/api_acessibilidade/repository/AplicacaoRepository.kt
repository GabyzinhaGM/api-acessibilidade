package com.gaby.api_acessibilidade.repository

import com.gaby.api_acessibilidade.entity.Aplicacao
import org.springframework.data.jpa.repository.JpaRepository

interface AplicacaoRepository : JpaRepository<Aplicacao, Long>