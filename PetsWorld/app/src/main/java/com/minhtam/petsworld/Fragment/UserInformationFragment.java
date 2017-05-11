package com.minhtam.petsworld.Fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.Dialog.MyCustomDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserInformationFragment extends Fragment {



    ImageView imvUserImage;
    TextView tvUserName,tvUserBirthDay,tvUserAddress,tvUserPhonenumbers;
    View v;
    Button btnPostImage;
    EditText edtPost;


    final int PICK_IMAGE = 1;

    //Dialog update information
    MyCustomDialog dialogUpdateInfo;
    Dialog dialogUpdate;
    ImageView imvUpdateImg;
    EditText edtUpdateName,edtUpdateBirthDay,edtUpdateAddress,edtUpdatePhonenumbers;
    Button btnOK,btnCancel;

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
        tvUserBirthDay = (TextView) v.findViewById(R.id.tvUserBirthDay);
        tvUserAddress = (TextView) v.findViewById(R.id.tvUserAddress);
        tvUserPhonenumbers = (TextView) v.findViewById(R.id.tvPhoneNumbers);

    }

    private void AddEvent() {

    }

}
