package com.example.shanggmiqr.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Created by weiyt.jiang on 2018/8/8.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper {//1.新建类继承SQLiteOpenHelper

    private Context context;//上下文

    //数据库中创建一张用户表
    public static final String User = "create table User ("
            + "id integer primary key autoincrement," + "name text,"
            + "code text," + "userpk text," + "org text," + "ts text,"+ "enablestate integer)";
    //数据库中创建一张仓库表
    public static final String Warehouse = "create table Warehouse ("
            + "id integer primary key autoincrement," + "stordocpk text,"
            + "code text," + "name text," + "enablestate text,"  + "orgcode text,"+ "ts text)";
    //数据库中创建一张物料表
    public static final String Material = "create table Material ("
            + "id integer primary key autoincrement," + "stordocpk text," + "name text," + "code text,"
            + "marbasclass text," + "ename text," + "materialspec text,"+ "materialtype text,"
            + "measdoccode text,"+ "orgcode text," + "brandname text," + "enablestate integer," + "materialbarcode text," + "ts text)";
//    //数据库中创建一张客户表
//    public static final String Customer = "create table Customer ("
//            + "id integer primary key autoincrement," + "pk_customer text,"
//            + "code text," + "name text," + "custstate integer," + "enablestate integer,"+ "custprop integer,"+ "custclassid integer,"+ "groupcode text," + "orgcode text,"+ "ts text)";
    //数据库中创建一张供应商表
    public static final String Supplier = "create table Supplier ("
            + "id integer primary key autoincrement," + "Pk_supplier text,"
            + "name text,"+ "cgorgcode text," + "code text," + "corpaddress text,"+ "orgcode text,"+ "supprop text," + "ts text)";
    //数据库中创建一张供应商表
    public static final String SupplierBankBody = "create table SupplierBankBody ("
            + "id integer primary key autoincrement," + "Pk_supplier text,"
            + "bankname text," + "accname text," + "accnum text)";
    //数据库中创建一张物流公司表
    public static final String LogisticsCompany = "create table LogisticsCompany ("
            + "id integer primary key autoincrement," + "org text,"
            + "code text," + "name text,"+ "ts text," + "status text)";
    //数据库中创建一张其他出库信息表以及对于的body表
    //flag为整单上传状态，Y为已经上传
    public static final String OtherOutgoing = "create table OtherOutgoing ("
            + "id integer primary key autoincrement," + "pobillcode text,"+ "dbilldate text," + "dr integer,"
            + "flag text,"+ "cwarecode text," +  "cwarename text)";
    public static final String OtherOutgoingBody = "create table OtherOutgoingBody ("
            + "id integer primary key autoincrement," + "pobillcode text," + "vcooporderbcode_b text,"+ "materialcode text," + "maccode text,"
            + "nnum integer,"  + "uploadnum text," + "scannum text,"+ "uploadflag text,"+  "pch text)";
    public static final String OtherOutgoingScanResult = "create table OtherOutgoingScanResult ("
            + "id integer primary key autoincrement," + "pobillcode text,"+ "vcooporderbcode_b text,"+ "cwarename text," + "materialcode text," + "platecode text,"
            + "boxcode text," + "xlh text,"+ "num text," + "itemuploadflag text,"+ "prodcutcode text)";
    public static final String OtherEntry = "create table OtherEntry ("
            + "id integer primary key autoincrement," + "pobillcode text,"+ "dbilldate text,"+ "dr integer,"
            + "flag text,"+ "cwarecode text," +  "cwarename text)";
    public static final String OtherEntryBody = "create table OtherEntryBody ("
            + "id integer primary key autoincrement," + "pobillcode text," + "vcooporderbcode_b text,"+ "materialcode text," + "maccode text,"
            + "nnum integer,"  + "uploadnum text," + "scannum text,"+ "uploadflag text,"+  "pch text)";
    public static final String OtherEntryScanResult = "create table OtherEntryScanResult ("
            + "id integer primary key autoincrement," + "pobillcode text,"+ "vcooporderbcode_b text,"+ "cwarename text," + "materialcode text," + "platecode text,"
            + "boxcode text,"+ "xlh text," + "num text," + "itemuploadflag text,"+ "prodcutcode text)";
    public static final String SaleDelivery = "create table SaleDelivery ("
            + "id integer primary key autoincrement," + "vtrantypecode text,"+ "dr integer,"+ "unitcode text," + "busitypecode text," + "vbillcode text,"
            + "dbilldate text," + "deptcode text,"+ "Pupsndoccode text,"+ "Transporttypecode text,"+ "billmakercode text,"+ "vmemo text,"+ "flag text,"+ "country text)";
    public static final String SaleDeliveryBody = "create table SaleDeliveryBody ("
            + "id integer primary key autoincrement," + "vbillcode text,"+ "vcooporderbcode_b text," + "matrcode text," + "matrname text,"
            + "maccode text," + "nnum text," + "scannum text," + "rackcode text,"+ "customer text,"+ "uploadflag text,"+ "cwarename text,"+ "orginal_cwarename text,"+ "cwarehousecode text)";
    public static final String SaleDeliveryScanResult = "create table SaleDeliveryScanResult ("
            + "id integer primary key autoincrement," + "vbillcode text,"+ "vcooporderbcode_b text,"+ "matrcode text," + "platecode text,"
            + "boxcode text," + "num text," + "prodcutcode text," + "itemuploadflag text,"+ "xlh text)";
    public static final String PurchaseReturn = "create table PurchaseReturn ("
            + "id integer primary key autoincrement,"+ "vbillcode text," + "dbilldate text,"+ "dr integer,"+ "ts text," + "flag text," + "org text)";
    public static final String PurchaseReturnBody = "create table PurchaseReturnBody ("
            + "id integer primary key autoincrement," + "vbillcode text,"+ "itempk text," + "materialname text," + "nnum text,"
            + "warehouse text," + "maccode text," + "materialcode text," + "scannum text,"+ "uploadflag text,"+ "orginal_cwarename text)";
    public static final String PurchaseReturnScanResult = "create table PurchaseReturnScanResult ("
            + "id integer primary key autoincrement," + "vbillcode text,"+ "itempk text,"+ "maccode text," + "materialcode text," + "platecode text,"
            + "boxcode text," + "num text," + "prodcutcode text," + "itemuploadflag text,"+ "xlh text)";
    public static final String PurchaseArrival = "create table PurchaseArrival ("
            + "id integer primary key autoincrement,"+ "headpk text,"+ "vbillcode text," + "dbilldate text,"+ "dr integer,"+ "ts text," + "flag text," + "num text," + "org text)";
    public static final String PurchaseArrivalBody = "create table PurchaseArrivalBody ("
            + "id integer primary key autoincrement,"+ "headpk text," + "vbillcode text,"+ "itempk text," + "materialname text," + "nnum text,"
            + "warehouse text," + "maccode text," + "materialcode text," + "scannum text,"+ "uploadflag text,"+ "orginal_cwarename text)";
    public static final String PurchaseArrivalScanResult = "create table PurchaseArrivalScanResult ("
            + "id integer primary key autoincrement,"+ "headpk text," + "vbillcode text,"+ "itempk text,"+ "maccode text," + "materialcode text," + "platecode text,"
            + "boxcode text," + "num text," + "prodcutcode text," + "itemuploadflag text,"+ "xlh text)";
    public static final String Loan = "create table Loan ("
            + "id integer primary key autoincrement,"+ "dr integer," + "pobillcode text,"
            + "dbilldate text," + "num text,"+ "ts text,"+ "flag text)";
    public static final String LoanBody = "create table LoanBody ("
            + "id integer primary key autoincrement," + "pobillcode text,"+ "itempk text," + "materialcode text,"
            + "maccode text," + "nnum text," + "scannum text," + "vemo text,"+ "uploadflag text,"+ "cwarename text,"+ "orginal_cwarename text,"+ "cwarecode text)";
    public static final String LoanScanResult = "create table LoanScanResult ("
            + "id integer primary key autoincrement," + "pobillcode text,"+ "itempk text,"+ "materialcode text," + "platecode text,"
            + "boxcode text," + "num text," + "prodcutcode text," + "itemuploadflag text,"+ "xlh text)";
    public static final String ProductEntry = "create table ProductEntry ("
            + "id integer primary key autoincrement," + "headpk text,"+ "dr integer,"+ "billcode text,"
            + "dbilldate text," + "cwarecode text,"+ "cwarename text,"+ "org text,"+ "ts text,"+ "totalnum text,"+ "flag text)";
    public static final String ProductEntryBody = "create table ProductEntryBody ("
            + "id integer primary key autoincrement," + "billcode text,"+ "itempk text," + "materialcode text," + "nnum text,"
            + "ysnum text,"+ "scannum text,"+ "uploadflag text)";
    public static final String ProductEntryScanResult = "create table ProductEntryScanResult ("
            + "id integer primary key autoincrement," + "billcode text,"+ "itempk text,"+ "materialcode text," + "platecode text,"
            + "boxcode text," + "num text," + "prodcutcode text," + "itemuploadflag text,"+ "xlh text)";
    public static final String AllocateTransfer = "create table AllocateTransfer ("
            + "id integer primary key autoincrement," +"headpk text,"+ "cunitcode text," + "runitcode text," + "billno text,"+ "org text,"+ "ts text,"
            + "dbilldate text,"+ "dr integer," + "num text,"+ "flag text)";
    public static final String AllocateTransferBody = "create table AllocateTransferBody ("
            + "id integer primary key autoincrement,"+"headpk text," + "billno text,"+ "itempk text,"+ "address text," + "materialcode text,"+ "materialclasscode text," + "nnum text,"
            + "rwarehousecode text,"+ "cwarehousecode text,"+ "scannum text,"+ "uploadflag text)";
    public static final String AllocateTransferScanResult = "create table AllocateTransferScanResult ("
            + "id integer primary key autoincrement," + "billno text,"+ "itempk text,"+ "materialcode text," + "platecode text,"
            + "boxcode text," + "num text," + "prodcutcode text," + "itemuploadflag text,"+ "xlh text)";
    public static final String QrcodeRule = "create table QrcodeRule ("
            + "id integer primary key autoincrement," + "matbasclassname text,"+ "Matbasclasscode text,"+ "name text," + "code text,"
            + "length text," + "bartype text," + "complement text," + "fillcode text,"+ "ts text)";
    public static final String QrcodeRuleBody = "create table QrcodeRuleBody ("
            + "id integer primary key autoincrement," + "Matbasclasscode text,"+ "itemlength text," + "startpos text,"+ "appobjattr text)";
    //2.实现构造方法
    public MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //int version-当前数据库的版本号，可用于对数据库进行升级操作
        super(context, name, factory, version);
        this.context = context;
    }


    //3.重写onCreate方法
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(User);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(Warehouse);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(Material);//执行建表语句，创建数据库
    //    sqLiteDatabase.execSQL(Customer);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(Supplier);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(LogisticsCompany);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(SupplierBankBody);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(OtherOutgoing);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(OtherOutgoingBody);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(OtherOutgoingScanResult);
        sqLiteDatabase.execSQL(OtherEntry);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(OtherEntryBody);//执行建表语句，创建数据库
        sqLiteDatabase.execSQL(OtherEntryScanResult);
        sqLiteDatabase.execSQL(SaleDelivery);
        sqLiteDatabase.execSQL(SaleDeliveryBody);
        sqLiteDatabase.execSQL(SaleDeliveryScanResult);
        sqLiteDatabase.execSQL(PurchaseReturn);
        sqLiteDatabase.execSQL(PurchaseReturnBody);
        sqLiteDatabase.execSQL(PurchaseReturnScanResult);
        sqLiteDatabase.execSQL(PurchaseArrival);
        sqLiteDatabase.execSQL(PurchaseArrivalBody);
        sqLiteDatabase.execSQL(PurchaseArrivalScanResult);
        sqLiteDatabase.execSQL(Loan);
        sqLiteDatabase.execSQL(LoanBody);
        sqLiteDatabase.execSQL(LoanScanResult);
        sqLiteDatabase.execSQL(ProductEntry);
        sqLiteDatabase.execSQL(ProductEntryBody);
        sqLiteDatabase.execSQL(ProductEntryScanResult);
        sqLiteDatabase.execSQL(AllocateTransfer);
        sqLiteDatabase.execSQL(AllocateTransferBody);
        sqLiteDatabase.execSQL(AllocateTransferScanResult);
        sqLiteDatabase.execSQL(QrcodeRule);
        sqLiteDatabase.execSQL(QrcodeRuleBody);
        Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show();
    }

    //4.重写onUpgrade方法
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }




}
