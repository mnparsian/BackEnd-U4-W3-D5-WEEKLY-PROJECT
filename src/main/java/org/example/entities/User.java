package org.example.entities;

import org.example.enumeration.PersonType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "users")
public class User extends Person{
    @Column(nullable = false,unique = true)
    private long cardNumber;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Borrows> borrowFromLibrary;

    public User() {
    }

    public User(String name, String surname, LocalDate dateOfBirth, long cardNumber) {
        super(name, surname, dateOfBirth,PersonType.USER);
        this.cardNumber = cardNumber;
        this.borrowFromLibrary = new ArrayList<>();
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public List<Borrows> getBorrowFromLibrary() {
        return borrowFromLibrary;
    }

    public void setBorrowFromLibrary(List<Borrows> borrowFromLibrary) {
        this.borrowFromLibrary = borrowFromLibrary;
    }

    @Override
    public String toString() {
        return "User{" +
                "cardNumber=" + cardNumber +
                ", borrowFromLibrary=" + borrowFromLibrary +
                "} " + super.toString();
    }
}
