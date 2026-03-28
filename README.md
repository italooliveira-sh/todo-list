# 🚀 To-Do Pro - Fullstack Task Management

[![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.x-brightgreen?style=flat-square&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=flat-square&logo=postgresql)](https://www.postgresql.org/)
[![JWT](https://img.shields.io/badge/JWT-Authentication-black?style=flat-square&logo=json-web-tokens)](https://jwt.io/)

O **To-Do Pro** é uma plataforma robusta de gerenciamento de tarefas multiusuário, desenvolvida com foco em segurança, escalabilidade e boas práticas de engenharia de software. O projeto serve como um showcase técnico de uma arquitetura limpa em camadas utilizando **Spring Boot 3** e **Java 21**.

---

## 🎯 Diferenciais Técnicos

- **Segurança Avançada:** Implementação de autenticação STATELESS via **JWT**, utilizando os padrões oficiais do **Spring Security 6**.
- **TDD (Test-Driven Development):** Desenvolvimento guiado por testes, garantindo mais de 90% de cobertura nas camadas de Service e Controller.
- **Categorização Inteligente:** Sistema flexível de categorias por usuário com contador de tarefas integrado.
- **Arquitetura Limpa:** Separação clara de responsabilidades entre Domínio, DTOs, Mappers, Services e Controllers.
- **Padrões de Resposta:** Tratamento global de exceções com mensagens padronizadas e códigos de status HTTP semânticos.

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21 (LTS)** & **Spring Boot 3.4**
- **Spring Data JPA** & **Hibernate**
- **PostgreSQL** (Banco de dados relacional)
- **Flyway** (Gerenciamento de migrações de DB)
- **Spring Security** & **jjwt** (Segurança e Tokens)
- **JUnit 5** & **Mockito** (Testes Automatizados)
- **Lombok** (Produtividade)

### Frontend (Em Breve)
- **Angular 19**
- **Material UI**

---

## 🏗️ Estrutura do Projeto

```text
├── backend/                # API REST Spring Boot
│   ├── src/main/java       # Código fonte organizado por camadas
│   ├── src/test/java       # Suite de testes unitários e de integração
│   └── build.gradle        # Gerenciamento de dependências
├── docs/                   # Documentação técnica detalhada
│   ├── REQUISITOS.md       # Regras de negócio e funcionalidades
│   ├── ARQUITETURA.md      # Decisões de design e MER
│   └── API.md              # Documentação detalhada dos Endpoints
├── infra/                  # Arquivos de infraestrutura (Docker Compose)
└── README.md               # Visão geral do projeto
```

---

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Docker & Docker Compose

### Executando o Banco de Dados (Postgres)
```bash
cd infra
docker-compose up -d
```

### Executando o Backend
```bash
cd backend
./gradlew bootRun
```

A API estará disponível em `http://localhost:8080`. Você pode conferir a documentação interativa em `http://localhost:8080/swagger-ui.html`.

---

## 📄 Documentação da API

Para detalhes sobre cada endpoint, modelos de requisição e resposta, consulte o nosso guia:
👉 **[Guia da API (docs/API.md)](./docs/API.md)**

---

## 👤 Autor

**Italo Oliveira** - [LinkedIn](https://www.linkedin.com/in/italo-oliveira-dev/) | [GitHub](https://github.com/ItaloOliveira-dev)
