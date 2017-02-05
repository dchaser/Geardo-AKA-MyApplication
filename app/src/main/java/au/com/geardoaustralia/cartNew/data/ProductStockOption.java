package au.com.geardoaustralia.cartNew.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by DasunPC on 1/24/17.
 */

public class ProductStockOption implements Parcelable {

    public int _id;
    public String buying_option_id;
    public String piece_color;
    public String piece_type;
    public String compatible_with;
    public String compatible_model;
    public String retail_package_included;
    public String features;
    public String material;
    public String min_sale_qty;
    public String remaining_qty;
    public Price price;

    public ProductStockOption() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this._id);
        dest.writeString(this.buying_option_id);
        dest.writeString(this.piece_color);
        dest.writeString(this.piece_type);
        dest.writeString(this.compatible_with);
        dest.writeString(this.compatible_model);
        dest.writeString(this.retail_package_included);
        dest.writeString(this.features);
        dest.writeString(this.material);
        dest.writeString(this.min_sale_qty);
        dest.writeString(this.remaining_qty);
        dest.writeParcelable(this.price, flags);
    }

    protected ProductStockOption(Parcel in) {
        this._id = in.readInt();
        this.buying_option_id = in.readString();
        this.piece_color = in.readString();
        this.piece_type = in.readString();
        this.compatible_with = in.readString();
        this.compatible_model = in.readString();
        this.retail_package_included = in.readString();
        this.features = in.readString();
        this.material = in.readString();
        this.min_sale_qty = in.readString();
        this.remaining_qty = in.readString();
        this.price = in.readParcelable(Price.class.getClassLoader());
    }

    public static final Creator<ProductStockOption> CREATOR = new Creator<ProductStockOption>() {
        @Override
        public ProductStockOption createFromParcel(Parcel source) {
            return new ProductStockOption(source);
        }

        @Override
        public ProductStockOption[] newArray(int size) {
            return new ProductStockOption[size];
        }
    };
}
