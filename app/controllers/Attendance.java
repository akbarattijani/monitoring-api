package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import database.Connection.Connect;
import database.Connection.Connection;
import enums.Database;
import httpactions.ApiAuth;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
                    sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude, date, custom) values (?,?,?,?,?)";
                    preparedStatement = Connection.getConnection().prepareStatement(sql);

                    preparedStatement.setInt(1, rs.getInt("id"));
                    preparedStatement.setDouble(2, longitude);
                    preparedStatement.setDouble(3, latitude);
                    preparedStatement.setTimestamp(4, new Timestamp(startDate.getTime()));
                    preparedStatement.setInt(5, 1);

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

    @Connect(
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
                    sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude, date, custom) values (?,?,?,?,?)";
                    preparedStatement = Connection.getConnection().prepareStatement(sql);

                    preparedStatement.setInt(1, rs.getInt("id"));
                    preparedStatement.setDouble(2, longitude);
                    preparedStatement.setDouble(3, latitude);
                    preparedStatement.setTimestamp(4, new Timestamp(endDate.getTime()));
                    preparedStatement.setInt(5, 4);

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

    @Connect(
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

    @Connect(
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

    @Connect(
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
            final Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(body.path("date").asText());

            String select = "SELECT * FROM t_absen WHERE id_user = ? AND start_date >= now()::date ORDER BY id LIMIT 1";
            PreparedStatement prepaired = Connection.getConnection().prepareStatement(select);
            prepaired.setInt(1, id);

            ResultSet rs = prepaired.executeQuery();
            if (rs.next()) {
                String sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude, date, custom) values (?,?,?,?,?)";
                PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(sql);

                preparedStatement.setInt(1, rs.getInt("id"));
                preparedStatement.setDouble(2, longitude);
                preparedStatement.setDouble(3, latitude);
                preparedStatement.setTimestamp(4, new Timestamp(date.getTime()));
                preparedStatement.setInt(5, 0);

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

    @Connect(
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
                    sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude, date, custom) values (?,?,?,?,?)";
                    preparedStatement = Connection.getConnection().prepareStatement(sql);

                    preparedStatement.setInt(1, rs.getInt("id"));
                    preparedStatement.setDouble(2, longitude);
                    preparedStatement.setDouble(3, latitude);
                    preparedStatement.setTimestamp(4, new Timestamp(breakDate.getTime()));
                    preparedStatement.setInt(5, 2);

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

                    return Body.echo(enums.Result.REQUEST_OK, "Gagal perbarui status istirahat. Ulangi beberapa saat lagi.");
                }
            } else {
                // Closing database connection
                prepaired.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, "Anda belum melakukan absen atau sudah selesai istirahat");
            }
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
                    sql = "INSERT INTO t_absen_detail (id_absen, longitude, latitude, date, custom) values (?,?,?,?,?)";
                    preparedStatement = Connection.getConnection().prepareStatement(sql);

                    preparedStatement.setInt(1, rs.getInt("id"));
                    preparedStatement.setDouble(2, longitude);
                    preparedStatement.setDouble(3, latitude);
                    preparedStatement.setTimestamp(4, new Timestamp(breakDate.getTime()));
                    preparedStatement.setInt(5, 3);

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

                    return Body.echo(enums.Result.REQUEST_OK, "Gagal perbarui status selesai istirahat. Ulangi beberapa saat lagi.");
                }
            } else {
                // Closing database connection
                prepaired.close();
                rs.close();
                Connection.disconnect();

                return Body.echo(enums.Result.REQUEST_OK, "Anda belum melakukan absen atau belum perbarui status istirahat");
            }
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
    public static Result get(int id, String date) {
        try {
            final Date fixDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
            JSONArray array = new JSONArray();

            String select = "SELECT a.id_user, b.* FROM t_absen a, t_absen_detail b WHERE a.id = b.id_absen AND a.id_user = ? AND a.start_date::date = ?";

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, new java.sql.Date(fixDate.getTime()));

            ResultSet rs = preparedStatement.executeQuery();

            boolean isNotNull = false;
            while (rs.next()) {
                isNotNull = true;

                JSONObject object = new JSONObject();
                object.put("id", rs.getInt("id_user"));
                object.put("id_absen", rs.getInt("id_absen"));
                object.put("longitude", rs.getDouble("longitude"));
                object.put("latitude", rs.getDouble("latitude"));
                object.put("date", rs.getTimestamp("date").toString());
                object.put("custom", rs.getInt("custom"));

                array.add(object);
            }

            preparedStatement.close();
            rs.close();
            Connection.disconnect();

            if (isNotNull) {
                return Body.echo(enums.Result.REQUEST_OK, array.toString());
            } else {
                return Body.echo(enums.Result.RESPONSE_NOTHING, "NOTHING");
            }
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
    public static Result history(int id, String startDate, String endDate) {
        try {
            final Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            final Date end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
            JSONArray array = new JSONArray();

            String select = "SELECT * FROM t_absen WHERE id_user = ? AND start_date::date BETWEEN ? AND ?";

            PreparedStatement preparedStatement = Connection.getConnection().prepareStatement(select);
            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, new java.sql.Date(start.getTime()));
            preparedStatement.setDate(3, new java.sql.Date(end.getTime()));

            ResultSet rs = preparedStatement.executeQuery();

            boolean isNotNull = false;
            while (rs.next()) {
                isNotNull = true;

                JSONObject object = new JSONObject();
                object.put("id", rs.getInt("id"));
                object.put("id_user", rs.getInt("id_user"));
                object.put("start_date", rs.getTimestamp("start_date").toString());

                if (rs.getTimestamp("end_date") == null) {
                    object.put("end_date", "Belum Tersedia");
                } else {
                    object.put("end_date", rs.getTimestamp("end_date").toString());
                }

                if (rs.getTimestamp("break_start_date") == null) {
                    object.put("break_start_date", "Belum Tersedia");
                } else {
                    object.put("break_start_date", rs.getTimestamp("break_start_date").toString());
                }

                if (rs.getTimestamp("break_end_date") == null) {
                    object.put("break_end_date", "Belum Tersedia");
                } else {
                    object.put("break_end_date", rs.getTimestamp("break_end_date").toString());
                }

                array.add(object);
            }

            preparedStatement.close();
            rs.close();
            Connection.disconnect();

            if (isNotNull) {
                return Body.echo(enums.Result.REQUEST_OK, array.toString());
            } else {
                return Body.echo(enums.Result.RESPONSE_NOTHING, "NOTHING");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Connection.disconnect();
            return status(401, e.getMessage());
        }
    }
}
