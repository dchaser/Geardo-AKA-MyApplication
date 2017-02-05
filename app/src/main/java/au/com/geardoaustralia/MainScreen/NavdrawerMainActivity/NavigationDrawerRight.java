package au.com.geardoaustralia.MainScreen.NavdrawerMainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.R;
import au.com.geardoaustralia.profile.ProfileScreen;
import au.com.geardoaustralia.utils.utilKit;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DasunPC on 1/12/17.
 */

public class NavigationDrawerRight extends Fragment implements DasunsAdapter.ClickListener {

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private DasunsAdapter dasunsAdapter;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARENED_DRAWER = "user_learned_drawer";

    private View containerView;

    RelativeLayout rlProfileArea;
    TextView tvName;
    CircleImageView civProfileImage;

    public NavigationDrawerRight() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(utilKit.readFromPreferences(getActivity(),KEY_USER_LEARENED_DRAWER,"false"));

        if(savedInstanceState != null){
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.nav_drawer_right_layout, container, false);

        //profile area stuff
        rlProfileArea = (RelativeLayout) layout.findViewById(R.id.rlProfileArea);
        rlProfileArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ProfileScreen.class));
            }
        });

        //setup recycler view for the Navigation Drawer
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawer_list);
        dasunsAdapter = new DasunsAdapter(getActivity(), getData());
        dasunsAdapter.setClickListener(this);
        recyclerView.setAdapter(dasunsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }



    public static List<Information> getData(){



        //build an arraylist of Information
        List<Information> data = new ArrayList<>();

        int[] icons = {R.drawable.ic_cart_grey, R.drawable.ic_account_blue, R.drawable.ic_store_grey, R.drawable.ic_attach_money_black_48dp,R.drawable.ic_cart_grey, R.drawable.ic_account_blue, R.drawable.ic_store_grey, R.drawable.ic_attach_money_black_48dp,R.drawable.ic_cart_grey, R.drawable.ic_account_blue, R.drawable.ic_store_grey, R.drawable.ic_attach_money_black_48dp};
        String[] titles = {"Cart","Login", "About Us", "Contact Us", "My Profile", "Corporate Gifts", "Privacy Policy" , "Shipping Policy" , "Terms & Conditions",  "Help & FAQs", "Order Returns" , "Help Us Improve"};

        //icons.length && i< titles.length;

        for(int i = 0; i < 12; i++){
            Information current = new Information();

            current.iconId = icons[i%icons.length];
            current.title = titles[i%titles.length];

            data.add(current);
        }

        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),mDrawerLayout, toolbar, R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer = true;
                    utilKit.saveToPreferences(getActivity(),KEY_USER_LEARENED_DRAWER, mUserLearnedDrawer+"");
                }

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset < 0.6){
                    toolbar.setAlpha(1-slideOffset );
                }
            }
        };

        if(!mUserLearnedDrawer && !mFromSavedInstanceState){

            //mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }



    @Override
    public void itemClicket(View view, int position) {

    }
}
