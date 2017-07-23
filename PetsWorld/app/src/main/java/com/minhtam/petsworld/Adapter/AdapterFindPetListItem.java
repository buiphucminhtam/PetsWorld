package com.minhtam.petsworld.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Model.FindPetPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by st on 5/23/2017.
 */

public class AdapterFindPetListItem extends RecyclerView.Adapter<AdapterFindPetListItem.ViewHolder> {
    private Context mContext;
    private ArrayList<FindPetPost> listFindPetPost;
    OnItemClickListener onItemClickListener;

    public AdapterFindPetListItem(Context context, ArrayList<FindPetPost> listFindPetPost) {
        super();
        this.mContext = context;
        this.listFindPetPost = listFindPetPost;
    }

    public interface OnItemClickListener{
        public void OnItemClickListener(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void add(int position,FindPetPost post) {
        listFindPetPost.add(position,post);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        listFindPetPost.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_findpet_post_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        FindPetPost FindPetPost = listFindPetPost.get(position);
        if (FindPetPost.getListPhoto() == null) {
            FindPetPost.setListPhoto(new ArrayList<Photo>());
        }

        holder.adaptetListPhoto = new AdapterListPhoto(mContext,FindPetPost.getListPhoto());
        holder.linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        holder.rvFindPetItem_ListImage.setLayoutManager(holder.linearLayoutManager);
        holder.rvFindPetItem_ListImage.setAdapter(holder.adaptetListPhoto);

        holder.tvFindPetItem_Username.setText(FindPetPost.getFullname());
        holder.tvFindPetItem_Datetime.setText(FindPetPost.getDatecreated());
        holder.tvPetInfo_Petname.setText(FindPetPost.getPetname());
        holder.tvLocation.setText(FindPetPost.getAddress());


        holder.tvPetInfo_PetType.setText(FindPetPost.getTypename());

        if (FindPetPost.getVaccine().equals("true")) {
            holder.cbPetInfo_Vacine.setChecked(true);
            holder.btnPetInfo_VaccineDate.setVisibility(View.VISIBLE);
            holder.btnPetInfo_VaccineDate.setText(FindPetPost.getVaccinedate());
        }

        if (FindPetPost.getListPhoto().size() == 1) {
            holder.rvFindPetItem_ListImage.setVisibility(View.GONE);
            Log.d("URL",FindPetPost.getListPhoto().get(0).getUrl());
            Picasso.with(mContext).load(WebserviceAddress.WEB_ADDRESS+FindPetPost.getListPhoto().get(0).getUrl()).into(holder.imvFindPetItem_bigimage);
        } else {
            holder.imvFindPetItem_bigimage.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return listFindPetPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView            tvFindPetItem_Username;
        TextView            tvFindPetItem_Datetime;
        TextView            tvPetInfo_Petname;
        TextView            tvPetInfo_PetType;
        TextView            tvLocation;
        CheckBox            cbPetInfo_Vacine;
        Button              btnPetInfo_VaccineDate;
        ImageView           imvFindPetItem_bigimage;
        RecyclerView        rvFindPetItem_ListImage;
        AdapterListPhoto    adaptetListPhoto;
        LinearLayoutManager linearLayoutManager;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFindPetItem_Username  = (TextView) itemView.findViewById(R.id.tvFindPetItem_Username);
            tvFindPetItem_Datetime  = (TextView) itemView.findViewById(R.id.tvFindPetItem_Datetime);
            tvPetInfo_Petname         = (TextView) itemView.findViewById(R.id.tvPetInfo_Petname);
            tvPetInfo_PetType         = (TextView) itemView.findViewById(R.id.tvPetInfo_PetType);
            cbPetInfo_Vacine          = (CheckBox) itemView.findViewById(R.id.cbPetInfo_Vacine);
            btnPetInfo_VaccineDate    = (Button) itemView.findViewById(R.id.btnPetInfo_VaccineDate);
            imvFindPetItem_bigimage = (ImageView) itemView.findViewById(R.id.imvFindPetItem_bigimage);
            rvFindPetItem_ListImage = (RecyclerView) itemView.findViewById(R.id.rvFindPetItem_ListImage);
            tvLocation                = (TextView) itemView.findViewById(R.id.tvFindPetItem_Location);
            cbPetInfo_Vacine.setClickable(false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClickListener(v,getAdapterPosition());
                    }
                }
            });
        }
    }
}
