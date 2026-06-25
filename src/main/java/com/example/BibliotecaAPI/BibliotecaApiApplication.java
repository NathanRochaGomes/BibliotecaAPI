package com.example.BibliotecaAPI;

import com.example.BibliotecaAPI.entity.*;
import com.example.BibliotecaAPI.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDate;

@SpringBootApplication
public class BibliotecaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BibliotecaApiApplication.class, args);
	}

	// Esse bloco roda automaticamente assim que o projeto liga e popula o banco via Java
	@Bean
	public CommandLineRunner populaBanco(AuthorRepository authorRepo, BookRepository bookRepo, LoanRepository loanRepo) {
		return args -> {
			// Cria e salva os Autores
			Author jk = new Author("J.K. Rowling");
			Author tolkien = new Author("J.R.R. Tolkien");
			Author machado = new Author("Machado de Assis");
			authorRepo.save(jk);
			authorRepo.save(tolkien);
			authorRepo.save(machado);

			// Cria e salva os Livros
			Book hp1 = new Book();
			hp1.setTitle("Harry Potter e a Pedra Filosofal");
			hp1.setGenre("Fantasia");
			hp1.setAvailableCopies(3);
			hp1.setAuthor(jk);

			Book sda = new Book();
			sda.setTitle("O Senhor dos Aneis");
			sda.setGenre("Fantasia");
			sda.setAvailableCopies(5);
			sda.setAuthor(tolkien);

			Book domCasmurro = new Book();
			domCasmurro.setTitle("Dom Casmurro");
			domCasmurro.setGenre("Romance");
			domCasmurro.setAvailableCopies(2);
			domCasmurro.setAuthor(machado);

			bookRepo.save(hp1);
			bookRepo.save(sda);
			bookRepo.save(domCasmurro);

			// Cria e salva um Empréstimo Ativo e Atrasado (Para testar os filtros e o /stats)
			Loan loan1 = new Loan();
			loan1.setBook(hp1);
			loan1.setBorrowerEmail("nathan@email.com");
			loan1.setLoanDate(LocalDate.now().minusDays(20)); // Criado há 20 dias (Atrasado)
			loan1.setStatus("ACTIVE");
			loanRepo.save(loan1);

			System.out.println(">>> BANCO DE DADOS POPULADO COM SUCESSO! <<<");
		};
	}
}