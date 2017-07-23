package com.minhtam.petsworld.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhtam.petsworld.Activity.FindOwnerItemDetailActivity;
import com.minhtam.petsworld.Activity.FindPetItemDetailActivity;
import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Class.Report;
import com.minhtam.petsworld.Class.UserInfo;
import com.minhtam.petsworld.LayoutManager.FindOwnerReportLayoutManager;
import com.minhtam.petsworld.LayoutManager.FindPetReportLayoutManager;
import com.minhtam.petsworld.Model.FindOwnerPost;
import com.minhtam.petsworld.Model.FindPetPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPhoto;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindOwner;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindPet;
import com.minhtam.petsworld.Util.KSOAP.CallUserInfo;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportManager extends Fragment {

    private final int FINDOWNER_POST = 1;
    private final int FINDPET_POST = 2;

    private TextView tvFragmentReport_FindOwnerReport,tvFragmentReport_FindPetReport;

//    private ArrayList<Report> listReport;
//    private ArrayList<Report> listReportFindOwner;
//    private ArrayList<Report> listReportFindPet;
    private View viewRoot;

    private FindOwnerReportLayoutManager findOwnerReportLayoutManager;
    private View mFindOwnerReportLayout;
    private FindPetReportLayoutManager findPetReportLayoutManager;
    private View mFindPetReportLayout;

    private ViewGroup mLayoutListReport;

    private boolean isFindOwnerReportItemClick = false;
    private Report reportSelected = null;

    private UserInfo userPostInfo;



    public ReportManager() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_report_manager, container, false);
        AddControl();
        AddEvent();

        return viewRoot;
    }

    private void AddControl() {
        tvFragmentReport_FindOwnerReport = (TextView) viewRoot.findViewById(R.id.tvFragmentReport_FindOwnerReport);
        tvFragmentReport_FindPetReport = (TextView) viewRoot.findViewById(R.id.tvFragmentReport_FindPetReport);

//        listReport = new ArrayList<>();
//        listReportFindOwner = new ArrayList<>();
//        listReportFindPet = new ArrayList<>();

//        new AsynctaskGetReport().execute();

        mLayoutListReport = (ViewGroup) viewRoot.findViewById(R.id.layoutListReport);

        mFindOwnerReportLayout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_findowner_report,null);

        mFindPetReportLayout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_findpet_report,null);

        mLayoutListReport.addView(mFindOwnerReportLayout);
        mLayoutListReport.addView(mFindPetReportLayout);

        mFindPetReportLayout.setVisibility(View.GONE);

        registerForContextMenu(viewRoot);
    }

    private FindOwnerReportLayoutManager.OnFindOwnerReportItemClickListener onFindOwnerReportItemClickListener = new FindOwnerReportLayoutManager.OnFindOwnerReportItemClickListener() {
        @Override
        public void onItemClickListener(View v, Report report) {
            isFindOwnerReportItemClick = true;
            reportSelected = report;
            viewRoot.showContextMenu();
        }
    };
    private FindPetReportLayoutManager.OnFindPetReportItemClickListener onFindPetReportItemClickListener = new FindPetReportLayoutManager.OnFindPetReportItemClickListener() {
        @Override
        public void onItemClickListener(View v, Report report) {
            isFindOwnerReportItemClick = false;
            reportSelected = report;
            viewRoot.showContextMenu();
        }
    };

    private void AddEvent() {
        findOwnerReportLayoutManager = new FindOwnerReportLayoutManager(getContext(),mFindOwnerReportLayout);
        findOwnerReportLayoutManager.setOnFindOwnerReportItemClickListener(onFindOwnerReportItemClickListener);
        tvFragmentReport_FindOwnerReport.setBackgroundResource(R.color.selector);

        tvFragmentReport_FindOwnerReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findOwnerReportLayoutManager = new FindOwnerReportLayoutManager(getContext(),mFindOwnerReportLayout);
                findOwnerReportLayoutManager.setOnFindOwnerReportItemClickListener(onFindOwnerReportItemClickListener);
                mFindPetReportLayout.setVisibility(View.GONE);
                mFindOwnerReportLayout.setVisibility(View.VISIBLE);
                tvFragmentReport_FindOwnerReport.setBackgroundResource(R.color.selector);
                tvFragmentReport_FindPetReport.setBackgroundResource(R.color.colorPrimary);
            }
        });

        tvFragmentReport_FindPetReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPetReportLayoutManager = new FindPetReportLayoutManager(getContext(),mFindPetReportLayout);
                findPetReportLayoutManager.setOnFindPetReportItemClickListener(onFindPetReportItemClickListener);
                mFindOwnerReportLayout.setVisibility(View.GONE);
                mFindPetReportLayout.setVisibility(View.VISIBLE);
                tvFragmentReport_FindOwnerReport.setBackgroundResource(R.color.colorPrimary);
                tvFragmentReport_FindPetReport.setBackgroundResource(R.color.selector);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(1, 1, 1, "Quản lý tài khoản");
        menu.add(1, 2, 2, "Xem chi tiết bài đăng");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1: new getUserInfo().execute();
                break;
            case 2: startDetailActivity();
                break;
            default: break;
        }
        return super.onContextItemSelected(item);
    }


    private void showDialogUserInformation() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_userinformation_detail);
        dialog.setTitle("Quản lý người dùng");
        dialog.show();

        TextView tvName = (TextView) dialog.findViewById(R.id.tvUserName);
        TextView tvAddress = (TextView) dialog.findViewById(R.id.tvUserAddress);
        TextView tvPhoneNumber = (TextView) dialog.findViewById(R.id.tvPhoneNumbers);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.imgUserImage);

        Button btnLock = (Button) dialog.findViewById(R.id.btnUserInfomation_Detail_BlockUser);

        if (userPostInfo != null) {
            if (userPostInfo.getState().equals("2")) {
                btnLock.setText("Mở tài khoản");
            } else {
                btnLock.setText("Khóa tài khoản");
            }
        }

        if (MainActivity.isAdmin) {
            btnLock.setVisibility(View.VISIBLE);
        }

        btnLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPostInfo != null) {
                    if (userPostInfo.getState().equals("2")) {
                        new unlockUser().execute();
                        dialog.dismiss();
                    } else {
                        if (!userPostInfo.getState().equals("0")) {
                            new lockUser().execute();
                            dialog.dismiss();
                        }
                    }
                }
            }
        });

        tvName.setText(userPostInfo.getFullname());
        tvAddress.setText(userPostInfo.getAddress());
        tvPhoneNumber.setText(userPostInfo.getPhone());

        Picasso.with(getActivity()).load(WebserviceAddress.WEB_ADDRESS + userPostInfo.getUserimage()).into(imageView);
    }

    private void startDetailActivity() {
        if(reportSelected!=null)
         new AsynctaskGetPost(reportSelected).execute();
    }

    //Call Get Report
//    private class AsynctaskGetReport extends AsyncTask<Void, Void, String> {
//        Dialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            progressDialog.setContentView(R.layout.progress_layout);
//            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
//            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            CallReport callReport = new CallReport();
//            String result = callReport.Get();
//            if (!result.equals("0")) {
//                Gson gson = new Gson();
//                Type listType = new TypeToken<List<Report>>() {
//                }.getType();
//                List<Report> posts = (List<Report>) gson.fromJson(result, listType);
//
//                listReport.addAll(0, posts);
//                return "OK";
//                } else {
//                    return "Chưa có bài viết nào";
//                }
//            }
//
//        @Override
//        protected void onPostExecute(String s) {
//            if (s.equals("OK")) {
//                for (Report report : listReport) {
//                    if(report.getTypepost() == 1) listReportFindOwner.add(report);
//                    else listReportFindPet.add(report);
//                }
//                findOwnerReportLayoutManager = new FindOwnerReportLayoutManager(getContext(),mFindOwnerReportLayout,listReportFindOwner);
//                findPetReportLayoutManager = new FindPetReportLayoutManager(getContext(),mFindPetReportLayout,listReportFindPet);
//
//                findOwnerReportLayoutManager.setOnFindOwnerReportItemClickListener(onFindOwnerReportItemClickListener);
//                findPetReportLayoutManager.setOnFindPetReportItemClickListener(onFindPetReportItemClickListener);
//            } else {
//                Toast.makeText(getContext(), "Lỗi mạng", Toast.LENGTH_SHORT).show();
//            }
//
//            progressDialog.dismiss();
//        }
//    }

    private class AsynctaskGetPost extends AsyncTask<Void, Void, Intent>{
        Dialog progressDialog;
        Report report;

        public AsynctaskGetPost(Report report) {
            super();
            this.report = report;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        }

        @Override
        protected Intent doInBackground(Void... params) {

            String result = null;
            if (isFindOwnerReportItemClick) {
                CallPostFindOwner callPostFindOwner = new CallPostFindOwner();
                result = callPostFindOwner.GetPostFindOwnerByAndPetId(report.getUserid(), report.getPetid());
            } else {
                CallPostFindPet callPostFindPet = new CallPostFindPet();
                result = callPostFindPet.GetPostFindPetByAndPetId(report.getUserid(), report.getPetid());
            }

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);

                if (jsonArray.length() > 0) {
                    if (isFindOwnerReportItemClick) {
                        FindOwnerPost findOwnerPost = new Gson().fromJson(jsonArray.getString(0), FindOwnerPost.class);

                        CallPhoto callPhoto = new CallPhoto();
                        String jsonPhoto = callPhoto.GetPhotoById(Integer.parseInt(findOwnerPost.getPetId()));

                        Type listTypePhoto = new TypeToken<List<Photo>>(){}.getType();
                        List<Photo> listPhoto = (List<Photo>) new Gson().fromJson(jsonPhoto, listTypePhoto);

                        ArrayList<Photo> listTemp = new ArrayList<>();
                        listTemp.addAll(listPhoto);

                        findOwnerPost.setListPhoto(listTemp);

                        Intent i = new Intent(getActivity(), FindOwnerItemDetailActivity.class);
                        i.putExtra("selecteditem", findOwnerPost);
                        return i;
                    } else {
                        FindPetPost findPetPost = new Gson().fromJson(jsonArray.getString(0), FindPetPost.class);

                        CallPhoto callPhoto = new CallPhoto();
                        String jsonPhoto = callPhoto.GetPhotoById(Integer.parseInt(findPetPost.getPetId()));

                        Type listTypePhoto = new TypeToken<List<Photo>>(){}.getType();
                        List<Photo> listPhoto = (List<Photo>) new Gson().fromJson(jsonPhoto, listTypePhoto);

                        ArrayList<Photo> listTemp = new ArrayList<>();
                        listTemp.addAll(listPhoto);

                        findPetPost.setListPhoto(listTemp);

                        Intent i = new Intent(getActivity(), FindPetItemDetailActivity.class);
                        i.putExtra("selecteditem", findPetPost);
                        return i;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Intent s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s!=null)
               startActivity(s);

        }
    }

    private class getUserInfo extends AsyncTask<Void,Void,String> {

        Dialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
            CallUserInfo callUserInfo = new CallUserInfo();
            String result = callUserInfo.CallGet(reportSelected.getUserid());

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(s);
                UserInfo userInfo = new Gson().fromJson(jsonArray.getString(0),UserInfo.class);
                userPostInfo = userInfo;
                showDialogUserInformation();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private class lockUser extends AsyncTask<Void,Void,Integer> {

        Dialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            CallUserInfo callUserInfo = new CallUserInfo();
            int result = 0;
            if(userPostInfo!=null)
            result = callUserInfo.LockUser(Integer.parseInt(userPostInfo.getId()));
            return result;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            String text = "";
            if(s==1)
                text = "Khóa tài khoản thành công";
            else
                text = "Lỗi, Kiểm tra lại kết nối mạng";

            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private class unlockUser extends AsyncTask<Void,Void,Integer> {

        Dialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            CallUserInfo callUserInfo = new CallUserInfo();
            int result = 0;
            if(userPostInfo!=null)
                result = callUserInfo.UnlockUser(Integer.parseInt(userPostInfo.getId()));
            return result;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            String text = "";
            if(s==1)
                text = "Mở khóa tài khoản thành công";
            else
                text = "Lỗi, Kiểm tra lại kết nối mạng";

            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }


}
