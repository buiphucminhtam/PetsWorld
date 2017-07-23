package com.minhtam.petsworld.Fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.minhtam.petsworld.Activity.FindOwnerItemDetailActivity;
import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.Activity.PlacePostFindOwnerActivity;
import com.minhtam.petsworld.Adapter.AdapterFindOwnerListItem;
import com.minhtam.petsworld.Adapter.ExpandableListViewPetTypeAdapter;
import com.minhtam.petsworld.Class.PetType;
import com.minhtam.petsworld.Class.Photo;
import com.minhtam.petsworld.Model.FindOwnerPost;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.GoogleMap.GPSTracker;
import com.minhtam.petsworld.Util.KSOAP.CallPetType;
import com.minhtam.petsworld.Util.KSOAP.CallPhoto;
import com.minhtam.petsworld.Util.KSOAP.CallPostFindOwner;
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

    //SEARCH
    private ExpandableListView exlvFindOwnerPettype;
    private ImageView btnSearch;
    private TextView tvSearchFindOwner;
    private EditText edtDistanceSearch_FindOwner;
    private AdapterFindOwnerListItem adapterTemp;
    private ArrayList<FindOwnerPost> listFindOwnerTemp;
    private boolean isSearchShowing = false; //Check search showing

    private ArrayList<PetType> listChild;
    private ArrayList<String> listHeader;
    private HashMap<String,ArrayList<PetType>> hashMapPetType;
    private ExpandableListViewPetTypeAdapter adapterListPetType;
    private boolean isPickPetType = false;
    private String filterTypeName = null;
    private boolean isTurnOnLocationMode = false;
    private double distance = 0;
    private boolean mLocationPermissionGranted = false;
    private int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private GPSTracker gps;



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


        //Search
        btnSearch = (ImageView) v.findViewById(R.id.btnSearchFindOwner);
        exlvFindOwnerPettype = (ExpandableListView) v.findViewById(R.id.exlvFindOwnerPettype);
        listHeader = new ArrayList<>();
        listChild = new ArrayList<>();
        hashMapPetType = new HashMap<>();
        listFindOwnerTemp = new ArrayList<>();
        adapterTemp = new AdapterFindOwnerListItem(getContext(), listFindOwnerTemp);
        adapterListPetType = new ExpandableListViewPetTypeAdapter(getContext(),listHeader,hashMapPetType);
        exlvFindOwnerPettype.setAdapter(adapterListPetType);
        tvSearchFindOwner = (TextView) v.findViewById(R.id.tvSearchFindOwner);
        edtDistanceSearch_FindOwner = (EditText) v.findViewById(R.id.edtDistanceSearch_FindOwner);

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

        //GPS
        gps = new GPSTracker(getContext());
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

        //SEARCH

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPickPetType && !isTurnOnLocationMode) {
                    if (isSearchShowing) {
                        isSearchShowing = false;
                        hideSearchDistance();
                        hideSearchType();
                    } else {
                        isSearchShowing = true;
                        showSearchType();
                        showSearchDistance();
                    }
                } else {
                    hideSearchDistance();
                    hideSearchType();
                    btnSearch.setImageResource(R.drawable.ic_search);
                    tvSearchFindOwner.setText(R.string.searchlayouttittle);
                }
            }
        });

        exlvFindOwnerPettype.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (isTurnOnLocationMode) {
                    tvSearchFindOwner.setText(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename() + " trong phạm vi: " + distance + " km");
                } else {
                    tvSearchFindOwner.setText(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename());
                }
                isPickPetType = true;
                addFilter(hashMapPetType.get(listHeader.get(groupPosition)).get(childPosition).getTypename());
                rvFindOwnerPost.swapAdapter(adapterTemp,false);
                exlvFindOwnerPettype.setVisibility(View.GONE);
                edtDistanceSearch_FindOwner.setVisibility(View.GONE);
                btnSearch.setImageResource(R.drawable.ic_clear);
                return true;
            }
        });

        edtDistanceSearch_FindOwner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "afterTextChanged");
                try {
                    if (edtDistanceSearch_FindOwner.getText().toString().equals("")) {
                        distance = 0;
                        isTurnOnLocationMode = false;
                        if (isPickPetType) {
                            addFilter(filterTypeName == null ? "" : filterTypeName);
                            rvFindOwnerPost.swapAdapter(adapterTemp,false);
                        } else {
                            rvFindOwnerPost.swapAdapter(adapter,false);
                        }
                    } else {
                        distance = Double.parseDouble(edtDistanceSearch_FindOwner.getText().toString());
                        isTurnOnLocationMode = true;
                        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                                android.Manifest.permission.ACCESS_FINE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = true;
                            addFilter(filterTypeName == null ? "" : filterTypeName);
                            rvFindOwnerPost.swapAdapter(adapterTemp,false);
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

        //Init
        new getDataFirstTime().execute();
    }

    private void showSearchType() {
        if (hashMapPetType.size() < 1) {
            new getListType().execute();
        } else {
            exlvFindOwnerPettype.setVisibility(View.VISIBLE);
        }
    }

    private void hideSearchType() {
        isPickPetType = false;
        rvFindOwnerPost.swapAdapter(adapter,false);
        exlvFindOwnerPettype.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    private void showSearchDistance() {
        edtDistanceSearch_FindOwner.setVisibility(View.VISIBLE);
    }

    private void hideSearchDistance() {
        isTurnOnLocationMode = false;
        edtDistanceSearch_FindOwner.setText("");
        edtDistanceSearch_FindOwner.setVisibility(View.GONE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
       if(requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
       {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
       }
    }

    private void addFilter(String type) {
        Log.d(TAG, "isPickPetType: " + isPickPetType);
        Log.d(TAG, "isTurnOnLocationMode: " + isTurnOnLocationMode);
        filterTypeName = type;
        listFindOwnerTemp.clear();
        if (isPickPetType) {
            for (FindOwnerPost post: listFindOwnerPost) {
                if (post.getTypename().equals(type)) {
                    listFindOwnerTemp.add(post);
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
                    if (listFindOwnerTemp.size() > 0) {
                        ArrayList<FindOwnerPost> listTemp = new ArrayList<>();
                        for (FindOwnerPost post : listFindOwnerTemp) {
                            if (calRange(myLocal.getLongitude(), myLocal.getLatitude(), post.getLongitude(), post.getLatitute()) <= distance) {
                                listTemp.add(post);
                                Log.d(TAG, "+1 Item Nearby");
                            }
                        }
                        listFindOwnerTemp.clear();
                        listFindOwnerTemp.addAll(listTemp);
                        rvFindOwnerPost.swapAdapter(adapterTemp,false);
                    } else {
                        if (!isPickPetType) {
                            for (FindOwnerPost post : listFindOwnerPost) {
                                if (calRange(myLocal.getLongitude(), myLocal.getLatitude(), post.getLongitude(), post.getLatitute()) <= distance) {
                                    listFindOwnerTemp.add(post);
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
            String maxId = callPostFindOwner.GetPostMaxIdFindOwner();
            if (!maxId.equals("0")) {
                //get post first
                String result = callPostFindOwner.GetPostFindOwnerOlder(Integer.parseInt(maxId));
                Log.d(TAG, result);
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

                    if (isPickPetType) {
                        addFilter(filterTypeName);
                    } else {
                        addFilter("");
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
            swipeContainerFindOwner.setRefreshing(false);
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
            exlvFindOwnerPettype.setVisibility(View.VISIBLE);
            adapterListPetType.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        rvFindOwnerPost.swapAdapter(adapter,false);
        edtDistanceSearch_FindOwner.setText("");
        exlvFindOwnerPettype.setVisibility(View.GONE);
        edtDistanceSearch_FindOwner.setVisibility(View.GONE);
        btnSearch.setImageResource(R.drawable.ic_search);
        adapter.notifyDataSetChanged();
        isPickPetType = false;
        isTurnOnLocationMode = false;
    }
}
