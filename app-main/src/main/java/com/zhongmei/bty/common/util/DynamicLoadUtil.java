package com.zhongmei.bty.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import android.content.Context;
import android.util.Log;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class DynamicLoadUtil {

    private static final String TAG = DynamicLoadUtil.class.getSimpleName();

    private Context context;

    public DynamicLoadUtil(Context context) {
        this.context = context;
    }

    public boolean load(String name) {

        if (copyDex(name) && copyDex("hack.jar")) {
            boolean hasBaseDexClassLoader = true;
            try {
                Class.forName("dalvik.system.BaseDexClassLoader");
            } catch (ClassNotFoundException e) {
                Log.e(TAG, e.getMessage(), e);
                hasBaseDexClassLoader = false;
                return false;
            }

            if (hasBaseDexClassLoader) {
                String dexPath = context.getDir("dex", 0).getAbsolutePath();
                String cachePath = context.getCacheDir().getAbsolutePath();

                DexClassLoader dex2ClassLoader = new DexClassLoader(dexPath + "/hack.jar", cachePath, cachePath,
                        context.getClassLoader());

                combinePathList(dex2ClassLoader);

                DexClassLoader dexClassLoader = new DexClassLoader(dexPath + "/" + name, cachePath, cachePath,
                        context.getClassLoader());

                combinePathList(dexClassLoader);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void combinePathList(ClassLoader loader) {
                PathClassLoader pathLoader = (PathClassLoader) context.getClassLoader();

        try {
                        Field pathListFiled = Class.forName("dalvik.system.BaseDexClassLoader").getDeclaredField("pathList");
            pathListFiled.setAccessible(true);
                        Field dexElementsFiled = Class.forName("dalvik.system.DexPathList").getDeclaredField("dexElements");
            dexElementsFiled.setAccessible(true);
                        Object pathList1 = pathListFiled.get(pathLoader);
                        Object dexElements1 = dexElementsFiled.get(pathList1);

                        Object pathList2 = pathListFiled.get(loader);
                        Object dexElements2 = dexElementsFiled.get(pathList2);
                        Object combineDexElements = combineArray(dexElements2, dexElements1);
                        dexElementsFiled.set(pathList1, combineDexElements);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }

    private boolean copyDex(String name) {
        File dexFile = context.getDir("dex", 0);
        try {
            InputStream localInputStream = context.getAssets().open(name);            FileOutputStream localFileOutputStream = new FileOutputStream(new File(dexFile, name));
            byte[] arrayOfByte = new byte[1024];
                        for (; ; ) {
                int i = localInputStream.read(arrayOfByte);
                if (i == -1) {
                    break;
                }
                localFileOutputStream.write(arrayOfByte, 0, i);
                localFileOutputStream.flush();
            }

            localFileOutputStream.close();
            localInputStream.close();
            return true;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return false;
        }

    }

}
