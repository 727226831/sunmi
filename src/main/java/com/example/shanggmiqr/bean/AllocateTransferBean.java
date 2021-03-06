package com.example.shanggmiqr.bean;

import com.facebook.stetho.Stetho;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class AllocateTransferBean {
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


    public String billno;
    public String dbilldate;
    public int dr;
    private String maccode;
    private String nnum;

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

    private String prodcutcode;
    private String xlh;

    public String getMaterialclasscode() {
        return materialclasscode;
    }

    public void setMaterialclasscode(String materialclasscode) {
        this.materialclasscode = materialclasscode;
    }

    private String materialclasscode;

    public String getRwarehousecode() {
        return rwarehousecode;
    }

    public void setRwarehousecode(String rwarehousecode) {
        this.rwarehousecode = rwarehousecode;
    }

    private String rwarehousecode;

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    private String materialcode;

    public boolean isSelected = false;
    public AllocateTransferBean(){
        super();
    }
    public AllocateTransferBean(String billno, String dbilldate, int dr) {
        super();
        this.billno = billno;
        this.dbilldate = dbilldate;
        this.dr = dr;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public void setBillno(String billcode) {
        this.billno = billno;
    }

    public void setDbilldate(String dbilldate) {
        this.dbilldate = dbilldate;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public String getBillno() {
        return billno;
    }

    public String getDbilldate() {
        return dbilldate;
    }

    public int getDr() {
        return dr;
    }

}
