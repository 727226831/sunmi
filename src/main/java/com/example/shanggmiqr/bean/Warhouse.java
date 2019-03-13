package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class Warhouse {

    /**
     * errno : 0
     * pagenum : 2
     * pagetotal : 3
     * data : [{"stordocpk":"1001J110000000000WCI","name":"商业应用客户仓","code":"0011","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:44"},{"stordocpk":"1001J110000000000WCS","name":"我有便利货架仓","code":"0016","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:46"},{"stordocpk":"1001J110000000000WEE","name":"易瑞通","code":"0045","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:53"},{"stordocpk":"1001J110000000000WEO","name":"金算盘财税","code":"0050","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:54"},{"stordocpk":"1001J110000000000WEU","name":"银谷普惠","code":"0053","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:56"},{"stordocpk":"1001J110000000000WFY","name":"快修猿","code":"0069","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:59"},{"stordocpk":"1001J110000000000WGI","name":"天正店","code":"0079","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:03"},{"stordocpk":"1001J110000000000WGS","name":"恩宁文化店","code":"0084","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:04"},{"stordocpk":"1001J110000000000WH2","name":"华宏店","code":"0089","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:05"},{"stordocpk":"1001J110000000000WHC","name":"唯晟店","code":"0094","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:06"},{"stordocpk":"1001J110000000000WHU","name":"潮新店","code":"0103","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:09"},{"stordocpk":"1001J110000000000WI4","name":"世麦店","code":"0108","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:10"},{"stordocpk":"1001J110000000000WJ6","name":"借机仓","code":"0127","enablestate":2,"orgcode":"1001","ts":"2018-07-14 15:37:20"},{"stordocpk":"1001J110000000000WJA","name":"售后配件仓","code":"0129","enablestate":2,"orgcode":"1001","ts":"2018-07-02 16:17:24"},{"stordocpk":"1001J110000000000WJS","name":"鑫宇仓电商仓","code":"0138","enablestate":2,"orgcode":"1001","ts":"2018-06-30 11:28:04"},{"stordocpk":"1001J110000000000WK4","name":"常州仓电商仓","code":"0144","enablestate":2,"orgcode":"1001","ts":"2018-07-02 16:18:42"},{"stordocpk":"1001J110000000064YQ5","name":"瑟曦影棚","code":"0123","enablestate":2,"orgcode":"100104","ts":"2018-07-05 16:27:18"},{"stordocpk":"0001J1100000000027TV","name":"川田-直运仓","code":"DTW","enablestate":2,"orgcode":"100103","ts":"2018-06-13 09:31:36"},{"stordocpk":"1001J110000000000WD6","name":"小虾米会计咨询","code":"0023","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:48"},{"stordocpk":"1001J110000000000WDA","name":"荣诚食品","code":"0025","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:48"},{"stordocpk":"1001J110000000000WDK","name":"全新科技","code":"0030","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:49"},{"stordocpk":"1001J110000000000WDU","name":"小数点科技","code":"0035","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:50"},{"stordocpk":"1001J110000000000WE4","name":"英迅动力","code":"0040","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:51"},{"stordocpk":"1001J110000000000WF6","name":"昇源金融服务","code":"0059","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:57"},{"stordocpk":"1001J110000000000WFI","name":"悠融服务","code":"0065","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:58"},{"stordocpk":"1001J110000000000WFW","name":"享相现场","code":"0068","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:59"},{"stordocpk":"1001J110000000000WG0","name":"澳新考拉","code":"0070","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:00"},{"stordocpk":"1001J110000000000WGE","name":"华图宏阳教育","code":"0077","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:03"},{"stordocpk":"1001J110000000000WGO","name":"粤成科技","code":"0082","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:04"},{"stordocpk":"1001J110000000000WGY","name":"合道创展企划店","code":"0087","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:05"},{"stordocpk":"1001J110000000000WH8","name":"企业管理店","code":"0092","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:06"},{"stordocpk":"1001J110000000000WHW","name":"富美贸易店","code":"0104","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:09"},{"stordocpk":"1001J110000000000WI6","name":"全民教育店","code":"0109","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:11"},{"stordocpk":"1001J110000000000WJW","name":"服务站物料仓","code":"0140","enablestate":2,"orgcode":"1001","ts":"2018-07-02 16:18:15"},{"stordocpk":"1001J110000000000WK8","name":"川田-泰泽材料仓","code":"0146","enablestate":2,"orgcode":"100103","ts":"2018-06-16 18:18:16"},{"stordocpk":"1001J110000000000WKC","name":"川田-本部成品仓","code":"0148","enablestate":2,"orgcode":"100103","ts":"2018-06-13 09:28:55"},{"stordocpk":"1001J110000000000WKI","name":"川田-客服维修仓（内部）","code":"0151","enablestate":2,"orgcode":"100103","ts":"2018-07-05 16:35:14"},{"stordocpk":"1001J110000000064YQD","name":"安联保险","code":"0127","enablestate":2,"orgcode":"100104","ts":"2018-07-05 16:27:21"},{"stordocpk":"1001J110000000064YQN","name":"华润置地地产","code":"0132","enablestate":2,"orgcode":"100104","ts":"2018-07-05 16:27:23"},{"stordocpk":"1001J110000000064YR3","name":"粤特汽车","code":"0140","enablestate":2,"orgcode":"100104","ts":"2018-07-05 16:27:26"},{"stordocpk":"0001J1100000000027OR","name":"直运仓","code":"DTW","enablestate":2,"orgcode":"100100","ts":"2018-05-17 10:54:11"},{"stordocpk":"1001J110000000000WBX","name":"收钱吧总仓","code":"0001","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:42"},{"stordocpk":"1001J110000000000WC2","name":"收钱吧客户仓","code":"0003","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:43"},{"stordocpk":"1001J110000000000WC6","name":"我有外卖总仓","code":"0005","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:43"},{"stordocpk":"1001J110000000000WCC","name":"我有外卖废品仓","code":"0008","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:44"},{"stordocpk":"1001J110000000000WCO","name":"我有便利样机仓","code":"0014","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:45"},{"stordocpk":"1001J110000000000WCY","name":"随想曲","code":"0019","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:47"},{"stordocpk":"1001J110000000000WD0","name":"海信集团","code":"0020","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:47"},{"stordocpk":"1001J110000000000WD4","name":"共享网络科技","code":"0022","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:47"},{"stordocpk":"1001J110000000000WDI","name":"国美电器","code":"0029","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:49"},{"stordocpk":"1001J110000000000WDS","name":"稀雅图建筑装饰","code":"0034","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:50"},{"stordocpk":"1001J110000000000WE2","name":"鼎坚商务咨询","code":"0039","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:51"},{"stordocpk":"1001J110000000000WE8","name":"博源资本","code":"0042","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:52"},{"stordocpk":"1001J110000000000WEA","name":"百城人力网络","code":"0043","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:52"},{"stordocpk":"1001J110000000000WEI","name":"快巴电子商务","code":"0047","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:53"},{"stordocpk":"1001J110000000000WEK","name":"汕国商信息资源","code":"0048","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:53"},{"stordocpk":"1001J110000000000WEQ","name":"泰业空间","code":"0051","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:54"},{"stordocpk":"1001J110000000000WES","name":"汕联合通达船务","code":"0052","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:55"},{"stordocpk":"1001J110000000000WF0","name":"平安普惠","code":"0056","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:56"},{"stordocpk":"1001J110000000000WF2","name":"万利电子商务","code":"0057","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:56"},{"stordocpk":"1001J110000000000WFC","name":"标远汽车","code":"0062","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:58"},{"stordocpk":"1001J110000000000WFS","name":"金蟾投资","code":"0066","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:59"},{"stordocpk":"1001J110000000000WFU","name":"智富咨询","code":"0067","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:59"},{"stordocpk":"1001J110000000000WG8","name":"青创科技","code":"0074","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:01"},{"stordocpk":"1001J110000000000WGK","name":"胜发电子店","code":"0080","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:04"},{"stordocpk":"1001J110000000000WGM","name":"广东东升信息技术","code":"0081","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:04"},{"stordocpk":"1001J110000000000WGU","name":"大勤环保科技店","code":"0085","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:05"},{"stordocpk":"1001J110000000000WGW","name":"侠客文化传媒店","code":"0086","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:05"},{"stordocpk":"1001J110000000000WH4","name":"和联兴店","code":"0090","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:06"},{"stordocpk":"1001J110000000000WH6","name":"思瑞店","code":"0091","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:06"},{"stordocpk":"1001J110000000000WHE","name":"火线店","code":"0095","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:07"},{"stordocpk":"1001J110000000000WHQ","name":"橙子科技店","code":"0101","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:08"},{"stordocpk":"1001J110000000000WHS","name":"永图店","code":"0102","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:09"},{"stordocpk":"1001J110000000000WI0","name":"经传店","code":"0106","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:10"},{"stordocpk":"1001J110000000000WI2","name":"捷顺科技店","code":"0107","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:10"},{"stordocpk":"1001J110000000000WIA","name":"鸿泰鼎石店","code":"0111","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:12"},{"stordocpk":"1001J110000000000WIC","name":"云联网络店","code":"0112","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:12"},{"stordocpk":"1001J110000000000WIM","name":"人寿保险店","code":"0117","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:45:13"},{"stordocpk":"1001J110000000000WJ4","name":"海外仓","code":"0126","enablestate":2,"orgcode":"1001","ts":"2018-07-02 16:17:03"},{"stordocpk":"1001J110000000000WJK","name":"售后待发货仓","code":"0134","enablestate":2,"orgcode":"1001","ts":"2018-07-02 16:17:53"},{"stordocpk":"1001J110000000000WJO","name":"鑫宇仓国内仓","code":"0136","enablestate":2,"orgcode":"1001","ts":"2018-06-30 11:26:37"},{"stordocpk":"1001J110000000000WKE","name":"川田-生产线边仓","code":"0149","enablestate":2,"orgcode":"100103","ts":"2018-06-16 18:24:59"},{"stordocpk":"1001J11000000000OLUA","name":"川田贸易-成品仓","code":"0156","enablestate":2,"orgcode":"10010301","ts":"2018-06-16 18:12:05"},{"stordocpk":"1001J11000000000OUM7","name":"川田-泰泽成品仓","code":"0157","enablestate":2,"orgcode":"100103","ts":"2018-06-16 18:19:28"},{"stordocpk":"1001J11000000006E543","name":"市场部仓库","code":"0148","enablestate":2,"orgcode":"100104","ts":"2018-07-11 15:43:47"},{"stordocpk":"1001J110000000064YQV","name":"小牛普惠","code":"0136","enablestate":2,"orgcode":"100104","ts":"2018-07-10 11:53:44"},{"stordocpk":"1001J11000000000BATH","name":"国内仓","code":"0146","enablestate":2,"orgcode":"1001","ts":"2018-07-02 16:18:56"},{"stordocpk":"1001J110000000064YRF","name":"志远汽贸","code":"0146","enablestate":2,"orgcode":"100104","ts":"2018-07-05 16:27:28"},{"stordocpk":"1001J110000000064YQ7","name":"岸睐文化","code":"0124","enablestate":2,"orgcode":"100104","ts":"2018-07-05 16:27:20"},{"stordocpk":"1001J110000000064YQX","name":"时间城","code":"0137","enablestate":2,"orgcode":"100104","ts":"2018-07-05 16:27:25"},{"stordocpk":"0001J1100000000027S8","name":"直运仓","code":"DTW","enablestate":2,"orgcode":"100102","ts":"2018-05-17 10:55:47"},{"stordocpk":"1001J110000000000WC8","name":"我有外卖样机仓","code":"0006","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:43"},{"stordocpk":"1001J110000000000WCK","name":"商业应用废品仓","code":"0012","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:45"},{"stordocpk":"1001J110000000000WCU","name":"我有便利废品仓","code":"0017","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:46"},{"stordocpk":"1001J110000000000WD2","name":"亿鑫塑胶","code":"0021","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:47"},{"stordocpk":"1001J110000000000WD8","name":"秒趣科技","code":"0024","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:48"},{"stordocpk":"1001J110000000000WDE","name":"宝凡信息技术","code":"0027","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:49"},{"stordocpk":"1001J110000000000WDG","name":"名匠装修","code":"0028","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:49"},{"stordocpk":"1001J110000000000WDO","name":"启航汽车","code":"0032","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:50"},{"stordocpk":"1001J110000000000WDQ","name":"海马汽车","code":"0033","enablestate":2,"orgcode":"100104","ts":"2018-05-25 11:44:50"}]
     */

    private int errno;
    private int pagenum;
    private int pagetotal;
    private List<WarhouseDataBean> data;

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public void setPagetotal(int pagetotal) {
        this.pagetotal = pagetotal;
    }

    public void setData(List<WarhouseDataBean> data) {
        this.data = data;
    }

    public int getErrno() {
        return errno;
    }

    public int getPagenum() {
        return pagenum;
    }

    public int getPagetotal() {
        return pagetotal;
    }

    public List<WarhouseDataBean> getData() {
        return data;
    }

    public static class WarhouseDataBean {
        /**
         * stordocpk : 1001J110000000000WCI
         * name : 商业应用客户仓
         * code : 0011
         * enablestate : 2
         * orgcode : 100104
         * ts : 2018-05-25 11:44:44
         */

        public String stordocpk;
        public String name;
        public String code;
        public int enablestate;
        public String orgcode;
        public String ts;

        public void setStordocpk(String stordocpk) {
            this.stordocpk = stordocpk;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setEnablestate(int enablestate) {
            this.enablestate = enablestate;
        }

        public void setOrgcode(String orgcode) {
            this.orgcode = orgcode;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public String getStordocpk() {
            return stordocpk;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public int getEnablestate() {
            return enablestate;
        }

        public String getOrgcode() {
            return orgcode;
        }

        public String getTs() {
            return ts;
        }
    }
}
