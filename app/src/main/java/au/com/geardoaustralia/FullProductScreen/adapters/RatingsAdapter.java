package au.com.geardoaustralia.FullProductScreen.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;


import java.text.DecimalFormat;
import java.util.ArrayList;

import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.CustomerReview;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.data.User;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.gallery.GalleryAdapter;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.utils.CommonConstants;

/**
 * Created by DasunPC on 1/10/17.
 */

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.RatingsViewHolder> {

//    public FullProductPage.ProductPageClickListener mItemClickListener;
    private ArrayList<CustomerReview> reviews;
    private ArrayList<User> users;
    private Context mContext;
    private final ImageLoader mImageLoader;

    public class RatingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView card_view;
        ImageView ivUserImage;
        TextView tvUserName;
        RatingBar userRating;
        TextView tvBoughtDaysAgo;
        TextView tvActualReview;


        public RatingsViewHolder(View itemView) {
            super(itemView);


            card_view = (CardView) itemView.findViewById(R.id.card_view);
            ivUserImage = (ImageView) itemView.findViewById(R.id.ivUserImage);
            userRating = (RatingBar) itemView.findViewById(R.id.userRating);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvBoughtDaysAgo = (TextView) itemView.findViewById(R.id.tvBoughtDaysAgo);
            tvActualReview = (TextView) itemView.findViewById(R.id.tvActualReview);
            card_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.card_view) {
//                if (mItemClickListener != null) {
//
//                    mItemClickListener.ratingsClicked(v, -1);
//                }

            }


//            if (v.getId() == R.id.ibFavorite) {
//                if (mItemClickListener != null) {
//
//                    mItemClickListener.makeFavorite(v, getAdapterPosition(), new Bundle());
//                }
//
//            }


        }


    }


    public RatingsAdapter(Context context, ArrayList<CustomerReview> reviews) {
        mContext = context;
        this.reviews = reviews;
        mImageLoader = new ImageLoader(mContext, R.drawable.logo_geardo);
    }

    @Override
    public RatingsAdapter.RatingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rating_item, parent, false);

        return new RatingsAdapter.RatingsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RatingsAdapter.RatingsViewHolder holder, int position) {

        CustomerReview review = reviews.get(position);


        if (TextUtils.isEmpty(review.user.imageUrlThumb)) {
            holder.ivUserImage.setImageResource(R.drawable.logo_geardo);
        } else {
            mImageLoader.loadAssetsImage(mContext, Uri.parse(CommonConstants.USER_PATH + review.user.imageUrlThumb), holder.ivUserImage);
        }

        holder.tvUserName.setText(review.user.display_name);

        double d = review.value;

        holder.userRating.setRating((float) d);
        holder.tvBoughtDaysAgo.setText("22 days ago");
        holder.tvActualReview.setText(review.rating);
    }


    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private GalleryAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final GalleryAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
