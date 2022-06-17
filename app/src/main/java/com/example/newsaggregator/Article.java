package com.example.newsaggregator;

import java.io.Serializable;

public class Article implements Serializable {

    public String author;
    public String title;
    public String desc;
    public String url;
    public String imageUrl;
    public String published;

    public Article(String author, String title, String desc, String url, String imageUrl,String published) {
        this.author = author;
        this.title = title;
        this.desc = desc;
        this.url = url;
        this.imageUrl = imageUrl;
        this.published = published;
    }
}
