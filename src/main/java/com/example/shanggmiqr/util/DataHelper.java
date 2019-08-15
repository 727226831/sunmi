package com.example.shanggmiqr.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.shanggmiqr.Url.iUrl;
import com.example.shanggmiqr.bean.CommonSendBean;
import com.example.shanggmiqr.bean.LoginBean;
import com.example.shanggmiqr.bean.OtherQueryBean;
import com.example.shanggmiqr.bean.QrcodeRule;
import com.example.shanggmiqr.bean.SaleDeliveryBean;
import com.example.shanggmiqr.bean.SaleDeliverySendBean;
import com.example.shanggmiqr.transaction.SaleDelivery;
import com.example.shanggmiqr.transaction.SaleDeliveryDetail;
import com.example.shanggmiqr.transaction.SaleDeliveryQrScanner;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
            }

        }
        cursor.close();
        return bean;
    }
    public static   String getMaccode(SQLiteDatabase db,String productcode,String matbasclasscode){
        String maccode="";
        QrcodeRule.DataBean.ItemBean bean=getCurrentQrcodeRule(db,matbasclasscode,"物料条码");

        if(bean.startpos==0 || bean.itemlength==0){
            return  null;
        }
        int beginIndex=bean.startpos - 1;
        int endIndex=bean.startpos - 1 + bean.itemlength;
        maccode= productcode.substring(beginIndex,endIndex);
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
    public static String getCwarehousecode(String cwarename,SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select code from Warehouse where name=?",
                new String[]{cwarename});
        String cwarehousecode = null;
            while (cursor.moveToNext()) {
                cwarehousecode = cursor.getString(cursor.getColumnIndex("code"));
            }
            cursor.close();

        return cwarehousecode;
    }
    public static String getCwarename(String cwarehousecode,SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("select name from Warehouse where code=?",
                new String[]{cwarehousecode});
        String cwarename=null;
            while (cursor.moveToNext()) {
                cwarename = cursor.getString(cursor.getColumnIndex("name"));
            }
            cursor.close();

        return cwarename;
    }

    public static int queryScanResultcount(SQLiteDatabase db, String code, String materialcode, String vcooporderbcode_b, int type) {
        Cursor cursor=null;
        int count=0;
        switch (type){
            case 0:
                cursor = db.rawQuery("select count(prodcutcode) from SaleDeliveryScanResult where vbillcode=? and matrcode=? and vcooporderbcode_b=? ",
                        new String[]{code, materialcode,vcooporderbcode_b});
                break;
            case 1:
                cursor = db.rawQuery("select count(prodcutcode) from OtherEntryScanResult  where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{code, materialcode, vcooporderbcode_b});
                break;
            case 2:
                cursor = db.rawQuery("select  count(prodcutcode) from OtherOutgoingScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{code,materialcode, vcooporderbcode_b});
                break;

        }
        while (cursor.moveToNext()){
            count = cursor.getInt(0);
        }

        cursor.close();

        return count;
    }
    public static  int getLengthInQrRule(String matbasclasscode,SQLiteDatabase db) {
        int length=-1;
        Cursor cursor = db.rawQuery("select length from QrcodeRule where Matbasclasscode=?",
                new String[]{matbasclasscode});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                length = cursor.getInt(cursor.getColumnIndex("length"));
            }
            cursor.close();
        }
        return length;
    }




    public static boolean isValidQr(String productcode,String matbasclasscode,String matrcode,SQLiteDatabase db,Context context) {

        String scannedMaccode = DataHelper.getMaccode(db,productcode,matbasclasscode);
        if (scannedMaccode == null || scannedMaccode.length() == 0) {
            Toast.makeText(context, "请检查物料信息及条码规则数据是否下载，或者是否为有效的条码", Toast.LENGTH_SHORT).show();
            return false;

        }
        Cursor cursor = db.rawQuery("select code from Material where materialbarcode=?",
                new String[]{scannedMaccode});

        //判断cursor中是否存在数据
        while (cursor.moveToNext()) {
            String code = cursor.getString(cursor.getColumnIndex("code"));

            if (code.equals(matrcode)) {
                cursor.close();
                return true;
            }
        }

        cursor.close();
        return false;
    }
    public static void updateScannum(SQLiteDatabase db, int scannum, String vbillcode, String itempk, int type) {
        ContentValues contentValues=new ContentValues();
        contentValues.put("scannum",scannum);


        switch (type){
            case 0:
                db.update("SaleDeliveryBody",contentValues,"vbillcode=? and vcooporderbcode_b=?",
                        new String[]{ vbillcode,itempk});

                break;
            case 1:
                db.update("OtherEntryBody",contentValues,"pobillcode=? and vcooporderbcode_b=?",
                        new String[]{ vbillcode,itempk});
                break;
            case 2:
                db.update("OtherOutgoingBody",contentValues,"pobillcode=? and vcooporderbcode_b=?",
                        new String[]{ vbillcode,itempk});
                break;
            case 4:
                db.update("LoanBody",contentValues,"pobillcode=? and itempk=?",
                        new String[]{ vbillcode,itempk});
                break;
            case 6:
                db.update("PurchaseArrivalBody",contentValues,"vbillcode=? and itempk=?",
                        new String[]{ vbillcode,itempk});
                break;
            case 7:
                db.update("PurchaseReturnBody",contentValues,"vbillcode=? and itempk=?",
                        new String[]{ vbillcode,itempk});
                break;

        }

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


    public static void insertOtherDataToDB(SQLiteDatabase db,OtherQueryBean otherEntryBean,int type) {

        List<OtherQueryBean.DataBean> otherEntryBeanList = otherEntryBean.getData();
        String otherTable="";
        String otherbodyTable="";
        switch (type){
            case 1:
                otherTable="OtherEntry";
                otherbodyTable="OtherEntryBody";
                break;
            case 2:
                otherTable="OtherOutgoing";
                otherbodyTable="OtherOutgoingBody";
                break;
        }

        for (OtherQueryBean.DataBean ob : otherEntryBeanList) {

            switch (ob.getDr()){
                case 0:
                    insertData(ob,db,otherbodyTable,otherTable);
                    break;
                case 1:
                    db.delete(otherTable, "pobillcode=?", new String[]{ob.getPobillcode()});
                    db.delete(otherbodyTable, "pobillcode=?", new String[]{ob.getPobillcode()});
                    db.delete(otherbodyTable, "pobillcode=?", new String[]{ob.getPobillcode()});

                    break;
                case 2:
                    db.delete(otherTable, "pobillcode=?", new String[]{ob.getPobillcode()});
                    db.delete(otherbodyTable, "pobillcode=?", new String[]{ob.getPobillcode()});
                     db.delete(otherbodyTable, "pobillcode=?", new String[]{ob.getPobillcode()});
                    insertData(ob,db,otherbodyTable,otherTable);
                    break;
            }



        }
    }

    private static void insertData(OtherQueryBean.DataBean ob,SQLiteDatabase db,String otherbodyTable,String  otherTable) {
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
            valuesInner.put("issn",obb.getIssn());
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

    public  static String downloadDatabase(String pagenum, Context context,int type) throws Exception {
        String WSDL_URI;
        String namespace;
        String WSDL_URI_current = BaseConfig.getNcUrl();//wsdl 的uri
        String namespace_current = "http://schemas.xmlsoap.org/soap/envelope/";//namespace
        String methodName = "sendToWISE";//要调用的方法名称
        String workCode=null;
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
                name="LatestSaleDeliveryTSInfo";
                workCode="R07";
                break;
            case 1:
                name="LatestOtherEntryTSInfo";
                workCode="R09";
                break;
            case 2:
                name="LatestOtherOutgoingTSInfo";
                workCode="R11";
                break;
            case 3:
                name="LatestProductEntryTSInfo";
                workCode="R35";
                break;
            case 4:
                name="LatestLoanTSInfo";
                workCode="R37";
                break;
            case 5:
                 name="LatestAllocateTransferTSInfo";
                workCode="R42";
                break;
            case 6:
                name="LatestPurchaseArrivalTSInfo";
                workCode="R40";
                break;
            case 7:
                name="LatestPurchaseReturnTSInfo";
                workCode="R39";
                break;

        }
        SharedPreferences latestDBTimeInfo = context.getSharedPreferences(name, 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", iUrl.begintime);
        String endtime = getDefaultEndTime();

        CommonSendBean userSend = new CommonSendBean(begintime, endtime, pagenum, "0");
        userSend.setAppuser(getUser(context));
        Gson gson = new Gson();
        String userSendBean = gson.toJson(userSend);
        request.addProperty("string", workCode);
        request.addProperty("string1", userSendBean);
        Log.i("request-->",request.toString());
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.bodyOut = request;
        envelope.dotNet = false;

        HttpTransportSE se = new HttpTransportSE(WSDL_URI);
        //version1.1 需要如下soapaction
        se.call(namespace + "sendToWISE", envelope);
        // 获取返回的数据
        Log.i("response-->",envelope.bodyIn.toString());
        SoapObject object = (SoapObject) envelope.bodyIn;

        // 获取返回的结果
       String otherOutgoingDataResp = object.getProperty(0).toString();
        return otherOutgoingDataResp;
    }

    public static String getUser(Context context) {
        SharedPreferences currentAccount= context.getSharedPreferences("current_account", 0);
        return  currentAccount.getString("user","");
    }

    public  static  void  setLatestdownloadtime(String begin,int type,Context context){
        String name=null;
        switch (type){
            case 0:
                name="LatestSaleDeliveryTSInfo";
                break;
            case 1:
                name="LatestOtherEntryTSInfo";
                break;
            case 2:
                name="LatestOtherOutgoingTSInfo";
                break;
            case 3:
                name="LatestProductEntryTSInfo";
                break;
            case 4:
                name="LatestLoanTSInfo";
                break;
            case 5:
                name="LatestAllocateTransferTSInfo";
                break;
            case 6:
                name="LatestPurchaseArrivalTSInfo";
                break;
            case 7:
                name="LatestPurchaseReturnTSInfo";
                break;

        }
        SharedPreferences latestDBTimeInfo5 = context.getSharedPreferences(name, 0);
        SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
        editor5.putString("latest_download_ts_begintime", begin);
        editor5.commit();
    }
    public  static  String getLatestdownloadbegintime(int type,Context context){
        String name=null;
        switch (type){
            case 0:
                name="LatestSaleDeliveryTSInfo";
                break;
            case 1:
                name="LatestOtherEntryTSInfo";
                break;
            case 2:
                name="LatestOtherOutgoingTSInfo";
                break;
            case 3:
                name="LatestProductEntryTSInfo";
                break;
            case 4:
                name="LatestLoanTSInfo";
                break;
            case 5:
                name="LatestAllocateTransferTSInfo";
                break;
            case 6:
                name="LatestPurchaseArrivalTSInfo";
                break;
            case 7:
                name="LatestPurchaseReturnTSInfo";
                break;

        }
        SharedPreferences latestDBTimeInfo = context.getSharedPreferences(name, 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", iUrl.begintime);
        return  begintime;
    }
    public  static  void putLatestdownloadbegintime(int type,Context context){
        String name=null;
        switch (type){
            case 0:
                name="LatestSaleDeliveryTSInfo";
                break;
            case 1:
                name="LatestOtherEntryTSInfo";
                break;
            case 2:
                name="LatestOtherOutgoingTSInfo";
                break;
            case 3:
                name="LatestProductEntryTSInfo";
                break;
            case 4:
                name="LatestLoanTSInfo";
                break;
            case 5:
                name="LatestAllocateTransferTSInfo";
                break;
            case 6:
                name="LatestPurchaseArrivalTSInfo";
                break;
            case 7:
                name="LatestPurchaseReturnTSInfo";
                break;

        }
        SharedPreferences latestDBTimeInfo = context.getSharedPreferences(name, 0);
        latestDBTimeInfo.edit().putString("latest_download_ts_begintime",Utils.getCurrentDateTimeNew()).commit();


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
                cursor = db.rawQuery("SELECT count(vbillcode) FROM SaleDelivery WHERE vbillcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
            case 1:
                cursor = db.rawQuery("SELECT count(pobillcode) FROM OtherEntry WHERE pobillcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
            case 2:
                cursor = db.rawQuery("SELECT count(pobillcode) FROM OtherOutgoing WHERE pobillcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;

        }
        while (cursor.moveToNext()){
            if(cursor.getInt(0)!=0){
                cursor.close();
                return true;
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
                name="query_saledelivery";
                break;
            case 1:
                name="query_otherentry";
                break;
            case 2:
                name="query_otheroutgoing";
                break;
        }
        SharedPreferences currentTimePeriod= context.getSharedPreferences(name, 0);
        String tempperiod =currentTimePeriod.getString("current_account","2018-09-01 至 2018-12-17");
        return tempperiod;
    }

    public static String uploadSaleDeliveryVBill(String workcode, SQLiteDatabase db,String vbillcode,String itempk,Context context,String company,
    String expresscode,int type) {
        String WSDL_URI = BaseConfig.getNcUrl();//wsdl 的uri
        String namespace = "http://schemas.xmlsoap.org/soap/envelope/";//namespace
        String methodName = "sendToWISE";//要调用的方法名称
        String warehousecode=null;
        String current_bisreturn="N";

        SoapObject request = new SoapObject(namespace, methodName);

        String shunm="0";
        // 设置需调用WebService接口需要传入的两个参数string、string1

        ArrayList<SaleDeliverySendBean.BodyBean> bodylist = new ArrayList<SaleDeliverySendBean.BodyBean>();
        Cursor cursor = null;
        switch (type){
            case 0:
                cursor=db.rawQuery("select * from SaleDeliveryBody where vbillcode=?  ",
                        new String[]{vbillcode});
                break;
            case 6:
                cursor=db.rawQuery("select * from PurchaseArrivalBody where vbillcode=?   ",
                        new String[]{vbillcode});
                break;
            case 7:
                cursor=db.rawQuery("select * from PurchaseReturnBody where vbillcode=? ",
                        new String[]{vbillcode});
                break;
        }



            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                SaleDeliverySendBean.BodyBean bean = new SaleDeliverySendBean.BodyBean();

                bean.nnum=cursor.getString(cursor.getColumnIndex("nnum"));

                switch (type){
                    case 0:
                        bean.itempk = cursor.getString(cursor.getColumnIndex("vcooporderbcode_b"));
                        bean.materialcode = cursor.getString(cursor.getColumnIndex("matrcode"));
                        warehousecode = getCwarehousecode(cursor.getString(cursor.getColumnIndex("cwarename")),db);
                        bean.setUploadflag(cursor.getString(cursor.getColumnIndex("uploadflag")));

                        break;
                    case 6:
                        bean.itempk = cursor.getString(cursor.getColumnIndex("itempk"));
                        bean.materialcode = cursor.getString(cursor.getColumnIndex("materialcode"));
                        warehousecode = cursor.getString(cursor.getColumnIndex("warehouse"));
                        bean.setWarehouse(cursor.getString(cursor.getColumnIndex("warehouse")));
                        bean.setUploadflag(cursor.getString(cursor.getColumnIndex("uploadflag")));

                        break;
                    case 7:
                        bean.itempk = cursor.getString(cursor.getColumnIndex("itempk"));
                        bean.materialcode = cursor.getString(cursor.getColumnIndex("materialcode"));
                        bean.setWarehouse(cursor.getString(cursor.getColumnIndex("warehouse")));
                        warehousecode = cursor.getString(cursor.getColumnIndex("warehouse"));
                        bean.setUploadflag(cursor.getString(cursor.getColumnIndex("uploadflag")));

                        break;
                }

                bean.setScannum(cursor.getString(cursor.getColumnIndex("scannum")));
                if(bean.getWarehouse()==null){
                    bean.setWarehouse("");
                }


                String num_check = cursor.getString(cursor.getColumnIndex("nnum"));
                if (Integer.parseInt(num_check) < 0) {
                    current_bisreturn = "Y";
                }


                bean.pch = "";

                ArrayList<SaleDeliverySendBean.BodyBean.SnBean> snlist = new ArrayList<SaleDeliverySendBean.BodyBean.SnBean>();

                    Cursor cursor3 = db.rawQuery("select prodcutcode,xlh from SaleDeliveryScanResult where  vbillcode=? and matrcode=? and vcooporderbcode_b=? and itemuploadflag=?",
                            new String[]{vbillcode, bean.materialcode, bean.itempk, "N"});

                        //判断cursor中是否存在数据
                        while (cursor3.moveToNext()) {
                            SaleDeliverySendBean.BodyBean.SnBean snbean = new SaleDeliverySendBean.BodyBean.SnBean();
                            String prodcutcode=cursor3.getString(cursor3.getColumnIndex("prodcutcode"));

                            snbean.txm = prodcutcode;
                            snbean.xlh = cursor3.getString(cursor3.getColumnIndex("xlh"));
                            snbean.xm = "";
                            snbean.tp = "";
                            snlist.add(snbean);
                            shunm=cursor3.getCount()+"";
                        }
                        cursor3.close();

                    bean.setShnum(shunm);
                    bean.sn = snlist;
                    //提交过一次的二次提交时不应该被计数

                    bodylist.add(bean);
                }
            cursor.close();




        //通过物流公司名称计算物流公司编号
        String wlCode = "";
        Cursor cursorLogistics = db.rawQuery("select code from LogisticsCompany where name=?",
                new String[]{company});
        if (cursorLogistics != null && cursorLogistics.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursorLogistics.moveToNext()) {
                wlCode = cursorLogistics.getString(0);
            }
            cursorLogistics.close();
        }
        SharedPreferences currentAccount= context.getSharedPreferences("current_account", 0);
        String current_user = currentAccount.getString("current_account","");
            SaleDeliverySendBean otherOutgoingSend = new SaleDeliverySendBean("APP", "123456",current_user,
                    wlCode,expresscode, warehousecode, current_bisreturn, vbillcode, bodylist);
            otherOutgoingSend.setCwhsmanagercode( currentAccount.getString("user",""));
            otherOutgoingSend.setAppuser(currentAccount.getString("user",""));
            otherOutgoingSend.setNcode(vbillcode);


            switch (type){

                case 6:
                   cursor=db.rawQuery("select * from PurchaseArrival where vbillcode=? ",
                            new String[]{vbillcode});

                    break;
                case 7:
                    cursor=db.rawQuery("select * from PurchaseReturn where vbillcode=? ",
                            new String[]{vbillcode});
                    break;
            }
            while (cursor.moveToNext()){
                otherOutgoingSend.setDbilldate(cursor.getString(cursor.getColumnIndex("dbilldate")));
                otherOutgoingSend.setNum(cursor.getString(cursor.getColumnIndex("num")));
                otherOutgoingSend.setBillmaker(currentAccount.getString("user",""));

            }
            cursor.close();

            for (int i = 0; i <otherOutgoingSend.getBody().size() ; i++) {
                if(otherOutgoingSend.getBody().get(i).getScannum()==null||otherOutgoingSend.getBody().get(i).getScannum().equals("0")){
                   otherOutgoingSend.getBody().remove(i);
                   i--;
                }else if(otherOutgoingSend.getBody().get(i).getUploadflag().equals("Y")){
                    otherOutgoingSend.getBody().remove(i);
                    i--;
                }

           }


            Gson gson = new Gson();
            String userSendBean = gson.toJson(otherOutgoingSend);

            request.addProperty("string", workcode);
            request.addProperty("string1", userSendBean);


        Log.i("request-->",request.toString());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.bodyOut = request;
        envelope.dotNet = false;
        String saleDeliveryUploadDataResp = null;

            HttpTransportSE se = new HttpTransportSE(WSDL_URI, 60000);
            //  se.call(null, envelope);//调用 version1.2
            //version1.1 需要如下soapaction
        try {

            se.call(namespace + "sendToWISE", envelope);

            if(envelope.bodyIn.toString().equals("SoapFault - faultcode: 'soap:Server' faultstring:" +
                    " 'JSONObject[\"num\"] not found.' faultactor: 'null' detail: org.kxml2.kdom.Node@70668e"))
            {
                returnMsg("接口异常");
            }

            Object object = envelope.getResponse();
            saleDeliveryUploadDataResp = new Gson().toJson(object);
            Log.i("response-->",envelope.bodyIn.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return saleDeliveryUploadDataResp;
    }

    public static String returnMsg(String string) {

        return  "{\"name\":\"anyType\",\"namespace\":\"http://www.w3.org/2001/XMLSchema\"," +
                "\"value\":\"{\\\"errno\\\":\\\"1\\\",\\\"errmsg+"+string+"}\",\"attributes\":[]}";
    }


}
