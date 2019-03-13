package com.example.shanggmiqr.bean;

import java.io.Serializable;

/**
 * Created by weiyt.jiang on 2018/8/10.
 */

public class CommonSendLogisticsBean implements Serializable{

    /**
     * begintime : 2018-07-20 00:00:00
     * endtime : 2018-07-21 00:00:00
     * pagenum : 2
     * pagetotal : 6
     */

    private String begtime;
    private String endtime;
    private String requestpage;

    public CommonSendLogisticsBean(String begintime, String endtime, String requestpage){
        this.begtime = begintime;
        this.endtime = endtime;
        this.requestpage = requestpage;
    }

    public void setBegintime(String begintime) {
        this.begtime = begintime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public void setRequestpage(String requestpage) {
        this.requestpage = requestpage;
    }

    public String getBegintime() {
        return begtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getRequestpage() {
        return requestpage;
    }

}
