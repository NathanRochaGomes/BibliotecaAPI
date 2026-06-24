package com.example.BibliotecaAPI.service;

import com.example.BibliotecaAPI.dto.*;
import com.example.BibliotecaAPI.entity.*;
import com.example.BibliotecaAPI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;

@Service
public class LoanService {
    @Autowired private LoanRepository loanRepository;
    @Autowired private BookRepository bookRepository;

    public Page<LoanResponseDTO> getAll(String status, Long bookId, String borrowerEmail, Pageable pageable) {
        return loanRepository.findWithFilters(status, bookId, borrowerEmail, pageable)
                .map(l -> new LoanResponseDTO(l.getId(), l.getBook().getTitle(), l.getBorrowerEmail(), l.getLoanDate(), l.getReturnDate(), l.getStatus()));
    }

    public LoanResponseDTO getOne(Long id) {
        Loan l = loanRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
        return new LoanResponseDTO(l.getId(), l.getBook().getTitle(), l.getBorrowerEmail(), l.getLoanDate(), l.getReturnDate(), l.getStatus());
    }

    public LoanResponseDTO create(LoanRequestDTO dto) {
        Book book = bookRepository.findById(dto.bookId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));

        if (book.getAvailableCopies() <= 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não há exemplares disponíveis deste livro");
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setBorrowerEmail(dto.borrowerEmail());
        loan.setLoanDate(LocalDate.now());
        loan.setStatus("ACTIVE");

        Loan saved = loanRepository.save(loan);
        return new LoanResponseDTO(saved.getId(), saved.getBook().getTitle(), saved.getBorrowerEmail(), saved.getLoanDate(), saved.getReturnDate(), saved.getStatus());
    }

    public LoanResponseDTO returnBook(Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
        if ("RETURNED".equals(loan.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este empréstimo já foi devolvido");
        }

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        loan.setStatus("RETURNED");
        loan.setReturnDate(LocalDate.now());
        Loan updated = loanRepository.save(loan);

        return new LoanResponseDTO(updated.getId(), updated.getBook().getTitle(), updated.getBorrowerEmail(), updated.getLoanDate(), updated.getReturnDate(), updated.getStatus());
    }

    public void delete(Long id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
        loanRepository.delete(loan);
    }
}