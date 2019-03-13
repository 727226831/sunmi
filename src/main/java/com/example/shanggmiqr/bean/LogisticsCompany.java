package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class LogisticsCompany {

    /**
     * errorno : 0
     * errmsg : 成功
     * totalpage : 2
     * requestpage : 1
     * datas : [{"org":"shangmi","code":"01","name":"顺丰","status":"0"}]
     */

    private String errorno;
    private String errmsg;
    private String totalpage;
    private String requestpage;
    private List<DatasBean> datas;

    public void setErrorno(String errorno) {
        this.errorno = errorno;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public void setTotalpage(String totalpage) {
        this.totalpage = totalpage;
    }

    public void setRequestpage(String requestpage) {
        this.requestpage = requestpage;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public String getErrorno() {
        return errorno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getTotalpage() {
        return totalpage;
    }

    public String getRequestpage() {
        return requestpage;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public static class DatasBean {
        /**
         * org : shangmi
         * code : 01
         * name : 顺丰
         * status : 0
         */

        public String org;
        public String code;
        public String name;
        public String status;
        public String ts;

        public void setOrg(String org) {
            this.org = org;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public String getOrg() {
            return org;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }

        public String getTs() {
            return ts;
        }
    }
}
