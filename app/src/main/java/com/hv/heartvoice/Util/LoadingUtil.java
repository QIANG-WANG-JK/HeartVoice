package com.hv.heartvoice.Util;

import android.app.Activity;
import android.app.ProgressDialog;

public class LoadingUtil {

    private static ProgressDialog progressDialog;

    public static void showLoading(Activity activity){
        showLoading(activity,"拼命加载中");
    }

    public static void showLoading(Activity activity, String msg) {
        if(activity == null || activity.isFinishing()){
            return;
        }
        if(progressDialog != null){
            return;
        }
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void hideLoading(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.hide();
            progressDialog = null;
        }
    }

}
