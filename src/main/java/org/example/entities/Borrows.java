package org.example.entities;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
public class Borrows {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id" , nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "item_id",nullable = false)
    private Library borrowItem;
    @Column(nullable = false)
    private LocalDate borrowDate;
    private LocalDate expectedReturnDate;
    private LocalDate returnDate;

    public Borrows() {
    }

    public Borrows(User user, Library borrowItem, LocalDate borrowDate) {
        this.user = user;
        this.borrowItem = borrowItem;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = borrowDate.plusDays(30);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Library getBorrowItem() {
        return borrowItem;
    }

    public void setBorrowItem(Library borrowItem) {
        this.borrowItem = borrowItem;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Borrows{" +
                "id=" + id +
                ", userId=" + (user != null ? user.getId() : null) +
                ", itemTitle=" + (borrowItem != null ? borrowItem.getTitle() : null) +
                ", borrowDate=" + borrowDate +
                ", expectedReturnDate=" + expectedReturnDate +
                ", returnDate=" + returnDate +
                '}';
    }

}
