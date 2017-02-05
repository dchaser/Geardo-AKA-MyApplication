package au.com.geardoaustralia.FullProductScreen;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import au.com.geardoaustralia.FullProductScreen.adapters.RatingsAdapter;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.CustomerReview;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.utils.CommonConstants;


public class ShippingFragment extends Fragment {

    private String TAG = RatingsFragment.class.getSimpleName();
    private ArrayList<CustomerReview> ratingsList;
    private ProgressDialog pDialog;
    private RatingsAdapter mAdapter;
    private RecyclerView recyclerView;

    Product selectedProduct;
    Bundle bundle;
    public static int selectedGalleryPosition = 0;

    public ShippingFragment() {
    }

    public static ShippingFragment getInstance(Bundle passer) {

        ShippingFragment shippingFragment = new ShippingFragment();
        shippingFragment.setArguments(passer);
        return shippingFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.product_shipping_fragment_layout, container, false);
        bundle = getArguments();


        if (bundle != null) {
            int id = bundle.getInt(FullProductPage.SELECTED_PRODUCT_ID, -1);

            if (id != -1) {
                selectedProduct = DatabaseManager.getInstance().getProductById(id);
            }
        }


        if (selectedProduct != null) {

            //hide the floating add to cart button on bottom toolbar
            ImageLoader mImageLoader = new ImageLoader(getActivity());
        }

        return layout;
    }


}
