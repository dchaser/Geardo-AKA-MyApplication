package au.com.geardoaustralia.MainScreen.MainContentMainActivity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import au.com.geardoaustralia.R;

/**
 * Created by DasunPC on 11/3/16.
 */

public class VpBannerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    int[] mResources = {
            R.drawable.category_one,
            R.drawable.category_two,
            R.drawable.category_three
    };

    public VpBannerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.banner_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(mResources[position]);


        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}