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
import com.example.shanggmiqr.adapter.OtherEntryScannerAdapter;
import com.example.shanggmiqr.bean.OtherEntryScanResultBean;
import com.example.shanggmiqr.util.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class OtherEntryQrScanner extends AppCompatActivity {
    //出库扫描
    private TextView pobillcodeText;
    private TextView cwarenameText;
    private TextView materialcodeText;
    private TextView numText;
    private TextView uploadnumText;
    private TextView scannumText;
    private String current_materialcode_qrRecv;
    private String current_maccode_qrRecv;
    private int current_nnum_qrRecv;
    private String current_uploadnum_qrRecv;
    private String current_pobillcode_qrRecv;
    private String current_cwarename_qrRecv;
    private String current_vcooporderbcode_b_qrRecv;
    private EditText plateCodeEditText;
    private String plateCodeEditTextContent;
    private EditText boxCodeEditText;
    private String boxCodeEditTextContent;
    private EditText productCodeEditText;
    private String productCodeEditTextContent;
    private Button scanCheckButton;
    private SQLiteDatabase db5;
    private MyDataBaseHelper helper5;
    private ListView tableBodyListView;
    private int count;//用于合格条码计数
    private Handler mHandler = null;
    private int current_qrcode_rule_length = 13;
    private String current_material_code;

    int type;
    String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_entry_qr_scanner);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        type=getIntent().getIntExtra("type",-1);
        switch (type){
            case 1:
                title="其他入库扫码";
                break;
            case 2:
                title="其他出库扫码";
                break;
        }
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
        helper5 = new MyDataBaseHelper(OtherEntryQrScanner.this, "ShangmiData", null, 1);
        scanCheckButton = (Button) findViewById(R.id.otherentry_ok_scanner);
        plateCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.otherentry_platecode_scanner);
        boxCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.otherentry_boxcode_scanner);
        boxCodeEditText.setOnKeyListener(onKeyListener);
        boxCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mHandler.hasMessages(0x20)){
                    mHandler.removeMessages(0x20);
                }
                mHandler.sendEmptyMessageDelayed(0x20,1000);
            }
        });
        productCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.otherentry_productcode_scanner);
        pobillcodeText = (TextView) findViewById(R.id.otherentry_pbill_scanner_text);
        cwarenameText = (TextView) findViewById(R.id.otherentry_cwarename_scanner_text);
        materialcodeText = (TextView) findViewById(R.id.otherentry_materialcode_scanner_text);
        numText = (TextView) findViewById(R.id.otherentry_num_scanner_text);
        uploadnumText = (TextView) findViewById(R.id.otherentry_uploadnum_scanner_text);
        scannumText = (TextView) findViewById(R.id.otherentry_scannednum_text);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db5 = helper5.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_qrdetail_scanner_otherentry);
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
        scannumText.setText("已扫码数量: " + DataHelper.queryScanResultcount(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv,
                current_vcooporderbcode_b_qrRecv,1));
        List<OtherEntryScanResultBean> list = showScannedQR();
        OtherEntryScannerAdapter adapter = new OtherEntryScannerAdapter(OtherEntryQrScanner.this, list, mListener21);
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

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x11:
                        //mlogInButton.setEnabled(true);
                        Toast.makeText(OtherEntryQrScanner.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x12:

                        SharedPreferences sharedPreferences12 = getSharedPreferences("UserInfo", 0);
                        //getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
                        String showTest12 = sharedPreferences12.getString("userinfo", "");
                        Toast.makeText(OtherEntryQrScanner.this, "用户数据下载成功", Toast.LENGTH_LONG).show();
                        break;
                    case 0x13:
                        //将数据库的数据显示出来

                        productCodeEditText.setText("");
                        boxCodeEditText.setText("");
                        boxCodeEditText.requestFocus();
                        // plateCodeEditText.setText("");

                        List<OtherEntryScanResultBean> list = showScannedQR();
                        OtherEntryScannerAdapter adapter = new OtherEntryScannerAdapter(OtherEntryQrScanner.this, list, mListener21);
                        tableBodyListView.setAdapter(adapter);
                        int current_scanSum =  DataHelper.queryScanResultcount(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv,
                                current_vcooporderbcode_b_qrRecv,type);
                        insertCountOfScannedQRCode(current_scanSum);
                        scannumText.setText("已扫码数量: " + current_scanSum);
                        break;
                    case 0x14:
                        Toast.makeText(OtherEntryQrScanner.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                        break;
                    case 0x15:
                        Toast.makeText(OtherEntryQrScanner.this, "请检查服务器连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        String exception = msg.getData().getString("Exception");
                        Toast.makeText(OtherEntryQrScanner.this, "错误："+exception, Toast.LENGTH_LONG).show();
                        break;
                    case 0x20:
                        mHandler.removeMessages(0x20);
                        getData();
                        break;
                    default:
                        break;
                }
            }
        };
    }
    View.OnKeyListener onKeyListener=new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (v.getId()) {
                    case R.id.otherentry_boxcode_scanner:
                        getData();
                        break;
                }
            }

            return false;
        }
    };
     private List<String> listcode;
    private void getData() {
        Log.i("content-->",boxCodeEditText.getText().toString());
        listcode= Arrays.asList(boxCodeEditText.getText().toString().split("\\s+"));

        for (int i = 0; i <listcode.size() ; i++) {

            productCodeEditText.setText(listcode.get(i));
            count = DataHelper.queryScanResultcount(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv,type);
            if(DataHelper.isAlreadyScanned(db5,current_pobillcode_qrRecv,productCodeEditText.getText().toString(),current_vcooporderbcode_b_qrRecv)){
                Toast.makeText(OtherEntryQrScanner.this, "此产品码已经扫描过", Toast.LENGTH_LONG).show();
                return;
            }

            if(count >= Math.abs(current_nnum_qrRecv)){
                Toast.makeText(OtherEntryQrScanner.this, "已经扫描指定数量", Toast.LENGTH_LONG).show();
                return;
            }
            if(productCodeEditText.getText().toString().isEmpty()){
                return;
            }

            if(productCodeEditText.getText().toString().length() != getLengthInQrRule()){
                Toast.makeText(OtherEntryQrScanner.this, "条码或二维码错误", Toast.LENGTH_LONG).show();
                return;
            }

            if(!isValidQr()){
                Toast.makeText(OtherEntryQrScanner.this, "条码不合法", Toast.LENGTH_LONG).show();
                return;
            }
            InsertintoTempQrDBForOutgoing();
            boxCodeEditText.setText("");
            scannumText.setText("已扫码数量："+count);


        }

    }
    private void insertCountOfScannedQRCode(int scannum) {
        ContentValues contentValues=new ContentValues();
        contentValues.put("scannum",scannum);
        String table="";
        switch (type){
            case 1:
               table="OtherEntryBody";
                break;
            case 2:
                table="OtherOutgoingBody";
                break;

        }
        db5.update(table,contentValues,"pobillcode=? and vcooporderbcode_b=?",
                new String[]{ current_pobillcode_qrRecv,current_vcooporderbcode_b_qrRecv});

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





    private List<OtherEntryScanResultBean> showScannedQR() {
        ArrayList<OtherEntryScanResultBean> list = new ArrayList<OtherEntryScanResultBean>();
        Cursor cursor=null;
        switch (type){
            case 1:
                cursor = db5.rawQuery("select platecode,boxcode,prodcutcode,itemuploadflag from OtherEntryScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv});
                break;
            case 2:
                cursor = db5.rawQuery("select platecode,boxcode,prodcutcode,itemuploadflag from OtherOutgoingScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv});
                break;

        }

        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherEntryScanResultBean bean = new OtherEntryScanResultBean();
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

        String scannedMaccode = DataHelper.getMaccode(db5,productCodeEditText.getText().toString(),current_maccode_qrRecv);
        if (scannedMaccode == null || scannedMaccode.length() == 0) {
            Toast.makeText(OtherEntryQrScanner.this, "请检查物料信息及条码规则数据是否下载，或者是否为有效的条码", Toast.LENGTH_SHORT).show();
            return false;

        }
        Cursor cursor = db5.rawQuery("select code from Material where materialbarcode=?",
                new String[]{scannedMaccode});

            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {

                current_material_code = cursor.getString(cursor.getColumnIndex("code"));

                if (current_material_code.equals(current_materialcode_qrRecv)) {
                    return true;
                }

            }


        return false;
    }



    private void InsertintoTempQrDBForOutgoing() {
   //插入临时数据库保持条码信息并显示在此页面
        Log.i("productcode",productCodeEditText.getText().toString());
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //使用 ContentValues 来对要添加的数据进行组装
                    ContentValues values = new ContentValues();
                    // 开始组装第一条数据
                    values.put("pobillcode", current_pobillcode_qrRecv);
                    values.put("cwarename", current_cwarename_qrRecv);
                    values.put("materialcode", current_materialcode_qrRecv);
                    values.put("vcooporderbcode_b", current_vcooporderbcode_b_qrRecv);
                    values.put("platecode", plateCodeEditText.getText().toString());
                    values.put("boxcode", "");
                    values.put("prodcutcode", productCodeEditText.getText().toString());
                    values.put("itemuploadflag", "N");
                    values.put("num", current_nnum_qrRecv);
                    values.put("xlh", DataHelper.getXlh(db5,productCodeEditText.getText().toString(),current_maccode_qrRecv));
                    // 插入第一条数据
                    String table="";
                    switch (type){
                        case 1:
                            table="OtherEntryScanResult";
                            break;
                        case 2:
                            table="OtherOutgoingScanResult";
                            break;

                    }
                    db5.insert(table, null, values);
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

    //判断单个item是否上传过，上传过的不允许再次操作
    private boolean isAlreadyUpload(String prodcutcode) {
        Cursor cursor31=null;
        switch (type){
            case 1:
                cursor31 = db5.rawQuery("select prodcutcode  from OtherEntryScanResult",
                        new String[]{});

                break;
            case 2:
                cursor31 = db5.rawQuery("select prodcutcode  from OtherOutgoingScanResult",
                        new String[]{});
                break;

        }
        if (cursor31 != null && cursor31.getCount() > 0) {
            Cursor cursor3 =null;
            // Cursor cursor3 = db3.rawQuery(sql3, null);
            switch (type){
                case 1:
                    cursor3 = db5.rawQuery("select prodcutcode  from OtherEntryScanResult where pobillcode=? and vcooporderbcode_b=? and materialcode=? and prodcutcode=? and itemuploadflag=?",
                            new String[]{current_pobillcode_qrRecv, current_vcooporderbcode_b_qrRecv, current_materialcode_qrRecv, prodcutcode, "N"});

                    break;
                case 2:
                    cursor3 = db5.rawQuery("select prodcutcode  from OtherOutgoingScanResult where pobillcode=? and vcooporderbcode_b=? and materialcode=? and prodcutcode=? and itemuploadflag=?",
                            new String[]{current_pobillcode_qrRecv, current_vcooporderbcode_b_qrRecv, current_materialcode_qrRecv, prodcutcode, "N"});
                    break;

            }

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
    private OtherEntryScannerAdapter.ScannerClickListener21 mListener21 = new OtherEntryScannerAdapter.ScannerClickListener21() {
        @Override
        public void myOnClick(int position, View v) {
            List<OtherEntryScanResultBean> listDel = showScannedQR();
            if (!isAlreadyUpload(listDel.get(position).getProdcutcode())) {
                switch (type){
                    case 1:
                        db5.execSQL("delete from OtherEntryScanResult where pobillcode=? and vcooporderbcode_b=? and prodcutcode=?",
                                new Object[]{current_pobillcode_qrRecv, current_vcooporderbcode_b_qrRecv, listDel.get(position).getProdcutcode()});

                        break;
                    case 2:
                        db5.execSQL("delete from OtherOutgoingScanResult where pobillcode=? and vcooporderbcode_b=? and prodcutcode=?",
                                new Object[]{current_pobillcode_qrRecv, current_vcooporderbcode_b_qrRecv, listDel.get(position).getProdcutcode()});
                        break;

                }

                count = DataHelper.queryScanResultcount(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv,type);
                int current_scanSum =  DataHelper.queryScanResultcount(db5,current_pobillcode_qrRecv, current_materialcode_qrRecv,
                        current_vcooporderbcode_b_qrRecv,1);
                insertCountOfScannedQRCode(current_scanSum);
                scannumText.setText("已扫码数量: " + current_scanSum);
                List<OtherEntryScanResultBean> list = showScannedQR();
                OtherEntryScannerAdapter adapter = new OtherEntryScannerAdapter(OtherEntryQrScanner.this, list, mListener21);
                tableBodyListView.setAdapter(adapter);
            } else {
                Toast.makeText(OtherEntryQrScanner.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
            }
        }
    };

}
