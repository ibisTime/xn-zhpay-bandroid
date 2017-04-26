package com.zhenghui.zhqb.merchant.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dell1 on 2016/12/15.
 */

public class ProductModel implements Serializable {


    /**
     * code : CP201703291727333540
     * category : 0
     * type : FL201700000000000001
     * name : we bare bears
     * slogan : 广告语
     * advPic : ANDROID_1490779593774_612_344.jpg
     * pic : ANDROID_1490779659224_421_750.jpg
     * description : 三只可以一起带走啦，三只可以一起带走啦，三只可以一起带走啦
     * status : 0
     * updater : U2017032914155538289
     * updateDatetime : Mar 29, 2017 5:27:33 PM
     * boughtCount : 0
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     * productSpecs : []
     */

    private String code;
    private String category;
    private String type;
    private String name;
    private String slogan;
    private double price1;
    private double price2;
    private double price3;
    private String advPic;
    private String pic;
    private String description;
    private String status;
    private String updater;
    private String updateDatetime;
    private int boughtCount;
    private String companyCode;
    private String remark;
    private String systemCode;
    private List<?> productSpecs;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        remark = remark;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getAdvPic() {
        return advPic;
    }

    public void setAdvPic(String advPic) {
        this.advPic = advPic;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public String getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public int getBoughtCount() {
        return boughtCount;
    }

    public void setBoughtCount(int boughtCount) {
        this.boughtCount = boughtCount;
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

    public List<?> getProductSpecs() {
        return productSpecs;
    }

    public void setProductSpecs(List<?> productSpecs) {
        this.productSpecs = productSpecs;
    }
}
