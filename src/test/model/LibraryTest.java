package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    Book book1;
    Book book2;
    Book book3;
    Book book4;
    Library library;

    @BeforeEach
    void runBefore() {

        List<String> author1 = new ArrayList<>();
        author1.add("Nikita Singh");
        author1.add("Durjoy Dutta");
        book1 = new Book("Someone Like You", author1, "Teenage Romance", 1);

        List<String> author2 = new ArrayList<>();
        author2.add("William Shakespeare");
        book2 = new Book("Hamlet", author2, "Tragedy", 2);

        List<String> author3 = new ArrayList<>();
        author3.add("Ruskin Bond");
        book3 = new Book("Room on the Roof", author3, "Fiction", 3);

        book4 = new Book("Othello", author2, "Tragedy", 4);

        library = new Library();

        library.addBook(book1);
        book2.setIsAvailable(false);
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);


    }

    @Test
    void TestConstructor() {

        assertEquals(4, library.getAllBooks().size());
        assertEquals(book1, library.getAllBooks().get(0));
        assertEquals(book2, library.getAllBooks().get(1));
        assertEquals(book3, library.getAllBooks().get(2));
        assertEquals(book4, library.getAllBooks().get(3));

        assertEquals(3, library.getAvailableBooks().size());
        assertFalse(library.getAvailableBooks().contains(book2));
        assertTrue(library.getAvailableBooks().contains(book1));

        assertEquals(1, library.getUnavailableBooks().size());
        assertEquals(book2, library.getUnavailableBooks().get(0));
        assertTrue(library.getUnavailableBooks().contains(book2));
        assertFalse(library.getUnavailableBooks().contains(book3));

    }

    @Test
    void TestBorrowBook() {

        assertFalse(library.getUnavailableBooks().contains(book1));
        assertTrue(library.getAvailableBooks().contains(book1));

        library.borrowBook(book1);

        // CHECK THE BOOKS IN THE LIST AVAILABLE BOOKS
        assertEquals(2, library.getAvailableBooks().size());
        assertFalse(library.getAvailableBooks().contains(book1));
        assertTrue(library.getAvailableBooks().contains(book3));
        assertEquals(2, library.getAvailableBooks().size());

        // CHECK THE BOOKS IN THE LIST UNAVAILABLE BOOKS
        assertEquals(2, library.getUnavailableBooks().size());
        assertTrue(library.getUnavailableBooks().contains(book1));
        assertFalse(library.getUnavailableBooks().contains(book3));

        //WILL REMAIN FALSE SINCE BOOK IS NOT AVAILABLE
        assertFalse(library.getAvailableBooks().contains(book2));
        library.borrowBook(book2);
        assertFalse(library.getAvailableBooks().contains(book2));

    }

    @Test
    void TestReturnBook() {

        assertFalse(library.getAvailableBooks().contains(book2));
        assertTrue(library.getUnavailableBooks().contains(book2));

        library.returnBook(book2);

        // // CHECK THE BOOKS IN THE LIST AVAILABLE BOOKS
        assertEquals(4, library.getAvailableBooks().size());
        assertEquals(book2, library.getAvailableBooks().get(3));
        assertTrue(library.getAvailableBooks().contains(book2));
        assertTrue(library.getAvailableBooks().contains(book1));

        // CHECK THE BOOKS IN THE LIST UNAVAILABLE BOOKS
        assertEquals(0, library.getUnavailableBooks().size());
        assertFalse(library.getUnavailableBooks().contains(book2));
        assertFalse(library.getUnavailableBooks().contains(book1));

        //WILL REMAIN TRUE SINCE BOOK IS AVAILABLE
        assertTrue(library.getAvailableBooks().contains(book4));
        library.returnBook(book4);
        assertTrue(library.getAvailableBooks().contains(book4));

    }

    @Test
    public void testToJson() {
        JSONObject json = library.toJson();

        // Verify the JSON object contains the expected fields and values
        assertTrue(json.has("allBooks"));
        assertTrue(json.has("availableBooks"));
        assertTrue(json.has("unavailableBooks"));

        JSONArray allBooksJson = json.getJSONArray("allBooks");
        JSONArray availableBooksJson = json.getJSONArray("availableBooks");
        JSONArray unavailableBooksJson = json.getJSONArray("unavailableBooks");

        assertEquals(4, allBooksJson.length());
        assertEquals(3, availableBooksJson.length());
        assertEquals(1, unavailableBooksJson.length());
    }
}
