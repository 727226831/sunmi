package com.example.shanggmiqr.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shanggmiqr.adapter.SaleDeliveryAdapter;
import com.example.shanggmiqr.bean.QrcodeRule;
import com.example.shanggmiqr.bean.SaleDeliveryBean;
import com.example.shanggmiqr.transaction.SaleDelivery;
import com.example.weiytjiang.shangmiqr.R;

import java.util.ArrayList;
import java.util.List;

public class iUntils {
    //0物料编码  1序列号
    public  static   QrcodeRule.DataBean.ItemBean getCurrentQrcodeRule(SQLiteDatabase db, String matbasclasscode, String type ) {
        Cursor cursor = db.rawQuery("select * from QrcodeRuleBody where Matbasclasscode=?",
                new String[]{matbasclasscode});


        QrcodeRule.DataBean.ItemBean bean=new QrcodeRule.DataBean.ItemBean();

        //判断cursor中是否存在数据
        while (cursor.moveToNext()) {
            bean = new QrcodeRule.DataBean.ItemBean();
            bean.itemlength = cursor.getInt(cursor.getColumnIndex("itemlength"));
            bean.startpos = cursor.getInt(cursor.getColumnIndex("startpos"));
            bean.appobjattr = cursor.getString(cursor.getColumnIndex("appobjattr"));
            if (type.equals(bean.appobjattr)) {
                return bean;
            } else if (type.equals(bean.appobjattr)) {
                return bean;
            }

        }
        cursor.close();
        return bean;
    }
    public static   String getMaccode(SQLiteDatabase db,String productcode,String matbasclasscode){
        String maccode="";
        QrcodeRule.DataBean.ItemBean bean=getCurrentQrcodeRule(db,matbasclasscode,"物料条码");
        maccode= productcode.substring(bean.startpos - 1,
                bean.startpos - 1 + bean.itemlength);
        return maccode;
    }
    public static   String getXlh(SQLiteDatabase db,String productcode,String matbasclasscode){
        String xlh="";
        QrcodeRule.DataBean.ItemBean bean=getCurrentQrcodeRule(db,matbasclasscode,"序列号");
        xlh= productcode.substring(bean.startpos - 1,
                bean.startpos - 1 + bean.itemlength);
        return xlh;
    }
    public static boolean isAlreadyScanned(SQLiteDatabase db,String pobillcode,String prodcutcode,String vcooporderbcode_b) {
        Cursor cursor = db.rawQuery("select * from OtherOutgoingScanResult where pobillcode=? and prodcutcode=? and vcooporderbcode_b=?",
                new String[]{pobillcode, prodcutcode, vcooporderbcode_b});
        while (cursor != null && cursor.getCount() > 0) {
            if (cursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }
    public static int countSum(SQLiteDatabase db,String pobillcode,String  materialcode,String vcooporderbcode_b) {
        Cursor cursor = db.rawQuery("select * from OtherOutgoingScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                new String[]{pobillcode,materialcode, vcooporderbcode_b});
        while (cursor != null && cursor.getCount() > 0) {
            return cursor.getCount();// //有城市在数据库已存在，返回true
        }
        return 0;
    }
    public static String countScannedQRCode(SQLiteDatabase db,String pobillcode, String materialcode,String vcooporderbcode_b) {
        String count = "0";
        Cursor cursor2 =db.rawQuery("select prodcutcode from OtherOutgoingScanResult where pobillcode=? and materialcode=? and vcooporderbcode_b=?",
                new String[]{pobillcode, materialcode, vcooporderbcode_b});
        if (cursor2 != null && cursor2.getCount() > 0) {
            //判断cursor中是否存在数据
            count = String.valueOf(cursor2.getCount());
            cursor2.close();
            return count;
        }
        return count;
    }
    public static void updateSaleDeliveryBodyscannum(SQLiteDatabase db, String scannum, String vbillcode, String vcooporderbcode_b) {

        ContentValues contentValues=new ContentValues();
        contentValues.put("scannum",scannum);

        db.update("SaleDeliveryBody",contentValues,"vbillcode=? and vcooporderbcode_b=?",
                new String[]{ vbillcode,vcooporderbcode_b});
    }







}
