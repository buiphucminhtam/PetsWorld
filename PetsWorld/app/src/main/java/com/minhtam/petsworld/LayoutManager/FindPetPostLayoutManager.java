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
import com.minhtam.petsworld.Adapter.AdapterFindPetListItem;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Model.FindPetPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPhoto;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindPet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by st on 6/10/2017.
 */

public class FindPetPostLayoutManager {
    private Context mContext;
    private View v;
    private ArrayList<FindPetPost> listFindPetPost;
    private RecyclerView rvFindPetPost;
    private AdapterFindPetListItem adapter;
    private LinearLayoutManager linearLayoutManager;

    public FindPetPostLayoutManager(Context context, View view) {
        super();
        this.v = view;
        this.mContext = context;

        AddControl();
        AddEvent();
    }

    private void AddControl() {
        rvFindPetPost = (RecyclerView) v.findViewById(R.id.rvUserInformationFindPetPost);
        listFindPetPost = new ArrayList<>();
        adapter = new AdapterFindPetListItem(mContext,listFindPetPost);
        linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        rvFindPetPost.setLayoutManager(linearLayoutManager);

        new AsynctaskGetFindPetPost().execute();

    }

    private void AddEvent() {
        adapter.setOnItemClickListener(new AdapterFindPetListItem.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {

            }
        });
    }

    //Call Get FindPetPost
    private class AsynctaskGetFindPetPost extends AsyncTask<Void, Void, String> {
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
            CallPostFindPet callPostFindPet = new CallPostFindPet();
            String result = callPostFindPet.GetPostFindPetByUserId(Integer.parseInt(MainActivity.userInfo.getId()));
            if (!result.equals("0")) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<FindPetPost>>() {
                }.getType();
                List<FindPetPost> posts = (List<FindPetPost>) gson.fromJson(result, listType);

                if (posts.size() > 0) {
                    for (FindPetPost post : posts) {
                        CallPhoto callPhoto = new CallPhoto();
                        String jsonPhoto = callPhoto.GetPhotoById(Integer.parseInt(post.getPetId()));

                        Type listTypePhoto = new TypeToken<List<Photo>>() {
                        }.getType();
                        List<Photo> listPhoto = (List<Photo>) gson.fromJson(jsonPhoto, listTypePhoto);

                        ArrayList<Photo> listTemp = new ArrayList<>();
                        listTemp.addAll(listPhoto);

                        post.setListPhoto(listTemp);
                    }
                    listFindPetPost.addAll(0, posts);
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
                rvFindPetPost.setAdapter(adapter);
            } else {
                Toast.makeText(mContext,s, Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
    }
}
