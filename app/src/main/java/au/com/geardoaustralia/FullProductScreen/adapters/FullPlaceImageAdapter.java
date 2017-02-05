package au.com.geardoaustralia.FullProductScreen.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.util.ArrayList;

import au.com.geardoaustralia.FullProductScreen.DetailsActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.utils.CommonConstants;

/**
 * Created by codeninja on 7/4/2015.
 */
public class FullPlaceImageAdapter extends PagerAdapter {

    Activity activity;
    private ArrayList<Image> images;
    ImageLoader imageLoader;
    Product selectedProduct;


    public FullPlaceImageAdapter(Activity activity, Product selectedProduct,  ArrayList<Image> images) {
        this.activity = activity;
        this.imageLoader = new ImageLoader(activity);
        this.images = images;
        this.selectedProduct = selectedProduct;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup paramViewGroup, final int position) {

        final Image image = images.get(position);

        LayoutInflater inflater = (LayoutInflater) this.activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.full_place_top_item,
                paramViewGroup, false);

        ImageView localImageView = (ImageView) view.findViewById(R.id.plcImage);

        if (TextUtils.isEmpty(image.thumb)) {
            localImageView.setImageResource(R.drawable.logo_geardo);
        } else {
            imageLoader.loadAssetsImage(activity, Uri.parse(CommonConstants.ROOT_PATH + image.thumb), localImageView);
        }


//        new DownloadImageTask(localImageView)
//                .execute(currentPlace.PlaceInfo.ImagesAndroid.get(position).ImageData);

        localImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        localImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                Intent localIntent = new Intent(FullPlaceImageAdapter.this.activity, DetailsActivity.class);

                localIntent.putExtra("selectedProduct", selectedProduct);
                localIntent.putExtra("position", position);
                FullPlaceImageAdapter.this.activity.startActivity(localIntent);


            }
        });
        // localImageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));

        // tvPlaceName.setText(this.currentPlaceName);

        paramViewGroup.addView(view);


        return view;
    }

    @Override
    public int getCount() {
        return selectedProduct.thumb_count;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    //Private classes
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
