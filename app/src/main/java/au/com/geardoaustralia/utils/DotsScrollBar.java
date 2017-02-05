package au.com.geardoaustralia.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import au.com.geardoaustralia.R;

/**
 * Created by DasunPC on 1/26/17.
 */

public class DotsScrollBar {

    RelativeLayout main_image_holder;

    public static void createDotScrollBar(Context context, LinearLayout llThreeDotsContainer, int selectedPage, int count) {
        for (int i = 0; i < count; i++) {
            ImageView dot = new ImageView(context);

            LinearLayout.LayoutParams vp =
                    new LinearLayout.LayoutParams(50 , LinearLayout.LayoutParams.WRAP_CONTENT);

            dot.setLayoutParams(vp);
            //dot.setPadding(10,0,0,10);

            if (i == selectedPage) {
                try {

                    dot.setImageResource(R.drawable.swipe_enabled);
                } catch (Exception e) {
                    Log.d("DotsScrollBar.java", "could not locate identifier");
                }
            } else {
                dot.setImageResource(R.drawable.swipe_disabled);
            }

            llThreeDotsContainer.addView(dot);
        }
        llThreeDotsContainer.invalidate();
    }
}