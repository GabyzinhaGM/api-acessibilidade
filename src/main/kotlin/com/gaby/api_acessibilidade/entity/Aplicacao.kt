package com.gaby.api_acessibilidade.entity

import jakarta.persistence.*
import com.gaby.api_acessibilidade.entity.enum.TipoAplicacao

@Entity
@Table(name = "aplicacoes")
data class Aplicacao(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(nullable = false)
        val nome: String,

        @Column(nullable = false)
        val link: String,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        val tipo: TipoAplicacao,

        @Column(nullable = false)
        val nivelAcessibilidade: Int,

        @Column(length = 1000)
        val observacoes: String?
)