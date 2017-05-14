package com.minhtam.petsworld.Adapter;

import android.app.Activity;
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
    Activity context;

    public GridViewAdapter(Activity context,ArrayList<String> listImagePath,ArrayList<String> listSelectedItem) {
        this.listImagePath = listImagePath;
        this.context = context;
        this.listSelectedItem = listSelectedItem;
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
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_gallery_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    listSelectedItem.add(listImagePath.get(position));
                } else {
                    listSelectedItem.remove(listImagePath.get(position));
                }
            }
        });

        Picasso.with(context).load("file://"+listImagePath.get(position)).fit().centerCrop().placeholder(android.R.drawable.progress_horizontal).into(holder.img);
        return convertView;
    }

    private class ViewHolder {
        ImageView img;

        public ViewHolder(View v) {
            img = (ImageView) v.findViewById(R.id.imvGalleryItem);
        }
    }
}
