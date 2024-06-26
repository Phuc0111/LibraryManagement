package model;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String title;
    private String authors;
    private int quantity;

    public Book(int id, String title, String authors, int quantity) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
