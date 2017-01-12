package au.com.geardoaustralia.cartNew.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;
import java.util.List;

import au.com.geardoaustralia.cartNew.data.Category;
import au.com.geardoaustralia.cartNew.data.CustomerReview;
import au.com.geardoaustralia.cartNew.data.Product;
import au.com.geardoaustralia.cartNew.data.User;
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
     * Product Table which defines all the columns of this table
     */
    interface PRODUCT {
        public static String TABLE_NAME = "product";
        public static String _ID = "_id";
        public static String CATEGORY_ID = "category_id";
        public static String PRODUCT_ID = "product_id";
        public static String PRICE = "price";
        public static String REDUCED_PRICE = "reduced_price";
        public static String DESCRIPTION = "description";
        public static String IMAGE_URL_ORIGINAL = "image_url_original";
        public static String IMAGE_URL_THUMB = "image_url_thumb";
        public static String IMAGE_URL_SMALL = "image_url_small";
        public static String IMAGE_URL_MEDIUM = "image_url_medium";
        public static String NAME = "name";
        public static String RELATED_PRODUCTS = "related";
        public static String RATINGS = "ratings";
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
        public static String LASTNAME = "lastname";
        public static String EMAIL = "email";
        public static String CREATED_AT = "created_at";
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

    public ArrayList<Product> getProductsByCategory(int categoryId) {
        SQLiteDatabase database = null;
        ArrayList<Product> products = new ArrayList<Product>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = PRODUCT.CATEGORY_ID + " = ?";
            String[] whereArgs = {"" + categoryId};
            cursor = database.query(PRODUCT.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();

                    product.id = getIntValue(cursor, PRODUCT._ID);
                    product.categoryId = getIntValue(cursor, PRODUCT.CATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlThumb = getStringValue(cursor, PRODUCT.IMAGE_URL_THUMB);
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

    public boolean saveCartProduct(Product myCartProduct) {

        SQLiteDatabase database = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.openDataBase();

            ContentValues contentValues = new ContentValues();

            contentValues.put(CART_PRODUCT.CATEGORY_ID, myCartProduct.categoryId);
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
                    product.categoryId = getIntValue(cursor, PRODUCT.CATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlThumb = getStringValue(cursor, PRODUCT.IMAGE_URL_THUMB);
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

    public ArrayList<CustomerReview> FillUserDetailsInGivenReviews(ArrayList<CustomerReview> customerReviews){

        CustomerReview customerReview = new CustomerReview();
        ArrayList<CustomerReview> reviews = customerReviews;

        for(int i = 0; i < customerReviews.size(); i++){

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
                        user.firstname = getStringValue(cursor, USERS.FIRSTNAME);
                        user.lastname = getStringValue(cursor, USERS.LASTNAME);
                        user.email = getStringValue(cursor, USERS.EMAIL);
                        user.created_at = getStringValue(cursor, USERS.CREATED_AT);

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

    public ArrayList<CustomerReview> getAllReviewsForProductId(int productId){

        SQLiteDatabase database = null;
        ArrayList<CustomerReview> reviews = new ArrayList<>();
        Cursor cursor = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(GlobalContext.getInstance());
            database = dbHelper.getReadableDatabase();
            String whereClause = RATINGS.PRODUCT_ID + " = ?";
            String[] whereArgs = {"" + productId};
            //cursor = database.query(RATINGS.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
            cursor = database.rawQuery("select * from ratings , users  where  ratings.customer_id = users._id and ratings.product_id = "+productId, null);


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
                    user.firstname = getStringValue(cursor, USERS.FIRSTNAME);
                    user.lastname = getStringValue(cursor, USERS.LASTNAME);
                    user.email = getStringValue(cursor, USERS.EMAIL);
                    user.created_at = getStringValue(cursor, USERS.CREATED_AT);


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
                    user.firstname = getStringValue(cursor, USERS.FIRSTNAME);
                    user.lastname = getStringValue(cursor, USERS.LASTNAME);
                    user.email = getStringValue(cursor, USERS.EMAIL);
                    user.created_at = getStringValue(cursor, USERS.CREATED_AT);

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

    public User getUserByUserId(int userId){

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
                user.firstname = getStringValue(cursor, USERS.FIRSTNAME);
                user.lastname = getStringValue(cursor, USERS.LASTNAME);
                user.email = getStringValue(cursor, USERS.EMAIL);
                user.created_at = getStringValue(cursor, USERS.CREATED_AT);


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
                    product.categoryId = getIntValue(cursor, PRODUCT.CATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlThumb = getStringValue(cursor, PRODUCT.IMAGE_URL_THUMB);
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
                    product.categoryId = getIntValue(cursor, PRODUCT.CATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlThumb = getStringValue(cursor, PRODUCT.IMAGE_URL_THUMB);
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
                    product.categoryId = getIntValue(cursor, PRODUCT.CATEGORY_ID);
                    product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                    product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                    product.imageUrlThumb = getStringValue(cursor, PRODUCT.IMAGE_URL_THUMB);
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
                product.categoryId = getIntValue(cursor, PRODUCT.CATEGORY_ID);
                product.productId = getIntValue(cursor, PRODUCT.PRODUCT_ID);
                product.imageUrlOriginal = getStringValue(cursor, PRODUCT.IMAGE_URL_ORIGINAL);
                product.imageUrlThumb = getStringValue(cursor, PRODUCT.IMAGE_URL_THUMB);
                product.imageUrlSmall = getStringValue(cursor, PRODUCT.IMAGE_URL_SMALL);
                product.imageUrlMedium = getStringValue(cursor, PRODUCT.IMAGE_URL_MEDIUM);
                product.name = getStringValue(cursor, PRODUCT.NAME);
                product.price = getStringValue(cursor, PRODUCT.PRICE);
                product.reducedPrice = getStringValue(cursor, PRODUCT.REDUCED_PRICE);
                product.description = getStringValue(cursor, PRODUCT.DESCRIPTION);
                product.relatedProductIDs = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RELATED_PRODUCTS));
                product.relatedRatings = utilKit.getStringArrayListFromCSV_String(getStringValue(cursor, PRODUCT.RATINGS));

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

}
