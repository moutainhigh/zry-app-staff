package com.zhongmei.bty.commonmodule.core.store;

import android.content.Context;

import com.zhongmei.yunfu.context.util.ArgsUtils;

import java.util.Map;
import java.util.WeakHashMap;

/**

 */
public abstract class StoreFactory {

    private Map<String, IStore> cache = new WeakHashMap<>();

    private Context context;

    public StoreFactory(Context context) {
        this.context = context;
    }

    public final synchronized IStore getStore(String name) {
        ArgsUtils.notNull(name, "Name of IStore is null");
        IStore iStore = cache.get(name);
        if (iStore == null) {
            iStore = onCreate(context, name);
            cache.put(name, iStore);
        }
        return iStore;
    }

    protected abstract IStore onCreate(Context context, String name);
}
