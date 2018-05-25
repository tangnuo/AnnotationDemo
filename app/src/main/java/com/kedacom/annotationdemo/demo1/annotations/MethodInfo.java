package com.kedacom.annotationdemo.demo1.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Dec ：https://blog.csdn.net/github_35180164/article/details/52118286
 * @Author : Caowj
 * @Date : 2018/5/24 17:46
 */
// 适用方法
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MethodInfo {
    String name() default "long";

    String data();

    int age() default 27;
}
