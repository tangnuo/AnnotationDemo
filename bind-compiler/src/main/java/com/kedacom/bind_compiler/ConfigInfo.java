package com.kedacom.bind_compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * @Dec ：对应需要生成某个类的全部相关信息
 * @Author : Caowj
 * @Date : 2018/5/23 13:54
 */
public class ConfigInfo {

    /**
     * 类
     */
    public TypeElement typeElement;
    /**
     * 类注解的值（布局ID）
     */
    public int layoutId;

    public String packageName;
    /**
     * key为id，也就是成员变量注解的值，value为对应的成员变量
     */
    public Map<Integer, VariableElement> mElements = new HashMap<>();

    /**
     * key为id，也就是click方法注解的值，value为对应的click方法
     */
    public Map<Integer, ExecutableElement> mMethods = new HashMap<>();

    /**
     * 新文件的后缀
     */
    public static final String CLASSSUFFIX = "_CAOWJ";

    public String getProxyClassFullName() {
        return typeElement.getQualifiedName().toString() + CLASSSUFFIX;
    }


    public String getClassName() {
        return typeElement.getSimpleName().toString() + CLASSSUFFIX;
    }


    /**
     * 生成新的Java文件
     *
     * @return
     */
    public String generateJavaCode() {
        //方法一：拼接字符串
        //String str = generateJavaCodeByStringBuilder();

        //方法二：采用Javapoet
        String str = generateJavaCodeByJavapoet();

        return str;
    }

    /******************************************************************************/

    /**
     * 采用Javapoet生成新类
     *
     * @return
     */
    private String generateJavaCodeByJavapoet() {
        return generateJavaFile().toString();
    }


    /**
     * 生成javaFile文件
     *
     * @return
     */
    public JavaFile generateJavaFile() {
        ClassName viewClass = ClassName.get("android.view", "View");
        ClassName keepClass = ClassName.get("android.support.annotation", "Keep");
        ClassName clickClass = ClassName.get("android.view", "View.OnClickListener");
        ClassName typeClass = ClassName.get(typeElement.getQualifiedName().toString().replace("." + typeElement.getSimpleName().toString(), ""), typeElement.getSimpleName().toString());

        /**
         * 构建方法
         */
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(typeClass, "host", Modifier.FINAL)
                .addParameter(viewClass, "object", Modifier.FINAL);

        if (layoutId > 0) {
            builder.addStatement("host.setContentView($L)", layoutId);
        }

        /**
         * 遍历添加类成员
         */
        for (int id : mElements.keySet()) {
            VariableElement variableElement = mElements.get(id);
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();
            //这里object如果不为空，则可以传入view等对象
            builder.addStatement("host.$L=($L)object.findViewById($L)", name, type, id);
        }

        /**
         * 声明Listener
         */
//        if (mMethod.size() > 0) {
//            methodBuilder.addStatement("$T listener", TypeUtil.ONCLICK_LISTENER);
//        }


        for (int id : mMethods.keySet()) {
            ExecutableElement executableElement = mMethods.get(id);
            VariableElement variableElement = mElements.get(id);
            String name = variableElement.getSimpleName().toString();

            TypeSpec comparator = TypeSpec.anonymousClassBuilder("")
                    .addSuperinterface(clickClass)
                    .addMethod(MethodSpec.methodBuilder("onClick")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC)
                            .addParameter(viewClass, "view")
                            .addStatement("host.$L(host.$L)", executableElement.getSimpleName().toString(), name)
                            .returns(void.class)
                            .build())
                    .build();
            builder.addStatement("host.$L.setOnClickListener($L)", name, comparator);
        }
        MethodSpec methodSpec = builder.build();

        /**
         * 构建类
         */
        TypeSpec typeSpec = TypeSpec.classBuilder(getClassName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(keepClass)
                .addMethod(methodSpec)
                .build();
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec).build();
        return javaFile;
    }


    /**
     * 采用StringBuilder拼接字符串生成新类
     *
     * @return
     */
    private String generateJavaCodeByStringBuilder() {
        StringBuilder builder = new StringBuilder();
        builder.append("//自动生成的注解类，勿动!!!\n");
        builder.append("package ").append(packageName).append(";\n\n");
//        builder.append("import com.kedacom.bind_api.*;\n");
        builder.append("import android.support.annotation.Keep;\n");
        builder.append("import android.view.View;\n");
        builder.append("import " + typeElement.getQualifiedName() + ";\n");
        builder.append('\n');

        builder.append("@Keep").append("\n");//禁止混淆，否则反射的时候找不到该类
        builder.append("public class ").append(getClassName());
        builder.append(" {\n");

        generateMethod(builder);

        builder.append("}\n");

        return builder.toString();
    }

    /**
     * 生成方法
     *
     * @param builder
     */
    private void generateMethod(StringBuilder builder) {
        builder.append("    public " + getClassName() + "(final " + typeElement.getSimpleName() + " host, View object) {\n");

        if (layoutId > 0) {
            builder.append("        host.setContentView(" + layoutId + ");\n");
        }
        for (int id : mElements.keySet()) {
            VariableElement variableElement = mElements.get(id);
            String name = variableElement.getSimpleName().toString();
            String type = variableElement.asType().toString();

            //这里object如果不为空，则可以传入view等对象
            builder.append("        host." + name).append(" = ");
            builder.append("(" + type + ")object.findViewById(" + id + ");\n");
        }

        for (int id : mMethods.keySet()) {
            ExecutableElement executableElement = mMethods.get(id);
            VariableElement variableElement = mElements.get(id);
            String name = variableElement.getSimpleName().toString();
            builder.append("        host." + name + ".setOnClickListener(new View.OnClickListener(){\n");
            builder.append("            @Override\n");
            builder.append("            public void onClick(View v) {\n");
            builder.append("                host." + executableElement.getSimpleName().toString() + "(host." + name + ");\n");
            builder.append("            }\n");
            builder.append("        });\n");
        }

        builder.append("    }\n");
    }


}
