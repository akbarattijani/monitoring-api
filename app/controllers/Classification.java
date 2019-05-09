package controllers;

import algoritm.KNearestNeighbor;
import com.fasterxml.jackson.databind.JsonNode;
import database.Connection.CheckDatabase;
import database.Connection.Connection;
import enums.Database;
import httpactions.ApiAuth;
import model.ClassificationModel;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.sql.PreparedStatement;
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
            List<ClassificationModel> samples = new ArrayList<>();
            String select = "SELECT * FROM m_sample";
            JsonNode body = request().body().asJson();

            Statement statement = Connection.getConnection().createStatement();
            ResultSet rs = statement.executeQuery(select);

            while (rs.next()) {
                samples.add(new ClassificationModel().setId(rs.getInt("id_user")).setBiner(rs.getString("biner").split(" ")));
            }

            String nip = body.path("nip").asText();
            String biner = body.path("biner").asText();

//            List<ClassificationModel> knn = new KNearestNeighbor().classification(samples, biner, 9);
//            int resultId = new NaiveBayes().classification(knn, biner.split(" "), true);
            int resultId = new KNearestNeighbor().classification(samples, biner.split(" "), 9);

            System.out.println("Result : " + resultId);

            select = "SELECT * FROM m_user where id = ? LIMIT 1";
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            preparedStatement.setInt(1, resultId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String nipCompare = resultSet.getString("nip");
                if (nipCompare.equals(nip)) {
                    // Closing database connection
                    rs.close();
                    resultSet.close();
                    statement.close();
                    preparedStatement.close();
                    Connection.disconnect();

                    return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                }
            }

            // Closing database connection
            rs.close();
            resultSet.close();
            statement.close();
            preparedStatement.close();
            Connection.disconnect();

            return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
