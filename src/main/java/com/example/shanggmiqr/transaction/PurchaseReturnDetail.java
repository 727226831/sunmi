package com.example.shanggmiqr.transaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.example.shanggmiqr.adapter.PurchaseReturnBodyTableAdapter;
import com.example.shanggmiqr.bean.PurchaseReturnBodyBean;
import com.example.shanggmiqr.bean.SaleDeliveryUploadFlagBean;
import com.example.shanggmiqr.bean.SalesRespBean;
import com.example.shanggmiqr.bean.SalesRespBeanValue;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class PurchaseReturnDetail extends AppCompatActivity {
    //其他出库明细
    private TextView vbillcodText;
    private TextView dbilldateText;
    private String current_sale_delivery_vbillcodeRecv;
    private String current_sale_delivery_dbilldateRecv;
    private SQLiteDatabase db4;
    private MyDataBaseHelper helper4;
    private List<PurchaseReturnBodyBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button saleDeliveryScanButton;
    private Button uploadAll_saleDeliveryButton;
    private Button uploadSingleButton;

    private String itempk;
    private String chosen_line_nnum;
    private String chosen_line_cwarename;

    private String chosen_line_maccode;
    private String chosen_line_matrname;
    private String chosen_line_matrcode;
    private String chosen_line_uploadflag;
    private String[] matrcodeList[];
    private Handler saleDeliveryDetailHandler = null;

    //物流公司选择
    private Spinner spinner;
    private Myadapter myadapter;
    private EditText expressCodeEditText;
    private List<String> logisticscompanies;
    //选择的仓库信息
    private String chooseLogisticscompany = "";
    //运单号
    private String expressCode = "";


    //要上传行号的集合
    private List<String> list;
    //要上传的产品码的集合

    private List<SaleDeliveryUploadFlagBean> lisitemtall;
    //nnum为正 bisreturn为N 为负则为Y
    private String current_bisreturn = "N";
    private ZLoadingDialog zLoadingDialog;
     PurchaseReturnBodyTableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_return_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        expressCodeEditText = (EditText) findViewById(R.id.expressCode_edit_text_purchase_return);
        saleDeliveryScanButton = (Button) findViewById(R.id.scan_purchaseReturn);
        uploadAll_saleDeliveryButton = (Button) findViewById(R.id.uploadall_purchaseReturn);
        uploadSingleButton = (Button) findViewById(R.id.upload_purchaseReturn);

        zLoadingDialog = new ZLoadingDialog(PurchaseReturnDetail.this);
        zLoadingDialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                .setLoadingColor(Color.BLUE)//颜色
                .setHintText("提交中...")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .setDurationTime(0.5); // 设置动画时间百分比 - 0.5倍
        helper4 = new MyDataBaseHelper(PurchaseReturnDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db4 = helper4.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_purchase_return_detail);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_sale_delivery_vbillcodeRecv = _intent.getStringExtra("current_sale_delivery_vbillcode");
            current_sale_delivery_dbilldateRecv = _intent.getStringExtra("current_sale_delivery_dbilldate");

        }

        vbillcodText = (TextView) findViewById(R.id.vbillcode_purchaseReturn);
        dbilldateText = (TextView) findViewById(R.id.dbilldate_purchaseReturn);
        switch (getIntent().getIntExtra("type",-1)){
            case 6:
                actionBar.setTitle("采购到货明细");
                vbillcodText.setText("到货单号:" + current_sale_delivery_vbillcodeRecv);
                dbilldateText.setText("到货日期:" + current_sale_delivery_dbilldateRecv);
                break;
            case 7:
                actionBar.setTitle("采购退货明细");
                vbillcodText.setText("退货单号:" + current_sale_delivery_vbillcodeRecv);
                dbilldateText.setText("退货日期:" + current_sale_delivery_dbilldateRecv);
                break;
        }

        //物流公司选择
        spinner = findViewById(R.id.spinner_logistics_company_purchase_return);
        //加载数据
        myadapter();


        saleDeliveryScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PurchaseReturnDetail.this, SaleDeliveryQrScanner.class);
                intent.putExtra("type",getIntent().getIntExtra("type",-1));
                intent.putExtra("current_vcooporderbcode_b_qrRecv", itempk);
                intent.putExtra("current_matrname_qrRecv", chosen_line_matrname);
                intent.putExtra("current_cwarename_qrRecv", chosen_line_cwarename);
                intent.putExtra("current_matrcode_qrRecv", chosen_line_matrcode);
                intent.putExtra("current_maccode_qrRecv", chosen_line_maccode);

                intent.putExtra("current_nnum_qrRecv", chosen_line_nnum);
                intent.putExtra("current_uploadflag_qrRecv", chosen_line_uploadflag);
                intent.putExtra("current_vbillcode_qrRecv", current_sale_delivery_vbillcodeRecv);

                startActivity(intent);
            }
        });
        uploadAll_saleDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushData(null);

            }
        });
        uploadSingleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushData(itempk);

            }
        });
        saleDeliveryDetailHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(PurchaseReturnDetail.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        zLoadingDialog.dismiss();
                        expressCodeEditText.setText("");
                        spinner.setSelection(logisticscompanies.size() - 1, true);
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(PurchaseReturnDetail.this, s, Toast.LENGTH_LONG).show();
                        updateAllUploadFlag();
                        listAllBodyPostition = QuerySaleDeliveryBody(current_sale_delivery_vbillcodeRecv);
                        adapter = new PurchaseReturnBodyTableAdapter(PurchaseReturnDetail.this, listAllBodyPostition, mListener);
                        tableBodyListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        if (isAllItemUpload()) {
                            finish();
                        }



                        break;
                    case 0x12:
                        Toast.makeText(PurchaseReturnDetail.this, "该发货单已经全部上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x13:
                        Toast.makeText(PurchaseReturnDetail.this, "该行已经上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x14:
                        Toast.makeText(PurchaseReturnDetail.this, "请先扫码再进行发货上传操作", Toast.LENGTH_LONG).show();
                        break;

                    case 0x16:
                        Toast.makeText(PurchaseReturnDetail.this, "不同仓库的行号不可以同时上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x17:
                        zLoadingDialog.dismiss();
                        String exception111 = msg.getData().getString("Exception111");
                        Toast.makeText(PurchaseReturnDetail.this, exception111, Toast.LENGTH_LONG).show();
                        break;
                    case 0x18:
                        zLoadingDialog.dismiss();
                        Toast.makeText(PurchaseReturnDetail.this, "接口异常", Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        zLoadingDialog.dismiss();
                        String s3 = msg.getData().getString("uploadResp");
                        Toast.makeText(PurchaseReturnDetail.this, s3, Toast.LENGTH_LONG).show();
                        break;
                    case 0x20:
                        zLoadingDialog.dismiss();
                        Toast.makeText(PurchaseReturnDetail.this, "运单号没有填写，首先填写运单号", Toast.LENGTH_LONG).show();
                        break;
                    case 0x21:
                        zLoadingDialog.dismiss();
                        Toast.makeText(PurchaseReturnDetail.this, "物流公司没有选择，首先选择物流公司", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void pushData(final String itempk) {
        zLoadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkConnected(PurchaseReturnDetail.this)) {
                    try {

                            String uploadResp = DataHelper.uploadSaleDeliveryVBill("R41",db4, current_sale_delivery_vbillcodeRecv,itempk,
                                    PurchaseReturnDetail.this,"",expressCode,getIntent().getIntExtra("type",-1));
                            if (!(null == uploadResp)) {


                                    Gson gson = new Gson();
                                    SalesRespBean respBean = gson.fromJson(uploadResp, SalesRespBean.class);
                                    Gson gson2 = new Gson();
                                    SalesRespBeanValue respBeanValue = gson2.fromJson(respBean.getValue(), SalesRespBeanValue.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("uploadResp", respBeanValue.getErrmsg());
                                    Message msg = new Message();

                                    if (respBeanValue.getErrno().equals("0")) {
                                        //19弹出erromsg
                                        msg.what = 0x11;
                                        Log.i("0x11","is run");
                                    } else {
                                        //19弹出erromsg
                                        msg.what = 0x19;
                                    }
                                    msg.setData(bundle);
                                    saleDeliveryDetailHandler.sendMessage(msg);

                            } else {
                                Message msg = new Message();
                                msg.what = 0x18;
                                saleDeliveryDetailHandler.sendMessage(msg);
                            }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Bundle bundle = new Bundle();
                        bundle.putString("Exception111", e.toString());
                        Message msg = new Message();
                        msg.what = 0x17;
                        msg.setData(bundle);
                        saleDeliveryDetailHandler.sendMessage(msg);
                        return;
                    }
                }
            }
        }).start();
    }

    private void myadapter() {
        logisticscompanies = new ArrayList<>();
        logisticscompanies = querylogisticscompanies();
        //String default_value = current_cwarename_qrRecv;
        String default_value = "";
        if (default_value.length() == 0) {
            logisticscompanies.add("请选择物流公司");
        } else {
            //logisticscompanies.add(current_cwarename_qrRecv);
        }
        myadapter = new Myadapter(this, R.layout.custom_spinner_layout, logisticscompanies);
        spinner.setAdapter(myadapter);
        //默认选中最后一项
        spinner.setSelection(logisticscompanies.size() - 1, true);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chooseLogisticscompany = myadapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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

    private List<String> querylogisticscompanies() {
        List<String> cars = new ArrayList<>();
        Cursor cursornew = db4.rawQuery("select name from LogisticsCompany",
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

    private boolean isAllItemUpload() {
        Log.i("list-->",new Gson().toJson(listAllBodyPostition));
        int count=0;
        for (int i = 0; i <listAllBodyPostition.size() ; i++) {
            if(listAllBodyPostition.get(i).getUploadflag().equals("Y")){
                count++;
            }
        }
        String flag="";
        if(count==0){
            flag="N";
        }else if(count!=listAllBodyPostition.size()){
            flag="PY";
        }else {
            flag="Y";
        }
        switch (getIntent().getIntExtra("type",-1)){
            case 6:

                db4.execSQL("update PurchaseArrival set flag=? where vbillcode=?", new String[]{flag, current_sale_delivery_vbillcodeRecv});
                break;
            case 7:
                db4.execSQL("update PurchaseReturn set flag=? where vbillcode=?", new String[]{flag, current_sale_delivery_vbillcodeRecv});

                break;
        }

        if(flag.equals("Y")){
            return true;
        }else {
            return  false;
        }
    }

    private void updateAllUploadFlag() {
        for (int i = 0; i <listAllBodyPostition.size() ; i++) {
            Log.i("data-->",new Gson().toJson(listAllBodyPostition));
            if(listAllBodyPostition.get(i).getScannnum()!=null){
                if(Math.abs(Integer.parseInt(listAllBodyPostition.get(i).getNnum()))==Integer.parseInt(listAllBodyPostition.get(i).getScannnum())){

                    switch (getIntent().getIntExtra("type",-1)){
                        case 6:
                            db4.execSQL("update PurchaseArrivalBody set uploadflag=? where vbillcode=? and itempk=? ",
                                    new String[]{"Y", current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(i).getItempk()});
                            break;
                        case 7:
                            db4.execSQL("update PurchaseReturnBody set uploadflag=? where vbillcode=? and itempk=? ",
                                    new String[]{"Y", current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(i).getItempk()});
                            break;
                    }

                }else {
                    switch (getIntent().getIntExtra("type",-1)){
                        case 6:
                            db4.execSQL("update PurchaseArrivalBody set uploadflag=? where vbillcode=? and itempk=? ",
                                    new String[]{"PY", current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(i).getItempk()});
                            break;
                        case 7:
                            db4.execSQL("update PurchaseReturnBody set uploadflag=? where vbillcode=? and itempk=? ",
                                    new String[]{"PY", current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(i).getItempk()});
                            break;
                    }

                }
            }
        }
        db4.execSQL("update SaleDeliveryScanResult set itemuploadflag=? where vbillcode=? ",
                new String[]{"Y", current_sale_delivery_vbillcodeRecv});


    }

    @Override
    protected void onStart() {

        listAllBodyPostition = QuerySaleDeliveryBody(current_sale_delivery_vbillcodeRecv);
        adapter = new PurchaseReturnBodyTableAdapter(PurchaseReturnDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                select(position);
            }
        });
        select(0);
        super.onStart();
    }

    private void select(int position) {
        adapter.select(position);
        saleDeliveryScanButton.setEnabled(true);
        uploadSingleButton.setEnabled(true);
        PurchaseReturnBodyBean local_saleDeliveryBodyBean = (PurchaseReturnBodyBean) adapter.getItem(position);
        itempk = local_saleDeliveryBodyBean.getItempk();
        chosen_line_matrname = local_saleDeliveryBodyBean.getMaterialname();
        chosen_line_cwarename = local_saleDeliveryBodyBean.getWarehouse();
        chosen_line_matrcode = local_saleDeliveryBodyBean.getMaterialcode();
        chosen_line_maccode = local_saleDeliveryBodyBean.getMaccode();
        chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
        chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();


    }


    public ArrayList<PurchaseReturnBodyBean> QuerySaleDeliveryBody(String current_sale_delivery_vbillcodeRecv) {
        ArrayList<PurchaseReturnBodyBean> list = new ArrayList<PurchaseReturnBodyBean>();
        Cursor cursor=null;
        switch (getIntent().getIntExtra("type",-1)){
            case 6:
                cursor = db4.rawQuery("select * from PurchaseArrivalBody where vbillcode=?",
                        new String[]{current_sale_delivery_vbillcodeRecv});
                break;
            case 7:
                cursor = db4.rawQuery("select * from PurchaseReturnBody where vbillcode=?",
                        new String[]{current_sale_delivery_vbillcodeRecv});
                break;
        }


            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {

                PurchaseReturnBodyBean bean = new PurchaseReturnBodyBean();
                bean.nnum = cursor.getString(cursor.getColumnIndex("nnum"));
                bean.itempk=cursor.getString(cursor.getColumnIndex("itempk"));
                bean.scannnum = cursor.getString(cursor.getColumnIndex("scannum"));

                bean.uploadflag = cursor.getString(cursor.getColumnIndex("uploadflag"));
                bean.materialname=cursor.getString(cursor.getColumnIndex("materialname"));
                bean.materialcode=cursor.getString(cursor.getColumnIndex("materialcode"));
              //  bean.warehouse=cursor.getString(cursor.getColumnIndex("warehouse"));
               if(cursor.getString(cursor.getColumnIndex("warehouse"))!=null){
                   bean.warehouse = DataHelper.getCwarename(cursor.getString(cursor.getColumnIndex("warehouse")), db4);
                }

                bean.maccode=cursor.getString(cursor.getColumnIndex("maccode"));
                list.add(bean);
            }
            cursor.close();

        return list;
    }



    public String queryMaccodeFromDB(String vbillcode, String vcooporderbcode_b, String matrcode) {
        String maccode = "error";
        Cursor cursor=null;
        switch (getIntent().getIntExtra("type",-1)){
            case 6:
                cursor = db4.rawQuery("select maccode from PurchaseArrivalBody where vbillcode=? " +
                        "and itempk=? and materialcode=? ", new String[]{vbillcode, vcooporderbcode_b, matrcode});
                break;
            case 7:
                cursor = db4.rawQuery("select maccode from PurchaseReturnBody where vbillcode=? " +
                        "and itempk=? and materialcode=? ", new String[]{vbillcode, vcooporderbcode_b, matrcode});
                break;
        }

        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex("maccode"));
            }
            cursor.close();
        }
        return maccode;
    }


    /**
     * 实现类，响应按钮点击事件
     */
    private PurchaseReturnBodyTableAdapter.MyClickListener2 mListener = new PurchaseReturnBodyTableAdapter.MyClickListener2() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent(PurchaseReturnDetail.this, SaleDeliveryQRDetail.class);
            intent.putExtra("type",getIntent().getIntExtra("type",-1));
            intent.putExtra("title",getIntent().getStringExtra("title"));
            intent.putExtra("current_nnum_qr", listAllBodyPostition.get(position).getNnum());
            intent.putExtra("current_maccode_qr", queryMaccodeFromDB(current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(position).getItempk(), listAllBodyPostition.get(position).getMaterialcode()));
            intent.putExtra("current_vbillcode_qr", current_sale_delivery_vbillcodeRecv);
            intent.putExtra("title",getIntent().getStringExtra("title"));
            intent.putExtra("current_vcooporderbcode_b_qr", listAllBodyPostition.get(position).getItempk());
            intent.putExtra("current_cwarename_qr", listAllBodyPostition.get(position).getWarehouse());
            intent.putExtra("current_matrname_qr", listAllBodyPostition.get(position).getMaterialname());
            intent.putExtra("current_matrcode_qr", listAllBodyPostition.get(position).getMaterialcode());
          //  intent.putExtra("current_customer_qr", listAllBodyPostition.get(position).getCustomer());
            startActivity(intent);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
