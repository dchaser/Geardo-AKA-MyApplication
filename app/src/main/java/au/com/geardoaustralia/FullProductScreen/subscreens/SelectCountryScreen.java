package au.com.geardoaustralia.FullProductScreen.subscreens;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.FullProductScreen.FullProductPage;
import au.com.geardoaustralia.MyFragment;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.data.Country;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.utils.CommonConstants;
import au.com.geardoaustralia.utils.GlobalContext;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectCountryScreen extends AppCompatActivity {

    @Bind(R.id.rvCountryList)
    RecyclerView rvCountryList;

    @Bind(R.id.ivGoBack)
    ImageView ivGoBack;

    public static CountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country_screen);
        ButterKnife.bind(SelectCountryScreen.this);

        adapter = new CountryAdapter<Country>(SelectCountryScreen.this);
        adapter.setData(DatabaseManager.getInstance().getAllCountries());

        final LinearLayoutManager llm = new LinearLayoutManager(SelectCountryScreen.this, LinearLayoutManager.VERTICAL, false);
        rvCountryList.setLayoutManager(llm);
        rvCountryList.setHasFixedSize(true);
        rvCountryList.setAdapter(adapter);
        adapter.setClickListener(new clicked() {
            @Override
            public void countryClicked(View v, int position, Bundle batton) {
                //after country selected update shipping option screen and close this activity

                String country = adapter.getCountryName(position);
                GlobalContext.getInstance().selectedCountry = country;
                startActivity(new Intent(SelectCountryScreen.this, ShippingOptinsScreen.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    public interface clicked {

        void countryClicked(View v, int position, Bundle batton);

    }


    static class CountryAdapter<T> extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

        Context mContext;
        public clicked mItemClickListener;
        List<T> formControlMaterList = new ArrayList<T>();

        // Provide a suitable constructor (depends on the kind of dataset)
        public CountryAdapter(Context context) {
            this.mContext = context;
        }

        public void setData(List<T> data) {
            if (data != null) {
                formControlMaterList = (List<T>) data;
                this.notifyDataSetChanged();
            }
        }

        @Override
        public CountryAdapter.CountryViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
            View mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_row, parent, false);
            CountryAdapter.CountryViewHolder vh = new CountryAdapter.CountryViewHolder(mview);
            return vh;
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public void onBindViewHolder(CountryAdapter.CountryViewHolder holder, int position) {

            final Country country = (Country) formControlMaterList.get(position);

            if (country.name != null) {

                holder.tvCountryName.setText(country.nicename);
            }
        }

        @Override
        public int getItemCount() {
            return formControlMaterList.size();
        }

        public int getCountryId(int position) {
            Country country = (Country) formControlMaterList.get(position);
            return country.id;
        }

        public String getCountryName(int position) {
            Country country = (Country) formControlMaterList.get(position);
            return country.nicename;
        }

        void setClickListener(clicked listenerOfClicks) {
            this.mItemClickListener = listenerOfClicks;
        }


        public class CountryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView tvCountryName;

            public CountryViewHolder(View itemView) {
                super(itemView);

                tvCountryName = (TextView) itemView.findViewById(R.id.tvCountryName);
                tvCountryName.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.tvCountryName) {
                    if (mItemClickListener != null) {

                        mItemClickListener.countryClicked(v, getAdapterPosition(), new Bundle());
                    }
                }
            }


        }

    }

}
