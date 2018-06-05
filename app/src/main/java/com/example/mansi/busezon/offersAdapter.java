package com.example.mansi.busezon;

import com.android.volley.toolbox.NetworkImageView;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class offersAdapter extends ArrayAdapter<offers> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ProgressDialog progressDialog;
    int sellerID ;
    int ID ;
    Button addToWish;

    public offersAdapter(Context context, List<offers> offerList) {

        super(context, 0, offerList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.offers_list_item, parent, false);
        }
        offers currentOffer = getItem(position);
        final int sellerID = currentOffer.getSellerID();
        final int ID = currentOffer.getID();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        thumbNail.setImageUrl(currentOffer.getImage(), imageLoader);

        TextView desc = convertView.findViewById(R.id.desc);
        String description = currentOffer.getwords();
        desc.setText(description);

        addToWish = convertView.findViewById(R.id.AddToWishlist);

        Context context = getContext();
        if(context instanceof WishlistActivity) {
            //addToWish.setVisibility(View.GONE);
            addToWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //progressDialog.show();
                    server.addToBag(sellerID,ID);
                }
            });
        }else{
            addToWish.setText("<3");
            addToWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //progressDialog.show();
                    server.addToWishlist(sellerID,ID);
                }
            });
        }

        return convertView;
    }



}
