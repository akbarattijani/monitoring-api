package exception;

import org.json.simple.JSONObject;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

public class TranslateException extends RuntimeException {
    private String errorCode;
    private String message;
    private String argument;

    public TranslateException(String errCode) {
        super();
        getBundle(errCode);
    }

    public TranslateException(String errCode, String argument) {
        super();
        this.argument = argument;
        getBundle(errCode);
    }

    @Override
    public String getMessage() {
        JSONObject object = new JSONObject();
        object.put("code", errorCode);
        object.put("message", message);
        object.put("argument", argument);

        return object.toString();
    }

    private String[] getBundle(String errorCode) {
        String bundle = new Bundle().getBundle(errorCode);
        String[] message = bundle.split(":");
        message[0] = message[0].trim();
        message[1] = message[1].trim();

        return message;
    }
}
