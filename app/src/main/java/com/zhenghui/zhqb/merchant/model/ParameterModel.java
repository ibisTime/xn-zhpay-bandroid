package com.zhenghui.zhqb.merchant.model;

/**
 * Created by LeiQ on 2017/4/20.
 */

public class ParameterModel {


    /**
     * code : PS201704201712514166
     * productCode : CP201704201702138532
     * dkey : key1
     * dvalue : value1
     * orderNo : 0
     * companyCode : U2017040718104693069
     * systemCode : CD-CZH000001
     */

    private String code;
    private String productCode;
    private String dkey = "";
    private String dvalue = "";
    private boolean isExist = false;
    private int orderNo;
    private String companyCode;
    private String systemCode;

    public boolean isExist() {
        return isExist;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getDkey() {
        return dkey;
    }

    public void setDkey(String dkey) {
        this.dkey = dkey;
    }

    public String getDvalue() {
        return dvalue;
    }

    public void setDvalue(String dvalue) {
        this.dvalue = dvalue;
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
