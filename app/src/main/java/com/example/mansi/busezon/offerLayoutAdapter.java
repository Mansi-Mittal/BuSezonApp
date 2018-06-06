package com.example.mansi.busezon;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by mansi on 5/6/18.
 */

public class offerLayoutAdapter extends ArrayAdapter<offerLayout> {


    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ProgressDialog progressDialog;

    public offerLayoutAdapter(Context context, List<offerLayout> offerList) {

        super(context, 0, offerList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.offer_layout, parent, false);
        }
        offerLayout current = getItem(position);

        ImageView thumbNail = convertView.findViewById(R.id.thumbnail);
        thumbNail.setImageResource(current.getImage());

        /*TextView desc = convertView.findViewById(R.id.desc);
        String description = current.getName();
        desc.setText(description);*/

        return convertView;
    }
}
