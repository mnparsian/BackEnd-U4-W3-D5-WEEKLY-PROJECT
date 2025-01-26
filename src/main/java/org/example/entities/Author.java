package org.example.entities;

import org.example.enumeration.PersonType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "authors")
public class Author extends Person {
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinTable(
            name = "author_books",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Books> books = new HashSet<>();

    public Author() {
    }

    public Author(String name, String surname, LocalDate dateOfBirth) {
        super(name, surname, dateOfBirth, PersonType.AUTHOR);
    }

    public Set<Books> getBooks() {
        return books;
    }

    public void setBooks(Set<Books> books) {
        this.books = books;
    }

    public void addBook(Books book) {
        this.books.add(book);
        book.getAuthors().add(this);
    }

    public void removeBook(Books book) {
        this.books.remove(book);
        book.getAuthors().remove(this);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                '}';
    }
}