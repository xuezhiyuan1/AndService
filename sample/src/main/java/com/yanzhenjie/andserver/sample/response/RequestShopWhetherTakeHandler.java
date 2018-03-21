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
 * 判断货物是否被取走
 * Created by Administrator on 2017/10/26.
 */

public class RequestShopWhetherTakeHandler implements RequestHandler {

    private SerialUtilOld serialUtilOld;
    String shipmentStatus;

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        try {
            Map<String, String> params = HttpRequestParser.parse(request);
            String machineid = URLDecoder.decode(params.get("machineId"), "utf-8");
            Properties prop = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory()+"/Vendor/Config"+"/config.properties"));
            prop.load(in);
            String port = prop.getProperty(machineid);
            if("com3".equals(port)){
                port = "/dev/ttymxc3";
            }else if("com4".equals(port)) {
                port = "/dev/ttymxc4";
            }
            serialUtilOld = new SerialUtilOld(port,19200,0);
            JSONObject json = new JSONObject();
            serialUtilOld.setData(ParamsSettingUtil.SEND_DATA_SHOP_STATE);
            byte[][] datas = {serialUtilOld.getDataByte()};
            String bytesToHexString = serialUtilOld.bytesToHexString(datas[0], datas[0].length);
            String order = bytesToHexString.substring(0, bytesToHexString.indexOf("ed"));
            String str = order + "ed";
            shipmentStatus = str.substring(26, 28);
            if("00".equals(shipmentStatus) || "07".equals(shipmentStatus) ){
                json.put("machineId",machineid);
                json.put("options",1);
                json.put("result","success");
                json.put("Msg","货仓无货");
            }else if("06".equals(shipmentStatus)){
                json.put("machineId",machineid);
                json.put("options",-1);
                json.put("result","success");
                json.put("Msg","货仓有货");
            }else{
                json.put("machineId",machineid);
                json.put("options",0);
                json.put("result","success");
                json.put("Msg","异常");
            }
            String data = json.toString();
            StringEntity stringEntity = new StringEntity(data,"utf-8");
            response.setEntity(stringEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
