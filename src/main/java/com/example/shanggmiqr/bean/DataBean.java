package com.example.shanggmiqr.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DataBean implements Parcelable {


        private String errno;
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

        private String vbillcode;
        private List<Body> body;
        private String num;
        private String ts;

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

        private String dbilldate;
        private String org;
        private String dr;
        private String headpk;
        public void setVbillcode(String vbillcode) {
            this.vbillcode = vbillcode;
        }
        public String getVbillcode() {
            return vbillcode;
        }

        public void setBody(List<Body> body) {
            this.body = body;
        }
        public List<Body> getBody() {
            return body;
        }

        public void setNum(String num) {
            this.num = num;
        }
        public String getNum() {
            return num;
        }



        public void setOrg(String org) {
            this.org = org;
        }
        public String getOrg() {
            return org;
        }

        public void setDr(String dr) {
            this.dr = dr;
        }
        public String getDr() {
            return dr;
        }

        public void setHeadpk(String headpk) {
            this.headpk = headpk;
        }
        public String getHeadpk() {
            return headpk;
        }
        public class Body {

            private String itempk;
            private String materialname;
            private String nnum;
            private String warehouse;
            private String maccode;
            private String materialcode;
            public void setItempk(String itempk) {
                this.itempk = itempk;
            }
            public String getItempk() {
                return itempk;
            }

            public void setMaterialname(String materialname) {
                this.materialname = materialname;
            }
            public String getMaterialname() {
                return materialname;
            }

            public void setNnum(String nnum) {
                this.nnum = nnum;
            }
            public String getNnum() {
                return nnum;
            }

            public void setWarehouse(String warehouse) {
                this.warehouse = warehouse;
            }
            public String getWarehouse() {
                return warehouse;
            }

            public void setMaccode(String maccode) {
                this.maccode = maccode;
            }
            public String getMaccode() {
                return maccode;
            }

            public void setMaterialcode(String materialcode) {
                this.materialcode = materialcode;
            }
            public String getMaterialcode() {
                return materialcode;
            }

        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.errno);
        dest.writeString(this.errmsg);
        dest.writeString(this.pagenum);
        dest.writeString(this.pagetotal);
        dest.writeList(this.data);
    }

    public DataBean() {
    }

    protected DataBean(Parcel in) {
        this.errno = in.readString();
        this.errmsg = in.readString();
        this.pagenum = in.readString();
        this.pagetotal = in.readString();
        this.data = new ArrayList<Data>();
        in.readList(this.data, Data.class.getClassLoader());
    }

    public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
        @Override
        public DataBean createFromParcel(Parcel source) {
            return new DataBean(source);
        }

        @Override
        public DataBean[] newArray(int size) {
            return new DataBean[size];
        }
    };
}
