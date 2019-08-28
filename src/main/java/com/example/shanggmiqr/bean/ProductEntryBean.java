package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class ProductEntryBean {
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


    public String billcode;
    public String dbilldate;
    public String cwarename;
    public int dr;
    private String prodcutcode;


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    private String flag;

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

    private String xlh;

    public String getNnum() {
        return nnum;
    }

    public void setNnum(String nnum) {
        this.nnum = nnum;
    }

    private String nnum;

    public String getMaccode() {
        return maccode;
    }

    public void setMaccode(String maccode) {
        this.maccode = maccode;
    }

    private String maccode;

    public boolean isSelected = false;
    public ProductEntryBean(){
        super();
    }
    public ProductEntryBean(String billcode, String dbilldate,String cwarename, int dr) {
        super();
        this.billcode = billcode;
        this.dbilldate = dbilldate;
        this.cwarename = cwarename;
        this.dr = dr;
    }

    public String getMaterialcode() {
        return materialcode;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    private String materialcode;
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }


    public void setBillcode(String billcode) {
        this.billcode = billcode;
    }

    public void setDbilldate(String dbilldate) {
        this.dbilldate = dbilldate;
    }

    public void setDr(int dr) {
        this.dr = dr;
    }

    public String getBillcode() {
        return billcode;
    }

    public String getDbilldate() {
        return dbilldate;
    }

    public int getDr() {
        return dr;
    }

    public String getCwarename() {
        return cwarename;
    }

    public void setCwarename(String cwarename) {
        this.cwarename = cwarename;
    }
}
