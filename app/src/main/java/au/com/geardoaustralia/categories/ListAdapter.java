package au.com.geardoaustralia.categories;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.utils.CommonConstants;

/**
 * Created by DasunPC on 11/29/16.
 */

public class ListAdapter extends BaseAdapter {

    ImageLoader imageLoader;
    private List<categoryModel> rows;
    final Context context;

    public ListAdapter(Context context, int textViewResourceId) {
        this.context = context;

    }

    public ListAdapter(Context context, int resource, List<categoryModel> items) {
        this.context = context;
        this.rows = items;
        this.imageLoader = new ImageLoader(context);

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

                if (TextUtils.isEmpty(p.thumnailUrl)) {
                    ivCatImage.setImageResource(R.drawable.logo_geardo);
                } else {
                    imageLoader.loadAssetsImage(this.context, Uri.parse(CommonConstants.CATEGORY_PATH + p.thumnailUrl), ivCatImage);
                }

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