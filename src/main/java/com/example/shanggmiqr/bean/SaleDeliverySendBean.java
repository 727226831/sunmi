package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class SaleDeliverySendBean {

    /**
     * lyxt : APP
     * tmcode : 123456
     * cwarehousecode : 000
     * bisreturn : 2
     * pobillcode : DN1111111111
     * data : [{"vcooporderbcode_b":"4331-Cxx-04","materialcode":"4331-Cxx-04","nnum":"4331-Cxx-04","pch":"","sn":[{"xlh":"10342423423","txm":"P06010002","xm":"N1232323","tp":"P0601"}]}]
     */

    public String lyxt;
    public String tmcode;
    public String cwhsmanagercode;
    public String cwarehousecode;
    public String bisreturn;
    public String pobillcode;
    public String wlcode;
    public String wlbill;
    public List<BodyBean> body;

    public SaleDeliverySendBean(String lyxt, String tmcode, String cwhsmanagercode,String wlcode,String wlbill, String cwarehousecode, String bisreturn, String pobillcode, List<BodyBean> body) {
        this.lyxt = lyxt;
        this.tmcode = tmcode;
        this.cwhsmanagercode = cwhsmanagercode;
        this.cwarehousecode = cwarehousecode;
        this.bisreturn = bisreturn;
        this.pobillcode = pobillcode;
        this.wlcode = wlcode;
        this.wlbill = wlbill;
        this.body = body;
    }

    public String getCwhsmanagercode() {
        return cwhsmanagercode;
    }

    public void setCwhsmanagercode(String cwhsmanagercode) {
        this.cwhsmanagercode = cwhsmanagercode;
    }

    public String getWlcode() {
        return wlcode;
    }

    public void setWlcode(String wlcode) {
        this.wlcode = wlcode;
    }

    public String getWlbill() {
        return wlbill;
    }

    public void setWlbill(String wlbill) {
        this.wlbill = wlbill;
    }

    public void setLyxt(String lyxt) {
        this.lyxt = lyxt;
    }

    public void setTmcode(String tmcode) {
        this.tmcode = tmcode;
    }

    public void setCwarehousecode(String cwarehousecode) {
        this.cwarehousecode = cwarehousecode;
    }

    public void setBisreturn(String bisreturn) {
        this.bisreturn = bisreturn;
    }

    public void setPobillcode(String pobillcode) {
        this.pobillcode = pobillcode;
    }

    public void setBody(List<BodyBean> body) {
        this.body = body;
    }

    public String getLyxt() {
        return lyxt;
    }

    public String getTmcode() {
        return tmcode;
    }

    public String getCwarehousecode() {
        return cwarehousecode;
    }

    public String getBisreturn() {
        return bisreturn;
    }

    public String getPobillcode() {
        return pobillcode;
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

        public String vcooporderbcode_b;
        public String materialcode;
        public String nnum;
        public String pch;
        public List<SnBean> sn;

        public void setVcooporderbcode_b(String vcooporderbcode_b) {
            this.vcooporderbcode_b = vcooporderbcode_b;
        }

        public void setMaterialcode(String materialcode) {
            this.materialcode = materialcode;
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

        public String getVcooporderbcode_b() {
            return vcooporderbcode_b;
        }

        public String getMaterialcode() {
            return materialcode;
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
