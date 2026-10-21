package com.example.thuchanh2;

import java.io.Serializable;

public class Book implements Serializable {
    private String name;
    private String author;
    private String category;
    private String status;
    private float rating;
    private String notes;

    public Book(String name, String author, String category, String status, float rating, String notes) {
        this.name = name;
        this.author = author;
        this.category = category;
        this.status = status;
        this.rating = rating;
        this.notes = notes;
    }

    public String getName() { return name; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public float getRating() { return rating; }
    public String getNotes() { return notes; }

    public void setName(String name) { this.name = name; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }
    public void setStatus(String status) { this.status = status; }
    public void setRating(float rating) { this.rating = rating; }
    public void setNotes(String notes) { this.notes = notes; }
}
