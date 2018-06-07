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
import java.util.List;

public class offersAdapter extends ArrayAdapter<offers> {
    Dialog myDialog;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ProgressDialog progressDialog;
    offers currentOffer;
    int ID ;
    Button addToWish;
    View v;
    List<offers> offerList;
    private offersAdapter adapter;

    public offersAdapter(Context context, List<offers> offerList) {

        super(context, 0, offerList);
        myDialog = new Dialog(context);
        this.offerList = offerList;
        this.adapter = this;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        v = convertView;
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.offers_list_item, parent, false);
        }
        currentOffer = getItem(position);
        ID = currentOffer.getID();

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

            addToWish.setBackgroundResource(R.drawable.cart);
            addToWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowPopup(v);
                    server.removeFromWish(UserInformation.UserId,ID);
                    String response = server.addToBag(UserInformation.UserId, ID);
                    //Toast.makeText(AppController.getInstance(),response,Toast.LENGTH_LONG).show();
                    offerList.remove(currentOffer);
                    adapter.notifyDataSetChanged();

                }
            });
        }else if(context instanceof shoppingCart || context instanceof productDisplay){
            addToWish.setBackgroundResource(R.drawable.wishlist);
            addToWish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String response = server.addToWishlist(UserInformation.UserId, ID);
                    //Toast.makeText(AppController.getInstance(),response,Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                }
            });
        }else{
            addToWish.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        return convertView;
    }

    public void ShowPopup(View v) {
        TextView txtclose;
        myDialog.setContentView(R.layout.custompopup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("close");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }



}
