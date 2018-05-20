/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.andserver.sample;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.sample.response.RequestAisleStateHandler;
import com.yanzhenjie.andserver.sample.response.RequestDoorLockHandler;
import com.yanzhenjie.andserver.sample.response.RequestDriverMotorStateHandler;
import com.yanzhenjie.andserver.sample.response.RequestFrontMotorHomingHandler;
import com.yanzhenjie.andserver.sample.response.RequestImageHandler;
import com.yanzhenjie.andserver.sample.response.RequestLeftTemperatureController;
import com.yanzhenjie.andserver.sample.response.RequestMachineStateHandler;
import com.yanzhenjie.andserver.sample.response.RequestOpenDoorHandler;
import com.yanzhenjie.andserver.sample.response.RequestPrintCodeTextHandler;
import com.yanzhenjie.andserver.sample.response.RequestPrintStringTextHandler;
import com.yanzhenjie.andserver.sample.response.RequestRearMotorHomingHandler;
import com.yanzhenjie.andserver.sample.response.RequestResetLocationHandler;
import com.yanzhenjie.andserver.sample.response.RequestRightTemperatureController;
import com.yanzhenjie.andserver.sample.response.RequestShipMentStateHandler;
import com.yanzhenjie.andserver.sample.response.RequestShipmentHandler;
import com.yanzhenjie.andserver.sample.response.RequestShopDoorWhetherOpenHandler;
import com.yanzhenjie.andserver.sample.response.RequestShopWhetherTakeHandler;
import com.yanzhenjie.andserver.sample.response.RequestSlideMotorStateHandler;
import com.yanzhenjie.andserver.sample.response.RequestTakeFrontMotorStateHandler;
import com.yanzhenjie.andserver.sample.response.RequestTakeRearMotorStateHandler;
import com.yanzhenjie.andserver.sample.response.RequestTakeTheWarehouseStateHandler;
import com.yanzhenjie.andserver.sample.response.RequestTestFrontMotorHandler;
import com.yanzhenjie.andserver.sample.response.RequestTestPostpositionMotorHandler;
import com.yanzhenjie.andserver.website.AssetsWebsite;
/**
 * <p>Server service.</p>
 * Created by Administrator
 */
public class CoreService extends Service {

    /**
     * AndServer.
     */
    private Server mServer;
    private AssetManager mAssetManager;

    @Override
    public void onCreate() {
        mAssetManager = getAssets();
        AndServer andServer = new AndServer.Build()
                .port(9000)
                .timeout(10 * 1000)
                //出货接口
                .registerHandler("/SaleMachineWebService/Sale",new RequestShipmentHandler())
                .website(new AssetsWebsite(mAssetManager, "web"))
                //判断货物是否被取走
                .registerHandler("/SaleMachineWebService/IsItemPicked",new RequestShopWhetherTakeHandler())
                //判断货舱门是否开启
                .registerHandler("/SaleMachineWebService/IsDispenserDoorOpen",new RequestShopDoorWhetherOpenHandler())
                //测试后置电机
                .registerHandler("/SaleMachineWebService/TestBackMotor",new RequestTestPostpositionMotorHandler())
                //后置电机归位
                .registerHandler("/SaleMachineWebService/ResetBackMotor",new RequestRearMotorHomingHandler())
                //测试前置电机
                .registerHandler("/SaleMachineWebService/TestElevator",new RequestTestFrontMotorHandler())
                //前置电机归位
                .registerHandler("/SaleMachineWebService/ResetElevator",new RequestFrontMotorHomingHandler())
                //复位
                .registerHandler("/SaleMachineWebService/ResetLocation",new RequestResetLocationHandler())
                //获取设备状态
                .registerHandler("/SaleMachineWebService/MachineState",new RequestMachineStateHandler())
                //获取货道状态
                .registerHandler("/SaleMachineWebService/AisleState",new RequestAisleStateHandler())
                //获取货仓状态
                .registerHandler("/SaleMachineWebService/TakeTheWarehouseState",new RequestTakeTheWarehouseStateHandler())
                //获取前置电机状态
                .registerHandler("/SaleMachineWebService/TakeFrontMotorState",new RequestTakeFrontMotorStateHandler())
                //获取后置电机状态
                .registerHandler("/SaleMachineWebService/TakeRearMotorState",new RequestTakeRearMotorStateHandler())
                //获取滑道电机状态
                .registerHandler("/SaleMachineWebService/SlideMotorState",new RequestSlideMotorStateHandler())
                //获取推手电机状态
                .registerHandler("/SaleMachineWebService/DriverMotorState",new RequestDriverMotorStateHandler())
                //获取本次出货状态
                .registerHandler("/SaleMachineWebService/ShipMentState",new RequestShipMentStateHandler())
                //关门
                .registerHandler("/SaleMachineWebService/DoorCloseState",new RequestDoorLockHandler())
                //开门
                .registerHandler("/SaleMachineWebService/DoorOpenState",new RequestOpenDoorHandler())
                //打印
                .registerHandler("/SaleMachineWebService/PrintStringText",new RequestPrintStringTextHandler())
                //左机柜温湿度
                .registerHandler("/SaleMachineWebService/LeftTemperatureController",new RequestLeftTemperatureController())
                //右机柜温湿度
                .registerHandler("/SaleMachineWebService/RightTemperatureController",new RequestRightTemperatureController())
                //打印二维码
                .registerHandler("/SaleMachineWebService/RequestPrintCodeTextHandler",new RequestPrintCodeTextHandler())
                //图片上传
                .registerHandler("/SaleMachineWebService/image",new RequestImageHandler())
                .listener(mListener)
                .build();
        // Create server.
        mServer = andServer.createServer();
    }

    /**
     * Server listener.
     */
    private Server.Listener mListener = new Server.Listener() {
        @Override
        public void onStarted() {
            ServerStatusReceiver.serverStart(CoreService.this);
        }

        @Override
        public void onStopped() {
            ServerStatusReceiver.serverStop(CoreService.this);
        }

        @Override
        public void onError(Exception e) {
            // Ports may be occupied.
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startServer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer(); // Stop server.
        if (mAssetManager != null)
            mAssetManager.close();
    }

    /**
     * Start server.
     */
    private void startServer() {
        if (mServer != null) {
            if (mServer.isRunning()) {
                ServerStatusReceiver.serverHasStarted(CoreService.this);
            } else {
                mServer.start();
            }
        }
    }

    /**
     * Stop server.
     */
    private void stopServer() {
        if (mServer != null) {
            mServer.stop();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
