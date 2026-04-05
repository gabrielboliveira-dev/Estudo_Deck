# 🧠 Estudo Deck - Plataforma Inteligente de Repetição Espaçada

*Transforme seu conhecimento em memória de longo prazo com uma arquitetura de software exemplar.*

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Arquitetura](https://img.shields.io/badge/Arquitetura-Clean%20%26%20DDD-1E90FF?style=for-the-badge)

---

## 📖 Sobre o Projeto

O **Estudo Deck** é uma plataforma de flashcards multi-usuário, social e gamificada, projetada para otimizar o aprendizado através do algoritmo de repetição espaçada **SM-2**. O projeto nasceu com um objetivo técnico claro: implementar uma solução complexa e rica em funcionalidades sobre uma base de **Clean Architecture** e princípios de **Domain-Driven Design (DDD)**, garantindo que as regras de negócio sejam o centro desacoplado e testável da aplicação.

O sistema evoluiu de uma ferramenta de estudo pessoal para um ecossistema colaborativo onde usuários podem não apenas gerenciar seu próprio aprendizado, mas também compartilhar conhecimento com a comunidade.

---

## 🛠️ Tecnologias e Ferramentas

A stack foi escolhida para garantir robustez, segurança e uma excelente experiência de desenvolvimento.

*   **Backend:** **Java 17** e **Spring Boot 3**: Utiliza o poder do ecossistema Spring moderno para criar uma aplicação segura, performática e de fácil manutenção.
*   **Frontend:** **Thymeleaf (Server-Side Rendering)**: Escolhido pela simplicidade e integração perfeita com o Spring, permitindo o desenvolvimento rápido de uma interface reativa e funcional.
*   **Persistência:** **Spring Data JPA** e **PostgreSQL**: Combina a produtividade de um ORM poderoso com a confiabilidade e os recursos avançados de um banco de dados relacional de ponta, incluindo o uso de queries nativas para **Busca Full-Text**.
*   **Autenticação:** **Spring Security**: Implementa o padrão da indústria para segurança, gerenciando o registro, login e proteção de rotas com senhas devidamente "hasheadas" (BCrypt).
*   **Testes:** **JUnit 5, Mockito e H2 Database**: Garante a qualidade do código com testes unitários para a lógica de domínio e testes de integração isolados que rodam em um banco de dados em memória.
*   **Containerização:** **Docker**: Fornece um ambiente de desenvolvimento reproduzível com um único comando, eliminando problemas de configuração de ambiente.

---

## 🏛️ Arquitetura

O projeto é estritamente organizado segundo os princípios da **Clean Architecture**, garantindo a separação de responsabilidades e a testabilidade.

1.  **Domain (Core):** Contém as entidades e regras de negócio puras (ex: `Deck`, `Flashcard`, `User`, o algoritmo SM-2). Não possui nenhuma dependência de frameworks. É o coração protegido da aplicação.
2.  **Application (Casos de Uso):** Orquestra o fluxo de dados e as regras de negócio. Define os "Use Cases" da aplicação (ex: `CreateDeckUseCase`, `ReviewFlashcardUseCase`) e as interfaces dos `Gateways` (repositórios).
3.  **Infrastructure (Camada Externa):** Implementa os detalhes técnicos. Contém os Controllers (Web e API), as implementações dos `Gateways` usando Spring Data JPA, e as configurações de segurança e frameworks.

A **Regra de Dependência** é rigorosamente seguida: todo o código aponta para dentro, do `Infrastructure` para o `Application`, e do `Application` para o `Domain`.

---

## ✨ Funcionalidades Implementadas

O "Estudo Deck" é uma plataforma completa que vai muito além de um simples CRUD.

### 🧠 Core de Aprendizado
- **Algoritmo SM-2**: Otimização da memorização com repetição espaçada.
- **Tipos de Cartões**: Suporte a cartões de **Pergunta/Resposta** e de **Omissão de Palavras (Cloze)**.
- **Gestão de "Leeches"**: Suspensão automática de cartões muito difíceis para não frustrar o usuário.
- **Sessões Customizadas (Cramming)**: Modo de revisão rápida para todos os cartões, sem afetar o agendamento inteligente.

### 📚 Organização e Gestão
- **CRUD Completo**: Gerenciamento total de baralhos e flashcards.
- **Hierarquia de Baralhos**: Organize seus estudos em sub-baralhos (ex: `Ciência > Física > Óptica`).
- **Tags**: Adicione múltiplas tags aos baralhos para uma organização transversal.
- **Busca Full-Text**: Encontre qualquer termo em todos os seus cartões com uma busca inteligente.
- **Importação e Exportação**: Migre seus dados para dentro e para fora da plataforma usando o formato **CSV**.

### 📊 Análise e Métricas
- **Gráfico de Previsão (Forecast)**: Visualize a quantidade de cartões agendados para os próximos 30 dias.
- **Heatmap de Consistência**: Um calendário visual que mostra sua frequência de estudos no último ano.
- **Estatísticas de Retenção**: Meça sua taxa de acerto em cartões novos vs. maduros.

### 🎮 Gamificação
- **Ofensivas (Streaks)**: O sistema calcula e exibe seus dias consecutivos de estudo para mantê-lo motivado.
- **Experiência (XP) e Níveis**: Ganhe pontos de experiência a cada revisão e suba de nível.

### 🌐 Social e Ecossistema
- **Sistema Multi-usuário**: Plataforma completa com registro e login, garantindo a privacidade total dos dados de cada usuário.
- **Marketplace de Baralhos**: Publique seus baralhos para a comunidade e explore o conteúdo criado por outros usuários.
- **Clonagem de Baralhos**: Adicione baralhos públicos à sua coleção com um único clique.

---

## 🚀 Como Rodar o Projeto

Siga estes passos para ter o ambiente de desenvolvimento rodando localmente.

### Pré-requisitos
- JDK 17+
- Apache Maven 3.8+
- Docker e Docker Compose

### Passos
1.  **Clone o repositório:**
    ```bash
    git clone <URL_DO_SEU_REPOSITORIO>
    cd estudodeck
    ```

2.  **Inicie o Banco de Dados:**
    Use o Docker Compose para subir o container do PostgreSQL.
    ```bash
    docker-compose up -d
    ```
    *Isso iniciará um banco de dados na porta `5432` com o nome `estudodeck`, usuário `postgres` e senha `sua_senha`.*

3.  **Verifique a Configuração:**
    O arquivo `src/main/resources/application.properties` já está configurado para usar as credenciais acima. Se você as alterou no `docker-compose.yml`, ajuste-as aqui também.

4.  **Execute a Aplicação:**
    Use o Maven para compilar e rodar o projeto.
    ```bash
    mvn spring-boot:run
    ```

5.  **Acesse a Aplicação:**
    - **Interface Web:** Abra seu navegador e acesse `http://localhost:8080/login`
    - **Primeiro Acesso:** Use o link na página de login para se registrar.

---

## 📡 Endpoints Principais (API)

A aplicação expõe uma API RESTful sob o prefixo `/api`.

| Método | Rota                                                 | Descrição                               |
|--------|------------------------------------------------------|-------------------------------------------|
| `POST` | `/api/decks`                                         | Cria um novo baralho.                     |
| `PUT`  | `/api/decks/{id}`                                    | Atualiza o nome de um baralho.            |
| `POST` | `/api/decks/{deckId}/flashcards`                     | Adiciona um novo flashcard a um baralho.  |
| `POST` | `/api/decks/{deckId}/flashcards/{flashcardId}/review`| Submete o resultado de uma revisão (0-5). |
| `POST` | `/api/decks/{deckId}/tags`                           | Adiciona uma tag a um baralho.            |
| `DELETE`| `/api/decks/{deckId}`                                | Exclui um baralho e todos os seus cartões.|

---

## 🏆 Demonstração de Boas Práticas

Este projeto serve como um case de estudo na aplicação de práticas modernas de engenharia de software:

-   **SOLID:** Cada classe e método possui uma responsabilidade única, especialmente visível na separação de Casos de Uso.
-   **Clean Architecture & DDD:** O código é organizado em torno do domínio de negócio, não de frameworks.
-   **CQRS Simplificado:** Há uma clara separação entre "Comandos" (operações de escrita, como `ReviewFlashcardUseCase`) e "Queries" (operações de leitura otimizadas, como `GetReviewForecastUseCase`).
-   **Testes Abrangentes:** O projeto utiliza testes unitários para a lógica de domínio e testes de integração isolados para os fluxos da aplicação.
-   **Segurança:** A autenticação e autorização são gerenciadas pelo Spring Security, com armazenamento seguro de senhas.
