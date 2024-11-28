package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Represents a List of Users in the form of {email: user}
public class UserDatabase implements Writable {

    private List<User> users;

    // EFFECTS: creates a list of Users
    public UserDatabase() {

        this.users = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a user to the list of users
    public void addUser(User user) {

        users.add(user);
    }

    // EFFECTS: checks if a user with "name" and "email" is present in the list and returns a boolean value
    public Boolean isUserPresent(String name, String email) {

        for (User u : users) {
            if ((u.getName().equals(name)) & (u.getEmail().equals(email))) {
                return true;

            }
        }

        return false;
    }

    // EFFECTS: returns the list of all the users
    public List<User> getAllUsers() {

        return users;
    }


    @Override
    public JSONObject toJson() {

        JSONObject json = new JSONObject();
        json.put("userDatabase", userDatabaseToJson());
        return json;

    }

    // EFFECTS: returns userDatabase as a JSONArray
    public JSONArray userDatabaseToJson() {

        JSONArray jsonArray = new JSONArray();

        for (User u : users) {
            jsonArray.put(u.toJson());
        }

        return jsonArray;
    }
}