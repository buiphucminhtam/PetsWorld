package com.minhtam.petsworld.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtam.petsworld.Activity.EditUserInfoActivity;
import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInformationFragment extends Fragment {



    private ImageView imvUserImage;
    private TextView tvUserName,tvUserAddress,tvUserPhonenumbers;
    private View v;
    private final int EDIT_USERINFO = 1;

    public UserInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_user_information, container, false);
        setHasOptionsMenu(true);
        AddControl();
        AddEvent();

        return v;
    }

    private void AddControl() {
        imvUserImage = (ImageView) v.findViewById(R.id.imgUserImage);
        tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvUserAddress = (TextView) v.findViewById(R.id.tvUserAddress);
        tvUserPhonenumbers = (TextView) v.findViewById(R.id.tvPhoneNumbers);

    }

    private void AddEvent() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(1,1,1,R.string.edit);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Intent i = new Intent(getActivity(),EditUserInfoActivity.class);
            startActivityForResult(i,EDIT_USERINFO);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_USERINFO && resultCode == getActivity().RESULT_OK) {
            updateUserInfo();
        }
    }

    private void updateUserInfo() {
        Picasso.with(getContext()).load(MainActivity.userInfo.getUserimage()).fit().into(imvUserImage);
        tvUserName.setText(MainActivity.userInfo.getFullname());
        tvUserAddress.setText(MainActivity.userInfo.getAddress());
        tvUserPhonenumbers.setText(MainActivity.userInfo.getPhone());
    }


}
