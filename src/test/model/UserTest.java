package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    User user1;
    User user2;
    User user3;

    Book book1;
    Book book2;
    Book book3;
    Book book4;
    Book book5;
    Library library;

    @BeforeEach
    void runBefore() {

        user1 = new User("Jake", "jakeperalta@gmail.com");
        user2 = new User("Rosa", "rosadiaz@gmail.com");
        user3 = new User("Scully", "scully@gmail.com");

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

        List<String> author4 = new ArrayList<>();
        author4.add("Jane Austen");
        book4 = new Book("Pride and Prejudice", author4, "Domestic Fiction", 4);

        book5 = new Book("Macbeth", author2, "Tragedy", 5);
        library = new Library();

        library.addBook(book1);
        book2.setIsAvailable(false);   // changed the availability status
        library.addBook(book2);
        library.addBook(book3);
        library.addBook(book4);
        library.addBook(book5);
    }

    @Test
    void testConstructor() {

        assertEquals("Jake", user1.getName());
        assertEquals("jakeperalta@gmail.com", user1.getEmail());
        user1.borrowBookByUser("Someone like you", "Nikita Singh");

        assertTrue(user1.getBorrowedBooks().contains(book1));
        assertFalse(user1.getBorrowedBooks().contains(book2));

    }

    @Test
    void testBorrowBook() {

        assertFalse(user1.getBorrowedBooks().contains(book1));
        assertTrue(user1.borrowBookByUser("Someone Like You", "Nikita Singh"));
        assertTrue(user1.getBorrowedBooks().contains(book1));
        assertEquals(book1, user1.getBorrowedBooks().get(0));
        assertFalse(user1.getBorrowedBooks().contains(book3));
        assertTrue(library.getUnavailableBooks().contains(book1));
        assertFalse(library.getAvailableBooks().contains(book1));
        assertFalse(book1.getIsAvailable());


        //ALREADY BORROWED
        assertFalse(user1.borrowBookByUser("Someone Like You", "Nikita Singh"));
        assertFalse(user3.borrowBookByUser("Someone Like You", "Nikita Singh"));
        assertTrue(user3.borrowBookByUser("Pride and prejudice", "Jane Austen"));
        assertTrue(user3.getBorrowedBooks().contains(book4));


        // ONLY TRUE WHEN TITLE AND AUTHOR BOTH ARE EQUAL
        assertFalse(user2.borrowBookByUser("othello", "William Shakespeare"));
        assertFalse(user2.borrowBookByUser("othello", "J. K. Rowling"));
        assertFalse(user2.borrowBookByUser("Room on the roof", "J. K. Rowling"));
        assertTrue(user2.borrowBookByUser("Macbeth", "William Shakespeare"));

        // CHECK IF THEY HAVE BEEN ADDED TO BORROWED BOOKS LIST
        assertTrue(user1.borrowBookByUser("room on the roof", "Ruskin Bond"));
        assertEquals(2, user1.getBorrowedBooks().size());
        assertTrue(user1.getBorrowedBooks().contains(book3));
        assertEquals(book3, user1.getBorrowedBooks().get(1));
        assertTrue(user1.getBorrowedBooks().contains(book1));

    }

    @Test
    void testReturnBook() {

        // BORROWED BY SOMEONE ELSE (NOT THE USER)
        assertFalse(user1.returnBookByUser("Hamlet", "William Shakespeare"));

        assertTrue(user1.borrowBookByUser("Macbeth", "William Shakespeare"));
        assertFalse(library.getAvailableBooks().contains(book5));
        assertTrue(user1.returnBookByUser("Macbeth", "William Shakespeare"));
        assertFalse(user1.getBorrowedBooks().contains(book5));
        assertFalse(library.getUnavailableBooks().contains(book5));
        assertTrue(library.getAvailableBooks().contains(book5));
        assertTrue(book5.getIsAvailable());

        //CANNOT RETURN SINCE IT HASN'T BEEN BORROWED
        assertFalse(user3.returnBookByUser("Someone like you", "Nikita Singh"));

        assertTrue(user3.borrowBookByUser("Someone like you", "Durjoy Dutta"));

        // ONLY TRUE WHEN TITLE AND AUTHOR BOTH ARE EQUAL
        assertFalse(user3.returnBookByUser("Someone like you", "William Shakespeare"));
        assertFalse(user3.returnBookByUser("othello", "Nikita Singh"));
        assertFalse(user3.returnBookByUser("othello", "William Shakespeare"));
        assertTrue(user3.returnBookByUser("Someone like you", "Durjoy Dutta"));

        //CAN'T RETURN BECAUSE BORROWED BY SOMEONE ELSE
        assertTrue(user1.borrowBookByUser("Pride and Prejudice", "Jane Austen"));
        assertTrue(user1.getBorrowedBooks().contains(book4));
        assertFalse(user3.returnBookByUser("Pride and Prejudice", "Jane Austen"));
        assertFalse(user3.getBorrowedBooks().contains(book4));

    }

    @Test
    void testUpdate() {

        user1.setName("Jake Peralta");
        user1.setEmail("jake.p@gmail.com");
        assertEquals("Jake Peralta", user1.getName());
        assertEquals("jake.p@gmail.com", user1.getEmail());

    }

    @Test
    public void testToJson() {

        JSONObject json = user1.toJson();

        assertTrue(json.has("name"));
        assertTrue(json.has("email"));
        assertTrue(json.has("borrowedBooks"));

        assertEquals("Jake", json.getString("name"));
        assertEquals("jakeperalta@gmail.com", json.getString("email"));

        JSONArray borrowedBooksJson = json.getJSONArray("borrowedBooks");
        assertEquals(0, borrowedBooksJson.length());

    }

    @Test
    public void testBorrowedBooksToJson() {

        JSONArray jsonArray = user1.borrowedBooksToJson();
        assertEquals(0, jsonArray.length());

        user1.getBorrowedBooks().add(book1);
        user1.getBorrowedBooks().add(book2);

        jsonArray = user1.borrowedBooksToJson();
        assertEquals(2, jsonArray.length());

        JSONObject bookJson1 = jsonArray.getJSONObject(0);
        JSONObject bookJson2 = jsonArray.getJSONObject(1);

        assertEquals("Someone Like You", bookJson1.getString("title"));
        assertEquals("Nikita Singh", bookJson1.getJSONArray("authors").getString(0));
        assertEquals("Teenage Romance", bookJson1.getString("genre"));
        assertEquals(1, bookJson1.getInt("bookID"));

        assertEquals("Hamlet", bookJson2.getString("title"));
    }

    @Test
    void testSetBorrowedBook() {

        user1.borrowBookByUser("Someone like you", "Nikita Singh");
        assertEquals(book1, user1.getBorrowedBooks().get(0));

        List<Book> lib = new ArrayList<>();
        lib.add(book1);
        lib.add(book2);
        lib.add(book3);

        user1.setBorrowedBooks(lib);
        assertEquals(lib, user1.getBorrowedBooks());
        assertEquals(book1, user1.getBorrowedBooks().get(0));
        assertEquals("Someone Like You", user1.getBorrowedBooks().get(0).getTitle());
        assertEquals(book2, user1.getBorrowedBooks().get(1));
        assertEquals(book3, user1.getBorrowedBooks().get(2));

    }
}