package com.minhtam.petsworld.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minhtam.petsworld.Activity.MainActivity;
import com.minhtam.petsworld.Activity.PlacePostActivity;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.WebserviceAddress;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindOwnersFragment extends Fragment {
    private ImageView imvFindOwner_userimage;
    private TextView tvFindOwner_userPost;
    private View v;
    private LinearLayout layout_Post;
    private final String TAG = "FIND_OWNER";

    private final int REQUEST_POST = 1;


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
        tvFindOwner_userPost = (TextView) v.findViewById(R.id.tvFindOwner_userPost);
        layout_Post = (LinearLayout) v.findViewById(R.id.layout_Post_FindOwner);

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_POST && resultCode == getActivity().RESULT_OK) {

        }
    }
}
