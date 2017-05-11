package com.minhtam.petsworld.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.minhtam.petsworld.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by st on 3/20/2017.
 */

public class GridViewAdapter extends BaseAdapter {

    ArrayList<String> listImagePath;
    ArrayList<String> listSelectedItem;
    ImageAdapter adapterSelected;
    Activity context;

    public GridViewAdapter(Activity context,ArrayList<String> listImagePath,ArrayList<String> listSelectedItem,ImageAdapter adapterSelected ) {
        this.listImagePath = listImagePath;
        this.context = context;
        this.listSelectedItem = listSelectedItem;
        this.adapterSelected = adapterSelected;
    }

    @Override
    public int getCount() {
        return listImagePath.size();
    }

    @Override
    public Object getItem(int position) {
        return listImagePath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_gallery_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.imvGalleryItem);
            holder.img.setSelected(false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Position",position+"");
                int selectedPosition = listSelectedItem.indexOf(listImagePath.get(position));
                if ( selectedPosition != -1) {

                    listSelectedItem.remove(listImagePath.get(position));
                    holder.img.setBackgroundColor(Color.WHITE);

                    //Change adater
                    notifyDataSetChanged();
                    adapterSelected.notifyItemRemoved(selectedPosition);
                    adapterSelected.notifyItemRangeChanged(selectedPosition,listSelectedItem.size());
                } else {

                    listSelectedItem.add(0,listImagePath.get(position));
                    holder.img.setBackgroundResource(R.color.selector);

                    notifyDataSetChanged();
                    adapterSelected.notifyItemInserted(selectedPosition);
                    adapterSelected.notifyItemRangeChanged(selectedPosition,listSelectedItem.size());
                }

            }
        });

        Picasso.with(context).load("file://"+listImagePath.get(position)).fit().centerCrop().placeholder(android.R.drawable.progress_horizontal).into(holder.img);
        return convertView;
    }

    private class ViewHolder {
        ImageView img;
    }
}
