package com.minhtam.petsworld.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.Dialog.MyImagViewDialog;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by st on 5/23/2017.
 */


public class AdapterListPhoto extends RecyclerView.Adapter<AdapterListPhoto.MyViewHolder> {
    private ArrayList<Photo> listPickedImage;
    private Context context;

    public AdapterListPhoto(Context context, ArrayList<Photo> listPickedImage) {
        super();
        this.context = context;
        this.listPickedImage = listPickedImage;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_gallery_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AdapterListPhoto.MyViewHolder holder, final int position) {

        if (listPickedImage.get(position).getUrl() != null) {
            Log.d("URL",listPickedImage.get(position).getUrl());
            Picasso.with(context).load(WebserviceAddress.WEB_ADDRESS+listPickedImage.get(position).getUrl()).fit().placeholder(R.drawable.progress_image).into(holder.img);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return listPickedImage.size();
    }


    public void setListPickedImage(ArrayList<Photo> listImage) {
        this.listPickedImage = listImage;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.imvGalleryItem);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyImagViewDialog dialog = new MyImagViewDialog(context,R.layout.dialog_view_image,listPickedImage.get(getAdapterPosition()).getUrl());
                    dialog.ShowDialog();                }
            });
        }
    }
}