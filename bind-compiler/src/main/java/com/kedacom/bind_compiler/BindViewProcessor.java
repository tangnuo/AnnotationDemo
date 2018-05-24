package com.kedacom.bind_compiler;

import com.google.auto.service.AutoService;
import com.kedacom.bind_annotations.BindClick;
import com.kedacom.bind_annotations.BindId;
import com.kedacom.bind_annotations.BindLayout;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * @Dec ：注解解析器，无需直接使用，通过@AutoService建立关联。
 * @Author : Caowj
 * @Date : 2018/5/23 14:21
 */
@AutoService(Processor.class)
public class BindViewProcessor extends AbstractProcessor {
    /**
     * 文件相关的辅助类
     */
    private Filer mFiler;
    /**
     * 元素相关的辅助类
     */
    private Elements mElementUtils;
    /**
     * 日志相关的辅助类
     */
    private Messager mMessager;

    /**
     * 一个需要生成的类的集合（key为类的全名，value为该类所有相关的需要的信息）
     */
    private Map<String, ConfigInfo> mProxyMap = new HashMap<String, ConfigInfo>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        collectionInfo(roundEnv);
        generateClass();
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //添加支持的注解类型
        Set<String> annotationTypes = new LinkedHashSet<>();
        annotationTypes.add(BindId.class.getCanonicalName());
        annotationTypes.add(BindLayout.class.getCanonicalName());
        annotationTypes.add(BindClick.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        //根据gradle文件中的版本决定
//        return SourceVersion.RELEASE_8;
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    /**
     * 生成代理类
     */
    private void generateClass() {
        for (String key : mProxyMap.keySet()) {
            ConfigInfo proxyInfo = mProxyMap.get(key);

            JavaFileObject sourceFile = null;
            try {
                sourceFile = mFiler.createSourceFile(proxyInfo.getProxyClassFullName(), proxyInfo.typeElement);
                Writer writer = sourceFile.openWriter();
                writer.write(proxyInfo.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                error(proxyInfo.typeElement, "===tb===%s", e.getMessage());
            }


//            方法二：
//            try {
//
//                JavaFile file = proxyInfo.generateJavaFile();
//                file.writeTo(mFiler);
//            }catch (IOException e){
//                error(proxyInfo.typeElement, "===tb===%s", e.getMessage());
//            }
        }
    }

    /**
     * 收集所需生成类的信息
     *
     * @param roundEnvironment
     */
    private void collectionInfo(RoundEnvironment roundEnvironment) {
        //process可能会多次调用，避免生成重复的代理类
        mProxyMap.clear();

        //获得被该注解声明的类
        Set<? extends Element> classElements = roundEnvironment.getElementsAnnotatedWith(BindLayout.class);
        //收集信息
        for (Element element : classElements) {
            if (element.getKind() == ElementKind.CLASS) {
                //获取注解的值
                TypeElement typeElement = (TypeElement) element;
                //类的完整路径
                String qualifiedName = typeElement.getQualifiedName().toString();
                /*获取包名*/
                String packageName = mElementUtils.getPackageOf(typeElement).getQualifiedName().toString();

                BindLayout findId = element.getAnnotation(BindLayout.class);
                if (findId != null) {
                    int value = findId.value();
                    //处理类注解
                    ConfigInfo proxyInfo = mProxyMap.get(qualifiedName);
                    if (proxyInfo == null) {
                        proxyInfo = new ConfigInfo();
                        mProxyMap.put(qualifiedName, proxyInfo);
                    }

                    proxyInfo.layoutId = value;
                    proxyInfo.typeElement = typeElement;
                    proxyInfo.packageName = packageName;
                }
            } else {
                continue;
            }
        }

        //获得被该注解声明的变量
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(BindId.class);

        System.out.print("变量注解的个数：" + elements.size());

        //收集信息
        for (Element element : elements) {
            if (element.getKind() == ElementKind.FIELD) {
                //获取注解的值
                BindId findId = element.getAnnotation(BindId.class);
                if (findId != null) {
                    int value = findId.value();

                    //处理成员变量注解
                    VariableElement variableElement = (VariableElement) element;
                    //这里先要获取上层封装类型，然后强转为TypeElement
                    String qualifiedName = ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
                    ConfigInfo proxyInfo = mProxyMap.get(qualifiedName);
                    if (proxyInfo == null) {
                        proxyInfo = new ConfigInfo();
                        mProxyMap.put(qualifiedName, proxyInfo);
                    }
                    proxyInfo.mElements.put(value, variableElement);
                }
            } else {
                continue;
            }
        }


        //获得被该注解声明的方法
        Set<? extends Element> elementsMethod = roundEnvironment.getElementsAnnotatedWith(BindClick.class);
        System.out.print("方法注解的个数：" + elements.size());
        for (Element element : elementsMethod) {
            if (element.getKind() == ElementKind.METHOD) {
                //获取注解的值
                BindClick onClick = element.getAnnotation(BindClick.class);
                if (onClick != null) {
                    int[] value = onClick.value();

                    if (value != null && value.length > 0) {
                        System.out.print("注解中包含方法的个数：" + value.length);
                        for (int i = 0; i < value.length; i++) {
                            //处理Click方法注解
                            ExecutableElement executableElement = (ExecutableElement) element;
                            //这里先要获取上层封装类型，然后强转为TypeElement
                            String qualifiedName = ((TypeElement) element.getEnclosingElement()).getQualifiedName().toString();
                            ConfigInfo proxyInfo = mProxyMap.get(qualifiedName);
                            if (proxyInfo == null) {
                                proxyInfo = new ConfigInfo();
                                mProxyMap.put(qualifiedName, proxyInfo);
                            }
                            proxyInfo.mMethods.put(value[i], executableElement);
                        }
                    }
                }
            } else {
                continue;
            }
        }
    }

    /**
     * 显示错误信息
     *
     * @param element
     * @param message
     * @param args
     */
    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        mMessager.printMessage(Diagnostic.Kind.ERROR, message, element);
    }
}
