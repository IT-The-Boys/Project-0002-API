package it.theboys.project0002api.handlers;

import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import io.undertow.server.HttpServerExchange;
import it.theboys.project0002api.Consts;
import it.theboys.project0002api.Utils;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.data.accounts.UserAccount;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.UsersWithAccount;

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