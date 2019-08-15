package com.example.shanggmiqr.transaction;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.SaleDeliveryScannerAdapter;
import com.example.shanggmiqr.bean.OutgoingScanResultBean;
import com.example.shanggmiqr.bean.SaleDeliveryScanResultBean;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class AllocateTransferQrScanner extends AppCompatActivity {
    //出库扫描

    private String current_itempk_qrRecv;
    private String current_scannum_qrRecv;
    private String current_materialcode_qrRecv;
    private String current_cwarehousecode_qrRecv;
    private String current_uploadflag_qrRecv;

    private String current_rwarehousecode_qrRecv;
    private String current_address_qrRecv;
    private String current_materialclasscode_qrRecv;


    private int current_nnum_qrRecv;
    private String current_vbillcode_qrRecv;

    private int current_maccode_rule_itemlength;
    private int current_maccode_rule_startpos;
    private int current_xlh_rule_itemlength;
    private int current_xlh_rule_startpos;
    private int current_qrcode_rule_length = 13;
    private String current_maccode_substring;
    private String current_xlh_substring;
    private String current_material_code;

    private EditText plateCodeEditText;
    private String plateCodeEditTextContent;
    private EditText boxCodeEditText;

    private EditText productCodeEditText;
    private String productCodeEditTextContent;
    private Button scanCheckButton;
    private SQLiteDatabase db5;
    private MyDataBaseHelper helper5;
    private ListView tableBodyListView;
    private int count;//用于合格条码计数
    private OutgoingScanResultBean outgoingScanResultBean;
    private Handler mHandler = null;
    private boolean isSuccess = false;
    private List<String> cars;
    private String temp_code;
    private TextView scannnumText;
    //用true代表托盘码箱码非空且不需要更新只需要更新产品码的状态，false代表第一次进入此页面或者箱码托盘码有更新
    private boolean scanStatus = true;
    private String maccode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocate_transfer_qr_scanner);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        helper5 = new MyDataBaseHelper(AllocateTransferQrScanner.this, "ShangmiData", null, 1);
        scanCheckButton = (Button) findViewById(R.id.allocatetransfer_ok_scanner);
        plateCodeEditText = (EditText) findViewById(R.id.allocatetransfer_platecode_scanner);
        boxCodeEditText = (EditText) findViewById(R.id.allocatetransfer_boxcode_scanner);
        productCodeEditText = (EditText) findViewById(R.id.allocatetransfer_productcode_scanner);
        scannnumText = (TextView) findViewById(R.id.allocatetransfer_scannednum_text);
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            current_itempk_qrRecv = intent.getStringExtra("current_itempk_qrRecv");
            current_nnum_qrRecv =  Integer.parseInt(intent.getStringExtra("current_nnum_qrRecv"));
            current_scannum_qrRecv = intent.getStringExtra("current_scannum_qrRecv");
            current_materialcode_qrRecv = intent.getStringExtra("current_materialcode_qrRecv");
            current_cwarehousecode_qrRecv = intent.getStringExtra("current_cwarehousecode_qrRecv");
            current_rwarehousecode_qrRecv = intent.getStringExtra("current_rwarehousecode_qrRecv");
            current_address_qrRecv = intent.getStringExtra("current_address_qrRecv");
            current_materialclasscode_qrRecv = intent.getStringExtra("current_materialclasscode_qrRecv");
            current_uploadflag_qrRecv = intent.getStringExtra("current_uploadflag_qrRecv");
            current_vbillcode_qrRecv = intent.getStringExtra("current_vbillcode_qrRecv");
            maccode=intent.getStringExtra("maccode");
        }
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db5 = helper5.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_allocatetransfer_qrdetail_scanner);
        scannnumText.setText("已扫码数量：" + countSum());
        List<SaleDeliveryScanResultBean> list = showScannedQR();
        SaleDeliveryScannerAdapter adapter = new SaleDeliveryScannerAdapter(AllocateTransferQrScanner.this, list, mListener2);
        tableBodyListView.setAdapter(adapter);

        scanCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //截取产品码的第五位到第九位，查看是否与物料大类匹配
              getData();
            }
        });
        productCodeEditText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event!=null&&event.getKeyCode()==KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    scanCheckButton.performClick();
                    return true;
                }
                else
                {
                    return false;

                }
            }

        });
        boxCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mHandler.hasMessages(0x19)){
                    mHandler.removeMessages(0x19);
                }
                mHandler.sendEmptyMessageDelayed(0x19,1000);
            }
        });
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x11:
                        //mlogInButton.setEnabled(true);
                        Toast.makeText(AllocateTransferQrScanner.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x12:

                        SharedPreferences sharedPreferences12 = getSharedPreferences("UserInfo", 0);
                        //getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
                        String showTest12 = sharedPreferences12.getString("userinfo", "");
                        Toast.makeText(AllocateTransferQrScanner.this, "用户数据下载成功", Toast.LENGTH_LONG).show();
                        break;
                    case 0x13:
                        //将数据库的数据显示出来
                        isSuccess = true;
                       boxCodeEditText.setText("");
                        //boxCodeEditText.setText("");
                        //plateCodeEditText.setText("");
                        scannnumText.setText("已扫码数量：" +  countSum());
                        boxCodeEditText.requestFocus();
                        List<SaleDeliveryScanResultBean> list = showScannedQR();
                        SaleDeliveryScannerAdapter adapter = new SaleDeliveryScannerAdapter(AllocateTransferQrScanner.this, list, mListener2);
                        tableBodyListView.setAdapter(adapter);
                        String current_scanSum = countSum()+"";
                        insertCountOfScannedQRCode(current_scanSum);
                        break;
                    case 0x14:
                        Toast.makeText(AllocateTransferQrScanner.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                        break;
                    case 0x15:
                        Toast.makeText(AllocateTransferQrScanner.this, "请检查服务器连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x18:
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(AllocateTransferQrScanner.this, "错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        mHandler.removeMessages(0x19);

                            getData();

                        break;
                    default:
                        break;
                }
            }
        };
    }

    private List<String> boxCodeEditTextContent;
    private void getData() {
        boxCodeEditTextContent= Arrays.asList(boxCodeEditText.getText().toString().split("\\s"));



        for (int i = 0; i <boxCodeEditTextContent.size() ; i++) {

            String productcode=boxCodeEditTextContent.get(i);

            if(i>Math.abs(current_nnum_qrRecv)-1)
            {
                Toast.makeText(AllocateTransferQrScanner.this, "已扫描指定数量", Toast.LENGTH_LONG).show();
                return;

            }



            if (isAlreadyScanned(productcode)) {
                Toast.makeText(AllocateTransferQrScanner.this, "此产品码已经扫描过", Toast.LENGTH_LONG).show();
                return;
            }
            if (isCwarenameEmpty()) {
                Toast.makeText(AllocateTransferQrScanner.this, "请选择仓库信息", Toast.LENGTH_LONG).show();
                return;
            }
            if(productcode.isEmpty()){
                return;
            }
            Log.i("maccode-->",maccode);
            if(productcode.length() != DataHelper.getLengthInQrRule(maccode,db5)){
                Toast.makeText(AllocateTransferQrScanner.this, "条码或二维码错误", Toast.LENGTH_LONG).show();
                return;
            }
            if(!DataHelper.isValidQr(productcode,maccode,current_materialcode_qrRecv,db5,getApplicationContext())){
                Toast.makeText(AllocateTransferQrScanner.this, "条码不合法", Toast.LENGTH_LONG).show();
                return;
            }

           insertintoTempQrDBForSaleDelivery(productcode);
            boxCodeEditText.setText("");
        }
    }


    private boolean isCwarenameEmpty() {
        Cursor cursornew = db5.rawQuery("select cwarehousecode from AllocateTransferBody where billno=? and itempk=?",
                new String[]{current_vbillcode_qrRecv, current_itempk_qrRecv});

            while (cursornew.moveToNext()) {
                String temp = cursornew.getString(cursornew.getColumnIndex("cwarehousecode"));
                if (temp.equals("") || temp.equals("请选择仓库")) {
                    return true;
                } else {
                    return false;
                }
            }
            cursornew.close();

        return false;
    }




    public int countSum() {
        Cursor cursor = db5.rawQuery("select * from AllocateTransferScanResult where  billno=? and materialcode=? and itempk=?",
                new String[]{current_vbillcode_qrRecv, current_materialcode_qrRecv, current_itempk_qrRecv});
        while (cursor != null && cursor.getCount() > 0) {
            // db.close();
            //  Log.i(" search_city_name_exist", str + "在数据库已存在,return true");
            return cursor.getCount();// //有城市在数据库已存在，返回true
        }
        return 0;
    }



    private boolean isAlreadyScanned(String s) {
        Cursor cursor = db5.rawQuery("select * from AllocateTransferScanResult where billno=? and prodcutcode=? and itempk=?",
                new String[]{current_vbillcode_qrRecv, s, current_itempk_qrRecv});
        while (cursor != null && cursor.getCount() > 0) {
            // db.close();
            //  Log.i(" search_city_name_exist", str + "在数据库已存在,return true");
            if (cursor.getCount() > 0) {
                return true;
            }// //有城市在数据库已存在，返回true
        }
        return false;
    }

    private void insertCountOfScannedQRCode(String scannum) {
        db5.execSQL("update AllocateTransferBody set scannum=? where billno=?  and itempk=?",
                new String[]{scannum, current_vbillcode_qrRecv,current_itempk_qrRecv});
    }


    private List<SaleDeliveryScanResultBean> showScannedQR() {
        ArrayList<SaleDeliveryScanResultBean> list = new ArrayList<SaleDeliveryScanResultBean>();
        //    String sql2 = "select " + "matrcode "  + ","+"platecode" + "," + "boxcode" + "," +  "prodcutcode" + "," + "num" + " from " + "SaleDeliveryScanResult";//注意：这里有单引号
        Cursor cursor = db5.rawQuery("select materialcode,platecode,boxcode,prodcutcode,num,itemuploadflag from AllocateTransferScanResult where billno=? and materialcode=? and itempk=?", new String[]{current_vbillcode_qrRecv, current_materialcode_qrRecv, current_itempk_qrRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                SaleDeliveryScanResultBean bean = new SaleDeliveryScanResultBean();

                bean.matrcode = cursor.getString(cursor.getColumnIndex("materialcode"));
                bean.platecode = cursor.getString(cursor.getColumnIndex("platecode"));
                bean.boxcode = cursor.getString(cursor.getColumnIndex("boxcode"));
                bean.prodcutcode = cursor.getString(cursor.getColumnIndex("prodcutcode"));
                bean.num = cursor.getString(cursor.getColumnIndex("num"));
                bean.itemuploadflag = cursor.getString(cursor.getColumnIndex("itemuploadflag"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    private void insertintoTempQrDBForSaleDelivery(final String productcode) {
//插入临时数据库保持条码信息并显示在此页面

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkConnected(AllocateTransferQrScanner.this)) {
                    try {
                        //使用 ContentValues 来对要添加的数据进行组装
                        ContentValues values = new ContentValues();
                        // 开始组装第一条数据
                        values.put("billno", current_vbillcode_qrRecv);
                        values.put("itempk", current_itempk_qrRecv);
                        values.put("materialcode", current_materialcode_qrRecv);
                        values.put("platecode", "");
                        values.put("boxcode", "");
                        values.put("prodcutcode", productcode);
                        values.put("num", "1");
                        values.put("itemuploadflag", "N");
                        values.put("xlh", DataHelper.getXlh(db5,productcode,maccode));
                        // 插入第一条数据
                        db5.insert("AllocateTransferScanResult", null, values);
                        values.clear();
                        Message msg = new Message();
                        msg.what = 0x13;
                        mHandler.sendMessage(msg);

                    } catch (Exception e) {
                        //e.printStackTrace();
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception", e.toString());
                        Message msg = new Message();
                        msg.what = 0x18;
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);
                    }
                } else {
                    Message msg = new Message();
                    msg.what = 0x10;
                    mHandler.sendMessage(msg);
                }
            }
        }).start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private SaleDeliveryScannerAdapter.ScannerClickListener mListener2 = new SaleDeliveryScannerAdapter.ScannerClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            List<SaleDeliveryScanResultBean> listDel = showScannedQR();
            if (!isAlreadyUpload(listDel.get(position).getProdcutcode())) {
                db5.execSQL("delete from AllocateTransferScanResult where billno=? and prodcutcode=? and itempk=?", new Object[]{current_vbillcode_qrRecv, listDel.get(position).getProdcutcode(), current_itempk_qrRecv});
                count = countSum();
                String current_scanSum =  countSum()+"";
                insertCountOfScannedQRCode(current_scanSum);
                List<SaleDeliveryScanResultBean> list = showScannedQR();
                SaleDeliveryScannerAdapter adapter = new SaleDeliveryScannerAdapter(AllocateTransferQrScanner.this, list, mListener2);
                tableBodyListView.setAdapter(adapter);
                scannnumText.setText("已扫码数量：" +  countSum());
            } else {
                Toast.makeText(AllocateTransferQrScanner.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
            }
        }
    };

    //判断单个item是否上传过，上传过的不允许再次操作
    private boolean isAlreadyUpload(String prodcutcode) {
        Cursor cursor31 = db5.rawQuery("select itempk  from AllocateTransferScanResult",
                new String[]{});
        if (cursor31 != null && cursor31.getCount() > 0) {
            Cursor cursor3 = db5.rawQuery("select itempk  from AllocateTransferScanResult where billno=? and itempk=? and prodcutcode=? and itemuploadflag=?",
                    new String[]{current_vbillcode_qrRecv, current_itempk_qrRecv, prodcutcode, "N"});
            // Cursor cursor3 = db3.rawQuery(sql3, null);
            if (cursor3 != null && cursor3.getCount() > 0) {
                //判断cursor中是否存在数据
                cursor3.close();
                return false;//false代表有未上传的
            }
        } else {
            return false;//数据库为空
        }
        return true;
    }
}
