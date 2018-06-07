package com.example.mansi.busezon;

import com.android.volley.toolbox.NetworkImageView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class offersAdapter extends ArrayAdapter<offers> {
    Dialog myDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ProgressDialog progressDialog;
    int sellerID ;
    int ID ;
    Button addToWish;
    View v;

    public offersAdapter(Context context, List<offers> offerList) {

        super(context, 0, offerList);
        myDialog = new Dialog(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        v = convertView;
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.offers_list_item, parent, false);
        }
        final offers currentOffer = getItem(position);
        //final int sellerID = currentOffer.getSellerID();
        final int ID = currentOffer.getID();

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
        thumbNail.setImageUrl(currentOffer.getImage(), imageLoader);

        TextView desc = convertView.findViewById(R.id.desc);
        String description = currentOffer.getwords();
        desc.setText(description);

        TextView price = convertView.findViewById(R.id.price);
        String prodPrice = currentOffer.getPrice()+ "";
        price.setText(prodPrice);

        addToWish = convertView.findViewById(R.id.AddToWishlist);

        Context context = getContext();
        if(context instanceof WishlistActivity) {
            //addToWish.setVisibility(View.GONE);
            addToWish.setBackgroundResource(R.drawable.cart);
            addToWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    server.removeFromWish(UserInformation.UserId,ID);
                    if(server.checkIfAlreadyExistCart(UserInformation.UserId,currentOffer.getID())) {
                        Toast.makeText(AppController.getInstance(),"Product already in cart",Toast.LENGTH_LONG).show();
                    }else{
                        server.addToBag(UserInformation.UserId, ID);
                    }
                    //ShowPopup(v);
                }
            });
        }else if(context instanceof SellHomepage){
            addToWish.setVisibility(View.GONE);
        }else{
            addToWish.setBackgroundResource(R.drawable.wishlist);
            addToWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(server.checkIfAlreadyExist(UserInformation.UserId,currentOffer.getID())){
                        Toast.makeText(AppController.getInstance(),"Product already in wishlist",Toast.LENGTH_LONG).show();
                    }else{
                        server.addToWishlist(UserInformation.UserId,currentOffer.getID());
                    }


                }
            });
        }

        return convertView;
    }

    public void ShowPopup(View v) {
        TextView txtclose;
        myDialog.setContentView(R.layout.custompopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("close");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }



}
