package com.example.shanggmiqr.bean;

/**
 * Created by Administrator on 2018/8/30 0030.
 */

public class SaleDeliveryScanResultBean {


    /**
     * matrcode : 物料编码
     * platecode : 托盘码
     * boxcode : 箱码
     * prodcutcode : 产品码
     * num : 1
     */

    public String matrcode;
    public String platecode;
    public String boxcode;
    public String prodcutcode;
    public String num;
    public String itemuploadflag;

    public void setMatrcode(String matrcode) {
        this.matrcode = matrcode;
    }

    public void setPlatecode(String platecode) {
        this.platecode = platecode;
    }

    public void setBoxcode(String boxcode) {
        this.boxcode = boxcode;
    }

    public void setProdcutcode(String prodcutcode) {
        this.prodcutcode = prodcutcode;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public void setItemuploadflag(String itemuploadflag) {
        this.itemuploadflag = itemuploadflag;
    }

    public String getMatrcode() {
        return matrcode;
    }

    public String getPlatecode() {
        return platecode;
    }

    public String getBoxcode() {
        return boxcode;
    }

    public String getProdcutcode() {
        return prodcutcode;
    }

    public String getNum() {
        return num;
    }

    public String getItemuploadflag() {
        return itemuploadflag;
    }
}
