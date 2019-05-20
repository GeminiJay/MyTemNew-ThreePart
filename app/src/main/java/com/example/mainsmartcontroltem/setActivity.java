package com.example.mainsmartcontroltem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.support.v7.widget.Toolbar;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class setActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener {


    int jianbuflag = 0;
    int yaobuflag = 0;
    int fubuflag = 0;
    int tuibuflag = 0;
    int beibuflag = 0;
    int xiazhibuflag = 0;
    public static String sendData = "";
    public String[] ConfigpartName = {"肩部","腰部","腹部","腿部","背部","下肢"};
    public String ConfigName = "SmartControlTemConfig.txt";
    public String MeumConfigName = "SmartControlTemMeumConfig.txt";
    public String[] readConfigpartName = {"","","","","",""};
    public String ConfigfilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        ActivityManager.getInstance().addActivity(this);
        Button j45 = findViewById(R.id.jianbu45);
        Button j50 = findViewById(R.id.jianbu50);
        Button j55 = findViewById(R.id.jianbu55);
        ToggleButton jswitch = findViewById(R.id.jianbuswitch);
        Button y45 = findViewById(R.id.yaobu45);
        Button y50 = findViewById(R.id.yaobu50);
        Button y55 = findViewById(R.id.yaobu55);
        ToggleButton yswitch = findViewById(R.id.yaobuswitch);
        Button f45 = findViewById(R.id.fubu45);
        Button f50 = findViewById(R.id.fubu50);
        Button f55 = findViewById(R.id.fubu55);
        ToggleButton fswitch = findViewById(R.id.fubuswitch);
        Button t45 = findViewById(R.id.tuibu45);
        Button t50 = findViewById(R.id.tuibu50);
        Button t55 = findViewById(R.id.tuibu55);
        ToggleButton tswitch = findViewById(R.id.tuibuswitch);
        Button b45 = findViewById(R.id.beibu45);
        Button b50 = findViewById(R.id.beibu50);
        Button b55 = findViewById(R.id.beibu55);
        ToggleButton bswitch = findViewById(R.id.beibuswitch);
        Button x45 = findViewById(R.id.xiazhibu45);
        Button x50 = findViewById(R.id.xiazhibu50);
        Button x55 = findViewById(R.id.xiazhibu55);
        ToggleButton xswitch = findViewById(R.id.xiazhibuswitch);
        SeekBar temseekbar = findViewById(R.id.TemseekBar);
        TextView jianbutext = findViewById(R.id.jianbu);
        TextView yaobutext = findViewById(R.id.yaobu);
        TextView fubutext = findViewById(R.id.fubu);
        TextView tuibutext = findViewById(R.id.tuibu);
        TextView beibutext = findViewById(R.id.beibu);
        TextView xiazhibutext = findViewById(R.id.xiazhibu);
        j45.setOnClickListener(this);
        j50.setOnClickListener(this);
        j55.setOnClickListener(this);
        jswitch.setOnCheckedChangeListener(this);
        y45.setOnClickListener(this);
        y50.setOnClickListener(this);
        y55.setOnClickListener(this);
        yswitch.setOnCheckedChangeListener(this);
        f45.setOnClickListener(this);
        f50.setOnClickListener(this);
        f55.setOnClickListener(this);
        fswitch.setOnCheckedChangeListener(this);
        t45.setOnClickListener(this);
        t50.setOnClickListener(this);
        t55.setOnClickListener(this);
        tswitch.setOnCheckedChangeListener(this);
        b45.setOnClickListener(this);
        b50.setOnClickListener(this);
        b55.setOnClickListener(this);
        bswitch.setOnCheckedChangeListener(this);
        x45.setOnClickListener(this);
        x50.setOnClickListener(this);
        x55.setOnClickListener(this);
        xswitch.setOnCheckedChangeListener(this);
        temseekbar.setOnSeekBarChangeListener(this);
        jianbutext.setOnClickListener(this);
        yaobutext.setOnClickListener(this);
        fubutext.setOnClickListener(this);
        tuibutext.setOnClickListener(this);
        beibutext.setOnClickListener(this);
        xiazhibutext.setOnClickListener(this);
        start();
        if (sendDataApplication.jianFlag == TRUE) {
            jswitch.setChecked(TRUE);
        } else {
            jswitch.setChecked(FALSE);
        }
        if (sendDataApplication.yaoFlag == TRUE) {
            yswitch.setChecked(TRUE);
        } else {
            yswitch.setChecked(FALSE);
        }
        if (sendDataApplication.fuFlag == TRUE) {
            fswitch.setChecked(TRUE);
        } else {
            fswitch.setChecked(FALSE);
        }
        if (sendDataApplication.tuiFlag == TRUE) {
            tswitch.setChecked(TRUE);
        } else {
            tswitch.setChecked(FALSE);
        }
        if (sendDataApplication.beiFlag == TRUE) {
            bswitch.setChecked(TRUE);
        } else {
            bswitch.setChecked(FALSE);
        }
        if (sendDataApplication.xiazhiFlag == TRUE) {
            xswitch.setChecked(TRUE);
        } else {
            xswitch.setChecked(FALSE);
        }
        temseekbar.setProgress(sendDataApplication.setTEM);
        Toolbar toolbar = findViewById(R.id.settoolbar);
        setSupportActionBar(toolbar);
        ConfigfilePath = getApplicationContext().getExternalCacheDir().getAbsolutePath()+"/";
        judgeConfigFileExist(ConfigpartName,MeumConfigName);
        judgeConfigFileExist(ConfigpartName,ConfigName);
        ConfigpartName = getFileContent(new File(ConfigfilePath + MeumConfigName));
        dealLayoutView();
    }
    public void dealLayoutView(){
        readConfigpartName = getFileContent(new File(ConfigfilePath + ConfigName));
        final LinearLayout jianpartsetlayout= findViewById(R.id.jianpartsetlayout);
        final LinearLayout yaopartsetlayout= findViewById(R.id.yaopartsetlayout);
        final LinearLayout fupartsetlayout= findViewById(R.id.fupartsetlayout);
        final LinearLayout tuipartsetlayout= findViewById(R.id.tuipartsetlayout);
        final LinearLayout beipartsetlayout= findViewById(R.id.beipartsetlayout);
        final LinearLayout xiazhipartsetlayout= findViewById(R.id.xiazhipartsetlayout);
        final TextView jianbu = findViewById(R.id.jianbu);
        final TextView yaobu = findViewById(R.id.yaobu);
        final TextView fubu = findViewById(R.id.fubu);
        final TextView tuibu = findViewById(R.id.tuibu);
        final TextView beibu = findViewById(R.id.beibu);
        final TextView xiazhibu = findViewById(R.id.xiazhibu);

        if (readConfigpartName[0].equals("")){
            jianpartsetlayout.setVisibility(View.GONE);
        } else {
            jianpartsetlayout.setVisibility(View.VISIBLE);
            jianbu.setText(readConfigpartName[0]);
        }
        if (readConfigpartName[1].equals("")){
            yaopartsetlayout.setVisibility(View.GONE);
        } else {
            yaopartsetlayout.setVisibility(View.VISIBLE);
            yaobu.setText(readConfigpartName[1]);
        }
        if (readConfigpartName[2].equals("")){
            fupartsetlayout.setVisibility(View.GONE);
        } else {
            fupartsetlayout.setVisibility(View.VISIBLE);
            fubu.setText(readConfigpartName[2]);
        }
        if (readConfigpartName[3].equals("")){
            tuipartsetlayout.setVisibility(View.GONE);
        } else {
            tuipartsetlayout.setVisibility(View.VISIBLE);
            tuibu.setText(readConfigpartName[3]);
        }
        if (readConfigpartName[4].equals("")){
            beipartsetlayout.setVisibility(View.GONE);
        } else {
            beipartsetlayout.setVisibility(View.VISIBLE);
            beibu.setText(readConfigpartName[4]);
        }
        if (readConfigpartName[5].equals("")){
            xiazhipartsetlayout.setVisibility(View.GONE);
        } else {
            xiazhipartsetlayout.setVisibility(View.VISIBLE);
            xiazhibu.setText(readConfigpartName[5]);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meum, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_1:
                Toast.makeText(this, "增加部位", Toast.LENGTH_SHORT).show();
                readConfigpartName = getFileContent(new File(ConfigfilePath + ConfigName));
                ConfigpartName = getFileContent(new File(ConfigfilePath + MeumConfigName));
                final ArrayList<Integer> addyourChoices = new ArrayList<>();
                final String[] additems = ConfigpartName;
                final boolean addinitChoiceSets[] = {false,false,false,false,false,false};
                // 设置默认选中的选项，全为false默认均未选中
                addyourChoices.clear();
                AlertDialog.Builder addmultiChoiceDialog =
                        new AlertDialog.Builder(setActivity.this);
                addmultiChoiceDialog.setTitle("请选择要增加部位的名称");
                addmultiChoiceDialog.setMultiChoiceItems(additems, addinitChoiceSets,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    addyourChoices.add(which);
                                } else {
                                    addyourChoices.remove(which);
                                }
                            }
                        });
                addmultiChoiceDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int size = addyourChoices.size();
                                for (int j = 0;j<ConfigpartName.length;j++){
                                    for (int i = 0; i < size; i++) {
                                        if (ConfigpartName[j].equals(additems[addyourChoices.get(i)])){
                                            readConfigpartName[j] = ConfigpartName[j];
                                        }
                                    }
                                }
                                writeConfigToFile(readConfigpartName,ConfigName);
                                dealLayoutView();
                            }
                        });
                addmultiChoiceDialog.show();
                break;
            case R.id.menu_2:
                Toast.makeText(this, "修改部位", Toast.LENGTH_SHORT).show();
                @SuppressLint("InflateParams") View  view = LayoutInflater.from(this).inflate(R.layout.meum_modify,null);
                final EditText jianeditText = view.findViewById(R.id.editText);
                final EditText yaoeditText = view.findViewById(R.id.editText2);
                final EditText fueditText = view.findViewById(R.id.editText3);
                final EditText tuieditText = view.findViewById(R.id.editText4);
                final EditText beieditText = view.findViewById(R.id.editText5);
                final EditText xiazhieditText = view.findViewById(R.id.editText6);
                final TextView jianbutext = findViewById(R.id.jianbu);
                final TextView yaobutext = findViewById(R.id.yaobu);
                final TextView fubutext = findViewById(R.id.fubu);
                final TextView tuibutext = findViewById(R.id.tuibu);
                final TextView beibutext = findViewById(R.id.beibu);
                final TextView xiazhibutext = findViewById(R.id.xiazhibu);
                final TextView modify_jian = view.findViewById(R.id.modify_jian);
                final TextView modify_yao = view.findViewById(R.id.modify_yao);
                final TextView modify_fu = view.findViewById(R.id.modify_fu);
                final TextView modify_tui = view.findViewById(R.id.modify_tui);
                final TextView modify_bei = view.findViewById(R.id.modify_bei);
                final TextView modify_xiazhi = view.findViewById(R.id.modify_xiazhi);
                AlertDialog.Builder modiffy_part =
                        new AlertDialog.Builder(setActivity.this);
                modiffy_part.setTitle("请输入要修改部位的名称").setView(view);
                modiffy_part.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!jianeditText.getText().toString().equals("")){
                                    readConfigpartName[0] = jianeditText.getText().toString();
                                    ConfigpartName[0] = jianeditText.getText().toString();

                                    Toast.makeText(setActivity.this,
                                            "将"+ConfigpartName[0]+"修改为："+jianeditText.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    jianbutext.setText(jianeditText.getText().toString());
                                }
                                if (!yaoeditText.getText().toString().equals("")){
                                    readConfigpartName[1] = yaoeditText.getText().toString();
                                    ConfigpartName[1] = yaoeditText.getText().toString();
                                    Toast.makeText(setActivity.this,
                                            "将"+ConfigpartName[1]+"修改为："+yaoeditText.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    yaobutext.setText(yaoeditText.getText().toString());
                                }
                                if (!fueditText.getText().toString().equals("")){
                                    readConfigpartName[2] = fueditText.getText().toString();
                                    ConfigpartName[2] = fueditText.getText().toString();
                                    Toast.makeText(setActivity.this,
                                            "将"+ConfigpartName[2]+"修改为："+fueditText.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    fubutext.setText(fueditText.getText().toString());
                                }
                                if (!tuieditText.getText().toString().equals("")){
                                    readConfigpartName[3] = tuieditText.getText().toString();
                                    ConfigpartName[3] = tuieditText.getText().toString();
                                    Toast.makeText(setActivity.this,
                                            "将"+ConfigpartName[3]+"修改为："+tuieditText.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    tuibutext.setText(tuieditText.getText().toString());
                                }
                                if (!beieditText.getText().toString().equals("")){
                                    readConfigpartName[4] = beieditText.getText().toString();
                                    ConfigpartName[4] = beieditText.getText().toString();
                                    Toast.makeText(setActivity.this,
                                            "将"+ConfigpartName[4]+"修改为："+beieditText.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    beibutext.setText(beieditText.getText().toString());
                                }
                                if (!xiazhieditText.getText().toString().equals("")){
                                    readConfigpartName[5] = xiazhieditText.getText().toString();
                                    ConfigpartName[5] = xiazhieditText.getText().toString();
                                    Toast.makeText(setActivity.this,
                                            "将"+ConfigpartName[5]+"修改为："+xiazhieditText.getText().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    xiazhibutext.setText(xiazhieditText.getText().toString());
                                }
                                writeConfigToFile(ConfigpartName,MeumConfigName);
                                writeConfigToFile(readConfigpartName,ConfigName);
                                dealLayoutView();
                            }
                        }).show();
                ConfigpartName = getFileContent(new File(ConfigfilePath + MeumConfigName));
                modify_jian.setText(ConfigpartName[0]);
                modify_yao.setText(ConfigpartName[1]);
                modify_fu.setText(ConfigpartName[2]);
                modify_tui.setText(ConfigpartName[3]);
                modify_bei.setText(ConfigpartName[4]);
                modify_xiazhi.setText(ConfigpartName[5]);
                break;
            case R.id.menu_3:
                Toast.makeText(this, "删除部位", Toast.LENGTH_SHORT).show();
                readConfigpartName = getFileContent(new File(ConfigfilePath + ConfigName));
                ConfigpartName = getFileContent(new File(ConfigfilePath + MeumConfigName));
                final ArrayList<Integer> yourChoices = new ArrayList<>();
                final String[] items = ConfigpartName;
                final boolean initChoiceSets[] = {false,false,false,false,false,false};
                // 设置默认选中的选项，全为false默认均未选中
                yourChoices.clear();
                AlertDialog.Builder multiChoiceDialog =
                        new AlertDialog.Builder(setActivity.this);
                multiChoiceDialog.setTitle("请选择要删除部位的名称");
                multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    yourChoices.add(which);
                                } else {
                                    yourChoices.remove(which);
                                }
                            }
                        });
                multiChoiceDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int size = yourChoices.size();
                                for (int j = 0;j<ConfigpartName.length;j++){
                                    for (int i = 0; i < size; i++) {
                                        if (ConfigpartName[j].equals(items[yourChoices.get(i)])){
                                            readConfigpartName[j] = "";
                                        }
                                    }
                                }
                                writeConfigToFile(readConfigpartName,ConfigName);
                                dealLayoutView();
                            }
                        });
                multiChoiceDialog.show();
                break;
        }
        return true;
    }

    public void writeConfigToFile(String[] content, String name){
        String midResult = "";
        for (String aContent : content) {
            midResult += aContent + "#";
            writeTxtToFile(midResult, ConfigfilePath, name);
        }
    }
    @SuppressLint("LongLogTag")
    public void judgeConfigFileExist(String[] s, String name){
        try
        {
            File f=new File(ConfigfilePath+ConfigName);
            if(!f.exists())
            {
                //生成文件夹之后，再生成文件，不然会出错
                makeFilePath(ConfigfilePath, name);
                f.getParentFile().mkdirs();
                f.createNewFile();
                writeConfigToFile(s,name);
            }else if (f.exists()&&f.length()==0){
                while(f.length()==0){
                    writeConfigToFile(s,name);
                }
            }
            else{
                Log.e("Config File Has Existed:", ConfigfilePath+name);
            }

        }
        catch (Exception e)
        {
            Log.e("Creat New Config File Error-->", "Create the file:" + ConfigfilePath + name);
        }
    }
    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent, String filePath, String fileName) {
        String strFilePath = filePath + fileName;
        try {
            File file = new File(strFilePath);
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.write(strcontent.getBytes());
            raf.seek(0);
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    //生成文件

    public void makeFilePath(String filePath, String fileName) {
        makeRootDirectory(filePath);
        try {
            File file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error:", "Error on write File:" + e);
        }
    }

    //生成文件夹

    public static void makeRootDirectory(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.e("error:", e + "");
        }
    }

    //读取指定目录下的所有TXT文件的文件内容
    public static String[] getFileContent(File file) {
        String result[];
        String line = "";
        if (!file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为""文件
                try {
                    RandomAccessFile raf = new RandomAccessFile(file, "rw");
                    //分行读取
                    line = new String(raf.readLine().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                } catch (java.io.FileNotFoundException e) {
                    Log.d("TestFile", "The File doesn't not exist.");
                } catch (IOException e) {
                    Log.d("TestFile", e.getMessage());
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }
        }
        result = line.split("#");
        return result;
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                do {

                } while (true);
            }
        }).start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.jianbu45:
                sendData = "j0x45";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[0]+"45℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.jianbu50:
                sendData = "j0x50";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[0]+"50℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.jianbu55:
                sendData = "j0x55";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[0]+"55℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.yaobu45:
                sendData = "y0x45";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[1]+"45℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.yaobu50:
                sendData = "y0x50";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[1]+"50℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.yaobu55:
                sendData = "y0x55";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[1]+"55℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fubu45:
                sendData = "f0x45";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[2]+"45℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fubu50:
                sendData = "f0x50";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[2]+"50℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fubu55:
                sendData = "f0x55";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[2]+"55℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tuibu45:
                sendData = "t0x45";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[3]+"45℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tuibu50:
                sendData = "t0x50";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[3]+"50℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tuibu55:
                sendData = "t0x55";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[3]+"55℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.beibu45:
                sendData = "b0x45";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[4]+"45℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.beibu50:
                sendData = "b0x50";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[4]+"50℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.beibu55:
                sendData = "b0x55";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[4]+"55℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.xiazhibu45:
                sendData = "x0x45";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[5]+"45℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.xiazhibu50:
                sendData = "x0x50";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[5]+"50℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.xiazhibu55:
                sendData = "x0x55";
                sendDataApplication.sendmidCommand = sendData;
                Toast.makeText(setActivity.this, "设置"+ConfigpartName[5]+"55℃", Toast.LENGTH_SHORT).show();
                break;
            case R.id.jianbu:
                jianbuflag = 1;
                Toast.makeText(setActivity.this, "选择"+ConfigpartName[0], Toast.LENGTH_SHORT).show();
                break;
            case R.id.yaobu:
                yaobuflag = 1;
                Toast.makeText(setActivity.this, "选择"+ConfigpartName[1], Toast.LENGTH_SHORT).show();
                break;
            case R.id.fubu:
                fubuflag = 1;
                Toast.makeText(setActivity.this, "选择"+ConfigpartName[2], Toast.LENGTH_SHORT).show();
                break;
            case R.id.tuibu:
                tuibuflag = 1;
                Toast.makeText(setActivity.this, "选择"+ConfigpartName[3], Toast.LENGTH_SHORT).show();
                break;
            case R.id.beibu:
                beibuflag = 1;
                Toast.makeText(setActivity.this, "选择"+ConfigpartName[4], Toast.LENGTH_SHORT).show();
                break;
            case R.id.xiazhibu:
                xiazhibuflag = 1;
                Toast.makeText(setActivity.this, "选择"+ConfigpartName[5], Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.jianbuswitch:
                if (isChecked) {
                    sendData = "j0x01";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.jianFlag = TRUE;
                    Toast.makeText(setActivity.this, ConfigpartName[0]+"开", Toast.LENGTH_SHORT).show();
                } else {
                    sendData = "j0x02";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.jianFlag = FALSE;
                    Toast.makeText(setActivity.this, ConfigpartName[0]+"关", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.yaobuswitch:
                if (isChecked) {
                    sendData = "y0x01";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.yaoFlag = TRUE;
                    Toast.makeText(setActivity.this, ConfigpartName[1]+"开", Toast.LENGTH_SHORT).show();
                } else {
                    sendData = "y0x02";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.yaoFlag = FALSE;
                    Toast.makeText(setActivity.this, ConfigpartName[1]+"关", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.fubuswitch:
                if (isChecked) {
                    sendData = "f0x01";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.fuFlag = TRUE;
                    Toast.makeText(setActivity.this, ConfigpartName[2]+"开", Toast.LENGTH_SHORT).show();
                } else {
                    sendData = "f0x02";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.fuFlag = FALSE;
                    Toast.makeText(setActivity.this, ConfigpartName[2]+"关", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tuibuswitch:
                if (isChecked) {
                    sendData = "t0x01";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.tuiFlag = TRUE;
                    Toast.makeText(setActivity.this, ConfigpartName[3]+"开", Toast.LENGTH_SHORT).show();
                } else {
                    sendData = "t0x02";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.tuiFlag = FALSE;
                    Toast.makeText(setActivity.this, ConfigpartName[3]+"关", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.beibuswitch:
                if (isChecked) {
                    sendData = "b0x01";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.beiFlag = TRUE;
                    Toast.makeText(setActivity.this, ConfigpartName[4]+"开", Toast.LENGTH_SHORT).show();
                } else {
                    sendData = "b0x02";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.beiFlag = FALSE;
                    Toast.makeText(setActivity.this, ConfigpartName[4]+"关", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.xiazhibuswitch:
                if (isChecked) {
                    sendData = "x0x01";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.xiazhiFlag = TRUE;
                    Toast.makeText(setActivity.this, ConfigpartName[5]+"开", Toast.LENGTH_SHORT).show();
                } else {
                    sendData = "x0x02";
                    sendDataApplication.sendmidCommand = sendData;
                    sendDataApplication.xiazhiFlag = FALSE;
                    Toast.makeText(setActivity.this, ConfigpartName[5]+"关", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int transferdata = (int) (progress * 0.4038 + 37);
        if (jianbuflag == 1 && yaobuflag == 0 && fubuflag == 0 && tuibuflag == 0 && beibuflag == 0 && xiazhibuflag == 0) {
            sendData = "j0x" + transferdata;
            sendDataApplication.sendmidCommand = sendData;
            Toast.makeText(setActivity.this, ConfigpartName[0] + transferdata + "℃", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 1 && fubuflag == 0 && tuibuflag == 0 && beibuflag == 0 && xiazhibuflag == 0) {
            sendData = "y0x" + transferdata;
            sendDataApplication.sendmidCommand = sendData;
            Toast.makeText(setActivity.this, ConfigpartName[1] + transferdata + "℃", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 0 && fubuflag == 1 && tuibuflag == 0 && beibuflag == 0 && xiazhibuflag == 0) {
            sendData = "f0x" + transferdata;
            sendDataApplication.sendmidCommand = sendData;
            Toast.makeText(setActivity.this, ConfigpartName[2] + transferdata + "℃", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 0 && fubuflag == 0 && tuibuflag == 1 && beibuflag == 0 && xiazhibuflag == 0) {
            sendData = "t0x" + transferdata;
            sendDataApplication.sendmidCommand = sendData;
            Toast.makeText(setActivity.this, ConfigpartName[3] + transferdata + "℃", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 0 && fubuflag == 0 && tuibuflag == 0 && beibuflag == 1 && xiazhibuflag == 0) {
            sendData = "b0x" + transferdata;
            sendDataApplication.sendmidCommand = sendData;
            Toast.makeText(setActivity.this, ConfigpartName[4] + transferdata + "℃", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 0 && fubuflag == 0 && tuibuflag == 0 && beibuflag == 0 && xiazhibuflag == 1) {
            sendData = "x0x" + transferdata;
            sendDataApplication.sendmidCommand = sendData;
            Toast.makeText(setActivity.this, ConfigpartName[5] + transferdata + "℃", Toast.LENGTH_SHORT).show();
        }
        sendDataApplication.setTEM = transferdata;
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
//        if(jianbuflag ==1)
//            Toast.makeText(setlayout.this, "肩部开始拖动", Toast.LENGTH_SHORT).show();
//        if(yaobuflag ==1)
//            Toast.makeText(setlayout.this, "腰部开始拖动", Toast.LENGTH_SHORT).show();
//        if(fubuflag ==1)
//            Toast.makeText(setlayout.this, "腹部开始拖动", Toast.LENGTH_SHORT).show();
        //Toast.makeText(setlayout.this, "开始拖动", Toast.LENGTH_SHORT).show();
    }


    public void onStopTrackingTouch(SeekBar seekBar) {
        if (jianbuflag == 1 && yaobuflag == 0 && fubuflag == 0 && tuibuflag == 0 && beibuflag == 0 && xiazhibuflag == 0) {
            jianbuflag = 0;
            Toast.makeText(setActivity.this, ConfigpartName[0]+"温度自定义设置完毕", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 1 && fubuflag == 0 && tuibuflag == 0 && beibuflag == 0 && xiazhibuflag == 0) {
            yaobuflag = 0;
            Toast.makeText(setActivity.this, ConfigpartName[1]+"温度自定义设置完毕", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 0 && fubuflag == 1 && tuibuflag == 0 && beibuflag == 0 && xiazhibuflag == 0) {
            fubuflag = 0;
            Toast.makeText(setActivity.this, ConfigpartName[2]+"温度自定义设置完毕", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 0 && fubuflag == 0 && tuibuflag == 1 && beibuflag == 0 && xiazhibuflag == 0) {
            tuibuflag = 0;
            Toast.makeText(setActivity.this, ConfigpartName[3]+"温度自定义设置完毕", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 0 && fubuflag == 0 && tuibuflag == 0 && beibuflag == 1 && xiazhibuflag == 0) {
            beibuflag = 0;
            Toast.makeText(setActivity.this, ConfigpartName[4]+"温度自定义设置完毕", Toast.LENGTH_SHORT).show();
        }
        if (jianbuflag == 0 && yaobuflag == 0 && fubuflag == 0 && tuibuflag == 0 && beibuflag == 0 && xiazhibuflag == 1) {
            xiazhibuflag = 0;
            Toast.makeText(setActivity.this, ConfigpartName[5]+"温度自定义设置完毕", Toast.LENGTH_SHORT).show();
        }
    }
}