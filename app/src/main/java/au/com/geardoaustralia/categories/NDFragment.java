package au.com.geardoaustralia.categories;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.R;

/**
 * Created by DasunPC on 11/22/16.
 */

public class NDFragment extends Fragment {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static DrawerLayout drawerLayout;
    private static View containerView;

    private static RecyclerView categoryFilterRV;
    public static catFilterAdapter adapter;
    public static List<categoryModel> subCatData = new ArrayList<>();
    LinearLayoutManager llm;


    public NDFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nd_fragment, container, false);

        categoryFilterRV = (RecyclerView) view.findViewById(R.id.categoryFilterRV);
        categoryFilterRV.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        adapter = new NDFragment.catFilterAdapter(getSubCategryDataSet(0), getActivity());
        adapter.setClickListener(new CategorySelectionScreen.ListenerToSubCategoryClicks() {
            @Override
            public void subCategoryClicked(View v, int position, Bundle batton) {

                Toast.makeText(getActivity(), "Sub Cat Clicked "+position, Toast.LENGTH_SHORT).show();

            }
        });


        categoryFilterRV.setAdapter(adapter);
        categoryFilterRV.setLayoutManager(llm);




        return view;
    }

    public static void changeDatasetAndOpenDrawer(int mainCategory){

        getSubCategryDataSet(mainCategory);
        adapter.notifyDataSetChanged();

        openDrawerExplicitly();

    }


    public static void openDrawerExplicitly() {

        //opens the drawer

        drawerLayout.openDrawer(containerView);

    }

    public static void closeDrawerExplicitly() {

        //opens the drawer

        drawerLayout.closeDrawer(containerView);

    }

    public boolean isDrawerOpened() {

        if (drawerLayout.isDrawerOpen(containerView)) {
            return true;
        } else {
            return false;
        }
    }

    public void setUp(int fragment_navigation_drawer_id, DrawerLayout dLayout, final Toolbar toolBar) {

        containerView = getActivity().findViewById(fragment_navigation_drawer_id);


        drawerLayout = dLayout;
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolBar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    toolBar.setAlpha(1 - slideOffset);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };

        //stop drawer from swipe gesture unless licked explicitly
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public static List<categoryModel> getSubCategryDataSet(int mainCategory) {

        switch (mainCategory){

            case 0:
                //Cell Phone & Accessories
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Cell Phone & Accessories", "Cell Phone", "CellPhone PArts", "Refurbished Cell Phone", "Wearable Technology"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }

                break;

            case 1:
                //Electronics
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Electronic Cigarettes", "Headphones & Earphone", "A/V Accessories & Cables", "Sattellite & Cable TV", "MP3/4 Accessories", "Home & Audio", "Gadgets", "MP3 Players", "Electronic Publishing"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }

                break;

            case 2:

                //Health and Beauty
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Makeup & Tools", "Shaving & Hair Removal", "Nail Art & Tools", "Beauty Equipment", "Tattoos & Body Art", "Skin Care", "Oral Hyiene", "Medical Supplies"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }

                break;

            case 3:

                //Home & Garden
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Home Appliances", "Arts,Crafts & Gifts", "Pet Supplies", "Home Decor", "Faucets, Showers & Accs", "Tools", "Housekeeping Organization", "Patio,Lawn & Garden"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }

                break;

            case 4:

                //Shoes & Accessories
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Sandals", "Sports shoes", "Casual Shoes", "Dress SHoes", "Slippers", "Shoe Parts and Accessories", "Boots", "Handmade Shoes"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }

                break;

            case 5:

                //Shoes & Accessories
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Sandals", "Sports shoes", "Casual Shoes", "Dress SHoes", "Slippers", "Shoe Parts and Accessories", "Boots", "Handmade Shoes"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }


                break;

            case 6:

                //Shoes & Accessories
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Sandals", "Sports shoes", "Casual Shoes", "Dress SHoes", "Slippers", "Shoe Parts and Accessories", "Boots", "Handmade Shoes"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }

                break;

            case 7:

                //Shoes & Accessories
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Sandals", "Sports shoes", "Casual Shoes", "Dress SHoes", "Slippers", "Shoe Parts and Accessories", "Boots", "Handmade Shoes"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }

                break;

            case 8:

                //Shoes & Accessories
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Sandals", "Sports shoes", "Casual Shoes", "Dress SHoes", "Slippers", "Shoe Parts and Accessories", "Boots", "Handmade Shoes"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }

                break;

            default:

                //Shoes & Accessories
                subCatData.clear();

                for (int i = 0; i < 9; i++) {

                    String[] titles = {"Sandals", "Sports shoes", "Casual Shoes", "Dress SHoes", "Slippers", "Shoe Parts and Accessories", "Boots", "Handmade Shoes"};

                    categoryModel current = new categoryModel();

                    current.Title = titles[i % titles.length];

                    subCatData.add(current);
                }

                break;


        }
        return subCatData;

    }

    private static class catFilterAdapter extends RecyclerView.Adapter<NDFragment.catFilterAdapter.catFilterVH> implements Filterable {


        private List<categoryModel> categoryList;
        private NDFragment.catFilterAdapter.subCategoryFilter subCategoryFilter;
        private CategorySelectionScreen.ListenerToSubCategoryClicks listenerToSubCategoryClicks;
        private List<categoryModel> dictionaryWords = new ArrayList<>();
        private List<categoryModel> filteredList = new ArrayList<>();
        private Context context;


        // Provide a suitable constructor (depends on the kind of dataset)
        catFilterAdapter(List<categoryModel> myDataset, Context context) {
            this.categoryList = new ArrayList<>();
            this.categoryList = myDataset;
            // this.filteredList = myDataset;
            this.context = context;
            subCategoryFilter = new NDFragment.catFilterAdapter.subCategoryFilter();

        }

        @Override
        public NDFragment.catFilterAdapter.catFilterVH onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_view, parent, false);
            NDFragment.catFilterAdapter.catFilterVH vh = new NDFragment.catFilterAdapter.catFilterVH(v);
            return vh;
        }


        @Override
        public void onBindViewHolder(NDFragment.catFilterAdapter.catFilterVH holder, int position) {


            holder.tvCategoryTitle.setText(categoryList.get(position).Title);

//            Drawable img = this.context.getResources().getDrawable(categoryList.get(position).thumnailUrl);
//            img.setBounds(0, 0, 60, 60);
//            holder.tvCategoryTitle.setCompoundDrawables(img, null, null, null);
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }

        void setClickListener(CategorySelectionScreen.ListenerToSubCategoryClicks listenerOfClicks) {
            this.listenerToSubCategoryClicks = listenerOfClicks;
        }

        @Override
        public Filter getFilter() {
            return subCategoryFilter;
        }


        class catFilterVH extends RecyclerView.ViewHolder implements View.OnClickListener {


            TextView tvCategoryTitle;


            catFilterVH(View itemView) {
                super(itemView);

                tvCategoryTitle = (TextView) itemView.findViewById(R.id.tvCategoryTitle);
                tvCategoryTitle.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                //deleteItem(getAdapterPosition());

                if (listenerToSubCategoryClicks != null) {

                    listenerToSubCategoryClicks.subCategoryClicked(v, getAdapterPosition(), new Bundle());
                }

            }


        }

        class subCategoryFilter extends Filter {

            private subCategoryFilter() {
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
