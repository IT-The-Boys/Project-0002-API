package it.theboys.project0002api.model.cah;

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.theboys.project0002api.enums.cah.GameServerStatus;
import it.theboys.project0002api.model.Card;
import it.theboys.project0002api.model.Player;

@Data
public class GameEngine {

   private static final Logger logger = LogManager.getLogger(GameEngine.class);

   private String serverHost;
   private String currentQuestion;
   private String gameServer;
   private String serverId;
   private String serverName;
   private String settings;

   private int currentRound;
   private int currentKaiser;

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

   public void setCurrentKaiser(int kaiser){
       this.currentKaiser = kaiser;
   }
   public int getCurrentKaiser(){
       return currentKaiser;
   }

   public void choseWhiteCard(){
       Player player = new Player();
       for(int i = 0; i <= player.playerList.size(); i++){
           //playerがwhiteカードを選ぶ処理
       }
   }

   public void judgeCard(Player player, Card whiteCard){
       //select winner
   }

   public void nextKaiser(){
       //winnerを次のKaizerに設定
   }


   private GameServerStatus status;

   public void run(){

   }

   public String getCards(final Player player){
       return "test card";
   }

   public void start() throws Exception{
       status = GameServerStatus.RUNNING;
        for(final Player player : this.players){
            if(player.getCards().size() > 0){
                for (int i = 0; i < player.getCards.size(); i++){
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
        this.kaiser = this.players.get(0);
        this.nextRound();
   }


   public String pause() throws Exception{

       status = GameServerStatus.PAUSED;

       return "Game pause";
   }

    public String end(){

        status = GameServerStatus.TERMINATED;

        return "Game over.";
    }
   
}