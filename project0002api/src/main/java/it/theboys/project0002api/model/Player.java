package it.theboys.project0002api.model;

import java.util.*;

import lombok.Data;

@Data
public class Player {
    public static Object players;
    private String playername;
    private String password;
    private String email;
    private String avatar_url;
    private int score = 0;
    
    public List<Integer> playerList = new ArrayList<Integer>(10);

    public void setPlayerrname(String playername){
        this.playername = playername;
    }
    public String getPlayername(){
        return playername;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }

    public void setAvatar_Url(String avatar_url){
        this.avatar_url = avatar_url;
    }
    public String getAvatar_Url(){
        return avatar_url;
    }

    public int getScore(){
        return score;
    }

    public void increaseScore(){
        score++;
    }

    public void resetScore(){
        score = 0;
    }
}