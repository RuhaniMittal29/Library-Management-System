package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Represents a library storing a collection of Books
public class Library implements Writable {

    private static List<Book> allBooks = new ArrayList<>();
    private static List<Book> availableBooks = new ArrayList<>();
    private static List<Book> unavailableBooks = new ArrayList<>();

    // EFFECTS: constructs a library and initializes allBooks, availableBooks and unavailableBooks as an empty list.
    public Library() {

        allBooks = new ArrayList<>();
        availableBooks = new ArrayList<>();
        unavailableBooks = new ArrayList<>();

    }

    // MODIFIES: this
    // EFFECTS: adds book to the list of all books and depending on their availability
    //          add them to available books or unavailable books.
    public void addBook(Book b) {

        allBooks.add(b);
        if (!b.getIsAvailable()) {
            unavailableBooks.add(b);
        } else {
            availableBooks.add(b);

        }
    }

    // MODIFIES: this, b.isAvailable
    // EFFECTS: if the book is Available, set b.isAvailable = false; remove book from
    //          available books list and add them to unavailable books list.
    public void borrowBook(Book b) {

        if (b.getIsAvailable()) {
            b.setIsAvailable(false);
            availableBooks.remove(b);
            unavailableBooks.add(b);

        }
    }

    // MODIFIES: this, b.isAvailable
    // EFFECTS: if the book is not Available, set b.isAvailable = true; remove book from
    //          unavailable books list and add them to available books list.
    public void returnBook(Book b) {

        if (!b.getIsAvailable()) {
            b.setIsAvailable(true);
            unavailableBooks.remove(b);
            availableBooks.add(b);

        }
    }

    // EFFECTS: Get list of all the books, available and unavailable books in the library.
    public List<Book> getAvailableBooks() {
        return availableBooks;

    }

    public List<Book> getUnavailableBooks() {
        return unavailableBooks;

    }

    public List<Book> getAllBooks() {
        return allBooks;
    }

    @Override
    public JSONObject toJson() {

        JSONObject json = new JSONObject();
        json.put("allBooks", allBooksToJson());
        json.put("availableBooks", availableBooksToJson());
        json.put("unavailableBooks", unavailableBooksToJson());
        return json;

    }

    // EFFECTS: returns the list of all books as a JSONArray
    private JSONArray allBooksToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Book b : allBooks) {
            jsonArray.put(b.toJson());

        }

        return jsonArray;
    }

    // EFFECTS: returns the list of available books as a JSONArray
    private JSONArray availableBooksToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Book b : availableBooks) {
            jsonArray.put(b.toJson());

        }

        return jsonArray;
    }

    // EFFECTS: returns the list of unavailable books as a JSONArray
    private JSONArray unavailableBooksToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Book b : unavailableBooks) {
            jsonArray.put(b.toJson());

        }

        return jsonArray;
    }

}