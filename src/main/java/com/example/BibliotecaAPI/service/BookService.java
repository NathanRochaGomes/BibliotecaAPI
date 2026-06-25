package com.example.BibliotecaAPI.service;

import com.example.BibliotecaAPI.dto.*;
import com.example.BibliotecaAPI.entity.*;
import com.example.BibliotecaAPI.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class BookService {

    @Autowired private BookRepository bookRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private LoanRepository loanRepository;

    public Page<BookResponseDTO> getAll(String title, String genre, Long authorId, Boolean available, Pageable pageable) {
        return bookRepository.findWithFilters(title, genre, authorId, available, pageable)
                .map(b -> new BookResponseDTO(b.getId(), b.getTitle(), b.getGenre(), b.getAvailableCopies(), b.getAuthor().getName()));
    }

    public BookResponseDTO getOne(Long id) {
        Book b = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
        return new BookResponseDTO(b.getId(), b.getTitle(), b.getGenre(), b.getAvailableCopies(), b.getAuthor().getName());
    }

    public BookResponseDTO create(BookRequestDTO dto) {
        Author author = authorRepository.findById(dto.authorId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autor não existe"));
        Book book = new Book();
        book.setTitle(dto.title());
        book.setGenre(dto.genre());
        book.setAvailableCopies(dto.availableCopies());
        book.setAuthor(author);
        Book saved = bookRepository.save(book);
        return new BookResponseDTO(saved.getId(), saved.getTitle(), saved.getGenre(), saved.getAvailableCopies(), saved.getAuthor().getName());
    }

    public BookResponseDTO update(Long id, BookRequestDTO dto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
        Author author = authorRepository.findById(dto.authorId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Autor não existe"));
        book.setTitle(dto.title());
        book.setGenre(dto.genre());
        book.setAvailableCopies(dto.availableCopies());
        book.setAuthor(author);
        Book updated = bookRepository.save(book);
        return new BookResponseDTO(updated.getId(), updated.getTitle(), updated.getGenre(), updated.getAvailableCopies(), updated.getAuthor().getName());
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado");
        if (loanRepository.existsByBookIdAndStatus(id, "ACTIVE")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é permitido excluir um livro com empréstimos ativos");
        }
        bookRepository.deleteById(id);
    }

    // REGRAS DA CARTA-DESAFIO
    public void saveCover(Long id, MultipartFile file) throws IOException {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo muito grande! O tamanho máximo permitido é 5MB.");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de arquivo inválido! Envie apenas imagens JPG ou PNG.");
        }

        book.setCoverImage(file.getBytes());
        book.setCoverContentType(contentType);
        bookRepository.save(book);
    }

    public Book getBookEntity(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }
}