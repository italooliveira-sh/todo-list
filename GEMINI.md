# 📑 MEMÓRIA DO PROJETO: ToDo Pro

Este documento serve como a "fonte da verdade" para o estado técnico e as decisões de arquitetura do projeto.

## 🏗️ ARQUITETURA GERAL

- **Backend:** Java 21 + Spring Boot 3.4.10.
- **Frontend:** Angular 19 + Tailwind CSS 4 + Angular Material.
- **Gerenciamento de Estado:** Signals (Angular 19 native).
- **Banco de Dados:** PostgreSQL (Gerenciado via Supabase).
- **Infraestrutura:** Docker, Docker Compose, Nginx Proxy.
- **Segurança:** Autenticação baseada em JWT (Stateless) com Claims customizadas.

---

## 🚀 ESTADO DAS FUNCIONALIDADES (v1.0.0)

### 1. Autenticação e Perfil
- ✅ Registro de usuário com validação Regex rigorosa.
- ✅ Login via JWT com extração dinâmica de metadados no frontend.
- ✅ Avatar dinâmico com lógica de iniciais (Composto vs Único).
- ✅ Normalização de nomes para Title Case.

### 2. Gestão de Tarefas e Categorias
- ✅ CRUD completo de Tarefas com Workflow controlado.
- ✅ CRUD completo de Categorias com cores e sincronização via Signals.
- ✅ Filtros dinâmicos e Busca Textual.
- ✅ Stats Cards reativos no Dashboard.

### 3. Infraestrutura e Qualidade
- ✅ Cobertura de Testes Unitários e Integração (TDD).
- ✅ Containerização completa via Docker.
- ✅ Suporte a Deploy Multi-Cloud (Vercel + Render + Supabase).
- ✅ Documentação técnica detalhada (API, Arquitetura, Requisitos).

---

## 🎨 PADRÕES E CONVENÇÕES ESTABELECIDOS

1. **Idioma:**
   - Código e nomes de arquivos: **Inglês**.
   - Mensagens de erro e documentação: **Português**.
2. **Frontend UI:**
   - Uso de Signals para Single Source of Truth.
   - Layout Pattern para isolamento de contextos (Auth vs Main).
3. **Segurança:**
   - Bloqueio de prazos retroativos.
   - Proteção de transições de status (Pendente -> Fazendo -> Concluído).

---

## 🏆 PROJETO CONCLUÍDO (v1.0.0)
Todas as metas iniciais e melhorias incrementais foram entregues com sucesso.
