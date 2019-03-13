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
