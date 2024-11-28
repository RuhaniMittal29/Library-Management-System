package persistence;

import model.Book;
import model.Library;
import model.User;
import model.UserDatabase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// Reads the data to the JSON file
public class JsonReader {

    private final String source1;
    private final String source2;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source1, String source2) {

        this.source1 = source1;
        this.source2 = source2;

    }

    // EFFECTS: reads the data of the user from the source file and returns it
    public User readUser() throws IOException {

        String jsonData = readFile(source1);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseUser(jsonObject);

    }

    // EFFECTS: reads source file as string and returns it
    public String readFile(String source) throws IOException {

        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));

        }
        return contentBuilder.toString();

    }

    // EFFECTS: parses user from JSON object and returns it
    public User parseUser(JSONObject jsonObject) {

        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        User user = new User(name, email);

        JSONArray borrowedBooksJson = jsonObject.getJSONArray("borrowedBooks");
        for (int i = 0; i < borrowedBooksJson.length(); i++) {
            JSONObject bookJson = borrowedBooksJson.getJSONObject(i);
            Book book = createBookFromJson(bookJson);
            user.getBorrowedBooks().add(book);

        }

        return user;
    }

    // EFFECTS: creates a book object with its fields
    public Book createBookFromJson(JSONObject bookJson) {

        String title = bookJson.getString("title");
        String genre = bookJson.getString("genre");
        int bookID = bookJson.getInt("bookID");

        JSONArray authorsArray = bookJson.getJSONArray("authors");
        List<String> authors = new ArrayList<>();
        for (int i = 0; i < authorsArray.length(); i++) {
            authors.add(authorsArray.getString(i));

        }

        Boolean isAvailable = bookJson.getBoolean("isAvailable");
        Book b = new Book(title, authors, genre, bookID);
        b.setIsAvailable(isAvailable);
        return b;

    }

    // EFFECTS: reads the data of the library from the source file and returns it
    public Library readLibrary() throws IOException {

        String jsonData = readFile(source2);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLibrary(jsonObject);

    }

    // EFFECTS: parses library from JSON object and returns it
    private Library parseLibrary(JSONObject jsonObject) {

        Library library = new Library();

        JSONArray allBooksJson = jsonObject.getJSONArray("allBooks");
        for (int i = 0; i < allBooksJson.length(); i++) {
            JSONObject bookJson = allBooksJson.getJSONObject(i);
            Book book = createBookFromJson(bookJson);
            library.getAllBooks().add(book);

        }

        JSONArray availableBooksJson = jsonObject.getJSONArray("availableBooks");
        for (int i = 0; i < availableBooksJson.length(); i++) {
            JSONObject bookJson = availableBooksJson.getJSONObject(i);
            Book book = createBookFromJson(bookJson);
            library.getAvailableBooks().add(book);

        }

        JSONArray unavailableBooksJson = jsonObject.getJSONArray("unavailableBooks");
        for (int i = 0; i < unavailableBooksJson.length(); i++) {
            JSONObject bookJson = unavailableBooksJson.getJSONObject(i);
            Book book = createBookFromJson(bookJson);
            library.getUnavailableBooks().add(book);

        }

        return library;
    }

    // EFFECTS: parses userDatabase from JSON object and returns it
    private UserDatabase parseUserDatabase(JSONObject jsonObject) {

        UserDatabase userDatabase = new UserDatabase();

        JSONArray userDatabaseJson = jsonObject.getJSONArray("userDatabase");
        for (int i = 0; i < userDatabaseJson.length(); i++) {
            JSONObject userJson = userDatabaseJson.getJSONObject(i);
            User user = parseUser(userJson);
            userDatabase.getAllUsers().add(user);

        }

        return userDatabase;
    }


    // EFFECTS: parses userDatabase from JSON object and returns it
    public UserDatabase readUsers() throws IOException {
        String jsonData = readFile(source1);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseUserDatabase(jsonObject);

    }

}