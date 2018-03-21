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
 * 获取本次出货状态
 * Created by Administrator on 2018/1/19.
 */

public class RequestShipMentStateHandler implements RequestHandler {
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
            state = str.substring(26, 28);
            if("00".equals(state)){
                result = "初始位置";
            }else if ("01".equals(state)){
                result = "收到出货指令";
            }else if("02".equals(state)){
                result = "电机正在运动";
            }else if("03".equals(state)){
                result = "电机到指定坐标正在推货";
            }else if("04".equals(state)){
                result = "货物到取货仓";
            }else if("05".equals(state)){
                result = "货物正在下降";
            }else if("06".equals(state)){
                result = "货物到取货口，取货仓打开";
            }else if("07".equals(state)){
                result = "用户取走货物";
            }else if("80".equals(state)){
                result = "电机故障，未到指定坐";
            }else if("81".equals(state)){
                result = "货道故障或者货道空，货物未到取货仓";
            }else if("82".equals(state)){
                result = "取货仓传感器故障，本次取货失败";
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
