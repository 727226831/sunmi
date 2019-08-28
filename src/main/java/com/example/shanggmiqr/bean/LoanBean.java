package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class LoanBean {
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


    public String pobillcode;
    public String dbilldate;
    public String dr;
    private String materialname;

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    private String materialcode;

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

    private String maccode;
    private String nnum;
    private String prodcutcode;
    private String xlh;
    public boolean isSelected = false;
    public LoanBean(){
        super();
    }
    public LoanBean(String vbillcode, String dbilldate, String dr) {
        super();
        this.pobillcode = vbillcode;
        this.dbilldate = dbilldate;
        this.dr = dr;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public void setPobillcode(String pobillcode) {
        this.pobillcode = pobillcode;
    }

    public void setDbilldate(String dbilldate) {
        this.dbilldate = dbilldate;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getPobillcode() {
        return pobillcode;
    }

    public String getDbilldate() {
        return dbilldate;
    }

    public String getDr() {
        return dr;
    }

}
