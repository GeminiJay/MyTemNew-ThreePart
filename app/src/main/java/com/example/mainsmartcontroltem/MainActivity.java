package com.example.mainsmartcontroltem;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import butterknife.ButterKnife;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import com.example.mainsmartcontroltem.view.NoiseboardView;
import static com.example.mainsmartcontroltem.PipelineActivity.btSocket;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener  {
    public String jianData_Vaule;
    public String yaoData_Vaule;
    public String fuData_Vaule;
    public int jianflag = 0;
    public int yaoflag = 0;
    public int fuflag = 0;
    public int allflag = 0;
    public int btflag = 1;
    private long firstTime;
    public String[] midData = {"0","0","0","0"};
    public String[] midDataFront = {"0","0","0","0"};
    Button bt_start;
    BluetoothAdapter mBluetoothAdapter;
    NoiseboardView dashboardView;
    TextView jianvTextValue;
    TextView yaovTextValue;
    TextView fuvTextValue;
    public String sendData = "";
    public float degree = 0.0f;  //记录指针旋转
    public String subString;
    public sendDataApplication myappsmart = (sendDataApplication) getApplication();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityManager.getInstance().addActivity(this);
        Button setTem = findViewById(R.id.backset);
        Button exit = findViewById(R.id.bt_exit);
        bt_start = findViewById(R.id.bt_start);
        Switch Bluetooth = findViewById(R.id.blooth);
        TextView jianvtext = findViewById(R.id.jianvText);
        TextView yaovtext = findViewById(R.id.yaovText);
        TextView fuvtext = findViewById(R.id.fuvText);
        jianvTextValue = findViewById(R.id.jianvTextValue);
        yaovTextValue = findViewById(R.id.yaovTextValue);
        fuvTextValue = findViewById(R.id.fuvTextValue);
        dashboardView = findViewById(R.id.noiseboardView);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bt_start.setOnClickListener(this);
        jianvtext.setOnClickListener(this);
        yaovtext.setOnClickListener(this);
        fuvtext.setOnClickListener(this);
        setTem.setOnClickListener(this);
        exit.setOnClickListener(this);
        Bluetooth.setOnCheckedChangeListener(this);
        if (sendDataApplication.bloothFlag == TRUE){
            Bluetooth.setChecked(TRUE);
        } else {
            Bluetooth.setChecked(FALSE);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_start:
                final TextView jianvtext = findViewById(R.id.jianvText);
                final TextView yaovtext = findViewById(R.id.yaovText);
                final TextView fuvtext = findViewById(R.id.fuvText);
                if (btflag == 1) {
                    bt_start.setText("开");
                    allflag = 1;
                    btflag = 0;
                    sendData = "a0x03";
                    sendDataApplication.sendmidCommand = sendData;
                    Toast.makeText(MainActivity.this, "停止显示实时温度", Toast.LENGTH_SHORT).show();
                } else {
                    bt_start.setText("关");
                    allflag = 0;
                    btflag = 1;
                    sendData = "a0x04";
                    sendDataApplication.sendmidCommand = sendData;
                    Toast.makeText(MainActivity.this, "正在显示实时温度", Toast.LENGTH_SHORT).show();
                }
                String ConfigfilePath = getApplicationContext().getExternalCacheDir().getAbsolutePath()+"/";
                String ConfigName = "SmartControlTemConfig.txt";
                String[] readConfigpartName = setActivity.getFileContent(new File(ConfigfilePath + ConfigName));
                if(!readConfigpartName[0].equals("")){
                    jianvtext.setText(String.format("%s\n实时温度", readConfigpartName[0]));
                } else {
                    jianvtext.setText("已删除肩部，若要设置请重新添加该部位！");
                }
                if(!readConfigpartName[1].equals("")){
                    yaovtext.setText(String.format("%s\n实时温度", readConfigpartName[1]));
                } else {
                    yaovtext.setText("已删除腰部，若要设置请重新添加该部位！");
                }
                if(!readConfigpartName[2].equals("")){
                    fuvtext.setText(String.format("%s\n实时温度", readConfigpartName[2]));
                } else {
                    fuvtext.setText("已删除腹部，若要设置请重新添加该部位！");
                }
                break;
            case R.id.backset:
                Intent intent = new Intent(MainActivity.this, setActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_control:
                sendData = "c0x03";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(MainActivity.this, "正在进行恒温控制", Toast.LENGTH_SHORT).show();
                break;
            case R.id.jianvText:
                jianflag = 1;
                yaoflag = 0;
                fuflag = 0;
                Toast.makeText(MainActivity.this, "显示肩部实时温度", Toast.LENGTH_SHORT).show();
                break;
            case R.id.yaovText:
                jianflag = 0;
                yaoflag = 1;
                fuflag = 0;
                Toast.makeText(MainActivity.this, "显示腰部实时温度", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fuvText:
                jianflag = 0;
                yaoflag = 0;
                fuflag = 1;
                Toast.makeText(MainActivity.this, "显示腹部实时温度", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_exit:
                ActivityManager.getInstance().exit();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.blooth:
                if (isChecked) {
                    Intent intent = new Intent(MainActivity.this, PipelineActivity.class);
                    startActivity(intent);
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                        enable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300); //300秒为蓝牙设备可见时间
                        startActivity(enable);
                        mBluetoothAdapter.enable();//打开蓝牙
                    }
                    sendDataApplication.bloothFlag = TRUE;
                } else {
                    if (btSocket != null) {
                        try {
                            btSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    mBluetoothAdapter.disable();// 关闭蓝牙
                    sendDataApplication.bloothFlag = FALSE;
                }
                break;
        }
    }

    /**
     * 连续按两次退出程序
     */
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime < 3000) {
            ButterKnife.unbind(this);
            finish();
        } else {
            firstTime = System.currentTimeMillis();
            Toast.makeText(this, "连续按两次退出程序", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param messageEvent
     */
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventMain(MessageEvent messageEvent){

        midData = messageEvent.getMessage().split("#");

        for (int i =0;i<midData.length;i++){
            if(Objects.equals(midData[i], midDataFront[i]))
                midData[i] = midDataFront[i];
            if(i==0){
                    jianData_Vaule = midData[i];
                    jianvTextValue.setText(midData[i]+ "℃");
            }
            if(i==1){
                    yaoData_Vaule = midData[i];
                    yaovTextValue.setText(midData[i]+ "℃");
            }
            if(i==2){
                    fuData_Vaule = midData[i];
                    fuvTextValue.setText(midData[i]+ "℃");
            }
            midDataFront[i] = midData[i];
        }

        if (jianflag == 1 && allflag == 1) {
            degree = Float.parseFloat(String.valueOf(jianData_Vaule));//获取到的值
            if (degree > 100) subString = String.valueOf(degree).substring(0, 3);
            else if (degree < 10) subString = String.valueOf(degree).substring(0, 1);
            else subString = String.valueOf(degree).substring(0, 2);
            int integer = Integer.parseInt(subString);
            if (dashboardView != null)
                dashboardView.setRealTimeValue(integer);
        }
        if (yaoflag == 1 && allflag == 1) {
            degree = Float.parseFloat(String.valueOf(yaoData_Vaule));//获取到的值
            if (degree > 100) subString = String.valueOf(degree).substring(0, 3);
            else if (degree < 10) subString = String.valueOf(degree).substring(0, 1);
            else subString = String.valueOf(degree).substring(0, 2);
            int integer = Integer.parseInt(subString);
            if (dashboardView != null)
                dashboardView.setRealTimeValue(integer);
        }
        if (fuflag == 1 && allflag == 1) {
            degree = Float.parseFloat(String.valueOf(fuData_Vaule));//获取到的值
            if (degree > 100) subString = String.valueOf(degree).substring(0, 3);
            else if (degree < 10) subString = String.valueOf(degree).substring(0, 1);
            else subString = String.valueOf(degree).substring(0, 2);
            int integer = Integer.parseInt(subString);
            if (dashboardView != null)
                dashboardView.setRealTimeValue(integer+37);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().hasSubscriberForEvent(MessageEvent.class)) {
            EventBus.getDefault().register(this);
        }
    }
}
