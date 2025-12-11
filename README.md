# 🧠 Estudo Deck - Plataforma de Repetição Espaçada

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Clean Arch](https://img.shields.io/badge/Architecture-Clean-blue?style=for-the-badge)

## 📖 Sobre o Projeto

O **Estudo Deck** é uma plataforma de flashcards gamificada focada na eficiência do aprendizado. O diferencial técnico deste projeto é a implementação "pura" do algoritmo de repetição espaçada **SM-2 (SuperMemo-2)** dentro de uma estrutura rigorosa de **Clean Architecture**.

O objetivo não é apenas criar um CRUD de cartões, mas isolar completamente a lógica complexa de memorização (Domínio) de frameworks externos, garantindo que as regras de negócio sejam o centro da aplicação.

## 🚀 Arquitetura & Design

O projeto segue os princípios de **Clean Architecture** (Robert C. Martin) e **DDD (Domain-Driven Design)**.

### A Regra de Dependência
A aplicação é dividida em círculos concêntricos. As dependências apontam **apenas para dentro**.
1.  **Domain (Core):** Entidades e regras de negócio (Algoritmo SM-2). Sem dependências de Frameworks.
2.  **Use Cases (Application):** Orquestração do fluxo de dados.
3.  **Interface Adapters:** Controllers, Presenters (Thymeleaf).
4.  **Infrastructure:** Banco de Dados (Postgres), UI, Bibliotecas externas.

### Tecnologias Principais

* **Backend:** Java 17+, Spring Boot 3
* **Frontend:** Thymeleaf (Server-side rendering), Bootstrap/Tailwind
* **Persistência:** Spring Data JPA, PostgreSQL
* **Ferramentas:** Lombok, Docker

## ⚙️ Como Executar

### Pré-requisitos
* Java 17+
* Maven
* Docker (Opcional, para banco de dados)

### Passos
1.  Clone o repositório:
    ```bash
    git clone [https://github.com/seu-usuario/estudodeck.git](https://github.com/seu-usuario/estudodeck.git)
    ```
2.  Configure o banco de dados `application.properties` (ou suba via Docker Compose):
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/estudodeck
    spring.datasource.username=postgres
    spring.datasource.password=sua_senha
    ```
3.  Execute a aplicação:
    ```bash
    ./mvnw spring-boot:run
    ```

## 📚 O Algoritmo SM-2
A lógica central calcula o próximo intervalo de revisão baseada em:
* **Repetições (n):** Número de acertos consecutivos.
* **Fator de Facilidade (EF):** Quão difícil é o cartão (padrão 2.5).
* **Intervalo (I):** Dias até a próxima revisão.

---
*Desenvolvido como projeto de estudo avançado em Engenharia de Software.*
