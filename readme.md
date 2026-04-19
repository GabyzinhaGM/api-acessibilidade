# API de Acessibilidade

API REST desenvolvida em **Kotlin + Spring Boot** para cadastro e
consulta de aplicações com diferentes níveis de acessibilidade.

O objetivo do projeto é demonstrar uma arquitetura organizada de API,
com boas práticas de desenvolvimento, documentação, validação de dados,
segurança com JWT e cobertura de testes.

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
-   Autenticar usuário e gerar token JWT
-   Proteger endpoints de escrita com autenticação

------------------------------------------------------------------------

# Exemplo de uso

## Cadastro de aplicação

Requisição:

```json
{
  "nome": "Portal da Prefeitura",
  "link": "https://www.prefeitura.gov.br",
  "tipo": "WEB",
  "nivelAcessibilidade": 4,
  "observacoes": "Boa navegação por teclado"
}
```

Resposta:

```json
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

# Autenticação

A API utiliza **JWT (JSON Web Token)** para proteger os endpoints de
escrita.

## Login

Endpoint:

```http
POST /auth/login
```

Requisição:

```json
{
  "username": "admin",
  "password": "123456"
}
```

Resposta:

```json
{
  "token": "seu-token-jwt"
}
```

## Uso do token

Após autenticar, envie o token no header das requisições protegidas:

```http
Authorization: Bearer seu-token-jwt
```

## Endpoints públicos

Os endpoints abaixo podem ser acessados sem autenticação:

-   `GET /aplicacoes`
-   `GET /aplicacoes/{id}`
-   `POST /auth/login`
-   `/swagger-ui/**`
-   `/v3/api-docs/**`

## Endpoints protegidos

Os endpoints abaixo exigem token JWT válido:

-   `POST /aplicacoes`
-   `PUT /aplicacoes/{id}`
-   `PATCH /aplicacoes/{id}`
-   `DELETE /aplicacoes/{id}`

------------------------------------------------------------------------

# Tecnologias utilizadas

-   Kotlin
-   Spring Boot
-   Spring Web
-   Spring Data JPA
-   Hibernate
-   H2 Database
-   Spring Security
-   JWT
-   Swagger / OpenAPI
-   Gradle

### Testes

-   JUnit 5
-   Mockito
-   MockMvc
-   Spring Security Test
-   JaCoCo (cobertura de testes)

------------------------------------------------------------------------

# Arquitetura do projeto

O projeto segue uma separação clara de responsabilidades.

```text
controller
    endpoints da API

auth
    autenticação e geração de token

security
    filtros e serviços de segurança JWT

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

config
    configurações gerais, OpenAPI e segurança
```

Fluxo de execução:

```text
Controller
   ↓
Service
   ↓
Mapper
   ↓
Repository
   ↓
Database
```

Fluxo de autenticação:

```text
Cliente
   ↓
AuthController
   ↓
AuthenticationManager
   ↓
JwtService
   ↓
Token JWT
```

------------------------------------------------------------------------

# Documentação da API

A documentação interativa da API é gerada automaticamente com
**Swagger**.

Após subir a aplicação, acesse:

http://localhost:8080/swagger-ui/index.html

A interface Swagger também permite informar o token JWT para testar
os endpoints protegidos.

------------------------------------------------------------------------

# Banco de dados

Durante desenvolvimento e testes é utilizado o **H2 Database** em
memória.

Console do H2:

http://localhost:8080/h2-console

Configuração padrão:

JDBC URL: `jdbc:h2:mem:api_acessibilidade`  
User: `sa`  
Password: `(vazio)`

------------------------------------------------------------------------

# Configuração de segurança

As credenciais e propriedades do JWT estão configuradas no
`application.yml`.

Exemplo:

```yaml
app:
  security:
    username: admin
    password: 123456
    jwt-secret: minha-chave-super-secreta-com-no-minimo-32-bytes
    jwt-expiration-ms: 3600000
```

------------------------------------------------------------------------

# Endpoints principais

## Autenticação

### Login

```http
POST /auth/login
```

------------------------------------------------------------------------

## Aplicações

### Criar aplicação

```http
POST /aplicacoes
```

### Listar aplicações

```http
GET /aplicacoes
```

Parâmetros disponíveis:

-   `page`
-   `size`
-   `sort`
-   `direction`
-   `tipo`
-   `nivelAcessibilidade`

Exemplo:

```http
GET /aplicacoes?tipo=WEB&sort=nome&direction=asc&page=0&size=5
```

### Buscar aplicação por ID

```http
GET /aplicacoes/{id}
```

### Atualizar aplicação

```http
PUT /aplicacoes/{id}
```

### Atualização parcial

```http
PATCH /aplicacoes/{id}
```

### Deletar aplicação

```http
DELETE /aplicacoes/{id}
```

------------------------------------------------------------------------

# Tratamento de erros

A API possui tratamento centralizado de exceções.

Exemplo de resposta de erro:

```json
{
  "timestamp": "2026-03-08T12:00:00",
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Aplicação não encontrada",
  "caminho": "/aplicacoes/99"
}
```

Também podem ocorrer respostas relacionadas à autenticação e autorização,
como:

-   `401 Unauthorized`
-   `403 Forbidden`

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

Testam endpoints, validações e regras de acesso usando:

-   MockMvc
-   WebMvcTest
-   Spring Security Test

------------------------------------------------------------------------

### Testes de integração

Testam o fluxo completo da aplicação:

```text
Controller → Service → Repository → H2
```

Incluindo cenários de autenticação e proteção de endpoints.

------------------------------------------------------------------------

# Como executar o projeto

## Pré-requisitos

-   Java 17
-   Gradle

## Executando localmente

```bash
./gradlew bootRun
```

A aplicação ficará disponível em:

http://localhost:8080

------------------------------------------------------------------------

# Cobertura de testes

O projeto gera relatório de cobertura com **JaCoCo**.

Para rodar os testes com relatório:

```bash
./gradlew clean test jacocoTestReport
```

Relatório HTML gerado em:

```text
build/reports/jacoco/test/html/index.html
```

------------------------------------------------------------------------

# Objetivo do projeto

Este projeto foi desenvolvido como parte de um portfólio backend com foco
em:

-   construção de APIs REST bem estruturadas
-   boas práticas com Kotlin e Spring Boot
-   validação e tratamento de erros
-   documentação com OpenAPI
-   autenticação e autorização com JWT
-   testes automatizados
-   organização arquitetural para evolução futura

------------------------------------------------------------------------

# Próximas evoluções

Evoluções planejadas para versões futuras:

-   persistência de usuários no banco de dados
-   substituição do usuário hardcoded por autenticação baseada em entidade
-   externalização de secrets e credenciais
-   migração para banco relacional persistente
-   versionamento e deploy da aplicação
