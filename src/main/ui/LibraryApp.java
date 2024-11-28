package ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import model.User;
import model.Library;
import model.Book;
import model.UserDatabase;
import persistence.JsonReader;
import persistence.JsonWriter;

// creates the ui for the library app
public class LibraryApp {
    private static final String USER_JSON_STORE = "./data/user.json";
    private static final String LIBRARY_JSON_STORE = "./data/library.json";
    private final Scanner scanner;
    private Library library;
    private User user;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;
    private UserDatabase users;


    // EFFECTS: creates the ui for library app and saves and loads the previous data
    public LibraryApp() {
        scanner = new Scanner(System.in);
        jsonWriter = new JsonWriter(USER_JSON_STORE, LIBRARY_JSON_STORE);
        jsonReader = new JsonReader(USER_JSON_STORE, LIBRARY_JSON_STORE);
        users = new UserDatabase();
        //loadUsers();
        run();
    }

    // EFFECTS: asks the user to input name and email, & has a library created
    private void inputUser() {
        System.out.println("Enter the name of user: ");
        String userName = scanner.nextLine();
        System.out.println("Enter the email of user: ");
        String email = scanner.nextLine();
        user = new User(userName, email);
        loadUsers();
        //loadLibrary();


        if (users.isUserPresent(userName, email)) {
            System.out.println("Welcome back, " + userName + "!");
            updateUser();
        } else {
            User newUser = new User(userName, email);
            users.addUser(newUser);
            System.out.println("Welcome, " + userName + "!");
        }
    }

    void updateUser() {
        for (User user: users.getAllUsers()) {
            if (user.getName().equals(this.user.getName()) && user.getEmail().equals(this.user.getEmail())) {
                List<Book> userBook = user.getBorrowedBooks();
                this.user.setBorrowedBooks(userBook);
                return;
            }
        }
    }

    // EFFECTS: Creates a Library with some books
    private void createLib() {

        List<String> author1 = new ArrayList<>();
        author1.add("Nikita Singh");
        author1.add("Durjoy Dutta");
        Book book1 = new Book("Someone Like You", author1, "Teenage Romance", 1);

        List<String> author2 = new ArrayList<>();
        author2.add("William Shakespeare");
        Book book2 = new Book("Hamlet", author2, "Tragedy", 2);

        List<String> author3 = new ArrayList<>();
        author3.add("Ruskin Bond");
        Book book3 = new Book("Room on the Roof", author3, "Fiction", 3);
        book3.setIsAvailable(false);

        Book book4 = new Book("Othello", author2, "Tragedy", 4);

        Book book5 = new Book("Macbeth", author2, "Tragedy", 5);
        Book book6 = new Book("Mac", author2, "Tragedy", 5);

        library = new Library();
        library.addBook(book1);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        library.addBook(book5);
        library.addBook(book6);
    }

    // EFFECTS: asks the user to input their choice, and runs the program till
    //          choice == 6
    private void run() {
        //loadUsers();
        boolean keepGoing = true;
        int choice;

        inputUser();
        createLib();

        while (keepGoing) {
            System.out.println("\n=============WELCOME TO THE LIBRARY=============");
            displayChoices();
            System.out.println("Your choice:");
            choice = Integer.parseInt(scanner.nextLine());

            if (choice == 8) {
                keepGoing = false;
            } else {
                executeCommand(choice);
            }
        }
        //saveUsers();
        System.out.println("\n==================THANK YOU!!=================");
    }


    // EFFECTS: display the choices (along with their functions)
    private void displayChoices() {
        System.out.println("\nSelect an option:");
        System.out.println("1 -> Print book information");
        System.out.println("2 -> View all the books by specific genre");
        System.out.println("3 -> Borrow a book");
        System.out.println("4 -> Return a book");
        System.out.println("5 -> View User History (borrowed books)");
        System.out.println("6 -> Save to the file");
        System.out.println("7 -> Load the file");
        System.out.println("8 -> Exit");
    }

    // EFFECTS: execute functions based on the choices.
    private void executeCommand(int choice) {
        if (choice == 1) {
            printBookInfo(library, scanner);
        } else if (choice == 2) {
            viewAllBooks(library, scanner);
        } else if (choice == 3) {
            borrowBook(scanner, user);
        } else if (choice == 4) {
            returnBook(scanner, user);
        } else if (choice == 5) {
            viewBorrowedBook(user);
        } else if (choice == 6) {
            saveUsers();
            saveLibrary();
        } else if (choice == 7) {
            loadLibrary();
        } else {
            System.out.println("Invalid choice. Please try again.");
        }
    }

    // CHOICE- 1======================================================
    // EFFECTS: Print information of a book based on their titles
    private static void printBookInfo(Library library, Scanner scanner) {
        System.out.print("Enter the title of the book: ");
        String title = scanner.nextLine();

        boolean found = false;
        for (Book book : library.getAllBooks()) {
            if (book.searchByTitle(title)) {
                System.out.println("------------------------------------------------------");
                System.out.println("Title:" + book.getTitle());
                System.out.println("Author:" + book.printAuthorName());
                System.out.println("Genre:" + book.getGenre());
                System.out.println("Book-ID:" + book.getBookID());
                System.out.println("Availability Status:"
                        +
                        (book.getIsAvailable() ? "Available" : "Not Available"));
                System.out.println("------------------------------------------------------");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Book not found.");
        }
    }

    // CHOICE- 2======================================================
    // EFFECTS:View all the books' title, and author for a specific genre
    private static void viewAllBooks(Library library, Scanner scanner) {

        System.out.print("Enter the genre: ");
        String genre = scanner.nextLine();

        System.out.println("===================================================");
        for (Book b : library.getAllBooks()) {
            if (b.getGenre().equalsIgnoreCase(genre)) {
                System.out.println(b.getTitle() + ", by " + b.printAuthorName() + " (genre = " + b.getGenre() + ")");
            }
        }
    }

    // CHOICE- 3======================================================
    // EFFECTS: Let user borrow a book if its available
    private static void borrowBook(Scanner scanner, User user) {

        System.out.println("Enter the name of book: ");
        String bookName = scanner.nextLine();
        System.out.println("Enter the name of ANY ONE AUTHOR: ");
        String author = scanner.nextLine();

        if (user.borrowBookByUser(bookName, author)) {
            System.out.println("=================================================================================");
            System.out.println("The " + bookName + " by " + author + " has been successfully issued!");
        } else {
            System.out.println("=================================================================================");
            System.out.println("Sorry, book has already been issued.");
            System.out.println("Try something else!!");
        }
    }

    // CHOICE- 4======================================================
    // EFFECTS: return a book if it had been borrowed by the user
    private static void returnBook(Scanner scanner, User user) {

        System.out.println("Enter the name of book: ");
        String bookName = scanner.nextLine();
        System.out.println("Enter the name of ANY ONE AUTHOR: ");
        String author = scanner.nextLine();

        if (user.returnBookByUser(bookName, author)) {
            System.out.println("=================================================================================");
            System.out.println(bookName + " by " + author + " has been successfully returned!!");
        } else {
            System.out.println("=================================================================================");
            System.out.println("Sorry, " + bookName + " couldn't be returned.");
        }
    }

    // CHOICE- 5======================================================
    // EFFECTS: View list of borrowed books by a user
    private static void viewBorrowedBook(User user) {

        List<Book> borrowedBooks = user.getBorrowedBooks();
        System.out.println("=============================================================");
        System.out.println("THE BOOKS THAT HAVE BEEN BORROWED BY YOU:-");
        for (Book b : borrowedBooks) {
            System.out.println(b.getTitle() + " by " + b.printAuthorName());
        }
    }

    // CHOICE- 6======================================================
    // Save all users in the user database
    private void saveUsers() {
        try {
            jsonWriter.openUser();
            jsonWriter.writeUsers(users);
            jsonWriter.closeUsers();
            System.out.println("Saved users to " + USER_JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + USER_JSON_STORE);
        }
    }

    // EFFECTS: saves the library to file
    private void saveLibrary() {
        try {
            jsonWriter.openLibrary();
            jsonWriter.writeLibrary(library);
            jsonWriter.closeLibrary();
            System.out.println("Saved library to " + LIBRARY_JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write library to file: " + LIBRARY_JSON_STORE);
        }
    }


    // CHOICE- 7======================================================
    // MODIFIES: this
    // EFFECTS: Load all users into the user database
    private void loadUsers() {
        try {
            users = jsonReader.readUsers();
            if (users == null) {
                System.out.println("Empty user");
            } else if (users.getAllUsers().size() == 0) {
                System.out.println("Empty");
            } else {
                System.out.println("Has been loaded");
            }
        } catch (IOException e) {
            System.out.println("Unable to read users from file: " + USER_JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads library from file
    private void loadLibrary() {
        try {
            library = jsonReader.readLibrary();
            System.out.println("Library has been Loaded");
        } catch (IOException e) {
            System.out.println("Unable to read from file: ");
        }
    }


}