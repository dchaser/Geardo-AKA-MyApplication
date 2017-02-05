package au.com.geardoaustralia.cartNew.data;

import android.os.Parcel;

/**
 * Created by DasunPC on 1/18/17.
 */

public class WholeSaleProduct extends Product {

    public int min_order;
    public String thumbnail_one;
    public String thumbnail_two;
    public String thumbnail_three;
    public String thumbnail_four;
    public String thumbnail_five;
    public String thumbnail_six;
    public String thumbnail_seven;
    public String thumbnail_eight;
    public String thumbnail_nine;
    public String thumbnail_ten;
    public String low_price_range;
    public String high_price_range;
    public String reduced_low_price_range;
    public String reduced_high_price_range;


    public WholeSaleProduct(){

    }

    protected WholeSaleProduct(Parcel in){
        super(in);
        min_order = in.readInt();
        thumbnail_one = in.readString();
        thumbnail_two = in.readString();
        thumbnail_three = in.readString();
        thumbnail_four = in.readString();
        thumbnail_five = in.readString();
        thumbnail_six = in.readString();
        thumbnail_seven = in.readString();
        thumbnail_eight = in.readString();
        thumbnail_nine = in.readString();
        thumbnail_ten = in.readString();
        low_price_range = in.readString();
        high_price_range = in.readString();
        reduced_low_price_range = in.readString();
        reduced_low_price_range = in.readString();
    }

    public static final Creator<WholeSaleProduct> CREATOR = new Creator<WholeSaleProduct>() {
        @Override
        public WholeSaleProduct createFromParcel(Parcel parcel) {
            return new WholeSaleProduct(parcel);
        }

        @Override
        public WholeSaleProduct[] newArray(int size) {
            return new WholeSaleProduct[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(min_order);
        dest.writeString(thumbnail_one);
        dest.writeString(thumbnail_two);
        dest.writeString(thumbnail_three);
        dest.writeString(thumbnail_four);
        dest.writeString(thumbnail_five);
        dest.writeString(thumbnail_six);
        dest.writeString(thumbnail_seven);
        dest.writeString(thumbnail_eight);
        dest.writeString(thumbnail_nine);
        dest.writeString(thumbnail_ten);
        dest.writeString(low_price_range);
        dest.writeString(high_price_range);
        dest.writeString(reduced_low_price_range);
        dest.writeString(reduced_high_price_range);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
