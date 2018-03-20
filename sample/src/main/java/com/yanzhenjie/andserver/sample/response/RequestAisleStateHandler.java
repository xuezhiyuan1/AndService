package com.yanzhenjie.andserver.sample.response;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.sample.ParamsSettingUtil;
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
 * 获取货道状态
 * Created by Administrator on 2018/1/19.
 */

public class RequestAisleStateHandler implements RequestHandler {

    private SerialUtilOld serialUtilOld;
    private String state;
    private String result;

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parse(request);
        String machineId = URLDecoder.decode(params.get("machineId"), "utf-8");
        int machineid = Integer.parseInt(machineId);
        try {
            serialUtilOld = new SerialUtilOld("/dev/ttymxc3",19200,0);
            serialUtilOld.setData(ParamsSettingUtil.SEND_DATA_SHOP_STATE);
            byte[] bytes = serialUtilOld.getDataByte();
            String bytesToHexString = serialUtilOld.bytesToHexString(bytes, bytes.length);
            String order = bytesToHexString.substring(0, bytesToHexString.indexOf("ed"));
            String str = order+"ed";
            state = str.substring(14, 16);
            if("00".equals(state)){
                result = "货道空闲";
            }else if ("01".equals(state)){
                result = "表示忙碌";
            }else if("02".equals(state)){
                result = "表示错误代码（需要复位人工处理）";
            }else if("03".equals(state)){
                result = "传感器故障(待定)";
            }
            JSONObject json = new JSONObject();
            json.put("machineId",machineid);
            json.put("Msg",result);
            json.put("result","success");
            String data = json.toString();
            StringEntity stringEntity = new StringEntity(data,"utf-8");
            response.setEntity(stringEntity);
        }catch (Exception e){
            e.getMessage();
        }

    }
}
