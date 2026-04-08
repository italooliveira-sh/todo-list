# 📝 Especificação e Status de Requisitos: ToDo Pro v1.0.0

Este documento atesta a entrega das funcionalidades e o cumprimento das regras de negócio estabelecidas para o projeto.

---

## ✅ Requisitos Funcionais Entregues

### 1. Sistema de Autenticação
- [x] Cadastro de novos usuários com criptografia de senha (BCrypt).
- [x] Login seguro gerando tokens JWT com claims de perfil.
- [x] Proteção Stateless de rotas (Garante que apenas usuários logados acessem dados).
- [x] Logout com invalidação local de tokens.

### 2. Gestão de Tarefas (Core)
- [x] Operações completas de CRUD (Create, Read, Update, Delete).
- [x] Atribuição de prioridades com níveis de criticidade visual.
- [x] Definição de prazos dinâmicos.
- [x] Sistema de filtros por busca textual, status e prioridade.

### 3. Ecossistema de Categorias
- [x] Criação de categorias personalizadas com seletor de cores hexadecimal.
- [x] Sincronização global automática via Signals (Mudanças na sidebar refletem instantaneamente no dashboard).
- [x] Contagem automática de tarefas vinculadas por categoria.

### 4. Dashboards e Métricas
- [x] Cards de estatística reativos aos filtros aplicados pelo usuário.
- [x] Visualização de progresso do workflow.

---

## ⚙️ Regras de Negócio Invioláveis (Garantidas no Backend e Frontend)

1. **Integridade de Workflow**: Uma tarefa **não pode** ser concluída sem antes ter sido iniciada.
2. **Validação Temporal**: O sistema rejeita qualquer prazo (deadline) definido no passado.
3. **Identidade Visual**: Nomes de usuários são normalizados para *Title Case* na exibição.
4. **Segurança de E-mail**: Validação rigorosa via Regex para impedir e-mails malformados ou espaços vazios.
5. **Multi-tenancy**: Isolamento total de dados; um usuário nunca terá acesso às tarefas de outro.

---

## 🛠️ Excelência Técnica (Requisitos Não Funcionais)

- **Backend reativo**: Desenvolvido com **Java 21** e **Spring Boot 3.4**.
- **Frontend moderno**: Utilização intensiva de **Angular 19 Signals** para eliminação de bugs de ciclo de vida.
- **Portabilidade**: Infraestrutura via **Docker** e **Docker Compose**.
- **Deploy Distribuído**: Suporte nativo para Vercel (Front), Render (Back) e Supabase (DB).
