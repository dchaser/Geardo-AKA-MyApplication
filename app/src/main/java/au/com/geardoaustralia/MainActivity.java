package au.com.geardoaustralia;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;
import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.MainScreen.MainContentMainActivity.VpBannerAdapter;
import au.com.geardoaustralia.MainScreen.NavdrawerMainActivity.NavigationDrawerFragment;
import au.com.geardoaustralia.MainScreen.profile.ProfileScreen;
import au.com.geardoaustralia.cart.ViewCartPopup;
import au.com.geardoaustralia.categories.CategorySelectionScreen;
import au.com.geardoaustralia.utils.EndlessRecyclerViewScrollListener;
import au.com.geardoaustralia.utils.GlobalContext;
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
    public static List<ProductInfoModel> data = new ArrayList<>();

    //bottom bar controls
    private LinearLayout llCats;
    private LinearLayout llDeals;
    private LinearLayout llCart;
    private LinearLayout llAccount;
    private LinearLayout llHome;

    private ImageView ivHome;
    private ImageView ivCats;
    private ImageView ivDeals;
    private ImageView ivCart;
    private ImageView ivAccount;

    private TextView tvHome;
    private TextView tvCats;
    private TextView tvDeals;
    private TextView tvCart;
    private TextView tvAccount;

    GlobalContext globalContext;

    Button btnGoToCart;
    TextView tvCartCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        toolbar = (Toolbar) findViewById(R.id.appToolBar);
        //toolbar will automatically route to 'menu creation' onOptionsMenu is available
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Realm realm = Realm.getDefaultInstance();
        /*realm.beginTransaction();
        //... add or update objects here ...
        realm.commitTransaction();*/

        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        globalContext = GlobalContext.getInstance();

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
                switch (position) {

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

            tabHost.addTab(
                    tabHost.newTab()
                            .setText(pagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }


        setUpBottomBarControls(globalContext.selectedPage);
        clearAll(globalContext.selectedPage);


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
                            Toast.makeText(MainActivity.this, "Cart", Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getSupportFragmentManager();
                            ViewCartPopup dFragment = ViewCartPopup.newInstance("Some Title");
                            dFragment.show(fm, "cart");

                        }
                    });
                }
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mCart:

                // Show DialogFragment
                FragmentManager fm = getSupportFragmentManager();
                ViewCartPopup dFragment = ViewCartPopup.newInstance("Some Title");
                dFragment.show(fm, "cart");

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);


        }

    }


    @Override
    public void onTabSelected(MaterialTab tab) {

        viewPager.setCurrentItem(tab.getPosition());

        switch (tab.getPosition()) {

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

        public Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }
    }

    public static List<ProductInfoModel> getDataSet() {

        for (int i = 0; i < 10; i++) {

            /*
            *
            *
    public  String Title;
    public int thumnailUrl;
    public String reducedPrice;
    public String price;
    public String descriptionl;
    */

            String[] titles = {"G-link HDMI Cable Gold edition v2.0", "Xiaomi 10000mAh Power Bank", "Battery 1160mAh for Gopro Hero 4", "HOCO Dual Car Charger UC204", "GoPro TriPod Float 7 in 1 Accessories Kit"};
            int[] pics = {R.drawable.glinkhdmiblackwire, R.drawable.xiaomi_power_bank, R.drawable.gopro_battery, R.drawable.dualcarcharger, R.drawable.suctioncup};
            String[] reducedPrices = {"$132.95", "$132.95", "$119.99", "$125.99", "$113.99"};
            String[] prices = {"$12.95", "$15.99", "$19.99", "$25.99", "$3.99"};
            String[] desc = {"HDMI 2.0, which is backwards compatible with earlier versions of the HDMI specification, is the most recent update of the HDMI specification. It also enables key enhancements to support market requirements for enhancing the consumer video and audio experience.\n" +
                    "\n", "Model NDY-02-AM\n" +
                    "Battery type Lithium-ion Polymer rechargeable cell\n" +
                    "Input Voltage DC 5.0V\n" +
                    "Output Voltage DC 5.1V\n" +
                    "Input current 2000mA(TYP)", "HDMI 2.0, which is backwards compatible with earlier versions of the HDMI specification, is the most recent update of the HDMI specification. It also enables key enhancements to support market requirements for enhancing the consumer video and audio experience.\n" +
                    "\n", "Auto Suction Cup+Tripod Mount \n" +
                    "•Not included the waterproof shell.\n" +
                    "•Compatible with: GoPro HD Hero 1/2/3/3+/4. \n" +
                    "•Dash/Windshield Suction mount for GoPro Camera.\n" +
                    "•180° rotation and adjustable arm for optimum positioning.", "100% New and High Quality!\n" +
                    "40 pcs Accessories Set for GoPro Hero 2 3 3+ 4\n" +
                    "Meet most of your needs for Supporting Accessories when you are Shooting with your GoPro Hero Camera. All in One !!"};

            ProductInfoModel current = new ProductInfoModel();

            current.Title = titles[i % titles.length];
            current.thumnailUrl = pics[i % pics.length];
            current.reducedPrice = reducedPrices[i % reducedPrices.length];
            current.price = prices[i % prices.length];
            current.descriptionl = desc[i % desc.length];

            data.add(current);
        }

        return data;
    }

    private void setUpBottomBarControls(int pageNumber) {

        //Home
        llHome = (LinearLayout) findViewById(R.id.llHome);
        ivHome = (ImageView) findViewById(R.id.ivHome);
        tvHome = (TextView) findViewById(R.id.tvHome);

        //Categories
        llCats = (LinearLayout) findViewById(R.id.llCats);
        ivCats = (ImageView) findViewById(R.id.ivCats);
        tvCats = (TextView) findViewById(R.id.tvCats);
        //Deals
        llDeals = (LinearLayout) findViewById(R.id.llDeals);
        ivDeals = (ImageView) findViewById(R.id.ivDeals);
        tvDeals = (TextView) findViewById(R.id.tvDeals);
        //Cart
        llCart = (LinearLayout) findViewById(R.id.llCart);
        ivCart = (ImageView) findViewById(R.id.ivCart);
        tvCart = (TextView) findViewById(R.id.tvCart);
        //Account
        llAccount = (LinearLayout) findViewById(R.id.llAccount);
        ivAccount = (ImageView) findViewById(R.id.ivAccount);
        tvAccount = (TextView) findViewById(R.id.tvAccount);

        clearAll(pageNumber);

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 0;

            }
        });


        llCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 1;
                startActivity(new Intent(MainActivity.this, CategorySelectionScreen.class));
                clearAll(globalContext.selectedPage);
                //go to category screen
                //startActivity(new Intent(MainActivity.this, CategorySelectionScreen.class));

            }
        });


        llDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 2;
                clearAll(globalContext.selectedPage);
                // startActivity(new Intent(MainActivity.this, DealsScreen.class));

            }
        });


        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 3;
                clearAll(globalContext.selectedPage);
                //startActivity(new Intent(MainActivity.this, DisplayCartDialogFragment.class));

            }
        });


        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 3;
                clearAll(globalContext.selectedPage);
                startActivity(new Intent(MainActivity.this, ProfileScreen.class));

            }
        });

    }

    private void clearAll(int page) {

        switch (page) {

            case 0:
                //Home selected
                ivHome.setBackgroundResource(R.drawable.ic_home_blue);
                tvHome.setTextColor(getResources().getColor(R.color.lightBlue));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));

                break;


            case 1:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(getResources().getColor(R.color.gray));

                //Categories selected
                ivCats.setBackgroundResource(R.drawable.ic_categories_blue);
                tvCats.setTextColor(getResources().getColor(R.color.lightBlue));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 2:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(getResources().getColor(R.color.gray));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals selected
                ivDeals.setBackgroundResource(R.drawable.ic_search_blue);
                tvDeals.setTextColor(getResources().getColor(R.color.lightBlue));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 3:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(getResources().getColor(R.color.gray));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart Selected
                ivCart.setBackgroundResource(R.drawable.ic_cart_blue);
                tvCart.setTextColor(getResources().getColor(R.color.lightBlue));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 4:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(getResources().getColor(R.color.gray));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account Selected
                ivAccount.setBackgroundResource(R.drawable.ic_account_blue);
                tvAccount.setTextColor(getResources().getColor(R.color.lightBlue));
                break;

            default:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_blue);
                tvHome.setTextColor(getResources().getColor(R.color.lightBlue));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));
                break;
        }


    }

    public static class MyFragment extends Fragment implements ListenerOfClicks, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

        private RecyclerView rv;
        private SliderLayout mDemoSlider;
        private EndlessRecyclerViewScrollListener scrollListener;

        //constructor
        public static MyFragment getInstance(int posiiton) {

            MyFragment myFragment = new MyFragment();
            Bundle args = new Bundle();
            args.putInt("position", posiiton);
            myFragment.setArguments(args);
            return myFragment;
        }


        @Override
        public void onStop() {
            //do this firstly before the fragment is destroyed i order to prevent a memory leak
            mDemoSlider.stopAutoCycle();
            super.onStop();
        }


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.myfragment_layout, container, false);
            //tvPosition = (TextView) layout.findViewById(R.id.tvPosition);
            Bundle bundle = getArguments();
            setHasOptionsMenu(true);

            mDemoSlider = (SliderLayout) layout.findViewById(R.id.slider);
            initiateImageSlider();

            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);

            //setup recyccler view
            rv = (RecyclerView) layout.findViewById(R.id.rv_recycler_view);
            rv.setHasFixedSize(true);
            adapter = new RVAdapter(getDataSet());
            adapter.setClickListener(this);
            adapter.setHasStableIds(true);
            rv.setAdapter(adapter);
            final GridLayoutManager llm = new GridLayoutManager(getContext(), 2);
            rv.setLayoutManager(llm);
            scrollListener = new EndlessRecyclerViewScrollListener(llm) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to the bottom of the list
                    loadNextDataFromApi(page);
                }
            };
            // Adds the scroll listener to RecyclerView
            rv.addOnScrollListener(scrollListener);
            rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            System.out.println("The RecyclerView is not scrolling");
                            break;
                        case RecyclerView.SCROLL_STATE_DRAGGING:
                            System.out.println("Scrolling now");
                            break;
                        case RecyclerView.SCROLL_STATE_SETTLING:
                            System.out.println("Scroll Settling");
                            break;

                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if(dx>0)
                    {
                       // System.out.println("Scrolled Right");

                    }
                    else if(dx < 0)
                    {
                       // System.out.println("Scrolled Left");

                    }
                    else {

                      //  System.out.println("No Horizontal Scrolled");
                    }

                    if(dy>0)
                    {
                       // System.out.println("Scrolled Downwards");
                    }
                    else if(dy < 0)
                    {
                       // System.out.println("Scrolled Upwards");

                    }
                    else {

                      //  System.out.println("No Vertical Scrolled");
                    }
                }
            });
            return layout;
        }

        // Append the next page of data into the adapter
        // This method probably sends out a network request and appends new data items to your adapter.
        public void loadNextDataFromApi(int offset) {
            Toast.makeText(getActivity(), "Loading Next Batch..No data", Toast.LENGTH_SHORT).show();
            // Send an API request to retrieve appropriate paginated data
            //  --> Send the request including an offset value (i.e `page`) as a query parameter.
            //  --> Deserialize and construct new model objects from the API response
            //  --> Append the new data objects to the existing set of items inside the array of items
            //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`
        }

        private void initiateImageSlider() {

            HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
            file_maps.put("Latest Designs", R.drawable.category_one);
            file_maps.put("Cheap Prices", R.drawable.category_two);
            file_maps.put("Great After Sales", R.drawable.category_three);
            file_maps.put("Top Ratings", R.drawable.category_four);

            for (String name : file_maps.keySet()) {
                TextSliderView textSliderView = new TextSliderView(getActivity());
                textSliderView
                        .description(name)
                        .image(file_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra", name);

                mDemoSlider.addSlider(textSliderView);
            }
        }




        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void itemClicked(View v, int position, Bundle batton) {

            if (batton != null) {

                //create intent with necessary parcelable productVM here
                Intent goToNext = new Intent(getActivity(), FullProductPage.class);
                ProductInfoModel current = data.get(position);
                batton.putParcelable("productModel", current);
                goToNext.putExtra("passer", batton);
                startActivity(goToNext);
            } else {

                startActivity(new Intent(getActivity(), FullProductPage.class));

            }

        }

        @Override
        public void makeFavorite(View v, int position, Bundle batton) {
            //make the product a favorite
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


        }

        @Override
        public void onSliderClick(BaseSliderView slider) {
            Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public interface ListenerOfClicks {

        void itemClicked(View v, int position, Bundle batton);

        void makeFavorite(View v, int position, Bundle batton);
    }

    private static class RVAdapter extends RecyclerView.Adapter<RVAdapter.ProductViewHolder> {

        private final static int FADE_DURATION = 1000; // in milliseconds
        private List<ProductInfoModel> productList = new ArrayList<>();
        private MainActivity.ListenerOfClicks listenerOfClicks;


        // Provide a suitable constructor (depends on the kind of dataset)
        RVAdapter(List<ProductInfoModel> myDataset) {
            this.productList = myDataset;
        }

        @Override
        public RVAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            ProductViewHolder vh = new ProductViewHolder(mview);
            return vh;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {

            holder.ivProductImage.setImageResource(productList.get(position).thumnailUrl);
            holder.tvProductTitle.setText(productList.get(position).Title);
            holder.tvProductReducedPrice.setText(productList.get(position).reducedPrice);
            holder.tvProductPrice.setText(productList.get(position).price);
            //holder.tvProductDescription.setText(productList.get(position).descriptionl);
// Set the view to fade in
            //setFadeAnimation(holder.card_view);
        }

        private void setFadeAnimation(View view) {
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setDuration(FADE_DURATION);
            view.startAnimation(anim);
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        void setClickListener(MainActivity.ListenerOfClicks listenerOfClicks) {
            this.listenerOfClicks = listenerOfClicks;
        }


        class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            ToggleButton ibFavorite;
            CardView card_view;
            ImageView ivProductImage;
            TextView tvProductTitle;
            TextView tvProductReducedPrice;
            TextView tvProductPrice;
            // TextView tvProductDescription;


            public ProductViewHolder(View itemView) {
                super(itemView);

                ibFavorite = (ToggleButton) itemView.findViewById(R.id.ibFavorite);
                card_view = (CardView) itemView.findViewById(R.id.card_view);
                card_view.setOnClickListener(this);

                ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
                tvProductTitle = (TextView) itemView.findViewById(R.id.tvProductTitle);
                tvProductReducedPrice = (TextView) itemView.findViewById(R.id.tvProductReducedPrice);
                tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
                //tvProductDescription = (TextView) itemView.findViewById(R.id.tvProductDescription);
            }

            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.card_view) {
                    if (listenerOfClicks != null) {

                        listenerOfClicks.itemClicked(v, getAdapterPosition(), new Bundle());
                    }

                }


                if (v.getId() == R.id.ibFavorite) {
                    if (listenerOfClicks != null) {

                        listenerOfClicks.makeFavorite(v, getAdapterPosition(), new Bundle());
                    }

                }


            }


        }

    }


}
