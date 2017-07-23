package com.minhtam.petsworld.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minhtam.petsworld.Class.Report;
import com.minhtam.petsworld.R;

import java.util.ArrayList;

/**
 * Created by st on 6/22/2017.
 */

public class AdapterReport extends BaseAdapter{

    private Context context;
    private ArrayList<Report> listReport;

    public AdapterReport(Context context, ArrayList<Report> listReport) {
        super();
        this.context = context;
        this.listReport = listReport;
    }

    @Override
    public int getCount() {
        return listReport.size();
    }

    @Override
    public Object getItem(int position) {
        return listReport.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listReport.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_report_item,null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.tvReportItem);

        tv.setText(listReport.get(position).toString());

        return convertView;
    }
}
