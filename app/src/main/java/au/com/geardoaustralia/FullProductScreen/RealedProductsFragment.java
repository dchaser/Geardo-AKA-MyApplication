package au.com.geardoaustralia.FullProductScreen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import au.com.geardoaustralia.FullProductScreen.adapters.RelatedProductsAdapter;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.gallery.GalleryAdapter;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.gallery.SlideshowDialogFragment;
import au.com.geardoaustralia.utils.GlobalContext;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by DasunPC on 1/9/17.
 */

public class RealedProductsFragment extends Fragment implements FullProductPage.ProductPageClickListener {

    private String TAG = RealedProductsFragment.class.getSimpleName();

    private RelatedProductsAdapter mAdapter;
    private RecyclerView recyclerView;

    Product selectedProduct;
    Bundle bundle;
    public static int selectedPosition = 0;

    ArrayList<Product> relatedProductsList;


    public static RealedProductsFragment getInstance(Bundle passer) {

        RealedProductsFragment realedProductsFragment = new RealedProductsFragment();
        realedProductsFragment.setArguments(passer);
        return realedProductsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.related_products_fragment_layout, container, false);
        relatedProductsList = new ArrayList<>();
        bundle = getArguments();

        recyclerView = (RecyclerView) layout.findViewById(R.id.related_products_recyclerview);

        if (bundle != null) {
            int id = bundle.getInt(FullProductPage.SELECTED_PRODUCT_ID, -1);

            if (id != -1) {
                selectedProduct = DatabaseManager.getInstance().getProductById(id);
            }
        }


        if (selectedProduct != null) {

            //hide the floating add to cart button on bottom toolbar
            FullProductPage.fabAddToCart.setVisibility(View.GONE);


            //Get related products objects set
            if (selectedProduct.relatedProductIDs.size() >= 0) {

                for (int i = 0; i < selectedProduct.relatedProductIDs.size(); i++) {

                    int productId = Integer.parseInt(selectedProduct.relatedProductIDs.get(i));
                    Product product = DatabaseManager.getInstance().getProductById(productId);
                    relatedProductsList.add(product);

                }

            }


        }


        mAdapter = new RelatedProductsAdapter(getActivity().getApplicationContext(), relatedProductsList);

        final GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(llm);
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
