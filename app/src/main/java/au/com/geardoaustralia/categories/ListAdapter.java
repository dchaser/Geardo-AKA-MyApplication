package au.com.geardoaustralia.categories;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import au.com.geardoaustralia.R;

/**
 * Created by DasunPC on 11/29/16.
 */

public class ListAdapter extends BaseAdapter {

    private List<categoryModel> rows;
    final Context context;

    public ListAdapter(Context context, int textViewResourceId) {
        this.context = context;

    }

    public ListAdapter(Context context, int resource, List<categoryModel> items) {
        this.context = context;
        this.rows = items;

    }


    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public categoryModel getItem(int position) {
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
            v = vi.inflate(R.layout.category_view, parent, false);
        }

        categoryModel p = getItem(position);

        if (p != null) {

            ImageView ivCatImage = (ImageView) v.findViewById(R.id.ivCatImage);
            TextView tvCategoryTitle = (TextView) v.findViewById(R.id.tvCategoryTitle);

            if(ivCatImage != null){

                Drawable img = this.context.getResources().getDrawable(p.thumnailUrl);
                ivCatImage.setImageDrawable(img);
               // img.setBounds(0, 0, 60, 60);
               // tvCategoryTitle.setCompoundDrawables(img, null, null, null);
            }

            if (tvCategoryTitle != null) {
                tvCategoryTitle.setText(p.Title);
            }


        }

        return v;
    }



}