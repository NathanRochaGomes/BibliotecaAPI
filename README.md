# 📚 API REST — Biblioteca

**Disciplina:** Desenvolvimento Backend  
**Integrantes:** Fernando Fernandes e Nathan Rocha  
**Etapa:** 2 — Definição da Arquitetura REST

---

## Entidades e Relacionamentos

| Entidade | Relacionamento |
|----------|----------------|
| `Author` | Possui vários `Books` (1:N) |
| `Book` | Pertence a um `Author` (N:1); possui vários `Loans` (1:N) |
| `Loan` | Pertence a um `Book` (N:1) |

---

## Arquitetura em Camadas

```
Controller → Service → Repository (JPA) → Banco de Dados
     ↑            ↑
    DTO         Entity
```

| Camada | Responsabilidade |
|--------|-----------------|
| **Controller** | Recebe requisições HTTP, delega ao Service, retorna respostas |
| **Service** | Contém a lógica de negócio |
| **Repository** | Acesso ao banco via Spring Data JPA |
| **DTO** | Objetos de entrada e saída (desacoplados da entidade) |

---

## Rotas

### Author

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| `GET` | `/authors` | Lista todos os autores | `200` |
| `GET` | `/authors/{id}` | Busca autor por ID | `200` |
| `POST` | `/authors` | Cria novo autor | `201` |
| `PUT` | `/authors/{id}` | Atualiza autor | `200` |
| `DELETE` | `/authors/{id}` | Remove autor | `204` |

**Parâmetros de listagem (`GET /authors`):**
- `?name=` — filtro por nome
- `?page=0&size=10&sort=name,asc` — paginação e ordenação

---

### Book

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| `GET` | `/books` | Lista todos os livros | `200` |
| `GET` | `/books/{id}` | Busca livro por ID | `200` |
| `POST` | `/books` | Cria novo livro | `201` |
| `PUT` | `/books/{id}` | Atualiza livro | `200` |
| `DELETE` | `/books/{id}` | Remove livro | `204` |

**Parâmetros de listagem (`GET /books`):**
- `?title=` `?genre=` `?authorId=` `?available=true`
- `?page=0&size=10&sort=title,asc`

---

### Loan

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| `GET` | `/Loans` | Lista todos os empréstimos | `200` |
| `GET` | `/Loans/{id}` | Busca empréstimo por ID | `200` |
| `POST` | `/Loans` | Registra novo empréstimo | `201` |
| `PUT` | `/Loans/{id}` | Atualiza empréstimo (ex.: devolução) | `200` |
| `DELETE` | `/Loans/{id}` | Remove empréstimo | `204` |

**Parâmetros de listagem (`GET /Loans`):**
- `?status=ACTIVE|RETURNED|OVERDUE` `?bookId=` `?borrowerEmail=`
- `?page=0&size=10&sort=loanDate,desc`

---

### Estatísticas

| Método | Rota | Descrição | Status |
|--------|------|-----------|--------|
| `GET` | `/stats/summary` | Totais gerais (livros, autores, empréstimos ativos/atrasados) | `200` |
| `GET` | `/stats/topbooks` | Ranking dos livros mais emprestados | `200` |
| `GET` | `/stats/overdue` | Empréstimos em atraso com dias calculados | `200` |

---

## Códigos HTTP de Erro

| Código | Situação |
|--------|----------|
| `400 Bad Request` | Dados inválidos na requisição |
| `404 Not Found` | Recurso não encontrado |
| `409 Conflict` | Regra de negócio violada (ex.: sem exemplares disponíveis) |
| `500 Internal Server Error` | Erro inesperado no servidor |

---

## Estrutura de Pacotes

```
com.biblioteca
├── controller
│   ├── AuthorController.java
│   ├── BookController.java
│   ├── LoanController.java
│   └── StatsController.java
├── service
│   ├── AuthorService.java
│   ├── BookService.java
│   ├── LoanService.java
│   └── StatsService.java
├── repository
│   ├── AuthorRepository.java
│   ├── BookRepository.java
│   └── LoanRepository.java
├── entity
│   ├── Author.java
│   ├── Book.java
│   └── Loan.java
├── dto
│   ├── AuthorRequestDTO.java
│   ├── AuthorResponseDTO.java
│   ├── BookRequestDTO.java
│   ├── BookResponseDTO.java
│   ├── LoanRequestDTO.java
│   └── LoanResponseDTO.java
└── exception
    └── GlobalExceptionHandler.java
```

---

## Regras de Negócio

- ❌ Não é permitido cadastrar empréstimo se não houver exemplares disponíveis → `409`
- 📉 Ao criar um empréstimo, `availableCopies` do livro é **decrementado**
- 📈 Ao registrar devolução, `availableCopies` é **incrementado** e `status` passa para `RETURNED`
- ❌ Não é permitido excluir um `Author` que possua `Books` cadastrados
- ❌ Não é permitido excluir um `Book` com `Loans` ativos