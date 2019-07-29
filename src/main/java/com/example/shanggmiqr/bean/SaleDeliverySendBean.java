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
    private String dbilldate;
    private String num;







    public String getBillmaker() {
        return billmaker;
    }

    public void setBillmaker(String billmaker) {
        this.billmaker = billmaker;
    }

    private String billmaker;

    public String getNcode() {
        return ncode;
    }

    public void setNcode(String ncode) {
        this.ncode = ncode;
    }

    private String ncode;



    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
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

    private String org;

    public String getAppuser() {
        return appuser;
    }

    public void setAppuser(String appuser) {
        this.appuser = appuser;
    }

    public String appuser;



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

    public String getWlorgcode() {
        return wlorgcode;
    }

    public void setWlorgcode(String wlorgcode) {
        this.wlorgcode = wlorgcode;
    }

    public String getWlbillcode() {
        return wlbillcode;
    }

    public void setWlbillcode(String wlbillcode) {
        this.wlbillcode = wlbillcode;
    }

    private String wlbillcode;
    private String wlorgcode;
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


        public String materialcode;
        public String nnum;
        public String pch;
        public List<SnBean> sn;

        public String getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(String warehouse) {
            this.warehouse = warehouse;
        }

        private String warehouse;

        public String getCwarecode() {
            return cwarecode;
        }

        public void setCwarecode(String cwarecode) {
            this.cwarecode = cwarecode;
        }

        private String cwarecode;
        public String getShnum() {
            return shnum;
        }

        public void setShnum(String shnum) {
            this.shnum = shnum;
        }

        private String shnum;


        public String getItempk() {
            return itempk;
        }

        public void setItempk(String itempk) {
            this.itempk = itempk;
        }

        public String itempk;



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
