package com.zhenghui.zhqb.merchant.model;

import java.util.List;

/**
 * Created by dell1 on 2016/12/17.
 */

public class OrderModel {


    /**
     * code : DD201612172045542310
     * type : 1
     * receiver :
     * reMobile :
     * reAddress : 浙江 杭州 余杭 哼哼唧唧就
     * receiptType :
     * receiptTitle :
     * applyUser : U2016121720245823318
     * applyNote :
     * applyDatetime : Dec 17, 2016 8:45:54 PM
     * amount1 : 0
     * amount2 : 0
     * amount3 : 0
     * payAmount1 : 0
     * payAmount2 : 0
     * payAmount3 : 0
     * yunfei : 0
     * payDatetime : Dec 17, 2016 8:45:56 PM
     * status : 3
     * updater : admin
     * updateDatetime : Dec 17, 2016 8:46:11 PM
     * logisticsCode : After
     * logisticsCompany : After
     * deliverer : U2016121720235973132
     * promptTimes : 0
     * companyCode : U2016121720235973132
     * systemCode : CD-CZH000001
     * mobile : 18984955240
     * productOrderList : [{"code":"CD201612172045542349","orderCode":"DD201612172045542310","productCode":"CP201612172034444143","quantity":1,"price1":0,"price2":0,"price3":0,"productName":"Help--001","advPic":"IOS_1481978079642599_4288_2848.jpg"}]
     */

    private String code;
    private String type;
    private String receiver;
    private String reMobile;
    private String reAddress;
    private String receiptType;
    private String receiptTitle;
    private String applyUser;
    private String applyNote;
    private String applyDatetime;
    private double amount1;
    private int amount2;
    private int amount3;
    private double payAmount1;
    private int payAmount2;
    private int payAmount3;
    private int yunfei;
    private String payDatetime;
    private String status;
    private String updater;
    private String updateDatetime;
    private String logisticsCode;
    private String logisticsCompany;
    private String deliverer;
    private int promptTimes;
    private String companyCode;
    private String systemCode;
    private String mobile;
    /**
     * code : CD201612172045542349
     * orderCode : DD201612172045542310
     * productCode : CP201612172034444143
     * quantity : 1
     * price1 : 0
     * price2 : 0
     * price3 : 0
     * productName : Help--001
     * advPic : IOS_1481978079642599_4288_2848.jpg
     */

    private List<ProductOrderListBean> productOrderList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReMobile() {
        return reMobile;
    }

    public void setReMobile(String reMobile) {
        this.reMobile = reMobile;
    }

    public String getReAddress() {
        return reAddress;
    }

    public void setReAddress(String reAddress) {
        this.reAddress = reAddress;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getApplyNote() {
        return applyNote;
    }

    public void setApplyNote(String applyNote) {
        this.applyNote = applyNote;
    }

    public String getApplyDatetime() {
        return applyDatetime;
    }

    public void setApplyDatetime(String applyDatetime) {
        this.applyDatetime = applyDatetime;
    }

    public double getAmount1() {
        return amount1;
    }

    public void setAmount1(double amount1) {
        this.amount1 = amount1;
    }

    public int getAmount2() {
        return amount2;
    }

    public void setAmount2(int amount2) {
        this.amount2 = amount2;
    }

    public int getAmount3() {
        return amount3;
    }

    public void setAmount3(int amount3) {
        this.amount3 = amount3;
    }

    public double getPayAmount1() {
        return payAmount1;
    }

    public void setPayAmount1(double payAmount1) {
        this.payAmount1 = payAmount1;
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

    public int getYunfei() {
        return yunfei;
    }

    public void setYunfei(int yunfei) {
        this.yunfei = yunfei;
    }

    public String getPayDatetime() {
        return payDatetime;
    }

    public void setPayDatetime(String payDatetime) {
        this.payDatetime = payDatetime;
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

    public String getLogisticsCode() {
        return logisticsCode;
    }

    public void setLogisticsCode(String logisticsCode) {
        this.logisticsCode = logisticsCode;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(String deliverer) {
        this.deliverer = deliverer;
    }

    public int getPromptTimes() {
        return promptTimes;
    }

    public void setPromptTimes(int promptTimes) {
        this.promptTimes = promptTimes;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<ProductOrderListBean> getProductOrderList() {
        return productOrderList;
    }

    public void setProductOrderList(List<ProductOrderListBean> productOrderList) {
        this.productOrderList = productOrderList;
    }

    public static class ProductOrderListBean {
        private String code;
        private String orderCode;
        private String productCode;
        private int quantity;
        private double price1;
        private int price2;
        private int price3;
        private String productName;
        private String advPic;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice1() {
            return price1;
        }

        public void setPrice1(double price1) {
            this.price1 = price1;
        }

        public int getPrice2() {
            return price2;
        }

        public void setPrice2(int price2) {
            this.price2 = price2;
        }

        public int getPrice3() {
            return price3;
        }

        public void setPrice3(int price3) {
            this.price3 = price3;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getAdvPic() {
            return advPic;
        }

        public void setAdvPic(String advPic) {
            this.advPic = advPic;
        }
    }
}
