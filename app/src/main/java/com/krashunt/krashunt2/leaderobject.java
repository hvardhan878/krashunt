package com.krashunt.krashunt2;

/**
 * Created by harsh on 6/10/2017.
 */

public class leaderobject {
    private String position;
    private String username;
    private String score;

    public leaderobject(String position,String username,String score)
    {
        this.position = position;
        this.username = username;
        this.score = score;
    }


    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
