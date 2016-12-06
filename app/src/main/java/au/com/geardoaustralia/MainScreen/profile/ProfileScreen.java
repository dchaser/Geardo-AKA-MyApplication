package au.com.geardoaustralia.MainScreen.profile;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.MainScreen.NavdrawerMainActivity.NavigationDrawerFragment;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.categories.CategorySelectionScreen;
import au.com.geardoaustralia.categories.NDFragment;
import au.com.geardoaustralia.categories.categoryModel;

public class ProfileScreen extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationDrawerFragment drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        toolbar = (Toolbar) findViewById(R.id.profilePageToolBar);
        //toolbar will automatically route to 'menu creation' onOptionsMenu is available
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }


        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer_profile);
        drawerFragment.setUp(R.id.fragment_nav_drawer_profile, (DrawerLayout) findViewById(R.id.dlProfileScreen), toolbar);




    }
}
