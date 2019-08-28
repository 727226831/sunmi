package com.example.shanggmiqr.bean;

import com.facebook.stetho.Stetho;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class PurchaseArrivalBean {
//第一个界面只显示两个

    /**
     * vtrantypecode : 4331-Cxx-04
     * unitcode : 4331-Cxx-04
     * busitypecode : 4331-Cxx-04
     * vbillcode : DN2018060400000023
     * dbilldate : 2018-06-04
     * deptcode :
     * Pupsndoccode :
     * Transporttypecode :
     * billmakercode : 4331-Cxx-04
     * vmemo :
     * country : CN
     */


    public String vbillcode;
    public String dbilldate;
    public int dr;
   private String materialcode;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String flag;

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    public String getMaterialname() {
        return materialname;
    }

    public void setMaterialname(String materialname) {
        this.materialname = materialname;
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

    private String materialname;
   private String maccode;
   private String nnum;
   private String prodcutcode;
   private String xlh;


    public boolean isSelected = false;
    public PurchaseArrivalBean(){
        super();
    }
    public PurchaseArrivalBean(String vbillcode, String dbilldate, int dr) {
        super();
        this.vbillcode = vbillcode;
        this.dbilldate = dbilldate;
        this.dr = dr;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public void setVbillcode(String vbillcode) {
        this.vbillcode = vbillcode;
    }

    public void setDbilldate(String dbilldate) {
        this.dbilldate = dbilldate;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public String getVbillcode() {
        return vbillcode;
    }

    public String getDbilldate() {
        return dbilldate;
    }

    public int getDr() {
        return dr;
    }

}
