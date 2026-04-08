# 📖 Guia de Referência da API: ToDo Pro

Esta documentação fornece os detalhes técnicos necessários para interagir com a API do ToDo Pro.

---

## 🔐 Autenticação e Segurança

A API utiliza **JWT (JSON Web Token)** para autenticação. Todas as rotas protegidas exigem o envio do token no cabeçalho `Authorization`.

**Formato:** `Authorization: Bearer <seu_token>`

### 💎 Estrutura do Token (Claims)
O token gerado no login é **Self-contained** e contém metadados para otimização do frontend:
- `name`: Nome completo formatado para exibição.
- `email`: Identificador único do usuário.
- `exp`: Prazo de expiração (2 horas).

---

## 👤 Endpoints de Usuário (`/api/users`, `/api/auth`)

### Registro de Novo Usuário
`POST /api/users`
- **Payload:**
```json
{
  "name": "Italo Oliveira",
  "email": "italo@email.com",
  "password": "senha_segura_123"
}
```
- **Validações:** E-mail via Regex rigoroso, Senha (mín. 6 chars), Nome (mín. 3 chars).

### Autenticação (Login)
`POST /api/auth/login`
- **Payload:**
```json
{
  "email": "italo@email.com",
  "password": "senha_segura_123"
}
```
- **Resposta (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ..."
}
```

---

## 📂 Endpoints de Categoria (`/api/categories`)

### Listar Categorias do Usuário
`GET /api/categories`
- **Resposta:** Lista de categorias ordenadas alfabeticamente, incluindo o `taskCount` (contagem de tarefas vinculadas).

### Criar/Editar Categoria
`POST /api/categories` | `PUT /api/categories/{id}`
- **Payload:**
```json
{
  "name": "Trabalho",
  "color": "#4169E1"
}
```

---

## ✅ Endpoints de Tarefa (`/api/tasks`)

### Criar Tarefa
`POST /api/tasks`
- **Payload:**
```json
{
  "title": "Finalizar v1.0",
  "description": "Revisar documentação e infra",
  "priority": "HIGH",
  "deadline": "2026-04-10T10:00:00",
  "categoryId": "uuid-da-categoria"
}
```
- **Regra:** O `deadline` não pode ser uma data no passado.

### Transições de Workflow (PATCH)
- **Iniciar:** `/api/tasks/{id}/start` ➔ Muda para `DOING`.
- **Concluir:** `/api/tasks/{id}/done` ➔ Muda para `DONE`.
    - *Nota:* Só é permitido concluir se a tarefa estiver em `DOING`.

---

## ⚠️ Padronização de Erros

Em caso de falha (4xx ou 5xx), a API retorna o objeto `ApiErrorMessage`:

```json
{
  "timestamp": "2026-04-08T10:00:00Z",
  "status": 400,
  "message": "Campos inválidos",
  "errors": {
    "email": "O e-mail informado é inválido"
  },
  "path": "/api/users",
  "method": "POST"
}
```
