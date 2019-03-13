package com.example.shanggmiqr.bean;

import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/3.
 */

public class MaterialBean {

    /**
     * data : [{"stordocpk":"1001J11000000000197A","name":"立顿英式果茶蜜桃味500ML","code":"F08000011","marbasclass":"F08 饮料","ename":"","materialspec":"500ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"立顿","enablestate":2,"materialbarcode":"6934024550705","ts":"2018-05-25 16:39:13"},{"stordocpk":"1001J11000000000197M","name":"七喜小胶瓶330ml","code":"F08000017","marbasclass":"F08 饮料","ename":"","materialspec":"330ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"百事","enablestate":2,"materialbarcode":"6908946287667","ts":"2018-05-25 16:39:15"},{"stordocpk":"1001J11000000000197W","name":"统一PET阿萨姆奶茶原味500ml","code":"F08000022","marbasclass":"F08 饮料","ename":"","materialspec":"500ML","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"统一","enablestate":2,"materialbarcode":"6925303730574","ts":"2018-05-25 16:39:17"},{"stordocpk":"1001J110000000001986","name":"统一TP奶茶麦香味250ml","code":"F08000027","marbasclass":"F08 饮料","ename":"","materialspec":"250ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"统一","enablestate":2,"materialbarcode":"6925303722623","ts":"2018-05-25 16:39:19"},{"stordocpk":"1001J11000000000198M","name":"伊利纯牛奶250ml","code":"F08000035","marbasclass":"F08 饮料","ename":"","materialspec":"250ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"伊利","enablestate":2,"materialbarcode":"6907992100272","ts":"2018-05-25 16:39:23"},{"stordocpk":"1001J11000000000198U","name":"诚有味鸭腿武汉黑鸭味40g","code":"F03000002","marbasclass":"F03 休闲零食","ename":"","materialspec":"40g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"诚有味","enablestate":2,"materialbarcode":"6924520610812","ts":"2018-05-25 16:39:24"},{"stordocpk":"1001J110000000001994","name":"甘源牌袋装兰花豆韩式烤肉味75g","code":"F03000007","marbasclass":"F03 休闲零食","ename":"","materialspec":"75g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"甘源","enablestate":2,"materialbarcode":"6940188805889","ts":"2018-05-25 16:39:26"},{"stordocpk":"1001J11000000000199G","name":"蜡笔小新果肉杯什锦200g","code":"F03000013","marbasclass":"F03 休闲零食","ename":"","materialspec":"200g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"蜡笔小新","enablestate":2,"materialbarcode":"6921101203876","ts":"2018-05-25 16:39:29"},{"stordocpk":"1001J11000000000199M","name":"傻二哥兰花豆麻辣味68g","code":"F03000016","marbasclass":"F03 休闲零食","ename":"","materialspec":"68g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"傻二哥","enablestate":2,"materialbarcode":"6933311067131","ts":"2018-05-25 16:39:30"},{"stordocpk":"1001J1100000000019AE","name":"阿尔卑斯至纯牛奶软糖条形装黄桃酸奶味","code":"F02000007","marbasclass":"F02 糖果","ename":"","materialspec":"47g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"阿尔卑斯","enablestate":2,"materialbarcode":"6911316380288","ts":"2018-05-25 16:39:35"},{"stordocpk":"1001J1100000000019BY","name":"亲亲虾条烧烤味46g","code":"F04000004","marbasclass":"F04 膨化食品","ename":"","materialspec":"46g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"亲亲","enablestate":2,"materialbarcode":"6923775923975","ts":"2018-05-25 16:39:46"},{"stordocpk":"1001J1100000000019C6","name":"四洲热浪薯片香辣味90g","code":"F04000008","marbasclass":"F04 膨化食品","ename":"","materialspec":"90g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"四洲","enablestate":2,"materialbarcode":"6932430200108","ts":"2018-05-25 16:39:48"},{"stordocpk":"1001J1100000000019CU","name":"合味道虾仁原味面84g","code":"F07000004","marbasclass":"F07 方便面","ename":"","materialspec":"84g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"日清","enablestate":2,"materialbarcode":"6917935002242","ts":"2018-05-25 16:39:52"},{"stordocpk":"1001J1100000000019DK","name":"嘉顿梳打饼干蔬菜味","code":"F05000009","marbasclass":"F05 饼干蛋糕","ename":"","materialspec":"115g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"嘉顿","enablestate":2,"materialbarcode":"6902227010449","ts":"2018-05-25 16:39:57"},{"stordocpk":"1001J1100000000019ES","name":"P1（4G）-B0451_前壳组件","code":"T14000018","marbasclass":"T14 壳体模组半成品","ename":"B0451_TOP_ASM","materialspec":"前壳组件/塑胶材质：PC/注塑成型/橙色/表面晒纹/VDI 21#","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019QM","name":"扫描底座塑料后壳","code":"T03000007","marbasclass":"T03 塑料件半成品","ename":"","materialspec":"ABS/Pantone Black 6C","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:43:51"},{"stordocpk":"1001J1100000000019QC","name":"T1液晶屏塑料面盖(V2)","code":"T03000002","marbasclass":"T03 塑料件半成品","ename":"","materialspec":"ABS757/PANTONE BLACK 6u","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019QS","name":"F15-主机塑料上盖 组件","code":"T03000010","marbasclass":"T03 塑料件半成品","ename":"","materialspec":"11836184-00","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:43:52"},{"stordocpk":"1001J1100000000019QU","name":"F15-顾显塑料转轴 组件","code":"T03000011","marbasclass":"T03 塑料件半成品","ename":"","materialspec":"F15-顾显塑料转轴 组件","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:43:52"},{"stordocpk":"1001J1100000000019QW","name":"F15-58打印机塑料翻盖组件","code":"T03000012","marbasclass":"T03 塑料件半成品","ename":"","materialspec":"12002130-00","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:43:53"},{"stordocpk":"1001J1100000000019RC","name":"M1-LCD-TFT4.5FWVGA+CTP_BLACK","code":"T02000002","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"TXDY450SFWPC-15","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:43:55"},{"stordocpk":"1001J1100000000019RE","name":"P1（4G）-全贴合屏（黑色）_帝晶_CMI","code":"T02000003","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"B900_LCM_ONCELL_BLACK_DJ_CMI","materialspec":"全贴合屏（黑色）_帝晶_CMI/TP厚度0.7mm/尺寸136.75*76.44/丝印黑色/5.5寸HD屏/ONCELL/尺寸：129.01*70.84*1.55mm/贴合厚度2.36mm/TP玻璃厂：旭硝子/TP IC:MSG5846/LCM玻璃厂：CMI/玻璃IC:ILI9881C/TP改6pin/防拆fpc移除","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019RK","name":"15.1寸液晶显示模块(HT150X02-100) BOE","code":"T02000006","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"BOE HT150X02-100","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019RM","name":"DMC 15.1寸5线电阻式触摸屏","code":"T02000007","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"DMC TP3478S2F0","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019RU","name":"TTI 15寸5线电阻式窄边触摸屏","code":"T02000011","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"TTI T010-7201-T080/5线电阻/322x245.5mm/线长205mm","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:43:58"},{"stordocpk":"1001J1100000000019RW","name":"8寸电容式触摸屏(FYP/FYP0800023-V02/我有外卖)","code":"T02000012","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"FYP0800023-V02/玻璃盖板/奕力芯片/FPC排线/我有外卖","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:43:59"},{"stordocpk":"1001J1100000000019S0","name":"9.7电容式触摸屏(FYP/FYP0970049-V00)","code":"T02000014","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"FYP0970049-V00/玻璃盖板/奕力芯片/FPC排线","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:43:59"},{"stordocpk":"1001J1100000000019S2","name":"9.7寸液晶模块(LG/LP097X02-SLQA)","code":"T02000015","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"LP097X02-SLQA/9.7/1024*768/LVDS","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019S4","name":"9.7寸液晶模块(SC/BI097XN02-V.Y)","code":"T02000016","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"BI097XN02-V.Y/9.7/1024*768/LVDS","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:00"},{"stordocpk":"1001J1100000000019S6","name":"8寸液晶模块(MD080IA01-30A)","code":"T02000017","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"MD080IA01-30A/LVDS接口/8寸/分辨率1024*768/IPS","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:00"},{"stordocpk":"1001J1100000000019SC","name":"9.7电容式触摸屏(BYD/FPC-TP11080A-VO-K)","code":"T02000020","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"FPC-TP11080A-VO-K/玻璃盖板/奕力芯片/FPC排线","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:01"},{"stordocpk":"1001J1100000000019SK","name":"14寸液晶模块(NV140FHM-H41)BOE","code":"T02000025","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"EDP/1920*1080/IPS","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:08"},{"stordocpk":"1001J1100000000019SM","name":"AbonTouch 15寸5线电阻式触摸屏(新)","code":"T02000026","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"AB-6515001031418122001/5线电阻/322x245.5mm/线长205mm","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:09"},{"stordocpk":"1001J1100000000019SQ","name":"H14 14寸电容式触摸屏(ZP)","code":"T02000028","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"ZP-1005-14-A/玻璃盖板/集创芯片/FPC排线/G+G","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:09"},{"stordocpk":"1001J1100000000019SU","name":"9.7寸液晶模块（IVO/H097IVO36002）","code":"T02000030","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"IPS/9.7寸/1024*768/LVDS","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:10"},{"stordocpk":"1001J1100000000019SW","name":"15.6寸非触摸贴合屏（BOE+TN","code":"T02000031","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"TN/1366*768/平波电子/PB156CG3947KT001/NT156WHM-N12","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:10"},{"stordocpk":"1001J1100000000019T0","name":"15.6寸非触摸贴合屏（CHI+IPS）","code":"T02000033","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"IPS/1920*1080/平波电子/PB156CG4039KT001/N156HCE-EAA","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019T8","name":"9.7寸液晶模块（贝纳 MD097IL03-30IT-36A）","code":"T02000037","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"IPS/9.7寸/1024*768/LVDS/MD097IL03-30IT-36A","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:12"},{"stordocpk":"1001J1100000000019TA","name":"9.7寸液晶模块（HRTEK097）","code":"T02000038","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"IPS/9.7寸/1024*768/LVDS/HRTEK097","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:13"},{"stordocpk":"1001J1100000000019TC","name":"9.7寸液晶模块（G097XL-S01  C1）","code":"T02000039","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"IPS/9.7寸/1024*768/LVDS/G097XL-S01  C1","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:13"},{"stordocpk":"1001J1100000000019TE","name":"15.6寸非触摸贴合屏（BOE+TN","code":"T02000040","marbasclass":"T02 液晶屏/触摸屏半成品","ename":"","materialspec":"TN/1366*768/COWIN/LTPG156BW363230006A/NT156WHM-N32","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019TQ","name":"ND010-PCBA","code":"T01000006","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"ND010/PCBA/SUB BOARD/POGO/V2.00","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019TS","name":"NR010-主板PCBA","code":"T01000007","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"NR010/USB HUB/TYPE C接口/配置/速报FPC/V1.1","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:16"},{"stordocpk":"1001J1100000000019TU","name":"NR010-子板PCBA","code":"T01000008","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"NR010/SUB PCBA","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:16"},{"stordocpk":"1001J1100000000019U4","name":"S2_MAIN_SCALE_PCB","code":"T01000013","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"L2500/弹片主板/单面贴片/42.5*27*1.6mm","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019U6","name":"S2_POWER_BUTTON_PCB","code":"T01000014","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"L2500/开关机按键/单面贴片/42.5*27*1.6mm","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019U2","name":"P1(S)主板PCBA","code":"T01000012","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"W6900(S)/主板PCBA/MT6737V/8GB/1GB/国内全网通/GPS/WIFI/BT/金融支付（IC/磁/NFC/Psam卡）/打印机","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:18"},{"stordocpk":"1001J1100000000019UC","name":"SLB741_IO_V1.00_PCB","code":"T01000017","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"T1s/S2/SLB741/IO","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:19"},{"stordocpk":"1001J1100000000019UK","name":"SWY_W5900_A01-主板PCBA 8+1","code":"T01000021","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"V1-主板PCBA 8+1-SWY_W5900_A01-","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:20"},{"stordocpk":"1001J1100000000019UM","name":"SWY-W5900-A01-主板PCBA","code":"T01000022","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"W5900_BOM1_LWDM590C-V1","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:21"},{"stordocpk":"1001J1100000000019UQ","name":"T1-PCB组件-14寸副板-WIFI","code":"T01000024","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"手机PCBA_BAM16002_14寸副板-WIFI-SVP_M00000","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:21"},{"stordocpk":"1001J1100000000019US","name":"T1-PCB组件-14寸主板-WIFI","code":"T01000025","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"手机PCBA_BAM16002_14寸主板-WIFI-SVP_M00000","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:22"},{"stordocpk":"1001J1100000000019UU","name":"T1-PCB组件-7寸副板-WIFI","code":"T01000026","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"手机PCBA_BAM16002_7寸副板-WIFI-SVP_M00000","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:22"},{"stordocpk":"1001J1100000000019UY","name":"T1-PCB组件-powerboard-无副屏","code":"T01000028","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"手机PCBA_BAM16002_powerboard-无副屏-SVP_M00000","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:23"},{"stordocpk":"1001J1100000000019VK","name":"F15 WIFI蓝牙部件(AC-3160)","code":"T01000039","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"AC 3160/MINI PCIE/M.2","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:26"},{"stordocpk":"1001J1100000000019VM","name":"PD-VFD220E顾显电路板","code":"T01000040","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"PD-VFD220E","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:26"},{"stordocpk":"1001J1100000000019VO","name":"PD-LED8N顾显电路板","code":"T01000041","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"PD-LED8N V3.2","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019VQ","name":"USB磁卡槽电路板(支持3.3V)","code":"T01000042","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"USB磁卡槽电路板(支持3.3V)","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:27"},{"stordocpk":"1001J1100000000019VW","name":"M-D36CLA2主板","code":"T01000045","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"M-D36CLA2/Intel ATOM D425 1.8GHz(512K二级缓存)+NM10/支持18位屏","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-08-04 19:25:34"},{"stordocpk":"1001J1100000000019W2","name":"Epoz2主机接口、电源转接板","code":"T01000048","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"Epoz2主机接口、电源转接板","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:29"},{"stordocpk":"1001J1100000000019W8","name":"Spoz广告灯箱背光板(24V)","code":"T01000051","marbasclass":"T01 电路板/主板半成品","ename":"","materialspec":"PMMA/大DIN 4PIN端子/24V/T=5.0mm/352.8*242mm","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:44:30"},{"stordocpk":"1001J110000000001978","name":"立顿英式果茶蜜桃味250ml","code":"F08000010","marbasclass":"F08 饮料","ename":"","materialspec":"250ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"立顿","enablestate":2,"materialbarcode":"6934024550712","ts":"2018-05-25 16:39:13"},{"stordocpk":"1001J11000000000196Q","name":"阿华田牛奶250ML/包","code":"F08000001","marbasclass":"F08 饮料","ename":"","materialspec":"250ML","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"阿华田","enablestate":2,"materialbarcode":"6957354260021","ts":"2018-05-25 16:39:09"},{"stordocpk":"1001J11000000000196S","name":"百事可乐600ml","code":"F08000002","marbasclass":"F08 饮料","ename":"","materialspec":"600ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"百事","enablestate":2,"materialbarcode":"6922858211091","ts":"2018-05-25 16:39:10"},{"stordocpk":"1001J11000000000197C","name":"立顿英式果茶柠檬味250ml","code":"F08000012","marbasclass":"F08 饮料","ename":"","materialspec":"250ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"百事","enablestate":2,"materialbarcode":"6934024550224","ts":"2018-05-25 16:39:14"},{"stordocpk":"1001J110000000001982","name":"统一PET小茗柠檬味480ml","code":"F08000025","marbasclass":"F08 饮料","ename":"","materialspec":"480ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"统一","enablestate":2,"materialbarcode":"6925303754952","ts":"2018-05-25 16:39:18"},{"stordocpk":"1001J110000000001984","name":"统一TP冰红茶250ml","code":"F08000026","marbasclass":"F08 饮料","ename":"","materialspec":"250ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"统一","enablestate":2,"materialbarcode":"6925303722562","ts":"2018-05-25 16:39:19"},{"stordocpk":"1001J11000000000198I","name":"维他柠檬茶罐装310ml","code":"F08000033","marbasclass":"F08 饮料","ename":"","materialspec":"310ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"维他","enablestate":2,"materialbarcode":"4891028706656","ts":"2018-05-25 16:39:22"},{"stordocpk":"1001J11000000000198K","name":"伊利畅意乳酸菌原味330ml","code":"F08000034","marbasclass":"F08 饮料","ename":"","materialspec":"330ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"伊利","enablestate":2,"materialbarcode":"6907992513188","ts":"2018-05-25 16:39:22"},{"stordocpk":"1001J11000000000198Q","name":"伊利早餐奶麦香味250ml","code":"F08000037","marbasclass":"F08 饮料","ename":"","materialspec":"250ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"伊利","enablestate":2,"materialbarcode":"6907992504476","ts":"2018-05-25 16:39:23"},{"stordocpk":"1001J11000000000198S","name":"诚有味烤脖劲辣味46g","code":"F03000001","marbasclass":"F03 休闲零食","ename":"","materialspec":"46g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"诚有味","enablestate":2,"materialbarcode":"6924520610638","ts":"2018-05-25 16:39:24"},{"stordocpk":"1001J110000000001992","name":"甘源牌袋装椒盐花生75g","code":"F03000006","marbasclass":"F03 休闲零食","ename":"","materialspec":"75g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"甘源","enablestate":2,"materialbarcode":"6940188805858","ts":"2018-05-25 16:39:26"},{"stordocpk":"1001J11000000000199A","name":"金锣辣脆脆肠42g","code":"F03000010","marbasclass":"F03 休闲零食","ename":"","materialspec":"42g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"金锣","enablestate":2,"materialbarcode":"6927462207621","ts":"2018-05-25 16:39:27"},{"stordocpk":"1001J11000000000199E","name":"蜡笔小新果肉杯桔子200g","code":"F03000012","marbasclass":"F03 休闲零食","ename":"","materialspec":"200g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"蜡笔小新","enablestate":2,"materialbarcode":"6921101207447","ts":"2018-05-25 16:39:28"},{"stordocpk":"1001J11000000000199I","name":"老肥彭卤蛋28g","code":"F03000014","marbasclass":"F03 休闲零食","ename":"","materialspec":"28g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"老肥彭","enablestate":2,"materialbarcode":"6943325201100","ts":"2018-05-25 16:39:29"},{"stordocpk":"1001J11000000000199O","name":"傻二哥兰花豆五香味68g","code":"F03000017","marbasclass":"F03 休闲零食","ename":"","materialspec":"68g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"傻二哥","enablestate":2,"materialbarcode":"6933311067643","ts":"2018-05-25 16:39:30"},{"stordocpk":"1001J11000000000199Y","name":"小老板烤海苔卷原味","code":"F03000022","marbasclass":"F03 休闲零食","ename":"","materialspec":"3.6g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"小老板","enablestate":2,"materialbarcode":"8858702410816","ts":"2018-05-25 16:39:32"},{"stordocpk":"1001J1100000000019AK","name":"不二家双棒巧克力","code":"F02000010","marbasclass":"F02 糖果","ename":"","materialspec":"24g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"不二家","enablestate":2,"materialbarcode":"49755343","ts":"2018-05-25 16:39:37"},{"stordocpk":"1001J1100000000019AM","name":"好丽友扭扭大王软糖果汁味","code":"F02000011","marbasclass":"F02 糖果","ename":"","materialspec":"40g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"好丽友","enablestate":2,"materialbarcode":"6920907804911","ts":"2018-05-25 16:39:37"},{"stordocpk":"1001J1100000000019AU","name":"曼妥思果汁24支糖","code":"F02000015","marbasclass":"F02 糖果","ename":"","materialspec":"37g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"曼妥思","enablestate":2,"materialbarcode":"6921211101154","ts":"2018-05-25 16:39:39"},{"stordocpk":"1001J1100000000019B0","name":"雀巢脆脆鲨80g","code":"F02000018","marbasclass":"F02 糖果","ename":"","materialspec":"80g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"雀巢","enablestate":2,"materialbarcode":"6917878048222","ts":"2018-05-25 16:39:40"},{"stordocpk":"1001J1100000000019B2","name":"旺仔QQ糖草莓味23g","code":"F02000019","marbasclass":"F02 糖果","ename":"","materialspec":"23g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"旺仔","enablestate":2,"materialbarcode":"6920548867115","ts":"2018-05-25 16:39:40"},{"stordocpk":"1001J1100000000019BA","name":"小样酸Q糖蓝莓味","code":"F02000023","marbasclass":"F02 糖果","ename":"","materialspec":"28g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"小样","enablestate":2,"materialbarcode":"6944437022362","ts":"2018-05-25 16:39:41"},{"stordocpk":"1001J1100000000019BG","name":"益达木糖醇40粒装冰凉薄荷味","code":"F02000026","marbasclass":"F02 糖果","ename":"","materialspec":"56g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"益达","enablestate":2,"materialbarcode":"6923450656181","ts":"2018-05-25 16:39:43"},{"stordocpk":"1001J1100000000019BI","name":"益达木糖醇40粒装清爽草莓味","code":"F02000027","marbasclass":"F02 糖果","ename":"","materialspec":"56g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"益达","enablestate":2,"materialbarcode":"6923450656150","ts":"2018-05-25 16:39:43"},{"stordocpk":"1001J1100000000019BM","name":"德芙士力架花生夹心巧克力70g","code":"F10000001","marbasclass":"F10 巧克力","ename":"","materialspec":"70g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"德芙","enablestate":2,"materialbarcode":"6914973601017","ts":"2018-05-25 16:39:44"},{"stordocpk":"1001J1100000000019BW","name":"乐事自然清爽黄瓜味70g","code":"F04000003","marbasclass":"F04 膨化食品","ename":"","materialspec":"70g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"乐事","enablestate":2,"materialbarcode":"6924743919259","ts":"2018-05-25 16:39:46"},{"stordocpk":"1001J1100000000019C0","name":"亲亲虾条香辣味46g","code":"F04000005","marbasclass":"F04 膨化食品","ename":"","materialspec":"46g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"亲亲","enablestate":2,"materialbarcode":"6923775923982","ts":"2018-05-25 16:39:46"},{"stordocpk":"1001J1100000000019C4","name":"上好佳鲜虾薯片45g","code":"F04000007","marbasclass":"F04 膨化食品","ename":"","materialspec":"45g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"上好佳","enablestate":2,"materialbarcode":"6909409012024","ts":"2018-05-25 16:39:47"},{"stordocpk":"1001J1100000000019C8","name":"四洲虾条原味40g","code":"F04000009","marbasclass":"F04 膨化食品","ename":"","materialspec":"40g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"四洲","enablestate":2,"materialbarcode":"6932430202119","ts":"2018-05-25 16:39:48"},{"stordocpk":"1001J1100000000019CE","name":"维他奶原味豆奶250ml","code":"F09000003","marbasclass":"F09 奶制品","ename":"","materialspec":"250ml","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"维他","enablestate":2,"materialbarcode":"4891028164395","ts":"2018-05-25 16:39:49"},{"stordocpk":"1001J1100000000019CG","name":"喜之郎优乐美奶茶草莓味80g","code":"F06000001","marbasclass":"F06 固体冲饮","ename":"","materialspec":"80g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"喜之郎","enablestate":2,"materialbarcode":"6926475203187","ts":"2018-05-25 16:39:50"},{"stordocpk":"1001J1100000000019CS","name":"合味道海鲜面84g","code":"F07000003","marbasclass":"F07 方便面","ename":"","materialspec":"84g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"日清","enablestate":2,"materialbarcode":"6917935002150","ts":"2018-05-25 16:39:52"},{"stordocpk":"1001J1100000000019D6","name":"达利园法式小面包","code":"F05000002","marbasclass":"F05 饼干蛋糕","ename":"","materialspec":"400g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"达利园","enablestate":2,"materialbarcode":"6911988011633","ts":"2018-05-25 16:39:54"},{"stordocpk":"1001J1100000000019DC","name":"达利园注心派蛋黄味","code":"F05000005","marbasclass":"F05 饼干蛋糕","ename":"","materialspec":"500g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"达利园","enablestate":2,"materialbarcode":"6911988005373","ts":"2018-05-25 16:39:56"},{"stordocpk":"1001J1100000000019DG","name":"好丽友蛋黄派","code":"F05000007","marbasclass":"F05 饼干蛋糕","ename":"","materialspec":"46g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"好丽友","enablestate":2,"materialbarcode":"6920907800616","ts":"2018-05-25 16:39:56"},{"stordocpk":"1001J1100000000019DM","name":"嘉顿梳打饼干芝麻味","code":"F05000010","marbasclass":"F05 饼干蛋糕","ename":"","materialspec":"105g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"嘉顿","enablestate":2,"materialbarcode":"6902227010456","ts":"2018-05-25 16:39:58"},{"stordocpk":"1001J1100000000019DO","name":"嘉士利粒粒威化饼干黑芝麻味","code":"F05000011","marbasclass":"F05 饼干蛋糕","ename":"","materialspec":"60g","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"嘉士利","enablestate":2,"materialbarcode":"6901180914917","ts":"2018-05-25 16:39:58"},{"stordocpk":"1001J1100000000019DY","name":"D1-15\"平板后壳组件(黑色_WIFI)","code":"T14000001","marbasclass":"T14 壳体模组半成品","ename":"","materialspec":"LCD面盖组件_BAM17001_黑色_注塑+组装_WIFI_M00000","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:40:29"},{"stordocpk":"1001J1100000000019E2","name":"D1-15\"平板转轴组件","code":"T14000005","marbasclass":"T14 壳体模组半成品","ename":"","materialspec":"转轴组件_SMS-ZZ-177_组装","materialtype":"","measdoccode":"","orgcode":"0001J1100000000008N7","brandname":"","enablestate":2,"materialbarcode":"","ts":"2018-05-25 16:42:39"}]
     * errno : 0
     * pagenum : 2
     * pagetotal : 34
     */

    private int errno;
    private int pagenum;
    private int pagetotal;
    private List<DataBean> data;

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
    }

    public void setPagetotal(int pagetotal) {
        this.pagetotal = pagetotal;
    }

    public void setData(List<DataBean> data) {
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

    public List<DataBean> getData() {
        return data;
    }

    public static class DataBean {
        /**
         * stordocpk : 1001J11000000000197A
         * name : 立顿英式果茶蜜桃味500ML
         * code : F08000011
         * marbasclass : F08 饮料
         * ename :
         * materialspec : 500ml
         * materialtype :
         * measdoccode :
         * orgcode : 0001J1100000000008N7
         * brandname : 立顿
         * enablestate : 2
         * materialbarcode : 6934024550705
         * ts : 2018-05-25 16:39:13
         */

        public String stordocpk;
        public String name;
        public String code;
        public String marbasclass;
        public String ename;
        public String materialspec;
        public String materialtype;
        public String measdoccode;
        public String orgcode;
        public String brandname;
        public int enablestate;
        public String materialbarcode;
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

        public void setMarbasclass(String marbasclass) {
            this.marbasclass = marbasclass;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public void setMaterialspec(String materialspec) {
            this.materialspec = materialspec;
        }

        public void setMaterialtype(String materialtype) {
            this.materialtype = materialtype;
        }

        public void setMeasdoccode(String measdoccode) {
            this.measdoccode = measdoccode;
        }

        public void setOrgcode(String orgcode) {
            this.orgcode = orgcode;
        }

        public void setBrandname(String brandname) {
            this.brandname = brandname;
        }

        public void setEnablestate(int enablestate) {
            this.enablestate = enablestate;
        }

        public void setMaterialbarcode(String materialbarcode) {
            this.materialbarcode = materialbarcode;
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

        public String getMarbasclass() {
            return marbasclass;
        }

        public String getEname() {
            return ename;
        }

        public String getMaterialspec() {
            return materialspec;
        }

        public String getMaterialtype() {
            return materialtype;
        }

        public String getMeasdoccode() {
            return measdoccode;
        }

        public String getOrgcode() {
            return orgcode;
        }

        public String getBrandname() {
            return brandname;
        }

        public int getEnablestate() {
            return enablestate;
        }

        public String getMaterialbarcode() {
            return materialbarcode;
        }

        public String getTs() {
            return ts;
        }
    }
}
