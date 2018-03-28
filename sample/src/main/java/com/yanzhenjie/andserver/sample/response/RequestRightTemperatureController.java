package com.yanzhenjie.andserver.sample.response;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

import android_serialport_api.SerialUtilOld;

/**
 * Created by think on 2018/3/28.
 */

public class RequestRightTemperatureController implements RequestHandler {

    private SerialUtilOld serialUtilOld;
    private int hun_tem;
    private int temperature;

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        try {
            Map<String, String> params = HttpRequestParser.parse(request);
            String machineid = URLDecoder.decode(params.get("machineId"), "utf-8");
            //左机柜
            serialUtilOld = new SerialUtilOld("/dev/ttymxc2",9600,0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] data = serialUtilOld.getDataByte();
                        Thread.sleep(2000);
                        String data_Str = serialUtilOld.bytesToHexString(data, data.length);
                        StringBuffer sb = new StringBuffer(data_Str);
                        String substring = sb.substring(0, 16);
                        //温度
                        temperature = Integer.parseInt(substring.substring(6, 10), 16);
                        //湿度
                        int humidity = Integer.parseInt(substring.substring(10, 14), 16);
                        hun_tem = humidity + 134;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            JSONObject json = new JSONObject();
            json.put("machineId",machineid);
            json.put("temperature",temperature);
            json.put("humidity",hun_tem);
            json.put("result","success");
            json.put("Msg","右机柜温湿度");
            String data = json.toString();
            StringEntity stringEntity = new StringEntity(data,"utf-8");
            response.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
