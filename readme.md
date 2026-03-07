# API de Acessibilidade

API REST desenvolvida em **Kotlin + Spring Boot** para cadastro e
consulta de aplicações com diferentes níveis de acessibilidade.

O objetivo do projeto é demonstrar uma arquitetura organizada de API,
com boas práticas de desenvolvimento, documentação, validação de dados e
cobertura de testes.

------------------------------------------------------------------------

# Funcionalidades

A API permite:

-   Cadastrar aplicações
-   Listar aplicações cadastradas
-   Buscar aplicação por ID
-   Atualizar uma aplicação
-   Atualizar parcialmente uma aplicação
-   Deletar uma aplicação
-   Filtrar aplicações por tipo ou nível de acessibilidade
-   Ordenar resultados
-   Paginar resultados

------------------------------------------------------------------------

# Exemplo de uso

Cadastro de aplicação:

``` json
{
  "nome": "Portal da Prefeitura",
  "link": "https://www.prefeitura.gov.br",
  "tipo": "WEB",
  "nivelAcessibilidade": 4,
  "observacoes": "Boa navegação por teclado"
}
```

Resposta:

``` json
{
  "id": 1,
  "nome": "Portal da Prefeitura",
  "link": "https://www.prefeitura.gov.br",
  "tipo": "WEB",
  "nivelAcessibilidade": 4,
  "observacoes": "Boa navegação por teclado"
}
```

------------------------------------------------------------------------

# Tecnologias utilizadas

-   Kotlin
-   Spring Boot
-   Spring Web
-   Spring Data JPA
-   Hibernate
-   H2 Database
-   Swagger / OpenAPI
-   Gradle

### Testes

-   JUnit 5
-   Mockito
-   MockMvc
-   JaCoCo (cobertura de testes)

------------------------------------------------------------------------

# Arquitetura do projeto

O projeto segue uma separação clara de responsabilidades.

    controller
        endpoints da API

    service
        regras de negócio

    repository
        acesso a dados

    entity
        modelo persistido no banco

    dto
        objetos de requisição e resposta

    mapper
        conversão entre DTO e entidade

    exception
        tratamento centralizado de erros

Fluxo de execução:

    Controller
       ↓
    Service
       ↓
    Mapper
       ↓
    Repository
       ↓
    Database

------------------------------------------------------------------------

# Documentação da API

A documentação interativa da API é gerada automaticamente com
**Swagger**.

Após subir a aplicação, acesse:

http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------------

# Banco de dados

Durante desenvolvimento e testes é utilizado o **H2 Database** em
memória.

Console do H2:

http://localhost:8080/h2-console

Configuração padrão:

JDBC URL: jdbc:h2:mem:api_acessibilidade\
User: sa\
Password: (vazio)

------------------------------------------------------------------------

# Endpoints principais

### Criar aplicação

POST /aplicacoes

### Listar aplicações

GET /aplicacoes

Parâmetros disponíveis:

page\
size\
sort\
direction\
tipo\
nivelAcessibilidade

Exemplo:

GET /aplicacoes?tipo=WEB&sort=nome&direction=asc&page=0&size=5

------------------------------------------------------------------------

### Buscar aplicação por ID

GET /aplicacoes/{id}

------------------------------------------------------------------------

### Atualizar aplicação

PUT /aplicacoes/{id}

------------------------------------------------------------------------

### Atualização parcial

PATCH /aplicacoes/{id}

------------------------------------------------------------------------

### Deletar aplicação

DELETE /aplicacoes/{id}

------------------------------------------------------------------------

# Tratamento de erros

A API possui tratamento centralizado de exceções.

Exemplo de resposta de erro:

``` json
{
  "timestamp": "2026-03-08T12:00:00",
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Aplicação não encontrada",
  "caminho": "/aplicacoes/99"
}
```

------------------------------------------------------------------------

# Testes

O projeto possui três níveis de testes.

### Testes unitários

-   Service
-   Mapper

Ferramentas:

-   JUnit
-   Mockito

------------------------------------------------------------------------

### Testes de controller

Testam endpoints e validações usando:

-   MockMvc
-   WebMvcTest

------------------------------------------------------------------------

### Testes de integração

Testam o fluxo completo da aplicação:

Controller → Service → Repository → H2

Utilizando:

-   SpringBootTest
-   MockMvc
-   Banco H2 em memória

------------------------------------------------------------------------

# Cobertura de testes

Cobertura gerada com **JaCoCo**.

Para gerar o relatório:

./gradlew test jacocoTestReport

O relatório HTML será gerado em:

build/reports/jacoco/test/html/index.html

------------------------------------------------------------------------

# Como executar o projeto

Após clonar  o repositório:

Entre na pasta do projeto:

cd api-acessibilidade

Execute a aplicação:

gradlew bootRun (Windows)

./gradlew bootRun (Linux / Mac)

A API estará disponível em:

http://localhost:8080

------------------------------------------------------------------------

# Executar testes

Para rodar os testes:

gradlew test (Windows)

./gradlew test (Linux / Mac)

------------------------------------------------------------------------

# Melhorias futuras

Possíveis evoluções do projeto:

-   Autenticação com Spring Security
-   Integração com banco PostgreSQL
-   Deploy em container Docker
-   Pipeline CI/CD
-   Cache com Redis

------------------------------------------------------------------------

# Autor

Projeto desenvolvido por Gabriela Godoy Marques, como estudo de arquitetura de APIs REST utilizando
**Kotlin + Spring Boot**.
