package persistence;

import org.json.JSONObject;

// creates a json object
public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
