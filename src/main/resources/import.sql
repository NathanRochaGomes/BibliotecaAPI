-- Cadastra Autores
INSERT INTO authors (name) VALUES ('J.K. Rowling');
INSERT INTO authors (name) VALUES ('J.R.R. Tolkien');

-- Cadastra Livros (Viculados aos IDs dos autores acima)
INSERT INTO books (title, genre, available_copies, author_id) VALUES ('Harry Potter e a Pedra Filosofal', 'Fantasia', 3, 1);
INSERT INTO books (title, genre, available_copies, author_id) VALUES ('O Senhor dos Anéis', 'Fantasia', 5, 2);

-- Cadastra um Empréstimo Ativo para o livro 1
INSERT INTO loans (borrower_email, loan_date, status, book_id) VALUES ('estudante@email.com', '2026-06-01', 'ACTIVE', 1);