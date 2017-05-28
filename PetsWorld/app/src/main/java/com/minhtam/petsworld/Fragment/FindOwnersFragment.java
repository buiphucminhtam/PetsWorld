package com.minhtam.petsworld.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.Activity.PlacePostActivity;
import com.minhtam.petsworld.Adapter.AdapterFindOwnerListItem;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Model.FindOwnerPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPhoto;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindOwner;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindOwnersFragment extends Fragment {
    private ImageView imvFindOwner_userimage;
    private View v;
    private LinearLayout layout_Post;
    private final String TAG = "FIND_OWNER";
    private final int REQUEST_POST = 1;

    private RecyclerView rvFindOwnerPost;
    private AdapterFindOwnerListItem adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<FindOwnerPost> listFindOwnerPost;

    public FindOwnersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_find_owners, container, false);
        AddControl();
        AddEvent();
        return v;
    }

    private void AddControl() {
        imvFindOwner_userimage = (ImageView) v.findViewById(R.id.imvFindOwner_userimage);
        layout_Post = (LinearLayout) v.findViewById(R.id.layout_Post_FindOwner);
        rvFindOwnerPost = (RecyclerView) v.findViewById(R.id.rvFindOwner_Post);
        listFindOwnerPost = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        adapter = new AdapterFindOwnerListItem(getContext(),listFindOwnerPost);
        rvFindOwnerPost.setLayoutManager(linearLayoutManager);
        rvFindOwnerPost.setAdapter(adapter);
        rvFindOwnerPost.hasFixedSize();
    }

    private void AddEvent() {

        if (!MainActivity.userInfo.getUserimage().equals("None")) {
            Picasso.with(getContext()).load(WebserviceAddress.WEB_ADDRESS + MainActivity.userInfo.getUserimage()).fit().into(imvFindOwner_userimage);
        }

        layout_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlacePostActivity.class);
                startActivityForResult(intent,REQUEST_POST);
            }
        });

        new getData().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_POST && resultCode == getActivity().RESULT_OK) {

        }
    }

    private class getData extends AsyncTask<Void, Void, String> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
            CallPostFindOwner callPostFindOwner = new CallPostFindOwner();
            String maxId = callPostFindOwner.GetPostMaxIdFindOwner();
            if (!maxId.equals("0")) {
                //get post first
                String result = callPostFindOwner.GetPostFindOwnerOlder(Integer.parseInt(maxId));
                Gson gson = new Gson();
                Type listType = new TypeToken<List<FindOwnerPost>>(){}.getType();
                List<FindOwnerPost> posts = (List<FindOwnerPost>) gson.fromJson(result, listType);

                if (posts.size() > 0) {
                    for (FindOwnerPost post : posts) {
                        CallPhoto callPhoto = new CallPhoto();
                        String jsonPhoto = callPhoto.GetPhotoById(Integer.parseInt(post.getPetId()));

                        Type listTypePhoto = new TypeToken<List<Photo>>(){}.getType();
                        List<Photo> listPhoto = (List<Photo>) gson.fromJson(jsonPhoto, listTypePhoto);

                        ArrayList<Photo> listTemp = new ArrayList<>();
                        listTemp.addAll(listPhoto);

                        post.setListPhoto(listTemp);
                    }
                    listFindOwnerPost.addAll(posts);
                    return "OK";
                }
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s.equals("error")) {
                Toast.makeText(getContext(), "Error!!!", Toast.LENGTH_SHORT).show();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
