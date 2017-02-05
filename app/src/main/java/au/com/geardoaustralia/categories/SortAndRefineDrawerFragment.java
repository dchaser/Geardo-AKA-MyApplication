package au.com.geardoaustralia.categories;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import au.com.geardoaustralia.R;
import au.com.geardoaustralia.utils.utilKit;

/**
 * Created by DasunPC on 1/4/17.
 */

public class SortAndRefineDrawerFragment extends ListFragment {

    private static final String TAG = SubCategorySelectorFragment.class.getSimpleName();
    private static ListView lvSortRefine;
    private static View containerView;
    int selectedPosition;

    boolean isSort = false;
    boolean isRefine = false;


    private ActionBarDrawerToggle mDrawerToggle;
    public static DrawerLayout mDrawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort_refine_layout, container, false);


        return view;
    }

    @Override
    public void onListItemClick(ListView l, View clickedViewItem, int position, long id) {
        super.onListItemClick(l, clickedViewItem, position, id);

        ListView lv = getListView();
        int size = lv.getChildCount();

        for (int i = 0; i < size; i++) {
            if (i != position) {
                lv.getChildAt(i).setBackgroundColor(Color.WHITE);
                ((TextView)lv.getChildAt(i).findViewById(R.id.tvCategoryTitle)).setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            }

        }

        //change dataset of recyclerview


        //set backgroud color of selected view to oragne and add the right intrinsic drawable to right
        clickedViewItem.setBackgroundColor(getResources().getColor(R.color.orange));
        ((TextView)clickedViewItem.findViewById(R.id.tvCategoryTitle)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_done_black_48dp, 0);


    }

    public void setUpSortView(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        isSort = true;

        ArrayList<String> sortFilters = new ArrayList<>();
        sortFilters.add("Best Match");
        sortFilters.add("Price:Low to High");
        sortFilters.add("Price:High to Low");
        sortFilters.add("Best Selling");
        sortFilters.add("Average Customer Reviews");

        //Get data from table by the SortAdapter
        SortAdapter sortAdapter = new SortAdapter(getActivity(), R.layout.subcategory_view, sortFilters );

        setListAdapter(sortAdapter);

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
               // mDrawerToggle.syncState();
            }
        });
    }


    public void setUpRefineView(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {

        isRefine = true;

        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);


                //getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                //getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                //mDrawerToggle.syncState();
            }
        });
    }


}
