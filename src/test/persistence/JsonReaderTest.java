package persistence;

import model.Library;
import model.User;
import model.UserDatabase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json",
                "./data/noSuchFile.json");
        try {
            User user = reader.readUser();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWorkRoom.json",
                "./data/testReaderEmptyWorkRoom.json");
        try {
            User user = reader.readUser();
            Library library = reader.readLibrary();
            assertEquals("Jake", user.getName());
            assertEquals("jake@gmail.com", user.getEmail());
        } catch (IOException e) {
            //fail("Couldn't read from file");
        }
    }


    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralUsers.json",
                "./data/testReaderGeneralLibrary.json");
        try {
            UserDatabase loadedUsers = reader.readUsers();
            User user = loadedUsers.getAllUsers().get(0);
            assertEquals("Jake", user.getName());
            assertEquals("jake@gmail.com", user.getEmail());

            Library library = reader.readLibrary();
            assertEquals(1, library.getAllBooks().size());
            assertEquals(0, library.getAvailableBooks().size());
            assertEquals(1, library.getUnavailableBooks().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom2() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralUsers.json",
                "./data/testReaderGeneralLibrary2.json");
        try {
            UserDatabase loadUsers = reader.readUsers();
            User user = loadUsers.getAllUsers().get(0);
            assertEquals("Jake", user.getName());
            assertEquals("jake@gmail.com", user.getEmail());

            Library library = reader.readLibrary();
            assertEquals(1, library.getAllBooks().size());
            assertEquals(1, library.getAvailableBooks().size());
            assertEquals(1, library.getUnavailableBooks().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}