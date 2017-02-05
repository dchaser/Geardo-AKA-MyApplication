/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.com.geardoaustralia.cartNew;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.geardoaustralia.DoubleDrawerActivity;
import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.framework.CartPresenter;
import au.com.geardoaustralia.cartNew.framework.CartPresenterImpl;
import au.com.geardoaustralia.cartNew.framework.CartView;
import au.com.geardoaustralia.cartNew.util.MenuCounterDrawable;
import au.com.geardoaustralia.cartNew.widget.MultiSwipeRefreshLayout;
import au.com.geardoaustralia.categories.CategorySelectionScreen;
import butterknife.Bind;

import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGD;
import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGW;
import static au.com.geardoaustralia.cartNew.util.LogUtils.makeLogTag;

/**
 * A base activity that handles common functionality in the app. This includes the
 * navigation drawer, login and authentication, Action Bar tweaks, amongst others.
 */
public abstract class BaseActivity extends AppCompatActivity implements MultiSwipeRefreshLayout.CanChildScrollUpCallback, Toolbar.OnMenuItemClickListener, CartView {


    private static final String TAG = makeLogTag(BaseActivity.class);

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    // Primary toolbar and drawer toggle
    public Toolbar baseToolbar;

    boolean doubleBackToExitPressedOnce = false;

    // variables that control the Action Bar auto hide behavior (aka "quick recall")
    private boolean mActionBarAutoHideEnabled = false;

    private int mActionBarAutoHideSensivity = 0;

    private int mActionBarAutoHideMinY = 0;

    private boolean mActionBarShown = true;


    // When set, these components will make baseToolbar be shown/hidden in sync with the action bar
    // to implement the "quick recall" effect (the Action Bar and the header views disappear
    // when you scroll down a list, and reappear quickly when you scroll up).
    private ArrayList<View> mHideableHeaderViews = new ArrayList<View>();

    private ObjectAnimator mStatusBarColorAnimator;

    private static final TypeEvaluator ARGB_EVALUATOR = new ArgbEvaluator();

    // Durations for certain animations we use:
    private static final int HEADER_HIDE_ANIM_DURATION = 300;

    // SwipeRefreshLayout allows the user to swipe the screen down to trigger a manual refresh
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

    private int mProgressBarTopWhenActionBarShown;

    private int mActionBarAutoHideSignal = 0;

    //--- android MVP ---//
    public Menu menu;
    public CartPresenter cartPresenter;
    public SearchView searchView;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mLeftDrawerView;
    private View mRightDrawerView;
    private Intent intent = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        baseToolbar = (Toolbar) findViewById(R.id.appToolBar);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        cartPresenter = new CartPresenterImpl(this);

        if(intent == null){

            handleIntent(getIntent());
        }


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //trySetupSwipeRefresh();
        //updateSwipeRefreshProgressBarTop();

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(MAIN_CONTENT_FADEIN_DURATION);
        } else {
            LOGW(TAG, "No view with ID main_content to fade in.");
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // this will help to load items in cart for all activity who are extending this class
        cartPresenter.showMeItemsInCart();
    }


//    private void trySetupSwipeRefresh() {
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setColorSchemeResources(
//                    R.color.flat_button_text);
//            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
////                    requestDataRefresh();
//                    LOGD(TAG, "Swiperefresh onRefresh");
//                }
//            });
//
//            if (mSwipeRefreshLayout instanceof MultiSwipeRefreshLayout) {
//                MultiSwipeRefreshLayout mswrl = (MultiSwipeRefreshLayout) mSwipeRefreshLayout;
//                mswrl.setCanChildScrollUpCallback(this);
//            }
//        }
//    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    protected Toolbar getActionBarToolbar() {
        if (baseToolbar == null) {
            baseToolbar = (Toolbar) findViewById(R.id.appToolBar);
            if (baseToolbar != null) {
                // Depending on which version of Android you are on the Toolbar or the ActionBar may be
                // active so the a11y description is set here.
//                mActionBarToolbar.setNavigationContentDescription(getResources().getString(R.string
//                        .navdrawer_description_a11y));
                setSupportActionBar(baseToolbar);
            }
        }
        return baseToolbar;
    }


    public void enableActionBarAutoHide(RecyclerView listview) {
        initActionBarAutoHide();
        listview.setOnScrollListener(new RecyclerView.OnScrollListener() {

            private static final int HIDE_THRESHOLD = 180;
            private int scrolledDistance = 0;
            private boolean controlsVisible = true;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                    autoShowOrHideActionBar(false);
                    controlsVisible = false;
                    scrolledDistance = 0;
                } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                    autoShowOrHideActionBar(true);
                    controlsVisible = true;
                    scrolledDistance = 0;
                }

                if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                    scrolledDistance += dy;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    protected void enableActionBarAutoHide(final ListView listView) {
        initActionBarAutoHide();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            /** The heights of all items. */
            private Map<Integer, Integer> heights = new HashMap<Integer, Integer>();
            private int lastCurrentScrollY = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {

                // Get the first visible item's view.
                View firstVisibleItemView = view.getChildAt(0);
                if (firstVisibleItemView == null) {
                    return;
                }

                // Save the height of the visible item.
                heights.put(firstVisibleItem, firstVisibleItemView.getHeight());

                // Calculate the height of all previous (hidden) items.
                int previousItemsHeight = 0;
                for (int i = 0; i < firstVisibleItem; i++) {
                    previousItemsHeight += heights.get(i) != null ? heights.get(i) : 0;
                }

                int currentScrollY = previousItemsHeight - firstVisibleItemView.getTop()
                        + view.getPaddingTop();

                onMainContentScrolled(currentScrollY, currentScrollY - lastCurrentScrollY);

                lastCurrentScrollY = currentScrollY;
            }
        });
    }

    /**
     * Indicates that the main content has scrolled (for the purposes of showing/hiding
     * the action bar for the "action bar auto hide" effect). currentY and deltaY may be exact
     * (if the underlying view supports it) or may be approximate indications:
     * deltaY may be INT_MAX to mean "scrolled forward indeterminately" and INT_MIN to mean
     * "scrolled backward indeterminately".  currentY may be 0 to mean "somewhere close to the
     * start of the list" and INT_MAX to mean "we don't know, but not at the start of the list"
     */
    private void onMainContentScrolled(int currentY, int deltaY) {
        if (deltaY > mActionBarAutoHideSensivity) {
            deltaY = mActionBarAutoHideSensivity;
        } else if (deltaY < -mActionBarAutoHideSensivity) {
            deltaY = -mActionBarAutoHideSensivity;
        }

        if (Math.signum(deltaY) * Math.signum(mActionBarAutoHideSignal) < 0) {
            // deltaY is a motion opposite to the accumulated signal, so reset signal
            mActionBarAutoHideSignal = deltaY;
        } else {
            // add to accumulated signal
            mActionBarAutoHideSignal += deltaY;
        }

        boolean shouldShow = currentY < mActionBarAutoHideMinY ||
                (mActionBarAutoHideSignal <= -mActionBarAutoHideSensivity);
        autoShowOrHideActionBar(shouldShow);
    }


    protected void autoShowOrHideActionBar(boolean show) {
        if (show == mActionBarShown) {
            return;
        }

        mActionBarShown = show;
//        onActionBarAutoShowOrHide(show);
    }

    /**
     * Initializes the Action Bar auto-hide (aka Quick Recall) effect.
     */
    private void initActionBarAutoHide() {
        mActionBarAutoHideEnabled = true;
        mActionBarAutoHideMinY = getResources().getDimensionPixelSize(
                R.dimen.action_bar_auto_hide_min_y);
        mActionBarAutoHideSensivity = getResources().getDimensionPixelSize(
                R.dimen.action_bar_auto_hide_sensivity);
    }

    protected void registerHideableHeaderView(View hideableHeaderView) {
        if (!mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.add(hideableHeaderView);
        }
    }

    protected void deregisterHideableHeaderView(View hideableHeaderView) {
        if (mHideableHeaderViews.contains(hideableHeaderView)) {
            mHideableHeaderViews.remove(hideableHeaderView);
        }
    }

    /**
     * This utility method handles Up navigation intents by searching for a parent activity and
     * navigating there if defined. When using this for an activity make sure to define both the
     * native parentActivity as well as the AppCompat one when supporting API levels less than 16.
     * when the activity has a single parent activity. If the activity doesn't have a single parent
     * activity then don't define one and this method will use back button functionality. If "Up"
     * functionality is still desired for activities without parents then use
     * {@code syntheticParentActivity} to define one dynamically.
     * <p>
     * Note: Up navigation intents are represented by a back arrow in the top left of the Toolbar
     * in Material Design guidelines.
     *
     * @param currentActivity         Activity in use when navigate Up action occurred.
     * @param syntheticParentActivity Parent activity to use when one is not already configured.
     */
    public static void navigateUpOrBack(Activity currentActivity,
                                        Class<? extends Activity> syntheticParentActivity) {
        // Retrieve parent activity from AndroidManifest.
        Intent intent = NavUtils.getParentActivityIntent(currentActivity);

        // Synthesize the parent activity when a natural one doesn't exist.
        if (intent == null && syntheticParentActivity != null) {
            try {
                intent = NavUtils.getParentActivityIntent(currentActivity, syntheticParentActivity);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (intent == null) {
            // No parent defined in manifest. This indicates the activity may be used by
            // in multiple flows throughout the app and doesn't have a strict parent. In
            // this case the navigation up button should act in the same manner as the
            // back button. This will result in users being forwarded back to other
            // applications if currentActivity was invoked from another application.
            currentActivity.onBackPressed();
        } else {
            if (NavUtils.shouldUpRecreateTask(currentActivity, intent)) {
                // Need to synthesize a backstack since currentActivity was probably invoked by a
                // different app. The preserves the "Up" functionality within the app according to
                // the activity hierarchy defined in AndroidManifest.xml via parentActivity
                // attributes.
                TaskStackBuilder builder = TaskStackBuilder.create(currentActivity);
                builder.addNextIntentWithParentStack(intent);
                builder.startActivities();
            } else {
                // Navigate normally to the manifest defined "Up" activity.
                NavUtils.navigateUpTo(currentActivity, intent);
            }
        }
    }

    private void updateSwipeRefreshProgressBarTop() {
        if (mSwipeRefreshLayout == null) {
            return;
        }

        int progressBarStartMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_start_margin);
        int progressBarEndMargin = getResources().getDimensionPixelSize(
                R.dimen.swipe_refresh_progress_bar_end_margin);
        int top = mActionBarShown ? mProgressBarTopWhenActionBarShown : 0;
        mSwipeRefreshLayout.setProgressViewOffset(false,
                top + progressBarStartMargin, top + progressBarEndMargin);
    }


    protected void onActionBarAutoShowOrHide(boolean shown) {
        if (mStatusBarColorAnimator != null) {
            mStatusBarColorAnimator.cancel();
        }
        mStatusBarColorAnimator.setEvaluator(ARGB_EVALUATOR);
        mStatusBarColorAnimator.start();

        updateSwipeRefreshProgressBarTop();

        for (final View view : mHideableHeaderViews) {
            if (shown) {
                ViewCompat.animate(view)
                        .translationY(0)
                        .alpha(1)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator())
                                // Setting Alpha animations should be done using the
                                // layer_type set to layer_type_hardware for the duration of the animation.
                        .withLayer();
            } else {
                ViewCompat.animate(view)
                        .translationY(-view.getBottom())
                        .alpha(0)
                        .setDuration(HEADER_HIDE_ANIM_DURATION)
                        .setInterpolator(new DecelerateInterpolator())
                                // Setting Alpha animations should be done using the
                                // layer_type set to layer_type_hardware for the duration of the animation.
                        .withLayer();
            }
        }
    }

    @Override
    public boolean canSwipeRefreshChildScrollUp() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (this instanceof CartActivity) {
            return true;
        }
        getMenuInflater().inflate(R.menu.home_act_filtered, menu);
        this.menu = menu;

        MenuItem searchItem = menu.findItem(R.id.mSearch);

        searchView = (SearchView) menu.findItem(R.id.mSearch).getActionView();

        if (isAlwaysExpanded()) {
            searchView.setIconifiedByDefault(false);
        } else {
            searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        }

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        List<SearchableInfo> searchableInfos = searchManager.getSearchablesInGlobalSearch();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this,MainActivity.class)));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               // Log.d("Query Submitted", "Query SubmittedQuery SubmittedQuery SubmittedQuery SubmittedQuery SubmittedQuery SubmittedQuery SubmittedQuery Submitted");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.d("Query", newText);
                return true;
            }
        });

        // getMenuInflater().inflate(R.menu.home_act_filtered, menu);
       // searchView = (SearchView) menu.findItem(R.id.mSearch).getActionView();

        // Add the filter & search buttons to the toolbar.
//        Toolbar toolbar = getActionBarToolbar();
//        toolbar.inflateMenu(R.menu.home_act_filtered);
//        toolbar.setOnMenuItemClickListener(this);
        cartPresenter.countCartItems();
        return true;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        this.intent = intent;
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // Do work using string

            Log.d("String " , query);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart:
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);

                return true;


        }
        return false;
    }



    protected void showRedBadgeCounter(int count) {
        // Get the notifications MenuItem and LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.menu_cart);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
        // Update LayerDrawable's MenuCounterDrawable
        setBadgeCount(this, icon, count/*DatabaseManager.getInstance().getProductCount()*/);
    }

    public void setBadgeCount(Context context, LayerDrawable icon, int count) {

        MenuCounterDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof MenuCounterDrawable) {
            badge = (MenuCounterDrawable) reuse;
        } else {
            badge = new MenuCounterDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_cart:
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                return true;

            case R.id.mSearch:
                return true;
        }
        return false;
    }

    @Override
    public void displayCounter(int size) {
        if (menu != null) {
            showRedBadgeCounter(size);
        } else {
            Toast.makeText(this, "menu object is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void displayCartItems(List items) {
        // in order to view cart items override in child classes
    }

    @Override
    public void displayCategoryItems(List items) {
        // in order to avail this feature access in child class
    }

    @Override
    public void onSuccessfulDeletion(Object item) {

    }

    @Override
    public void onProductActionResponse(boolean isActionSuccessful) {

    }

    @Override
    public void onBackPressed() {
        this.doubleBackToExitPressedOnce = true;

        //modified not fixed
        if(this instanceof MainActivity){
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }


    }
}
