package com.example.shanggmiqr.Url;

import android.content.Context;
import android.content.SharedPreferences;

public class iUrl {
    public  static String WSDL_URI="http://erptest.sunmi.com:8001/uapws/service/INetWebserviceServer";
    public  static String namespace="http://schemas.xmlsoap.org/soap/envelope/";
    public  static String begintime="2019-06-01 00:00:01";
    public  static  String getBegintime(Context context){
       SharedPreferences updateConfig = context.getSharedPreferences("configInfo", 0);
       return  updateConfig.getString("begintime",iUrl.begintime);
    }

}
