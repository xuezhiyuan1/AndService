package com.yanzhenjie.andserver.sample.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by think on 2018/3/29.
 */

public class VersionUtil {

    public static String getVersionName(Context context) throws Exception{
        //获取packManager实例
        PackageManager packageManager = context.getPackageManager();
        //getPackManagerName  是当前包名
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        String versionName = packageInfo.versionName;
        return versionName;
    }

    public static int getVersionCode(Context context) throws Exception{
        //获取packManager实例
        PackageManager packageManager = context.getPackageManager();
        //getPackManagerName  是当前包名
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        int versionCode = packageInfo.versionCode;
        return versionCode;
    }

}
