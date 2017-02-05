package au.com.geardoaustralia.cartNew.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DasunPC on 1/26/17.
 */

public class ShippingMethod implements Parcelable {

    public int id;
    public String country;
    public String company;
    public String shipping_origin;
    public String tracking;
    public String shipping_time;
    public String shipping_cost;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.country);
        dest.writeString(this.company);
        dest.writeString(this.shipping_origin);
        dest.writeString(this.tracking);
        dest.writeString(this.shipping_time);
        dest.writeString(this.shipping_cost);
    }

    public ShippingMethod() {
    }

    protected ShippingMethod(Parcel in) {
        this.id = in.readInt();
        this.country = in.readString();
        this.company = in.readString();
        this.shipping_origin = in.readString();
        this.tracking = in.readString();
        this.shipping_time = in.readString();
        this.shipping_cost = in.readString();
    }

    public static final Parcelable.Creator<ShippingMethod> CREATOR = new Parcelable.Creator<ShippingMethod>() {
        @Override
        public ShippingMethod createFromParcel(Parcel source) {
            return new ShippingMethod(source);
        }

        @Override
        public ShippingMethod[] newArray(int size) {
            return new ShippingMethod[size];
        }
    };
}
