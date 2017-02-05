package au.com.geardoaustralia.cartNew.data;

import java.util.ArrayList;

/**
 * Created by ashish (Min2) on 08/02/16.
 * <p/>
 * Avoiding internal setters/getters
 * http://developer.android.com/training/articles/perf-tips.html#GettersSetters
 */
public class Category {

    public int id;
    public int categoryId;
    public String imageUrlOriginal;
    public String imageUrlThumb;
    public String imageUrlSmall;
    public String imageUrlMedium;
    public String name;
    public String searchTags;
    public ArrayList<Subcategory> subCategoryList;

}
