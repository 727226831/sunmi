package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */

public class OtherOutgoingQrDetailBean {
    /**
     * prodcutcode : 产品码
     * boxcode : 箱码
     * platecode : 托盘码
     */

    public String prodcutcode;
    public String boxcode;
    public String platecode;
    public String itemuploadflag;

    public void setProdcutcode(String prodcutcode) {
        this.prodcutcode = prodcutcode;
    }

    public void setBoxcode(String boxcode) {
        this.boxcode = boxcode;
    }

    public void setPlatecode(String platecode) {
        this.platecode = platecode;
    }
    public void setItemuploadflag(String itemuploadflag) {
        this.itemuploadflag = itemuploadflag;
    }

    public String getItemuploadflag() {
        return itemuploadflag;
    }
    public String getProdcutcode() {
        return prodcutcode;
    }

    public String getBoxcode() {
        return boxcode;
    }

    public String getPlatecode() {
        return platecode;
    }
}
