package au.com.geardoaustralia.categories;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.utils.CommonConstants;

/**
 * Created by DasunPC on 1/17/17.
 */

public class SortAdapter extends BaseAdapter {

    ImageLoader imageLoader;
    private ArrayList<String> rows;
    final Context context;

    public SortAdapter(Context context, int resource, ArrayList<String> items) {
        this.context = context;
        this.rows = items;
        this.imageLoader = new ImageLoader(context);

    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public String getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(parent.getContext());
            v = vi.inflate(R.layout.subcategory_view, parent, false);
        }

        String item = getItem(position);

        if (item != null) {

            TextView tvCategoryTitle = (TextView) v.findViewById(R.id.tvCategoryTitle);


            if (tvCategoryTitle != null) {
                tvCategoryTitle.setText(item);
            }


        }

        return v;
    }


}
