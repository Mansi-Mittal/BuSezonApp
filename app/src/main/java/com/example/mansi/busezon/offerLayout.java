package com.example.mansi.busezon;

import android.widget.Button;

/**
 * Created by mansi on 5/6/18.
 */

public class offerLayout {

    private int image ;
    private Button like;

    public offerLayout(int image, Button like) {
        this.image = image;
        this.like = like;
    }


    public int getImage() {
        return image;
    }

    public Button getLike() {
        return like;
    }
}
