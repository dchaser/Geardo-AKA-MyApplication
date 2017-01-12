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
    public int categoryId;
    public int productId;
    public String price;
    public String reducedPrice;
    public String description;
    public String imageUrlOriginal;
    public String imageUrlThumb;
    public String imageUrlSmall;
    public String imageUrlMedium;
    public String name;
    //search tags will not be needed i
    public int quantity;

    //following fiedls are comma seperated text fields in sqlite but here they should give a ArrayList of rating objects and related ones
    public ArrayList<String> relatedProductIDs;
    public ArrayList<String> relatedRatings;

    /*
    * Utility variables which are supposed to work for item deletion and addition
    */
    public boolean isInCart = false;
    public double totalPrice = 0;

    public Product(){

        relatedProductIDs = new ArrayList<>();
        relatedRatings = new ArrayList<>();
    }


    protected Product(Parcel in) {
        id = in.readInt();
        categoryId = in.readInt();
        productId = in.readInt();
        price = in.readString();
        reducedPrice = in.readString();
        description = in.readString();
        imageUrlOriginal = in.readString();
        imageUrlThumb = in.readString();
        imageUrlSmall = in.readString();
        imageUrlMedium = in.readString();
        name = in.readString();
        quantity = in.readInt();
        this.relatedProductIDs = in.readArrayList(String.class.getClassLoader());
        this.relatedRatings = in.readArrayList(String.class.getClassLoader());
        isInCart = in.readByte() != 0;
        totalPrice = in.readDouble();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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
        dest.writeInt(id);
        dest.writeInt(categoryId);
        dest.writeInt(productId);
        dest.writeString(price);
        dest.writeString(reducedPrice);
        dest.writeString(description);
        dest.writeString(imageUrlOriginal);
        dest.writeString(imageUrlThumb);
        dest.writeString(imageUrlSmall);
        dest.writeString(imageUrlMedium);
        dest.writeString(name);
        dest.writeInt(quantity);
        dest.writeList(relatedProductIDs);
        dest.writeList(relatedRatings);
        dest.writeByte((byte) (isInCart ? 1 : 0));
        dest.writeDouble(totalPrice);
    }
}
