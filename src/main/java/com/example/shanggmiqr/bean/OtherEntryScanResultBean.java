package com.example.shanggmiqr.bean;

/**
 * Created by Administrator on 2018/8/30 0030.
 */

public class OtherEntryScanResultBean {


    /**
     * pobillcode : 出库单号
     * cwarename : 仓库名称
     * materialcode : 物料编号
     * platecode : 托盘码
     * boxcode : 箱码
     * prodcutcode : 产品码
     */

    public String itemuploadflag;
    public String platecode;
    public String boxcode;
    public String prodcutcode;



    public String getItemuploadflag() {
        return itemuploadflag;
    }

    public void setItemuploadflag(String itemuploadflag) {
        this.itemuploadflag = itemuploadflag;
    }

    public String getPlatecode() {
        return platecode;
    }

    public void setPlatecode(String platecode) {
        this.platecode = platecode;
    }

    public String getBoxcode() {
        return boxcode;
    }

    public void setBoxcode(String boxcode) {
        this.boxcode = boxcode;
    }

    public String getProdcutcode() {
        return prodcutcode;
    }

    public void setProdcutcode(String prodcutcode) {
        this.prodcutcode = prodcutcode;
    }

}
