package controllers;

import database.Connection.Connect;
import database.Connection.Connection;
import database.dao.impl.SampleImpl;
import enums.Database;
import httpactions.ApiAuth;
import mapper.Mapper;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.util.List;

import static play.mvc.Controller.request;
import static play.mvc.Results.status;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@ApiAuth
public class Sample {

    @Connect(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result insert() {
        try {
            model.Sample sample = new Mapper().toModel(request().body().asJson(), model.Sample.class);
            SampleImpl dao = new SampleImpl();
            int count = dao.save(sample);

            // Closing database connection
            Connection.disconnect();

            return Body.echo(enums.Result.REQUEST_OK, "Inserting Success : " + count);
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
    public static Result insertAll() {
        try {
            List<model.Sample> samples = new Mapper().toModels(request().body().asJson(), model.Sample.class);
            SampleImpl dao = new SampleImpl();

            int count = 0;
            for (model.Sample sample : samples) {
                int c = dao.save(sample);
                count += c;
            }

            // Closing database connection
            Connection.disconnect();

            return Body.echo(enums.Result.REQUEST_OK, "Inserting Success : " + count);
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
