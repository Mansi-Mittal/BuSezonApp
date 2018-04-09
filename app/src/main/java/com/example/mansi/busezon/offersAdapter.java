package com.example.mansi.busezon;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mansi on 21/2/18.
 */

public class offersAdapter extends ArrayAdapter<offers> {

    public offersAdapter(Context context, List<offers> offerList) {
        super(context, 0, offerList);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View listItemView=convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(
                    R.layout.offers_list_item,parent,false);
        }

        offers currentOffer=getItem(position);

        TextView desc=(TextView)listItemView.findViewById(R.id.desc);
        String description = currentOffer.getwords();
        desc.setText(description);

        ImageView img = (ImageView)listItemView.findViewById(R.id.img);
        int image=currentOffer.getImage();
        img.setImageResource(image);

        return listItemView;
    }
}
