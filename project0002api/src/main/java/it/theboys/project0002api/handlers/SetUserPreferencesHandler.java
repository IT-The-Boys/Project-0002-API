package it.theboys.project0002api.handlers;

import java.util.Map;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.util.Annotations;

import org.bson.json.JsonObject;
import org.springframework.boot.json.JsonParser;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.Utils;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;

public class SetUserPreferencesHandler extends BaseHandler {
    public static final String OP = Consts.Operation.SET_USER_PREFERENCES.toString();
    private final JsonParser parser = new JsonParser();
    private final UsersWithAccount accounts;

    public SetUserPreferencesHandler(@Annotations.UsersWithAccount UsersWithAccount accounts) {
        this.accounts = accounts;
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        UserAccount account = user.getAccount();
        if (account == null) return JsonWrapper.EMPTY;

        try {
            JsonObject obj = parser.parse(params.getStringNotNull(Consts.GeneralKeys.USER_PREFERENCES)).getAsJsonObject();
            Map<String, String> map = Utils.toMap(obj);
            account.updatePreferences(map);
            accounts.updatePreferences(account, map);
            return account.preferences.toJson(map.keySet().toArray(new String[0]));
        } catch (IllegalStateException | JsonSyntaxException ex) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST, ex);
        }
    }
}