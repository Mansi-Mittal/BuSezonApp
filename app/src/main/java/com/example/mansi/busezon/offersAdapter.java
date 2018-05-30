package com.example.mansi.busezon;

import com.android.volley.toolbox.NetworkImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import java.util.List;

public class offersAdapter extends ArrayAdapter<offers> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public offersAdapter(Context context, List<offers> offerList) {
        super(context, 0, offerList);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(
                    R.layout.offers_list_item,parent,false);
        }
        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

        offers currentOffer=getItem(position);

        thumbNail.setImageUrl(currentOffer.getImage(), imageLoader);

        TextView desc=convertView.findViewById(R.id.desc);
        String description = currentOffer.getwords();
        desc.setText(description);
        return convertView;
    }
}
