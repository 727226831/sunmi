package com.example.shanggmiqr.bean;

import java.util.List;

public class MenuBean {


        private String issuccess;
        private String errmsg;
        private List<Power> power;
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

        public void setPower(List<Power> power) {
            this.power = power;
        }
        public List<Power> getPower() {
            return power;
        }


    public class Power {

        private String menuname;
        private String menucode;
        public void setMenuname(String menuname) {
            this.menuname = menuname;
        }
        public String getMenuname() {
            return menuname;
        }

        public void setMenucode(String menucode) {
            this.menucode = menucode;
        }
        public String getMenucode() {
            return menucode;
        }

    }
}
