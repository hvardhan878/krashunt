package com.krashunt.krashunt2;

/**
 * Created by harsh on 2/10/2017.
 */

public class user {
    private String username;
    private String email;
    private String contact;
    private String coins;

    public user(){
      this.username ="username";
        this.email="email";
        this.contact="contact";
        this.coins="coins";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }
}
