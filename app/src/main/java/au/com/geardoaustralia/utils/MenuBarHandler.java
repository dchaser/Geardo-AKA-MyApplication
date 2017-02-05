package au.com.geardoaustralia.utils;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.profile.ProfileScreen;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.CartActivity;
import au.com.geardoaustralia.categories.CategorySelectionScreen;
import au.com.geardoaustralia.deals.DealsScreen;

/**
 * Created by DasunPC on 12/28/16.
 */

public class MenuBarHandler {

    Context mContext;
    GlobalContext globalContext;

    //bottom bar controls
    private LinearLayout llCats;
    private LinearLayout llDeals;
    private LinearLayout llCart;
    private LinearLayout llAccount;
    private LinearLayout llHome;

    private ImageView ivHome;
    private ImageView ivCats;
    private ImageView ivDeals;
    private ImageView ivCart;
    private ImageView ivAccount;

    private TextView tvHome;
    private TextView tvCats;
    private TextView tvDeals;
    private TextView tvCart;
    private TextView tvAccount;

    public MenuBarHandler(Context context) {

        this.mContext = context;
        globalContext = GlobalContext.getInstance();

        setUpBottomBarControls(globalContext.selectedPage);
        clearAll(globalContext.selectedPage);

    }

    private void setUpBottomBarControls(int pageNumber) {

        //Home
        llHome = (LinearLayout) ((AppCompatActivity)mContext).findViewById(R.id.llHome);
        ivHome = (ImageView)((AppCompatActivity)mContext).findViewById(R.id.ivHome);
        tvHome = (TextView) ((AppCompatActivity)mContext).findViewById(R.id.tvHome);

        //Categories
        llCats = (LinearLayout) ((AppCompatActivity)mContext).findViewById(R.id.llCats);
        ivCats = (ImageView) ((AppCompatActivity)mContext).findViewById(R.id.ivCats);
        tvCats = (TextView) ((AppCompatActivity)mContext).findViewById(R.id.tvCats);
        //Deals
        llDeals = (LinearLayout) ((AppCompatActivity)mContext).findViewById(R.id.llDeals);
        ivDeals = (ImageView) ((AppCompatActivity)mContext).findViewById(R.id.ivDeals);
        tvDeals = (TextView) ((AppCompatActivity)mContext).findViewById(R.id.tvDeals);
        //Cart
        llCart = (LinearLayout) ((AppCompatActivity)mContext).findViewById(R.id.llCart);
        ivCart = (ImageView) ((AppCompatActivity)mContext).findViewById(R.id.ivCart);
        tvCart = (TextView) ((AppCompatActivity)mContext).findViewById(R.id.tvCart);
        //Account
        llAccount = (LinearLayout) ((AppCompatActivity)mContext).findViewById(R.id.llAccount);
        ivAccount = (ImageView) ((AppCompatActivity)mContext).findViewById(R.id.ivAccount);
        tvAccount = (TextView) ((AppCompatActivity)mContext).findViewById(R.id.tvAccount);


        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class to Zero : MainActivity
                if(globalContext.selectedPage != 0){
                    globalContext.selectedPage = 0;
                    (mContext).startActivity(new Intent(mContext, MainActivity.class));
                }


            }
        });


        llCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }


                //set page number in App class to One : CategorySelectionScreen
                if(globalContext.selectedPage != 1){
                    globalContext.selectedPage = 1;
                    (mContext).startActivity(new Intent(mContext, CategorySelectionScreen.class));
                }


                //clearAll(globalContext.selectedPage);
                //go to category screen
                //startActivity(new Intent(MainActivity.this, CategorySelectionScreen.class));

            }
        });


        llDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class to Three : DealsScreen
                if(globalContext.selectedPage != 2){
                    globalContext.selectedPage = 2;
                    (mContext).startActivity(new Intent(mContext, DealsScreen.class));
                }



                //clearAll(globalContext.selectedPage);

            }
        });


        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class to Four : CartActivity
                if(globalContext.selectedPage != 3){
                    globalContext.selectedPage = 3;
                    (mContext).startActivity(new Intent(mContext, CartActivity.class));
                }



            }
        });


        llAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (globalContext == null) {

                    globalContext = GlobalContext.getInstance();
                }

                //set page number in App class to Four : ProfileScreen
                if(globalContext.selectedPage != 4){
                    globalContext.selectedPage = 4;
                    (mContext).startActivity(new Intent(mContext, ProfileScreen.class));
                }



            }
        });

    }

    private void clearAll(int page) {

        switch (page) {



            case 0:
                //Home selected
                ivHome.setBackgroundResource(R.drawable.ic_home_blue);
                tvHome.setTextColor(mContext.getResources().getColor(R.color.lightBlue));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(mContext.getResources().getColor(R.color.gray));

                break;


            case 1:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Categories selected
                ivCats.setBackgroundResource(R.drawable.ic_categories_blue);
                tvCats.setTextColor(mContext.getResources().getColor(R.color.lightBlue));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(mContext.getResources().getColor(R.color.gray));
                break;
            case 2:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Deals selected
                ivDeals.setBackgroundResource(R.drawable.ic_search_blue);
                tvDeals.setTextColor(mContext.getResources().getColor(R.color.lightBlue));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(mContext.getResources().getColor(R.color.gray));
                break;
            case 3:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Cart Selected
                ivCart.setBackgroundResource(R.drawable.ic_cart_blue);
                tvCart.setTextColor(mContext.getResources().getColor(R.color.lightBlue));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(mContext.getResources().getColor(R.color.gray));
                break;
            case 4:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_grey);
                tvHome.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Account Selected
                ivAccount.setBackgroundResource(R.drawable.ic_account_blue);
                tvAccount.setTextColor(mContext.getResources().getColor(R.color.lightBlue));
                break;

            default:
                //Home
                ivHome.setBackgroundResource(R.drawable.ic_home_blue);
                tvHome.setTextColor(mContext.getResources().getColor(R.color.lightBlue));

                //Categories
                ivCats.setBackgroundResource(R.drawable.ic_categories_grey);
                tvCats.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Deals
                ivDeals.setBackgroundResource(R.drawable.ic_search_grey);
                tvDeals.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Cart
                ivCart.setBackgroundResource(R.drawable.ic_cart_grey);
                tvCart.setTextColor(mContext.getResources().getColor(R.color.gray));

                //Account
                ivAccount.setBackgroundResource(R.drawable.ic_account_grey);
                tvAccount.setTextColor(mContext.getResources().getColor(R.color.gray));
                break;
        }


    }
}
