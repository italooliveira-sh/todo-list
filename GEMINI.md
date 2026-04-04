# 📑 MEMÓRIA DO PROJETO: ToDo Pro

Este documento serve como a "fonte da verdade" para o estado técnico e as decisões de arquitetura do projeto.

## 🏗️ ARQUITETURA GERAL

- **Backend:** Java 21 + Spring Boot 3.4.10.
- **Frontend:** Angular 19+ + Tailwind CSS + Angular Material.
- **Banco de Dados:** PostgreSQL (gerenciado via Flyway migrations).
- **Segurança:** Autenticação baseada em JWT (Stateless).

---

## 🚀 ESTADO DAS FUNCIONALIDADES

### 1. Autenticação e Usuários
- **Backend:** 
  - ✅ Registro de usuário (`UserService.registerUser`) com criação automática de categorias padrão.
  - ✅ Login via `AuthService.login` gerando JWT.
  - ✅ Proteção de rotas via `SecurityFilter`.
- **Frontend:**
  - ✅ Tela de Login (Refatorada com layout compartilhado).
  - ✅ Tela de Registro (Layout independente com validação de senha).
  - ✅ `TokenService` para armazenamento local do JWT.
  - ✅ `AuthService`: Método `login` e `register` prontos.
  - ✅ `TaskService`: CRUD completo de tarefas pronto.

### 2. Gestão de Tarefas e Categorias (Backend Pronto)
- ✅ CRUD completo de Tarefas (com prioridades e status).
- ✅ CRUD completo de Categorias.
- ✅ Filtragem e busca implementadas.
- ⚠️ **Frontend:** Telas de Dashboard e CRUD iniciadas.

---

## 🎨 PADRÕES E CONVENÇÕES ESTABELECIDOS

1. **Idioma:**
   - Código, símbolos e nomes de arquivos: **Inglês**.
   - Mensagens de erro, feedback e logs de commit: **Português**.
2. **Frontend UI:**
   - Uso de `AuthLayoutComponent` para centralizar a estética de login/registro.
   - Uso de `MainLayout` para a aplicação autenticada (Header, Sidebar, Footer).
   - Estilo: Tailwind CSS para layout e Material para componentes.
3. **Erros e Exceções:**
   - Backend: Centralizado em `GlobalExceptionHandler`.
   - Frontend: `auth.interceptor.ts` pronto para lidar com tokens.

---

## 📌 PRÓXIMOS PASSOS (TASK LIST)
1. [x] Implementar `AuthService.register` no frontend para conectar com `/api/users`.
2. [x] Adicionar feedback visual (SnackBar) para erros de login/register.
3. [x] Implementar listagem de tarefas no Dashboard (UI dinâmica).
4. [x] Implementar criação e edição de tarefas (Modal/Formulário).
5. [ ] Implementar filtros de busca e status no Dashboard.
6. [ ] Implementar gestão de Categorias (CRUD de categorias).
