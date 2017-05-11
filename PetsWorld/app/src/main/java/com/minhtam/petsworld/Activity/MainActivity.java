package com.minhtam.petsworld.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.minhtam.petsworld.Adapter.MenuFragmentPageAdapter;
import com.minhtam.petsworld.Fragment.FindOwnersFragment;
import com.minhtam.petsworld.Fragment.FindPetsFragment;
import com.minhtam.petsworld.Fragment.UserInformationFragment;
import com.minhtam.petsworld.R;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons ={R.drawable.ic_menu,R.drawable.ic_pets,R.drawable.ic_person_pin_circle,R.drawable.ic_account_box};
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
                        tabLayout.getTabAt(position).setIcon(R.drawable.ic_menu_selected);
                        break;
                    case 1: setupTabIcons();
                        tabLayout.getTabAt(position).setIcon(R.drawable.ic_pets_selected);
                        break;
                    case 2: setupTabIcons();
                        tabLayout.getTabAt(position).setIcon(R.drawable.ic_person_circle_selected);
                        break;
                    case 3: setupTabIcons();
                        tabLayout.getTabAt(position).setIcon(R.drawable.ic_account_box_selected);
                        break;
                    default:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_menu_selected);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        MenuFragmentPageAdapter adapter = new MenuFragmentPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new FindOwnersFragment(),"Tìm người nuôi");
        adapter.addFragment(new FindPetsFragment(),"Tìm thú nuôi");
        adapter.addFragment(new UserInformationFragment(),"Trang cá nhân");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        for(int i = 0; i< tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }


    private void runFadeOutAnimation() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        a.reset();
        LinearLayout ll = (LinearLayout) findViewById(R.id.activity_main);
        ll.clearAnimation();
        ll.startAnimation(a);
    }
}
