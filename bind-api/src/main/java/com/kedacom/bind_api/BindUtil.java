package com.kedacom.bind_api;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Constructor;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Dec ：
 * @Author : Caowj
 * @Date : 2018/5/23 15:21
 */
public class BindUtil {

    /**
     * 用来缓存反射出来的类，节省每次都去反射引起的性能问题
     */
    static final Map<Class<?>, Constructor<?>> BIND_MAP = new LinkedHashMap<>();

    public static void inject(Activity o) {
        inject(o, o.getWindow().getDecorView());
    }

    public static void inject(Activity host, View root) {
        String classFullName = host.getClass().getName() + "_CAOWJ";
        try {
            Constructor constructor = BIND_MAP.get(host.getClass());
            if (constructor == null) {
                Class proxy = Class.forName(classFullName);
                constructor = proxy.getDeclaredConstructor(host.getClass(), View.class);
                BIND_MAP.put(host.getClass(), constructor);
            }
            constructor.setAccessible(true);
            constructor.newInstance(host, root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
