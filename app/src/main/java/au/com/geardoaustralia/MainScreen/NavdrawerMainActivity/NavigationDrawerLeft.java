package au.com.geardoaustralia.MainScreen.NavdrawerMainActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.login.SignupActivity;
import au.com.geardoaustralia.profile.ProfileScreen;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.splash.SplashActivity;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.utilKit;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerLeft extends Fragment implements DasunsAdapter.ClickListener {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private FirebaseAuth auth;



    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;

    private RecyclerView recyclerView;
    private DasunsAdapter dasunsAdapter;
    String TAG = NavigationDrawerLeft.class.getSimpleName();

    private static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARENED_DRAWER = "user_learned_drawer";

    private View containerView;

    RelativeLayout rlProfileArea;
    TextView tvName;
    String displayname = "";
    Uri photouri;
    ImageView ivProfileImage;

    private Bitmap theBitmap = null;

    public NavigationDrawerLeft() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(utilKit.readFromPreferences(getActivity(), KEY_USER_LEARENED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.nav_drawer_left_layout, container, false);

        tvName = (TextView) layout.findViewById(R.id.tvName);
        ivProfileImage = (CircleImageView) layout.findViewById(R.id.ivProfileImage);

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

        auth = GlobalContext.getFAuthInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser != null) {

            displayname = firebaseUser.getDisplayName();
            String email = firebaseUser.getEmail();
            photouri = firebaseUser.getPhotoUrl();
            String uid = firebaseUser.getUid();
            List<UserInfo> info = (List<UserInfo>) firebaseUser.getProviderData();
            String providerId = firebaseUser.getProviderId();
            List<String> providers = firebaseUser.getProviders();

        }



        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        tvName.setText(displayname);
        if (photouri != null) {

            Picasso.with(getActivity().getApplicationContext()).load(photouri)
                    .placeholder(R.drawable.logo_geardo).error(R.drawable.ic_launcher)
                    .into(ivProfileImage);


        }

    }

    public static List<Information> getData() {


        //build an arraylist of Information
        List<Information> data = new ArrayList<>();

        int[] icons = {R.drawable.ic_cart_grey, R.drawable.ic_account_blue, R.drawable.ic_store_grey, R.drawable.ic_attach_money_black_48dp, R.drawable.ic_cart_grey, R.drawable.ic_account_blue, R.drawable.ic_store_grey, R.drawable.ic_attach_money_black_48dp, R.drawable.ic_cart_grey, R.drawable.ic_account_blue, R.drawable.ic_store_grey, R.drawable.ic_attach_money_black_48dp};
        String[] titles = {"Cart", "Login", "About Us", "Contact Us", "My Profile", "Corporate Gifts", "Privacy Policy", "Shipping Policy", "Terms & Conditions", "Help & FAQs", "Order Returns", "Help Us Improve"};

        //icons.length && i< titles.length;

        for (int i = 0; i < 12; i++) {
            Information current = new Information();

            current.iconId = icons[i % icons.length];
            current.title = titles[i % titles.length];

            data.add(current);
        }

        return data;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    utilKit.saveToPreferences(getActivity(), KEY_USER_LEARENED_DRAWER, mUserLearnedDrawer + "");
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
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {

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

        //startActivity(new Intent(getActivity(), FullProductPage.class));

    }
}
