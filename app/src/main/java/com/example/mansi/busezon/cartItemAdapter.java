package com.example.mansi.busezon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

/**
 * Created by mansi on 5/6/18.
 */

public class cartItemAdapter extends ArrayAdapter<cartItem> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ProgressDialog progressDialog;
    Button removeFromCart,moveToWishlist;
    List<cartItem> CartList;
    cartItemAdapter adapter;
    cartItem current ;

    public cartItemAdapter(Context context, List<cartItem> CartList) {

        super(context, 0, CartList);
        this.CartList = CartList;
        this.adapter = this;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cart_list_item, parent, false);
        }
        current = getItem(position);

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.cart_thumbnail);
        thumbNail.setImageUrl(current.getImage(), imageLoader);

        TextView name = convertView.findViewById(R.id.cart_name);
        String currentName = current.getName();
        name.setText("Product Name : "+currentName);

        TextView price = convertView.findViewById(R.id.cart_price);
        String P =current.getPrice()+"";
        price.setText("Price :" + P);

        TextView qty = convertView.findViewById(R.id.cart_qty);
        String Qty = current.getQty()+"";
        qty.setText("Qty : "+ Qty);

        removeFromCart = convertView.findViewById(R.id.rm_Cart);
        removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String response = server.removeFromBag(UserInformation.UserId,current.getId());
                Toast.makeText(AppController.getInstance(),response,Toast.LENGTH_LONG).show();
                CartList.remove(current);
                adapter.notifyDataSetChanged();
            }
        });

        moveToWishlist = convertView.findViewById(R.id.move);
        moveToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                server.removeFromBag(UserInformation.UserId,current.getId());
                CartList.remove(current);
                adapter.notifyDataSetChanged();
                String response = server.addToWishlist(UserInformation.UserId,current.getId());
                Toast.makeText(AppController.getInstance(),response,Toast.LENGTH_LONG).show();
            }
        });
        return convertView;
    }

}
