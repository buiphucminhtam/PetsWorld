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
import com.minhtam.petsworld.Activity.FindPetItemDetailActivity;
import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.Activity.PlacePostFindPetActivity;
import com.minhtam.petsworld.Adapter.AdapterFindPetListItem;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Model.FindPetPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallPhoto;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindPet;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.minhtam.petsworld.Util.RecyclerViewUtil.EndlessScrollListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindPetsFragment extends Fragment {
    private ImageView imvFindPet_userimage;
    private View v;
    private LinearLayout layout_Post;
    private final String TAG = "FIND_OWNER";
    private final int REQUEST_POST = 1;
    private final int REQUEST_DETAIL = 2;
    private final int RESULT_DELETE = 3;
    private final int RESULT_EDIT = 4;

    private RecyclerView rvFindPetPost;
    private AdapterFindPetListItem adapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<FindPetPost> listFindPetPost;
    private View progressFooter;
    private EndlessScrollListener scrollListener;
    private CallPostFindPet callPostFindPet;
    private SwipeRefreshLayout swipeContainerFindPet;


    public FindPetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_find_pets, container, false);
        AddControl();
        AddEvent();
        return v;
    }

    private void AddControl() {
        imvFindPet_userimage  = (ImageView) v.findViewById(R.id.imvFindPets_userimage);
        swipeContainerFindPet = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainerFindPet);
        layout_Post             = (LinearLayout) v.findViewById(R.id.layout_Post_FindPets);
        rvFindPetPost         = (RecyclerView) v.findViewById(R.id.rvFindPets_Post);
        progressFooter          = v.findViewById(R.id.footer);
        listFindPetPost       = new ArrayList<>();
        linearLayoutManager     = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        adapter                 = new AdapterFindPetListItem(getContext(),listFindPetPost);
        linearLayoutManager.setRecycleChildrenOnDetach(false);
        rvFindPetPost.setLayoutManager(linearLayoutManager);
        rvFindPetPost.setAdapter(adapter);


        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int totalItemsCount) {
                if (listFindPetPost.size() > 0) {
                    if (Integer.parseInt(listFindPetPost.get(listFindPetPost.size() - 1).getId()) > 10) {
                        new FindPetsFragment.loadOlderData().execute();
                    }
                }
                return true;
            }
        };

        rvFindPetPost.addOnScrollListener(scrollListener);

        callPostFindPet = new CallPostFindPet();
    }

    private void AddEvent() {

        if (MainActivity.userInfo.getUserimage() != null) {
            if (!MainActivity.userInfo.getUserimage().equals("None")) {
                Picasso.with(getContext()).load(WebserviceAddress.WEB_ADDRESS + MainActivity.userInfo.getUserimage()).fit().into(imvFindPet_userimage);
            }
        }



        layout_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.userInfo.getUsername().equals("None") && !MainActivity.userInfo.getAddress().equals("None") && !MainActivity.userInfo.getPhone().equals("None")) {
                    Intent intent = new Intent(getActivity(), PlacePostFindPetActivity.class);
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

        adapter.setOnItemClickListener(new AdapterFindPetListItem.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                FindPetPost findPetPost = listFindPetPost.get(position);
                Intent i = new Intent(getActivity(), FindPetItemDetailActivity.class);
                i.putExtra("selecteditem",findPetPost);
                startActivityForResult(i,REQUEST_DETAIL);
            }
        });

        swipeContainerFindPet.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(listFindPetPost.size()>0)
                    new FindPetsFragment.loadNewest().execute();
                else
                    new FindPetsFragment.getDataFirstTime().execute();
            }
        });

        new FindPetsFragment.getDataFirstTime().execute();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_POST && resultCode == getActivity().RESULT_OK) {
            FindPetPost post = data.getParcelableExtra("result");
            if (post != null) {
                int pos = listFindPetPost.indexOf(post);
                if (pos > -1) {
                    adapter.remove(pos);
                    adapter.add(0, post);
                } else {
                    new loadNewest().execute();
                }
            }
        }
        else if (requestCode == REQUEST_DETAIL && resultCode == RESULT_DELETE) {
            String findpetid = data.getStringExtra("deleted");
            for(int i = 0; i<listFindPetPost.size(); i++) {
                if (listFindPetPost.get(i).getId().equals(findpetid)) {
                    adapter.remove(i);
                }
            }
        }
        else if(requestCode == REQUEST_DETAIL && resultCode == RESULT_EDIT){
            FindPetPost post = data.getParcelableExtra("findpetpost");
            for(int i = 0; i<listFindPetPost.size(); i++) {
                if (listFindPetPost.get(i).getId().equals(post.getId())) {
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
            String maxId = callPostFindPet.GetPostMaxIdFindPet();
            if (!maxId.equals("0")) {
                //get post first
                String result = callPostFindPet.GetPostFindPetOlder(Integer.parseInt(maxId));
                Gson gson = new Gson();
                Type listType = new TypeToken<List<FindPetPost>>(){}.getType();
                List<FindPetPost> posts = (List<FindPetPost>) gson.fromJson(result, listType);

                if (posts.size() > 0) {
                    for (FindPetPost post : posts) {
                        CallPhoto callPhoto = new CallPhoto();
                        String jsonPhoto = callPhoto.GetPhotoById(Integer.parseInt(post.getPetId()));

                        Type listTypePhoto = new TypeToken<List<Photo>>(){}.getType();
                        List<Photo> listPhoto = (List<Photo>) gson.fromJson(jsonPhoto, listTypePhoto);

                        ArrayList<Photo> listTemp = new ArrayList<>();
                        listTemp.addAll(listPhoto);

                        post.setListPhoto(listTemp);
                    }
                    listFindPetPost.addAll(posts);
                    return "OK";
                }
            }
            return "error";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            swipeContainerFindPet.setRefreshing(false);
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
            if(progressFooter!=null) progressFooter.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            if (listFindPetPost.size() > 0)
                result = callPostFindPet.GetPostFindPetOlder(Integer.parseInt(listFindPetPost.get(listFindPetPost.size() - 1).getId())-1);

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
                listFindPetPost.addAll(posts);
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
            if (listFindPetPost.size() > 0) {
                String result = callPostFindPet.GetPostFindPetNewest(Integer.parseInt(listFindPetPost.get(0).getId()));
                Gson gson = new Gson();
                Type listType = new TypeToken<List<FindPetPost>>() {
                }.getType();
                List<FindPetPost> posts = (List<FindPetPost>) gson.fromJson(result, listType);

                if (posts.size() > 0) {
                    for (FindPetPost post : posts) {
                        CallPhoto callPhoto = new CallPhoto();
                        String jsonPhoto = callPhoto.GetPhotoById(Integer.parseInt(post.getPetId()));

                        Type listTypePhoto = new TypeToken<List<Photo>>() {}.getType();
                        List<Photo> listPhoto = (List<Photo>) gson.fromJson(jsonPhoto, listTypePhoto);

                        ArrayList<Photo> listTemp = new ArrayList<>();
                        listTemp.addAll(listPhoto);

                        post.setListPhoto(listTemp);
                    }
                    listFindPetPost.addAll(0,posts);
                    return "OK";
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            swipeContainerFindPet.setRefreshing(false);
            if(s.equals("OK")) {
                adapter.notifyDataSetChanged();
            }
        }
    }

}
