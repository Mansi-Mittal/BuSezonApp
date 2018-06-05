package com.example.mansi.busezon;

/**
 * Created by mansi on 5/6/18.
 */

public class cartItem {

    private String image;
    private String name;
    private int price;
    private int qty;

    public cartItem(String image, String name, int price, int qty) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }


    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }
}
