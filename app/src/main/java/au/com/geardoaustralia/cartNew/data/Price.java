package au.com.geardoaustralia.cartNew.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DasunPC on 1/25/17.
 */

public class Price implements Parcelable {

    public  int _id;
    public  String stock_option_id;
    public  String single_item_price;
    public  String one;
    public  String two;
    public  String three;
    public  String four;
    public  String five;
    public  String six;
    public  String seven;
    public  String eight;
    public  String nine;
    public  String ten;
    public  String eleven;
    public  String twelve;
    public  String fifteen;
    public  String twenty;
    public  String thirty;
    public  String forty;
    public  String fifty;
    public  String hundred;
    public  String hundred_fifty;
    public  String two_hundred;
    public  String five_hundred;
    public  String seven_hundred;
    public  String thousand;
    public  String two_thousand;
    public  String five_thousand;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this.stock_option_id);
        dest.writeString(this.single_item_price);
        dest.writeString(this.one);
        dest.writeString(this.two);
        dest.writeString(this.three);
        dest.writeString(this.four);
        dest.writeString(this.five);
        dest.writeString(this.six);
        dest.writeString(this.seven);
        dest.writeString(this.eight);
        dest.writeString(this.nine);
        dest.writeString(this.ten);
        dest.writeString(this.eleven);
        dest.writeString(this.twelve);
        dest.writeString(this.fifteen);
        dest.writeString(this.twenty);
        dest.writeString(this.thirty);
        dest.writeString(this.forty);
        dest.writeString(this.fifty);
        dest.writeString(this.hundred);
        dest.writeString(this.hundred_fifty);
        dest.writeString(this.two_hundred);
        dest.writeString(this.five_hundred);
        dest.writeString(this.seven_hundred);
        dest.writeString(this.thousand);
        dest.writeString(this.two_thousand);
        dest.writeString(this.five_thousand);
    }

    public Price() {
    }

    protected Price(Parcel in) {
        this._id = in.readInt();
        this.stock_option_id = in.readString();
        this.single_item_price = in.readString();
        this.one = in.readString();
        this.two = in.readString();
        this.three = in.readString();
        this.four = in.readString();
        this.five = in.readString();
        this.six = in.readString();
        this.seven = in.readString();
        this.eight = in.readString();
        this.nine = in.readString();
        this.ten = in.readString();
        this.eleven = in.readString();
        this.twelve = in.readString();
        this.fifteen = in.readString();
        this.twenty = in.readString();
        this.thirty = in.readString();
        this.forty = in.readString();
        this.fifty = in.readString();
        this.hundred = in.readString();
        this.hundred_fifty = in.readString();
        this.two_hundred = in.readString();
        this.five_hundred = in.readString();
        this.seven_hundred = in.readString();
        this.thousand = in.readString();
        this.two_thousand = in.readString();
        this.five_thousand = in.readString();
    }

    public static final Parcelable.Creator<Price> CREATOR = new Parcelable.Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel source) {
            return new Price(source);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };
}
