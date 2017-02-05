package au.com.geardoaustralia;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.MainScreen.NavdrawerMainActivity.DasunsAdapter;
import au.com.geardoaustralia.MainScreen.NavdrawerMainActivity.Information;
import au.com.geardoaustralia.MainScreen.NavdrawerMainActivity.NavigationDrawerLeft;
import au.com.geardoaustralia.cartNew.BaseActivity;
import au.com.geardoaustralia.categories.CategorySelectionScreen;
import au.com.geardoaustralia.login.SignInActivity;
import au.com.geardoaustralia.login.SignupActivity;
import au.com.geardoaustralia.utils.GlobalContext;

/**
 * Created by DasunPC on 1/23/17.
 */

public class TopSliderActivity extends BaseActivity implements DasunsAdapter.ClickListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private DasunsAdapter dasunsAdapter;
    String TAG = NavigationDrawerLeft.class.getSimpleName();

    TextView tvSignIn;
    TextView tvJoinFree;

    //visible and invisible view toggle
    LinearLayout llSignInOrJoinFree;
    private FirebaseAuth auth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_layout);

        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(this);
        tvJoinFree = (TextView) findViewById(R.id.tvJoinFree);
        tvJoinFree.setOnClickListener(this);

        //setup recycler view for the Navigation Drawer
        recyclerView = (RecyclerView) findViewById(R.id.rv_list);
        dasunsAdapter = new DasunsAdapter(TopSliderActivity.this, getData());
        dasunsAdapter.setClickListener(this);
        recyclerView.setAdapter(dasunsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TopSliderActivity.this));

        llSignInOrJoinFree = (LinearLayout) findViewById(R.id.llSignInOrJoinFree);

        //Get Firebase auth instance
        auth = GlobalContext.getFAuthInstance();


        if (auth.getCurrentUser() != null) {

            llSignInOrJoinFree.setVisibility(View.GONE);
        }


        // Add the back button to the toolbar.
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.close);
        toolbar.setNavigationContentDescription(R.string.close_and_go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
        finish();

    }

    public static List<Information> getData() {


        //build an arraylist of Information
        List<Information> data = new ArrayList<>();

        int[] icons = {R.drawable.home, R.drawable.categories, R.drawable.r_v_items, R.drawable.my_orders,R.drawable.my_reviews,  R.drawable.r_p_items, R.drawable.messages, R.drawable.my_favorits, R.drawable.one_step_by_setup, R.drawable.my_acc, R.drawable.feedback, R.drawable.rate_gd};
        String[] titles = {"Home", "All Categories", "Recently Viewed Items", "My Orders", "My Reviews", "Recently Purchased Items", "Messages", "My Favorites", "1-Step Buy Settings", "My Account", "Feedback", "Rate Geardo"};

        //icons.length && i< titles.length;

        for (int i = 0; i < 12; i++) {
            Information current = new Information();

            current.iconId = icons[i % icons.length];
            current.title = titles[i % titles.length];

            data.add(current);
        }

        return data;
    }

    @Override
    public void itemClicket(View view, int position) {

        switch (position){

            case 0:
                //Main Activity
                if(GlobalContext.getInstance().selectedPage == 0){
                    onBackPressed();
                }else{
                    GlobalContext.getInstance().selectedPage = 0;
                    startActivity(new Intent(TopSliderActivity.this, MainActivity.class));
                    finish();
                }
                break;
            case 1:
                //Category Screen
                GlobalContext.getInstance().selectedPage = 1;
                startActivity(new Intent(TopSliderActivity.this, CategorySelectionScreen.class));
                finish();
                break;
            case 2:
                //Recently Viewed Items page
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                //My Orders
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                //My Reviews
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                //Recently Purchased items
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();

                break;
            case 8:
                //Messages
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case 9:
                //My Favorites
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case 10:
                //1 Step Buy Settings
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case 11:
                //My Account
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case 12:
                //Feedback
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case 13:
                //Rate Gear do
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;


            default:
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.tvSignIn){

            startActivity(new Intent(TopSliderActivity.this, SignInActivity.class));

        }

        if(view.getId() == R.id.tvJoinFree){

            startActivity(new Intent(TopSliderActivity.this, SignupActivity.class));

        }
    }
}
