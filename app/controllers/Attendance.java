package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import database.Connection.CheckDatabase;
import database.Connection.Connection;
import enums.Database;
import httpactions.ApiAuth;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static play.mvc.Controller.request;
import static play.mvc.Results.status;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@ApiAuth
public class Attendance {
    @CheckDatabase(
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
            final JsonNode body = request().body().asJson();
            final int id = body.path("id").asInt();
            final Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(body.path("start_date").asText());
            final double longitude = body.path("longitude").asDouble();
            final double latitude = body.path("latitude").asDouble();

            String sql = "INSERT INTO t_absen (id_user, start_date) values (?,?)";
            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, new Timestamp(startDate.getTime()));

            int count = preparedStatement.executeUpdate();

            if (count > 0) {
                String select = "SELECT * FROM t_absen WHERE id_user = ? ORDER BY id DESC LIMIT 1";

                PreparedStatement prepaired = Connection.getConnection().prepareStatement(select);
                prepaired.setInt(1, id);

                ResultSet rs = prepaired.executeQuery();
                if (rs.next()) {
                    sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude) values (?,?,?)";
                    preparedStatement = Connection.getConnection().prepareStatement(sql);

                    preparedStatement.setInt(1, rs.getInt("id"));
                    preparedStatement.setDouble(2, longitude);
                    preparedStatement.setDouble(3, latitude);

                    int countDetail = preparedStatement.executeUpdate();
                    if (countDetail > 0) {
                        // Closing database connection
                        preparedStatement.close();
                        prepaired.close();
                        rs.close();
                        Connection.disconnect();

                        return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                    } else {
                        sql = "DELETE FROM t_absen WHERE id = ?";
                        preparedStatement = Connection.getConnection().prepareStatement(sql);
                        preparedStatement.setInt(1, rs.getInt("id"));
                        preparedStatement.executeUpdate();

                        // Closing database connection
                        preparedStatement.close();
                        prepaired.close();
                        rs.close();
                        Connection.disconnect();

                        return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                    }
                } else {
                    return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                }
            } else {
                // Closing database connection
                preparedStatement.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result update() {
        try {
            final JsonNode body = request().body().asJson();
            final int id = body.path("id").asInt();
            final Date endDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(body.path("end_date").asText());
            final double longitude = body.path("longitude").asDouble();
            final double latitude = body.path("latitude").asDouble();

            String select = "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date ORDER BY id LIMIT 1";
            PreparedStatement prepaired = Connection.getConnection().prepareStatement(select);
            prepaired.setInt(1, id);

            ResultSet rs = prepaired.executeQuery();
            if (rs.next()) {
                String sql = "UPDATE t_absen SET end_date = ? WHERE id = ?";
                PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

                preparedStatement.setTimestamp(1, new Timestamp(endDate.getTime()));
                preparedStatement.setInt(2, rs.getInt("id"));

                int count = preparedStatement.executeUpdate();
                if (count > 0) {
                    sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude) values (?,?,?)";
                    preparedStatement = Connection.getConnection().prepareStatement(sql);

                    preparedStatement.setInt(1, rs.getInt("id"));
                    preparedStatement.setDouble(2, longitude);
                    preparedStatement.setDouble(3, latitude);

                    int countDetail = preparedStatement.executeUpdate();
                    if (countDetail > 0) {
                        // Closing database connection
                        preparedStatement.close();
                        prepaired.close();
                        rs.close();
                        Connection.disconnect();

                        return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                    } else {
                        // Closing database connection
                        preparedStatement.close();
                        prepaired.close();
                        rs.close();
                        Connection.disconnect();

                        return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                    }
                } else {
                    // Closing database connection
                    preparedStatement.close();
                    prepaired.close();
                    rs.close();
                    Connection.disconnect();

                    return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                }
            } else {
                // Closing database connection
                prepaired.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    public static Result check(int id) {
        try {
            String select = "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date ORDER BY id LIMIT 1";

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                // Closing database connection
                preparedStatement.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
            } else {
                // Closing database connection
                preparedStatement.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    public static Result checkEndAttendance(int id) {
        try {
            String select = "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date AND end_date IS NULL ORDER BY id LIMIT 1";

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                // Closing database connection
                preparedStatement.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
            } else {
                // Closing database connection
                preparedStatement.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result track() {
        try {
            final JsonNode body = request().body().asJson();
            final int id = body.path("id").asInt();
            final double longitude = body.path("longitude").asDouble();
            final double latitude = body.path("latitude").asDouble();

            String select = "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date ORDER BY id LIMIT 1";
            PreparedStatement prepaired = Connection.getConnection().prepareStatement(select);
            prepaired.setInt(1, id);

            ResultSet rs = prepaired.executeQuery();
            if (rs.next()) {
                String sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude) values (?,?,?)";
                PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

                preparedStatement.setInt(1, rs.getInt("id"));
                preparedStatement.setDouble(2, longitude);
                preparedStatement.setDouble(3, latitude);

                int countDetail = preparedStatement.executeUpdate();
                if (countDetail > 0) {
                    // Closing database connection
                    preparedStatement.close();
                    prepaired.close();
                    rs.close();
                    Connection.disconnect();

                    return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                } else {
                    // Closing database connection
                    preparedStatement.close();
                    prepaired.close();
                    rs.close();
                    Connection.disconnect();

                    return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                }
            } else {
                // Closing database connection
                prepaired.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result breakOut() {
        try {
            final JsonNode body = request().body().asJson();
            final int id = body.path("id").asInt();
            final Date breakDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(body.path("break_date").asText());
            final double longitude = body.path("longitude").asDouble();
            final double latitude = body.path("latitude").asDouble();

            String select = "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date AND end_date IS NULL AND break_end_date IS NULL ORDER BY id LIMIT 1";
            PreparedStatement prepaired = Connection.getConnection().prepareStatement(select);
            prepaired.setInt(1, id);

            ResultSet rs = prepaired.executeQuery();
            if (rs.next()) {
                String sql = "UPDATE t_absen SET break_start_date = ? WHERE id = ?";
                PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

                preparedStatement.setTimestamp(1, new Timestamp(breakDate.getTime()));
                preparedStatement.setInt(2, rs.getInt("id"));

                int count = preparedStatement.executeUpdate();
                if (count > 0) {
                    sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude) values (?,?,?)";
                    preparedStatement = Connection.getConnection().prepareStatement(sql);

                    preparedStatement.setInt(1, rs.getInt("id"));
                    preparedStatement.setDouble(2, longitude);
                    preparedStatement.setDouble(3, latitude);

                    int countDetail = preparedStatement.executeUpdate();
                    if (countDetail > 0) {
                        // Closing database connection
                        preparedStatement.close();
                        prepaired.close();
                        rs.close();
                        Connection.disconnect();

                        return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                    } else {
                        // Closing database connection
                        preparedStatement.close();
                        prepaired.close();
                        rs.close();
                        Connection.disconnect();

                        return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                    }
                } else {
                    // Closing database connection
                    preparedStatement.close();
                    prepaired.close();
                    rs.close();
                    Connection.disconnect();

                    return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                }
            } else {
                // Closing database connection
                prepaired.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }

    @CheckDatabase(
            database = Database.POSTGRESQL,
            host = "ec2-23-23-173-30.compute-1.amazonaws.com",
            databaseName = "d87s2lf0vv7l32",
            userName = "ppxiknjbrpshfp",
            password = "dadde9e960e7acc54bf9b09a35ef98f4ec01a149e1560b4a8c4f6909271cc76c",
            port = "5432"
    )
    @BodyParser.Of(value = BodyParser.Json.class , maxLength = 1024 * 1024 * 1024)
    public static Result breakIn() {
        try {
            final JsonNode body = request().body().asJson();
            final int id = body.path("id").asInt();
            final Date breakDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(body.path("break_date").asText());
            final double longitude = body.path("longitude").asDouble();
            final double latitude = body.path("latitude").asDouble();

            String select = "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date AND end_date IS NULL AND break_start_date IS NOT NULL ORDER BY id LIMIT 1";
            PreparedStatement prepaired = Connection.getConnection().prepareStatement(select);
            prepaired.setInt(1, id);

            ResultSet rs = prepaired.executeQuery();
            if (rs.next()) {
                String sql = "UPDATE t_absen SET break_end_date = ? WHERE id = ?";
                PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

                preparedStatement.setTimestamp(1, new Timestamp(breakDate.getTime()));
                preparedStatement.setInt(2, rs.getInt("id"));

                int count = preparedStatement.executeUpdate();
                if (count > 0) {
                    sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude) values (?,?,?)";
                    preparedStatement = Connection.getConnection().prepareStatement(sql);

                    preparedStatement.setInt(1, rs.getInt("id"));
                    preparedStatement.setDouble(2, longitude);
                    preparedStatement.setDouble(3, latitude);

                    int countDetail = preparedStatement.executeUpdate();
                    if (countDetail > 0) {
                        // Closing database connection
                        preparedStatement.close();
                        prepaired.close();
                        rs.close();
                        Connection.disconnect();

                        return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                    } else {
                        // Closing database connection
                        preparedStatement.close();
                        prepaired.close();
                        rs.close();
                        Connection.disconnect();

                        return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                    }
                } else {
                    // Closing database connection
                    preparedStatement.close();
                    prepaired.close();
                    rs.close();
                    Connection.disconnect();

                    return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                }
            } else {
                // Closing database connection
                prepaired.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
