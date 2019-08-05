package com.example.shanggmiqr.bean;

public class LoginBean {
    public String getAppuser() {
        return appuser;
    }

    public void setAppuser(String appuser) {
        this.appuser = appuser;
    }

    public String getApppassword() {
        return apppassword;
    }

    public void setApppassword(String apppassword) {
        this.apppassword = apppassword;
    }

    String appuser;
    String apppassword;


        private String issuccess;
        private String errmsg;
        private String power;
        public void setIssuccess(String issuccess) {
            this.issuccess = issuccess;
        }
        public String getIssuccess() {
            return issuccess;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }
        public String getErrmsg() {
            return errmsg;
        }

        public void setPower(String power) {
            this.power = power;
        }
        public String getPower() {
            return power;
        }


}
