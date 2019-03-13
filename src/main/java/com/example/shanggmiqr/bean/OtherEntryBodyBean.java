package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/13.
 */

public class OtherEntryBodyBean {

    /**
     * maccode :
     * nnum : 2
     * pch :
     */
    public String vcooporderbcode_b;
    public String maccode;
    public String materialcode;
    public int nnum;
    public String uploadnum;
    public String scannum;
    public String uploadflag;
    private boolean isSelected = false;
    public OtherEntryBodyBean(){
        super();
    }

    public OtherEntryBodyBean(String vcooporderbcode_b, String materialcode, String maccode, int nnum, String uploadnum, String scannum, String uploadflag ) {
        super();
        this.vcooporderbcode_b =vcooporderbcode_b;
        this.materialcode = materialcode;
        this.maccode = maccode;
        this.nnum = nnum;
        this.uploadnum = uploadnum;
        this.scannum =scannum;
        this.uploadflag=uploadflag;
    }

    public String getVcooporderbcode_b() {
        return vcooporderbcode_b;
    }

    public void setVcooporderbcode_b(String vcooporderbcode_b) {
        this.vcooporderbcode_b = vcooporderbcode_b;
    }

    public void setMaterialcode(String materialcode) {
        this.materialcode = materialcode;
    }

    public void setMaccode(String maccode) {
        this.maccode = maccode;
    }

    public void setNnum(int nnum) {
        this.nnum = nnum;
    }

    public void setScannum(String scannum) {
        this.scannum = scannum;
    }
    public void setUploadflag(String uploadflag) {
        this.uploadflag = uploadflag;
    }
    public void setUploadnum(String uploadnum) {
        this.uploadnum = uploadnum;
    }

    public String getMaterialcode() {
        return materialcode;
    }

    public String getMaccode() {
        return maccode;
    }

    public int getNnum() {
        return nnum;
    }

    public String getUploadnum() {
        return uploadnum;
    }
    public String getScannum() {
        return scannum;
    }
    public String getUploadflag() {
        return uploadflag;
    }

    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
