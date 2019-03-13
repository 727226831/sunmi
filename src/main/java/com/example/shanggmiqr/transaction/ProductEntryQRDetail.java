package com.example.shanggmiqr.transaction;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.ProductEntryTableQrDetailAdapter;
import com.example.shanggmiqr.bean.ProductEntryQrDetailBean;
import com.example.shanggmiqr.util.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class ProductEntryQRDetail extends AppCompatActivity {
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
    private String current_ysnum_qrRecv;
    private String current_uploadflag_qrRecv;
    private String current_vbillcode_qrRecv;

    private SQLiteDatabase db5;
    private MyDataBaseHelper helper5;
    private List<ProductEntryQrDetailBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_entry_qr_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        helper5 = new MyDataBaseHelper(ProductEntryQRDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db5 = helper5.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_productentry_qrdetail);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_itempk_qrRecv = _intent.getStringExtra("current_itempk_qrRecv");
            current_nnum_qrRecv = _intent.getStringExtra("current_nnum_qrRecv");
            current_scannum_qrRecv = _intent.getStringExtra("current_scannum_qrRecv");
            current_materialcode_qrRecv = _intent.getStringExtra("current_materialcode_qrRecv");
            current_ysnum_qrRecv = _intent.getStringExtra("current_ysnum_qrRecv");
            current_uploadflag_qrRecv = _intent.getStringExtra("current_uploadflag_qrRecv");
            current_vbillcode_qrRecv = _intent.getStringExtra("current_vbillcode_qrRecv");
        }
        vcooporderbcode_bText = (TextView) findViewById(R.id.vcooporderbcode_b_productentryqr);
        //matrnameText = (TextView) findViewById(R.id.matrname_productentryqr);
        matrcodeText = (TextView) findViewById(R.id.matrcode_productentryqr);
        numQRText = (TextView) findViewById(R.id.num_productentryqr);
        vbillcodeText = (TextView) findViewById(R.id.vbillcode_productentryqr);
        scanButton = (Button) findViewById(R.id.scan_productentryqr);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isVcooporderbcode_bAlreadyUpload()) {
                    finish();
                    Intent intent = new Intent(ProductEntryQRDetail.this, ProductEntryQrScanner.class);
                    intent.putExtra("current_itempk_qrRecv", current_itempk_qrRecv);
                    intent.putExtra("current_nnum_qrRecv", current_nnum_qrRecv);
                    intent.putExtra("current_scannum_qrRecv", current_scannum_qrRecv);
                    intent.putExtra("current_materialcode_qrRecv", current_materialcode_qrRecv);
                    intent.putExtra("current_ysnum_qrRecv", current_ysnum_qrRecv);
                    intent.putExtra("current_uploadflag_qrRecv", current_uploadflag_qrRecv);
                    intent.putExtra("current_vbillcode_qrRecv", current_vbillcode_qrRecv);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductEntryQRDetail.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
                }
            }
        });
        vcooporderbcode_bText.setText("行号:  " + current_itempk_qrRecv);
       // matrnameText.setText("物料名称:" + current_matrname_qrRecv);
        matrcodeText.setText("物料编码:" + current_materialcode_qrRecv);
        numQRText.setText("数量:  " + current_nnum_qrRecv);
        vbillcodeText.setText("单号:  " + current_vbillcode_qrRecv);
        listAllBodyPostition = QueryProductEntryBody(current_materialcode_qrRecv);
        String current_scanSum = countScannedQRCode(current_vbillcode_qrRecv, current_materialcode_qrRecv);
        insertCountOfScannedQRCode(current_scanSum);
        ProductEntryTableQrDetailAdapter adapter = new ProductEntryTableQrDetailAdapter(ProductEntryQRDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
    }

    private boolean isVcooporderbcode_bAlreadyUpload() {
        Cursor cursornew = db5.rawQuery("select uploadflag from ProductEntryBody where billcode=? and itempk=?",
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
    public ArrayList<ProductEntryQrDetailBean> QueryProductEntryBody(String current_matrcode_qrRecv) {
        ArrayList<ProductEntryQrDetailBean> list = new ArrayList<ProductEntryQrDetailBean>();
        Cursor cursor = db5.rawQuery("select platecode,boxcode,prodcutcode,itemuploadflag from ProductEntryScanResult where billcode=? and materialcode=? and itempk=?", new String[]{current_vbillcode_qrRecv, current_matrcode_qrRecv, current_itempk_qrRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                ProductEntryQrDetailBean bean = new ProductEntryQrDetailBean();
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

    private String countScannedQRCode(String vbillcode, String materialcode) {
        String count = "0";
        Cursor cursor2 = db5.rawQuery("select prodcutcode from ProductEntryScanResult where billcode=? and materialcode=? and itempk=?", new String[]{vbillcode, materialcode,current_itempk_qrRecv});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
    }

    private void insertCountOfScannedQRCode(String scannum) {
        db5.execSQL("update ProductEntryBody set scannum=? where billcode=? and materialcode=? and itempk=?", new String[]{scannum, current_vbillcode_qrRecv, current_materialcode_qrRecv,current_itempk_qrRecv});
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
    private ProductEntryTableQrDetailAdapter.MyClickListener mListener = new ProductEntryTableQrDetailAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            if (!isAlreadyUpload(listAllBodyPostition.get(position).getProdcutcode())) {
                db5.execSQL("delete from ProductEntryScanResult where prodcutcode=?", new Object[]{listAllBodyPostition.get(position).getProdcutcode()});

                String current_scanSum = countScannedQRCode(current_vbillcode_qrRecv, current_materialcode_qrRecv);
                insertCountOfScannedQRCode(current_scanSum);
                listAllBodyPostition = QueryProductEntryBody(current_materialcode_qrRecv);
                ProductEntryTableQrDetailAdapter adapter = new ProductEntryTableQrDetailAdapter(ProductEntryQRDetail.this, listAllBodyPostition, mListener);
                tableBodyListView.setAdapter(adapter);
            } else {
                Toast.makeText(ProductEntryQRDetail.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
            }
        }
    };

    //判断单个item是否上传过，上传过的不允许再次操作
    private boolean isAlreadyUpload(String prodcutcode) {
        Cursor cursor31 = db5.rawQuery("select itempk  from ProductEntryScanResult",
                new String[]{});
        if (cursor31 != null && cursor31.getCount() > 0) {
            Cursor cursor3 = db5.rawQuery("select itempk  from ProductEntryScanResult where billcode=? and itempk=? and itemuploadflag=?",
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
