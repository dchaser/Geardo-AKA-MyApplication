package au.com.geardoaustralia.categories;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;

/**
 * Created by DasunPC on 1/16/17.
 */

public class CategoryMasterFragment extends ListFragment {

    ListView lvSubCatList;
    int selectedPos = 0;

    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master, container);

        // get data from the table by the ListAdapter
        ListAdapter customAdapter = new ListAdapter(getActivity(), R.layout.category_view, DatabaseManager.getInstance().getAllCategories());

        setListAdapter(customAdapter);




        return view;
    }

    @Override
    public void onListItemClick(ListView l, View clickedViewItem, int position, long id) {
        super.onListItemClick(l, clickedViewItem, position, id);

        ListView lv = getListView();
        int size = lv.getChildCount();


        for (int a = 0; a < size; a++) {

            if (a != position) {

                lv.getChildAt(a).setBackgroundColor(Color.WHITE);
            }
        }

        //change sub category data set according to main category
        //main category is position plus one
        CategorySelectionScreen.changeDataset(position+1);
        clickedViewItem.setBackgroundColor(getResources().getColor(R.color.orange));

        CategorySelectionScreen.slidingPanel.closePane();

    }
}
