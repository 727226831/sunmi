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
import android.provider.ContactsContract;
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
import com.example.shanggmiqr.adapter.ProductEntryAdapter;
import com.example.shanggmiqr.bean.ProductEntryBean;
import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.AllocateTransferAdapter;
import com.example.shanggmiqr.bean.AllocateTransferBean;
import com.example.shanggmiqr.bean.AllocateTransferQuery;
import com.example.shanggmiqr.bean.CommonSendAllocateBean;
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

public class AllocateTransfer extends AppCompatActivity implements OnClickListener {
    private String saleDelivDataResp;
    private Button downloadButton;
    private Button queryProductEntryButton;
    private Button displayallProductEntryButton;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private Handler allocateTransferHandler = null;
    private ListView tableListView;
    private List<AllocateTransferBean> listAllPostition;
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
        setContentView(R.layout.allocate_transfer_manage);
        helper3 = new MyDataBaseHelper(AllocateTransfer.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts_allocate_transfer);
        //显示最后一次的下载时间
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestAllocateTransferTSInfo", 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "2019-02-02 00:00:01");
        lst_downLoad_ts.setText("最后一次下载:"+begintime);

        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        dialog = new ZLoadingDialog(AllocateTransfer.this);
        downloadButton = (Button) findViewById(R.id.download_allocate_transfer);
        downloadButton.setOnClickListener(this);
        queryProductEntryButton = (Button) findViewById(R.id.query_allocate_transfer);
        queryProductEntryButton.setOnClickListener(this);
        displayallProductEntryButton = (Button) findViewById(R.id.displayall_allocate_transfer);
        displayallProductEntryButton.setOnClickListener(this);
        buttonExport=findViewById(R.id.b_export);
        buttonExport.setOnClickListener(this);
        tableListView = (ListView) findViewById(R.id.list_allocate_transfer);
        List<AllocateTransferBean> list = queryAllocateTransfer();
        listAllPostition = list;
        final AllocateTransferAdapter adapter1 = new AllocateTransferAdapter(AllocateTransfer.this, list, mListener);
        tableListView.setAdapter(adapter1);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.select(position);
                AllocateTransferBean saleDelivery1Bean = (AllocateTransferBean) adapter1.getItem(position);
                chosen_line_vbillcode = saleDelivery1Bean.getBillno();
                chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();

            }
        });
        allocateTransferHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(AllocateTransfer.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        //插入UI表格数据
                        dialog.dismiss();
                        List<AllocateTransferBean> list = queryAllocateTransfer();
                        listAllPostition = list;
                        final AllocateTransferAdapter adapter = new AllocateTransferAdapter(AllocateTransfer.this, list, mListener);
                        tableListView.setAdapter(adapter);
                        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.select(position);
                                AllocateTransferBean saleDelivery1Bean = (AllocateTransferBean) adapter.getItem(position);
                                chosen_line_vbillcode = saleDelivery1Bean.getBillno();
                                chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();
                            }
                        });


                        break;
                    case 0x18:
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(AllocateTransfer.this, s, Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(AllocateTransfer.this, "调拨出库单下载异常，错误："+exception, Toast.LENGTH_LONG).show();
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
    }



    @Override
    protected void onStart() {
        super.onStart();
        allocateTransferHandler.sendEmptyMessage(0x11);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_allocate_transfer:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkConnected(AllocateTransfer.this)) {
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
                                                    .show();
                                        }
                                    });
                                    //R07发货单
                                    String allocateTransferData = DataHelper.downloadDatabase("1",AllocateTransfer.this,5);
                                    if (null == allocateTransferData) {
                                        dialog.dismiss();
                                        return;
                                    }

                                    Gson gson7 = new Gson();
                                    final AllocateTransferQuery allocateTransferQuery = gson7.fromJson(allocateTransferData, AllocateTransferQuery.class);
                                    int pagetotal = Integer.parseInt(allocateTransferQuery.getPagetotal());
                                    if (pagetotal == 1) {
                                        insertDownloadDataToDB(allocateTransferQuery);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        allocateTransferHandler.sendMessage(msg);
                                    } else if (pagetotal < 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(AllocateTransfer.this, allocateTransferQuery.getErrmsg(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        insertDownloadDataToDB(allocateTransferQuery);
                                        for (int pagenum = 2; pagenum <= pagetotal; pagenum++) {
                                            String saleDeliveryData2 =  DataHelper.downloadDatabase(pagenum+"",AllocateTransfer.this,5);
                                            AllocateTransferQuery saleDeliveryQuery2 = gson7.fromJson(saleDeliveryData2, AllocateTransferQuery.class);
                                            insertDownloadDataToDB(saleDeliveryQuery2);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        allocateTransferHandler.sendMessage(msg);
                                    }

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DataHelper.putLatestdownloadbegintime(getIntent().getIntExtra("type",-1),AllocateTransfer.this);
                                            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestAllocateTransferTSInfo", 0);
                                            String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "2019-02-02 00:00:01");
                                            lst_downLoad_ts.setText("最后一次下载:"+begintime);
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Toast.makeText(AllocateTransfer.this, "请先到基础数据管理界面下载仓库信息", Toast.LENGTH_LONG).show();
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
                                allocateTransferHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0x10;
                            allocateTransferHandler.sendMessage(msg);
                        }
                    }
                }).start();
                try {

                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.query_allocate_transfer:
                popupQuery();

                break;
            case R.id.displayall_allocate_transfer:
                List<AllocateTransferBean> list = displayAllProductEntry();
                listAllPostition = list;
                final AllocateTransferAdapter adapter = new AllocateTransferAdapter(AllocateTransfer.this, list, mListener);
                tableListView.setAdapter(adapter);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.select(position);

                        AllocateTransferBean saleDelivery1Bean = listAllPostition.get(position);
                        chosen_line_vbillcode = saleDelivery1Bean.getBillno();
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

    private void insertDownloadDataToDB(AllocateTransferQuery productEntryQuery) {

        List<AllocateTransferQuery.DataBean> saleDeliveryBeanList = productEntryQuery.getData();
        for (AllocateTransferQuery.DataBean ob : saleDeliveryBeanList) {

            String billno = ob.getBillno();
            String dr = ob.getDr();

            //0:新增-正常下载保持 1：删除，删除对应单据 2：修改，先删除对应单据再保持
            switch (Integer.parseInt(ob.getDr())){
                case 0:
                    insertDb(ob);
                    break;
                case 1:
                    db3.delete("AllocateTransfer", "billno=?", new String[]{billno});
                    db3.delete("AllocateTransferBody", "billno=?", new String[]{billno});
                    db3.delete("AllocateTransferScanResult", "billno=?", new String[]{billno});
                    break;
                case 2:
                    db3.delete("AllocateTransfer", "billno=?", new String[]{billno});
                    db3.delete("AllocateTransferBody", "billno=?", new String[]{billno});
                    db3.delete("AllocateTransferScanResult", "billno=?", new String[]{billno});
                    insertDb(ob);
                    break;
            }



        }
    }

    private void insertDb(AllocateTransferQuery.DataBean ob) {
        String billno = ob.getBillno();
        String dbilldate = ob.getDbilldate();
        String dr = ob.getDr();
        String ts = ob.getTs();
        String num = ob.getNum();
        String cunitcode = ob.getCunitcode();
        String runitcode = ob.getRunitcode();
        String org = ob.getOrg();
        String headpk =ob.getHeadpk();
        List<AllocateTransferQuery.DataBean.BodyBean> saleDeliveryDatabodysList = ob.getBody();
        //使用 ContentValues 来对要添加的数据进行组装
        ContentValues values = new ContentValues();
        for (AllocateTransferQuery.DataBean.BodyBean obb : saleDeliveryDatabodysList) {
            String itempk = obb.getItempk();
            String address = obb.getAddress();
            String materialcode = obb.getMaterialcode();
            String materialclasscode = obb.getMaterialclasscode();
            String nnum = obb.getNnum();
            String rwarehousecode = obb.getRwarehousecode();
            String cwarehousecode = obb.getCwarehousecode();
            String scannum = countScannedQRCode(billno, materialcode,itempk);
            //这里应该执行的是插入第二个表的操作
            ContentValues valuesInner = new ContentValues();
            valuesInner.put("billno", billno);
            valuesInner.put("headpk",headpk);
            valuesInner.put("itempk", itempk);
            valuesInner.put("address", address);
            valuesInner.put("materialcode", materialcode);
            valuesInner.put("materialclasscode", materialclasscode);
            valuesInner.put("nnum", nnum);
            valuesInner.put("rwarehousecode", rwarehousecode);
            valuesInner.put("cwarehousecode", cwarehousecode);
            valuesInner.put("scannum", scannum);
            valuesInner.put("issn",obb.getIssn());
            valuesInner.put("maccode",obb.getMaccode());

            //N代表尚未上传
            valuesInner.put("uploadflag", "N");
            db3.insert("AllocateTransferBody", null, valuesInner);
            valuesInner.clear();
        }
        values.put("billno", billno);
        values.put("dbilldate", dbilldate);
        values.put("dr", dr);
        values.put("cunitcode", cunitcode);
        values.put("runitcode", runitcode);
        values.put("org", org);
        values.put("ts", ts);
        values.put("num", num);
        values.put("headpk",headpk);
        values.put("flag", "N");
        // 插入第一条数据
        db3.insert("AllocateTransfer", null, values);
        values.clear();
    }



    private String countScannedQRCode(String billcode, String materialcode,String itempk) {
        String count = "0";
        Cursor cursor2 = db3.rawQuery("select prodcutcode from AllocateTransferScanResult where billno=? and materialcode=? and itempk=? ", new String[]{billcode, materialcode,itempk});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
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

    public ArrayList<AllocateTransferBean> queryAllocateTransfer() {
        ArrayList<AllocateTransferBean> list = new ArrayList<AllocateTransferBean>();
        List<String> list_update = new ArrayList<String>();
       // String sql2 = "select " + "vbillcode" + "," + "dbilldate" + "," + "dr" + " from " + "SaleDelivery";//注意：这里有单引号
      //  Cursor cursor = db3.rawQuery(sql2, null);
        Cursor cursor = db3.rawQuery("select billno,dbilldate,dr from AllocateTransfer where flag=? order by dbilldate desc", new String[]{"N"});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                AllocateTransferBean bean = new AllocateTransferBean();
                bean.billno = cursor.getString(cursor.getColumnIndex("billno"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                list.add(bean);

            }

            cursor.close();
        }

        return list;
    }
    public ArrayList<AllocateTransferBean> displayAllProductEntry() {
        ArrayList<AllocateTransferBean> list = new ArrayList<AllocateTransferBean>();
        Cursor cursor = db3.rawQuery("select billno,dbilldate,dr,flag from AllocateTransfer order by dbilldate desc", null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                AllocateTransferBean bean = new AllocateTransferBean();
                bean.billno = cursor.getString(cursor.getColumnIndex("billno"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                 Log.i("item-->",bean.billno+"/"+bean.dbilldate);
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private AllocateTransferAdapter.MyClickListener mListener = new AllocateTransferAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent(AllocateTransfer.this, AllocateTransferDetail.class);
            intent.putExtra("current_sale_delivery_vbillcode", listAllPostition.get(position).getBillno());
            intent.putExtra("current_sale_delivery_dbilldate", listAllPostition.get(position).getDbilldate());
            intent.putExtra("current_sale_delivery_orgRecv", queryOrg(listAllPostition.get(position).getBillno()));

            startActivity(intent);

        }
    };

    private String queryOrg(String billno) {
        String s="";
        Cursor cursor = db3.rawQuery("select org from AllocateTransfer where billno=?", new String[]{billno});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                s = cursor.getString(cursor.getColumnIndex("org"));
            }
            cursor.close();
        }
        return s;
    }
    private void popupQuery() {
        List<String> listWarehouse;

        LayoutInflater layoutInflater = LayoutInflater.from(AllocateTransfer.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditText = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinner = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinner = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        final Button showdailogTwo = (Button)  textEntryView.findViewById(R.id.showdailogTwo);
        time = (TextView)  textEntryView.findViewById(R.id.timeshow_saledelivery);

        String tempperiod =DataHelper.getQueryTime(AllocateTransfer.this,getIntent().getIntExtra("type",-1));
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
                AllocateTransfer.this, android.R.layout.simple_spinner_item, listWarehouse);
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
                AllocateTransfer.this, android.R.layout.simple_spinner_item, uploadflag);
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

        AlertDialog.Builder ad1 = new AlertDialog.Builder(AllocateTransfer.this);
        ad1.setTitle("出入查询条件:");
        ad1.setView(textEntryView);
        time.setText(tempperiod);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                String temp=codeNumEditText.getText().toString();
                exportList= queryexport(temp,query_cwarename,query_uploadflag);
                 listAllPostition=new ArrayList<>();
                 listAllPostition=removeDuplicate(exportList);

                //PurchaseReturnAdapter adapter=new PurchaseReturnAdapter(ProductEntry.this,saleDeliveryBeanList,mListener);
                AllocateTransferAdapter adapter=new AllocateTransferAdapter(AllocateTransfer.this,listAllPostition,mListener);
                tableListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();



            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框

    }

    List< AllocateTransferBean> exportList;

    private    List< AllocateTransferBean>  removeDuplicate(List< AllocateTransferBean> list)  {
        List< AllocateTransferBean>  beanList=new ArrayList<>();
        beanList.addAll(list);

        for  ( int  i  =   0 ; i  <  beanList.size()  -   1 ; i ++ )  {

            for  ( int  j  =  beanList.size()  -   1 ; j  >  i; j -- )  {

                if  (beanList.get(j).getBillno().equals(beanList.get(i).getBillno()))  {
                    beanList.remove(j);
                }
            }
        }
        return beanList;
    }
    private ArrayList< AllocateTransferBean> queryexport(String vbillcode,String current_cwarename,String query_uploadflag) {
        ArrayList< AllocateTransferBean> list = new ArrayList<>();
        SharedPreferences currentTimePeriod= getSharedPreferences("query_allocatetransfer", 0);
        String start_temp = currentTimePeriod.getString("starttime", iUrl.begintime);
        String end_temp = currentTimePeriod.getString("endtime", Utils.getDefaultEndTime());
        Cursor cursor=null;
        if(query_uploadflag.equals("ALL")){
            cursor = db3.rawQuery("select AllocateTransfer.billno, AllocateTransfer.dbilldate,AllocateTransferbody.materialcode,AllocateTransferbody.materialclasscode,AllocateTransfer.dr," +
                    "AllocateTransferbody.maccode,AllocateTransferbody.nnum,AllocateTransferscanresult.prodcutcode,AllocateTransferBody.rwarehousecode," +
                    "AllocateTransferscanresult.xlh" + " from AllocateTransfer inner join AllocateTransferbody on AllocateTransfer.billno=AllocateTransferbody.billno " +
                    "left join AllocateTransferscanresult on AllocateTransferbody.billno=AllocateTransferscanresult.billno " +
                    "and AllocateTransferbody.itempk=AllocateTransferscanresult.itempk where AllocateTransfer.billno" +
                    " like '%" + vbillcode + "%' and AllocateTransferBody.rwarehousecode"+ " like '%" + current_cwarename + "%' order by dbilldate desc", null);

        }else {
            cursor = db3.rawQuery("select AllocateTransfer.billno, AllocateTransfer.dbilldate,AllocateTransferbody.materialcode,AllocateTransferbody.materialclasscode,AllocateTransfer.dr," +
                    "AllocateTransferbody.maccode,AllocateTransferbody.nnum,AllocateTransferScanResult.prodcutcode,AllocateTransferBody.rwarehousecode," +
                    "AllocateTransferScanResult.xlh" + " from AllocateTransfer inner join AllocateTransferbody on AllocateTransfer.billno=AllocateTransferbody.billno " +
                    "left join AllocateTransferscanresult on AllocateTransferbody.billno=AllocateTransferScanResult.billno " +
                    "and AllocateTransferbody.itempk=AllocateTransferScanResult.itempk where AllocateTransferbody.uploadflag=? and AllocateTransfer.billno" +
                    " like '%" + vbillcode + "%' and AllocateTransferBody.rwarehousecode"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});

        }


        //判断cursor中是否存在数据
        while (cursor.moveToNext()) {

            AllocateTransferBean bean = new  AllocateTransferBean();

            bean.billno = cursor.getString(cursor.getColumnIndex("billno"));
            bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
            bean.setMaterialcode(cursor.getString(cursor.getColumnIndex("materialcode")));
            bean.setRwarehousecode(cursor.getString(cursor.getColumnIndex("rwarehousecode")));
            bean.setMaterialclasscode(cursor.getString(cursor.getColumnIndex("materialclasscode")));
            bean.setMaccode(cursor.getString(cursor.getColumnIndex("maccode")));
            bean.setNnum(cursor.getString(cursor.getColumnIndex("nnum")));
            bean.setProdcutcode(cursor.getString(cursor.getColumnIndex("prodcutcode")));
            bean.setXlh(cursor.getString(cursor.getColumnIndex("xlh")));
            bean.dr= cursor.getInt(cursor.getColumnIndex("dr"));
            if (DataHelper.queryTimePeriod(bean.billno,start_temp,end_temp,getIntent().getIntExtra("type",-1),db3)) {
                list.add(bean);
            }


        }
        cursor.close();

        return list;
    }


    private void exportData( List<AllocateTransferBean> exportList) {
        Log.i("exportList",new Gson().toJson(exportList));
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        File file=new File(sdCardDir+"/sunmi");
        if(!file.exists()){
            file.mkdir();
        }
        Date curDate =  new Date(System.currentTimeMillis());
        file=new File(sdCardDir+"/sunmi",formatter.format(curDate)+".txt");
        Toast.makeText(AllocateTransfer.this,"导出数据位置："+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            outputStream.write(("发货单号"+"\t"+ "单据日期"+"\t"+"物料编码"+"\t"+"物料名称"+"\t"+
                    "物料大类"+"\t"+"序列号"+"\t"+"条形码"+"\t").getBytes());
            for (int j = 0; j <exportList.size() ; j++) {
                if(exportList.get(j).getXlh()!=null ) {
                    outputStream.write("\r\n".getBytes());
                    outputStream.write((exportList.get(j).getBillno()+"\t"
                            +exportList.get(j).getDbilldate()+"\t"
                            +exportList.get(j).getMaterialcode()+"\t"
                            +exportList.get(j).getMaterialclasscode()+"\t"
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
                SharedPreferences currentTimePeriod= getSharedPreferences("query_allocatetransfer", 0);
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
}
