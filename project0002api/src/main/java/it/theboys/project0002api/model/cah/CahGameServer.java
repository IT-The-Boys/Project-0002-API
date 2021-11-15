package it.theboys.project0002api.model.cah;

import java.util.List;

import it.theboys.project0002api.enums.cah.CahGamePhase;
import it.theboys.project0002api.enums.cah.GameServerStatus;
import it.theboys.project0002api.model.database.cah.CahCard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CahGameServer {
    
    private String gameSettings;

    private GameEngine gameEngine;

    private GameServerStatus gameStatus;

    private String gameHost;

    private CahGamePhase gamePhase;

    private int gameDealer;

    private int gameTurn;

    private List<CahPlayer> playerList;

    private CahCard question;

    private int gameRound = 0;


    public String initialize(String settings, CahPlayer user){
        this.gameSettings=settings;
        gameHost=user.getPlayer();
        user.setPlayerSeat(playerList.size()+1);
        playerList.add(user);
        gameStatus=GameServerStatus.PENDING;
        
        return "Game Pending";
    }

    public String start() throws Exception{
        
        gameStatus = GameServerStatus.RUNNING;

        return "Game start";
    }

    public String join(){
        
        if(playerList.size() >= 3){
            gameStatus=GameServerStatus.RUNNING;
            newRound();
            return "game started";
        }
        return "player joined";
    }

    
    public String pickAnswer(CahPlayer playernum, List<CahCard> answer){
        for (CahPlayer player:playerList){
                player.getPlayerAnswer().size();

        }
        return "You pick This answer!!";
    }

    public String pickWinner(String playerId){    //Winner選択とポイント加算の処理
        //playerList[playerId].setPoints+1;
        for (CahPlayer player:playerList){
            player.clearAnswer();
        }
        gameRound++;
        newRound();
        return "Winner is you!!";
    }

    public String pause() throws Exception{
        
        gameStatus = GameServerStatus.PAUSED;

        return "Game pause";
    }

    private void newRound(){
        gameDealer=1;
        gamePhase=CahGamePhase.DRAW;
    }

    private void draw(int handSize){
        for (CahPlayer player:playerList){
            while(player.getPlayerHand().size() <= handSize){
                player.draw(new CahCard());
            }
        }
        question=new CahCard();
        gamePhase=CahGamePhase.ANSWER_PICKING;
    }

}