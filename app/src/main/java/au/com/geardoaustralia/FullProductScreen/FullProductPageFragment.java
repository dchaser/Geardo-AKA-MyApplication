package au.com.geardoaustralia.FullProductScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.cartNew.widget.AddCartDialogActivity;
import au.com.geardoaustralia.gallery.GalleryAdapter;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.gallery.SlideshowDialogFragment;
import au.com.geardoaustralia.utils.GlobalContext;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by DasunPC on 1/5/17.
 */

public class FullProductPageFragment extends Fragment  {

    //implements FullProductPage.ProductPageClickListener

    private String TAG = FullProductPageFragment.class.getSimpleName();
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

    Product selectedProduct;
    Bundle bundle;
    public static int selectedGalleryPosition = 0;

    FloatingActionButton icAddToWishListt;
    public static FloatingActionButton fabAddToCart;


    //charts
    LinearLayout llFiveStars;
    LinearLayout llFourStars;
    LinearLayout llThreeStars;
    LinearLayout llTwoStars;
    LinearLayout llOneStars;


    public static FullProductPageFragment getInstance(Bundle passer) {

        FullProductPageFragment fullProductPageFragment = new FullProductPageFragment();
        fullProductPageFragment.setArguments(passer);
        return fullProductPageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.product_page_fragment_layout, container, false);
        //tvPosition = (TextView) layout.findViewById(R.id.tvPosition);

        tvImgCount = (TextView) layout.findViewById(R.id.tvImgCount);
        icAddToWishListt = (FloatingActionButton) layout.findViewById(R.id.icAddToWishListt);


        llFiveStars = (LinearLayout) layout.findViewById(R.id.llFiveStars);
        llFourStars = (LinearLayout) layout.findViewById(R.id.llFourStars);
        llThreeStars = (LinearLayout) layout.findViewById(R.id.llThreeStars);
        llTwoStars = (LinearLayout) layout.findViewById(R.id.llTwoStars);
        llOneStars = (LinearLayout) layout.findViewById(R.id.llOneStars);

        fabAddToCart = (FloatingActionButton) getActivity().findViewById(R.id.fabAddToCart);

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

                    //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //invoke bottom sheet to display

                    Intent intent = new Intent(getActivity(), AddCartDialogActivity.class);
                    intent.putExtra(AddCartDialogActivity.SELECTED_PRODUCT_ITEM, selectedProduct);
                    startActivityForResult(intent, AddCartDialogActivity.REQUEST_TO_ADD_IN_CART);

                }
            }
        });

        bundle = getArguments();

        if (bundle != null) {
            int id = bundle.getInt(FullProductPage.SELECTED_PRODUCT_ID, -1);

            if (id != -1) {
                selectedProduct = DatabaseManager.getInstance().getProductById(id);
            }
        }


        recyclerView = (RecyclerView) layout.findViewById(R.id.gallery_recycler_view);
        tvImgCount = (TextView) layout.findViewById(R.id.tvImgCount);
        pTitle = (TextView) layout.findViewById(R.id.pTitle);
        pShortDesc = (TextView) layout.findViewById(R.id.pShortDesc);
        tvReducedPrice = (TextView) layout.findViewById(R.id.tvReducedPrice);
        pPrice = (TextView) layout.findViewById(R.id.pPrice);

        icAddToWishListt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Crouton.makeText(getActivity(), "Item Added to Wishlist", Style.INFO).show();
            }
        });


        if (selectedProduct != null) {

            pTitle.setText(selectedProduct.name);
            pShortDesc.setText(selectedProduct.description);
            tvReducedPrice.setText(selectedProduct.reducedPrice);
            pPrice.setText(selectedProduct.price);

            pDialog = new ProgressDialog(getActivity());
            images = new ArrayList<>();
            mAdapter = new GalleryAdapter(getActivity().getApplicationContext(), images);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(mAdapter);

            tvImgCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("images", images);


                    bundle.putInt("position", selectedGalleryPosition);

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                    newFragment.setArguments(bundle);
                    newFragment.show(ft, "slideshow");
                }
            });

            recyclerView.addOnItemTouchListener(new GalleryAdapter.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new GalleryAdapter.ClickListener() {

                @Override
                public void onClick(View view, int position) {

                    selectedGalleryPosition = position;
                }

                @Override
                public void onLongClick(View view, int position) {
                    selectedGalleryPosition = position;
                }
            }));

            fetchImages();

        } else {
            Toast.makeText(getActivity(), "Error...", Toast.LENGTH_SHORT).show();
        }

        int colerloop[] = {1, 2, 2, 2, 3, 3, 3, 3, 1, 1};
        int heightLoop[] = {300, 200, 200, 200, 100, 100, 100, 100, 300, 300};
        for (int j = 0; j < colerloop.length; j++) {
            drawChart(1, colerloop[j], heightLoop[j]);
        }


        return layout;
    }

    public void drawChart(int count, int color, int height) {
        System.out.println(count + color + height);
        if (color == 3) {
            color = Color.RED;
        } else if (color == 1) {
            color = Color.BLUE;
        } else if (color == 2) {
            color = Color.GREEN;
        }
        for (int k = 1; k <= count; k++) {
            View view = new View(getActivity());
            view.setBackgroundColor(color);
            view.setLayoutParams(new LinearLayout.LayoutParams(25, height));
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            params.setMargins(3, 0, 0, 0); // substitute parameters for left, // top, right, bottom
            view.setLayoutParams(params);
            llFiveStars.addView(view);
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                                image.name = (object.getString("name"));

                                JSONObject url = object.getJSONObject("url");
                                image.thumb = (url.getString("thumb"));

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

//    @Override
//    public void overViewClicked(View v, int position) {
//
//    }
//
//    @Override
//    public void relatedClickedViewClicked(View v, int position) {
//
//    }
//
//    @Override
//    public void ratingsClicked(View v, int position) {
//
//    }
//
}
