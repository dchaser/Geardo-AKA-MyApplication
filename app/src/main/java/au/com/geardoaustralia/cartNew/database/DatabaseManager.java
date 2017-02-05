package au.com.geardoaustralia.cartNew.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.cartNew.data.BuyingOption;
import au.com.geardoaustralia.cartNew.data.Category;
import au.com.geardoaustralia.cartNew.data.Country;
import au.com.geardoaustralia.cartNew.data.CustomerReview;
import au.com.geardoaustralia.cartNew.data.Price;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.data.ProductStockOption;
import au.com.geardoaustralia.cartNew.data.ShippingMethod;
import au.com.geardoaustralia.cartNew.data.Subcategory;
import au.com.geardoaustralia.cartNew.data.User;
import au.com.geardoaustralia.categories.categoryModel;
import au.com.geardoaustralia.utils.DateUtil;
import au.com.geardoaustralia.utils.GlobalContext;
import au.com.geardoaustralia.utils.utilKit;

import static au.com.geardoaustralia.cartNew.util.LogUtils.LOGE;
import static au.com.geardoaustralia.cartNew.util.LogUtils.makeLogTag;

public class DatabaseManager {

    private static final String TAG = makeLogTag(DatabaseManager.class);
    private static DatabaseManager singleInstance;

    /**
     * Category Table which defines all the columns of this table
     */
    interface CATEGORY {
        public static String TABLE_NAME = "category";
        public static String _ID = "_id";
        public static String CATEGORY_ID = "category_id";
        public static String IMAGE_URL_ORIGINAL = "image_url_original";
        public static String IMAGE_URL_THUMB = "image_url_thumb";
        public static String IMAGE_URL_SMALL = "image_url_small";
        public static String IMAGE_URL_MEDIUM = "image_url_medium";
        public static String NAME = "name";
        public static String SEARCH_META_TAGS = "search_meta_tags";
    }

    /**
     * Sub Category Table which defines all the columns of this table
     */
    interface SUBCATEGORY {
        public static String TABLE_NAME = "subcategory";
        public static String _ID = "_id";
        public static String CATEGORY_ID = "category_id";
        public static String SUBCATEGORY_ID = "subcategory_id";
        public static String IMAGE_URL_ORIGINAL = "image_url_original";
        public static String IMAGE_URL_THUMB = "image_url_thumb";
        public static String IMAGE_URL_SMALL = "image_url_small";
        public static String IMAGE_URL_MEDIUM = "image_url_medium";
        public static String NAME = "name";
    }

    /**
     * Product Table which defines all the columns of this table
     */
    interface PRODUCT {

        public static String TABLE_NAME = "product";
        public static String _ID = "_id";
        public static String SUBCATEGORY_ID = "subcategory_id";
        public static String SECONDARY_SUBCATEGORY_ID = "secondary_subcat_id";
        public static String FREE_SHIPPING = "is_free_shipping";
        public static String MOBILE_EXCLUSIVE_DEAL = "is_mobile_exclusive";
        public static String TERNARY_SUBCATEGORY_ID = "ternary_subcat_id";
        public static String MERCHANT_ID = "merchant_id";
        public static String SHIPPING_ID = "shipping_id";
        public static String ITEM_CODE = "item_code";
        public static String THUMB_ONE = "product_thumb_one";
        public static String THUMB_TWO = "product_thumb_two";
        public static String THUMB_THREE = "product_thumb_three";
        public static String THUMB_FOUR = "product_thumb_four";
        public static String THUMB_FIVE = "product_thumb_five";
        public static String THUMB_SIX = "product_thumb_six";
        public static String THUMB_SEVEN = "product_thumb_seven";
        public static String THUMB_EIGHT = "product_thumb_eight";
        public static String THUMB_NINE = "product_thumb_nine";
        public static String THUMB_TEN = "product_thumb_ten";
        public static String THUMB_COUNT = "thumb_count";
        public static String MODEL_NUMBER = "model_number";
        public static String GROSS_WEIGHT = "gross_wight_package";
        public static String PACKAGGE_SIZE = "package_size";
        public static String IN_STOCK_QUANTITY = "in_stock_quantity";
        public static String IS_FAVORITE = "isFavorite";
        public static String SEASONAL_DISCOUNT = "seasonal_discount";
        public static String RECENTLY_VIEWED = "recently_viewed";
        public static String IS_IN_CART = "isInCart";
        public static String MIN_ORDER_AMOUNT = "min_order";
        public static String PRODUCT_ID = "product_id";
        public static String PRICE = "price";
        public static String REDUCED_PRICE = "reduced_price";
        public static String DESCRIPTION = "description";
        public static String IMAGE_URL_ORIGINAL = "image_url_original";
        public static String IMAGE_URL_SMALL = "image_url_small";
        public static String IMAGE_URL_MEDIUM = "image_url_medium";
        public static String NAME = "name";
        public static String RELATED_PRODUCTS = "related";
        public static String RATINGS = "ratings";
    }

    interface BUYING_OPTIONS {

        public static String TABLE_NAME = "product_buying_options";
        public static String _ID = "_id";
        public static String PRODUCT_ID = "product_id";
        public static String OPTION_NAME = "option_name";
        public static String IMAGE_NAME = "image_name";
    }

    interface STOCK_OPTIONS {

        public static String TABLE_NAME = "product_stock_options";
        public static String _ID = "_id";
        public static String BUYING_OPTION_ID = "buying_option_id";
        public static String PIECE_COLOR = "piece_color";
        public static String PIECE_TYPE = "piece_type";
        public static String PIECE_COMPATIBLE_WITH = "compatible_with";
        public static String PIECE_COMPATIBLE_MODEL = "compatible_model";
        public static String RETAIL_PACKAGE_INCLUDED = "retail_package_included";
        public static String FEATURES = "features";
        public static String MATERIAL = "material";
        public static String MIN_SALE_QUANTITY = "min_sale_qty";
        public static String REMAINING_QUANTITY = "remaining_qty";

    }

    interface PRICES {

        public static String TABLE_NAME = "prices";
        public static String _ID = "_id";
        public static String STOCK_OPTION_ID = "stock_option_id";
        public static String SINGLE_ITEM_PRICE = "single_item_price";
        public static String ONE = "one";
        public static String TWO = "two";
        public static String THREE = "three";
        public static String FOUR = "four";
        public static String FIVE = "five";
        public static String SIX = "six";
        public static String SEVEN = "seven";
        public static String EIGHT = "eight";
        public static String NINE = "nine";
        public static String TEN = "ten";
        public static String ELEVEN = "eleven";
        public static String TWELVE = "twelve";
        public static String FIFTEEN = "fifteen";
        public static String TWENTY = "twenty";
        public static String THIRTY = "thirty";
        public static String FORTY = "forty";
        public static String FIFTY = "fifty";
        public static String HUNDERED = "hundred";
        public static String HUNDERED_FIFTY = "hundred_fifty";
        public static String TWO_HUNDERED = "two_hundred";
        public static String FIVE_HUNDERED = "five_hundred";
        public static String SEVEN_HUNDERED = "seven_hundred";
        public static String THOUSAND = "thousand";
        public static String TWO_THOUSAND = "two_thousand";
        public static String FIVE_THOUSAND = "five_thousand";

    }

    /**
     * Cart Table which defines all the columns of this table
     */
    interface CART_PRODUCT {
        public static String TABLE_NAME = "cart_product";
        public static String _ID = "_id";
        public static String CATEGORY_ID = "category_id";
        public static String PRODUCT_ID = "product_id";
        public static String TOTAL_PRICE = "total_price";
        public static String QUANTITY = "quantity";
    }

    /**
     * Rating Table which defines all the columns of this table
     */
    interface RATINGS {
        public static String TABLE_NAME = "ratings";
        public static String _ID = "_id";
        public static String RATINGVALUE = "value";
        public static String RATINGTEXT = "rating";
        public static String CUSTOMER_ID = "customer_id";
        public static String PRODUCT_ID = "product_id";
        public static String TIMESTAMP = "timestamp";
    }

    /**
     * Users Table which defines all the columns of this table
     */
    interface USERS {
        public static String TABLE_NAME = "users";
        public static String _ID = "_id";
        public static String IMAGE_URL_THUMB = "image_url_thumbnail";
        public static String IMAGE_URL_SMALL = "image_url_small";
        public static String FIRSTNAME = "firstname";
        public static String EMAIL = "email";
        public static String CREATED_AT = "created_at";
        public static String oauth_provider = "oauth_provider";
        public static String oauth_uid = "oauth_uid";
        public static String username = "username";
        public static String password = "password";
        public static String etc = "etc";
    }

    interface SHIPPING {

        public static String TABLE_NAME = "shipping";
        public static String _ID = "_id";
        public static String SHIPPNG_COUNTRY = "country";
        public static String COMPANY = "company";
        public static String SHIPPING_ORIGIN = "shipping_origin";
        public static String TRACKING = "tracking";
        public static String SHIPPING_TIME = "shipping_time";
        public static String SHIPPING_COST = "shipping_cost";
    }

    interface COUNTRY {
        public static String TABLE_NAME = "country";
        public static String ID = "id";
        public static String ISO = "iso";
        public static String NAME = "name";
        public static String NICENAME = "nicename";
        public static String ISO3 = "iso3";
        public static String NUMCODE = "numcode";
        public static String PHONECODE = "phonecode";
    }

    /**
     * Singleton object of the class in order to access the database assets and resources.
     */
    public static DatabaseManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new DatabaseManager();
        }
        return singleInstance;
    }

    /**
     * get Column Index value of the database column
     *
     * @return int
     */
    private int getColumnIndex(Cursor cursor, String columnName) {
        return cursor.getColumnIndex(columnName);
    }

    /**
     * get String value of the database column
     *
     * @return String
     */
    private String getStringValue(Cursor cursor, String columnName) {
        return cursor.getString(getColumnIndex(cursor, columnName));
    }

    /**
     * get long value of the database column
     *
     * @return String
     */
    private long getLongValue(Cursor cursor, String columnName) {
        return cursor.getLong(getColumnIndex(cursor, columnName));
    }

    /**
     * get Integer value of the database column
     *
     * @return int
     */
    private int getIntValue(Cursor cursor, String columnName) {
        try {
            return cursor.getInt(getColumnIndex(cursor, columnName));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * get long value of the database column
     *
     * @return String
     */
    private double getDoubleValue(Cursor cursor, String columnName) {
        return cursor.getDouble(getColumnIndex(cursor, columnName));
    }

    /*************
     * User Table Queries Start
     *************/

    public ArrayList<User> getAllUsers() {
        SQLiteDatabase database = null;
        ArrayList<User> users = new ArrayList<>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "SELECT * FROM users";
            cursor = database.rawQuery(MY_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.id = getIntValue(cursor, RATINGS._ID);
                    user.imageUrlThumb = getStringValue(cursor, USERS.IMAGE_URL_THUMB);
                    user.imageUrlSmall = getStringValue(cursor, USERS.IMAGE_URL_SMALL);
                    user.display_name = getStringValue(cursor, USERS.FIRSTNAME);
                    user.email = getStringValue(cursor, USERS.EMAIL);
                    user.created_at = getStringValue(cursor, USERS.CREATED_AT);
                    user.oauth_provider = getStringValue(cursor, USERS.oauth_provider);
                    user.oauth_uid = getStringValue(cursor, USERS.oauth_uid);
                    user.username = getStringValue(cursor, USERS.username);
                    user.password = getStringValue(cursor, USERS.password);
                    user.etc = getStringValue(cursor, USERS.etc);


                    users.add(user);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return users;
    }

    public User getUserByUserId(int userId) {

        User user = new User();
        user.id = -1;
        SQLiteDatabase database = null;
        CustomerReview review = new CustomerReview();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = USERS._ID + " = ?";
            String[] whereArgs = {"" + userId};
            cursor = database.query(USERS.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {

                user.id = getIntValue(cursor, RATINGS._ID);
                user.imageUrlThumb = getStringValue(cursor, USERS.IMAGE_URL_THUMB);
                user.imageUrlSmall = getStringValue(cursor, USERS.IMAGE_URL_SMALL);
                user.display_name = getStringValue(cursor, USERS.FIRSTNAME);
                user.email = getStringValue(cursor, USERS.EMAIL);
                user.created_at = getStringValue(cursor, USERS.CREATED_AT);
                user.oauth_provider = getStringValue(cursor, USERS.oauth_provider);
                user.oauth_uid = getStringValue(cursor, USERS.oauth_uid);
                user.username = getStringValue(cursor, USERS.username);
                user.password = getStringValue(cursor, USERS.password);
                user.etc = getStringValue(cursor, USERS.etc);


            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return user;
    }

    public boolean saveUserToDB(User user, Context context) {

        //save a user to the DB with the minimal set of information we have

        SQLiteDatabase database = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.openDataBase();

            ContentValues contentValues = new ContentValues();

            contentValues.put(USERS.CREATED_AT, DateUtil.formatDateTime(context, DateUtil.currentDate()));
            contentValues.put(USERS.EMAIL, user.email);
            contentValues.put(USERS.etc, user.etc);
            contentValues.put(USERS.FIRSTNAME, user.display_name);
            contentValues.put(USERS.IMAGE_URL_SMALL, user.imageUrlSmall);
            contentValues.put(USERS.IMAGE_URL_THUMB, user.imageUrlThumb);
            contentValues.put(USERS.oauth_provider, user.oauth_provider);
            contentValues.put(USERS.oauth_uid, user.oauth_uid);
            contentValues.put(USERS.username, user.username);
            contentValues.put(USERS.password, user.username);

            long userId = database.insert(USERS.TABLE_NAME, null, contentValues);
            if (userId == -1) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOGE(TAG, "Exception in saving user in database ", e);
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return false;
    }

    /*************User Table Queries End*************/
    /*************
     * Ratings Table Queries Start
     *************/

    public ArrayList<CustomerReview> getAllCustomerReviews() {
        SQLiteDatabase database = null;
        ArrayList<CustomerReview> reviews = new ArrayList<>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "SELECT * FROM ratings where ";
            cursor = database.rawQuery(MY_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    CustomerReview review = new CustomerReview();
                    review.id = getIntValue(cursor, RATINGS._ID);
                    review.value = getDoubleValue(cursor, RATINGS.RATINGVALUE);
                    review.rating = getStringValue(cursor, RATINGS.RATINGTEXT);
                    review.customer_id = getIntValue(cursor, RATINGS.CUSTOMER_ID);
                    review.product_id = getIntValue(cursor, RATINGS.PRODUCT_ID);
                    review.created_at = getStringValue(cursor, RATINGS.TIMESTAMP);

                    reviews.add(review);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return FillUserDetailsInGivenReviews(reviews);
    }

    public ArrayList<CustomerReview> FillUserDetailsInGivenReviews(ArrayList<CustomerReview> customerReviews) {

        CustomerReview customerReview = new CustomerReview();
        ArrayList<CustomerReview> reviews = customerReviews;

        for (int i = 0; i < customerReviews.size(); i++) {

            customerReview = customerReviews.get(i);
            SQLiteDatabase database = null;
            Cursor cursor = null;

            try {
                String whereClause = USERS._ID + " = ?";
                String[] whereArgs = {"" + customerReview.customer_id};
                cursor = database.query(USERS.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
                if (cursor.moveToFirst()) {

                    do {
                        User user = new User();
                        user.id = getIntValue(cursor, USERS._ID);
                        user.imageUrlThumb = getStringValue(cursor, USERS.IMAGE_URL_THUMB);
                        user.imageUrlSmall = getStringValue(cursor, USERS.IMAGE_URL_SMALL);
                        user.display_name = getStringValue(cursor, USERS.FIRSTNAME);
                        user.email = getStringValue(cursor, USERS.EMAIL);
                        user.created_at = getStringValue(cursor, USERS.CREATED_AT);
                        user.oauth_provider = getStringValue(cursor, USERS.oauth_provider);
                        user.oauth_uid = getStringValue(cursor, USERS.oauth_uid);
                        user.username = getStringValue(cursor, USERS.username);
                        user.password = getStringValue(cursor, USERS.password);
                        user.etc = getStringValue(cursor, USERS.etc);


                        customerReview.user = user;

                    } while (cursor.moveToNext());
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (database != null) {
                    database.close();
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }


        return reviews;

    }

    public ArrayList<CustomerReview> getAllReviewsForProductId(int productId) {

        SQLiteDatabase database = null;
        ArrayList<CustomerReview> reviews = new ArrayList<>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = RATINGS.PRODUCT_ID + " = ?";
            String[] whereArgs = {"" + productId};
            //cursor = database.query(RATINGS.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            cursor = database.rawQuery("select * from ratings , users  where  ratings.customer_id = users._id and ratings.product_id = " + productId, null);


            if (cursor.moveToFirst()) {
                do {

                    CustomerReview review = new CustomerReview();

                    //get review info
                    review.id = getIntValue(cursor, RATINGS._ID);
                    review.rating = getStringValue(cursor, RATINGS.RATINGTEXT);
                    review.created_at = getStringValue(cursor, RATINGS.TIMESTAMP);
                    review.value = getDoubleValue(cursor, RATINGS.RATINGVALUE);
                    review.customer_id = getIntValue(cursor, RATINGS.CUSTOMER_ID);
                    review.product_id = getIntValue(cursor, RATINGS.PRODUCT_ID);

                    //get user info to nexted object user
                    User user = new User();
                    user.id = getIntValue(cursor, USERS._ID);
                    user.imageUrlThumb = getStringValue(cursor, USERS.IMAGE_URL_THUMB);
                    user.imageUrlSmall = getStringValue(cursor, USERS.IMAGE_URL_SMALL);
                    user.display_name = getStringValue(cursor, USERS.FIRSTNAME);
                    user.email = getStringValue(cursor, USERS.EMAIL);
                    user.created_at = getStringValue(cursor, USERS.CREATED_AT);
                    user.oauth_provider = getStringValue(cursor, USERS.oauth_provider);
                    user.oauth_uid = getStringValue(cursor, USERS.oauth_uid);
                    user.username = getStringValue(cursor, USERS.username);
                    user.password = getStringValue(cursor, USERS.password);
                    user.etc = getStringValue(cursor, USERS.etc);


                    review.user = user;
                    reviews.add(review);
                } while (cursor.moveToNext());
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }


        }
        return reviews;
    }

    /*************Ratings Table Queries End*************/
    /*************
     * Category Table Queries Start
     *************/


    /* provides categories by keyword search tags*/
    public ArrayList<Category> searchCategoriesByKeyword(String keyword) {
        //should search a result set of categories containing the keyword
        SQLiteDatabase database = null;
        ArrayList<Category> categories = new ArrayList<Category>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "select * from " + CATEGORY.TABLE_NAME + " WHERE " + CATEGORY.SEARCH_META_TAGS + " LIKE '%kk%' ";
            cursor = database.rawQuery(MY_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    Category category = new Category();

                    category.id = getIntValue(cursor, CATEGORY._ID);
                    category.categoryId = getIntValue(cursor, CATEGORY.CATEGORY_ID);
                    category.imageUrlOriginal = getStringValue(cursor, CATEGORY.IMAGE_URL_ORIGINAL);
                    category.imageUrlThumb = getStringValue(cursor, CATEGORY.IMAGE_URL_THUMB);
                    category.imageUrlSmall = getStringValue(cursor, CATEGORY.IMAGE_URL_SMALL);
                    category.imageUrlMedium = getStringValue(cursor, CATEGORY.IMAGE_URL_MEDIUM);
                    category.name = getStringValue(cursor, CATEGORY.NAME);
                    category.searchTags = getStringValue(cursor, CATEGORY.SEARCH_META_TAGS);

                    categories.add(category);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return categories;
    }

    public Category getCategoryById(int categoryId) {
        SQLiteDatabase database = null;
        Cursor cursor = null;

        Category category = null;
        try {

            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = CATEGORY.CATEGORY_ID + " = ?";
            String[] whereArgs = {"" + categoryId};
            cursor = database.query(CATEGORY.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            category = new Category();


            if (cursor.moveToFirst()) {
                do {
                    category = new Category();
                    category.id = getIntValue(cursor, CATEGORY._ID);
                    category.categoryId = getIntValue(cursor, CATEGORY.CATEGORY_ID);
                    category.imageUrlOriginal = getStringValue(cursor, CATEGORY.IMAGE_URL_ORIGINAL);
                    category.imageUrlThumb = getStringValue(cursor, CATEGORY.IMAGE_URL_THUMB);
                    category.imageUrlSmall = getStringValue(cursor, CATEGORY.IMAGE_URL_SMALL);
                    category.imageUrlMedium = getStringValue(cursor, CATEGORY.IMAGE_URL_MEDIUM);
                    category.name = getStringValue(cursor, CATEGORY.NAME);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return category;
    }

    public ArrayList<Category> getCategories() {
        SQLiteDatabase database = null;
        ArrayList<Category> categories = new ArrayList<Category>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            cursor = database.query(CATEGORY.TABLE_NAME, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.id = getIntValue(cursor, CATEGORY._ID);
                    category.categoryId = getIntValue(cursor, CATEGORY.CATEGORY_ID);
                    category.imageUrlOriginal = getStringValue(cursor, CATEGORY.IMAGE_URL_ORIGINAL);
                    category.imageUrlThumb = getStringValue(cursor, CATEGORY.IMAGE_URL_THUMB);
                    category.imageUrlSmall = getStringValue(cursor, CATEGORY.IMAGE_URL_SMALL);
                    category.imageUrlMedium = getStringValue(cursor, CATEGORY.IMAGE_URL_MEDIUM);
                    category.name = getStringValue(cursor, CATEGORY.NAME);

                    categories.add(category);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return categories;
    }

    public ArrayList<categoryModel> getAllCategories() {

        SQLiteDatabase database = null;
        ArrayList<categoryModel> categoryModels = new ArrayList<>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "SELECT * FROM category";
            cursor = database.rawQuery(MY_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    categoryModel categoryModel = new categoryModel();

                    categoryModel.Title = getStringValue(cursor, CATEGORY.NAME);
                    categoryModel.thumnailUrl = getStringValue(cursor, CATEGORY.IMAGE_URL_THUMB);


                    categoryModels.add(categoryModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return categoryModels;
    }

    /*************Category Table Queries End*************/
    /*************
     * SubCategory Table Queries Start
     *************/

     /* get Sub Category by its name */
    public Subcategory getSubcategoryByName(String name) {

        //should search a result set of categories containing the keyword
        SQLiteDatabase database = null;
        Subcategory subcategory = null;
        Cursor cursor = null;
        try {

            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = SUBCATEGORY.NAME + " = ?";
            String[] whereArgs = {"" + name};
            cursor = database.query(SUBCATEGORY.TABLE_NAME, null, whereClause, whereArgs, null, null, null);

            if (cursor.moveToFirst()) {
                subcategory = new Subcategory();

                subcategory._id = getIntValue(cursor, SUBCATEGORY._ID);
                subcategory.category_id = getIntValue(cursor, SUBCATEGORY.CATEGORY_ID);
                subcategory.subcategory_id = getIntValue(cursor, SUBCATEGORY.SUBCATEGORY_ID);
                subcategory.name = getStringValue(cursor, SUBCATEGORY.NAME);
                subcategory.image_url_original = getStringValue(cursor, SUBCATEGORY.IMAGE_URL_ORIGINAL);
                subcategory.image_url_thumb = getStringValue(cursor, SUBCATEGORY.IMAGE_URL_THUMB);
                subcategory.image_url_small = getStringValue(cursor, SUBCATEGORY.IMAGE_URL_SMALL);
                subcategory.image_url_medium = getStringValue(cursor, SUBCATEGORY.IMAGE_URL_MEDIUM);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return subcategory;
    }

    /* get Sub Category names as a String Collection */
    public ArrayList<String> getAllSubCategoryNamesByCategory(int categoryId) {
        SQLiteDatabase database = null;
        ArrayList<String> category_names = new ArrayList<>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = SUBCATEGORY.CATEGORY_ID + " = ?";
            String[] whereArgs = {"" + categoryId};
            cursor = database.query(SUBCATEGORY.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            while (cursor.moveToNext()) {

                String category_name = getStringValue(cursor, SUBCATEGORY.NAME);

                category_names.add(category_name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return category_names;
    }

    /* get all Sub Categories from  */
    public ArrayList<Subcategory> getAllSubCategoriesPerCategory(int categoryId) {
        SQLiteDatabase database = null;
        ArrayList<Subcategory> sub_category_names = new ArrayList<>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = SUBCATEGORY.CATEGORY_ID + " = ?";
            String[] whereArgs = {"" + categoryId};
            cursor = database.query(SUBCATEGORY.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            while (cursor.moveToNext()) {

                String category_name = getStringValue(cursor, SUBCATEGORY.NAME);

                Subcategory subcategory = new Subcategory();

                subcategory._id = getIntValue(cursor, SUBCATEGORY._ID);
                subcategory.category_id = getIntValue(cursor, SUBCATEGORY.CATEGORY_ID);
                subcategory.subcategory_id = getIntValue(cursor, SUBCATEGORY.SUBCATEGORY_ID);
                subcategory.image_url_original = getStringValue(cursor, SUBCATEGORY.IMAGE_URL_ORIGINAL);
                subcategory.image_url_thumb = getStringValue(cursor, SUBCATEGORY.IMAGE_URL_THUMB);
                subcategory.image_url_small = getStringValue(cursor, SUBCATEGORY.IMAGE_URL_SMALL);
                subcategory.image_url_medium = getStringValue(cursor, SUBCATEGORY.IMAGE_URL_MEDIUM);
                subcategory.name = getStringValue(cursor, SUBCATEGORY.NAME);


                sub_category_names.add(subcategory);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return sub_category_names;
    }


    /*************SubCategory Table Queries End*************/
    /*************
     * Cart Product Table Queries Start
     *************/

    /*Delete item from Cart Product table*/
    public boolean deleteCartProduct(Product myCartProduct) {

        SQLiteDatabase database = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.openDataBase();
            String whereClause = CART_PRODUCT.PRODUCT_ID + " = ?";
            String[] whereArgs = {"" + myCartProduct.productId};

            int deleted = database.delete(CART_PRODUCT.TABLE_NAME, whereClause, whereArgs);

            if (deleted == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOGE(TAG, "Exception while deleting cart product ", e);
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return false;
    }

    public boolean saveCartProduct(Product myCartProduct) {

        SQLiteDatabase database = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.openDataBase();

            ContentValues contentValues = new ContentValues();

            contentValues.put(CART_PRODUCT.CATEGORY_ID, myCartProduct.subcategoryId);
            contentValues.put(CART_PRODUCT.PRODUCT_ID, myCartProduct.productId);
            contentValues.put(CART_PRODUCT.TOTAL_PRICE, myCartProduct.totalPrice);
            contentValues.put(CART_PRODUCT.QUANTITY, myCartProduct.quantity);

            long productId = database.insert(CART_PRODUCT.TABLE_NAME, null, contentValues);
            if (productId == -1) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOGE(TAG, "Exception in saving product in cart ", e);
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return false;
    }

    public int getProductCount() {

        SQLiteDatabase database = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            Cursor cursor = database.query(CART_PRODUCT.TABLE_NAME, null, null, null, null, null, null);
            return cursor.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return 0;

    }

    public ArrayList<Product> getCartProducts() {
        SQLiteDatabase database = null;
        ArrayList<Product> products = new ArrayList<Product>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "SELECT * FROM cart_product cp, product p WHERE  cp.product_id = p.product_id";
            cursor = database.rawQuery(MY_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.id = getIntValue(cursor, PRODUCT._ID);
                    product.subcategoryId = getIntValue(cursor, PRODUCT.SUBCATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlSmall = getStringValue(cursor, PRODUCT.IMAGE_URL_SMALL);
                    product.imageUrlMedium = getStringValue(cursor, PRODUCT.IMAGE_URL_MEDIUM);
                    product.name = getStringValue(cursor, PRODUCT.NAME);
                    product.price = getStringValue(cursor, PRODUCT.PRICE);
                    product.reducedPrice = getStringValue(cursor, PRODUCT.REDUCED_PRICE);
                    product.description = getStringValue(cursor, PRODUCT.DESCRIPTION);
                    product.relatedProductIDs = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RELATED_PRODUCTS));
                    product.relatedRatings = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RATINGS));

                    products.add(product);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return products;
    }

    public boolean removeAllCartProducts() {
        SQLiteDatabase database = null;
        ArrayList<Product> products = new ArrayList<Product>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "TRUNCATE TABLE cart_product";
            cursor = database.rawQuery(MY_QUERY, null);

          return true;

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return false;
    }


    /************Cart Product Table Queries End*************/
    /*************
     * Product Table Queries Start
     *************/

    public ArrayList<Product> getProductsBySubCategoryName(String categoryId) {
        SQLiteDatabase database = null;
        ArrayList<Product> products = new ArrayList<Product>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = PRODUCT.SUBCATEGORY_ID + " = ?";
            String[] whereArgs = {"" + categoryId};
            cursor = database.query(PRODUCT.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();

                    product.id = getIntValue(cursor, PRODUCT._ID);
                    product.subcategoryId = getIntValue(cursor, PRODUCT.SUBCATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlSmall = getStringValue(cursor, PRODUCT.IMAGE_URL_SMALL);
                    product.imageUrlMedium = getStringValue(cursor, PRODUCT.IMAGE_URL_MEDIUM);
                    product.name = getStringValue(cursor, PRODUCT.NAME);
                    product.price = getStringValue(cursor, PRODUCT.PRICE);
                    product.reducedPrice = getStringValue(cursor, PRODUCT.REDUCED_PRICE);
                    product.description = getStringValue(cursor, PRODUCT.DESCRIPTION);
                    product.relatedProductIDs = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RELATED_PRODUCTS));
                    product.relatedRatings = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RATINGS));


                    products.add(product);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return products;
    }


    /* provides products by keyword search tags*/
    public ArrayList<Product> searchProductsByKeyword(String keyword) {
        //should search a result set of products containing the keyword
        SQLiteDatabase database = null;
        ArrayList<Product> products = new ArrayList<Product>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "select * from " + PRODUCT.TABLE_NAME + " WHERE LOWER(" + PRODUCT.NAME + ") LIKE LOWER('%" + keyword + "%')";
            cursor = database.rawQuery(MY_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.id = getIntValue(cursor, PRODUCT._ID);
                    product.subcategoryId = getIntValue(cursor, PRODUCT.SUBCATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlSmall = getStringValue(cursor, PRODUCT.IMAGE_URL_SMALL);
                    product.imageUrlMedium = getStringValue(cursor, PRODUCT.IMAGE_URL_MEDIUM);
                    product.name = getStringValue(cursor, PRODUCT.NAME);
                    product.price = getStringValue(cursor, PRODUCT.PRICE);
                    product.reducedPrice = getStringValue(cursor, PRODUCT.REDUCED_PRICE);
                    product.description = getStringValue(cursor, PRODUCT.DESCRIPTION);
                    product.relatedProductIDs = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RELATED_PRODUCTS));
                    product.relatedRatings = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RATINGS));
                    product.secondary_subcat_id = getIntValue(cursor, PRODUCT.SECONDARY_SUBCATEGORY_ID);
                    product.ternary_subcat_id = getIntValue(cursor, PRODUCT.TERNARY_SUBCATEGORY_ID);
                    product.merchant_id = getIntValue(cursor, PRODUCT.MERCHANT_ID);
                    product.shipping_id = getStringValue(cursor, PRODUCT.SHIPPING_ID);
                    product.item_code = getStringValue(cursor, PRODUCT.ITEM_CODE);
                    product.product_thumb_one = getStringValue(cursor, PRODUCT.THUMB_ONE);
                    product.product_thumb_two = getStringValue(cursor, PRODUCT.THUMB_TWO);
                    product.product_thumb_three = getStringValue(cursor, PRODUCT.THUMB_THREE);
                    product.product_thumb_four = getStringValue(cursor, PRODUCT.THUMB_FOUR);
                    product.product_thumb_five = getStringValue(cursor, PRODUCT.THUMB_FIVE);
                    product.product_thumb_six = getStringValue(cursor, PRODUCT.THUMB_SIX);
                    product.product_thumb_seven = getStringValue(cursor, PRODUCT.THUMB_SEVEN);
                    product.product_thumb_eight = getStringValue(cursor, PRODUCT.THUMB_EIGHT);
                    product.product_thumb_nine = getStringValue(cursor, PRODUCT.THUMB_NINE);
                    product.product_thumb_ten = getStringValue(cursor, PRODUCT.THUMB_TEN);
                    product.thumb_count = getIntValue(cursor, PRODUCT.THUMB_COUNT);
                    product.model_number = getStringValue(cursor, PRODUCT.MODEL_NUMBER);
                    product.gross_wight_package = getStringValue(cursor, PRODUCT.GROSS_WEIGHT);
                    product.package_size = getStringValue(cursor, PRODUCT.PACKAGGE_SIZE);
                    product.in_stock_quantity = getStringValue(cursor, PRODUCT.IN_STOCK_QUANTITY);
                    product.isFavorite = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                    product.seasonal_discount = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                    product.is_free_shipping = getIntValue(cursor, PRODUCT.SEASONAL_DISCOUNT);
                    product.recently_viewed = getStringValue(cursor, PRODUCT.RECENTLY_VIEWED);
                    int is_incart = getIntValue(cursor, PRODUCT.IS_IN_CART);
                    if (is_incart > 0) {
                        product.isInCart = true;
                    } else {
                        product.isInCart = false;
                    }
                    product.is_mobile_exclusive = getIntValue(cursor, PRODUCT.MOBILE_EXCLUSIVE_DEAL);
                    product.min_order = getStringValue(cursor, PRODUCT.MIN_ORDER_AMOUNT);


                    products.add(product);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return products;
    }

    /* get single product by its  product ID */
    public Product getProductById(int productId) {
        SQLiteDatabase database = null;
        Product product = new Product();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = PRODUCT.PRODUCT_ID + " = ?";
            String[] whereArgs = {"" + productId};
            cursor = database.query(PRODUCT.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {


                product.id = getIntValue(cursor, PRODUCT._ID);
                product.subcategoryId = getIntValue(cursor, PRODUCT.SUBCATEGORY_ID);
                product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                product.imageUrlSmall = getStringValue(cursor, PRODUCT.IMAGE_URL_SMALL);
                product.imageUrlMedium = getStringValue(cursor, PRODUCT.IMAGE_URL_MEDIUM);
                product.name = getStringValue(cursor, PRODUCT.NAME);
                product.price = getStringValue(cursor, PRODUCT.PRICE);
                product.reducedPrice = getStringValue(cursor, PRODUCT.REDUCED_PRICE);
                product.description = getStringValue(cursor, PRODUCT.DESCRIPTION);
                product.relatedProductIDs = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RELATED_PRODUCTS));
                product.relatedRatings = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RATINGS));
                product.secondary_subcat_id = getIntValue(cursor, PRODUCT.SECONDARY_SUBCATEGORY_ID);
                product.ternary_subcat_id = getIntValue(cursor, PRODUCT.TERNARY_SUBCATEGORY_ID);
                product.merchant_id = getIntValue(cursor, PRODUCT.MERCHANT_ID);
                product.shipping_id = getStringValue(cursor, PRODUCT.SHIPPING_ID);
                product.item_code = getStringValue(cursor, PRODUCT.ITEM_CODE);
                product.product_thumb_one = getStringValue(cursor, PRODUCT.THUMB_ONE);
                product.product_thumb_two = getStringValue(cursor, PRODUCT.THUMB_TWO);
                product.product_thumb_three = getStringValue(cursor, PRODUCT.THUMB_THREE);
                product.product_thumb_four = getStringValue(cursor, PRODUCT.THUMB_FOUR);
                product.product_thumb_five = getStringValue(cursor, PRODUCT.THUMB_FIVE);
                product.product_thumb_six = getStringValue(cursor, PRODUCT.THUMB_SIX);
                product.product_thumb_seven = getStringValue(cursor, PRODUCT.THUMB_SEVEN);
                product.product_thumb_eight = getStringValue(cursor, PRODUCT.THUMB_EIGHT);
                product.product_thumb_nine = getStringValue(cursor, PRODUCT.THUMB_NINE);
                product.product_thumb_ten = getStringValue(cursor, PRODUCT.THUMB_TEN);
                product.thumb_count = getIntValue(cursor, PRODUCT.THUMB_COUNT);
                product.model_number = getStringValue(cursor, PRODUCT.MODEL_NUMBER);
                product.gross_wight_package = getStringValue(cursor, PRODUCT.GROSS_WEIGHT);
                product.package_size = getStringValue(cursor, PRODUCT.PACKAGGE_SIZE);
                product.in_stock_quantity = getStringValue(cursor, PRODUCT.IN_STOCK_QUANTITY);
                product.isFavorite = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                product.seasonal_discount = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                product.is_free_shipping = getIntValue(cursor, PRODUCT.SEASONAL_DISCOUNT);
                product.recently_viewed = getStringValue(cursor, PRODUCT.RECENTLY_VIEWED);
                int is_incart = getIntValue(cursor, PRODUCT.IS_IN_CART);
                if (is_incart > 0) {
                    product.isInCart = true;
                } else {
                    product.isInCart = false;
                }
                product.is_mobile_exclusive = getIntValue(cursor, PRODUCT.MOBILE_EXCLUSIVE_DEAL);
                product.min_order = getStringValue(cursor, PRODUCT.MIN_ORDER_AMOUNT);


            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return product;
    }

    /* provides products by batches*/
    public ArrayList<Product> getAllProdcutsByBatches(int numberOfPrducts) {
        SQLiteDatabase database = null;
        ArrayList<Product> products = new ArrayList<Product>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "SELECT * FROM product cp limit " + numberOfPrducts;
            cursor = database.rawQuery(MY_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();

                    product.id = getIntValue(cursor, PRODUCT._ID);
                    product.subcategoryId = getIntValue(cursor, PRODUCT.SUBCATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlSmall = getStringValue(cursor, PRODUCT.IMAGE_URL_SMALL);
                    product.imageUrlMedium = getStringValue(cursor, PRODUCT.IMAGE_URL_MEDIUM);
                    product.name = getStringValue(cursor, PRODUCT.NAME);
                    product.price = getStringValue(cursor, PRODUCT.PRICE);
                    product.reducedPrice = getStringValue(cursor, PRODUCT.REDUCED_PRICE);
                    product.description = getStringValue(cursor, PRODUCT.DESCRIPTION);
                    product.relatedProductIDs = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RELATED_PRODUCTS));
                    product.relatedRatings = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RATINGS));
                    product.secondary_subcat_id = getIntValue(cursor, PRODUCT.SECONDARY_SUBCATEGORY_ID);
                    product.ternary_subcat_id = getIntValue(cursor, PRODUCT.TERNARY_SUBCATEGORY_ID);
                    product.merchant_id = getIntValue(cursor, PRODUCT.MERCHANT_ID);
                    product.shipping_id = getStringValue(cursor, PRODUCT.SHIPPING_ID);
                    product.item_code = getStringValue(cursor, PRODUCT.ITEM_CODE);
                    product.product_thumb_one = getStringValue(cursor, PRODUCT.THUMB_ONE);
                    product.product_thumb_two = getStringValue(cursor, PRODUCT.THUMB_TWO);
                    product.product_thumb_three = getStringValue(cursor, PRODUCT.THUMB_THREE);
                    product.product_thumb_four = getStringValue(cursor, PRODUCT.THUMB_FOUR);
                    product.product_thumb_five = getStringValue(cursor, PRODUCT.THUMB_FIVE);
                    product.product_thumb_six = getStringValue(cursor, PRODUCT.THUMB_SIX);
                    product.product_thumb_seven = getStringValue(cursor, PRODUCT.THUMB_SEVEN);
                    product.product_thumb_eight = getStringValue(cursor, PRODUCT.THUMB_EIGHT);
                    product.product_thumb_nine = getStringValue(cursor, PRODUCT.THUMB_NINE);
                    product.product_thumb_ten = getStringValue(cursor, PRODUCT.THUMB_TEN);
                    product.thumb_count = getIntValue(cursor, PRODUCT.THUMB_COUNT);
                    product.model_number = getStringValue(cursor, PRODUCT.MODEL_NUMBER);
                    product.gross_wight_package = getStringValue(cursor, PRODUCT.GROSS_WEIGHT);
                    product.package_size = getStringValue(cursor, PRODUCT.PACKAGGE_SIZE);
                    product.in_stock_quantity = getStringValue(cursor, PRODUCT.IN_STOCK_QUANTITY);
                    product.isFavorite = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                    product.seasonal_discount = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                    product.is_free_shipping = getIntValue(cursor, PRODUCT.SEASONAL_DISCOUNT);
                    product.recently_viewed = getStringValue(cursor, PRODUCT.RECENTLY_VIEWED);
                    int is_incart = getIntValue(cursor, PRODUCT.IS_IN_CART);
                    if (is_incart > 0) {
                        product.isInCart = true;
                    } else {
                        product.isInCart = false;
                    }
                    product.is_mobile_exclusive = getIntValue(cursor, PRODUCT.MOBILE_EXCLUSIVE_DEAL);
                    product.min_order = getStringValue(cursor, PRODUCT.MIN_ORDER_AMOUNT);


                    products.add(product);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return products;
    }

    /* provides all products*/
    public ArrayList<Product> getAllProdcuts() {
        SQLiteDatabase database = null;
        ArrayList<Product> products = new ArrayList<Product>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "SELECT * FROM product";
            cursor = database.rawQuery(MY_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();

                    product.id = getIntValue(cursor, PRODUCT._ID);
                    product.subcategoryId = getIntValue(cursor, PRODUCT.SUBCATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlSmall = getStringValue(cursor, PRODUCT.IMAGE_URL_SMALL);
                    product.imageUrlMedium = getStringValue(cursor, PRODUCT.IMAGE_URL_MEDIUM);
                    product.name = getStringValue(cursor, PRODUCT.NAME);
                    product.price = getStringValue(cursor, PRODUCT.PRICE);
                    product.reducedPrice = getStringValue(cursor, PRODUCT.REDUCED_PRICE);
                    product.description = getStringValue(cursor, PRODUCT.DESCRIPTION);
                    product.relatedProductIDs = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RELATED_PRODUCTS));
                    product.relatedRatings = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RATINGS));
                    product.secondary_subcat_id = getIntValue(cursor, PRODUCT.SECONDARY_SUBCATEGORY_ID);
                    product.ternary_subcat_id = getIntValue(cursor, PRODUCT.TERNARY_SUBCATEGORY_ID);
                    product.merchant_id = getIntValue(cursor, PRODUCT.MERCHANT_ID);
                    product.shipping_id = getStringValue(cursor, PRODUCT.SHIPPING_ID);
                    product.item_code = getStringValue(cursor, PRODUCT.ITEM_CODE);
                    product.product_thumb_one = getStringValue(cursor, PRODUCT.THUMB_ONE);
                    product.product_thumb_two = getStringValue(cursor, PRODUCT.THUMB_TWO);
                    product.product_thumb_three = getStringValue(cursor, PRODUCT.THUMB_THREE);
                    product.product_thumb_four = getStringValue(cursor, PRODUCT.THUMB_FOUR);
                    product.product_thumb_five = getStringValue(cursor, PRODUCT.THUMB_FIVE);
                    product.product_thumb_six = getStringValue(cursor, PRODUCT.THUMB_SIX);
                    product.product_thumb_seven = getStringValue(cursor, PRODUCT.THUMB_SEVEN);
                    product.product_thumb_eight = getStringValue(cursor, PRODUCT.THUMB_EIGHT);
                    product.product_thumb_nine = getStringValue(cursor, PRODUCT.THUMB_NINE);
                    product.product_thumb_ten = getStringValue(cursor, PRODUCT.THUMB_TEN);
                    product.thumb_count = getIntValue(cursor, PRODUCT.THUMB_COUNT);
                    product.model_number = getStringValue(cursor, PRODUCT.MODEL_NUMBER);
                    product.gross_wight_package = getStringValue(cursor, PRODUCT.GROSS_WEIGHT);
                    product.package_size = getStringValue(cursor, PRODUCT.PACKAGGE_SIZE);
                    product.in_stock_quantity = getStringValue(cursor, PRODUCT.IN_STOCK_QUANTITY);
                    product.isFavorite = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                    product.seasonal_discount = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                    product.is_free_shipping = getIntValue(cursor, PRODUCT.SEASONAL_DISCOUNT);
                    product.recently_viewed = getStringValue(cursor, PRODUCT.RECENTLY_VIEWED);
                    int is_incart = getIntValue(cursor, PRODUCT.IS_IN_CART);
                    if (is_incart > 0) {
                        product.isInCart = true;
                    } else {
                        product.isInCart = false;
                    }
                    product.is_mobile_exclusive = getIntValue(cursor, PRODUCT.MOBILE_EXCLUSIVE_DEAL);
                    product.min_order = getStringValue(cursor, PRODUCT.MIN_ORDER_AMOUNT);


                    products.add(product);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return products;
    }

    public ArrayList<Product> getProductsBySubCategoryID(int categoryId) {
        SQLiteDatabase database = null;
        ArrayList<Product> products = new ArrayList<Product>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = PRODUCT.SUBCATEGORY_ID + " = ?";
            String[] whereArgs = {"" + categoryId};
            cursor = database.query(PRODUCT.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();

                    product.id = getIntValue(cursor, PRODUCT._ID);
                    product.subcategoryId = getIntValue(cursor, PRODUCT.SUBCATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlSmall = getStringValue(cursor, PRODUCT.IMAGE_URL_SMALL);
                    product.imageUrlMedium = getStringValue(cursor, PRODUCT.IMAGE_URL_MEDIUM);
                    product.name = getStringValue(cursor, PRODUCT.NAME);
                    product.price = getStringValue(cursor, PRODUCT.PRICE);
                    product.reducedPrice = getStringValue(cursor, PRODUCT.REDUCED_PRICE);
                    product.description = getStringValue(cursor, PRODUCT.DESCRIPTION);
                    product.relatedProductIDs = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RELATED_PRODUCTS));
                    product.relatedRatings = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RATINGS));
                    product.secondary_subcat_id = getIntValue(cursor, PRODUCT.SECONDARY_SUBCATEGORY_ID);
                    product.ternary_subcat_id = getIntValue(cursor, PRODUCT.TERNARY_SUBCATEGORY_ID);
                    product.merchant_id = getIntValue(cursor, PRODUCT.MERCHANT_ID);
                    product.shipping_id = getStringValue(cursor, PRODUCT.SHIPPING_ID);
                    product.item_code = getStringValue(cursor, PRODUCT.ITEM_CODE);
                    product.product_thumb_one = getStringValue(cursor, PRODUCT.THUMB_ONE);
                    product.product_thumb_two = getStringValue(cursor, PRODUCT.THUMB_TWO);
                    product.product_thumb_three = getStringValue(cursor, PRODUCT.THUMB_THREE);
                    product.product_thumb_four = getStringValue(cursor, PRODUCT.THUMB_FOUR);
                    product.product_thumb_five = getStringValue(cursor, PRODUCT.THUMB_FIVE);
                    product.product_thumb_six = getStringValue(cursor, PRODUCT.THUMB_SIX);
                    product.product_thumb_seven = getStringValue(cursor, PRODUCT.THUMB_SEVEN);
                    product.product_thumb_eight = getStringValue(cursor, PRODUCT.THUMB_EIGHT);
                    product.product_thumb_nine = getStringValue(cursor, PRODUCT.THUMB_NINE);
                    product.product_thumb_ten = getStringValue(cursor, PRODUCT.THUMB_TEN);
                    product.thumb_count = getIntValue(cursor, PRODUCT.THUMB_COUNT);
                    product.model_number = getStringValue(cursor, PRODUCT.MODEL_NUMBER);
                    product.gross_wight_package = getStringValue(cursor, PRODUCT.GROSS_WEIGHT);
                    product.package_size = getStringValue(cursor, PRODUCT.PACKAGGE_SIZE);
                    product.in_stock_quantity = getStringValue(cursor, PRODUCT.IN_STOCK_QUANTITY);
                    product.isFavorite = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                    product.seasonal_discount = getStringValue(cursor, PRODUCT.IS_FAVORITE);
                    product.is_free_shipping = getIntValue(cursor, PRODUCT.SEASONAL_DISCOUNT);
                    product.recently_viewed = getStringValue(cursor, PRODUCT.RECENTLY_VIEWED);
                    int is_incart = getIntValue(cursor, PRODUCT.IS_IN_CART);
                    if (is_incart > 0) {
                        product.isInCart = true;
                    } else {
                        product.isInCart = false;
                    }
                    product.is_mobile_exclusive = getIntValue(cursor, PRODUCT.MOBILE_EXCLUSIVE_DEAL);
                    product.min_order = getStringValue(cursor, PRODUCT.MIN_ORDER_AMOUNT);


                    products.add(product);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return products;
    }

    public boolean saveProductAsFavorite(int myProductId) {

        SQLiteDatabase database = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.openDataBase();
            Cursor cursor = null;
            String query = "UPDATE product SET isFavorite='" + 1 + "' WHERE product_id=" + myProductId + "";
            String[] whereArgs = {"" + myProductId};
            cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            cursor.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            LOGE(TAG, "Exception in saving product in cart ", e);
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return false;
    }

    public boolean removeProductFromFavorite(int myProductId) {

        SQLiteDatabase database = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.openDataBase();
            Cursor cursor = null;
            String query = "UPDATE product SET isFavorite='" + 0 + "' WHERE product_id=" + myProductId + "";
            String[] whereArgs = {"" + myProductId};
            cursor = database.rawQuery(query, null);
            cursor.moveToFirst();
            cursor.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            LOGE(TAG, "Exception in saving product in cart ", e);
        } finally {
            if (database != null) {
                database.close();
            }
        }
        return false;
    }


    /*************Product Table Queries End*************/


    /*************
     * Buying Options Table Queries Start
     *************/

    public ArrayList<BuyingOption> getBuyingOptionsPerProduct(Product product) {

        SQLiteDatabase database = null;
        ArrayList<BuyingOption> options = new ArrayList<BuyingOption>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = BUYING_OPTIONS.PRODUCT_ID + " = ?";
            String[] whereArgs = {"" + product.id};
            cursor = database.query(BUYING_OPTIONS.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    BuyingOption option = new BuyingOption();

                    option._id = getIntValue(cursor, BUYING_OPTIONS._ID);
                    option.product_id = getIntValue(cursor, BUYING_OPTIONS.PRODUCT_ID);
                    option.option_name = getStringValue(cursor, BUYING_OPTIONS.OPTION_NAME);
                    option.image_name = getStringValue(cursor, BUYING_OPTIONS.IMAGE_NAME);


                    options.add(option);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return options;
    }

    /*************Buying Options Table Queries End*************/


    /*************
     * PRODUCT STOCK Options Table Queries Start
     *************/

    public ProductStockOption getStockOptionForASingleBuyingOption(BuyingOption buyingOption) {

        SQLiteDatabase database = null;
        Cursor cursor = null;
        ProductStockOption productStockOption = null;

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = STOCK_OPTIONS.BUYING_OPTION_ID + " = ?";
            String[] whereArgs = {"" + buyingOption._id};
            cursor = database.query(STOCK_OPTIONS.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {

                productStockOption = new ProductStockOption();

                productStockOption._id = getIntValue(cursor, STOCK_OPTIONS._ID);
                productStockOption.buying_option_id = getStringValue(cursor, STOCK_OPTIONS.BUYING_OPTION_ID);
                productStockOption.piece_color = getStringValue(cursor, STOCK_OPTIONS.PIECE_COLOR);
                productStockOption.piece_type = getStringValue(cursor, STOCK_OPTIONS.PIECE_TYPE);
                productStockOption.compatible_with = getStringValue(cursor, STOCK_OPTIONS.PIECE_COMPATIBLE_WITH);
                productStockOption.compatible_model = getStringValue(cursor, STOCK_OPTIONS.PIECE_COMPATIBLE_MODEL);
                productStockOption.retail_package_included = getStringValue(cursor, STOCK_OPTIONS.RETAIL_PACKAGE_INCLUDED);
                productStockOption.features = getStringValue(cursor, STOCK_OPTIONS.FEATURES);
                productStockOption.material = getStringValue(cursor, STOCK_OPTIONS.MATERIAL);
                productStockOption.min_sale_qty = getStringValue(cursor, STOCK_OPTIONS.MIN_SALE_QUANTITY);
                productStockOption.remaining_qty = getStringValue(cursor, STOCK_OPTIONS.REMAINING_QUANTITY);

                buyingOption.productStockOption = productStockOption;

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return productStockOption;
    }

    /*************PRODUCT STOCK Options Table Queries End*************/


    /*************
     * Prices Table Queries Start
     *************/

    public Price getPricePerStockOption(ProductStockOption productStockOption) {

        SQLiteDatabase database = null;
        Cursor cursor = null;
        Price price = null;

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = PRICES.STOCK_OPTION_ID + " = ?";
            String[] whereArgs = {"" + productStockOption._id};
            cursor = database.query(PRICES.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {

                price = new Price();

                price._id = getIntValue(cursor, PRICES._ID);
                price.stock_option_id = getStringValue(cursor, PRICES.STOCK_OPTION_ID);
                price.single_item_price = getStringValue(cursor, PRICES.SINGLE_ITEM_PRICE);
                price.one = getStringValue(cursor, PRICES.ONE);
                price.two = getStringValue(cursor, PRICES.TWO);
                price.three = getStringValue(cursor, PRICES.THREE);
                price.four = getStringValue(cursor, PRICES.FOUR);
                price.five = getStringValue(cursor, PRICES.FIVE);
                price.six = getStringValue(cursor, PRICES.SIX);
                price.seven = getStringValue(cursor, PRICES.SEVEN);
                price.eight = getStringValue(cursor, PRICES.EIGHT);
                price.nine = getStringValue(cursor, PRICES.NINE);
                price.ten = getStringValue(cursor, PRICES.TEN);
                price.eleven = getStringValue(cursor, PRICES.ELEVEN);
                price.twelve = getStringValue(cursor, PRICES.TWELVE);
                price.fifteen = getStringValue(cursor, PRICES.FIFTEEN);
                price.twenty = getStringValue(cursor, PRICES.TWENTY);
                price.thirty = getStringValue(cursor, PRICES.THIRTY);
                price.fifty = getStringValue(cursor, PRICES.FIFTY);
                price.hundred = getStringValue(cursor, PRICES.HUNDERED);
                price.hundred_fifty = getStringValue(cursor, PRICES.HUNDERED_FIFTY);
                price.two_hundred = getStringValue(cursor, PRICES.TWO_HUNDERED);
                price.five_hundred = getStringValue(cursor, PRICES.FIVE_HUNDERED);
                price.seven_hundred = getStringValue(cursor, PRICES.SEVEN_HUNDERED);
                price.thousand = getStringValue(cursor, PRICES.THOUSAND);
                price.two_thousand = getStringValue(cursor, PRICES.TWO_THOUSAND);
                price.five_thousand = getStringValue(cursor, PRICES.FIVE_THOUSAND);


            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return price;
    }

    /*************Price Table Queries End*************/


    /*************
     * Shipping Table Queries Start
     *************/

    public ArrayList<ShippingMethod> getShippingMethodsPerCountryName(String country) {

        ArrayList<ShippingMethod> shippingMethods = new ArrayList<>();
        SQLiteDatabase database = null;
        Cursor cursor = null;

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = SHIPPING.SHIPPNG_COUNTRY + " = ?";
            String[] whereArgs = {"" + country};
            cursor = database.query(SHIPPING.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {
                do {

                    ShippingMethod shippingMethod = new ShippingMethod();

                    shippingMethod.id = getIntValue(cursor, SHIPPING._ID);
                    shippingMethod.country = getStringValue(cursor, SHIPPING.SHIPPNG_COUNTRY);
                    shippingMethod.company = getStringValue(cursor, SHIPPING.COMPANY);
                    shippingMethod.shipping_origin = getStringValue(cursor, SHIPPING.SHIPPING_ORIGIN);
                    shippingMethod.tracking = getStringValue(cursor, SHIPPING.TRACKING);
                    shippingMethod.shipping_time = getStringValue(cursor, SHIPPING.SHIPPING_TIME);
                    shippingMethod.shipping_cost = getStringValue(cursor, SHIPPING.SHIPPING_COST);

                    shippingMethods.add(shippingMethod);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return shippingMethods;
    }

    /*************Shipping Table Queries End*************/


    /*************
     * Country Table Queries Start
     *************/

    public String getCountryNameByISOCode(String isocode) {

        SQLiteDatabase database = null;
        Cursor cursor = null;
        String name = null;

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = COUNTRY.ISO + " = ?";
            String[] whereArgs = {"" + isocode};
            cursor = database.query(COUNTRY.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {

                name = getStringValue(cursor, COUNTRY.NICENAME);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return name;
    }

    public ArrayList<Country> getAllCountries() {
        SQLiteDatabase database = null;
        ArrayList<Country> countries = new ArrayList<>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();

            final String MY_QUERY = "SELECT * FROM country";
            cursor = database.rawQuery(MY_QUERY, null);

            if (cursor.moveToFirst()) {
                do {
                    Country country = new Country();

                    country.id = getIntValue(cursor, COUNTRY.ID);
                    country.iso = getStringValue(cursor, COUNTRY.ISO);
                    country.name = getStringValue(cursor, COUNTRY.NAME);
                    country.nicename = getStringValue(cursor, COUNTRY.NICENAME);
                    country.iso3 = getStringValue(cursor, COUNTRY.ISO3);
                    country.numcode = getStringValue(cursor, COUNTRY.NUMCODE);
                    country.phonecode = getStringValue(cursor, COUNTRY.PHONECODE);

                    countries.add(country);
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return countries;
    }



    /*************Country Table Queries End*************/


}
