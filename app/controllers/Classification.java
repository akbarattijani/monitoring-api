package controllers;

import algoritm.KNearestNeighbor;
import com.fasterxml.jackson.databind.JsonNode;
import database.Connection.CheckDatabase;
import database.Connection.Connection;
import enums.Database;
import httpactions.ApiAuth;
import javafx.util.Pair;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.status;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@ApiAuth
public class Classification {

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result search() {
        try {
            List<Pair<Integer, String>> samples = new ArrayList<>();
            String select = "SELECT * FROM m_sample";
            JsonNode body = request().body().asJson();

            Statement statement = Connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(select);

            while (rs.next()) {
                samples.add(new Pair<>(rs.getInt("id_user"), rs.getString("biner")));
            }

            int id = body.path("id").asInt();
            String biner = body.path("biner").asText();

            List<Pair<Integer, String[]>> knn = new KNearestNeighbor().classification(samples, biner, 10);

            return Body.echo(enums.Result.REQUEST_OK, String.valueOf(samples.size()));
        } catch (Exception e) {
            e.printStackTrace();
            return status(401, e.getMessage());
        }
    }
}
