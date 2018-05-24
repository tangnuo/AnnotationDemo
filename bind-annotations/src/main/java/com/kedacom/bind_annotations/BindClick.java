package com.kedacom.bind_annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Dec ：
 * @Author : Caowj
 * @Date : 2018/5/23 10:29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface BindClick {
    int[] value();
}


