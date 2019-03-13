package com.example.shanggmiqr.transaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.OtherOutBodyTableAdapter;
import com.example.shanggmiqr.bean.OtherOutgoingBodyBean;
import com.example.shanggmiqr.bean.OtherOutgoingRespBean;
import com.example.shanggmiqr.bean.OtherOutgoingSendBean;
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

/**
 * Created by weiyt.jiang on 2018/8/14.
 */

public class OtherOutgoingDetail extends AppCompatActivity {
    //其他出库明细
    private TextView poBillCodeText;
    private TextView cwareNameText;
    private TextView dbilldateText;
    private String current_pobillcodeRecv;
    private String current_cwarenameRecv;
    private String current_cwarecodeRecv;
    private String current_dbilldateRecv;
    private SQLiteDatabase db4;
    private MyDataBaseHelper helper4;
    private List<OtherOutgoingBodyBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button otherOutgoingScanButton;
    private Button uploadallOthergoingButton;
    private String chosen_line_maccode;
    private String chosen_line_materialcode;
    private String chosen_line_vcooporderbcode_b;
    private int chosen_line_nnum;
    private String chosen_line_uploadnum;
    private String otherOutgoingUploadDataResp;
    private Handler otherOutgoingDetailHandler = null;
    private ZLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_outgoing_detail);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        otherOutgoingScanButton = (Button) findViewById(R.id.scan_other_outgoing);
        uploadallOthergoingButton = (Button) findViewById(R.id.uploadall_other_outgoing);
        otherOutgoingScanButton.setEnabled(false);
        dialog = new ZLoadingDialog(OtherOutgoingDetail.this);
        helper4 = new MyDataBaseHelper(OtherOutgoingDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db4 = helper4.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_pobillcodeRecv = _intent.getStringExtra("current_pobillcode");
            current_cwarenameRecv = _intent.getStringExtra("current_cwarename");
            current_cwarecodeRecv = _intent.getStringExtra("current_cwarecode");
            current_dbilldateRecv = _intent.getStringExtra("current_dbilldate");
        }
        poBillCodeText = (TextView) findViewById(R.id.pobill_otheroutgoing);
        cwareNameText = (TextView) findViewById(R.id.cwarename_otheroutgoing);
        dbilldateText = (TextView) findViewById(R.id.dbilldate_otheroutgoing);
        poBillCodeText.setText("出库单号:" + current_pobillcodeRecv);
        cwareNameText.setText("出库仓库:" + current_cwarenameRecv);
        dbilldateText.setText("单据日期:" + current_dbilldateRecv);
        listAllBodyPostition = QueryOtherOutgoingBody(current_pobillcodeRecv);
        final OtherOutBodyTableAdapter adapter = new OtherOutBodyTableAdapter(OtherOutgoingDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.select(position);
                otherOutgoingScanButton.setEnabled(true);
                OtherOutgoingBodyBean otherOutgoingBodyBean = (OtherOutgoingBodyBean) adapter.getItem(position);
                chosen_line_maccode = otherOutgoingBodyBean.getMaccode();
                chosen_line_materialcode = otherOutgoingBodyBean.getMaterialcode();
                chosen_line_nnum = otherOutgoingBodyBean.getNnum();
                chosen_line_uploadnum = otherOutgoingBodyBean.getUploadnum();
                chosen_line_vcooporderbcode_b = otherOutgoingBodyBean.getVcooporderbcode_b();
                //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
            }
        });
        otherOutgoingScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAlreadyUpload()) {
                    Intent intent = new Intent(OtherOutgoingDetail.this, OtherOutgoingQrScanner.class);
                    intent.putExtra("current_pobillcode_scanner", current_pobillcodeRecv);
                    intent.putExtra("current_cwarename_scanner", current_cwarenameRecv);
                    intent.putExtra("chosen_line_maccode_scanner", chosen_line_maccode);
                    intent.putExtra("chosen_line_materialcode_scanner", chosen_line_materialcode);
                    intent.putExtra("chosen_line_nnum_scanner", String.valueOf(chosen_line_nnum));
                    intent.putExtra("chosen_line_uploadnum_scanner", chosen_line_uploadnum);
                    intent.putExtra("chosen_line_vcooporderbcode_b_scanner", chosen_line_vcooporderbcode_b);
                    startActivity(intent);
                } else {
                    Toast.makeText(OtherOutgoingDetail.this, "此订单已经提交过，不允许再次操作", Toast.LENGTH_LONG).show();
                }
            }
        });
        uploadallOthergoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.isNetworkConnected(OtherOutgoingDetail.this)) {
                            try {
                                if (!isAlreadyUpload()) {
                                    String uploadResp = uploadOutgingPobill("R12");
                                    if (!(null == uploadResp)) {
                                        Gson gson = new Gson();
                                        Bundle bundle = new Bundle();
                                        OtherOutgoingRespBean respBean = gson.fromJson(uploadResp, OtherOutgoingRespBean.class);

                                        if (respBean.getIssuccess().equals("N")) {
                                            bundle.putString("uploadResp", respBean.getMsg());
                                            Message msg = new Message();
                                            msg.what = 0x23;
                                            msg.setData(bundle);
                                            otherOutgoingDetailHandler.sendMessage(msg);
                                        } else if (respBean.getIssuccess().equals("Y")) {
                                            updateAllUploadStatus(current_pobillcodeRecv);
                                            bundle.putString("uploadResp", respBean.getMsg());
                                            Message msg = new Message();
                                            msg.what = 0x18;
                                            msg.setData(bundle);
                                            otherOutgoingDetailHandler.sendMessage(msg);
                                        }
                                    }else{
                                        Message msg = new Message();
                                        msg.what = 0x20;
                                        otherOutgoingDetailHandler.sendMessage(msg);
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(OtherOutgoingDetail.this, "此订单已经提交过，不允许再次操作", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (IOException e) {
                               // e.printStackTrace();
                                Bundle bundle = new Bundle();
                                bundle.putString("Exception222", e.toString());
                                Message msg = new Message();
                                msg.what = 0x19;
                                msg.setData(bundle);
                                otherOutgoingDetailHandler.sendMessage(msg);
                                return;
                            } catch (XmlPullParserException e) {
                               // e.printStackTrace();
                                Bundle bundle = new Bundle();
                                bundle.putString("Exception222", e.toString());
                                Message msg = new Message();
                                msg.what = 0x19;
                                msg.setData(bundle);
                                otherOutgoingDetailHandler.sendMessage(msg);
                                return;
                            }
                        }
                    }
                }).start();
            }
        });
        otherOutgoingDetailHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(OtherOutgoingDetail.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        break;
                    case 0x18:
                        dialog.dismiss();
                        String s = msg.getData().getString("uploadResp");
                       // Toast.makeText(OtherOutgoingDetail.this, s, Toast.LENGTH_LONG).show();
                        listAllBodyPostition = QueryOtherOutgoingBody(current_pobillcodeRecv);
                        final OtherOutBodyTableAdapter adapter = new OtherOutBodyTableAdapter(OtherOutgoingDetail.this, listAllBodyPostition, mListener);
                        tableBodyListView.setAdapter(adapter);
                        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                adapter.select(position);
                                otherOutgoingScanButton.setEnabled(true);
                                OtherOutgoingBodyBean otherOutgoingBodyBean = (OtherOutgoingBodyBean) adapter.getItem(position);
                                chosen_line_maccode = otherOutgoingBodyBean.getMaccode();
                                chosen_line_materialcode = otherOutgoingBodyBean.getMaterialcode();
                                chosen_line_nnum = otherOutgoingBodyBean.getNnum();
                                chosen_line_uploadnum = otherOutgoingBodyBean.getUploadnum();
                                chosen_line_vcooporderbcode_b = otherOutgoingBodyBean.getVcooporderbcode_b();
                                //Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
                            }
                        });
//                        final AlertDialog alertDialog2 = new AlertDialog.Builder(OtherOutgoingDetail.this)
//                                .setMessage("   "+s)
//                                .create();
//                        alertDialog2.show();
//                        WindowManager m = getWindowManager();
//                        Display d = m.getDefaultDisplay();//为获取屏幕宽、高     
//                        android.view.WindowManager.LayoutParams p = alertDialog2.getWindow().getAttributes();//获取对话框当前的参数值     
//                        p.width = (int) (d.getWidth() * 0.4); //宽度设置为屏幕的0.5 
//                        p.alpha = 0.5f;//   
//                        alertDialog2.getWindow().setAttributes(p);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
                              //  alertDialog2.dismiss();
                                Intent intent = new Intent(OtherOutgoingDetail.this, OtherOutgoing.class);
                        startActivity(intent);
                        finish();
       //                     }
          //              }, 5000);
                        break;
                    case 0x19:
                        dialog.dismiss();
                        String exception222 = msg.getData().getString("Exception222");
                        Toast.makeText(OtherOutgoingDetail.this, exception222, Toast.LENGTH_LONG).show();
                        break;
                    case 0x20:
                        dialog.dismiss();
                        Toast.makeText(OtherOutgoingDetail.this, "接口异常", Toast.LENGTH_LONG).show();
                        break;
                    case 0x21:
                        Toast.makeText(OtherOutgoingDetail.this, "请扫描后再进行提交", Toast.LENGTH_LONG).show();
                        break;
                    case 0x22:
                        dialog.dismiss();
                        Toast.makeText(OtherOutgoingDetail.this, "后台接口返回异常", Toast.LENGTH_LONG).show();
                        break;
                    case 0x23:
                        dialog.dismiss();
                        String ss = msg.getData().getString("uploadResp");
                        Toast.makeText(OtherOutgoingDetail.this, ss, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public boolean isAlreadyUpload() {
        Cursor cursor3 = db4.rawQuery("select * from OtherOutgoingBody where pobillcode=? and uploadflag=?",
                new String[]{current_pobillcodeRecv, "N"});
        // Cursor cursor3 = db3.rawQuery(sql3, null);
        if (cursor3 != null && cursor3.getCount() > 0) {
            return false;
        }
        return true;
    }

    private void updateAllUploadStatus(String current_pobillcodeRecv) {
        //单个item扫描结果全部置为Y
        db4.execSQL("update OtherOutgoingScanResult set itemuploadflag=? where pobillcode=?", new String[]{"Y", current_pobillcodeRecv});
        //所有行号发送状态全部置为Y
        db4.execSQL("update OtherOutgoingBody set uploadflag=? where pobillcode=?", new String[]{"Y", current_pobillcodeRecv});
        //其他出入库只运行成功发送一次，故更新flag为Y
        db4.execSQL("update OtherOutgoing set flag=? where pobillcode=?", new String[]{"Y", current_pobillcodeRecv});
        Cursor cursor = db4.rawQuery("select vcooporderbcode_b,scannum from OtherOutgoingBody where uploadflag=? and pobillcode=?", new String[]{"Y", current_pobillcodeRecv});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String tempnum = cursor.getString(cursor.getColumnIndex("scannum"));
                String vcooporderbcode_b = cursor.getString(cursor.getColumnIndex("vcooporderbcode_b"));
                db4.execSQL("update OtherOutgoingBody set uploadnum=? where pobillcode=? and vcooporderbcode_b=?", new String[]{tempnum, current_pobillcodeRecv, vcooporderbcode_b});
            }
            cursor.close();
        }
    }


    public ArrayList<OtherOutgoingBodyBean> QueryOtherOutgoingBody(String current_pobillcodeRecv) {
        ArrayList<OtherOutgoingBodyBean> list = new ArrayList<OtherOutgoingBodyBean>();
        Cursor cursor = db4.rawQuery("select vcooporderbcode_b,materialcode,maccode,nnum,uploadnum,scannum,uploadflag from OtherOutgoingBody where pobillcode=?", new String[]{current_pobillcodeRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherOutgoingBodyBean bean = new OtherOutgoingBodyBean();
                bean.vcooporderbcode_b = cursor.getString(cursor.getColumnIndex("vcooporderbcode_b"));
                bean.materialcode = cursor.getString(cursor.getColumnIndex("materialcode"));
                bean.maccode = cursor.getString(cursor.getColumnIndex("maccode"));
                bean.nnum = cursor.getInt(cursor.getColumnIndex("nnum"));
                bean.uploadnum = cursor.getString(cursor.getColumnIndex("uploadnum"));
                bean.scannum = cursor.getString(cursor.getColumnIndex("scannum"));
                bean.uploadflag = cursor.getString(cursor.getColumnIndex("uploadflag"));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    @Override
    protected void onResume() {
        super.onResume();
        otherOutgoingScanButton.setEnabled(false);
        listAllBodyPostition = QueryOtherOutgoingBody(current_pobillcodeRecv);
        final OtherOutBodyTableAdapter adapter = new OtherOutBodyTableAdapter(OtherOutgoingDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.select(position);
                otherOutgoingScanButton.setEnabled(true);
                OtherOutgoingBodyBean otherOutgoingBodyBean = (OtherOutgoingBodyBean) adapter.getItem(position);
                chosen_line_maccode = otherOutgoingBodyBean.getMaccode();
                chosen_line_materialcode = otherOutgoingBodyBean.getMaterialcode();
                chosen_line_nnum = otherOutgoingBodyBean.getNnum();
                chosen_line_uploadnum = otherOutgoingBodyBean.getUploadnum();
                chosen_line_vcooporderbcode_b = otherOutgoingBodyBean.getVcooporderbcode_b();
                //Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
            }
        });
    }

    private String uploadOutgingPobill(String workcode) throws IOException, XmlPullParserException {
        String WSDL_URI;
        String namespace;
        int temp_count = 0;
        String WSDL_URI_current = BaseConfig.getNcUrl();//wsdl 的uri
        String namespace_current = "http://schemas.xmlsoap.org/soap/envelope/";//namespace
        String methodName = "sendToWISE";//要调用的方法名称
        SharedPreferences proxySp = getSharedPreferences("configInfo", 0);
        if (proxySp.getString("WSDL_URI", WSDL_URI_current).equals("") || proxySp.getString("namespace", namespace_current).equals("")) {
            WSDL_URI = WSDL_URI_current;
            namespace = namespace_current;
        } else {
            WSDL_URI = proxySp.getString("WSDL_URI", WSDL_URI_current);
            namespace = proxySp.getString("namespace", namespace_current);
        }
        SoapObject request = new SoapObject(namespace, methodName);
        // 设置需调用WebService接口需要传入的两个参数string、string1
        ArrayList<OtherOutgoingSendBean.BodyBean> bodylist = new ArrayList<OtherOutgoingSendBean.BodyBean>();
        Cursor cursor2 = db4.rawQuery("select materialcode,nnum,pch,vcooporderbcode_b,scannum from OtherOutgoingBody where pobillcode=?",
                new String[]{current_pobillcodeRecv});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor2.moveToNext()) {
                OtherOutgoingSendBean.BodyBean bean = new OtherOutgoingSendBean.BodyBean();
                bean.materialcode = cursor2.getString(cursor2.getColumnIndex("materialcode"));
                //要求上传的是扫描的数量，虽然名字是nnum
                bean.nnum = cursor2.getString(cursor2.getColumnIndex("scannum"));
                bean.pch = cursor2.getString(cursor2.getColumnIndex("pch"));
                bean.vcooporderbcode_b = cursor2.getString(cursor2.getColumnIndex("vcooporderbcode_b"));
                ArrayList<OtherOutgoingSendBean.BodyBean.SnBean> snlist = new ArrayList<OtherOutgoingSendBean.BodyBean.SnBean>();

                Cursor cursor3 = db4.rawQuery("select platecode,boxcode,prodcutcode,xlh from OtherOutgoingScanResult where pobillcode=? and vcooporderbcode_b=? and materialcode=?",
                        new String[]{current_pobillcodeRecv, bean.vcooporderbcode_b, bean.materialcode});

                if (cursor3 != null && cursor3.getCount() > 0) {
                    //判断cursor中是否存在数据
                    while (cursor3.moveToNext()) {
                        OtherOutgoingSendBean.BodyBean.SnBean snbean = new OtherOutgoingSendBean.BodyBean.SnBean();
                        snbean.tp = cursor3.getString(cursor3.getColumnIndex("platecode"));
                        snbean.xm = cursor3.getString(cursor3.getColumnIndex("boxcode"));
                        snbean.txm = cursor3.getString(cursor3.getColumnIndex("prodcutcode"));
                        snbean.xlh = cursor3.getString(cursor3.getColumnIndex("xlh"));
                        snlist.add(snbean);
                        temp_count++;
                    }
                    cursor3.close();
                }
                bean.sn = snlist;
                bodylist.add(bean);
            }
            cursor2.close();
        }

        OtherOutgoingSendBean otherOutgoingSend = new OtherOutgoingSendBean("APP", current_pobillcodeRecv, current_cwarecodeRecv, bodylist);
        Gson gson = new Gson();
        String userSendBean = gson.toJson(otherOutgoingSend);
        request.addProperty("string", workcode);
        request.addProperty("string1", userSendBean);
        //request.addProperty("string1", "{\"begintime\":\"1900-01-20 00:00:00\",\"endtime\":\"2018-08-21 00:00:00\", \"pagenum\":\"1\",\"pagetotal\":\"66\"}");
        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

        envelope.bodyOut = request;
        envelope.dotNet = false;
        if (temp_count != 0) {
            HttpTransportSE se = new HttpTransportSE(WSDL_URI,60000);
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
            SoapObject object = (SoapObject) envelope.bodyIn;
            int sizeValue = object.getPropertyCount();
            // 获取返回的结果
            if (object != null && sizeValue > 0) {
                otherOutgoingUploadDataResp = object.getProperty(0).toString();
            } else {
                Message msg = new Message();
                msg.what = 0x22;
                otherOutgoingDetailHandler.sendMessage(msg);
            }
        } else {
            Message msg = new Message();
            msg.what = 0x21;
            otherOutgoingDetailHandler.sendMessage(msg);
        }
        return otherOutgoingUploadDataResp;
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private OtherOutBodyTableAdapter.MyClickListener2 mListener = new OtherOutBodyTableAdapter.MyClickListener2() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent(OtherOutgoingDetail.this, OtherOutgoingQRDetail.class);
            intent.putExtra("current_pobillcode_qr", current_pobillcodeRecv);
            intent.putExtra("current_cwarename_qr", current_cwarenameRecv);
            intent.putExtra("current_materialcode_qr", listAllBodyPostition.get(position).getMaterialcode());
            intent.putExtra("current_maccode_qr", listAllBodyPostition.get(position).getMaccode());
            intent.putExtra("current_nnum_qr", String.valueOf(listAllBodyPostition.get(position).getNnum()));
            intent.putExtra("current_uploadnum_qr", listAllBodyPostition.get(position).getUploadnum());
            intent.putExtra("current_vcooporderbcode_b_qr", listAllBodyPostition.get(position).getVcooporderbcode_b());
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
