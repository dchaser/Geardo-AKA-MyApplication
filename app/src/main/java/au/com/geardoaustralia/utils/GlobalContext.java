package au.com.geardoaustralia.utils;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;
import au.com.geardoaustralia.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by DasunPC on 11/8/16.
 */

public class GlobalContext extends Application {

    public static final String TAG = GlobalContext.class
            .getSimpleName();
    public static List<ProductInfoModel> data = new ArrayList<>();
    public static Basket myCart;

    //page number
    public int selectedPage = 0;

    private RequestQueue mRequestQueue;
    private static FirebaseAuth auth;
    private static GlobalContext mInstance;
    //Firebae database
    public FirebaseDatabase firebaseDatabase;
    //Firebase crash reporting instance
    public FirebaseAnalytics mFirebaseAnalytics;

    RealmConfiguration config = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);

        //config = new RealmConfiguration.Builder(getApplicationContext()).build();
        //Realm.setDefaultConfiguration(config);
        myCart = new Basket();
        selectedPage = 0;
    }


    public static synchronized GlobalContext getInstance() {
        return mInstance;
    }

    public static synchronized FirebaseAuth getFAuthInstance(){return auth;}

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    public static List<ProductInfoModel> makeTestDataSet() {

        for (int i = 0; i < 10; i++) {

            String[] titles = {"G-link HDMI Cable Gold edition v2.0", "Xiaomi 10000mAh Power Bank", "Battery 1160mAh for Gopro Hero 4", "HOCO Dual Car Charger UC204", "GoPro TriPod Float 7 in 1 Accessories Kit"};
            int[] pics = {R.drawable.glinkhdmiblackwire, R.drawable.xiaomi_power_bank, R.drawable.gopro_battery, R.drawable.dualcarcharger, R.drawable.suctioncup};
            String[] reducedPrices = {"$132.95", "$132.95", "$119.99", "$125.99", "$113.99"};
            String[] prices = {"$12.95", "$15.99", "$19.99", "$25.99", "$3.99"};
            String[] desc = {"HDMI 2.0, which is backwards compatible with earlier versions of the HDMI specification, is the most recent update of the HDMI specification. It also enables key enhancements to support market requirements for enhancing the consumer video and audio experience.\n" +
                    "\n", "Model NDY-02-AM\n" +
                    "Battery type Lithium-ion Polymer rechargeable cell\n" +
                    "Input Voltage DC 5.0V\n" +
                    "Output Voltage DC 5.1V\n" +
                    "Input current 2000mA(TYP)", "HDMI 2.0, which is backwards compatible with earlier versions of the HDMI specification, is the most recent update of the HDMI specification. It also enables key enhancements to support market requirements for enhancing the consumer video and audio experience.\n" +
                    "\n", "Auto Suction Cup+Tripod Mount \n" +
                    "•Not included the waterproof shell.\n" +
                    "•Compatible with: GoPro HD Hero 1/2/3/3+/4. \n" +
                    "•Dash/Windshield Suction mount for GoPro Camera.\n" +
                    "•180° rotation and adjustable arm for optimum positioning.", "100% New and High Quality!\n" +
                    "40 pcs Accessories Set for GoPro Hero 2 3 3+ 4\n" +
                    "Meet most of your needs for Supporting Accessories when you are Shooting with your GoPro Hero Camera. All in One !!"};

            ProductInfoModel current = new ProductInfoModel();

            current.Title = titles[i % titles.length];
            current.thumnailUrl = pics[i % pics.length];
            current.reducedPrice = reducedPrices[i % reducedPrices.length];
            current.price = prices[i % prices.length];
            current.descriptionl = desc[i % desc.length];

            data.add(current);
        }

        return data;
    }
}