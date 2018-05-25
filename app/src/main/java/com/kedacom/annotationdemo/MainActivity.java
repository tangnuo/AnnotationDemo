package com.kedacom.annotationdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.kedacom.annotationdemo.demo1.Demo1Activity;
import com.kedacom.annotationdemo.demo2.Demo2Activity;
import com.kedacom.annotationdemo.demo3.Demo3Activity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button1, button2, button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        button1 = findViewById(R.id.btn1);
        button2 = findViewById(R.id.btn2);
        button3 = findViewById(R.id.btn3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent mIntent;
        switch (v.getId()) {
            case R.id.btn1:
                mIntent = new Intent();
                mIntent.setClass(this, Demo1Activity.class);
                startActivity(mIntent);
                break;

            case R.id.btn2:
                mIntent = new Intent();
                mIntent.setClass(this, Demo2Activity.class);
                startActivity(mIntent);
                break;
            case R.id.btn3:
                mIntent = new Intent();
                mIntent.setClass(this, Demo3Activity.class);
                startActivity(mIntent);
                break;
        }
    }
}
