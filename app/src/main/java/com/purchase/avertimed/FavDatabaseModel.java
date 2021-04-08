package com.purchase.avertimed;

public  class FavDatabaseModel {
    private String favName;
    private String id;
    private String image_url;
    private String price;
    private String QT;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public void setFavName(String favName) {
        this.favName = favName;
    }

    public String getFavName() {
        return favName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setQT(String qt) {
        this.QT = qt;
    }

    public String getQT() {
        return QT;
    }
}
