package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class User {

    /**
     * errno : 0
     * errmsg : 查询成功
     * pagenum : 1
     * pagetotal : 6
     * data : [{"userpk":"0001J1100000000020FJ","code":"sunmi05","name":"集团管理员sunmi05","org":"","enablestate":2,"ts":"2018-05-18 10:49:40"},{"userpk":"1001J110000000000AB9","code":"uf06","name":"hr实施人员1","org":"","enablestate":2,"ts":"2018-05-18 13:20:56"},{"userpk":"1001J110000000000W33","code":"SM0443","name":"尹凯月","org":"","enablestate":2,"ts":"2018-06-19 09:24:11"},{"userpk":"1001J110000000000W39","code":"SM0059","name":"卢春瑛","org":"","enablestate":2,"ts":"2018-05-25 10:59:41"},{"userpk":"1001J11000000000105A","code":"SM0859","name":"王国婵","org":"","enablestate":2,"ts":"2018-05-25 15:22:58"},{"userpk":"1001J11000000000105G","code":"SM0793","name":"李晓军","org":"","enablestate":2,"ts":"2018-05-25 15:22:58"},{"userpk":"1001J11000000000105M","code":"SM0808","name":"易威林","org":"","enablestate":2,"ts":"2018-05-25 15:22:58"},{"userpk":"1001J11000000000105S","code":"SM0832","name":"蒋敦川","org":"","enablestate":2,"ts":"2018-05-25 15:22:58"},{"userpk":"1001J11000000000105Y","code":"SM0168","name":"倪神州","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J110000000001064","code":"SM0193","name":"张新婷","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J11000000000106A","code":"SM0647","name":"李少文","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J11000000000106G","code":"SM0648","name":"孙恒志","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J11000000000106M","code":"SM0655","name":"周吉瑞","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J11000000000106S","code":"SM0690","name":"訾卫齐","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J11000000000106Y","code":"SM0742","name":"俞杰耀","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J110000000001074","code":"SM0818","name":"高云龙","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J11000000000107A","code":"SM0533","name":"史德锋","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J11000000000107G","code":"SM0653","name":"鲁旭升","org":"","enablestate":2,"ts":"2018-05-25 15:22:59"},{"userpk":"1001J11000000000107M","code":"SM0807","name":"朱勇","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J11000000000107S","code":"SM0450","name":"何伟伟","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J11000000000107Y","code":"SM0425","name":"张苏红","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J110000000001084","code":"SM0704","name":"聂鹤宇","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J11000000000108A","code":"SM0757","name":"胡亦卓","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J11000000000108G","code":"SM0873","name":"程琳","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J11000000000108M","code":"SM0407","name":"张文瑭","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J11000000000108S","code":"SM0652","name":"夏凯凯","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J11000000000108Y","code":"SM0783","name":"冯翔","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J110000000001094","code":"SM0789","name":"王旭明","org":"","enablestate":2,"ts":"2018-05-25 15:23:00"},{"userpk":"1001J11000000000109A","code":"SM0453","name":"韩双锋","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J11000000000109G","code":"SM0732","name":"郭世礼","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J11000000000109M","code":"SM0532","name":"李东","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J11000000000109S","code":"SM0804","name":"张亮亮","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J11000000000109Y","code":"SM0792","name":"周天外","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J1100000000010A4","code":"SM0856","name":"缪文","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J1100000000010AA","code":"SM0727","name":"姜明霞","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J1100000000010AG","code":"SM0586","name":"徐跻平","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J1100000000010AM","code":"SM0670","name":"王晓光","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J1100000000010AS","code":"SM0541","name":"周林","org":"","enablestate":2,"ts":"2018-05-25 15:23:01"},{"userpk":"1001J1100000000010AY","code":"SM0752","name":"周志刚","org":"","enablestate":2,"ts":"2018-05-25 15:23:02"},{"userpk":"1001J110000000000W3Q","code":"uf09","name":"LYB","org":"","enablestate":2,"ts":"2018-08-01 11:24:11"},{"userpk":"1001J1100000000010B4","code":"SM0509","name":"杨真安","org":"","enablestate":2,"ts":"2018-05-25 15:23:02"},{"userpk":"1001J1100000000010BA","code":"SM0788","name":"钱戈","org":"","enablestate":2,"ts":"2018-05-25 15:23:02"},{"userpk":"1001J1100000000010BG","code":"SM0855","name":"何平","org":"","enablestate":2,"ts":"2018-07-03 09:25:34"},{"userpk":"1001J1100000000010BM","code":"SM0867","name":"程家玲","org":"","enablestate":2,"ts":"2018-05-25 15:23:02"},{"userpk":"1001J1100000000010BS","code":"SM0840","name":"何奇辉","org":"","enablestate":2,"ts":"2018-05-25 15:23:02"},{"userpk":"1001J1100000000010BY","code":"SM0881","name":"张永","org":"","enablestate":2,"ts":"2018-05-25 15:23:02"},{"userpk":"1001J1100000000010C4","code":"SM0501","name":"苏宏然","org":"","enablestate":2,"ts":"2018-05-25 15:23:02"},{"userpk":"1001J1100000000010CA","code":"SM0768","name":"杜佩珺","org":"","enablestate":2,"ts":"2018-05-25 15:23:02"},{"userpk":"1001J1100000000010CG","code":"SM0684","name":"应莉","org":"","enablestate":2,"ts":"2018-05-25 15:23:02"},{"userpk":"1001J1100000000010CM","code":"SM0748","name":"徐发柳","org":"","enablestate":2,"ts":"2018-05-25 15:23:03"},{"userpk":"1001J1100000000010CS","code":"SM0848","name":"胡伟鹏","org":"","enablestate":2,"ts":"2018-05-25 15:23:03"},{"userpk":"1001J1100000000010CY","code":"SM0004","name":"陈桂鸿","org":"","enablestate":2,"ts":"2018-05-25 15:23:03"},{"userpk":"1001J1100000000010D4","code":"SM0432","name":"丁勇","org":"","enablestate":2,"ts":"2018-05-25 15:23:03"},{"userpk":"1001J1100000000010DA","code":"SM0711","name":"程林","org":"","enablestate":2,"ts":"2018-05-25 15:23:03"},{"userpk":"1001J1100000000010DG","code":"SM0735","name":"张胜旺","org":"","enablestate":2,"ts":"2018-05-25 15:23:03"},{"userpk":"1001J1100000000010DM","code":"SM0864","name":"邵继生","org":"","enablestate":2,"ts":"2018-05-25 15:23:03"},{"userpk":"1001J1100000000010DS","code":"SM0872","name":"蔡晓桓","org":"","enablestate":2,"ts":"2018-05-25 15:23:03"},{"userpk":"1001J1100000000010DY","code":"SM0806","name":"念海冲","org":"","enablestate":2,"ts":"2018-05-25 15:23:03"},{"userpk":"1001J1100000000010E4","code":"SM0813","name":"尹伊君","org":"","enablestate":2,"ts":"2018-05-25 15:23:04"},{"userpk":"1001J1100000000010EA","code":"SM0756","name":"夏云","org":"","enablestate":2,"ts":"2018-05-25 15:23:04"},{"userpk":"1001J1100000000010EG","code":"SM0609","name":"毕薇","org":"","enablestate":2,"ts":"2018-05-25 15:23:04"},{"userpk":"1001J1100000000010EM","code":"SM0607","name":"陈栋","org":"","enablestate":2,"ts":"2018-05-25 15:23:04"},{"userpk":"1001J1100000000010ES","code":"SM0591","name":"邵海强","org":"","enablestate":2,"ts":"2018-05-25 15:23:04"},{"userpk":"1001J1100000000010EY","code":"SM0657","name":"靳雨坤","org":"","enablestate":2,"ts":"2018-05-25 15:23:04"},{"userpk":"1001J1100000000010F4","code":"SM0439","name":"邓宏志","org":"","enablestate":2,"ts":"2018-05-25 15:23:04"},{"userpk":"1001J1100000000010FA","code":"SM0785","name":"张金普","org":"","enablestate":2,"ts":"2018-08-06 17:12:55"},{"userpk":"1001J1100000000010FG","code":"SM0436","name":"佘湘枫","org":"","enablestate":2,"ts":"2018-07-13 23:51:52"},{"userpk":"1001J1100000000010FM","code":"SM0849","name":"万磊","org":"","enablestate":2,"ts":"2018-05-25 15:23:04"},{"userpk":"1001J1100000000010FS","code":"SM0784","name":"陈炳升","org":"","enablestate":2,"ts":"2018-05-25 15:23:05"},{"userpk":"1001J1100000000010FY","code":"SM0871","name":"李佳","org":"","enablestate":2,"ts":"2018-06-13 09:52:13"},{"userpk":"1001J1100000000010G4","code":"SM0824","name":"田锐","org":"","enablestate":2,"ts":"2018-05-25 15:23:05"},{"userpk":"1001J1100000000010GA","code":"SM0406","name":"陆进程","org":"","enablestate":2,"ts":"2018-05-25 15:23:05"},{"userpk":"1001J1100000000010GG","code":"SM0649","name":"邢启凡","org":"","enablestate":2,"ts":"2018-05-25 15:23:05"},{"userpk":"1001J1100000000010GM","code":"SM0865","name":"杨席东","org":"","enablestate":2,"ts":"2018-05-25 15:23:05"},{"userpk":"1001J1100000000010GS","code":"SM0027","name":"周燕娜","org":"","enablestate":2,"ts":"2018-05-25 15:23:05"},{"userpk":"1001J1100000000010GY","code":"SM0706","name":"晁力凡","org":"","enablestate":2,"ts":"2018-05-25 15:23:05"},{"userpk":"1001J1100000000010H4","code":"SM0520","name":"邓成进","org":"","enablestate":2,"ts":"2018-05-25 15:23:05"},{"userpk":"1001J1100000000010HA","code":"SM0531","name":"徐赟庭","org":"","enablestate":2,"ts":"2018-05-25 15:23:05"},{"userpk":"0001J1100000000020F3","code":"sunmi03","name":"集团管理员sunmi03","org":"","enablestate":2,"ts":"2018-05-11 13:12:31"},{"userpk":"1001J1100000000006H1","code":"uf02","name":"uf02","org":"","enablestate":2,"ts":"2018-08-06 10:40:53"},{"userpk":"1001J110000000000ABD","code":"uf07","name":"hr实施人员2","org":"","enablestate":2,"ts":"2018-05-18 13:21:11"},{"userpk":"1001J110000000000KHS","code":"CT0003","name":"马先亮","org":"","enablestate":2,"ts":"2018-05-23 10:36:25"},{"userpk":"1001J110000000000KI2","code":"WY0026","name":"周莉苹","org":"","enablestate":2,"ts":"2018-06-14 17:17:59"},{"userpk":"1001J110000000000KIC","code":"SM0456","name":"陈洋","org":"","enablestate":2,"ts":"2018-06-22 16:29:30"},{"userpk":"1001J110000000000L7I","code":"WY0056","name":"姚玫菲","org":"","enablestate":2,"ts":"2018-05-23 10:36:07"},{"userpk":"1001J110000000000ZGR","code":"SM0686","name":"陶敏敏","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZGX","code":"SM0696","name":"仇永生","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZH3","code":"SM0699","name":"李相柳","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZH9","code":"SM0681","name":"蓝万强","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZHF","code":"SM0729","name":"周华华","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZHL","code":"SM0739","name":"蒋琛","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZHR","code":"SM0746","name":"孙洪伟","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZHX","code":"SM0654","name":"屠琦益","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZI3","code":"SM0779","name":"曹亮","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZI9","code":"SM0801","name":"李玉卓","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZIF","code":"SM0835","name":"姜智能","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZIL","code":"SM0842","name":"王忠平","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZIR","code":"SM0579","name":"沈军华","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZIX","code":"SM0514","name":"兰胜芳","org":"","enablestate":2,"ts":"2018-05-25 15:22:50"},{"userpk":"1001J110000000000ZJ3","code":"SM0394","name":"徐欣","org":"","enablestate":2,"ts":"2018-05-25 15:22:51"}]
     */

    private String errno;
    private String errmsg;
    private String pagenum;
    private int pagetotal;
    private List<UserDataBean> data;

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

    public void setData(List<UserDataBean> data) {
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

    public List<UserDataBean> getData() {
        return data;
    }

    public static class UserDataBean {
        /**
         * userpk : 0001J1100000000020FJ
         * code : sunmi05
         * name : 集团管理员sunmi05
         * org :
         * enablestate : 2
         * ts : 2018-05-18 10:49:40
         */

        public String userpk;
        public String code;
        public String name;
        public String org;
        public int enablestate;
        public String ts;

        public void setUserpk(String userpk) {
            this.userpk = userpk;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public void setEnablestate(int enablestate) {
            this.enablestate = enablestate;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public String getUserpk() {
            return userpk;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public String getOrg() {
            return org;
        }

        public int getEnablestate() {
            return enablestate;
        }

        public String getTs() {
            return ts;
        }
    }
}
