package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/9/18.
 */

public class AllocateTransferUploadFlagBean {

    /**
     * vbillcode : 0
     * vcooporderbcode_b : 查询成功
     * prodcutcode : 1
     */

    public String billno;
    public String itempk;
    public String prodcutcode;

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public void setItempk(String itempk) {
        this.itempk = itempk;
    }

    public void setProdcutcode(String prodcutcode) {
        this.prodcutcode = prodcutcode;
    }

    public String getItempk() {
        return itempk;
    }

    public String getProdcutcode() {
        return prodcutcode;
    }
}
