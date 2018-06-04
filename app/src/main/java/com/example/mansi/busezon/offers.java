package com.example.mansi.busezon;

/**
 * Created by mansi on 21/2/18.
 */

public class offers {

    private String image;
    private String words;
    private int id;

    public offers(int id,String image, String words){
        this.id = id;
        this.image=image;
        this.words=words;
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
}
