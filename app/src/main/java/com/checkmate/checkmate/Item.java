package com.checkmate.checkmate;


/**
 * Created by Brady on 10/27/2016.
 */

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Item {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("hf")
    @Expose
    private String hf;
    @SerializedName("uhf")
    @Expose
    private String uhf;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("purchased")
    @Expose
    private String purchased;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHf() {
        return hf;
    }
/*
    public void setHf(String hf) {
        this.hf = hf;
    }

    public String getUhf() {
        return uhf;
    }

    public void setUhf(String uhf) {
        this.uhf = uhf;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }
/*
    public void setPrice(String price) {
        this.price = price;
    }

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }*/

}


