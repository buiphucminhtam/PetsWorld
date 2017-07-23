package com.minhtam.petsworld.LayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhtam.petsworld.Adapter.AdapterReport;
import com.minhtam.petsworld.Class.Report;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallReport;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by st on 6/22/2017.
 */

public class FindPetReportLayoutManager {
    private Context mContext;
    private ListView lvReport;
    private ArrayList<Report> listReport;
    private AdapterReport adapterReport;
    private Button btnFindPetReport_Date;
    private View v;
    private OnFindPetReportItemClickListener onFindPetReportItemClickListener;

    private Dialog datePickerDialog;
    private DatePicker datePicker;

    public FindPetReportLayoutManager(Context context, View v) {
        mContext = context;
        this.v = v;

        AddControl();
        AddEvent();
    }

    private void AddControl() {
        listReport = new ArrayList<>();
        lvReport = (ListView) v.findViewById(R.id.lvFindPet_Report);
        btnFindPetReport_Date = (Button) v.findViewById(R.id.btnFindPetReport_Date);
        adapterReport = new AdapterReport(mContext,listReport);
        lvReport.setAdapter(adapterReport);

        //Get report in current time
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        btnFindPetReport_Date.setText(dateFormat.format(Calendar.getInstance().getTime()));
        new AsynctaskGetReport().execute(dateFormat.format(Calendar.getInstance().getTime()));

        //init dialog timepicker
        datePickerDialog = new Dialog(mContext);
        datePickerDialog.setContentView(R.layout.dialog_datepicker);
        datePickerDialog.setCancelable(true);
        datePickerDialog.setTitle("");
        datePicker = (DatePicker) datePickerDialog.findViewById(R.id.DatePicker);
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Calendar c = Calendar.getInstance();
                c.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                btnFindPetReport_Date.setText(dateFormat.format(c.getTime()));
                new AsynctaskGetReport().execute(dateFormat.format(c.getTime()));
            }
        });
    }
    private void AddEvent() {
        lvReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onFindPetReportItemClickListener != null) {
                    onFindPetReportItemClickListener.onItemClickListener(view,listReport.get(position));
                }
            }
        });

        btnFindPetReport_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    public void setOnFindPetReportItemClickListener(OnFindPetReportItemClickListener onFindPetReportItemClickListener) {
        this.onFindPetReportItemClickListener = onFindPetReportItemClickListener;
    }

    public interface OnFindPetReportItemClickListener{
        public void onItemClickListener(View v, Report report);
    }

    private class AsynctaskGetReport extends AsyncTask<String, Void, String> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(String... params) {
            CallReport callReport = new CallReport();
            String result = callReport.GetReportByDate(params[0],2);
            if (!result.equals("0")) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Report>>() {
                }.getType();
                List<Report> posts = (List<Report>) gson.fromJson(result, listType);
                if(listReport.size()>0) listReport.clear();
                if (posts.size()>0) listReport.addAll(0, posts);
                return "OK";
            } else {
                return "Chưa có bài viết nào";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("OK")) {
                adapterReport.notifyDataSetChanged();
            } else {
                Toast.makeText(mContext, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
    }
}
