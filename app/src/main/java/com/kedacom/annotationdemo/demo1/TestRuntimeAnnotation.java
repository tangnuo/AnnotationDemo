package com.kedacom.annotationdemo.demo1;

import com.kedacom.annotationdemo.demo1.annotations.ClassInfo;
import com.kedacom.annotationdemo.demo1.annotations.FieldInfo;
import com.kedacom.annotationdemo.demo1.annotations.MethodInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @Dec ：https://blog.csdn.net/github_35180164/article/details/52118286
 * @Author : Caowj
 * @Date : 2018/5/24 17:49
 */
@ClassInfo("Test Class")
public class TestRuntimeAnnotation {

    @FieldInfo(value = {1, 2})
    public String fieldInfo2 = "FiledInfo";

    @FieldInfo(value = {10086})
    public int i = 100;

    @MethodInfo(name = "BlueBird", data = "Big")
    public static String getMethodInfo() {
        return TestRuntimeAnnotation.class.getSimpleName();
    }


    public static void main(String[] a) {
        _testRuntimeAnnotation();
    }

    /**
     * 测试运行时注解
     */
    private static void _testRuntimeAnnotation() {
        StringBuffer sb = new StringBuffer();
        Class<?> cls = TestRuntimeAnnotation.class;
        Constructor<?>[] constructors = cls.getConstructors();

        // 获取指定类型的注解
        sb.append("Class注解：").append("\n");
        ClassInfo classInfo = cls.getAnnotation(ClassInfo.class);
        if (classInfo != null) {
            //修饰符+类名
            sb.append(Modifier.toString(cls.getModifiers())).append(" ")
                    .append(cls.getSimpleName())
                    .append("\n");
            sb.append("注解值: ").append(classInfo.value()).append("\n\n");
        }

        sb.append("Field注解：").append("\n");
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            FieldInfo fieldInfo = field.getAnnotation(FieldInfo.class);
            if (fieldInfo != null) {
                //修饰符+返回类型+字段名
                sb.append(Modifier.toString(field.getModifiers())).append(" ")
                        .append(field.getType().getSimpleName()).append(" ")
                        .append(field.getName()).append("\n");
                sb.append("注解值: ").append(Arrays.toString(fieldInfo.value())).append("\n\n");
            }
        }

        sb.append("Method注解：").append("\n");
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            MethodInfo methodInfo = method.getAnnotation(MethodInfo.class);
            if (methodInfo != null) {
                //修饰符+返回值类型+方法名
                sb.append(Modifier.toString(method.getModifiers())).append(" ")
                        .append(method.getReturnType().getSimpleName()).append(" ")
                        .append(method.getName()).append("\n");
                sb.append("注解值: ").append("\n");
                sb.append("name: ").append(methodInfo.name()).append("\n");
                sb.append("data: ").append(methodInfo.data()).append("\n");
                sb.append("age: ").append(methodInfo.age()).append("\n");
            }
        }

        System.out.print(sb.toString());
    }
}
