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

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.AllocateTransferBodyTableAdapter;
import com.example.shanggmiqr.bean.AllocateTransferBodyBean;
import com.example.shanggmiqr.bean.AllocateTransferSendBean;
import com.example.shanggmiqr.bean.AllocateTransferUploadFlagBean;
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
import java.util.List;

;

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class AllocateTransferDetail extends AppCompatActivity {
    //调拨出库
    private TextView vbillcodText;
    private TextView dbilldateText;
    private String current_sale_delivery_vbillcodeRecv;
    private String current_sale_delivery_dbilldateRecv;
    private String current_sale_delivery_orgRecv;
    private SQLiteDatabase db4;
    private MyDataBaseHelper helper4;
    private List<AllocateTransferBodyBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button saleDeliveryScanButton;
    private Button uploadAll_saleDeliveryButton;
    private Button uploadSingleButton;
    private String headpk;
    private String billmaker;
    private String chosen_line_nnum;
    private String chosen_line_itempk;
    private String chosen_line_ysnum;
    private String chosen_line_materialcode;
    private String chosen_line_scannum;
    private String chosen_line_uploadflag;
    private String chosen_line_address;
    private String chosen_line_materialclasscode;
    private String chosen_line_cwarehousecode;
    private String chosen_line_rwarehousecode;

    private String[] matrcodeList[];
    private Handler allocateTransferDetailHandler = null;
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
    private List<AllocateTransferUploadFlagBean> lisitemtall;
    //nnum为正 bisreturn为N 为负则为Y
    private String current_bisreturn = "N";
    private ZLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocate_transfer_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        expressCodeEditText = (EditText) findViewById(R.id.expressCode_edit_text_allocateTransfer);
        saleDeliveryScanButton = (Button) findViewById(R.id.scan_allocateTransfer);
        uploadAll_saleDeliveryButton = (Button) findViewById(R.id.uploadall_allocateTransfer);
        uploadSingleButton = (Button) findViewById(R.id.upload_allocateTransfer);
        saleDeliveryScanButton.setEnabled(false);
        uploadSingleButton.setEnabled(false);
        dialog = new ZLoadingDialog(AllocateTransferDetail.this);
        helper4 = new MyDataBaseHelper(AllocateTransferDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db4 = helper4.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_allocate_transfer_detail);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_sale_delivery_vbillcodeRecv = _intent.getStringExtra("current_sale_delivery_vbillcode");
            current_sale_delivery_dbilldateRecv = _intent.getStringExtra("current_sale_delivery_dbilldate");
            current_sale_delivery_orgRecv = _intent.getStringExtra("current_sale_delivery_org");
        }
        vbillcodText = (TextView) findViewById(R.id.vbillcode_allocateTransfer);
        dbilldateText = (TextView) findViewById(R.id.dbilldate_allocateTransfer);

        vbillcodText.setText("发货单号:" + current_sale_delivery_vbillcodeRecv);
        dbilldateText.setText("发货日期:" + current_sale_delivery_dbilldateRecv);
        //物流公司选择
        spinner = findViewById(R.id.spinner_logistics_company_allocateTransfer);
        //加载数据
        myadapter();
        listAllBodyPostition = QueryAllocateTransferBody(current_sale_delivery_vbillcodeRecv);
        final AllocateTransferBodyTableAdapter adapter = new AllocateTransferBodyTableAdapter(AllocateTransferDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.select(position);
                saleDeliveryScanButton.setEnabled(true);
                uploadSingleButton.setEnabled(true);
                AllocateTransferBodyBean local_saleDeliveryBodyBean = (AllocateTransferBodyBean) adapter.getItem(position);

                chosen_line_itempk = local_saleDeliveryBodyBean.getItempk();
                chosen_line_address = local_saleDeliveryBodyBean.getAddress();
                chosen_line_materialcode = local_saleDeliveryBodyBean.getMaterialcode();
                chosen_line_materialclasscode = local_saleDeliveryBodyBean.getMaterialclasscode();
                chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
                chosen_line_cwarehousecode = local_saleDeliveryBodyBean.getCwarehousecode();
                chosen_line_rwarehousecode = local_saleDeliveryBodyBean.getRwarehousecode();
                chosen_line_scannum = local_saleDeliveryBodyBean.getScannum();
                chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();

                Toast.makeText(AllocateTransferDetail.this, chosen_line_materialcode, Toast.LENGTH_LONG).show();
            }
        });
        saleDeliveryScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllocateTransferDetail.this, AllocateTransferQrScanner.class);
                intent.putExtra("current_itempk_qrRecv", chosen_line_itempk);
                intent.putExtra("current_address_qrRecv", chosen_line_address);
                intent.putExtra("current_materialcode_qrRecv", chosen_line_materialcode);
                intent.putExtra("current_materialclasscode_qrRecv", chosen_line_materialclasscode);
                intent.putExtra("current_nnum_qrRecv", chosen_line_nnum);
                intent.putExtra("current_cwarehousecode_qrRecv", chosen_line_cwarehousecode);
                intent.putExtra("current_rwarehousecode_qrRecv", chosen_line_rwarehousecode);
                intent.putExtra("current_scannum_qrRecv", chosen_line_scannum);
                intent.putExtra("current_uploadflag_qrRecv", chosen_line_uploadflag);
                intent.putExtra("current_vbillcode_qrRecv", current_sale_delivery_vbillcodeRecv);
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
                    AlertDialog.Builder build = new AlertDialog.Builder(AllocateTransferDetail.this);
                    build.setTitle("温馨提示")
                            .setMessage("运单号或者物流公司为空，确定要继续吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Utils.isNetworkConnected(AllocateTransferDetail.this)) {
                                                try {
                                                    //Y代表已经上传过
                                                    if (iaAlreadyUploadAll()) {
                                                        Message msg = new Message();
                                                        msg.what = 0x12;
                                                        allocateTransferDetailHandler.sendMessage(msg);
                                                    } else if (isCwarenameSame()) {
                                                        String uploadResp = uploadSaleDeliveryVBill("R43", list);
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
                                                                allocateTransferDetailHandler.sendMessage(msg);
                                                            }
                                                        } else {
                                                            Message msg = new Message();
                                                            msg.what = 0x18;
                                                            allocateTransferDetailHandler.sendMessage(msg);
                                                        }
                                                    } else {
                                                        Message msg = new Message();
                                                        msg.what = 0x16;
                                                        allocateTransferDetailHandler.sendMessage(msg);
                                                    }

                                                } catch (IOException e) {
                                                    //e.printStackTrace();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("Exception111", e.toString());
                                                    Message msg = new Message();
                                                    msg.what = 0x17;
                                                    msg.setData(bundle);
                                                    allocateTransferDetailHandler.sendMessage(msg);
                                                    return;
                                                } catch (XmlPullParserException e) {
                                                    //e.printStackTrace();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("Exception111", e.toString());
                                                    Message msg = new Message();
                                                    msg.what = 0x17;
                                                    msg.setData(bundle);
                                                    allocateTransferDetailHandler.sendMessage(msg);
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
                            if (Utils.isNetworkConnected(AllocateTransferDetail.this)) {
                                try {

                                    //Y代表已经上传过
                                    if (iaAlreadyUploadAll()) {
                                        Message msg = new Message();
                                        msg.what = 0x12;
                                        allocateTransferDetailHandler.sendMessage(msg);
                                    } else if (isCwarenameSame()) {
                                        String uploadResp = uploadSaleDeliveryVBill("R43", list);
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
                                                allocateTransferDetailHandler.sendMessage(msg);
                                            }
                                        } else {
                                            Message msg = new Message();
                                            msg.what = 0x18;
                                            allocateTransferDetailHandler.sendMessage(msg);
                                        }
                                    } else {
                                        Message msg = new Message();
                                        msg.what = 0x16;
                                        allocateTransferDetailHandler.sendMessage(msg);
                                    }

                                } catch (IOException e) {
                                    //e.printStackTrace();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Exception111", e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x17;
                                    msg.setData(bundle);
                                    allocateTransferDetailHandler.sendMessage(msg);
                                    return;
                                } catch (XmlPullParserException e) {
                                    //e.printStackTrace();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Exception111", e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x17;
                                    msg.setData(bundle);
                                    allocateTransferDetailHandler.sendMessage(msg);
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
                    AlertDialog.Builder build = new AlertDialog.Builder(AllocateTransferDetail.this);
                    build.setTitle("温馨提示")
                            .setMessage("运单号或者物流公司为空，确定要继续吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (Utils.isNetworkConnected(AllocateTransferDetail.this)) {
                                                try {
                                                    //Y代表已经上传过
                                                    if (iaAlreadyUploadSingle(chosen_line_itempk)) {
                                                        Message msg = new Message();
                                                        msg.what = 0x13;
                                                        allocateTransferDetailHandler.sendMessage(msg);
                                                    } else {
                                                        String uploadResp = uploadSaleDeliveryVBill("R43", list);
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
                                                                allocateTransferDetailHandler.sendMessage(msg);
                                                            }
                                                        } else {
                                                            Message msg = new Message();
                                                            msg.what = 0x18;
                                                            allocateTransferDetailHandler.sendMessage(msg);
                                                        }
                                                    }

                                                } catch (IOException e) {
                                                    // e.printStackTrace();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("Exception111", e.toString());
                                                    Message msg = new Message();
                                                    msg.what = 0x17;
                                                    msg.setData(bundle);
                                                    allocateTransferDetailHandler.sendMessage(msg);
                                                    return;
                                                } catch (XmlPullParserException e) {
                                                    // e.printStackTrace();
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("Exception111", e.toString());
                                                    Message msg = new Message();
                                                    msg.what = 0x17;
                                                    msg.setData(bundle);
                                                    allocateTransferDetailHandler.sendMessage(msg);
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
                            if (Utils.isNetworkConnected(AllocateTransferDetail.this)) {
                                try {
                                    //Y代表已经上传过
                                    if (iaAlreadyUploadSingle(chosen_line_itempk)) {
                                        Message msg = new Message();
                                        msg.what = 0x13;
                                        allocateTransferDetailHandler.sendMessage(msg);
                                    } else {
                                        String uploadResp = uploadSaleDeliveryVBill("R43", list);
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
                                                allocateTransferDetailHandler.sendMessage(msg);
                                            }
                                        } else {
                                            Message msg = new Message();
                                            msg.what = 0x18;
                                            allocateTransferDetailHandler.sendMessage(msg);
                                        }
                                    }

                                } catch (IOException e) {
                                    // e.printStackTrace();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Exception111", e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x17;
                                    msg.setData(bundle);
                                    allocateTransferDetailHandler.sendMessage(msg);
                                    return;
                                } catch (XmlPullParserException e) {
                                    // e.printStackTrace();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Exception111", e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x17;
                                    msg.setData(bundle);
                                    allocateTransferDetailHandler.sendMessage(msg);
                                    return;
                                }
                            }
                        }
                    }).start();
                }

            }
        });
        allocateTransferDetailHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(AllocateTransferDetail.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        dialog.dismiss();
                        expressCodeEditText.setText("");
                        spinner.setSelection(logisticscompanies.size() - 1, true);
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(AllocateTransferDetail.this, s, Toast.LENGTH_LONG).show();
                        updateUploadFlag();
                        if (isAllItemUpload()) {
                            Intent intent = new Intent(AllocateTransferDetail.this, SaleDelivery.class);
                            startActivity(intent);
                            finish();
                        }
                        listAllBodyPostition = QueryAllocateTransferBody(current_sale_delivery_vbillcodeRecv);
                        final AllocateTransferBodyTableAdapter adapterNew = new AllocateTransferBodyTableAdapter(AllocateTransferDetail.this, listAllBodyPostition, mListener);
                        tableBodyListView.setAdapter(adapterNew);
                        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapterNew.select(position);
                                saleDeliveryScanButton.setEnabled(true);
                                uploadSingleButton.setEnabled(true);
                                AllocateTransferBodyBean local_saleDeliveryBodyBean = (AllocateTransferBodyBean) adapterNew.getItem(position);

                                chosen_line_itempk = local_saleDeliveryBodyBean.getItempk();
                                chosen_line_address = local_saleDeliveryBodyBean.getAddress();
                                chosen_line_materialcode = local_saleDeliveryBodyBean.getMaterialcode();
                                chosen_line_materialclasscode = local_saleDeliveryBodyBean.getMaterialclasscode();
                                chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
                                chosen_line_cwarehousecode = local_saleDeliveryBodyBean.getCwarehousecode();
                                chosen_line_rwarehousecode = local_saleDeliveryBodyBean.getRwarehousecode();
                                chosen_line_scannum = local_saleDeliveryBodyBean.getScannum();
                                chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();

                            }
                        });

                        break;
                    case 0x12:
                        Toast.makeText(AllocateTransferDetail.this, "该发货单已经全部上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x13:
                        Toast.makeText(AllocateTransferDetail.this, "该行已经上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x14:
                        Toast.makeText(AllocateTransferDetail.this, "请先扫码再进行发货上传操作", Toast.LENGTH_LONG).show();
                        break;
                    case 0x15:
                        dialog.dismiss();
                        expressCodeEditText.setText("");
                        spinner.setSelection(logisticscompanies.size() - 1, true);
                        String s2 = msg.getData().getString("uploadResp");
                        Toast.makeText(AllocateTransferDetail.this, s2, Toast.LENGTH_LONG).show();
                        updateAllUploadFlag();
                        if (isAllItemUpload()) {
                            Intent intent = new Intent(AllocateTransferDetail.this, SaleDelivery.class);
                            startActivity(intent);
                            finish();
                        }
                        listAllBodyPostition = QueryAllocateTransferBody(current_sale_delivery_vbillcodeRecv);
                        final AllocateTransferBodyTableAdapter adapterNew2 = new AllocateTransferBodyTableAdapter(AllocateTransferDetail.this, listAllBodyPostition, mListener);
                        tableBodyListView.setAdapter(adapterNew2);
                        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapterNew2.select(position);
                                saleDeliveryScanButton.setEnabled(true);
                                uploadSingleButton.setEnabled(true);
                                AllocateTransferBodyBean local_saleDeliveryBodyBean = (AllocateTransferBodyBean) adapterNew2.getItem(position);

                                chosen_line_itempk = local_saleDeliveryBodyBean.getItempk();
                                chosen_line_address = local_saleDeliveryBodyBean.getAddress();
                                chosen_line_materialcode = local_saleDeliveryBodyBean.getMaterialcode();
                                chosen_line_materialclasscode = local_saleDeliveryBodyBean.getMaterialclasscode();
                                chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
                                chosen_line_cwarehousecode = local_saleDeliveryBodyBean.getCwarehousecode();
                                chosen_line_rwarehousecode = local_saleDeliveryBodyBean.getRwarehousecode();
                                chosen_line_scannum = local_saleDeliveryBodyBean.getScannum();
                                chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();
                            }
                        });
                        break;
                    case 0x16:
                        Toast.makeText(AllocateTransferDetail.this, "不同仓库的行号不可以同时上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x17:
                        dialog.dismiss();
                        String exception111 = msg.getData().getString("Exception111");
                        Toast.makeText(AllocateTransferDetail.this, exception111, Toast.LENGTH_LONG).show();
                        break;
                    case 0x18:
                        dialog.dismiss();
                        Toast.makeText(AllocateTransferDetail.this, "接口异常", Toast.LENGTH_LONG).show();
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String s3 = msg.getData().getString("uploadResp");
                        Toast.makeText(AllocateTransferDetail.this, s3, Toast.LENGTH_LONG).show();
                        break;
                    case 0x20:
                        dialog.dismiss();
                        Toast.makeText(AllocateTransferDetail.this, "运单号没有填写，首先填写运单号", Toast.LENGTH_LONG).show();
                        break;
                    case 0x21:
                        dialog.dismiss();
                        Toast.makeText(AllocateTransferDetail.this, "物流公司没有选择，首先选择物流公司", Toast.LENGTH_LONG).show();
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
        Cursor cursor00 = db4.rawQuery("select itempk from ProductEntryBody where billcode=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "Y"});
        Cursor cursor01 = db4.rawQuery("select itempk from ProductEntryBody where billcode=?",
                new String[]{current_sale_delivery_vbillcodeRecv});
        int t1=cursor00.getCount();
        int t2=cursor01.getCount();
        if (t1 > 0 && t1 == t2) {
            db4.execSQL("update ProductEntry set flag=? where billcode=?", new String[]{"Y", current_sale_delivery_vbillcodeRecv});
            return true;
        } else if (t1 > 0 && t1 < t2) {
            db4.execSQL("update ProductEntry set flag=? where billcode=?", new String[]{"PY", current_sale_delivery_vbillcodeRecv});
            return false;
        } else {
            return false;
        }
    }

    private boolean isCwarenameSame() {
      return true;
    }

    private void updateItemUploadFlag(List<String> listitem) {
        for (String send : listitem) {
            db4.execSQL("update ProductEntryScanResult set itemuploadflag=? where billcode=? and itempk=? and materialcode=? and prodcutcode=?", new String[]{"Y", current_sale_delivery_vbillcodeRecv, chosen_line_itempk, chosen_line_materialcode, send});
        }
    }

    //扫描上传的prodcutcode更新状态
    private void updateAllItemUploadFlag(List<AllocateTransferUploadFlagBean> saleDeliveryUploadFlagBean) {
        for (AllocateTransferUploadFlagBean sdu : saleDeliveryUploadFlagBean) {
            String curr_vbillcode = sdu.getBillno();
            String curr_Vcooporderbcode_b = sdu.getItempk();
            String curr_Prodcutcode = sdu.getProdcutcode();
            db4.execSQL("update ProductEntryScanResult set itemuploadflag=? where billcode=? and itempk=? and prodcutcode=?", new String[]{"Y", curr_vbillcode, curr_Vcooporderbcode_b, curr_Prodcutcode});
        }
    }

    private boolean iaAlreadyUploadSingle(String chosen_line_itempk) {
        Cursor cursor3 = db4.rawQuery("select itempk from ProductEntryBody where billcode=? and itempk=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, chosen_line_itempk, "Y"});
        lisitemtall = new ArrayList<AllocateTransferUploadFlagBean>();
        // Cursor cursor3 = db3.rawQuery(sql3, null);
        if (cursor3 != null && cursor3.getCount() > 0) {
            return true;
        } else {
            //判断cursor中是否存在数据
            //     while (cursor3.moveToNext()) {
            list = new ArrayList<String>();
            list.add(chosen_line_itempk);
            Cursor cursor4 = db4.rawQuery("select prodcutcode,itemuploadflag from ProductEntryScanResult where billcode=? and itempk=? and itemuploadflag=?",
                    new String[]{current_sale_delivery_vbillcodeRecv, chosen_line_itempk, "N"});
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
        Cursor cursor = db4.rawQuery("select billno from AllocateTransfer where billno=? and flag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "Y"});
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        lisitemtall = new ArrayList<AllocateTransferUploadFlagBean>();
        Cursor cursor3 = db4.rawQuery("select billno,itempk from AllocateTransferBody where billno=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "N"});
        Cursor cursorpy = db4.rawQuery("select billno,itempk from AllocateTransferBody where billno=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "PY"});
        list = new ArrayList<String>();
        if (cursor3 != null && cursor3.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor3.moveToNext()) {
                list.add(cursor3.getString(cursor3.getColumnIndex("itempk")));
                Cursor cursor4 = db4.rawQuery("select billno,itempk,prodcutcode,itemuploadflag from AllocateTransferScanResult where billno=? and itempk=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursor3.getString(cursor3.getColumnIndex("itempk")), "N"});
                if (cursor4 != null && cursor4.getCount() > 0) {
                    //判断cursor中是否存在数据
                    while (cursor4.moveToNext()) {
                        AllocateTransferUploadFlagBean itemall = new AllocateTransferUploadFlagBean();
                        itemall.billno = cursor4.getString(cursor4.getColumnIndex("billno"));
                        itemall.itempk = cursor4.getString(cursor4.getColumnIndex("itempk"));
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
                Cursor cursor5 = db4.rawQuery("select billno,itempk,prodcutcode,itemuploadflag from AllocateTransferScanResult where billno=? and itempk=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursorpy.getString(cursorpy.getColumnIndex("itempk")), "N"});
                if (cursor5 != null && cursor5.getCount() > 0) {
                    //判断cursor中是否存在数据
                    while (cursor5.moveToNext()) {
                        AllocateTransferUploadFlagBean itemall2 = new AllocateTransferUploadFlagBean();
                        itemall2.billno = cursor5.getString(cursor5.getColumnIndex("billno"));
                        itemall2.itempk = cursor5.getString(cursor5.getColumnIndex("itempk"));
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
        Cursor cursor31 = db4.rawQuery("select prodcutcode,itemuploadflag from AllocateTransferScanResult where billno=? and itempk=? and itemuploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, chosen_line_itempk, "Y"});
        Cursor cursor32 = db4.rawQuery("select nnum from AllocateTransferBody where billno=? and itempk=?",
                new String[]{current_sale_delivery_vbillcodeRecv, chosen_line_itempk});
        if (cursor31 != null && cursor31.getCount() > 0 && cursor32 != null && cursor32.getCount() > 0) {
            while (cursor32.moveToNext()) {
                String nnum = cursor32.getString(cursor32.getColumnIndex("nnum"));
                if (cursor31.getCount() == Math.abs(Integer.parseInt(nnum))) {
                    db4.execSQL("update AllocateTransferBody set uploadflag=? where billno=? and itempk=? and materialcode=?", new String[]{"Y", current_sale_delivery_vbillcodeRecv, chosen_line_itempk, chosen_line_materialcode});
                } else if (cursor31.getCount() < Math.abs(Integer.parseInt(nnum))) {
                    db4.execSQL("update AllocateTransferBody set uploadflag=? where billno=? and itempk=? and materialcode=?", new String[]{"PY", current_sale_delivery_vbillcodeRecv, chosen_line_itempk, chosen_line_materialcode});
                }
                cursor31.close();
                cursor32.close();
            }
        }
    }

    private void updateAllUploadFlag() {
        Cursor cursor31 = db4.rawQuery("select itempk from AllocateTransferScanResult where billno=?",
                new String[]{current_sale_delivery_vbillcodeRecv});
        if (cursor31 != null && cursor31.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor31.moveToNext()) {
                Cursor cursor3 = db4.rawQuery("select prodcutcode,itemuploadflag from AllocateTransferScanResult where billno=? and itempk=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursor31.getString(cursor31.getColumnIndex("itempk")), "Y"});
                Cursor cursor32 = db4.rawQuery("select nnum,materialcode from AllocateTransferBody where billno=? and itempk=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursor31.getString(cursor31.getColumnIndex("itempk"))});
                if (cursor3 != null && cursor3.getCount() > 0 && cursor32 != null && cursor32.getCount() > 0) {
                    while (cursor32.moveToNext()) {
                        String nnum = cursor32.getString(cursor32.getColumnIndex("nnum"));
                        String matrcode = cursor32.getString(cursor32.getColumnIndex("materialcode"));
                        if (cursor3.getCount() == Math.abs(Integer.parseInt(nnum))) {
                            db4.execSQL("update AllocateTransferBody set uploadflag=? where billno=? and itempk=? and materialcode=?", new String[]{"Y", current_sale_delivery_vbillcodeRecv, cursor31.getString(cursor31.getColumnIndex("itempk")), matrcode});
                        } else if (cursor3.getCount() < Math.abs(Integer.parseInt(nnum))) {
                            db4.execSQL("update AllocateTransferBody set uploadflag=? where billno=? and itempk=? and materialcode=?", new String[]{"PY", current_sale_delivery_vbillcodeRecv, cursor31.getString(cursor31.getColumnIndex("itempk")), matrcode});
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
        ArrayList<AllocateTransferSendBean.BodyBean> bodylist = new ArrayList<AllocateTransferSendBean.BodyBean>();
        Cursor cursor2 = db4.rawQuery("select itempk,materialcode,address,nnum,scannum from AllocateTransferBody where billno=? ", new String[]{current_sale_delivery_vbillcodeRecv});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor2.moveToNext()) {
                AllocateTransferSendBean.BodyBean bean = new AllocateTransferSendBean.BodyBean();
                bean.itempk = cursor2.getString(cursor2.getColumnIndex("itempk"));
                bean.yfnum = cursor2.getString(cursor2.getColumnIndex("nnum"));
                bean.materialcode = cursor2.getString(cursor2.getColumnIndex("materialcode"));
                int scanNum = 0;
                ArrayList<AllocateTransferSendBean.BodyBean.SnBean> snlist = new ArrayList<AllocateTransferSendBean.BodyBean.SnBean>();
                if (list.contains(bean.itempk)) {
                    Cursor cursor3 = db4.rawQuery("select platecode,boxcode,prodcutcode,xlh from AllocateTransferScanResult where billno=? and itempk=? and itemuploadflag=?",
                            new String[]{current_sale_delivery_vbillcodeRecv, bean.itempk, "N"});
                    if (cursor3 != null && cursor3.getCount() > 0) {
                        //判断cursor中是否存在数据
                        while (cursor3.moveToNext()) {
                            AllocateTransferSendBean.BodyBean.SnBean snbean = new AllocateTransferSendBean.BodyBean.SnBean();
                            snbean.xlh = cursor3.getString(cursor3.getColumnIndex("xlh"));
                            snbean.txm = cursor3.getString(cursor3.getColumnIndex("prodcutcode"));
                            snbean.xm = cursor3.getString(cursor3.getColumnIndex("boxcode"));
                            snbean.tp = cursor3.getString(cursor3.getColumnIndex("platecode"));
                            scanNum += 1;
                            upload_num++;
                            snlist.add(snbean);
                        }
                        cursor3.close();
                    }
                    bean.sn = snlist;
                    //提交过一次的二次提交时不应该被计数
                    bean.sfnum = String.valueOf(scanNum);
                    bodylist.add(bean);
                }
            }
            cursor2.close();
        }
        if (bodylist.size()==0){
            Message msg = new Message();
            msg.what = 0x14;
            allocateTransferDetailHandler.sendMessage(msg);
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
        AllocateTransferSendBean otherOutgoingSend = new AllocateTransferSendBean("APP",current_user,current_sale_delivery_vbillcodeRecv,"","",current_sale_delivery_orgRecv, billmaker,bodylist);
        Gson gson = new Gson();
        String userSendBean = gson.toJson(otherOutgoingSend);

        request.addProperty("string", workcode);
        request.addProperty("string1", userSendBean);
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
        // 获取返回的结果
        // saleDeliveryUploadDataResp = object.getProperty(0).toString();
        return saleDeliveryUploadDataResp;
    }

    @Override
    protected void onResume() {
        super.onResume();
        saleDeliveryScanButton.setEnabled(false);
        uploadSingleButton.setEnabled(false);
        listAllBodyPostition = QueryAllocateTransferBody(current_sale_delivery_vbillcodeRecv);
        final AllocateTransferBodyTableAdapter adapterNew = new AllocateTransferBodyTableAdapter(AllocateTransferDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapterNew);
        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapterNew.select(position);
                saleDeliveryScanButton.setEnabled(true);
                uploadSingleButton.setEnabled(true);
                AllocateTransferBodyBean local_saleDeliveryBodyBean = (AllocateTransferBodyBean) adapterNew.getItem(position);
                chosen_line_itempk = local_saleDeliveryBodyBean.getItempk();
                chosen_line_address = local_saleDeliveryBodyBean.getAddress();
                chosen_line_materialcode = local_saleDeliveryBodyBean.getMaterialcode();
                chosen_line_materialclasscode = local_saleDeliveryBodyBean.getMaterialclasscode();
                chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
                chosen_line_cwarehousecode = local_saleDeliveryBodyBean.getCwarehousecode();
                chosen_line_rwarehousecode = local_saleDeliveryBodyBean.getRwarehousecode();
                chosen_line_scannum = local_saleDeliveryBodyBean.getScannum();
                chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();
            }
        });
    }

    public ArrayList<AllocateTransferBodyBean> QueryAllocateTransferBody(String current_sale_delivery_vbillcodeRecv) {
        ArrayList<AllocateTransferBodyBean> list = new ArrayList<AllocateTransferBodyBean>();
        Cursor cursor = db4.rawQuery("select itempk,address,materialcode,materialclasscode,nnum,rwarehousecode,cwarehousecode,scannum,uploadflag from AllocateTransferBody where billno=?", new String[]{current_sale_delivery_vbillcodeRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                AllocateTransferBodyBean bean = new AllocateTransferBodyBean();
                bean.itempk = cursor.getString(cursor.getColumnIndex("itempk"));
                bean.address = cursor.getString(cursor.getColumnIndex("address"));
                bean.materialcode = cursor.getString(cursor.getColumnIndex("materialcode"));
                bean.materialclasscode = cursor.getString(cursor.getColumnIndex("materialclasscode"));
                bean.nnum = cursor.getString(cursor.getColumnIndex("nnum"));
                bean.rwarehousecode = cursor.getString(cursor.getColumnIndex("rwarehousecode"));
                bean.cwarehousecode = cursor.getString(cursor.getColumnIndex("cwarehousecode"));
                bean.scannum = cursor.getString(cursor.getColumnIndex("scannum"));
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

    public String QueryMaccodeFromDB(String vbillcode, String itempk, String matrcode) {
        String maccode = "error";
        Cursor cursor = db4.rawQuery("select materialcode from AllocateTransferBody where billno=? and itempk=? and materialcode=? ", new String[]{vbillcode, itempk, matrcode});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                return cursor.getString(cursor.getColumnIndex("materialcode"));
            }
            cursor.close();
        }
        return maccode;
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private AllocateTransferBodyTableAdapter.MyClickListener2 mListener = new AllocateTransferBodyTableAdapter.MyClickListener2() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent(AllocateTransferDetail.this, AllocateTransferQRDetail.class);
            intent.putExtra("current_itempk_qrRecv", chosen_line_itempk);
            intent.putExtra("current_address_qrRecv", chosen_line_address);
            intent.putExtra("current_materialcode_qrRecv", chosen_line_materialcode);
            intent.putExtra("current_materialclasscode_qrRecv", chosen_line_materialclasscode);
            intent.putExtra("current_nnum_qrRecv", chosen_line_nnum);
            intent.putExtra("current_cwarehousecode_qrRecv", chosen_line_cwarehousecode);
            intent.putExtra("current_rwarehousecode_qrRecv", chosen_line_rwarehousecode);
            intent.putExtra("current_scannum_qrRecv", chosen_line_scannum);
            intent.putExtra("current_uploadflag_qrRecv", chosen_line_uploadflag);
            intent.putExtra("current_vbillcode_qrRecv", current_sale_delivery_vbillcodeRecv);
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
