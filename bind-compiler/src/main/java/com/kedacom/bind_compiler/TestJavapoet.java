package com.kedacom.bind_compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;

import javax.lang.model.element.Modifier;

/**
 * 学习Javapoet
 * Created by mingwei on 12/21/16.
 */
public class TestJavapoet {
    public static void main(String[] arge) {
        //定义方法
        MethodSpec methodmain = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "arge")
                .addStatement("$T.out.print($S)", System.class, "hello javapoet")
                .build();

        //定义类
        TypeSpec typeMain = TypeSpec.classBuilder("MainPP")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(methodmain)
                .build();

        //创建Java文件
        JavaFile javaFile = JavaFile.builder("com.kedacom.japdemo", typeMain).build();

        try {
            //在控制台看新建的类
            javaFile.writeTo(System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
