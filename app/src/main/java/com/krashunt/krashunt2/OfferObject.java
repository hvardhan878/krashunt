package com.krashunt.krashunt2;

/**
 * Created by harsh on 29/10/2017.
 */

public class OfferObject {

    private String content;
    private String cost;
    private String imageResource;
    private String location;
    private String stock;
    private String terms;

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public OfferObject(String cost, String content, String imageResource, String location, String stock, String terms) {
        this.content = content;
        this.imageResource = imageResource;
        this.location=location;

        this.cost = cost;
        this.stock = stock;


    }
public OfferObject(){}
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
