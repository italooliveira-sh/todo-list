# 🏗️ Arquitetura de Software: ToDo Pro

Este documento descreve as decisões arquiteturais, padrões de design e a modelagem de dados que sustentam a plataforma ToDo Pro.

---

## 1. Visão Geral da Arquitetura

O sistema utiliza uma arquitetura **Decoupled (Desacoplada)** com uma separação clara entre o provedor de serviços (Backend API) e o consumidor de interface (Frontend SPA).

- **Backend**: Arquitetura em camadas (Layered Architecture) seguindo princípios de Clean Code.
- **Frontend**: Arquitetura modular orientada a funcionalidades (Feature-based) com gerenciamento de estado reativo.
- **Comunicação**: RESTful API com payloads JSON e segurança baseada em tokens JWT.

---

## 2. Modelo de Dados (MER)

O banco de dados PostgreSQL foi modelado para garantir a integridade referencial e o isolamento total entre usuários.

### 2.1 Entidades Principais

| Entidade | Descrição | Regras de Negócio |
| :--- | :--- | :--- |
| **User** | Credenciais e Perfil | Identificado unicamente pelo e-mail. Senhas protegidas com BCrypt. |
| **Task** | Unidade de trabalho | Obrigatório possuir um dono (User). Status e Prioridade são Enums. |
| **Category** | Agrupador lógico | Criadas dinamicamente pelo usuário. Cores em formato Hexadecimal. |

### 2.2 Relacionamentos
- **User 1:N Task**: Um usuário possui várias tarefas.
- **User 1:N Category**: Um usuário gerencia suas próprias categorias.
- **Category 1:N Task**: Uma categoria pode agrupar diversas tarefas (Relacionamento opcional, deleção da categoria resulta em `SET NULL` na tarefa).

---

## 3. Padrões de Projeto (Backend)

### 3.1 Data Transfer Objects (DTO)
Utilizamos DTOs para todas as entradas e saídas da API. Isso impede o vazamento de detalhes de implementação do banco de dados (Entidades JPA) para a camada de visualização e permite validações customizadas via **Bean Validation**.

### 3.2 Camada de Mapeamento (Mappers)
Componentes dedicados realizam a conversão entre Entidades e DTOs, mantendo a lógica de transformação isolada das Regras de Negócio (Services).

### 3.3 Tratamento Global de Exceções
Implementamos um `GlobalExceptionHandler` utilizando `@ControllerAdvice`. Isso garante que qualquer erro no sistema resulte em uma resposta JSON padronizada (`ApiErrorMessage`), facilitando o consumo pelo frontend.

---

## 4. Reatividade e Estado (Frontend)

### 4.1 Gerenciamento de Estado com Signals
O Angular 19 introduziu os **Signals**, que utilizamos para gerenciar o estado global de categorias.
- **Shared State Service**: O `CategoryService` atua como um Store. Quando uma categoria é alterada, o sinal é atualizado e todos os componentes (Sidebar, Dashboard, Formulários) reagem instantaneamente sem necessidade de recarregamento manual.

### 4.2 Layout Patterns
- **AuthLayout**: Focado em conversão (Login/Register), com design limpo e centralizado.
- **MainLayout**: Estrutura de Dashboard com Sidebar colapsável e Header persistente, otimizado para o fluxo de trabalho do usuário.

---

## 5. Infraestrutura e Deploy

### 5.1 Containerização
O projeto é 100% Dockerizado.
- **Nginx como Proxy Reverso**: O container de frontend utiliza Nginx para servir arquivos estáticos e atuar como proxy para o backend, mitigando problemas de CORS e centralizando a entrada na porta 80.

### 5.2 Estratégia Multi-Cloud
- **Database**: Supabase (PostgreSQL Gerenciado).
- **Backend**: Render (Hospedagem de Containers com Auto-deploy).
- **Frontend**: Vercel (Otimizado para SPAs com suporte a rewrites de API).
