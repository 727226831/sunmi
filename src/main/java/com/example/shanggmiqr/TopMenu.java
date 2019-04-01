package com.example.shanggmiqr;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.shanggmiqr.bean.CommonSendBean;
import com.example.shanggmiqr.bean.CommonSendLogisticsBean;
import com.example.shanggmiqr.bean.Customer;
import com.example.shanggmiqr.bean.LogisticsCompany;
import com.example.shanggmiqr.bean.MaterialBean;
import com.example.shanggmiqr.bean.QrcodeRule;
import com.example.shanggmiqr.bean.Supplier;
import com.example.shanggmiqr.bean.User;
import com.example.shanggmiqr.bean.Warhouse;
import com.example.shanggmiqr.transaction.BusinessOperation;
import com.example.shanggmiqr.transaction.DataManage;
import com.example.shanggmiqr.util.BaseConfig;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.MyImageView;
import com.example.shanggmiqr.util.NumberProgressBar;
import com.example.shanggmiqr.util.Utils;
import com.example.weiytjiang.shangmiqr.R;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/8.
 */

public class TopMenu extends AppCompatActivity implements MyImageView.OnClickListener {
    private MyImageView mdataManageButtonView;
    private MyImageView mnavigatorButtonView;

    private String dataResp;
    private SQLiteDatabase db2;
    private MyDataBaseHelper helper2;
    private Handler dataHandler = null;
    private String material_ts_begintime;
    private String material_ts_endtime;
    private String qrcode_rule_ts_begintime;
    private String qrcode_rule_ts_endtime;
    private String user_ts_begintime;
    private String user_ts_endtime;
    private String warhouse_ts_begintime;
    private String warhouse_ts_endtime;
    private String customer_ts_begintime;
    private String customer_ts_endtime;
    private String supplier_ts_endtime;
    private String supplier_ts_begintime;
    private String logistics_company_ts_begintime;
    private String logistics_company_ts_endtime;
    private String exceptionString;
    private NumberProgressBar bnp;
    //	MediumSlateBlue	123 104 238	#7B68EE
    private int warhouse_reached_color = Color.rgb(123,104,238);
    //	SpringGreen	0 255 127	#00FF7F
    private int material_reached_color = Color.rgb(0,255,127);
    //IndianRed1	255 106 106	#FF6A6A
    private int user_reached_color = Color.rgb(255,106,106);
    //DeepPink	255 20 147	#FF1493
    private int qrcode_rule_reached_color = Color.rgb(255,20,147);
    //goldenrod	218 165 32	#DAA520
    private int customer_reached_color = Color.rgb(218,165,32);
    //	Cyan4	0 139 139	#008B8B
    private int supplier_reached_color = Color.rgb(0,139,139);

    private ZLoadingDialog dialog;
    //用临时变量count计数下载成功的基础数据个数，满6 dialog消失
    private int count = 0;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_menu);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mdataManageButtonView = (MyImageView)findViewById(R.id.data_manage);
        mnavigatorButtonView = (MyImageView)findViewById(R.id.work_navi);
        mdataManageButtonView.setOnClickListener(this);
        mnavigatorButtonView.setOnClickListener(this);
        helper2 = new MyDataBaseHelper(TopMenu.this,"ShangmiData",null,1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错
        db2 = helper2.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        dialog = new ZLoadingDialog(TopMenu.this);
        //基础信息离线下载暂时屏蔽掉-S 20181102
        Intent intent = getIntent();
        String str = intent.getStringExtra("from_login");
        if ("Y".equals(str)){
        dialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                .setLoadingColor(Color.BLUE)//颜色
                .setHintText("基础数据信息下载中...")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                //     .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                .show();
        downloadWarehouseData();
        downloadMaterial2Data();
        downloadUser2Data();
        downloadQrcodeData();
    //    downloadCustomerData();
        downloadSupplierData();
        downloadLogisticsCompanyData();
        }
        //基础信息离线下载暂时屏蔽掉-E  20181102
        dataHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(TopMenu.this, "请检查网络连接并到基础信息界面手动更新基础数据", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        count++;
                        if (count ==6){
                            dialog.dismiss();
                        }
                        String latest_warhouse_ts = getCurrentDataTime();
                        SharedPreferences latestDBTimeInfo1= getSharedPreferences("LatestDBTimeInfo", 0);
                        SharedPreferences.Editor editor1 = latestDBTimeInfo1.edit();
                        editor1.putString("latest_warhouse_ts",latest_warhouse_ts);
                        editor1.commit();

                        break;
                    case 0x12:
                        count++;
                        if (count ==6){
                            dialog.dismiss();
                        }
                        String latest_material_ts = getCurrentDataTime();
                        SharedPreferences latestDBTimeInfo2 = getSharedPreferences("LatestDBTimeInfo", 0);
                        SharedPreferences.Editor editor2 = latestDBTimeInfo2.edit();
                        editor2.putString("latest_material_ts",latest_material_ts);
                        editor2.commit();

                        break;
                    case 0x13:
                        count++;
                        if (count ==6){
                            dialog.dismiss();
                        }
                        String latest_user_ts = getCurrentDataTime();
                        SharedPreferences latestDBTimeInfo3 = getSharedPreferences("LatestDBTimeInfo", 0);
                        SharedPreferences.Editor editor3 = latestDBTimeInfo3.edit();
                        editor3.putString("latest_user_ts",latest_user_ts);
                        editor3.commit();

                        break;
                    case 0x14:
                        count++;
                        if (count ==6){
                            dialog.dismiss();
                        }
                        String latest_customer_ts = getCurrentDataTime();
                        SharedPreferences latestDBTimeInfo4 = getSharedPreferences("LatestDBTimeInfo", 0);
                        SharedPreferences.Editor editor4 = latestDBTimeInfo4.edit();
                        editor4.putString("latest_customer_ts",latest_customer_ts);
                        editor4.commit();

                        break;
                    case 0x15:
                        count++;
                        if (count ==6){
                            dialog.dismiss();
                        }
                        String latest_qr_ts = getCurrentDataTime();
                        SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestDBTimeInfo", 0);
                        SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                        editor5.putString("latest_qr_ts",latest_qr_ts);
                        editor5.commit();

                        break;
                    case 0x16:
                        count++;
                        if (count ==6){
                            dialog.dismiss();
                        }
                        String latest_supplier_ts = getCurrentDataTime();
                        SharedPreferences latestDBTimeInfo6 = getSharedPreferences("LatestDBTimeInfo", 0);
                        SharedPreferences.Editor editor6 = latestDBTimeInfo6.edit();
                        editor6.putString("latest_supplier_ts",latest_supplier_ts);
                        editor6.commit();

                        break;
                    case 0x17:

                        break;
                    case 0x18:

                        break;
                    case 0x19:
                        dialog.dismiss();
                        exceptionString = msg.getData().getString("Exception19");
                        Toast.makeText(TopMenu.this, "仓库信息下载异常，请到基础数据管理界面手动下载, 错误："+exceptionString, Toast.LENGTH_LONG).show();
                        break;
                    case 0x20:
                        dialog.dismiss();
                        exceptionString = msg.getData().getString("Exception20");
                        Toast.makeText(TopMenu.this, "物料信息下载异常，请到基础数据管理界面手动下载, 错误："+exceptionString, Toast.LENGTH_LONG).show();
                        break;
                    case 0x21:
                        dialog.dismiss();
                        exceptionString = msg.getData().getString("Exception21");
                        Toast.makeText(TopMenu.this, "用户信息下载异常，请到基础数据管理界面手动下载, 错误："+exceptionString, Toast.LENGTH_LONG).show();
                        break;
                    case 0x22:
                        dialog.dismiss();
                        exceptionString = msg.getData().getString("Exception22");
                        Toast.makeText(TopMenu.this, "条码字典信息下载异常，请到基础数据管理界面手动下载, 错误："+exceptionString, Toast.LENGTH_LONG).show();
                        break;
                    case 0x23:
                        dialog.dismiss();
                        exceptionString = msg.getData().getString("Exception23");
                        Toast.makeText(TopMenu.this, "客户信息下载异常，请到基础数据管理界面手动下载, 错误："+exceptionString, Toast.LENGTH_LONG).show();
                        break;
                    case 0x24:
                        dialog.dismiss();
                        exceptionString = msg.getData().getString("Exception24");
                        Toast.makeText(TopMenu.this, "供应商信息下载异常，请到基础数据管理界面手动下载, 错误："+exceptionString, Toast.LENGTH_LONG).show();
                        break;
                    case 0x25:
                        dialog.dismiss();
                        exceptionString = msg.getData().getString("Exception25");
                        Toast.makeText(TopMenu.this, "物流公司信息下载异常，请到基础数据管理界面手动下载, 错误："+exceptionString, Toast.LENGTH_LONG).show();
                        break;
                    case 0x26:
                        count++;
                        if (count ==6){
                            dialog.dismiss();
                        }
                        String latest_logistics_company_ts = getCurrentDataTime();
                        SharedPreferences latestDBTimeInfo26 = getSharedPreferences("LatestDBTimeInfo", 0);
                        SharedPreferences.Editor editor26 = latestDBTimeInfo26.edit();
                        editor26.putString("latest_logistics_company_ts",latest_logistics_company_ts);
                        editor26.commit();

                        break;
                    default:
                        break;
                }
            }
        };

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder build = new AlertDialog.Builder(this);
                build.setTitle("注意")
                        .setMessage("确定要退出吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                finish();

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                            }
                        })
                        .show();
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.data_manage:
                Intent intent=new Intent(TopMenu.this, DataManage.class);
                startActivity(intent);
                break;
            case R.id.work_navi:
                Intent intent2=new Intent(TopMenu.this, BusinessOperation.class);
                startActivity(intent2);
                break;

        }
    }
    private String getCurrentDataTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
    private void downloadLogisticsCompanyData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected(TopMenu.this)) {
                    try {
                        String logisticsCompanyData = downloadDatabase("R50","1");
                        if (null == logisticsCompanyData) {
                            return;
                        }
                        Gson gsonUser6 =new Gson();
                        LogisticsCompany logisticsCompany = gsonUser6.fromJson(logisticsCompanyData,LogisticsCompany.class);
                        if (Integer.parseInt(logisticsCompany.getTotalpage()) ==1){
                            insertLogisticsCompanyDataToDB(logisticsCompany);
                        }else if(Integer.parseInt(logisticsCompany.getTotalpage()) <1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TopMenu.this, "当前物流公司信息已经是最新数据", Toast.LENGTH_LONG).show();
                                    count++;
                                    if (count ==6){
                                        dialog.dismiss();
                                    }
                                }
                            });
                            return;
                        }else {
                            insertLogisticsCompanyDataToDB(logisticsCompany);
                            for (int pagenum = 2; pagenum <= Integer.parseInt(logisticsCompany.getTotalpage()); pagenum++) {
                                String logisticsCompanyData2 = downloadDatabase("R50", String.valueOf(pagenum));
                                LogisticsCompany logisticsCompany2 = gsonUser6.fromJson(logisticsCompanyData2, LogisticsCompany.class);
                                insertLogisticsCompanyDataToDB(logisticsCompany2);
                            }
                        }
                        String currentTs = Utils.getCurrentDate();
                        logistics_company_ts_begintime = Utils.getCurrentDateTimeNew() ;
                        logistics_company_ts_endtime = Utils.getDefaultEndTime();
                        SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestLogisticsCompanyTSInfo", 0);
                        SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                        editor5.putString("latest_logistics_company_ts_begintime",logistics_company_ts_begintime);
                        editor5.putString("latest_logistics_company_ts_endtime",logistics_company_ts_endtime);
                        editor5.commit();
                        Message msg = new Message();
                        msg.what = 0x26;
                        dataHandler.sendMessage(msg);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception25", e.toString());
                        Message msg = new Message();
                        msg.what = 0x25;
                        msg.setData(bundle);
                        dataHandler.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = 0x10;
                    dataHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void downloadSupplierData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected(TopMenu.this)) {
                    try {
                        String supplierData = downloadDatabase("R06","1");
                        if (null == supplierData) {
                            return;
                        }
                        Gson gsonUser6 =new Gson();
                        Supplier supplier = gsonUser6.fromJson(supplierData,Supplier.class);
                        if (supplier.getPagetotal() ==1){
                            insertSupplierDataToDB(supplier);
                        }else if(supplier.getPagetotal() <1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TopMenu.this, "当前供应商信息已经是最新数据", Toast.LENGTH_LONG).show();
                                    count++;
                                    if (count ==6){
                                        dialog.dismiss();
                                    }
                                }
                            });
                            return;
                        }else {
                            insertSupplierDataToDB(supplier);
                            for (int pagenum = 2; pagenum <= supplier.getPagetotal(); pagenum++) {
                                String supplierData2 = downloadDatabase("R06", String.valueOf(pagenum));
                                Supplier supplier2 = gsonUser6.fromJson(supplierData2, Supplier.class);
                                insertSupplierDataToDB(supplier2);
                            }
                        }
                        String currentTs = Utils.getCurrentDate();
                        supplier_ts_begintime = Utils.getCurrentDateTimeNew() ;
                        supplier_ts_endtime = Utils.getDefaultEndTime();
                        SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestSupplierTSInfo", 0);
                        SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                        editor5.putString("latest_supplier_ts_begintime",supplier_ts_begintime);
                        editor5.putString("latest_supplier_ts_endtime",supplier_ts_endtime);
                        editor5.commit();
                        Message msg = new Message();
                        msg.what = 0x16;
                        dataHandler.sendMessage(msg);
                    } catch (Exception e) {
                       // e.printStackTrace();
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception24", e.toString());
                        Message msg = new Message();
                        msg.what = 0x24;
                        msg.setData(bundle);
                        dataHandler.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = 0x10;
                    dataHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void downloadQrcodeData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected(TopMenu.this)) {
                    try {
                        String qrcodeData = downloadDatabase("R13","1");
                        if (null == qrcodeData) {
                            return;
                        }
                        Gson gsonQrcodeRule =new Gson();
                        QrcodeRule qrcodeRule = gsonQrcodeRule.fromJson(qrcodeData,QrcodeRule.class);
                        if (qrcodeRule.getPagetotal() ==1){
                            insertQrcodeRuledDataToDB(qrcodeRule);

                        }else if(qrcodeRule.getPagetotal() <1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TopMenu.this, "当前条码字典信息已经是最新数据", Toast.LENGTH_LONG).show();
                                    count++;
                                    if (count ==6){
                                        dialog.dismiss();
                                    }
                                }
                            });
                            return;
                        }else {
                            insertQrcodeRuledDataToDB(qrcodeRule);
                            for (int pagenum = 2; pagenum <= qrcodeRule.getPagetotal(); pagenum++) {
                                String qrcodeRuleData2 = downloadDatabase("R13", String.valueOf(pagenum));
                                QrcodeRule qrcodeRule2 = gsonQrcodeRule.fromJson(qrcodeRuleData2, QrcodeRule.class);
                                insertQrcodeRuledDataToDB(qrcodeRule2);
                            }
                        }
                        String currentTs = Utils.getCurrentDate();
                        qrcode_rule_ts_begintime =Utils.getCurrentDateTimeNew() ;
                        qrcode_rule_ts_endtime = Utils.getDefaultEndTime();
                        SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestQrcodeRuleTSInfo", 0);
                        SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                        editor5.putString("latest_qrcode_rule_ts_begintime",qrcode_rule_ts_begintime);
                        editor5.putString("latest_qrcode_rule_ts_endtime",qrcode_rule_ts_endtime);
                        editor5.commit();
                        Message msg = new Message();
                        msg.what = 0x15;
                        dataHandler.sendMessage(msg);
                    } catch (Exception e) {
                       //e.printStackTrace();
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception22", e.toString());
                        Message msg = new Message();
                        msg.what = 0x22;
                        msg.setData(bundle);
                        dataHandler.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = 0x10;
                    dataHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void downloadCustomerData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected(TopMenu.this)) {
                    try {
                        String customerData = downloadDatabase("R04","1");
                        if (null == customerData) {
                            return;
                        }
                        Gson gsonUser2 =new Gson();
                        Customer customer = gsonUser2.fromJson(customerData,Customer.class);
                        if (customer.getPagetotal() ==1){
                            insertCustomerDataToDB(customer);
                        }else if(customer.getPagetotal() <1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TopMenu.this, "当前客户字典信息已经是最新数据", Toast.LENGTH_LONG).show();
                                    count++;
                                    if (count ==6){
                                        dialog.dismiss();
                                    }
                                }
                            });
                            return;
                        }else{
                            insertCustomerDataToDB(customer);
                            for (int pagenum = 2;pagenum<=customer.getPagetotal();pagenum++){
                                String customerData2 = downloadDatabase("R04",String.valueOf(pagenum));
                                Customer customer2 = gsonUser2.fromJson(customerData2, Customer.class);
                                insertCustomerDataToDB(customer2);
                            }
                        }
                        String currentTs = Utils.getCurrentDate();
                        customer_ts_begintime = Utils.getCurrentDateTimeNew() ;
                        customer_ts_endtime = Utils.getDefaultEndTime();
                        SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestCustomerTSInfo", 0);
                        SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                        editor5.putString("latest_customer_ts_begintime",customer_ts_begintime);
                        editor5.putString("latest_customer_ts_endtime",customer_ts_endtime);
                        editor5.commit();
                        Message msg = new Message();
                        msg.what = 0x14;
                        dataHandler.sendMessage(msg);
                    } catch (Exception e) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception23", e.toString());
                        Message msg = new Message();
                        msg.what = 0x23;
                        msg.setData(bundle);
                        dataHandler.sendMessage(msg);
                      //  e.printStackTrace();
                    }
                } else {
                    Message msg = new Message();
                    msg.what = 0x10;
                    dataHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void downloadUser2Data() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected(TopMenu.this)) {
                    try {
                        String userData = downloadDatabase("R03","1");
                        if (null == userData) {
                            return;
                        }
                        Gson gsonUser2 =new Gson();
                        User user2 = gsonUser2.fromJson(userData,User.class);
                        if (user2.getPagetotal() ==1){
                            insertUserDataToDB(user2);
                        }else if(user2.getPagetotal() <1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TopMenu.this, "当前用户信息已经是最新数据", Toast.LENGTH_LONG).show();
                                    count++;
                                    if (count ==6){
                                        dialog.dismiss();
                                    }
                                }
                            });
                            return;
                        }else{
                            insertUserDataToDB(user2);
                            for (int pagenum = 2;pagenum<=user2.getPagetotal();pagenum++){
                                String userData2 = downloadDatabase("R03",String.valueOf(pagenum));
                                User userBean2 = gsonUser2.fromJson(userData2, User.class);
                                insertUserDataToDB(userBean2);
                            }
                        }
                        String currentTs = Utils.getCurrentDate();
                        user_ts_begintime = Utils.getCurrentDateTimeNew() ;
                        user_ts_endtime = Utils.getDefaultEndTime();
                        SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestUserTSInfo", 0);
                        SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                        editor5.putString("latest_user_ts_begintime",user_ts_begintime);
                        editor5.putString("latest_user_ts_endtime",user_ts_endtime);
                        editor5.commit();
                        Message msg = new Message();
                        msg.what = 0x13;
                        dataHandler.sendMessage(msg);
                    } catch (Exception e) {
                       // e.printStackTrace();
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception21", e.toString());
                        Message msg = new Message();
                        msg.what = 0x21;
                        msg.setData(bundle);
                        dataHandler.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = 0x10;
                    dataHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void downloadMaterial2Data() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected(TopMenu.this)) {
                    try {
                        String materialData = downloadDatabase("R02","1");
                        if (null == materialData) {
                            return;
                        }
                        Gson gsonUser =new Gson();
                        MaterialBean materialBean = gsonUser.fromJson(materialData, MaterialBean.class);
                        if (materialBean.getPagetotal() ==1){
                            insertMaterialdDataToDB(materialBean);
                        }else if(materialBean.getPagetotal() <1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TopMenu.this, "当前物料信息已经是最新数据", Toast.LENGTH_LONG).show();
                                    count++;
                                    if (count ==6){
                                        dialog.dismiss();
                                    }
                                }
                            });
                            return;
                        }else{
                            insertMaterialdDataToDB(materialBean);
                            for (int pagenum = 2;pagenum<=materialBean.getPagetotal();pagenum++){
                                String materialData2 = downloadDatabase("R02",String.valueOf(pagenum));
                                MaterialBean materialBean2 = gsonUser.fromJson(materialData2, MaterialBean.class);
                                insertMaterialdDataToDB(materialBean2);
                            }
                        }
                        String currentTs = Utils.getCurrentDate();
                        material_ts_begintime = Utils.getCurrentDateTimeNew() ;
                        material_ts_endtime = Utils.getDefaultEndTime();
                        SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestMaterialTSInfo", 0);
                        SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                        editor5.putString("latest_material_ts_begintime",material_ts_begintime);
                        editor5.putString("latest_material_ts_endtime",material_ts_endtime);
                        editor5.commit();
                        Message msg = new Message();
                        msg.what = 0x12;
                        dataHandler.sendMessage(msg);
                    } catch (Exception e) {
                        //e.printStackTrace();
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception20", e.toString());
                        Message msg = new Message();
                        msg.what = 0x20;
                        msg.setData(bundle);
                        dataHandler.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = 0x10;
                    dataHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    private void downloadWarehouseData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkConnected(TopMenu.this)) {
                    try {
                        String warhouseData = downloadDatabase("R01","1");
                        if (null == warhouseData) {
                            return;
                        }
                        Gson gsonUser =new Gson();
                        Warhouse warhouse = gsonUser.fromJson(warhouseData, Warhouse.class);
                        if (warhouse.getPagetotal() ==1){
                            insertWarhousedDataToDB(warhouse);
                        }else if(warhouse.getPagetotal() <1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TopMenu.this, "当前仓库信息已经是最新数据", Toast.LENGTH_LONG).show();
                                    count++;
                                    if (count ==6){
                                        dialog.dismiss();
                                    }
                                }
                            });
                            return;
                        }else{
                            insertWarhousedDataToDB(warhouse);
                            for (int pagenum = 2;pagenum<=warhouse.getPagetotal();pagenum++){
                                String materialData2 = downloadDatabase("R01",String.valueOf(pagenum));
                                Warhouse warhouseBean2 = gsonUser.fromJson(materialData2, Warhouse.class);
                                insertWarhousedDataToDB(warhouseBean2);
                            }
                        }
                        String currentTs = Utils.getCurrentDate();
                        warhouse_ts_begintime = Utils.getCurrentDateTimeNew() ;
                        warhouse_ts_endtime = Utils.getDefaultEndTime();
                        SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestWarhouseTSInfo", 0);
                        SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                        editor5.putString("latest_warhouse_ts_begintime",warhouse_ts_begintime);
                        editor5.putString("latest_warhouse_ts_endtime",warhouse_ts_endtime);
                        editor5.commit();
                        Message msg = new Message();
                        msg.what = 0x11;
                        dataHandler.sendMessage(msg);
                    } catch (Exception e) {
                      //  e.printStackTrace();
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception19", e.toString());
                        Message msg = new Message();
                        msg.what = 0x19;
                        msg.setData(bundle);
                        dataHandler.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = 0x10;
                    dataHandler.sendMessage(msg);
                }
            }
        }).start();
    }
    private void insertLogisticsCompanyDataToDB(LogisticsCompany logisticsCompany) {

        //对象中拿到集合
        List<LogisticsCompany.DatasBean> customerBeanList = logisticsCompany.getDatas();
        for (LogisticsCompany.DatasBean cb:customerBeanList){
            String org = cb.getOrg();
            String code = cb.getCode();
            String name = cb.getName();
            String status = cb.getStatus();
            String ts = cb.getTs();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            // 开始组装第一条数据
            values.put("org",org);
            values.put("code",code);
            values.put("name",name);
            values.put("status",status);
            values.put("ts",ts);
            // 插入第一条数据
            db2.insert("LogisticsCompany",null,values);
            values.clear();
        }
    }
    private void insertSupplierDataToDB(Supplier supplier) {
        List<Supplier.DataBean> supplierBeanList = supplier.getData();
        for (Supplier.DataBean cb:supplierBeanList){
            String Pk_supplie = cb.getPk_supplie();
            String name = cb.getName();
            String cgorgcode = cb.getCgorgcode();
            String code = cb.getCode();
            String corpaddress = cb.getCorpaddress();
            String orgcode = cb.getOrgcode();
            int supprop = cb.getSupprop();
            String ts = cb.getTs();
            List<Supplier.DataBean.SupplierbankBean> supplierbankBeenList = cb.getSupplierbank();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            for (Supplier.DataBean.SupplierbankBean sbb:supplierbankBeenList){
                ContentValues valuesInner = new ContentValues();
                String bankname = sbb.getBankname();
                String accname = sbb.getAccname();
                String accnum = sbb.getAccnum();
                valuesInner.put("Pk_supplier",Pk_supplie);
                valuesInner.put("bankname",bankname);
                valuesInner.put("accname",accname);
                valuesInner.put("accnum",accnum);
                db2.insert("SupplierBankBody", null, valuesInner);
                valuesInner.clear();
            }
            // List<Supplier.SupplierDataBean.SupplierbankBean> supplierbankBeenList = supplierBeanList;
            values.put("Pk_supplier",Pk_supplie);
            values.put("name",name);
            values.put("cgorgcode",cgorgcode);
            values.put("code",code);
            values.put("corpaddress",corpaddress);
            values.put("orgcode",orgcode);
            values.put("supprop",supprop);
            values.put("ts",ts);
            // 插入第一条数据
            db2.insert("Supplier",null,values);
            values.clear();
        }
    }

    private void insertCustomerDataToDB(Customer customer) {

        //对象中拿到集合
        List<Customer.CustomerDataBean> customerBeanList = customer.getData();
        for (Customer.CustomerDataBean cb:customerBeanList){
            String pk_customer = cb.getPk_customer();
            String code = cb.getCode();
            String name = cb.getName();
            int custstate = cb.getCuststate();
            int enablestate = cb.getEnablestate();
            int custprop = cb.getCustprop();
            String custclassid = cb.getCustclassid();
            String groupcode = cb.getGroupcode();
            String orgcode = cb.getOrgcode();
            String ts = cb.getTs();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            // 开始组装第一条数据
            values.put("pk_customer",pk_customer);
            values.put("code",code);
            values.put("name",name);
            values.put("custstate",custstate);
            values.put("enablestate",enablestate);
            values.put("custprop",custprop);
            values.put("custclassid",custclassid);
            values.put("groupcode",groupcode);
            values.put("orgcode",orgcode);
            values.put("ts",ts);
            // 插入第一条数据
            db2.insert("Customer",null,values);
            values.clear();
        }
    }

    private void insertWarhousedDataToDB(Warhouse warhouse) {
        //对象中拿到集合
        List<Warhouse.WarhouseDataBean> warhouseDataBeanList = warhouse.getData();
        for (Warhouse.WarhouseDataBean wb:warhouseDataBeanList){
            String stordocpk = wb.getStordocpk();
            String name = wb.getName();
            String code = wb.getCode();
            int enablestate = wb.getEnablestate();
            String orgcode = wb.getOrgcode();
            String ts = wb.getTs();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            // 开始组装第一条数据
            values.put("stordocpk",stordocpk);
            values.put("name",name);
            values.put("code",code);
            values.put("enablestate",enablestate);
            values.put("orgcode",orgcode);
            values.put("ts",ts);
            // 插入第一条数据
            db2.insert("Warehouse",null,values);
            values.clear();
        }
    }

    /**
     * webservice查询下载
     */
    public String downloadDatabase(String workCode,String pageNum) throws Exception {
        String WSDL_URI;
        String namespace;
        String begintime= "2001-01-01 00:00:00";
        String endtime =Utils.getDefaultEndTime();
        String WSDL_URI_current = BaseConfig.getNcUrl();//wsdl 的uri
        String namespace_current = "http://schemas.xmlsoap.org/soap/envelope/";//namespace
        String methodName = "sendToWISE";//要调用的方法名称
        SharedPreferences proxySp = getSharedPreferences("configInfo", 0);
        if (proxySp.getString("WSDL_URI", WSDL_URI_current).equals("") || proxySp.getString("namespace", namespace_current).equals("")) {
            WSDL_URI = WSDL_URI_current;
            namespace = namespace_current;
        } else {
            WSDL_URI = proxySp.getString("WSDL_URI", WSDL_URI_current);
            namespace = proxySp.getString("namespace", namespace_current);
        }
        SoapObject request = new SoapObject(namespace, methodName);
        // 设置需调用WebService接口需要传入的两个参数string、string1
        if (workCode.equals("R02")) {
            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestMaterialTSInfo", 0);
            begintime = latestDBTimeInfo.getString("latest_material_ts_begintime", "2001-01-01 00:00:00");
            endtime = latestDBTimeInfo.getString("latest_material_ts_endtime", Utils.getDefaultEndTime());
        } else if (workCode.equals("R13")){
            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestQrcodeRuleTSInfo", 0);
            begintime = latestDBTimeInfo.getString("latest_qrcode_rule_ts_begintime", "2001-01-01 00:00:00");
            endtime = latestDBTimeInfo.getString("latest_qrcode_rule_ts_endtime", Utils.getDefaultEndTime());
        } else if (workCode.equals("R03")){
            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestUserTSInfo", 0);
            begintime = latestDBTimeInfo.getString("latest_user_ts_begintime", "2001-01-01 00:00:00");
            endtime = latestDBTimeInfo.getString("latest_user_ts_endtime", Utils.getDefaultEndTime());
        }else if (workCode.equals("R01")){
            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestWarhouseTSInfo", 0);
            begintime = latestDBTimeInfo.getString("latest_warhouse_ts_begintime", "2001-01-01 00:00:00");
            endtime = latestDBTimeInfo.getString("latest_warhouse_ts_endtime", Utils.getDefaultEndTime());
        }else if (workCode.equals("R04")){
            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestCustomerTSInfo", 0);
            begintime = latestDBTimeInfo.getString("latest_customer_ts_begintime", "2001-01-01 00:00:00");
            endtime = latestDBTimeInfo.getString("latest_customer_ts_endtime", Utils.getDefaultEndTime());
        }else if (workCode.equals("R06")){
            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestSupplierTSInfo", 0);
            begintime = latestDBTimeInfo.getString("latest_supplier_ts_begintime", "2001-01-01 00:00:00");
            endtime = latestDBTimeInfo.getString("latest_supplier_ts_endtime", Utils.getDefaultEndTime());
        }else if (workCode.equals("R50")){
            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestLogisticsCompanyTSInfo", 0);
            begintime = latestDBTimeInfo.getString("latest_logistics_company_ts_begintime", "2001-01-01 00:00:00");
            endtime = latestDBTimeInfo.getString("latest_logistics_company_ts_endtime", Utils.getDefaultEndTime());
        }
        CommonSendBean userSend = new CommonSendBean(begintime, endtime, pageNum, "0");
        Gson gson = new Gson();
        String userSendBean = gson.toJson(userSend);
        if (workCode.equals("R50")){
            CommonSendLogisticsBean userSend2 = new CommonSendLogisticsBean(begintime, endtime, pageNum);
            userSendBean = gson.toJson(userSend2);
        }
        request.addProperty("string", workCode);
        request.addProperty("string1", userSendBean);
        //request.addProperty("string1", "{\"begintime\":\"1900-01-20 00:00:00\",\"endtime\":\"2018-08-21 00:00:00\", \"pagenum\":\"1\",\"pagetotal\":\"66\"}");
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.bodyOut = request;
        envelope.dotNet = false;

        HttpTransportSE se = new HttpTransportSE(WSDL_URI);
        //  se.call(null, envelope);//调用 version1.2
        //version1.1 需要如下soapaction
        se.call(namespace + "sendToWISE", envelope);
        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        dataResp = object.getProperty(0).toString();
        return dataResp;
    }

    private void insertUserDataToDB(User user2) {
        //对象中拿到集合
        List<User.UserDataBean> userBeanList2 = user2.getData();
        for (User.UserDataBean ub:userBeanList2){
            String userpk = ub.getUserpk();
            String code = ub.getCode();
            String name = ub.getName();
            String org = ub.getOrg();
            int enablestate = ub.getEnablestate();
            String ts = ub.getTs();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            // 开始组装第一条数据
            values.put("name",name);
            values.put("code",code);
            values.put("userpk",userpk);
            values.put("org",org);
            values.put("enablestate",enablestate);
            values.put("ts",ts);
            // 插入第一条数据
            db2.insert("User",null,values);
            values.clear();
        }
    }

    private void insertQrcodeRuledDataToDB (QrcodeRule qrcodeRule){
        //对象中拿到集合
        List<QrcodeRule.DataBean> qrCodeRuleBeanList = qrcodeRule.getData();
        for (QrcodeRule.DataBean cb:qrCodeRuleBeanList){
            String matbasclassname = cb.getMatbasclassname();
            String Matbasclasscode = cb.getMatbasclasscode();
            String name = cb.getName();
            String code = cb.getCode();
            int length = cb.getLength();
            String bartype = cb.getBartype();
            int complement = cb.getComplement();
            String fillcode = cb.getFillcode();
            String ts = cb.getTs();
            List<QrcodeRule.DataBean.ItemBean> qrcodeRuleItemBeanList = cb.getItem();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            for (QrcodeRule.DataBean.ItemBean sbb:qrcodeRuleItemBeanList){
                //这里应该执行的是插入第二个表的操作
                ContentValues valuesInner = new ContentValues();
                String appobjattr = sbb.getAppobjattr();
                String itemlength = sbb.getItemlength();
                String startpos = sbb.getStartpos();
                valuesInner.put("Matbasclasscode",Matbasclasscode);
                valuesInner.put("appobjattr",appobjattr);
                valuesInner.put("itemlength",itemlength);
                valuesInner.put("startpos",startpos);
                db2.insert("QrcodeRuleBody", null, valuesInner);
                valuesInner.clear();
            }
            // List<Supplier.SupplierDataBean.SupplierbankBean> supplierbankBeenList = supplierBeanList;
            values.put("matbasclassname",matbasclassname);
            values.put("Matbasclasscode",Matbasclasscode);
            values.put("name",name);
            values.put("code",code);
            values.put("length",length);
            values.put("bartype",bartype);
            values.put("complement",complement);
            values.put("fillcode",fillcode);
            values.put("ts",ts);
            // 插入第一条数据
            db2.insert("QrcodeRule",null,values);
            values.clear();
        }
    }
    private void insertMaterialdDataToDB (MaterialBean materialBean){
        //对象中拿到集合
        List<MaterialBean.DataBean> materialDataBeanList = materialBean.getData();
        for (MaterialBean.DataBean mb:materialDataBeanList){
            //目前是以返回报文为准建立的bean，与文档并不一样
            String stordocpk = mb.getStordocpk();
            String name = mb.getName();
            String code = mb.getCode();
            String marbasclass = mb.getMarbasclass();
            String ename = mb.getEname();
            String materialspec = mb.getMaterialspec();
            String materialtype = mb.getMaterialtype();
            String measdoccode = mb.getMeasdoccode();
            String orgcode = mb.getOrgcode();
            String brandname = mb.getBrandname();
            int enablestate = mb.getEnablestate();
            String materialbarcode = mb.getMaterialbarcode();
            String ts = mb.getTs();

            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            // 开始组装第一条数据
            values.put("stordocpk",stordocpk);
            values.put("name",name);
            values.put("code",code);
            values.put("marbasclass",marbasclass);
            values.put("ename",ename);
            values.put("materialspec",materialspec);
            values.put("materialtype",materialtype);
            values.put("measdoccode",measdoccode);
            values.put("orgcode",orgcode);
            values.put("brandname",brandname);
            values.put("enablestate",enablestate);
            values.put("materialbarcode",materialbarcode);
            values.put("ts",ts);
            // 插入第一条数据
            db2.insert("Material",null,values);
            values.clear();
        }
    }
    public boolean isNetworkConnected(Context context) {
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
}
