package au.com.geardoaustralia;

import android.app.Activity;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.cartNew.CommonTabFragment;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.cartNew.widget.CollectionRecyclerView;
import au.com.geardoaustralia.utils.CommonConstants;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGE;

/**
 * Created by DasunPC on 12/26/16.
 */

public class MyFragment extends CommonTabFragment implements LoaderManager.LoaderCallbacks<List<Product>>, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


    private static final int TAG_METADATA_TOKEN = 0x8;
    private SliderLayout mDemoSlider;
    int fragMentTag;
    // create boolean for fetching data
    private boolean isViewShown = false;

    private CollectionRecyclerView mRecyclerView;
    public MyFragment.GenericAdapter adapter;

    public MyFragment(){

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (getActivity() instanceof MaterialTabListener) {
//            ((TabListener) getActivity()).onTabFragmentViewCreated(this);
//        }

        if (getActivity() instanceof TabListener) {
            ((TabListener) getActivity()).onTabFragmentViewCreated(this);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getActivity() instanceof TabListener) {
            ((TabListener) getActivity()).onTabFragmentAttached(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() instanceof TabListener) {
            ((TabListener) getActivity()).onTabFragmentDetached(this);
        }
    }



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        fragMentTag = args.getInt("tag");


        //int randInt = new Random().nextInt(2) + 1;

        getLoaderManager().initLoader(fragMentTag, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.myfragment_layout, container, false);

        mRecyclerView = (CollectionRecyclerView) layout.findViewById(R.id.list);

        ButterKnife.bind(this, layout);
        getActivity().overridePendingTransition(0, 0);
        Bundle bundle = getArguments();
        setHasOptionsMenu(true);

        mDemoSlider = (SliderLayout) layout.findViewById(R.id.slider);
        initiateImageSlider();

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final GridLayoutManager llm = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        adapter = new MyFragment.GenericAdapter<>(getActivity());
        mRecyclerView.setAdapter(adapter);
        adapter.setClickListener(mItemClickListener);
    }

    @Override
    public void onStop() {
        //do this firstly before the fragment is destroyed i order to prevent a memory leak
        mDemoSlider.stopAutoCycle();
        super.onStop();

    }

    private MyFragment.ListenerOfClicks mItemClickListener = new MyFragment.ListenerOfClicks() {

        @Override
        public void itemClicked(View v, int position, Bundle batton) {
            int productId = adapter.getProductId(position);

            Intent intent = new Intent(getActivity(), FullProductPage.class);
            intent.putExtra(FullProductPage.SELECTED_PRODUCT_ID, productId);
            startActivity(intent);
        }

        @Override
        public void makeFavorite(View v, int position, Bundle batton) {

            int productId = adapter.getProductId(position);
            Product product = DatabaseManager.getInstance().getProductById(productId);

            if(product.isFavorite != null && product.isFavorite.equals("1")){
                //remove from favorites
                DatabaseManager.getInstance().removeProductFromFavorite(productId);
                Crouton.makeText(getActivity(),"Removed from Favorites", Style.ALERT).show();
            }else{
                DatabaseManager.getInstance().saveProductAsFavorite(productId);
                Crouton.makeText(getActivity(),"Added to Favorites", Style.INFO).show();
            }


        }
    };

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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            isViewShown = true;

        } else {
            isViewShown = false;
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }



    @Override
    public android.support.v4.content.Loader<List<Product>> onCreateLoader(int id, Bundle args) {
        return new ProductListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Product>> loader, List<Product> data) {


        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Product>> loader) {
        adapter.setData(null);

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


    public static class ProductListLoader extends android.support.v4.content.AsyncTaskLoader<List<Product>> {

        private static final String TAG = ProductListLoader.class.getSimpleName();

        List<Product> mApps;


        public ProductListLoader(Context context) {
            super(context);
        }

        /**
         * This is where the bulk of our work is done. This function is called in a background thread
         * and should generate a new set of data to be published by the loader.
         */
        @Override
        public ArrayList<Product> loadInBackground() {

            ArrayList<Product> result = DatabaseManager.getInstance().getAllProdcuts();

            try {
                return result;

            } catch (Exception e) {
                LOGE(TAG, "Type of exception ", e);
            }

            return result;
        }

        /**
         * Called when there is new data to deliver to the client. The super class will take care of
         * delivering it; the implementation here just adds a little more logic.
         */
        @Override
        public void deliverResult(List<Product> apps) {
            if (isReset()) {
                // An async query came in while the loader is stopped. We
                // don't need the result.
                if (apps != null) {
                    onReleaseResources(apps);
                }
            }
            List<Product> oldApps = apps;
            mApps = apps;

            if (isStarted()) {
                // If the Loader is currently started, we can immediately
                // deliver its results.
                super.deliverResult(apps);
            }

            // At this point we can release the resources associated with
            // 'oldApps' if needed; now that the new result is delivered we
            // know that it is no longer in use.
            if (oldApps != null) {
                onReleaseResources(oldApps);
            }
        }

        /**
         * Handles a request to start the Loader.
         */
        @Override
        protected void onStartLoading() {
            if (mApps != null) {
                // If we currently have a result available, deliver it
                // immediately.
                deliverResult(mApps);
            }

            if (takeContentChanged() || mApps == null) {
                // If the data has changed since the last time it was loaded
                // or is not currently available, start a load.
                forceLoad();
            }
        }

        /**
         * Handles a request to stop the Loader.
         */
        @Override
        protected void onStopLoading() {
            // Attempt to cancel the current load task if possible.
            cancelLoad();
        }

        /**
         * Handles a request to cancel a load.
         */
        @Override
        public void onCanceled(List<Product> apps) {
            super.onCanceled(apps);

            // At this point we can release the resources associated with 'apps'
            // if needed.
            onReleaseResources(apps);
        }

        /**
         * Handles a request to completely reset the Loader.
         */
        @Override
        protected void onReset() {
            super.onReset();

            // Ensure the loader is stopped
            onStopLoading();

            // At this point we can release the resources associated with 'apps'
            // if needed.
            if (mApps != null) {
                onReleaseResources(mApps);
                mApps = null;
            }
        }

        /**
         * Helper function to take care of releasing resources associated with an actively loaded data
         * set.
         */
        protected void onReleaseResources(List<Product> apps) {
            // For a simple List<> there is nothing to do. For something
            // like a Cursor, we would close it here.
        }
    }

    static class GenericAdapter<T> extends RecyclerView.Adapter<GenericAdapter.ProductViewHolder> {

        private final ImageLoader mImageLoader;
        Context mContext;
        public ListenerOfClicks mItemClickListener;
        List<Product> formControlMaterList = new ArrayList<Product>();

        // Provide a suitable constructor (depends on the kind of dataset)
        public GenericAdapter(Context context) {
            this.mContext = context;
            mImageLoader = new ImageLoader(mContext, R.drawable.logo_geardo);
        }

        public void setData(List<T> data) {
            if (data != null) {
                formControlMaterList = (List<Product>) data;
                this.notifyDataSetChanged();
            }
        }

        @Override
        public GenericAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
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
        public void onBindViewHolder(GenericAdapter.ProductViewHolder holder, int position) {

                final Product productItem = formControlMaterList.get(position);

                holder.cbIsInCart.setChecked(productItem.isInCart);

                if (TextUtils.isEmpty(productItem.imageUrlOriginal)) {
                    holder.ivProductImage.setImageResource(R.drawable.logo_geardo);
                } else {
                    Picasso.with(mContext).load(CommonConstants.ROOT_PATH+productItem.imageUrlOriginal).into(holder.ivProductImage);
                    //mImageLoader.loadAssetsImage(mContext, Uri.parse(CommonConstants.ROOT_PATH + productItem.imageUrlOriginal), holder.ivProductImage);
                }

                holder.tvProductTitle.setText(productItem.name);
                holder.tvProductReducedPrice.setText(productItem.price);
                holder.tvProductPrice.setText(productItem.price);
                //holder.tvProductDescription.setText(productList.get(position).descriptionl);
                // Set the view to fade in
                //setFadeAnimation(holder.card_view);

            if(productItem.isFavorite != null && productItem.isFavorite.equals("1")){
                holder.ibFavorite.setChecked(true);
            }else{
                holder.ibFavorite.setChecked(false);
            }
        }

        @Override
        public int getItemCount() {
            return formControlMaterList.size();
        }

        public int getProductId(int position) {
            Product product = formControlMaterList.get(position);
            return product.id;
        }

        void setClickListener(ListenerOfClicks listenerOfClicks) {
            this.mItemClickListener = listenerOfClicks;
        }


        public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            CheckBox cbIsInCart;

            ToggleButton ibFavorite;

            CardView card_view;

            ImageView ivProductImage;

            TextView tvProductTitle;

            TextView tvProductReducedPrice;

            TextView tvProductPrice;


            public ProductViewHolder(View itemView) {
                super(itemView);

                cbIsInCart = (CheckBox) itemView.findViewById(R.id.cbIsInCart);
                ibFavorite = (ToggleButton) itemView.findViewById(R.id.ibFavorite);
                card_view = (CardView) itemView.findViewById(R.id.card_view);
                ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
                tvProductTitle = (TextView) itemView.findViewById(R.id.tvProductTitle);
                tvProductReducedPrice = (TextView) itemView.findViewById(R.id.tvProductReducedPrice);
                tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
                card_view.setOnClickListener(this);
                ibFavorite.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.card_view) {
                    if (mItemClickListener != null) {

                        mItemClickListener.itemClicked(v, getAdapterPosition(), new Bundle());
                    }

                }


                if (v.getId() == R.id.ibFavorite) {
                    if (mItemClickListener != null) {

                        mItemClickListener.makeFavorite(v, getAdapterPosition(), new Bundle());
                    }

                }


            }


        }

    }


    public interface ListenerOfClicks {

        void itemClicked(View v, int position, Bundle batton);

        void makeFavorite(View v, int position, Bundle batton);
    }


}


