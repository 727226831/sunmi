package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/9/18.
 */

public class ProductEntryUploadFlagBean {

    /**
     * vbillcode : 0
     * vcooporderbcode_b : 查询成功
     * prodcutcode : 1
     */

    public String billcode;
    public String itempk;
    public String prodcutcode;

    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }

    public void setItempk(String itempk) {
        this.itempk = itempk;
    }

    public void setProdcutcode(String prodcutcode) {
        this.prodcutcode = prodcutcode;
    }

    public String getBillcode() {
        return billcode;
    }

    public String getItempk() {
        return itempk;
    }

    public String getProdcutcode() {
        return prodcutcode;
    }
}
