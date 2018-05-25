package com.kedacom.bind_compiler.util;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * @Dec ：
 * @Author : Caowj
 * @Date : 2018/5/24 16:59
 */
public class ClassValidator {

    /**
     * 判断是否是private修饰
     */
    public static boolean isPrivate(Element annotatedClass) {
        return annotatedClass.getModifiers().contains(Modifier.PRIVATE);

    }

    /**
     * 获取类的完整路径
     */
    static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace(".", "$");
    }

}
