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
import android.os.Looper;
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
import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.SaleDeliveryAdapter;
import com.example.shanggmiqr.bean.CommonSendNoPagetotalBean;
import com.example.shanggmiqr.bean.SaleDeliveryBean;
import com.example.shanggmiqr.bean.SaleDeliveryQuery;
import com.example.shanggmiqr.util.BaseConfig;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



public class SaleDelivery extends AppCompatActivity implements OnClickListener {
    private Button downloadDeliveryButton;
    private Button querySaleDeliveryButton;
    private Button displayallSaleDeliveryButton;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private Handler saleDeliveryHandler = null;
    private ListView tableListView;


    private ZLoadingDialog dialog;

    private String query_uploadflag="";

    private List<String> uploadflag;
    private TextView lst_downLoad_ts;
    private TextView time;
    private  Button buttonexport;
    private  String begintime;
    SharedPreferences latestDBTimeInfo;
    SaleDeliveryAdapter adapter;
    List<SaleDeliveryBean> saleDeliveryBeanList;

    int type;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_delivery_manage);
        helper3 = new MyDataBaseHelper(SaleDelivery.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。'
        type=getIntent().getIntExtra("type",-1);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(getIntent().getStringExtra("title"));
        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts);
        //显示最后一次的下载时间
        latestDBTimeInfo = getSharedPreferences("LatestSaleDeliveryTSInfo", 0);
        begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", iUrl.begintime);
        lst_downLoad_ts.setText("最后一次下载:"+begintime);

        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        dialog = new ZLoadingDialog(SaleDelivery.this);
        downloadDeliveryButton = (Button) findViewById(R.id.download_sale_delivery);
        downloadDeliveryButton.setOnClickListener(this);
        querySaleDeliveryButton = (Button) findViewById(R.id.query_sale_delivery);
        buttonexport=findViewById(R.id.b_export);
        buttonexport.setOnClickListener(this);
        querySaleDeliveryButton.setOnClickListener(this);
        displayallSaleDeliveryButton = (Button) findViewById(R.id.displayall_sale_delivery);
        //displayallSaleDeliveryButton.setVisibility(View.INVISIBLE);
        displayallSaleDeliveryButton.setOnClickListener(this);

        tableListView = (ListView) findViewById(R.id.list_sale_delivery);
        saleDeliveryBeanList = querySaleDelivery();
        Log.i("saleDeliveryBeanList-->",saleDeliveryBeanList.size()+"");


       adapter = new SaleDeliveryAdapter(SaleDelivery.this, saleDeliveryBeanList, mListener);
        tableListView.setAdapter(adapter);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.select(position);


            }
        });
        saleDeliveryHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(dialog!=null){
                    dialog.dismiss();
                }

                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(SaleDelivery.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        //插入UI表格数据

                       saleDeliveryBeanList = querySaleDelivery();

                        adapter = new SaleDeliveryAdapter(SaleDelivery.this,saleDeliveryBeanList, mListener);
                        tableListView.setAdapter(adapter);
                        Toast.makeText(SaleDelivery.this, "单据下载完成", Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:

                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(SaleDelivery.this, "单据下载异常，错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
        Intent intent = getIntent();
        String str = intent.getStringExtra("from_business_operation");
        if ("Y".equals(str)) {
            downloadDeliveryButton.performClick();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //返回之后重新下载

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_sale_delivery:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (DataHelper.isNetworkConnected(SaleDelivery.this)) {
                            try {
                                if (DataHelper.isWarehouseDBDownloaed(db3)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                           DataHelper.showDialog(dialog);
                                        }
                                    });
                                    //R07发货单
                                    String saleDeliveryData =DataHelper.downloadDatabase("1",SaleDelivery.this,type);
                                    if (null == saleDeliveryData) {
                                        dialog.dismiss();
                                        return;
                                    }

                                    Gson gson7 = new Gson();
                                    final SaleDeliveryQuery saleDeliveryQuery = gson7.fromJson(saleDeliveryData, SaleDeliveryQuery.class);

                                    if (saleDeliveryQuery.getPagetotal() == 1) {
                                        insertDownloadDataToDB(saleDeliveryQuery);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        saleDeliveryHandler.sendMessage(msg);
                                    } else if (saleDeliveryQuery.getPagetotal() < 1) {

                                     runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {

                                             dialog.dismiss();
                                             Toast.makeText(SaleDelivery.this, saleDeliveryQuery.getErrmsg(), Toast.LENGTH_LONG).show();

                                         }
                                     });

                                    } else {
                                        insertDownloadDataToDB(saleDeliveryQuery);
                                        for (int pagenum = 2; pagenum <= saleDeliveryQuery.getPagetotal(); pagenum++) {
                                            String saleDeliveryData2 = DataHelper.downloadDatabase( String.valueOf(pagenum),
                                                    SaleDelivery.this,type);
                                            SaleDeliveryQuery saleDeliveryQuery2 = new Gson().fromJson(saleDeliveryData2, SaleDeliveryQuery.class);
                                            insertDownloadDataToDB(saleDeliveryQuery2);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        saleDeliveryHandler.sendMessage(msg);
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DataHelper.putLatestdownloadbegintime(getIntent().getIntExtra("type",-1),SaleDelivery.this);
                                            begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", iUrl.begintime);
                                            lst_downLoad_ts.setText("最后一次下载:"+begintime);
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Toast.makeText(SaleDelivery.this, "请先到基础数据管理界面下载仓库信息", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Bundle bundle = new Bundle();
                                bundle.putString("Exception", e.toString());
                                 Message msg = new Message();
                                msg.what = 0x19;
                                msg.setData(bundle);
                                saleDeliveryHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0x10;
                            saleDeliveryHandler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.query_sale_delivery:
                popupQuery();
                break;
            case R.id.b_export:
                exportData(exportList);
                break;
            case R.id.displayall_sale_delivery:
                 saleDeliveryBeanList = displayAllSaleDelivery();

                adapter = new SaleDeliveryAdapter(SaleDelivery.this, saleDeliveryBeanList, mListener);
                tableListView.setAdapter(adapter);
                break;
        }
    }

    private void exportData( List<SaleDeliveryBean> exportList) {
        Log.i("exportList",new Gson().toJson(exportList));
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        File file=new File(sdCardDir+"/sunmi");
        if(!file.exists()){
            file.mkdir();
        }
        Date curDate =  new Date(System.currentTimeMillis());
        file=new File(sdCardDir+"/sunmi",formatter.format(curDate)+".txt");
        Toast.makeText(SaleDelivery.this,"导出数据位置："+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
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
                            +exportList.get(j).getMatrcode()+"\t"
                            +exportList.get(j).getMatrname()+"\t"
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void insertDownloadDataToDB(SaleDeliveryQuery saleDeliveryQuery) {

        List<SaleDeliveryQuery.DataBean> saleDeliveryBeanList = saleDeliveryQuery.getData();


        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("日志yyyy年MM月dd日HH时mm分ss秒");
        File file=new File(sdCardDir+"/sunmi");
        if(!file.exists()){
            file.mkdir();
        }
        Date curDate =  new Date(System.currentTimeMillis());
        file=new File(sdCardDir+"/sunmi",formatter.format(curDate)+".txt");
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            for (SaleDeliveryQuery.DataBean ob : saleDeliveryBeanList) {

                outputStream.write("\r\n".getBytes());
                outputStream.write((ob.getVbillcode()+"/"+ob.getDr()).getBytes());
                //0:新增-正常下载保持 1：删除，删除对应单据 2：修改，先删除对应单据再保持
                switch (ob.getDr()){
                    case 0:
                        setSaleDeliveryData(ob);
                        setSaleDeliverybodyData(ob);
                        break;
                    case 1:
                        db3.delete("SaleDelivery", "vbillcode=?", new String[]{ob.getVbillcode()});
                        db3.delete("SaleDeliveryBody", "vbillcode=?", new String[]{ob.getVbillcode()});
                        db3.delete("SaleDeliveryScanResult", "vbillcode=?", new String[]{ob.getVbillcode()});
                        break;
                    case 2:
                        db3.delete("SaleDelivery", "vbillcode=?", new String[]{ob.getVbillcode()});
                        db3.delete("SaleDeliveryBody", "vbillcode=?", new String[]{ob.getVbillcode()});
                        db3.delete("SaleDeliveryScanResult", "vbillcode=?", new String[]{ob.getVbillcode()});
                        setSaleDeliveryData(ob);
                        setSaleDeliverybodyData(ob);
                        break;

                }

            }

            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private void setSaleDeliverybodyData(SaleDeliveryQuery.DataBean ob) {
        List<SaleDeliveryQuery.DataBean.BodysBean> saleDeliveryDatabodysList = ob.getBody();

        for (SaleDeliveryQuery.DataBean.BodysBean obb : saleDeliveryDatabodysList) {
            String cwarename = getCwarename(obb.getCwarehousecode());
            String orginal_cwarename = existOriginalCwarename(obb.getCwarehousecode());
            int scannum = DataHelper.queryScanResultcount(db3,ob.getVbillcode(),obb.getVcooporderbcode_b(), obb.getMatrcode(),type);

            //这里应该执行的是插入第二个表的操作
            ContentValues valuesInner = new ContentValues();
            valuesInner.put("vbillcode", ob.getVbillcode());
            valuesInner.put("vcooporderbcode_b", obb.getVcooporderbcode_b());
            valuesInner.put("matrcode",obb.getMatrcode());
            valuesInner.put("matrname", obb.getMatrname());
            valuesInner.put("maccode", obb.getMaccode());
            valuesInner.put("nnum", obb.getNnum());
            valuesInner.put("scannum", scannum);
            valuesInner.put("rackcode", obb.getRackcode());
            valuesInner.put("customer", obb.getCustomer());
            valuesInner.put("cwarehousecode", obb.getCwarehousecode());
            valuesInner.put("cwarename", cwarename);
            valuesInner.put("orginal_cwarename", orginal_cwarename);
            //N代表尚未上传
            valuesInner.put("uploadflag", "N");
            valuesInner.put("issn",obb.getIssn());

            db3.insert("SaleDeliveryBody", null, valuesInner);
            valuesInner.clear();
        }

    }

    private void setSaleDeliveryData(SaleDeliveryQuery.DataBean ob ) {

        //使用 ContentValues 来对要添加的数据进行组装
        ContentValues values = new ContentValues();
        values.put("vtrantypecode", ob.getVtrantypecode());
        values.put("unitcode", ob.getUnitcode());
        values.put("busitypecode", ob.getBusitypecode());
        values.put("vbillcode", ob.getVbillcode());
        values.put("dbilldate", ob.getDbilldate());
        values.put("deptcode", ob.getDeptcode());
        values.put("Pupsndoccode", ob.getPupsndoccode());
        values.put("Transporttypecode", ob.getTransporttypecode());
        values.put("billmakercode", ob.getBillmakercode());
        values.put("country", ob.getCountry());
        values.put("dr", ob.getDr());
        values.put("flag", "N");
        values.put("vmemo",ob.getVmemo());
        values.put("type",getIntent().getIntExtra("type",-1));


        // 插入第一条数据
        db3.insert("SaleDelivery", null, values);
        values.clear();
    }






    private String existOriginalCwarename(String cwarehousecode) {
        String flag;
        if (cwarehousecode.equals("")){
            flag ="N";
        }else{
            flag ="Y";
        }
        return flag;
    }

    private String getCwarename(String cwarehousecode) {
        Cursor cursor = db3.rawQuery("select name from Warehouse where code=?",
                new String[]{cwarehousecode});
        String cwarename=null;
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                cwarename = cursor.getString(cursor.getColumnIndex("name"));
            }
            cursor.close();
        }else{
            cwarename = "";
        }
        return cwarename;
    }






    String query_cwarename;
    private void popupQuery() {
        List<String> listWarehouse;

        LayoutInflater layoutInflater = LayoutInflater.from(SaleDelivery.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditText = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinner = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinner = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        final Button showdailogTwo = (Button)  textEntryView.findViewById(R.id.showdailogTwo);
        time = (TextView)  textEntryView.findViewById(R.id.timeshow_saledelivery);

        String tempperiod =DataHelper.getQueryTime(SaleDelivery.this,type);
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
                SaleDelivery.this, android.R.layout.simple_spinner_item, listWarehouse);
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
                SaleDelivery.this, android.R.layout.simple_spinner_item, uploadflag);
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

        AlertDialog.Builder ad1 = new AlertDialog.Builder(SaleDelivery.this);
        ad1.setTitle("出入查询条件:");
        ad1.setView(textEntryView);
        time.setText(tempperiod);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                String temp=codeNumEditText.getText().toString();
                exportList= queryexport(temp,query_cwarename,query_uploadflag);
                saleDeliveryBeanList=new ArrayList<>();
                saleDeliveryBeanList.addAll(removeDuplicate(exportList));
                adapter = new SaleDeliveryAdapter(SaleDelivery.this, saleDeliveryBeanList, mListener);
                tableListView.setAdapter(adapter);



            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }
    List<SaleDeliveryBean> exportList;

    private    List<SaleDeliveryBean>  removeDuplicate(List<SaleDeliveryBean> list)  {
        List<SaleDeliveryBean>  beanList=new ArrayList<>();
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
                SharedPreferences currentTimePeriod= getSharedPreferences("query_saledelivery", 0);
                SharedPreferences.Editor editor1 = currentTimePeriod.edit();
                editor1.putString("current_account",st+" 至 "+et);
                editor1.putString("starttime",st+ " "+"00:00:01");
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


    public ArrayList<SaleDeliveryBean> queryexport(String vbillcode,String current_cwarename,String query_uploadflag) {
        ArrayList<SaleDeliveryBean> list = new ArrayList<SaleDeliveryBean>();
        SharedPreferences currentTimePeriod= getSharedPreferences("query_saledelivery", 0);
        String start_temp = currentTimePeriod.getString("starttime", iUrl.begintime);
        String end_temp = currentTimePeriod.getString("endtime", Utils.getDefaultEndTime());
        Cursor cursor=null;
         if(query_uploadflag.equals("ALL")){
             cursor = db3.rawQuery("select saledelivery.vbillcode, saledelivery.dbilldate,saledeliverybody.matrcode,saledelivery.dr," +
                     "saledeliverybody.matrname,saledeliverybody.maccode,saledeliverybody.nnum, saledeliveryscanresult.prodcutcode," +
                     "saledeliveryscanresult.xlh" + " from saledelivery inner join saledeliverybody on saledelivery.vbillcode=saledeliverybody.vbillcode " +
                     "left join saledeliveryscanresult on saledeliverybody.vbillcode=saledeliveryscanresult.vbillcode " +
                     "and saledeliverybody.vcooporderbcode_b=saledeliveryscanresult.vcooporderbcode_b where saledelivery.vbillcode" +
                     " like '%" + vbillcode + "%' and saledeliverybody.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", null);

         }else {
             cursor = db3.rawQuery("select saledelivery.vbillcode, saledelivery.dbilldate,saledeliverybody.matrcode,saledelivery.dr," +
                     "saledeliverybody.matrname,saledeliverybody.maccode,saledeliverybody.nnum, saledeliveryscanresult.prodcutcode," +
                     "saledeliveryscanresult.xlh" + " from saledelivery inner join saledeliverybody on saledelivery.vbillcode=saledeliverybody.vbillcode " +
                     "left join saledeliveryscanresult on saledeliverybody.vbillcode=saledeliveryscanresult.vbillcode " +
                     "and saledeliverybody.vcooporderbcode_b=saledeliveryscanresult.vcooporderbcode_b where saledeliverybody.uploadflag=? and saledelivery.vbillcode" +
                     " like '%" + vbillcode + "%' and saledeliverybody.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});

         }


            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {

                SaleDeliveryBean bean = new SaleDeliveryBean();

                bean.vbillcode = cursor.getString(cursor.getColumnIndex("vbillcode"));

                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.setMatrcode(cursor.getString(cursor.getColumnIndex("matrcode")));
                bean.setMatrname(cursor.getString(cursor.getColumnIndex("matrname")));
                bean.setMaccode(cursor.getString(cursor.getColumnIndex("maccode")));
                bean.setNnum(cursor.getString(cursor.getColumnIndex("nnum")));
                bean.setProdcutcode(cursor.getString(cursor.getColumnIndex("prodcutcode")));
                bean.setXlh(cursor.getString(cursor.getColumnIndex("xlh")));
                bean.dr= cursor.getInt(cursor.getColumnIndex("dr"));


                if (DataHelper.queryTimePeriod(bean.vbillcode,start_temp,end_temp,type,db3)) {
                    list.add(bean);
                }

            }
            cursor.close();

        return list;
    }





    public ArrayList<SaleDeliveryBean> querySaleDelivery() {
        ArrayList<SaleDeliveryBean> list = new ArrayList<SaleDeliveryBean>();


        Cursor cursor = db3.rawQuery("select vbillcode,dbilldate,dr from SaleDelivery where flag=? and type=? order by dbilldate desc",
                new String[]{"N",getIntent().getIntExtra("type",-1)+""});


            while (cursor.moveToNext()) {
                SaleDeliveryBean bean = new SaleDeliveryBean();
                bean.vbillcode = cursor.getString(cursor.getColumnIndex("vbillcode"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                list.add(bean);

            }

        cursor.close();

        return list;
    }
    public ArrayList<SaleDeliveryBean> displayAllSaleDelivery() {
        ArrayList<SaleDeliveryBean> list = new ArrayList<SaleDeliveryBean>();
        Cursor cursor = db3.rawQuery("select vbillcode,dbilldate,dr from SaleDelivery order by dbilldate desc", null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                SaleDeliveryBean bean = new SaleDeliveryBean();
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
    private SaleDeliveryAdapter.MyClickListener mListener = new SaleDeliveryAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {

            Intent intent = new Intent(SaleDelivery.this, SaleDeliveryDetail.class);
            intent.putExtra("current_sale_delivery_vbillcode", saleDeliveryBeanList.get(position).getVbillcode());
            intent.putExtra("current_sale_delivery_dbilldate", saleDeliveryBeanList.get(position).getDbilldate());
            intent.putExtra("type",type);
            intent.putExtra("title",getIntent().getStringExtra("title"));

            startActivity(intent);

        }
    };
}
