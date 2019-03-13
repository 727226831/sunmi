package com.example.shanggmiqr.transaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.SaleDeliveryTableQrDetailAdapter;
import com.example.shanggmiqr.bean.SaleDeliveryQrDetailBean;
import com.example.shanggmiqr.util.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class SaleDeliveryQRDetail extends AppCompatActivity {
    //出库条码明细
    private TextView vcooporderbcode_bText;
    private TextView matrnameText;
    private TextView matrcodeText;
    private TextView numQRText;
    private TextView vbillcodeText;
    private String current_vcooporderbcode_b_qrRecv;
    private String current_matrname_qrRecv;
    private String current_cwarename_qrRecv;
    private String current_nnum_qrRecv;
    private String current_matrcode_qrRecv;
    private String current_customer_qrRecv;
    private String current_vbillcode_qrRecv;
    private String current_maccode_qrRecv;
    private SQLiteDatabase db5;
    private MyDataBaseHelper helper5;
    private List<SaleDeliveryQrDetailBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button scanButton;
    private Spinner spinner;
    private Myadapter myadapter;
    private List<String> cars;
    private String temp_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_delivery_qr_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        helper5 = new MyDataBaseHelper(SaleDeliveryQRDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db5 = helper5.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_saledelivery_qrdetail);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_vcooporderbcode_b_qrRecv = _intent.getStringExtra("current_vcooporderbcode_b_qr");
            current_nnum_qrRecv = _intent.getStringExtra("current_nnum_qr");
            current_matrname_qrRecv = _intent.getStringExtra("current_matrname_qr");
            current_cwarename_qrRecv = _intent.getStringExtra("current_cwarename_qr");
            current_matrcode_qrRecv = _intent.getStringExtra("current_matrcode_qr");
            current_maccode_qrRecv = _intent.getStringExtra("current_maccode_qr");
            current_customer_qrRecv = _intent.getStringExtra("current_customer_qr");
            current_vbillcode_qrRecv = _intent.getStringExtra("current_vbillcode_qr");
        }
        vcooporderbcode_bText = (TextView) findViewById(R.id.vcooporderbcode_b_saledeliveryqr);
        matrnameText = (TextView) findViewById(R.id.matrname_saledeliveryqr);
        matrcodeText = (TextView) findViewById(R.id.matrcode_saledeliveryqr);
        numQRText = (TextView) findViewById(R.id.num_saledeliveryqr);
        vbillcodeText = (TextView) findViewById(R.id.vbillcode_saledeliveryqr);
        scanButton = (Button) findViewById(R.id.scan_saledeliveryqr);
        spinner = findViewById(R.id.spinner_saledelivery_qrdetail);
        myadapter();
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isVcooporderbcode_bAlreadyUpload()) {
                    finish();
                    Intent intent = new Intent(SaleDeliveryQRDetail.this, SaleDeliveryQrScanner.class);
                    intent.putExtra("current_vcooporderbcode_b_qrRecv", current_vcooporderbcode_b_qrRecv);
                    intent.putExtra("current_matrname_qrRecv", current_matrname_qrRecv);
                    intent.putExtra("current_matrcode_qrRecv", current_matrcode_qrRecv);
                    intent.putExtra("current_cwarename_qrRecv", current_cwarename_qrRecv);
                    intent.putExtra("current_maccode_qrRecv", current_maccode_qrRecv);
                    intent.putExtra("current_customer_qrRecv", current_customer_qrRecv);
                    intent.putExtra("current_nnum_qrRecv", current_nnum_qrRecv);
                    intent.putExtra("current_vbillcode_qrRecv", current_vbillcode_qrRecv);
                    startActivity(intent);
                } else {
                    Toast.makeText(SaleDeliveryQRDetail.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
                }
            }
        });
        vcooporderbcode_bText.setText("行号  :" + current_vcooporderbcode_b_qrRecv);
        matrnameText.setText("物料名称:" + current_matrname_qrRecv);
        matrcodeText.setText("物料编码:" + current_matrcode_qrRecv);
        numQRText.setText("发货数量:" + current_nnum_qrRecv);
        vbillcodeText.setText("发货单号:" + current_vbillcode_qrRecv);
        listAllBodyPostition = QuerySaleDeliveryBody(current_matrcode_qrRecv);
        String current_scanSum = countScannedQRCode(current_vbillcode_qrRecv, current_matrcode_qrRecv);
        insertCountOfScannedQRCode(current_scanSum);
        SaleDeliveryTableQrDetailAdapter adapter = new SaleDeliveryTableQrDetailAdapter(SaleDeliveryQRDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
    }

    private void myadapter() {
        cars = new ArrayList<>();
        cars = queryWarehouseInfo();
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
        Cursor cursornew = db5.rawQuery("select orginal_cwarename from SaleDeliveryBody where vbillcode=? and vcooporderbcode_b=? and orginal_cwarename=?",
                new String[]{current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv, "Y"});
        if (cursornew != null && cursornew.getCount() > 0) {
            cursornew.close();
            return true;
        }
        return false;
    }

    private boolean allowChangeWarehousename() {
        Cursor cursornew = db5.rawQuery("select prodcutcode from SaleDeliveryScanResult where vbillcode=? and vcooporderbcode_b=?",
                new String[]{current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv});
        if (cursornew != null && cursornew.getCount() > 0) {
            cursornew.close();
            return false;
        }
        return true;
    }

    private void updateWarehouseInfo(String name, String current_vbillcode_qrRecv, String current_vcooporderbcode_b_qrRecv) {
        if (!name.equals("请选择仓库")) {
            db5.execSQL("update SaleDeliveryBody set cwarename=?  where vbillcode=? and vcooporderbcode_b=?", new String[]{name, current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv});
            //   db5.execSQL("update SaleDeliveryBody set cwarename=?  where vbillcode=? and vcooporderbcode_b=?", new String[]{name, current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv});
        }
    }

    private List<String> queryWarehouseInfo() {
        List<String> cars = new ArrayList<>();
        Cursor cursornew = db5.rawQuery("select name from Warehouse",
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

    /*** 定义一个Myadapter类继承ArrayAdapter * 重写以下两个方法 *
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

    private boolean isVcooporderbcode_bAlreadyUpload() {
        Cursor cursornew = db5.rawQuery("select uploadflag from SaleDeliveryBody where vbillcode=? and vcooporderbcode_b=?",
                new String[]{current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv});
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
    public ArrayList<SaleDeliveryQrDetailBean> QuerySaleDeliveryBody(String current_matrcode_qrRecv) {
        ArrayList<SaleDeliveryQrDetailBean> list = new ArrayList<SaleDeliveryQrDetailBean>();
        Cursor cursor = db5.rawQuery("select platecode,boxcode,prodcutcode,itemuploadflag from SaleDeliveryScanResult where vbillcode=? and matrcode=? and vcooporderbcode_b=?", new String[]{current_vbillcode_qrRecv, current_matrcode_qrRecv, current_vcooporderbcode_b_qrRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                SaleDeliveryQrDetailBean bean = new SaleDeliveryQrDetailBean();
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
        Cursor cursor2 = db5.rawQuery("select prodcutcode from SaleDeliveryScanResult where vbillcode=? and matrcode=? and vcooporderbcode_b=?", new String[]{vbillcode, matrcode,current_vcooporderbcode_b_qrRecv});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
    }

    private void insertCountOfScannedQRCode(String scannum) {
        db5.execSQL("update SaleDeliveryBody set scannum=? where vbillcode=? and matrcode=? and vcooporderbcode_b=?", new String[]{scannum, current_vbillcode_qrRecv, current_matrcode_qrRecv,current_vcooporderbcode_b_qrRecv});
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
    private SaleDeliveryTableQrDetailAdapter.MyClickListener mListener = new SaleDeliveryTableQrDetailAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            if (!isAlreadyUpload(listAllBodyPostition.get(position).getProdcutcode())) {
                db5.execSQL("delete from SaleDeliveryScanResult where prodcutcode=?", new Object[]{listAllBodyPostition.get(position).getProdcutcode()});

                String current_scanSum = countScannedQRCode(current_vbillcode_qrRecv, current_matrcode_qrRecv);
                insertCountOfScannedQRCode(current_scanSum);
                listAllBodyPostition = QuerySaleDeliveryBody(current_matrcode_qrRecv);
                SaleDeliveryTableQrDetailAdapter adapter = new SaleDeliveryTableQrDetailAdapter(SaleDeliveryQRDetail.this, listAllBodyPostition, mListener);
                tableBodyListView.setAdapter(adapter);
            } else {
                Toast.makeText(SaleDeliveryQRDetail.this, "已经执行发货操作的行号不允许再进行操作", Toast.LENGTH_LONG).show();
            }
        }
    };

    //判断单个item是否上传过，上传过的不允许再次操作
    private boolean isAlreadyUpload(String prodcutcode) {
        Cursor cursor31 = db5.rawQuery("select vcooporderbcode_b  from SaleDeliveryScanResult",
                new String[]{});
        if (cursor31 != null && cursor31.getCount() > 0) {
            Cursor cursor3 = db5.rawQuery("select vcooporderbcode_b  from SaleDeliveryScanResult where vbillcode=? and vcooporderbcode_b=? and itemuploadflag=?",
                    new String[]{current_vbillcode_qrRecv, current_vcooporderbcode_b_qrRecv, "N"});
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
