package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */

public class PurchaseReturnBodyBean {


    private boolean isSelected = false;
    /**
     * itempk : 1001J11000000006KO95
     * materialname : A1塑料上盖
     * nnum : -8
     * warehouse : null
     * maccode : M01
     * materialcode : M01000102
     */

    public String itempk;
    public String materialname;
    public String nnum;
    public String maccode;
    public String materialcode;
    public String scannnum;
    public String uploadflag;

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String warehouse;
    public PurchaseReturnBodyBean(){
        super();
    }

    public PurchaseReturnBodyBean(boolean isSelected, String itempk, String materialname, String nnum, String maccode, String materialcode, String scannnum, String uploadflag,String warehouse) {
        this.isSelected = isSelected;
        this.itempk = itempk;
        this.materialname = materialname;
        this.nnum = nnum;
        this.maccode = maccode;
        this.materialcode = materialcode;
        this.scannnum = scannnum;
        this.uploadflag = uploadflag;
        this.warehouse=warehouse;
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

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
    }

    public String getNnum() {
        return nnum;
    }

    public void setNnum(String nnum) {
        this.nnum = nnum;
    }

    public String getMaccode() {
        return maccode;
    }

    public void setMaccode(String maccode) {
        this.maccode = maccode;
    }

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    public String getScannnum() {
        return scannnum;
    }

    public void setScannnum(String scannnum) {
        this.scannnum = scannnum;
    }

    public String getUploadflag() {
        return uploadflag;
    }

    public void setUploadflag(String uploadflag) {
        this.uploadflag = uploadflag;
    }
}
