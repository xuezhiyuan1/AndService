package com.yanzhenjie.andserver.sample;

/**
 * Created by Administrator
 */

public class ParamsSettingUtil {

    //复位命令
    //7E,0A 00 01 22 00 00 01 AC ED
    public static final  byte [] SEND_DATA_RESET = {(byte) 0x7E, 0x0A, 0x00,
            (byte) 0x01,0x22,0x00,0x00,0x01,(byte)172, (byte) 0xED};

    //7E 0B 00 01 36 00 01 02 01 C4 ED
    //前置电机测试
    public static final  byte [] SEND_DATA_FRONT_TEXT = {(byte) 0x7E, 0x0B, 0x00,
            (byte) 0x01,0x36,0x00,0x01,0x02,0x01,(byte)0xC4, (byte) 0xED};

    //后置电机测试
    //后置电机测试
    //7E 0E 00 01 35 00 02 02 01 03 01 25 F0 ED
    public static final  byte [] SEND_DATA_BEHIND_TEXT = {(byte) 0x7E, 0x0E, 0x00,
            (byte) 0x01,0x35,0x00,0x02,0x02,0x01,0x03,0x01,0x25,(byte)0xF0, (byte) 0xED};

    //前置电机归位
    //7E 09 00 01 39 00 01 C2 ED
    public static final  byte [] SEND_DATA_FRONT_HOMING = {(byte) 0x7E, 0x09, 0x00,
            (byte) 0x01,0x39,0x00,0x01,(byte)0xC2, (byte) 0xED};

    //后置点击归位
    //7E 0A 00 01 38 00 02 03 C6 ED
    public static final  byte [] SEND_DATA_BEHIND_HOMING = {(byte) 0x7E, 0x0A, 0x00,
            (byte) 0x01,0x38,0x00,0x02,0x03,(byte)0xC6, (byte) 0xED};

    //获取当前设备状态   出错
    //7E 0A 00 01 11 00 00 01 9B ED
    public static final  byte [] SEND_DATA_SHOP_STATE = {(byte) 0x7E, 0x0A, 0x00,
            (byte) 0x01,0x11,0x00,0x00,0x01,(byte)0x9B, (byte) 0xED};

    //出货指令
    //7E 11 00 01 37 00 01 02     01 02 02 21 03 01 22     16 ED
    public static final  byte [] SEND_DATA_SHOP_ORDER = {(byte) 0x7E, 0x11, 0x00,
            (byte) 0x01,0x37,0x00,0x01,0x02,0x01,0x02,0x02,0x21,0x03,0x01,0x22,(byte)0x16, (byte) 0xED};

    //门锁开关  开门  7E 09 00 01 3A 00 01 C3 ED
    public static  final byte [] SEND_DOOR_LOCK_OPEN = {(byte)0x7E, 0x09, 0x00, 0x01,
            (byte) 0x3A,0x00,0x01, (byte) 0xC3, (byte) 0xED,};

    //门锁开关  关门  7E 09 00 01 3A 00 00 C2 ED
    public static  final byte [] SEND_DOOR_LOCK_CLOSE = {(byte)0x7E, 0x09, 0x00, 0x01,
            (byte) 0x3A,0x00,0x00, (byte) 0xC2, (byte) 0xED,};
}
