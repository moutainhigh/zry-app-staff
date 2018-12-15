package com.zhongmei.bty.data.db.ormlite;

import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.sql.SQLException;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Loader;
import android.database.ContentObserver;
import android.net.Uri;
import android.util.Log;

/**
 * 使用ORMLite从数据库中查询记录的Loader实现。
 * 使用方法参见{@link android.content.CursorLoader}
 *
 * @param <T>
 */
public abstract class OrmliteLoader<T> extends AsyncTaskLoader<T> {

    private static final String TAG = OrmliteLoader.class.getName();

    private final Uri mUri;

    private T mData;
    private ContentObserver mObserver;

    protected OrmliteLoader(Context context, Uri uri) {
        super(context);
        this.mUri = uri;
    }

    @Override
    public T loadInBackground() {
        DatabaseHelper databaseHelper = DBHelperManager.getHelper();
        try {
            return query(databaseHelper);
        } catch (SQLException ex) {
            Log.w(TAG, "Query Error!", ex);
        } finally {
            DBHelperManager.releaseHelper(databaseHelper);
        }
        return null;
    }

    @Override
    public void deliverResult(T data) {
        mData = data;
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }

        if (mObserver == null) {
            mObserver = new ForceLoadContentObserver();
            ContentResolver resolver = getContext().getContentResolver();
            resolver.registerContentObserver(mUri, true, mObserver);
        }

        if (takeContentChanged() || mData == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        if (mData != null) {
            mData = null;
        }

        if (mObserver != null) {
            ContentResolver resolver = getContext().getContentResolver();
            resolver.unregisterContentObserver(mObserver);
            mObserver = null;
        }
    }

    /**
     * 返回符合要求的记录，在回调的{@link android.app.LoaderManager.LoaderCallbacks#onLoadFinished(Loader, Object)}
     * 将传入此返回值。
     *
     * @param databaseHelper
     * @return
     * @throws SQLException
     */
    protected abstract T query(DatabaseHelper databaseHelper) throws SQLException;

}
