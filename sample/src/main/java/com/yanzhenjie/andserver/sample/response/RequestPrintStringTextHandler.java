package com.yanzhenjie.andserver.sample.response;

import android.os.Handler;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.sample.util.PrinterUtil;
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

/**
 * 打印购买商品信息
 * Created by think on 2018/3/22.
 */

public class RequestPrintStringTextHandler implements RequestHandler {
    private PrinterUtil printerInstance;
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        try {
            Map<String, String> params = HttpRequestParser.parse(request);
            String machineId = URLDecoder.decode(params.get("machineId"), "utf-8");
            int machineid = Integer.parseInt(machineId);
            String str = URLDecoder.decode(params.get("context"), "utf-8");
            printerInstance = PrinterUtil.getInstance().init();
            printerInstance.OpenComPort();
            printerInstance.printTest(str);
            JSONObject json = new JSONObject();
            json.put("machineId",machineid);
            json.put("Msg",str);
            json.put("result","success");
            String data = json.toString();
            StringEntity stringEntity = new StringEntity(data,"utf-8");
            response.setEntity(stringEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
