package com.example.shanggmiqr.util;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.tencent.bugly.crashreport.CrashReport;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        CrashReport.initCrashReport(getApplicationContext(), "8a1b82cbb3", false);


        //  ExceptionHandler.getInstance().initConfig(this);

    }


}
