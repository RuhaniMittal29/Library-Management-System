package persistence;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import model.Book;
import model.Library;
import model.User;
import model.UserDatabase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {

        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json",
                    "./data/my\0illegal:fileName.json");
            writer.openUser();
            writer.openLibrary();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {

        try {
            User user = new User("Jake", "jake@gmail.com");

            Library library = new Library();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyUsers.json",
                    "./data/testWriterEmptyLibrary.json");
            writer.openUser();
            writer.openLibrary();
            writer.writeUser(user);
            writer.writeLibrary(library);
            writer.closeUsers();
            writer.closeLibrary();

            JsonReader reader = new JsonReader("./data/testWriterEmptyUsers.json",
                    "./data/testWriterEmptyLibrary.json");
            user = reader.readUser();
            assertEquals("Jake", user.getName());
            assertEquals("jake@gmail.com", user.getEmail());


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {

        try {

            List<String> author1 = new ArrayList<>();
            author1.add("Nikita Singh");
            author1.add("Durjoy Dutta");
            Book book1 = new Book("Someone Like You", author1, "Teenage Romance", 1);

            User user = new User("Jake", "jake@gmail.com");
            Library library = new Library();
            library.addBook(book1);
            user.borrowBookByUser("Someone Like You","Nikita Singh");

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralUsers.json",
                    "./data/testWriterGeneralLibrary.json");
            writer.openUser();
            writer.openLibrary();
            writer.writeUser(user);
            writer.writeLibrary(library);
            writer.closeUsers();
            writer.closeLibrary();

            JsonReader reader = new JsonReader("./data/testWriterGeneralUsers.json",
                    "./data/testWriterGeneralLibrary.json");
            user = reader.readUser();
            assertEquals("Jake", user.getName());
            assertEquals("jake@gmail.com", user.getEmail());
            assertEquals(1, user.getBorrowedBooks().size());

            library = reader.readLibrary();
            assertEquals(1, library.getAllBooks().size());
            assertEquals(0, library.getAvailableBooks().size());
            assertEquals(1, library.getUnavailableBooks().size());


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriteUsers() {
        try {

            UserDatabase users = new UserDatabase();
            users.addUser(new User("Alice", "alice@example.com"));
            users.addUser(new User("Bob", "bob@example.com"));

            JsonWriter writer = new JsonWriter("./data/testWriteUsers.json", "./data/testWriteUsers.json");
            writer.openUser();

            writer.writeUsers(users);

            writer.closeUsers();

            JsonReader reader = new JsonReader("./data/testWriteUsers.json", "./data/testWriteUsers.json");

            JSONObject json = new JSONObject(reader.readFile("./data/testWriteUsers.json"));
            UserDatabase readUsers = new UserDatabase();
            readUsers = reader.readUsers();

            User readUser1 = readUsers.getAllUsers().get(0);
            User readUser2 = readUsers.getAllUsers().get(1);

            assertNotNull(readUser1);
            assertNotNull(readUser2);

            assertEquals("Alice", readUser1.getName());
            assertEquals("alice@example.com", readUser1.getEmail());

            assertEquals("Bob", readUser2.getName());
            assertEquals("bob@example.com", readUser2.getEmail());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}