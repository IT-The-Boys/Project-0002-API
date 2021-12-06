package it.theboys.project0002api.server;

import java.io.IOException;

import javax.security.auth.login.Configuration.Parameters;



import org.springframework.boot.autoconfigure.web.reactive.WebFluxProperties.Cookie;
import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;


import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;

public abstract class BaseCahHandler extends BaseJsonHandler {

    @Override
    protected JsonElement handle(HttpServerExchange exchange) throws StatusException {
        Cookie sid = exchange.getRequestCookies().get("PYX-Session");
        User user = null;
        if (sid != null) user = Sessions.get().getUser(sid.getValue());

        Parameters params;
        try {
            params = Parameters.fromExchange(exchange);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new StatusException(StatusCodes.INTERNAL_SERVER_ERROR, ex);
        }

        String op = params.getStringNotNull(Consts.GeneralKeys.OP);
        if (!Handlers.skipUserCheck(op) && user == null) {
            throw new CahException(Consts.ErrorCode.NOT_REGISTERED);
        } else if (user != null && !user.isValid()) {
            Sessions.get().invalidate(sid.getValue());
            throw new CahException(Consts.ErrorCode.SESSION_EXPIRED);
        } else {
            return handleRequest(op, user, params, exchange);
        }
    }

    protected abstract JsonElement handleRequest(@Nullable String op, @Nullable User user, Parameters params, HttpServerExchange exchange) throws StatusException;

    public static class CahException extends StatusException {
        public final Consts.ErrorCode code;
        public final JsonWrapper data;

        public CahException(Consts.ErrorCode code) {
            super(StatusCodes.CONFLICT);
            this.code = code;
            this.data = null;
        }

        public CahException(Consts.ErrorCode code, Throwable cause) {
            super(StatusCodes.CONFLICT, cause);
            this.code = code;
            this.data = null;
        }

        public CahException(Consts.ErrorCode code, JsonWrapper data) {
            super(StatusCodes.CONFLICT);
            this.code = code;
            this.data = data;
        }

        @Override
        public String getMessage() {
            return code + (data != null ? (": " + data.toString()) : "");
        }
    }
}