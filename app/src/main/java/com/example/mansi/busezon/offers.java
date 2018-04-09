package com.example.mansi.busezon;

import android.net.Uri;

/**
 * Created by mansi on 21/2/18.
 */

public class offers {

    private int image;
    private String words;

    public offers(int image, String words){
        this.image=image;
        this.words=words;
    }

    public int getImage() {
        return image;
    }

    public String getwords() {
        return words;
    }
}
