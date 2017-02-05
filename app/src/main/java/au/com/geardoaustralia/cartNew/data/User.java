package au.com.geardoaustralia.cartNew.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DasunPC on 1/10/17.
 */

public class User implements Parcelable{

    //general details
    public int id;
    public String imageUrlThumb;
    public String imageUrlSmall;
    public String display_name;
    public String email;
    public String created_at;
    public String oauth_provider;
    public String oauth_uid;
    public String username;
    public String password;
    public String etc;

    public User(){

    }

    public User(String imageUrlThumb, String imageUrlSmall, String display_name,  String email, String oauth_provider, String oauth_uid, String username, String password, String etc) {
        this.imageUrlThumb = imageUrlThumb;
        this.imageUrlSmall = imageUrlSmall;
        this.display_name = display_name;
        this.email = email;
        this.oauth_provider = oauth_provider;
        this.oauth_uid = oauth_uid;
        this.username = username;
        this.password = password;
        this.etc = etc;
    }


    protected User(Parcel in) {
        id = in.readInt();
        imageUrlThumb = in.readString();
        imageUrlSmall = in.readString();
        display_name = in.readString();
        email = in.readString();
        created_at = in.readString();
        oauth_provider = in.readString();
        oauth_uid = in.readString();
        username = in.readString();
        password = in.readString();
        etc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imageUrlThumb);
        dest.writeString(imageUrlSmall);
        dest.writeString(display_name);
        dest.writeString(email);
        dest.writeString(created_at);
        dest.writeString(oauth_provider);
        dest.writeString(oauth_uid);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(etc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

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
}
