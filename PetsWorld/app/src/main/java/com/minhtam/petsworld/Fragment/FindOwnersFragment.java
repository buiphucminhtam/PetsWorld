package com.minhtam.petsworld.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minhtam.petsworld.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindOwnersFragment extends Fragment {
    ImageView imvFindOwner_userimage;
    TextView tvFindOwner_userPost;
    View v;
    LinearLayout layout_Post;
    final String TAG = "FIND_OWNER";


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

    }

}
