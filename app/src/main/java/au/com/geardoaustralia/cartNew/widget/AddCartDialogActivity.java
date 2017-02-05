package au.com.geardoaustralia.cartNew.widget;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import au.com.geardoaustralia.R;
import au.com.geardoaustralia.cartNew.BaseActivity;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.util.ImageLoader;
import au.com.geardoaustralia.utils.CommonConstants;

import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGD;
import static au.com.geardoaustralia.cartNew.util.LogUtils.makeLogTag;


public class AddCartDialogActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = makeLogTag(AddCartDialogActivity.class);

    public static final String SELECTED_PRODUCT_ITEM = "com.crazymin2.retailstore.ui.SELECTED_PRODUCT_ITEM";

    public static final String CART_STATUS = "com.crazymin2.retailstore.ui.CART_STATUS";

    private Product selectedItem;
    private Button cartButt;

    public static final int REQUEST_TO_ADD_IN_CART = 1;
    private ImageLoader mImageLoader;

    enum MyCart {
        ADD_TO_CART(
                0) {
            @Override
            public String action() {
                return "Add to cart";
            }
        },
        IN_CART(
                1) {
            @Override
            public String action() {
                return "In Cart";
            }
        },
        UPDATE_CART(
                2) {
            @Override
            public String action() {
                return "Update";
            }
        };
        private int value;

        public abstract String action();

        private MyCart(int value) {
            this.value = value;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);
        mImageLoader = new ImageLoader(this, R.drawable.logo_geardo);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            selectedItem = extras.getParcelable(SELECTED_PRODUCT_ITEM);
        }

        setTitle("");

        cartButt = (Button) findViewById(R.id.cartButt);

        cartButt.setOnClickListener(this);

        ImageView btnClose = (ImageView) findViewById(R.id.btn_Close);
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCartDialogActivity.this.finish();
            }
        });

        TextView itemName = (TextView) findViewById(R.id.itemName);
        TextView itemPrice = (TextView) findViewById(R.id.itemPrice);
        if (selectedItem != null) {
            itemName.setText(selectedItem.name);

            String boldBit = "AU $ " + "<b>" + selectedItem.price + "</b>";
            itemPrice.setText(Html.fromHtml(boldBit));
            mImageLoader.loadAssetsImage(this, Uri.parse("file:///android_asset/product/" + selectedItem.imageUrlMedium), (ImageView) findViewById(R.id.productImage));
        }

        TextView tvSelection = (TextView) findViewById(R.id.tvSelection);
        TextView tvQuantity = (TextView) findViewById(R.id.tvQuantity);




        displayCartButtBehavior();
    }

    private void displayCartButtBehavior() {
        if (selectedItem.isInCart) {
            cartButt.setEnabled(false);
            cartButt.setTag(MyCart.IN_CART);
            cartButt.setText(MyCart.IN_CART.action());
        } else {
            cartButt.setEnabled(true);
            cartButt.setText(MyCart.ADD_TO_CART.action());
            cartButt.setTag(MyCart.ADD_TO_CART);
        }
    }

    private void saveProduct() {
        cartPresenter.addItem(selectedItem);
    }

    @Override
    public void onProductActionResponse(boolean isActionSuccessful) {
        super.onProductActionResponse(isActionSuccessful);
        if (isActionSuccessful) {
            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra(CART_STATUS, true);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "Error while inserting data", Toast.LENGTH_SHORT).show();
            LOGD(TAG, "Item not added into local database");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cartButt:
                MyCart mycart = (MyCart) cartButt.getTag();
                switch (mycart) {
                    case ADD_TO_CART:
                        LOGD(TAG, "Add to cart");
                        saveProduct();
                        break;
                    case IN_CART:
                        LOGD(TAG, "In cart");
                        break;
                    case UPDATE_CART:
                        LOGD(TAG, "Update cart");
                        break;
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        cartPresenter.onDestroy();
        super.onDestroy();
    }
}
