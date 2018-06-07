package com.example.mansi.busezon;

/**
 * Created by mansi on 21/2/18.
 */

public class offers {

    private String image;
    private String words;
    private int id;
    private String sellerID;
    private int price;

    public offers(int id, String image, String words, String sellerID, int price){
        this.id = id;
        this.image=image;
        this.words=words;
        this.sellerID = sellerID;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public String getwords() {
        return words;
    }

    public int getID() {
        return id;
    }

    public String getSellerID() {
        return sellerID;
    }

    public int getPrice() {
        return price;
    }
}
