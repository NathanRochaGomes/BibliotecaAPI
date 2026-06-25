# 📚 Guia Oficial de Apresentação e Roteiro de Testes — BibliotecaAPI

Este arquivo serve como roteiro oficial para a defesa e demonstração das funcionalidades do backend da **BibliotecaAPI**.

A aplicação foi desenvolvida em **Java com Spring Boot**, utilizando uma arquitetura em camadas e persistência em banco de dados em memória (**H2**).

---

# 1. ARQUITETURA

## Estrutura do projeto

> O projeto foi estruturado seguindo o padrão de Arquitetura em Camadas. Nós separamos a aplicação em:
>
> 1. **Controllers**: Responsáveis por expor os endpoints HTTP e receber as requisições.
> 2. **Services**: Onde fica isolada toda a nossa lógica e regras de negócio do sistema.
> 3. **Repositories**: Interfaces do Spring Data JPA que gerenciam a persistência e consultas ao banco de dados.
> 4. **DTOs (Data Transfer Objects)**: Objetos de transferência para entrada e saída de dados, garantindo o desacoplamento e a segurança das nossas entidades.
>
> Além disso, criamos um arquivo `.env` na raiz para gerenciar as variáveis de ambiente e proteger as credenciais de acesso.

---

# 2. CARGA INICIAL DE DADOS (Inserções Automáticas)

Ao iniciar a aplicação pelo arquivo `BibliotecaApiApplication.java`, o banco de dados é populado automaticamente via código Java com a seguinte estrutura:

## Autores

| ID | Nome             |
| -- | ---------------- |
| 1  | J.K. Rowling     |
| 2  | J.R.R. Tolkien   |
| 3  | Machado de Assis |

## Livros

| ID | Título                           |
| -- | -------------------------------- |
| 1  | Harry Potter e a Pedra Filosofal |
| 2  | O Senhor dos Aneis               |
| 3  | Dom Casmurro                     |

## Empréstimos

* **1 empréstimo ativo e atrasado** (ID 1)
* Livro associado: **Livro 1**
* E-mail do usuário: `nathan@email.com`

---

# 3. ROTEIRO DE TESTES NO INSOMNIA

## 🔹 Passo 3.1: Listagem Geral e Filtros (GET)

### 1. Listar todos os autores

**Método:** `GET`

**URL**

```http
http://localhost:8080/authors
```
---

### 2. Testar filtro de busca por nome

**Método:** `GET`

**URL**

```http
http://localhost:8080/authors?name=Rowling
```
---

### 3. Listar livros com paginação customizada

**Método:** `GET`

**URL**

```http
http://localhost:8080/books?size=2&sort=title,desc
```

**Resposta esperada**

* Status `200 OK`
* Retorna no máximo 2 livros.
* Ordenação de Z para A.

---

## 🔹 Passo 3.2: Inserções de Dados (POST)

### 1. Cadastrar um novo autor

**Método:** `POST`

**URL**

```http
http://localhost:8080/authors
```

**Body**

```json
{
  "name": "George R.R. Martin"
}
```

**Resposta esperada**

* Status `201 Created`
* Autor criado com ID 4.

---

### 2. Cadastrar um novo livro

**Método:** `POST`

**URL**

```http
http://localhost:8080/books
```

**Body**

```json
{
  "title": "A Guerra dos Tronos",
  "genre": "Fantasia",
  "availableCopies": 5,
  "authorId": 4
}
```

**Resposta esperada**

* Status `201 Created`

---

## 🔹 Passo 3.3: Alterações e Atualizações (PUT)

### Atualizar dados de um livro

**Método:** `PUT`

**URL**

```http
http://localhost:8080/books/1
```

**Body**

```json
{
  "title": "Harry Potter e a Pedra Filosofal - Edição Especial",
  "genre": "Fantasia",
  "availableCopies": 10,
  "authorId": 1
}
```

**Resposta esperada**

* Status `200 OK`
* Livro atualizado com o novo título.

---

## 🔹 Passo 3.4: Fluxo Completo de Empréstimo e Devolução

### 1. Realizar um novo empréstimo

**Método:** `POST`

**URL**

```http
http://localhost:8080/loans
```

**Body**

```json
{
  "bookId": 2,
  "borrowerEmail": "fernando@email.com"
}
```

**Resposta esperada**

* Status `201 Created`
* Status do empréstimo = `"ACTIVE"`
* O sistema reduz automaticamente uma unidade do estoque.

---

### 2. Registrar uma devolução

**Método:** `PUT`

**URL**

```http
http://localhost:8080/loans/2
```

**Body**

```
No Body
```

**Resposta esperada**

* Status `200 OK`
* Status alterado para `"RETURNED"`
* Data de devolução preenchida.
* Estoque incrementado em 1 unidade.

---

## 🔹 Passo 3.5: Exclusões de Dados (DELETE)

### Deletar um empréstimo finalizado

**Método:** `DELETE`

**URL**

```http
http://localhost:8080/loans/2
```

**Resposta esperada**

* Status `204 No Content`

---

## 🔹 Passo 3.6: Painel Analítico e Visualizações de Dados

### 1. Resumo geral

**Método:** `GET`

**URL**

```http
http://localhost:8080/stats/summary
```

**Resposta esperada**

* Status `200 OK`
* Totais de livros, autores e empréstimos calculados dinamicamente.

---

### 2. Listar empréstimos em atraso

**Método:** `GET`

**URL**

```http
http://localhost:8080/stats/overdue
```

**Resposta esperada**

* Status `200 OK`

---

# 🚨 4. DEMONSTRAÇÃO DE ERROS E REGRAS DE NEGÓCIO

Demonstre que a API possui validações e regras de negócio robustas.

---

## 🔹 Erro 400 — Bad Request

### Validação de dados inválidos

**Método:** `POST`

**URL**

```http
http://localhost:8080/authors
```

**Body**

```json
{
  "name": ""
}
```

**Resposta esperada**

* Status `400 Bad Request`

---

## 🔹 Erro 404 — Not Found

### Autor inexistente

**Método:** `GET`

**URL**

```http
http://localhost:8080/authors/999
```

**Resposta esperada**

* Status `404 Not Found`
* Mensagem:

```text
Autor não encontrado.
```

---

## 🔹 Erro 409 — Conflict

### Exclusão de autor com livros vinculados

**Método:** `DELETE`

**URL**

```http
http://localhost:8080/authors/1
```

**Resposta esperada**

* Status `409 Conflict`

```text
Não é permitido excluir um autor com livros vinculados.
```

---

## 🔹 Erro 409 — Conflict

### Exclusão de livro com empréstimos ativos

**Método:** `DELETE`

**URL**

```http
http://localhost:8080/books/1
```

**Resposta esperada**

* Status `409 Conflict`

```text
Não é permitido excluir um livro com empréstimos ativos.
```

---

# 📸 5. CARTA-DESAFIO: UPLOAD E DOWNLOAD SEGURO DE IMAGENS

Demonstre a implementação do gerenciamento seguro de arquivos anexos armazenados diretamente no banco de dados em formato binário (**BLOB**).

---

## 🔹 Upload da capa do livro

### Método

`POST`

### URL

```http
http://localhost:8080/books/1/cover
```

### Configuração no Insomnia

1. Clique em **Body**.
2. Selecione **Multipart Form**.
3. Adicione um campo:

| Campo   | Valor       |
| ------- | ----------- |
| Name    | file        |
| Tipo    | File        |
| Arquivo | capa-hp.png |

4. Clique em **Send**.

### Resposta esperada

* Status `201 Created`

```text
Capa do livro enviada com sucesso!
```

---

## 🔹 Visualizar a imagem armazenada

### Método

`GET`

### URL

```http
http://localhost:8080/books/1/cover
```

### Resposta esperada

* Status `200 OK`
* O Insomnia renderiza a imagem na aba **Preview**, comprovando o upload e download da capa diretamente do banco de dados.
