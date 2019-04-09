package com.example.shanggmiqr.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.shanggmiqr.Url.iUrl;
import com.example.shanggmiqr.bean.CommonSendBean;
import com.example.shanggmiqr.bean.OtherQueryBean;
import com.example.shanggmiqr.bean.QrcodeRule;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import static com.example.shanggmiqr.util.Utils.getDefaultEndTime;

public class DataHelper {
    //0物料编码  1序列号
    public  static   QrcodeRule.DataBean.ItemBean getCurrentQrcodeRule(SQLiteDatabase db, String matbasclasscode, String type ) {
        Cursor cursor = db.rawQuery("select * from QrcodeRuleBody where Matbasclasscode=?",
                new String[]{matbasclasscode});


        QrcodeRule.DataBean.ItemBean bean=new QrcodeRule.DataBean.ItemBean();

        //判断cursor中是否存在数据
        while (cursor.moveToNext()) {
            bean = new QrcodeRule.DataBean.ItemBean();
            bean.itemlength = cursor.getInt(cursor.getColumnIndex("itemlength"));
            bean.startpos = cursor.getInt(cursor.getColumnIndex("startpos"));
            bean.appobjattr = cursor.getString(cursor.getColumnIndex("appobjattr"));
            if (type.equals(bean.appobjattr)) {
                return bean;
            } else if (type.equals(bean.appobjattr)) {
                return bean;
            }

        }
        cursor.close();
        return bean;
    }
    public static   String getMaccode(SQLiteDatabase db,String productcode,String matbasclasscode){
        String maccode="";
        QrcodeRule.DataBean.ItemBean bean=getCurrentQrcodeRule(db,matbasclasscode,"物料条码");
        maccode= productcode.substring(bean.startpos - 1,
                bean.startpos - 1 + bean.itemlength);
        return maccode;
    }
    public static   String getXlh(SQLiteDatabase db,String productcode,String matbasclasscode){
        String xlh="";
        QrcodeRule.DataBean.ItemBean bean=getCurrentQrcodeRule(db,matbasclasscode,"序列号");
        xlh= productcode.substring(bean.startpos - 1,
                bean.startpos - 1 + bean.itemlength);
        return xlh;
    }
    public static boolean isAlreadyScanned(SQLiteDatabase db,String pobillcode,String prodcutcode,String vcooporderbcode_b) {
        Cursor cursor = db.rawQuery("select * from OtherOutgoingScanResult where pobillcode=? and prodcutcode=? and vcooporderbcode_b=?",
                new String[]{pobillcode, prodcutcode, vcooporderbcode_b});
        while (cursor != null && cursor.getCount() > 0) {
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    public static int queryScanResultcount(SQLiteDatabase db, String code, String materialcode, String vcooporderbcode_b, int type) {
        Cursor cursor=null;
        int count;
        switch (type){
            case 0:
                cursor = db.rawQuery("select count(prodcutcode) from OtherEntryScanResult  where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{code, materialcode, vcooporderbcode_b});
                break;
            case 1:
                cursor = db.rawQuery("select  count(prodcutcode) from OtherOutgoingScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{code,materialcode, vcooporderbcode_b});
                break;
            case 2:
                cursor = db.rawQuery("select count(prodcutcode) from SaleDeliveryScanResult where vbillcode=? and matrcode=? and vcooporderbcode_b=? ",
                        new String[]{code, materialcode,vcooporderbcode_b});
                break;
        }


        cursor.moveToFirst();
        count = cursor.getInt(0);
        cursor.close();

        return count;
    }
    public static void updateSaleDeliveryBodyscannum(SQLiteDatabase db, String scannum, String vbillcode, String vcooporderbcode_b) {

        ContentValues contentValues=new ContentValues();
        contentValues.put("scannum",scannum);

        db.update("SaleDeliveryBody",contentValues,"vbillcode=? and vcooporderbcode_b=?",
                new String[]{ vbillcode,vcooporderbcode_b});
    }
    public static List<String> queryWarehouseInfo(SQLiteDatabase db) {
        List<String> cars = new ArrayList<>();
        Cursor cursornew = db.rawQuery("select name from Warehouse",
                null);
        if (cursornew != null && cursornew.getCount() > 0) {
            while (cursornew.moveToNext()) {
                String name = cursornew.getString(cursornew.getColumnIndex("name"));
                cars.add(name);
            }
            cursornew.close();
        }
        return cars;
    }

    //type 1:OtherEntry
    public static void insertOtherDataToDB(SQLiteDatabase db,OtherQueryBean otherEntryBean,int type) {
        List<OtherQueryBean.DataBean> otherEntryBeanList = otherEntryBean.getData();
        String otherTable="";
        String otherbodyTable="";
        switch (type){
            case 0:
                otherTable="OtherEntry";
                otherbodyTable="OtherEntryBody";
                break;
            case 1:
                otherTable="OtherOutgoing";
                otherbodyTable="OtherOutgoingBody";
                break;
        }
        for (OtherQueryBean.DataBean ob : otherEntryBeanList) {
           boolean isPobillcodeExist=isPobillcodeExist(db,ob.getPobillcode());
            if("0".equals(ob.getDr())&& isPobillcodeExist){
                continue;
            }
            //等于1时
            if("1".equals((ob.getDr()))|| ("2".equals(ob.getDr())&&isPobillcodeExist))
            {

                db.beginTransaction();
                try {
                    db.delete(otherTable, "pobillcode=?", new String[]{ob.getPobillcode()});
                    db.delete(otherbodyTable, "pobillcode=?", new String[]{ob.getPobillcode()});
                    //  db3.delete("OtherEntryScanResult", "pobillcode=?", new String[]{pobillcode});
                   db.setTransactionSuccessful();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();

                }
                finally {
                    db.endTransaction();
                }
            }
            if("1".equals(ob.getDr())){
                continue;
            }
            List<OtherQueryBean.DataBean.BodyBean> outGoingDatabodyList = ob.getBody();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            for (OtherQueryBean.DataBean.BodyBean obb : outGoingDatabodyList) {
                String materialcode = obb.getMaterialcode();
                String maccode = obb.getMaccode();
                int nnum = obb.getNnum();
                String pch = obb.getPch();
                String vcooporderbcode_b = obb.getVcooporderbcode_b();
                int scannum = queryScanResultcount(db,ob.getPobillcode(), materialcode, vcooporderbcode_b,0);
                //这里应该执行的是插入第二个表的操作
                ContentValues valuesInner = new ContentValues();
                valuesInner.put("pobillcode", ob.getPobillcode());
                valuesInner.put("materialcode", materialcode);
                valuesInner.put("maccode", maccode);
                valuesInner.put("nnum", nnum);
                valuesInner.put("pch", pch);
                valuesInner.put("uploadnum", "0");
                valuesInner.put("scannum", scannum);
                valuesInner.put("uploadflag", "N");
                valuesInner.put("vcooporderbcode_b", vcooporderbcode_b);

                db.insert(otherbodyTable, null, valuesInner);
                valuesInner.clear();
            }
            values.put("pobillcode", ob.getPobillcode());
            values.put("cwarecode",ob.getCwarecode());
            values.put("cwarename", ob.getCwarename());
            values.put("dbilldate", ob.getDbilldate());
            values.put("dr", ob.getDr());
            values.put("flag", "N");
            // 插入第一条数据
            db.insert(otherTable, null, values);
            values.clear();

        }
    }
    public static boolean isPobillcodeExist(SQLiteDatabase db,String pobillcode) {
        Cursor cursor2 = db.rawQuery("select pobillcode from OtherEntry where pobillcode=?", new String[]{pobillcode});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            cursor2.close();
            return true;
        }else {
            return false;
        }
    }

    public static void showDialog(ZLoadingDialog dialog) {
        dialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                .setLoadingColor(Color.BLUE)//颜色
                .setHintText("Loading...")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                //     .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                .show();
    }

    public  static String downloadDatabase(String workCode, String pagenum, Context context,int type) throws Exception {
        String WSDL_URI;
        String namespace;
        String WSDL_URI_current = BaseConfig.getNcUrl();//wsdl 的uri
        String namespace_current = "http://schemas.xmlsoap.org/soap/envelope/";//namespace
        String methodName = "sendToWISE";//要调用的方法名称
        SharedPreferences proxySp = context.getSharedPreferences("configInfo", 0);
        if (proxySp.getString("WSDL_URI", WSDL_URI_current).equals("") || proxySp.getString("namespace", namespace_current).equals("")) {
            WSDL_URI = WSDL_URI_current;
            namespace = namespace_current;
        } else {
            WSDL_URI = proxySp.getString("WSDL_URI", WSDL_URI_current);
            namespace = proxySp.getString("namespace", namespace_current);
        }
        SoapObject request = new SoapObject(namespace, methodName);
        // 设置需调用WebService接口需要传入的两个参数string、string1
        String name="";
        switch (type){
            case 0:
                name="LatestOtherEntryTSInfo";
                break;
            case 1:
                name="LatestOtherOutgoingTSInfo";
                break;
            case 2:
                name="LatestSaleDeliveryTSInfo";
                break;
        }
        SharedPreferences latestDBTimeInfo = context.getSharedPreferences(name, 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_systime", iUrl.begintime);
        String endtime = getDefaultEndTime();

        CommonSendBean userSend = new CommonSendBean(begintime, endtime, pagenum, pagenum);
        Gson gson = new Gson();
        String userSendBean = gson.toJson(userSend);
        request.addProperty("string", workCode);
        request.addProperty("string1", userSendBean);
        Log.i("request",userSendBean);
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.bodyOut = request;
        envelope.dotNet = false;

        HttpTransportSE se = new HttpTransportSE(WSDL_URI);
        //version1.1 需要如下soapaction
        se.call(namespace + "sendToWISE", envelope);
        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
       String otherOutgoingDataResp = object.getProperty(0).toString();
       Log.i("response-->",otherOutgoingDataResp);
        return otherOutgoingDataResp;
    }
    public static boolean isWarehouseDBDownloaed(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select name from Warehouse",
                null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }
    public static boolean queryTimePeriod(String code ,String startTime,String endTime,int type,SQLiteDatabase db) {
        Cursor cursor=null;
        switch (type){
            case 0:
                cursor = db.rawQuery("SELECT count(pobillcode) FROM OtherEntry WHERE pobillcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
            case 1:
                cursor = db.rawQuery("SELECT count(pobillcode) FROM OtherOutgoing WHERE pobillcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
            case 2:
                cursor = db.rawQuery("SELECT * FROM SaleDelivery WHERE vbillcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
        }
        while (cursor.moveToNext()){
            if(cursor.getInt(0)!=0){

                return true;
            }else {

                return  false;
            }

        }
        cursor.close();
        return  false;

    }
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }

        return false;
    }

    public static String getQueryTime(Context context,int type) {
        String name="";
        switch (type){
            case 0:
                name="query_otherentry";
                break;
            case 1:
                name="query_otheroutgoing";
                break;
            case 2:
                name="query_saledelivery";
                break;

        }
        SharedPreferences currentTimePeriod= context.getSharedPreferences(name, 0);
        String tempperiod =currentTimePeriod.getString("current_account","2018-09-01 至 2018-12-17");
        return tempperiod;
    }


}
