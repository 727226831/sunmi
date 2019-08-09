package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class PurchaseReturnQuery {


    private String errno;
    private String errmsg;
    private String pagenum;
    private String pagetotal;
    private List<DataBean> data;

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {


        private String vbillcode;
        private String ts;
        private String dbilldate;
        private String org;
        private String dr;
        private List<BodyBean> body;


        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        private String num;


        public String getVbillcode() {
            return vbillcode;
        }

        public void setVbillcode(String vbillcode) {
            this.vbillcode = vbillcode;
        }

        public String getTs() {
            return ts;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public String getDbilldate() {
            return dbilldate;
        }

        public void setDbilldate(String dbilldate) {
            this.dbilldate = dbilldate;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getDr() {
            return dr;
        }

        public void setDr(String dr) {
            this.dr = dr;
        }

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public static class BodyBean {
            /**
             * itempk : 1001J11000000006KO95
             * materialname : A1塑料上盖
             * nnum : -8
             * warehouse : null
             * maccode : M01
             * materialcode : M01000102
             */

            private String itempk;
            private String materialname;
            private String nnum;
            private Object warehouse;
            private String maccode;
            private String materialcode;

            public String getItempk() {
                return itempk;
            }

            public void setItempk(String itempk) {
                this.itempk = itempk;
            }

            public String getMaterialname() {
                return materialname;
            }

            public void setMaterialname(String materialname) {
                this.materialname = materialname;
            }

            public String getNnum() {
                return nnum;
            }

            public void setNnum(String nnum) {
                this.nnum = nnum;
            }

            public Object getWarehouse() {
                return warehouse;
            }

            public void setWarehouse(Object warehouse) {
                this.warehouse = warehouse;
            }

            public String getMaccode() {
                return maccode;
            }

            public void setMaccode(String maccode) {
                this.maccode = maccode;
            }

            public String getMaterialcode() {
                return materialcode;
            }

            public void setMaterialcode(String materialcode) {
                this.materialcode = materialcode;
            }
        }
    }
}
