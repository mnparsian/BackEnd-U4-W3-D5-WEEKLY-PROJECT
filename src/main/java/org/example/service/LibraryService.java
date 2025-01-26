package org.example.service;

import com.github.javafaker.Faker;
import org.example.entities.*;
import org.example.enumeration.GenreType;
import org.example.enumeration.PeriodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private static final Logger logger = LoggerFactory.getLogger(LibraryService.class);
    private final EntityManager em;

    public LibraryService(EntityManager em) {
        this.em = em;
    }

    // Generate Random Data
    public void generateSampleData() {
        Faker faker = new Faker();
        try {
            em.getTransaction().begin();

            // Generate Users
            for (int i = 0; i < 10; i++) {
                User user = new User(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        faker.number().randomNumber(8, true)
                );
                em.persist(user);
            }

            // Generate Authors
            List<Author> authors = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Author author = new Author(
                        faker.name().firstName(),
                        faker.name().lastName(),
                        faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                );
                em.persist(author);
                authors.add(author);
            }

            // Generate books and assign authors
            for (int i = 0; i < 15; i++) {
                Books book = new Books(
                        faker.number().randomNumber(10, true),
                        faker.book().title()

                );
                book.setPublishYear(faker.number().numberBetween(1900,2026));
                book.setNumberOfPages(faker.number().numberBetween(100,700));
                book.setGenre(faker.options().option(GenreType.class));


                int numberOfAuthors = faker.number().numberBetween(1, 3);
                for (int j = 0; j < numberOfAuthors; j++) {
                    Author randomAuthor = authors.get(faker.number().numberBetween(0, authors.size()));
                    book.addAuthor(randomAuthor);
                }

                em.persist(book);
            }


            // Generate Magazines
            for (int i = 0; i < 5; i++) {
        Magazine magazine =
            new Magazine(
                faker.number().randomNumber(10, true),
                faker.book().publisher(),
                faker.options().option(PeriodType.class));
                magazine.setPublishYear(faker.number().numberBetween(1900,2026));
                magazine.setNumberOfPages(faker.number().numberBetween(20,200));
                em.persist(magazine);
            }

            // Generate Borrows
            List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
            List<Library> items = em.createQuery("SELECT l FROM Library l", Library.class).getResultList();

            for (int i = 0; i < 10; i++) {
                User user = users.get(faker.number().numberBetween(0, users.size()));
                Library item = items.get(faker.number().numberBetween(0, items.size()));

                LocalDate borrowDate = LocalDate.now().minusDays(faker.number().numberBetween(1, 30));
                Borrows borrow = new Borrows(user, item, borrowDate);

                if (faker.bool().bool()) {
                    borrow.setReturnDate(borrowDate.plusDays(faker.number().numberBetween(1, 30)));
                }

                em.persist(borrow);
            }

            em.getTransaction().commit();
            logger.info("Sample data and borrows generated successfully!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while generating sample data", e);
            throw new RuntimeException("An error occurred while generating sample data.");
        }
    }

    // Save user
    public void savePerson(Person person) {
        try {
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            logger.info("Successfully saved person: {}", person);
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving person: {}", person, e);
            throw new RuntimeException("An error occurred while saving the person.");
        }
    }

    // Save library item
    public void saveLibraryItem(Library item) {
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
            logger.info("Successfully saved library item: {}", item);
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while saving library item: {}", item, e);
            throw new RuntimeException("An error occurred while saving the library item.");
        }
    }

    // Register Borrow item
    public void borrowItem(User user, Library item) {
        if (user == null || item == null) {
            throw new IllegalArgumentException("User or Library item not found.");
        }

        try {
            em.getTransaction().begin();
            Borrows borrow = new Borrows(user, item, LocalDate.now());
            em.persist(borrow);
            em.getTransaction().commit();
            logger.info("Successfully borrowed item: {} by user: {}", item.getTitle(), user.getName());
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while borrowing item: {} by user: {}", item != null ? item.getTitle() : "null", user != null ? user.getName() : "null", e);
            throw new RuntimeException("An error occurred while borrowing the item.");
        }
    }

    // Register return borrow item
    public void returnItem(long borrowId) {
        try {
            em.getTransaction().begin();
            Borrows borrow = em.find(Borrows.class, borrowId);
            if (borrow != null && borrow.getReturnDate() == null) {
                borrow.setReturnDate(LocalDate.now());
                em.merge(borrow);
                logger.info("Successfully returned item: {}", borrow.getBorrowItem().getTitle());
            } else {
                logger.warn("Borrow record not found or item already returned for borrow ID: {}", borrowId);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while returning item for borrow ID: {}", borrowId, e);
            throw new RuntimeException("An error occurred while returning the item.");
        }
    }

    // Search by ISBN
    public Library searchByISBN(long isbn) {
        TypedQuery<Library> query = em.createQuery(
                "SELECT l FROM Library l WHERE l.isbnCode = :isbn", Library.class);
        query.setParameter("isbn", isbn);
        return query.getResultStream().findFirst().orElse(null);
    }

    // Delete by ISBN
    public void deleteByISBN(long isbn) {
        try {
            em.getTransaction().begin();
            Library item = searchByISBN(isbn);
            if (item != null) {
                em.remove(em.contains(item) ? item : em.merge(item));
                logger.info("Successfully deleted item with ISBN: {}", isbn);
            } else {
                logger.warn("Item with ISBN {} not found for deletion.", isbn);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            logger.error("Error while deleting item with ISBN: {}", isbn, e);
            throw new RuntimeException("An error occurred while deleting the item.");
        }
    }

    //Search by publish year
    public List<Library> getItemsByPublicationYear(int year) {
        TypedQuery<Library> query = em.createQuery(
                "SELECT l FROM Library l WHERE l.publishYear = :year", Library.class);
        query.setParameter("year", year);
        return query.getResultList();
    }

    // All users
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    //All Authors
    public List<Author> getAllAuthors() {
        return em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
    }

    // All books
    public List<Books> getAllBooks() {
        return em.createQuery("SELECT b FROM Books b", Books.class).getResultList();
    }

    // All magazines
    public List<Magazine> getAllMagazines() {
        return em.createQuery("SELECT m FROM Magazine m", Magazine.class).getResultList();
    }

    // Search by title
    public List<Library> searchByTitle(String titlePart) {
        TypedQuery<Library> query = em.createQuery(
                "SELECT l FROM Library l WHERE l.title LIKE :titlePart", Library.class);
        query.setParameter("titlePart", "%" + titlePart + "%");
        return query.getResultList();
    }

    // Search by author
    public List<Books> searchBooksByAuthorName(String name, String surname) {
        TypedQuery<Books> query = em.createQuery(
                "SELECT b FROM Books b JOIN b.authors a WHERE a.name = :name AND a.surname = :surname", Books.class);
        query.setParameter("name", name);
        query.setParameter("surname", surname);
        return query.getResultList();
    }

    // Active borrows
    public List<Borrows> getActiveBorrowsByUser(long cardNumber) {
        TypedQuery<Borrows> query = em.createQuery(
                "SELECT b FROM Borrows b WHERE b.user.cardNumber = :cardNumber AND b.returnDate IS NULL", Borrows.class);
        query.setParameter("cardNumber", cardNumber);
        return query.getResultList();
    }

    // Lated borrows
    public List<Borrows> getOverdueBorrows() {
        TypedQuery<Borrows> query = em.createQuery(
                "SELECT b FROM Borrows b WHERE b.expectedReturnDate < CURRENT_DATE AND b.returnDate IS NULL", Borrows.class);
        return query.getResultList();
    }
}
