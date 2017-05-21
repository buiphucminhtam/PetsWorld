package com.minhtam.petsworld.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.minhtam.petsworld.Class.PetType;
import com.minhtam.petsworld.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by st on 5/20/2017.
 */

public class ExpandableListViewPetTypeAdapter extends BaseExpandableListAdapter {
    private final String TAG = "ADAPTER";
    private Context mContext;
    private ArrayList<String> listHeader = null;
    private HashMap<String,ArrayList<PetType>> hashMapPetType = null;

    public ExpandableListViewPetTypeAdapter(Context context,ArrayList<String> listHeader,HashMap<String,ArrayList<PetType>> hashMapPetType) {
        super();
        this.mContext = context;
        this.listHeader = listHeader;
        this.hashMapPetType = hashMapPetType;
    }

    @Override
    public int getGroupCount() {
        return hashMapPetType.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return hashMapPetType.get(listHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.insert_pettype_header_item_layout,null);
        }
        Log.d(TAG,"Header: " + listHeader.get(groupPosition));
        TextView tvHeader = (TextView) convertView.findViewById(R.id.tvInsertPetInfo_PetType_Header);
        if (listHeader.get(groupPosition) != null) {
            tvHeader.setText(listHeader.get(groupPosition));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.insert_pettype_child_item_layout,null);
        }
        TextView tvChild = (TextView) convertView.findViewById(R.id.tvInsertPetInfo_PetType_Child);
        if (hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename() != null) {
            tvChild.setText(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
