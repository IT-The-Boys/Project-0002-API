package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import io.undertow.server.HttpServerExchange;
import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;

public class PrepareShutdownHandler extends BaseHandler {
    public static final String OP = Consts.Operation.PREPARE_SHUTDOWN.toString();

    public PrepareShutdownHandler() {
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) throws BaseJsonHandler.StatusException {
        if (!user.isAdmin()) throw new BaseCahHandler.CahException(Consts.ErrorCode.NOT_ADMIN);

        PreparingShutdown.get().set(true);

        return JsonWrapper.EMPTY;
    }
}