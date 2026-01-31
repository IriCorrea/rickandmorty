# Rick and Morty BFF

Este projeto é um **Backend for Frontend (BFF)** desenvolvido em Java com Spring Boot 3, que atua como um proxy inteligente para a API pública do Rick and Morty.

O projeto segue uma abordagem **Design-First**, onde o código (interfaces e DTOs) é gerado a partir de um contrato OpenAPI (Swagger).

## 🚀 Tecnologias Utilizadas

*   **Java 21**
*   **Spring Boot 3.2.2**
*   **Maven**
*   **OpenAPI Generator** (Scaffolding)
*   **Lombok** (Boilerplate reduction)
*   **Spring Cache** (Otimização)
*   **JUnit 5 & Mockito** (Testes)

## 📋 Pré-requisitos

Antes de começar, certifique-se de ter instalado em sua máquina:

*   **Java JDK 21**
*   **Maven 3.8+**
*   **IDE** (IntelliJ IDEA, Eclipse, VS Code)

### ⚠️ Importante: Configuração da IDE

Como este projeto utiliza **Lombok** e **Geração de Código**, você precisa:

1.  **Instalar o Plugin do Lombok**: Certifique-se de que sua IDE tenha o plugin do Lombok instalado e o processamento de anotações habilitado.
2.  **Gerar os Fontes**: O projeto não compilará imediatamente ao ser aberto porque as classes da API (`PersonagensApi`, DTOs) são geradas em tempo de build.

## 🛠️ Instalação e Build

1.  Clone o repositório.
2.  Abra o terminal na raiz do projeto.
3.  Execute o comando abaixo para baixar as dependências e **gerar as classes do OpenAPI**:

```bash
mvn clean install
```

> **Nota**: Este passo é crucial. O plugin `openapi-generator-maven-plugin` lerá o arquivo `src/main/resources/openapi/openapi.yaml` e criará as interfaces e modelos na pasta `target/generated-sources/openapi`. O Maven está configurado para reconhecer essa pasta como fonte.

## ▶️ Como Rodar

Após o build com sucesso, você pode rodar a aplicação via terminal ou IDE.

**Via Terminal:**
```bash
mvn spring-boot:run
```

**Via IDE:**
Localize a classe `com.api.rickandmorty.RickAndMortyApplication` e execute o método `main`.

## 🔌 Endpoints Disponíveis

A aplicação rodará por padrão na porta `8080`.

### 1. Listar Personagens (Paginado)
Retorna uma lista simplificada (apenas ID e Nome) com paginação.

*   **URL**: `GET /personagens`
*   **Query Param**: `page` (Opcional, default: 1)
*   **Exemplo**:
    ```bash
    curl "http://localhost:8080/personagens?page=1"
    ```

### 2. Buscar Personagem por Nome
Busca um personagem específico pelo nome. Retorna o primeiro match encontrado.

*   **URL**: `GET /personagens/{nome}`
*   **Exemplo**:
    ```bash
    curl "http://localhost:8080/personagens/Rick"
    ```

## 🧪 Testes

Para executar os testes unitários e de integração:

```bash
mvn test
```

## 🏗️ Arquitetura

*   **Contract-First**: Definição da API em `src/main/resources/openapi/openapi.yaml`.
*   **Controller**: Implementa a interface gerada pelo OpenAPI.
*   **Service**: Contém a lógica de negócio, cache e transformação de dados.
*   **Client**: Utiliza `RestClient` para consumir a API externa do Rick and Morty.
*   **Exception Handling**: Tratamento global de erros para garantir respostas HTTP adequadas.
