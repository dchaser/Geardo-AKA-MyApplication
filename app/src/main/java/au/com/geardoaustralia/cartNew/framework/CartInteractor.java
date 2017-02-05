package au.com.geardoaustralia.cartNew.framework;

/**
 * Created by b_ashish on 23-Feb-16.
 */
public interface CartInteractor {

    void saveItemInCart(Object item, OnCartResponseListener listener);

    void removeItemFromCart(Object item, OnCartResponseListener listener);

    void removeAllItemsFromCart(OnCartResponseListener listener);

    void getTotalItemsInCart(OnCartResponseListener listener);

    void getCategoryItems(OnCartResponseListener listener, int categoryId);

    void countTotalItems(OnCartResponseListener listener);

}
