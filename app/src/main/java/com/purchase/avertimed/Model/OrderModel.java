package com.purchase.avertimed.Model;

import java.util.ArrayList;

public class OrderModel {

    String UserOrderID;
    String OrderCode;
    String OrderAmount;
    String Tax;

    public String getUserOrderID() {
        return UserOrderID;
    }

    public void setUserOrderID(String userOrderID) {
        UserOrderID = userOrderID;
    }

    public String getOrderCode() {
        return OrderCode;
    }

    public void setOrderCode(String orderCode) {
        OrderCode = orderCode;
    }

    public String getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        OrderAmount = orderAmount;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getShippingCharge() {
        return ShippingCharge;
    }

    public void setShippingCharge(String shippingCharge) {
        ShippingCharge = shippingCharge;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    String Discount;
    String ShippingCharge;
    String TotalAmount;
    String OrderStatus;
    String date;
    ArrayList<Product> products;

}
