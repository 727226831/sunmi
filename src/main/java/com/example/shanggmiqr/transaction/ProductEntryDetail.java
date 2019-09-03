package com.example.shanggmiqr.transaction;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.example.shanggmiqr.adapter.ProductEntryBodyTableAdapter;
import com.example.shanggmiqr.bean.ProductEntryBodyBean;
import com.example.shanggmiqr.bean.ProductEntrySendBean;
import com.example.shanggmiqr.bean.ProductEntryUploadFlagBean;
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

public class ProductEntryDetail extends AppCompatActivity {
    //其他出库明细
    private TextView vbillcodText;
    private TextView dbilldateText;
    private String current_sale_delivery_vbillcodeRecv;
    private String current_sale_delivery_dbilldateRecv;
    private SQLiteDatabase db4;
    private MyDataBaseHelper helper4;
    private List<ProductEntryBodyBean> listAllBodyPostition;
    private ListView tableBodyListView;
    private Button saleDeliveryScanButton;
    private Button uploadAll_saleDeliveryButton;
    private Button uploadSingleButton;

    private String chosen_line_nnum;
    private String chosen_line_itempk;
    private String chosen_line_ysnum;
    private String chosen_line_materialcode;
    private String chosen_line_scannum;
    private String chosen_line_uploadflag;
    private Handler productEntryDetailHandler = null;

    //要上传行号的集合
    private List<String> list;
    //要上传的产品码的集合
    private List<String> listitem;

    //nnum为正 bisreturn为N 为负则为Y
    private String current_bisreturn = "N";
    private ZLoadingDialog dialog;
    private String maccode;
    ProductEntryBodyTableAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_entry_detail);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        saleDeliveryScanButton = (Button) findViewById(R.id.scan_productEntry);
        uploadAll_saleDeliveryButton = (Button) findViewById(R.id.uploadall_productEntry);
        if(!getIntent().getStringExtra("flag").equals("N")){
            saleDeliveryScanButton.setEnabled(false);
            uploadAll_saleDeliveryButton.setEnabled(false);
        }
        uploadSingleButton = (Button) findViewById(R.id.upload_productEntry);

        dialog = new ZLoadingDialog(ProductEntryDetail.this);
        helper4 = new MyDataBaseHelper(ProductEntryDetail.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错，因为里面有直接加的toast。。。
        db4 = helper4.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        tableBodyListView = (ListView) findViewById(R.id.list_body_product_entry_detail);
        Intent _intent = getIntent();
        //从Intent当中根据key取得value
        if (_intent != null) {
            current_sale_delivery_vbillcodeRecv = _intent.getStringExtra("current_sale_delivery_vbillcode");
            current_sale_delivery_dbilldateRecv = _intent.getStringExtra("current_sale_delivery_dbilldate");
        }
        vbillcodText = (TextView) findViewById(R.id.vbillcode_productEntry);
        dbilldateText = (TextView) findViewById(R.id.dbilldate_productEntry);

        vbillcodText.setText("入库单号:" + current_sale_delivery_vbillcodeRecv);
        dbilldateText.setText("入库日期:" + current_sale_delivery_dbilldateRecv);
        listAllBodyPostition = QueryProductEntryBody(current_sale_delivery_vbillcodeRecv);

        adapter = new ProductEntryBodyTableAdapter(ProductEntryDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
         select(0);
        tableBodyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                select(position);

            }
        });

        saleDeliveryScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductEntryDetail.this, ProductEntryQrScanner.class);
                intent.putExtra("current_itempk_qrRecv", chosen_line_itempk);
                intent.putExtra("current_nnum_qrRecv", chosen_line_nnum);
                intent.putExtra("current_scannum_qrRecv", chosen_line_scannum);
                intent.putExtra("current_materialcode_qrRecv", chosen_line_materialcode);
                intent.putExtra("current_ysnum_qrRecv", chosen_line_ysnum);
                intent.putExtra("current_uploadflag_qrRecv", chosen_line_uploadflag);
                intent.putExtra("current_vbillcode_qrRecv", current_sale_delivery_vbillcodeRecv);
                intent.putExtra("maccode",maccode);

                startActivity(intent);
            }
        });
        uploadAll_saleDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (Utils.isNetworkConnected(ProductEntryDetail.this)) {
                                try {

                                    //Y代表已经上传过
                                    if (iaAlreadyUploadAll()) {
                                        Message msg = new Message();
                                        msg.what = 0x12;
                                        productEntryDetailHandler.sendMessage(msg);
                                    } else if (isCwarenameSame()) {
                                        String uploadResp = uploadSaleDeliveryVBill("R36", list);
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

                                                    msg.what = 0x15;
                                                } else {
                                                    //19弹出erromsg
                                                    msg.what = 0x19;
                                                }
                                                msg.setData(bundle);
                                                productEntryDetailHandler.sendMessage(msg);

                                        } else {
                                            Message msg = new Message();
                                            msg.what = 0x18;
                                            productEntryDetailHandler.sendMessage(msg);
                                        }
                                    } else {
                                        Message msg = new Message();
                                        msg.what = 0x16;
                                        productEntryDetailHandler.sendMessage(msg);
                                    }

                                } catch (IOException e) {
                                    //e.printStackTrace();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Exception111", e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x17;
                                    msg.setData(bundle);
                                    productEntryDetailHandler.sendMessage(msg);
                                    return;
                                } catch (XmlPullParserException e) {
                                    //e.printStackTrace();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Exception111", e.toString());
                                    Message msg = new Message();
                                    msg.what = 0x17;
                                    msg.setData(bundle);
                                    productEntryDetailHandler.sendMessage(msg);
                                    return;
                                }
                            }
                        }
                    }).start();


            }
        });

        productEntryDetailHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x10:
                        Toast.makeText(ProductEntryDetail.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                        break;
                    case 0x12:
                        Toast.makeText(ProductEntryDetail.this, "该发货单已经全部上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x13:
                        Toast.makeText(ProductEntryDetail.this, "该行已经上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x14:
                        Toast.makeText(ProductEntryDetail.this, "请先扫码再进行发货上传操作", Toast.LENGTH_LONG).show();
                        break;
                    case 0x15:
                        dialog.dismiss();
                        String s2 = msg.getData().getString("uploadResp");
                        Toast.makeText(ProductEntryDetail.this, s2, Toast.LENGTH_LONG).show();
                        updateAllUploadFlag();
                        listAllBodyPostition = QueryProductEntryBody(current_sale_delivery_vbillcodeRecv);
                        adapter = new ProductEntryBodyTableAdapter(ProductEntryDetail.this, listAllBodyPostition, mListener);
                        tableBodyListView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        isAllItemUpload();
                            finish();

                        break;
                    case 0x16:
                        Toast.makeText(ProductEntryDetail.this, "不同仓库的行号不可以同时上传", Toast.LENGTH_LONG).show();
                        break;
                    case 0x17:
                        dialog.dismiss();
                        String exception111 = msg.getData().getString("Exception111");
                        Toast.makeText(ProductEntryDetail.this, exception111, Toast.LENGTH_LONG).show();
                        break;
                    case 0x18:
                        dialog.dismiss();

                        break;
                    case 0x19:
                        dialog.dismiss();
                        String s3 = msg.getData().getString("uploadResp");
                        Toast.makeText(ProductEntryDetail.this, s3, Toast.LENGTH_LONG).show();
                        break;
                    case 0x20:
                        dialog.dismiss();
                        Toast.makeText(ProductEntryDetail.this, "运单号没有填写，首先填写运单号", Toast.LENGTH_LONG).show();
                        break;
                    case 0x21:
                        dialog.dismiss();
                        Toast.makeText(ProductEntryDetail.this, "物流公司没有选择，首先选择物流公司", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void select(int position) {
        adapter.select(position);

        ProductEntryBodyBean local_saleDeliveryBodyBean = (ProductEntryBodyBean) adapter.getItem(position);
        maccode=local_saleDeliveryBodyBean.getMaccode();
        chosen_line_itempk = local_saleDeliveryBodyBean.getItempk();
        chosen_line_nnum = local_saleDeliveryBodyBean.getNnum();
        chosen_line_scannum = local_saleDeliveryBodyBean.getScannum();
        chosen_line_uploadflag = local_saleDeliveryBodyBean.getUploadflag();
        chosen_line_ysnum = local_saleDeliveryBodyBean.getYsnum();
        chosen_line_materialcode = local_saleDeliveryBodyBean.getMaterialcode();

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



    private boolean isAllItemUpload() {
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
            if(isN==false && isY){
                flag="Y";
            }else {
                flag="PY";
            }
        }else {
            flag="N";
        }


        db4.execSQL("update ProductEntry set flag=? where billcode=?",
                new String[]{flag, current_sale_delivery_vbillcodeRecv});
        if(flag.equals("Y")){
            return true;
        }else {
            return  false;
        }

    }

    private boolean isCwarenameSame() {
      return true;
    }





    private boolean iaAlreadyUploadAll() {
        Cursor cursor = db4.rawQuery("select billcode from ProductEntry where billcode=? and flag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "Y"});
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }

        Cursor cursor3 = db4.rawQuery("select billcode,itempk from ProductEntryBody where billcode=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "N"});
        Cursor cursorpy = db4.rawQuery("select billcode,itempk from ProductEntryBody where billcode=? and uploadflag=?",
                new String[]{current_sale_delivery_vbillcodeRecv, "PY"});
        list = new ArrayList<String>();
        if (cursor3 != null && cursor3.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor3.moveToNext()) {
                list.add(cursor3.getString(cursor3.getColumnIndex("itempk")));
                Cursor cursor4 = db4.rawQuery("select billcode,itempk,prodcutcode,itemuploadflag from ProductEntryScanResult where billcode=? and itempk=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursor3.getString(cursor3.getColumnIndex("itempk")), "N"});
                if (cursor4 != null && cursor4.getCount() > 0) {
                    //判断cursor中是否存在数据
                    while (cursor4.moveToNext()) {
                        ProductEntryUploadFlagBean itemall = new ProductEntryUploadFlagBean();
                        itemall.billcode = cursor4.getString(cursor4.getColumnIndex("billcode"));
                        itemall.itempk = cursor4.getString(cursor4.getColumnIndex("itempk"));
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
                list.add(cursorpy.getString(cursorpy.getColumnIndex("itempk")));
                Cursor cursor5 = db4.rawQuery("select billcode,itempk,prodcutcode,itemuploadflag from ProductEntryScanResult where billcode=? and itempk=? and itemuploadflag=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, cursorpy.getString(cursorpy.getColumnIndex("itempk")), "N"});
                if (cursor5 != null && cursor5.getCount() > 0) {
                    //判断cursor中是否存在数据
                    while (cursor5.moveToNext()) {
                        ProductEntryUploadFlagBean itemall2 = new ProductEntryUploadFlagBean();
                        itemall2.billcode = cursor5.getString(cursor5.getColumnIndex("billcode"));
                        itemall2.itempk = cursor5.getString(cursor5.getColumnIndex("itempk"));
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


        db4.execSQL("update ProductEntryScanResult set itemuploadflag=?  where billcode=? ",
                new String[]{"Y", current_sale_delivery_vbillcodeRecv});
        for (int i = 0; i <listAllBodyPostition.size() ; i++) {
            int nnum=Integer.parseInt(listAllBodyPostition.get(i).getNnum());
            int ysnum=Integer.parseInt(listAllBodyPostition.get(i).getYsnum());
            int scannum=Integer.parseInt(listAllBodyPostition.get(i).getScannum());

            if(scannum!=0 ){
                String uplodaflag="";
                if(scannum==(ysnum-nnum)){
                   uplodaflag="Y";
                }else {
                    uplodaflag="PY";
                }
                ContentValues contentValues=new ContentValues();
                contentValues.put("uploadflag",uplodaflag);
               contentValues.put("nnum", listAllBodyPostition.get(i).getScannum());
                db4.update("ProductEntryBody",contentValues,"billcode=? and itempk=?",
                        new String[]{current_sale_delivery_vbillcodeRecv, listAllBodyPostition.get(i).getItempk()});

            }
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
        ArrayList<ProductEntrySendBean.BodyBean> bodylist = new ArrayList<ProductEntrySendBean.BodyBean>();
        Cursor cursor2 = db4.rawQuery("select itempk,materialcode,ysnum,nnum,scannum,uploadflag from ProductEntryBody where billcode=? ", new String[]{current_sale_delivery_vbillcodeRecv});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor2.moveToNext()) {
                ProductEntrySendBean.BodyBean bean = new ProductEntrySendBean.BodyBean();
                bean.itempk = cursor2.getString(cursor2.getColumnIndex("itempk"));
                String num_check = cursor2.getString(cursor2.getColumnIndex("nnum"));
                bean.setUploadflag(cursor2.getString(cursor2.getColumnIndex("uploadflag")));
                bean.pch = "";
                int scanNum = 0;
                ArrayList<ProductEntrySendBean.BodyBean.SnBean> snlist = new ArrayList<ProductEntrySendBean.BodyBean.SnBean>();
                if (list.contains(bean.itempk)) {
                    Cursor cursor3 = db4.rawQuery("select platecode,boxcode,prodcutcode,xlh from ProductEntryScanResult where billcode=? and itempk=? and itemuploadflag=?",
                            new String[]{current_sale_delivery_vbillcodeRecv, bean.itempk, "N"});
                    if (cursor3 != null && cursor3.getCount() > 0) {
                        //判断cursor中是否存在数据
                        while (cursor3.moveToNext()) {
                            ProductEntrySendBean.BodyBean.SnBean snbean = new ProductEntrySendBean.BodyBean.SnBean();
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
                    bean.nnum = String.valueOf(scanNum);
                    bodylist.add(bean);
                }
            }
            cursor2.close();
        }
        if (bodylist.size()==0){
            Message msg = new Message();
            msg.what = 0x14;
            productEntryDetailHandler.sendMessage(msg);
            return null;
        }

        Cursor cursor=db4.rawQuery("select * from ProductEntry where billcode=?",
                new String[]{current_sale_delivery_vbillcodeRecv});
        ProductEntrySendBean otherOutgoingSend = new ProductEntrySendBean("", "",bodylist);
        while (cursor.moveToNext()){
            otherOutgoingSend.setHeadpk( cursor.getString(cursor.getColumnIndex("headpk")));
           otherOutgoingSend.setBillmaker(DataHelper.getUser(ProductEntryDetail.this));
        }
        for (int i = 0; i <otherOutgoingSend.getBody().size() ; i++) {
            if(otherOutgoingSend.getBody().get(i).getNnum().equals("0")){
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
        String saleDeliveryUploadDataResp = null;
        if (upload_num == 0) {
        } else {

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

        }

        HttpTransportSE se = new HttpTransportSE(WSDL_URI, 300000);
        se.call(namespace + "sendToWISE", envelope);
        // 获取返回的数据
        // SoapObject object = (SoapObject) envelope.bodyIn;
        Object object = envelope.getResponse();
        saleDeliveryUploadDataResp = new Gson().toJson(object);
        Log.i("response-->",saleDeliveryUploadDataResp);
        return saleDeliveryUploadDataResp;
    }



    @Override
    protected void onStart() {
        super.onStart();
        listAllBodyPostition = QueryProductEntryBody(current_sale_delivery_vbillcodeRecv);
        adapter = new ProductEntryBodyTableAdapter(ProductEntryDetail.this, listAllBodyPostition, mListener);
        tableBodyListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<ProductEntryBodyBean> QueryProductEntryBody(String current_sale_delivery_vbillcodeRecv) {
        ArrayList<ProductEntryBodyBean> list = new ArrayList<ProductEntryBodyBean>();
        Cursor cursor = db4.rawQuery("select itempk,materialcode,nnum,ysnum,scannum,uploadflag,maccode from ProductEntryBody where billcode=?", new String[]{current_sale_delivery_vbillcodeRecv});
        if (cursor != null && cursor.getCount() > 0) {
            //判断cursor中是否存在数据
            while (cursor.moveToNext()) {
                ProductEntryBodyBean bean = new ProductEntryBodyBean();
                bean.itempk = cursor.getString(cursor.getColumnIndex("itempk"));
                bean.materialcode = cursor.getString(cursor.getColumnIndex("materialcode"));
                bean.nnum = cursor.getString(cursor.getColumnIndex("nnum"));
                bean.ysnum = cursor.getString(cursor.getColumnIndex("ysnum"));
                bean.scannum = cursor.getString(cursor.getColumnIndex("scannum"));
                bean.uploadflag = cursor.getString(cursor.getColumnIndex("uploadflag"));

                bean.setMaccode(cursor.getString(cursor.getColumnIndex("maccode")));
                list.add(bean);
            }
            cursor.close();
        }

        return list;
    }


    /**
     * 实现类，响应按钮点击事件
     */
    private ProductEntryBodyTableAdapter.MyClickListener2 mListener = new ProductEntryBodyTableAdapter.MyClickListener2() {
        @Override
        public void myOnClick(int position, View v) {
            select(position);
            Intent intent = new Intent(ProductEntryDetail.this, ProductEntryQRDetail.class);
            intent.putExtra("current_itempk_qrRecv", listAllBodyPostition.get(position).getItempk());
            intent.putExtra("current_nnum_qrRecv", listAllBodyPostition.get(position).getNnum());
            intent.putExtra("current_scannum_qrRecv", listAllBodyPostition.get(position).getScannum());
            intent.putExtra("current_materialcode_qrRecv", listAllBodyPostition.get(position).getMaterialcode());
            intent.putExtra("current_ysnum_qrRecv", listAllBodyPostition.get(position).getYsnum());
            intent.putExtra("current_uploadflag_qrRecv", listAllBodyPostition.get(position).getUploadflag());
            intent.putExtra("current_vbillcode_qrRecv", current_sale_delivery_vbillcodeRecv);
            intent.putExtra("maccode",maccode);
            intent.putExtra("type",getIntent().getIntExtra("type",-1));
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
