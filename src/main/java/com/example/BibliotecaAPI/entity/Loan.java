package com.example.BibliotecaAPI.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String borrowerEmail;
	private LocalDate loanDate;
	private LocalDate returnDate;
	private String status; // ACTIVE, RETURNED, OVERDUE

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	public Loan() {}

	// Getters e Setters
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getBorrowerEmail() { return borrowerEmail; }
	public void setBorrowerEmail(String borrowerEmail) { this.borrowerEmail = borrowerEmail; }
	public LocalDate getLoanDate() { return loanDate; }
	public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }
	public LocalDate getReturnDate() { return returnDate; }
	public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public Book getBook() { return book; }
	public void setBook(Book book) { this.book = book; }
}