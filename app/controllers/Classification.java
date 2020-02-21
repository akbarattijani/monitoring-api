package controllers;

import algoritm.KNearestNeighbor;
import algoritm.NaiveBayes;
import com.fasterxml.jackson.databind.JsonNode;
import database.Connection.Connect;
import database.Connection.Connection;
import database.dao.impl.DatasetImpl;
import database.dao.impl.SampleImpl;
import database.dao.impl.UserImpl;
import enums.Database;
import httpactions.ApiAuth;
import model.Dataset;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.util.ArrayList;
import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.status;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@ApiAuth
public class Classification {

    @Connect(
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
            JsonNode body = request().body().asJson();
            List<model.Classification> samples = new ArrayList<>();
            List<model.Sample> sampleList = new SampleImpl().getAll();

            for (model.Sample sample : sampleList) {
                samples.add(new model.Classification().setId(sample.getIdUser()).setBiner(sample.getBiner().split(" ")));
            }

            String nip = body.path("nip").asText();
            String biner = body.path("biner").asText();

            List<model.Classification> knn = new KNearestNeighbor().classification(samples, biner, 9);
            int resultId = new NaiveBayes().classification(knn, biner.split(" "), true);
//            int resultId = new KNearestNeighbor().classification(samples, biner.split(" "), 9);

            System.out.println("Result : " + resultId);
            model.User user = new UserImpl().getById(resultId);

            if (user != null) {
                String nipCompare = user.getNip();
                if (nipCompare.equals(nip)) {
                    // Closing database connection
                    Connection.disconnect();

                    return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                }
            }

            Connection.disconnect();
            return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @Connect(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result dataMining() {
        try {
            JsonNode body = request().body().asJson();
            List<Dataset> datasets = new DatasetImpl().getAll();

            JSONArray array = new JSONArray();
            for (int i = 0; i < body.size(); i++) {
                JsonNode object = body.get(i);
                List<Dataset> knn = new KNearestNeighbor().preProccessing(datasets, object.path("data").asText(), 15);
                String result = new NaiveBayes().dataMining(knn, object.path("data").asText().split(" "), true);

                JSONObject obj = new JSONObject();
                obj.put("nim", object.path("nim").asText());
                obj.put("category", result);
                array.add(obj);
            }

            return Body.echo(enums.Result.REQUEST_OK, array.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
