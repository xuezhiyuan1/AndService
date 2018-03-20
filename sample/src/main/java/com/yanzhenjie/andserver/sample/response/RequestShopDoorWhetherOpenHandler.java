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

import android_serialport_api.SerialUtilOld;

/**
 * 判断货舱门是否开启
 * Created by Administrator on 2017/10/26.
 */

public class RequestShopDoorWhetherOpenHandler implements RequestHandler {

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        try {
            Map<String, String> params = HttpRequestParser.parse(request);
            String machineId = URLDecoder.decode(params.get("machineId"), "utf-8");
            int machineid = Integer.parseInt(machineId);
            JSONObject json = new JSONObject();
            json.put("machineId",machineid);
            json.put("options",-1);
            json.put("result","success");
            String data = json.toString();
            StringEntity stringEntity = new StringEntity(data,"utf-8");
            response.setEntity(stringEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
     }
}
