# 📖 Guia da API - To-Do Pro

Esta documentação detalha os endpoints disponíveis na API, os formatos de requisição e resposta, e os mecanismos de autenticação.

---

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação. Todas as requisições (exceto login e cadastro) exigem o cabeçalho `Authorization`.

**Formato:** `Authorization: Bearer <seu_token>`

---

## 👤 Usuários & Autenticação (`/api/auth`, `/api/users`)

### Cadastro de Usuário
- **Endpoint:** `POST /api/users`
- **Acesso:** Público
- **Request:**
```json
{
  "name": "italo",
  "email": "italo@email.com",
  "password": "senha123"
}
```
- **Response:** `201 Created`

### Login
- **Endpoint:** `POST /api/auth/login`
- **Acesso:** Público
- **Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 📂 Categorias (`/api/categories`)

### Criar Categoria
- **Endpoint:** `POST /api/categories`
- **Acesso:** Autenticado
- **Request:**
```json
{
  "name": "Trabalho",
  "color": "#FF0000"
}
```
- **Response:** `201 Created`

### Listar Categorias
- **Endpoint:** `GET /api/categories`
- **Acesso:** Autenticado (Retorna apenas as categorias do usuário logado)
- **Response:** `200 OK`
```json
[
  {
    "id": "uuid-1",
    "name": "Trabalho",
    "color": "#FF0000",
    "taskCount": 5
  }
]
```

---

## ✅ Tarefas (`/api/tasks`)

### Criar Tarefa
- **Endpoint:** `POST /api/tasks`
- **Acesso:** Autenticado
- **Request:**
```json
{
  "title": "Finalizar documentação",
  "description": "Escrever o arquivo API.md",
  "priority": "HIGH",
  "deadline": "2026-03-30T10:00:00",
  "categoryId": "uuid-da-categoria"
}
```
- **Response:** `201 Created`

### Listar Todas as Tarefas
- **Endpoint:** `GET /api/tasks`
- **Acesso:** Autenticado
- **Response:** `200 OK`

### Atualizar Tarefa
- **Endpoint:** `PUT /api/tasks/{id}`
- **Acesso:** Autenticado (Dono da tarefa)

### Transições de Status
- **Iniciar:** `PATCH /api/tasks/{id}/start` (Muda para `DOING`)
- **Concluir:** `PATCH /api/tasks/{id}/done` (Muda para `DONE`)

---

## ⚠️ Tratamento de Erros

A API utiliza um formato padronizado para mensagens de erro:

```json
{
  "timestamp": "2026-03-27T02:00:00Z",
  "status": 400,
  "statusText": "Bad Request",
  "message": "Campos inválidos",
  "path": "/api/tasks",
  "method": "POST",
  "errors": {
    "title": "O título é obrigatório"
  }
}
```

### Códigos Comuns:
- `401 Unauthorized`: Token ausente ou inválido.
- `403 Forbidden`: Tentativa de acessar recurso de outro usuário.
- `404 Not Found`: Recurso não encontrado.
- `409 Conflict`: Violação de unicidade (ex: nome de categoria duplicado).
