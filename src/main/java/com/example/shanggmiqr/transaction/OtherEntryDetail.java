package com.example.shanggmiqr.transaction;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.bean.OtherBodyBean;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.OtherEntryBodyTableAdapter;
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

public class OtherEntryDetail extends AppCompatActivity {
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
    private List<OtherBodyBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button otherEntryScanButton;
    private Button uploadallOtherentryButton;
    private String chosen_line_maccode;
    private String chosen_line_materialcode;
    private String chosen_line_vcooporderbcode_b;
    private int chosen_line_nnum;
    private String chosen_line_uploadnum;
    private String otherEntryUploadDataResp;
    private Handler otherEntryDetailHandler = null;
    private ZLoadingDialog dialog;
    private String scannum="0";
    String workcode;
    String title="";
    OtherEntryBodyTableAdapter adapter;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_entry_detail);
        type=getIntent().getIntExtra("type",-1);


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        otherEntryScanButton = (Button) findViewById(R.id.scan_other_entry);
        uploadallOtherentryButton = (Button) findViewById(R.id.uploadall_other_entry);
        if(!getIntent().getStringExtra("flag").equals("N")){
            otherEntryScanButton.setEnabled(false);
            uploadallOtherentryButton.setEnabled(false);
        }

        dialog = new ZLoadingDialog(OtherEntryDetail.this);
        helper4 = new MyDataBaseHelper(OtherEntryDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db4 = helper4.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_otherentry);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_pobillcodeRecv = _intent.getStringExtra("current_pobillcode");
            current_cwarenameRecv = _intent.getStringExtra("current_cwarename");
            current_cwarecodeRecv = _intent.getStringExtra("current_cwarecode");
            current_dbilldateRecv = _intent.getStringExtra("current_dbilldate");
        }
        poBillCodeText = (TextView) findViewById(R.id.pobill_otherentry);
        cwareNameText = (TextView) findViewById(R.id.cwarename_otherentry);
        dbilldateText = (TextView) findViewById(R.id.dbilldate_otherentry);
        switch (type){
            case 1:
                workcode="R10";
                title="其他入库明细";
                poBillCodeText.setText("入库单号:" + current_pobillcodeRecv);
                cwareNameText.setText("入库仓库:" + current_cwarenameRecv);
                break;
            case 2:
                workcode="R12";
                title="其他出库明细";
                poBillCodeText.setText("出库单号:" + current_pobillcodeRecv);
                cwareNameText.setText("出库仓库:" + current_cwarenameRecv);
                break;
        }
        actionBar.setTitle(title);

        dbilldateText.setText("单据日期:" + current_dbilldateRecv);


        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                select(position);




                //  Toast.makeText(OtherOutgoingDetail.this,chosen_line_maccode,Toast.LENGTH_LONG).show();
            }
        });
        otherEntryScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isAlreadyUpload()) {

                    Intent intent = new Intent(OtherEntryDetail.this,SaleDeliveryQrScanner.class);
                    intent.putExtra("type",type);
                    intent.putExtra("current_vbillcode_qrRecv", current_pobillcodeRecv);
                    intent.putExtra("current_cwarename_scanner", current_cwarenameRecv);
                    intent.putExtra("current_maccode_qrRecv", chosen_line_maccode);
                    intent.putExtra("current_matrcode_qrRecv", chosen_line_materialcode);
                    intent.putExtra("current_nnum_qrRecv", String.valueOf(chosen_line_uploadnum));
                    intent.putExtra("chosen_line_uploadnum_scanner", chosen_line_uploadnum);
                    intent.putExtra("current_vcooporderbcode_b_qrRecv", chosen_line_vcooporderbcode_b);
                    startActivity(intent);
                } else {
                    Toast.makeText(OtherEntryDetail.this, "此订单已经提交过，不允许再次操作", Toast.LENGTH_LONG).show();
                }
            }
        });
        uploadallOtherentryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.isNetworkConnected(OtherEntryDetail.this)) {
                            try {
                                if (!isAlreadyUpload()) {
                                    String uploadResp = uploadOutgingPobill(workcode);
                                    if (!(null == uploadResp)) {
                                        Gson gson = new Gson();
                                        Bundle bundle = new Bundle();
                                        OtherOutgoingRespBean respBean = gson.fromJson(uploadResp, OtherOutgoingRespBean.class);

                                        if (respBean.getIssuccess().equals("N")) {
                                            bundle.putString("uploadResp", respBean.getMsg());
                                            Message msg = new Message();
                                            msg.what = 0x23;
                                            msg.setData(bundle);
                                            otherEntryDetailHandler.sendMessage(msg);
                                        } else if (respBean.getIssuccess().equals("Y")) {
                                            updateAllUploadStatus();
                                            bundle.putString("uploadResp", respBean.getMsg());
                                            Message msg = new Message();
                                            msg.what = 0x18;
                                            msg.setData(bundle);
                                            otherEntryDetailHandler.sendMessage(msg);
                                        }
                                    }else{
                                        Message msg = new Message();
                                        msg.what = 0x20;
                                        otherEntryDetailHandler.sendMessage(msg);
                                    }
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(OtherEntryDetail.this, "此订单已经提交过，不允许再次操作", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                //e.printStackTrace();
                                Bundle bundle = new Bundle();
                                bundle.putString("Exception333", e.toString());
                                Message msg = new Message();
                                msg.what = 0x19;
                                msg.setData(bundle);
                                otherEntryDetailHandler.sendMessage(msg);
                                return;
                            } catch (XmlPullParserException e) {
                               // e.printStackTrace();
                                Bundle bundle = new Bundle();
                                bundle.putString("Exception333", e.toString());
                                Message msg = new Message();
                                msg.what = 0x19;
                                msg.setData(bundle);
                                otherEntryDetailHandler.sendMessage(msg);
                                return;
                            }
                        }
                    }
                }).start();
            }
        });
        otherEntryDetailHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(OtherEntryDetail.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x11:
                        break;
                    case 0x18:
                        dialog.dismiss();
                        String s = msg.getData().getString("uploadResp");
                        Toast.makeText(OtherEntryDetail.this, s, Toast.LENGTH_LONG).show();
                        listAllBodyPostition = QueryOtherEntryBody(current_pobillcodeRecv);
                        adapter = new OtherEntryBodyTableAdapter(OtherEntryDetail.this, listAllBodyPostition, mListener);
                        tableBodyListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        checkAllupdate();
                            finish();



                        break;
                    case 0x19:
                        dialog.dismiss();
                        String exception333 = msg.getData().getString("Exception333");
                        Toast.makeText(OtherEntryDetail.this, exception333, Toast.LENGTH_LONG).show();
                        break;
                    case 0x20:
                        dialog.dismiss();

                        break;
                    case 0x21:
                        Toast.makeText(OtherEntryDetail.this, "请扫描后再进行提交", Toast.LENGTH_LONG).show();
                        break;
                    case 0x22:
                        dialog.dismiss();
                        Toast.makeText(OtherEntryDetail.this, "后台接口返回异常", Toast.LENGTH_LONG).show();
                        break;
                    case 0x23:
                        dialog.dismiss();
                        String ss = msg.getData().getString("uploadResp");
                        Toast.makeText(OtherEntryDetail.this, ss, Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private boolean checkAllupdate() {

        Boolean isY=false;
        Boolean isPY=false;
        String flag="";
        for (int i = 0; i <listAllBodyPostition.size() ; i++) {
            if(listAllBodyPostition.get(i).getUploadflag().equals("Y")){
                isY=true;
            }else if(listAllBodyPostition.get(i).getUploadflag().equals("PY")){
                isPY=true;
            }
        }

        if(isY && isPY==false){
            flag="Y";
            otherEntryScanButton.setEnabled(false);
            uploadallOtherentryButton.setEnabled(false);
        }else if(isPY==false && isY==false){
            flag="N";
        }else {
            flag="PY";
            otherEntryScanButton.setEnabled(false);
            uploadallOtherentryButton.setEnabled(false);
        }




            switch (type) {
                case 1:
                    db4.execSQL("update  OtherEntry set flag=? where pobillcode=?", new String[]{flag, current_pobillcodeRecv});
                    break;
                case 2:
                    db4.execSQL("update  OtherOutgoing set flag=? where pobillcode=?", new String[]{flag, current_pobillcodeRecv});
                    break;
            }

        if(flag.equals("Y")){
            return true;
        }else {
            return  false;
        }
    }

    private void select(int position) {
        adapter.select(position);
        OtherBodyBean otherOutgoingBodyBean = (OtherBodyBean) adapter.getItem(position);
        chosen_line_maccode = otherOutgoingBodyBean.getMaccode();
        chosen_line_materialcode = otherOutgoingBodyBean.getMaterialcode();
        chosen_line_nnum = otherOutgoingBodyBean.getNnum();
        chosen_line_uploadnum = otherOutgoingBodyBean.getUploadnum();
        chosen_line_vcooporderbcode_b = otherOutgoingBodyBean.getVcooporderbcode_b();
        scannum=otherOutgoingBodyBean.getScannum();
    }

    public boolean isAlreadyUpload() {
        Cursor cursor=null;
        switch (type){
            case 1:
                cursor = db4.rawQuery("select * from OtherEntryBody where pobillcode=? and uploadflag=?",
                        new String[]{current_pobillcodeRecv, "N"});
                break;
            case 2:
                cursor = db4.rawQuery("select * from OtherOutgoingBody where pobillcode=? and uploadflag=?",
                        new String[]{current_pobillcodeRecv, "N"});
                break;
        }

        if (cursor != null && cursor.getCount() > 0) {
            return false;
        }
        return true;
    }

    private void updateAllUploadStatus() {
        for (int i = 0; i <listAllBodyPostition.size() ; i++) {

            if(!listAllBodyPostition.get(i).getScannum().equals("0")){
                ContentValues contentValues=new ContentValues();
                contentValues.put("nnum",listAllBodyPostition.get(i).getScannum());
                if(Math.abs(Integer.parseInt(listAllBodyPostition.get(i).getUploadnum()))==Integer.parseInt(listAllBodyPostition.get(i).getScannum())) {

                    contentValues.put("uploadflag","Y");


                }else {
                    contentValues.put("uploadflag","PY");
                }
                switch (type){
                    case 1:
                        db4.update("OtherEntryBody",contentValues,"pobillcode=? and vcooporderbcode_b=?",
                                new String[]{current_pobillcodeRecv, listAllBodyPostition.get(i).getVcooporderbcode_b()});
                        break;
                    case 2:

                        db4.update("OtherOutgoingBody",contentValues,"pobillcode=? and vcooporderbcode_b=?",
                                new String[]{current_pobillcodeRecv, listAllBodyPostition.get(i).getVcooporderbcode_b()});
                        break;
                }

                db4.execSQL("update SaleDeliveryScanResult set itemuploadflag=? where vbillcode=? and vcooporderbcode_b=?",
                        new String[]{"Y", current_pobillcodeRecv, listAllBodyPostition.get(i).getVcooporderbcode_b()});

            }
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        listAllBodyPostition = QueryOtherEntryBody(current_pobillcodeRecv);
        adapter = new OtherEntryBodyTableAdapter(OtherEntryDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        select(0);
    }

    public ArrayList<OtherBodyBean> QueryOtherEntryBody(String current_pobillcodeRecv) {
        ArrayList<OtherBodyBean> list = new ArrayList<OtherBodyBean>();
        Cursor cursor=null;
        switch (type){
            case 1:
                cursor = db4.rawQuery("select vcooporderbcode_b,materialcode,maccode,nnum,uploadnum,scannum,uploadflag from OtherEntryBody where pobillcode=?",
                        new String[]{current_pobillcodeRecv});
                break;
            case 2:
              cursor = db4.rawQuery("select vcooporderbcode_b,materialcode,maccode,nnum,uploadnum,scannum,uploadflag from OtherOutgoingBody where pobillcode=?",
                        new String[]{current_pobillcodeRecv});
                break;
        }

        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                OtherBodyBean bean = new OtherBodyBean();
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


    private String uploadOutgingPobill(String workcode) throws IOException, XmlPullParserException {
        String WSDL_URI;
        String namespace;

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
        Cursor cursor2=null;
        switch (type){
            case 1:
                cursor2 = db4.rawQuery("select materialcode,nnum,pch,vcooporderbcode_b,scannum,uploadflag from OtherEntryBody where pobillcode=?",
                        new String[]{current_pobillcodeRecv});
                break;
            case 2:
                cursor2 = db4.rawQuery("select materialcode,nnum,pch,vcooporderbcode_b,scannum,uploadflag from OtherOutgoingBody where pobillcode=?",
                        new String[]{current_pobillcodeRecv});
                break;
        }

        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor2.moveToNext()) {
                OtherOutgoingSendBean.BodyBean bean = new OtherOutgoingSendBean.BodyBean();
                bean.materialcode = cursor2.getString(cursor2.getColumnIndex("materialcode"));
                //要求上传的是扫描的数量，虽然名字是nnum
                bean.nnum = cursor2.getString(cursor2.getColumnIndex("scannum"));
                bean.setScannum(cursor2.getString(cursor2.getColumnIndex("scannum")));
                bean.pch = cursor2.getString(cursor2.getColumnIndex("pch"));
                bean.vcooporderbcode_b = cursor2.getString(cursor2.getColumnIndex("vcooporderbcode_b"));
                bean.setItempk( cursor2.getString(cursor2.getColumnIndex("vcooporderbcode_b")));
                bean.setUploadflag(cursor2.getString(cursor2.getColumnIndex("uploadflag")));
                ArrayList<OtherOutgoingSendBean.BodyBean.SnBean> snlist = new ArrayList<OtherOutgoingSendBean.BodyBean.SnBean>();
                Cursor cursor3=null;

                cursor3 = db4.rawQuery("select platecode,boxcode,prodcutcode,xlh from SaleDeliveryScanResult where vbillcode=? and vcooporderbcode_b=? and matrcode=?",
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

                    }
                    cursor3.close();
                }
                bean.sn = snlist;
                bodylist.add(bean);
            }
            cursor2.close();
        }


        OtherOutgoingSendBean otherOutgoingSend = new OtherOutgoingSendBean("APP", current_pobillcodeRecv, current_cwarecodeRecv, bodylist);
        for (int i = 0; i <otherOutgoingSend.getBody().size() ; i++) {
            if(otherOutgoingSend.getBody().get(i).getScannum().equals("0")){
                otherOutgoingSend.getBody().remove(i);
                i--;
            }else if(otherOutgoingSend.getBody().get(i).getUploadflag().equals("Y")){
                otherOutgoingSend.getBody().remove(i);
                i--;
            }

        }
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

        HttpTransportSE se = new HttpTransportSE(WSDL_URI, 300000);
            //  se.call(null, envelope);//调用 version1.2
            //version1.1 需要如下soapaction
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                            .setLoadingColor(Color.BLUE)//颜色
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
            Log.i("response-->",envelope.bodyIn.toString());
            if (object != null && sizeValue > 0) {

                otherEntryUploadDataResp = object.getProperty(0).toString();
            } else {
                Message msg = new Message();
                msg.what = 0x22;
                otherEntryDetailHandler.sendMessage(msg);
            }



        return otherEntryUploadDataResp;
    }

    /**
     * 实现类，响应按钮点击事件
     */
    private OtherEntryBodyTableAdapter.MyClickListener2 mListener = new OtherEntryBodyTableAdapter.MyClickListener2() {
        @Override
        public void myOnClick(int position, View v) {
            Intent intent = new Intent(OtherEntryDetail.this, OtherEntryQRDetail.class);
            intent.putExtra("type",type);
            intent.putExtra("current_pobillcode_qr", current_pobillcodeRecv);
            intent.putExtra("current_cwarename_qr", current_cwarenameRecv);
            intent.putExtra("current_materialcode_qr", listAllBodyPostition.get(position).getMaterialcode());
            intent.putExtra("current_maccode_qr", listAllBodyPostition.get(position).getMaccode());
            intent.putExtra("current_nnum_qr", String.valueOf(listAllBodyPostition.get(position).getNnum()));
            intent.putExtra("current_uploadnum_qr", listAllBodyPostition.get(position).getUploadnum());
            intent.putExtra("current_vcooporderbcode_b_qr", listAllBodyPostition.get(position).getVcooporderbcode_b());
            intent.putExtra("flag",getIntent().getStringExtra("flag"));
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
