package org.example.entities;

import org.example.enumeration.ArticleType;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)

public abstract class Library {
    @Id
    @GeneratedValue
    protected long id;
    @Column(nullable = false,unique = true)
    protected long isbnCode;
    @Column(nullable = false)
    protected String title;
    protected int publishYear;
    protected int numberOfPages;
    @Enumerated(EnumType.STRING)
    protected ArticleType articleType;
    @OneToMany(mappedBy = "borrowItem",cascade = CascadeType.ALL)
    protected List<Borrows> listOfBorrowers;

    public Library() {
    }

    public Library(long isbnCode, String title) {
        this.title = title;
        this.isbnCode = isbnCode;
    }

    public Library(long isbnCode, String title, int publishYear, int numberOfPages) {
        this.isbnCode = isbnCode;
        this.title = title;
        this.publishYear = publishYear;
        this.numberOfPages = numberOfPages;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getisbnCode() {
        return isbnCode;
    }

    public void setisbnCode(long isbnCode) {
        this.isbnCode = isbnCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public ArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(ArticleType articleType) {
        this.articleType = articleType;
    }

    @Override
    public String toString() {
        return " " +
                "ISBN = " + isbnCode +
                ", title = '" + title + '\'' +
                ", publishYear = " + publishYear +
                ", numberOfPages = " + numberOfPages +
                ' ';
    }
}




