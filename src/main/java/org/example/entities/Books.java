package org.example.entities;

import org.example.entities.Library;
import org.example.enumeration.GenreType;
import org.example.enumeration.ArticleType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Books extends Library {
    @ManyToMany(mappedBy = "books",cascade = CascadeType.ALL)
    private Set<Author> authors = new HashSet<>();

    @Enumerated(value = EnumType.STRING)
    private GenreType genre;

    public Books() {
    }

    public Books(long isbnCode, String title, Set<Author> authors, GenreType genre) {
        super(isbnCode, title);
        this.authors = authors;
        this.genre = genre;
        this.articleType = ArticleType.BOOK;
    }

    public Books(long isbnCode, String title) {
        super(isbnCode, title);
        this.articleType = ArticleType.BOOK;
    }

    public GenreType getGenre() {
        return genre;
    }

    public void setGenre(GenreType genre) {
        this.genre = genre;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }

    public void removeAuthor(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this);
    }

    @Override
    public String toString() {
        return "Books{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", isbnCode=" + getisbnCode() +
                '}';
    }
}
