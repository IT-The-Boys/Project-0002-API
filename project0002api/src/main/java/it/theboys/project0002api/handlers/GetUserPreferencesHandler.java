package it.theboys.project0002api.handlers;


import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.data.accounts.UserAccount;
import it.theboys.project0002api.server.Parameters;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class GetUserPreferencesHandler extends BaseHandler {
    public static final String OP = Consts.Operation.GET_USER_PREFERENCES.toString();

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) {
        UserAccount account = user.getAccount();
        if (account == null) return JsonWrapper.EMPTY;

        String[] keys;
        String keysStr = params.getString(Consts.GeneralKeys.USER_PREFERENCES);
        if (keysStr == null) keys = null;
        else keys = keysStr.split(",");

        return account.preferences.toJson(keys);
    }
}