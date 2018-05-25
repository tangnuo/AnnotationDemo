package com.kedacom.annotationdemo.demo1.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Dec ：https://blog.csdn.net/github_35180164/article/details/52118286
 * @Author : Caowj
 * @Date : 2018/5/24 17:45
 */
// 适用field属性，也包括enum常量
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldInfo {
    int[] value();
}


