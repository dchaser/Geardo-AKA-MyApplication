package au.com.geardoaustralia.utils;

import java.text.DecimalFormat;

/**
 * Created by ashish (Min2) on 09/02/16.
 */
public class CommonConstants {


    public static final String ROOT_PATH = "file:///android_asset/product/";
    public static final String USER_PATH = "file:///android_asset/userimages/";
    public static final String CATEGORY_PATH = "file:///android_asset/category/";

    public static final DecimalFormat decimalFormat = new DecimalFormat("#,###,###.##");

    //gives the decimal formatted defined by decimalFormat.
    public static String getDecimalString(String value) {

        try {
            double result = Double.parseDouble(value); // Make use of autoboxing.  It's also easier to read.
            String output = decimalFormat.format(result);
            return output;
        } catch (NumberFormatException e) {
            // value did not contain a valid double
            return "";
        }
//        return "" + (value != null ? decimalFormat.format(value) : "");
    }

}
