package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */

public class SaleDeliveryBodyBean {


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

    public String vcooporderbcode_b;
    public String cwarename;
    public String matrcode;
    public String matrname;
    public String maccode;
    public String nnum;
    public String scannnum;
    public String rackcode;
    public String Customer;
    public String uploadflag;
    public SaleDeliveryBodyBean(){
        super();
    }

    public SaleDeliveryBodyBean(String vcooporderbcode_b,String cwarename, String matrcode, String matrname, String maccode, String nnum,String scannnum, String rackcode, String customer,String uploadflag) {
        super();
        this.vcooporderbcode_b = vcooporderbcode_b;
        this.cwarename = cwarename;
        this.matrcode = matrcode;
        this.matrname = matrname;
        this.maccode = maccode;
        this.nnum = nnum;
        this.scannnum = scannnum;
        this.rackcode = rackcode;
        this.Customer = customer;
        this.uploadflag = uploadflag;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public void setVcooporderbcode_b(String vcooporderbcode_b) {
        this.vcooporderbcode_b = vcooporderbcode_b;
    }
    public void setCwarename(String cwarename) {
        this.cwarename = cwarename;
    }

    public void setMatrcode(String matrcode) {
        this.matrcode = matrcode;
    }

    public void setMatrname(String matrname) {
        this.matrname = matrname;
    }

    public void setMaccode(String maccode) {
        this.maccode = maccode;
    }

    public void setNnum(String nnum) {
        this.nnum = nnum;
    }

    public void setScannnum(String scannnum) {
        this.scannnum = scannnum;
    }

    public void setRackcode(String rackcode) {
        this.rackcode = rackcode;
    }

    public void setCustomer(String Customer) {
        this.Customer = Customer;
    }
    public void setUploadflag(String uploadflag) {
        this.uploadflag = uploadflag;
    }


    public String getVcooporderbcode_b() {
        return vcooporderbcode_b;
    }
    public String getCwarename() {
        return cwarename;
    }

    public String getMatrcode() {
        return matrcode;
    }

    public String getMatrname() {
        return matrname;
    }

    public String getMaccode() {
        return maccode;
    }

    public String getNnum() {
        return nnum;
    }

    public String getScannnum() {
        return scannnum;
    }

    public String getRackcode() {
        return rackcode;
    }

    public String getCustomer() {
        return Customer;
    }
    public String getUploadflag() {
        return uploadflag;
    }
}
