package com.example.BibliotecaAPI.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String genre;
    private Integer availableCopies;

    // REQUISITO DA CARTA-DESAFIO: Armazenamento seguro do arquivo em bytes (BLOB)
    @Lob
    @Column(name = "cover_image", length = 5000000) // Suporta arquivos de até 5MB
    private byte[] coverImage;

    private String coverContentType; // Guarda o tipo MIME (ex: image/png)

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "book")
    private List<Loan> loans;

    public Book() {}

    // Getters e Setters antigos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    // REQUISITO DA CARTA-DESAFIO: Novos Getters e Setters para o arquivo
    public byte[] getCoverImage() { return coverImage; }
    public void setCoverImage(byte[] coverImage) { this.coverImage = coverImage; }
    public String getCoverContentType() { return coverContentType; }
    public void setCoverContentType(String coverContentType) { this.coverContentType = coverContentType; }
}