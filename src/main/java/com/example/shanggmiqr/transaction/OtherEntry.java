package com.example.shanggmiqr.transaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.BusinessOperation;
import com.example.shanggmiqr.Url.iUrl;
import com.example.shanggmiqr.bean.OtherBean;
import com.example.shanggmiqr.bean.OtherQueryBean;
import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.OtherEntryTableAdapter;

import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class OtherEntry extends AppCompatActivity implements OnClickListener {
    private Button downloadOtherEntryButton;
    private Button queryEntryButton;
    private Button displayallEntryButton;
    private Handler otherEntryHandler = null;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private ListView tableListView;
    private List<OtherBean> listAllPostition;
    private String query_cwarename;
    private String query_uploadflag;
    private ZLoadingDialog dialog;
    private List<String> test;
    private TextView lst_downLoad_ts;
    private Button buttonexport;
    int type;

    String name="";
    String title="";
    OtherEntryTableAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_entry_manage);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

         type=getIntent().getIntExtra("type",-1);
        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts_otherentry);
        //显示最后一次的下载时间

        downloadOtherEntryButton = (Button) findViewById(R.id.download_other_entry);
        title=getIntent().getStringExtra("title");
        switch (type){
            case 1:
                name="LatestOtherEntryTSInfo";

                downloadOtherEntryButton.setText("下载其他入库单");
                break;
            case 2:
                name="LatestOtherOutgoingTSInfo";

                downloadOtherEntryButton.setText("下载其他出库单");
                break;
        }

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getIntent().getStringExtra("title"));
        }
        SharedPreferences latestDBTimeInfo = getSharedPreferences(name, 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "");
        lst_downLoad_ts.setText("最后一次下载:"+begintime);

        helper3 = new MyDataBaseHelper(OtherEntry.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象

        downloadOtherEntryButton.setOnClickListener(this);
        queryEntryButton = (Button) findViewById(R.id.query_other_entry);
        queryEntryButton.setOnClickListener(this);
        displayallEntryButton = (Button) findViewById(R.id.displayall_other_entry);
        //displayallEntryButton.setVisibility(View.INVISIBLE);
        displayallEntryButton.setOnClickListener(this);
        tableListView = (ListView) findViewById(R.id.list_otherentry);
        dialog = new ZLoadingDialog(OtherEntry.this);
        buttonexport=findViewById(R.id.b_export);
        buttonexport.setOnClickListener(this);

        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.select(position);


            }
        });
        otherEntryHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(OtherEntry.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        dialog.dismiss();
                        Toast.makeText(OtherEntry.this, title+"单已经下载", Toast.LENGTH_LONG).show();
                        //插入UI表格数据

                        initAdapter();

                        break;
                    case 0x18:
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(OtherEntry.this, s, Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(OtherEntry.this, title+"单下载异常，错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        Intent intent = getIntent();
        String str = intent.getStringExtra("from_business_operation");
        if ("Y".equals(str)) {
            downloadOtherEntryButton.performClick();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        initAdapter();
    }

    private void initAdapter() {
        List<OtherBean> list = queryAll();

        listAllPostition = list;
        adapter = new OtherEntryTableAdapter(OtherEntry.this, list, mListener);
        tableListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_other_entry:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (DataHelper.isNetworkConnected(OtherEntry.this)) {
                            try {
                                if (DataHelper.isWarehouseDBDownloaed(db3)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                          DataHelper.showDialog(dialog);
                                        }
                                    });
                                    //其他入库查询，嵌套json,使用两张表存储，通过pobillcode关联
                                    String outEntryData = DataHelper.downloadDatabase("1",OtherEntry.this,type);
                                    if (null == outEntryData) {
                                        dialog.dismiss();
                                        return;
                                    }

                                    Gson gson7 = new Gson();
                                    final OtherQueryBean otherEntryBean = gson7.fromJson(outEntryData, OtherQueryBean.class);

                                    if (otherEntryBean.getPagetotal() == 1) {
                                        DataHelper.insertOtherDataToDB(db3,otherEntryBean,type);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        otherEntryHandler.sendMessage(msg);
                                    } else if (otherEntryBean.getPagetotal() < 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(OtherEntry.this, otherEntryBean.getErrmsg(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        Log.i("otherEntryBean",new Gson().toJson(otherEntryBean));
                                        DataHelper.insertOtherDataToDB(db3,otherEntryBean,type);
                                        for (int pagenum = 2; pagenum <= otherEntryBean.getPagetotal(); pagenum++) {
                                            String outGoingData2 = DataHelper.downloadDatabase( String.valueOf(pagenum),OtherEntry.this,type);
                                            OtherQueryBean outGoingBean2 = gson7.fromJson(outGoingData2, OtherQueryBean.class);
                                            DataHelper.insertOtherDataToDB(db3,outGoingBean2,type);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        otherEntryHandler.sendMessage(msg);
                                    }


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DataHelper.putLatestdownloadbegintime(getIntent().getIntExtra("type",-1),OtherEntry.this);
                                            SharedPreferences latestDBTimeInfo = getSharedPreferences(name, 0);
                                            String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", iUrl.begintime);
                                            lst_downLoad_ts.setText("最后一次下载:"+begintime);
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Toast.makeText(OtherEntry.this, "请先到基础数据管理界面下载仓库信息", Toast.LENGTH_LONG).show();
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
                                otherEntryHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0x10;
                            otherEntryHandler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.query_other_entry:
                popupQuery();
                break;
            case R.id.b_export:
                export();
                break;
            case R.id.displayall_other_entry:
                initAdapter();

                break;
        }
    }






    private TextView time;
    private void popupQuery() {
        LayoutInflater layoutInflater = LayoutInflater.from(OtherEntry.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditTextOtherEntry = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinnerOtherEntry = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinnerEntry = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        final Button showdailogTwo = (Button)  textEntryView.findViewById(R.id.showdailogTwo);
        time = (TextView)  textEntryView.findViewById(R.id.timeshow_saledelivery);


        String tempperiod =DataHelper.getQueryTime(OtherEntry.this,type);
        time.setText(tempperiod);
        showdailogTwo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogTwo();
            }
        });
        test = DataHelper.queryWarehouseInfo(db3);
        test.add("");
        List<String> uploadflag = new ArrayList();
        uploadflag.add("是");
        uploadflag.add("否");
        uploadflag.add("全部");
        uploadflag.add("部分提交");
        final ArrayAdapter adapter = new ArrayAdapter(
                OtherEntry.this, android.R.layout.simple_spinner_item, test);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOtherEntry.setAdapter(adapter);
        spinnerOtherEntry.setSelection(test.size() - 1, true);
        query_cwarename = adapter.getItem(test.size() - 1).toString();
        spinnerOtherEntry.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                query_cwarename = adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ArrayAdapter adapter2 = new ArrayAdapter(
                OtherEntry.this, android.R.layout.simple_spinner_item, uploadflag);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flag_spinnerEntry.setAdapter(adapter2);
       flag_spinnerEntry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               switch (position){
                   case 0:
                       query_uploadflag = "Y";
                       break;
                   case 1:
                       query_uploadflag = "N";
                       break;
                   case 2:
                       query_uploadflag = "ALL";
                       break;
                   case 3:
                       query_uploadflag = "PY";
                       break;



               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
        AlertDialog.Builder ad1 = new AlertDialog.Builder(OtherEntry.this);
        ad1.setTitle("请输入出库单号:");
        ad1.setView(textEntryView);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
              if(query_cwarename == null){
                    query_cwarename = adapter.getItem(test.size() - 1).toString();
                }

                bean1 = query(codeNumEditTextOtherEntry.getText().toString(), query_cwarename,query_uploadflag);
                listAllPostition =removeDuplicate(bean1);
                final OtherEntryTableAdapter adapter3 = new OtherEntryTableAdapter(OtherEntry.this, listAllPostition, mListener);
                tableListView.setAdapter(adapter3);
                adapter3.notifyDataSetChanged();

            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框
    }
    ArrayList<OtherBean> bean1;

    private   ArrayList<OtherBean>  removeDuplicate(ArrayList<OtherBean> list)  {
        ArrayList<OtherBean>  beanList=new ArrayList<>();
        beanList.addAll(list);
        for  ( int  i  =   0 ; i  <  beanList.size()  -   1 ; i ++ )  {
            for  ( int  j  =  beanList.size()  -   1 ; j  >  i; j -- )  {
                if  (beanList.get(j).getPobillcode().equals(beanList.get(i).getPobillcode()))  {
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
                String st=Utils.parseDate(startTime.getYear()+"-"+(startTime.getMonth()+1)+"-"+startTime.getDayOfMonth());
                String et =  Utils.parseDate(endTime.getYear()+"-"+(endTime.getMonth()+1)+"-"+endTime.getDayOfMonth());
                SharedPreferences currentTimePeriod=null;
                switch (type){
                    case 1:
                        currentTimePeriod= getSharedPreferences("query_otherentry", 0);
                      break;
                    case 2:
                        currentTimePeriod= getSharedPreferences("query_otheroutgoing", 0);
                        break;
                }

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
    private void export() {
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        File file=new File(sdCardDir+"/sunmi/export");
        if(!file.exists()){
            file.mkdir();
        }
        Date curDate =  new Date(System.currentTimeMillis());
        file=new File(sdCardDir+"/sunmi/export",formatter.format(curDate)+".txt");
        Toast.makeText(OtherEntry.this,"导出数据位置："+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            outputStream.write(("发货单号"+"\t"+ "单据日期"+"\t"+"物料编码"+"\t"+"物料名称"+"\t"+
                    "物料大类"+"\t"+"序列号"+"\t"+"条形码").getBytes());
            for (int j = 0; j <bean1.size() ; j++) {
                if(bean1.get(j).getXlh()!=null ) {
                    outputStream.write("\r\n".getBytes());
                    outputStream.write((bean1.get(j).getPobillcode() + "\t"
                            + bean1.get(j).getDbilldate() + "\t"
                            + bean1.get(j).getMaterialcode() + "\t"
                            + bean1.get(j).getMatrname() + "\t"
                            + bean1.get(j).getMaccode() + "\t"
                            + bean1.get(j).getXlh() + "\t"
                            + bean1.get(j).getProdcutcode()).getBytes());
                }

            }
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<OtherBean> query(String pobillcode, String current_cwarename,String query_uploadflag) {
        ArrayList<OtherBean> list = new ArrayList<OtherBean>();
        Cursor cursor=null;
        SharedPreferences currentTimePeriod;
        String start_temp="";
        String end_temp="";
        switch (type){
            case 1:
                currentTimePeriod= getSharedPreferences("query_otherentry", 0);
                start_temp = currentTimePeriod.getString("starttime", iUrl.begintime);
                end_temp = currentTimePeriod.getString("endtime", Utils.getDefaultEndTime());
                if(query_uploadflag.equals("ALL")){
                    cursor = db3.rawQuery("select otherentry.pobillcode,otherentry.flag,otherentry.cwarecode,otherentry.cwarename,otherentry.dr, otherentry.dbilldate," +
                            "otherentrybody.materialcode,otherentry.dr," +
                            "otherentrybody.maccode,otherentrybody.nnum, SaleDeliveryScanResult.prodcutcode," +
                            "SaleDeliveryScanResult.xlh" + " from otherentry left join otherentrybody on otherentry.pobillcode=otherentrybody.pobillcode " +
                            "left join SaleDeliveryScanResult on otherentrybody.pobillcode=SaleDeliveryScanResult.vbillcode " +
                            "and otherentrybody.vcooporderbcode_b=SaleDeliveryScanResult.vcooporderbcode_b where otherentry.pobillcode" +
                            " like '%" + pobillcode + "%' and otherentry.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", null);
                }else {
                    cursor = db3.rawQuery("select otherentry.pobillcode,otherentry.flag,otherentry.cwarecode,otherentry.cwarename,otherentry.dr, otherentry.dbilldate,otherentrybody.materialcode,otherentry.dr," +
                            "otherentrybody.maccode,otherentrybody.nnum, SaleDeliveryScanResult.prodcutcode," +
                            "SaleDeliveryScanResult.xlh" + " from otherentry left join otherentrybody on otherentry.pobillcode=otherentrybody.pobillcode " +
                            "left join SaleDeliveryScanResult on otherentrybody.pobillcode=SaleDeliveryScanResult.vbillcode " +
                            "and otherentrybody.vcooporderbcode_b=SaleDeliveryScanResult.vcooporderbcode_b where flag=? and otherentry.pobillcode" +
                            " like '%" + pobillcode + "%' and otherentry.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});

                }
              break;
            case 2:
               currentTimePeriod= getSharedPreferences("query_otheroutgoing", 0);
                start_temp = currentTimePeriod.getString("starttime", iUrl.begintime);
                end_temp = currentTimePeriod.getString("endtime", Utils.getDefaultEndTime());
                if(query_uploadflag.equals("ALL")){
                    cursor = db3.rawQuery("select otheroutgoing.pobillcode,otheroutgoing.flag,otheroutgoing.cwarecode,otheroutgoing.cwarename,otheroutgoing.dr, otheroutgoing.dbilldate," +
                            "otheroutgoingbody.materialcode,otheroutgoingbody.maccode,otheroutgoingbody.nnum, SaleDeliveryScanResult.prodcutcode," +
                            "SaleDeliveryScanResult.xlh" + " from otheroutgoing left join otheroutgoingbody on otheroutgoing.pobillcode=otheroutgoingbody.pobillcode " +
                            "left join SaleDeliveryScanResult on otheroutgoingbody.pobillcode=SaleDeliveryScanResult.vbillcode " +
                            "and otheroutgoingbody.vcooporderbcode_b=SaleDeliveryScanResult.vcooporderbcode_b where  otheroutgoing.pobillcode" +
                            " like '%" + pobillcode + "%' and otheroutgoing.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc",null);

                }else {
                    cursor = db3.rawQuery("select otheroutgoing.pobillcode,otheroutgoing.flag,otheroutgoing.cwarecode,otheroutgoing.cwarename,otheroutgoing.dr, otheroutgoing.dbilldate," +
                            "otheroutgoingbody.materialcode,otheroutgoingbody.maccode,otheroutgoingbody.nnum,  SaleDeliveryScanResult.prodcutcode," +
                            "SaleDeliveryScanResult.xlh" + " from otheroutgoing left join otheroutgoingbody on otheroutgoing.pobillcode=otheroutgoingbody.pobillcode " +
                            "left join  SaleDeliveryScanResult on otheroutgoingbody.pobillcode= SaleDeliveryScanResult.vbillcode " +
                            "and otheroutgoingbody.vcooporderbcode_b=SaleDeliveryScanResult.vcooporderbcode_b where flag=? and otheroutgoing.pobillcode" +
                            " like '%" + pobillcode + "%' and otheroutgoing.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});

                }
              break;
        }


                //判断cursor中是否存在数据
                while (cursor.moveToNext()) {
                    OtherBean bean = new OtherBean();
                    bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                    bean.cwarecode = cursor.getString(cursor.getColumnIndex("cwarecode"));
                    bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                    bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                    bean.setMaterialcode(cursor.getString(cursor.getColumnIndex("materialcode")));
                    bean.setMaccode(cursor.getString(cursor.getColumnIndex("maccode")));
                    bean.setNnum(cursor.getString(cursor.getColumnIndex("nnum")));
                    bean.setProdcutcode(cursor.getString(cursor.getColumnIndex("prodcutcode")));
                    bean.setXlh(cursor.getString(cursor.getColumnIndex("xlh")));
                    bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                    bean.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
                    if (DataHelper.queryTimePeriod(bean.pobillcode,start_temp,end_temp,type,db3)) {
                        list.add(bean);
                    }

                }
                cursor.close();

        return list;
    }

    public ArrayList<OtherBean> queryAll() {
        ArrayList<OtherBean> list = new ArrayList<OtherBean>();
        Cursor cursor=null;
         switch (type){
             case 1:
                 cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr,flag from OtherEntry where flag=? order by dbilldate desc",
                         new String[]{"N"});
                 break;
             case 2:
                 cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr,flag from OtherOutgoing where flag=? order by dbilldate desc", new String[]{"N"});
                 break;
         }


        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherBean bean = new OtherBean();
                bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                bean.cwarecode = cursor.getString(cursor.getColumnIndex("cwarecode"));
                bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                bean.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
                list.add(bean);

            }

            cursor.close();
        }

        return list;
    }






    /**
     * 实现类，响应按钮点击事件
     */
    private OtherEntryTableAdapter.MyClickListener mListener = new OtherEntryTableAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {

            Intent  intent = new Intent(OtherEntry.this, OtherEntryDetail.class);
            intent.putExtra("type",type);
            intent.putExtra("current_pobillcode", listAllPostition.get(position).getPobillcode());
            intent.putExtra("current_cwarename", listAllPostition.get(position).getCwarename());
            intent.putExtra("current_cwarecode", listAllPostition.get(position).getCwarecode());
            intent.putExtra("current_dbilldate", listAllPostition.get(position).getDbilldate());
            intent.putExtra("flag",listAllPostition.get(position).getFlag());
            Log.i("flag-->",new Gson().toJson(listAllPostition.get(position)));
            startActivity(intent);
        }
    };
}
