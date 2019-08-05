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
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.LoanAdapter;
import com.example.shanggmiqr.bean.CommonSendNoPagetotalBean;
import com.example.shanggmiqr.bean.LoanBean;
import com.example.shanggmiqr.bean.LoanQuery;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/9.
 */

public class LoanBill extends AppCompatActivity implements OnClickListener {
    private String saleDelivDataResp;
    private Button downloadDeliveryButton;
    private Button querySaleDeliveryButton;
    private Button displayallSaleDeliveryButton;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private Handler saleDeliveryHandler = null;
    private ListView tableListView;
    private List<LoanBean> listAllPostition;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_manage);
        helper3 = new MyDataBaseHelper(LoanBill.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts_loan);
        //显示最后一次的下载时间
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestLoanTSInfo", 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "2018-09-01 00:00:01");
        lst_downLoad_ts.setText("最后一次下载:"+begintime);

        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        dialog = new ZLoadingDialog(LoanBill.this);
        downloadDeliveryButton = (Button) findViewById(R.id.download_loan);
        downloadDeliveryButton.setOnClickListener(this);
        querySaleDeliveryButton = (Button) findViewById(R.id.query_loan);
        querySaleDeliveryButton.setOnClickListener(this);
        displayallSaleDeliveryButton = (Button) findViewById(R.id.displayall_loan);
        //displayallSaleDeliveryButton.setVisibility(View.INVISIBLE);
        displayallSaleDeliveryButton.setOnClickListener(this);

        tableListView = (ListView) findViewById(R.id.list_loan);
        List<LoanBean> list = querySaleDelivery();
        listAllPostition = list;
        final LoanAdapter adapter1 = new LoanAdapter(LoanBill.this, list, mListener);
        tableListView.setAdapter(adapter1);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.select(position);
                LoanBean saleDelivery1Bean = (LoanBean) adapter1.getItem(position);
                chosen_line_vbillcode = saleDelivery1Bean.getPobillcode();
                chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();

            }
        });
        saleDeliveryHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(LoanBill.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        //插入UI表格数据
                        dialog.dismiss();
                        List<LoanBean> list = querySaleDelivery();
                        listAllPostition = list;
                        final LoanAdapter adapter = new LoanAdapter(LoanBill.this, list, mListener);
                        tableListView.setAdapter(adapter);
                        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.select(position);
                                LoanBean saleDelivery1Bean = (LoanBean) adapter.getItem(position);
                                chosen_line_vbillcode = saleDelivery1Bean.getPobillcode();
                                chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();
                                //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                            }
                        });
                        Toast.makeText(LoanBill.this, "借出单下载完成", Toast.LENGTH_LONG).show();
                        break;
                    case 0x18:
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(LoanBill.this, s, Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(LoanBill.this, "借出单下载异常，错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                    default:
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
        //downloadDeliveryButton.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_loan:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkConnected(LoanBill.this)) {
                            try {
                                if (isWarehouseDBDownloaed()) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
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
                                    });
                                    //R07发货单
                                    String loanData = DataHelper.downloadDatabase( "1",LoanBill.this,4);
                                    if (null == loanData) {
                                        dialog.dismiss();
                                        return;
                                    }
                                    Gson gson7 = new Gson();
                                    LoanQuery loanQuery = gson7.fromJson(loanData, LoanQuery.class);
                                    int pagetotal = Integer.parseInt(loanQuery.getPagetotal());
                                    if (pagetotal == 1) {
                                        insertDownloadDataToDB(loanQuery);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        saleDeliveryHandler.sendMessage(msg);
                                    } else if (pagetotal < 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(LoanBill.this, "借出单已经是最新", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        insertDownloadDataToDB(loanQuery);
                                        for (int pagenum = 2; pagenum <= pagetotal; pagenum++) {
                                            String saleDeliveryData2 = DataHelper.downloadDatabase(String.valueOf(pagenum),
                                                    LoanBill.this,4);
                                            LoanQuery saleDeliveryQuery2 = gson7.fromJson(saleDeliveryData2, LoanQuery.class);
                                            insertDownloadDataToDB(saleDeliveryQuery2);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        saleDeliveryHandler.sendMessage(msg);
                                    }
                                    String currentTs = Utils.getCurrentDateTimeNew();
                                    SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestLoanTSInfo", 0);
                                    SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                                    editor5.putString("latest_download_ts_begintime", currentTs);
                                    editor5.commit();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestLoanTSInfo", 0);
                                            String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "2018-09-01 00:00:01");
                                            lst_downLoad_ts.setText("最后一次下载:"+begintime);
                                        }
                                    });
                                }else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Toast.makeText(LoanBill.this, "请先到基础数据管理界面下载仓库信息", Toast.LENGTH_LONG).show();
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
            case R.id.query_loan:
                popupQuery();
                break;
            case R.id.displayall_loan:
                List<LoanBean> list = displayAllSaleDelivery();
                listAllPostition = list;
                final LoanAdapter adapter = new LoanAdapter(LoanBill.this, list, mListener);
                tableListView.setAdapter(adapter);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.select(position);
                        SaleDeliveryBean saleDelivery1Bean = (SaleDeliveryBean) adapter.getItem(position);
                        chosen_line_vbillcode = saleDelivery1Bean.getVbillcode();
                        chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();
                        //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                    }
                });
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

    private void insertDownloadDataToDB(LoanQuery loanquery) {

        List<LoanQuery.DataBean> loanBeanList = loanquery.getData();
        for (LoanQuery.DataBean ob : loanBeanList) {

            String pobillcode = ob.getPobillcode();
            String dbilldate = ob.getDbilldate();
            String num = ob.getNum();
            String ts = ob.getTs();
            String dr =ob.getDr();
            //0:新增-正常下载保持 1：删除，删除对应单据 2：修改，先删除对应单据再保持

            if("0".equals(dr)&& isVbillcodeExist(pobillcode)){
                continue;
            }
            //等于1时
            if("1".equals(dr) || ("2".equals(dr)&&isVbillcodeExist(pobillcode)))
            {
                //操作选择二 通过单号删除
                //删除三张表
                db3.beginTransaction();
                try {
                    db3.delete("Loan", "pobillcode=?", new String[]{pobillcode});
                    db3.delete("LoanBody", "pobillcode=?", new String[]{pobillcode});
                    db3.delete("LoanScanResult", "pobillcode=?", new String[]{pobillcode});
                    db3.setTransactionSuccessful();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                finally {
                    db3.endTransaction();
                }
            }
            if("1".equals(dr)){
                continue;
            }
            List<LoanQuery.DataBean.BodyBean> loanDatabodysList = ob.getBody();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            for (LoanQuery.DataBean.BodyBean obb : loanDatabodysList) {
                String materialcode = obb.getMaterialcode();
                String maccode = obb.getMaccode();
                String nnum = obb.getNnum();
                String itempk = obb.getItempk();
                String cwarecode = obb.getCwarecode();
                String cwarename = obb.getCwarename();
                String vemo = obb.getVemo();;
                String scannum = countScannedQRCode(pobillcode,itempk, materialcode);
                String orginal_cwarename = existOriginalCwarename(cwarename);
                //这里应该执行的是插入第二个表的操作
                ContentValues valuesInner = new ContentValues();
                valuesInner.put("pobillcode", pobillcode);
                valuesInner.put("itempk", itempk);
                valuesInner.put("materialcode", materialcode);
                valuesInner.put("maccode", maccode);
                valuesInner.put("nnum", nnum);
                valuesInner.put("scannum", scannum);
                valuesInner.put("cwarecode", cwarecode);
                valuesInner.put("cwarename", cwarename);
                valuesInner.put("orginal_cwarename", orginal_cwarename);
                valuesInner.put("vemo", vemo);
                //N代表尚未上传
                valuesInner.put("uploadflag", "N");
                db3.insert("LoanBody", null, valuesInner);
                valuesInner.clear();
            }
                values.put("pobillcode", pobillcode);
            values.put("dbilldate", dbilldate);
            values.put("num", num);
            values.put("ts", ts);
            values.put("dr", dr);
            values.put("flag", "N");
            // 插入第一条数据
            db3.insert("Loan", null, values);
            values.clear();
        }
    }

    private boolean isVbillcodeExist(String pobillcode) {
        Cursor cursor2 = db3.rawQuery("select pobillcode from Loan where pobillcode=?", new String[]{pobillcode});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            cursor2.close();
            return true;
        }else {
            return false;
        }
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

    private String getDefaultEndTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day) + " " + "23:59:59";
    }

    private String countScannedQRCode(String pobillcode,String itempk, String materialcode) {
        String count = "0";
        Cursor cursor2 = db3.rawQuery("select prodcutcode from LoanScanResult where pobillcode=? and materialcode=? and itempk=? ", new String[]{pobillcode, materialcode,itempk});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
    }

    private void popupQuery() {
        LayoutInflater layoutInflater = LayoutInflater.from(LoanBill.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditText = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinner = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinner = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        final Button showdailogTwo = (Button)  textEntryView.findViewById(R.id.showdailogTwo);
        time = (TextView)  textEntryView.findViewById(R.id.timeshow_saledelivery);
        SharedPreferences currentTimePeriod= getSharedPreferences("query_loanbill", 0);
        final String tempperiod =currentTimePeriod.getString("current_account","2018-09-01 至 2018-12-17");
        showdailogTwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTwo();
            }
        });
        test = queryWarehouseInfo();
        test.add("");
        uploadflag = new ArrayList();
        uploadflag.add("是");
        uploadflag.add("部分上传");
        uploadflag.add("否");
        final ArrayAdapter adapter = new ArrayAdapter(
                LoanBill.this, android.R.layout.simple_spinner_item, test);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(test.size() - 1, true);
        query_cwarename =adapter.getItem(test.size() - 1).toString();
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                query_cwarename=adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ArrayAdapter adapter2 = new ArrayAdapter(
                LoanBill.this, android.R.layout.simple_spinner_item, uploadflag);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flag_spinner.setAdapter(adapter2);
        flag_spinner.setSelection(uploadflag.size() - 1, true);
        if ("是".equals(adapter2.getItem(uploadflag.size() - 1).toString())){
            query_uploadflag = "Y";
        } else if ("否".equals(adapter2.getItem(uploadflag.size() - 1).toString())){
            query_uploadflag = "N";
        } else {
            query_uploadflag = "PY";
        }
        flag_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if ("是".equals(adapter2.getItem(i).toString())){
                   query_uploadflag = "Y";
               } else if ("否".equals(adapter2.getItem(i).toString())){
                   query_uploadflag = "N";
               } else {
                   query_uploadflag = "PY";
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog.Builder ad1 = new AlertDialog.Builder(LoanBill.this);
        ad1.setTitle("出入查询条件:");
        ad1.setView(textEntryView);
        time.setText(tempperiod);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                String temp=codeNumEditText.getText().toString();
                if(query_cwarename == null){
                    query_cwarename = adapter.getItem(test.size() - 1).toString();
                }
                if(query_uploadflag == null){
                    query_uploadflag = "N";
                }
                ArrayList<LoanBean> bean1 = query(temp,query_cwarename,query_uploadflag);
                listAllPostition = bean1;
                final LoanAdapter adapter3 = new LoanAdapter(LoanBill.this, bean1, mListener);
                tableListView.setAdapter(adapter3);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter3.select(position);
                        SaleDeliveryBean saleDelivery1Bean = (SaleDeliveryBean) adapter3.getItem(position);
                        chosen_line_vbillcode = saleDelivery1Bean.getVbillcode();
                        chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();
                        //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框
        time.setText(tempperiod);
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
                SharedPreferences currentTimePeriod= getSharedPreferences("query_loanbill", 0);
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
    private List<String> queryWarehouseInfo() {
        List<String> cars = new ArrayList<>();
        Cursor cursornew = db3.rawQuery("select name from Warehouse",
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
    public ArrayList<LoanBean> query(String pobillcode,String current_cwarename,String query_uploadflag) {
        ArrayList<LoanBean> list = new ArrayList<LoanBean>();
        SharedPreferences currentTimePeriod= getSharedPreferences("query_loanbill", 0);
        String start_temp = currentTimePeriod.getString("starttime","2018-09-01 00:00:01");
        String end_temp = currentTimePeriod.getString("endtime", Utils.getDefaultEndTime());
        Cursor cursor = db3.rawQuery("select pobillcode,dbilldate,dr from Loan where flag=? and pobillcode like '%" + pobillcode + "%' order by dbilldate desc", new String[]{query_uploadflag});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                LoanBean bean = new LoanBean();
                bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr= cursor.getString(cursor.getColumnIndex("dr"));
                if(queryCwarename(current_cwarename, bean.pobillcode)){
                    if (queryTimePeriod(bean.pobillcode,start_temp,end_temp)) {
                        list.add(bean);
                    }
                }
            }
            cursor.close();
        }
        return list;
    }

    private boolean queryTimePeriod(String vbillcode,String startTime,String endTime) {
        Cursor cursor = db3.rawQuery("SELECT * FROM Loan WHERE pobillcode =? and " +
                "dbilldate>=? and dbilldate<?",
        new String[] {vbillcode, startTime, endTime});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            cursor.close();
            return true;
        }else{
            return false;
        }
    }

    private boolean queryCwarename(String current_cwarename,String pobillcode) {

        Cursor cursor = db3.rawQuery("select pobillcode from LoanBody where pobillcode =? and cwarename like '%" + current_cwarename + "%'  ", new String[]{pobillcode});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
            }
            cursor.close();
            return true;
        }
        return false;
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

    public ArrayList<LoanBean> querySaleDelivery() {
        ArrayList<LoanBean> list = new ArrayList<LoanBean>();
        List<String> list_update = new ArrayList<String>();
        Cursor cursor = db3.rawQuery("select pobillcode,dbilldate,dr from Loan where flag=? order by dbilldate desc", new String[]{"N"});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                LoanBean bean = new LoanBean();
                bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getString(cursor.getColumnIndex("dr"));
                list.add(bean);
            }

            cursor.close();
        }
        return list;
    }
    public ArrayList<LoanBean> displayAllSaleDelivery() {
        ArrayList<LoanBean> list = new ArrayList<LoanBean>();
        Cursor cursor = db3.rawQuery("select pobillcode,dbilldate,dr from Loan order by dbilldate desc", null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                LoanBean bean = new LoanBean();
                bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getString(cursor.getColumnIndex("dr"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private LoanAdapter.MyClickListener mListener = new LoanAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent(LoanBill.this, LoanDetail.class);
            intent.putExtra("current_sale_delivery_vbillcode", listAllPostition.get(position).getPobillcode());
            intent.putExtra("current_sale_delivery_dbilldate", listAllPostition.get(position).getDbilldate());
            intent.putExtra("type",getIntent().getIntExtra("type",-1));
            startActivity(intent);
        }
    };
}
