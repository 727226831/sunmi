package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class ProductEntryQuery {

    /**
     * errno : 0
     * errmsg : 查询成功
     * pagenum : 1
     * pagetotal : 1
     * data : [{"billcode":"PR2018091400000193","dbilldate":"2018-09-14 13:21:37","dr":"0","ts":"2018-09-14 13:21:38","cwarecode":"001","cwarename":"米开成品仓","org":"100100","totalnum":"0","headpk":"1001D2100000000BGJ2Q","pagetotal":"1","pagenum":"1","body":[{"materialcode":"P01070034","nnum":"0","ysnum":"25","itempk":"1001D2100000000BGJ2R"}]},{"billcode":"PR2018091600000196","dbilldate":"2018-09-16 19:11:57","dr":"0","ts":"2018-09-16 19:11:57","cwarecode":"001","cwarename":"米开成品仓","org":"100100","totalnum":"0","headpk":"1001D2100000000BH1Q0","pagetotal":"1","pagenum":"1","body":[{"materialcode":"P01070034","nnum":"0","ysnum":"50","itempk":"1001D2100000000BH1Q1"}]},{"billcode":"PR2019011700000200","dbilldate":"2019-01-17 14:07:47","dr":"2","ts":"2019-01-17 17:35:16","cwarecode":"000","cwarename":"七星国内仓","org":"1001","totalnum":"0","headpk":"1001D2100000000C3HMP","pagetotal":"1","pagenum":"1","body":[{"materialcode":"P03070001","nnum":"0","ysnum":"2","itempk":"1001D2100000000C3HMR"},{"materialcode":"P07010006","nnum":"0","ysnum":"2","itempk":"1001D2100000000C3HMS"}]},{"billcode":"PR2019011700000201","dbilldate":"2019-01-17 14:08:29","dr":"2","ts":"2019-01-21 22:10:02","cwarecode":"000","cwarename":"七星国内仓","org":"1001","totalnum":"0","headpk":"1001D2100000000C3HMV","pagetotal":"1","pagenum":"1","body":[{"materialcode":"P03070001","nnum":"0","ysnum":"2","itempk":"1001D2100000000C3HMX"},{"materialcode":"P07010006","nnum":"0","ysnum":"2","itempk":"1001D2100000000C3HMY"}]},{"billcode":"PR2018120400000199","dbilldate":"2018-12-04 10:53:33","dr":"2","ts":"2019-01-21 22:10:02","cwarecode":"000","cwarename":"七星国内仓","org":"1001","totalnum":"0","headpk":"1001D2100000000C0GVP","pagetotal":"1","pagenum":"1","body":[{"materialcode":"C02000001","nnum":"0","ysnum":"10","itempk":"1001D2100000000C0GVR"}]},{"billcode":"PR2019011700000202","dbilldate":"2019-01-17 14:08:37","dr":"2","ts":"2019-01-21 22:10:02","cwarecode":"0217","cwarename":"飞力士仓","org":"1001","totalnum":"0","headpk":"1001D2100000000C3HN1","pagetotal":"1","pagenum":"1","body":[{"materialcode":"P03070001","nnum":"0","ysnum":"2","itempk":"1001D2100000000C3HN3"},{"materialcode":"P07010006","nnum":"0","ysnum":"2","itempk":"1001D2100000000C3HN4"}]},{"billcode":"PR2019011700000203","dbilldate":"2019-01-17 14:08:44","dr":"2","ts":"2019-01-21 22:10:02","cwarecode":"0217","cwarename":"飞力士仓","org":"1001","totalnum":"0","headpk":"1001D2100000000C3HN7","pagetotal":"1","pagenum":"1","body":[{"materialcode":"P03070001","nnum":"0","ysnum":"2","itempk":"1001D2100000000C3HN9"},{"materialcode":"P07010006","nnum":"0","ysnum":"2","itempk":"1001D2100000000C3HNA"}]}]
     */

    private String errno;
    private String errmsg;
    private String pagenum;
    private String pagetotal;
    private List<DataBean> data;

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

    public void setPagetotal(String pagetotal) {
        this.pagetotal = pagetotal;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getErrno() {
        return errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getPagenum() {
        return pagenum;
    }

    public String getPagetotal() {
        return pagetotal;
    }

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {
        /**
         * billcode : PR2018091400000193
         * dbilldate : 2018-09-14 13:21:37
         * dr : 0
         * ts : 2018-09-14 13:21:38
         * cwarecode : 001
         * cwarename : 米开成品仓
         * org : 100100
         * totalnum : 0
         * headpk : 1001D2100000000BGJ2Q
         * pagetotal : 1
         * pagenum : 1
         * body : [{"materialcode":"P01070034","nnum":"0","ysnum":"25","itempk":"1001D2100000000BGJ2R"}]
         */

        private String billcode;
        private String dbilldate;
        private String dr;
        private String ts;
        private String cwarecode;
        private String cwarename;
        private String org;
        private String totalnum;
        private String headpk;
        private String pagetotal;
        private String pagenum;
        private List<BodyBean> body;

        public void setBillcode(String billcode) {
            this.billcode = billcode;
        }

        public void setDbilldate(String dbilldate) {
            this.dbilldate = dbilldate;
        }

        public void setDr(String dr) {
            this.dr = dr;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public void setCwarecode(String cwarecode) {
            this.cwarecode = cwarecode;
        }

        public void setCwarename(String cwarename) {
            this.cwarename = cwarename;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public void setTotalnum(String totalnum) {
            this.totalnum = totalnum;
        }

        public void setHeadpk(String headpk) {
            this.headpk = headpk;
        }

        public void setPagetotal(String pagetotal) {
            this.pagetotal = pagetotal;
        }

        public void setPagenum(String pagenum) {
            this.pagenum = pagenum;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public String getBillcode() {
            return billcode;
        }

        public String getDbilldate() {
            return dbilldate;
        }

        public String getDr() {
            return dr;
        }

        public String getTs() {
            return ts;
        }

        public String getCwarecode() {
            return cwarecode;
        }

        public String getCwarename() {
            return cwarename;
        }

        public String getOrg() {
            return org;
        }

        public String getTotalnum() {
            return totalnum;
        }

        public String getHeadpk() {
            return headpk;
        }

        public String getPagetotal() {
            return pagetotal;
        }

        public String getPagenum() {
            return pagenum;
        }

        public List<BodyBean> getBody() {
            return body;
        }

        public static class BodyBean {
            /**
             * materialcode : P01070034
             * nnum : 0
             * ysnum : 25
             * itempk : 1001D2100000000BGJ2R
             */

            private String materialcode;
            private String nnum;
            private String ysnum;
            private String itempk;

            public void setMaterialcode(String materialcode) {
                this.materialcode = materialcode;
            }

            public void setNnum(String nnum) {
                this.nnum = nnum;
            }

            public void setYsnum(String ysnum) {
                this.ysnum = ysnum;
            }

            public void setItempk(String itempk) {
                this.itempk = itempk;
            }

            public String getMaterialcode() {
                return materialcode;
            }

            public String getNnum() {
                return nnum;
            }

            public String getYsnum() {
                return ysnum;
            }

            public String getItempk() {
                return itempk;
            }
        }
    }
}
