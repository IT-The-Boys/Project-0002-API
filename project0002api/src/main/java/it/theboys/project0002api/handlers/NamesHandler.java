package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.ConnectedUsers;
import com.google.gson.JsonArray;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

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