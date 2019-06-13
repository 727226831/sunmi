package com.example.shanggmiqr;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shanggmiqr.Url.iUrl;
import com.example.shanggmiqr.bean.CommonSendNoPagetotalBean;
import com.example.shanggmiqr.bean.User;
import com.example.shanggmiqr.util.BaseConfig;
import com.example.shanggmiqr.util.MyDataBaseHelper;
import com.example.shanggmiqr.util.Utils;
import com.example.weiytjiang.shangmiqr.BuildConfig;
import com.example.weiytjiang.shangmiqr.R;
import com.google.gson.Gson;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private Button mdownloadUserInfo;
    private Button mquitButton;
    private Button msettingButton;
    private EditText accountEdittext;
    private EditText pwdEdittext;
    private Button mlogInButton;
    private String result;
    private String number;
    private MyDataBaseHelper helper;
    private SQLiteDatabase db;
    private String user_ts_begintime;
    private String user_ts_endtime;
    private TextView textViewversion;
    private ZLoadingDialog dialog;
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    SharedPreferences updateConfig;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        //设定URL值
        //使用本地保存的代替


      //测试服


         updateConfig = getSharedPreferences("configInfo", 0);
         editor = updateConfig.edit();
        String URL = updateConfig.getString("WSDL_URI", iUrl.WSDL_URI);
        BaseConfig.setNcUrl(URL);

        mdownloadUserInfo = (Button) findViewById(R.id.downloaduserinfo);
        msettingButton = (Button) findViewById(R.id.setting);
        mquitButton = (Button) findViewById(R.id.quit);
        mlogInButton = (Button) findViewById(R.id.login);
        accountEdittext = (EditText) findViewById(R.id.accountEdittext);
        pwdEdittext = (EditText) findViewById(R.id.pwdEdittext);
        textViewversion=findViewById(R.id.tv_version);
       textViewversion.setText("版本号:"+ BuildConfig.VERSION_NAME);
        helper = new MyDataBaseHelper(MainActivity.this, "ShangmiData", null, 1);
        //创建或打开一个现有的数据库（数据库存在直接打开，否则创建一个新数据库）
        //创建数据库操作必须放在主线程，否则会报错
        db = helper.getWritableDatabase();//获取到了 SQLiteDatabase 对象
        if (isUserDBEmpty()) {
            mlogInButton.setEnabled(false);
        } else {
            mlogInButton.setEnabled(true);
        }
        mlogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msgLogin = new Message();
                        if (searchUser(accountEdittext.getText().toString(), pwdEdittext.getText().toString())) {
                            SharedPreferences currentAccount= getSharedPreferences("current_account", 0);
                            SharedPreferences.Editor editor1 = currentAccount.edit();
                            editor1.putString("current_account",pwdEdittext.getText().toString());

                            editor1.commit();
                            msgLogin.what = 0x13;
                            mHandler.sendMessage(msgLogin);
                            Intent intent = new Intent(MainActivity.this, TopMenu.class);
                            intent.putExtra("from_login", "Y");
                            startActivity(intent);
                        } else {
                            msgLogin.what = 0x14;
                            mHandler.sendMessage(msgLogin);
                        }
                    }
                }).start();

            }
        });
        mquitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        msettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
                View textEntryView = layoutInflater.inflate(R.layout.proxyserver_setting_dialog, null);
                final EditText editTextName = (EditText) textEntryView.findViewById(R.id.editTextName);
                final EditText editTextNumEditText = (EditText) textEntryView.findViewById(R.id.editTextNum);
                AlertDialog.Builder ad1 = new AlertDialog.Builder(MainActivity.this);
                ad1.setTitle("WebService设置:");


                editTextName.setText(updateConfig.getString("WSDL_URI",iUrl.WSDL_URI));
                editTextNumEditText.setText(updateConfig.getString("namespace",iUrl.namespace));
                ad1.setView(textEntryView);



                ad1.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {


                        editor.putString("WSDL_URI", editTextName.getText().toString());
                        editor.putString("namespace", editTextNumEditText.getText().toString());
                        editor.commit();
                    }
                });
                ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });
                ad1.show();// 显示对话框
            }
        });
        mdownloadUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ZLoadingDialog(MainActivity.this);
                dialog.setLoadingBuilder(Z_TYPE.CHART_RECT)//设置类型
                        .setLoadingColor(Color.BLUE)//颜色
                        .setHintText("数据信息下载中...")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .setHintTextSize(16) // 设置字体大小 dp
                        .setHintTextColor(Color.GRAY)  // 设置字体颜色
                        .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                        //     .setDialogBackgroundColor(Color.parseColor("#CC111111")) // 设置背景色，默认白色
                        .show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (isNetworkConnected(MainActivity.this)) {
                            try {
                                    String userData = getRemoteInfo("R03", "1");
                                    Message msg = new Message();
                                    if (userData == null) {
                                        msg.what = 0x15;
                                    } else {
                                        msg.what = 0x12;
                                    }
                                    if (null == userData) {
                                        mHandler.sendMessage(msg);
                                        return;
                                    }

                                    Gson gsonUser = new Gson();
                                    User user = gsonUser.fromJson(userData, User.class);
                                if (user.getPagetotal() ==1){
                                    insertUserDataToDB(user);
                                }else if(user.getPagetotal() <1){

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                        Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                                    return;
                                }else{
                                    insertUserDataToDB(user);
                                    for (int pagenum = 2; pagenum <= user.getPagetotal(); pagenum++) {
                                        String userData2 = getRemoteInfo("R03", String.valueOf(pagenum));
                                        User userBean2 = gsonUser.fromJson(userData2, User.class);
                                        insertUserDataToDB(userBean2);
                                    }
                                }

                                    user_ts_begintime = Utils.getCurrentDateTimeNew();
                                    user_ts_endtime = Utils.getDefaultEndTime();
                                    SharedPreferences latestDBTimeInfo5 = getSharedPreferences("LatestUserTSInfo", 0);
                                    SharedPreferences.Editor editor5 = latestDBTimeInfo5.edit();
                                    editor5.putString("latest_user_ts_begintime", user_ts_begintime);
                                    editor5.putString("latest_user_ts_endtime", user_ts_endtime);
                                    editor5.commit();
                                    mHandler.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Bundle bundle = new Bundle();
                                bundle.putString("Exception333", e.toString());
                                Message msg = new Message();
                                msg.what = 0x16;
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Message msg = new Message();
                            msg.what = 0x11;
                            mHandler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });

    }

   Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             if(dialog!=null){
                 dialog.dismiss();
             }

            switch (msg.what) {

                case 0x11:
                    //mlogInButton.setEnabled(true);
                    Toast.makeText(MainActivity.this, "请检查网络连接", Toast.LENGTH_LONG).show();
                    break;
                case 0x12:
                    mlogInButton.setEnabled(true);
                    String latest_user_ts = Utils.getCurrentDataTime();
                    SharedPreferences latestDBTimeInfo3 = getSharedPreferences("LatestDBTimeInfo", 0);
                    SharedPreferences.Editor editor3 = latestDBTimeInfo3.edit();
                    editor3.putString("latest_user_ts", latest_user_ts);
                    editor3.commit();


                    break;
                case 19:
                    Toast.makeText(MainActivity.this, "用户信息已是最新", Toast.LENGTH_SHORT).show();
                    break;
                case 0x14:
                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    break;
                case 0x15:

                    Toast.makeText(MainActivity.this, "用户数据已下载", Toast.LENGTH_LONG).show();
                    break;
                case 0x16:
                    String exception333 = msg.getData().getString("Exception333");
                    Toast.makeText(MainActivity.this, "用户最新信息下载失败,错误信息："+exception333, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };




    private boolean isUserDBEmpty() {
        Cursor cursor = db.rawQuery("select * from User",
                null);
        while (cursor.getCount() > 0) {
            // db.close();
            //  Log.i(" search_city_name_exist", str + "在数据库已存在,return true");
            return false;// //有城市在数据库已存在，返回false
        }
        return true;
    }

    private void insertUserDataToDB(User user) {
        //对象中拿到集合
        List<User.UserDataBean> userBeanList = user.getData();
        if(userBeanList==null){
            return;
        }
        for (User.UserDataBean ub : userBeanList) {
            String userpk = ub.getUserpk();
            String code = ub.getCode();
            String name = ub.getName();
            String org = ub.getOrg();
            String ts = ub.getTs();
            int enablestate = ub.getEnablestate();
            //使用 ContentValues 来对要添加的数据进行组装
            ContentValues values = new ContentValues();
            // 开始组装第一条数据
            values.put("name", name);
            values.put("code", code);
            values.put("userpk", userpk);
            values.put("org", org);
            values.put("enablestate", enablestate);
            values.put("ts", ts);
            // 插入第一条数据
            db.insert("User", null, values);
            values.clear();
        }
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * webservice查询
     */
    public String getRemoteInfo(String workcode, String pageNum) throws Exception {
        String WSDL_URI;
        String namespace;
        String begintime= "2001-01-01 00:00:00";
        String endtime =Utils.getDefaultEndTime();
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
        if (workcode.equals("R03")){
            SharedPreferences latestDBTimeInfo = getSharedPreferences("LatestUserTSInfo", 0);
            begintime = latestDBTimeInfo.getString("latest_user_ts_begintime", "2001-01-01 00:00:00");
            endtime = latestDBTimeInfo.getString("latest_user_ts_endtime", Utils.getDefaultEndTime());
        }
        // 设置需调用WebService接口需要传入的两个参数string、string1
        CommonSendNoPagetotalBean userSend = new CommonSendNoPagetotalBean(begintime, endtime, pageNum);
        Gson gson = new Gson();
        String userSendBean = gson.toJson(userSend);
        request.addProperty("string", workcode);
        request.addProperty("string1", userSendBean);
        Log.i("request-->",request.toString());
        //request.addProperty("string1", "{\"begintime\":\"1900-01-20 00:00:00\",\"endtime\":\"2018-08-21 00:00:00\", \"pagenum\":\"1\",\"pagetotal\":\"66\"}");
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
            Log.i("object-->",object.toString());
            // 获取返回的结果
            result = object.getProperty(0).toString();
            //   JSONObject jsonObject = new JSONObject(result);


            //等login后再关闭
            // db.close();
            return result;

        } catch (IOException e) {
           // e.printStackTrace();
            Bundle bundle = new Bundle();
            bundle.putString("Exception333", e.toString());
            Message msg = new Message();
            msg.what = 0x16;
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        } catch (XmlPullParserException e) {
            //e.printStackTrace();
            Bundle bundle = new Bundle();
            bundle.putString("Exception333", e.toString());
            Message msg = new Message();
            msg.what = 0x16;
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
        return result;
    }

    public boolean searchUser(String name, String pwd) {
        if (name.equals("") || pwd.equals("")) {
            return false;
        }
        Cursor cursor = db.rawQuery("select * from User where name=? and code=?",
                new String[]{name, pwd});
        while (cursor.moveToNext()) {
            // db.close();
            //  Log.i(" search_city_name_exist", str + "在数据库已存在,return true");
            return true;// //有城市在数据库已存在，返回true
        }
        //   db.close();
        // Log.i(" search_city_name_exist", str + "在数据库不存在，return false");
        return false;// //在数据库以前存在 false
    }
}


