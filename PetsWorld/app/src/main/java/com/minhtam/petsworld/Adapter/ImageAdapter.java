package com.minhtam.petsworld.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.Dialog.MyImagViewDialog;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by st on 3/24/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private ArrayList<String> listPickedImage;
    private Context context;
    private boolean isClickToDelItem = true;

    public ImageAdapter(Context context, ArrayList<String> listPickedImage) {
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
    public void onBindViewHolder(ImageAdapter.MyViewHolder holder, final int position) {
        if (isClickToDelItem) {
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listPickedImage.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, listPickedImage.size());
                }
            });
            Picasso.with(context).load("file://" + listPickedImage.get(position)).fit().placeholder(R.drawable.progress_image).into(holder.img);
        } else {
            Picasso.with(context).load(WebserviceAddress.WEB_ADDRESS + listPickedImage.get(position)).fit().placeholder(R.drawable.progress_image).into(holder.img);
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

    public void TurnOffClickToDelItem() {
        isClickToDelItem = false;
    }

    public void setListPickedImage(ArrayList<String> listImage) {
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
                    MyImagViewDialog dialog = new MyImagViewDialog(context,R.layout.dialog_view_image,listPickedImage.get(getAdapterPosition()));
                    dialog.ShowDialog();
                }
            });
        }
    }
}
