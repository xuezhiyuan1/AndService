package com.yanzhenjie.andserver.sample.response;

import android.os.Environment;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.sample.ParamsSettingUtil;
import com.yanzhenjie.andserver.sample.util.PropertiesUtils;
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
import java.util.Properties;

import android_serialport_api.SerialUtilOld;

/**
 * 获取前置电机状态
 * Created by Administrator on 2018/1/19.
 */

public class RequestTakeFrontMotorStateHandler implements RequestHandler{

    private SerialUtilOld serialUtilOld;
    private String state;
    private String result;
    private String port;

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
        try {
            serialUtilOld = new SerialUtilOld(port,19200,0);
            serialUtilOld.setData(ParamsSettingUtil.SEND_DATA_SHOP_STATE);
            byte[] bytes = serialUtilOld.getDataByte();
            String bytesToHexString = serialUtilOld.bytesToHexString(bytes, bytes.length);
            String order = bytesToHexString.substring(0, bytesToHexString.indexOf("ed"));
            String str = order+"ed";
            state = str.substring(18, 20);
            if("00".equals(state)){
                result = "初始位置";
            }else if ("01".equals(state)){
                result = "正在向上运动";
            }else if("02".equals(state)){
                result = "正在归位";
            }else if("03".equals(state)){
                result = "到指定位置";
            }else if("80".equals(state)){
                result = "上限位被触发";
            }else if("81".equals(state)){
                result = "前置电机运动超时";
            }else if("82".equals(state)){
                result = "其它故障";
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
