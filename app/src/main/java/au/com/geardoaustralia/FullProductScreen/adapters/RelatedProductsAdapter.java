package au.com.geardoaustralia.FullProductScreen.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.FullProductScreen.FullProductPageFragment;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.gallery.GalleryAdapter;
import au.com.geardoaustralia.gallery.Image;
import au.com.geardoaustralia.utils.CommonConstants;

/**
 * Created by DasunPC on 1/9/17.
 */

public class RelatedProductsAdapter extends RecyclerView.Adapter<RelatedProductsAdapter.RelatedProductViewHolder> {

    public FullProductPage.ProductPageClickListener mItemClickListener;
    private List<Product> relatedProducts;
    private Context mContext;
    private final ImageLoader mImageLoader;

    public class RelatedProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox cbIsInCart;

        ToggleButton ibFavorite;

        CardView card_view;

        ImageView ivProductImage;

        TextView tvProductTitle;

        TextView tvProductReducedPrice;

        TextView tvProductPrice;


        public RelatedProductViewHolder(View itemView) {
            super(itemView);

            cbIsInCart = (CheckBox) itemView.findViewById(R.id.cbIsInCart);
            ibFavorite = (ToggleButton) itemView.findViewById(R.id.ibFavorite);
            ibFavorite.setVisibility(View.GONE);

            card_view = (CardView) itemView.findViewById(R.id.card_view);
            ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);
            tvProductTitle = (TextView) itemView.findViewById(R.id.tvProductTitle);
            tvProductReducedPrice = (TextView) itemView.findViewById(R.id.tvProductReducedPrice);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
            card_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.card_view) {
                if (mItemClickListener != null) {

                    mItemClickListener.relatedClickedViewClicked(v, -1);
                }

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



    public RelatedProductsAdapter(Context context, ArrayList<Product> relatedOnes) {
        mContext = context;
        this.relatedProducts = relatedOnes;
        mImageLoader = new ImageLoader(mContext, R.drawable.logo_geardo);
    }

    @Override
    public RelatedProductsAdapter.RelatedProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        return new RelatedProductsAdapter.RelatedProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RelatedProductViewHolder holder, int position) {

        Product productItem = relatedProducts.get(position);


        holder.cbIsInCart.setChecked(productItem.isInCart);

        if (TextUtils.isEmpty(productItem.imageUrlOriginal)) {
            holder.ivProductImage.setImageResource(R.drawable.logo_geardo);
        } else {
            mImageLoader.loadAssetsImage(mContext, Uri.parse(CommonConstants.ROOT_PATH + productItem.imageUrlOriginal), holder.ivProductImage);
        }

        holder.tvProductTitle.setText(productItem.name);
        holder.tvProductReducedPrice.setText(productItem.price);
        holder.tvProductPrice.setText(productItem.price);

    }


    @Override
    public int getItemCount() {
        return relatedProducts.size();
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
