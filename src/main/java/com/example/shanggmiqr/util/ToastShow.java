package com.example.shanggmiqr.util;

import android.content.Context;
import android.widget.Toast;

public class ToastShow {
    public static Toast toast;
    public static   void show(Context context,String string,int duartion){
        if(toast==null){
           toast=Toast.makeText(context,string,duartion);
        }else {
            toast.setText(string);
            toast.setDuration(duartion);
        }
        toast.show();
    }

}
