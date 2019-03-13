package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class ProductEntrySendBean {

    /**
     * lyxt : APP
     * tmcode : 123456
     * cwarehousecode : 000
     * bisreturn : 2
     * pobillcode : DN1111111111
     * data : [{"vcooporderbcode_b":"4331-Cxx-04","materialcode":"4331-Cxx-04","nnum":"4331-Cxx-04","pch":"","sn":[{"xlh":"10342423423","txm":"P06010002","xm":"N1232323","tp":"P0601"}]}]
     */

    public String headpk;
    public String billmaker;
    public List<BodyBean> body;

    public ProductEntrySendBean(String headpk, String billmaker, List<BodyBean> body) {
        this.headpk = headpk;
        this.billmaker = billmaker;
        this.body = body;
    }

    public String getHeadpk() {
        return headpk;
    }

    public void setHeadpk(String headpk) {
        this.headpk = headpk;
    }

    public String getBillmaker() {
        return billmaker;
    }

    public void setBillmaker(String billmaker) {
        this.billmaker = billmaker;
    }

    public void setBody(List<BodyBean> body) {
        this.body = body;
    }

    public List<BodyBean> getBody() {
        return body;
    }

    public static class BodyBean {
        /**
         * vcooporderbcode_b : 4331-Cxx-04
         * materialcode : 4331-Cxx-04
         * nnum : 4331-Cxx-04
         * pch :
         * sn : [{"xlh":"10342423423","txm":"P06010002","xm":"N1232323","tp":"P0601"}]
         */

        public String itempk;
        public String nnum;
        public String pch;
        public List<SnBean> sn;

        public String getItempk() {
            return itempk;
        }

        public void setItempk(String itempk) {
            this.itempk = itempk;
        }

        public void setNnum(String nnum) {
            this.nnum = nnum;
        }

        public void setPch(String pch) {
            this.pch = pch;
        }

        public void setSn(List<SnBean> sn) {
            this.sn = sn;
        }

        public String getNnum() {
            return nnum;
        }

        public String getPch() {
            return pch;
        }

        public List<SnBean> getSn() {
            return sn;
        }

        public static class SnBean {
            /**
             * xlh : 10342423423
             * txm : P06010002
             * xm : N1232323
             * tp : P0601
             */

            public String xlh;
            public String txm;
            public String xm;
            public String tp;

            public void setXlh(String xlh) {
                this.xlh = xlh;
            }

            public void setTxm(String txm) {
                this.txm = txm;
            }

            public void setXm(String xm) {
                this.xm = xm;
            }

            public void setTp(String tp) {
                this.tp = tp;
            }

            public String getXlh() {
                return xlh;
            }

            public String getTxm() {
                return txm;
            }

            public String getXm() {
                return xm;
            }

            public String getTp() {
                return tp;
            }
        }
    }
}
