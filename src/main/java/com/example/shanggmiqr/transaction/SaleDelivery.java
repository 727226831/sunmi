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

import com.example.shanggmiqr.Url.iUrl;
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
import com.zyao89.view.zloading.Z_TYPE;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/9.
 */

public class SaleDelivery extends AppCompatActivity implements OnClickListener {
    private String saleDelivDataResp;
    private Button downloadDeliveryButton;
    private Button querySaleDeliveryButton;
    private Button displayallSaleDeliveryButton;
    private SQLiteDatabase db3;
    private MyDataBaseHelper helper3;
    private Handler saleDeliveryHandler = null;
    private ListView tableListView;
    private List<SaleDeliveryBean> listAllPostition;
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
    private  Button buttonexport;
    private  String begintime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_delivery_manage);
        helper3 = new MyDataBaseHelper(SaleDelivery.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        lst_downLoad_ts = (TextView)findViewById(R.id.last_downLoad_ts);
        //显示最后一次的下载时间
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestSaleDeliveryTSInfo", 0);
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
        List<SaleDeliveryBean> list = querySaleDelivery();
        listAllPostition = list;
        final SaleDeliveryAdapter adapter1 = new SaleDeliveryAdapter(SaleDelivery.this, list, mListener);
        tableListView.setAdapter(adapter1);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter1.select(position);
                SaleDeliveryBean saleDelivery1Bean = (SaleDeliveryBean) adapter1.getItem(position);
                chosen_line_vbillcode = saleDelivery1Bean.getVbillcode();
                chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();

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

                        List<SaleDeliveryBean> list = querySaleDelivery();
                        listAllPostition = list;
                        final SaleDeliveryAdapter adapter = new SaleDeliveryAdapter(SaleDelivery.this, list, mListener);
                        tableListView.setAdapter(adapter);
                        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.select(position);
                                SaleDeliveryBean saleDelivery1Bean = (SaleDeliveryBean) adapter.getItem(position);
                                chosen_line_vbillcode = saleDelivery1Bean.getVbillcode();
                                chosen_line_dbilldate = saleDelivery1Bean.getDbilldate();

                            }
                        });
                        Toast.makeText(SaleDelivery.this, "出库单下载完成", Toast.LENGTH_LONG).show();
                        break;
                    case 0x18:
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(SaleDelivery.this, s, Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:

                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(SaleDelivery.this, "发货单下载异常，错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                    case 0x12:
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
            case R.id.download_sale_delivery:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkConnected(SaleDelivery.this)) {
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
                                    String saleDeliveryData = downloadDatabase("R07", "1");
                                    if (null == saleDeliveryData) {
                                        dialog.dismiss();
                                        return;
                                    }
                                    Gson gson7 = new Gson();
                                    SaleDeliveryQuery saleDeliveryQuery = gson7.fromJson(saleDeliveryData, SaleDeliveryQuery.class);

                                    if (saleDeliveryQuery.getPagetotal() == 1) {
                                        insertDownloadDataToDB(saleDeliveryQuery);
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        saleDeliveryHandler.sendMessage(msg);
                                    } else if (saleDeliveryQuery.getPagetotal() < 1) {

                                        Message msg = new Message();
                                        msg.what = 0x12;
                                        saleDeliveryHandler.sendMessage(msg);

                                    } else {
                                        insertDownloadDataToDB(saleDeliveryQuery);
                                        for (int pagenum = 2; pagenum <= saleDeliveryQuery.getPagetotal(); pagenum++) {
                                            String saleDeliveryData2 = downloadDatabase("R07", String.valueOf(pagenum));
                                            SaleDeliveryQuery saleDeliveryQuery2 = gson7.fromJson(saleDeliveryData2, SaleDeliveryQuery.class);
                                            insertDownloadDataToDB(saleDeliveryQuery2);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        saleDeliveryHandler.sendMessage(msg);
                                    }
                                    String currentTs = saleDeliveryQuery.getTs();
                                    SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestSaleDeliveryTSInfo", 0);
                                    SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                                    editor5.putString("latest_download_ts_begintime", currentTs);
                                    editor5.commit();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

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
                export();
                break;
            case R.id.displayall_sale_delivery:
                List<SaleDeliveryBean> list = displayAllSaleDelivery();
                listAllPostition = list;
                final SaleDeliveryAdapter adapter = new SaleDeliveryAdapter(SaleDelivery.this, list, mListener);
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

    private void export() {
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
                    "物料大类"+"\t"+"序列号"+"\t"+"条形码").getBytes());
            for (int j = 0; j <bean1.size() ; j++) {
                outputStream.write("\r\n".getBytes());

                outputStream.write((bean1.get(j).getVbillcode()+"\t"
                        +bean1.get(j).getDbilldate()+"\t"
                        +bean1.get(j).getMatrcode()+"\t"
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SaleDelivery.this, BusinessOperation.class);
                startActivity(intent);
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //点击完返回键，执行的动作
            Intent intent = new Intent(SaleDelivery.this, BusinessOperation.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    private boolean isVbillcodeExist=false;
    private void insertDownloadDataToDB(SaleDeliveryQuery saleDeliveryQuery) {

        List<SaleDeliveryQuery.DataBean> saleDeliveryBeanList = saleDeliveryQuery.getData();
        for (SaleDeliveryQuery.DataBean ob : saleDeliveryBeanList) {

            String vtrantypecode = ob.getVtrantypecode();
            String unitcode = ob.getUnitcode();
            String busitypecode = ob.getBusitypecode();
            String vbillcode = ob.getVbillcode();
            String dbilldate = ob.getDbilldate();
            String deptcode = ob.getDeptcode();
            String pupsndoccode = ob.getPupsndoccode();
            String transporttypecode = ob.getTransporttypecode();
            String billmakercode = ob.getBillmakercode();
            String country = ob.getCountry();
            String vmemo = ob.getVmemo();
            String dr =ob.getDr();
            //0:新增-正常下载保持 1：删除，删除对应单据 2：修改，先删除对应单据再保持
             isVbillcodeExist=isVbillcodeExist(vbillcode);
            if("0".equals(dr)&& isVbillcodeExist){
                continue;
            }

            //等于1时
            if("1".equals(dr) || ("2".equals(dr)&&isVbillcodeExist))
            {
                //操作选择二 通过单号删除
                //删除三张表

                try {
                    db3.beginTransaction();
                    db3.delete("SaleDelivery", "vbillcode=?", new String[]{vbillcode});
                    db3.delete("SaleDeliveryBody", "vbillcode=?", new String[]{vbillcode});
                   // db3.delete("SaleDeliveryScanResult", "vbillcode=?", new String[]{vbillcode});
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
            List<SaleDeliveryQuery.DataBean.BodysBean> saleDeliveryDatabodysList = ob.getBodys();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();

            for (SaleDeliveryQuery.DataBean.BodysBean obb : saleDeliveryDatabodysList) {
                String vcooporderbcode_b = obb.getVcooporderbcode_b();
                String matrcode = obb.getMatrcode();
                String matrname = obb.getMatrname();
                String maccode = obb.getMaccode();
                int nnum = obb.getNnum();
                String rackcode = obb.getRackcode();
                String customer = obb.getCustomer();
                String cwarehousecode = obb.getCwarehousecode();
                String cwarename = getCwarename(obb.getCwarehousecode());
                String orginal_cwarename = existOriginalCwarename(obb.getCwarehousecode());
                String scannum = countScannedQRCode(vbillcode,vcooporderbcode_b, matrcode);
                //这里应该执行的是插入第二个表的操作
                ContentValues valuesInner = new ContentValues();
                valuesInner.put("vbillcode", vbillcode);
                valuesInner.put("vcooporderbcode_b", vcooporderbcode_b);
                valuesInner.put("matrcode", matrcode);
                valuesInner.put("matrname", matrname);
                valuesInner.put("maccode", maccode);
                valuesInner.put("nnum", nnum);
                valuesInner.put("scannum", scannum);
                valuesInner.put("rackcode", rackcode);
                valuesInner.put("customer", customer);
                valuesInner.put("cwarehousecode", cwarehousecode);
                valuesInner.put("cwarename", cwarename);
                valuesInner.put("orginal_cwarename", orginal_cwarename);
                //N代表尚未上传
                valuesInner.put("uploadflag", "N");
                db3.insert("SaleDeliveryBody", null, valuesInner);
                valuesInner.clear();
            }
            values.put("vtrantypecode", vtrantypecode);
            values.put("unitcode", unitcode);
            values.put("busitypecode", busitypecode);
            values.put("vbillcode", vbillcode);
            values.put("dbilldate", dbilldate);
            values.put("deptcode", deptcode);
            values.put("Pupsndoccode", pupsndoccode);
            values.put("Transporttypecode", transporttypecode);
            values.put("billmakercode", billmakercode);
            values.put("country", country);
            values.put("dr", dr);
            values.put("flag", "N");
            values.put("vmemo", vmemo);
            // 插入第一条数据
            db3.insert("SaleDelivery", null, values);
            values.clear();
        }
    }

    private boolean isVbillcodeExist(String vbillcode) {
        Cursor cursor2 = db3.rawQuery("select vbillcode from SaleDelivery where vbillcode=?", new String[]{vbillcode});
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

    private String countScannedQRCode(String vbillcode,String vcooporderbcode_b, String matrcode) {
        String count = "0";
        Cursor cursor2 = db3.rawQuery("select count(prodcutcode) from SaleDeliveryScanResult where vbillcode=? and matrcode=? and vcooporderbcode_b=? ", new String[]{vbillcode, matrcode,vcooporderbcode_b});
        cursor2.moveToFirst();
        count = cursor2.getString(0);
        cursor2.close();
        return count;
    }

  ArrayList<SaleDeliveryBean> bean1;
    private void popupQuery() {
        LayoutInflater layoutInflater = LayoutInflater.from(SaleDelivery.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditText = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinner = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinner = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        final Button showdailogTwo = (Button)  textEntryView.findViewById(R.id.showdailogTwo);
        time = (TextView)  textEntryView.findViewById(R.id.timeshow_saledelivery);
        SharedPreferences currentTimePeriod= getSharedPreferences("query_saledelivery", 0);
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
                SaleDelivery.this, android.R.layout.simple_spinner_item, test);
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
                SaleDelivery.this, android.R.layout.simple_spinner_item, uploadflag);
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
        AlertDialog.Builder ad1 = new AlertDialog.Builder(SaleDelivery.this);
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
                bean1 = queryexport(temp,query_cwarename,query_uploadflag);

                listAllPostition = bean1;


                adapter3 = new SaleDeliveryAdapter(SaleDelivery.this, removeDuplicate(bean1), mListener);

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
    SaleDeliveryAdapter adapter3;
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

    public ArrayList<SaleDeliveryBean> queryexport(String vbillcode,String current_cwarename,String query_uploadflag) {
        ArrayList<SaleDeliveryBean> list = new ArrayList<SaleDeliveryBean>();
        SharedPreferences currentTimePeriod= getSharedPreferences("query_saledelivery", 0);
        String start_temp = currentTimePeriod.getString("starttime", iUrl.begintime);
        String end_temp = currentTimePeriod.getString("endtime", Utils.getDefaultEndTime());

        Cursor cursor = db3.rawQuery("select saledelivery.vbillcode, saledelivery.dbilldate,saledeliverybody.matrcode,saledelivery.dr," +
                "saledeliverybody.matrname,saledeliverybody.maccode,saledeliverybody.nnum, saledeliveryscanresult.prodcutcode," +
                "saledeliveryscanresult.xlh" + " from saledelivery inner join saledeliverybody on saledelivery.vbillcode=saledeliverybody.vbillcode " +
                "inner join saledeliveryscanresult on saledeliverybody.vbillcode=saledeliveryscanresult.vbillcode " +
                "and saledeliverybody.vcooporderbcode_b=saledeliveryscanresult.vcooporderbcode_b where flag=? and saledelivery.vbillcode" +
                " like '%" + vbillcode + "%' and saledeliverybody.cwarename"+ " like '%" + current_cwarename + "%' order by dbilldate desc", new String[]{query_uploadflag});

        if (cursor != null && cursor.getCount() > 0) {
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

                if (queryTimePeriod(bean.vbillcode,start_temp,end_temp)) {
                    list.add(bean);
                }

            }
            cursor.close();
        }
        return list;
    }

    private boolean queryTimePeriod(String vbillcode,String startTime,String endTime) {
        Cursor cursor = db3.rawQuery("SELECT * FROM SaleDelivery WHERE vbillcode =? and " +
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

    private boolean queryCwarename(String current_cwarename,String vbillcode) {

        Cursor cursor = db3.rawQuery("select vbillcode from SaleDeliveryBody where vbillcode =? and cwarename like '%" + current_cwarename + "%'  ", new String[]{vbillcode});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
            }
            cursor.close();
            return true;
        }
        return false;
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

        String endtime = getDefaultEndTime();
        CommonSendNoPagetotalBean userSend = new CommonSendNoPagetotalBean(begintime, endtime, pagenum);
        Gson gson = new Gson();
        String userSendBean = gson.toJson(userSend);
        request.addProperty("string", workCode);
        request.addProperty("string1", userSendBean);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.bodyOut = request;
        envelope.dotNet = false;

        HttpTransportSE se = new HttpTransportSE(WSDL_URI);

        se.call(namespace + "sendToWISE", envelope);
        // 获取返回的数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回的结果
        saleDelivDataResp = object.getProperty(0).toString();
        Log.i("sale data->",saleDelivDataResp);
        return saleDelivDataResp;
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

    public ArrayList<SaleDeliveryBean> querySaleDelivery() {
        ArrayList<SaleDeliveryBean> list = new ArrayList<SaleDeliveryBean>();
        List<String> list_update = new ArrayList<String>();

        Cursor cursor = db3.rawQuery("select vbillcode,dbilldate,dr from SaleDelivery where flag=? order by dbilldate desc", new String[]{"N"});
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
            //  Toast.makeText(OtherOutgoing.this,listAllPostition.get(position).getPobillcode(),Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SaleDelivery.this, SaleDeliveryDetail.class);
            intent.putExtra("current_sale_delivery_vbillcode", listAllPostition.get(position).getVbillcode());
            intent.putExtra("current_sale_delivery_dbilldate", listAllPostition.get(position).getDbilldate());
            startActivity(intent);

        }
    };
}
