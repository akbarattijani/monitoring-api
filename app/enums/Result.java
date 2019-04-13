package enums;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public enum Result {

    REQUEST_OK(200),
    REQUEST_BAD(100 |
                101 |
                201 |
                202 |
                203 |
                204 |
                205 |
                206 |
                300 |
                301 |
                302 |
                303 |
                304 |
                305 |
                307 |
                400 |
                401 |
                402 |
                403 |
                404 |
                405 |
                406 |
                407 |
                408 |
                409 |
                410 |
                411 |
                412 |
                413 |
                414 |
                415 |
                416 |
                417 |
                500 |
                501 |
                502 |
                503 |
                504 |
                505),
    RESPONSE_OK("OK"),
    RESPONSE_BAD("ERROR"),
    RESULT_DATABASE_NOT_CONNECTED("Database not connected");

    private Object defaultValue;

    Result(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return defaultValue.toString();
    }

}
