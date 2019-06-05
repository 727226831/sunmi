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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.BusinessOperation;
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

import java.util.ArrayList;
import java.util.Calendar;
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
                        Toast.makeText(AllocateTransfer.this, "调拨出库单下载完成", Toast.LENGTH_LONG).show();
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
    public void onResume()
    {
        super.onResume();
        //返回之后重新下载
        //downloadDeliveryButton.performClick();
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
                                    String allocateTransferData = downloadDatabase("R42", "1");
                                    if (null == allocateTransferData) {
                                        dialog.dismiss();
                                        return;
                                    }
                                    Gson gson7 = new Gson();
                                    AllocateTransferQuery allocateTransferQuery = gson7.fromJson(allocateTransferData, AllocateTransferQuery.class);
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
                                                Toast.makeText(AllocateTransfer.this, "调拨出库单已经是最新", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } else {
                                        insertDownloadDataToDB(allocateTransferQuery);
                                        for (int pagenum = 2; pagenum <= pagetotal; pagenum++) {
//                                            String saleDeliveryData2 = downloadDatabase("R42", String.valueOf(pagenum));
//                                            AllocateTransferQuery saleDeliveryQuery2 = gson7.fromJson(saleDeliveryData2, AllocateTransferQuery.class);
//                                            insertDownloadDataToDB(saleDeliveryQuery2);
                                        }
                                        Message msg = new Message();
                                        msg.what = 0x11;
                                        allocateTransferHandler.sendMessage(msg);
                                    }
                                    String currentTs = Utils.getCurrentDateTimeNew();
                                    SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestAllocateTransferTSInfo", 0);
                                    SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                                    editor5.putString("latest_download_ts_begintime", currentTs);
                                    editor5.commit();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
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
                Intent intent = new Intent(AllocateTransfer.this, BusinessOperation.class);
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
            Intent intent = new Intent(AllocateTransfer.this, BusinessOperation.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    private void insertDownloadDataToDB(AllocateTransferQuery productEntryQuery) {

        List<AllocateTransferQuery.DataBean> saleDeliveryBeanList = productEntryQuery.getData();
        for (AllocateTransferQuery.DataBean ob : saleDeliveryBeanList) {

            String billno = ob.getBillno();
            String dbilldate = ob.getDbilldate();
            String dr = ob.getDr();
            String ts = ob.getTs();
            String num = ob.getNum();
            String cunitcode = ob.getCunitcode();
            String runitcode = ob.getRunitcode();
            String org = ob.getOrg();
            String headpk =ob.getHeadpk();
            //0:新增-正常下载保持 1：删除，删除对应单据 2：修改，先删除对应单据再保持

            if("0".equals(dr)&& isBillcodeExist(billno)){
                continue;
            }
            //等于1时
            if("1".equals(dr) || ("2".equals(dr)&&isBillcodeExist(billno)))
            {
                //操作选择二 通过单号删除
                //删除三张表
                db3.beginTransaction();
                try {
                    db3.delete("AllocateTransfer", "billno=?", new String[]{billno});
                    db3.delete("AllocateTransferBody", "billno=?", new String[]{billno});
                    db3.delete("AllocateTransferScanResult", "billno=?", new String[]{billno});
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
    }

    private boolean isBillcodeExist(String billno) {
        Cursor cursor2 = db3.rawQuery("select billno from AllocateTransfer where billno=?", new String[]{billno});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            cursor2.close();
            return true;
        }else {
            return false;
        }
    }

    private String getDefaultEndTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day) + " " + "23:59:59";
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

    private void popupQuery() {
        LayoutInflater layoutInflater = LayoutInflater.from(AllocateTransfer.this);
        View textEntryView = layoutInflater.inflate(R.layout.query_outgoing_dialog, null);
        final EditText codeNumEditText = (EditText) textEntryView.findViewById(R.id.codenum);
        final Spinner spinner = (Spinner) textEntryView.findViewById(R.id.warehouse_spinner);
        final Spinner flag_spinner = (Spinner) textEntryView.findViewById(R.id.upload_flag_spinner);
        test = queryWarehouseInfo();
        test.add("");
        uploadflag = new ArrayList();
        uploadflag.add("是");
        uploadflag.add("部分上传");
        uploadflag.add("否");
        final ArrayAdapter adapter = new ArrayAdapter(
                AllocateTransfer.this, android.R.layout.simple_spinner_item, test);
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
                AllocateTransfer.this, android.R.layout.simple_spinner_item, uploadflag);
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
        AlertDialog.Builder ad1 = new AlertDialog.Builder(AllocateTransfer.this);
        ad1.setTitle("出入查询条件:");
        ad1.setView(textEntryView);
        ad1.setPositiveButton("查询", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                String temp=codeNumEditText.getText().toString();
                if(query_cwarename == null){
                    query_cwarename = adapter.getItem(test.size() - 1).toString();
                }
                if(query_uploadflag == null){
                    query_uploadflag = "N";
                }
                ArrayList<AllocateTransferBean> bean1 = query(temp,query_cwarename,query_uploadflag);
                listAllPostition = bean1;
                final AllocateTransferAdapter adapter3 = new AllocateTransferAdapter(AllocateTransfer.this, bean1, mListener);
                tableListView.setAdapter(adapter3);
                tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        adapter3.select(position);
                        AllocateTransferBean saleDelivery1Bean = (AllocateTransferBean) adapter3.getItem(position);
                        chosen_line_vbillcode = saleDelivery1Bean.getBillno();
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
    public ArrayList<AllocateTransferBean> query(String vbillcode,String current_cwarename,String query_uploadflag) {
        ArrayList<AllocateTransferBean> list = new ArrayList<AllocateTransferBean>();

        Cursor cursor = db3.rawQuery("select billno,dbilldate,dr from AllocateTransfer where flag=? and billno like '%" + vbillcode + "%' order by dbilldate desc", new String[]{query_uploadflag});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                AllocateTransferBean bean = new AllocateTransferBean();
                bean.billno = cursor.getString(cursor.getColumnIndex("billcode"));
                bean.dbilldate = cursor.getString(cursor.getColumnIndex("dbilldate"));
                bean.dr= cursor.getInt(cursor.getColumnIndex("dr"));
                if(queryCwarename(current_cwarename, bean.billno)){
                list.add(bean);
                }
            }
            cursor.close();
        }
        return list;
    }

    private boolean queryCwarename(String current_cwarename,String vbillcode) {

        Cursor cursor = db3.rawQuery("select billcode from ProductEntry where cwarename like '%" + current_cwarename + "%'  ", new String[]{vbillcode});
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
        SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestAllocateTransferTSInfo", 0);
        String begintime = latestDBTimeInfo.getString("latest_download_ts_begintime", "2019-02-02 00:00:01");
        String endtime = getDefaultEndTime();
        SharedPreferences currentAccount= getSharedPreferences("current_account", 0);
        String cwhsmanagercode = currentAccount.getString("current_account","");
        CommonSendAllocateBean userSend = new CommonSendAllocateBean(begintime, endtime, cwhsmanagercode,pagenum);
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
        saleDelivDataResp = object.getProperty(0).toString();
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
        Cursor cursor = db3.rawQuery("select billno,dbilldate,dr from AllocateTransfer order by dbilldate desc", null);
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
}
