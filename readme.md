# API de Acessibilidade

API REST desenvolvida em **Kotlin + Spring Boot** para cadastro e consulta de aplicações com diferentes níveis de acessibilidade.

O objetivo do projeto é demonstrar uma arquitetura organizada de API, com boas práticas de desenvolvimento, documentação, validação de dados e cobertura de testes.

---

# Funcionalidades

A API permite:

- Cadastrar aplicações
- Listar aplicações cadastradas
- Buscar aplicação por ID
- Atualizar uma aplicação
- Atualizar parcialmente uma aplicação
- Deletar uma aplicação
- Filtrar aplicações por tipo ou nível de acessibilidade
- Ordenar resultados
- Paginar resultados

---

# Exemplo de uso

Cadastro de aplicação:

```json
{
  "nome": "Portal da Prefeitura",
  "link": "https://www.prefeitura.gov.br",
  "tipo": "WEB",
  "nivelAcessibilidade": 4,
  "observacoes": "Boa navegação por teclado"
}