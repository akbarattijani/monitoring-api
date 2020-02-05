package exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.simple.JSONObject;

import java.io.Serializable;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@SuppressWarnings("serial")
@JsonIgnoreProperties({"cause", "localizedMessage", "stackTrace", "isoLanguage"})
public class TranslateException extends RuntimeException implements Serializable {
    private String errorCode;
    private String message;
    private String argument;

    public TranslateException(String errCode) {
        super();
        String[] bundle = getBundle(errCode);
        this.errorCode = bundle[0];
        this.message = bundle[1];
    }

    public TranslateException(String errCode, String argument) {
        super();
        this.argument = argument;
        String[] bundle = getBundle(errCode);
        this.errorCode = bundle[0];
        this.message = bundle[1];
    }

    @Override
    public String getMessage() {
        JSONObject object = new JSONObject();
        object.put("code", errorCode);
        object.put("message", message);
        if (argument != null) {
            object.put("argument", argument);
        }

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
