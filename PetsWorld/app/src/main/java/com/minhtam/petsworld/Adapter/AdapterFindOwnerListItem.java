package com.minhtam.petsworld.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtam.petsworld.Model.FindOwnerPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by st on 5/23/2017.
 */

public class AdapterFindOwnerListItem extends RecyclerView.Adapter<AdapterFindOwnerListItem.ViewHolder> {
    private Context mContext;
    private ArrayList<FindOwnerPost> listFindOwnerPost;

    public AdapterFindOwnerListItem(Context context, ArrayList<FindOwnerPost> listFindOwnerPost) {
        super();
        this.mContext = context;
        this.listFindOwnerPost = listFindOwnerPost;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_findowner_post_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FindOwnerPost findOwnerPost = listFindOwnerPost.get(position);

        holder.adaptetListPhoto = new AdapterListPhoto(mContext,findOwnerPost.getListPhoto());
        holder.linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        holder.rvFindOwnerItem_ListImage.setLayoutManager(holder.linearLayoutManager);
        holder.rvFindOwnerItem_ListImage.setAdapter(holder.adaptetListPhoto);

        if (!findOwnerPost.getFullname().equals("None")) {
            holder.tvFindOwnerItem_Username.setText(findOwnerPost.getFullname());
        }
        holder.tvFindOwnerItem_Datetime.setText(findOwnerPost.getDatecreated());
        holder.tvPetInfo_Petname.setText(findOwnerPost.getPetname());


        holder.tvPetInfo_PetType.setText(findOwnerPost.getTypename());

        if (findOwnerPost.getVaccine().equals("true")) {
            holder.cbPetInfo_Vacine.setChecked(true);
        }

        if (findOwnerPost.getListPhoto().size() == 1) {
            holder.rvFindOwnerItem_ListImage.setVisibility(View.GONE);
            Log.d("URL",findOwnerPost.getListPhoto().get(0).getUrl());
            Picasso.with(mContext).load(WebserviceAddress.WEB_ADDRESS+findOwnerPost.getListPhoto().get(0).getUrl()).into(holder.imvFindOwnerItem_bigimage);
        } else {
            holder.imvFindOwnerItem_bigimage.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return listFindOwnerPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView            tvFindOwnerItem_Username;
        TextView            tvFindOwnerItem_Datetime;
        TextView            tvPetInfo_Petname;
        TextView            tvPetInfo_PetType;
        CheckBox            cbPetInfo_Vacine;
        ImageView           imvFindOwnerItem_bigimage;
        RecyclerView        rvFindOwnerItem_ListImage;
        AdapterListPhoto    adaptetListPhoto;
        LinearLayoutManager linearLayoutManager;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFindOwnerItem_Username  = (TextView) itemView.findViewById(R.id.tvFindOwnerItem_Username);
            tvFindOwnerItem_Datetime  = (TextView) itemView.findViewById(R.id.tvFindOwnerItem_Datetime);
            tvPetInfo_Petname         = (TextView) itemView.findViewById(R.id.tvPetInfo_Petname);
            tvPetInfo_PetType         = (TextView) itemView.findViewById(R.id.tvPetInfo_PetType);
            cbPetInfo_Vacine          = (CheckBox) itemView.findViewById(R.id.cbPetInfo_Vacine);
            imvFindOwnerItem_bigimage = (ImageView) itemView.findViewById(R.id.imvFindOwnerItem_bigimage);
            rvFindOwnerItem_ListImage = (RecyclerView) itemView.findViewById(R.id.rvFindOwnerItem_ListImage);
        }
    }
}
