package au.com.geardoaustralia.categories;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.utilKit;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class SortAndRefinerScreen extends AppCompatActivity {

    Toolbar toolbar;
    ImageButton ibGridAndLinear;
    TextView tvRefine;
    TextView tvSort;

    RecyclerView subCatSorterRV;
    private static SortAndRefinerScreen.SubCatProductAdapter adapter;
    public static SortAndRefinerScreen.SubCatProductAdapterOne adapterOne;
    public static List<ProductInfoModel> filteredList = new ArrayList<>();
    public static List<ProductInfoModel> dictionaryWords = new ArrayList<>();
    LinearLayoutManager llm;
    GridLayoutManager glm;
    boolean toggleLayoutManager = false;

    //paging fields
    //http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort_and_refiner_screen);

        utilKit.hideSoftKeyboard(this);

        Intent i = getIntent();
        String subcategory = i.getStringExtra("subcat");

        toolbar = (Toolbar) findViewById(R.id.refineToolbar);
        //toolbar will automatically route to menu creation onOptionsMenu is available
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setup Sort/Refiner buttons and Recycler View UI grid changer button
        ibGridAndLinear = (ImageButton) findViewById(R.id.ibGridAndLinear);
        tvSort = (TextView) findViewById(R.id.tvSort);
        tvRefine = (TextView) findViewById(R.id.tvRefine);


        //setup recyclerview
        subCatSorterRV = (RecyclerView) findViewById(R.id.subCatSorterRV);
        subCatSorterRV.setHasFixedSize(true);
        adapter = new SubCatProductAdapter(getDataSet(), SortAndRefinerScreen.this);
        adapterOne = new SubCatProductAdapterOne(getDataSet(), SortAndRefinerScreen.this);
        adapter.setClickListener(new ListenerToProductClicks() {
            @Override
            public void productClicked(View v, int position, Bundle batton) {

            }


        });
        subCatSorterRV.setAdapter(adapter);
        llm = new LinearLayoutManager(SortAndRefinerScreen.this, LinearLayoutManager.VERTICAL, false);
        glm = new GridLayoutManager(SortAndRefinerScreen.this, 2);
        subCatSorterRV.addOnScrollListener(new RecyclerView.OnScrollListener() {

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

                    Log.i("Yy!", "end called");

                    // Do something

                    loading = true;
                    Crouton.makeText(SortAndRefinerScreen.this, "loading..", Style.INFO).show();

                }
            }

        });

        subCatSorterRV.setLayoutManager(glm);

        ibGridAndLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //initially false
                if (toggleLayoutManager) {
                    toggleLayoutManager = false;

                    subCatSorterRV.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    //set grid layout manager with glm
                    subCatSorterRV.setLayoutManager(glm);
                    ibGridAndLinear.setBackgroundResource(R.drawable.ic_grid_grey);
                    //adapter.notifyDataSetChanged();
                } else {
                    //set linear layout manager with llm
                    subCatSorterRV.setAdapter(adapterOne);
                    adapterOne.notifyDataSetChanged();
                    toggleLayoutManager = true;
                    subCatSorterRV.setLayoutManager(llm);
                    ibGridAndLinear.setBackgroundResource(R.drawable.ic_store_grey);
                    //adapter.notifyDataSetChanged();
                }
            }
        });

    }


    public interface ListenerToProductClicks {

        void productClicked(View v, int position, Bundle batton);
    }


    private static class SubCatProductAdapter extends RecyclerView.Adapter<SortAndRefinerScreen.SubCatProductAdapter.SubCetegoryViewHolder> implements Filterable {

        //For grid view layout adapter

        private List<ProductInfoModel> productList;
        private SortAndRefinerScreen.SubCatProductAdapter.SubCategoryFilter subCategoryFilter;
        private SortAndRefinerScreen.ListenerToProductClicks listenerToProductClicks;
        private List<ProductInfoModel> dictionaryWords = new ArrayList<>();
        private List<ProductInfoModel> filteredList = new ArrayList<>();

        // Provide a suitable constructor (depends on the kind of dataset)
        SubCatProductAdapter(List<ProductInfoModel> myDataset, Context context) {
            this.productList = new ArrayList<>();
            this.productList = myDataset;
            // this.filteredList = myDataset;
            subCategoryFilter = new SortAndRefinerScreen.SubCatProductAdapter.SubCategoryFilter();
        }

        @Override
        public SubCetegoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_layout, parent, false);
            final SortAndRefinerScreen.SubCatProductAdapter.SubCetegoryViewHolder vh = new SortAndRefinerScreen.SubCatProductAdapter.SubCetegoryViewHolder(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("ff", "ddd");

                }
            });

            return vh;
        }


        @Override
        public void onBindViewHolder(SortAndRefinerScreen.SubCatProductAdapter.SubCetegoryViewHolder holder, int position) {

            holder.ivProductImage.setImageResource(productList.get(position).thumnailUrl);
            holder.tvPPrice.setText(productList.get(position).price);
            holder.tvFShipping.setText("free shiiping included");
            holder.tvMinOrder.setText("min order");
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
        public Filter getFilter() {
            return subCategoryFilter;
        }


        class SubCetegoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            //for Grid layout card view
            CardView card_view;
            ImageView ivProductImage;
            TextView tvPPrice;
            TextView tvFShipping;
            TextView tvMinOrder;
            ToggleButton tbFavorite;


            SubCetegoryViewHolder(View itemView) {
                super(itemView);

                card_view = (CardView) itemView.findViewById(R.id.card_view);
                card_view.setOnClickListener(this);
                ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
                tvPPrice = (TextView) itemView.findViewById(R.id.tvPPrice);
                tvFShipping = (TextView) itemView.findViewById(R.id.tvFShipping);
                tvMinOrder = (TextView) itemView.findViewById(R.id.tvMinOrder);
                tbFavorite = (ToggleButton) itemView.findViewById(R.id.tbFavorite);

            }

            @Override
            public void onClick(View v) {

                //deleteItem(getAdapterPosition());

                if (listenerToProductClicks != null) {

                    listenerToProductClicks.productClicked(v, getAdapterPosition(), new Bundle());
                }

            }


        }

        class SubCategoryFilter extends Filter {

            private SubCategoryFilter() {
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
                    for (final ProductInfoModel mWords : productList) {
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


    }

    private static class SubCatProductAdapterOne extends RecyclerView.Adapter<SortAndRefinerScreen.SubCatProductAdapterOne.SubCetegoryViewHolder> implements Filterable {

        //for linear layout adapter
        private List<ProductInfoModel> productList;
        private SortAndRefinerScreen.SubCatProductAdapterOne.SubCategoryFilter subCategoryFilter;
        private SortAndRefinerScreen.ListenerToProductClicks listenerToProductClicks;
        private List<ProductInfoModel> dictionaryWords = new ArrayList<>();
        private List<ProductInfoModel> filteredList = new ArrayList<>();

        // Provide a suitable constructor (depends on the kind of dataset)
        SubCatProductAdapterOne(List<ProductInfoModel> myDataset, Context context) {
            this.productList = new ArrayList<>();
            this.productList = myDataset;
            // this.filteredList = myDataset;
            subCategoryFilter = new SortAndRefinerScreen.SubCatProductAdapterOne.SubCategoryFilter();
        }

        @Override
        public SubCetegoryViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sort_layout_one, parent, false);
            final SortAndRefinerScreen.SubCatProductAdapterOne.SubCetegoryViewHolder vh = new SortAndRefinerScreen.SubCatProductAdapterOne.SubCetegoryViewHolder(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("ff", "ddd");

                }
            });

            return vh;
        }


        @Override
        public void onBindViewHolder(SortAndRefinerScreen.SubCatProductAdapterOne.SubCetegoryViewHolder holder, int position) {

            holder.ivProductTumbnailImage.setImageResource(productList.get(position).thumnailUrl);
            holder.tvPTitle.setText(productList.get(position).Title);
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

        @Override
        public Filter getFilter() {
            return subCategoryFilter;
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

                    listenerToProductClicks.productClicked(v, getAdapterPosition(), new Bundle());
                }

            }


        }

        class SubCategoryFilter extends Filter {

            private SubCategoryFilter() {
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
                    for (final ProductInfoModel mWords : productList) {
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


    }


    public static List<ProductInfoModel> getDataSet() {

        GlobalContext globalContext = GlobalContext.getInstance();
        globalContext.makeTestDataSet();
        return globalContext.data;
    }

}

