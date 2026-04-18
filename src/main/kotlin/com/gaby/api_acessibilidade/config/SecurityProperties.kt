package com.gaby.api_acessibilidade.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.security")
data class SecurityProperties(
        val username: String,
        val password: String
)