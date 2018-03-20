package com.yanzhenjie.andserver.sample.util;

import android.os.Environment;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android_serialport_api.SerialUtilOld;

/**
 * Created by Administrator on 2018/2/7.
 */

public class PropertiesUtils {

    private static PropertiesUtils propertiesUtils = null;
    private PropertiesUtils(){};
    public static PropertiesUtils propertiesUtils (){
        if (propertiesUtils == null){
            propertiesUtils = new PropertiesUtils();
        }
        return propertiesUtils;
    }

    public Properties properties(String file){
        Properties prop = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return prop;
    }

}
