package au.com.geardoaustralia.utils;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import au.com.geardoaustralia.MyFragment;
import au.com.geardoaustralia.cartNew.data.Category;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.database.DatabaseManager;

import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGE;

/**
 * Created by DasunPC on 1/4/17.
 */

public class ProductProvider extends ContentProvider {


    ArrayList<Category> categories;
    ArrayList<Product> products;

    @Override
    public boolean onCreate() {


        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {

        String keyword = uri.getLastPathSegment().toLowerCase();

        DatabaseManager manager = DatabaseManager.getInstance();
        //products = manager.searchProductsByKeyword(keyword);
        products = manager.getAllProdcuts();

        MatrixCursor matrixCursor = new MatrixCursor(new String[]{
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
        });


        if (products != null) {

            int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));

            int length = products.size();

            for (int i = 0; i < length && matrixCursor.getCount() < limit; i++) {

                Product product = products.get(i);

                if(product.name.toLowerCase().contains(keyword)){
                    matrixCursor.addRow(new Object[]{i, product.name, i});

                }

//                if(product.searchTags != null) {
//
//                    List<String> items = Arrays.asList(product.searchTags.toLowerCase().split("\\s*,\\s*"));
//
//                    if (items.size() > 0) {
//
//
//                        if (items.contains(keyword)) {
//
//                        }
//                    }
//                }

            }

            return matrixCursor;
        }


        return null;
    }

    public boolean containsIgnoreCase(String str, ArrayList<String> list){
        for(String i : list){
            if(i.equalsIgnoreCase(str))
                return true;
        }
        return false;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


}
