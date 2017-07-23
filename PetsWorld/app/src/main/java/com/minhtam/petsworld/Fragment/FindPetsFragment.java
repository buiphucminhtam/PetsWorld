package com.minhtam.petsworld.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minhtam.petsworld.Activity.FindPetItemDetailActivity;
import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.Activity.PlacePostFindPetActivity;
import com.minhtam.petsworld.Adapter.AdapterFindPetListItem;
import com.minhtam.petsworld.Adapter.ExpandableListViewPetTypeAdapter;
import com.minhtam.petsworld.Class.PetType;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Model.FindPetPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.GoogleMap.GPSTracker;
import com.minhtam.petsworld.Util.KSOAP.CallPetType;
import com.minhtam.petsworld.Util.KSOAP.CallPhoto;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindPet;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.minhtam.petsworld.Util.RecyclerViewUtil.EndlessScrollListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindPetsFragment extends Fragment {
    private ImageView imvFindPet_userimage;
    private View v;
    private LinearLayout layout_Post;
    private final String TAG = "FIND_PET";
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

    //SEARCH
    private ExpandableListView exlvFindPetPettype;
    private ImageView btnSearch;
    private TextView tvSearchFindPet;
    private EditText edtDistanceSearch_FindPet;
    private AdapterFindPetListItem adapterTemp;

    private ArrayList<PetType> listChild;
    private ArrayList<String> listHeader;
    private HashMap<String,ArrayList<PetType>> hashMapPetType;
    private ExpandableListViewPetTypeAdapter adapterListPetType;
    private boolean isPickPetType = false;
    private String filterTypeName = null;
    private boolean isTurnOnLocationMode = false;
    private double distance = 0;
    private boolean mLocationPermissionGranted = false;
    private ArrayList<FindPetPost> listFindPetTemp;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GPSTracker gps;


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

        //Search
        gps = new GPSTracker(getContext());
        btnSearch = (ImageView) v.findViewById(R.id.btnSearchFindPet);
        exlvFindPetPettype = (ExpandableListView) v.findViewById(R.id.exlvFindPetPettype);
        listHeader = new ArrayList<>();
        listChild = new ArrayList<>();
        hashMapPetType = new HashMap<>();
        listFindPetTemp = new ArrayList<>();
        adapterTemp = new AdapterFindPetListItem(getContext(), listFindPetTemp);
        adapterListPetType = new ExpandableListViewPetTypeAdapter(getContext(),listHeader,hashMapPetType);
        exlvFindPetPettype.setAdapter(adapterListPetType);
        tvSearchFindPet = (TextView) v.findViewById(R.id.tvSearchFindPet);
        edtDistanceSearch_FindPet = (EditText) v.findViewById(R.id.edtDistanceSearch_FindPet);


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

        //SEARCH
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPickPetType && !isTurnOnLocationMode) {
                    showSearchType();
                    showSearchDistance();
                } else {
                    hideSearchDistance();
                    hideSearchType();
                    btnSearch.setImageResource(R.drawable.ic_search);
                    tvSearchFindPet.setText(R.string.searchlayouttittle);
                }
            }
        });

        exlvFindPetPettype.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (isTurnOnLocationMode) {
                    tvSearchFindPet.setText(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename() + " trong phạm vi: " + distance + " km");
                } else {
                    tvSearchFindPet.setText(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename());
                }
                isPickPetType = true;
                addFilter(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename());
                rvFindPetPost.swapAdapter(adapterTemp,false);
                exlvFindPetPettype.setVisibility(View.GONE);
                edtDistanceSearch_FindPet.setVisibility(View.GONE);
                btnSearch.setImageResource(R.drawable.ic_clear);
                return true;
            }
        });

        edtDistanceSearch_FindPet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "afterTextChanged");
                try {
                    if (edtDistanceSearch_FindPet.getText().toString().equals("")) {
                        distance = 0;
                        isTurnOnLocationMode = false;
                        if (isPickPetType) {
                            addFilter(filterTypeName == null ? "" : filterTypeName);
                            rvFindPetPost.swapAdapter(adapterTemp,false);
                        } else {
                            rvFindPetPost.swapAdapter(adapter,false);
                        }
                    } else {
                        distance = Double.parseDouble(edtDistanceSearch_FindPet.getText().toString());
                        isTurnOnLocationMode = true;
                        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = true;
                            addFilter(filterTypeName == null ? "" : filterTypeName);
                            rvFindPetPost.swapAdapter(adapterTemp,false);
                        } else {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        }
                    }

                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        new FindPetsFragment.getDataFirstTime().execute();
    }

    private void showSearchType() {
        if (hashMapPetType.size() < 1) {
            new getListType().execute();
        } else {
            exlvFindPetPettype.setVisibility(View.VISIBLE);
        }
    }

    private void hideSearchType() {
        isPickPetType = false;
        rvFindPetPost.swapAdapter(adapter,false);
        adapter.notifyDataSetChanged();
    }

    private void showSearchDistance() {
        edtDistanceSearch_FindPet.setVisibility(View.VISIBLE);
    }

    private void hideSearchDistance() {
        isTurnOnLocationMode = false;
        edtDistanceSearch_FindPet.setText("");
        edtDistanceSearch_FindPet.setVisibility(View.GONE);
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
    private void addFilter(String type) {
        Log.d(TAG, "isPickPetType: " + isPickPetType);
        Log.d(TAG, "isTurnOnLocationMode: " + isTurnOnLocationMode);
        filterTypeName = type;
        listFindPetTemp.clear();
        if (isPickPetType) {
            for (FindPetPost post: listFindPetPost) {
                if (post.getTypename().equals(type)) {
                    listFindPetTemp.add(post);
                    Log.d(TAG, "+1 Item Type");
                }
            }
        }
        if (isTurnOnLocationMode) {

            Location myLocal;
            if (mLocationPermissionGranted) {
                if (!gps.canGetLocation()) {
                    gps.showSettingsAlert();
                } else {
                    myLocal = gps.getLocation();
                    if (listFindPetTemp.size() > 0) {
                        ArrayList<FindPetPost> listTemp = new ArrayList<>();
                        for (FindPetPost post : listFindPetTemp) {
                            if (calRange(myLocal.getLongitude(), myLocal.getLatitude(), post.getLongitude(), post.getLatitute()) <= distance) {
                                listTemp.add(post);
                                Log.d(TAG, "+1 Item Nearby");
                            }
                        }
                        listFindPetTemp.clear();
                        listFindPetTemp.addAll(listTemp);
                        rvFindPetPost.swapAdapter(adapterTemp,false);
                    } else {
                        if (!isPickPetType) {
                            for (FindPetPost post : listFindPetPost) {
                                if (calRange(myLocal.getLongitude(), myLocal.getLatitude(), post.getLongitude(), post.getLatitute()) <= distance) {
                                    listFindPetTemp.add(post);
                                    Log.d(TAG, "+1 Item Nearby");
                                }
                            }
                        }
                    }
                }
            }
        }

        adapterTemp.notifyDataSetChanged();
    }


    private double calRange(double lon1,double lat1,double lon2,double lat2) {
        int R = 6371; // km
        double x = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2);
        double y = (lat2 - lat1);
        return (Math.sqrt(x * x + y * y) * R);
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
                if (isPickPetType) {
                    addFilter(filterTypeName);
                }
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

                    if (isPickPetType) {
                        addFilter(filterTypeName);
                    }
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


    private class getListType extends AsyncTask<Void, Void, String> {
        Dialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_layout);
            progressDialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            progressDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
        }

        @Override
        protected String doInBackground(Void... params) {
            CallPetType callPetType = new CallPetType();
            String result = callPetType.Get();
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<PetType>>(){}.getType();
            List<PetType> posts = (List<PetType>) gson.fromJson(s, listType);
            Log.d(TAG,"List Pet Type: "+s);
            listChild.addAll(posts);
            //add to list header
            if (listChild.size() > 0) {
                //Add header to list header
                for (PetType petType : listChild) {
                    String headerTemp = petType.getTypename().substring(0, petType.getTypename().indexOf(" "));
                    if (listHeader.indexOf(headerTemp) == -1) {
                        listHeader.add(headerTemp);
                    }
                }
                //Add header to hashmap with list
                for (String header : listHeader) {
                    hashMapPetType.put(header,new ArrayList<PetType>());
                }

                //Add child to hash map
                for (PetType petType : listChild) {
                    String headerTemp = petType.getTypename().substring(0, petType.getTypename().indexOf(" "));
                    if (hashMapPetType.get(headerTemp) != null) {
                        hashMapPetType.get(headerTemp).add(petType);
                    }
                }
            }
            exlvFindPetPettype.setVisibility(View.VISIBLE);
            adapterListPetType.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

}
