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


public interface DatabaseHelper {

    SQLiteDatabase getReadableDatabase();

    SQLiteDatabase getWritableDatabase();


    <D extends Dao<E, ?>, E> D getDao(Class<E> classType) throws SQLException;


    <E> E callInTransaction(Callable<E> callable) throws SQLException;

    ChangeSupportable getChangeSupportable();


    class Registry {

        private static final Set<DataChangeObserver> observers =
                Collections.synchronizedSet(new LinkedHashSet<DataChangeObserver>());

        private static final ExecutorService cachedThreadPool = Executors.newFixedThreadPool(5,
                new DefaultThreadFactory("DatabaseHelper.Registry.notifyChange"));


        public static void register(DataChangeObserver observer) {
                        observers.add(observer);
                    }

        public static void registerWithDinnerTable(DataChangeObserver observer) {
            List<DataChangeObserver> temps = new ArrayList<DataChangeObserver>(observers);
            observers.clear();
            observers.add(observer);
            observers.addAll(temps);
        }


        public static void unregister(DataChangeObserver observer) {
            observers.remove(observer);
        }

        public static void notifyChange(final Collection<Uri> uris) {
            List<DataChangeObserver> temps = new ArrayList<DataChangeObserver>(observers);
                                    if (Thread.currentThread().getName().startsWith("volley")) {
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


        public static boolean isRegistered(DataChangeObserver observer) {
            return observers != null && observers.contains(observer);
        }
    }


    interface DataChangeObserver {
        void onChange(Collection<Uri> uris);
    }


    interface ChangeSupportable {

        void addChange(Class<?> tableClass);

        <T extends IEntity<?>> void addChange(Class<?> tableClass, List<T> entities);

        <T extends IEntity<?>> void addChange(Class<?> tableClass, T entity);

        boolean isChange();

        void clearChange();

        void notifyChange();

    }

}
