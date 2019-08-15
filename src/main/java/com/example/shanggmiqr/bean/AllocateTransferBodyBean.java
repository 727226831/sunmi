package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */

public class AllocateTransferBodyBean {


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
    public String address;
    public String materialcode;
    public String materialclasscode;
    public String nnum="0";
    public String rwarehousecode;
    public String cwarehousecode;

    public String getMaccode() {
        return maccode;
    }

    public void setMaccode(String maccode) {
        this.maccode = maccode;
    }

    private String maccode;
    public String uploadflag;
    public String scannum;
    public AllocateTransferBodyBean(){
        super();
    }

    public AllocateTransferBodyBean(String itempk, String address, String materialcode, String materialclasscode, String nnum, String rwarehousecode, String cwarehousecode, String uploadflag, String scannum) {
        super();
        this.itempk = itempk;
        this.address = address;
        this.materialcode = materialcode;
        this.materialclasscode = materialclasscode;
        this.nnum = nnum;
        this.rwarehousecode = rwarehousecode;
        this.cwarehousecode = cwarehousecode;
        this.uploadflag = uploadflag;
        this.scannum = scannum;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getItempk() {
        return itempk;
    }

    public void setItempk(String itempk) {
        this.itempk = itempk;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    public String getMaterialclasscode() {
        return materialclasscode;
    }

    public void setMaterialclasscode(String materialclasscode) {
        this.materialclasscode = materialclasscode;
    }

    public String getNnum() {
        return nnum;
    }

    public void setNnum(String nnum) {
        this.nnum = nnum;
    }

    public String getRwarehousecode() {
        return rwarehousecode;
    }

    public void setRwarehousecode(String rwarehousecode) {
        this.rwarehousecode = rwarehousecode;
    }

    public String getCwarehousecode() {
        return cwarehousecode;
    }

    public void setCwarehousecode(String cwarehousecode) {
        this.cwarehousecode = cwarehousecode;
    }

    public String getUploadflag() {
        return uploadflag;
    }

    public void setUploadflag(String uploadflag) {
        this.uploadflag = uploadflag;
    }

    public String getScannum() {
        return scannum;
    }

    public void setScannum(String scannum) {
        this.scannum = scannum;
    }
}
