package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.data.accounts.PasswordAccount;
import it.theboys.project0002api.data.accounts.UserAccount;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.BaseJsonHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.*;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.CookieImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mindrot.jbcrypt.BCrypt;

import java.text.ParseException;
import java.util.regex.Pattern;

public class RegisterHandler extends BaseHandler {
    public static final String OP = Consts.Operation.REGISTER.toString();
    private final BanList banList;
    private final ConnectedUsers users;

    public RegisterHandler(@Annotations.BanList BanList banList,
                           @Annotations.UsersWithAccount UsersWithAccount accounts,
                           @Annotations.ConnectedUsers ConnectedUsers users) {
        this.banList = banList;
        this.accounts = accounts;
        this.users = users;
    }

    @NotNull
    @Override
    public JsonWrapper handle(@Nullable User user, Parameters params, HttpServerExchange exchange) throws BaseJsonHandler.StatusException {
        if (banList.contains(exchange.getHostName()))
            throw new BaseCahHandler.CahException(Consts.ErrorCode.BANNED);

        PreparingShutdown.get().check();

        Consts.AuthType type;
        try {
            type = Consts.AuthType.parse(params.getStringNotNull(Consts.GeneralKeys.AUTH_TYPE));
        } catch (ParseException ex) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST, ex);
        }

        UserAccount account;
        String nickname;
        switch (type) {
            case PASSWORD:
                nickname = params.getStringNotNull(Consts.UserData.NICKNAME);
                if (!Pattern.matches(Consts.VALID_NAME_PATTERN, nickname))
                    throw new BaseCahHandler.CahException(Consts.ErrorCode.INVALID_NICK);

                account = accounts.getPasswordAccountForNickname(nickname);
                if (account == null) { // Without account
                    user = new User(nickname, exchange.getHostName(), Sessions.generateNewId());
                } else {
                    String password = params.getStringNotNull(Consts.AuthType.PASSWORD);
                    if (password.isEmpty() || !BCrypt.checkpw(password, ((PasswordAccount) account).hashedPassword))
                        throw new BaseCahHandler.CahException(Consts.ErrorCode.WRONG_PASSWORD);

                    user = User.withAccount(account, exchange.getHostName());
                }
                break;
            case GOOGLE:
                if (!socialLogin.googleEnabled())
                    throw new BaseCahHandler.CahException(Consts.ErrorCode.UNSUPPORTED_AUTH_TYPE);

                GoogleIdToken.Payload googleToken = socialLogin.verifyGoogle(params.getStringNotNull(Consts.AuthType.GOOGLE));
                if (googleToken == null) throw new BaseCahHandler.CahException(Consts.ErrorCode.GOOGLE_INVALID_TOKEN);

                account = accounts.getGoogleAccount(googleToken);
                if (account == null) throw new BaseCahHandler.CahException(Consts.ErrorCode.GOOGLE_NOT_REGISTERED);

                nickname = account.username;
                user = User.withAccount(account, exchange.getHostName());
                break;
            case FACEBOOK:
                if (!socialLogin.facebookEnabled())
                    throw new BaseCahHandler.CahException(Consts.ErrorCode.UNSUPPORTED_AUTH_TYPE);

                FacebookToken facebookToken = socialLogin.verifyFacebook(params.getStringNotNull(Consts.AuthType.FACEBOOK));
                if (facebookToken == null)
                    throw new BaseCahHandler.CahException(Consts.ErrorCode.FACEBOOK_INVALID_TOKEN);

                account = accounts.getFacebookAccount(facebookToken);
                if (account == null) throw new BaseCahHandler.CahException(Consts.ErrorCode.FACEBOOK_NOT_REGISTERED);

                nickname = account.username;
                user = User.withAccount(account, exchange.getHostName());
                break;
            case GITHUB:
                if (!socialLogin.githubEnabled())
                    throw new BaseCahHandler.CahException(Consts.ErrorCode.UNSUPPORTED_AUTH_TYPE);

                String githubToken = params.getStringNotNull(Consts.AuthType.GITHUB);

                GithubProfileInfo githubInfo = socialLogin.infoGithub(githubToken);
                account = accounts.getGithubAccount(githubInfo);
                if (account == null) throw new BaseCahHandler.CahException(Consts.ErrorCode.GITHUB_NOT_REGISTERED);

                nickname = account.username;
                user = User.withAccount(account, exchange.getHostName());
                break;
            case TWITTER:
                if (!socialLogin.twitterEnabled())
                    throw new BaseCahHandler.CahException(Consts.ErrorCode.UNSUPPORTED_AUTH_TYPE);

                String twitterTokens = params.getStringNotNull(Consts.AuthType.TWITTER);

                TwitterProfileInfo twitterInfo = socialLogin.infoTwitter(twitterTokens);
                account = accounts.getTwitterAccount(twitterInfo);
                if (account == null) throw new BaseCahHandler.CahException(Consts.ErrorCode.TWITTER_NOT_REGISTERED);

                nickname = account.username;
                user = User.withAccount(account, exchange.getHostName());
                break;
            default:
                throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);
        }

        User registeredUser = users.checkAndAdd(user);
        if (registeredUser != null) user = registeredUser;
        exchange.setResponseCookie(new CookieImpl("PYX-Session", Sessions.get().add(user)));

        return new JsonWrapper()
                .add(Consts.UserData.NICKNAME, nickname)
                .add(Consts.UserData.IS_ADMIN, user.isAdmin());
    }
}