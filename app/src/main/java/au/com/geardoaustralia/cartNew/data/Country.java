package au.com.geardoaustralia.cartNew.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DasunPC on 1/26/17.
 */

public class Country implements Parcelable {

    public int id;
    public String iso;
    public String name;
    public String nicename;
    public String iso3;
    public String numcode;
    public String phonecode;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.iso);
        dest.writeString(this.name);
        dest.writeString(this.nicename);
        dest.writeString(this.iso3);
        dest.writeString(this.numcode);
        dest.writeString(this.phonecode);
    }

    public Country() {
    }

    protected Country(Parcel in) {
        this.id = in.readInt();
        this.iso = in.readString();
        this.name = in.readString();
        this.nicename = in.readString();
        this.iso3 = in.readString();
        this.numcode = in.readString();
        this.phonecode = in.readString();
    }

    public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
}
