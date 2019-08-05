package com.example.shanggmiqr.transaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.AllocateTransferTableQrDetailAdapter;
import com.example.shanggmiqr.bean.AllocateTransferQrDetailBean;
import com.example.shanggmiqr.util.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class AllocateTransferQRDetail extends AppCompatActivity {
    //出库条码明细
    private TextView vcooporderbcode_bText;
    private TextView matrnameText;
    private TextView matrcodeText;
    private TextView numQRText;
    private TextView vbillcodeText;

    private String current_itempk_qrRecv;
    private String current_scannum_qrRecv;
    private String current_materialcode_qrRecv;
    private String current_nnum_qrRecv;
    private String current_materialclasscode_qrRecv;
    private String current_uploadflag_qrRecv;
    private String current_vbillcode_qrRecv;
    private String current_address_qrRecv;
    private String current_cwarehousecode_qrRecv;
    private String current_rwarehousecode_qrRecv;

    private SQLiteDatabase db5;
    private MyDataBaseHelper helper5;
    private List<AllocateTransferQrDetailBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button scanButton;
    private Spinner spinner;
    private List<String> cars;
    private String temp_code;
    private String maccode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocate_transfer_qr_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        helper5 = new MyDataBaseHelper(AllocateTransferQRDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db5 = helper5.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_allocatetransfer_qrdetail);
        Intent intent = getIntent();
        //从Intent当中根据key取得value
        if (intent != null) {
            current_itempk_qrRecv = intent.getStringExtra("current_itempk_qrRecv");
            current_nnum_qrRecv = intent.getStringExtra("current_nnum_qrRecv");
            current_scannum_qrRecv = intent.getStringExtra("current_scannum_qrRecv");
            current_materialcode_qrRecv = intent.getStringExtra("current_materialcode_qrRecv");
            current_materialclasscode_qrRecv = intent.getStringExtra("current_materialclasscode_qrRecv");
            current_address_qrRecv = intent.getStringExtra("current_address_qrRecv");
            current_uploadflag_qrRecv = intent.getStringExtra("current_uploadflag_qrRecv");
            current_vbillcode_qrRecv = intent.getStringExtra("current_vbillcode_qrRecv");
            current_cwarehousecode_qrRecv = intent.getStringExtra("current_cwarehousecode_qrRecv");
            current_rwarehousecode_qrRecv = intent.getStringExtra("current_rwarehousecode_qrRecv");
            maccode=intent.getStringExtra("maccode");

        }
        vcooporderbcode_bText = (TextView) findViewById(R.id.vcooporderbcode_b_allocatetransferqr);
        matrnameText = (TextView) findViewById(R.id.matrname_allocatetransferqr);
        matrcodeText = (TextView) findViewById(R.id.matrcode_allocatetransferqr);
        numQRText = (TextView) findViewById(R.id.num_allocatetransferqr);
        vbillcodeText = (TextView) findViewById(R.id.vbillcode_allocatetransferqr);
        scanButton = (Button) findViewById(R.id.scan_allocatetransferqr);
        spinner = findViewById(R.id.spinner_allocatetransfer_qrdetail);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isVcooporderbcode_bAlreadyUpload()) {
                    finish();
                    Intent intent = new Intent(AllocateTransferQRDetail.this, AllocateTransferQrScanner.class);
                    intent.putExtra("current_itempk_qrRecv", current_itempk_qrRecv);
                    intent.putExtra("current_nnum_qrRecv", current_nnum_qrRecv);
                    intent.putExtra("current_scannum_qrRecv", current_scannum_qrRecv);
                    intent.putExtra("current_materialcode_qrRecv", current_materialcode_qrRecv);
                    intent.putExtra("current_cwarehousecode_qrRecv", current_cwarehousecode_qrRecv);
                    intent.putExtra("current_rwarehousecode_qrRecv", current_rwarehousecode_qrRecv);
                    intent.putExtra("current_address_qrRecv", current_address_qrRecv);
                    intent.putExtra("current_materialclasscode_qrRecv", current_materialclasscode_qrRecv);
                    intent.putExtra("current_uploadflag_qrRecv", current_uploadflag_qrRecv);
                    intent.putExtra("current_vbillcode_qrRecv", current_vbillcode_qrRecv);
                    intent.putExtra("maccode",maccode);

                    startActivity(intent);
                } else {
                    Toast.makeText(AllocateTransferQRDetail.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
                }
            }
        });
        vcooporderbcode_bText.setText("行号  :" + current_itempk_qrRecv);
       // matrnameText.setText("物料名称:" + current_matrname_qrRecv);
        matrcodeText.setText("物料编码:" + current_materialcode_qrRecv);
        numQRText.setText("数量:  " + current_nnum_qrRecv);
        vbillcodeText.setText("单号:  " + current_vbillcode_qrRecv);

        listAllBodyPostition = QueryAllocateTransferBody(current_materialcode_qrRecv);
        String current_scanSum = countScannedQRCode(current_vbillcode_qrRecv, current_materialcode_qrRecv);
        insertCountOfScannedQRCode(current_scanSum);
        AllocateTransferTableQrDetailAdapter adapter = new AllocateTransferTableQrDetailAdapter(AllocateTransferQRDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
    }

    private boolean isVcooporderbcode_bAlreadyUpload() {
        Cursor cursornew = db5.rawQuery("select uploadflag from AllocateTransferBody where billno=? and itempk=?",
                new String[]{current_vbillcode_qrRecv, current_itempk_qrRecv});
        if (cursornew != null && cursornew.getCount() > 0) {
            while (cursornew.moveToNext()) {
                String uploadflag1 = cursornew.getString(cursornew.getColumnIndex("uploadflag"));
                if ("Y".equals(uploadflag1)) {
                    return true;
                }
            }
            cursornew.close();
        }
        return false;
    }

    //下面的方法内容需要根据实际更新
    public ArrayList<AllocateTransferQrDetailBean> QueryAllocateTransferBody(String current_matrcode_qrRecv) {

        ArrayList<AllocateTransferQrDetailBean> list = new ArrayList<AllocateTransferQrDetailBean>();
        Cursor cursor = db5.rawQuery("select platecode,boxcode,prodcutcode,itemuploadflag from AllocateTransferScanResult where billno=? " +
                "and materialcode=? and itempk=?", new String[]{current_vbillcode_qrRecv, current_matrcode_qrRecv, current_itempk_qrRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                AllocateTransferQrDetailBean bean = new AllocateTransferQrDetailBean();
                bean.platecode = cursor.getString(cursor.getColumnIndex("platecode"));
                bean.boxcode = cursor.getString(cursor.getColumnIndex("boxcode"));
                bean.prodcutcode = cursor.getString(cursor.getColumnIndex("prodcutcode"));
                bean.itemuploadflag = cursor.getString(cursor.getColumnIndex("itemuploadflag"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    private String countScannedQRCode(String vbillcode, String matrcode) {
        String count = "0";
        Cursor cursor2 = db5.rawQuery("select prodcutcode from AllocateTransferScanResult where billno=? and materialcode=? and itempk=?", new String[]{vbillcode, matrcode,current_itempk_qrRecv});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
    }

    private void insertCountOfScannedQRCode(String scannum) {
        db5.execSQL("update AllocateTransferBody set scannum=? where billno=? and materialcode=? and itempk=?", new String[]{scannum, current_vbillcode_qrRecv, current_materialcode_qrRecv,current_itempk_qrRecv});
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
    private AllocateTransferTableQrDetailAdapter.MyClickListener mListener = new AllocateTransferTableQrDetailAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            if (!isAlreadyUpload(listAllBodyPostition.get(position).getProdcutcode())) {
                db5.execSQL("delete from AllocateTransferScanResult where prodcutcode=?", new Object[]{listAllBodyPostition.get(position).getProdcutcode()});

                String current_scanSum = countScannedQRCode(current_vbillcode_qrRecv, current_materialcode_qrRecv);
                insertCountOfScannedQRCode(current_scanSum);
                listAllBodyPostition = QueryAllocateTransferBody(current_materialcode_qrRecv);
                AllocateTransferTableQrDetailAdapter adapter = new AllocateTransferTableQrDetailAdapter(AllocateTransferQRDetail.this, listAllBodyPostition, mListener);
                tableBodyListView.setAdapter(adapter);
            } else {
                Toast.makeText(AllocateTransferQRDetail.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
            }
        }
    };

    //判断单个item是否上传过，上传过的不允许再次操作
    private boolean isAlreadyUpload(String prodcutcode) {
        Cursor cursor31 = db5.rawQuery("select itempk  from AllocateTransferScanResult",
                new String[]{});
        if (cursor31 != null && cursor31.getCount() > 0) {
            Cursor cursor3 = db5.rawQuery("select itempk  from AllocateTransferScanResult where billno=? and itempk=? and itemuploadflag=?",
                    new String[]{current_vbillcode_qrRecv, current_itempk_qrRecv, "N"});
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
