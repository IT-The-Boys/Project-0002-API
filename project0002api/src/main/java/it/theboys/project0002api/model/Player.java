package it.theboys.project0002api.model;

import lombok.Data;

@Data
public class Player {
    private String username;

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
}