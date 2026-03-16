# 📝 Especificação de Requisitos: Fullstack To-Do Pro

## 🎯 Visão Geral
O objetivo deste projeto é criar uma plataforma de gestão de tarefas (To-Do List) multiusuário. O foco principal é aplicar conceitos de alta disponibilidade, segurança e boas práticas de engenharia, servindo como um portfólio técnico para demonstrar competências em **Java (Spring Boot)**, **Angular** e **Cultura DevOps**.

## ✅ Requisitos Funcionais (RF)

| ID | Descrição | Status |
|----|-----------|--------|
| RF01 | Gestão de Usuários: O sistema deve permitir o registro, login e logout de usuários. | ⏳ Pendente |
| RF02 | Autenticação Segura: O acesso deve ser protegido por tokens (JWT), garantindo o isolamento de dados entre usuários. | ⏳ Pendente |
| RF03 | CRUD de Tarefas: Capacidade de criar, ler, atualizar e excluir tarefas. | 🔄 Em Progresso (Entidade criada) |
| RF04 | Categorização: Possibilidade de atribuir categorias às tarefas (ex: Trabalho, Pessoal, Estudos). | ⏳ Pendente |
| RF05 | Priorização: Atribuição de níveis de prioridade (Baixa, Média, Alta). | ⏳ Pendente |
| RF06 | Estados da Tarefa: Controle de fluxo da tarefa (Pendente, Em Progresso, Concluída). | ⏳ Pendente |
| RF07 | Datas de Conclusão: Definição de prazos (deadlines) para as tarefas. | ⏳ Pendente |

## ⚙️ Requisitos Não Funcionais (RNF)

| ID | Descrição | Status |
|----|-----------|--------|
| RNF01 | Persistência: Armazenamento de dados em banco de dados relacional PostgreSQL. | ✅ Concluído (Configurado) |
| RNF02 | Escalabilidade e Portabilidade: A aplicação (Backend e Frontend) deve ser containerizada utilizando Docker. | ⏳ Pendente |
| RNF03 | Documentação da API: Interface de documentação interativa utilizando Swagger/OpenAPI. | ⏳ Pendente |
| RNF04 | Testabilidade: Cobertura de testes unitários no backend com JUnit 5 e Mockito. | 🔄 Em Progresso (Testes básicos criados) |
| RNF05 | Observabilidade: Implementação de logs estruturados para monitoramento da saúde da aplicação. | ⏳ Pendente |
| RNF06 | Arquitetura: Implementação seguindo padrões de Camadas ou Clean Architecture. | 🔄 Em Progresso (Estrutura criada) |

## 🛠️ Tecnologias Planejadas
- **Backend:** Java 21, Spring Boot 3.3.10, PostgreSQL, JWT
- **Frontend:** Angular (TypeScript), Material UI
- **DevOps:** Docker, Docker Compose, GitHub Actions
- **Testes:** JUnit 5, Mockito, Jest

## 📊 Roadmap
1. ✅ Setup do projeto e arquitetura
2. 🔄 Implementação do CRUD básico de tarefas (TDD)
3. ⏳ Autenticação e autorização
4. ⏳ Frontend Angular
5. ⏳ DevOps (Docker, CI/CD)
6. ⏳ Funcionalidades avançadas (categorias, prioridades, etc.)