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
 * 复位
 * Created by Administrator on 2017/12/26.
 */

public class RequestResetLocationHandler implements RequestHandler{
    private SerialUtilOld serialUtilOld;
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parse(request);
        String machineId = URLDecoder.decode(params.get("machineId"), "utf-8");
        int machineid = Integer.parseInt(machineId);
        try {
            JSONObject json = new JSONObject();
            json.put("machineId",machineid);
            json.put("result","success");
            json.put("Msg","正在复位");
            String data = json.toString();
            serialUtilOld = new SerialUtilOld("/dev/ttymxc3",19200,0);
            serialUtilOld.setData(ParamsSettingUtil.SEND_DATA_RESET);
            StringEntity stringEntity = new StringEntity(data,"utf-8");
            response.setEntity(stringEntity);
        }catch (Exception e){
            e.getMessage();
        }
    }
}
