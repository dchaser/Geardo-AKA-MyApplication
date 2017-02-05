package au.com.geardoaustralia.cartNew.framework;

/**
 * Created by b_ashish on 23-Feb-16.
 */
public interface CartPresenter {

    void onDestroy();

    void addItem(Object item);

    void removeItem(Object item);

    void removeAllItems();

    void showMeCategoryItems(int id);

    void showMeItemsInCart();

    void countCartItems();

}
