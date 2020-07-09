package com.karol.piekarski.mydiabetics_dzienniczek.Java.Model;

public class Notes {

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Notes() {}

    public Notes(String title, String content)
    {
        this.title = title;
        this.content = content;
    }
}
