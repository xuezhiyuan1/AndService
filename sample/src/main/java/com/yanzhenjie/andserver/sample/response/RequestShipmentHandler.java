package com.yanzhenjie.andserver.sample.response;

import android.os.Environment;
import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.sample.ParamsSettingUtil;
import com.yanzhenjie.andserver.sample.util.MakeFileUtils;
import com.yanzhenjie.andserver.sample.util.PropertiesUtils;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android_serialport_api.SerialUtilOld;

/**
 * 出货接口
 * Created by Administrator on 2017/10/25.
 */

public class RequestShipmentHandler implements RequestHandler {
    private SerialUtilOld serialUtilOld;
    private int deliverySpeed = 2000;
    private byte[] SEND_DATA_SHOP_ORDER;
    int SheckSums;
    private volatile Boolean isQueryShipmentStatus = true;
    private volatile Lock lock = new ReentrantLock();
    private String machineid;
    private BufferedWriter out;
    private String year;
    private String month;
    private String day;
    private String shipmentStatus;
    private Date date;
    private String timestamp;
    private Date date2;
    private String sd;
    private SimpleDateFormat sdf;
    private volatile String resultData;

    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {

        Map<String, String> params = HttpRequestParser.parse(request);
        lock.lock();
        try {
            //设置参数
            String xA = URLDecoder.decode(params.get("xAxis"), "utf-8");
            int x = Integer.parseInt(xA);
            String yA = URLDecoder.decode(params.get("yAxis"), "utf-8");
            int y = Integer.parseInt(yA);
            String zA = URLDecoder.decode(params.get("zAxis"), "utf-8");
            int z = Integer.parseInt(zA);
            if(z<60)z=60;
            String deliverySpeedStr = URLDecoder.decode(params.get("deliverySpeed"), "utf-8");
            if(!"".equals(deliverySpeedStr))deliverySpeed = Integer.parseInt(deliverySpeedStr);
            machineid = URLDecoder.decode(params.get("machineId"), "utf-8");
            Properties prop = PropertiesUtils.propertiesUtils().properties(Environment.getExternalStorageDirectory() + "/Vendor/Config" + "/config.properties");
            String port = prop.getProperty(machineid);
            //设置串口号  波特率
            try {
                serialUtilOld = new SerialUtilOld(port,19200,0);
                byte[] byte_x_behind_shop = serialUtilOld.intToBytes(x);
                byte[] byte_y_behind_shop = serialUtilOld.intToBytes(y);
                byte[] byte_x_front_shop = serialUtilOld.intToBytes(z);
                byte[] byte_go_Shop_Sv = serialUtilOld.intToBytes(deliverySpeed);

                SEND_DATA_SHOP_ORDER = new byte[]{(byte) 0x7E, 0x13, 0x00,
                        (byte) 0x01,0x37,0x00,0x01,byte_x_front_shop[0],byte_x_front_shop[1],0x02,byte_y_behind_shop[0],byte_y_behind_shop[1],0x03,byte_x_behind_shop[0],byte_x_behind_shop[1],byte_go_Shop_Sv[0],byte_go_Shop_Sv[1],(byte)SheckSums,(byte) 0xED};
                SheckSums = 0;
                for(int a = 0;a < 17;a++){
                    SheckSums += SEND_DATA_SHOP_ORDER[a];
                    SheckSums = SheckSums &0x00ff;
                }
                int goout_Shop_Order;
                goout_Shop_Order = SheckSums;
                byte []SEND_DATA_SHOP_ORDER_OK = new byte[]{(byte) 0x7E, 0x13, 0x00,
                        (byte) 0x01,0x37,0x00,0x01,byte_x_front_shop[0],byte_x_front_shop[1],0x02,byte_y_behind_shop[0],byte_y_behind_shop[1],0x03,byte_x_behind_shop[0],byte_x_behind_shop[1],byte_go_Shop_Sv[0],byte_go_Shop_Sv[1],(byte)goout_Shop_Order,(byte) 0xED};
                //发送出货命令
                serialUtilOld.setData(SEND_DATA_SHOP_ORDER_OK);
                final byte[][] da = {serialUtilOld.getDataByte()};
                if(da == null){
                    out.write("没收到售货机回复信息");
                }
                String bytesToHexString = serialUtilOld.bytesToHexString(da[0], da[0].length);
                String order = bytesToHexString.substring(0, bytesToHexString.indexOf("ed"));
                String str = order + "ed";
                //获取当前时间
                date = new Date();
                year = String.format("%tY", date);
                month = String.format("%tB", date);
                day = String.format("%te", date);
                timestamp = String.valueOf(date.getTime());
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                // 时间戳转换成时间
                sd = sdf.format(new Date(Long.parseLong(String.valueOf(timestamp))));
                MakeFileUtils.getInstance().makeFilePath(Environment.getExternalStorageDirectory()+"/Vendor/Log","/"+machineid+"-"+year+month+day+".txt");
                out = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory()+"/Vendor/Log/"+machineid+"-"+year+month+day+".txt",true));
                out.write( "====================================================================="+"\r\n"); // \r\n即为换行
                out.write(sd +":"+str+"\r\n"); // \r\n即为换行
            }catch (Exception e){
                e.getMessage();
            }


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isQueryShipmentStatus = true;
            int count = 0;
            int count2 = 0;
            JSONObject json = new JSONObject();
            resultData = "";
            while (isQueryShipmentStatus) {
                try {
                    Thread.sleep(500);
                }catch (Exception e){
                    e.getMessage();
                }
                try {
                date2 = new Date();
                timestamp = String.valueOf(date2.getTime());
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                // 时间戳转换成时间
                    sd = sdf.format(new Date(Long.parseLong(String.valueOf(timestamp))));
                    //发送查询设备状态指令
                    serialUtilOld.setData(ParamsSettingUtil.SEND_DATA_SHOP_STATE);
                    out.write(sd+":+查询设备状态指令+\r\n"); // \r\n即为换行
                    final byte[][] datas = {serialUtilOld.getDataByte()};
                    String bytesToHexString = serialUtilOld.bytesToHexString(datas[0], datas[0].length);
                    String order = bytesToHexString.substring(0, bytesToHexString.indexOf("ed"));
                    String str = order + "ed";
                    out.write(sd+":"+str+"\r\n");
                    shipmentStatus = str.substring(26, 28);
                    out.write(sd+":"+shipmentStatus+"\r\n");

                    //判设备状态指令
                    if ("80".equals(shipmentStatus) || "81".equals(shipmentStatus) || "82".equals(shipmentStatus)){
                        out.write(sd+":"+str+"----80-82------"+"\r\n");
                        json.put("options",2);
                        resultData = json.toString();
                        isQueryShipmentStatus = false;
                    }else if ("07".equals(shipmentStatus)){
                        out.write(sd+":"+str+"----07------"+count+"\r\n");
                        json.put("machineId",machineid);
                        json.put("options",1);
                        json.put("result","success");
                        resultData = json.toString();
                        isQueryShipmentStatus = false;
                    }else if("06".equals(shipmentStatus)) {
                        count2++;
                        out.write(sd + ":" + str + "----06------" + count2 + "\r\n");
                    }else {
                        count++;
                        out.write(sd+":"+str+"----00----"+count+"\r\n");
                    }
                    if (count2 == 150) {
                        json.put("options", 3);
                        resultData = json.toString();
                        isQueryShipmentStatus = false;
                    }else if(count2 == 18){
                        serialUtilOld.setData(ParamsSettingUtil.SEND_DOOR_LOCK_OPEN);
                    }
                    if (count == 160) {
                        //***********************************
                        json.put("options",2);
                        resultData = json.toString();
                        isQueryShipmentStatus = false;
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
            out.write("--------------------------------------------------------------"+"\r\n"+"\r\n"+"\r\n"+"\r\n");
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件

            try {
                Thread.sleep(10000);
                serialUtilOld.setData(ParamsSettingUtil.SEND_DOOR_LOCK_CLOSE);
                StringEntity stringEntity = new StringEntity(resultData, "UTF-8");
                response.setEntity(stringEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

}
