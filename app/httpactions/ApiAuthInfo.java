package httpactions;


import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class ApiAuthInfo {
    private String apiKey;

    private ApiAuthInfo() {}

    public static Optional<ApiAuthInfo> from(String authorization) {
        if (authorization == null) {
            return Optional.empty();
        }

        final int firstSpacePos = authorization.indexOf(' ');
        final String authType = authorization.substring(0, firstSpacePos);

        if (!"ApiAuth".equals(authType)) {
            return Optional.empty();
        }

        final String cleaned = authorization.substring(firstSpacePos + 1);
        final Map<String, String> map = Stream.of(cleaned.split(","))
                .map(x -> x.split("="))
                .collect(toMap(
                        x -> x[0].trim(),
                        x -> x[1].trim().substring(1, x[1].trim().length() - 1)
                ));

        ApiAuthInfo result = new ApiAuthInfo();
        result.apiKey = map.getOrDefault("api_key", "");

        return Optional.of(result);
    }

    public String getApiKey() {
        return apiKey;
    }

    public ApiAuthInfo setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }
}
