package org.example;

import org.example.entities.*;
import org.example.enumeration.PeriodType;
import org.example.service.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;
import java.util.List;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Library");
        EntityManager em = emf.createEntityManager();
        LibraryService libraryService = new LibraryService(em);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\nMain Menu:");
                System.out.println("1. Generate Data Automatically");
                System.out.println("2. Manage Data");
                System.out.println("3. Search");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                int mainChoice = scanner.nextInt();

                switch (mainChoice) {
                    case 1:
                        libraryService.generateSampleData();
                        System.out.println("Sample data generated successfully!");
                        break;
                    case 2:
                        manageData(scanner, libraryService, em);
                        break;
                    case 3:
                        searchMenu(scanner, libraryService);
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        em.close();
                        emf.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                logger.error("Error in main menu: ", e);
                System.out.println("An error occurred. Please check the logs for details.");
            }
        }
    }

    private static void manageData(Scanner scanner, LibraryService libraryService, EntityManager em) {
        while (true) {
            try {
                System.out.println("\nManage Data:");
                System.out.println("1. Add User");
                System.out.println("2. Add Book");
                System.out.println("3. Add Magazine");
                System.out.println("4. Add Author");
                System.out.println("5. Borrow Item");
                System.out.println("6. Return Item");
                System.out.println("7. Delete Item by ISBN");
                System.out.println("8. View All Users");
                System.out.println("9. View All Authors");
                System.out.println("10. View All Books");
                System.out.println("11. View All Magazines");
                System.out.println("0. Back to Main Menu");
                System.out.print("Enter your choice: ");
                int manageChoice = scanner.nextInt();

                switch (manageChoice) {
                    case 1:
                        System.out.print("Enter user name: ");
                        String userName = scanner.next();
                        System.out.print("Enter user surname: ");
                        String userSurname = scanner.next();
                        System.out.print("Enter user card number: ");
                        long cardNumber = scanner.nextLong();
                        User user = new User(userName, userSurname, null, cardNumber);
                        libraryService.savePerson(user);
                        break;
                    case 2:
                        System.out.print("Enter book title: ");
                        String bookTitle = scanner.next();
                        System.out.print("Enter book ISBN: ");
                        long bookIsbn = scanner.nextLong();
                        Books book = new Books(bookIsbn, bookTitle);
                        libraryService.saveLibraryItem(book);
                        break;
                    case 3:
                        System.out.print("Enter magazine title: ");
                        String magazineTitle = scanner.next();
                        System.out.print("Enter magazine ISBN: ");
                        long magazineIsbn = scanner.nextLong();
                        Magazine magazine = new Magazine(magazineIsbn, magazineTitle, PeriodType.MONTHLY);
                        libraryService.saveLibraryItem(magazine);
                        break;
                    case 4:
                        System.out.print("Enter author name: ");
                        String authorName = scanner.next();
                        System.out.print("Enter author surname: ");
                        String authorSurname = scanner.next();
                        Author author = new Author(authorName, authorSurname, null);
                        libraryService.savePerson(author);
                        break;
                    case 5:
                        System.out.print("Enter user ID: ");
                        long userId = scanner.nextLong();
                        System.out.print("Enter item ID: ");
                        long itemId = scanner.nextLong();
                        User borrowingUser = em.find(User.class, userId);
                        Library borrowingItem = em.find(Library.class, itemId);
                        if (borrowingUser == null || borrowingItem == null) {
                            System.out.println("Error: User or item not found.");
                        } else {
                            libraryService.borrowItem(borrowingUser, borrowingItem);
                            System.out.println("Item borrowed successfully!");
                        }
                        break;
                    case 6:
                        System.out.print("Enter borrow ID: ");
                        long borrowId = scanner.nextLong();
                        libraryService.returnItem(borrowId);
                        System.out.println("Item returned successfully!");
                        break;
                    case 7:
                        System.out.print("Enter ISBN to delete: ");
                        long isbnToDelete = scanner.nextLong();
                        libraryService.deleteByISBN(isbnToDelete);
                        System.out.println("Item deleted successfully (if it existed).");
                        break;
                    case 8:
                        List<User> allUsers = libraryService.getAllUsers();
                        if (!allUsers.isEmpty()) {
                            allUsers.forEach(System.out::println);
                        } else {
                            System.out.println("No users found.");
                        }
                        break;
                    case 9:
                        List<Author> allAuthors = libraryService.getAllAuthors();
                        if (!allAuthors.isEmpty()) {
                            allAuthors.forEach(System.out::println);
                        } else {
                            System.out.println("No authors found.");
                        }
                        break;
                    case 10:
                        List<Books> allBooks = libraryService.getAllBooks();
                        if (!allBooks.isEmpty()) {
                            allBooks.forEach(System.out::println);
                        } else {
                            System.out.println("No books found.");
                        }
                        break;
                    case 11:
                        List<Magazine> allMagazines = libraryService.getAllMagazines();
                        if (!allMagazines.isEmpty()) {
                            allMagazines.forEach(System.out::println);
                        } else {
                            System.out.println("No magazines found.");
                        }
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                logger.error("Error in manage data menu: ", e);
                System.out.println("An error occurred. Please check the logs for details.");
            }
        }
    }


    private static void searchMenu(Scanner scanner, LibraryService libraryService) {
        while (true) {
            try {
                System.out.println("\nSearch Menu:");
                System.out.println("1. Search by Title");
                System.out.println("2. Search Books by Author");
                System.out.println("3. Active Borrows by User");
                System.out.println("4. Overdue Borrows");
                System.out.println("5. Search Item by ISBN");
                System.out.println("6. View Items by Publication Year");
                System.out.println("0. Back to Main Menu");
                System.out.print("Enter your choice: ");
                int searchChoice = scanner.nextInt();

                switch (searchChoice) {
                    case 1:
                        System.out.print("Enter part of the title: ");
                        String titlePart = scanner.next();
                        libraryService.searchByTitle(titlePart)
                                .forEach(item -> System.out.println(item.getTitle()));
                        break;
                    case 2:
                        System.out.print("Enter author name: ");
                        String authorName = scanner.next();
                        System.out.print("Enter author surname: ");
                        String authorSurname = scanner.next();
                        libraryService.searchBooksByAuthorName(authorName, authorSurname)
                                .forEach(book -> System.out.println(book.getTitle()));
                        break;
                    case 3:
                        System.out.print("Enter user card number: ");
                        long cardNumber = scanner.nextLong();
                        libraryService.getActiveBorrowsByUser(cardNumber)
                                .forEach(borrow -> System.out.println(borrow.getBorrowItem().getTitle()));
                        break;
                    case 4:
                        libraryService.getOverdueBorrows()
                                .forEach(borrow -> System.out.println(borrow.getBorrowItem().getTitle()));
                        break;
                    case 5:
                        System.out.print("Enter ISBN: ");
                        long isbnToSearch = scanner.nextLong();
                        Library foundItem = libraryService.searchByISBN(isbnToSearch);
                        if (foundItem != null) {
                            System.out.println("Found Item: " + foundItem);
                        } else {
                            System.out.println("No item found with ISBN: " + isbnToSearch);
                        }
                        break;
                    case 6:
                        System.out.print("Enter publication year: ");
                        int year = scanner.nextInt();
                        List<Library> itemsByYear = libraryService.getItemsByPublicationYear(year);
                        if (!itemsByYear.isEmpty()) {
                            itemsByYear.forEach(System.out::println);
                        } else {
                            System.out.println("No items found for the year: " + year);
                        }
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            } catch (Exception e) {
                logger.error("Error in search menu: ", e);
                System.out.println("An error occurred. Please check the logs for details.");
            }
        }
    }
}
