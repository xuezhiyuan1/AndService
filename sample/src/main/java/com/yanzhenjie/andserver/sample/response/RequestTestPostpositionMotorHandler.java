package com.yanzhenjie.andserver.sample.response;

import android.os.Environment;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.sample.util.PropertiesUtils;
import com.yanzhenjie.andserver.util.HttpRequestParser;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Properties;

import android_serialport_api.SerialUtil;
import android_serialport_api.SerialUtilOld;

/**
 * 测试后置电机
 * Created by Administrator on 2017/10/26.
 */

public class RequestTestPostpositionMotorHandler implements RequestHandler{

    private SerialUtilOld serialUtilOld;
    int BBB;
    private  String port;

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parse(request);
        String machineId = URLDecoder.decode(params.get("machineId"), "utf-8");
        int machineid = Integer.parseInt(machineId);
        Properties prop = PropertiesUtils.propertiesUtils().properties(Environment.getExternalStorageDirectory() + "/Vendor/Config" + "/config.properties");
        port = prop.getProperty(machineId);
        if("com3".equals(port)){
            port = "/dev/ttymxc3";
        }else if("com4".equals(port)) {
            port = "/dev/ttymxc4";
        }
        //后置电机水平坐标
        String xAxis = URLDecoder.decode(params.get("xAxis"), "utf-8");
        final int xLocation = Integer.parseInt(xAxis);
        //后置电机垂直坐标
        String yAxis = URLDecoder.decode(params.get("yAxis"), "utf-8");
        final int yLocation = Integer.parseInt(yAxis);
        String data = null;
        try {
            JSONObject json = new JSONObject();
            json.put("xAxis",xLocation);
            json.put("yAxis",yLocation);
            json.put("machineId",machineid);
            json.put("result","success");
            data = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    serialUtilOld = new SerialUtilOld(port,19200,0);
                    byte[] byte_h_behind_shop = serialUtilOld.intToBytes(xLocation);
                    //左右
                    byte[] byte_zy_behind_shop = serialUtilOld.intToBytes(yLocation);
                    //先后置垂直     后左右
                    byte[] SEND_DATA_HZY_ORDER = new byte[]{(byte) 0x7E, 0x0E, 0x00,
                            (byte) 0x01, 0x35, 0x00, 0x02, byte_zy_behind_shop[0], byte_zy_behind_shop[1], 0x03, byte_h_behind_shop[0], byte_h_behind_shop[1], (byte) 0xF0, (byte) 0xED};
                    BBB = 0;
                    for (int a = 0; a < 12; a++) {
                        BBB += SEND_DATA_HZY_ORDER[a];
                        //&0x00ff 去掉高位 留下低位
                        BBB = BBB & 0x00ff;
                    }
                    int goout_Shop_Order = 0;
                    goout_Shop_Order = BBB;
                    byte[] SEND_DATA_HZY_ORDER_OK = new byte[]{(byte) 0x7E, 0x0E, 0x00,
                            (byte) 0x01, 0x35, 0x00, 0x02, byte_zy_behind_shop[0], byte_zy_behind_shop[1], 0x03, byte_h_behind_shop[0], byte_h_behind_shop[1], (byte) goout_Shop_Order, (byte) 0xED};
                    serialUtilOld.setData(SEND_DATA_HZY_ORDER_OK);
                }catch (Exception e){
                    e.getMessage();
                }
            }
        }).start();
        StringEntity stringEntity = new StringEntity(data,"utf-8");
        response.setEntity(stringEntity);
    }
}
