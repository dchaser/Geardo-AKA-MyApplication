package au.com.geardoaustralia.FullProductScreen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.ArrayList;

import au.com.geardoaustralia.FullProductScreen.helpers.DetailsTouchImageAdapter;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.gallery.Image;

/**
 * Created by codeninja on 7/5/2015.
 */
public class DetailsActivity extends Activity {

    private ArrayList<Image> images;
    ViewPager vpager;
    InputStream localInputStream;
    DetailsTouchImageAdapter adapter;
    String selectedImagePathLarge = "";

    Product selectedProduct;
    int position = 0;

    Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.details_activity);

        btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.selectedProduct = getIntent().getParcelableExtra("selectedProduct");
        this.position = getIntent().getIntExtra("position", 0);

        fetchImages();

        vpager = (ViewPager) findViewById(R.id.vpager);
        adapter = new DetailsTouchImageAdapter(this, images);
        vpager.setAdapter(adapter);
        vpager.setCurrentItem(this.position);


    }

    //get image set
    private void fetchImages() {
        images = new ArrayList<>();
        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("please wait...");
        pDialog.show();
        int size = selectedProduct.thumb_count;

        this.images = fillImageArrayUsingThumbnailCount(size);
        pDialog.hide();

    }


    private ArrayList<Image> fillImageArrayUsingThumbnailCount(int sliderSize) {

        ArrayList<Image> file_maps = new ArrayList<>();

        switch (sliderSize) {

            case 1:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                break;
            case 2:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                break;
            case 3:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                break;
            case 4:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                break;
            case 5:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                file_maps.add(new Image("Caption 5", selectedProduct.product_thumb_five));
                break;
            case 6:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                file_maps.add(new Image("Caption 5", selectedProduct.product_thumb_five));
                file_maps.add(new Image("Caption 6", selectedProduct.product_thumb_six));
                break;
            case 7:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                file_maps.add(new Image("Caption 5", selectedProduct.product_thumb_five));
                file_maps.add(new Image("Caption 6", selectedProduct.product_thumb_six));
                file_maps.add(new Image("Caption 7", selectedProduct.product_thumb_seven));
                break;
            case 8:

                file_maps.add(new Image("Caption 1", selectedProduct.product_thumb_one));
                file_maps.add(new Image("Caption 2", selectedProduct.product_thumb_two));
                file_maps.add(new Image("Caption 3", selectedProduct.product_thumb_three));
                file_maps.add(new Image("Caption 4", selectedProduct.product_thumb_four));
                file_maps.add(new Image("Caption 5", selectedProduct.product_thumb_five));
                file_maps.add(new Image("Caption 6", selectedProduct.product_thumb_six));
                file_maps.add(new Image("Caption 7", selectedProduct.product_thumb_seven));
                file_maps.add(new Image("Caption 8", selectedProduct.product_thumb_eight));
                break;
            case 9:
                break;
            case 10:
                break;


        }

        return file_maps;

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

