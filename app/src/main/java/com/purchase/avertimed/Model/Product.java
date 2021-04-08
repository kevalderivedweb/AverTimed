package com.purchase.avertimed.Model;

public class Product {

    String ProductID;
    String ProductTitleEn;
    String ProductTitleFr;

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductTitleEn() {
        return ProductTitleEn;
    }

    public void setProductTitleEn(String productTitleEn) {
        ProductTitleEn = productTitleEn;
    }

    public String getProductTitleFr() {
        return ProductTitleFr;
    }

    public void setProductTitleFr(String productTitleFr) {
        ProductTitleFr = productTitleFr;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    String ProductCode;
    String Price;
    String ProductImage;
}
