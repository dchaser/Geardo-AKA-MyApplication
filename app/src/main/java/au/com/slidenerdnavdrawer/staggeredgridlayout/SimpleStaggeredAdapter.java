package au.com.slidenerdnavdrawer.staggeredgridlayout;

import android.content.Context;
import android.view.View;

import au.com.slidenerdnavdrawer.R;


public class SimpleStaggeredAdapter extends SimpleAdapter {

    public SimpleStaggeredAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        super.onBindViewHolder(itemHolder, position);

        final View itemView = itemHolder.itemView;
        if (position % 4 == 0) {
            int height = itemView.getContext().getResources()
                    .getDimensionPixelSize(R.dimen.card_min_width);
            itemView.setMinimumHeight(height);
        } else {
            itemView.setMinimumHeight(0);
        }
    }
}
