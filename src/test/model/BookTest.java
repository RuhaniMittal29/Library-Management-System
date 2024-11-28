package model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    Book book1;
    Book book2;
    Book book3;
    Book book4;

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

        List<String> author4 = new ArrayList<>();
        author4.add("Jane Austen");
        book4 = new Book("Pride and Prejudice", author4 , "Domestic Fiction", 4);
    }

    @Test
    void TestConstructor() {

        assertEquals("Someone Like You", book1.getTitle());
        assertEquals(2, book1.getAuthors().size());
        assertEquals("Nikita Singh", book1.getAuthors().get(0));
        assertEquals("Durjoy Dutta", book1.getAuthors().get(1));
        assertEquals("Teenage Romance", book1.getGenre());
        assertEquals(1, book1.getBookID());
        assertTrue(book1.getIsAvailable());

        book1.setIsAvailable(false);
        assertFalse(book1.getIsAvailable());

    }

    @Test
    void TestSearchByAuthor() {

        assertTrue(book1.searchByAuthor("Durjoy Dutta"));
        assertFalse(book3.searchByAuthor("William Shakespeare"));
        assertFalse(book4.searchByAuthor("Ruskin Bond"));

    }

    @Test
    void TestSearchBytitle() {

        assertTrue(book1.searchByTitle("someone like you"));
        assertFalse(book2.searchByTitle("othello"));
        assertTrue(book3.searchByTitle("Room on the Roof"));

    }

    @Test
    void TestPrintAuthor() {

        assertEquals("Nikita Singh; Durjoy Dutta; ", book1.printAuthorName());
        assertEquals("Ruskin Bond; ", book3.printAuthorName());

    }

    @Test
    public void testToJson() {
        JSONObject json = book1.toJson();

        // Verify the JSON object contains the expected fields and values
        assertEquals("Someone Like You", json.getString("title"));

        JSONArray authorsArray = json.getJSONArray("authors");
        assertEquals("Nikita Singh", authorsArray.getString(0));
        assertEquals("Durjoy Dutta", authorsArray.getString(1));

        assertEquals("Teenage Romance", json.getString("genre"));
        assertEquals(1, json.getInt("bookID"));
        assertEquals(true, json.getBoolean("isAvailable"));
    }


}