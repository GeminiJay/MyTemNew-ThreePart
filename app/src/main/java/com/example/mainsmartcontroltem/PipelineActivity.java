package com.example.mainsmartcontroltem;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;

import org.greenrobot.eventbus.EventBus;


public class PipelineActivity extends AppCompatActivity {
    public static BluetoothSocket btSocket;

    private BluetoothAdapter bluetoothAdapter;
    private ArrayAdapter<String> deviceAdapter;
    private List<String> listDevices;
    private ListView listView;
    private LinearLayout btContent;
    private Button searchBT;
    final private static int MESSAGE_READ = 100;
    public String result = "";
    public String sendConmand = "0";
    public sendDataApplication mysendCMD = (sendDataApplication) getApplication();
    String frontCMD = "1";


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipeline);
        ActivityManager.getInstance().addActivity(this);
        listView = this.findViewById(R.id.list);
        btContent = findViewById(R.id.bt_content_llt);
        searchBT = findViewById(R.id.search_btn);

        listDevices = new ArrayList<>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        deviceAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item, listDevices);

        searchBT.setOnClickListener(new BTListener());

        listView.setAdapter(deviceAdapter);
        listView.setOnItemClickListener(new ItemClickListener());//添加监听

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            //下面几行是为了在logcat里面看到搜索到的设备细节，需要的话，可以将注释打开
            /*Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();
            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e("-----" + keyName, String.valueOf(b.get(keyName)));
            }*/

            //搜索设备时，取得设备的MAC地址
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String str = device.getName() + "|" + device.getAddress();
                if (listDevices.indexOf(str) == -1)// 防止重复添加
                    listDevices.add(str); // 获取设备名称和mac地址
                if (deviceAdapter != null) {
                    deviceAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Pipeline Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }




    /**
     * 蓝牙开启与搜索按钮点击监听
     */
    class BTListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.search_btn) {
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "请先开启蓝牙", Toast.LENGTH_SHORT).show();
                } else {
                    btContent.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    if (listDevices != null) {
                        listDevices.clear();
                        if (deviceAdapter != null) {
                            deviceAdapter.notifyDataSetChanged();
                        }
                    }
                    bluetoothAdapter.startDiscovery();
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(receiver, filter);

                }
            }
        }
    }

    /**
     * 蓝牙选项，listview列表点击监听
     */
    class ItemClickListener implements AdapterView.OnItemClickListener {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            if (!bluetoothAdapter.isEnabled()) {
                Toast.makeText(getApplicationContext(), "请先开启蓝牙", Toast.LENGTH_SHORT).show();
            } else {
                bluetoothAdapter.cancelDiscovery();//停止搜索
                String str = listDevices.get(position);
                String macAdress = str.split("\\|")[1];

                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(macAdress);
                try {
                    Method clientMethod = device.getClass()
                            .getMethod("createRfcommSocket", int.class);
                    btSocket = (BluetoothSocket) clientMethod.invoke(device, 1);
                    connect(btSocket);//连接设备

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 连接蓝牙及获取数据
     */
    public void connect(final BluetoothSocket btSocket) {
        try {
            btSocket.connect();//连接
            if (btSocket.isConnected()) {
                Log.i("----connect--- :", "连接成功");
                Toast.makeText(getApplicationContext(), "蓝牙连接成功", Toast.LENGTH_SHORT).show();
                listView.setVisibility(View.GONE);
                btContent.setVisibility(View.VISIBLE);

                new ConnetThread().start();//通信

            } else {
                Log.e("----connect--- :", "连接成功");
                Toast.makeText(getApplicationContext(), "蓝牙连接失败", Toast.LENGTH_SHORT).show();
                btSocket.close();
                listView.setVisibility(View.VISIBLE);
                btContent.setVisibility(View.GONE);
                Log.e("--------- :", "连接关闭");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * 蓝牙通信管理
     */
    private class ConnetThread extends Thread {
        public void run() {

            try {

                InputStream inputStream = btSocket.getInputStream();
                OutputStream outputStream = btSocket.getOutputStream();
                byte[] data = new byte[1024];
                int len;

                while (true) {

                    try {
                        try {
                            Thread.sleep(100);//等待0.5秒，让数据接收完整
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        len = inputStream.read(data);
                        result = new String(data,0,len);
                        //result = String.valueOf(len);//new String(data, 0,len);
                        //result = result.substring(0, len);
                        Message msg = new Message();
                        msg.what = MESSAGE_READ;
                        msg.obj = result;
                        handler.sendMessage(msg);
                        sendConmand = mysendCMD.sendmidCommand + "#";
                        Log.v("发送:", sendConmand);
                        outputStream.write(sendConmand.getBytes(StandardCharsets.UTF_8));

                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }


    }


    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_READ:
                    result = (String) msg.obj;
                    if (EventBus.getDefault().hasSubscriberForEvent(MessageEvent.class)) {
                        EventBus.getDefault().post(new MessageEvent(result));
                    }

                    //Toast.makeText(getApplicationContext(), sendConmand, Toast.LENGTH_SHORT).show();

            }
        }
    };
}
