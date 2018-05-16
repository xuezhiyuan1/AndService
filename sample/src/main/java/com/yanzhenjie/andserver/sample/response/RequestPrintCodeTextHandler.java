package com.yanzhenjie.andserver.sample.response;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by think on 2018/5/7.
 */

public class RequestPrintCodeTextHandler implements RequestHandler {

    private PrinterUtil printerInstance;
    private List<String> list;
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        list = new ArrayList<>();
        try {
            Map<String, String> params = HttpRequestParser.parse(request);
            String machineId = URLDecoder.decode(params.get("machineId"), "utf-8");
            int machineid = Integer.parseInt(machineId);
            //对齐方式
            String Align = URLDecoder.decode(params.get("align"), "utf-8");
            int align = Integer.parseInt(Align);
            String str = URLDecoder.decode(params.get("context"), "utf-8");
            //店名
            String nameOfShop = URLDecoder.decode(params.get("nameOfShop"), "utf-8");
            //订单号
            String unitNumber = URLDecoder.decode(params.get("UnitNumber"), "utf-8");
            //折前价格
            String discountPrice = URLDecoder.decode(params.get("DiscountPrice"));
            //优惠价格
            String amountOfBenefit = URLDecoder.decode(params.get("AmountOfBenefit"));
            //折后价格
            String amountAfterDiscount = URLDecoder.decode(params.get("AmountAfterDiscount"));
            //已收明细  ObviousReceipt
            String obviousReceipt = URLDecoder.decode(params.get("ObviousReceipt"));
            //支付
            String pay = URLDecoder.decode(params.get("pay"));
            //收款
            String makeCollections = URLDecoder.decode(params.get("makeCollections"));
            /*//营业员 SalesTax
            String SalesTax = URLDecoder.decode(params.get("SalesTax"));*/
            /*//找零
            String findChange = URLDecoder.decode(params.get("findChange"));
            //会员号
            String vip = URLDecoder.decode(params.get("vip"));
            //本次积分
            String Integration = URLDecoder.decode(params.get("Integration"));
            //本店电话
            String tel = URLDecoder.decode(params.get("tel"));
            //本店地址
            String Address = URLDecoder.decode(params.get("Address"));*/
            //现在时间
            String time = URLDecoder.decode(params.get("time"));
            //二维码信息
            String info = URLDecoder.decode(params.get("info"));
            String[] split = str.split("[,]");
            printerInstance = PrinterUtil.getInstance().init();
            printerInstance.OpenComPort();
            JSONObject json = new JSONObject();
            for(int i = 0;i<split.length;i++){
                String data = split[i];
                list.add(data);
                json.put("msg"+i,data);
            }
            //,SalesTax,findChange,vip,Integration,tel,Address,,info
            printerInstance.printTest2(list,align,unitNumber,nameOfShop,discountPrice,amountOfBenefit,amountAfterDiscount,
                    obviousReceipt,pay,makeCollections,time,info);
            json.put("machineId",machineid);
            json.put("result","success");
            String data = json.toString();
            StringEntity stringEntity = new StringEntity(data,"utf-8");
            response.setEntity(stringEntity);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
