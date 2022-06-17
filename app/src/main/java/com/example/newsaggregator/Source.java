package com.example.newsaggregator;

import java.io.Serializable;

public class Source implements Serializable {

    public String id;
    public String name;
    public String category;

    public Source(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

}
