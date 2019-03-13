package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */

public class LoanBodyBean {


    private boolean isSelected = false;
    public String materialcode;
    public String nnum;
    public String itempk;
    public String cwarename;
    public String vemo;
    public String uploadflag;
    public String scannum;
    public LoanBodyBean(){
        super();
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public LoanBodyBean(String materialcode, String maccode, String nnum, String itempk, String cwarename, String vemo, String uploadflag, String scannum) {
        this.materialcode = materialcode;
        this.nnum = nnum;
        this.itempk = itempk;
        this.cwarename = cwarename;
        this.vemo = vemo;
        this.uploadflag = uploadflag;
        this.scannum = scannum;
    }

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    public String getNnum() {
        return nnum;
    }

    public void setNnum(String nnum) {
        this.nnum = nnum;
    }

    public String getItempk() {
        return itempk;
    }

    public void setItempk(String itempk) {
        this.itempk = itempk;
    }

    public String getCwarename() {
        return cwarename;
    }

    public void setCwarename(String cwarename) {
        this.cwarename = cwarename;
    }

    public String getVemo() {
        return vemo;
    }

    public void setVemo(String vemo) {
        this.vemo = vemo;
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
