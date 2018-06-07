package com.example.mansi.busezon;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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

    public cartItemAdapter(Context context, List<cartItem> CartList) {

        super(context, 0, CartList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.cart_list_item, parent, false);
        }
        final cartItem current = getItem(position);

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
                server.removeFromBag(UserInformation.UserId,current.getId());
                shoppingCart s =new shoppingCart();
                //s.reload();
            }
        });

        moveToWishlist = convertView.findViewById(R.id.move);
        moveToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!server.checkIfAlreadyExist(UserInformation.UserId,current.getId())){
                    server.addToWishlist(UserInformation.UserId,current.getId());
                }
            }
        });
        return convertView;
    }

}
