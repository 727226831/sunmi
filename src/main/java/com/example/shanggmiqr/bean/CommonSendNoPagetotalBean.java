package com.example.shanggmiqr.bean;

import java.io.Serializable;

/**
 * Created by weiyt.jiang on 2018/8/10.
 */

public class CommonSendNoPagetotalBean implements Serializable{

    /**
     * begintime : 2018-07-20 00:00:00
     * endtime : 2018-07-21 00:00:00
     * pagenum : 2
     * pagetotal : 6
     */

    private String begintime;
    private String endtime;
    private String pagenum;

    public CommonSendNoPagetotalBean(String begintime, String endtime, String pagenum){
        this.begintime = begintime;
        this.endtime = endtime;
        this.pagenum = pagenum;
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
