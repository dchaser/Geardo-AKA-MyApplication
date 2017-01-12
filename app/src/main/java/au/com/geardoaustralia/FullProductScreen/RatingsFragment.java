package au.com.geardoaustralia.FullProductScreen;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import au.com.geardoaustralia.FullProductScreen.adapters.RatingsAdapter;
import au.com.geardoaustralia.FullProductScreen.adapters.RelatedProductsAdapter;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.CustomerReview;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.data.User;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.gallery.GalleryAdapter;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.utils.CommonConstants;

/**
 * Created by DasunPC on 1/9/17.
 */

public class RatingsFragment extends Fragment implements FullProductPage.ProductPageClickListener {


    private String TAG = RatingsFragment.class.getSimpleName();
    private ArrayList<CustomerReview> ratingsList;
    private ProgressDialog pDialog;
    private RatingsAdapter mAdapter;
    private RecyclerView recyclerView;

    Product selectedProduct;
    Bundle bundle;
    public static int selectedGalleryPosition = 0;

    FloatingActionButton icAddToWishListt;

    //charts
    LinearLayout llFiveStars;
    LinearLayout llFourStars;
    LinearLayout llThreeStars;
    LinearLayout llTwoStars;
    LinearLayout llOneStars;

    ImageView ivImageThumb;
    TextView productTitle;
    TextView productBought;
    TextView productSavedBy;

    public RatingsFragment() {
    }

    public static RatingsFragment getInstance(Bundle passer) {

        RatingsFragment ratingsFragment = new RatingsFragment();
        ratingsFragment.setArguments(passer);
        return ratingsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.product_ratings_fragment_layout, container, false);
        bundle = getArguments();

        recyclerView = (RecyclerView) layout.findViewById(R.id.ratings_recycler_view);

        if (bundle != null) {
            int id = bundle.getInt(FullProductPage.SELECTED_PRODUCT_ID, -1);

            if (id != -1) {
                selectedProduct = DatabaseManager.getInstance().getProductById(id);
            }
        }


        if (selectedProduct != null) {

//            hide the floating add to cart button on bottom toolbar
            FullProductPage.fabAddToCart.setVisibility(View.GONE);

            ImageLoader mImageLoader = new ImageLoader(getActivity());
            //setup frgamnet controls
            ivImageThumb = (ImageView) layout.findViewById(R.id.ivImageThumb);
            productTitle = (TextView) layout.findViewById(R.id.productTitle);
            productBought = (TextView) layout.findViewById(R.id.productBought);
            productSavedBy = (TextView) layout.findViewById(R.id.productSavedBy);

            if (TextUtils.isEmpty(selectedProduct.imageUrlOriginal)) {
                ivImageThumb.setImageResource(R.drawable.logo_geardo);
            } else {
                mImageLoader.loadAssetsImage(getActivity(), Uri.parse(CommonConstants.ROOT_PATH + selectedProduct.imageUrlOriginal), ivImageThumb);
            }

            productTitle.setText(selectedProduct.name);
            productSavedBy.setText("2000 people");
            productSavedBy.setText("3000 people");

            ratingsList = DatabaseManager.getInstance().getAllReviewsForProductId(selectedProduct.id);

        }


        mAdapter = new RatingsAdapter(getActivity().getApplicationContext(), ratingsList);

        // final GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

//        tvImgCount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("images", images);
//
//
//                bundle.putInt("position", selectedGalleryPosition);
//
//                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
//                newFragment.setArguments(bundle);
//                newFragment.show(ft, "slideshow");
//            }
//        });
//
//        recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {
//
//            @Override
//            public void onClick(View view, int position) {
//
//                selectedGalleryPosition = position;
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                selectedGalleryPosition = position;
//            }
//        }));
//
//        fetchImages();
//
//        }else{
//            Toast.makeText(getActivity(),"Error...", Toast.LENGTH_SHORT).show();
//        }


        return layout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void overViewClicked(View v, int position) {

    }

    @Override
    public void relatedClickedViewClicked(View v, int position) {

    }

    @Override
    public void ratingsClicked(View v, int position) {

    }
}
