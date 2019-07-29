package com.example.shanggmiqr.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.LoanScannerAdapter;
import com.example.shanggmiqr.bean.SaleDeliveryScanResultBean;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class LoanQrScanner extends AppCompatActivity {
    //出库扫描

    private String current_vcooporderbcode_b_qrRecv;
    private String current_maccode_qrRecv;
    private String current_matrcode_qrRecv;
    private String current_customer_qrRecv;
    private String current_cwarename_qrRecv;
    private int current_nnum_qrRecv;
    private String current_vbillcode_qrRecv;

    private EditText plateCodeEditText;
    private EditText boxCodeEditText;
    private List<String> boxCodeEditTextContent;
    private EditText productCodeEditText;
    private Button scanCheckButton;
    private SQLiteDatabase db5;
    private MyDataBaseHelper helper5;
    private ListView tableBodyListView;
    private int count;//用于合格条码计数
    private Handler mHandler = null;
    private Spinner spinner;
    private Myadapter myadapter;
    private List<String> cars;
    private TextView scannnumText;
    private TextView qrcode_xm_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_qr_scanner);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        helper5 = new MyDataBaseHelper(LoanQrScanner.this, "ShangmiData", null, 1);
        scanCheckButton = (Button) findViewById(R.id.loan_ok_scanner);
        plateCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.loan_platecode_scanner);
        boxCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.loan_boxcode_scanner);
        productCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.loan_productcode_scanner);
        scannnumText = (TextView) findViewById(R.id.loan_scannednum_text);
        qrcode_xm_Text = (TextView)findViewById(R.id.qrcode_xm_loan);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_vcooporderbcode_b_qrRecv = _intent.getStringExtra("current_vcooporderbcode_b_qrRecv");
        //    current_matrname_qrRecv = _intent.getStringExtra("current_matrname_qrRecv");
            current_cwarename_qrRecv = _intent.getStringExtra("current_cwarename_qrRecv");
            current_matrcode_qrRecv = _intent.getStringExtra("current_matrcode_qrRecv");
            current_maccode_qrRecv = _intent.getStringExtra("current_maccode_qrRecv");
            current_customer_qrRecv = _intent.getStringExtra("current_customer_qrRecv");
            current_nnum_qrRecv = Integer.parseInt(_intent.getStringExtra("current_nnum_qrRecv"));
            current_vbillcode_qrRecv = _intent.getStringExtra("current_vbillcode_qrRecv");
        }
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db5 = helper5.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_loan_qrdetail_scanner);
        scannnumText.setText("已扫码数量：" + countSum());
        spinner = findViewById(R.id.loan_spinner_scanner_text);
        myadapter();
        List<SaleDeliveryScanResultBean> list = showScannedQR();
        LoanScannerAdapter adapter = new LoanScannerAdapter(LoanQrScanner.this, list, mListener2);
        tableBodyListView.setAdapter(adapter);
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
        scanCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //截取产品码的第五位到第九位，查看是否与物料大类匹配
                getData();

            }
        });

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x11:
                        //mlogInButton.setEnabled(true);
                        Toast.makeText(LoanQrScanner.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x12:

                        SharedPreferences sharedPreferences12 = getSharedPreferences("UserInfo", 0);
                        //getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
                        String showTest12 = sharedPreferences12.getString("userinfo", "");
                        Toast.makeText(LoanQrScanner.this, "用户数据下载成功", Toast.LENGTH_LONG).show();
                        break;
                    case 0x13:

                        boxCodeEditText.requestFocus();
                        boxCodeEditText.setFocusable(true);
                        List<SaleDeliveryScanResultBean> list = showScannedQR();
                        LoanScannerAdapter adapter = new LoanScannerAdapter(LoanQrScanner.this, list, mListener2);
                        tableBodyListView.setAdapter(adapter);

                        int current_scanSum =countSum();
                        scannnumText.setText("已扫码数量：" + current_scanSum);
                        DataHelper.updateScannum(db5,current_scanSum,current_vbillcode_qrRecv,
                                current_vcooporderbcode_b_qrRecv,getIntent().getIntExtra("type",-1));
                        break;
                    case 0x14:
                        Toast.makeText(LoanQrScanner.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                        break;
                    case 0x15:
                        Toast.makeText(LoanQrScanner.this, "请检查服务器连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x18:
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(LoanQrScanner.this, "错误："+exception, Toast.LENGTH_LONG).show();
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

    private void getData() {
        boxCodeEditTextContent= Arrays.asList(boxCodeEditText.getText().toString().split("\\s"));
        qrcode_xm_Text.setText("二维箱码："+boxCodeEditTextContent.size());
        for (int i = 0; i <boxCodeEditTextContent.size() ; i++) {

            String productcode=boxCodeEditTextContent.get(i);

            count = countSum();


            if (count >= Math.abs(current_nnum_qrRecv)) {
                Toast.makeText(LoanQrScanner.this, "已经扫描指定数量", Toast.LENGTH_LONG).show();
                return;
            }

            if (isAlreadyScanned(productcode)) {
                Toast.makeText(LoanQrScanner.this, "此产品码已经扫描过", Toast.LENGTH_LONG).show();
                return;
            }
            if (isCwarenameEmpty()) {
                Toast.makeText(LoanQrScanner.this, "请选择仓库信息", Toast.LENGTH_LONG).show();
                return;
            }
            if(productcode.isEmpty()){
                return;
            }

            if(productcode.length() != DataHelper.getLengthInQrRule(current_maccode_qrRecv,db5)){
                Toast.makeText(LoanQrScanner.this, "条码或二维码错误", Toast.LENGTH_LONG).show();
                return;
            }
            if(!DataHelper.isValidQr(productcode,current_maccode_qrRecv,current_matrcode_qrRecv,db5,getApplicationContext())){
                Toast.makeText(LoanQrScanner.this, "条码不合法", Toast.LENGTH_LONG).show();
                return;
            }

            insertintoTempQrDBForSaleDelivery(productcode);
            boxCodeEditText.setText("");
        }

    }

    private boolean isCwarenameEmpty() {
        Cursor cursornew = db5.rawQuery("select cwarename from LoanBody where pobillcode=? and itempk=?",
                new String[]{current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv});
        if (cursornew != null && cursornew.getCount() > 0) {
            while (cursornew.moveToNext()) {
                String temp = cursornew.getString(cursornew.getColumnIndex("cwarename"));
                if (temp.equals("") || temp.equals("请选择仓库")) {
                    return true;
                } else {
                    return false;
                }
            }
            cursornew.close();
        }
        return false;
    }

    private void myadapter() {
        cars = new ArrayList<>();
        cars = DataHelper.queryWarehouseInfo(db5);
        String default_value = current_cwarename_qrRecv;
        if (default_value.length() == 0) {
            cars.add("请选择仓库");
        } else {
            cars.add(current_cwarename_qrRecv);
        }
        myadapter = new Myadapter(this, R.layout.custom_spinner_layout, cars);
        spinner.setAdapter(myadapter);
        //默认选中最后一项
        spinner.setSelection(cars.size() - 1, true);
        if (!isOriginaWarehouse()) {
            if(!allowChangeWarehousename()) {
                spinner.setEnabled(false);
            }else{
            spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String name = myadapter.getItem(i).toString();
                            updateWarehouseInfo(name, current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            }
        } else {
            spinner.setEnabled(false);
        }

    }

    //原始订单带过来的仓库不许修改，后面手动选择的又没扫描的订单可以修改
    private boolean isOriginaWarehouse() {
        Cursor cursornew = db5.rawQuery("select orginal_cwarename from LoanBody where pobillcode=? and itempk=? and orginal_cwarename=?",
                new String[]{current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv, "Y"});
        if (cursornew != null && cursornew.getCount() > 0) {
            cursornew.close();
            return true;
        }
        return false;
    }

    private boolean allowChangeWarehousename() {
        Cursor cursornew = db5.rawQuery("select prodcutcode from LoanScanResult where pobillcode=? and itempk=?",
                new String[]{current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv});
        if (cursornew != null && cursornew.getCount() > 0) {
            cursornew.close();
            return false;
        }
        return true;
    }

    private void updateWarehouseInfo(String name, String current_vbillcode_qrRecv, String current_vcooporderbcode_b_qrRecv) {
        if (!name.equals("请选择仓库")) {
            db5.execSQL("update LoanBody set cwarename=?  where pobillcode=? and itempk=?", new String[]{name, current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv});
        }
    }



    /**
     * 定义一个Myadapter类继承ArrayAdapter * 重写以下两个方法 *
     */
    class Myadapter<T> extends ArrayAdapter {
        public Myadapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            //返回数据的统计数量，大于0项则减去1项，从而不显示最后一项
            int i = super.getCount();
            return i > 0 ? i - 1 : i;
        }
    }



    public int countSum() {
        Cursor cursor = db5.rawQuery("select * from LoanScanResult where  pobillcode=? and materialcode=? and itempk=?",
                new String[]{current_vbillcode_qrRecv, current_matrcode_qrRecv, current_vcooporderbcode_b_qrRecv});
        while (cursor != null && cursor.getCount() > 0) {
            // db.close();
            //  Log.i(" search_city_name_exist", str + "在数据库已存在,return true");
            return cursor.getCount();// //有城市在数据库已存在，返回true
        }
        return 0;
    }



    private boolean isAlreadyScanned(String s) {
        Cursor cursor = db5.rawQuery("select * from LoanScanResult where pobillcode=? and prodcutcode=? and itempk=?",
                new String[]{current_vbillcode_qrRecv, s, current_vcooporderbcode_b_qrRecv});
        while (cursor != null && cursor.getCount() > 0) {
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    private void insertCountOfScannedQRCode(String scannum) {
        db5.execSQL("update LoanBody set scannum=? where pobillcode=? and materialcode=? and itempk=?", new String[]{scannum, current_vbillcode_qrRecv, current_matrcode_qrRecv, current_vcooporderbcode_b_qrRecv});
    }


    private List<SaleDeliveryScanResultBean> showScannedQR() {
        ArrayList<SaleDeliveryScanResultBean> list = new ArrayList<SaleDeliveryScanResultBean>();

        Cursor cursor = db5.rawQuery("select materialcode,platecode,boxcode,prodcutcode,num,itemuploadflag from LoanScanResult where pobillcode=? and materialcode=? and itempk=?", new String[]{current_vbillcode_qrRecv, current_matrcode_qrRecv, current_vcooporderbcode_b_qrRecv});
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
                if (Utils.isNetworkConnected(LoanQrScanner.this)) {
                    try {
                        //使用 ContentValues 来对要添加的数据进行组装
                        ContentValues values = new ContentValues();
                        // 开始组装第一条数据
                        values.put("pobillcode", current_vbillcode_qrRecv);
                        values.put("itempk", current_vcooporderbcode_b_qrRecv);
                        values.put("materialcode", current_matrcode_qrRecv);
                        values.put("platecode", plateCodeEditText.getText().toString());
                        values.put("boxcode", boxCodeEditText.getText().toString());
                        values.put("prodcutcode", productCodeEditText.getText().toString());
                        values.put("num", current_nnum_qrRecv);
                        values.put("itemuploadflag", "N");
                        values.put("xlh", DataHelper.getXlh(db5,productcode,current_maccode_qrRecv));
                        // 插入第一条数据
                        db5.insert("LoanScanResult", null, values);
                        values.clear();
                        Message msg = new Message();
                        msg.what = 0x13;
                        mHandler.sendMessage(msg);

                    } catch (Exception e) {
                        e.printStackTrace();
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
    private LoanScannerAdapter.ScannerClickListener mListener2 = new LoanScannerAdapter.ScannerClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            List<SaleDeliveryScanResultBean> listDel = showScannedQR();
            if (!isAlreadyUpload(listDel.get(position).getProdcutcode())) {
                db5.execSQL("delete from LoanScanResult where pobillcode=? and prodcutcode=? and itempk=?", new Object[]{current_vbillcode_qrRecv, listDel.get(position).getProdcutcode(), current_vcooporderbcode_b_qrRecv});
                count = countSum();

                insertCountOfScannedQRCode(count+"");
                List<SaleDeliveryScanResultBean> list = showScannedQR();
                LoanScannerAdapter adapter = new LoanScannerAdapter(LoanQrScanner.this, list, mListener2);
                tableBodyListView.setAdapter(adapter);
                scannnumText.setText("已扫码数量：" + count);
            } else {
                Toast.makeText(LoanQrScanner.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
            }
        }
    };

    //判断单个item是否上传过，上传过的不允许再次操作
    private boolean isAlreadyUpload(String prodcutcode) {
        Cursor cursor31 = db5.rawQuery("select itempk  from LoanScanResult",
                new String[]{});
        if (cursor31 != null && cursor31.getCount() > 0) {
            Cursor cursor3 = db5.rawQuery("select itempk  from LoanScanResult where pobillcode=? and itempk=? and prodcutcode=? and itemuploadflag=?",
                    new String[]{current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv, prodcutcode, "N"});
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
