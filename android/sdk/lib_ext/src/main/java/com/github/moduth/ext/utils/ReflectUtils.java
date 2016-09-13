/*
 * The GPL License (GPL)
 *
 * Copyright (c) 2016 MarkZhai (http://zhaiyifan.cn)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.moduth.ext.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.github.moduth.ext.component.logger.Logger;

/**
 * 反射工具类，主要处理了各种exception
 * <p>
 * Created by zhaiyifan on 2015/8/3.
 */
public final class ReflectUtils {

    private final static String TAG = "ReflectUtils";

    private ReflectUtils() {
        // static usage.
    }

    /**
     * Get the corresponding field through reflection.
     *
     * @param className Class name.
     * @param fieldName Field name.
     * @param instance  Instance which hold the field, null for static field.
     */
    public static Object getField(String className, String fieldName, Object instance) {
        try {
            return getField(Class.forName(className), fieldName, instance);
        } catch (Throwable e) {
            Logger.i(TAG, "fail to get field " + fieldName + " with " + instance + " from " + className);
        }
        return null;
    }

    /**
     * Get the corresponding field through reflection.
     *
     * @param clazz     Class to reflect.
     * @param fieldName Field name.
     * @param instance  Instance which hold the field, null for static field.
     */
    public static Object getField(Class<?> clazz, String fieldName, Object instance) {
        try {
            Field field = clazz.getField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Throwable e) {
            Logger.i(TAG, "fail to get field " + fieldName + " with " + instance + " from " + clazz);
        }
        return null;
    }

    /**
     * Set the corresponding field through refection.
     *
     * @param className Class name to reflect.
     * @param fieldName Field name.
     * @param instance  Instance which hold the field, null for static field.
     * @param value     Value to set.
     * @return true if set is carried out successfully, false otherwise.
     */
    public static boolean setField(String className, String fieldName, Object instance, Object value) {
        try {
            return setField(Class.forName(className), fieldName, value, instance);
        } catch (Throwable e) {
            Logger.i(TAG, "fail to set field " + fieldName + " to " + value + " with " + instance + " from " + className);
        }
        return false;
    }

    /**
     * Set the corresponding field through refection.
     *
     * @param clazz     Class to reflect.
     * @param fieldName Field name.
     * @param instance  Instance which hold the field, null for static field.
     * @param value     Value to set.
     * @return true if set is carried out successfully, false otherwise.
     */
    public static boolean setField(Class<?> clazz, String fieldName, Object instance, Object value) {
        try {
            Field field = clazz.getField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
            return true;
        } catch (Throwable e) {
            Logger.i(TAG, "fail to set field " + fieldName + " to " + value + " with " + instance + " from " + clazz);
        }
        return false;
    }

    /**
     * Invoke corresponding method through reflection.
     *
     * @param className  Class to reflect.
     * @param methodName Method name.
     * @param instance   Instance which contains the method, null for static method.
     * @param args       Arguments passed to the method.
     * @return the result.
     */
    public static Object invokeMethod(String className, String methodName, Object instance, Object[] args) {
        return invokeMethod(className, methodName, assumeArgumentTypes(args), instance, args);
    }

    /**
     * Invoke corresponding method through reflection.
     *
     * @param clazz      Class to reflect.
     * @param methodName Method name.
     * @param instance   Instance which contains the method, null for static method.
     * @param args       Arguments passed to the method.
     * @return the result.
     */
    public static Object invokeMethod(Class<?> clazz, String methodName, Object instance, Object[] args) {
        return invokeMethod(clazz, methodName, assumeArgumentTypes(args), instance, args);
    }

    /**
     * Invoke corresponding method through reflection.
     *
     * @param className  Class to reflect.
     * @param methodName Method name.
     * @param argsTypes  Arguments types of this method.
     * @param instance   Instance which contains the method, null for static method.
     * @param args       Arguments passed to the method.
     * @return the result.
     */
    public static Object invokeMethod(String className, String methodName, Class<?>[] argsTypes, Object instance, Object[] args) {
        try {
            return invokeMethod(Class.forName(className), methodName, argsTypes, instance, args);
        } catch (Throwable e) {
            Logger.i(TAG, "fail to invoke method " + methodName + " with " + instance + " from " + className);
        }
        return null;
    }

    /**
     * Invoke corresponding method through reflection.
     *
     * @param clazz      Class to reflect.
     * @param methodName Method name.
     * @param argsTypes  Arguments types of this method.
     * @param instance   Instance which contains the method, null for static method.
     * @param args       Arguments passed to the method.
     * @return the result.
     */
    public static Object invokeMethod(Class<?> clazz, String methodName, Class<?>[] argsTypes, Object instance, Object[] args) {
        try {
            Method method = clazz.getMethod(methodName, argsTypes);
            return method.invoke(instance, args);
        } catch (Throwable e) {
            Logger.i(TAG, "fail to invoke method " + methodName + " with " + instance + " from " + clazz);
        }
        return null;
    }

    private static Class<?>[] assumeArgumentTypes(Object[] args) {
        Class<?>[] argsTypes = null;
        if (args != null && args.length > 0) {
            argsTypes = new Class<?>[args.length];
            int i = 0;
            for (Object obj : args) {
                if (obj == null) {
                    throw new IllegalArgumentException("Null argument is not permitted for automatic argument class type assumption.");
                }
                argsTypes[i++] = obj.getClass();
            }
        }
        return argsTypes;
    }
}

