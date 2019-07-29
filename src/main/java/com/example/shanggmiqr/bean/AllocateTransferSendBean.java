package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class AllocateTransferSendBean {

    /**
     * lyxt : APP
     * tmcode : 123456
     * cwarehousecode : 000
     * bisreturn : 2
     * pobillcode : DN1111111111
     * data : [{"vcooporderbcode_b":"4331-Cxx-04","materialcode":"4331-Cxx-04","nnum":"4331-Cxx-04","pch":"","sn":[{"xlh":"10342423423","txm":"P06010002","xm":"N1232323","tp":"P0601"}]}]
     */

    public String lyxt;
    public String cwhsmanagercode;
    public String cwarehousecode;
    public String org;
    public String pobillcode;
    public String drwarehouse;
    public String num;
    public List<BodyBean> body;

    public String getWlbillcode() {
        return wlbillcode;
    }

    public void setWlbillcode(String wlbillcode) {
        this.wlbillcode = wlbillcode;
    }

    private String wlbillcode;

    public String getWlorgcode() {
        return wlorgcode;
    }

    public void setWlorgcode(String wlorgcode) {
        this.wlorgcode = wlorgcode;
    }

    private String wlorgcode;

    public AllocateTransferSendBean(String lyxt, String cwhsmanagercode,  String pobillcode,String cwarehousecode, String org, String drwarehouse, String num, List<BodyBean> body) {
        this.lyxt = lyxt;
        this.cwhsmanagercode = cwhsmanagercode;
        this.cwarehousecode = cwarehousecode;
        this.org = org;
        this.pobillcode = pobillcode;
        this.drwarehouse = drwarehouse;
        this.num = num;
        this.body = body;
    }

    public String getLyxt() {
        return lyxt;
    }

    public void setLyxt(String lyxt) {
        this.lyxt = lyxt;
    }

    public String getCwhsmanagercode() {
        return cwhsmanagercode;
    }

    public void setCwhsmanagercode(String cwhsmanagercode) {
        this.cwhsmanagercode = cwhsmanagercode;
    }

    public String getCwarehousecode() {
        return cwarehousecode;
    }

    public void setCwarehousecode(String cwarehousecode) {
        this.cwarehousecode = cwarehousecode;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getPobillcode() {
        return pobillcode;
    }

    public void setPobillcode(String pobillcode) {
        this.pobillcode = pobillcode;
    }

    public String getDrwarehouse() {
        return drwarehouse;
    }

    public void setDrwarehouse(String drwarehouse) {
        this.drwarehouse = drwarehouse;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public List<BodyBean> getBody() {
        return body;
    }

    public void setBody(List<BodyBean> body) {
        this.body = body;
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
        public String materialcode;
        public String yfnum;
        public String sfnum;
        public List<SnBean> sn;
        private String drwarehouse;

        public String getDrwarehouse() {
            return drwarehouse;
        }

        public void setDrwarehouse(String drwarehouse) {
            this.drwarehouse = drwarehouse;
        }

        public String getCwarehousecode() {
            return cwarehousecode;
        }

        public void setCwarehousecode(String cwarehousecode) {
            this.cwarehousecode = cwarehousecode;
        }

        private String cwarehousecode;

        public String getItempk() {
            return itempk;
        }

        public void setItempk(String itempk) {
            this.itempk = itempk;
        }

        public String getMaterialcode() {
            return materialcode;
        }

        public void setMaterialcode(String materialcode) {
            this.materialcode = materialcode;
        }

        public String getYfnum() {
            return yfnum;
        }

        public void setYfnum(String yfnum) {
            this.yfnum = yfnum;
        }

        public String getSfnum() {
            return sfnum;
        }

        public void setSfnum(String sfnum) {
            this.sfnum = sfnum;
        }

        public List<SnBean> getSn() {
            return sn;
        }

        public void setSn(List<SnBean> sn) {
            this.sn = sn;
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
