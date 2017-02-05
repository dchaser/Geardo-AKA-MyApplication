package au.com.geardoaustralia.categories;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.BaseActivity;
import au.com.geardoaustralia.cartNew.data.Category;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.data.Subcategory;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.utils.CommonConstants;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.MenuBarHandler;
import au.com.geardoaustralia.utils.utilKit;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class SortAndRefinerScreen extends BaseActivity implements View.OnClickListener {

    Toolbar toolbar;
    MenuBarHandler menuBarHandler;
    ImageButton ibGridAndLinear;
    TextView tvRefine;
    TextView tvSort;
    boolean isSort = false;
    boolean isRefine = false;

    private SortAndRefineDrawerFragment sortAndRefineDrawerFragment;

    RecyclerView subCatSorterRV;
    public static SortAndRefinerScreen.SubCatProductAdapter adapter;
    public static SortAndRefinerScreen.SubCatProductAdapterOne adapterOne;

    public static List<ProductInfoModel> filteredList = new ArrayList<>();
    public static List<ProductInfoModel> dictionaryWords = new ArrayList<>();
    LinearLayoutManager llm;
    //    GridLayoutManager glm;
    StaggeredGridLayoutManager sglm;
    boolean toggleLayoutManager = false;

    //paging fields
    //http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    String subcategory_name = null;
    Subcategory selectedSubcategory;

//    index % 4 == 3           --->  full span cell
//    index % 8 == 0, 5        --->  half span cell
//    index % 8 == 1, 2, 4, 6  ---> quarter span cell

    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 1;
    private static final int TYPE_THREE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_and_refiner_screen);

        sortAndRefineDrawerFragment = (SortAndRefineDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.sort_drawer_fragment);

        utilKit.hideSoftKeyboard(this);

        llm = new LinearLayoutManager(SortAndRefinerScreen.this, LinearLayoutManager.VERTICAL, false);
        sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        Intent i = getIntent();
        subcategory_name = i.getStringExtra("passing_subcategory_name");

        menuBarHandler = new MenuBarHandler(SortAndRefinerScreen.this);
        toolbar = (Toolbar) findViewById(R.id.appToolBar);
        //toolbar will automatically route to menu creation onOptionsMenu is available
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup Sort/Refiner buttons and Recycler View UI grid changer button
        ibGridAndLinear = (ImageButton) findViewById(R.id.ibGridAndLinear);

        tvSort = (TextView) findViewById(R.id.tvSort);
        tvSort.setOnClickListener(this);

        tvRefine = (TextView) findViewById(R.id.tvRefine);
        tvRefine.setOnClickListener(this);


        //setup recyclerview
        subCatSorterRV = (RecyclerView) findViewById(R.id.subCatSorterRV);
        subCatSorterRV.setHasFixedSize(true);

        try {
            if (subcategory_name != null) {

                this.selectedSubcategory = DatabaseManager.getInstance().getSubcategoryByName(subcategory_name);

                ArrayList<Product> subCatProducts = DatabaseManager.getInstance().getProductsBySubCategoryID(this.selectedSubcategory._id);

                adapter = new SubCatProductAdapter(subCatProducts, SortAndRefinerScreen.this);

                adapterOne = new SubCatProductAdapterOne(subCatProducts, SortAndRefinerScreen.this);

                adapter.setClickListener(new ListenerToProductClicks() {
                    @Override
                    public void productClicked(View v, int position, int productId) {

                        Intent toFullProductage = new Intent(SortAndRefinerScreen.this, FullProductPage.class);
                        toFullProductage.putExtra(FullProductPage.SELECTED_PRODUCT_ID, productId);
                        startActivity(toFullProductage);
                    }


                });

                adapterOne.setClickListener(new ListenerToProductClicks() {
                    @Override
                    public void productClicked(View v, int position, int productId) {


                        Intent toFullProductage = new Intent(SortAndRefinerScreen.this, FullProductPage.class);
                        toFullProductage.putExtra(FullProductPage.SELECTED_PRODUCT_ID, productId);
                        startActivity(toFullProductage);
                    }


                });
                subCatSorterRV.setAdapter(adapter);
                subCatSorterRV.setLayoutManager(sglm);
                adapterOne.notifyDataSetChanged();

                ibGridAndLinear.setOnClickListener(this);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ibGridAndLinear:

                //initially false
                if (toggleLayoutManager) {
                    toggleLayoutManager = false;

                    subCatSorterRV.setLayoutManager(sglm);
                    adapter.notifyDataSetChanged();
                    subCatSorterRV.setAdapter(adapter);

                    //set grid layout manager with glm
                    ibGridAndLinear.setBackgroundResource(R.drawable.ic_grid_grey);
                    //adapter.notifyDataSetChanged();
                } else {
                    //set linear layout manager with llm
                    subCatSorterRV.setLayoutManager(llm);
                    adapterOne.notifyDataSetChanged();
                    subCatSorterRV.setAdapter(adapterOne);
                    toggleLayoutManager = true;
                    ibGridAndLinear.setBackgroundResource(R.drawable.ic_store_grey);
                    //adapter.notifyDataSetChanged();
                }

                break;

            case R.id.tvSort:

                    isSort = true;
                    isRefine = false;

                    sortAndRefineDrawerFragment.setUpSortView(R.id.sort_drawer_fragment, (DrawerLayout) findViewById(R.id.drawer_layout), baseToolbar);

                    if (sortAndRefineDrawerFragment.mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                            sortAndRefineDrawerFragment.mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        sortAndRefineDrawerFragment.mDrawerLayout.openDrawer(Gravity.RIGHT);

                    }

                break;

            case R.id.tvRefine:

                    isRefine = true;
                    isSort = false;

                    sortAndRefineDrawerFragment.setUpRefineView(R.id.sort_drawer_fragment, (DrawerLayout) findViewById(R.id.drawer_layout), baseToolbar);

                    if (sortAndRefineDrawerFragment.mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                        sortAndRefineDrawerFragment.mDrawerLayout.closeDrawer(Gravity.RIGHT);
                    } else {
                        sortAndRefineDrawerFragment.mDrawerLayout.openDrawer(Gravity.RIGHT);

                    }

                break;

        }
    }


    public interface ListenerToProductClicks {

        void productClicked(View v, int position, int productId);
    }

    private static class SubCatProductAdapter extends RecyclerView.Adapter<SortAndRefinerScreen.SubCatProductAdapter.SubCetegoryViewHolder> {

        Context context;
        private ArrayList<Product> productList;
        private SortAndRefinerScreen.ListenerToProductClicks listenerToProductClicks;
        private static ImageLoader imageLoader;

        // Provide a suitable constructor (depends on the kind of dataset)
        SubCatProductAdapter(ArrayList<Product> myDataset, Context context) {
            this.productList = myDataset;
            this.context = context;
            imageLoader = new ImageLoader(context);
        }

        @Override
        public SubCetegoryViewHolder onCreateViewHolder(final ViewGroup parent,
                                                        final int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_layout, parent, false);

            itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    final int type = viewType;
                    final ViewGroup.LayoutParams lp = itemView.getLayoutParams();
                    if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                        StaggeredGridLayoutManager.LayoutParams sglp =
                                (StaggeredGridLayoutManager.LayoutParams) lp;
                        switch (type) {
                            case TYPE_ONE:
                                sglp.setFullSpan(false);
                                sglp.width = (itemView.getWidth()) + 5;
                                sglp.height = (itemView.getHeight()) + 5;
                                break;
                            case TYPE_TWO:
                                sglp.setFullSpan(false);
                                sglp.width = (itemView.getWidth()) + 5;
                                sglp.height = (itemView.getHeight()) + 45;
                                break;
                            case TYPE_THREE:
                                sglp.setFullSpan(false);
                                sglp.width = (itemView.getWidth()) + 5;
                                sglp.height = (itemView.getHeight()) + 75;
                                break;
                        }
                        itemView.setLayoutParams(sglp);
                        final StaggeredGridLayoutManager lm =
                                (StaggeredGridLayoutManager) ((RecyclerView) parent).getLayoutManager();
                        lm.invalidateSpanAssignments();
                    }
                    itemView.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });

            final SortAndRefinerScreen.SubCatProductAdapter.SubCetegoryViewHolder vh = new SortAndRefinerScreen.SubCatProductAdapter.SubCetegoryViewHolder(itemView);

            return vh;
        }

        @Override
        public void onBindViewHolder(SortAndRefinerScreen.SubCatProductAdapter.SubCetegoryViewHolder holder, int position) {

            Product product = productList.get(position);

            if (TextUtils.isEmpty(product.imageUrlOriginal)) {
                holder.ivProductImage.setImageResource(R.drawable.logo_geardo);
            } else {

                Picasso.with(context).load(CommonConstants.ROOT_PATH + product.imageUrlOriginal).into(holder.ivProductImage);
//                imageLoader.loadAssetsImage(this.context , Uri.parse(CommonConstants.ROOT_PATH + product.imageUrlOriginal), holder.ivProductImage);
            }

            holder.card_view.setTag(product.id);
            holder.tvPTitle.setText(product.name);
            holder.tvPPrice.setText(product.price);
            holder.tvFShipping.setText("free shiiping included");
            holder.tvMinOrder.setText("min order 1 item");

//            holder.ivFavorite.setImageResource(R.drawable.ic_favorite_border_orange_48dp);

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        void setClickListener(ListenerToProductClicks listenerToProductClicks) {
            this.listenerToProductClicks = listenerToProductClicks;
        }

        @Override
        public int getItemViewType(int position) {
            final int modeEight = position % 8;
            switch (modeEight) {
                case 0:
                case 5:
                    return TYPE_ONE;
                case 1:
                case 2:
                case 4:
                case 6:
                    return TYPE_TWO;
            }
            return TYPE_THREE;
        }

        class SubCetegoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //for Grid layout card view
            CardView card_view;
            ImageView ivProductImage;
            TextView tvPTitle;
            TextView tvPPrice;
            TextView tvFShipping;
            TextView tvMinOrder;
            ToggleButton tbFavorite;


            SubCetegoryViewHolder(View itemView) {
                super(itemView);

                card_view = (CardView) itemView.findViewById(R.id.card_view);
                card_view.setOnClickListener(this);
                ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
                tvPTitle = (TextView) itemView.findViewById(R.id.tvPTitle);
                tvPPrice = (TextView) itemView.findViewById(R.id.tvPPrice);
                tvFShipping = (TextView) itemView.findViewById(R.id.tvFShipping);
                tvMinOrder = (TextView) itemView.findViewById(R.id.tvMinOrder);
                tbFavorite = (ToggleButton) itemView.findViewById(R.id.tbFavorite);

            }

            @Override
            public void onClick(View v) {

                //deleteItem(getAdapterPosition());

                if (listenerToProductClicks != null) {

                    listenerToProductClicks.productClicked(v, getAdapterPosition(), Integer.parseInt(card_view.getTag().toString()));
                }

            }


        }


    }

    private static class SubCatProductAdapterOne extends RecyclerView.Adapter<SortAndRefinerScreen.SubCatProductAdapterOne.SubCetegoryViewHolder> {

        //for linear layout adapter
        Context context;
        private ArrayList<Product> productList;
        private SortAndRefinerScreen.ListenerToProductClicks listenerToProductClicks;
        private static ImageLoader imageLoaderOne;

        // Provide a suitable constructor (depends on the kind of dataset)
        SubCatProductAdapterOne(ArrayList<Product> myDataset, Context context) {
            this.context = context;
            this.productList = myDataset;
            imageLoaderOne = new ImageLoader(context);
        }

        @Override
        public SubCetegoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_layout_one, parent, false);
            final SortAndRefinerScreen.SubCatProductAdapterOne.SubCetegoryViewHolder vh = new SortAndRefinerScreen.SubCatProductAdapterOne.SubCetegoryViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(SortAndRefinerScreen.SubCatProductAdapterOne.SubCetegoryViewHolder holder, int position) {
            Product product = productList.get(position);


            if (TextUtils.isEmpty(product.imageUrlOriginal)) {
                holder.ivProductTumbnailImage.setImageResource(R.drawable.logo_geardo);
            } else {
                Picasso.with(context).load(CommonConstants.ROOT_PATH + product.imageUrlOriginal).into(holder.ivProductTumbnailImage);
                //int resID = resources.getIdentifier(product.imageUrlOriginal, "drawable",  context.getPackageName());
                //holder.ivProductTumbnailImage.setImageResource(resID);
                // imageLoaderOne.loadAssetsImage(this.context , Uri.parse(CommonConstants.ROOT_PATH + product.imageUrlOriginal), holder.ivProductTumbnailImage);
            }

//            Picasso.with(context).load(R.drawable.landing_screen).into(imageView1);
//            Picasso.with(context).load(new File(...)).into(imageView3);

            //setTag or card_view as the clicked product ID
            holder.card_view.setTag(product.id);


            holder.tvPTitle.setText(product.name);
            holder.tvFShipping.setText("free shipping");
            holder.tvOrders.setText("orders");
            holder.tvMinOrder.setText("4 min orders");
            holder.tvRating.setText("5.0 rating");
//            holder.tbFavorite.setImageResource(R.drawable.ic_favorite_border_orange_48dp);

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        void setClickListener(ListenerToProductClicks listenerToProductClicks) {
            this.listenerToProductClicks = listenerToProductClicks;
        }

        class SubCetegoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            CardView card_view;
            ImageView ivProductTumbnailImage;
            TextView tvPTitle;
            TextView tvFShipping;
            TextView tvOrders;
            TextView tvMinOrder;
            TextView tvRating;
            ToggleButton tbFavorite;


            SubCetegoryViewHolder(View itemView) {
                super(itemView);

                card_view = (CardView) itemView.findViewById(R.id.card_view);
                card_view.setOnClickListener(this);

                ivProductTumbnailImage = (ImageView) itemView.findViewById(R.id.ivProductTumbnailImage);
                tvPTitle = (TextView) itemView.findViewById(R.id.tvPTitle);
                tvFShipping = (TextView) itemView.findViewById(R.id.tvFShipping);
                tvOrders = (TextView) itemView.findViewById(R.id.tvOrders);
                tvMinOrder = (TextView) itemView.findViewById(R.id.tvMinOrder);
                tvRating = (TextView) itemView.findViewById(R.id.tvRating);
                tbFavorite = (ToggleButton) itemView.findViewById(R.id.tbFavorite);

            }

            @Override
            public void onClick(View v) {

                //deleteItem(getAdapterPosition());

                if (listenerToProductClicks != null) {

                    listenerToProductClicks.productClicked(v, getAdapterPosition(), Integer.parseInt(card_view.getTag().toString()));
                }

            }


        }


    }


}

