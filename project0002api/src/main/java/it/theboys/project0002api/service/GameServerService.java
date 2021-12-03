package it.theboys.project0002api.service;

import java.util.UUID;
import org.springframework.stereotype.Service;

import it.theboys.project0002api.exception.InvalidChatException;
import it.theboys.project0002api.exception.InvalidGameException;
import it.theboys.project0002api.exception.InvalidParamException;
import it.theboys.project0002api.exception.NotFoundException;
import it.theboys.project0002api.model.Game;
import it.theboys.project0002api.model.GamePlay;
import it.theboys.project0002api.model.GameStatus;
import it.theboys.project0002api.model.Player;
import it.theboys.project0002api.storage.GameStorage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameServerService {

    private final ChatService chatService;

    public Game createGame(Player player) {
        Game game = new Game();
        game.setGameId(UUID.randomUUID().toString());
        game.addPlayer(player);
        game.setStatus(GameStatus.NEW);
        game.setChat(chatService.createChat(player));
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game connectToGame(Player player, String gameId, String chatId) throws InvalidParamException, InvalidGameException, InvalidChatException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Game with privided id doesn't exist");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);

        game.addPlayer(player);
        chatService.connectToChat(player, chatId);
        game.setStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundException("Game not found");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new InvalidGameException("Game is already finished");
        }
        GameStorage.getInstance().setGame(game);
        return game;
    }
}
