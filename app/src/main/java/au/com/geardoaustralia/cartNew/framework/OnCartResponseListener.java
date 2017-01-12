package au.com.geardoaustralia.cartNew.framework;

import java.util.List;

public interface OnCartResponseListener {

    void onSuccess();

    void onError();

    void onSuccessfulRemoved(Object item);

    void onDisplayItemsOnCart(List items);

    void onDisplayCategoryItems(List items);

    void onDisplayTotalItem(int size);

}
