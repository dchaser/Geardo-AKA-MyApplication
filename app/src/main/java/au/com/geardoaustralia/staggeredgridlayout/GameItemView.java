package au.com.geardoaustralia.staggeredgridlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageView;
import au.com.geardoaustralia.R;

/**
 * Created by DasunPC on 11/9/16.
 */

public class GameItemView extends GridLayout {

    private ImageView iv_logo_team_away, iv_logo_team_home;

    public GameItemView(Context context) {
        super(context);
    }

    public GameItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        iv_logo_team_home = (ImageView) findViewById(R.id.iv_logo_team_home);
    }


}
