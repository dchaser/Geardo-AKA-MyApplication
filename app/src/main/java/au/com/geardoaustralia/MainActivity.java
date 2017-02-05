package au.com.geardoaustralia;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.MainScreen.NavdrawerMainActivity.NavigationDrawerLeft;
import au.com.geardoaustralia.cartNew.BaseActivity;
import au.com.geardoaustralia.cartNew.CartActivity;
import au.com.geardoaustralia.cartNew.CommonTabFragment;
import au.com.geardoaustralia.cartNew.database.DatabaseHelper;
import au.com.geardoaustralia.categories.CategorySelectionScreen;
import au.com.geardoaustralia.login.SignupActivity;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.MenuBarHandler;
import au.com.geardoaustralia.utils.Sliding;
import au.com.geardoaustralia.utils.utilKit;

import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGD;

public class MainActivity extends BaseActivity implements CommonTabFragment.TabListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager viewPager;
    private TabLayout mTabLayout;
    MenuBarHandler menuBarHandler;
    OurViewPagerAdapter mViewPagerAdapter = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        // Add the back button to the toolbar.
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.hamburger);
        toolbar.setNavigationContentDescription(R.string.close_and_go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    startActivity(new Intent(MainActivity.this, TopSliderActivity.class));
                    overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });


        menuBarHandler = new MenuBarHandler(MainActivity.this);


        //setup tabs
        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);

        new Handler().postDelayed(
                new Runnable() {
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

        if (menuBarHandler != null) {
            GlobalContext globalContext = GlobalContext.getInstance();
            globalContext.selectedPage = 0;
            menuBarHandler = new MenuBarHandler(MainActivity.this);
        }
    }


    private void renderTabsWithPage() {

        mViewPagerAdapter = new OurViewPagerAdapter(getSupportFragmentManager());
        MyFragment first = new MyFragment();
        Bundle f = new Bundle();
        f.putInt("tag", 0);
        first.setArguments(f);
        MyFragment second = new MyFragment();
        Bundle s = new Bundle();
        s.putInt("tag", 1);
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

        public void addFragment(Fragment fragment, String title) {
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