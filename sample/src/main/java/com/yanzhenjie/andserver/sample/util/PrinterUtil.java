package com.yanzhenjie.andserver.sample.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *   Created by Administrator on 2018/2/7.
 */

public class PrinterUtil {
    private static final String TAG = "PrinterUtil";
    //使用volatile保证了多线程访问时instance变量的可见性，避免了instance初始化时其他变量属性还没赋值完时，被另外线程调用
    private static volatile PrinterUtil mPrinter;
    private SerialControl comA;
    private boolean isOpenCom = false;

    public PrinterUtil() {
    }

    /**
     * 单例模式的最佳实现。内存占用地，效率高，线程安全，多线程操作原子性。
     *
     * @return
     */
    public static PrinterUtil getInstance() {
        // 对象实例化时与否判断（不使用同步代码块，instance不等于null时，直接返回对象，提高运行效率）
        if (mPrinter == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (PrinterUtil.class) {
                //未初始化，则初始instance变量
                if (mPrinter == null) {
                    mPrinter = new PrinterUtil();
                }
            }
        }
        return mPrinter;
    }

    public boolean isOpenCom() {
        return isOpenCom;
    }

    /**
     * 初始化串口打印机设备，默认数据：1.串口:/dev/ttyS4; 2.波特率:115200;
     *
     * @return
     */
    public PrinterUtil init() {
        comA = new SerialControl();
        // /dev/ttymxc1
        comA.setPort("/dev/ttymxc5");
        comA.setBaudRate(9600);
        return mPrinter;
    }

    /**
     * 打开串口设备
     *
     * @return
     */
    public boolean OpenComPort() {
        try {
            comA.open();
            isOpenCom = true;
            return isOpenCom;
        } catch (SecurityException e) {
            ToastUtil.showToast("没有读写权限！");
            isOpenCom = false;
            return isOpenCom;
        } catch (IOException e) {
            ToastUtil.showToast("io流错误！");
            isOpenCom = false;
            return isOpenCom;
        } catch (InvalidParameterException e) {
            ToastUtil.showToast("参数错误！");
            isOpenCom = false;
            return isOpenCom;
        }
    }

    /**
     *
     * 关闭串口设备
     */
    public void CloseComPort() {
        if (comA != null) {
            comA.stopSend();
            comA.close();
            isOpenCom = false;
        }
    }
    //测试剧中
    public void printTest1(List<String> dataCargoNumber,int align,String UnitNumber,String NameOfShop,String DiscountPrice,
                           String AmountOfBenefit,String AmountAfterDiscount,
                           String ObviousReceipt,String pay,String makeCollections,String time){
        //,String SalesTax,String findChange,
       // String vip,String Integration,String tel,String Address,  ,String info
        //初始化
        comA.send(PrinterASCII.SetInitPrintMachine());
        //字间距
        comA.send(PrinterASCII.SetWordSpacing(1));
        //行间距
        comA.send(PrinterASCII.SetRowSpacing(6));

        //printTwoCode(info);
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString(time,0));
        comA.send(PrinterASCII.SetAlignment(1));
        /*comA.send(PrinterASCII.PrintString("-----"+"温馨提示，祝您健康"+"-----",0));
        printTest2("本店地址："+Address,0);
        //comA.send(PrinterASCII.SetAlignment(0));
        //comA.send(PrinterASCII.PrintString("本店地址："+Address,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("本店电话："+tel,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("本次积分："+Integration,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("会员号："+vip,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("找零："+findChange,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("营业员："+SalesTax,0));*/
        comA.send(PrinterASCII.PrintString("收款："+makeCollections,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("支付："+pay,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("已收明细："+ObviousReceipt,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("折后金额："+AmountAfterDiscount,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("优惠金额："+AmountOfBenefit,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("折前金额："+DiscountPrice,0));

        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("----------------------------",0));



        printTest(dataCargoNumber, align);
        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("----------------------------",0));

        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("单价"+"       "+"数量"+"       "+"金额",0));

        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("单位"+"       "+"批号"+"       "+"厂家",0));

        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("货号"+"       "+"品名"+"       "+"规格",0));

        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("订单号："+UnitNumber,0));
        printTest2(NameOfShop,1);
        try {
            PrintFeedCutpaper(4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //测试剧中
    public void printTest2(List<String> dataCargoNumber,int align,String UnitNumber,String NameOfShop,String DiscountPrice,
                           String AmountOfBenefit,String AmountAfterDiscount,
                           String ObviousReceipt,String pay,String makeCollections,String time,String info){
        //,String SalesTax,String findChange,
        // String vip,String Integration,String tel,String Address,  ,String info
        //初始化
        comA.send(PrinterASCII.SetInitPrintMachine());
        //字间距
        comA.send(PrinterASCII.SetWordSpacing(1));
        //行间距
        comA.send(PrinterASCII.SetRowSpacing(6));

        printTwoCode(info);
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString(time,0));
        comA.send(PrinterASCII.SetAlignment(1));
        /*comA.send(PrinterASCII.PrintString("-----"+"温馨提示，祝您健康"+"-----",0));
        printTest2("本店地址："+Address,0);
        //comA.send(PrinterASCII.SetAlignment(0));
        //comA.send(PrinterASCII.PrintString("本店地址："+Address,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("本店电话："+tel,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("本次积分："+Integration,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("会员号："+vip,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("找零："+findChange,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("营业员："+SalesTax,0));*/
        comA.send(PrinterASCII.PrintString("收款："+makeCollections,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("支付："+pay,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("已收明细："+ObviousReceipt,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("折后金额："+AmountAfterDiscount,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("优惠金额："+AmountOfBenefit,0));
        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("折前金额："+DiscountPrice,0));

        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("----------------------------",0));



        printTest(dataCargoNumber, align);
        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("----------------------------",0));

        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("单价"+"       "+"数量"+"       "+"金额",0));

        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("单位"+"       "+"批号"+"       "+"厂家",0));

        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.PrintString("货号"+"       "+"品名"+"       "+"规格",0));

        comA.send(PrinterASCII.SetAlignment(0));
        comA.send(PrinterASCII.PrintString("订单号："+UnitNumber,0));
        printTest2(NameOfShop,1);
        try {
            PrintFeedCutpaper(4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试打印，有一定的格式
     * @param data 数据集合
     * @param align 对齐方式
     */
    public void printTest(List<String> data,int align) {
        if (comA.isOpen()) {
            //初始化打印机
            comA.send(PrinterASCII.SetInitPrintMachine());
            //如果内容此行没有填满，        0:左对齐;1:水平居中;2:右对齐
            comA.send(PrinterASCII.SetAlignment(align));
            comA.send(PrinterASCII.SetGoChineseMode());
            //字间距
            comA.send(PrinterASCII.SetWordSpacing(1));
            //行间距
            comA.send(PrinterASCII.SetRowSpacing(5));
            //字体大小
            comA.send(PrinterASCII.SetPrintDirection(1));

            for(int m= 0;m<data.size();m++){
                String str = data.get(m);
                StringBuffer sb = new StringBuffer(str);
                int number = 22;
                for(int i = 0;i<sb.length();i++){
                    if((i+1)*number > sb.length()){
                        int line = sb.length();
                        String sub = sb.substring(i*number,line);
                        comA.send(PrinterASCII.PrintString(sub,0));
                        int length = sub.length();
                        String substring_Str = sb.substring(0, sb.length() - length);
                        //再次倒叙
                        for(int j=0;j<substring_Str.length();j++){
                            if((j+1)*number > substring_Str.length()){
                                int line_d = substring_Str.length();
                                String sub_d = substring_Str.substring(0,line_d - j*number);
                                comA.send(PrinterASCII.PrintString(sub_d,0));
                                break;
                            }else{
                                String substring_d = substring_Str.substring(substring_Str.length() - number * (j + 1), substring_Str.length() - j * number);
                                comA.send(PrinterASCII.PrintString(substring_d,0));
                            }
                        }
                        break;
                    }else{
                        String string = sb.substring(i*number,(i+1)*number);
                        Log.d("s",string);
                    }
                }
            }
            //comA.send(PrinterASCII.SetCancelChineseMode());

           /*comA.send(PrinterASCII.SetPrintFeed_n(4));

            try {
                PrintFeedCutpaper(2);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        } else {
            ToastUtil.showToast("请打开打印机！");
            return;
        }
    }



    public void printTest2(String str,int align) {
        if (comA.isOpen()) {
            //初始化打印机
            comA.send(PrinterASCII.SetInitPrintMachine());
            //如果内容此行没有填满，        0:左对齐;1:水平居中;2:右对齐
            comA.send(PrinterASCII.SetAlignment(align));
            comA.send(PrinterASCII.SetGoChineseMode());
            //字间距
            comA.send(PrinterASCII.SetWordSpacing(1));
            //行间距
            comA.send(PrinterASCII.SetRowSpacing(5));
            //字体大小
            comA.send(PrinterASCII.SetPrintDirection(1));
            StringBuffer sb = new StringBuffer(str);
            int number = 15;
                for(int i = 0;i<sb.length();i++){
                    if((i+1)*number > sb.length()){
                        int line = sb.length();
                        String sub = sb.substring(i*number,line);
                        comA.send(PrinterASCII.PrintString(sub,0));
                        int length = sub.length();
                        String substring_Str = sb.substring(0, sb.length() - length);
                        Log.d("a",substring_Str);
                        comA.send(PrinterASCII.PrintString(substring_Str,0));
                        break;
                    }else{
                        String string = sb.substring(i*number,(i+1)*number);
                        Log.d("a",string);
                        //comA.send(PrinterASCII.PrintString(string,0));
                    }
                }
            }
        }




    /**
     * @param iLine
     * @throws IOException 走纸换行，再切纸，清理缓存
     */
    private void PrintFeedCutpaper(int iLine) throws IOException {
        comA.send(PrinterASCII.PrintFeedline(iLine));
        comA.send(PrinterASCII.PrintCutpaper(0));
        comA.send(PrinterASCII.SetInitPrintMachine());
    }

    private int[] getBitmapParamsData(String imgPath) {
        Bitmap bm = BitmapFactory.decodeFile(imgPath, getBitmapOption(1)); // 将图片的长和宽缩小味原来的1/2
        int width = bm.getWidth();
        int  heigh = bm.getHeight();
        int iDataLen = width * heigh;
        int[] pixels = new int[iDataLen];
        bm.getPixels(pixels, 0, width, 0, 0, width, heigh);
        return pixels;
    }

    // 获取SDCard图片路径
    private String getBitmapPath(String fileName) {
        String imgPath = Environment.getExternalStorageDirectory().getPath() + "/fp" + "fp.jpg";
        return imgPath;
    }

    // BitmapOption 位图选项
    private static BitmapFactory.Options getBitmapOption(int inSampleSize) {
        System.gc();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return options;
    }

    //打印二维码
    public void printTwoCode(String info){
        comA.send(PrinterASCII.SetAlignment(1));
        comA.send(PrinterASCII.SetBigTwoCode(6));
        comA.send(PrinterASCII.SetPrintTwoCode(info));
    }


    private class SerialControl extends SerialHelper {
        public SerialControl() {
        }

        @Override
        protected void onDataReceived(final ComBean ComRecData) {
        }
    }
}
