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
import com.example.shanggmiqr.adapter.PurchaseReturnAdapter;
import com.example.shanggmiqr.bean.PurchaseReturnBean;
import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.ProductEntryAdapter;
import com.example.shanggmiqr.bean.CommonSendNoPagetotalBean;
import com.example.shanggmiqr.bean.ProductEntryBean;
import com.example.shanggmiqr.bean.ProductEntryQuery;
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
 * Created by weiyt.jiang on 2018/8/9.
 */

public class ProductEntry extends AppCompatActivity implements OnClickListener {
    private String saleDelivDataResp;
    private Button downloadButton;
    private Button queryProductEntryButton;
    private Button displayallProductEntryButton;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private Handler productEntryHandler = null;
    private ListView tableListView;
    private List<ProductEntryBean> listAllPostition;
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
    ProductEntryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_entry_manage);
        helper3 = new MyDataBaseHelper(ProductEntry.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts_product_entry);
        //显示最后一次的下载时间
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestProductEntryTSInfo", 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "");
        lst_downLoad_ts.setText("最后一次下载:"+begintime);

        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        dialog = new ZLoadingDialog(ProductEntry.this);
        downloadButton = (Button) findViewById(R.id.download_product_entry);
        downloadButton.setOnClickListener(this);
        queryProductEntryButton = (Button) findViewById(R.id.query_product_entry);
        queryProductEntryButton.setOnClickListener(this);
        displayallProductEntryButton = (Button) findViewById(R.id.displayall_product_entry);
        //displayallSaleDeliveryButton.setVisibility(View.INVISIBLE);
        displayallProductEntryButton.setOnClickListener(this);

        tableListView = (ListView) findViewById(R.id.list_product_entry);

        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                select(position);

            }
        });
        productEntryHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(ProductEntry.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        //插入UI表格数据
                        dialog.dismiss();
                        initAdapter();
                        Toast.makeText(ProductEntry.this, "产成品入库单下载完成", Toast.LENGTH_LONG).show();
                        break;
                    case 0x18:
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(ProductEntry.this, s, Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(ProductEntry.this, "产成品入库单下载异常，错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        Intent intent = getIntent();
        String str = intent.getStringExtra("from_business_operation");
        if ("Y".equals(str)) {
            downloadButton.performClick();
        }
        Button buttonExport=findViewById(R.id.b_export);
        buttonExport.setOnClickListener(this);
    }

    private void select(int position) {
        adapter.select(position);
        ProductEntryBean saleDelivery1Bean = (ProductEntryBean) adapter.getItem(position);
        chosen_line_vbillcode = saleDelivery1Bean.getBillcode();
        chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAdapter();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_product_entry:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkConnected(ProductEntry.this)) {
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
                                    String productEntryData = DataHelper.downloadDatabase( "1",ProductEntry.this,3);

                                    if (null == productEntryData) {
                                        dialog.dismiss();
                                        return;
                                    }

                                    final ProductEntryQuery productEntryQuery = new Gson().fromJson(productEntryData, ProductEntryQuery.class);
                                    int pagetotal = Integer.parseInt(productEntryQuery.getPagetotal());
                                    if (pagetotal == 1) {
                                        insertDownloadDataToDB(productEntryQuery);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        productEntryHandler.sendMessage(msg);
                                    } else if (pagetotal < 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(ProductEntry.this,productEntryQuery.getErrmsg(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        insertDownloadDataToDB(productEntryQuery);
                                        for (int pagenum = 2; pagenum <= pagetotal; pagenum++) {
                                            String saleDeliveryData2 = DataHelper.downloadDatabase(String.valueOf(pagenum),ProductEntry.this,3);
                                            ProductEntryQuery saleDeliveryQuery2 =new Gson().fromJson(saleDeliveryData2, ProductEntryQuery.class);
                                            insertDownloadDataToDB(saleDeliveryQuery2);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        productEntryHandler.sendMessage(msg);
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DataHelper.putLatestdownloadbegintime(getIntent().getIntExtra("type",-1),ProductEntry.this);
                                            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestProductEntryTSInfo", 0);
                                            String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "2018-09-01 00:00:01");
                                            lst_downLoad_ts.setText("最后一次下载:"+begintime);
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Toast.makeText(ProductEntry.this, "请先到基础数据管理界面下载仓库信息", Toast.LENGTH_LONG).show();
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
                                productEntryHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0x10;
                            productEntryHandler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.query_product_entry:
                popupQuery();
                break;
            case R.id.displayall_product_entry:
                initAdapter();
                break;
            case R.id.b_export:
                exportData(exportList);
                break;
        }
    }
    boolean isExport=false;
    private void initAdapter() {

        listAllPostition =  displayAllProductEntry();
        adapter = new ProductEntryAdapter(ProductEntry.this, listAllPostition, mListener);
        tableListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if(listAllPostition.size()!=0){
            select(0);
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

    private void insertDownloadDataToDB(ProductEntryQuery productEntryQuery) {

        List<ProductEntryQuery.DataBean> saleDeliveryBeanList = productEntryQuery.getData();
        for (ProductEntryQuery.DataBean ob : saleDeliveryBeanList) {
            Log.i("bean-->",new Gson().toJson(ob));
            String billcode = ob.getBillcode();


            //0:新增-正常下载保持 1：删除，删除对应单据 2：修改，先删除对应单据再保持
            switch (Integer.parseInt(ob.getDr())){
                case 0:
                    insertDb(ob);
                    break;
                case 1:
                    db3.delete("ProductEntry", "billcode=?", new String[]{billcode});
                    db3.delete("ProductEntryBody", "billcode=?", new String[]{billcode});
                    db3.delete("ProductEntryScanResult", "billcode=?", new String[]{billcode});
                    break;
                case 2:
                    db3.delete("ProductEntry", "billcode=?", new String[]{billcode});
                    db3.delete("ProductEntryBody", "billcode=?", new String[]{billcode});
                    db3.delete("ProductEntryScanResult", "billcode=?", new String[]{billcode});
                    insertDb(ob);
                    break;
            }


        }
    }

    private void insertDb(ProductEntryQuery.DataBean ob) {
        String billcode = ob.getBillcode();
        String dbilldate = ob.getDbilldate();
        String dr = ob.getDr();
        String ts = ob.getTs();
        String cwarecode = ob.getCwarecode();
        String cwarename = ob.getCwarename();
        String org = ob.getOrg();
        String totalnum = ob.getTotalnum();
        String headpk = ob.getHeadpk();
        List<ProductEntryQuery.DataBean.BodyBean> saleDeliveryDatabodysList = ob.getBody();
        //使用 ContentValues 来对要添加的数据进行组装
        ContentValues values = new ContentValues();
        Boolean isY=false;
        Boolean isPY=false;
        for (ProductEntryQuery.DataBean.BodyBean obb : saleDeliveryDatabodysList) {
            String itempk = obb.getItempk();
            String materialcode = obb.getMaterialcode();
            String nnum = obb.getNnum();
            String ysnum = obb.getYsnum();
            String scannum = countScannedQRCode(billcode, materialcode,itempk);
            //这里应该执行的是插入第二个表的操作
            ContentValues valuesInner = new ContentValues();
            valuesInner.put("billcode", billcode);
            valuesInner.put("itempk", itempk);
            valuesInner.put("materialcode", materialcode);
            valuesInner.put("nnum", nnum);
            valuesInner.put("ysnum", ysnum);
            valuesInner.put("scannum", scannum);
            valuesInner.put("maccode",obb.getMaccode());
            //N代表尚未上传
            if(Integer.parseInt(nnum)!=0){

               if(Integer.parseInt(nnum)==Integer.parseInt(ysnum)){
                   isY=true;
                   valuesInner.put("uploadflag", "Y");
               } else {
                   isPY=true;
                   valuesInner.put("uploadflag", "PY");
               }
            }else {
                valuesInner.put("uploadflag", "N");
            }

            db3.insert("ProductEntryBody", null, valuesInner);
            valuesInner.clear();
        }
        values.put("billcode", billcode);
        values.put("dbilldate", dbilldate);
        values.put("dr", dr);
        values.put("ts", ts);
        values.put("cwarecode", cwarecode);
        values.put("cwarename", cwarename);
        values.put("org", org);
        values.put("totalnum", totalnum);
        values.put("headpk", headpk);
        if(isY==true && isPY==false){
            values.put("flag", "Y");
        }else if(isPY){
            values.put("flag", "PY");
        }else {
            values.put("flag", "N");
        }



        // 插入第一条数据
        db3.insert("ProductEntry", null, values);
        values.clear();
    }





    private String countScannedQRCode(String billcode, String materialcode,String itempk) {
        String count = "0";
        Cursor cursor2 = db3.rawQuery("select prodcutcode from ProductEntryScanResult where billcode=? and materialcode=? and itempk=? ", new String[]{billcode, materialcode,itempk});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
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
                String st=Utils.parseDate(startTime.getYear()+"-"+(startTime.getMonth()+1)+"-"+startTime.getDayOfMonth());
                String et =  Utils.parseDate(endTime.getYear()+"-"+(endTime.getMonth()+1)+"-"+endTime.getDayOfMonth());
                SharedPreferences currentTimePeriod= getSharedPreferences("query_productentry", 0);
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



    /**
     * webservice查询下载
     */


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


    public ArrayList<ProductEntryBean> displayAllProductEntry() {
        isExport=false;
        ArrayList<ProductEntryBean> list = new ArrayList<ProductEntryBean>();
        Cursor cursor = db3.rawQuery("select billcode,dbilldate,dr,cwarename from ProductEntry where flag=? order by dbilldate desc", new String[]{"N"});

            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                ProductEntryBean bean = new ProductEntryBean();
                bean.billcode = cursor.getString(cursor.getColumnIndex("billcode"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                bean.setFlag("N");
                list.add(bean);
            }
            cursor.close();

        return list;
    }
    private void popupQuery() {
        List<String> listWarehouse;

        LayoutInflater layoutInflater = LayoutInflater.from(ProductEntry.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditText = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinner = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinner = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        final Button showdailogTwo = (Button)  textEntryView.findViewById(R.id.showdailogTwo);
        time = (TextView)  textEntryView.findViewById(R.id.timeshow_saledelivery);

        String tempperiod =DataHelper.getQueryTime(ProductEntry.this,getIntent().getIntExtra("type",-1));
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
                ProductEntry.this, android.R.layout.simple_spinner_item, listWarehouse);
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
                ProductEntry.this, android.R.layout.simple_spinner_item, uploadflag);
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

        AlertDialog.Builder ad1 = new AlertDialog.Builder(ProductEntry.this);
        ad1.setTitle("出入查询条件:");
        ad1.setView(textEntryView);
        time.setText(tempperiod);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                String temp=codeNumEditText.getText().toString();
                exportList= queryexport(temp,query_cwarename,query_uploadflag);
                saleDeliveryBeanList=new ArrayList<>();
                saleDeliveryBeanList.addAll(removeDuplicate(exportList));
                //PurchaseReturnAdapter adapter=new PurchaseReturnAdapter(ProductEntry.this,saleDeliveryBeanList,mListener);
                ProductEntryAdapter adapter=new ProductEntryAdapter(ProductEntry.this,saleDeliveryBeanList,mListener);
                tableListView.setAdapter(adapter);



            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }

    List<ProductEntryBean> exportList;
    List<ProductEntryBean> saleDeliveryBeanList;
    private    List<ProductEntryBean>  removeDuplicate(List<ProductEntryBean> list)  {
        List<ProductEntryBean>  beanList=new ArrayList<>();
        beanList.addAll(list);

        for  ( int  i  =   0 ; i  <  beanList.size()  -   1 ; i ++ )  {

            for  ( int  j  =  beanList.size()  -   1 ; j  >  i; j -- )  {

                if  (beanList.get(j).getBillcode().equals(beanList.get(i).getBillcode()))  {
                    beanList.remove(j);
                }
            }
        }
        return beanList;
    }
    private ArrayList<ProductEntryBean> queryexport(String vbillcode,String current_cwarename,String query_uploadflag) {
        isExport=true;
        ArrayList<ProductEntryBean> list = new ArrayList<>();
        SharedPreferences currentTimePeriod= getSharedPreferences("query_productentry", 0);
        String start_temp = currentTimePeriod.getString("starttime", iUrl.begintime);
        String end_temp = currentTimePeriod.getString("endtime", Utils.getDefaultEndTime());
        Cursor cursor=null;
        if(query_uploadflag.equals("ALL")){
            cursor = db3.rawQuery("select ProductEntry.billcode,ProductEntry.flag, ProductEntry.dbilldate,ProductEntrybody.materialcode,ProductEntry.dr," +
                    "ProductEntrybody.maccode,ProductEntrybody.nnum,ProductEntryscanresult.prodcutcode,ProductEntry.cwarename," +
                    "ProductEntryscanresult.xlh" + " from ProductEntry inner join ProductEntrybody on ProductEntry.billcode=ProductEntrybody.billcode " +
                    "left join ProductEntryscanresult on ProductEntrybody.billcode=ProductEntryscanresult.billcode " +
                    "and ProductEntrybody.itempk=ProductEntryscanresult.itempk where ProductEntry.billcode" +
                    " like '%" + vbillcode + "%' and ProductEntry.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", null);

        }else {
            cursor = db3.rawQuery("select ProductEntry.billcode,ProductEntry.flag, ProductEntry.dbilldate,ProductEntrybody.materialcode,ProductEntry.dr," +
                    "ProductEntrybody.maccode,ProductEntrybody.nnum,ProductEntryScanResult.prodcutcode,ProductEntry.cwarename," +
                    "ProductEntryScanResult.xlh" + " from ProductEntry inner join ProductEntrybody on ProductEntry.billcode=ProductEntrybody.billcode " +
                    "left join ProductEntryscanresult on ProductEntrybody.billcode=ProductEntryScanResult.billcode " +
                    "and ProductEntrybody.itempk=ProductEntryScanResult.itempk where ProductEntry.flag=? and ProductEntry.billcode" +
                    " like '%" + vbillcode + "%' and ProductEntry.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});

        }


        //判断cursor中是否存在数据
        while (cursor.moveToNext()) {

            ProductEntryBean bean = new ProductEntryBean();

            bean.billcode = cursor.getString(cursor.getColumnIndex("billcode"));
            bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
            bean.setMaterialcode(cursor.getString(cursor.getColumnIndex("materialcode")));
            bean.setCwarename(cursor.getString(cursor.getColumnIndex("cwarename")));
        //    bean.setMaterialname(cursor.getString(cursor.getColumnIndex("materialname")));
            bean.setMaccode(cursor.getString(cursor.getColumnIndex("maccode")));
            bean.setNnum(cursor.getString(cursor.getColumnIndex("nnum")));
            bean.setProdcutcode(cursor.getString(cursor.getColumnIndex("prodcutcode")));
            bean.setXlh(cursor.getString(cursor.getColumnIndex("xlh")));
            bean.dr= cursor.getInt(cursor.getColumnIndex("dr"));
            bean.setFlag(cursor.getString(cursor.getColumnIndex("flag")));


            if (DataHelper.queryTimePeriod(bean.billcode,start_temp,end_temp,getIntent().getIntExtra("type",-1),db3)) {
                list.add(bean);
            }

        }
        cursor.close();

        return list;
    }


    private void exportData( List<ProductEntryBean> exportList) {
        Log.i("exportList",new Gson().toJson(exportList));
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        File file=new File(sdCardDir+"/sunmi/export");
        if(!file.exists()){
            file.mkdir();
        }
        Date curDate =  new Date(System.currentTimeMillis());
        file=new File(sdCardDir+"/sunmi/export",formatter.format(curDate)+".txt");
        Toast.makeText(ProductEntry.this,"导出数据位置："+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);

            outputStream.write(("发货单号"+"\t"+ "单据日期"+"\t"+"物料编码"+"\t"+"物料名称"+"\t"+
                    "物料大类"+"\t"+"序列号"+"\t"+"条形码"+"\t").getBytes());
            for (int j = 0; j <exportList.size() ; j++) {
                if(exportList.get(j).getXlh()!=null ) {
                    outputStream.write("\r\n".getBytes());
                    outputStream.write((exportList.get(j).getBillcode()+"\t"
                            +exportList.get(j).getDbilldate()+"\t"
                            +exportList.get(j).getMaterialcode()+"\t"
                            +"null"+"\t"
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

    /**
     * 实现类，响应按钮点击事件
     */
    private ProductEntryAdapter.MyClickListener mListener = new ProductEntryAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            select(position);
            Intent intent = new Intent(ProductEntry.this, ProductEntryDetail.class);
            if(isExport){
                intent.putExtra("current_sale_delivery_vbillcode",saleDeliveryBeanList.get(position).getBillcode());
                intent.putExtra("current_sale_delivery_dbilldate", saleDeliveryBeanList.get(position).getDbilldate());
                intent.putExtra("flag",saleDeliveryBeanList.get(position).getFlag());

            }else {
                intent.putExtra("current_sale_delivery_vbillcode", listAllPostition.get(position).getBillcode());
                intent.putExtra("current_sale_delivery_dbilldate", listAllPostition.get(position).getDbilldate());
                intent.putExtra("flag",listAllPostition.get(position).getFlag());
            }

            startActivity(intent);

        }
    };
}
