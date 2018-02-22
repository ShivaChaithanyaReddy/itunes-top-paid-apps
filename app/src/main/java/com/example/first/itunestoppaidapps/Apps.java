package com.example.first.itunestoppaidapps;

import java.io.Serializable;

/**
 * Created by Chaithanya on 2/22/2017.
 */

public class Apps implements Serializable {

    String name;
    String price;
    String image;
    boolean favourite;

    @Override
    public String toString() {
        return "Apps{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +

                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
