package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class Qrcode {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * code : 1022
         * name : 上海仓库
         * org : PUBG
         * ts : 2018-07-20 00:00:00
         * barpk : 223231231231232
         * pagenum : 2
         * pagetotal : 3
         */

        private String code;
        private String name;
        private String org;
        private String ts;
        private String barpk;
        private String pagenum;
        private String pagetotal;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getTs() {
            return ts;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public String getBarpk() {
            return barpk;
        }

        public void setBarpk(String barpk) {
            this.barpk = barpk;
        }

        public String getPagenum() {
            return pagenum;
        }

        public void setPagenum(String pagenum) {
            this.pagenum = pagenum;
        }

        public String getPagetotal() {
            return pagetotal;
        }

        public void setPagetotal(String pagetotal) {
            this.pagetotal = pagetotal;
        }
    }
}
