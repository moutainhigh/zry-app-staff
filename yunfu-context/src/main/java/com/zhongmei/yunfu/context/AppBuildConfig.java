package com.zhongmei.yunfu.context;

import java.lang.reflect.Field;

/**
 * Created by demo on 2018/12/15
 */

public final class AppBuildConfig {

    public static class BuildType {
        public static final int RELEASE = 0;
        public static final int DEVELOP = 1;
        public static final int DEBUG = 2;
        public static final int GLDERP = 3;
        public static final int CI = 4;
        public static final int SINGAPORE = 5;
    }

    public static boolean DEBUG = Boolean.parseBoolean("true");
    public static String BUILD_TYPE = "debug";
    public static int VERSION_CODE = -1;
    public static String VERSION_NAME = "";
    public static int MY_BUILD_TYPE = 0;
    public static boolean ALLOW_COMPRESS = false;
    public static Boolean OFFLINE_SUPPORT = false;
    public static final boolean CUSTOMIZATION_TICKET_SYSTEM = true;
    public static final boolean INNER_AD_MODULE = true;

    public static void init(Class<?> buildConfigClass) {
        Field[] fields = buildConfigClass.getDeclaredFields();
        for (Field field : fields) {
            if (hasField(field.getName())) {
                try {
                    field.setAccessible(true);
                    setValue(AppBuildConfig.class, field.getName(), field.get(null));
                } catch (Exception e) {
                    System.err.println("AppBuildConfig: " + e.getMessage());
                }
            }
        }
    }

    private static boolean hasField(String fieldName) {
        Field[] fields = AppBuildConfig.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }

    private static void setValue(Class<?> clazz, String fieldName, Object newValue) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, newValue);
    }

    public static boolean hasBuildType(int... buildType) {
        if (buildType != null) {
            for (int type : buildType) {
                if (type == MY_BUILD_TYPE) {
                    return true;
                }
            }
        }
        return false;
    }

}
