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
import android.widget.TextView;
import android.widget.Toast;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.OtherEntryTableQrDetailAdapter;
import com.example.shanggmiqr.bean.OtherOutgoingQrDetailBean;
import com.example.shanggmiqr.util.MyDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class OtherEntryQRDetail extends AppCompatActivity {
    //出库条码明细
    private TextView poBillCodeQRText;
    private TextView cwareNameQRText;
    private TextView material_noQRText;
    private TextView numQRText;
    private TextView pchQRText;
    private String current_materialcode_qrRecv;
    private String current_maccode_qrRecv;
    private String current_nnum_qrRecv;
    private String current_uploadnum_qrRecv;
    private String current_pobillcode_qrRecv;
    private String current_cwarename_qrRecv;
    private String current_vcooporderbcode_b_qrRecv;
    private SQLiteDatabase db5;
    private MyDataBaseHelper helper5;
    private List<OtherOutgoingQrDetailBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button scanButton;
    OtherEntryTableQrDetailAdapter adapter;
    int type;
    String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_entry_qr_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        type=getIntent().getIntExtra("type",-1);
        switch (type){
            case 1:
                title="其他入库扫码明细";
                break;
            case 2:
                title="其他出库扫码明细";
                break;
        }

        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
        type=getIntent().getIntExtra("type",-1);
        helper5 = new MyDataBaseHelper(OtherEntryQRDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db5 = helper5.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_otherentry_qrdetail);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_pobillcode_qrRecv = _intent.getStringExtra("current_pobillcode_qr");
            current_cwarename_qrRecv = _intent.getStringExtra("current_cwarename_qr");
            current_materialcode_qrRecv = _intent.getStringExtra("current_materialcode_qr");
            current_maccode_qrRecv = _intent.getStringExtra("current_maccode_qr");
            current_nnum_qrRecv = _intent.getStringExtra("current_nnum_qr");
            current_uploadnum_qrRecv = _intent.getStringExtra("current_uploadnum_qr");
            current_vcooporderbcode_b_qrRecv = _intent.getStringExtra("current_vcooporderbcode_b_qr");

        }
        poBillCodeQRText = (TextView)findViewById(R.id.pobill_otherentryqr);
        cwareNameQRText = (TextView)findViewById(R.id.cwarename_otherentryqr);
        material_noQRText = (TextView)findViewById(R.id.material_no_otherentryqr);
        numQRText = (TextView)findViewById(R.id.nnum_otherentryqr);
        pchQRText = (TextView)findViewById(R.id.scannum_otherentryqr);
        scanButton = (Button)findViewById(R.id.scan_otherentryqr) ;
        if(!getIntent().getStringExtra("flag").equals("N")){
            scanButton.setEnabled(false);
        }
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAlreadyUpload()){
                    Intent intent = new Intent(OtherEntryQRDetail.this,SaleDeliveryQrScanner.class);
                    intent.putExtra("type",type);
                    intent.putExtra("current_vbillcode_qrRecv", current_pobillcode_qrRecv);
                    intent.putExtra("current_cwarename_scanner", current_cwarename_qrRecv);
                    intent.putExtra("current_maccode_qrRecv",current_maccode_qrRecv);
                    intent.putExtra("current_matrcode_qrRecv",current_materialcode_qrRecv);
                    intent.putExtra("current_nnum_qrRecv",String.valueOf(current_nnum_qrRecv));
                    intent.putExtra("chosen_line_uploadnum_scanner",current_uploadnum_qrRecv);
                    intent.putExtra("current_vcooporderbcode_b_qrRecv", current_vcooporderbcode_b_qrRecv);
                    startActivity(intent);
                }else{
                    Toast.makeText(OtherEntryQRDetail.this,"已经执行发货操作的行号不允许再进行操作",Toast.LENGTH_LONG).show();
                }
            }
        });
        poBillCodeQRText.setText("发货单号:"+current_pobillcode_qrRecv);
        cwareNameQRText.setText("发货仓库:"+current_cwarename_qrRecv);
        material_noQRText.setText("物料编号:"+current_materialcode_qrRecv);
        numQRText.setText("单据数量  :"+current_nnum_qrRecv);
        pchQRText.setText("提交数量 :"+current_uploadnum_qrRecv);
        listAllBodyPostition = QueryOtherEntryBody(current_pobillcode_qrRecv,current_materialcode_qrRecv);
        String current_scanSum = countScannedQRCode(current_pobillcode_qrRecv,current_materialcode_qrRecv);
        insertCountOfScannedQRCode(current_scanSum);
        adapter = new OtherEntryTableQrDetailAdapter(OtherEntryQRDetail.this, listAllBodyPostition,mListener);
        tableBodyListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
//下面的方法内容需要根据实际更新
    public ArrayList<OtherOutgoingQrDetailBean> QueryOtherEntryBody(String current_pobillcodeRecv,String materialcode) {
        ArrayList<OtherOutgoingQrDetailBean> list = new ArrayList<OtherOutgoingQrDetailBean>();
        Cursor cursor=null;

        cursor = db5.rawQuery("select prodcutcode,itemuploadflag from SaleDeliveryScanResult where vbillcode=? and matrcode=? " +
                "and vcooporderbcode_b=?", new String[]{current_pobillcodeRecv, materialcode,
                current_vcooporderbcode_b_qrRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherOutgoingQrDetailBean bean = new OtherOutgoingQrDetailBean();
                bean.prodcutcode = cursor.getString(cursor.getColumnIndex("prodcutcode"));
                bean.itemuploadflag = cursor.getString(cursor.getColumnIndex("itemuploadflag"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }
    private String countScannedQRCode(String pobillcode,String materialcode) {
        String count = "0";

        Cursor cursor2 = db5.rawQuery("select prodcutcode from SaleDeliveryScanResult where vbillcode=? and matrcode=? and vcooporderbcode_b=?",
                new String[]{pobillcode, materialcode,current_vcooporderbcode_b_qrRecv});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
    }
    private void insertCountOfScannedQRCode(String scannum) {
        switch (type){
            case 1:
                db5.execSQL("update OtherEntryBody set scannum=? where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{scannum,current_pobillcode_qrRecv,current_materialcode_qrRecv,current_vcooporderbcode_b_qrRecv});
                break;
            case 2:
                db5.execSQL("update OtherOutgoingBody set scannum=? where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                        new String[]{scannum,current_pobillcode_qrRecv,current_materialcode_qrRecv,current_vcooporderbcode_b_qrRecv});
                break;
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        listAllBodyPostition = QueryOtherEntryBody(current_pobillcode_qrRecv,current_materialcode_qrRecv);
        String current_scanSum = countScannedQRCode(current_pobillcode_qrRecv,current_materialcode_qrRecv);
        insertCountOfScannedQRCode(current_scanSum);
        adapter = new OtherEntryTableQrDetailAdapter(OtherEntryQRDetail.this, listAllBodyPostition,mListener);
        tableBodyListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
    public boolean isAlreadyUpload() {
        Cursor cursor3=null;
        switch (type){
            case 1:
                cursor3 = db5.rawQuery("select pobillcode from OtherEntryBody where pobillcode=? and uploadflag=?",
                        new String[]{current_pobillcode_qrRecv, "N"});
                break;
            case 2:
                cursor3 = db5.rawQuery("select pobillcode from OtherOutgoingBody where pobillcode=? and uploadflag=?",
                        new String[]{current_pobillcode_qrRecv, "N"});
                break;
        }

        // Cursor cursor3 = db3.rawQuery(sql3, null);
        if (cursor3 != null && cursor3.getCount() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private OtherEntryTableQrDetailAdapter.MyClickListener mListener = new OtherEntryTableQrDetailAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {


            if(listAllBodyPostition.get(position).getItemuploadflag().equals("N")){
                db5.execSQL("delete from SaleDeliveryScanResult where vbillcode=? and prodcutcode=?" +
                        " and vcooporderbcode_b=?", new Object[]{current_pobillcode_qrRecv, listAllBodyPostition.get(position).getProdcutcode(),
                        current_vcooporderbcode_b_qrRecv});

                listAllBodyPostition = QueryOtherEntryBody(current_pobillcode_qrRecv,current_materialcode_qrRecv);
                String current_scanSum = countScannedQRCode(current_pobillcode_qrRecv,current_materialcode_qrRecv);
                insertCountOfScannedQRCode(current_scanSum);
                adapter = new OtherEntryTableQrDetailAdapter(OtherEntryQRDetail.this, listAllBodyPostition,mListener);
                tableBodyListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(OtherEntryQRDetail.this,"已经执行发货操作的行号不允许再进行操作",Toast.LENGTH_LONG).show();
            }

        }
    };
}
