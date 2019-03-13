package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/9/18.
 */

public class SaleDeliveryUploadFlagBean {

    /**
     * vbillcode : 0
     * vcooporderbcode_b : 查询成功
     * prodcutcode : 1
     */

    public String vbillcode;
    public String vcooporderbcode_b;
    public String prodcutcode;

    public void setVbillcode(String vbillcode) {
        this.vbillcode = vbillcode;
    }

    public void setVcooporderbcode_b(String vcooporderbcode_b) {
        this.vcooporderbcode_b = vcooporderbcode_b;
    }

    public void setProdcutcode(String prodcutcode) {
        this.prodcutcode = prodcutcode;
    }

    public String getVbillcode() {
        return vbillcode;
    }

    public String getVcooporderbcode_b() {
        return vcooporderbcode_b;
    }

    public String getProdcutcode() {
        return prodcutcode;
    }
}
