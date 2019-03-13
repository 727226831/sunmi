package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class OtherEntryQuery {

    //其他入库查询R09 跟其他出库查询的query是一样的，为了看起来清晰，故新建文件
    /**
     * errno : 0
     * errmsg : 查询成功
     * pagenum : 1
     * pagetotal : 1
     * data : [{"pobillcode":"TC2018073100000448","cwarecode":"000","cwarename":"七星国内仓","body":[{"materialcode":"P01120014","maccode":"P0112","nnum":2,"pch":""}]}]
     */

    private String errno;
    private String errmsg;
    private String pagenum;
    private int pagetotal;
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

    public void setPagetotal(int pagetotal) {
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

    public int getPagetotal() {
        return pagetotal;
    }

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {
        /**
         * pobillcode : TC2018073100000448
         * cwarecode : 000
         * cwarename : 七星国内仓
         * body : [{"materialcode":"P01120014","maccode":"P0112","nnum":2,"pch":""}]
         */

        public String pobillcode;
        public String cwarecode;
        public String cwarename;
        public String dbilldate;
        public String dr;
        private List<BodyBean> body;

        public void setPobillcode(String pobillcode) {
            this.pobillcode = pobillcode;
        }

        public void setCwarecode(String cwarecode) {
            this.cwarecode = cwarecode;
        }

        public void setCwarename(String cwarename) {
            this.cwarename = cwarename;
        }

        public void setDbilldate(String dbilldate) {
            this.dbilldate = dbilldate;
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

        public String getCwarecode() {
            return cwarecode;
        }

        public String getCwarename() {
            return cwarename;
        }

        public String getDbilldate() {
            return dbilldate;
        }
        public String getDr() {
            return dr;
        }

        public List<BodyBean> getBody() {
            return body;
        }

        public static class BodyBean {
            /**
             * materialcode : P01120014
             * maccode : P0112
             * nnum : 2
             * pch :
             */

            private String materialcode;
            private String maccode;
            private int nnum;
            private String pch;
            private String vcooporderbcode_b;

            public void setMaterialcode(String materialcode) {
                this.materialcode = materialcode;
            }

            public void setMaccode(String maccode) {
                this.maccode = maccode;
            }

            public void setVcooporderbcode_b(String vcooporderbcode_b) {
                this.vcooporderbcode_b = vcooporderbcode_b;
            }

            public void setNnum(int nnum) {
                this.nnum = nnum;
            }

            public void setPch(String pch) {
                this.pch = pch;
            }

            public String getMaterialcode() {
                return materialcode;
            }

            public String getMaccode() {
                return maccode;
            }

            public int getNnum() {
                return nnum;
            }

            public String getPch() {
                return pch;
            }

            public String getVcooporderbcode_b() {
                return vcooporderbcode_b;
            }
        }
    }


}
