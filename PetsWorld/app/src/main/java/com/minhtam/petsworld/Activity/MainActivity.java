package com.minhtam.petsworld.Activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.minhtam.petsworld.Adapter.MenuFragmentPageAdapter;
import com.minhtam.petsworld.Class.UserInfo;
import com.minhtam.petsworld.Fragment.FindOwnersFragment;
import com.minhtam.petsworld.Fragment.FindPetsFragment;
import com.minhtam.petsworld.Fragment.ReportManager;
import com.minhtam.petsworld.Fragment.UserInformationFragment;
import com.minhtam.petsworld.R;
import com.minhtam.petsworld.Util.KSOAP.CallUserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MenuFragmentPageAdapter adapter;
    public static boolean isAdmin = false;

    public static UserInfo userInfo;
    private int[] tabIcons ={R.drawable.ic_pets,R.drawable.ic_person_pin_circle,R.drawable.ic_account_box,R.drawable.ic_error};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        runFadeOutAnimation();

        //Setup Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup viewPage
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        //get user info
        userInfo = getIntent().getParcelableExtra("userInfo");
        new getUserInfo().execute();

        //Setup tablauout
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.DKGRAY);
        setupTabIcons();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: setupTabIcons();
                        tabLayout.getTabAt(position).setIcon(R.drawable.ic_person_circle_selected);
                        break;
                    case 1: setupTabIcons();
                        tabLayout.getTabAt(position).setIcon(R.drawable.ic_pets_selected);
                        break;

                    case 2: setupTabIcons();
                        tabLayout.getTabAt(position).setIcon(R.drawable.ic_account_box_selected);
                        break;

                    case 3: setupTabIcons();
                        tabLayout.getTabAt(position).setIcon(R.drawable.ic_error_selected);
                        break;

                    default:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_pets_selected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MenuFragmentPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FindOwnersFragment(),"Tìm người nuôi");
        adapter.addFragment(new FindPetsFragment(),"Tìm thú nuôi");
        adapter.addFragment(new UserInformationFragment(),"Trang cá nhân");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        for(int i = 0; i< tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }

        //Set tap 0 for selected first
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_menu_selected);
    }


    private void runFadeOutAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        a.reset();
        LinearLayout ll = (LinearLayout) findViewById(R.id.activity_main);
        ll.clearAnimation();
        ll.startAnimation(a);
    }

    private class getUserInfo extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            CallUserInfo callUserInfo = new CallUserInfo();
            String jsonUserInfo = callUserInfo.CallGet(Integer.parseInt(MainActivity.userInfo.getId()));
            Log.d(TAG,"jsonUSERINFO: "+jsonUserInfo);
            try {
                JSONArray jsonArray = new JSONArray(jsonUserInfo);
                JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
                userInfo.setId(jsonObject.getString("id"));
                userInfo.setUserimage(jsonObject.getString("userimage"));
                userInfo.setAddress(jsonObject.getString("address"));
                userInfo.setPhone(jsonObject.getString("phone"));
                userInfo.setDatecreated(jsonObject.getString("datecreated"));

                if (jsonObject.getInt("state") == 0) {
                    isAdmin = true;
                }else isAdmin = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (isAdmin) {
                adapter.addFragment(new ReportManager(), "Quản lý báo cáo");
            } else {
                if (adapter.getCount() > 3) {
                    adapter.removeFragment(adapter.getCount()-1);
                }
            }
            adapter.notifyDataSetChanged();
            setupTabIcons();
        }
    }
}
