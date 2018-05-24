package com.kedacom.annotationdemo.demo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kedacom.annotationdemo.R;
import com.kedacom.bind_annotations.BindClick;
import com.kedacom.bind_annotations.BindId;
import com.kedacom.bind_annotations.BindLayout;
import com.kedacom.bind_api.BindUtil;

/**
 * 使用自定义注解框架
 */
@BindLayout(R.layout.activity_demo1_test)
public class Demo2Activity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    @BindId(R.id.tv_hint1)
    TextView textView1;
    @BindId(R.id.tv_hint2)
    TextView textView2;
    @BindId(R.id.btn1)
    Button btn1;
    @BindId(R.id.btn2)
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BindUtil.inject(this);
        initData();
    }

    private void initData() {
        textView1.setText("显示数据1111" + TAG);
        textView2.setText("显示数据2222" + TAG);

        btn1.setText("测试按钮1");
        btn2.setText("测试按钮2");
    }

    @BindClick({R.id.btn1, R.id.btn2})
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

