package au.com.geardoaustralia.categories;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.R;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by DasunPC on 11/22/16.
 */

public class CategorySelectionScreen extends AppCompatActivity {


    private Toolbar toolbar;
    private ImageButton ivHamburger;
    private EditText etSearchMe;
    private ImageButton ivCart;
    private NDFragment ndFragment;
    private RecyclerView categoryRV;
    public static CategorySelectionScreen.RVCategoryAdapter adapter;
    public static List<categoryModel> catData = new ArrayList<>();

    //selected item in recyclerview
    //public static int selected_item = 0;

    //paging things
    //http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = (Toolbar) findViewById(R.id.catToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ivHamburger = (ImageButton) toolbar.findViewById(R.id.ivHamburger);
        ivHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this code should be moved into recycler view onItemClick to load the side fragment with correct adapter category list
                if (ndFragment.isDrawerOpened()) {

                    ndFragment.closeDrawerExplicitly();
                } else {
                    ndFragment.openDrawerExplicitly();
                }
            }
        });

        etSearchMe = (EditText) toolbar.findViewById(R.id.etSearchMe);

        ivCart = (ImageButton) toolbar.findViewById(R.id.ivCart);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Crouton.makeText(CategorySelectionScreen.this, "Cart clicks", Style.ALERT).show();
            }
        });


        ndFragment = (NDFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_id);
        //setup method shoud be provided with view that must be passed from activity to fragment
        ndFragment.setUp(R.id.fragment_navigation_drawer_id, (DrawerLayout) findViewById(R.id.activity_drawer_layout), toolbar);

        //setup recyclerview
        categoryRV = (RecyclerView) findViewById(R.id.categoryRV);
        categoryRV.setHasFixedSize(true);
        adapter = new RVCategoryAdapter(getDataSet(), CategorySelectionScreen.this);
        adapter.setClickListener(new ListenerToCategoryClicks() {
            @Override
            public void categoryClicked(View v, int position, Bundle batton) {

               // selected_item = position;
               // categoryRV.getChildAt(position).setSelected(true);
               // v.setBackgroundColor(getResources().getColor(R.color.lightorange));

                NDFragment.changeDatasetAndOpenDrawer(position);

                Toast.makeText(CategorySelectionScreen.this, "Clicked "+position, Toast.LENGTH_SHORT).show();
            }
        });
        categoryRV.setAdapter(adapter);
        final LinearLayoutManager llm = new LinearLayoutManager(CategorySelectionScreen.this, LinearLayoutManager.VERTICAL, false);

        categoryRV.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
                    Crouton.makeText(CategorySelectionScreen.this, "loading..", Style.INFO).show();

                }
            }

        });

        categoryRV.setLayoutManager(llm);

    }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){

            MenuInflater inflator = getMenuInflater();
            inflator.inflate(R.menu.category_manu, menu);
            return true;
        }

        public static List<categoryModel> getDataSet () {

            for (int i = 0; i < 9; i++) {

                String[] titles = {"Cell Phone & Accessories", "Electronics", "Health & Beauty", "Home & Garden", "Shoes & Accessories", "Sports & Outdoors", "Babies,Kids,Moms&Toys", "Apparel", "Fashion Accessories", "Wedding & Events"};
                int[] pics = {R.drawable.cell_phones, R.drawable.electronics, R.drawable.health_beauty, R.drawable.home_guarden, R.drawable.shoes, R.drawable.sports, R.drawable.babies, R.drawable.apparel, R.drawable.fashion, R.drawable.wedding};

                categoryModel current = new categoryModel();

                current.Title = titles[i % titles.length];
                current.thumnailUrl = pics[i % pics.length];

                catData.add(current);
            }

            return catData;
        }

        public interface ListenerToCategoryClicks {

            void categoryClicked(View v, int position, Bundle batton);
        }

    public interface ListenerToSubCategoryClicks {

        void subCategoryClicked(View v, int position, Bundle batton);
    }

        private static class RVCategoryAdapter extends RecyclerView.Adapter<CategorySelectionScreen.RVCategoryAdapter.CetegoryViewHolder> implements Filterable {


            private List<categoryModel> categoryList;
            private CategorySelectionScreen.RVCategoryAdapter.CategoryFilter categoryFilter;
            private CategorySelectionScreen.ListenerToCategoryClicks listenerToCategoryClicks;
            private List<categoryModel> dictionaryWords = new ArrayList<>();
            private List<categoryModel> filteredList = new ArrayList<>();
            private Context context;


            // Provide a suitable constructor (depends on the kind of dataset)
            RVCategoryAdapter(List<categoryModel> myDataset, Context context) {
                this.categoryList = new ArrayList<>();
                this.categoryList = myDataset;
                // this.filteredList = myDataset;
                this.context = context;
                categoryFilter = new CategorySelectionScreen.RVCategoryAdapter.CategoryFilter();

            }

            @Override
            public CategorySelectionScreen.RVCategoryAdapter.CetegoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                                   int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
                CategorySelectionScreen.RVCategoryAdapter.CetegoryViewHolder vh = new CategorySelectionScreen.RVCategoryAdapter.CetegoryViewHolder(v);
                return vh;
            }


            @Override
            public void onBindViewHolder(CategorySelectionScreen.RVCategoryAdapter.CetegoryViewHolder holder, int position) {

//                if(selected_item > 0) {
//                    holder.tvCategoryTitle.setBackgroundColor(Color.parseColor("#FFA500"));
//                } else {
//
//                    holder.tvCategoryTitle.setBackgroundColor(Color.parseColor("#ffffff")); //actually you should set to the normal text color
//                    selected_item = 0;
//                }

                holder.tvCategoryTitle.setText(categoryList.get(position).Title);

                Drawable img = this.context.getResources().getDrawable(categoryList.get(position).thumnailUrl);
                img.setBounds(0, 0, 60, 60);
                holder.tvCategoryTitle.setCompoundDrawables(img, null, null, null);

            }

            @Override
            public int getItemCount() {
                return categoryList.size();
            }

            void setClickListener(CategorySelectionScreen.ListenerToCategoryClicks listenerOfClicks) {
                this.listenerToCategoryClicks = listenerOfClicks;
            }

            @Override
            public Filter getFilter() {
                return categoryFilter;
            }


            class CetegoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


                TextView tvCategoryTitle;


                CetegoryViewHolder(View itemView) {
                    super(itemView);

                    tvCategoryTitle = (TextView) itemView.findViewById(R.id.tvCategoryTitle);
                    tvCategoryTitle.setOnClickListener(this);
                }

                @Override
                public void onClick(View v) {

                    //deleteItem(getAdapterPosition());

                    if (listenerToCategoryClicks != null) {

                        listenerToCategoryClicks.categoryClicked(v, getAdapterPosition(), new Bundle());
                    }

                }


            }

            class CategoryFilter extends Filter {

                private CategoryFilter() {
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
                        for (final categoryModel mWords : categoryList) {
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
