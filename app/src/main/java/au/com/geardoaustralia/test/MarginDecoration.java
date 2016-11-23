package au.com.geardoaustralia.test;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import au.com.geardoaustralia.R;

/**
 * Created by DasunPC on 11/20/16.
 */

public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public MarginDecoration(Context context) {
        margin = context.getResources().getDimensionPixelSize(R.dimen.card_insets);
    }

    @Override
    public void getItemOffsets(
            Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(margin, margin, margin, margin);
    }
}