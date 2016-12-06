package au.com.geardoaustralia.categories;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cart.ViewCartPopup;
import au.com.geardoaustralia.utils.GlobalContext;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by DasunPC on 11/22/16.
 */

public class CategorySelectionScreen extends AppCompatActivity {

    GlobalContext globalContext;
    //bottom bar controls
    private LinearLayout llCats;
    private LinearLayout llDeals;
    private LinearLayout llCart;
    private LinearLayout llAccount;
    private LinearLayout llHome;

    private ImageView ivHome;
    private ImageView ivCats;
    private ImageView ivDeals;
    private ImageView ivCart;
    private ImageView ivAccount;

    private TextView tvHome;
    private TextView tvCats;
    private TextView tvDeals;
    private TextView tvCart;
    private TextView tvAccount;

    //from beginning
    private Toolbar toolbar;
    private NDFragment ndFragment;
    public static CategorySelectionScreen.RVCategoryAdapter adapter;
    public static List<categoryModel> catData = new ArrayList<>();
    private static int[] selectedArray;



    public static ListView lvCategories;
    public static int selectedPos = -1;

    Button btnGoToCart;
    TextView tvCartCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_layout);

        if (globalContext == null) {

            globalContext = GlobalContext.getInstance();
        }

        //set page number in App class
        globalContext.selectedPage = 1;

        toolbar = (Toolbar) findViewById(R.id.catToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ndFragment = (NDFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_id);
        //setup method shoud be provided with view that must be passed from activity to fragment
        ndFragment.setUp(R.id.fragment_navigation_drawer_id, (DrawerLayout) findViewById(R.id.activity_drawer_layout), toolbar);

        setUpBottomBarControls(globalContext.selectedPage);
        clearAll(globalContext.selectedPage);

        lvCategories = (ListView) findViewById(R.id.lvCategories);

        // get data from the table by the ListAdapter
        ListAdapter customAdapter = new ListAdapter(this, R.layout.category_view, getDataSet());

        lvCategories.setAdapter(customAdapter);
        //lvCategories.setItemsCanFocus(true);

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View clickedViewItem, int i, long l) {
                selectedPos = i;

                //Object o = lvCategories.getItemAtPosition(i);
                clickedViewItem.setBackgroundColor(getResources().getColor(R.color.orange));
                NDFragment.changeDatasetAndOpenDrawer(i);


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        //handle search bar
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.mSearch).getActionView();
        // Assumes current activity is the searchable activity
        ComponentName componentName = new ComponentName(this, MainActivity.class);//getComponentName();
        SearchableInfo info = searchManager.getSearchableInfo(componentName);
        searchView.setSearchableInfo(info);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("App", "setOnSearchClickListener");
                if (searchView.getQuery().length() == 0)
                    searchView.setQuery("", true);
            }
        });

        final Menu menus = menu;
        // SetUpMenu(mMenu);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                View view = (View) findViewById(R.id.mCart);
                MenuItem item = menus.getItem(1);
                if (item.getItemId() == R.id.mCart) {
                    item.setActionView(R.layout.notification_update_count_layout);
                    View itemChooser = item.getActionView();
                    tvCartCount = (TextView) itemChooser.findViewById(R.id.tvCartCount);
                    btnGoToCart = (Button) itemChooser.findViewById(R.id.btnGoToCart);
                    btnGoToCart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(CategorySelectionScreen.this, "Cart", Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getSupportFragmentManager();
                            ViewCartPopup dFragment = ViewCartPopup.newInstance("Some Title");
                            dFragment.show(fm, "cart");

                        }
                    });
                }
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mCart:

                // Show DialogFragment
                FragmentManager fm = getSupportFragmentManager();
                ViewCartPopup dFragment = ViewCartPopup.newInstance("Some Title");
                dFragment.show(fm, "cart");

                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);


        }

    }


    private void setUpBottomBarControls(int pageNumber) {

        //Home
        llHome = (LinearLayout) findViewById(R.id.llHome);
        ivHome = (ImageView) findViewById(R.id.ivHome);
        tvHome = (TextView) findViewById(R.id.tvHome);

        //Categories
        llCats = (LinearLayout) findViewById(R.id.llCats);
        ivCats = (ImageView) findViewById(R.id.ivCats);
        tvCats = (TextView) findViewById(R.id.tvCats);
        //Deals
        llDeals = (LinearLayout) findViewById(R.id.llDeals);
        ivDeals = (ImageView) findViewById(R.id.ivDeals);
        tvDeals = (TextView) findViewById(R.id.tvDeals);
        //Cart
        llCart = (LinearLayout) findViewById(R.id.llCart);
        ivCart = (ImageView) findViewById(R.id.ivCart);
        tvCart = (TextView) findViewById(R.id.tvCart);
        //Account
        llAccount = (LinearLayout) findViewById(R.id.llAccount);
        ivAccount = (ImageView) findViewById(R.id.ivAccount);
        tvAccount = (TextView) findViewById(R.id.tvAccount);

        clearAll(pageNumber);

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 0;

            }
        });


        llCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 1;
                startActivity(new Intent(CategorySelectionScreen.this, CategorySelectionScreen.class));
                clearAll(globalContext.selectedPage);
                //go to category screen
                //startActivity(new Intent(MainActivity.this, CategorySelectionScreen.class));

            }
        });


        llDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 2;
                clearAll(globalContext.selectedPage);
                // startActivity(new Intent(MainActivity.this, DealsScreen.class));

            }
        });


        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 3;
                clearAll(globalContext.selectedPage);
                //startActivity(new Intent(MainActivity.this, DisplayCartDialogFragment.class));

            }
        });


        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class
                globalContext.selectedPage = 3;
                clearAll(globalContext.selectedPage);
                // startActivity(new Intent(MainActivity.this, AccountScreen.class));

            }
        });

    }

    private void clearAll(int page) {

        switch (page) {

            case 0:
                //Home selected
                ivHome.setBackgroundResource(R.drawable.ic_home_blue);
                tvHome.setTextColor(getResources().getColor(R.color.lightBlue));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));

                break;


            case 1:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(getResources().getColor(R.color.gray));

                //Categories selected
                ivCats.setBackgroundResource(R.drawable.ic_categories_blue);
                tvCats.setTextColor(getResources().getColor(R.color.lightBlue));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 2:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(getResources().getColor(R.color.gray));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals selected
                ivDeals.setBackgroundResource(R.drawable.ic_search_blue);
                tvDeals.setTextColor(getResources().getColor(R.color.lightBlue));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 3:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(getResources().getColor(R.color.gray));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart Selected
                ivCart.setBackgroundResource(R.drawable.ic_cart_blue);
                tvCart.setTextColor(getResources().getColor(R.color.lightBlue));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));
                break;
            case 4:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(getResources().getColor(R.color.gray));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account Selected
                ivAccount.setBackgroundResource(R.drawable.ic_account_blue);
                tvAccount.setTextColor(getResources().getColor(R.color.lightBlue));
                break;

            default:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_blue);
                tvHome.setTextColor(getResources().getColor(R.color.lightBlue));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(getResources().getColor(R.color.gray));
                break;
        }


    }

    public static List<categoryModel> getDataSet() {

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
            selectedArray = new int[myDataset.size()];

            // Now initialize the Array with all zeros
            for (int i = 0; i < selectedArray.length; i++)
                selectedArray[i] = 0;

        }

        @Override
        public CategorySelectionScreen.RVCategoryAdapter.CetegoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                               int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_view, parent, false);
            final CategorySelectionScreen.RVCategoryAdapter.CetegoryViewHolder vh = new CategorySelectionScreen.RVCategoryAdapter.CetegoryViewHolder(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("ff", "ddd");
                    // Update the selectedArray. Set 1 for the selected item

                    // and 0 for the others.
                    // updateSelectedArray(vh.getAdapterPosition());

//                        if ( != RecyclerView.NO_POSITION) {
//                            if (recyclerItemClickListener != null) {
//                                recyclerItemClickListener.onItemLongClick(selectedPos, contactHolder.itemView);
//
//                                // Repopulate the list here
//                                notifyDatasetChanged();
//                            }
//                        }
                }
            });

            return vh;
        }


        @Override
        public void onBindViewHolder(CategorySelectionScreen.RVCategoryAdapter.CetegoryViewHolder holder, int position) {

            holder.tvCategoryTitle.setText(categoryList.get(position).Title);

            Drawable img = this.context.getResources().getDrawable(categoryList.get(position).thumnailUrl);
            img.setBounds(0, 0, 60, 60);
            holder.tvCategoryTitle.setCompoundDrawables(img, null, null, null);


            // RVCategoryAdapter.CetegoryViewHolder viewHolder= (CetegoryViewHolder) categoryRV.findViewHolderForPosition(position);
//            if (selectedArray[position] == 0) {
//
//                holder.tvCategoryTitle.setBackgroundColor(Color.WHITE);
//            }
//            else {
//                holder.tvCategoryTitle.setBackgroundColor(Color.GREEN);
//            }
//            notifyItemChanged(position);
//            final int SELECTED = 1;
//            if (selectedArray[position] == SELECTED) {
//               // holder.itemView.setSelected(true);
//
//            } else {
//                //holder.itemView.setSelected(false);
//                holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.white));
//            }

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

//            public void toggleSelection(int pos) {
//                if (selectedItems.get(pos, false)) {
//                    selectedItems.delete(pos);
//                }
//                else {
//                    selectedItems.put(pos, true);
//                }
//                notifyItemChanged(pos);
//            }
//
//            public void clearSelections() {
//                selectedItems.clear();
//                notifyDataSetChanged();
//            }
//
//            public int getSelectedItemCount() {
//                return selectedItems.size();
//            }
//
//            public List<Integer> getSelectedItems() {
//                List<Integer> items =
//                        new ArrayList<Integer>(selectedItems.size());
//                for (int i = 0; i < selectedItems.size(); i++) {
//                    items.add(selectedItems.keyAt(i));
//                }
//                return items;
//            }

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
