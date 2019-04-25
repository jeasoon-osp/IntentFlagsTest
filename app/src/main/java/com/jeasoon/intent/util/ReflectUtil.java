package com.jeasoon.intent.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
public class ReflectUtil {

    private ReflectUtil() {
    }

    public static Object invokeStatic(String clazzName, String methodName) {
        try {
            return invokeStatic(Class.forName(clazzName), methodName, new Class[]{}, new Object[]{});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeStatic(String clazzName, String methodName, Class<?>[] argTypes, Object[] args) {
        try {
            return invokeStatic(Class.forName(clazzName), methodName, argTypes, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeStatic(Class<?> clazz, String methodName) {
        return invokeStatic(clazz, methodName, new Class[]{}, new Object[]{});
    }

    public static Object invokeStatic(Class<?> clazz, String methodName, Class<?>[] argTypes, Object[] args) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, argTypes);
            method.setAccessible(true);
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invoke(Object obj, String methodName) {
        return invoke(obj, methodName, new Class[]{}, new Object[]{});
    }

    public static Object invoke(Object obj, String methodName, Class<?>[] argTypes, Object[] args) {
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName, argTypes);
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object fieldStatic(String clazzName, String fieldName) {
        try {
            return fieldStatic(Class.forName(clazzName), fieldName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object fieldStatic(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void fieldStatic(String clazzName, String fieldName, Object dst) {
        try {
            fieldStatic(Class.forName(clazzName), fieldName, dst);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void fieldStatic(Class<?> clazz, String fieldName, Object dst) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(null, dst);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object field(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void field(Object obj, String fieldName, Object dst) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, dst);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T, K> K newProxy(T base, Class<K> interfaceClass) {
        return newProxy(base, interfaceClass, new DefaultMethodConvert<T, K>(), new DefaultProxyCallback<T, K>());
    }

    public static <T, K> K newProxy(T base, Class<K> interfaceClass, MethodConverter methodConverter) {
        return newProxy(base, interfaceClass, methodConverter, new DefaultProxyCallback<T, K>());
    }

    public static <T, K> K newProxy(T base, Class<K> interfaceClass, ProxyCallback<T, K> proxyCallback) {
        return newProxy(base, interfaceClass, new DefaultMethodConvert<T, K>(), proxyCallback);
    }

    public static <T, K> K newProxy(T base, Class<K> interfaceClass, MethodConverter methodConverter, ProxyCallback<T, K> proxyCallback) {
        return (K) Proxy.newProxyInstance(ReflectUtil.class.getClassLoader(), new Class[]{interfaceClass}, new ObjectProxy<>(base, methodConverter, proxyCallback));
    }

    public interface MethodConverter<T, K> {
        Method convert(T base, K interfaceProxy, Method interfaceMethod, Object[] args) throws Throwable;
    }

    public interface ProxyCallback<T, K> {

        /**
         * @return 如果返回true, 代表拦截, 则直接返回null, 不会调用本体函数
         */
        boolean onBefore(T base, K interfaceProxy, Method interfaceMethod, Method realMethod, Object[] args) throws Throwable;

        Object onInvoke(T base, K interfaceProxy, Method interfaceMethod, Method realMethod, Object[] args) throws Throwable;

        Object onReturning(T base, K interfaceProxy, Method interfaceMethod, Method realMethod, Object[] args, Object result) throws Throwable;

        /**
         * @return 如果返回true, 代表拦截, 则直接返回null, 此时已调用过本体函数
         */
        boolean onAfter(T base, K interfaceProxy, Method interfaceMethod, Method realMethod, Object[] args, Object result) throws Throwable;

    }

    public static class DefaultMethodConvert<T, K> implements MethodConverter<T, K> {

        @Override
        public Method convert(T base, K interfaceProxy, Method interfaceMethod, Object[] args) throws Throwable {
            Method baseMethod = getMethod(base.getClass(), interfaceMethod.getName(), interfaceMethod.getParameterTypes());
            baseMethod.setAccessible(true);
            return baseMethod;
        }

        private Method getMethod(Class<?> baseClass, String name, Class<?>[] paramTypes) throws Throwable {
            if (baseClass == null) {
                throw new NoSuchMethodException("Can't find method " + name + "(" + Arrays.toString(paramTypes) + ")" + " in " + baseClass);
            }
            try {
                return baseClass.getDeclaredMethod(name, paramTypes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getMethod(baseClass.getSuperclass(), name, paramTypes);
        }

    }

    public static class DefaultProxyCallback<T, K> implements ProxyCallback<T, K> {

        @Override
        public boolean onBefore(T base, K interfaceProxy, Method interfaceMethod, Method realMethod, Object[] args) throws Throwable {
            return false;
        }

        @Override
        public Object onInvoke(T base, K interfaceProxy, Method interfaceMethod, Method realMethod, Object[] args) throws Throwable {
            return realMethod.invoke(base, args);
        }

        @Override
        public Object onReturning(T base, K interfaceProxy, Method interfaceMethod, Method realMethod, Object[] args, Object result) throws Throwable {
            return result;
        }

        @Override
        public boolean onAfter(T base, K interfaceProxy, Method interfaceMethod, Method realMethod, Object[] args, Object result) throws Throwable {
            return false;
        }
    }

    private static class ObjectProxy<T, K> implements InvocationHandler {

        private T                     mBase;
        private ProxyCallback<T, K>   mProxyCallback;
        private MethodConverter<T, K> mMethodConverter;
        private Map<Method, Method>   mMethodMap;

        private ObjectProxy(T base, MethodConverter<T, K> methodConverter, ProxyCallback<T, K> proxyCallback) {
            mBase = base;
            mProxyCallback = proxyCallback;
            mMethodConverter = methodConverter;
            mMethodMap = new ConcurrentHashMap<>();
        }

        @Override
        public Object invoke(Object interfaceProxy, Method interfaceMethod, Object[] args) throws Throwable {
            Method cachedMethod = mMethodMap.get(interfaceMethod);
            if (cachedMethod == null) {
                synchronized (interfaceMethod) {
                    cachedMethod = mMethodMap.get(interfaceMethod);
                    if (cachedMethod == null) {
                        if (mMethodConverter != null) {
                            cachedMethod = mMethodConverter.convert(mBase, (K) interfaceProxy, interfaceMethod, args);
                        } else {
                            cachedMethod = interfaceMethod;
                        }
                        mMethodMap.put(interfaceMethod, cachedMethod);
                    }
                }
            }
            Method realMethod = cachedMethod;
            if (mProxyCallback == null) {
                return realMethod.invoke(mBase, args);
            }
            if (mProxyCallback.onBefore(mBase, (K) interfaceProxy, interfaceMethod, realMethod, args)) {
                return null;
            }
            Object result;
            result = mProxyCallback.onInvoke(mBase, (K) interfaceProxy, interfaceMethod, realMethod, args);
            result = mProxyCallback.onReturning(mBase, (K) interfaceProxy, interfaceMethod, realMethod, args, result);
            if (mProxyCallback.onAfter(mBase, (K) interfaceProxy, interfaceMethod, realMethod, args, result)) {
                return null;
            }
            return result;
        }

    }

}
