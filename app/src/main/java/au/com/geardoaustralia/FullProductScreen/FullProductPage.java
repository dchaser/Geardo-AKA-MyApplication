package au.com.geardoaustralia.FullProductScreen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DecimalFormat;
import java.util.ArrayList;

import au.com.geardoaustralia.FullProductScreen.adapters.FullPlaceImageAdapter;
import au.com.geardoaustralia.FullProductScreen.adapters.RelatedProductsAdapter;
import au.com.geardoaustralia.FullProductScreen.subscreens.ShippingOptinsScreen;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.BaseActivity;
import au.com.geardoaustralia.cartNew.data.BuyingOption;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.data.ProductStockOption;
import au.com.geardoaustralia.cartNew.data.ShippingMethod;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.cartNew.util.LogUtils;
import au.com.geardoaustralia.cartNew.widget.AddCartDialogActivity;
import au.com.geardoaustralia.gallery.GalleryAdapter;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.gallery.SlideshowDialogFragment;
import au.com.geardoaustralia.login.SignInActivity;
import au.com.geardoaustralia.utils.CommonConstants;
import au.com.geardoaustralia.utils.DotsScrollBar;
import au.com.geardoaustralia.utils.GlobalContext;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by DasunPC on 11/1/16.
 */

public class FullProductPage extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final String TAG = LogUtils.makeLogTag(FullProductPage.class);
    public static final String SELECTED_PRODUCT_ID = "au.com.geardoaustralia.FullProductScreen.SELECTED_PRODUCT_ID";
    public static final int REQUEST_TO_ADD_IN_CART = 1;
    public static final String SELECTED_PRODUCT = "";
    private FirebaseAuth auth;

    Bundle bundle;
    public int productId;
    public static Product selectedProduct;

    //scrollview
    @Bind(R.id.mainScrollView)
    NestedScrollView mainScrollView;
    @Bind(R.id.tvRelated)
    TextView tvRelated;
    @Bind(R.id.llBottom)
    LinearLayout llBottom;

    //top slider content
    ImageView imgViewThreeDots;
    private ArrayList<Image> images;
    @Bind(R.id.gallery_viewpager)
    ViewPager pager;
    @Bind(R.id.tvImgCount)
    TextView tvImgCount;
    @Bind(R.id.tbAddToFavorite)
    ToggleButton tbAddToFavorite;
    public static int selectedGalleryPosition = 0;
    private FullPlaceImageAdapter adapter;

    //usable views
    @Bind(R.id.tvProductName)
    TextView tvProductName;
    @Bind(R.id.tvPriceRange)
    TextView tvPriceRange;
    @Bind(R.id.tvMinOrder)
    TextView tvMinOrder;
    @Bind(R.id.tvNoOfOrders)
    TextView tvNoOfOrders;
    @Bind(R.id.rlClickToSelectOptions)
    RelativeLayout rlClickToSelectOptions;
    @Bind(R.id.rlClickToSelectShippingOptions)
    RelativeLayout rlClickToSelectShippingOptions;
    @Bind(R.id.tvShippingCost)
    TextView tvShippingCost;
    //add shiping ptions as text views into this
    @Bind(R.id.llShippingOptionPresenter)
    LinearLayout llShippingOptionPresenter;
    @Bind(R.id.btnBuyItNow)
    Button btnBuyItNow;
    @Bind(R.id.btnAddToCart)
    Button btnAddToCart;
    @Bind(R.id.tvShareItem)
    TextView tvShareItem;
    @Bind(R.id.rlClickToSelectItemSpecification)
    RelativeLayout rlClickToSelectItemSpecification;
    @Bind(R.id.rlClickToSelectItemDescription)
    RelativeLayout rlClickToSelectItemDescription;
    @Bind(R.id.rlClickToSeeReviews)
    RelativeLayout rlClickToSeeReviews;
    @Bind(R.id.tvSoldByCompositeBold)
    TextView tvSoldByCompositeBold;

    //Bottom two button to Add to cart and buy now
    @Bind(R.id.btnBuyNowScroll)
    Button btnBuyNowScroll;
    @Bind(R.id.btnAddToCartScroll)
    Button btnAddToCartScroll;


    //Related Product at bottom
    @Bind(R.id.rvRelatedItems)
    RecyclerView rvRelatedItems;
    private RelatedProductsAdapter mAdapter;
    ArrayList<Product> relatedProductsList;


    //shipping helper variables
    int counter = 0;
    boolean isFree = false;

    //galler dots
    LinearLayout llThreeDotsContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_product_screen);
        ButterKnife.bind(FullProductPage.this);
        productId = getIntent().getIntExtra(SELECTED_PRODUCT_ID, 0);
        auth = FirebaseAuth.getInstance();

        try {
            //get the product from DB, which matching the ID above.
            DatabaseManager manager = DatabaseManager.getInstance();
            selectedProduct = manager.getProductById(productId);
            //get buying options for product
            selectedProduct.buyingOptions = manager.getBuyingOptionsPerProduct(selectedProduct);
            //get stock options for the buying options
            for (int c = 0; c < selectedProduct.buyingOptions.size(); c++) {

                BuyingOption not_filled_stock_option = selectedProduct.buyingOptions.get(c);

                //now the option variable has its stock option variable filled
                selectedProduct.buyingOptions.get(c).productStockOption = manager.getStockOptionForASingleBuyingOption(not_filled_stock_option);

            }

            //now get price per each stock option
            for (int i = 0; i < selectedProduct.buyingOptions.size(); i++) {

                ProductStockOption option = selectedProduct.buyingOptions.get(i).productStockOption;
                option.price = manager.getPricePerStockOption(option);
                selectedProduct.buyingOptions.get(i).productStockOption = option;
            }
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

        //set slider on top
        initiateImageSlider();

        //set dot bar
        updateIndicator(0);

        //set data on views
        try {
            setDataOnViews();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        //set scroll listener
        mainScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                Rect mReact = new Rect();
                mainScrollView.getHitRect(mReact);
                if (rvRelatedItems.getLocalVisibleRect(mReact)) {
                    // visible
                    llBottom.setVisibility(View.VISIBLE);
                } else {
                    // invisible
                    llBottom.setVisibility(View.GONE);
                }
            }
        });

        //related products setup
        relatedProductsList = new ArrayList<>();
        //Get related products objects set
        if (selectedProduct.relatedProductIDs.size() >= 0) {

            for (int i = 0; i < selectedProduct.relatedProductIDs.size(); i++) {

                int productId = Integer.parseInt(selectedProduct.relatedProductIDs.get(i));
                Product product = DatabaseManager.getInstance().getProductById(productId);
                relatedProductsList.add(product);

            }

        }

        mAdapter = new RelatedProductsAdapter(FullProductPage.this.getApplicationContext(), relatedProductsList);
        final GridLayoutManager llm = new GridLayoutManager(FullProductPage.this, 2);
        rvRelatedItems.setLayoutManager(llm);
        rvRelatedItems.setAdapter(mAdapter);

        //


    }

    private void setDataOnViews() throws IllegalAccessException {

        tvProductName.setText(selectedProduct.name);

        //get price range from stock options
        if (selectedProduct.price == null || TextUtils.isEmpty(selectedProduct.price)) {

        } else {
            //set here
            String boldBit = "AU $ " + "<b>" + selectedProduct.price + "</b>";
            tvPriceRange.setText(Html.fromHtml(boldBit));
        }


        //min order
        if (selectedProduct.min_order != null) {
            tvMinOrder.setText("Min Order: " + selectedProduct.min_order + " Pieces");
//            tvNoOfOrders.setText(selectedProduct.n);
        } else {
            tvMinOrder.setVisibility(View.GONE);
            tvNoOfOrders.setVisibility(View.GONE);

            //add some bottom margin to above text view
            RelativeLayout.LayoutParams llp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(00, 0, 0, 30); // llp.setMargins(left, top, right, bottom);
            tvPriceRange.setLayoutParams(llp);

        }

        rlClickToSelectOptions.setOnClickListener(this);
        rlClickToSelectShippingOptions.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
        btnBuyItNow.setOnClickListener(this);
        btnAddToCartScroll.setOnClickListener(this);
        btnBuyNowScroll.setOnClickListener(this);



        //make related items recycler view on bottom

    }



    private void displayShipping() {

        if (GlobalContext.getInstance().selectedCountry == null) {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String countryCodeValue = tm.getNetworkCountryIso();
            //get country name by ISO Code
            String name = DatabaseManager.getInstance().getCountryNameByISOCode(countryCodeValue.toUpperCase());
            ArrayList<ShippingMethod> shippingOptions = DatabaseManager.getInstance().getShippingMethodsPerCountryName(name);

            Double avgShippingCost = 0.0;
            //one country ahs few shipping options
            //if its free shipping then display it else dislay the average price
            for (int a = 0; a < shippingOptions.size(); a++) {

                String cost = shippingOptions.get(a).shipping_cost;

                if (cost.equals("Free Shipping")) {

                    isFree = true;

                } else {
                    counter++;
                    avgShippingCost = avgShippingCost + Double.parseDouble(shippingOptions.get(a).shipping_cost);
                    //divide by number of option s to get average
                    avgShippingCost = avgShippingCost / counter;
                }

            }

            if (isFree) {
                tvShippingCost.setText("Free Shipping to " + name);

                //add text view
//                llShippingOptionPresenter
            } else {

                DecimalFormat df = new DecimalFormat("#.##");
                String twoDigitNum = df.format(avgShippingCost);
                tvShippingCost.setText(twoDigitNum);
            }
        } else {


            ArrayList<ShippingMethod> shippingOptions = DatabaseManager.getInstance().getShippingMethodsPerCountryName(GlobalContext.getInstance().selectedCountry);

            Double avgShippingCost = 0.0;
            //one country ahs few shipping options
            //if its free shipping then display it else dislay the average price
            for (int a = 0; a < shippingOptions.size(); a++) {

                String cost = shippingOptions.get(a).shipping_cost;

                if (cost.equals("Free Shipping")) {

                    isFree = true;

                } else {
                    counter++;
                    avgShippingCost = avgShippingCost + Double.parseDouble(shippingOptions.get(a).shipping_cost);
                    //divide by number of option s to get average
                    avgShippingCost = avgShippingCost / counter;
                }

            }

            if (isFree) {
                tvShippingCost.setText("Free Shipping to " + GlobalContext.getInstance().selectedCountry);
            } else {

                DecimalFormat df = new DecimalFormat("#.##");
                String twoDigitNum = df.format(avgShippingCost);
                tvShippingCost.setText(twoDigitNum);
            }
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        //shipping cost display
        displayShipping();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddCartDialogActivity.REQUEST_TO_ADD_IN_CART) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                selectedProduct.isInCart = data.getBooleanExtra(AddCartDialogActivity.CART_STATUS, false);

            }

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        updateIndicator(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    // class methods
    public void updateIndicator(int currentPage) {

        llThreeDotsContainer = (LinearLayout) findViewById(R.id.llThreeDotsContainer);
        llThreeDotsContainer.removeAllViews();

        DotsScrollBar.createDotScrollBar(this, llThreeDotsContainer, currentPage, selectedProduct.thumb_count);
    }

    private void initiateImageSlider() {

        //now get the images
        fetchImages();


        tbAddToFavorite.setOnClickListener(this);
        //set state
        if (selectedProduct.isFavorite != null && selectedProduct.isFavorite.equals("1")) {
            tbAddToFavorite.setChecked(true);
        } else {
            tbAddToFavorite.setChecked(false);
        }

        this.adapter = new FullPlaceImageAdapter(
                this, selectedProduct, this.images);
        this.pager.setAdapter(this.adapter);
        this.pager.setOnPageChangeListener(FullProductPage.this);

        tvImgCount.setText(String.valueOf(selectedProduct.thumb_count));
        tvImgCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("images", images);
                bundle.putInt("position", selectedGalleryPosition);

                FragmentTransaction ft = FullProductPage.this.getSupportFragmentManager().beginTransaction();
                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }
        });


    }

    //get image set
    private void fetchImages() {
        images = new ArrayList<>();
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("please wait...");
        pDialog.show();
        int size = selectedProduct.thumb_count;

        this.images = fillImageArrayUsingThumbnailCount(size);
        pDialog.hide();

    }


    private ArrayList<Image> fillImageArrayUsingThumbnailCount(int sliderSize) {

        ArrayList<Image> file_maps = new ArrayList<>();

        switch (sliderSize) {

            case 1:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                break;
            case 2:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                break;
            case 3:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                break;
            case 4:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                break;
            case 5:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                file_maps.add(new Image("Caption 5", selectedProduct.product_thumb_five));
                break;
            case 6:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                file_maps.add(new Image("Caption 5", selectedProduct.product_thumb_five));
                file_maps.add(new Image("Caption 6", selectedProduct.product_thumb_six));
                break;
            case 7:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                file_maps.add(new Image("Caption 5", selectedProduct.product_thumb_five));
                file_maps.add(new Image("Caption 6", selectedProduct.product_thumb_six));
                file_maps.add(new Image("Caption 7", selectedProduct.product_thumb_seven));
                break;
            case 8:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                file_maps.add(new Image("Caption 5", selectedProduct.product_thumb_five));
                file_maps.add(new Image("Caption 6", selectedProduct.product_thumb_six));
                file_maps.add(new Image("Caption 7", selectedProduct.product_thumb_seven));
                file_maps.add(new Image("Caption 8", selectedProduct.product_thumb_eight));
                break;
            case 9:
                break;
            case 10:
                break;


        }

        return file_maps;

    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.tbAddToFavorite) {
            if (selectedProduct.isFavorite != null && selectedProduct.isFavorite.equals("1")) {
                //remove from favorites
                DatabaseManager.getInstance().removeProductFromFavorite(productId);
                Crouton.makeText(FullProductPage.this, "Removed from Favorites", Style.ALERT).show();
            } else {
                DatabaseManager.getInstance().saveProductAsFavorite(productId);
                Crouton.makeText(FullProductPage.this, "Added to Favorites", Style.INFO).show();
            }
        }

        if (view.getId() == R.id.rlClickToSelectOptions) {

        }

        if (view.getId() == R.id.rlClickToSelectShippingOptions) {

            Intent intent = new Intent(FullProductPage.this, ShippingOptinsScreen.class);
            intent.putExtra("selectedProduct", selectedProduct);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.stay);

        }

        if (view.getId() == R.id.btnAddToCart) {

            FirebaseUser firebaseUser = auth.getCurrentUser();

            if (firebaseUser != null) {


                Intent intent = new Intent(FullProductPage.this, AddCartDialogActivity.class);
                intent.putExtra(AddCartDialogActivity.SELECTED_PRODUCT_ITEM, selectedProduct);
                startActivityForResult(intent, AddCartDialogActivity.REQUEST_TO_ADD_IN_CART);
            }else{
                Intent intent = new Intent(FullProductPage.this, SignInActivity.class);
                startActivity(intent);
            }

        }
        if (view.getId() == R.id.btnBuyItNow) {

        }
        if (view.getId() == R.id.btnAddToCartScroll) {

            FirebaseUser firebaseUser = auth.getCurrentUser();

            if (firebaseUser != null) {


                Intent intent = new Intent(FullProductPage.this, AddCartDialogActivity.class);
                intent.putExtra(AddCartDialogActivity.SELECTED_PRODUCT_ITEM, selectedProduct);
                startActivityForResult(intent, AddCartDialogActivity.REQUEST_TO_ADD_IN_CART);
            }else{
                Intent intent = new Intent(FullProductPage.this, SignInActivity.class);
                startActivity(intent);
            }


        }
        if (view.getId() == R.id.btnBuyNowScroll) {

        }
    }
}


