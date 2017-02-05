package au.com.geardoaustralia.cartNew.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by ashish (Min2) on 08/02/16.
 * <p/>
 * Avoiding internal setters/getters
 * http://developer.android.com/training/articles/perf-tips.html#GettersSetters
 */
public class Product implements Parcelable {


    public int id;
    public int subcategoryId;
    public int secondary_subcat_id;
    public int ternary_subcat_id;
    public int productId;
    public int merchant_id;
    public int is_free_shipping;
    public int is_mobile_exclusive;
    public String price;
    public String shipping_id;
    public String reducedPrice;
    public String description;
    public String imageUrlOriginal;
    public String imageUrlThumb;
    public String imageUrlSmall;
    public String imageUrlMedium;
    public String name;
    public String item_code;
    public String product_thumb_one;
    public String product_thumb_two;
    public String product_thumb_three;
    public String product_thumb_four;
    public String product_thumb_five;
    public String product_thumb_six;
    public String product_thumb_seven;
    public String product_thumb_eight;
    public String product_thumb_nine;
    public String product_thumb_ten;
    public String min_order;
    public String model_number;
    public String gross_wight_package;
    public String package_size;
    public String in_stock_quantity;
    public String isFavorite;
    public String seasonal_discount;
    public String recently_viewed;
    public ArrayList<String> relatedProductIDs;
    public ArrayList<String> relatedRatings;
    public ArrayList<BuyingOption> buyingOptions;
    public boolean isInCart = false;
    public double totalPrice = 0;
    public int quantity = 0;
    public int selectedBuyingOptionId = 0;
    public int thumb_count = 0;

    public Product(){

        relatedProductIDs = new ArrayList<>();
        relatedRatings = new ArrayList<>();
        buyingOptions = new ArrayList<>();
    }


    /*
    * (non-Javadoc)
    *
    * @see java.lang.Object#equals(java.lang.Object)
    *
    * Please don not override toString() method it will give response which cause override method
    * equals() failure.
    *
    * the reason behind to override equals and hashCode method is to compare added cart products in
    * local database with the products coming from server on basis of unique product id.
    */

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.subcategoryId);
        dest.writeInt(this.secondary_subcat_id);
        dest.writeInt(this.ternary_subcat_id);
        dest.writeInt(this.productId);
        dest.writeInt(this.merchant_id);
        dest.writeInt(this.is_free_shipping);
        dest.writeInt(this.is_mobile_exclusive);
        dest.writeString(this.price);
        dest.writeString(this.shipping_id);
        dest.writeString(this.reducedPrice);
        dest.writeString(this.description);
        dest.writeString(this.imageUrlOriginal);
        dest.writeString(this.imageUrlThumb);
        dest.writeString(this.imageUrlSmall);
        dest.writeString(this.imageUrlMedium);
        dest.writeString(this.name);
        dest.writeString(this.item_code);
        dest.writeString(this.product_thumb_one);
        dest.writeString(this.product_thumb_two);
        dest.writeString(this.product_thumb_three);
        dest.writeString(this.product_thumb_four);
        dest.writeString(this.product_thumb_five);
        dest.writeString(this.product_thumb_six);
        dest.writeString(this.product_thumb_seven);
        dest.writeString(this.product_thumb_eight);
        dest.writeString(this.product_thumb_nine);
        dest.writeString(this.product_thumb_ten);
        dest.writeString(this.min_order);
        dest.writeString(this.model_number);
        dest.writeString(this.gross_wight_package);
        dest.writeString(this.package_size);
        dest.writeString(this.in_stock_quantity);
        dest.writeString(this.isFavorite);
        dest.writeString(this.seasonal_discount);
        dest.writeString(this.recently_viewed);
        dest.writeStringList(this.relatedProductIDs);
        dest.writeStringList(this.relatedRatings);
        dest.writeTypedList(this.buyingOptions);
        dest.writeByte(this.isInCart ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.totalPrice);
        dest.writeInt(this.quantity);
        dest.writeInt(this.selectedBuyingOptionId);
        dest.writeInt(this.thumb_count);
    }

    protected Product(Parcel in) {
        this.id = in.readInt();
        this.subcategoryId = in.readInt();
        this.secondary_subcat_id = in.readInt();
        this.ternary_subcat_id = in.readInt();
        this.productId = in.readInt();
        this.merchant_id = in.readInt();
        this.is_free_shipping = in.readInt();
        this.is_mobile_exclusive = in.readInt();
        this.price = in.readString();
        this.shipping_id = in.readString();
        this.reducedPrice = in.readString();
        this.description = in.readString();
        this.imageUrlOriginal = in.readString();
        this.imageUrlThumb = in.readString();
        this.imageUrlSmall = in.readString();
        this.imageUrlMedium = in.readString();
        this.name = in.readString();
        this.item_code = in.readString();
        this.product_thumb_one = in.readString();
        this.product_thumb_two = in.readString();
        this.product_thumb_three = in.readString();
        this.product_thumb_four = in.readString();
        this.product_thumb_five = in.readString();
        this.product_thumb_six = in.readString();
        this.product_thumb_seven = in.readString();
        this.product_thumb_eight = in.readString();
        this.product_thumb_nine = in.readString();
        this.product_thumb_ten = in.readString();
        this.min_order = in.readString();
        this.model_number = in.readString();
        this.gross_wight_package = in.readString();
        this.package_size = in.readString();
        this.in_stock_quantity = in.readString();
        this.isFavorite = in.readString();
        this.seasonal_discount = in.readString();
        this.recently_viewed = in.readString();
        this.relatedProductIDs = in.createStringArrayList();
        this.relatedRatings = in.createStringArrayList();
        this.buyingOptions = in.createTypedArrayList(BuyingOption.CREATOR);
        this.isInCart = in.readByte() != 0;
        this.totalPrice = in.readDouble();
        this.quantity = in.readInt();
        this.selectedBuyingOptionId = in.readInt();
        this.thumb_count = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
