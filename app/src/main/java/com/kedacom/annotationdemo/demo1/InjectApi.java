package com.kedacom.annotationdemo.demo1;

import android.app.Activity;
import android.view.View;

import com.kedacom.annotationdemo.demo1.annotations.InjectClick;
import com.kedacom.annotationdemo.demo1.annotations.InjectId;
import com.kedacom.annotationdemo.demo1.annotations.InjectLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Dec ：
 * @Author : Caowj
 * @Date : 2018/5/23 10:06
 */
public class InjectApi {

    public static void bindId(final Activity obj) {
        //使用反射调用setContentView
        Class<?> cls = obj.getClass();
        if (cls.isAnnotationPresent(InjectLayout.class)) {
            InjectLayout mId = cls.getAnnotation(InjectLayout.class);
            int id = mId.value();
            obj.setContentView(id);
        }


        // 使用反射调用findViewById
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectId.class)) {
                InjectId mId = field.getAnnotation(InjectId.class);
                if (mId == null) {
                    continue;//如果该方法上没有注解，循环下一个
                }
                int viewId = mId.value();
                View view = obj.findViewById(viewId);
                field.setAccessible(true);
                try {
                    field.set(obj, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        // 使用反射调用setOnclick
        Method[] methods = cls.getDeclaredMethods();
        for (final Method method : methods) {
            InjectClick mClick = method.getAnnotation(InjectClick.class);
            if (mClick == null) {
                continue;
            }

            int[] values = mClick.value();
            for (int i = 0; i < values.length; i++) {
                int clickId = values[i];
                final View mView = obj.findViewById(clickId);
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            method.invoke(obj, mView);//反射调用用户设定的方法
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

    }
}
