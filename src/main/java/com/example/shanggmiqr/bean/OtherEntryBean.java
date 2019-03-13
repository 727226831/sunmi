package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */

public class OtherEntryBean {

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
    private boolean isSelected = false;
    //private Boolean operation;    //用于记录是否被选中的状态

    public OtherEntryBean(){
        super();
    }

    public OtherEntryBean(String pobillcode, String cwarecode, String cwarename,int dr, String dbilldate) {
        super();
        this.pobillcode = pobillcode;
        this.cwarecode = cwarecode;
        this.cwarename = cwarename;
        this.dr = dr;
        this.dbilldate =dbilldate;
    }
//
//    public OtherOutgoingBean(String pobillcode, String cwarecode, String cwarename, boolean operation) {
//        this.pobillcode = pobillcode;
//        this.cwarecode = cwarecode;
//        this.cwarename = cwarename;
//        this.operation = operation;
//    }

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
