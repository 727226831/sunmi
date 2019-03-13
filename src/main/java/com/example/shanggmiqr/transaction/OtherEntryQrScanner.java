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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.OtherEntryScannerAdapter;
import com.example.shanggmiqr.bean.OtherEntryScanResultBean;
import com.example.shanggmiqr.bean.QrcodeRule;
import com.example.shanggmiqr.util.MyDataBaseHelper;

import java.util.ArrayList;
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
    private boolean isSuccess = false;

    private int current_maccode_rule_itemlength;
    private int current_maccode_rule_startpos;
    private int current_xlh_rule_itemlength;
    private int current_xlh_rule_startpos;
    private int current_qrcode_rule_length = 13;
    private String current_maccode_substring;
    private String current_xlh_substring;
    private String current_material_code;
    private boolean scanStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_entry_qr_scanner);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        helper5 = new MyDataBaseHelper(OtherEntryQrScanner.this, "ShangmiData", null, 1);
        scanCheckButton = (Button) findViewById(R.id.otherentry_ok_scanner);
        plateCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.otherentry_platecode_scanner);
        boxCodeEditText = (scut.carson_ho.diy_view.SuperEditText) findViewById(R.id.otherentry_boxcode_scanner);
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
        scannumText.setText("已扫码数量: " + countScannedQRCode(current_pobillcode_qrRecv, current_materialcode_qrRecv));
        List<OtherEntryScanResultBean> list = showScannedQR();
        OtherEntryScannerAdapter adapter = new OtherEntryScannerAdapter(OtherEntryQrScanner.this, list, mListener21);
        tableBodyListView.setAdapter(adapter);

        scanCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //截取产品码的第五位到第九位，查看是否与物料大类匹配
                count = countSum();
                if ((!isAlreadyScanned(productCodeEditText.getText().toString()) && !isEditTextEmpty() && (productCodeEditText.getText().toString().length() == getLengthInQrRule())) && count < Math.abs(current_nnum_qrRecv) && isValidQr()) {
                    InsertintoTempQrDBForOutgoing();
                    scanStatus = true;
                } else if (count >= Math.abs(current_nnum_qrRecv)) {
                    Toast.makeText(OtherEntryQrScanner.this, "已经扫描指定数量", Toast.LENGTH_LONG).show();
                } else if (isEditTextEmpty()) {
                    Toast.makeText(OtherEntryQrScanner.this, "二维码区域不可以为空", Toast.LENGTH_LONG).show();
                } else if (isAlreadyScanned(productCodeEditText.getText().toString())) {
                    Toast.makeText(OtherEntryQrScanner.this, "此产品码已经扫描过", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(OtherEntryQrScanner.this, "条码或二维码错误", Toast.LENGTH_LONG).show();
                }
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
                        isSuccess = true;
                        productCodeEditText.setText("");
                        //boxCodeEditText.setText("");
                        // plateCodeEditText.setText("");
                        productCodeEditText.requestFocus();
                        List<OtherEntryScanResultBean> list = showScannedQR();
                        OtherEntryScannerAdapter adapter = new OtherEntryScannerAdapter(OtherEntryQrScanner.this, list, mListener21);
                        tableBodyListView.setAdapter(adapter);
                        String current_scanSum = countScannedQRCode(current_pobillcode_qrRecv, current_materialcode_qrRecv);
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
                    default:
                        break;
                }
            }
        };
    }

    private boolean isAlreadyScanned(String s) {
        Cursor cursor = db5.rawQuery("select * from OtherEntryScanResult where pobillcode=? and prodcutcode=? and vcooporderbcode_b=?",
                new String[]{current_pobillcode_qrRecv, s, current_vcooporderbcode_b_qrRecv});
        while (cursor != null && cursor.getCount() > 0) {
            // db.close();
            //  Log.i(" search_city_name_exist", str + "在数据库已存在,return true");
            if (cursor.getCount() > 0) {
                return true;
            }// //有城市在数据库已存在，返回true
        }
        return false;
    }

    public int countSum() {
        Cursor cursor = db5.rawQuery("select * from OtherEntryScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                new String[]{current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv});
        while (cursor != null && cursor.getCount() > 0) {
            // db.close();
            //  Log.i(" search_city_name_exist", str + "在数据库已存在,return true");
            return cursor.getCount();// //有城市在数据库已存在，返回true
        }
        return 0;
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

    private String countScannedQRCode(String pobillcode, String materialcode) {
        String count = "0";
        Cursor cursor2 = db5.rawQuery("select prodcutcode from OtherEntryScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?", new String[]{pobillcode, materialcode, current_vcooporderbcode_b_qrRecv});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
    }

    private void insertCountOfScannedQRCode(String scannum) {
        db5.execSQL("update OtherEntryBody set scannum=? where pobillcode=? and materialcode=? and vcooporderbcode_b=?", new String[]{scannum, current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv});
    }

    private boolean isEditTextEmpty() {

        if (("").equals(productCodeEditText.getText().toString()) || null == productCodeEditText.getText().toString()) {
            return true;// //有城市在数据库已存在，返回false
        }
        return false;
    }

    private List<OtherEntryScanResultBean> showScannedQR() {
        ArrayList<OtherEntryScanResultBean> list = new ArrayList<OtherEntryScanResultBean>();
        Cursor cursor = db5.rawQuery("select platecode,boxcode,prodcutcode,itemuploadflag from OtherEntryScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?", new String[]{current_pobillcode_qrRecv, current_materialcode_qrRecv, current_vcooporderbcode_b_qrRecv});
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
        //String category =productCodeEditText.getText().toString().substring(0,3);
        String scannedMaccode = getCurrentQrcodeRule();
        if (scannedMaccode == null || scannedMaccode.length() == 0) {
            Toast.makeText(OtherEntryQrScanner.this, "请检查物料信息及条码规则数据是否下载，或者是否为有效的条码", Toast.LENGTH_SHORT).show();
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

    private String getCurrentQrcodeRule() {
        Cursor cursor = db5.rawQuery("select * from QrcodeRuleBody where Matbasclasscode=?",
                new String[]{current_maccode_qrRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                QrcodeRule.DataBean.ItemBean bean = new QrcodeRule.DataBean.ItemBean();
                bean.itemlength = cursor.getString(cursor.getColumnIndex("itemlength"));
                bean.startpos = cursor.getString(cursor.getColumnIndex("startpos"));
                bean.appobjattr = cursor.getString(cursor.getColumnIndex("appobjattr"));
                if ("物料条码".equals(bean.appobjattr)) {
                    current_maccode_rule_itemlength = Integer.parseInt(bean.itemlength);
                    current_maccode_rule_startpos = Integer.parseInt(bean.startpos);
                } else if ("序列号".equals(bean.appobjattr)) {
                    current_xlh_rule_itemlength = Integer.parseInt(bean.itemlength);
                    current_xlh_rule_startpos = Integer.parseInt(bean.startpos);
                }
            }
            cursor.close();
            current_maccode_substring = productCodeEditText.getText().toString().substring(current_maccode_rule_startpos - 1, current_maccode_rule_startpos - 1 + current_maccode_rule_itemlength);
            current_xlh_substring = productCodeEditText.getText().toString().substring(current_xlh_rule_startpos - 1, current_xlh_rule_startpos - 1 + current_xlh_rule_itemlength);
        }
        return current_maccode_substring;
    }

    private void InsertintoTempQrDBForOutgoing() {
   //插入临时数据库保持条码信息并显示在此页面

        new Thread(new Runnable() {
            @Override
            public void run() {
                //         if (Utils.isNetworkConnected(OtherEntryQrScanner.this)) {
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
                    values.put("prodcutcode", productCodeEditText.getText().toString());
                    values.put("itemuploadflag", "N");
                    values.put("num", current_nnum_qrRecv);
                    values.put("xlh", current_xlh_substring);
                    // 插入第一条数据
                    db5.insert("OtherEntryScanResult", null, values);
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
                //          } else {
                //      Message msg = new Message();
                //           msg.what = 0x10;
                //          mHandler.sendMessage(msg);
                //      }
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
        Cursor cursor31 = db5.rawQuery("select prodcutcode  from OtherEntryScanResult",
                new String[]{});
        if (cursor31 != null && cursor31.getCount() > 0) {
            Cursor cursor3 = db5.rawQuery("select prodcutcode  from OtherEntryScanResult where pobillcode=? and vcooporderbcode_b=? and materialcode=? and prodcutcode=? and itemuploadflag=?",
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
    private OtherEntryScannerAdapter.ScannerClickListener21 mListener21 = new OtherEntryScannerAdapter.ScannerClickListener21() {
        @Override
        public void myOnClick(int position, View v) {
            List<OtherEntryScanResultBean> listDel = showScannedQR();
            if (!isAlreadyUpload(listDel.get(position).getProdcutcode())) {
                db5.execSQL("delete from OtherEntryScanResult where pobillcode=? and vcooporderbcode_b=? and prodcutcode=?", new Object[]{current_pobillcode_qrRecv, current_vcooporderbcode_b_qrRecv, listDel.get(position).getProdcutcode()});
                count = countSum();
                String current_scanSum = countScannedQRCode(current_pobillcode_qrRecv, current_materialcode_qrRecv);
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
