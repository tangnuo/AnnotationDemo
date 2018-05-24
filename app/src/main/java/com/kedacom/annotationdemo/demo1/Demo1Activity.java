package com.kedacom.annotationdemo.demo1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kedacom.annotationdemo.R;

/**
 * 使用反射解析运行时注解
 */
@InjectLayout(R.layout.activity_demo1_test)
public class Demo1Activity extends AppCompatActivity {

    @InjectId(R.id.tv_hint1)
    private TextView textView1;
    @InjectId(R.id.tv_hint2)
    private TextView textView2;
    @InjectId(R.id.btn1)
    private Button btn1;
    @InjectId(R.id.btn2)
    private Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InjectApi.bindId(this);
        initData();
    }

    private void initData() {
        textView1.setText("显示数据1111");
        textView2.setText("显示数据2222");

        btn1.setText("测试按钮1");
        btn2.setText("测试按钮2");
    }

    @InjectClick({R.id.btn1, R.id.btn2})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                Toast.makeText(this, "我是按钮11111", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn2:
                Toast.makeText(this, "我是按钮22222", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
