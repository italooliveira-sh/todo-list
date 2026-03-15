# Arquitetura do Projeto To-Do List

## Arquitetura
- **Backend:** Java 21 + Spring Boot 3.3.10 (API REST com JPA/Hibernate, PostgreSQL 16).
  - **Detalhes Técnicos:**
    - Java 21 com Spring Boot 3.3.10, Gradle como build tool.
    - Dependências principais: Spring Web (para REST), Spring Data JPA (para persistência), PostgreSQL Driver.
    - Estrutura de pacotes: `com.example.todolist` com subpacotes `controller`, `service`, `repository`, `model`, `config`, `dto`.
    - Entidade principal: `Task` (campos: id (Long), title (String), description (String), completed (Boolean), createdAt (LocalDateTime), updatedAt (LocalDateTime)).
    - Endpoints REST: 
      - GET /api/tasks (listar todas, com paginação opcional).
      - POST /api/tasks (criar nova tarefa).
      - PUT /api/tasks/{id} (atualizar tarefa).
      - DELETE /api/tasks/{id} (excluir tarefa).
      - PATCH /api/tasks/{id}/complete (marcar como concluída).
    - Validação: Bean Validation (javax.validation) nos DTOs.
    - Tratamento de erros: @ControllerAdvice para respostas padronizadas.
- **Frontend:** React (TypeScript, Vite).
  - React 18+ com TypeScript, Vite para build.
  - Estrutura: Componentes funcionais (TaskList, TaskForm), Hooks (useState, useEffect), Axios para API.
  - UI: Material-UI ou Tailwind CSS.
- **DevOps:** Docker, Docker Compose, GitHub Actions desde o início.
  - Docker: Dockerfile para backend (baseado em openjdk:21), para frontend (nginx para servir build).
  - Docker Compose: Serviços para backend, postgres, frontend-react.
  - CI/CD: GitHub Actions para build/test backend, build frontend, deploy (ex: Railway ou Heroku).
- **Estrutura:** Mono-repo com pastas backend/, frontend-react/, docs/.
- **Banco de Dados:** PostgreSQL 16 em todos os ambientes (desenvolvimento e produção).
- **Testes:** JUnit/Mockito no backend, Jest no frontend.
- **Versionamento:** Git com branches feature/*, main para produção.

## Funcionalidades MVP
- CRUD de tarefas (criar, listar, editar, excluir).
- Marcar como concluída/pendente.
- Filtros básicos.

## Roadmap Definido
1. Arquitetura completa.
2. Backend MVP.
3. Frontend React MVP.
4. DevOps (Docker, CI/CD).
5. Evoluções (autenticação, etc.).