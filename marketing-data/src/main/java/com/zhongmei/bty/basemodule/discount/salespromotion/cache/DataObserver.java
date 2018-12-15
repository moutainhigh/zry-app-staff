package com.zhongmei.bty.basemodule.discount.salespromotion.cache;

import android.net.Uri;

import com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder.BasicHolder;
import com.zhongmei.yunfu.context.util.ThreadUtils;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.Collection;

/**
 * Created by demo on 2018/12/15
 */

public class DataObserver implements DatabaseHelper.DataChangeObserver {

    private final BasicHolder<?> holder;
    private final Uri uri;

    public DataObserver(BasicHolder<?> holder, Uri uri) {
        this.uri = uri;
        this.holder = holder;
    }


    @Override
    public void onChange(Collection<Uri> uris) {
        if (uris.contains(uri)) {
            ThreadUtils.runOnWorkThread(new Runnable() {
                public void run() {
                    try {
                        holder.refresh();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
