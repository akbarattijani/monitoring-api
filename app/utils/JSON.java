package utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by AKBAR on 01/10/2016.
 */
public class JSON {

    public static JSONObject convertToObject(JsonNode json) throws ParseException {
        return (JSONObject) new JSONParser().parse(json.toString());
    }

    public static JSONArray convertToArray(JsonNode json) throws ParseException {
        return (JSONArray) new JSONParser().parse(json.toString());
    }

    public static JSONObject convertToObject(JsonNode json, String keys) throws ParseException {
        return (JSONObject) new JSONParser().parse(json.get(keys).toString());
    }

    public static JSONArray convertToArray(JsonNode json, String keys) throws ParseException {
        return (JSONArray) new JSONParser().parse(json.get(keys).toString());
    }
}
