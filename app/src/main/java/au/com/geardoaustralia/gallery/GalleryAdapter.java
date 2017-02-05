package au.com.geardoaustralia.gallery;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import au.com.geardoaustralia.FullProductScreen.FullProductPageFragment;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.utils.CommonConstants;

/**
 * Created by DasunPC on 11/8/16.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private List<Image> images;
    private Context mContext;
    ImageLoader mImageLoader;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }


    public GalleryAdapter(Context context, List<Image> images) {
        mContext = context;
        this.images = images;
        this.mImageLoader = new ImageLoader(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Image image = images.get(position );

        FullProductPageFragment.selectedGalleryPosition = position;

        if (TextUtils.isEmpty(image.thumb)) {
            holder.thumbnail.setImageResource(R.drawable.logo_geardo);
        } else {
            mImageLoader.loadAssetsImage(mContext, Uri.parse(CommonConstants.ROOT_PATH + image.thumb), holder.thumbnail);
        }

//        if (TextUtils.isEmpty(image.thumb)) {
//            holder.thumbnail.setImageResource(R.drawable.logo_geardo);
//        } else {
//            ImageLoader imageLoader = new ImageLoader(mContext);
//            imageLoader.loadAssetsImage(mContext,holder.thumbnail,);
//            Picasso.with(mContext).load(CommonConstants.ROOT_PATH+image.thumb).into(holder.thumbnail);
//            //mImageLoader.loadAssetsImage(mContext, Uri.parse(CommonConstants.ROOT_PATH + productItem.imageUrlOriginal), holder.ivProductImage);
//        }

//        Glide.with(mContext).load(image.getMedium())
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return images.size();
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