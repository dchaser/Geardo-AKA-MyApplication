package au.com.geardoaustralia.test;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import au.com.geardoaustralia.R;

/**
 * Created by DasunPC on 11/20/16.
 */

public class TextViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public TextViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.text);
    }
}