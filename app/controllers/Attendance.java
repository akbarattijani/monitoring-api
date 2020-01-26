package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import database.Connection.Connect;
import database.Connection.Connection;
import database.dao.impl.AttendanceDetailImpl;
import database.dao.impl.AttendanceImpl;
import enums.Database;
import httpactions.ApiAuth;
import mapper.Mapper;
import model.AttendanceDetail;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import play.mvc.BodyParser;
import play.mvc.Result;
import utils.Body;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            model.Attendance attendance = new Mapper().toModel(request().body().asJson(), model.Attendance.class);
            AttendanceImpl dao = new AttendanceImpl();
            int count = dao.save(attendance);

            if (count > 0) {
                model.Attendance a = dao.getByIdUser(attendance.getIdUser());

                if (a != null) {
                    AttendanceDetailImpl daoDetail = new AttendanceDetailImpl();
                    AttendanceDetail attendanceDetail = new Mapper().toModel(request().body().asJson(), model.AttendanceDetail.class);
                    attendanceDetail.setIdAbsen(a.getId());
                    attendanceDetail.setDate(a.getStartDate());
                    attendanceDetail.setCustom(1);

                    int countDetail = daoDetail.save(attendanceDetail);
                    if (countDetail > 0) {
                        // Closing database connection
                        Connection.disconnect();
                        return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                    } else {
                        dao.delete("id = ?", new Object[] {a.getId()});

                        // Closing database connection
                        Connection.disconnect();
                        return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                    }
                } else {
                    return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                }
            } else {
                // Closing database connection
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
            final int id = body.path("id_user").asInt();

            AttendanceImpl dao = new AttendanceImpl();
            model.Attendance attendance = dao.getTodayByIdUser(id);

            if (attendance != null) {
                attendance.setEndDate(body.path("end_date").asText());

                int count = dao.save(attendance);
                if (count > 0) {
                    AttendanceDetail attendanceDetail = new AttendanceDetail();
                    attendanceDetail.setIdAbsen(attendance.getId());
                    attendanceDetail.setLongitude(body.path("longitude").asDouble());
                    attendanceDetail.setLatitude(body.path("latitude").asDouble());
                    attendanceDetail.setDate(attendance.getEndDate());
                    attendanceDetail.setCustom(4);

                    int countDetail = new AttendanceDetailImpl().save(attendanceDetail);
                    if (countDetail > 0) {
                        Connection.disconnect();
                        return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                    } else {
                        Connection.disconnect();
                        return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                    }
                } else {
                    Connection.disconnect();
                    return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                }
            } else {
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
            model.Attendance attendance = new AttendanceImpl().getTodayByIdUser(id);
            if (attendance != null) {
                // Closing database connection
                Connection.disconnect();
                return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
            } else {
                // Closing database connection
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
            model.Attendance attendance = new AttendanceImpl().getEndTodayByIdUser(id);
            if (attendance != null) {
                // Closing database connection
                Connection.disconnect();
                return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
            } else {
                // Closing database connection
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
            model.Attendance attendance = new AttendanceImpl().getTodayByIdUser(body.path("id_user").asInt());

            if (attendance != null) {
                AttendanceDetail attendanceDetail = new AttendanceDetail();
                attendanceDetail.setIdAbsen(attendance.getId());
                attendanceDetail.setLongitude(body.path("longitude").asDouble());
                attendanceDetail.setLatitude(body.path("latitude").asDouble());
                attendanceDetail.setDate(body.path("date").asText());
                attendanceDetail.setCustom(0);

                int countDetail = new AttendanceDetailImpl().save(attendanceDetail);
                if (countDetail > 0) {
                    Connection.disconnect();
                    return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                } else {
                    Connection.disconnect();
                    return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                }
            } else {
                // Closing database connection
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
            AttendanceImpl dao = new AttendanceImpl();
            model.Attendance attendance = dao.getBreakOutTodayByIdUser(body.path("id_user").asInt());

            if (attendance != null) {
                attendance.setBreakStartDate(body.path("break_start_date").asText());

                int count = dao.save(attendance);
                if (count > 0) {
                    AttendanceDetail attendanceDetail = new AttendanceDetail();
                    attendanceDetail.setIdAbsen(attendance.getId());
                    attendanceDetail.setLongitude(body.path("longitude").asDouble());
                    attendanceDetail.setLatitude(body.path("latitude").asDouble());
                    attendanceDetail.setDate(attendance.getBreakStartDate());
                    attendanceDetail.setCustom(2);

                    int countDetail = new AttendanceDetailImpl().save(attendanceDetail);
                    if (countDetail > 0) {
                        Connection.disconnect();
                        return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                    } else {
                        Connection.disconnect();
                        return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                    }
                } else {
                    // Closing database connection
                    Connection.disconnect();
                    return Body.echo(enums.Result.REQUEST_OK, "Gagal perbarui status istirahat. Ulangi beberapa saat lagi.");
                }
            } else {
                // Closing database connection
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
            AttendanceImpl dao = new AttendanceImpl();
            model.Attendance attendance = dao.getBreakInTodayByIdUser(body.path("id_user").asInt());

            if (attendance != null) {
                attendance.setBreakEndDate( body.path("break_end_date").asText());

                int count = dao.save(attendance);
                if (count > 0) {
                    AttendanceDetail attendanceDetail = new AttendanceDetail();
                    attendanceDetail.setIdAbsen(attendance.getId());
                    attendanceDetail.setLongitude(body.path("longitude").asDouble());
                    attendanceDetail.setLatitude(body.path("latitude").asDouble());
                    attendanceDetail.setDate(attendance.getBreakEndDate());
                    attendanceDetail.setCustom(3);

                    int countDetail = new AttendanceDetailImpl().save(attendanceDetail);
                    if (countDetail > 0) {
                        Connection.disconnect();
                        return Body.echo(enums.Result.REQUEST_OK, Boolean.TRUE.toString());
                    } else {
                        Connection.disconnect();
                        return Body.echo(enums.Result.REQUEST_OK, Boolean.FALSE.toString());
                    }
                } else {
                    // Closing database connection
                    Connection.disconnect();
                    return Body.echo(enums.Result.REQUEST_OK, "Gagal perbarui status selesai istirahat. Ulangi beberapa saat lagi.");
                }
            } else {
                // Closing database connection
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
            JSONArray array = new JSONArray();
            List<model.Attendance> attendanceList = new AttendanceImpl().getAttendances(id, startDate, endDate);

            for (model.Attendance attendance : attendanceList) {
                JSONObject object = new JSONObject();
                object.put("id", attendance.getId());
                object.put("id_user", attendance.getIdUser());
                object.put("start_date", attendance.getStartDate());

                String end = attendance.getEndDate();
                if (end == null || end.trim().equals("")) {
                    object.put("end_date", "Belum Tersedia");
                } else {
                    object.put("end_date", end);
                }

                String breakOut = attendance.getBreakStartDate();
                if (breakOut == null || breakOut.trim().equals("")) {
                    object.put("break_start_date", "Belum Tersedia");
                } else {
                    object.put("break_start_date", breakOut);
                }

                String breakIn = attendance.getBreakEndDate();
                if (breakIn == null || breakIn.trim().equals("")) {
                    object.put("break_end_date", "Belum Tersedia");
                } else {
                    object.put("break_end_date", breakIn);
                }

                array.add(object);
            }

            Connection.disconnect();
            if (attendanceList.size() > 0) {
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
    public static Result getAttendances(int id) {
        try {
            JSONArray array = new JSONArray();
            List<model.Attendance> attendanceList = new AttendanceImpl().getAll(id);

            for (model.Attendance attendance : attendanceList) {
                JSONObject object = new JSONObject();
                object.put("id", attendance.getId());
                object.put("id_user", attendance.getIdUser());
                object.put("start_date", attendance.getStartDate());

                String end = attendance.getEndDate();
                if (end == null || end.trim().equals("")) {
                    object.put("end_date", "Belum Tersedia");
                } else {
                    object.put("end_date", end);
                }

                String breakOut = attendance.getBreakStartDate();
                if (breakOut == null || breakOut.trim().equals("")) {
                    object.put("break_start_date", "Belum Tersedia");
                } else {
                    object.put("break_start_date", breakOut);
                }

                String breakIn = attendance.getBreakEndDate();
                if (breakIn == null || breakIn.trim().equals("")) {
                    object.put("break_end_date", "Belum Tersedia");
                } else {
                    object.put("break_end_date", breakIn);
                }

                array.add(object);
            }

            Connection.disconnect();
            if (attendanceList.size() > 0) {
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
    public static Result getAttendanceDetail(int id) {
        try {
            JSONArray array = new JSONArray();
            List<AttendanceDetail> attendanceList = new AttendanceDetailImpl().getDetailByIdAbsen(id);

            for (AttendanceDetail attendance : attendanceList) {
                JSONObject object = new JSONObject();
                object.put("id", attendance.getId());
                object.put("id_absen", attendance.getIdAbsen());
                object.put("longitude", attendance.getLongitude());
                object.put("latitude", attendance.getLatitude());
                object.put("custom", attendance.getCustom());
                object.put("date", attendance.getDate());

                array.add(object);
            }

            Connection.disconnect();
            if (attendanceList.size() > 0) {
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
