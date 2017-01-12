package au.com.geardoaustralia.deals;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import au.com.geardoaustralia.MainActivity;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.BaseActivity;
import au.com.geardoaustralia.utils.MenuBarHandler;

public class DealsScreen extends BaseActivity {

    MenuBarHandler menuBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_screen);

        menuBarHandler = new MenuBarHandler(DealsScreen.this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DealsScreen.this, MainActivity.class));
        finish();
    }
}
