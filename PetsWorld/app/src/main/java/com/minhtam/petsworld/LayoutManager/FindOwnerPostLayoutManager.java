package com.minhtam.petsworld.LayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.Adapter.AdapterFindOwnerListItem;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Model.FindOwnerPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPhoto;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindOwner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by st on 6/10/2017.
 */

public class FindOwnerPostLayoutManager {
    private Context mContext;
    private View v;
    private ArrayList<FindOwnerPost> listFindOwnerPost;
    private RecyclerView rvFindOwnerPost;
    private AdapterFindOwnerListItem adapter;
    private LinearLayoutManager linearLayoutManager;

    public FindOwnerPostLayoutManager(Context context, View view) {
        super();
        this.v = view;
        this.mContext = context;

        AddControl();
        AddEvent();
    }

    private void AddControl() {
        rvFindOwnerPost = (RecyclerView) v.findViewById(R.id.rvUserInformationFindOwnerPost);
        listFindOwnerPost = new ArrayList<>();
        adapter = new AdapterFindOwnerListItem(mContext,listFindOwnerPost);
        linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        rvFindOwnerPost.setLayoutManager(linearLayoutManager);

        new AsynctaskGetFindOwnerPost().execute();

    }

    private void AddEvent() {
        adapter.setOnItemClickListener(new AdapterFindOwnerListItem.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {

            }
        });
    }

    //Call Get FindOwnerPost
    private class AsynctaskGetFindOwnerPost extends AsyncTask<Void, Void, String> {
        Dialog progressDialog;
        String password;

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
        protected String doInBackground(Void... params) {
            CallPostFindOwner callPostFindOwner = new CallPostFindOwner();
            String result = callPostFindOwner.GetPostFindOwnerByUserId(Integer.parseInt(MainActivity.userInfo.getId()));
            if (!result.equals("0")) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<FindOwnerPost>>() {
                }.getType();
                List<FindOwnerPost> posts = (List<FindOwnerPost>) gson.fromJson(result, listType);

                if (posts.size() > 0) {
                    for (FindOwnerPost post : posts) {
                        CallPhoto callPhoto = new CallPhoto();
                        String jsonPhoto = callPhoto.GetPhotoById(Integer.parseInt(post.getPetId()));

                        Type listTypePhoto = new TypeToken<List<Photo>>() {
                        }.getType();
                        List<Photo> listPhoto = (List<Photo>) gson.fromJson(jsonPhoto, listTypePhoto);

                        ArrayList<Photo> listTemp = new ArrayList<>();
                        listTemp.addAll(listPhoto);

                        post.setListPhoto(listTemp);
                    }
                    listFindOwnerPost.addAll(0, posts);
                    return "OK";
                } else {
                    return "Chưa có bài viết nào";
                }
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals("OK")) {
                rvFindOwnerPost.setAdapter(adapter);
            } else {
                Toast.makeText(mContext,s, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
    }
}
