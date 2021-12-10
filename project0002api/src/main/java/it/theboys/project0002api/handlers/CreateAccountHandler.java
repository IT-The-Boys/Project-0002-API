package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.data.accounts.UserAccount;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.BaseJsonHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.regex.Pattern;


public class CreateAccountHandler extends BaseHandler {
    public static final String OP = Consts.Operation.CREATE_ACCOUNT.toString();
    private final BanList banList;
    private final ConnectedUsers connectedUsers;
    private final UsersWithAccount accounts;
    private final Emails emails;

    public CreateAccountHandler(@Annotations.BanList BanList banList,
                                @Annotations.ConnectedUsers ConnectedUsers connectedUsers,
                                @Annotations.UsersWithAccount UsersWithAccount accounts,
                                @Annotations.Emails Emails emails) {
        this.banList = banList;
        this.connectedUsers = connectedUsers;
        this.accounts = accounts;
        this.emails = emails;
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) throws BaseJsonHandler.StatusException {
        if (banList.contains(exchange.getHostName()))
            throw new BaseCahHandler.CahException(Consts.ErrorCode.BANNED);

        PreparingShutdown.get().check();

        String nickname = params.getStringNotNull(Consts.UserData.NICKNAME);
        if (!Pattern.matches(Consts.VALID_NAME_PATTERN, nickname))
            throw new BaseCahHandler.CahException(Consts.ErrorCode.INVALID_NICK);
        if (connectedUsers.hasUser(nickname) || accounts.hasNickname(nickname))
            throw new BaseCahHandler.CahException(Consts.ErrorCode.NICK_IN_USE);

        UserAccount account;
        Consts.AuthType type;
        try {
            type = Consts.AuthType.parse(params.getStringNotNull(Consts.GeneralKeys.AUTH_TYPE));
        } catch (ParseException ex) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST, ex);
        }

        switch (type) {
            case PASSWORD:
                if (!emails.enabled()) throw new BaseCahHandler.CahException(Consts.ErrorCode.UNSUPPORTED_AUTH_TYPE);

                String email = params.getStringNotNull(Consts.UserData.EMAIL);
                if (email.isEmpty()) throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);

                if (accounts.hasEmail(email)) throw new BaseCahHandler.CahException(Consts.ErrorCode.EMAIL_IN_USE);

                String password = params.getStringNotNull(Consts.AuthType.PASSWORD);
                if (password.isEmpty()) throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);

                account = accounts.registerWithPassword(nickname, email, password);
                emails.sendEmailVerification(account);
                break;
            
            default:
                throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);
        }

        return account.toJson();
    }
}