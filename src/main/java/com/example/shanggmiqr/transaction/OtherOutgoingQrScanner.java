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
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.util.iUntils;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.OtherOutgoingScannerAdapter;
import com.example.shanggmiqr.bean.OtherOutgoingQrDetailBean;
import com.example.shanggmiqr.bean.OutgoingScanResultBean;
import com.example.shanggmiqr.bean.QrcodeRule;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.google.gson.annotations.Until;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class OtherOutgoingQrScanner extends AppCompatActivity {
    //出库扫描
    private TextView pobillcodeText;
    private TextView cwarenameText;
    private TextView materialcodeText;
    private TextView numText;
    private TextView uploadnumText;
    private TextView scannednumText;
    private String current_materialcode_qrRecv;
    private String current_maccode_qrRecv;
    private int current_nnum_qrRecv;
    private String current_uploadnum_qrRecv;
    private String current_pobillcode_qrRecv;
    private String current_cwarename_qrRecv;
    private String current_vcooporderbcode_b_qrRecv;
    private EditText plateCodeEditText;
    private EditText boxCodeEditText;
    private EditText productCodeEditText;
    private Button scanCheckButton;
    private SQLiteDatabase db5;
    private MyDataBaseHelper helper5;
    private ListView tableBodyListView;
    private int count;//用于合格条码计数

    private int current_qrcode_rule_length = 13;
    private String current_material_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_outgoing_qr_scanner);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        helper5 = new MyDataBaseHelper(OtherOutgoingQrScanner.this, "ShangmiData", null, 1);
        scanCheckButton = (Button) findViewById(R.id.outgoing_ok_scanner);
        plateCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.outgoing_platecode_scanner);
        boxCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.outgoing_boxcode_scanner);
        productCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.outgoing_productcode_scanner);
        pobillcodeText = (TextView) findViewById(R.id.outgoing_pbill_scanner_text);
        cwarenameText = (TextView) findViewById(R.id.outgoing_cwarename_scanner_text);
        materialcodeText = (TextView) findViewById(R.id.outgoing_materialcode_scanner_text);
        numText = (TextView) findViewById(R.id.outgoing_num_scanner_text);
        uploadnumText = (TextView) findViewById(R.id.outgoing_uploadnum_scanner_text);
        scannednumText = (TextView) findViewById(R.id.outgoing_scannednum_text);
        boxCodeEditText.setOnKeyListener(onKeyListener);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db5 = helper5.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_qrdetail_scanner);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_pobillcode_qrRecv = _intent.getStringExtra("current_pobillcode_scanner");
            current_cwarename_qrRecv = _intent.getStringExtra("current_cwarename_scanner");
            current_materialcode_qrRecv = _intent.getStringExtra("chosen_line_materialcode_scanner");
            current_maccode_qrRecv = _intent.getStringExtra("chosen_line_maccode_scanner");
            current_nnum_qrRecv = Integer.parseInt(_intent.getStringExtra("chosen_line_nnum_scanner"));
            current_uploadnum_qrRecv = _intent.getStringExtra("chosen_line_uploadnum_scanner");
            current_vcooporderbcode_b_qrRecv = _intent.getStringExtra("chosen_line_vcooporderbcode_b_scanner");
        }
        pobillcodeText.setText("单号:" + current_pobillcode_qrRecv);
        cwarenameText.setText("仓库:" + current_cwarename_qrRecv);
        materialcodeText.setText("物料编码:" + current_materialcode_qrRecv);
        numText.setText("单据数量:" + current_nnum_qrRecv);
        uploadnumText.setText("提交数量:" + current_uploadnum_qrRecv);

        scannednumText.setText("已扫码数量: " + iUntils.countScannedQRCode(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv,
                current_vcooporderbcode_b_qrRecv));
        List<OutgoingScanResultBean> list = showScannedQR();
        OtherOutgoingScannerAdapter adapter = new OtherOutgoingScannerAdapter(OtherOutgoingQrScanner.this, list, mListener21);
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

    }
    View.OnKeyListener onKeyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (v.getId()) {
                    case R.id.outgoing_boxcode_scanner:
                        getData();
                        break;
                }
            }

            return false;
        }
    };
   Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x11:
                    //mlogInButton.setEnabled(true);
                    Toast.makeText(OtherOutgoingQrScanner.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                    break;
                case 0x12:

                    SharedPreferences sharedPreferences12 = getSharedPreferences("UserInfo", 0);
                    //getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
                    String showTest12 = sharedPreferences12.getString("userinfo", "");
                    Toast.makeText(OtherOutgoingQrScanner.this, "用户数据下载成功", Toast.LENGTH_LONG).show();
                    break;
                case 0x13:
                    //将数据库的数据显示出来
                    productCodeEditText.setText("");
                    boxCodeEditText.setText("");
                    boxCodeEditText.requestFocus();


                    List<OutgoingScanResultBean> list = showScannedQR();
                    OtherOutgoingScannerAdapter adapter = new OtherOutgoingScannerAdapter(OtherOutgoingQrScanner.this, list, mListener21);
                    tableBodyListView.setAdapter(adapter);
                    String current_scanSum =  iUntils.countScannedQRCode(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv,
                            current_vcooporderbcode_b_qrRecv);

                    insertCountOfScannedQRCode(current_scanSum);
                    scannednumText.setText("已扫码数量: " + current_scanSum);
                    break;
                case 0x14:
                    Toast.makeText(OtherOutgoingQrScanner.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    break;
                case 0x15:
                    Toast.makeText(OtherOutgoingQrScanner.this, "请检查服务器连接", Toast.LENGTH_LONG).show();
                    break;
                case 0x19:
                    String exception = msg.getData().getString("Exception");
                    Toast.makeText(OtherOutgoingQrScanner.this, "错误："+exception, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
    private  List<String> listcode;
    private void getData() {

        count = iUntils.countSum(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv);
        listcode= Arrays.asList(boxCodeEditText.getText().toString().split("\\s+"));
        scannednumText.setText("已扫码数量："+listcode.size());
        for (int i = 0; i <listcode.size() ; i++) {
            productCodeEditText.setText(listcode.get(i));

            if(iUntils.isAlreadyScanned(db5,current_pobillcode_qrRecv,productCodeEditText.getText().toString(),current_vcooporderbcode_b_qrRecv)){
                Toast.makeText(OtherOutgoingQrScanner.this, "此产品码已经扫描过", Toast.LENGTH_LONG).show();
                return;
            }
            if(count >= Math.abs(current_nnum_qrRecv)){
                Toast.makeText(OtherOutgoingQrScanner.this, "已经扫描指定数量", Toast.LENGTH_LONG).show();
                return;
            }
            if(productCodeEditText.getText().toString().length() != getLengthInQrRule()){
                Toast.makeText(OtherOutgoingQrScanner.this, "条码或二维码错误", Toast.LENGTH_LONG).show();
                return;
            }
            if(!isValidQr()){
                return;
            }

            InsertintoTempQrDBForOutgoing(productCodeEditText.getText().toString());
            boxCodeEditText.setText("");

        }


    }





    private void insertCountOfScannedQRCode(String scannum) {
        db5.execSQL("update OtherOutgoingBody set scannum=? where pobillcode=? and materialcode=? and vcooporderbcode_b=?", new String[]{scannum, current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv});
    }

    private int getLengthInQrRule() {
        Cursor cursor = db5.rawQuery("select length from QrcodeRule where Matbasclasscode=?",
                new String[]{current_maccode_qrRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                current_qrcode_rule_length = cursor.getInt(cursor.getColumnIndex("length"));
            }
            cursor.close();
        }
        return current_qrcode_rule_length;
    }

    private List<OutgoingScanResultBean> showScannedQR() {
        ArrayList<OutgoingScanResultBean> list = new ArrayList<OutgoingScanResultBean>();
        Cursor cursor = db5.rawQuery("select platecode,boxcode,prodcutcode,itemuploadflag from OtherOutgoingScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?", new String[]{current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OutgoingScanResultBean bean = new OutgoingScanResultBean();
                bean.itemuploadflag = cursor.getString(cursor.getColumnIndex("itemuploadflag"));
                bean.platecode = cursor.getString(cursor.getColumnIndex("platecode"));
                bean.boxcode = cursor.getString(cursor.getColumnIndex("boxcode"));
                bean.prodcutcode = cursor.getString(cursor.getColumnIndex("prodcutcode"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    private boolean isValidQr() {
        //String category =productCodeEditText.getText().toString().substring(0,3);

        String scannedMaccode = iUntils.getMaccode(db5,productCodeEditText.getText().toString(),current_maccode_qrRecv);
        if (scannedMaccode == null || scannedMaccode.length() == 0) {
            Toast.makeText(OtherOutgoingQrScanner.this, "请检查物料信息及条码规则数据是否下载，或者是否为有效的条码", Toast.LENGTH_SHORT).show();
            return false;

        }
        Cursor cursor = db5.rawQuery("select code from Material where materialbarcode=?",
                new String[]{scannedMaccode});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                current_material_code = cursor.getString(cursor.getColumnIndex("code"));
                if (current_material_code.equals(current_materialcode_qrRecv)) {
                    return true;
                }

            }
            cursor.close();
        }
        return false;
    }



    private void InsertintoTempQrDBForOutgoing(final String prodcutcode) {
//插入临时数据库保持条码信息并显示在此页面

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkConnected(OtherOutgoingQrScanner.this)) {
                    try {
                        //使用 ContentValues 来对要添加的数据进行组装
                        ContentValues values = new ContentValues();
                        // 开始组装第一条数据
                        values.put("pobillcode", current_pobillcode_qrRecv);
                        values.put("cwarename", current_cwarename_qrRecv);
                        values.put("materialcode", current_materialcode_qrRecv);
                        values.put("vcooporderbcode_b", current_vcooporderbcode_b_qrRecv);
                        values.put("platecode", plateCodeEditText.getText().toString());
                        values.put("boxcode", boxCodeEditText.getText().toString());

                        values.put("prodcutcode", prodcutcode);
                        values.put("itemuploadflag", "N");

                        values.put("xlh", iUntils.getXlh(db5,prodcutcode,current_maccode_qrRecv));
                        values.put("num", current_nnum_qrRecv);
                        // 插入第一条数据
                        db5.insert("OtherOutgoingScanResult", null, values);
                        values.clear();
                        Message msg = new Message();
                        msg.what = 0x13;
                        mHandler.sendMessage(msg);

                    } catch (Exception e) {
                        //e.printStackTrace();
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception", e.toString());
                        Message msg = new Message();
                        msg.what = 0x19;
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

    //下面的方法内容需要根据实际更新
    public ArrayList<OtherOutgoingQrDetailBean> QueryOtherOutgoingBody(String current_pobillcodeRecv) {
        ArrayList<OtherOutgoingQrDetailBean> list = new ArrayList<OtherOutgoingQrDetailBean>();
        Cursor cursor = db5.rawQuery("select platecode,boxcode,prodcutcode from OtherOutgoingScanResult where pobillcode=?", new String[]{current_pobillcodeRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherOutgoingQrDetailBean bean = new OtherOutgoingQrDetailBean();
                bean.platecode = cursor.getString(cursor.getColumnIndex("platecode"));
                bean.boxcode = cursor.getString(cursor.getColumnIndex("boxcode"));
                bean.prodcutcode = cursor.getString(cursor.getColumnIndex("prodcutcode"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
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

    //判断单个item是否上传过，上传过的不允许再次操作
    private boolean isAlreadyUpload(String prodcutcode) {
        Cursor cursor31 = db5.rawQuery("select prodcutcode  from OtherOutgoingScanResult",
                new String[]{});
        if (cursor31 != null && cursor31.getCount() > 0) {
            Cursor cursor3 = db5.rawQuery("select prodcutcode  from OtherOutgoingScanResult where pobillcode=? and vcooporderbcode_b=? and materialcode=? and prodcutcode=? and itemuploadflag=?",
                    new String[]{current_pobillcode_qrRecv, current_vcooporderbcode_b_qrRecv, current_materialcode_qrRecv, prodcutcode, "N"});
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

    /**
     * 实现类，响应按钮点击事件
     */
    private OtherOutgoingScannerAdapter.ScannerClickListener21 mListener21 = new OtherOutgoingScannerAdapter.ScannerClickListener21() {
        @Override
        public void myOnClick(int position, View v) {
            List<OutgoingScanResultBean> listDel = showScannedQR();
            if (!isAlreadyUpload(listDel.get(position).getProdcutcode())) {
                db5.execSQL("delete from OtherOutgoingScanResult where pobillcode=? and vcooporderbcode_b=? and prodcutcode=?", new Object[]{current_pobillcode_qrRecv, current_vcooporderbcode_b_qrRecv, listDel.get(position).getProdcutcode()});
                count = iUntils.countSum(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv);
                String current_scanSum = iUntils.countScannedQRCode(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv,
                        current_vcooporderbcode_b_qrRecv);
                scannednumText.setText("已扫码数量: " + current_scanSum);
                insertCountOfScannedQRCode(current_scanSum);
                List<OutgoingScanResultBean> list = showScannedQR();
                OtherOutgoingScannerAdapter adapter = new OtherOutgoingScannerAdapter(OtherOutgoingQrScanner.this, list, mListener21);
                tableBodyListView.setAdapter(adapter);
            } else {
                Toast.makeText(OtherOutgoingQrScanner.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
            }
        }
    };

}
