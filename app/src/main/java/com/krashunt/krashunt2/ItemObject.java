package com.krashunt.krashunt2;

/**
 * Created by harsh on 23/9/2017.
 */

public class ItemObject {

    private String content;
    private String imageResource;
    private String imageResourcee;
    private String cost;
    private String details;
    private String deadline;

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public ItemObject(String cost, String details, String content, String imageResource, String imageResourcee, String deadline) {
        this.content = content;
        this.imageResource = imageResource;
        this.imageResourcee = imageResourcee;
        this.cost = cost;
        this.details = details;
        this.deadline = deadline;
    }
    public ItemObject() {

    }

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public String getImageResourcee() {
        return imageResourcee;
    }

    public void setImageResourcee(String imageResourcee) {
        this.imageResourcee = imageResourcee;
    }
}
