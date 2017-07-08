package com.minhtam.petsworld.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhtam.petsworld.Activity.FindOwnerItemDetailActivity;
import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.Activity.PlacePostFindOwnerActivity;
import com.minhtam.petsworld.Adapter.AdapterFindOwnerListItem;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Model.FindOwnerPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPhoto;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindOwner;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.minhtam.petsworld.Util.RecyclerViewUtil.EndlessScrollListener;
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
    private final int REQUEST_DETAIL = 2;
    private final int RESULT_DELETE = 3;
    private final int RESULT_EDIT = 4;

    private RecyclerView rvFindOwnerPost;
    private AdapterFindOwnerListItem adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<FindOwnerPost> listFindOwnerPost;
    private View progressFooter;
    private EndlessScrollListener scrollListener;
    private CallPostFindOwner callPostFindOwner;
    private SwipeRefreshLayout swipeContainerFindOwner;
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
        imvFindOwner_userimage  = (ImageView) v.findViewById(R.id.imvFindOwner_userimage);
        swipeContainerFindOwner = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainerFindOwner);
        layout_Post             = (LinearLayout) v.findViewById(R.id.layout_Post_FindOwner);
        rvFindOwnerPost         = (RecyclerView) v.findViewById(R.id.rvFindOwner_Post);
        progressFooter          = v.findViewById(R.id.footer);
        listFindOwnerPost       = new ArrayList<>();
        linearLayoutManager     = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        adapter                 = new AdapterFindOwnerListItem(getContext(),listFindOwnerPost);
        linearLayoutManager.setRecycleChildrenOnDetach(false);
        rvFindOwnerPost.setLayoutManager(linearLayoutManager);
        rvFindOwnerPost.setAdapter(adapter);


        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int totalItemsCount) {
                if (listFindOwnerPost.size() > 0) {
                    if (Integer.parseInt(listFindOwnerPost.get(listFindOwnerPost.size() - 1).getId()) > 10) {
                        new loadOlderData().execute();
                    }
                }
                return true;
            }
        };

        rvFindOwnerPost.addOnScrollListener(scrollListener);

        callPostFindOwner = new CallPostFindOwner();
    }

    private void AddEvent() {

        if (MainActivity.userInfo.getUserimage() != null) {
            if (!MainActivity.userInfo.getUserimage().equals("None")) {
                Picasso.with(getContext()).load(WebserviceAddress.WEB_ADDRESS + MainActivity.userInfo.getUserimage()).fit().into(imvFindOwner_userimage);
            }
        }

        layout_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.userInfo.getUsername().equals("None") && !MainActivity.userInfo.getAddress().equals("None") && !MainActivity.userInfo.getPhone().equals("None")) {
                    Intent intent = new Intent(getActivity(), PlacePostFindOwnerActivity.class);
                    startActivityForResult(intent, REQUEST_POST);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Phải thêm đầy đủ thông tin cá nhân trước");
                    builder.setCancelable(true);
                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    builder.create().show();

                }
            }
        });

        adapter.setOnItemClickListener(new AdapterFindOwnerListItem.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                FindOwnerPost findOwnerPost = listFindOwnerPost.get(position);
                Intent i = new Intent(getActivity(), FindOwnerItemDetailActivity.class);
                i.putExtra("selecteditem",findOwnerPost);
                startActivityForResult(i,REQUEST_DETAIL);
            }
        });

        swipeContainerFindOwner.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(listFindOwnerPost.size()>0)
                    new loadNewest().execute();
                else
                    new getDataFirstTime().execute();
            }
        });

        new getDataFirstTime().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_POST && resultCode == getActivity().RESULT_OK) {
            FindOwnerPost post = (FindOwnerPost) data.getParcelableExtra("findownerpost");
            if (post != null) {
                int pos = listFindOwnerPost.indexOf(post);
                if (pos > -1) {
                    adapter.remove(pos);
                    adapter.add(0, post);
                } else {
                    new loadNewest().execute();
                }
            }
        }
        else if (requestCode == REQUEST_DETAIL && resultCode == RESULT_DELETE) {
            String findOwnerId = data.getStringExtra("deleted");
            for(int i = 0; i<listFindOwnerPost.size(); i++) {
                if (listFindOwnerPost.get(i).getId().equals(findOwnerId)) {
                    adapter.remove(i);
                }
            }
        }
        else if(requestCode == REQUEST_DETAIL && resultCode == RESULT_EDIT){
            FindOwnerPost post = data.getParcelableExtra("findownerpost");
            for(int i = 0; i<listFindOwnerPost.size(); i++) {
                if (listFindOwnerPost.get(i).getId().equals(post.getId())) {
                    adapter.remove(i);
                    adapter.add(i,post);
                }
            }

        }
    }



    private class getDataFirstTime extends AsyncTask<Void, Void, String> {
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
            swipeContainerFindOwner.setRefreshing(false);
            if (s.equals("error")) {
                Toast.makeText(getContext(), "Không có bài viết nào", Toast.LENGTH_SHORT).show();
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    private class loadOlderData extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressFooter.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            if (listFindOwnerPost.size() > 0)
                result = callPostFindOwner.GetPostFindOwnerOlder(Integer.parseInt(listFindOwnerPost.get(listFindOwnerPost.size() - 1).getId())-1);

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
                    listFindOwnerPost.addAll(posts);
                    return "OK";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("")) {
                progressFooter.setVisibility(View.GONE);
                scrollListener.setLoaded();
                return;
            }
            Log.d(TAG,"LOAD MORE");
            adapter.notifyDataSetChanged();
        }
    }

    private class loadNewest extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            if (listFindOwnerPost.size() > 0) {
                String result = callPostFindOwner.GetPostFindOwnerNewest(Integer.parseInt(listFindOwnerPost.get(0).getId()));
                Gson gson = new Gson();
                Type listType = new TypeToken<List<FindOwnerPost>>() {
                }.getType();
                List<FindOwnerPost> posts = (List<FindOwnerPost>) gson.fromJson(result, listType);

                if (posts.size() > 0) {
                    for (FindOwnerPost post : posts) {
                        CallPhoto callPhoto = new CallPhoto();
                        String jsonPhoto = callPhoto.GetPhotoById(Integer.parseInt(post.getPetId()));

                        Type listTypePhoto = new TypeToken<List<Photo>>() {}.getType();
                        List<Photo> listPhoto = (List<Photo>) gson.fromJson(jsonPhoto, listTypePhoto);

                        ArrayList<Photo> listTemp = new ArrayList<>();
                        listTemp.addAll(listPhoto);

                        post.setListPhoto(listTemp);
                    }
                    listFindOwnerPost.addAll(0,posts);
                    return "OK";
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            swipeContainerFindOwner.setRefreshing(false);
            if(s.equals("OK")) {
                adapter.notifyDataSetChanged();
            }
        }
    }
}
