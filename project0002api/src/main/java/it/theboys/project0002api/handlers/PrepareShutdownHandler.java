package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.BaseJsonHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.PreparingShutdown;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

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