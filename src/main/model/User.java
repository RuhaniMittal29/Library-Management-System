package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// represents a user with name, email and their borrowing history
public class User implements Writable {
    private String name;
    private String email;
    private List<Book> borrowedBooks;
    Library lib = new Library();


    // MODIFIES: constructs a user with name, email and initializes borrowedBooks as an empty list.
    public User(String name, String email) {

        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    // MODIFIES: this
    // EFFECTS: search book by Title and Author, borrow book and added it to the
    //          list of Borrowed books by author
    public boolean borrowBookByUser(String title, String author) {

        boolean valid = false;
        List<Book> available = lib.getAvailableBooks();
        for (Book b : available) {
            if (b.searchByTitle(title) && b.searchByAuthor(author)) {
                lib.borrowBook(b);
                this.borrowedBooks.add(b);
                valid = true;
                EventLog.getInstance().logEvent(new Event(String.format("Book with ID: %s borrowed by User: %s",
                        b.getBookID(), name)));
                break;

            }
        }
        return valid;
    }

    // MODIFIES: this
    // EFFECTS: search book by Title and Author, and return book and
    //          removed it from the list of borrowed books
    public Boolean returnBookByUser(String title, String author) {

        boolean valid = false;
        for (Book b : borrowedBooks) {
            if (b.searchByTitle(title) && b.searchByAuthor(author)) {
                lib.returnBook(b);
                this.borrowedBooks.remove(b);
                valid = true;
                EventLog.getInstance().logEvent(new Event(String.format("Book with ID: %s returned by User: %s",
                        b.getBookID(), name)));
                break;

            }
        }
        return valid;

    }

    // EFFECTS: Get list of Borrowed books, name and email
    public List<Book> getBorrowedBooks() {

        return borrowedBooks;

    }

    public String getName() {

        return name;

    }

    public String getEmail() {

        return email;

    }

    // MODIFIES: this
    // EFFECTS: update the name to newName
    public void setName(String newName) {

        this.name = newName;

    }

    // MODIFIES: this
    // EFFECTS: update the email to newEmail
    public void setEmail(String newEmail) {

        this.email = newEmail;

    }

    @Override
    public JSONObject toJson() {

        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("email", email);
        json.put("borrowedBooks", borrowedBooksToJson());
        return json;

    }

    // EFFECTS: returns borrowed books as a JSONArray
    public JSONArray borrowedBooksToJson() {

        JSONArray jsonArray = new JSONArray();

        for (Book b : borrowedBooks) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }
}