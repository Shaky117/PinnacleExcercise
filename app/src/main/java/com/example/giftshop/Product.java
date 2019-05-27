package com.example.giftshop;

public class Product {
    private int id;
    private String name;
    private int price;
    private String description;
    private String category;

    public Product(int id, String name, int price, String description, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public Product(String name, int price, String description, String category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }



}
