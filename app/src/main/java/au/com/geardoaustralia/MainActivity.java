package au.com.geardoaustralia;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;
import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.MainScreen.MainContentMainActivity.VpBannerAdapter;
import au.com.geardoaustralia.MainScreen.NavdrawerMainActivity.NavigationDrawerFragment;
import au.com.geardoaustralia.categories.CategorySelectionScreen;
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
    public static List<ProductInfoModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appbar);

        toolbar = (Toolbar) findViewById(R.id.appToolBar);
        //toolbar will automatically route to 'menu creation' onOptionsMenu is available
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
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

    public static class MyFragment extends Fragment implements ListenerOfClicks {

        private RecyclerView rv;
        private ViewPager vpBanner;
        private ImageView imgViewThreeDots;

        //bottom bar controls
        private LinearLayout llCats;

        private ImageView ivHome;
        private ImageView ivCats;
        private ImageView ivSearch;
        private ImageView ivCart;
        private ImageView ivAccount;

        private TextView tvHome;
        private TextView tvCats;
        private TextView tvSearch;
        private TextView tvCart;
        private TextView tvAccount;

        //paging things
        //http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
        private int previousTotal = 0;
        private boolean loading = true;
        private int visibleThreshold = 5;
        int firstVisibleItem, visibleItemCount, totalItemCount;


        //constructor
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
            setHasOptionsMenu(true);
            setUpBottomBarControls(layout);

//            if (bundle != null) {
//
//                // tvPosition.setText("The page selected is " + bundle.getInt("position"));
//            }
            imgViewThreeDots = (ImageView) layout.findViewById(R.id.imgViewThreeDots);
            //setup banner on top of each fragment
            vpBanner = (ViewPager) layout.findViewById(R.id.vpBanner);
            VpBannerAdapter vpBannerAdapter = new VpBannerAdapter(getContext());
            vpBanner.setAdapter(vpBannerAdapter);
            vpBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int page) {
                    ChangeThreeDotsImage(page);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            ChangeThreeDotsImage(vpBanner.getCurrentItem());

            //setup recyccler view
            rv = (RecyclerView) layout.findViewById(R.id.rv_recycler_view);
            rv.setHasFixedSize(true);
            adapter = new RVAdapter(getDataSet());
            adapter.setClickListener(this);
            rv.setAdapter(adapter);
            final GridLayoutManager llm = new GridLayoutManager(getContext(), 2);
//            StaggeredGridLayoutManager _sGridLayoutManager = new StaggeredGridLayoutManager(3,
//                    StaggeredGridLayoutManager.VERTICAL);

            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                        Crouton.makeText(getActivity(), "loading..", Style.INFO).show();
                    }
                }
            });
            rv.setLayoutManager(llm);
            return layout;
        }

        private void ChangeThreeDotsImage(int vpItem) {


            switch (vpItem) {
                case 0:
                    Drawable swipe_1 = getActivity().getResources().getDrawable(
                            R.drawable.pride_swipe_1_enabled);
                    imgViewThreeDots.setImageDrawable(swipe_1);
                    break;

                case 1:
                    Drawable swipe_2 = getActivity().getResources().getDrawable(
                            R.drawable.pride_swipe_2_enabled);
                    imgViewThreeDots.setImageDrawable(swipe_2);
                    break;

                case 2:
                    Drawable swipe_3 = getActivity().getResources().getDrawable(
                            R.drawable.pride_swipe_3_enabled);
                    imgViewThreeDots.setImageDrawable(swipe_3);
                    break;

            }

        }

        private void setUpBottomBarControls(View layout) {

            llCats = (LinearLayout) layout.findViewById(R.id.llCats);
            llCats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), CategorySelectionScreen.class));
                }
            });

            ivHome = (ImageView) layout.findViewById(R.id.ivHome);
            ivCats = (ImageView) layout.findViewById(R.id.ivCats);
            ivSearch = (ImageView) layout.findViewById(R.id.ivSearch);
            ivCart = (ImageView) layout.findViewById(R.id.ivCart);
            ivAccount = (ImageView) layout.findViewById(R.id.ivAccount);

            tvHome = (TextView) layout.findViewById(R.id.tvHome);
            tvCats = (TextView) layout.findViewById(R.id.tvCats);
            tvSearch = (TextView) layout.findViewById(R.id.tvSearch);
            tvCart = (TextView) layout.findViewById(R.id.tvCart);
            tvAccount = (TextView) layout.findViewById(R.id.tvAccount);


            ivHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ivCats.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), CategorySelectionScreen.class));
                }
            });

            ivSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ivCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            ivAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

            super.onCreateOptionsMenu(menu, inflater);
            inflater.inflate(R.menu.menu_main, menu);

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            final SearchView searchView =
                    (SearchView) menu.findItem(R.id.mSearch).getActionView();

            searchView.setQueryHint("Search Geardo");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {


                    return true;

                }

                @Override
                public boolean onQueryTextChange(String query) {


                    query = query.toLowerCase();

                    if (query.length() > 0) {
                        adapter.getFilter().filter(query.toString());
                        return true;
                    } else {
                        return false;
                    }

                }
            });
        }

    }


    public interface ListenerOfClicks {

        void itemClicked(View v, int position, Bundle batton);
    }

    private static class RVAdapter extends RecyclerView.Adapter<RVAdapter.ProductViewHolder> implements Filterable {


        private List<ProductInfoModel> productList;
        private CustomFilter mFilter;
        private MainActivity.ListenerOfClicks listenerOfClicks;
        private List<ProductInfoModel> dictionaryWords = new ArrayList<>();
        private List<ProductInfoModel> filteredList = new ArrayList<>();


        // Provide a suitable constructor (depends on the kind of dataset)
         RVAdapter(List<ProductInfoModel> myDataset) {
            this.productList = new ArrayList<>();
            this.productList = myDataset;
            // this.filteredList = myDataset;
            mFilter = new CustomFilter();

        }

        @Override
        public RVAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
            ProductViewHolder vh = new ProductViewHolder(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {

            /*
            *  public  String Title;
    public int thumnailUrl;
    public String reducedPrice;
    public String price;
    public String descriptionl;

    */
            holder.ivProductImage.setImageResource(productList.get(position).thumnailUrl);
            holder.tvProductTitle.setText(productList.get(position).Title);
            holder.tvProductReducedPrice.setText(productList.get(position).reducedPrice);
            holder.tvProductPrice.setText(productList.get(position).price);
            //holder.tvProductDescription.setText(productList.get(position).descriptionl);
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

         void setClickListener(MainActivity.ListenerOfClicks listenerOfClicks) {
            this.listenerOfClicks = listenerOfClicks;
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }


         class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

             CardView card_view;
             ImageView ivProductImage;
             TextView tvProductTitle;
             TextView tvProductReducedPrice;
             TextView tvProductPrice;
            // TextView tvProductDescription;


             public ProductViewHolder(View itemView) {
                 super(itemView);

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

                //deleteItem(getAdapterPosition());

                if (listenerOfClicks != null) {

                    listenerOfClicks.itemClicked(v, getAdapterPosition(), new Bundle());
                }

            }


        }

         class CustomFilter extends Filter {

            private CustomFilter() {
                super();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredList.clear();
                final FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    filteredList.addAll(dictionaryWords);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final ProductInfoModel mWords : productList) {
                        if (mWords.Title.toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(mWords);
                        }
                    }
                }
                System.out.println("Count Number " + filteredList.size());
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // System.out.println("Count Number 2 " + ((List<ProductInfoModel>) results.values).size());
               adapter.notifyDataSetChanged();
            }
        }

    /*
    *
    *
    *  public ProductInfoModel removeItem(int position) {
        final ProductInfoModel model = productList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, ProductInfoModel model) {
        productList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final ProductInfoModel model = productList.remove(fromPosition);
        productList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<ProductInfoModel> models) {
        //a method which will animate between the List of objects currently displayed in the Adapter to the filtered List we are going to supply to the method.
        //order is important!
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<ProductInfoModel> newModels) {
        for (int i = productList.size() - 1; i >= 0; i--) {
            final ProductInfoModel model = productList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ProductInfoModel> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final ProductInfoModel model = newModels.get(i);
            if (!productList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ProductInfoModel> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final ProductInfoModel model = newModels.get(toPosition);
            final int fromPosition = productList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
    *
    *
    * */

    }


}
