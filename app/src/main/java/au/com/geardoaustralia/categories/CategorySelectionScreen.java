package au.com.geardoaustralia.categories;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.TopSliderActivity;
import au.com.geardoaustralia.cartNew.BaseActivity;
import au.com.geardoaustralia.cartNew.data.Category;
import au.com.geardoaustralia.cartNew.data.Subcategory;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.MenuBarHandler;

/**
 * Created by DasunPC on 11/22/16.
 */

public class CategorySelectionScreen extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    GlobalContext globalContext;

        private SubCategorySelectorFragment subCategorySelectorFragment;
    public static List<String> subCatData = new ArrayList<>();
    private static int[] selectedArray;
    public static SlidingPaneLayout slidingPanel;

    MenuBarHandler menuBarHandler;

    public static ListView lvSubCatList;
    public static int selectedPos = -1;

    public static dataListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_layout);


        slidingPanel = (SlidingPaneLayout) findViewById(R.id.slidingPanel);
        slidingPanel.closePane();

        // Add the back button to the toolbar.
        Toolbar toolbar = getActionBarToolbar();
        toolbar.setNavigationIcon(R.drawable.hamburger);
        toolbar.setNavigationContentDescription(R.string.close_and_go_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CategorySelectionScreen.this, TopSliderActivity.class));
                overridePendingTransition(R.anim.slide_up, R.anim.stay);
            }
        });

        menuBarHandler = new MenuBarHandler(CategorySelectionScreen.this);
        adapter = new dataListAdapter((ArrayList<String>) subCatData, CategorySelectionScreen.this);

        lvSubCatList = (ListView) findViewById(R.id.lvSubCatList);
        lvSubCatList.setAdapter(adapter);
        lvSubCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View clickedViewItem, int position, long l) {

                String selectedScategoryName = (String) lvSubCatList.getAdapter().getItem(position);
                selectedPos = position;

                //Object o = lvCategories.getItemAtPosition(i);
//                clickedViewItem.setBackgroundColor(getResources().getColor(R.color.orange));
                Intent toSortScreen = new Intent(CategorySelectionScreen.this, SortAndRefinerScreen.class);
                toSortScreen.putExtra("passing_subcategory_name", selectedScategoryName);
                startActivity(toSortScreen);


            }
        });

        //set electronic as First Subcategory
        CategorySelectionScreen.changeDataset(1);
        slidingPanel.openPane();
    }

    public static void changeDataset(int mainCategory) {

        subCatData.clear();
        ArrayList<String> subCategories = DatabaseManager.getInstance().getAllSubCategoryNamesByCategory(mainCategory);

        for (int i = 0; i < subCategories.size(); i++) {


            subCatData.add(subCategories.get(i));
        }

        adapter.notifyDataSetChanged();

    }




    class dataListAdapter extends BaseAdapter {

        ArrayList<String> subCatData;
        Context context;


        public dataListAdapter(ArrayList<String> subCatData, Context context) {

            this.subCatData = subCatData;
            this.context = context;

        }

        public int getCount() {
            return subCatData.size();
        }

        public Object getItem(int position) {
            return subCatData.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            SubCategoryViewHolder holder;

        /*
         * If convertView is not null, we can reuse it directly, no inflation required!
         * We only inflate a new View when the convertView is null.
         */
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.subcategory_view, null);

                // Create a ViewHolder and store references to the two children views
                holder = new SubCategoryViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.tvCategoryTitle);

                // The tag can be any Object, this just happens to be the ViewHolder
                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (SubCategoryViewHolder) convertView.getTag();
            }

            // Bind that data efficiently!
            holder.title.setText(subCatData.get(position));

            return convertView;
        }
    }

    static class SubCategoryViewHolder {
        TextView title;
    }


}
