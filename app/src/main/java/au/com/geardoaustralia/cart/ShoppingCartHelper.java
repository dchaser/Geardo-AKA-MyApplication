package au.com.geardoaustralia.cart;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;
import au.com.geardoaustralia.R;
import au.com.geardoaustralia.utils.GlobalContext;

/**
 * Created by DasunPC on 12/6/16.
 */

public class ShoppingCartHelper {

    public static final String PRODUCT_INDEX = "PRODUCT_INDEX";

    private static List<ProductInfoModel> catalog;
    public static Map<ProductInfoModel, ShoppingCartEntry> cartMap = new HashMap<ProductInfoModel, ShoppingCartEntry>();

    public static List<ProductInfoModel> getCatalog(Resources res) {
        if (catalog == null) {
            catalog = new Vector<ProductInfoModel>();

            GlobalContext globalContext = GlobalContext.getInstance();
            List<ProductInfoModel> models = globalContext.makeTestDataSet();

            for (ProductInfoModel i : models) {
                catalog.add(i);
            }


        }

        return catalog;
    }

    public static void setQuantity(ProductInfoModel product, int quantity) {
        // Get the current cart entry
        ShoppingCartEntry curEntry = cartMap.get(product);

        // If the quantity is zero or less, remove the products
        if (quantity <= 0) {
            if (curEntry != null)
                removeProduct(product);
            return;
        }

        // If a current cart entry doesn't exist, create one
        if (curEntry == null) {
            curEntry = new ShoppingCartEntry(product, quantity);
            cartMap.put(product, curEntry);
            return;
        }

        // Update the quantity
        curEntry.setQuantity(quantity);
    }

    public static int getCartTotalCount(){

        int total = 0;

        for (ShoppingCartEntry list : cartMap.values()) {
            total += list.getQuantity();
        }

       return total;
    }

    public static int getProductQuantity(ProductInfoModel product) {
        // Get the current cart entry
        ShoppingCartEntry curEntry = cartMap.get(product);

        if (curEntry != null)
            return curEntry.getQuantity();

        return 0;
    }

    public static void removeProduct(ProductInfoModel product) {
        cartMap.remove(product);
    }

    public static List<ProductInfoModel> getCartList() {
        List<ProductInfoModel> cartList = new Vector<ProductInfoModel>(cartMap.keySet().size());
        for (ProductInfoModel p : cartMap.keySet()) {
            cartList.add(p);
        }

        return cartList;
    }

}

