package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class Customer {

    /**
     * errno : 0
     * errmsg : 查询成功
     * pagenum : 1
     * pagetotal : 103
     * data : [{"pk_customer":"1001J1100000000026OP","code":"C000994","name":"汕头市龙湖区源杰大鼎饭店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:26:46"},{"pk_customer":"1001J1100000000026PD","code":"C001000","name":"汕头市心意鲜花店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:26:48"},{"pk_customer":"1001J1100000000026PX","code":"C001005","name":"汕头市金平区红双菱","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:26:49"},{"pk_customer":"1001J1100000000026QH","code":"C001010","name":"广东省汕头市DIY水景坊","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:26:51"},{"pk_customer":"1001J1100000000026R1","code":"C001015","name":"汕头市金平区佰姓购物","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:26:52"},{"pk_customer":"1001J1100000000026T5","code":"C001034","name":"汕头市金平区回味饺面店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:26:57"},{"pk_customer":"1001J1100000000026TL","code":"C001038","name":"揭阳市流沙镇音","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:26:58"},{"pk_customer":"1001J1100000000026U5","code":"C001043","name":"揭阳市普宁市心悦宠物店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:00"},{"pk_customer":"1001J1100000000026UP","code":"C001048","name":"汕头市金平区佳友购物超市","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:01"},{"pk_customer":"1001J1100000000026V9","code":"C001053","name":"汕头市金平区生物股长","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:02"},{"pk_customer":"1001J1100000000026VH","code":"C001055","name":"汕头市金平区讯嘉数码","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:03"},{"pk_customer":"1001J1100000000026W1","code":"C001060","name":"汕头市东信数码","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:04"},{"pk_customer":"1001J1100000000026WL","code":"C001065","name":"汕头市龙湖区尚美剪烫店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:06"},{"pk_customer":"1001J1100000000026ZP","code":"C001093","name":"揭阳市学生街乌妹妹","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:13"},{"pk_customer":"1001J110000000002709","code":"C001098","name":"汕头市龙湖区思韵茶行","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:15"},{"pk_customer":"1001J11000000000270T","code":"C001103","name":"汕头龙湖上豪鸡排","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:16"},{"pk_customer":"1001J11000000000271D","code":"C001108","name":"汕头市MAX心鹿","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:18"},{"pk_customer":"1001J11000000000271X","code":"C001113","name":"汕头市陕西肉夹馍凉皮","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:19"},{"pk_customer":"1001J11000000000272H","code":"C001118","name":"汕头市金平区恒生手机红荔店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:21"},{"pk_customer":"1001J110000000002731","code":"C001123","name":"汕头市金平区优乐哆","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:22"},{"pk_customer":"1001J11000000000275X","code":"C001149","name":"汕头市潮南区依尔美","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:29"},{"pk_customer":"1001J11000000000276P","code":"C001156","name":"汕头市峡山街道特价区","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:31"},{"pk_customer":"1001J110000000002779","code":"C001161","name":"广东省桥头镇米米丽","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:32"},{"pk_customer":"1001J11000000000278T","code":"C001175","name":"汕头市潮阳区谷饶环立手机店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:36"},{"pk_customer":"1001J11000000000279D","code":"C001180","name":"汕头市潮阳区派派休闲小站","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:37"},{"pk_customer":"1001J1100000000027A9","code":"C001188","name":"汕头市宁和街欧米兰","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:40"},{"pk_customer":"1001J110000000000VP9","code":"100102","name":"上海商米深圳分公司","custstate":1,"enablestate":2,"custprop":1,"custclassid":"88","groupcode":"sunmi","orgcode":"","ts":"2018-05-23 18:17:19"},{"pk_customer":"1001J1100000000027AT","code":"C001193","name":"汕头市金平区中国移动4G鸿通通讯","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:41"},{"pk_customer":"1001J1100000000027BD","code":"C001198","name":"汕头市澄海区彤彤家烘培材料","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:42"},{"pk_customer":"1001J1100000000027BT","code":"C001202","name":"汕头市麦莱基餐厅","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:43"},{"pk_customer":"1001J1100000000027CD","code":"C001207","name":"汕头市龙湖区517炸鸡","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:45"},{"pk_customer":"1001J1100000000027CX","code":"C001212","name":"汕头市百变时尚美发店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:46"},{"pk_customer":"1001J1100000000027DH","code":"C001217","name":"汕头市潮南区博详电讯","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:48"},{"pk_customer":"1001J1100000000027ET","code":"C001229","name":"汕头市潮南区万家福超市","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:51"},{"pk_customer":"1001J1100000000027FD","code":"C001234","name":"汕头市金平区博诚数码","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:52"},{"pk_customer":"1001J1100000000027HH","code":"C001253","name":"揭阳市三月写真馆","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:27:58"},{"pk_customer":"1001J1100000000027ID","code":"C001261","name":"汕头市龙湖区小熊专业贴膜","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:00"},{"pk_customer":"1001J1100000000027IX","code":"C001266","name":"汕头市金平区永盛粮油店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:01"},{"pk_customer":"1001J1100000000027JH","code":"C001271","name":"汕头市澄海区锌数码","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:02"},{"pk_customer":"1001J1100000000027LL","code":"C001290","name":"汕头市金平区兄妹理发店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:08"},{"pk_customer":"1001J1100000000027N1","code":"C001303","name":"汕头市服装街橙意","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:11"},{"pk_customer":"1001J1100000000027NL","code":"C001308","name":"汕头市新阪宿","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:12"},{"pk_customer":"1001J1100000000027P5","code":"C001322","name":"汕头市粤度数码","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:16"},{"pk_customer":"1001J1100000000027PP","code":"C001327","name":"汕头市正宗隆江猪脚饭","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:18"},{"pk_customer":"1001J1100000000027QP","code":"C001336","name":"汕头市龙湖区裕兴隆江猪脚饭","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:20"},{"pk_customer":"1001J1100000000027R9","code":"C001341","name":"汕头市C&S美妆","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:21"},{"pk_customer":"1001J1100000000027U5","code":"C001367","name":"汕头市益家人便利店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:29"},{"pk_customer":"1001J1100000000027UP","code":"C001372","name":"汕头市杰成通讯","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:30"},{"pk_customer":"1001J1100000000027VP","code":"C001381","name":"汕头市珠津油漆","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:33"},{"pk_customer":"1001J1100000000027W9","code":"C001386","name":"汕头市龙湖区手工饺子","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:34"},{"pk_customer":"1001J1100000000027WT","code":"C001391","name":"汕头市捷步电动自行车行","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:36"},{"pk_customer":"1001J1100000000027XD","code":"C001396","name":"汕头市金平区广兴电器店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:28:37"},{"pk_customer":"1001J110000000000VP3","code":"1001","name":"上海商米科技有限公司","custstate":1,"enablestate":2,"custprop":1,"custclassid":"88","groupcode":"sunmi","orgcode":"","ts":"2018-05-23 18:16:48"},{"pk_customer":"1001J110000000000VP7","code":"100100","name":"深圳米开朗基罗科技有限公司","custstate":1,"enablestate":2,"custprop":1,"custclassid":"88","groupcode":"sunmi","orgcode":"","ts":"2018-06-28 11:49:47"},{"pk_customer":"1001J110000000000VPA","code":"100103","name":"广东川田科技有限公司","custstate":1,"enablestate":2,"custprop":1,"custclassid":"88","groupcode":"sunmi","orgcode":"","ts":"2018-05-23 18:17:28"},{"pk_customer":"1001J1100000000023GO","code":"C000021","name":"汕头市优客龙进口商品","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-26 16:57:37"},{"pk_customer":"1001J110000000001DDK","code":"000001","name":"上海喔噻互联网科技有限公司","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-08-20 10:10:01"},{"pk_customer":"1001J110000000001DQK","code":"000115","name":"深圳市比亚迪供应链管理有限公司","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-26 11:10:02"},{"pk_customer":"1001J1100000000023H8","code":"C000026","name":"汕头市金平区石头宴三阳炖品店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-26 16:57:38"},{"pk_customer":"1001J1100000000023I4","code":"C000034","name":"汕头金平区落夜湾来荷花园店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-26 16:57:40"},{"pk_customer":"1001J1100000000023JW","code":"C000050","name":"汕头市龙湖区翻滚吧饮品小吃店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-26 16:57:44"},{"pk_customer":"1001J1100000000023MS","code":"C000076","name":"汕头龙湖首捷电脑商行","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-26 16:57:51"},{"pk_customer":"1001J1100000000023WP","code":"C000094","name":"汕头市金平君临华便利店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:24"},{"pk_customer":"1001J1100000000023X9","code":"C000099","name":"汕头市龙湖区轩达数码","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:25"},{"pk_customer":"1001J1100000000023XT","code":"C000104","name":"汕头市龙湖区八葉世家红酒","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:27"},{"pk_customer":"1001J1100000000023YT","code":"C000113","name":"汕头市龙湖区旺德福购物","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:30"},{"pk_customer":"1001J1100000000023ZD","code":"C000118","name":"汕头市金平区超跃美食店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:32"},{"pk_customer":"1001J110000000002419","code":"C000135","name":"汕头市龙湖区德骏数码","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:38"},{"pk_customer":"1001J11000000000241T","code":"C000140","name":"汕头市金平区n记炸鸡","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:40"},{"pk_customer":"1001J11000000000242D","code":"C000145","name":"汕头龙湖依美服装店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:42"},{"pk_customer":"1001J11000000000243X","code":"C000159","name":"汕头市金平区苏记餐饮店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:47"},{"pk_customer":"1001J11000000000244X","code":"C000168","name":"汕头市金平区千里香火锅店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:49"},{"pk_customer":"1001J11000000000245H","code":"C000173","name":"汕头市金平区全家福餐饮店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:51"},{"pk_customer":"1001J110000000002461","code":"C000178","name":"汕头市金平区食之坊餐饮店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:53"},{"pk_customer":"1001J11000000000247X","code":"C000195","name":"汕头市金平区伟彬弟鱼丸粿条面店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:58"},{"pk_customer":"1001J11000000000248D","code":"C000199","name":"汕头市金平区味聚源饮食店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:22:59"},{"pk_customer":"1001J11000000000248X","code":"C000204","name":"汕头龙湖本色牛仔服装店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:00"},{"pk_customer":"1001J11000000000249H","code":"C000209","name":"汕头市金平区鹏仕餐饮店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:02"},{"pk_customer":"1001J1100000000024BP","code":"C000229","name":"汕头市金平区盛少锋餐饮店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:08"},{"pk_customer":"1001J1100000000024C9","code":"C000234","name":"汕头市龙湖区鸡翅小哥","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:10"},{"pk_customer":"1001J1100000000024CT","code":"C000239","name":"汕头市龙湖区张记酸菜鱼","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:11"},{"pk_customer":"1001J1100000000024FP","code":"C000265","name":"汕头市金平区潮惠餐饮店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:19"},{"pk_customer":"1001J1100000000024G9","code":"C000270","name":"汕头市龙湖区刘海金餐饮店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:21"},{"pk_customer":"1001J1100000000024H9","code":"C000279","name":"汕头市金平区蜜源餐饮店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:24"},{"pk_customer":"1001J1100000000024HT","code":"C000284","name":"汕头市瓦罐营养美食","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:25"},{"pk_customer":"1001J1100000000024ID","code":"C000289","name":"汕头市龙湖区潮汕火足肉","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:27"},{"pk_customer":"1001J1100000000024JT","code":"C000302","name":"汕头金平博雅木艺","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:31"},{"pk_customer":"1001J1100000000024KD","code":"C000307","name":"汕头市浩森贸易有限公司","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:32"},{"pk_customer":"1001J1100000000024LD","code":"C000316","name":"汕头市洁净一百店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:35"},{"pk_customer":"1001J1100000000024LX","code":"C000321","name":"汕头市龙湖区饶平煲仔饭","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:36"},{"pk_customer":"1001J1100000000024MD","code":"C000325","name":"汕头市金平区汉金水果店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:37"},{"pk_customer":"1001J1100000000024PH","code":"C000353","name":"汕头市龙湖区富隆肥鸡饭店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:46"},{"pk_customer":"1001J1100000000024Q1","code":"C000358","name":"汕头市龙湖区七门会社服装店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:47"},{"pk_customer":"1001J1100000000024SD","code":"C000379","name":"汕头市胜强运输","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:53"},{"pk_customer":"1001J1100000000024SX","code":"C000384","name":"汕头市龙湖区益家人便利店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:55"},{"pk_customer":"1001J1100000000024TH","code":"C000389","name":"汕头金平微尼尔数码","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:56"},{"pk_customer":"1001J1100000000024UD","code":"C000397","name":"汕头市懒人日记","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:23:59"},{"pk_customer":"1001J1100000000024UX","code":"C000402","name":"汕头市龙湖区派派餐饮店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:24:00"},{"pk_customer":"1001J1100000000024VH","code":"C000407","name":"汕头金平亚鸿甘草水果华坞店","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:24:01"},{"pk_customer":"1001J1100000000024WD","code":"C000415","name":"汕头市龙湖区兴旺购物","custstate":1,"enablestate":2,"custprop":0,"custclassid":"01","groupcode":"sunmi","orgcode":"","ts":"2018-05-27 14:24:04"}]
     */

    private String errno;
    private String errmsg;
    private String pagenum;
    private int pagetotal;
    private List<CustomerDataBean> data;

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

    public void setData(List<CustomerDataBean> data) {
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

    public List<CustomerDataBean> getData() {
        return data;
    }

    public static class CustomerDataBean {
        /**
         * pk_customer : 1001J1100000000026OP
         * code : C000994
         * name : 汕头市龙湖区源杰大鼎饭店
         * custstate : 1
         * enablestate : 2
         * custprop : 0
         * custclassid : 01
         * groupcode : sunmi
         * orgcode :
         * ts : 2018-05-27 14:26:46
         */

        public String pk_customer;
        public String code;
        public String name;
        public int custstate;
        public int enablestate;
        public int custprop;
        public String custclassid;
        public String groupcode;
        public String orgcode;
        public String ts;

        public void setPk_customer(String pk_customer) {
            this.pk_customer = pk_customer;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCuststate(int custstate) {
            this.custstate = custstate;
        }

        public void setEnablestate(int enablestate) {
            this.enablestate = enablestate;
        }

        public void setCustprop(int custprop) {
            this.custprop = custprop;
        }

        public void setCustclassid(String custclassid) {
            this.custclassid = custclassid;
        }

        public void setGroupcode(String groupcode) {
            this.groupcode = groupcode;
        }

        public void setOrgcode(String orgcode) {
            this.orgcode = orgcode;
        }

        public void setTs(String ts) {
            this.ts = ts;
        }

        public String getPk_customer() {
            return pk_customer;
        }

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }

        public int getCuststate() {
            return custstate;
        }

        public int getEnablestate() {
            return enablestate;
        }

        public int getCustprop() {
            return custprop;
        }

        public String getCustclassid() {
            return custclassid;
        }

        public String getGroupcode() {
            return groupcode;
        }

        public String getOrgcode() {
            return orgcode;
        }

        public String getTs() {
            return ts;
        }
    }
}
