package au.com.geardoaustralia.cart;

import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;

/**
 * Created by DasunPC on 12/6/16.
 */

public class ShoppingCartEntry  {

    private ProductInfoModel mProduct;
    private int mQuantity;

    public ShoppingCartEntry(ProductInfoModel product, int quantity) {
        mProduct = product;
        mQuantity = quantity;
    }

    public ProductInfoModel getProduct() {
        return mProduct;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

}
