package au.com.geardoaustralia.gallery;

import java.io.Serializable;

/**
 * Created by DasunPC on 11/8/16.
 */

public class Image implements Serializable {
    public String name;
    public String thumb;


    public Image(){}

    public Image(String name, String thumb) {
        this.name = name;
        this.thumb = thumb;
    }


}