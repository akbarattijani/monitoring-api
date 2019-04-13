package httpactions;

import play.mvc.Http;
import java.util.Optional;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class Authorization {
    private static String apiKey;

    public static Optional<Authorization> current() {
        return Optional.ofNullable(((Authorization) Http.Context.current().args.get("settings")));
    }

    public static String getApiKey() {
        return apiKey;
    }

    public Authorization setApiKey(String apiKey) {
        Authorization.apiKey = apiKey;
        return this;
    }
}
