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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.Url.iUrl;
import com.example.shanggmiqr.bean.OtherEntryBean;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.OtherOutgoingTableAdapter;
import com.example.shanggmiqr.bean.CommonSendBean;
import com.example.shanggmiqr.bean.OtherOutgoingBean;
import com.example.shanggmiqr.bean.OtherOutgoingQuery;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.shanggmiqr.util.Utils.getDefaultEndTime;

/**
 * Created by weiyt.jiang on 2018/8/9.
 * 其他出库
 */

public class OtherOutgoing extends AppCompatActivity implements OnClickListener {
    private String otherOutgoingDataResp;
    private String otherOutgoingUploadDataResp;
    private Button downloadOtherOutgingButton;
    private Button queryOutgoingButton;
    private Button displayallOutgoingButton;
    private Handler otherOutgoingHandler = null;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private ListView tableListView;
    private List<OtherOutgoingBean> listAllPostition;
    private String chosen_line_Pobillcode;
    private String chosen_line_Cwarename;
    private String chosen_line_Cwarecode;
    private String query_cwarename;
    private String query_uploadflag;
    private ZLoadingDialog dialog;
    private List<String> test;
    private TextView lst_downLoad_ts;
    private  Button buttonexport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_outging_manage);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts_outgoing);
        //显示最后一次的下载时间
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestOtherOutgoingTSInfo", 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_systime", iUrl.begintime);
        lst_downLoad_ts.setText("最后一次下载:"+begintime);
        helper3 = new MyDataBaseHelper(OtherOutgoing.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db3 = helper3.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        downloadOtherOutgingButton = (Button) findViewById(R.id.download_other_outging);
        downloadOtherOutgingButton.setOnClickListener(this);
        queryOutgoingButton = (Button) findViewById(R.id.query_other_outging);
        queryOutgoingButton.setOnClickListener(this);
        displayallOutgoingButton = (Button) findViewById(R.id.displayall_other_outging);
        //displayallOutgoingButton.setVisibility(View.INVISIBLE);
        displayallOutgoingButton.setOnClickListener(this);
        tableListView = (ListView) findViewById(R.id.list);
        buttonexport=findViewById(R.id.b_export);
        buttonexport.setOnClickListener(this);
        dialog = new ZLoadingDialog(OtherOutgoing.this);
        List<OtherOutgoingBean> list = queryAll();
        listAllPostition = list;
        final OtherOutgoingTableAdapter adapter1 = new OtherOutgoingTableAdapter(OtherOutgoing.this, list, mListener);
        tableListView.setAdapter(adapter1);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.select(position);
                OtherOutgoingBean otherOutgoingBean = (OtherOutgoingBean) adapter1.getItem(position);
                chosen_line_Pobillcode = otherOutgoingBean.getPobillcode();
                chosen_line_Cwarename = otherOutgoingBean.getCwarename();
                chosen_line_Cwarecode = otherOutgoingBean.getCwarecode();
                //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
            }
        });
        otherOutgoingHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(OtherOutgoing.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        dialog.dismiss();
                        Toast.makeText(OtherOutgoing.this, "其他出库单已经下载", Toast.LENGTH_LONG).show();
                        //插入UI表格数据

                        List<OtherOutgoingBean> list = queryAll();
                        listAllPostition = list;
                        final OtherOutgoingTableAdapter adapter = new OtherOutgoingTableAdapter(OtherOutgoing.this, list, mListener);
                        tableListView.setAdapter(adapter);
                        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.select(position);
                                OtherOutgoingBean otherOutgoingBean = (OtherOutgoingBean) adapter.getItem(position);
                                chosen_line_Pobillcode = otherOutgoingBean.getPobillcode();
                                chosen_line_Cwarename = otherOutgoingBean.getCwarename();
                                chosen_line_Cwarecode = otherOutgoingBean.getCwarecode();
                                //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                    case 0x18:
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(OtherOutgoing.this, s, Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(OtherOutgoing.this, "其他出库单下载异常，错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        Intent intent = getIntent();
        String str = intent.getStringExtra("from_business_operation");
        if ("Y".equals(str)) {
            downloadOtherOutgingButton.performClick();
        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        //返回之后重新下载(暂时屏蔽)
        //downloadOtherOutgingButton.performClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(OtherOutgoing.this, BusinessOperation.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //点击完返回键，执行的动作
            Intent intent = new Intent(OtherOutgoing.this, BusinessOperation.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.download_other_outging:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkConnected(OtherOutgoing.this)) {
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
                                    //其他出库查询，嵌套json,使用两张表存储，通过pobillcode关联
                                    String outGoingData = downloadDatabase("R11", "1");
                                    if (null == outGoingData) {
                                        dialog.dismiss();
                                        return;
                                    }
                                    Gson gson7 = new Gson();
                                    OtherOutgoingQuery outGoingBean = gson7.fromJson(outGoingData, OtherOutgoingQuery.class);

                                    if (outGoingBean.getPagetotal() == 1) {
                                        insertDownloadDataToDB(outGoingBean);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        otherOutgoingHandler.sendMessage(msg);
                                    } else if (outGoingBean.getPagetotal() < 1) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialog.dismiss();
                                                Toast.makeText(OtherOutgoing.this, "其他出库单已经是最新", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        insertDownloadDataToDB(outGoingBean);
                                        for (int pagenum = 2; pagenum <= outGoingBean.getPagetotal(); pagenum++) {
                                            String outGoingData2 = downloadDatabase("R11", String.valueOf(pagenum));
                                            OtherOutgoingQuery outGoingBean2 = gson7.fromJson(outGoingData2, OtherOutgoingQuery.class);
                                            insertDownloadDataToDB(outGoingBean2);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        otherOutgoingHandler.sendMessage(msg);
                                    }
                                    String currentTs = getLatestDbilldate();
                                    String systime = Utils.getCurrentDateTimeNew();
                                    SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestOtherOutgoingTSInfo", 0);
                                    SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                                    editor5.putString("latest_download_ts_begintime", currentTs);
                                    editor5.putString("latest_download_ts_systime", systime);
                                    editor5.commit();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestOtherOutgoingTSInfo", 0);
                                            String begintime = latestDBTimeInfo.getString("latest_download_ts_systime", iUrl.begintime);
                                            lst_downLoad_ts.setText("最后一次下载:"+begintime);
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            Toast.makeText(OtherOutgoing.this, "请先到基础数据管理界面下载仓库信息", Toast.LENGTH_LONG).show();
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
                                otherOutgoingHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0x10;
                            otherOutgoingHandler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
            case R.id.query_other_outging:
                popupQuery();
                break;
            case R.id.b_export:
                export();
                break;
            case R.id.displayall_other_outging:
                List<OtherOutgoingBean> list = displayAll();
                listAllPostition = list;
                final OtherOutgoingTableAdapter adapter = new OtherOutgoingTableAdapter(OtherOutgoing.this, list, mListener);
                tableListView.setAdapter(adapter);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter.select(position);
                        OtherOutgoingBean otherOutgoingBean = (OtherOutgoingBean) adapter.getItem(position);
                        chosen_line_Pobillcode = otherOutgoingBean.getPobillcode();
                        chosen_line_Cwarename = otherOutgoingBean.getCwarename();
                        chosen_line_Cwarecode = otherOutgoingBean.getCwarecode();
                        //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                    }
                });
                break;
        }
    }
    private void export() {
        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();

        SimpleDateFormat   formatter   =   new   SimpleDateFormat   ("yyyy年MM月dd日HH时mm分ss秒");
        File file=new File(sdCardDir+"/sunmi");
        if(!file.exists()){
            file.mkdir();
        }
        Date curDate =  new Date(System.currentTimeMillis());
        file=new File(sdCardDir+"/sunmi",formatter.format(curDate)+".txt");
        Toast.makeText(OtherOutgoing.this,"导出数据位置："+file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            outputStream.write(("发货单号"+"\t"+ "单据日期"+"\t"+"物料编码"+"\t"+"物料名称"+"\t"+
                    "物料大类"+"\t"+"序列号"+"\t"+"条形码").getBytes());
            for (int j = 0; j <bean1.size() ; j++) {
                outputStream.write("\r\n".getBytes());

                outputStream.write((bean1.get(j).getPobillcode()+"\t"
                        +bean1.get(j).getDbilldate()+"\t"
                        +bean1.get(j).getMaterialcode()+"\t"
                        +bean1.get(j).getMatrname()+"\t"
                        +bean1.get(j).getMaccode()+"\t"
                        +bean1.get(j).getXlh()+"\t"
                        +bean1.get(j).getProdcutcode()).getBytes());

            }
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isWarehouseDBDownloaed() {
        Cursor cursor = db3.rawQuery("select name from Warehouse",
                null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private String getLatestDbilldate() throws ParseException {
        Cursor cursor = db3.rawQuery("select dbilldate from OtherOutgoing order by dbilldate desc", null);
        ArrayList<String> date = new ArrayList<String>();
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                date.add(cursor.getString(cursor.getColumnIndex("dbilldate")));
            }
        } else {
            date.add(iUrl.begintime);
        }
        //加1秒
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datetemp = df.parse(date.get(0));
        datetemp.setTime(datetemp.getTime() + 1000);
        String addone = df.format(datetemp);
        return addone;
    }
   private boolean isPobillcodeExist=false;
    private void insertDownloadDataToDB(OtherOutgoingQuery outGoingBean) {
        List<OtherOutgoingQuery.DataBean> outGoingBeanList = outGoingBean.getData();
        for (OtherOutgoingQuery.DataBean ob : outGoingBeanList) {
            String pobillcode = ob.getPobillcode();
            String cwarecode = ob.getCwarecode();
            String cwarename = ob.getCwarename();
            String dbilldate = ob.getDbilldate();
            String dr = ob.getDr();
            isPobillcodeExist=isPobillcodeExist(pobillcode);
            if("0".equals(dr)&& isPobillcodeExist){
                continue;
            }
            //等于1时
            if("1".equals(dr)|| ("2".equals(dr)&&isPobillcodeExist))
            {
//                ContentValues contentupdateValues = new ContentValues();
//                contentupdateValues.put("dr", dr);
//                //操作选择一 将现有单据的dr = 0 更改为dr= 1
//                db3.update("OtherOutgoing",contentupdateValues,"pobillcode=?", new String[]{pobillcode});
                //操作选择二 通过单号删除
                //删除三张表
                db3.beginTransaction();
                try {
                    db3.delete("OtherOutgoing", "pobillcode=?", new String[]{pobillcode});
                    db3.delete("OtherOutgoingBody", "pobillcode=?", new String[]{pobillcode});
                  //  db3.delete("OtherOutgoingScanResult", "pobillcode=?", new String[]{pobillcode});
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
            List<OtherOutgoingQuery.DataBean.BodyBean> outGoingDatabodyList = ob.getBody();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            for (OtherOutgoingQuery.DataBean.BodyBean obb : outGoingDatabodyList) {
                String materialcode = obb.getMaterialcode();
                String maccode = obb.getMaccode();
                int nnum = obb.getNnum();
                String pch = obb.getPch();
                String vcooporderbcode_b = obb.getVcooporderbcode_b();
                String scannum = countScannedQRCode(pobillcode, materialcode, vcooporderbcode_b);
                //这里应该执行的是插入第二个表的操作
                ContentValues valuesInner = new ContentValues();
                valuesInner.put("pobillcode", pobillcode);
                valuesInner.put("materialcode", materialcode);
                valuesInner.put("maccode", maccode);
                valuesInner.put("nnum", nnum);
                valuesInner.put("pch", pch);
                valuesInner.put("uploadnum", "0");
                valuesInner.put("scannum", scannum);
                valuesInner.put("uploadflag", "N");
                valuesInner.put("vcooporderbcode_b", vcooporderbcode_b);
                db3.insert("OtherOutgoingBody", null, valuesInner);
                valuesInner.clear();
            }
            values.put("pobillcode", pobillcode);
            values.put("cwarecode", cwarecode);
            values.put("cwarename", cwarename);
            values.put("dbilldate", dbilldate);
            values.put("dr", dr);
            values.put("flag", "N");
            // 插入第一条数据
            db3.insert("OtherOutgoing", null, values);
            values.clear();
        }
    }

    private boolean isPobillcodeExist(String pobillcode) {
        Cursor cursor2 = db3.rawQuery("select pobillcode from OtherOutgoing where pobillcode=?", new String[]{pobillcode});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            cursor2.close();
            return true;
        }else {
            return false;
        }
    }

    private String countScannedQRCode(String pobillcode, String materialcode, String vcooporderbcode_b) {
        String count = "0";
            Cursor cursor2 = db3.rawQuery("select count(prodcutcode) from OtherOutgoingScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?", new String[]{pobillcode, materialcode, vcooporderbcode_b});
             cursor2.moveToFirst();
            count = cursor2.getString(0);
             cursor2.close();

        return count;
    }

    private void popupQuery() {
        LayoutInflater layoutInflater = LayoutInflater.from(OtherOutgoing.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditTextOtherOutgoing = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinnerOtherOutgoing = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinnerOutgoing = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        test = queryWarehouseInfo();
        test.add("");
        List<String> uploadflag = new ArrayList();
        uploadflag.add("是");
        uploadflag.add("否");
        final ArrayAdapter adapter = new ArrayAdapter(
                OtherOutgoing.this, android.R.layout.simple_spinner_item, test);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOtherOutgoing.setAdapter(adapter);
        spinnerOtherOutgoing.setSelection(test.size() - 1, true);
        query_cwarename = adapter.getItem(test.size() - 1).toString();
        spinnerOtherOutgoing.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                query_cwarename = adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        final ArrayAdapter adapter2 = new ArrayAdapter(
                OtherOutgoing.this, android.R.layout.simple_spinner_item, uploadflag);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        flag_spinnerOutgoing.setAdapter(adapter2);
        flag_spinnerOutgoing.setSelection(uploadflag.size()-1, true);
        if ("是".equals(adapter2.getItem(uploadflag.size()-1).toString())){
            query_uploadflag = "Y";
        } else {
            query_uploadflag = "N";
        }
        flag_spinnerOutgoing.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ("是".equals(adapter2.getItem(i).toString())){
                    query_uploadflag = "Y";
                } else {
                    query_uploadflag = "N";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog.Builder ad1 = new AlertDialog.Builder(OtherOutgoing.this);
        ad1.setTitle("请输入出库单号:");
        ad1.setView(textEntryView);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                if (query_cwarename == null) {
                    query_cwarename = adapter.getItem(test.size() - 1).toString();
                }
                if(query_uploadflag == null){
                    query_uploadflag = "N";
                }
                bean1 = query(codeNumEditTextOtherOutgoing.getText().toString(), query_cwarename,query_uploadflag);
                listAllPostition = bean1;
                final OtherOutgoingTableAdapter adapter3 = new OtherOutgoingTableAdapter(OtherOutgoing.this,removeDuplicate(bean1), mListener);
                tableListView.setAdapter(adapter3);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter3.select(position);
                        OtherOutgoingBean otherOutgoingBean = (OtherOutgoingBean) adapter3.getItem(position);
                        chosen_line_Pobillcode = otherOutgoingBean.getPobillcode();
                        chosen_line_Cwarename = otherOutgoingBean.getCwarename();
                        chosen_line_Cwarecode = otherOutgoingBean.getCwarecode();
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
    }
    ArrayList<OtherOutgoingBean> bean1;
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
    private   ArrayList<OtherOutgoingBean>  removeDuplicate(ArrayList<OtherOutgoingBean> list)  {

            ArrayList<OtherOutgoingBean>  beanList=new ArrayList<>();
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

    public ArrayList<OtherOutgoingBean> query(String pobillcode, String current_cwarename,String query_uploadflag) {
        ArrayList<OtherOutgoingBean> list = new ArrayList<OtherOutgoingBean>();
            Cursor cursor1 = db3.rawQuery("select otheroutgoing.pobillcode,otheroutgoing.cwarecode,otheroutgoing.cwarename,otheroutgoing.dr, otheroutgoing.dbilldate," +
                    "otheroutgoingbody.materialcode,otheroutgoingbody.maccode,otheroutgoingbody.maccode,otheroutgoingbody.nnum, otheroutgoingscanresult.prodcutcode," +
                    "otheroutgoingscanresult.xlh" + " from otheroutgoing left join otheroutgoingbody on otheroutgoing.pobillcode=otheroutgoingbody.pobillcode " +
                    "left join otheroutgoingscanresult on otheroutgoingbody.pobillcode=otheroutgoingscanresult.pobillcode " +
                    "and otheroutgoingbody.vcooporderbcode_b=otheroutgoingscanresult.vcooporderbcode_b where flag=? and otheroutgoing.pobillcode" +
                    " like '%" + pobillcode + "%' and otheroutgoing.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});
            if (cursor1 != null && cursor1.getCount() > 0) {
                //判断cursor中是否存在数据
                while (cursor1.moveToNext()) {
                    OtherOutgoingBean bean = new OtherOutgoingBean();
                    bean.pobillcode = cursor1.getString(cursor1.getColumnIndex("pobillcode"));
                    bean.cwarecode = cursor1.getString(cursor1.getColumnIndex("cwarecode"));
                    bean.cwarename = cursor1.getString(cursor1.getColumnIndex("cwarename"));
                    bean.dbilldate = cursor1.getString(cursor1.getColumnIndex("dbilldate"));
                    bean.dr = cursor1.getInt(cursor1.getColumnIndex("dr"));
                    bean.setMaterialcode(cursor1.getString(cursor1.getColumnIndex("materialcode")));
                    bean.setMaccode(cursor1.getString(cursor1.getColumnIndex("maccode")));
                    bean.setNnum(cursor1.getString(cursor1.getColumnIndex("nnum")));
                    bean.setProdcutcode(cursor1.getString(cursor1.getColumnIndex("prodcutcode")));
                    bean.setXlh(cursor1.getString(cursor1.getColumnIndex("xlh")));
                    list.add(bean);
                }
                cursor1.close();
            }

        return list;
    }

    private boolean queryCwarename(String current_cwarename, String pobillcode) {

        Cursor cursor = db3.rawQuery("select pobillcode from OtherOutgoing where pobillcode =? and cwarename=?", new String[]{pobillcode, current_cwarename});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
            }
            cursor.close();
            return true;
        }
        return false;
    }

    public ArrayList<OtherOutgoingBean> queryAll() {
        ArrayList<OtherOutgoingBean> list = new ArrayList<OtherOutgoingBean>();
        List<String> list_update = new ArrayList<String>();
    //    String sql2 = "select " + "pobillcode" + "," + "dbilldate" + "," + "cwarecode" + "," + "cwarename" + "," + "dr" + " from " + "OtherOutgoing";//注意：这里有单引号
      //  Cursor cursor = db3.rawQuery(sql2, null);
        Cursor cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr from OtherOutgoing where flag=? order by dbilldate desc", new String[]{"N"});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherOutgoingBean bean = new OtherOutgoingBean();
                bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                bean.cwarecode = cursor.getString(cursor.getColumnIndex("cwarecode"));
                bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                if (bean.dr == 1) {
                    list_update.add(bean.pobillcode);
                    if (null == list || list.size() == 0) {
                    } else {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getPobillcode().equals(bean.pobillcode)) {
                                list.remove(i);
                            }
                        }
                    }
                } else {
                    if (null == list_update || list_update.size() == 0) {
                        list.add(bean);
                    } else {
                        if (list_update.contains(bean.pobillcode)) {
                        } else {
                            list.add(bean);
                        }
                    }
                }
            }
            cursor.close();
        }
        return list;
    }

    public ArrayList<OtherOutgoingBean> displayAll() {
        ArrayList<OtherOutgoingBean> list = new ArrayList<OtherOutgoingBean>();
        Cursor cursor = db3.rawQuery("select pobillcode,dbilldate,cwarecode,cwarename,dr from OtherOutgoing order by dbilldate desc", null);
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherOutgoingBean bean = new OtherOutgoingBean();
                bean.pobillcode = cursor.getString(cursor.getColumnIndex("pobillcode"));
                bean.cwarecode = cursor.getString(cursor.getColumnIndex("cwarecode"));
                bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr = cursor.getInt(cursor.getColumnIndex("dr"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * webservice查询下载
     */
    public String downloadDatabase(String workCode, String pagenum) throws Exception {
        String WSDL_URI;
        String namespace;
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
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestOtherOutgoingTSInfo", 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", iUrl.begintime);
        String endtime = getDefaultEndTime();
        CommonSendBean userSend = new CommonSendBean(begintime, endtime, pagenum, "0");
        Gson gson = new Gson();
        String userSendBean = gson.toJson(userSend);
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
        otherOutgoingDataResp = object.getProperty(0).toString();
        return otherOutgoingDataResp;
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

    /**
     * 实现类，响应按钮点击事件
     */
    private OtherOutgoingTableAdapter.MyClickListener mListener = new OtherOutgoingTableAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            //  Toast.makeText(OtherOutgoing.this,listAllPostition.get(position).getPobillcode(),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OtherOutgoing.this, OtherOutgoingDetail.class);
            intent.putExtra("current_pobillcode", listAllPostition.get(position).getPobillcode());
            intent.putExtra("current_cwarename", listAllPostition.get(position).getCwarename());
            intent.putExtra("current_cwarecode", listAllPostition.get(position).getCwarecode());
            intent.putExtra("current_dbilldate", listAllPostition.get(position).getDbilldate());
            startActivity(intent);
        }
    };
}
