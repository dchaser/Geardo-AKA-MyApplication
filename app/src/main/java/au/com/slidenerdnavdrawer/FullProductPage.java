package au.com.slidenerdnavdrawer;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import au.com.slidenerdnavdrawer.gallery.GalleryAdapter;
import au.com.slidenerdnavdrawer.gallery.Image;
import au.com.slidenerdnavdrawer.gallery.SlideshowDialogFragment;
import au.com.slidenerdnavdrawer.staggeredgridlayout.VerticalStaggeredGridFragment;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by DasunPC on 11/1/16.
 */

public class FullProductPage extends AppCompatActivity implements MaterialTabListener {

    private Toolbar toolbar;
    private MaterialTabHost mTabHost;
    private ViewPager mPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_product_screen);

        toolbar = (Toolbar) findViewById(R.id.appToolBarSubActivity);
        //toolbar will automatically route to menu creation onOptionsMenu is available
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                switch (position){

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {

        //when new tab selected change the viewpager
        mPager.setCurrentItem(tab.getPosition());

        switch (tab.getPosition()){

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
            //return FullProductPage.FullProductPageFragment.getInstance(position);
            return VerticalStaggeredGridFragment.newInstance(position);
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


        public static FullProductPage.FullProductPageFragment getInstance(int posiiton) {

            FullProductPage.FullProductPageFragment fullProductPageFragment = new FullProductPage.FullProductPageFragment();
            Bundle args = new Bundle();
            args.putInt("position", posiiton);
            fullProductPageFragment.setArguments(args);
            return fullProductPageFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.product_page_fragment_layout, container, false);
            //tvPosition = (TextView) layout.findViewById(R.id.tvPosition);
            Bundle bundle = getArguments();

            if (bundle != null) {

            }

            recyclerView = (RecyclerView) layout.findViewById(R.id.gallery_recycler_view);

            pDialog = new ProgressDialog(getActivity());
            images = new ArrayList<>();
            mAdapter = new GalleryAdapter(getActivity().getApplicationContext(), images);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
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


