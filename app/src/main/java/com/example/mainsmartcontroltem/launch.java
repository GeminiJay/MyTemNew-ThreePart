package com.example.mainsmartcontroltem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import java.io.InputStream;

public class launch extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 1500; // 两秒后进入系统

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ActivityManager.getInstance().addActivity(this);
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(launch.this,
                        MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }

        }, SPLASH_DISPLAY_LENGHT);

    }
    @SuppressLint("ResourceType")
    @Override
    protected void onResume() {
        super.onResume();
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_launch);
        InputStream is;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inSampleSize = 2;
        is = getResources().openRawResource(R.drawable.t);
        Bitmap bm = BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd = new BitmapDrawable(getResources(), bm);
        layout.setBackgroundDrawable(bd);
    }
}
