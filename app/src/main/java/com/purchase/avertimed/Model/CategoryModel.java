package com.purchase.avertimed.Model;

public class CategoryModel {

    public int cat_id;
    public String cat_name_en;
    public String txt_price;
    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTxt_price() {
        return txt_price;
    }

    public void setTxt_price(String txt_price) {
        this.txt_price = txt_price;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name_en() {
        return cat_name_en;
    }

    public void setCat_name_en(String cat_name_en) {
        this.cat_name_en = cat_name_en;
    }

    public String getCat_name_fr() {
        return cat_name_fr;
    }

    public void setCat_name_fr(String cat_name_fr) {
        this.cat_name_fr = cat_name_fr;
    }

    public String getCat_name_image() {
        return cat_name_image;
    }

    public void setCat_name_image(String cat_name_image) {
        this.cat_name_image = cat_name_image;
    }

    public String cat_name_fr;
    public String cat_name_image;
}
