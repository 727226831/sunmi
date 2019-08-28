package com.example.shanggmiqr.bean;

public class OtherBean {
    /**
     * pobillcode : TC2018063000000247
     * cwarecode : 0145
     * cwarename : 叮当猫
     */

    public String pobillcode;
    public String cwarecode;
    public String cwarename;
    public String dbilldate;
    public int dr;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String flag;
    private boolean isSelected = false;

    String matrname;

    public String getMatrname() {
        return matrname;
    }

    public void setMatrname(String matrname) {
        this.matrname = matrname;
    }

    public String getMaccode() {
        return maccode;
    }

    public void setMaccode(String maccode) {
        this.maccode = maccode;
    }

    public String getNnum() {
        return nnum;
    }

    public void setNnum(String nnum) {
        this.nnum = nnum;
    }

    public String getProdcutcode() {
        return prodcutcode;
    }

    public void setProdcutcode(String prodcutcode) {
        this.prodcutcode = prodcutcode;
    }

    public String getXlh() {
        return xlh;
    }

    public void setXlh(String xlh) {
        this.xlh = xlh;
    }

    String maccode;
    String nnum;
    String prodcutcode;
    String xlh;

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    String materialcode;

    //private Boolean operation;    //用于记录是否被选中的状态

    public OtherBean(){
        super();
    }

    public OtherBean(String pobillcode, String cwarecode, String cwarename,int dr, String dbilldate) {
        super();
        this.pobillcode = pobillcode;
        this.cwarecode = cwarecode;
        this.cwarename = cwarename;
        this.dr = dr;
        this.dbilldate =dbilldate;
    }


    public void setPobillcode(String pobillcode) {
        this.pobillcode = pobillcode;
    }

    public void setCwarecode(String cwarecode) {
        this.cwarecode = cwarecode;
    }

    public void setCwarename(String cwarename) {
        this.cwarename = cwarename;
    }

    public void setDbilldate(String dbilldate) {
        this.dbilldate = dbilldate;
    }
    public void setDr(int dr) {
        this.dr = dr;
    }

    public int getDr() {
        return dr;
    }
    public String getPobillcode() {
        return pobillcode;
    }

    public String getCwarecode() {
        return cwarecode;
    }

    public String getCwarename() {
        return cwarename;
    }

    public String getDbilldate() {
        return dbilldate;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
