package au.com.geardoaustralia.MainScreen.MainContentMainActivity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by DasunPC on 11/3/16.
 */

public class ProductInfoModel implements Parcelable {

    //@Index
    //@com.google.gson.annotations.SerializedName("id")
    //public int localId;

    //@com.google.gson.annotations.SerializedName("cloudProductId")
    // public String cloudId;

    @com.google.gson.annotations.SerializedName("title")
    public String Title;
    @com.google.gson.annotations.SerializedName("thumnailUrl")
    public int thumnailUrl;
    @com.google.gson.annotations.SerializedName("reducedPrice")
    public String reducedPrice;
    @com.google.gson.annotations.SerializedName("price")
    public String price;
    @com.google.gson.annotations.SerializedName("descriptionl")
    public String descriptionl;
    @com.google.gson.annotations.SerializedName("sizes")
    public String[] sizes;

    public ProductInfoModel() {

    }

    public ProductInfoModel(String title, int thumnailUrl, String reducedPrice, String price, String descriptionl, String[] sizes) {
        Title = title;
        this.thumnailUrl = thumnailUrl;
        this.reducedPrice = reducedPrice;
        this.price = price;
        this.descriptionl = descriptionl;
        sizes = sizes;
    }

    // parcelable contents
    public ProductInfoModel(Parcel in) {
        ReadFromParcel(in);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    private void ReadFromParcel(Parcel in) {

        this.Title = in.readString();
        this.thumnailUrl = in.readInt();
        this.reducedPrice = in.readString();
        this.price = in.readString();
        this.descriptionl = in.readString();
        this.sizes = in.createStringArray();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.Title);
        dest.writeInt(this.thumnailUrl);
        dest.writeString(this.reducedPrice);
        dest.writeString(this.price);
        dest.writeString(this.descriptionl);
        dest.writeStringArray(this.sizes);
    }

    public static final Parcelable.Creator<ProductInfoModel> CREATOR = new Parcelable.Creator<ProductInfoModel>() {

        @Override
        public ProductInfoModel createFromParcel(Parcel in) {
            return new ProductInfoModel(in);
        }

        @Override
        public ProductInfoModel[] newArray(int size) {
            return new ProductInfoModel[size];
        }

    };
}
