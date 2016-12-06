package au.com.geardoaustralia.FullProductScreen;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;
import au.com.geardoaustralia.cart.ShoppingCartHelper;
import au.com.geardoaustralia.cart.ViewCartPopup;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.gallery.GalleryAdapter;
import au.com.geardoaustralia.gallery.SlideshowDialogFragment;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by DasunPC on 11/1/16.
 */

public class FullProductPage extends AppCompatActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private MaterialTabHost mTabHost;
    private ViewPager mPager;

    Bundle bundle;
    FloatingActionButton fabAddToCart;
    RelativeLayout badge_layout1;
    Button btnGoToCart;
    TextView tvCartCount;
    private Menu mMenu;
    private BottomSheetBehavior bottomSheetBehavior;
    int flag = 0;

    Button b1;
    MenuItem cartMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_product_screen);


        toolbar = (Toolbar) findViewById(R.id.appToolBarSubActivity);
        //toolbar will automatically route to menu creation onOptionsMenu is available
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        bundle = intent.getBundleExtra("passer");

        //setup tab host
        mTabHost = (MaterialTabHost) findViewById(R.id.mTabHost);
        mPager = (ViewPager) findViewById(R.id.mPager);

        // init view pager
        ProductPageTabAdapter mPagerAdapter = new ProductPageTabAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change the
                mTabHost.setSelectedNavigationItem(position);

                //change heading title
                switch (position) {

                    case 0:
                        toolbar.setTitle(R.string.overview);
                        break;

                    case 1:
                        toolbar.setTitle(R.string.related);
                        break;

                    case 2:
                        toolbar.setTitle(R.string.on_sale);
                        break;

                    default:
                        toolbar.setTitle(R.string.app_name);
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

                GlobalContext global = GlobalContext.getInstance();

                if (bundle != null) {

                    ProductInfoModel infoModel = bundle.getParcelable("productModel");

                    int existingCountIfAny = ShoppingCartHelper.getProductQuantity(infoModel);
                    tvCartCount.setBackgroundResource(android.R.drawable.ic_notification_overlay);

                    if (existingCountIfAny == 0) {
                        ShoppingCartHelper.setQuantity(infoModel, 1);
                    } else if (existingCountIfAny > 0) {
                        ShoppingCartHelper.setQuantity(infoModel, ++existingCountIfAny);
                    }


                    if (mMenu != null) {

                        int size = ShoppingCartHelper.getCartTotalCount();
                        tvCartCount.setText(size + "");
                    }

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //invoke bottom sheet to display

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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        //handle search bar
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.mSearch).getActionView();
        // Assumes current activity is the searchable activity
        ComponentName componentName = new ComponentName(this, MainActivity.class);//getComponentName();
        SearchableInfo info = searchManager.getSearchableInfo(componentName);
        searchView.setSearchableInfo(info);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("App", "setOnSearchClickListener");
                if (searchView.getQuery().length() == 0)
                    searchView.setQuery("", true);
            }
        });

        final Menu menus = menu;
        // SetUpMenu(mMenu);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                View view = (View) findViewById(R.id.mCart);
                MenuItem item = menus.getItem(1);
                if (item.getItemId() == R.id.mCart) {
                    item.setActionView(R.layout.notification_update_count_layout);
                    View itemChooser = item.getActionView();
                    tvCartCount = (TextView) itemChooser.findViewById(R.id.tvCartCount);
                    btnGoToCart = (Button) itemChooser.findViewById(R.id.btnGoToCart);
                    btnGoToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           // Toast.makeText(FullProductPage.this, "Cart", Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getSupportFragmentManager();
                            ViewCartPopup dFragment = ViewCartPopup.newInstance("Some Title");
                            dFragment.show(fm, "cart");

                        }
                    });
                }
            }
        });


        mMenu = menu;


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;


            case R.id.mCart:
                FragmentManager fm = getSupportFragmentManager();
                ViewCartPopup dFragment = ViewCartPopup.newInstance("Some Title");
                dFragment.show(fm, "cart");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }




    @Override
    public void onTabSelected(MaterialTab tab) {

        //when new tab selected change the viewpager
        mPager.setCurrentItem(tab.getPosition());

        switch (tab.getPosition()) {

            case 0:
                toolbar.setTitle(R.string.overview);
                break;

            case 1:
                toolbar.setTitle(R.string.related);
                break;

            case 2:
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

    private class ProductPageTabAdapter extends FragmentStatePagerAdapter {

        int icons[] = {R.drawable.ic_border_inner_black_48dp, R.drawable.ic_unfold_more_black_48dp, R.drawable.ic_attach_money_black_48dp, R.drawable.ic_apps_black_48dp};

        public ProductPageTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //chaging between GLide iage gallery and Staggered grid layout 2016/11/09
            return FullProductPage.FullProductPageFragment.getInstance(bundle);
            //return VerticalStaggeredGridFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.cats)[position];
        }

        public Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }
    }

    public static class FullProductPageFragment extends Fragment implements FullProductPage.ProductPageClickListener {


        private String TAG = MainActivity.class.getSimpleName();
        private static final String endpoint = "http://www.delaroystudios.com/glide/json/glideimages.json";
        private ArrayList<Image> images;
        private ProgressDialog pDialog;
        private GalleryAdapter mAdapter;
        private RecyclerView recyclerView;
        public TextView tvImgCount;
        private int countOfImages;

        private TextView pTitle;
        private TextView pShortDesc;
        private TextView tvReducedPrice;
        private TextView pPrice;

        ProductInfoModel infoModel;

        FloatingActionButton icAddToWishListt;


        public static FullProductPage.FullProductPageFragment getInstance(Bundle passer) {

            FullProductPage.FullProductPageFragment fullProductPageFragment = new FullProductPage.FullProductPageFragment();
            fullProductPageFragment.setArguments(passer);
            return fullProductPageFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.product_page_fragment_layout, container, false);
            //tvPosition = (TextView) layout.findViewById(R.id.tvPosition);
            Bundle bundle = getArguments();

            if (bundle != null) {

                infoModel = bundle.getParcelable("productModel");
            }

            recyclerView = (RecyclerView) layout.findViewById(R.id.gallery_recycler_view);
            tvImgCount = (TextView) layout.findViewById(R.id.tvImgCount);
            pTitle = (TextView) layout.findViewById(R.id.pTitle);
            pShortDesc = (TextView) layout.findViewById(R.id.pShortDesc);
            tvReducedPrice = (TextView) layout.findViewById(R.id.tvReducedPrice);
            pPrice = (TextView) layout.findViewById(R.id.pPrice);

            icAddToWishListt = (FloatingActionButton) layout.findViewById(R.id.icAddToWishListt);
            icAddToWishListt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Crouton.makeText(getActivity(), "Item Added to Wishlist", Style.INFO).show();
                }
            });

            pTitle.setText(infoModel.Title);
            pShortDesc.setText(infoModel.descriptionl);
            tvReducedPrice.setText(infoModel.reducedPrice);
            pPrice.setText(infoModel.price);

            pDialog = new ProgressDialog(getActivity());
            images = new ArrayList<>();
            mAdapter = new GalleryAdapter(getActivity().getApplicationContext(), images);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);


            recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {

                @Override
                public void onClick(View view, int position) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("images", images);
                    bundle.putInt("position", position);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            fetchImages();
            return layout;
        }


        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }


        @Override
        public void productPageClicked(View v, int position) {


        }

        private void fetchImages() {

            pDialog.setMessage("please wait...");
            pDialog.show();

            JsonArrayRequest req = new JsonArrayRequest(endpoint,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());
                            pDialog.hide();
                            images.clear();

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject object = response.getJSONObject(i);
                                    Image image = new Image();
                                    image.setName(object.getString("name"));

                                    JSONObject url = object.getJSONObject("url");
                                    image.setSmall(url.getString("small"));
                                    image.setMedium(url.getString("medium"));
                                    image.setLarge(url.getString("large"));
                                    image.setTimestamp(object.getString("timestamp"));

                                    images.add(image);

                                } catch (JSONException e) {
                                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                                }
                            }

                            tvImgCount.setText(String.valueOf(response.length()));
                            mAdapter.notifyDataSetChanged();


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error: " + error.getMessage());
                    pDialog.hide();
                }
            });

            // Adding request to request queue
            GlobalContext.getInstance().addToRequestQueue(req);
        }
    }

    public interface ProductPageClickListener {

        public void productPageClicked(View v, int position);
    }


}


