# 🏗️ Arquitetura do Projeto: Fullstack To-Do Pro

Este documento detalha as decisões técnicas, padrões de projeto e a infraestrutura que sustentam a plataforma To-Do Pro.

---

## 1. Stack Tecnológica

| Componente           | Tecnologia               | Versão          |
| :---                 | :---                     | :---            |
| **Linguagem Backend**| Java                     | 21 (LTS)        |
| **Framework Backend**| Spring Boot              | 3.4.x           |
| **Gerenciador Dep.** | Gradle (Kotlin DSL)      | Atual           |
| **Linguagem Frontend**| Angular                  | 19              |
| **Banco de Dados** | PostgreSQL               | 16              |
| **Segurança** | Spring Security + JWT    | -               |
| **Documentação** | SpringDoc OpenAPI/Swagger| 3               |
| **Containerização** | Docker / Docker Compose  | Latest          |

---

## 2. Modelagem de Dados (MER)

Utilizaremos o PostgreSQL 16 com **UUIDs** para chaves primárias, garantindo maior segurança e facilidade em migrações de dados.

### 2.1 Entidade: User
* `id`: UUID (PK)
* `username`: VARCHAR(50) (Unique, Not Null)
* `email`: VARCHAR(100) (Unique, Not Null)
* `password`: VARCHAR(255) (Hashed)
* `created_at`: TIMESTAMP (Default: now())

### 2.2 Entidade: Task
* `id`: UUID (PK)
* `title`: VARCHAR(100) (Not Null)
* `description`: TEXT
* `status`: ENUM ('PENDING', 'DOING', 'DONE')
* `priority`: ENUM ('LOW', 'MEDIUM', 'HIGH')
* `deadline`: TIMESTAMP
* `user_id`: UUID (FK -> User)
* `category_id`: UUID (FK -> Category, Nullable)

### 2.3 Entidade: Category
* `id`: UUID (PK)
* `name`: VARCHAR(50) (Not Null)
* `color`: VARCHAR(7) (Hex Code)
* `user_id`: UUID (FK -> User)

---

## 3. Estrutura de Pastas (Monorepo)

A organização visa separar as responsabilidades e facilitar o pipeline de CI/CD.

```text
/todo-application
├── .github/workflows/      # Pipelines de CI/CD (GitHub Actions)
├── backend/                # Spring Boot App (Gradle)
│   ├── src/main/java/com/italooliveira/projeto/
│   │   ├── config/         # Security, JWT, Swagger
│   │   ├── controllers/    # Camada de entrada REST
│   │   ├── domain/         # Entidades JPA e Enums
│   │   ├── dto/            # Data Transfer Objects (Request/Response)
│   │   ├── repositories/   # Spring Data JPA
│   │   └── services/       # Regras de Negócio
│   ├── build.gradle.kts
│   └── Dockerfile
├── frontend/               # Angular App
│   ├── src/
│   └── Dockerfile
├── docs/                   # Documentação do Projeto
│   ├── REQUISITOS.md
│   └── ARQUITETURA.md
├── infra/                  # Arquivos de infraestrutura local
│   └── docker-compose.yml  # Orquestração (Postgres + App)
├── .gitignore
└── README.md               # Apresentação do projeto e instruções de uso