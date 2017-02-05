package au.com.geardoaustralia.FullProductScreen.helpers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.utils.CommonConstants;

/**
 * Created by Dasun on 7/9/2015.
 */
public class DetailsTouchImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<Image> images;
    ImageLoader imageLoader;

    public DetailsTouchImageAdapter(Activity paramActivity,  ArrayList<Image> images)
    {
        this._activity = paramActivity;
        this.images = images;
        this.imageLoader = new ImageLoader(_activity);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup paramViewGroup, int position)
    {

        final Image image = images.get(position);
        // TouchImageView localImageView = new TouchImageView(this._activity);
        ImageView localImageView = new ImageView(this._activity);

//        new DownloadImageTask(localImageView)
//                .execute(selectedProduct.PlaceInfo.ImagesAndroid.get(paramInt).ImageData);

        if (TextUtils.isEmpty(image.thumb)) {
            localImageView.setImageResource(R.drawable.logo_geardo);
        } else {
            imageLoader.loadAssetsImage(_activity, Uri.parse(CommonConstants.ROOT_PATH + image.thumb), localImageView);
        }

        localImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        localImageView.bringToFront();
        localImageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        paramViewGroup.addView(localImageView);

        return localImageView;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
