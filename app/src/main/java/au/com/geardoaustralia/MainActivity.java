package au.com.geardoaustralia;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.com.geardoaustralia.MainScreen.NavdrawerMainActivity.NavigationDrawerFragment;
import au.com.geardoaustralia.cartNew.BaseActivity;
import au.com.geardoaustralia.cartNew.CommonTabFragment;
import au.com.geardoaustralia.cartNew.database.DatabaseHelper;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.MenuBarHandler;

import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGD;

public class MainActivity extends BaseActivity implements CommonTabFragment.TabListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private NavigationDrawerFragment drawerFragment;

    private ViewPager viewPager;
    private TabLayout mTabLayout;

    MenuBarHandler menuBarHandler;

    //Database Objects
    DatabaseHelper databaseHelper;

    OurViewPagerAdapter mViewPagerAdapter = null;
    private Set<CommonTabFragment> mTabFragments = new HashSet<CommonTabFragment>();

    //titles for tab layout items (indices must correspond to the above)
    private static final int[] TAB_TITLE_RES_ID = new int[]{
            R.string.on_sale,
    };


    SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), baseToolbar);

        menuBarHandler = new MenuBarHandler(MainActivity.this);
        //setup tabs
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);

        new Handler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        renderTabsWithPage();

                        //select the first tab
                        mTabLayout.getTabAt(0).select();


                        mViewPagerAdapter.notifyDataSetChanged();
                    }
                }, 100);




    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();

        if(menuBarHandler != null){
            GlobalContext globalContext = GlobalContext.getInstance();
            globalContext.selectedPage = 0;
            menuBarHandler = new MenuBarHandler(MainActivity.this);
        }
    }


    private void renderTabsWithPage() {

        mViewPagerAdapter = new OurViewPagerAdapter(getSupportFragmentManager());
        MyFragment first = new MyFragment();
        Bundle f = new Bundle();
        f.putInt("tag",0);
        first.setArguments(f);
        MyFragment second = new MyFragment();
        Bundle s = new Bundle();
        s.putInt("tag",1);
        second.setArguments(s);
        mViewPagerAdapter.addFragment(first, "Latest");
        mViewPagerAdapter.addFragment(second, "Trendy");
        viewPager.setAdapter(mViewPagerAdapter);
        mTabLayout.setTabsFromPagerAdapter(mViewPagerAdapter);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                viewPager.setCurrentItem(tab.getPosition(), true);

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {

                    case 0:
                        baseToolbar.setTitle(R.string.latest);
                        break;

                    case 1:
                        baseToolbar.setTitle(R.string.most_viewed);
                        break;

                    case 2:
                        baseToolbar.setTitle(R.string.trendy);
                        break;

                    case 3:
                        baseToolbar.setTitle(R.string.on_sale);
                        break;

                    default:
                        baseToolbar.setTitle(R.string.app_name);
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do nothing
            }
        });

        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.gold));

        viewPager.setPageMargin(getResources()
                .getDimensionPixelSize(R.dimen.home_page_margin));
        viewPager.setPageMarginDrawable(R.drawable.page_margin);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onTabFragmentViewCreated(Fragment fragment) {

    }




    @Override
    public void onTabFragmentAttached(Fragment fragment) {

    }

    @Override
    public void onTabFragmentDetached(Fragment fragment) {

    }

    private class OurViewPagerAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public OurViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public Fragment getItem(int position) {
            LOGD(TAG, "Creating fragment #" + position);
            return fragments.get(position);
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }



}