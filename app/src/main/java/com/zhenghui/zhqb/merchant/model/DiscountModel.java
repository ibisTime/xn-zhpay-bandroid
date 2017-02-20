package com.zhenghui.zhqb.merchant.model;

/**
 * Created by LeiQ on 2016/12/30.
 */

public class DiscountModel {


    /**
     * code : ZKQ201612182138594649
     * name : 双update
     * type : 1
     * key1 : 30000
     * key2 : 5000
     * description : 使用详情
     * price : 500
     * currency : CNY
     * validateStart : May 3, 2016 12:00:00 AM
     * validateEnd : May 4, 2016 12:00:00 AM
     * createDatetime : Dec 18, 2016 9:38:59 PM
     * status : 0
     * storeCode : 店铺编号
     * systemCode : 系统编号
     */

    private String code;
    private String name;
    private String type;
    private int key1;
    private int key2;
    private String description;
    private int price;
    private String currency;
    private String validateStart;
    private String validateEnd;
    private String createDatetime;
    private String status;
    private String storeCode;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getKey1() {
        return key1;
    }

    public void setKey1(int key1) {
        this.key1 = key1;
    }

    public int getKey2() {
        return key2;
    }

    public void setKey2(int key2) {
        this.key2 = key2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValidateStart() {
        return validateStart;
    }

    public void setValidateStart(String validateStart) {
        this.validateStart = validateStart;
    }

    public String getValidateEnd() {
        return validateEnd;
    }

    public void setValidateEnd(String validateEnd) {
        this.validateEnd = validateEnd;
    }

    public String getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(String createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }
}
