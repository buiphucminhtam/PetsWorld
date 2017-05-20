package com.minhtam.petsworld.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.minhtam.petsworld.Class.PetType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by st on 5/20/2017.
 */

public class ExpandableListViewPetTypeAdapter extends BaseExpandableListAdapter {

    Context mContext;
    ArrayList<String> listHeader;
    HashMap<String,ArrayList<PetType>> hashMapPetType;

    public ExpandableListViewPetTypeAdapter(Context context,ArrayList<String> listHeader,HashMap<String,ArrayList<PetType>> hashMapPetType) {
        super();
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
