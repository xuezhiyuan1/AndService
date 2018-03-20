package com.yanzhenjie.andserver.sample.response;

import android.os.Environment;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.sample.ParamsSettingUtil;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;

import android_serialport_api.SerialUtilOld;

/**
 * Created by think on 2018/3/20.
 */

public class RequestOpenDoorHandler implements RequestHandler {
    private SerialUtilOld serialUtilOld;
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        try {
            Map<String, String> params = HttpRequestParser.parse(request);
            String machineid = URLDecoder.decode(params.get("machineId"), "utf-8");
            Properties prop = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory()+"/Vendor/Config"+"/config.properties"));
            prop.load(in);
            String port = prop.getProperty(machineid);
            serialUtilOld = new SerialUtilOld(port,19200,0);
            JSONObject json = new JSONObject();
            serialUtilOld.setData(ParamsSettingUtil.SEND_DOOR_LOCK_OPEN);
            json.put("machineId",machineid);
            json.put("options",1);
            json.put("result","success");
            json.put("Msg","门仓打开");
            String data = json.toString();
            StringEntity stringEntity = new StringEntity(data,"utf-8");
            response.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
