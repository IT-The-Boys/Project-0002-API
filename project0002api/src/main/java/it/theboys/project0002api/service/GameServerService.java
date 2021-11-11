package it.theboys.project0002api.service;

import java.util.UUID;
import org.springframework.stereotype.Service;
import it.theboys.project0002api.exception.InvalidGameException;
import it.theboys.project0002api.exception.InvalidParamException;
import it.theboys.project0002api.exception.NotFoundException;
import it.theboys.project0002api.model.Game;
import it.theboys.project0002api.model.GamePlay;
import it.theboys.project0002api.model.GameStatus;
import it.theboys.project0002api.model.Player;
import it.theboys.project0002api.model.TicToe;
import it.theboys.project0002api.storage.GameStorage;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameServerService {

    public Game createGame(Player player) {
        Game game = new Game();
        game.setBoard(new int[3][3]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player);
        game.setStatus(GameStatus.NEW);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game connectToGame(Player player2, String gameId) throws InvalidParamException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gameId)) {
            throw new InvalidParamException("Game with privided id doesn't exist");
        }
        Game game = GameStorage.getInstance().getGames().get(gameId);

        if (game.getPlayer2() != null) {
            throw new InvalidGameException("Game is not valid anymore");
        }

        game.setPlayer2(player2);
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

        // DONT NEED ALL THIS SHIT
        int[][] board = game.getBoard();
        board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getType().getValue();

        Boolean xWinner = checkWinner(game.getBoard(), TicToe.X);
        Boolean oWinner = checkWinner(game.getBoard(), TicToe.O);

        if (xWinner) {
            game.setWinner(TicToe.X);
        } else if (oWinner) {
            game.setWinner(TicToe.O);
        }
        //Maybe need this lol
        GameStorage.getInstance().setGame(game);
        return game;
    }

    //REALLY FUCKING DONT NEED THIS, JUST WIN CHECK FOR TICTACTOE GAME
    private Boolean checkWinner(int[][] board, TicToe ticToe) {
        int[] boardArray = new int[9];
        int counterIndex = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardArray[counterIndex] = board[i][j];
                counterIndex++;
            }
        }

        int[][] winCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
        for(int i = 0; i < winCombinations.length; i++){
            int counter = 0;
            for(int j = 0; j < winCombinations[i].length; j++){
                if(boardArray[winCombinations[i][j]] == ticToe.getValue()){
                    counter++;
                    if(counter == 3){
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
