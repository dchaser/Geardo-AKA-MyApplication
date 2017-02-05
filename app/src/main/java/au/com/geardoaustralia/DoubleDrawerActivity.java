package au.com.geardoaustralia;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class DoubleDrawerActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private View mLeftDrawerView;
    private View mRightDrawerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_drawer);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mDrawerLayout == null || mLeftDrawerView == null || mRightDrawerView == null || mDrawerToggle == null) {
            // Configure navigation drawer
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mLeftDrawerView = findViewById(R.id.left_drawer);
            mRightDrawerView = findViewById(R.id.right_drawer);
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.drawer_open, R.string.drawer_close) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View drawerView) {
                    if(drawerView.equals(mLeftDrawerView)) {
//                        getSupportActionBar().setTitle(getTitle());
                        supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                        mDrawerToggle.syncState();
                    }
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    if(drawerView.equals(mLeftDrawerView)) {
//                        getSupportActionBar().setTitle(getString(R.string.app_name));
                        supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                        mDrawerToggle.syncState();
                    }
                }

                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    // Avoid normal indicator glyph behaviour. This is to avoid glyph movement when opening the right drawer
                    //super.onDrawerSlide(drawerView, slideOffset);
                }
            };

            mDrawerLayout.setDrawerListener(mDrawerToggle); // Set the drawer toggle as the DrawerListener
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        // If the nav drawer is open, hide action items related to the content view
        for(int i = 0; i< menu.size(); i++)
            menu.getItem(i).setVisible(!mDrawerLayout.isDrawerOpen(mLeftDrawerView));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerToggle.onOptionsItemSelected(item);

                if(mDrawerLayout.isDrawerOpen(mRightDrawerView))
                    mDrawerLayout.closeDrawer(mRightDrawerView);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
