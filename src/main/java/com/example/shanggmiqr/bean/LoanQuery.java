package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class LoanQuery {

    /**
     * errno : 0
     * errmsg : 查询成功
     * pagenum : 1
     * pagetotal : 1
     * data : [{"pobillcode":"SA201811070007","dbilldate":"2018-11-07 13:31:59","ts":"2018-12-21 12:57:21","num":"22","dr":"2","body":[{"materialcode":"P03050005","maccode":"P0305","nnum":"2","itempk":"1001D2100000000BNJGT","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"M04030006","maccode":"M0403","nnum":"3","itempk":"1001D2100000000BNJGU","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"S06000002","maccode":"S06","nnum":"4","itempk":"1001D2100000000BNJGV","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"T07000008","maccode":"T07","nnum":"5","itempk":"1001D2100000000BNJGW","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"P03030007","maccode":"P0303","nnum":"3","itempk":"1001D2100000000BNJGX","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"P03030008","maccode":"P0303","nnum":"2","itempk":"1001D2100000000BNJGY","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"S08000003","maccode":"S08","nnum":"3","itempk":"1001D2100000000BNJGZ","cwarecode":"","cwarename":"","vemo":""}]},{"pobillcode":"SA201809050004","dbilldate":"2018-09-05 14:51:44","ts":"2018-12-21 12:57:21","num":"1","dr":"2","body":[{"materialcode":"C04000002","maccode":"C04","nnum":"1","itempk":"1001D2100000000B3835","cwarecode":"","cwarename":"","vemo":"唐征宇"}]},{"pobillcode":"SA201809050005","dbilldate":"2018-09-05 14:52:56","ts":"2018-12-21 12:57:21","num":"1","dr":"2","body":[{"materialcode":"C04000002","maccode":"C04","nnum":"1","itempk":"1001D2100000000B3839","cwarecode":"","cwarename":"","vemo":"张三"}]},{"pobillcode":"SA201901170014","dbilldate":"2019-01-17 13:59:37","ts":"2019-01-17 15:56:49","num":"4","dr":"2","body":[{"materialcode":"P07010006","maccode":"P0701","nnum":"2","itempk":"1001D2100000000C3HMD","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"P03070001","maccode":"P0307","nnum":"2","itempk":"1001D2100000000C3HME","cwarecode":"","cwarename":"","vemo":""}]},{"pobillcode":"SA201901170015","dbilldate":"2019-01-17 14:00:20","ts":"2019-01-17 15:56:49","num":"4","dr":"2","body":[{"materialcode":"P07010006","maccode":"P0701","nnum":"2","itempk":"1001D2100000000C3HMH","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"P03070001","maccode":"P0307","nnum":"2","itempk":"1001D2100000000C3HMI","cwarecode":"","cwarename":"","vemo":""}]},{"pobillcode":"SA201901170016","dbilldate":"2019-01-17 14:00:24","ts":"2019-01-17 15:56:49","num":"4","dr":"2","body":[{"materialcode":"P07010006","maccode":"P0701","nnum":"2","itempk":"1001D2100000000C3HML","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"P03070001","maccode":"P0307","nnum":"2","itempk":"1001D2100000000C3HMM","cwarecode":"","cwarename":"","vemo":""}]},{"pobillcode":"SA201812240013","dbilldate":"2018-12-24 14:23:46","ts":"2019-01-17 15:56:49","num":"1","dr":"2","body":[{"materialcode":"P07020005","maccode":"P0702","nnum":"1","itempk":"1001AA100000000C1D35","cwarecode":"","cwarename":"","vemo":""}]},{"pobillcode":"SA201901210027","dbilldate":"2019-01-21 00:00:00","ts":"2019-01-21 20:17:46","num":"2","dr":"2","body":[{"materialcode":"P06020032","maccode":"P0602","nnum":"1","itempk":"1001BB100000000C47R2","cwarecode":"","cwarename":"","vemo":""}]},{"pobillcode":"SA201901220031","dbilldate":"2019-01-22 00:00:00","ts":"2019-01-22 20:12:35","num":"5","dr":"0","body":[{"materialcode":"P03060001","maccode":"P0306","nnum":"5","itempk":"1001BB100000000C4A1M","cwarecode":"","cwarename":"","vemo":""}]}]
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
         * pobillcode : SA201811070007
         * dbilldate : 2018-11-07 13:31:59
         * ts : 2018-12-21 12:57:21
         * num : 22
         * dr : 2
         * body : [{"materialcode":"P03050005","maccode":"P0305","nnum":"2","itempk":"1001D2100000000BNJGT","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"M04030006","maccode":"M0403","nnum":"3","itempk":"1001D2100000000BNJGU","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"S06000002","maccode":"S06","nnum":"4","itempk":"1001D2100000000BNJGV","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"T07000008","maccode":"T07","nnum":"5","itempk":"1001D2100000000BNJGW","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"P03030007","maccode":"P0303","nnum":"3","itempk":"1001D2100000000BNJGX","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"P03030008","maccode":"P0303","nnum":"2","itempk":"1001D2100000000BNJGY","cwarecode":"","cwarename":"","vemo":""},{"materialcode":"S08000003","maccode":"S08","nnum":"3","itempk":"1001D2100000000BNJGZ","cwarecode":"","cwarename":"","vemo":""}]
         */

        private String pobillcode;
        private String dbilldate;
        private String ts;
        private String num;
        private String dr;
        private List<BodyBean> body;

        public void setPobillcode(String pobillcode) {
            this.pobillcode = pobillcode;
        }

        public void setDbilldate(String dbilldate) {
            this.dbilldate = dbilldate;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public void setDr(String dr) {
            this.dr = dr;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public String getPobillcode() {
            return pobillcode;
        }

        public String getDbilldate() {
            return dbilldate;
        }

        public String getTs() {
            return ts;
        }

        public String getNum() {
            return num;
        }

        public String getDr() {
            return dr;
        }

        public List<BodyBean> getBody() {
            return body;
        }

        public static class BodyBean {
            /**
             * materialcode : P03050005
             * maccode : P0305
             * nnum : 2
             * itempk : 1001D2100000000BNJGT
             * cwarecode :
             * cwarename :
             * vemo :
             */

            private String materialcode;
            private String maccode;
            private String nnum;
            private String itempk;
            private String cwarecode;
            private String cwarename;
            private String vemo;

            public void setMaterialcode(String materialcode) {
                this.materialcode = materialcode;
            }

            public void setMaccode(String maccode) {
                this.maccode = maccode;
            }

            public void setNnum(String nnum) {
                this.nnum = nnum;
            }

            public void setItempk(String itempk) {
                this.itempk = itempk;
            }

            public void setCwarecode(String cwarecode) {
                this.cwarecode = cwarecode;
            }

            public void setCwarename(String cwarename) {
                this.cwarename = cwarename;
            }

            public void setVemo(String vemo) {
                this.vemo = vemo;
            }

            public String getMaterialcode() {
                return materialcode;
            }

            public String getMaccode() {
                return maccode;
            }

            public String getNnum() {
                return nnum;
            }

            public String getItempk() {
                return itempk;
            }

            public String getCwarecode() {
                return cwarecode;
            }

            public String getCwarename() {
                return cwarename;
            }

            public String getVemo() {
                return vemo;
            }
        }
    }
}
