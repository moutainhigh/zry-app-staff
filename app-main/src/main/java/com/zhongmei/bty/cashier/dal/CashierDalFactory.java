package com.zhongmei.bty.cashier.dal;

import android.content.Context;
import android.os.Looper;

import com.zhongmei.yunfu.context.util.ArgsUtils;
import com.zhongmei.bty.cashier.dal.impl.db.DBDal;
import com.zhongmei.bty.cashier.dal.impl.net.NetDal;
import com.zhongmei.bty.cashier.exception.OperateException;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.WeakHashMap;

/**
 * Created by demo on 2018/12/15
 * <p>
 * <p>由于com.zhongmei.bty.data.operates.OperatesFactory包含了目前工程中所有的Dal和Operate的封装，
 * 量将会特别大，所以把Cashier(快餐)的单独放到该类中<p/>
 * <p>
 * <p>注意:所有Dal必须在子线程中调用，否则会抛异常</p>
 *
 * @see OperatesFactory
 */
public class CashierDalFactory {

    private static HashMap<Class<? extends Dal>, Class<? extends BaseDal>> support = new HashMap<>();
    private static WeakHashMap<Class<? extends Dal>, Dal> dalPool = new WeakHashMap<>();

    static {
//        support.put(OrderCenterDal.class, OrderCenterDBDalImpl.class);
    }

    @Deprecated
    public static <T extends IOperates> T adapter(IOperates.ImplContext implContext, Class<T> tClass) throws OperateException {
        Class<? extends Dal> dalClass = tClass.asSubclass(Dal.class);
        return (T) obtain(implContext.getContext(), dalClass);
    }

    public static synchronized <T extends Dal> T obtain(Context context, Class<T> tClass) {
        ArgsUtils.notNull(context, "Context");
        ArgsUtils.notNull(tClass, "Class of Dal");

        Class<? extends BaseDal> destCls = support.get(tClass);

        if (destCls == null) {
            throw new OperateException("Can`t support Dal:" + tClass);
        }

        try {
            Dal dal = dalPool.get(destCls);
            if (dal == null) {
                BaseDal baseDal = destCls.newInstance();
                baseDal.onCreate(context);

                dal = (Dal) Proxy.newProxyInstance(tClass.getClassLoader(),
                        new Class[]{tClass}, new InnerDBDalProxy(baseDal));

                dalPool.put(destCls, dal);
            }

            return (T) dal;
        } catch (OperateException e) {
            throw new OperateException(e);
        } catch (IllegalAccessException e) {
            throw new OperateException(e);
        } catch (InstantiationException e) {
            throw new OperateException(e);
        }
    }

    private static class InnerDBDalProxy implements InvocationHandler {

        final BaseDal impl;
        final boolean mustAsync;

        InnerDBDalProxy(BaseDal impl) {
            this(impl, true);
        }

        InnerDBDalProxy(BaseDal impl, boolean mustAsync) {
            this.impl = impl;
            this.mustAsync = mustAsync;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (mustAsync && isMainThread()) {
                throw new RuntimeException("Dal can`t support invoke on main-thread");
            }
            if (impl instanceof DBDal) {
                return invokeDb(proxy, (DBDal) impl, method, args);
            } else if (proxy instanceof NetDal) {
                return invokeNet(proxy, (NetDal) impl, method, args);
            } else {
                throw new UnsupportedOperationException("Unsupported proxy:" + method);
            }
        }

        private static Object invokeDb(Object proxy, DBDal dbDal, Method method, Object[] args) throws Throwable {
            dbDal.prepare();
            Object object = method.invoke(dbDal, args);
            dbDal.release();
            return object;
        }

        private static Object invokeNet(Object proxy, NetDal netDal, Method method, Object[] args) throws Throwable {
            return method.invoke(netDal, args);
        }

        private static boolean isMainThread() {
            return Looper.getMainLooper() == Looper.myLooper();
        }
    }
}
