package com.yanzhenjie.andserver.sample.util;

import java.io.File;

/**
 * Created by Administrator on 2018/2/7.
 */

public class MakeFileUtils {
    private static MakeFileUtils instance = null;

    private MakeFileUtils(){}

    public static synchronized MakeFileUtils getInstance(){
        if(instance == null){
            instance = new MakeFileUtils();
        }
        return instance;
    }

    private  File file;

    //创建文件夹
    public void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    //创建文件
    public File makeFilePath(String filePath, String fileName) {
        file = null;
        createPath(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
