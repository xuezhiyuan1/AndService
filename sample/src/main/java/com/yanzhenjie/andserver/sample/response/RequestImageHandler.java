package com.yanzhenjie.andserver.sample.response;

import com.yanzhenjie.andserver.RequestHandler;
import com.yanzhenjie.andserver.util.HttpRequestParser;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 *
 * 图片上传
 */
public class RequestImageHandler implements RequestHandler {

    private static final String SERVER = "http://cmoss.cloudmirror.cn/api/v1/file/";
    private static final String LOCALHOST = "http://localhost:8080";
    private static final String FILE_PATH = "/sdcard/vendor/image/";
    private static final String ABSOLUTE_PATH = "/storage/emulated/0/vendor/image/";

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        Map<String, String> params = HttpRequestParser.parse(httpRequest);
        String imageId = params.get("imageId");

        File file = new File(FILE_PATH +imageId+".jpg");
        if (!file.exists() && file.length() == 0){

            File fileDir = new File(FILE_PATH);
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }
            try {
                if(!file.exists())file.createNewFile();
                URL url = new URL(SERVER+imageId+"/stream");
                // 打开连接
                URLConnection con = url.openConnection();
                //设置请求超时为5s
                con.setConnectTimeout(5 * 1000);
                // 输入流
                InputStream is = con.getInputStream();
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;

                // 输出的文件流
                OutputStream os = new FileOutputStream(file);

                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                // 完毕，关闭所有流
                os.close();
                is.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String responseString = "{'imageUrl':'"+LOCALHOST+ ABSOLUTE_PATH +imageId+".jpg"+"'}";
        response(200, responseString, httpResponse);
    }


    private void response(int responseCode, String message, HttpResponse response) throws IOException {
        response.setStatusCode(responseCode);

        StringEntity stringEntity = new StringEntity(message, "utf-8");
        response.setEntity(stringEntity);
    }


}
