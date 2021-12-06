package it.theboys.project0002api.singletons;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.catalina.authenticator.jaspic.PersistentProviderRegistrations.Providers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.handlers.BanHandler;
import it.theboys.project0002api.handlers.BaseHandler;
import it.theboys.project0002api.handlers.CardcastAddCardsetHandler;
import it.theboys.project0002api.handlers.CardcastListCardsetsHandler;
import it.theboys.project0002api.handlers.CardcastRemoveCardsetHandler;
import it.theboys.project0002api.handlers.ChangeGameOptionsHandler;
import it.theboys.project0002api.handlers.ChatHandler;
import it.theboys.project0002api.handlers.CreateAccountHandler;
import it.theboys.project0002api.handlers.CreateGameHandler;
import it.theboys.project0002api.handlers.DislikeGameHandler;
import it.theboys.project0002api.handlers.FirstLoadHandler;
import it.theboys.project0002api.handlers.GameChatHandler;
import it.theboys.project0002api.handlers.GameListHandler;
import it.theboys.project0002api.handlers.GameOptionsSuggestionDecisionHandler;
import it.theboys.project0002api.handlers.GetCardsHandler;
import it.theboys.project0002api.handlers.GetGameInfoHandler;
import it.theboys.project0002api.handlers.GetMeHandler;
import it.theboys.project0002api.handlers.GetSuggestedGameOptionsHandler;
import it.theboys.project0002api.handlers.GetUserPreferencesHandler;
import it.theboys.project0002api.handlers.JoinGameHandler;
import it.theboys.project0002api.handlers.JudgeSelectHandler;
import it.theboys.project0002api.handlers.KickHandler;
import it.theboys.project0002api.handlers.LeaveGameHandler;
import it.theboys.project0002api.handlers.LikeGameHandler;
import it.theboys.project0002api.handlers.LogoutHandler;
import it.theboys.project0002api.handlers.NamesHandler;
import it.theboys.project0002api.handlers.PlayCardHandler;
import it.theboys.project0002api.handlers.PongHandler;
import it.theboys.project0002api.handlers.PrepareShutdownHandler;
import it.theboys.project0002api.handlers.RegisterHandler;
import it.theboys.project0002api.handlers.SetUserPreferencesHandler;
import it.theboys.project0002api.handlers.SpectateGameHandler;
import it.theboys.project0002api.handlers.StartGameHandler;
import it.theboys.project0002api.handlers.StopGameHandler;
import it.theboys.project0002api.server.BaseCahHandler;

public final class Handlers {
    private final static Map<String, Class<? extends BaseHandler>> LIST;
    private final static List<String> SKIP_USER_CHECK;
    private static final Map<Class<? extends BaseHandler>, BaseHandler> handlers = new HashMap<>();

    static {
        LIST = new HashMap<>();
        LIST.put(BanHandler.OP, BanHandler.class);
        LIST.put(CardcastAddCardsetHandler.OP, CardcastAddCardsetHandler.class);
        LIST.put(CardcastListCardsetsHandler.OP, CardcastListCardsetsHandler.class);
        LIST.put(CardcastRemoveCardsetHandler.OP, CardcastRemoveCardsetHandler.class);
        LIST.put(ChangeGameOptionsHandler.OP, ChangeGameOptionsHandler.class);
        LIST.put(ChatHandler.OP, ChatHandler.class);
        LIST.put(CreateGameHandler.OP, CreateGameHandler.class);
        LIST.put(FirstLoadHandler.OP, FirstLoadHandler.class);
        LIST.put(GameChatHandler.OP, GameChatHandler.class);
        LIST.put(GameListHandler.OP, GameListHandler.class);
        LIST.put(GetCardsHandler.OP, GetCardsHandler.class);
        LIST.put(GetGameInfoHandler.OP, GetGameInfoHandler.class);
        LIST.put(JoinGameHandler.OP, JoinGameHandler.class);
        LIST.put(JudgeSelectHandler.OP, JudgeSelectHandler.class);
        LIST.put(KickHandler.OP, KickHandler.class);
        LIST.put(LeaveGameHandler.OP, LeaveGameHandler.class);
        LIST.put(LogoutHandler.OP, LogoutHandler.class);
        LIST.put(NamesHandler.OP, NamesHandler.class);
        LIST.put(PlayCardHandler.OP, PlayCardHandler.class);
        LIST.put(RegisterHandler.OP, RegisterHandler.class);
        LIST.put(SpectateGameHandler.OP, SpectateGameHandler.class);
        LIST.put(StartGameHandler.OP, StartGameHandler.class);
        LIST.put(StopGameHandler.OP, StopGameHandler.class);
        LIST.put(LikeGameHandler.OP, LikeGameHandler.class);
        LIST.put(DislikeGameHandler.OP, DislikeGameHandler.class);
        LIST.put(GetMeHandler.OP, GetMeHandler.class);
        LIST.put(PongHandler.OP, PongHandler.class);
        LIST.put(GameOptionsSuggestionDecisionHandler.OP, GameOptionsSuggestionDecisionHandler.class);
        LIST.put(GetSuggestedGameOptionsHandler.OP, GetSuggestedGameOptionsHandler.class);
        LIST.put(CreateAccountHandler.OP, CreateAccountHandler.class);
        LIST.put(PrepareShutdownHandler.OP, PrepareShutdownHandler.class);
        LIST.put(GetUserPreferencesHandler.OP, GetUserPreferencesHandler.class);
        LIST.put(SetUserPreferencesHandler.OP, SetUserPreferencesHandler.class);

        SKIP_USER_CHECK = new ArrayList<>();
        SKIP_USER_CHECK.add(RegisterHandler.OP);
        SKIP_USER_CHECK.add(FirstLoadHandler.OP);
        SKIP_USER_CHECK.add(CreateAccountHandler.OP);
    }

    public static boolean skipUserCheck(String op) {
        return SKIP_USER_CHECK.contains(op);
    }

    @NotNull
    public static BaseHandler obtain(String op) throws BaseCahHandler.CahException {
        BaseHandler handler;
        Class<? extends BaseHandler> cls = Handlers.LIST.get(op);
        if (cls != null) {
            handler = handlers.get(cls);
            if (handler == null) {
                try {
                    Constructor<?> constructor = cls.getConstructors()[0];
                    Parameter[] parameters = constructor.getParameters();
                    Object[] objects = new Object[parameters.length];

                    for (int i = 0; i < parameters.length; i++)
                        objects[i] = Providers.get(parameters[i].getAnnotations()[0].annotationType()).get();

                    handler = (BaseHandler) constructor.newInstance(objects);
                    handlers.put(cls, handler);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_OP, ex);
                }
            }
        } else {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_OP);
        }

        return handler;
    }
}