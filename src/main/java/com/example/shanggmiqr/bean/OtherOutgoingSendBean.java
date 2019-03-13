package com.example.shanggmiqr.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/10.
 */

public class OtherOutgoingSendBean implements Serializable {


    /**
     * pobillcode : 单据号
     * cwarehousecode : 仓库编号
     * body : [{"materialcode":"物料编号","nnum":"数量","pch":"批次号","sn":[{"xlh":"序列号","txm":"条形码","xm":"箱码","tp":"托盘码"}]}]
     */
    public String lyxt;
    public String pobillcode;
    public String cwarehousecode;
    public List<BodyBean> body;

    public OtherOutgoingSendBean(String lyxt,String pobillcode, String cwarehousecode,List<BodyBean> body) {
        this.lyxt = lyxt;
        this.pobillcode = pobillcode;
        this.cwarehousecode = cwarehousecode;
        this.body = body;
    }

    public void setPobillcode(String pobillcode) {
        this.pobillcode = pobillcode;
    }

    public void setCwarehousecode(String cwarehousecode) {
        this.cwarehousecode = cwarehousecode;
    }

    public void setLyxt(String lyxt) {
        this.lyxt = lyxt;
    }

    public void setBody(List<BodyBean> body) {
        this.body = body;
    }

    public String getPobillcode() {
        return pobillcode;
    }

    public String getLyxt() {
        return lyxt;
    }

    public String getCwarehousecode() {
        return cwarehousecode;
    }

    public List<BodyBean> getBody() {
        return body;
    }

    public static class BodyBean {
        /**
         * materialcode : 物料编号
         * nnum : 数量
         * pch : 批次号
         * sn : [{"xlh":"序列号","txm":"条形码","xm":"箱码","tp":"托盘码"}]
         */

        public String materialcode;
        public String nnum;
        public String pch;
        public List<SnBean> sn;
        public String vcooporderbcode_b;
        public void setMaterialcode(String materialcode) {
            this.materialcode = materialcode;
        }
        public void setVcooporderbcode_b(String vcooporderbcode_b) {
            this.vcooporderbcode_b = vcooporderbcode_b;
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
        public String geVcooporderbcode_b() {
            return vcooporderbcode_b;
        }
        public List<SnBean> getSn() {
            return sn;
        }

        public static class SnBean {
            /**
             * xlh : 序列号
             * txm : 条形码
             * xm : 箱码
             * tp : 托盘码
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

