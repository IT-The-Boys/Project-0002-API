package it.theboys.project0002api.model.cah;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import it.theboys.project0002api.enums.cah.GameServerStatus;
import it.theboys.project0002api.model.card.BlackCard;
import it.theboys.project0002api.model.card.WhiteCard;
import lombok.Data;

@Data
public class GameEngine {

    public static void main(final String[] args){
        System.out.println("Starting...");
        new GameEngine();
    }

    private String serverHost;
    private String currentQuestion;
    private String gameServer;
    private String serverId;
    private String serverName;
    private String settings;
    public ArrayList<Player> players;
    public ArrayList<Player> playerIter;
    public List<WhiteCard> whiteCards;
    public ArrayList<BlackCard> blackCards;
    public Player czar;
    public GameServerStatus gameStatus;
    
    private int currentRound;
    private GameServerStatus status;
    private Player player;


    public void setServerHost(String Host){
        this.serverHost = Host;
    }
    public String getServerHost(){
        return serverHost;
    }

    public void setCurrentQuestion(String CardQuestion){
        this.currentQuestion = CardQuestion;
    }
    public String getCurrentQuestion(){
        return currentQuestion;
    }

    public void setGameServer(String WebSocketServer){
        this.gameServer = WebSocketServer;
    }
    public String getGameServer(){
        return gameServer;
    }

    public void setSettings(String settings){
        this.settings = settings;
    }
    public String getSettings(){
        return settings;
    }

    public void setServerId(String uuid){
        this.serverId = uuid;
    }
    public String getServerId(){
        return serverId;
    }

    public void setServerName(String serverName){
        this.serverName = serverName;
    }
    public String getServerName(){
        return serverName;
    }

    public void setCurrentRound(int round){
        this.currentRound = round;
    }
    public int getCurrentrRound(){
        return currentRound;
    }

    public void checkNext(){
        if(this.proceedToNext()){
            Collections.shuffle(this.players);
            this.playerIter = new ArrayList<Player>(this.players);
            this.playerIter.remove(this.czar);
            for(int i = 0; i < this.playerIter.size(); i++){
                final Player player = this.playerIter.get(i);
                if(player.isWaiting()){
                    this.playerIter.remove(player);
                }
            }
            this.status = GameServerStatus.CZAR_TURN;
        }
    }

    public boolean proceedToNext(){
        for (final Player player : this.players){
            if (!player.hasPlayedCards() && !player.isCzar() && !player.isWaiting()){
                return false;
            }
        }
        return true;
    }

    public Player getPlayer(final String username){
        for(final Player player : this.players){
            if(player.getName().equals(username)){
                return player;
            }
        }
        return null;
    }


    public String getCards(final Player player){
        this.player = player;
        final StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

    public Player getCzar(){
        Collections.shuffle(this.players);
        if(this.players.get(0) == this.czar){
            return this.getCzar();
        }
        return this.players.get(0);
    }

    public void nextRound(){
        if(this.players.size() < 3){
            this.status = GameServerStatus.NOT_ENOUGH_PLAYERS;
            return;
        }
    }

    //カード準備
    private void setupCards(){
        this.players = new ArrayList<Player>();
        this .whiteCards = Collections.synchronizedList(new ArrayList<WhiteCard>());
        this.blackCards = new ArrayList<BlackCard>();
    }

    public void start() throws Exception{
        status = GameServerStatus.RUNNING;
        for(final Player player : this.players){
            if(player.getCards().size() > 0){
                for (int i = 0; i < player.getCards().size(); i++){
                    this.whiteCards.add(player.getCards().get(0));
                    player.getCards().remove(0);
                }
            }
            player.drawCardsForStart();
        }
        String[] users = new String[players.size()];
        for(int i = 0; i < players.size(); i++){

            users[i] = players.get(i).getName();
        }
        this.czar = this.players.get(0);
        this.nextRound();
    }
}