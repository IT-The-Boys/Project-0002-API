package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.util.Annotations;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;

public class NamesHandler extends BaseHandler {
    public static final String OP = Consts.Operation.NAMES.toString();
    private final ConnectedUsers users;

    public NamesHandler(@Annotations.ConnectedUsers ConnectedUsers users) {
        this.users = users;
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) {
        JsonArray array = new JsonArray();
        for (User item : users.getUsers()) array.add(item.toSmallJson().obj());
        return new JsonWrapper(Consts.GeneralKeys.NAMES, array);
    }
}