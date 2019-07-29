package com.example.shanggmiqr.transaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
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

import com.example.shanggmiqr.bean.DataBean;
import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.LoanBodyTableAdapter;
import com.example.shanggmiqr.bean.LoanBodyBean;
import com.example.shanggmiqr.bean.SaleDeliverySendBean;
import com.example.shanggmiqr.bean.SaleDeliveryUploadFlagBean;
import com.example.shanggmiqr.bean.SalesRespBean;
import com.example.shanggmiqr.bean.SalesRespBeanValue;
import com.example.shanggmiqr.util.BaseConfig;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class LoanDetail extends AppCompatActivity {
    //借出单明细
    private TextView vbillcodText;
    private TextView dbilldateText;
    private String current_sale_delivery_vbillcodeRecv;
    private String current_sale_delivery_dbilldateRecv;
    private SQLiteDatabase db4;
    private MyDataBaseHelper helper4;
    private List<LoanBodyBean> listAllBodyPostition;
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
    private Handler saleDeliveryDetailHandler = null;
    private List<String> upload_cwarename;
    private String upload_all_cwarename;
    private String upload_all_cwarehousecode;
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
    private List<String> listitem;
    private List<SaleDeliveryUploadFlagBean> lisitemtall;
    //nnum为正 bisreturn为N 为负则为Y
    private String current_bisreturn = "N";
    private ZLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        expressCodeEditText = (EditText) findViewById(R.id.expressCode_edit_text_loanDetail);
        saleDeliveryScanButton = (Button) findViewById(R.id.scan_loanDetail);
        uploadAll_saleDeliveryButton = (Button) findViewById(R.id.uploadall_loanDetail);
        uploadSingleButton = (Button) findViewById(R.id.upload_loanDetail);
        saleDeliveryScanButton.setEnabled(false);
        uploadSingleButton.setEnabled(false);
        dialog = new ZLoadingDialog(LoanDetail.this);
        helper4 = new MyDataBaseHelper(LoanDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db4 = helper4.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_loanDetail_detail);
        Intent _intent = getIntent();
        Log.i("detail-->",getIntent().getIntExtra("type",-1)+"");
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_sale_delivery_vbillcodeRecv = _intent.getStringExtra("current_sale_delivery_vbillcode");
            current_sale_delivery_dbilldateRecv = _intent.getStringExtra("current_sale_delivery_dbilldate");
        }
        vbillcodText = (TextView) findViewById(R.id.vbillcode_loanDetail);
        dbilldateText = (TextView) findViewById(R.id.dbilldate_loanDetail);

        vbillcodText.setText("借出单号:" + current_sale_delivery_vbillcodeRecv);
        dbilldateText.setText("单据日期:" + current_sale_delivery_dbilldateRecv);
        //物流公司选择
        spinner = findViewById(R.id.spinner_logistics_company_loanDetail);
        //加载数据
        myadapter();
        listAllBodyPostition = QueryLoanBody(current_sale_delivery_vbillcodeRecv);
        final LoanBodyTableAdapter adapter = new LoanBodyTableAdapter(LoanDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.select(position);
                saleDeliveryScanButton.setEnabled(true);
                uploadSingleButton.setEnabled(true);
                LoanBodyBean local_saleDeliveryBodyBean = (LoanBodyBean) adapter.getItem(position);

                chosen_line_vcooporderbcode_b = local_saleDeliveryBodyBean.getItempk();
              //  chosen_line_matrname = local_saleDeliveryBodyBean.getMaccode();
                chosen_line_cwarename = local_saleDeliveryBodyBean.getCwarename();
                chosen_line_matrcode = local_saleDeliveryBodyBean.getMaterialcode();
                chosen_line_customer = local_saleDeliveryBodyBean.getVemo();
                chosen_line_maccode = QueryMaccodeFromDB(current_sale_delivery_vbillcodeRecv, local_saleDeliveryBodyBean.getItempk(), local_saleDeliveryBodyBean.getMaterialcode());
                chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
                chosen_line_scannnum = local_saleDeliveryBodyBean.getScannum();
                chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();

                Toast.makeText(LoanDetail.this, chosen_line_matrcode, Toast.LENGTH_LONG).show();
            }
        });
        saleDeliveryScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoanDetail.this, LoanQrScanner.class);
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
                    AlertDialog.Builder build = new AlertDialog.Builder(LoanDetail.this);
                    build.setTitle("温馨提示")
                            .setMessage("运单号或者物流公司为空，确定要继续吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Utils.isNetworkConnected(LoanDetail.this)) {
                                                try {
                                                    //Y代表已经上传过
                                                    if (iaAlreadyUploadAll()) {
                                                        Message msg = new Message();
                                                        msg.what = 0x12;
                                                        saleDeliveryDetailHandler.sendMessage(msg);
                                                    } else if (isCwarenameSame()) {
                                                        String uploadResp = uploadSaleDeliveryVBill("R38", list);
                                                        if (!(null == uploadResp)) {
                                                            if (!(null == lisitemtall)) {
                                                                Gson gson = new Gson();
                                                                SalesRespBean respBean = gson.fromJson(uploadResp, SalesRespBean.class);
                                                                Gson gson2 = new Gson();
                                                                SalesRespBeanValue respBeanValue = gson2.fromJson(respBean.getValue(), SalesRespBeanValue.class);
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("uploadResp", respBeanValue.getErrmsg());
                                                                Message msg = new Message();
                                                                if (respBeanValue.getErrno().equals("0")) {
                                                                    //19弹出erromsg
                                                                    updateAllItemUploadFlag(lisitemtall);
                                                                    msg.what = 0x15;
                                                                } else {
                                                                    //19弹出erromsg
                                                                    msg.what = 0x19;
                                                                }
                                                                msg.setData(bundle);
                                                                saleDeliveryDetailHandler.sendMessage(msg);
                                                            }
                                                        } else {
                                                            Message msg = new Message();
                                                            msg.what = 0x18;
                                                            saleDeliveryDetailHandler.sendMessage(msg);
                                                        }
                                                    } else {
                                                        Message msg = new Message();
                                                        msg.what = 0x16;
                                                        saleDeliveryDetailHandler.sendMessage(msg);
                                                    }

                                                } catch (IOException e) {
                                                    //e.printStackTrace();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("Exception111", e.toString());
                                                    Message msg = new Message();
                                                    msg.what = 0x17;
                                                    msg.setData(bundle);
                                                    saleDeliveryDetailHandler.sendMessage(msg);
                                                    return;
                                                } catch (XmlPullParserException e) {
                                                    //e.printStackTrace();
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
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    return;
                                }
                            })
                            .show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (Utils.isNetworkConnected(LoanDetail.this)) {
                                try {

                                    //Y代表已经上传过
                                    if (iaAlreadyUploadAll()) {
                                        Message msg = new Message();
                                        msg.what = 0x12;
                                        saleDeliveryDetailHandler.sendMessage(msg);
                                    } else if (isCwarenameSame()) {
                                        String uploadResp = uploadSaleDeliveryVBill("R38", list);
                                        if (!(null == uploadResp)) {
                                            if (!(null == lisitemtall)) {
                                                Gson gson = new Gson();
                                                SalesRespBean respBean = gson.fromJson(uploadResp, SalesRespBean.class);
                                                Gson gson2 = new Gson();
                                                SalesRespBeanValue respBeanValue = gson2.fromJson(respBean.getValue(), SalesRespBeanValue.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("uploadResp", respBeanValue.getErrmsg());
                                                Message msg = new Message();
                                                if (respBeanValue.getErrno().equals("0")) {
                                                    //19弹出erromsg
                                                    updateAllItemUploadFlag(lisitemtall);
                                                    msg.what = 0x15;
                                                } else {
                                                    //19弹出erromsg
                                                    msg.what = 0x19;
                                                }
                                                msg.setData(bundle);
                                                saleDeliveryDetailHandler.sendMessage(msg);
                                            }
                                        } else {
                                            Message msg = new Message();
                                            msg.what = 0x18;
                                            saleDeliveryDetailHandler.sendMessage(msg);
                                        }
                                    } else {
                                        Message msg = new Message();
                                        msg.what = 0x16;
                                        saleDeliveryDetailHandler.sendMessage(msg);
                                    }

                                } catch (IOException e) {
                                    //e.printStackTrace();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Exception111", e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x17;
                                    msg.setData(bundle);
                                    saleDeliveryDetailHandler.sendMessage(msg);
                                    return;
                                } catch (XmlPullParserException e) {
                                    //e.printStackTrace();
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
                    AlertDialog.Builder build = new AlertDialog.Builder(LoanDetail.this);
                    build.setTitle("温馨提示")
                            .setMessage("运单号或者物流公司为空，确定要继续吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Utils.isNetworkConnected(LoanDetail.this)) {
                                                try {
                                                    //Y代表已经上传过
                                                    if (iaAlreadyUploadSingle(chosen_line_vcooporderbcode_b)) {
                                                        Message msg = new Message();
                                                        msg.what = 0x13;
                                                        saleDeliveryDetailHandler.sendMessage(msg);
                                                    } else {
                                                        String uploadResp = uploadSaleDeliveryVBill("R38", list);
                                                        if (!(null == uploadResp)) {
                                                            if (!(null == listitem)) {
                                                                Gson gson = new Gson();
                                                                SalesRespBean respBean = gson.fromJson(uploadResp, SalesRespBean.class);
                                                                Gson gson2 = new Gson();
                                                                SalesRespBeanValue respBeanValue = gson2.fromJson(respBean.getValue(), SalesRespBeanValue.class);
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("uploadResp", respBeanValue.getErrmsg());
                                                                Message msg = new Message();
                                                                if (respBeanValue.getErrno().equals("0")) {
                                                                    //19弹出erromsg
                                                                    updateItemUploadFlag(listitem);
                                                                    msg.what = 0x11;
                                                                } else {
                                                                    //19弹出erromsg
                                                                    msg.what = 0x19;
                                                                }
                                                                msg.setData(bundle);
                                                                saleDeliveryDetailHandler.sendMessage(msg);
                                                            }
                                                        } else {
                                                            Message msg = new Message();
                                                            msg.what = 0x18;
                                                            saleDeliveryDetailHandler.sendMessage(msg);
                                                        }
                                                    }

                                                } catch (IOException e) {
                                                    // e.printStackTrace();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("Exception111", e.toString());
                                                    Message msg = new Message();
                                                    msg.what = 0x17;
                                                    msg.setData(bundle);
                                                    saleDeliveryDetailHandler.sendMessage(msg);
                                                    return;
                                                } catch (XmlPullParserException e) {
                                                    // e.printStackTrace();
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
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .show();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (Utils.isNetworkConnected(LoanDetail.this)) {
                                try {
                                    //Y代表已经上传过
                                    if (iaAlreadyUploadSingle(chosen_line_vcooporderbcode_b)) {
                                        Message msg = new Message();
                                        msg.what = 0x13;
                                        saleDeliveryDetailHandler.sendMessage(msg);
                                    } else {
                                        String uploadResp = uploadSaleDeliveryVBill("R38", list);
                                        if (!(null == uploadResp)) {
                                            if (!(null == listitem)) {
                                                Gson gson = new Gson();
                                                SalesRespBean respBean = gson.fromJson(uploadResp, SalesRespBean.class);
                                                Gson gson2 = new Gson();
                                                SalesRespBeanValue respBeanValue = gson2.fromJson(respBean.getValue(), SalesRespBeanValue.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("uploadResp", respBeanValue.getErrmsg());
                                                Message msg = new Message();
                                                if (respBeanValue.getErrno().equals("0")) {
                                                    //19弹出erromsg
                                                    updateItemUploadFlag(listitem);
                                                    msg.what = 0x11;
                                                } else {
                                                    //19弹出erromsg
                                                    msg.what = 0x19;
                                                }
                                                msg.setData(bundle);
                                                saleDeliveryDetailHandler.sendMessage(msg);
                                            }
                                        } else {
                                            Message msg = new Message();
                                            msg.what = 0x18;
                                            saleDeliveryDetailHandler.sendMessage(msg);
                                        }
                                    }

                                } catch (IOException e) {
                                    // e.printStackTrace();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Exception111", e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x17;
                                    msg.setData(bundle);
                                    saleDeliveryDetailHandler.sendMessage(msg);
                                    return;
                                } catch (XmlPullParserException e) {
                                    // e.printStackTrace();
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

            }
        });
        saleDeliveryDetailHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(LoanDetail.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        dialog.dismiss();
                        expressCodeEditText.setText("");
                        spinner.setSelection(logisticscompanies.size() - 1, true);
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(LoanDetail.this, s, Toast.LENGTH_LONG).show();
                        updateUploadFlag();
                        if (isAllItemUpload()) {
                            Intent intent = new Intent(LoanDetail.this, SaleDelivery.class);
                            startActivity(intent);
                            finish();
                        }
                        listAllBodyPostition = QueryLoanBody(current_sale_delivery_vbillcodeRecv);
                        final LoanBodyTableAdapter adapterNew = new LoanBodyTableAdapter(LoanDetail.this, listAllBodyPostition, mListener);
                        tableBodyListView.setAdapter(adapterNew);
                        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapterNew.select(position);
                                saleDeliveryScanButton.setEnabled(true);
                                uploadSingleButton.setEnabled(true);
                                LoanBodyBean local_saleDeliveryBodyBean = (LoanBodyBean) adapterNew.getItem(position);

                                chosen_line_vcooporderbcode_b = local_saleDeliveryBodyBean.getItempk();
                         //       chosen_line_matrname = local_saleDeliveryBodyBean.getMaccode();
                                chosen_line_cwarename = local_saleDeliveryBodyBean.getCwarename();
                                chosen_line_matrcode = local_saleDeliveryBodyBean.getMaterialcode();
                                chosen_line_maccode = QueryMaccodeFromDB(current_sale_delivery_vbillcodeRecv, local_saleDeliveryBodyBean.getItempk(), local_saleDeliveryBodyBean.getMaterialcode());
                                chosen_line_customer = local_saleDeliveryBodyBean.getVemo();
                                chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
                                chosen_line_scannnum = local_saleDeliveryBodyBean.getScannum();
                                chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();

                                //        Toast.makeText(SaleDeliveryDetail.this,chosen_line_matrcode,Toast.LENGTH_LONG).show();
                            }
                        });

                        break;
                    case 0x12:
                        Toast.makeText(LoanDetail.this, "该发货单已经全部上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x13:
                        Toast.makeText(LoanDetail.this, "该行已经上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x14:
                        Toast.makeText(LoanDetail.this, "请先扫码再进行发货上传操作", Toast.LENGTH_LONG).show();
                        break;
                    case 0x15:
                        dialog.dismiss();
                        expressCodeEditText.setText("");
                        spinner.setSelection(logisticscompanies.size() - 1, true);
                        String s2 = msg.getData().getString("uploadResp");
                        Toast.makeText(LoanDetail.this, s2, Toast.LENGTH_LONG).show();
                        updateAllUploadFlag();
                        if (isAllItemUpload()) {
                            Intent intent = new Intent(LoanDetail.this, SaleDelivery.class);
                            startActivity(intent);
                            finish();
                        }
                        listAllBodyPostition = QueryLoanBody(current_sale_delivery_vbillcodeRecv);
                        final LoanBodyTableAdapter adapterNew2 = new LoanBodyTableAdapter(LoanDetail.this, listAllBodyPostition, mListener);
                        tableBodyListView.setAdapter(adapterNew2);
                        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapterNew2.select(position);
                                saleDeliveryScanButton.setEnabled(true);
                                uploadSingleButton.setEnabled(true);
                                LoanBodyBean local_saleDeliveryBodyBean = (LoanBodyBean) adapterNew2.getItem(position);
                                chosen_line_vcooporderbcode_b = local_saleDeliveryBodyBean.getItempk();
                             //   chosen_line_matrname = local_saleDeliveryBodyBean.getMaccode();
                                chosen_line_matrcode = local_saleDeliveryBodyBean.getMaterialcode();
                                chosen_line_cwarename = local_saleDeliveryBodyBean.getCwarename();
                                chosen_line_maccode = QueryMaccodeFromDB(current_sale_delivery_vbillcodeRecv, local_saleDeliveryBodyBean.getItempk(), local_saleDeliveryBodyBean.getMaterialcode());
                                chosen_line_customer = local_saleDeliveryBodyBean.getVemo();
                                chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
                                chosen_line_scannnum = local_saleDeliveryBodyBean.getScannum();
                                chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();
                            }
                        });
                        break;
                    case 0x16:
                        Toast.makeText(LoanDetail.this, "不同仓库的行号不可以同时上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x17:
                        dialog.dismiss();
                        String exception111 = msg.getData().getString("Exception111");
                        Toast.makeText(LoanDetail.this, exception111, Toast.LENGTH_LONG).show();
                        break;
                    case 0x18:
                        dialog.dismiss();
                        Toast.makeText(LoanDetail.this, "接口异常", Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String s3 = msg.getData().getString("uploadResp");
                        Toast.makeText(LoanDetail.this, s3, Toast.LENGTH_LONG).show();
                        break;
                    case 0x20:
                        dialog.dismiss();
                        Toast.makeText(LoanDetail.this, "运单号没有填写，首先填写运单号", Toast.LENGTH_LONG).show();
                        break;
                    case 0x21:
                        dialog.dismiss();
                        Toast.makeText(LoanDetail.this, "物流公司没有选择，首先选择物流公司", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
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
        Cursor cursor00 = db4.rawQuery("select itempk from LoanBody where pobillcode=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "Y"});
        Cursor cursor01 = db4.rawQuery("select itempk from LoanBody where pobillcode=?",
                new String[]{current_sale_delivery_vbillcodeRecv});
        int t1=cursor00.getCount();
        int t2=cursor01.getCount();
        if (t1 > 0 && t1 == t2) {
            db4.execSQL("update Loan set flag=? where pobillcode=?", new String[]{"Y", current_sale_delivery_vbillcodeRecv});
            return true;
        } else if (t1 > 0 && t1 < t2) {
            db4.execSQL("update Loan set flag=? where pobillcode=?", new String[]{"PY", current_sale_delivery_vbillcodeRecv});
            return false;
        } else {
            return false;
        }
    }

    private boolean isCwarenameSame() {
        Cursor cursor3 = db4.rawQuery("select cwarename from LoanBody where pobillcode=? ",
                new String[]{current_sale_delivery_vbillcodeRecv});
        upload_cwarename = new ArrayList<String>();
        HashSet<String> hashSet = new HashSet<String>();
        if (cursor3 != null && cursor3.getCount() > 0) {
            while (cursor3.moveToNext()) {
                upload_cwarename.add(cursor3.getString(cursor3.getColumnIndex("cwarename")));
            }
            cursor3.close();
        }
        for (int i = 0; i < upload_cwarename.size(); i++) {
            hashSet.add(upload_cwarename.get(i).toString());
        }
        if (upload_cwarename.size() == 1) {
            return true;
        } else if (hashSet.size() == upload_cwarename.size()) {
            return false;
        } else {
            return true;
        }
    }

    private void updateItemUploadFlag(List<String> listitem) {
        for (String send : listitem) {
            db4.execSQL("update LoanScanResult set itemuploadflag=? where pobillcode=? and itempk=? and materialcode=? and prodcutcode=?", new String[]{"Y", current_sale_delivery_vbillcodeRecv, chosen_line_vcooporderbcode_b, chosen_line_matrcode, send});
        }
    }

    //扫描上传的prodcutcode更新状态
    private void updateAllItemUploadFlag(List<SaleDeliveryUploadFlagBean> saleDeliveryUploadFlagBean) {
        for (SaleDeliveryUploadFlagBean sdu : saleDeliveryUploadFlagBean) {
            String curr_vbillcode = sdu.getVbillcode();
            String curr_Vcooporderbcode_b = sdu.getVcooporderbcode_b();
            String curr_Prodcutcode = sdu.getProdcutcode();
            db4.execSQL("update LoanScanResult set itemuploadflag=? where pobillcode=? and itempk=? and prodcutcode=?", new String[]{"Y", curr_vbillcode, curr_Vcooporderbcode_b, curr_Prodcutcode});
        }
    }

    private boolean iaAlreadyUploadSingle(String chosen_line_vcooporderbcode_b) {
        Cursor cursor3 = db4.rawQuery("select itempk from LoanBody where pobillcode=? and itempk=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, chosen_line_vcooporderbcode_b, "Y"});
        lisitemtall = new ArrayList<SaleDeliveryUploadFlagBean>();
        // Cursor cursor3 = db3.rawQuery(sql3, null);
        if (cursor3 != null && cursor3.getCount() > 0) {
            return true;
        } else {
            //判断cursor中是否存在数据
            //     while (cursor3.moveToNext()) {
            list = new ArrayList<String>();
            list.add(chosen_line_vcooporderbcode_b);
            Cursor cursor4 = db4.rawQuery("select prodcutcode,itemuploadflag from LoanScanResult where pobillcode=? and itempk=? and itemuploadflag=?",
                    new String[]{current_sale_delivery_vbillcodeRecv, chosen_line_vcooporderbcode_b, "N"});
            listitem = new ArrayList<String>();
            if (cursor4 != null && cursor4.getCount() > 0) {
                //判断cursor中是否存在数据
                while (cursor4.moveToNext()) {
                    listitem.add(cursor4.getString(cursor4.getColumnIndex("prodcutcode")));
                }
                cursor4.close();
            }
            //      }
            cursor3.close();
            return false;//false代表有未上传的
        }
    }

    private boolean iaAlreadyUploadAll() {
        Cursor cursor = db4.rawQuery("select pobillcode from Loan where pobillcode=? and flag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "Y"});
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        lisitemtall = new ArrayList<SaleDeliveryUploadFlagBean>();
        Cursor cursor3 = db4.rawQuery("select pobillcode,itempk from LoanBody where pobillcode=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "N"});
        Cursor cursorpy = db4.rawQuery("select pobillcode,itempk from LoanBody where pobillcode=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "PY"});
        list = new ArrayList<String>();
        if (cursor3 != null && cursor3.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor3.moveToNext()) {
                list.add(cursor3.getString(cursor3.getColumnIndex("itempk")));
                Cursor cursor4 = db4.rawQuery("select pobillcode,itempk,prodcutcode,itemuploadflag from LoanScanResult where pobillcode=? and itempk=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursor3.getString(cursor3.getColumnIndex("itempk")), "N"});
                if (cursor4 != null && cursor4.getCount() > 0) {
                    //判断cursor中是否存在数据
                    while (cursor4.moveToNext()) {
                        SaleDeliveryUploadFlagBean itemall = new SaleDeliveryUploadFlagBean();
                        itemall.vbillcode = cursor4.getString(cursor4.getColumnIndex("pobillcode"));
                        itemall.vcooporderbcode_b = cursor4.getString(cursor4.getColumnIndex("itempk"));
                        itemall.prodcutcode = cursor4.getString(cursor4.getColumnIndex("prodcutcode"));
                        lisitemtall.add(itemall);
                    }
                    cursor4.close();
                }
            }
            cursor3.close();
        }
        if (cursorpy != null && cursorpy.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursorpy.moveToNext()) {
                list.add(cursorpy.getString(cursorpy.getColumnIndex("itempk")));
                Cursor cursor5 = db4.rawQuery("select pobillcode,itempk,prodcutcode,itemuploadflag from LoanScanResult where pobillcode=? and itempk=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursorpy.getString(cursorpy.getColumnIndex("itempk")), "N"});
                if (cursor5 != null && cursor5.getCount() > 0) {
                    //判断cursor中是否存在数据
                    while (cursor5.moveToNext()) {
                        SaleDeliveryUploadFlagBean itemall2 = new SaleDeliveryUploadFlagBean();
                        itemall2.vbillcode = cursor5.getString(cursor5.getColumnIndex("pobillcode"));
                        itemall2.vcooporderbcode_b = cursor5.getString(cursor5.getColumnIndex("itempk"));
                        itemall2.prodcutcode = cursor5.getString(cursor5.getColumnIndex("prodcutcode"));
                        lisitemtall.add(itemall2);
                    }
                    cursor5.close();
                }
            }
            cursorpy.close();
        }

        return false;
    }

    private void updateUploadFlag() {
        Cursor cursor31 = db4.rawQuery("select prodcutcode,itemuploadflag from LoanScanResult where pobillcode=? and itempk=? and itemuploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, chosen_line_vcooporderbcode_b, "Y"});
        Cursor cursor32 = db4.rawQuery("select nnum from LoanBody where pobillcode=? and itempk=?",
                new String[]{current_sale_delivery_vbillcodeRecv, chosen_line_vcooporderbcode_b});
        if (cursor31 != null && cursor31.getCount() > 0 && cursor32 != null && cursor32.getCount() > 0) {
            while (cursor32.moveToNext()) {
                String nnum = cursor32.getString(cursor32.getColumnIndex("nnum"));
                if (cursor31.getCount() == Math.abs(Integer.parseInt(nnum))) {
                    db4.execSQL("update LoanBody set uploadflag=? where pobillcode=? and itempk=? and materialcode=?", new String[]{"Y", current_sale_delivery_vbillcodeRecv, chosen_line_vcooporderbcode_b, chosen_line_matrcode});
                } else if (cursor31.getCount() < Math.abs(Integer.parseInt(nnum))) {
                    db4.execSQL("update LoanBody set uploadflag=? where pobillcode=? and itempk=? and materialcode=?", new String[]{"PY", current_sale_delivery_vbillcodeRecv, chosen_line_vcooporderbcode_b, chosen_line_matrcode});
                }
                cursor31.close();
                cursor32.close();
            }
        }
    }

    private void updateAllUploadFlag() {
        Cursor cursor31 = db4.rawQuery("select itempk from LoanScanResult where pobillcode=?",
                new String[]{current_sale_delivery_vbillcodeRecv});
        if (cursor31 != null && cursor31.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor31.moveToNext()) {
                Cursor cursor3 = db4.rawQuery("select prodcutcode,itemuploadflag from LoanScanResult where pobillcode=? and itempk=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursor31.getString(cursor31.getColumnIndex("itempk")), "Y"});
                Cursor cursor32 = db4.rawQuery("select nnum,materialcode from LoanBody where pobillcode=? and itempk=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursor31.getString(cursor31.getColumnIndex("itempk"))});
                if (cursor3 != null && cursor3.getCount() > 0 && cursor32 != null && cursor32.getCount() > 0) {
                    while (cursor32.moveToNext()) {
                        String nnum = cursor32.getString(cursor32.getColumnIndex("nnum"));
                        String matrcode = cursor32.getString(cursor32.getColumnIndex("materialcode"));
                        if (cursor3.getCount() == Math.abs(Integer.parseInt(nnum))) {
                            db4.execSQL("update LoanBody set uploadflag=? where pobillcode=? and itempk=? and materialcode=?", new String[]{"Y", current_sale_delivery_vbillcodeRecv, cursor31.getString(cursor31.getColumnIndex("itempk")), matrcode});
                        } else if (cursor3.getCount() < Math.abs(Integer.parseInt(nnum))) {
                            db4.execSQL("update LoanBody set uploadflag=? where pobillcode=? and itempk=? and materialcode=?", new String[]{"PY", current_sale_delivery_vbillcodeRecv, cursor31.getString(cursor31.getColumnIndex("itempk")), matrcode});
                        }
                        cursor3.close();
                        cursor32.close();
                    }
                }
            }
            cursor31.close();
        }
    }

    private String uploadSaleDeliveryVBill(String workcode, List<String> list) throws IOException, XmlPullParserException {
        String WSDL_URI;
        String namespace;
        String WSDL_URI_current = BaseConfig.getNcUrl();//wsdl 的uri
        String namespace_current = "http://schemas.xmlsoap.org/soap/envelope/";//namespace
        String methodName = "sendToWISE";//要调用的方法名称
        SharedPreferences proxySp = getSharedPreferences("configInfo", 0);
        int upload_num = 0;
        if (proxySp.getString("WSDL_URI", WSDL_URI_current).equals("") || proxySp.getString("namespace", namespace_current).equals("")) {
            WSDL_URI = WSDL_URI_current;
            namespace = namespace_current;
        } else {
            WSDL_URI = proxySp.getString("WSDL_URI", WSDL_URI_current);
            namespace = proxySp.getString("namespace", namespace_current);
        }
        SoapObject request = new SoapObject(namespace, methodName);
        // 设置需调用WebService接口需要传入的两个参数string、string1
        ArrayList<SaleDeliverySendBean.BodyBean> bodylist = new ArrayList<SaleDeliverySendBean.BodyBean>();
        Cursor cursor2 = db4.rawQuery("select itempk,materialcode,cwarename,nnum,scannum from LoanBody where pobillcode=? ", new String[]{current_sale_delivery_vbillcodeRecv});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor2.moveToNext()) {
                SaleDeliverySendBean.BodyBean bean = new SaleDeliverySendBean.BodyBean();
                bean.itempk = cursor2.getString(cursor2.getColumnIndex("itempk"));
                bean.materialcode = cursor2.getString(cursor2.getColumnIndex("materialcode"));
                bean.setCwarecode(DataHelper.getCwarehousecode(cursor2.getString(cursor2.getColumnIndex("cwarename")),db4));

                String num_check = cursor2.getString(cursor2.getColumnIndex("nnum"));
                if (Integer.parseInt(num_check) < 0) {
                    current_bisreturn = "Y";
                }
                //因为前面已经筛选过cwarename，因此此处不同行的cwarename是唯一的
                upload_all_cwarename = cursor2.getString(cursor2.getColumnIndex("cwarename"));
                bean.pch = "";
                int scanNum = 0;
                ArrayList<SaleDeliverySendBean.BodyBean.SnBean> snlist = new ArrayList<SaleDeliverySendBean.BodyBean.SnBean>();
                if (list.contains(bean.getItempk())) {
                    Cursor cursor3 = db4.rawQuery("select platecode,boxcode,prodcutcode,xlh from LoanScanResult where  pobillcode=? and materialcode=? and itempk=? and itemuploadflag=?",
                            new String[]{current_sale_delivery_vbillcodeRecv, bean.materialcode, bean.getItempk(), "N"});
                    if (cursor3 != null && cursor3.getCount() > 0) {
                        //判断cursor中是否存在数据
                        while (cursor3.moveToNext()) {
                            SaleDeliverySendBean.BodyBean.SnBean snbean = new SaleDeliverySendBean.BodyBean.SnBean();
                            snbean.xlh = cursor3.getString(cursor3.getColumnIndex("xlh"));
                            snbean.txm = cursor3.getString(cursor3.getColumnIndex("prodcutcode"));
                            snbean.xm = cursor3.getString(cursor3.getColumnIndex("boxcode"));
                            snbean.tp = cursor3.getString(cursor3.getColumnIndex("platecode"));
                            upload_all_cwarehousecode = getCwarehousecode(upload_all_cwarename);
                            scanNum += 1;
                            upload_num++;
                            snlist.add(snbean);
                        }
                        cursor3.close();
                    }
                    bean.sn = snlist;
                    //提交过一次的二次提交时不应该被计数
                    bean.nnum = String.valueOf(scanNum);
                    bodylist.add(bean);
                }
            }
            cursor2.close();
        }
        if (bodylist.size()==0){
            Message msg = new Message();
            msg.what = 0x14;
            saleDeliveryDetailHandler.sendMessage(msg);
            return null;
        }
        //通过物流公司名称计算物流公司编号
        String wlCode = "";
        Cursor cursorLogistics = db4.rawQuery("select code from LogisticsCompany where name=?",
                new String[]{chooseLogisticscompany});
        if (cursorLogistics != null && cursorLogistics.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursorLogistics.moveToNext()) {
                wlCode = cursorLogistics.getString(0);
            }
            cursorLogistics.close();
        }
        SharedPreferences currentAccount= getSharedPreferences("current_account", 0);
        String current_user = currentAccount.getString("current_account","");
        SaleDeliverySendBean otherOutgoingSend = new SaleDeliverySendBean("APP", "123456",current_user, wlCode, expressCode, upload_all_cwarehousecode, current_bisreturn, current_sale_delivery_vbillcodeRecv, bodylist);
        otherOutgoingSend.setAppuser(DataHelper.getUser(LoanDetail.this));
        otherOutgoingSend.setBillmaker(DataHelper.getUser(LoanDetail.this));
        otherOutgoingSend.setWlorgcode(wlCode);
        otherOutgoingSend.setWlbillcode(expressCode);
        Gson gson = new Gson();
        String userSendBean = gson.toJson(otherOutgoingSend);

        request.addProperty("string", workcode);
        request.addProperty("string1", userSendBean);
        Log.i("request-->",request.toString());
        //request.addProperty("string1", "{\"begintime\":\"1900-01-20 00:00:00\",\"endtime\":\"2018-08-21 00:00:00\", \"pagenum\":\"1\",\"pagetotal\":\"66\"}");
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.bodyOut = request;
        envelope.dotNet = false;
        String saleDeliveryUploadDataResp = null;
        if (upload_num == 0) {
        } else {
            HttpTransportSE se = new HttpTransportSE(WSDL_URI, 60000);
            //  se.call(null, envelope);//调用 version1.2
            //version1.1 需要如下soapaction
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                            .setLoadingColor(Color.BLUE)//颜色
                            .setHintText("等待服务器返回数据...")
                            .setCancelable(false)
                            .setCanceledOnTouchOutside(false)
                            .setHintTextSize(16) // 设置字体大小 dp
                            .setHintTextColor(Color.GRAY)  // 设置字体颜色
                            .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                            //     .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                            .show();
                }
            });
            se.call(namespace + "sendToWISE", envelope);
            // 获取返回的数据
            // SoapObject object = (SoapObject) envelope.bodyIn;
            Object object = envelope.getResponse();
            saleDeliveryUploadDataResp = new Gson().toJson(object);
        }
        Log.i("response-->",envelope.bodyIn.toString());
        // 获取返回的结果
        // saleDeliveryUploadDataResp = object.getProperty(0).toString();
        return saleDeliveryUploadDataResp;
    }

    @Override
    protected void onResume() {
        super.onResume();
        saleDeliveryScanButton.setEnabled(false);
        uploadSingleButton.setEnabled(false);
        listAllBodyPostition = QueryLoanBody(current_sale_delivery_vbillcodeRecv);
        final LoanBodyTableAdapter adapterNew = new LoanBodyTableAdapter(LoanDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapterNew);
        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterNew.select(position);
                saleDeliveryScanButton.setEnabled(true);
                uploadSingleButton.setEnabled(true);
                LoanBodyBean local_saleDeliveryBodyBean = (LoanBodyBean) adapterNew.getItem(position);
                chosen_line_vcooporderbcode_b = local_saleDeliveryBodyBean.getItempk();
              //  chosen_line_matrname = local_saleDeliveryBodyBean.getMaccode();
                chosen_line_cwarename = local_saleDeliveryBodyBean.getCwarename();
                chosen_line_matrcode = local_saleDeliveryBodyBean.getMaterialcode();
                chosen_line_maccode = QueryMaccodeFromDB(current_sale_delivery_vbillcodeRecv, local_saleDeliveryBodyBean.getItempk(), local_saleDeliveryBodyBean.getMaterialcode());
                chosen_line_customer = local_saleDeliveryBodyBean.getVemo();
                chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
                chosen_line_scannnum = local_saleDeliveryBodyBean.getScannum();
                chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();
            }
        });
    }

    public ArrayList<LoanBodyBean> QueryLoanBody(String current_sale_delivery_vbillcodeRecv) {
        ArrayList<LoanBodyBean> list = new ArrayList<LoanBodyBean>();
        Cursor cursor = db4.rawQuery("select itempk,cwarecode,cwarename,materialcode,maccode,nnum,scannum,vemo,uploadflag from LoanBody where pobillcode=?", new String[]{current_sale_delivery_vbillcodeRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                LoanBodyBean bean = new LoanBodyBean();
                bean.itempk = cursor.getString(cursor.getColumnIndex("itempk"));
                bean.scannum = cursor.getString(cursor.getColumnIndex("scannum"));
                bean.materialcode = cursor.getString(cursor.getColumnIndex("materialcode"));
        //        bean.maccode = cursor.getString(cursor.getColumnIndex("maccode"));
                bean.nnum = cursor.getString(cursor.getColumnIndex("nnum"));
                bean.cwarename = cursor.getString(cursor.getColumnIndex("cwarename"));
                bean.vemo = cursor.getString(cursor.getColumnIndex("vemo"));
                bean.uploadflag = cursor.getString(cursor.getColumnIndex("uploadflag"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    private String getCwarehousecode(String cwarename) {
        Cursor cursor = db4.rawQuery("select code from Warehouse where name=?",
                new String[]{cwarename});
        String cwarehousecode = null;
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                cwarehousecode = cursor.getString(cursor.getColumnIndex("code"));
            }
            cursor.close();
        } else {
            cwarehousecode = "";
        }
        return cwarehousecode;
    }

    public String QueryMaccodeFromDB(String vbillcode, String vcooporderbcode_b, String matrcode) {
        String maccode = "error";
        Cursor cursor = db4.rawQuery("select maccode from LoanBody where pobillcode=? and itempk=? and materialcode=? ", new String[]{vbillcode, vcooporderbcode_b, matrcode});
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
    private LoanBodyTableAdapter.MyClickListener2 mListener = new LoanBodyTableAdapter.MyClickListener2() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent(LoanDetail.this, LoanQRDetail.class);
            intent.putExtra("current_vcooporderbcode_b_qr", listAllBodyPostition.get(position).getItempk());
            intent.putExtra("current_nnum_qr", listAllBodyPostition.get(position).getNnum());
            intent.putExtra("current_cwarename_qr", listAllBodyPostition.get(position).getCwarename());
      //      intent.putExtra("current_matrname_qr", listAllBodyPostition.get(position).getMaccode());
            intent.putExtra("current_matrcode_qr", listAllBodyPostition.get(position).getMaterialcode());
            intent.putExtra("current_maccode_qr", QueryMaccodeFromDB(current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(position).getItempk(), listAllBodyPostition.get(position).getMaterialcode()));
            intent.putExtra("current_customer_qr", listAllBodyPostition.get(position).getVemo());
            intent.putExtra("current_vbillcode_qr", current_sale_delivery_vbillcodeRecv);
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
