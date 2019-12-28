package httpactions;

import database.Connection.Connect;
import enums.Database;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Optional;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */
public class ApiAuthAction extends Action<ApiAuth> {

    @Override
    public F.Promise<Result> call(Http.Context context) {
        try {
            final String authorizationHeader = context.request().getHeader("Authorization");
            final Optional<ApiAuthInfo> infoOpt = ApiAuthInfo.from(authorizationHeader);

            if (infoOpt.isPresent()) {
                final ApiAuthInfo ai = infoOpt.get();
                final String apiKey = ai.getApiKey();

                if(!"DMA128256512AI".equalsIgnoreCase(apiKey)) {
                    return F.Promise.promise(() -> status(401, "Invalid API key " + apiKey + "."));
                }

                openConnection();
                final Authorization auth = new Authorization().setApiKey(ai.getApiKey());
                context.args.put("settings", auth);

                return delegate.call(context);
            } else {
                return F.Promise.promise(() -> status(401, "Authorization header is missing or not valid."));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return F.Promise.promise(() -> status(401, "Authorization header is missing or not valid."));
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
    private void openConnection(){}
}
