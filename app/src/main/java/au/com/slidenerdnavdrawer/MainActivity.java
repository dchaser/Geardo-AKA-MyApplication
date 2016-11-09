package au.com.slidenerdnavdrawer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import au.com.slidenerdnavdrawer.MainScreen.MainContentMainActivity.RVAdapter;
import au.com.slidenerdnavdrawer.MainScreen.MainContentMainActivity.VpBannerAdapter;
import au.com.slidenerdnavdrawer.MainScreen.MainContentMainActivity.infoModel;
import au.com.slidenerdnavdrawer.MainScreen.NavdrawerMainActivity.NavigationDrawerFragment;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private NavigationDrawerFragment drawerFragment;
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private MaterialTabAdapter pagerAdapter;
    public static RVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        toolbar = (Toolbar) findViewById(R.id.appToolBar);
        //toolbar will automatically route to menu creation onOptionsMenu is available
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        //setup tabs
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.pager);

        // init view pager
        pagerAdapter = new MaterialTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
                switch (position){

                    case 0:
                        toolbar.setTitle(R.string.latest);
                        break;

                    case 1:
                        toolbar.setTitle(R.string.most_viewed);
                        break;

                    case 2:
                        toolbar.setTitle(R.string.trendy);
                        break;

                    case 3:
                        toolbar.setTitle(R.string.on_sale);
                        break;
                    default:
                        toolbar.setTitle(R.string.app_name);
                        break;
                }
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            /*
            * tabHost.addTab(
                    tabHost.newTab()
                            .setIcon(getIcon(i))
                            .setTabListener(this)
            );
            * */
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.mSearch).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.mSearch) {
            return true;
        } else {
            return false;
        }
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            //Crouton.makeText(MainActivity.this, query, Style.INFO).show();
            Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {

        viewPager.setCurrentItem(tab.getPosition());

        switch (tab.getPosition()){

            case 0:
                toolbar.setTitle(R.string.latest);
                break;

            case 1:
                toolbar.setTitle(R.string.most_viewed);
                break;

            case 2:
                toolbar.setTitle(R.string.trendy);
                break;

            case 3:
                toolbar.setTitle(R.string.on_sale);
                break;
            default:
                toolbar.setTitle(R.string.app_name);
                break;
        }
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class MaterialTabAdapter extends FragmentStatePagerAdapter {

        int icons[] = {R.drawable.ic_border_inner_black_48dp, R.drawable.ic_unfold_more_black_48dp, R.drawable.ic_attach_money_black_48dp, R.drawable.ic_apps_black_48dp};

        public MaterialTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyFragment.getInstance(position);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        public Drawable getIcon(int position){
            return getResources().getDrawable(icons[position]);
        }
    }

    public static List<infoModel> getDataSet(){

        List<infoModel> data = new ArrayList<>();

        for(int i=0; i<10; i++){

            int[] pics = {R.drawable.cable,R.drawable.stick};
            String[] prices = {"$9.99", "$15.99"};

            infoModel current = new infoModel();

            current.thumnailUrl = pics[i%pics.length];
            current.price = prices[i%prices.length];

            data.add(current);
        }

        return data;
    }

    public static class MyFragment extends Fragment implements ListenerOfClicks {

        //timer to animate the banner
        Timer timer;
        int page = 1;

        private RecyclerView rv;
        private ViewPager vpBanner;

        //paging things
        //http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
        private int previousTotal = 0;
        private boolean loading = true;
        private int visibleThreshold = 5;
        int firstVisibleItem, visibleItemCount, totalItemCount;


        public static MyFragment getInstance(int posiiton) {

            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", posiiton);
            myFragment.setArguments(args);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.myfragment_layout, container, false);
            //tvPosition = (TextView) layout.findViewById(R.id.tvPosition);
            Bundle bundle = getArguments();

            if (bundle != null) {

                // tvPosition.setText("The page selected is " + bundle.getInt("position"));
            }

            //setup banner on top of each fragment
            vpBanner = (ViewPager) layout.findViewById(R.id.vpBanner);
            VpBannerAdapter vpBannerAdapter = new VpBannerAdapter(getContext());
            vpBanner.setAdapter(vpBannerAdapter);

            //setup recyccler view
            rv = (RecyclerView) layout.findViewById(R.id.rv_recycler_view);
            rv.setHasFixedSize(true);
            adapter = new RVAdapter(getDataSet());
            adapter.setClickListener(this);
            rv.setAdapter(adapter);
            final GridLayoutManager llm = new GridLayoutManager(getContext(), 2);

            rv.addOnScrollListener(new RecyclerView.OnScrollListener(){

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached

                        Log.i("Yaeye!", "end called");

                        // Do something

                        loading = true;
                        Crouton.makeText(getActivity(),"loading..", Style.INFO).show();
                    }
                }
            });

//            LinearLayoutManager llm = new LinearLayoutManager(getActivity());

            rv.setLayoutManager(llm);
           // pageSwitcher(2);

            return layout;
        }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void itemClicked(View v, int position, Bundle batton) {

        if(batton != null){

            //create intent with necessary parcelable productVM here
            Intent goToNext = new Intent(getActivity(),FullProductPage.class);
            goToNext.putExtra("productVM", batton);
            startActivity(goToNext);
        }else{

            startActivity(new Intent(getActivity(), FullProductPage.class));

        }

        }

        //make banner animation
        public void pageSwitcher(int seconds) {
            timer = new Timer(); // At this line a new Thread will be created
            timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
            // in
            // milliseconds
        }

        class RemindTask extends TimerTask {

            @Override
            public void run() {

                // As the TimerTask run on a seprate thread from UI thread we have
                // to call runOnUiThread to do work on UI thread.
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        if (page > 2) { // In my case the number of pages are 5

                            //make page = 0 to reset form the beginning
                            page = 0;
                        } else {
                            vpBanner.setCurrentItem(page++);
                        }
                    }
                });

            }
        }
    }


    public interface ListenerOfClicks{

        public void itemClicked(View v,int position, Bundle batton );
    }



}
