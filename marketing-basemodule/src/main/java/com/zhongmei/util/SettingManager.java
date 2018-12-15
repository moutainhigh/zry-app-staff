package com.zhongmei.util;

import android.content.Context;
import android.text.TextUtils;

import com.zhongmei.bty.basemodule.commonbusiness.utils.Utils;
import com.zhongmei.bty.basemodule.trade.settings.IPanelItemSettings;
import com.zhongmei.yunfu.context.util.ArgsUtils;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.bty.commonmodule.component.ISettings;
import com.zhongmei.bty.commonmodule.core.annotions.CMD;
import com.zhongmei.bty.commonmodule.core.annotions.COMMIT;
import com.zhongmei.bty.commonmodule.core.annotions.GET;
import com.zhongmei.bty.commonmodule.core.annotions.PUT;
import com.zhongmei.bty.commonmodule.core.annotions.SETTINGS;
import com.zhongmei.bty.commonmodule.core.store.IStore;
import com.zhongmei.bty.commonmodule.core.store.SharedPreferenceStore;
import com.zhongmei.bty.commonmodule.core.store.StoreFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 用于控制全局的配置管理
 */
public class SettingManager {

    private static final String PREFIX = "@Settings@_";

    private static StoreFactory storeFactory;
    private static Map<String, ISettings> pool = new WeakHashMap<>();

    /**
     * 初始化设置管理器，请注意：该方法必须在主线程中调用
     *
     * @param context 上下文环境
     * @throws RuntimeException 在子线程中调用时抛出该异常
     * @deprecated
     */
    public static synchronized void init(Context context) {
        //ArgsUtils.mustInMainThread("");
        if (storeFactory == null) {
            storeFactory = new StoreFactory(context) {
                @Override
                protected IStore onCreate(Context context, String name) {
                    return new SharedPreferenceStore(context, name);
                }
            };
        }
    }

    /**
     * 根据接口类型获取该类型的实现类
     *
     * @param interfaceClassT 设置接口类，请注意：这里必须为接口类型，不能为Class或其它类型
     * @param <T>             设置接口的子类型{@link ISettings}
     * @return 返回目标设置接口类的实现类
     */
    public static <T extends ISettings> T getSettings(Class<T> interfaceClassT) {
        init(BaseApplication.sInstance);
        ArgsUtils.notNull(storeFactory, "Please call init() at first");
        ArgsUtils.notNull(interfaceClassT, "Type of interface is null");
        ArgsUtils.mustInterface(interfaceClassT, "Type of ISetting must is interface");

        if (interfaceClassT == IPanelItemSettings.class) {
            return (T) Utils.getCurrentIpanelItemSettings();
        }

        String name;
        SETTINGS settingsAnnotation = interfaceClassT.getAnnotation(SETTINGS.class);
        if (settingsAnnotation == null) {
            name = interfaceClassT.getName().replace(".", "_");
        } else {
            name = settingsAnnotation.name();
        }
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("The name of ("
                    + interfaceClassT.getName() + ") is empty");
        }
        String fileName = PREFIX + name;

        ISettings iSettings = pool.get(fileName);
        if (iSettings == null) {
            IStore iStore = storeFactory.getStore(fileName);
            if (iStore == null) {
                throw new IllegalArgumentException("Can`t fetch IStore(name=" + name + ")");
            }
            try {
                iSettings = (ISettings) Proxy.newProxyInstance(interfaceClassT.getClassLoader(),
                        new Class[]{interfaceClassT},
                        new SettingsHandler(iStore));
                pool.put(fileName, iSettings);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return (T) iSettings;
    }

    static class SettingsHandler implements InvocationHandler {

        private static Map<String, CMDObject> cmdCache = new WeakHashMap<>();

        final IStore store;

        SettingsHandler(IStore store) {
            this.store = store;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            CMDObject cmdObject = fetchCMDObject(method);
            Object result = doCMD(store, cmdObject, args);

            if (result == null && cmdObject.cmd == CMD.GET) {
                throw new UnknownError("Unknown:" + method);
            }

            return result;
        }

        private static Object doCMD(IStore iStore, CMDObject cmdObject, Object[] args) {
            if (iStore == null || cmdObject == null) {
                return null;
            }
            CMD cmd = cmdObject.cmd;
            switch (cmd) {
                case GET:
                    Class<?> returnType = cmdObject.type;
                    if (int.class == returnType) {
                        return iStore.getInt(cmdObject.key, (Integer) cmdObject.data);
                    } else if (float.class == returnType) {
                        return iStore.getFloat(cmdObject.key, (Float) cmdObject.data);
                    } else if (long.class == returnType) {
                        return iStore.getLong(cmdObject.key, (Long) cmdObject.data);
                    } else if (String.class == returnType) {
                        return iStore.getString(cmdObject.key, (String) cmdObject.data);
                    } else if (boolean.class == returnType) {
                        return iStore.getBoolean(cmdObject.key, (Boolean) cmdObject.data);
                    }
                    break;
                case PUT:
                    if (args != null && args.length == 1) {
                        Object data = args[0];
                        Class<?> parameterType = cmdObject.type;
                        if (int.class == parameterType) {
                            iStore.putInt(cmdObject.key, (Integer) data);
                        } else if (float.class == parameterType) {
                            iStore.putFloat(cmdObject.key, (Float) data);
                        } else if (long.class == parameterType) {
                            iStore.putLong(cmdObject.key, (Long) data);
                        } else if (String.class == parameterType) {
                            iStore.putString(cmdObject.key, (String) data);
                        } else if (boolean.class == parameterType) {
                            iStore.putBoolean(cmdObject.key, (Boolean) data);
                        }
                    }
                    break;
                case COMMIT:
                    iStore.commit();
                    break;
                case NONE:
                    break;
            }
            return null;
        }

        private static CMDObject fetchCMDObject(Method method) {
            if (method == null) {
                return null;
            }
            String methodKey = keyOfMethod(method);
            CMDObject cmdObject = cmdCache.get(methodKey);
            if (cmdObject == null) {
                cmdObject = ofMethod(method);
                cmdCache.put(methodKey, cmdObject);
            }
            return cmdObject;
        }

        private static String keyOfMethod(Method method) {
//            return cls.getName() + method.getName();
            return method.toString();
        }

        private static CMDObject ofMethod(Method method) {
            Annotation[] annotations;
            if (method != null && (annotations = method.getAnnotations()) != null) {
                for (Annotation annotation : annotations) {
                    Class<?> cls = annotation.annotationType();
                    if (GET.class == cls) {
                        GET get = (GET) annotation;
                        CMD cmd = CMD.GET;
                        String key = get.key();
                        Class<?> returnType = method.getReturnType();
                        if (int.class == returnType) {
                            return new CMDObject(cmd, key, returnType, get.defInt());
                        } else if (float.class == returnType) {
                            return new CMDObject(cmd, key, returnType, get.defFloat());
                        } else if (long.class == returnType) {
                            return new CMDObject(cmd, key, returnType, get.defLong());
                        } else if (String.class == returnType) {
                            return new CMDObject(cmd, key, returnType, get.defString());
                        } else if (boolean.class == returnType) {
                            return new CMDObject(cmd, key, returnType, get.defBoolean());
                        }
                        throw new IllegalArgumentException("Can`t find default value for " + method.getName());
                    } else if (PUT.class == cls) {
                        Class<?>[] paramsTypes = method.getParameterTypes();
                        if (paramsTypes == null || paramsTypes.length == 0) {
                            throw new IllegalArgumentException("Can`t find param from " + method.getName());
                        }
                        if (paramsTypes.length > 1) {
                            throw new IllegalArgumentException(method.getName() + " support only one param");
                        }
                        Class<?> returnType = method.getReturnType();
                        if (returnType != void.class) {
                            throw new IllegalArgumentException("Annotation(Put) must return void");
                        }
                        return new CMDObject(CMD.PUT, ((PUT) annotation).key(), paramsTypes[0], null);
                    } else if (COMMIT.class == cls) {
                        Class<?> returnType = method.getReturnType();
                        if (returnType != void.class) {
                            throw new IllegalArgumentException("Annotation(Commit) must return void");
                        }
                        return new CMDObject(CMD.COMMIT, null, null, null);
                    }
                }
            }
            return new CMDObject(CMD.NONE, null, null, null);
        }

        static class CMDObject {

            final CMD cmd;
            final String key;
            final Class<?> type;
            final Object data;

            CMDObject(CMD cmd, String key, Class<?> type, Object data) {
                this.cmd = cmd;
                this.key = key;
                this.type = type;
                this.data = data;
            }
        }
    }
}
