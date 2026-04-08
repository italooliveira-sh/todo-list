# 🚀 ToDo Pro - Task Management Intelligence

[![Java 21](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot 3.4](https://img.shields.io/badge/Spring%20Boot-3.4-brightgreen?style=for-the-badge&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Angular 19](https://img.shields.io/badge/Angular-19-red?style=for-the-badge&logo=angular)](https://angular.io/)
[![Tailwind CSS 4](https://img.shields.io/badge/Tailwind--blue?style=for-the-badge&logo=tailwind-css)](https://tailwindcss.com/)
[![Docker](https://img.shields.io/badge/Docker-Container-blue?style=for-the-badge&logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

O **ToDo Pro** é uma plataforma de gerenciamento de tarefas multiusuário de alta performance, projetada com foco em segurança, escalabilidade e UX fluida. O projeto demonstra a aplicação prática de conceitos avançados de engenharia de software, unindo um backend reativo e testado a um frontend moderno movido a **Signals**.

---

## 💎 Diferenciais do Projeto

- **Arquitetura Reativa com Signals:** Utilização do novo motor de reatividade do Angular 19 para um gerenciamento de estado granular, eliminando problemas de ciclo de vida e otimizando a renderização.
- **Segurança Stateless Avançada:** Autenticação via **JWT (JSON Web Tokens)** com extração dinâmica de metadados (*name*, *email*), permitindo que o frontend opere de forma totalmente desacoplada do estado da sessão do servidor.
- **TDD (Test-Driven Development):** Cobertura rigorosa das camadas de serviço e controladores, garantindo que as regras de negócio sejam invioláveis desde a concepção.
- **Infraestrutura Multi-Cloud:** Configuração pronta para deploy distribuído (Vercel + Render + Supabase), com suporte nativo a Docker e Proxy reverso via Nginx para mitigação de CORS.
- **UI/UX Premium:** Interface construída com **Tailwind CSS 4** e **Angular Material**, apresentando modo colapsável, layouts compartilhados e feedbacks instantâneos.

---

## ✨ Funcionalidades Principais

| Categoria | Funcionalidades |
| :--- | :--- |
| **Autenticação** | Login/Registro, Validação de E-mail via Regex, Iniciais dinâmicas no Avatar. |
| **Gestão de Tarefas** | CRUD Completo, Priorização (Alta/Média/Baixa), Workflow Pendente -> Fazendo -> Concluído. |
| **Categorias** | Gestão de categorias com cores customizadas e sincronização em tempo real via Signals. |
| **Dashboard** | Estatísticas reativas a filtros de busca, categoria e status. |
| **Segurança** | Bloqueio de prazos retroativos, isolamento de dados entre usuários, validação Bean Validation. |

---

## 🛠️ Stack Tecnológica

### Backend (The Brain)
- **Spring Boot 3.4** (Framework Base)
- **Spring Security 6** (Segurança e JWT)
- **Spring Data JPA** & **PostgreSQL** (Persistência)
- **Flyway** (Evolutionary Database Design)
- **JUnit 5 & Mockito** (Quality Assurance)

### Frontend (The Face)
- **Angular 19** (Signals & Standalone Components)
- **Tailwind CSS 4** (Estilização de Próxima Geração)
- **Angular Material** (Enterprise UI Components)
- **RxJS** (Asynchronous Stream Processing)

---

## 🏗️ Arquitetura e Organização

```text
├── backend/                # API RESTful robusta
│   ├── domain/             # Entidades e Regras de Negócio
│   ├── dto/                # Contratos de entrada/saída (Data Transfer Objects)
│   ├── services/           # Lógica centralizada e Transações
│   ├── mappers/            # Desacoplamento entre Entidades e DTOs
│   └── exceptions/         # Tratamento global de erros padronizado
├── frontend/               # Aplicação SPA moderna
│   ├── core/               # Singletons (Services, Interceptors, Guards)
│   ├── features/           # Módulos independentes por funcionalidade
│   └── shared/             # Componentes comuns e Layouts estruturais
├── docs/                   # Documentação de Engenharia
└── infra/                  # Docker Compose & Orquestração
```

---

## 🚀 Guia de Execução Rápida

### Via Docker (Recomendado)
```bash
cd infra
docker-compose up -d --build
```
- Frontend: `http://localhost`
- Backend API: `http://localhost:8080`

### Via Local (Desenvolvimento)
1. **Backend:** `./gradlew bootRun`
2. **Frontend:** `npm install && npm start`

---

## 📄 Documentação Técnica Detalhada

- 📘 **[Guia da API (Endpoints)](./docs/API.md)**
- 🏗️ **[Decisões de Arquitetura](./docs/ARQUITETURA.md)**
- 📝 **[Histórico de Requisitos](./docs/REQUISITOS.md)**

---

## 👤 Autor

**Italo Oliveira** - Desenvolvedor Fullstack
[LinkedIn](https://www.linkedin.com/in/italo-oliveira-xd/) | [GitHub](https://github.com/ItaloOliveira-sh)
