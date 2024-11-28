package persistence;

import model.Library;
import model.User;
import model.UserDatabase;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Writes the data to the JSON file
public class JsonWriter {

    private static final int TAB = 4;
    private PrintWriter writerUsers;
    private PrintWriter writerLibrary;
    private final String destination1;
    private final String destination2;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination1, String destination2) {

        this.destination1 = destination1;
        this.destination2 = destination2;

    }

    // MODIFIES: this
    // EFFECTS: opens writer for User; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void openUser() throws FileNotFoundException {

        writerUsers = new PrintWriter(new File(destination1));

    }

    // MODIFIES: this
    // EFFECTS: opens writer for Library; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void openLibrary() throws FileNotFoundException {

        writerLibrary = new PrintWriter(new File(destination2));

    }

    // MODIFIES: this
    // EFFECTS: writes the JSON representation of the provided user to the destination file
    public void writeUser(User user) {

        JSONObject json = user.toJson();
        saveToFileUsers(json.toString(TAB));

    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void closeUsers() {
        writerUsers.close();
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void closeLibrary() {
        writerLibrary.close();
    }


    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFileUsers(String json) {
        writerUsers.append(json);
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFileLibrary(String json) {
        writerLibrary.append(json);
    }

    // MODIFIES: this
    // EFFECTS: writes the JSON representation of the library to the destination file
    public void writeLibrary(Library library) {

        JSONObject json = library.toJson();
        saveToFileLibrary(json.toString(TAB));

    }

    // MODIFIES: this
    // EFFECTS: writes the JSON representation of all the Users to the destination file
    public void writeUsers(UserDatabase users) {
        JSONObject jsonObject = users.toJson();
        saveToFileUsers(jsonObject.toString(TAB));
    }
}