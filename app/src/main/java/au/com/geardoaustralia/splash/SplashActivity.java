package au.com.geardoaustralia.splash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.utilKit;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DasunPC on 10/25/16.
 */

public class SplashActivity extends AppCompatActivity {

    private static final String endpoint = "http://www.delaroystudios.com/glide/json/glideimages.json";
    private ProgressDialog pDialog;
//    private List<ProductThumbnailImageItem> productThumbnailImageItems = new ArrayList<>();
//    private ListView lvAutoScroll;
//    private ImageAdapter mAdapter;

    final long totalScrollTime = 100000; //total scroll time. I think that 300 000 000 years is close enough to infinity. if not enought you can restart timer in onFinish()

    final int scrollPeriod = 1000; // every 20 ms scoll will happened. smaller values for smoother

    final int heightToScroll = 20; // will be scrolled to 20 px every time. smaller values for smoother scrolling

    CircleImageView civPImage;
    TextView tvUserName;
    TextView tvAlreadyHaveAccount;
    Button btnCreateAccount;
    Button btnSignIn;

    // [START declare_auth]
    private FirebaseAuth auth;
    // [END declare_auth]

    private static int TIME_OUT = 4000; //Time to launch the another activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        pDialog = new ProgressDialog(this);
        if (utilKit.isNetworkAvailable(this)) {

            auth = GlobalContext.getFAuthInstance();

            FirebaseUser firebaseUser = auth.getCurrentUser();

            if (firebaseUser != null) {


            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, TIME_OUT);



//            lvAutoScroll = (ListView) findViewById(R.id.lvAutoScroll);
//            lvAutoScroll.post(new Runnable() {
//                @Override
//                public void run() {
//                    new CountDownTimer(totalScrollTime, scrollPeriod) {
//                        public void onTick(long millisUntilFinished) {
//                            lvAutoScroll.scrollBy(0, heightToScroll);
//                            mAdapter.notifyDataSetChanged();
//                        }
//
//                        public void onFinish() {
//                            //you can add code for restarting timer here
//                            lvAutoScroll.scrollBy(0, heightToScroll);
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }.start();
//                }
//            });
//
//            productThumbnailImageItems.clear();
//
//            for (int i = 0; i < 50; i++) {
//
//                ProductThumbnailImageItem image = new ProductThumbnailImageItem(R.drawable.category_one, R.drawable.category_two);
//                productThumbnailImageItems.add(image);
//
//            }
//
//            if (productThumbnailImageItems.size() > 0) {
//                mAdapter = new ImageAdapter(SplashActivity.this, productThumbnailImageItems);
//            }
//
//            lvAutoScroll.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
//
//            civPImage = (CircleImageView) findViewById(R.id.civPImage);
//            //set correct profile image
//
//            btnCreateAccount = (Button) findViewById(R.id.btnCreateAccount);
//            btnCreateAccount.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //should go to create account page
//                    startActivity(new Intent(SplashActivity.this, SignupActivity.class));
//                }
//            });
//
//            btnSignIn = (Button) findViewById(R.id.btnSignIn);
//            btnSignIn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //go to ding in screen
//                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
//                }
//            });


        } else {

            utilKit.showDialog(SplashActivity.this);
        }


    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    class ProductThumbnailImageItem {
//
//        private String item_image_url_one;
//        private String item_image_url_two;
//        public int imageOne;
//        public int imageTwo;
//
//        public ProductThumbnailImageItem(int imageOne, int imageTwo) {
//            this.imageOne = imageOne;
//            this.imageTwo = imageTwo;
//        }
//
//        public ProductThumbnailImageItem(String ivPrductThumb1, String ivPrductThumb2) {
//            this.item_image_url_one = ivPrductThumb1;
//            this.item_image_url_two = ivPrductThumb2;
//        }
//
////
////        public String getItem_image_url_one() {
////            return item_image_url_one;
////        }
////
////        public void setItem_image_url_one(String ivPrductThumb1) {
////            this.item_image_url_one = ivPrductThumb1;
////        }
////
////        public String getItem_image_url_two() {
////            return item_image_url_two;
////        }
////
////        public void setItem_image_url_two(String ivPrductThumb2) {
////            this.item_image_url_two = ivPrductThumb2;
////        }
//
//    }
//
//    class ImageAdapter extends BaseAdapter {
//
//        Context context;
//        List<ProductThumbnailImageItem> menuItems;
//
//        ImageAdapter(Context context, List<ProductThumbnailImageItem> menuItems) {
//            this.context = context;
//            this.menuItems = menuItems;
//        }
//
//        @Override
//        public int getCount() {
//            return menuItems.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return menuItems.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return menuItems.indexOf(getItem(position));
//        }
//
//        private class ViewHolder {
//            ImageView ivPrductThumb1;
//            ImageView ivPrductThumb2;
//        }
//
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ViewHolder holder = null;
//
//            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.product_image_item, null);
//                holder = new ViewHolder();
//
//                holder.ivPrductThumb1 = (ImageView) convertView.findViewById(R.id.ivPrductThumb1);
//                holder.ivPrductThumb2 = (ImageView) convertView.findViewById(R.id.ivPrductThumb2);
//
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            ProductThumbnailImageItem row_pos = menuItems.get(position);
//
//            holder.ivPrductThumb1.setImageResource(row_pos.imageOne);
//            holder.ivPrductThumb2.setImageResource(row_pos.imageTwo);
//
////            Picasso.with(context)
////                    .load(row_pos.getItem_image_url_one())
////                    .into(holder.ivPrductThumb1);
////
////            Picasso.with(context)
////                    .load(row_pos.getItem_image_url_two())
////                    .into(holder.ivPrductThumb2);
//
//
//            return convertView;
//        }
//
//    }
//
//    private void fetchImages() {
//
//        pDialog.setMessage("please wait...");
//        pDialog.show();
//
//        JsonArrayRequest req = new JsonArrayRequest(endpoint,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("hhh", response.toString());
//                        pDialog.hide();
//                        productThumbnailImageItems.clear();
//
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject object = response.getJSONObject(i);
//                                JSONObject url = object.getJSONObject("url");
//
//                                ProductThumbnailImageItem image = new ProductThumbnailImageItem(url.getString("small"), url.getString("medium"));
//
////                                image.setLarge(url.getString("large"));
////                                image.setTimestamp(object.getString("timestamp"));
//
//                                productThumbnailImageItems.add(image);
//
//                            } catch (JSONException e) {
//                                Log.e("hhh", "Json parsing error: " + e.getMessage());
//                            }
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("hhh", "Error: " + error.getMessage());
//                pDialog.hide();
//            }
//        });
//
//        // Adding request to request queue
//        GlobalContext.getInstance().addToRequestQueue(req);
//    }

}