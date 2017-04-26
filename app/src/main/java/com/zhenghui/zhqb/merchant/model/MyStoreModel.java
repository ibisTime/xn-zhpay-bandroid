package com.zhenghui.zhqb.merchant.model;

/**
 * Created by LeiQ on 2016/12/29.
 */

public class MyStoreModel {


    /**
     * code : SJ201703271539202900
     * name : 我们一起去看大海吧
     * level : 1
     * type : 10
     * slogan : 广告语
     * advPic : ANDROID_1490600286665_459_816.jpg
     * pic : ANDROID_1490600356956_459_816.jpg||ANDROID_1490600361739_459_816.jpg
     * description : 店铺描述必须要20个字，店铺描述必须要20个字，店铺描述必须要20个字，店铺描述必须要20个字，
     * province : 浙江省
     * city : 杭州市
     * area : 余杭区
     * address : 梦想小镇天使村
     * longitude : 120.00185698270799
     * latitude : 30.288363066307237
     * bookMobile : 18984955240
     * smsMobile : 18984955240
     * pdf :
     * uiLocation : 2
     * uiOrder : 1
     * legalPersonName :
     * userReferee : U2017032715081935237
     * rate1 : 0
     * rate2 : 0.1
     * rate3 : 0.1
     * isDefault : 1
     * status : 1
     * updater : CCG201600000000000001
     * updateDatetime : Mar 28, 2017 10:42:21 AM
     * owner : U2017032711071985293
     * contractNo : ZHS-201703271610258851
     * totalRmbNum : 0
     * totalJfNum : 0
     * totalDzNum : 0
     * totalScNum : 0
     * companyCode : CD-CCG000007
     * systemCode : CD-CCG000007
     * refereeMobile : 18767101909
     */

    private StoreBean store;
    /**
     * store : {"code":"SJ201703271539202900","name":"我们一起去看大海吧","level":"1","type":"10","slogan":"广告语","advPic":"ANDROID_1490600286665_459_816.jpg","pic":"ANDROID_1490600356956_459_816.jpg||ANDROID_1490600361739_459_816.jpg","description":"店铺描述必须要20个字，店铺描述必须要20个字，店铺描述必须要20个字，店铺描述必须要20个字，","province":"浙江省","city":"杭州市","area":"余杭区","address":"梦想小镇天使村","longitude":"120.00185698270799","latitude":"30.288363066307237","bookMobile":"18984955240","smsMobile":"18984955240","pdf":"","uiLocation":"2","uiOrder":"1","legalPersonName":"","userReferee":"U2017032715081935237","rate1":0,"rate2":0.1,"rate3":0.1,"isDefault":"1","status":"1","updater":"CCG201600000000000001","updateDatetime":"Mar 28, 2017 10:42:21 AM","owner":"U2017032711071985293","contractNo":"ZHS-201703271610258851","totalRmbNum":0,"totalJfNum":0,"totalDzNum":0,"totalScNum":0,"companyCode":"CD-CCG000007","systemCode":"CD-CCG000007","refereeMobile":"18767101909"}
     * totalIncome : 0
     */

    private int totalIncome;

    public StoreBean getStore() {
        return store;
    }

    public void setStore(StoreBean store) {
        this.store = store;
    }

    public int getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(int totalIncome) {
        this.totalIncome = totalIncome;
    }

    public static class StoreBean {
        private String code;
        private String name;
        private String level;
        private String type;
        private String slogan;
        private String advPic;
        private String pic;
        private String description;
        private String province;
        private String city;
        private String area;
        private String address;
        private String longitude;
        private String latitude;
        private String bookMobile;
        private String smsMobile;
        private String pdf;
        private String uiLocation;
        private String uiOrder;
        private String legalPersonName;
        private String userReferee;
        private double rate1;
        private double rate2;
        private double rate3;
        private String isDefault;
        private String status;
        private String updater;
        private String updateDatetime;
        private String owner;
        private String remark;
        private String contractNo;
        private double totalRmbNum;
        private double totalJfNum;
        private double totalDzNum;
        private double totalScNum;
        private String companyCode;
        private String systemCode;
        private String refereeMobile;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getBookMobile() {
            return bookMobile;
        }

        public void setBookMobile(String bookMobile) {
            this.bookMobile = bookMobile;
        }

        public String getSmsMobile() {
            return smsMobile;
        }

        public void setSmsMobile(String smsMobile) {
            this.smsMobile = smsMobile;
        }

        public String getPdf() {
            return pdf;
        }

        public void setPdf(String pdf) {
            this.pdf = pdf;
        }

        public String getUiLocation() {
            return uiLocation;
        }

        public void setUiLocation(String uiLocation) {
            this.uiLocation = uiLocation;
        }

        public String getUiOrder() {
            return uiOrder;
        }

        public void setUiOrder(String uiOrder) {
            this.uiOrder = uiOrder;
        }

        public String getLegalPersonName() {
            return legalPersonName;
        }

        public void setLegalPersonName(String legalPersonName) {
            this.legalPersonName = legalPersonName;
        }

        public String getUserReferee() {
            return userReferee;
        }

        public void setUserReferee(String userReferee) {
            this.userReferee = userReferee;
        }

        public double getRate1() {
            return rate1;
        }

        public void setRate1(double rate1) {
            this.rate1 = rate1;
        }

        public double getRate2() {
            return rate2;
        }

        public void setRate2(double rate2) {
            this.rate2 = rate2;
        }

        public double getRate3() {
            return rate3;
        }

        public void setRate3(double rate3) {
            this.rate3 = rate3;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
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

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getContractNo() {
            return contractNo;
        }

        public void setContractNo(String contractNo) {
            this.contractNo = contractNo;
        }

        public double getTotalRmbNum() {
            return totalRmbNum;
        }

        public void setTotalRmbNum(double totalRmbNum) {
            this.totalRmbNum = totalRmbNum;
        }

        public double getTotalJfNum() {
            return totalJfNum;
        }

        public void setTotalJfNum(double totalJfNum) {
            this.totalJfNum = totalJfNum;
        }

        public double getTotalDzNum() {
            return totalDzNum;
        }

        public void setTotalDzNum(double totalDzNum) {
            this.totalDzNum = totalDzNum;
        }

        public double getTotalScNum() {
            return totalScNum;
        }

        public void setTotalScNum(double totalScNum) {
            this.totalScNum = totalScNum;
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

        public String getRefereeMobile() {
            return refereeMobile;
        }

        public void setRefereeMobile(String refereeMobile) {
            this.refereeMobile = refereeMobile;
        }
    }
}
