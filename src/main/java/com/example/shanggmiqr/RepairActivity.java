package com.example.shanggmiqr;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shanggmiqr.bean.DataBean;
import com.example.shanggmiqr.bean.MenuBean;
import com.example.shanggmiqr.bean.RepairBean;
import com.example.shanggmiqr.util.BaseConfig;
import com.example.shanggmiqr.util.DataHelper;
import com.example.weiytjiang.shangmiqr.R;
import com.google.gson.Gson;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class RepairActivity extends AppCompatActivity {
   private Button buttonSubmit;
   private EditText editTextOld,editTextNew,editTextUser;
    RepairBean repairBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        editTextNew=findViewById(R.id.et_new);
        editTextOld=findViewById(R.id.et_old);
        buttonSubmit=findViewById(R.id.b_submit);
        editTextUser=findViewById(R.id.et_user);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             new Thread(
                     new Runnable() {
                         @Override
                         public void run() {
                             if (DataHelper.isNetworkConnected(RepairActivity.this)) {
                                repairBean =new Gson().fromJson(repair(),RepairBean.class);
                                handler.sendEmptyMessage(0);

                             }
                         }
                     }
             ).start();
            }
        });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case  0:
                    Toast.makeText(RepairActivity.this,repairBean.getErrmsg(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private String repair() {
            String result=null;
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


            request.addProperty("string", "R75");
            RepairBean repairBean=new RepairBean();
            repairBean.setAppuser(editTextUser.getText().toString());
            repairBean.setNewpassword(editTextNew.getText().toString());
            repairBean.setOldpassword(editTextOld.getText().toString());
            request.addProperty("string1",new Gson().toJson(repairBean));
            Log.i("request-->",request.toString());

            //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);

            envelope.bodyOut = request;
            envelope.dotNet = false;

            HttpTransportSE se = new HttpTransportSE(WSDL_URI);
            try {
                //   se.call(null, envelope);//调用
                se.call(namespace + "sendToWISE", envelope);
                // 获取返回的数据
                SoapObject object = (SoapObject) envelope.bodyIn;
                Log.i("response-->",object.toString());
                // 获取返回的结果
                result = object.getProperty(0).toString();

                return result;

            } catch (Exception e) {
                 e.printStackTrace();

            }
            return result;
        }

}
