package au.com.geardoaustralia.cartNew.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DasunPC on 1/24/17.
 */

public class BuyingOption implements Parcelable {

    public int _id;
    public int product_id;
    public String option_name;
    public String image_name;
    public ProductStockOption productStockOption;

    public BuyingOption() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeInt(this.product_id);
        dest.writeString(this.option_name);
        dest.writeString(this.image_name);
        dest.writeParcelable(this.productStockOption, flags);
    }

    protected BuyingOption(Parcel in) {
        this._id = in.readInt();
        this.product_id = in.readInt();
        this.option_name = in.readString();
        this.image_name = in.readString();
        this.productStockOption = in.readParcelable(ProductStockOption.class.getClassLoader());
    }

    public static final Creator<BuyingOption> CREATOR = new Creator<BuyingOption>() {
        @Override
        public BuyingOption createFromParcel(Parcel source) {
            return new BuyingOption(source);
        }

        @Override
        public BuyingOption[] newArray(int size) {
            return new BuyingOption[size];
        }
    };
}
