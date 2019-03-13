package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class Supplier {

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
         * Pk_supplie : 1001J110000000001DEQ
         * name : 汕头市昕盈贸易有限公司
         * cgorgcode :
         * code : 000012
         * corpaddress : 1001J110000000001DEP
         * supplierbank : [{"bankname":"中国民生银行汕头人民广场支行","accname":false,"accnum":"695178099"}]
         * orgcode :
         * supprop : 0
         * ts : 2018-05-25 17:36:01
         */

        public String Pk_supplie;
        public String name;
        public String cgorgcode;
        public String code;
        public String corpaddress;
        public String orgcode;
        public int supprop;
        public String ts;
        public List<SupplierbankBean> supplierbank;

        public void setPk_supplie(String Pk_supplie) {
            this.Pk_supplie = Pk_supplie;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCgorgcode(String cgorgcode) {
            this.cgorgcode = cgorgcode;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setCorpaddress(String corpaddress) {
            this.corpaddress = corpaddress;
        }

        public void setOrgcode(String orgcode) {
            this.orgcode = orgcode;
        }

        public void setSupprop(int supprop) {
            this.supprop = supprop;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public void setSupplierbank(List<SupplierbankBean> supplierbank) {
            this.supplierbank = supplierbank;
        }

        public String getPk_supplie() {
            return Pk_supplie;
        }

        public String getName() {
            return name;
        }

        public String getCgorgcode() {
            return cgorgcode;
        }

        public String getCode() {
            return code;
        }

        public String getCorpaddress() {
            return corpaddress;
        }

        public String getOrgcode() {
            return orgcode;
        }

        public int getSupprop() {
            return supprop;
        }

        public String getTs() {
            return ts;
        }

        public List<SupplierbankBean> getSupplierbank() {
            return supplierbank;
        }

        public static class SupplierbankBean {
            /**
             * bankname : 中国民生银行汕头人民广场支行
             * accname : false
             * accnum : 695178099
             */

            private String bankname;
            private String accname;
            private String accnum;

            public void setBankname(String bankname) {
                this.bankname = bankname;
            }

            public void setAccname(String accname) {
                this.accname = accname;
            }

            public void setAccnum(String accnum) {
                this.accnum = accnum;
            }

            public String getBankname() {
                return bankname;
            }

            public String getAccname() {
                return accname;
            }

            public String getAccnum() {
                return accnum;
            }
        }
    }
}
