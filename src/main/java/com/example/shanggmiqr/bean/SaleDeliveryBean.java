package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class SaleDeliveryBean {
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
    String matrcode;

    public String getCwarename() {
        return cwarename;
    }

    public void setCwarename(String cwarename) {
        this.cwarename = cwarename;
    }

    private String cwarename;

    public String getMatrcode() {
        return matrcode;
    }

    public void setMatrcode(String matrcode) {
        this.matrcode = matrcode;
    }

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

    String matrname;
    String maccode;
    String nnum;
    String prodcutcode;
    String xlh;
    public String wlcode;

    public String getWlcode() {
        return wlcode;
    }

    public void setWlcode(String wlcode) {
        this.wlcode = wlcode;
    }

    public String getWlbill() {
        return wlbill;
    }

    public void setWlbill(String wlbill) {
        this.wlbill = wlbill;
    }

    public String wlbill;

    public boolean isSelected = false;
    public SaleDeliveryBean(){
        super();
    }
    public SaleDeliveryBean( String vbillcode, String dbilldate,int dr) {
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
