package com.yanzhenjie.andserver.sample.util;

import java.io.UnsupportedEncodingException;

/**
 * Created by 17710890509 on 2018/3/19.
 */

public class PrinterASCII {

    private static String hexString = "0123456789ABCDEF";
    PrinterASCII(){
    }


    //初始化打印机
    public static byte[] SetInitPrintMachine(){
        byte[] var1;
        (var1 = new byte[2])[0] = 27;
         var1[1] = 64;
        return var1;
    }

    //旋转字体角度   0 不旋转  1 逆时针90  2  逆时针180  3  逆时针270
    public static byte[] SetRotatePrint(int var0){
        byte[] var1;
        (var1 = new byte[3])[0] = 28;
        var1[1] = 73;
        var1[2] = (byte) var0;
        return var1;
    }

    //打印汉字
    public static byte[] PrintString(String var0, int var1) {
        try {
            byte[] var5;
            int var2 = (var5 = var0.getBytes("GB2312")).length;
            if(var1 == 0) {
                ++var2;
            }

            byte[] var3 = new byte[var2];
            //将数组5拷贝到指定数组3
            System.arraycopy(var5, 0, var3, 0, var5.length);

            if(var1 == 0) {
                var3[var2 - 1] = 10;
            }
            return var3;
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
            return null;
        }
    }

    //打印并换行
    public static byte[] SetPrintWrap (){
        byte[] var1;
        (var1 = new byte[1])[0] = 10;
        return var1;
    }

    //设置字体大小


    //设置字间距
    public static byte[] SetWordSpacing(int word){
        byte[] var1;
        (var1 = new byte[3])[0] = 27;
        var1[1] = 32;
        var1[2] = (byte) word;
        return var1;
    }


    //水平放大字符
    public static byte[] SetHorizontallyMagnifiedCharacter (int bigChar) {
        byte[] var1;
        (var1 = new byte[3])[0] = 27;
        var1[1] = 85;
        var1[2] = (byte) bigChar;
        return var1;
    }

    //垂直放大字符
    public static byte[] SetVerticalMagnifiedCharacter(int bigChar){
        byte[] var1;
        (var1 = new byte[3])[0] = 27;
        var1[1] = 86;
        var1[2] = (byte) bigChar;
        return var1;
    }

    //放大字符
    public static byte[] SetVerticalMagnifiedCharacter(int horizontally,int vertical){
        byte[] var1;
        (var1 = new byte[4])[0] = 27;
        var1[1] = 88;
        var1[2] = (byte) horizontally;
        var1[3] = (byte) vertical;
        return var1;
    }

    //设置行间距
    public static byte[] SetRowSpacing(int row){
        byte[] var1;
        (var1 = new byte[3])[0] = 27;
        var1[1] = 49;
        var1[2] = (byte) row;
        return var1;
    }



    //打印并回车
    public static byte[] SetPrintCar(){
        byte[] var1;
        (var1 = new byte[1])[0] = 13;
        return var1;
    }

    //打印并进纸
    public static byte[] SetPrintFeed(int var2){
        byte[] var;
        (var = new byte[3]) [0] = 27;
        var[1] = 74;
        var[2] = (byte) var2;
        return var;
    }

    //打印并进纸n行
    public static byte[] SetPrintFeed_n(int var2){
        byte[] var;
        (var = new byte[3]) [0] = 27;
        var[1] = 100;
        var[2] = (byte) var2;
        return var;
    }

    //设置打印方向 由左到右 1   由右到左 0
    public static byte[] SetPrintDirection(int var0){
         byte[] var1;
         (var1 = new byte[3])[0] = 27;
         var1[1] = 61;
         var1[2] = (byte) var0;
         return var1;
    }

    //设置对齐方式  0 左对齐  1 居中  2  右对齐
    public static byte[] SetAlignment(int var0) {
        byte[] var1;
        (var1 = new byte[3])[0] = 27;
        var1[1] = 97;
        if(var0 > 2) {
            var1[2] = 2;
        } else {
            var1[2] = (byte)var0;
        }

        return var1;
    }


    //取消设置上划线  0  取消
    public static byte[] SetUpLine(int up){
        byte[] var1;
        (var1 = new byte[3])[0] = 27;
        var1[1] = 43;
        var1[2] = (byte) up;
        return var1;
    }

    //取消设置下划线  0  取消
    public static byte[] SetDownLine(int down){
        byte[] var1;
        (var1 = new byte[3])[0] = 27;
        var1[1] = 45;
        var1[2] = (byte) down;
        return var1;
    }
    //设置取消反白打印 white = 1  设置    0  取消
    public static byte[] SetWhitePrint(int white){
        byte[] var1;
        (var1 = new byte[3])[0] = 29;
        var1[1] = 66;
        var1[2] = (byte) white;
        return var1;
    }

    //进入汉字模式
    public static byte[] SetGoChineseMode(){
        byte[] var0;
        (var0 = new byte[2])[0] = 28;
        var0[1] = 38;
        return var0;
    }

    //取消汉字模式
    public static byte[] SetCancelChineseMode(){
        byte[] var0;
        (var0 = new byte[2])[0] = 28;
        var0[1] = 46;
        return var0;
    }


    //走纸换行，
    public static byte[] PrintFeedline(int var0){
        byte[] var1;
        (var1 = new byte[3])[0] = 27;
        var1[1] = 100;
        var1[2] = (byte) var0;
        return var1;
    }

    //再切纸
    public static byte[] PrintCutpaper(int var0){
        byte[] var1 = new byte[3];
        if(var0 != 1){
            var1[0] = 27;
            var1[1] = 105;
        }else{
            var1[0] = 27;
            var1[1] = 109;
        }
        var1[2] = (byte) var0;
        return var1;
    }

    //打印二维码
    public static byte[] SetPrintTwoCode(String str){
        String s = str2HexStr(str);
        String [] arr2 = s.split(" ");
        int length1 = arr2.length;
        byte[] var0;
        (var0 = new byte[6+length1])[0] = 0x1d;
        var0[1] = 0x6b;
        var0[2] = 0x20;
        //版本
        var0[3] = 0x01;
        //纠错等级M
        var0[4] = 0x02;
        for(int i = 0;i<length1;i++){
            String s1 = arr2[i];
            Byte decode = Byte.decode("0x" + s1);
            //byte b = Byte.parseByte(s1);
            var0[i+5] = decode;
        }
        var0[5+length1]  = 0x00;
        return var0;
    }


    //放大二维码
    public static byte[] SetBigTwoCode(int twice){
        byte[] var;
        (var = new byte[3])[0] = 29;
        var[1] = 87;
        var[2] = (byte) twice;
        return var;
    }



    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }




}
