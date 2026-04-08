# 🧠 Estudo Deck

✨ **Plataforma Inteligente de Repetição Espaçada**

---

## 🚀 Sobre o Projeto

O **Estudo Deck** é uma plataforma de flashcards projetada para otimizar o aprendizado através do algoritmo de repetição espaçada **SM-2**. O projeto nasceu com o objetivo técnico de implementar uma solução complexa sobre uma base estrita de **Clean Architecture**, **SOLID** e **Domain-Driven Design (DDD)**. Ele isola as regras complexas de memorização de qualquer dependência externa, garantindo um código altamente testável, manutenível e pronto para escalar no futuro.

---

## 🛠️ Tecnologias e Ferramentas

Este projeto utiliza uma stack moderna e robusta do ecossistema Java:

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.x
    * *Justificativa:* Framework padrão da indústria que acelera a criação de aplicações stand-alone, facilitando injeção de dependência sem poluir o domínio.
* **Banco de Dados:** PostgreSQL
    * *Justificativa:* Banco de dados relacional robusto, excelente para modelagens complexas e buscas estruturadas exigidas pelo sistema.
* **Frontend / Templates:** Thymeleaf
    * *Justificativa:* Renderização server-side com integração nativa ao Spring, permitindo construir a UI de forma fluida e direta.
* **Persistência:** Spring Data JPA / Hibernate
    * *Justificativa:* Abstrai o boilerplate do JDBC, facilitando o mapeamento das entidades de infraestrutura para o banco de dados.
* **Contêineres:** Docker & Docker Compose
    * *Justificativa:* Padroniza o ambiente de desenvolvimento, orquestrando o PostgreSQL rapidamente sem necessidade de instalações locais.
* **Testes:** JUnit 5, Mockito
    * *Justificativa:* Ferramentas padrão para garantir que as regras de negócio funcionem perfeitamente isoladas do framework.
* **CI/CD:** GitHub Actions
    * *Justificativa:* Pipeline automatizado para validação de build e testes a cada novo commit ou Pull Request.

---

## 🏗️ Arquitetura

O projeto segue a **Clean Architecture**, dividindo o sistema em camadas concêntricas para garantir separação de responsabilidades, testabilidade e manutenibilidade:

1.  **Enterprise Business Rules (Domain):** Contém as entidades centrais (`Flashcard`, `Deck`) e a lógica matemática do algoritmo SM-2. Zero dependências de frameworks externos.
2.  **Application Business Rules (Application):** Define os Casos de Uso (`CreateDeckUseCase`) e as interfaces de repositório, orquestrando a lógica de negócio.
3.  **Interface Adapters (Infrastructure):** Implementa os gateways que traduzem os dados entre o banco de dados (JPA) e os Casos de Uso, além dos Controllers para as requisições web.
4.  **Frameworks & Drivers:** Camada mais externa, abrangendo o Spring Boot, configurações do banco de dados e UI.

Além disso, o projeto adere aos princípios **SOLID** e **Clean Code**, promovendo um código flexível e fácil de estender.

---

## ✨ Funcionalidades Implementadas

O Estudo Deck oferece as seguintes funcionalidades principais:

* **Algoritmo de Repetição Espaçada (SM-2):**
    * Cálculo inteligente do próximo intervalo de revisão com base na dificuldade do cartão e repetições consecutivas.
    * *Status:* Implementado no Core Domain, totalmente encapsulado e testado.
* **Gestão de Baralhos (Decks):**
    * Criação de agrupamentos lógicos de flashcards.
    * *Status:* Implementado através de Casos de Uso e persistência com PostgreSQL.
* **Interface Web Interativa:**
    * Telas construídas em Thymeleaf para operação do sistema.
    * *Status:* Implementado o fluxo de criação de novos baralhos. A "mesa de estudos" para revisão está em desenvolvimento.
* **Integração Contínua (CI):**
    * Testes e builds automatizados na nuvem.
    * *Status:* Implementado via GitHub Actions utilizando serviços efêmeros de banco de dados.

---

## ⚙️ Como Rodar o Projeto Localmente

Siga estes passos para configurar e executar o Estudo Deck em sua máquina local.

### Pré-requisitos

* Java 17 JDK
* Docker & Docker Compose
* Apache Maven


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