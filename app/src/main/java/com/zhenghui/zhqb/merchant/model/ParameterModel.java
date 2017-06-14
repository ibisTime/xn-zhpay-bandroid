package com.zhenghui.zhqb.merchant.model;

import java.io.Serializable;

/**
 * Created by LeiQ on 2017/4/20.
 */

public class ParameterModel implements Serializable {


    /**
     * code : PS201706062244320112782
     * name : 土豪金
     * productCode : CP2017060618304790037168
     * price1 : 1000
     * price2 : 1000
     * price3 : 1000
     * status : 1
     * quantity : 1
     * province : 浙江
     * weight : 1
     * orderNo : 1
     * companyCode : U201706041609037734313
     * systemCode : CD-CZH000001
     */

    private String code;
    private String name;
    private String productCode;
    private double price1;
    private double price2;
    private double price3;
    private String status;
    private int quantity;
    private String province;
    private double weight;
    private int orderNo;
    private String companyCode;
    private String systemCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public double getPrice1() {
        return price1;
    }

    public void setPrice1(double price1) {
        this.price1 = price1;
    }

    public double getPrice2() {
        return price2;
    }

    public void setPrice2(double price2) {
        this.price2 = price2;
    }

    public double getPrice3() {
        return price3;
    }

    public void setPrice3(double price3) {
        this.price3 = price3;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
