package com.tibbiodev.diyabetim.models;

/**
 * Created by User on 15.12.2016.
 */
public class UsefulInfo {

    private String title;
    private String link;
    private String summary;

    public UsefulInfo(){

    }

    public UsefulInfo(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
