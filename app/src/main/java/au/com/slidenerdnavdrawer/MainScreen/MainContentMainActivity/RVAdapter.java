package au.com.slidenerdnavdrawer.MainScreen.MainContentMainActivity;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.com.slidenerdnavdrawer.MainActivity;
import au.com.slidenerdnavdrawer.R;

/**
 * Created by DasunPC on 11/3/16.
 */

public  class RVAdapter extends RecyclerView.Adapter<RVAdapter.ProductViewHolder> {
    private List<infoModel> productList;
    private List<infoModel> filteredProductList;
    private MainActivity.ListenerOfClicks listenerOfClicks;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    // Provide a suitable constructor (depends on the kind of dataset)
    public RVAdapter(List<infoModel> myDataset) {
        this.productList = new ArrayList<>();
        this.productList = myDataset;
        this.filteredProductList =  new ArrayList<>();
    }

    @Override
    public RVAdapter.ProductViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ProductViewHolder vh = new ProductViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
//        holder.iv_image.setImageResource(mDataset.get(position).thumnailUrl);
//        holder.tv_text.setText(mDataset.get(position).price);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setClickListener(MainActivity.ListenerOfClicks listenerOfClicks){
        this.listenerOfClicks = listenerOfClicks;
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public CardView card_view;
        public  TextView tv_text;
        public ImageView iv_image;

        public ProductViewHolder(View itemView) {
            super(itemView);

            card_view = (CardView) itemView.findViewById(R.id.card_view);
            tv_text = (TextView) itemView.findViewById(R.id.tv_text);
            iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
            card_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            //deleteItem(getAdapterPosition());

            if(listenerOfClicks != null){

                listenerOfClicks.itemClicked(v,getAdapterPosition(),new Bundle());
            }

        }


    }

}

