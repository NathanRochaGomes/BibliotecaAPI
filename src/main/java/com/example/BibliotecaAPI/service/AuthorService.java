package com.example.BibliotecaAPI.service;

import com.example.BibliotecaAPI.dto.*;
import com.example.BibliotecaAPI.entity.Author;
import com.example.BibliotecaAPI.repository.AuthorRepository;
import com.example.BibliotecaAPI.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private BookRepository bookRepository;

    // Listar todos com paginação e filtro
    public Page<AuthorResponseDTO> getAll(String name, Pageable pageable) {
        Page<Author> authors;
        if (name == null || name.isEmpty()) {
            authors = repository.findAll(pageable);
        } else {
            authors = repository.findByNameContainingIgnoreCase(name, pageable);
        }
        return authors.map(a -> new AuthorResponseDTO(a.getId(), a.getName()));
    }

    // Buscar apenas um (Se não achar, retorna Erro 404)
    public AuthorResponseDTO getOne(Long id) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado."));
        return new AuthorResponseDTO(author.getId(), author.getName());
    }

    // Criar (Salvar)
    public AuthorResponseDTO create(AuthorRequestDTO dto) {
        Author author = new Author(dto.name());
        Author saved = repository.save(author);
        return new AuthorResponseDTO(saved.getId(), saved.getName());
    }

    // Atualizar
    public AuthorResponseDTO update(Long id, AuthorRequestDTO dto) {
        Author author = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado."));
        author.setName(dto.name());
        Author updated = repository.save(author);
        return new AuthorResponseDTO(updated.getId(), updated.getName());
    }

    // Deletar com validação de segurança (Regra corporativa do PDF)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor não encontrado.");
        }
        if (bookRepository.existsByAuthorId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Não é permitido excluir um autor com livros vinculados.");
        }
        repository.deleteById(id);
    }
}