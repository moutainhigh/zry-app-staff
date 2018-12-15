package com.zhongmei.yunfu.orm;

import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.atask.DefaultThreadFactory;
import com.zhongmei.yunfu.db.IEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**

 */
public interface DatabaseHelper {

    SQLiteDatabase getReadableDatabase();

    SQLiteDatabase getWritableDatabase();

    /**
     * 获取指定表的Dao对象
     *
     * @param classType 与表对应的类
     */
    <D extends Dao<E, ?>, E> D getDao(Class<E> classType) throws SQLException;

    /**
     * 在同一个数据库事务中执行操作
     *
     * @param callable 包装要执行的操作
     */
    <E> E callInTransaction(Callable<E> callable) throws SQLException;

    ChangeSupportable getChangeSupportable();

    /**
     * 数据库数据变更监听注册
     *
     * @version: 1.0
     * @date 2015年10月10日
     */
    class Registry {

        private static final Set<DataChangeObserver> observers =
                Collections.synchronizedSet(new LinkedHashSet<DataChangeObserver>());

        private static final ExecutorService cachedThreadPool = Executors.newFixedThreadPool(5,
                new DefaultThreadFactory("DatabaseHelper.Registry.notifyChange"));

        /**
         * 注册对数据库数据变更的监听。 在不需要监听时需要调用
         * {@link #unregister(DataChangeObserver)}
         * 方法取消注册，否则会引起资源泄漏。
         */
        public static void register(DataChangeObserver observer) {
            // 桌台刷新类型排到前面 modify 20170306 begin
            observers.add(observer);
            // 桌台刷新类型排到前面 modify 20170306 end
        }

        public static void registerWithDinnerTable(DataChangeObserver observer) {
            List<DataChangeObserver> temps = new ArrayList<DataChangeObserver>(observers);
            observers.clear();
            observers.add(observer);
            observers.addAll(temps);
        }

        /**
         * 取消对数据库数据变更的监听。
         */
        public static void unregister(DataChangeObserver observer) {
            observers.remove(observer);
        }

        public static void notifyChange(final Collection<Uri> uris) {
            List<DataChangeObserver> temps = new ArrayList<DataChangeObserver>(observers);
            //  modify 20170302
            //如果是volley 线程切换线程回调,否则本线程处理回调
            if (Thread.currentThread().getName().startsWith(/*RequestQueue.VOLLEY_THREAD_NAME*/"volley")) {
                for (final DataChangeObserver observer : temps) {
                    cachedThreadPool.execute(new Runnable() {
                        public void run() {
                            observer.onChange(uris);
                        }
                    });
                }
            } else {
                for (final DataChangeObserver observer : temps) {
                    observer.onChange(uris);
                }
            }
        }

        /**
         * 判断观察者是否已经注册过
         *
         * @param observer 指定观察者
         * @return true为注册过，false为未注册过
         */
        public static boolean isRegistered(DataChangeObserver observer) {
            return observers != null && observers.contains(observer);
        }
    }

    /**
     * @version: 1.0
     * @date 2015年10月10日
     */
    interface DataChangeObserver {
        void onChange(Collection<Uri> uris);
    }

    /**

     */
    interface ChangeSupportable {

        void addChange(Class<?> tableClass);

        <T extends IEntity<?>> void addChange(Class<?> tableClass, List<T> entities);

        <T extends IEntity<?>> void addChange(Class<?> tableClass, T entity);

        boolean isChange();

        void clearChange();

        void notifyChange();

    }

}
