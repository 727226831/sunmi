package com.example.shanggmiqr.transaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.solver.GoalRow;
import android.support.v7.app.AlertDialog;
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
import com.example.shanggmiqr.adapter.SaleDeliveryBodyTableAdapter;
import com.example.shanggmiqr.bean.SaleDeliveryBodyBean;
import com.example.shanggmiqr.bean.SaleDeliveryUploadFlagBean;
import com.example.shanggmiqr.bean.SalesRespBean;
import com.example.shanggmiqr.bean.SalesRespBeanValue;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class SaleDeliveryDetail extends AppCompatActivity {
    //其他出库明细
    private TextView vbillcodText;
    private TextView dbilldateText;
    private String current_sale_delivery_vbillcodeRecv;
    private String current_sale_delivery_dbilldateRecv;
    private SQLiteDatabase db4;
    private MyDataBaseHelper helper4;
    private List<SaleDeliveryBodyBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button saleDeliveryScanButton;
    private Button uploadAll_saleDeliveryButton;
    private Button uploadSingleButton;
    private String chosen_line_vcooporderbcode_b;
    private String chosen_line_nnum;
    private String chosen_line_cwarename;
    private String chosen_line_scannnum;
    private String chosen_line_maccode;
    private String chosen_line_matrname;
    private String chosen_line_matrcode;
    private String chosen_line_customer;
    private String chosen_line_uploadflag;
    private String[] matrcodeList[];
    private Handler saleDeliveryDetailHandler = null;
    private List<String> upload_cwarename;

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
    private List<String> listAll;



    //nnum为正 bisreturn为N 为负则为Y

    private ZLoadingDialog zLoadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sale_delivery_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        actionBar.setTitle(getIntent().getStringExtra("title")+"明细");

         expressCodeEditText = (EditText) findViewById(R.id.expressCode_edit_text);
        saleDeliveryScanButton = (Button) findViewById(R.id.scan_saleDelivery);
        uploadAll_saleDeliveryButton = (Button) findViewById(R.id.uploadall_saleDelivery);
        uploadSingleButton = (Button) findViewById(R.id.upload_saleDelivery);
        saleDeliveryScanButton.setEnabled(false);
        uploadSingleButton.setEnabled(false);
        zLoadingDialog= new ZLoadingDialog(SaleDeliveryDetail.this);
        zLoadingDialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                .setLoadingColor(Color.BLUE)//颜色
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setHintTextSize(16) // 设置字体大小 dp
                .setHintTextColor(Color.GRAY)  // 设置字体颜色
                .setDurationTime(0.5); // 设置动画时间百分比 - 0.5倍
        helper4 = new MyDataBaseHelper(SaleDeliveryDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db4 = helper4.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_sale_delivery_detail);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_sale_delivery_vbillcodeRecv = _intent.getStringExtra("current_sale_delivery_vbillcode");
            current_sale_delivery_dbilldateRecv = _intent.getStringExtra("current_sale_delivery_dbilldate");


        }
        vbillcodText = (TextView) findViewById(R.id.vbillcode_saleDelivery);
        dbilldateText = (TextView) findViewById(R.id.dbilldate_saleDelivery);

        vbillcodText.setText("发货单号:" + current_sale_delivery_vbillcodeRecv);
        dbilldateText.setText("发货日期:" + current_sale_delivery_dbilldateRecv);
        //物流公司选择
        spinner = findViewById(R.id.spinner_logistics_company);

        //加载数据
        myadapter();


        saleDeliveryScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SaleDeliveryDetail.this, SaleDeliveryQrScanner.class);
                intent.putExtra("current_vcooporderbcode_b_qrRecv", chosen_line_vcooporderbcode_b);
                intent.putExtra("current_matrname_qrRecv", chosen_line_matrname);
                intent.putExtra("current_cwarename_qrRecv", chosen_line_cwarename);
                intent.putExtra("current_matrcode_qrRecv", chosen_line_matrcode);
                intent.putExtra("current_maccode_qrRecv", chosen_line_maccode);
                intent.putExtra("current_customer_qrRecv", chosen_line_customer);
                intent.putExtra("current_nnum_qrRecv", chosen_line_nnum);
                intent.putExtra("current_uploadflag_qrRecv", chosen_line_uploadflag);
                intent.putExtra("current_vbillcode_qrRecv", current_sale_delivery_vbillcodeRecv);
                intent.putExtra("type",getIntent().getIntExtra("type",-1));

                startActivity(intent);
            }
        });
        uploadAll_saleDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expressCode = expressCodeEditText.getText().toString();

                if ("".equals(expressCode) || "".equals(chooseLogisticscompany) || "请选择物流公司".equals(chooseLogisticscompany)) {
                    if("".equals(chooseLogisticscompany) || "请选择物流公司".equals(chooseLogisticscompany)){
                        chooseLogisticscompany ="";
                    }
                    AlertDialog.Builder build = new AlertDialog.Builder(SaleDeliveryDetail.this);
                    build.setTitle("温馨提示")
                            .setMessage("运单号或者物流公司为空，确定要继续吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    pushData();

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                                }
                            })
                            .show();
                } else {


                  pushData();
                }

            }
        });
        uploadSingleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expressCode = expressCodeEditText.getText().toString();


                if ("".equals(expressCode) || "".equals(chooseLogisticscompany) || "请选择物流公司".equals(chooseLogisticscompany)) {
                    if("".equals(chooseLogisticscompany) || "请选择物流公司".equals(chooseLogisticscompany)){
                        chooseLogisticscompany ="";
                    }
                    AlertDialog.Builder build = new AlertDialog.Builder(SaleDeliveryDetail.this);
                    build.setTitle("温馨提示")
                            .setMessage("运单号或者物流公司为空，确定要继续吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {

                                    dialog.dismiss();
                                  pushData();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                  dialog.dismiss();
                                }
                            })
                            .show();
                }else{

                    pushData();
                }

            }
        });
        saleDeliveryDetailHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                zLoadingDialog.dismiss();
                switch (msg.what) {

                    case 0x15:
                        zLoadingDialog.dismiss();
                        Toast.makeText(SaleDeliveryDetail.this,msg.getData().getString("uploadResp"), Toast.LENGTH_LONG).show();
                        updateAllUploadFlag();
                        listAllBodyPostition = QuerySaleDeliveryBody(current_sale_delivery_vbillcodeRecv);
                        setAllItemUpload();
                        finish();
                        break;


                    case 0x18:
                        zLoadingDialog.dismiss();

                        break;
                    case 0x19:
                        zLoadingDialog.dismiss();
                        String s3 = msg.getData().getString("uploadResp");
                        Toast.makeText(SaleDeliveryDetail.this, s3, Toast.LENGTH_LONG).show();
                        break;
                    case 0x20:
                        zLoadingDialog.dismiss();
                        Toast.makeText(SaleDeliveryDetail.this, "运单号没有填写，首先填写运单号", Toast.LENGTH_LONG).show();
                        break;
                    case 0x21:
                        zLoadingDialog.dismiss();
                        Toast.makeText(SaleDeliveryDetail.this, "物流公司没有选择，首先选择物流公司", Toast.LENGTH_LONG).show();
                        break;
                    case 0x22:

                        listAllBodyPostition = QuerySaleDeliveryBody(current_sale_delivery_vbillcodeRecv);
                        adapter = new SaleDeliveryBodyTableAdapter(SaleDeliveryDetail.this, listAllBodyPostition, mListener);
                        tableBodyListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        break;
                    case 0x23:
                        Toast.makeText(SaleDeliveryDetail.this, "该发货单已经全部上传", Toast.LENGTH_LONG).show();
                        break;

                    default:
                        break;
                }
            }
        };
    }

    private void pushData() {

        zLoadingDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Utils.isNetworkConnected(SaleDeliveryDetail.this)) {
                    try {
                        //Y代表已经上传过
                        if (iaAlreadyUploadAll()) {
                           saleDeliveryDetailHandler.sendEmptyMessage(0x23);
                        } else{
                            String  workcode="";
                            switch (getIntent().getIntExtra("type",-1)){
                                case 0:
                                    workcode="R08";
                                    break;
                                case 8:
                                    workcode="R15";
                                    break;
                            }
                            String uploadResp = DataHelper.uploadSaleDeliveryVBill(workcode, db4,current_sale_delivery_vbillcodeRecv,
                                    SaleDeliveryDetail.this,chooseLogisticscompany,expressCode,getIntent().getIntExtra("type",-1));
                            zLoadingDialog.dismiss();
                            if (null != uploadResp) {

                                    SalesRespBean respBean = new Gson().fromJson(uploadResp, SalesRespBean.class);
                                    SalesRespBeanValue respBeanValue =new Gson().fromJson(respBean.getValue(), SalesRespBeanValue.class);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("uploadResp", respBeanValue.getErrmsg());
                                    Message msg = new Message();
                                    if (respBeanValue.getErrno().equals("0")) {
                                        //19弹出erromsg
                                        msg.what = 0x15;
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
                        }

                    } catch (Exception e) {
                        e.printStackTrace();


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

    private void setAllItemUpload() {
        Boolean isY=false;
        Boolean isPY=false;
        Boolean isN=false;


        String flag="";
        for (int i = 0; i <listAllBodyPostition.size() ; i++) {
            if(listAllBodyPostition.get(i).getUploadflag().equals("Y")){
                isY=true;
            }else if(listAllBodyPostition.get(i).getUploadflag().equals("PY")){
                isPY=true;
            }else if(listAllBodyPostition.get(i).getUploadflag().equals("N")){
                isN=true;
            }
        }

        if(isPY || isY){
            if(isN==false && isY && isPY==false ){
                flag="Y";
            }else {
                flag="PY";
            }
        }else {
            flag="N";
        }


        db4.execSQL("update SaleDelivery set flag=? where vbillcode=?", new String[]{flag, current_sale_delivery_vbillcodeRecv});

    }
    SaleDeliveryBodyTableAdapter adapter;
    @Override
    protected void onStart() {

        super.onStart();

        listAllBodyPostition = QuerySaleDeliveryBody(current_sale_delivery_vbillcodeRecv);
        adapter = new SaleDeliveryBodyTableAdapter(SaleDeliveryDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.i("data-->",new Gson().toJson(listAllBodyPostition));
        select(0);
        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select(position);
            }
        });

    }

    private void select(int position) {

        adapter.select(position);
        saleDeliveryScanButton.setEnabled(true);
        uploadSingleButton.setEnabled(true);
        SaleDeliveryBodyBean local_saleDeliveryBodyBean = listAllBodyPostition.get(position);
        chosen_line_vcooporderbcode_b = local_saleDeliveryBodyBean.getVcooporderbcode_b();
        chosen_line_matrname = local_saleDeliveryBodyBean.getMatrname();
        chosen_line_cwarename = local_saleDeliveryBodyBean.getCwarename();
        chosen_line_matrcode = local_saleDeliveryBodyBean.getMatrcode();
        chosen_line_customer = local_saleDeliveryBodyBean.getCustomer();
        chosen_line_maccode = QueryMaccodeFromDB(current_sale_delivery_vbillcodeRecv, local_saleDeliveryBodyBean.getVcooporderbcode_b(), local_saleDeliveryBodyBean.getMatrcode());
        chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
        chosen_line_scannnum = local_saleDeliveryBodyBean.getScannnum();
        chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();

    }

    private boolean isCwarenameSame() {
        for (int i = 0; i <listAllBodyPostition.size() ; i++) {
            listAllBodyPostition.get(i).getCwarename();
        }
        return  false;
    }







    private boolean iaAlreadyUploadAll() {
        Cursor cursor = db4.rawQuery("select vbillcode from SaleDelivery where vbillcode=? and flag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "Y"});
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }

        Cursor cursor3 = db4.rawQuery("select vbillcode,vcooporderbcode_b from SaleDeliveryBody where vbillcode=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "N"});
        Cursor cursorpy = db4.rawQuery("select vbillcode,vcooporderbcode_b from SaleDeliveryBody where vbillcode=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "PY"});
        list = new ArrayList<String>();
        if (cursor3 != null && cursor3.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor3.moveToNext()) {
                list.add(cursor3.getString(cursor3.getColumnIndex("vcooporderbcode_b")));
                Cursor cursor4 = db4.rawQuery("select vbillcode,vcooporderbcode_b,prodcutcode,itemuploadflag from SaleDeliveryScanResult where vbillcode=? and vcooporderbcode_b=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursor3.getString(cursor3.getColumnIndex("vcooporderbcode_b")), "N"});
                if (cursor4 != null && cursor4.getCount() > 0) {
                    //判断cursor中是否存在数据
                    while (cursor4.moveToNext()) {
                        SaleDeliveryUploadFlagBean itemall = new SaleDeliveryUploadFlagBean();
                        itemall.vbillcode = cursor4.getString(cursor4.getColumnIndex("vbillcode"));
                        itemall.vcooporderbcode_b = cursor4.getString(cursor4.getColumnIndex("vcooporderbcode_b"));
                        itemall.prodcutcode = cursor4.getString(cursor4.getColumnIndex("prodcutcode"));

                    }
                    cursor4.close();
                }
            }
            cursor3.close();
        }
        if (cursorpy != null && cursorpy.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursorpy.moveToNext()) {
                list.add(cursorpy.getString(cursorpy.getColumnIndex("vcooporderbcode_b")));
                Cursor cursor5 = db4.rawQuery("select vbillcode,vcooporderbcode_b,prodcutcode,itemuploadflag from SaleDeliveryScanResult where vbillcode=? and vcooporderbcode_b=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursorpy.getString(cursorpy.getColumnIndex("vcooporderbcode_b")), "N"});
                if (cursor5 != null && cursor5.getCount() > 0) {
                    //判断cursor中是否存在数据
                    while (cursor5.moveToNext()) {
                        SaleDeliveryUploadFlagBean itemall2 = new SaleDeliveryUploadFlagBean();
                        itemall2.vbillcode = cursor5.getString(cursor5.getColumnIndex("vbillcode"));
                        itemall2.vcooporderbcode_b = cursor5.getString(cursor5.getColumnIndex("vcooporderbcode_b"));
                        itemall2.prodcutcode = cursor5.getString(cursor5.getColumnIndex("prodcutcode"));


                    }
                    cursor5.close();
                }
            }
            cursorpy.close();
        }

        return false;
    }



    private void updateAllUploadFlag() {
        for (int i = 0; i <listAllBodyPostition.size() ; i++) {
            if(Integer.parseInt(listAllBodyPostition.get(i).getScannnum())!=0){
                if(Integer.parseInt(listAllBodyPostition.get(i).getNnum())==Integer.parseInt(listAllBodyPostition.get(i).getScannnum())){
                    db4.execSQL("update SaleDeliveryBody set uploadflag=? where vbillcode=? and vcooporderbcode_b=?",
                            new String[]{"Y", current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(i).getVcooporderbcode_b()});

                }else {
                    db4.execSQL("update SaleDeliveryBody set uploadflag=? where vbillcode=? and vcooporderbcode_b=? ",
                            new String[]{"PY", current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(i).getVcooporderbcode_b()});

                }

            }

        }
        db4.execSQL("update SaleDeliveryScanResult set itemuploadflag=? where vbillcode=? ",
                new String[]{"Y", current_sale_delivery_vbillcodeRecv});

    }







    public ArrayList<SaleDeliveryBodyBean> QuerySaleDeliveryBody(String current_sale_delivery_vbillcodeRecv) {

        ArrayList<SaleDeliveryBodyBean> list = new ArrayList<SaleDeliveryBodyBean>();

        Cursor cursor= db4.rawQuery("select vcooporderbcode_b,cwarehousecode,cwarename,matrcode,matrname," +
                        "nnum,scannum,customer,uploadflag from SaleDeliveryBody where vbillcode=?",
                new String[]{current_sale_delivery_vbillcodeRecv});

            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                SaleDeliveryBodyBean bean = new SaleDeliveryBodyBean();
                bean.vcooporderbcode_b = cursor.getString(cursor.getColumnIndex("vcooporderbcode_b"));
                bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                bean.matrcode = cursor.getString(cursor.getColumnIndex("matrcode"));
                bean.matrname = cursor.getString(cursor.getColumnIndex("matrname"));
                bean.nnum = cursor.getString(cursor.getColumnIndex("nnum"));
                bean.scannnum = cursor.getString(cursor.getColumnIndex("scannum"));
                bean.Customer = cursor.getString(cursor.getColumnIndex("customer"));
                bean.uploadflag = cursor.getString(cursor.getColumnIndex("uploadflag"));
                list.add(bean);

            }
            cursor.close();

        return list;
    }





    public String QueryMaccodeFromDB(String vbillcode, String vcooporderbcode_b, String matrcode) {
        String maccode = "error";
        Cursor cursor = db4.rawQuery("select maccode from SaleDeliveryBody where vbillcode=? and vcooporderbcode_b=? and matrcode=? ", new String[]{vbillcode, vcooporderbcode_b, matrcode});
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
    private SaleDeliveryBodyTableAdapter.MyClickListener2 mListener = new SaleDeliveryBodyTableAdapter.MyClickListener2() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent(SaleDeliveryDetail.this, SaleDeliveryQRDetail.class);
            intent.putExtra("current_vcooporderbcode_b_qr", listAllBodyPostition.get(position).getVcooporderbcode_b());
            intent.putExtra("current_nnum_qr", listAllBodyPostition.get(position).getNnum());
            intent.putExtra("current_cwarename_qr", listAllBodyPostition.get(position).getCwarename());
            intent.putExtra("current_matrname_qr", listAllBodyPostition.get(position).getMatrname());
            intent.putExtra("current_matrcode_qr", listAllBodyPostition.get(position).getMatrcode());
            intent.putExtra("current_maccode_qr", QueryMaccodeFromDB(current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(position).getVcooporderbcode_b(), listAllBodyPostition.get(position).getMatrcode()));
            intent.putExtra("current_customer_qr", listAllBodyPostition.get(position).getCustomer());
            intent.putExtra("current_vbillcode_qr", current_sale_delivery_vbillcodeRecv);
            intent.putExtra("type",getIntent().getIntExtra("type",-1));
            intent.putExtra("title",getIntent().getStringExtra("title"));
            startActivity(intent);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
