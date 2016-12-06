package au.com.geardoaustralia.cart;

import android.os.Bundle;
import android.view.View;

/**
 * Created by DasunPC on 12/6/16.
 */
public interface SCClickListener {

    void itemRemoved(View v, int position, Bundle batton);

    void itemEdit(View v, int position, Bundle batton);
}