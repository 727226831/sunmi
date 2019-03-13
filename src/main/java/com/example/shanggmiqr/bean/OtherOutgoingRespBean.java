package com.example.shanggmiqr.bean;

/**
 * Created by weiyt.jiang on 2018/8/31.
 */

public class OtherOutgoingRespBean {

    /**
     * code : 单据号
     * issuccess : 是否成功
     * msg : 异常信息
     */

    private String code;
    private String issuccess;
    private String msg;

    public void setCode(String code) {
        this.code = code;
    }

    public void setIssuccess(String issuccess) {
        this.issuccess = issuccess;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getIssuccess() {
        return issuccess;
    }

    public String getMsg() {
        return msg;
    }
}
