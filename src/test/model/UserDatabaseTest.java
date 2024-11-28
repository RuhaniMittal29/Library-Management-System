package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserDatabaseTest {

    User user1;
    User user2;
    User user3;
    Book book1;
    UserDatabase userDatabase;

    @BeforeEach
    void runBefore() {

        user1 = new User("Jake", "jakeperalta@gmail.com");
        user2 = new User("Rosa", "rosadiaz@gmail.com");
        user3 = new User("Scully", "scully@gmail.com");
        userDatabase = new UserDatabase();
        userDatabase.addUser(user1);
        userDatabase.addUser(user2);

        List<String> author1 = new ArrayList<>();
        author1.add("Nikita Singh");
        author1.add("Durjoy Dutta");
        book1 = new Book("Someone Like You", author1, "Teenage Romance", 1);


    }

    @Test
    void testConstructor() {

        user1.borrowBookByUser("Someone like you", "Nikita singh");
        assertEquals("Jake", userDatabase.getAllUsers().get(0).getName());
        assertEquals("jakeperalta@gmail.com", userDatabase.getAllUsers().get(0).getEmail());
        assertEquals(user1, userDatabase.getAllUsers().get(0));

        assertEquals(user2, userDatabase.getAllUsers().get(1));
    }

    @Test
    void testAddUser() {

        List<User> u1 = new ArrayList<>();
        u1.add(user1);
        u1.add(user2);
        assertEquals(u1, userDatabase.getAllUsers());

        u1.add(user3);
        userDatabase.addUser(user3);
        assertEquals(u1, userDatabase.getAllUsers());
    }

    @Test
    void testisUserPresent() {

        assertTrue(userDatabase.isUserPresent("Jake", "jakeperalta@gmail.com"));
        assertTrue(userDatabase.isUserPresent("Rosa", "rosadiaz@gmail.com"));
        userDatabase.addUser(user3);
        assertTrue(userDatabase.isUserPresent("Scully", "scully@gmail.com"));
        assertFalse(userDatabase.isUserPresent("abc", "abc@gmail.com"));
    }
}
