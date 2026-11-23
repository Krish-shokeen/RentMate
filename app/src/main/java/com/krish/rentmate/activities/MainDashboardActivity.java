package com.krish.rentmate.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.krish.rentmate.R;
import com.krish.rentmate.fragments.ExpensesFragment;
import com.krish.rentmate.fragments.ExploreFragment;
import com.krish.rentmate.fragments.HomeFragment;
import com.krish.rentmate.fragments.ProfileFragment;
import com.krish.rentmate.fragments.RoommatesFragment;

public class MainDashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private final Fragment homeFragment = new HomeFragment();
    private final Fragment exploreFragment = new ExploreFragment();
    private final Fragment roommatesFragment = new RoommatesFragment();
    private final Fragment expensesFragment = new ExpensesFragment();
    private final Fragment profileFragment = new ProfileFragment();
    private Fragment activeFragment = homeFragment;

    private final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Setup fragments without recreating them
        fragmentManager.beginTransaction().add(R.id.fragment_container, profileFragment, "5").hide(profileFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, expensesFragment, "4").hide(expensesFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, roommatesFragment, "3").hide(roommatesFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, exploreFragment, "2").hide(exploreFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment_container, homeFragment, "1").commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                switchFragment(homeFragment);
                return true;
            } else if (itemId == R.id.nav_explore) {
                switchFragment(exploreFragment);
                return true;
            } else if (itemId == R.id.nav_roommates) {
                switchFragment(roommatesFragment);
                return true;
            } else if (itemId == R.id.nav_expenses) {
                switchFragment(expensesFragment);
                return true;
            } else if (itemId == R.id.nav_profile) {
                switchFragment(profileFragment);
                return true;
            }
            return false;
        });
    }

    private void switchFragment(Fragment targetFragment) {
        if (activeFragment != targetFragment) {
            fragmentManager.beginTransaction().hide(activeFragment).show(targetFragment).commit();
            activeFragment = targetFragment;
        }
    }

    public void navigateToTab(int navItemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(navItemId);
        }
    }
}