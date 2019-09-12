package com.example.shanggmiqr.bean;

import java.util.List;

public class MarketBean {


        private String errno;

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    private String ts;
        private String errmsg;
        private String pagenum;
        private String pagetotal;
        private List<Data> data;
        public void setErrno(String errno) {
            this.errno = errno;
        }
        public String getErrno() {
            return errno;
        }



        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }
        public String getErrmsg() {
            return errmsg;
        }

        public void setPagenum(String pagenum) {
            this.pagenum = pagenum;
        }
        public String getPagenum() {
            return pagenum;
        }

        public void setPagetotal(String pagetotal) {
            this.pagetotal = pagetotal;
        }
        public String getPagetotal() {
            return pagetotal;
        }

        public void setData(List<Data> data) {
            this.data = data;
        }
        public List<Data> getData() {
            return data;
        }

    public class Data {

        private List<String> body;
        private String unitcode;
        private String billmakercode;
        private String country;
        private String cdeliveryid;
        private String vbillcode;
        private String transporttypecode;
        private String pupsndoccode;
        private String vtrantypecode;
        private String deptcode;

        public String getDbilldate() {
            return dbilldate;
        }

        public void setDbilldate(String dbilldate) {
            this.dbilldate = dbilldate;
        }

        private String dbilldate;
        private String busitypecode;
        private String vmemo;
        private String dr;
        public void setBody(List<String> body) {
            this.body = body;
        }
        public List<String> getBody() {
            return body;
        }

        public void setUnitcode(String unitcode) {
            this.unitcode = unitcode;
        }
        public String getUnitcode() {
            return unitcode;
        }

        public void setBillmakercode(String billmakercode) {
            this.billmakercode = billmakercode;
        }
        public String getBillmakercode() {
            return billmakercode;
        }

        public void setCountry(String country) {
            this.country = country;
        }
        public String getCountry() {
            return country;
        }

        public void setCdeliveryid(String cdeliveryid) {
            this.cdeliveryid = cdeliveryid;
        }
        public String getCdeliveryid() {
            return cdeliveryid;
        }

        public void setVbillcode(String vbillcode) {
            this.vbillcode = vbillcode;
        }
        public String getVbillcode() {
            return vbillcode;
        }

        public void setTransporttypecode(String transporttypecode) {
            this.transporttypecode = transporttypecode;
        }
        public String getTransporttypecode() {
            return transporttypecode;
        }

        public void setPupsndoccode(String pupsndoccode) {
            this.pupsndoccode = pupsndoccode;
        }
        public String getPupsndoccode() {
            return pupsndoccode;
        }

        public void setVtrantypecode(String vtrantypecode) {
            this.vtrantypecode = vtrantypecode;
        }
        public String getVtrantypecode() {
            return vtrantypecode;
        }

        public void setDeptcode(String deptcode) {
            this.deptcode = deptcode;
        }
        public String getDeptcode() {
            return deptcode;
        }



        public void setBusitypecode(String busitypecode) {
            this.busitypecode = busitypecode;
        }
        public String getBusitypecode() {
            return busitypecode;
        }

        public void setVmemo(String vmemo) {
            this.vmemo = vmemo;
        }
        public String getVmemo() {
            return vmemo;
        }

        public void setDr(String dr) {
            this.dr = dr;
        }
        public String getDr() {
            return dr;
        }

    }

}
