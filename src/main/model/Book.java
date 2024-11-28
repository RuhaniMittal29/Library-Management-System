package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.*;

// Represents a book with title, authors, genre, availability and its bookID
public class Book implements Writable {

    private final String title;
    private final List<String> authors;
    private final String genre;
    private boolean isAvailable;
    private final int bookID;


    // REQUIRES: authors list cannot be null
    // MODIFIES: constructs a Book with title, authors list, genre, bookId and initial availability status as "true"
    public Book(String title, List<String> authors, String genre, int bookID) {

        this.title = title;
        this.authors = authors;
        this.genre = genre;
        this.bookID = bookID;
        this.isAvailable = true; // Initially available

    }

    // EFFECTS: returns true if the book author == authorName
    public boolean searchByAuthor(String authorName) {

        for (String author : authors) {
            if (author.equalsIgnoreCase(authorName)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: returns true if the book title == titleName
    public Boolean searchByTitle(String titleName) {

        return (title.equalsIgnoreCase(titleName.toLowerCase()));

    }

    // EFFECTS: returns authors' names (semicolon separated String)
    public String printAuthorName() {

        StringBuilder a = new StringBuilder();
        for (String author : authors) {
            a.append(author).append("; ");
        }
        return a.toString();

    }

    // EFFECTS: Get title, genre, bookID and availability of a book.
    public String getTitle() {

        return title;

    }

    public List<String> getAuthors() {

        return authors;

    }

    public String getGenre() {

        return genre;

    }

    public int getBookID() {

        return bookID;

    }

    public boolean getIsAvailable() {

        return isAvailable;

    }


    // EFFECTS: Set availability of book to 'b'
    public void setIsAvailable(Boolean b) {

        this.isAvailable = b;

    }

    @Override
    public JSONObject toJson() {

        JSONObject json = new JSONObject();
        json.put("title", title);

        // returns list of authors as JSONArray
        JSONArray authorsArray = new JSONArray();
        for (String author : authors) {
            authorsArray.put(author);

        }

        json.put("authors", authorsArray);
        json.put("genre", genre);
        json.put("bookID", bookID);
        json.put("isAvailable", isAvailable);
        return json;

    }
}