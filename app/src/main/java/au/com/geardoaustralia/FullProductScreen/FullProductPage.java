package au.com.geardoaustralia.FullProductScreen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.BaseActivity;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.cartNew.util.LogUtils;
import au.com.geardoaustralia.cartNew.widget.AddCartDialogActivity;
import au.com.geardoaustralia.gallery.GalleryAdapter;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.gallery.SlideshowDialogFragment;
import au.com.geardoaustralia.utils.GlobalContext;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGD;

/**
 * Created by DasunPC on 11/1/16.
 */

public class FullProductPage extends BaseActivity implements MaterialTabListener {

    private static final String TAG = LogUtils.makeLogTag(FullProductPage.class);
    public static final String SELECTED_PRODUCT_ID = "au.com.geardoaustralia.FullProductScreen.SELECTED_PRODUCT_ID";
    public static final int REQUEST_TO_ADD_IN_CART = 1;
    public static final String SELECTED_PRODUCT = "";


    private MaterialTabHost mTabHost;
    private ViewPager mPager;

    Bundle bundle;
    public static FloatingActionButton fabAddToCart;
    RelativeLayout badge_layout1;
    Button btnGoToCart;
    TextView tvCartCount;
    private Menu mMenu;
    private BottomSheetBehavior bottomSheetBehavior;
    private ListView lvSizeSelector;
    int flag = 0;

    Button b1;
    MenuItem cartMenuItem;

    public int productId;
    public Product selectedProduct;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_product_screen);

        productId = getIntent().getIntExtra(SELECTED_PRODUCT_ID, 0);

        try {
            //get the product from DB, which matching the ID above.
            DatabaseManager manager = DatabaseManager.getInstance();
            selectedProduct = manager.getProductById(productId);
            bundle = new Bundle();
            bundle.putInt(FullProductPage.SELECTED_PRODUCT_ID, productId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add the back button to the toolbar.
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.ic_up);
        toolbar.setNavigationContentDescription(R.string.close_and_go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateUpOrBack(FullProductPage.this, null);
            }
        });

        //setup tab host
        mTabHost = (MaterialTabHost) findViewById(R.id.mTabHost);
        mPager = (ViewPager) findViewById(R.id.mPager);
        lvSizeSelector = (ListView) findViewById(R.id.lvSizeSelector);
        // init view pager
        ProductPageTabAdapter mPagerAdapter = new ProductPageTabAdapter(getSupportFragmentManager());

        //Create a bundle to pass the selected product ID to sub fragment
        FullProductPageFragment fullProductPageFragment = FullProductPageFragment.getInstance(bundle);
        RealedProductsFragment realedProductsFragment = RealedProductsFragment.getInstance(bundle);
        RatingsFragment ratingsFragment = RatingsFragment.getInstance(bundle);
        mPagerAdapter.addFragment(fullProductPageFragment, "OVERVIEW");
        mPagerAdapter.addFragment(realedProductsFragment, "RELATED");
        mPagerAdapter.addFragment(ratingsFragment, "RATINGS");
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change the
                mTabHost.setSelectedNavigationItem(position);

                //change heading title
                switch (position) {

                    case 0:
                        baseToolbar.setTitle(R.string.overview);
                        break;

                    case 1:
                        baseToolbar.setTitle(R.string.related);
                        break;

                    case 2:
                        baseToolbar.setTitle(R.string.on_sale);
                        break;

                    default:
                        baseToolbar.setTitle(R.string.app_name);
                        break;
                }
            }
        });

        // insert all tabs from pagerAdapter data
        for (int i = 0; i < mPagerAdapter.getCount(); i++) {

            mTabHost.addTab(
                    mTabHost.newTab()
                            .setText(mPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

        fabAddToCart = (FloatingActionButton) findViewById(R.id.fabAddToCart);

        fabAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Product product = bundle.getParcelable(FullProductPage.SELECTED_PRODUCT);

                if (bundle != null) {


//                    lvSizeSelector.setAdapter(new ArrayAdapter<String>(FullProductPage.this, android.R.layout.simple_list_item_1, android.R.id.text1, infoModel.sizes));
//                    lvSizeSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//                        }
//                    });

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //invoke bottom sheet to display

                    Intent intent = new Intent(FullProductPage.this, AddCartDialogActivity.class);
                    intent.putExtra(AddCartDialogActivity.SELECTED_PRODUCT_ITEM, selectedProduct);
                    startActivityForResult(intent, AddCartDialogActivity.REQUEST_TO_ADD_IN_CART);

                }
            }
        });
        b1 = (Button) findViewById(R.id.b1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 0) {

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (flag == 1) {

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });

        View bsm = findViewById(R.id.bsm1);
        bottomSheetBehavior = BottomSheetBehavior.from(bsm);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == bottomSheetBehavior.STATE_EXPANDED) {

                    b1.setText("Collapse");
                    flag = 1;
                } else if (newState == bottomSheetBehavior.STATE_HIDDEN) {

                    b1.setText("Expand");
                    flag = 0;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddCartDialogActivity.REQUEST_TO_ADD_IN_CART) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                boolean isInCart = data.getBooleanExtra(AddCartDialogActivity.CART_STATUS, false);
                selectedProduct.isInCart = isInCart;

            }
            //mAdapter.notifyDataSetChanged();
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {

        //when new tab selected change the viewpager
        mPager.setCurrentItem(tab.getPosition());

        switch (tab.getPosition()) {

            case 0:
                baseToolbar.setTitle(R.string.overview);
                break;

            case 1:
                baseToolbar.setTitle(R.string.related);
                break;

            case 2:
                baseToolbar.setTitle(R.string.on_sale);
                break;

            default:
                baseToolbar.setTitle(R.string.app_name);
                break;

        }
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    private class ProductPageTabAdapter extends FragmentStatePagerAdapter {

        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public ProductPageTabAdapter(FragmentManager fm) {
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


    public interface ProductPageClickListener {

        public void overViewClicked(View v, int position);

        public void relatedClickedViewClicked(View v, int position);

        public void ratingsClicked(View v, int position);

    }



}


