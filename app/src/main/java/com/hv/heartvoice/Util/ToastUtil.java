package com.hv.heartvoice.Util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

import es.dmoral.toasty.Toasty;

public class ToastUtil {

    private static Context context;

    public static void init(Context applicationContext) {
        context=applicationContext;
    }

    public static void errorShort(@StringRes int id) {
        Toasty.error(context,id, Toast.LENGTH_SHORT).show();
    }

    public static void errorShort(String msg) {
        Toasty.error(context,msg, Toast.LENGTH_SHORT).show();
    }

    public static void errorLong(@StringRes int id) {
        Toasty.error(context,id, Toast.LENGTH_LONG).show();
    }

    public static void successShort(@StringRes int id) {
        Toasty.success(context,id, Toast.LENGTH_SHORT).show();
    }

    public static void successLong(@StringRes int id) {
        Toasty.success(context,id, Toast.LENGTH_LONG).show();
    }

}
