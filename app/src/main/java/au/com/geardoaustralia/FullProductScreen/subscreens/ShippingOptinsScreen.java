package au.com.geardoaustralia.FullProductScreen.subscreens;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.ShippingMethod;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.categories.CategorySelectionScreen;
import au.com.geardoaustralia.categories.SortAndRefinerScreen;
import au.com.geardoaustralia.utils.GlobalContext;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ShippingOptinsScreen extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.ivGoBack)
    ImageView ivGoBack;

    @Bind(R.id.rlShipToCountrySelect)
    RelativeLayout rlShipToCountrySelect;

    @Bind(R.id.tvShipToCountry)
    public TextView tvShipToCountry;

    @Bind(R.id.lvShippingOptions)
    ListView lvShippingOptions;
    static shippingOptionsAdapter adapter;
    ArrayList<ShippingMethod> shippingOptions = new ArrayList<>();

    String selectedCountry = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_optins_screen);
        ButterKnife.bind(ShippingOptinsScreen.this);


        if (GlobalContext.getInstance().selectedCountry == null) {

            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            String countryCodeValue = tm.getNetworkCountryIso();
            //get country name by ISO Code
            String name = DatabaseManager.getInstance().getCountryNameByISOCode(countryCodeValue.toUpperCase());
            GlobalContext.getInstance().selectedCountry = name;
            if (name != null) {
                selectedCountry = name;
                GlobalContext.getInstance().selectedCountry = name;
                tvShipToCountry.setText(name);
            }

            MakeShippingAdapter(name);


        }else{
            tvShipToCountry.setText(GlobalContext.getInstance().selectedCountry);
            MakeShippingAdapter(GlobalContext.getInstance().selectedCountry);
        }

        ivGoBack.setOnClickListener(this);
        rlShipToCountrySelect.setOnClickListener(this);
    }

    private void MakeShippingAdapter(String name) {

        shippingOptions = DatabaseManager.getInstance().getShippingMethodsPerCountryName(name);
        adapter = new shippingOptionsAdapter(shippingOptions, ShippingOptinsScreen.this);
        lvShippingOptions.setAdapter(adapter);
        lvShippingOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View clickedViewItem, int position, long l) {

                ShippingMethod selected = (ShippingMethod) lvShippingOptions.getAdapter().getItem(position);
                GlobalContext.getInstance().selectedCountry = selected.country;


            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.rlShipToCountrySelect:

                //start country sleect activity wiht a full list of countries
                startActivity(new Intent(ShippingOptinsScreen.this, SelectCountryScreen.class));
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.stay);

                break;

            case R.id.ivGoBack:
                onBackPressed();
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_to_left, R.anim.stay);
        finish();


    }

    @Override
    protected void onResume() {
        super.onResume();

       // evaluateShippingOptionsForCountry(GlobalContext.getInstance().selectedCountry);
    }



    class shippingOptionsAdapter extends BaseAdapter {

        ArrayList<ShippingMethod> shippingMethods;
        Context context;


        public shippingOptionsAdapter(ArrayList<ShippingMethod> shippingMethods, Context context) {

            this.shippingMethods = shippingMethods;
            this.context = context;

        }

        public int getCount() {
            return shippingMethods.size();
        }

        public Object getItem(int position) {
            return shippingMethods.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            final ShippingMethod shippingMethod = shippingMethods.get(position);

            LayoutInflater inflater = getLayoutInflater();

            ShippingMethodViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.shipping_single_method_row, null);

                // Create a ViewHolder and store references to the two children views
                holder = new ShippingMethodViewHolder();
                holder.tvShippingCost = (TextView) convertView.findViewById(R.id.tvShippingCost);
                holder.tvShippingCompany = (TextView) convertView.findViewById(R.id.tvShippingCompany);
                holder.tvShippingTime = (TextView) convertView.findViewById(R.id.tvShippingTime);
                holder.rbSelectOption = (RadioButton) convertView.findViewById(R.id.rbSelectOption);
                holder.rlMainParent = (RelativeLayout) convertView.findViewById(R.id.rlMainParent);

                // The tag can be any Object, this just happens to be the ViewHolder
                convertView.setTag(holder);
            } else {
                // Get the ViewHolder back to get fast access to the TextView
                // and the ImageView.
                holder = (ShippingMethodViewHolder) convertView.getTag();
            }

            // Bind that data efficiently!
            if(shippingMethod.shipping_cost.equals("Free Shipping")){
                holder.tvShippingCost.setText(shippingMethod.shipping_cost);

            }else{
                holder.tvShippingCost.setText("AU $ "+shippingMethod.shipping_cost);

            }

            holder.tvShippingCompany.setText(shippingMethod.company);
            holder.tvShippingTime.setText("Expected deliverywithin "+shippingMethod.shipping_time);

            holder.rlMainParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GlobalContext.getInstance().selectedShippingMethod = shippingMethod;
                    onBackPressed();
                }
            });

            return convertView;
        }
    }

    static class ShippingMethodViewHolder {
        TextView tvShippingCost;
        TextView tvShippingCompany;
        TextView tvShippingTime;
        RadioButton rbSelectOption;
        RelativeLayout rlMainParent;
    }
}
