package com.example.shanggmiqr.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.BusinessOperation;
import com.example.shanggmiqr.Url.iUrl;
import com.example.shanggmiqr.adapter.PurchaseArrivalAdapter;
import com.example.shanggmiqr.bean.PurchaseArrivalBean;
import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.PurchaseReturnAdapter;
import com.example.shanggmiqr.bean.CommonSendAllocateBean;
import com.example.shanggmiqr.bean.PurchaseReturnBean;
import com.example.shanggmiqr.bean.PurchaseReturnQuery;
import com.example.shanggmiqr.bean.SaleDeliveryBean;
import com.example.shanggmiqr.util.BaseConfig;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 采购退货
 */

public class PurchaseReturn extends AppCompatActivity implements OnClickListener {
    private String saleDelivDataResp;
    private Button downloadPurchaseReturnButton;
    private Button queryPurchaseReturnButton;
    private Button displayallPurchaseReturnButton;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private Handler purchaseReturnHandler = null;
    private ListView tableListView;
    private List<PurchaseReturnBean> listAllPostition;
    private String chosen_line_vbillcode;
    private String chosen_line_dbilldate;
    private String saleDeliveryUploadDataResp;
    private ZLoadingDialog dialog;
    private String query_cwarename;
    private String query_uploadflag;
    private List<String> test;
    private List<String> uploadflag;
    private TextView lst_downLoad_ts;
    private TextView time;
    private Button buttonExport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_return_manage);
        helper3 = new MyDataBaseHelper(PurchaseReturn.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra("title"));
        }
        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts_purchase_return);
        //显示最后一次的下载时间
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestPurchaseReturnTSInfo", 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "");
        lst_downLoad_ts.setText("最后一次下载:"+begintime);

        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        dialog = new ZLoadingDialog(PurchaseReturn.this);
        downloadPurchaseReturnButton = (Button) findViewById(R.id.download_purchase_return);
        downloadPurchaseReturnButton.setOnClickListener(this);
        queryPurchaseReturnButton = (Button) findViewById(R.id.query_purchase_return);
        queryPurchaseReturnButton.setOnClickListener(this);
        displayallPurchaseReturnButton = (Button) findViewById(R.id.displayall_purchase_return);
        //displayallSaleDeliveryButton.setVisibility(View.INVISIBLE);
        displayallPurchaseReturnButton.setOnClickListener(this);

        tableListView = (ListView) findViewById(R.id.list_purchase_return);
        buttonExport=findViewById(R.id.b_export);
        buttonExport.setOnClickListener(this);
        List<PurchaseReturnBean> list = querySaleDelivery();
        listAllPostition = list;
        final PurchaseReturnAdapter adapter1 = new PurchaseReturnAdapter(PurchaseReturn.this, list, mListener);
        tableListView.setAdapter(adapter1);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.select(position);
                PurchaseReturnBean saleDelivery1Bean = (PurchaseReturnBean) adapter1.getItem(position);
                chosen_line_vbillcode = saleDelivery1Bean.getVbillcode();
                chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();

            }
        });
        purchaseReturnHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(PurchaseReturn.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        //插入UI表格数据
                        dialog.dismiss();
                        List<PurchaseReturnBean> list = querySaleDelivery();
                        listAllPostition = list;
                        final PurchaseReturnAdapter adapter = new PurchaseReturnAdapter(PurchaseReturn.this, list, mListener);
                        tableListView.setAdapter(adapter);
                        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.select(position);
                                PurchaseReturnBean saleDelivery1Bean = (PurchaseReturnBean) adapter.getItem(position);
                                chosen_line_vbillcode = saleDelivery1Bean.getVbillcode();
                                chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();
                                //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                            }
                        });

                        break;
                    case 0x18:
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(PurchaseReturn.this, s, Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(PurchaseReturn.this, "采购退货单下载异常，错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        Intent intent = getIntent();
        String str = intent.getStringExtra("from_business_operation");
        if ("Y".equals(str)) {
            downloadPurchaseReturnButton.performClick();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
       purchaseReturnHandler.sendEmptyMessage(0x11);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_purchase_return:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkConnected(PurchaseReturn.this)) {
                            try {
                                if (isWarehouseDBDownloaed()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                                                    .setLoadingColor(Color.BLUE)//颜色
                                                    .setCancelable(false)
                                                    .setCanceledOnTouchOutside(false)
                                                    .setHintTextSize(16) // 设置字体大小 dp
                                                    .setHintTextColor(Color.GRAY)  // 设置字体颜色
                                                    .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                                                    //     .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                                                    .show();
                                        }
                                    });
                                    //R07发货单
                                    String saleDeliveryData = DataHelper.downloadDatabase( "1",PurchaseReturn.this,7);
                                    if (null == saleDeliveryData) {
                                        dialog.dismiss();
                                        return;
                                    }

                                    Gson gson7 = new Gson();
                                    final PurchaseReturnQuery purchaseReturnQuery = gson7.fromJson(saleDeliveryData, PurchaseReturnQuery.class);
                                    int pagetotal = Integer.parseInt(purchaseReturnQuery.getPagetotal());
                                    if (pagetotal == 1) {
                                        insertDownloadDataToDB(purchaseReturnQuery);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        purchaseReturnHandler.sendMessage(msg);
                                    } else if (pagetotal < 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(PurchaseReturn.this, purchaseReturnQuery.getErrmsg(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        insertDownloadDataToDB(purchaseReturnQuery);
                                        for (int pagenum = 2; pagenum <= pagetotal; pagenum++) {
                                            String saleDeliveryData2 = DataHelper.downloadDatabase( pagenum+"",PurchaseReturn.this,7);
                                            PurchaseReturnQuery saleDeliveryQuery2 = gson7.fromJson(saleDeliveryData2, PurchaseReturnQuery.class);
                                            insertDownloadDataToDB(saleDeliveryQuery2);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        purchaseReturnHandler.sendMessage(msg);
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DataHelper.putLatestdownloadbegintime(getIntent().getIntExtra("type",-1),PurchaseReturn.this);
                                            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestPurchaseReturnTSInfo", 0);
                                            String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "2018-09-01 00:00:01");
                                            lst_downLoad_ts.setText("最后一次下载:"+begintime);
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Toast.makeText(PurchaseReturn.this, "请先到基础数据管理界面下载仓库信息", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                //e.printStackTrace();
                                Bundle bundle = new Bundle();
                                bundle.putString("Exception", e.toString());
                                Message msg = new Message();
                                msg.what = 0x19;
                                msg.setData(bundle);
                                purchaseReturnHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0x10;
                            purchaseReturnHandler.sendMessage(msg);
                        }
                    }
                }).start();


                break;
            case R.id.query_purchase_return:
                popupQuery();
                break;
            case R.id.displayall_purchase_return:
                List<PurchaseReturnBean> list = displayAllSaleDelivery();
                listAllPostition = list;
                final PurchaseReturnAdapter adapter = new PurchaseReturnAdapter(PurchaseReturn.this, list, mListener);
                tableListView.setAdapter(adapter);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.select(position);
                        PurchaseReturnBean saleDelivery1Bean = (PurchaseReturnBean) adapter.getItem(position);
                        chosen_line_vbillcode = saleDelivery1Bean.getVbillcode();
                        chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();
                        //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.b_export:
                exportData(exportList);
                break;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean isWarehouseDBDownloaed() {
        Cursor cursor = db3.rawQuery("select name from Warehouse",
                null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }else {
            return false;
        }
    }

    private void insertDownloadDataToDB(PurchaseReturnQuery saleDeliveryQuery) {

        List<PurchaseReturnQuery.DataBean> saleDeliveryBeanList = saleDeliveryQuery.getData();
        for (PurchaseReturnQuery.DataBean ob : saleDeliveryBeanList) {
            Log.i("PurchaseReturn-->",new Gson().toJson(ob));
            String vbillcode = ob.getVbillcode();
            //0:新增-正常下载保持 1：删除，删除对应单据 2：修改，先删除对应单据再保持
             switch (Integer.parseInt(ob.getDr())){
                 case 0:
                     insertDb(ob);
                     break;
                 case 1:
                     db3.delete("PurchaseReturn", "vbillcode=?", new String[]{vbillcode});
                     db3.delete("PurchaseReturnBody", "vbillcode=?", new String[]{vbillcode});
                     db3.delete("PurchaseReturnScanResult", "vbillcode=?", new String[]{vbillcode});
                     break;
                 case 2:
                     db3.delete("PurchaseReturn", "vbillcode=?", new String[]{vbillcode});
                     db3.delete("PurchaseReturnBody", "vbillcode=?", new String[]{vbillcode});
                     db3.delete("PurchaseReturnScanResult", "vbillcode=?", new String[]{vbillcode});
                     insertDb(ob);
                     break;
             }


        }
    }

    private void insertDb(PurchaseReturnQuery.DataBean ob) {
        String vbillcode = ob.getVbillcode();
        String dbilldate = ob.getDbilldate();
        String ts = ob.getTs();
        String org = ob.getOrg();
        String dr =ob.getDr();
        List<PurchaseReturnQuery.DataBean.BodyBean> saleDeliveryDatabodysList = ob.getBody();
        //使用 ContentValues 来对要添加的数据进行组装
        ContentValues values = new ContentValues();
        for (PurchaseReturnQuery.DataBean.BodyBean obb : saleDeliveryDatabodysList) {
            String itempk = obb.getItempk();
            String materialcode = obb.getMaterialcode();
            String nnum = obb.getNnum();
            String maccode = obb.getMaccode();
            //          Object warehouse = obb.getWarehouse();
            String materialname = obb.getMaterialname();
            //这里应该执行的是插入第二个表的操作
            ContentValues valuesInner = new ContentValues();
            valuesInner.put("vbillcode", vbillcode);
            valuesInner.put("maccode", maccode);
            valuesInner.put("nnum", nnum);
            valuesInner.put("scannum", "0");
            valuesInner.put("itempk", itempk);
            valuesInner.put("materialcode", materialcode);
            valuesInner.put("materialname", materialname);
            //N代表尚未上传
            valuesInner.put("uploadflag", "N");
            db3.insert("PurchaseReturnBody", null, valuesInner);
            valuesInner.clear();
        }
        values.put("vbillcode", vbillcode);
        values.put("dbilldate", dbilldate);
        values.put("ts", ts);
        values.put("num",ob.getNum());
        values.put("org", org);
        values.put("dr", dr);
        values.put("flag", "N");
        // 插入第一条数据
        db3.insert("PurchaseReturn", null, values);
        values.clear();
    }








    private void popupQuery() {
        List<String> listWarehouse;

        LayoutInflater layoutInflater = LayoutInflater.from(PurchaseReturn.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditText = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinner = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinner = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        final Button showdailogTwo = (Button)  textEntryView.findViewById(R.id.showdailogTwo);
        time = (TextView)  textEntryView.findViewById(R.id.timeshow_saledelivery);

        String tempperiod =DataHelper.getQueryTime(PurchaseReturn.this,getIntent().getIntExtra("type",-1));
        showdailogTwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTwo();
            }
        });

        //仓库选择
        listWarehouse = DataHelper.queryWarehouseInfo(db3);
        listWarehouse.add("");
        final ArrayAdapter arrayAdapter = new ArrayAdapter(
                PurchaseReturn.this, android.R.layout.simple_spinner_item, listWarehouse);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(listWarehouse.size()-1);
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                query_cwarename=arrayAdapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //订单选择
        uploadflag = new ArrayList();
        uploadflag.add("否");
        uploadflag.add("部分上传");
        uploadflag.add("是");
        uploadflag.add("全部");
        final ArrayAdapter adapter2 = new ArrayAdapter(
                PurchaseReturn.this, android.R.layout.simple_spinner_item, uploadflag);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flag_spinner.setAdapter(adapter2);

        flag_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        query_uploadflag = "N";
                        break;
                    case 1:
                        query_uploadflag = "PY";
                        break;
                    case 2:
                        query_uploadflag = "Y";
                        break;
                    case 3:
                        query_uploadflag = "ALL";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        AlertDialog.Builder ad1 = new AlertDialog.Builder(PurchaseReturn.this);
        ad1.setTitle("出入查询条件:");
        ad1.setView(textEntryView);
        time.setText(tempperiod);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                String temp=codeNumEditText.getText().toString();
                exportList= queryexport(temp,query_cwarename,query_uploadflag);
                listAllPostition=new ArrayList<>();
                listAllPostition=removeDuplicate(exportList);
                PurchaseReturnAdapter adapter=new PurchaseReturnAdapter(PurchaseReturn.this,exportList,mListener);
                tableListView.setAdapter(adapter);



            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }

    List<PurchaseReturnBean> exportList;

    private    List<PurchaseReturnBean>  removeDuplicate(List<PurchaseReturnBean> list)  {
        List<PurchaseReturnBean>  beanList=new ArrayList<>();
        beanList.addAll(list);

        for  ( int  i  =   0 ; i  <  beanList.size()  -   1 ; i ++ )  {

            for  ( int  j  =  beanList.size()  -   1 ; j  >  i; j -- )  {

                if  (beanList.get(j).getVbillcode().equals(beanList.get(i).getVbillcode()))  {
                    beanList.remove(j);
                }
            }
        }
        return beanList;
    }
    private ArrayList<PurchaseReturnBean> queryexport(String vbillcode,String current_cwarename,String query_uploadflag) {
        ArrayList<PurchaseReturnBean> list = new ArrayList<>();
        SharedPreferences currentTimePeriod= getSharedPreferences("query_purchasereturn", 0);
        String start_temp = currentTimePeriod.getString("starttime", iUrl.begintime);
        String end_temp = currentTimePeriod.getString("endtime", Utils.getDefaultEndTime());
        Cursor cursor=null;
        if(query_uploadflag.equals("ALL")){
            cursor = db3.rawQuery("select PurchaseReturn.vbillcode, PurchaseReturn.dbilldate,PurchaseReturnbody.materialcode,PurchaseReturn.dr," +
                    "PurchaseReturnbody.materialname,PurchaseReturnbody.maccode,PurchaseReturnbody.nnum,saledeliveryscanresult.prodcutcode," +
                    "saledeliveryscanresult.xlh" + " from PurchaseReturn inner join PurchaseReturnbody on PurchaseReturn.vbillcode=PurchaseReturnbody.vbillcode " +
                    "left join saledeliveryscanresult on PurchaseReturnbody.vbillcode=saledeliveryscanresult.vbillcode " +
                    "and PurchaseReturnbody.itempk=saledeliveryscanresult.vcooporderbcode_b where PurchaseReturn.vbillcode" +
                    " like '%" + vbillcode + "%' and PurchaseReturnbody.warehouse"+ " like '%" + current_cwarename + "%' order by dbilldate desc", null);

        }else {
            cursor = db3.rawQuery("select PurchaseReturn.vbillcode, PurchaseReturn.dbilldate,PurchaseReturnbody.materialcode,PurchaseReturn.dr," +
                    "PurchaseReturnbody.materialname,PurchaseReturnbody.maccode,PurchaseReturnbody.nnum,saledeliveryscanresult.prodcutcode," +
                    "saledeliveryscanresult.xlh" + " from PurchaseReturn inner join PurchaseReturnbody on PurchaseReturn.vbillcode=PurchaseReturnbody.vbillcode " +
                    "left join saledeliveryscanresult on PurchaseReturnbody.vbillcode=saledeliveryscanresult.vbillcode " +
                    "and PurchaseReturnbody.itempk=saledeliveryscanresult.vcooporderbcode_b where PurchaseReturnbody.uploadflag=? and PurchaseReturn.vbillcode" +
                    " like '%" + vbillcode + "%' and PurchaseReturnbody.warehouse"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});

        }


        //判断cursor中是否存在数据
        while (cursor.moveToNext()) {

            PurchaseReturnBean bean = new PurchaseReturnBean();

            bean.vbillcode = cursor.getString(cursor.getColumnIndex("vbillcode"));
            bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
            bean.setMaterialcode(cursor.getString(cursor.getColumnIndex("materialcode")));
            bean.setMaterialname(cursor.getString(cursor.getColumnIndex("materialname")));
            bean.setMaccode(cursor.getString(cursor.getColumnIndex("maccode")));
            bean.setNnum(cursor.getString(cursor.getColumnIndex("nnum")));
            bean.setProdcutcode(cursor.getString(cursor.getColumnIndex("prodcutcode")));
            bean.setXlh(cursor.getString(cursor.getColumnIndex("xlh")));
            bean.dr= cursor.getInt(cursor.getColumnIndex("dr"));


            if (DataHelper.queryTimePeriod(bean.vbillcode,start_temp,end_temp,getIntent().getIntExtra("type",-1),db3)) {
                list.add(bean);
            }

        }
        cursor.close();

        return list;
    }

    private void showDialogTwo() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_date, null);
        final DatePicker startTime = (DatePicker) view.findViewById(R.id.st);
        final DatePicker endTime = (DatePicker) view.findViewById(R.id.et);
        startTime.updateDate(startTime.getYear(), startTime.getMonth(), 01);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("选择时间");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String day_startTime,month_startTime,year_startTime;
                String day_endTime,month_endTime,year_endTime;
                year_startTime = String.valueOf(startTime.getYear());
                year_endTime = String.valueOf(endTime.getYear());
                if(startTime.getDayOfMonth()<10){
                    day_startTime = "0"+String.valueOf(startTime.getDayOfMonth());
                }else{
                    day_startTime = String.valueOf(startTime.getDayOfMonth());
                }
                if((startTime.getMonth())<10){
                    month_startTime = "0"+String.valueOf(startTime.getMonth() + 1);
                }else{
                    month_startTime = String.valueOf(startTime.getMonth() + 1);
                }
                if(endTime.getDayOfMonth()<10){
                    day_endTime = "0"+String.valueOf(endTime.getDayOfMonth());
                }else{
                    day_endTime = String.valueOf(endTime.getDayOfMonth());
                }
                if((endTime.getMonth())<10){
                    month_endTime = "0"+String.valueOf(endTime.getMonth() + 1);
                }else{
                    month_endTime = String.valueOf(endTime.getMonth() + 1);
                }
                String st = year_startTime+"-" + month_startTime+"-" + day_startTime;
                String et = year_endTime+"-" + month_endTime+"-" + day_endTime;
                SharedPreferences currentTimePeriod= getSharedPreferences("query_purchasereturn", 0);
                SharedPreferences.Editor editor1 = currentTimePeriod.edit();
                editor1.putString("current_account",st+" 至 "+et);
                editor1.putString("starttime",st+ " "+"00:00:00");
                editor1.putString("endtime",et+ " "+"23:59:59");
                editor1.commit();
                time.setText(st+" 至 "+et);
            }
        });
        builder.setNegativeButton("取消", null);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
        //自动弹出键盘问题解决
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        startTime.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        endTime.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
    }
    private void exportData( List<PurchaseReturnBean> exportList) {
        Log.i("exportList",new Gson().toJson(exportList));
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        File file=new File(sdCardDir+"/sunmi/export");
        if(!file.exists()){
            file.mkdir();
        }
        Date curDate =  new Date(System.currentTimeMillis());
        file=new File(sdCardDir+"/sunmi/export",formatter.format(curDate)+".txt");
        Toast.makeText(PurchaseReturn.this,"导出数据位置："+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            outputStream.write(("发货单号"+"\t"+ "单据日期"+"\t"+"物料编码"+"\t"+"物料名称"+"\t"+
                    "物料大类"+"\t"+"序列号"+"\t"+"条形码"+"\t").getBytes());
            for (int j = 0; j <exportList.size() ; j++) {
                if(exportList.get(j).getXlh()!=null ) {
                    outputStream.write("\r\n".getBytes());
                    outputStream.write((exportList.get(j).getVbillcode()+"\t"
                            +exportList.get(j).getDbilldate()+"\t"
                            +exportList.get(j).getMaterialcode()+"\t"
                            +exportList.get(j).getMaterialname()+"\t"
                            +exportList.get(j).getMaccode()+"\t"
                            +exportList.get(j).getXlh()+"\t"
                            +exportList.get(j).getProdcutcode()).getBytes());
                }

            }
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
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

    public ArrayList<PurchaseReturnBean> querySaleDelivery() {
        ArrayList<PurchaseReturnBean> list = new ArrayList<PurchaseReturnBean>();
        List<String> list_update = new ArrayList<String>();
       // String sql2 = "select " + "vbillcode" + "," + "dbilldate" + "," + "dr" + " from " + "SaleDelivery";//注意：这里有单引号
      //  Cursor cursor = db3.rawQuery(sql2, null);
        Cursor cursor = db3.rawQuery("select vbillcode,dbilldate,dr,flag from PurchaseReturn  order by dbilldate desc", null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                PurchaseReturnBean bean = new PurchaseReturnBean();
                bean.vbillcode = cursor.getString(cursor.getColumnIndex("vbillcode"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                if(!cursor.getString(cursor.getColumnIndex("flag")).equals("Y")) {
                    list.add(bean);
                }
            }

            cursor.close();
        }
        return list;
    }
    public ArrayList<PurchaseReturnBean> displayAllSaleDelivery() {
        ArrayList<PurchaseReturnBean> list = new ArrayList<PurchaseReturnBean>();
        Cursor cursor = db3.rawQuery("select vbillcode,dbilldate,dr from PurchaseReturn order by dbilldate desc", null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                PurchaseReturnBean bean = new PurchaseReturnBean();
                bean.vbillcode = cursor.getString(cursor.getColumnIndex("vbillcode"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private PurchaseReturnAdapter.MyClickListener mListener = new PurchaseReturnAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent(PurchaseReturn.this, PurchaseReturnDetail.class);
            intent.putExtra("current_sale_delivery_vbillcode", listAllPostition.get(position).getVbillcode());
            intent.putExtra("current_sale_delivery_dbilldate", listAllPostition.get(position).getDbilldate());
            intent.putExtra("type",7);
            startActivity(intent);

        }
    };
}
