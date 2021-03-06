package com.example.shanggmiqr.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.shanggmiqr.Url.iUrl;
import com.example.shanggmiqr.bean.CommonSendBean;
import com.example.shanggmiqr.bean.LogisticsBean;
import com.example.shanggmiqr.bean.OtherQueryBean;
import com.example.shanggmiqr.bean.QrcodeRule;
import com.example.shanggmiqr.bean.SaleDeliverySendBean;
import com.example.shanggmiqr.transaction.SaleDeliveryDetail;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                cursor = db.rawQuery("select count(prodcutcode) from SaleDeliveryScanResult where vbillcode=?  and vcooporderbcode_b=? ",
                        new String[]{code, vcooporderbcode_b});
                break;
            case 1:
                cursor = db.rawQuery("select count(prodcutcode) from OtherEntryScanResult  where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{code, materialcode, vcooporderbcode_b});
                break;
            case 2:
                cursor = db.rawQuery("select  count(prodcutcode) from OtherOutgoingScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{code,materialcode, vcooporderbcode_b});
                break;
            case 8:
                cursor = db.rawQuery("select count(prodcutcode) from SaleDeliveryScanResult where vbillcode=?  and vcooporderbcode_b=? ",
                        new String[]{code,vcooporderbcode_b});
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
    public static void updateLogistics(SQLiteDatabase db, String vbillcode,String logistics,String logisticscode, int type) {
        ContentValues contentValues=new ContentValues();
        contentValues.put("logistics",logistics);
        contentValues.put("logisticscode",logisticscode);

        switch (type){
            case 0:
                db.update("SaleDelivery",contentValues,"vbillcode=? ",
                        new String[]{ vbillcode});

                break;
            case 1:
                db.update("OtherEntry",contentValues,"pobillcode=?",
                        new String[]{ vbillcode});
                break;
            case 2:
                db.update("OtherOutgoing",contentValues,"pobillcode=?",
                        new String[]{ vbillcode});
                break;
            case 4:
                db.update("Loan",contentValues,"pobillcode=?",
                        new String[]{ vbillcode});
                break;
            case 6:
                db.update("PurchaseArrival",contentValues,"vbillcode=? ",
                        new String[]{ vbillcode});
                break;
            case 7:
                db.update("PurchaseReturn",contentValues,"vbillcode=? ",
                        new String[]{ vbillcode});
                break;
            case 8:
                db.update("SaleDelivery",contentValues,"vbillcode=? ",
                        new String[]{ vbillcode});
                break;



        }

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
            case 8:
                db.update("SaleDeliveryBody",contentValues,"vbillcode=? and vcooporderbcode_b=?",
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
        Boolean isY=false;
        Boolean isPY=false;
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
            valuesInner.put("uploadnum",obb.getYsnum());
            valuesInner.put("scannum", scannum);
            if(nnum!=0){

                if(nnum==Integer.parseInt(obb.getYsnum())){
                    isY=true;
                    valuesInner.put("uploadflag", "Y");
                } else {
                    isPY=true;
                    valuesInner.put("uploadflag", "PY");
                }
            }else {
                valuesInner.put("uploadflag", "N");
            }

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
        if(isY==true && isPY==false){
            values.put("flag", "Y");
        }else if(isPY){
            values.put("flag", "PY");
        }else {
            values.put("flag", "N");
        }

        // 插入第一条数据
        db.insert(otherTable, null, values);
        values.clear();
    }




    public static void showDialog(ZLoadingDialog dialog) {
        dialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                .setLoadingColor(Color.BLUE)//颜色
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                .show();
    }

    public  static String downloadDatabase(String pagenum, Context context,int type) throws Exception {
        Date curDate =  new Date(System.currentTimeMillis());
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        saveData("请求时间"+formatter.format(curDate));
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
            case 8:
                name="LatestExportTSInfo";
                workCode="R14";
                break;


        }
        SharedPreferences latestDBTimeInfo = context.getSharedPreferences(name, 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", iUrl.getBegintime(context));
        String endtime = getDefaultEndTime();
        CommonSendBean userSend = new CommonSendBean(begintime, endtime, pagenum, "0");
        userSend.setAppuser(getUser(context));
        Gson gson = new Gson();
        String userSendBean = gson.toJson(userSend);
        request.addProperty("string", workCode);
        request.addProperty("string1", userSendBean);
        Log.i("request code-->",workCode);
        Log.i("request-->",request.toString());
        Log.i("url-->",WSDL_URI+"?");
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.bodyOut = request;
        envelope.dotNet = false;
        Log.i("url-->",WSDL_URI);

        HttpTransportSE se = new HttpTransportSE(WSDL_URI);
        //version1.1 需要如下soapaction
        se.call(namespace + "sendToWISE", envelope);
        // 获取返回的数据


        SoapObject object = (SoapObject) envelope.bodyIn;

        // 获取返回的结果
       String otherOutgoingDataResp = object.getProperty(0).toString();
        Log.i("response-->",otherOutgoingDataResp);
        saveData("返回时间"+formatter.format(curDate));
       saveData(request.toString()+"\r\n"+otherOutgoingDataResp);
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
            case 8:
                name="LatestExportTSInfo";
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
            case 8:
                name="LatestExportTSInfo";
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
            case 3:
                cursor = db.rawQuery("SELECT count(billcode) FROM ProductEntry WHERE billcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
            case 4:
                cursor = db.rawQuery("SELECT count(pobillcode) FROM Loan WHERE pobillcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
            case 5:

                cursor = db.rawQuery("SELECT count(billno) FROM AllocateTransfer WHERE billno=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
            case 6:
                cursor = db.rawQuery("SELECT count(vbillcode) FROM PurchaseArrival WHERE vbillcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
            case 7:
                cursor = db.rawQuery("SELECT count(vbillcode) FROM PurchaseReturn WHERE vbillcode=? and "+
                                "dbilldate>=? and dbilldate<?",
                        new String[] { code,startTime, endTime});
                break;
            case 8:
                cursor = db.rawQuery("SELECT count(vbillcode) FROM SaleDelivery WHERE vbillcode=? and "+
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
            case 3:
                name="query_productentry";
                break;
            case 4:
                name="query_loanbill";
                break;
            case 5:
                name="query_allocatetransfer";
                break;
            case 6:
                name="query_purchasearrival";
                break;
            case 7:
                name="query_purchasereturn";
                break;
        }
        SharedPreferences currentTimePeriod= context.getSharedPreferences(name, 0);
        String tempperiod =currentTimePeriod.getString("current_account","2018-09-01 至 2019-08-23");
        return tempperiod;
    }
    public static String getRequestbody(String workcode,String json) {

        return  "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:inet=\"http://net.sm.itf.nc/INetWebserviceServer\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <inet:sendToWISE>\n" +
                "         <!--Optional:-->\n" +
                "         <string>"+workcode+"</string>\n" +
                "         <!--Optional:-->\n" +
                "         <string1>\n" + json+ "</string1>\n" +
                "      </inet:sendToWISE>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
    }
    public static void insertLogistics(SQLiteDatabase db, LogisticsBean logisticsBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("billcode",logisticsBean.getBillcode());
        contentValues.put("name",logisticsBean.getName());
        contentValues.put("code",logisticsBean.getCode());
        db.insert("Logistics",null,contentValues);
    }
    public static LogisticsBean getLogisticsbybillcode(SQLiteDatabase db, LogisticsBean logisticsBean) {
        LogisticsBean logisticsBean1;
        return  logisticsBean;
    }

    public static String getRequestJson(SQLiteDatabase db, String vbillcode, Context context, String company,
                                        String expresscode, int type) {


        String warehousecode=null;
        String current_bisreturn="N";
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
            case 8:
                cursor=db.rawQuery("select * from SaleDeliveryBody where vbillcode=?  ",
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
                    case 8:
                        bean.itempk = cursor.getString(cursor.getColumnIndex("vcooporderbcode_b"));
                        bean.materialcode = cursor.getString(cursor.getColumnIndex("matrcode"));
                        warehousecode = getCwarehousecode(cursor.getString(cursor.getColumnIndex("cwarename")),db);
                        bean.setUploadflag(cursor.getString(cursor.getColumnIndex("uploadflag")));
                        break;
                }
                bean.setCwarecode(warehousecode);
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

                    Cursor cursor3 = db.rawQuery("select prodcutcode,xlh from SaleDeliveryScanResult where  vbillcode=?  and vcooporderbcode_b=? and itemuploadflag=?",
                            new String[]{vbillcode, bean.itempk, "N"});

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
                }else if(otherOutgoingSend.getBody().get(i).getSn().isEmpty()){
                    otherOutgoingSend.getBody().remove(i);
                    i--;
                }

           }


            Gson gson = new Gson();

            if(otherOutgoingSend.getBody().isEmpty()){
                return "{\"errno\": \"1\",\"errmsg\": \"扫描数量为0不能提交\"}";
            }
            String code="";
        for (int i = 0; i <otherOutgoingSend.getBody().size() ; i++) {
            if(code.equals("")){
                code=otherOutgoingSend.getBody().get(i).getCwarecode();
            }

            if(!code.equals(otherOutgoingSend.getBody().get(i).getCwarecode())){
                return "{\"errno\": \"1\",\"errmsg\": \"不同仓库的行号不可以同时上传\"}";
            }
        }
            otherOutgoingSend.setCwarehousecode(otherOutgoingSend.getBody().get(0).getCwarecode());
            String userSendBean = gson.toJson(otherOutgoingSend);


        return userSendBean;
    }
    public  static void getData(String r,String data){
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:inet=\"http://net.sm.itf.nc/INetWebserviceServer\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <inet:sendToWISE>\n" +
                "         <!--Optional:-->\n" +
                "         <string>"+r+"</string>\n" +
                "         <!--Optional:-->\n" +
                "         <string1>\n" + data+ "</string1>\n" +
                "      </inet:sendToWISE>\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        Request request = new Request.Builder()
                .url(iUrl.WSDL_URI)
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              Log.i("okhttp",response.body().string());

            }
        });

    };
    public static void   setLog(Context context,String log){
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        File file=new File(sdCardDir+"/sunmi");
        if(!file.exists()){
            file.mkdir();
        }
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        Date curDate =  new Date(System.currentTimeMillis());

        file=new File(sdCardDir+"/sunmi/log","ScanLog.txt");
        try {
            FileOutputStream out=new FileOutputStream(file,true);

                StringBuffer sb=new StringBuffer();
                sb.append("\r\n");
                sb.append(formatter.format(curDate));
               sb.append("\r\n");
                sb.append(log);
               sb.append("\r\n");
                out.write(sb.toString().getBytes("utf-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveData( String string) {
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        File file=new File(sdCardDir+"/sunmi/log");
        if(!file.exists()){
            file.mkdir();
        }
        Date curDate =  new Date(System.currentTimeMillis());
        file=new File(sdCardDir+"/sunmi/log","datalog.txt");
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file,true);
            outputStream.write("\r\n".getBytes());
            outputStream.write(formatter.format(curDate).getBytes());
            outputStream.write("\r\n".getBytes());
            outputStream.write(string.getBytes());


            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
