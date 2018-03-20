package com.yanzhenjie.andserver.sample.response;

import com.yanzhenjie.andserver.RequestHandler;
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

import android_serialport_api.SerialUtil;
import android_serialport_api.SerialUtilOld;

/**
 * 测试前置电机
 * Created by Administrator on 2017/10/26.
 */

public class RequestTestFrontMotorHandler implements RequestHandler {

    private SerialUtilOld serialUtilOld;
    private byte[] SEND_DATA_Q_ORDER;
    int AAA;
    private int zLocation;

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parse(request);
        String machineId = URLDecoder.decode(params.get("machineId"), "utf-8");
        int machineid = Integer.parseInt(machineId);
        String zAxis = URLDecoder.decode(params.get("zAxis"), "utf-8");
        zLocation = Integer.parseInt(zAxis);
        try {
            JSONObject json = new JSONObject();
            json.put("machineId",machineid);
            json.put("zAxis",zLocation);
            json.put("result","success");
            String data = json.toString();
            StringEntity stringEntity = new StringEntity(data,"utf-8");
            response.setEntity(stringEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    serialUtilOld = new SerialUtilOld("/dev/ttymxc3",19200,0);
                    byte[] byte_q_behind_shop = serialUtilOld.intToBytes(zLocation);
                    SEND_DATA_Q_ORDER = new byte[]{(byte) 0x7E, 0x0B, 0x00,
                            (byte) 0x01, 0x36, 0x00, 0x01, byte_q_behind_shop[0], byte_q_behind_shop[1], (byte) AAA, (byte) 0xED};
                    AAA = 0;
                    for (int a = 0; a < 9; a++) {
                        AAA += SEND_DATA_Q_ORDER[a];
                        //&0x00ff 去掉高位 留下低位
                        AAA = AAA & 0x00ff;
                    }
                    int goout_Shop_Order = 0;
                    goout_Shop_Order = AAA;
                    byte[] SEND_DATA_Q_ORDER_OK = new byte[]{(byte) 0x7E, 0x0B, 0x00,
                            (byte) 0x01, 0x36, 0x00, 0x01, byte_q_behind_shop[0], byte_q_behind_shop[1], (byte) goout_Shop_Order, (byte) 0xED};
                    serialUtilOld.setData(SEND_DATA_Q_ORDER_OK);
                }catch (Exception e){
                    e.getMessage();
                }
            }
        }).start();

    }
}
