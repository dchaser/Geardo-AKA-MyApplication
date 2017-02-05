package au.com.geardoaustralia.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.utils.GlobalContext;

public class FirebaseTestActivity extends AppCompatActivity {

    public static List<ProductInfoModel> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_test);

        final LinearLayout layout = (LinearLayout) findViewById(R.id.linearMain);
        final Button btn = (Button) findViewById(R.id.second);

        final GlobalContext global = (GlobalContext.getInstance());
//        global.makeTestDataSet();
        data = global.data;


    }


}
