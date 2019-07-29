package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */

public class ProductEntryBodyBean {


    private boolean isSelected = false;
    /**
     * vcooporderbcode_b : 10
     * matrcode : P06010002
     * matrname : WCDMA无线数据终端-商米V1
     * maccode : P0601
     * nnum : 20
     * rackcode :
     * Customer : C003925
     */

    public String itempk;
    public String materialcode;
    public String nnum;
    public String ysnum;
    public String uploadflag;
    public String scannum;

    public String getMaccode() {
        return maccode;
    }

    public void setMaccode(String maccode) {
        this.maccode = maccode;
    }

    private String maccode;
    public ProductEntryBodyBean(){
        super();
    }

    public ProductEntryBodyBean(String itempk, String materialcode, String nnum, String ysnum, String uploadflag,String scannum) {
        super();
        this.itempk = itempk;
        this.materialcode = materialcode;
        this.nnum = nnum;
        this.ysnum = ysnum;
        this.uploadflag = uploadflag;
        this.scannum = scannum;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setItempk(String itempk) {
        this.itempk = itempk;
    }
    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    public void setNnum(String nnum) {
        this.nnum = nnum;
    }

    public void setYsnum(String ysnum) {
        this.ysnum = ysnum;
    }

    public void setUploadflag(String uploadflag) {
        this.uploadflag = uploadflag;
    }

    public void setScannum(String scannum) {
        this.scannum = scannum;
    }

    public String getItempk() {
        return itempk;
    }

    public String getMaterialcode() {
        return materialcode;
    }

    public String getNnum() {
        return nnum;
    }

    public String getYsnum() {
        return ysnum;
    }

    public String getUploadflag() {
        return uploadflag;
    }

    public String getScannum() {
        return scannum;
    }
}
