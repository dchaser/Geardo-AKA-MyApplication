package au.com.geardoaustralia.utils;

import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.MainScreen.MainContentMainActivity.ProductInfoModel;

/**
 * Created by DasunPC on 11/28/16.
 */

public class Basket {

    public List<ProductInfoModel> items;

    public Basket(){

       items = new ArrayList<>();
    }
}
