package com.example.mansi.busezon;

/**
 * Created by mansi on 5/6/18.
 */

public class cartItem {

    private String image;
    private String name;
    private String price;
    private String qty;

    public cartItem(String image, String name, String price, String qty) {
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

    public String getPrice() {
        return price;
    }

    public String getQty() {
        return qty;
    }
}
