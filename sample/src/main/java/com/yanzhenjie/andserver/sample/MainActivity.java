package com.yanzhenjie.andserver.sample;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.yanzhenjie.andserver.sample.CoreService;
import com.yanzhenjie.andserver.sample.R;
import com.yanzhenjie.andserver.sample.ServerStatusReceiver;
import com.yanzhenjie.loading.dialog.LoadingDialog;
import com.yanzhenjie.nohttp.tools.NetUtil;


/**
 * Created by Administrator
 */
public class MainActivity extends AppCompatActivity  {

    private Intent mService;
    /**
     * Accept and server status.
     */

    private ServerStatusReceiver mReceiver;
    /**
     * Show message
     */
    private TextView mTvMessage;

    private LoadingDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 窗口全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setContentView(R.layout.activity_main);
        // 显示提供的连接
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        // AndServer run in the service.
        mService = new Intent(this, CoreService.class);
        mReceiver = new ServerStatusReceiver(this);
        mReceiver.register();
        //  开启服务
        showDialog();
        startService(mService);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mReceiver.unRegister();
    }


    /**
     * Start notify.
     */
    public void serverStart() {
        closeDialog();
        String message = getString(R.string.server_start_succeed);

        String ip = NetUtil.getLocalIPAddress();
        if (!TextUtils.isEmpty(ip)) {
            message += ("\nhttp://" + ip + ":9000/\n"
                    + "http://" + ip + ":9000/SaleMachineWebService/Sale\n"
                    + "http://" + ip + ":9000/SaleMachineWebService/IsItemPicked\n"
                    + "http://" + ip + ":9000/SaleMachineWebService/IsDispenserDoorOpen\n"
                    + "http://" + ip + ":9000/SaleMachineWebService/TestBackMotor\n"
                    + "http://" + ip + ":9000/SaleMachineWebService/ResetBackMotor\n"
                    + "http://" + ip + ":9000/SaleMachineWebService/TestElevator\n"
                    + "http://" + ip + ":9000/SaleMachineWebService/ResetElevator\n"
                    + "http://" + ip + ":9000/SaleMachineWebService/ResetLocation\n"
                    + "http://" + ip + ":9000/web/image/image.jpg");
        }
        mTvMessage.setText(message);
    }

    /**
     * Started notify.
     */
    public void serverHasStarted() {
        closeDialog();
        Toast.makeText(this, R.string.server_started, Toast.LENGTH_SHORT).show();
    }

    /**
     * Stop notify.
     */
    public void serverStop() {
        closeDialog();
        mTvMessage.setText(R.string.server_stop_succeed);
    }

    private void showDialog() {
        if (mDialog == null)
            mDialog = new LoadingDialog(this);
        if (!mDialog.isShowing()) mDialog.show();
    }

    private void closeDialog() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }


}
