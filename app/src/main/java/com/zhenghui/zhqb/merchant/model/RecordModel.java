package com.zhenghui.zhqb.merchant.model;

/**
 * Created by LeiQ on 2017/6/10.
 */

public class RecordModel {


    /**
     * code : XF2017061016253791414890
     * userId : U201706071406231322783
     * storeCode : SJ2017060416155036789952
     * storeUserId : U201706041609037734313
     * price : 1000
     * createDatetime : Jun 10, 2017 4:25:37 PM
     * status : 1
     * payType : 1
     * payAmount2 : 0
     * payAmount3 : 1000
     * storeAmount : 750
     * payDatetime : Jun 10, 2017 4:25:37 PM
     * remark : 余额支付O2O消费
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     * store : {"code":"SJ2017060416155036789952","name":"安卓店铺","level":"2","type":"1","slogan":"广告语","advPic":"ANDROID_1496564031759_580_580.jpg","pic":"ANDROID_1496564058384_580_580.jpg||ANDROID_1496564063708_580_580.jpg||ANDROID_1496564073059_580_580.jpg","description":"哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈不哈哈哈哈哈哈哈哈哈那就是就是就是计算机技术就","province":"浙江省","city":"杭州市","area":"余杭区","address":"梦想小镇","longitude":"119.998089","latitude":"30.38812","bookMobile":"1234567","smsMobile":"18984955240","uiLocation":"1","uiOrder":"1","legalPersonName":"雷黔","userReferee":"U2017010713451027748","isDefault":"1","status":"2","updater":"xman","updateDatetime":"Jun 5, 2017 10:20:10 AM","createDatetime":"Jun 4, 2017 4:15:50 PM","approveUser":"xman","approveDatetime":"Jun 9, 2017 11:00:36 AM","onUser":"xman","onDatetime":"Jun 9, 2017 11:00:43 AM","offUser":"xman","offDatetime":"Jun 9, 2017 11:00:32 AM","owner":"U201706041609037734313","companyCode":"CD-CZH000001","systemCode":"CD-CZH000001"}
     * user : {"userId":"U201706071406231322783","kind":"f1","loginName":"18984955240","nickname":"31322783","mobile":"18984955240","identityFlag":"0","userReferee":"U2017010713451027748"}
     * storeUser : {"userId":"U201706041609037734313","kind":"f2","loginName":"18984955240","nickname":"37734313","mobile":"18984955240","identityFlag":"0","userReferee":""}
     */

    private String code;
    private String userId;
    private String storeCode;
    private String storeUserId;
    private int price;
    private String createDatetime;
    private String status;
    private String payType;
    private int payAmount2;
    private int payAmount3;
    private int storeAmount;
    private String payDatetime;
    private String remark;
    private String companyCode;
    private String systemCode;
    /**
     * code : SJ2017060416155036789952
     * name : 安卓店铺
     * level : 2
     * type : 1
     * slogan : 广告语
     * advPic : ANDROID_1496564031759_580_580.jpg
     * pic : ANDROID_1496564058384_580_580.jpg||ANDROID_1496564063708_580_580.jpg||ANDROID_1496564073059_580_580.jpg
     * description : 哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈不哈哈哈哈哈哈哈哈哈那就是就是就是计算机技术就
     * province : 浙江省
     * city : 杭州市
     * area : 余杭区
     * address : 梦想小镇
     * longitude : 119.998089
     * latitude : 30.38812
     * bookMobile : 1234567
     * smsMobile : 18984955240
     * uiLocation : 1
     * uiOrder : 1
     * legalPersonName : 雷黔
     * userReferee : U2017010713451027748
     * isDefault : 1
     * status : 2
     * updater : xman
     * updateDatetime : Jun 5, 2017 10:20:10 AM
     * createDatetime : Jun 4, 2017 4:15:50 PM
     * approveUser : xman
     * approveDatetime : Jun 9, 2017 11:00:36 AM
     * onUser : xman
     * onDatetime : Jun 9, 2017 11:00:43 AM
     * offUser : xman
     * offDatetime : Jun 9, 2017 11:00:32 AM
     * owner : U201706041609037734313
     * companyCode : CD-CZH000001
     * systemCode : CD-CZH000001
     */

    private StoreBean store;
    /**
     * userId : U201706071406231322783
     * kind : f1
     * loginName : 18984955240
     * nickname : 31322783
     * mobile : 18984955240
     * identityFlag : 0
     * userReferee : U2017010713451027748
     */

    private UserBean user;
    /**
     * userId : U201706041609037734313
     * kind : f2
     * loginName : 18984955240
     * nickname : 37734313
     * mobile : 18984955240
     * identityFlag : 0
     * userReferee :
     */

    private StoreUserBean storeUser;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreUserId() {
        return storeUserId;
    }

    public void setStoreUserId(String storeUserId) {
        this.storeUserId = storeUserId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public int getPayAmount2() {
        return payAmount2;
    }

    public void setPayAmount2(int payAmount2) {
        this.payAmount2 = payAmount2;
    }

    public int getPayAmount3() {
        return payAmount3;
    }

    public void setPayAmount3(int payAmount3) {
        this.payAmount3 = payAmount3;
    }

    public int getStoreAmount() {
        return storeAmount;
    }

    public void setStoreAmount(int storeAmount) {
        this.storeAmount = storeAmount;
    }

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public StoreBean getStore() {
        return store;
    }

    public void setStore(StoreBean store) {
        this.store = store;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public StoreUserBean getStoreUser() {
        return storeUser;
    }

    public void setStoreUser(StoreUserBean storeUser) {
        this.storeUser = storeUser;
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
        private String uiLocation;
        private String uiOrder;
        private String legalPersonName;
        private String userReferee;
        private String isDefault;
        private String status;
        private String updater;
        private String updateDatetime;
        private String createDatetime;
        private String approveUser;
        private String approveDatetime;
        private String onUser;
        private String onDatetime;
        private String offUser;
        private String offDatetime;
        private String owner;
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

        public String getCreateDatetime() {
            return createDatetime;
        }

        public void setCreateDatetime(String createDatetime) {
            this.createDatetime = createDatetime;
        }

        public String getApproveUser() {
            return approveUser;
        }

        public void setApproveUser(String approveUser) {
            this.approveUser = approveUser;
        }

        public String getApproveDatetime() {
            return approveDatetime;
        }

        public void setApproveDatetime(String approveDatetime) {
            this.approveDatetime = approveDatetime;
        }

        public String getOnUser() {
            return onUser;
        }

        public void setOnUser(String onUser) {
            this.onUser = onUser;
        }

        public String getOnDatetime() {
            return onDatetime;
        }

        public void setOnDatetime(String onDatetime) {
            this.onDatetime = onDatetime;
        }

        public String getOffUser() {
            return offUser;
        }

        public void setOffUser(String offUser) {
            this.offUser = offUser;
        }

        public String getOffDatetime() {
            return offDatetime;
        }

        public void setOffDatetime(String offDatetime) {
            this.offDatetime = offDatetime;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
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

    public static class UserBean {
        private String userId;
        private String kind;
        private String loginName;
        private String nickname;
        private String mobile;
        private String identityFlag;
        private String userReferee;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIdentityFlag() {
            return identityFlag;
        }

        public void setIdentityFlag(String identityFlag) {
            this.identityFlag = identityFlag;
        }

        public String getUserReferee() {
            return userReferee;
        }

        public void setUserReferee(String userReferee) {
            this.userReferee = userReferee;
        }
    }

    public static class StoreUserBean {
        private String userId;
        private String kind;
        private String loginName;
        private String nickname;
        private String mobile;
        private String identityFlag;
        private String userReferee;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIdentityFlag() {
            return identityFlag;
        }

        public void setIdentityFlag(String identityFlag) {
            this.identityFlag = identityFlag;
        }

        public String getUserReferee() {
            return userReferee;
        }

        public void setUserReferee(String userReferee) {
            this.userReferee = userReferee;
        }
    }
}
