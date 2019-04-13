package utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.mvc.Result;
import play.mvc.Results;

import java.awt.image.BufferedImage;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class Body {

    private static String print = "";

    public static void echo(int[][] matrix) {

        for(int[] val : matrix) {

            for(int v : val) {
                print += v+"\t";
            }

            print += "\n";
        }

        echo(200, print);
        print = "";
    }

    public static void echo(BufferedImage matrix) {

        for (int i = 0; i < matrix.getHeight(); i++) {
            for (int j = 0; j < matrix.getWidth(); j++) {
                print += matrix.getRGB(j, i);
            }

            print += "\n";
        }

        echo(200, print);
        print = "";
    }

    public static Result echo(JSONArray json) {
        return Results.ok(json.toString());
    }

    public static Result echo(JSONObject json) {
        return Results.ok(json.toString());
    }

    public static Result echo(Object code, String text) {

        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("result", text);

        return Results.ok(json.toString());
    }
}
