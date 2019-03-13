package com.example.shanggmiqr.bean;

import java.io.Serializable;

/**
 * Created by weiyt.jiang on 2018/8/10.
 */

public class CommonSendAllocateBean implements Serializable{

    /**
     * begintime : 2018-07-20 00:00:00
     * endtime : 2018-07-21 00:00:00
     * pagenum : 2
     * pagetotal : 6
     */

    private String begintime;
    private String endtime;
    private String pagenum;
    private String cwhsmanagercode;

    public CommonSendAllocateBean(String begintime, String endtime, String cwhsmanagercode, String pagenum){
        this.begintime = begintime;
        this.endtime = endtime;
        this.cwhsmanagercode = cwhsmanagercode;
        this.pagenum = pagenum;
    }

    public String getCwhsmanagercode() {
        return cwhsmanagercode;
    }

    public void setCwhsmanagercode(String cwhsmanagercode) {
        this.cwhsmanagercode = cwhsmanagercode;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

    public String getBegintime() {
        return begintime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getPagenum() {
        return pagenum;
    }

}
