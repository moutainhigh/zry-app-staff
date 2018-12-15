package com.zhongmei.yunfu.context.session.core.auth;

import android.text.TextUtils;

import com.zhongmei.yunfu.context.session.core.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by demo on 2018/12/15
 */

public abstract class Auth implements IAuth {

    public interface Filter {
        boolean access(String targetName, String targetValue);
    }

    private final Object LOCK = new Object();
    private Map<String, Resource> cache = new ConcurrentHashMap<>();
    private boolean isLoading = false;

    public Auth() {
    }

    public final boolean hasAuth(String authCode) {
        return hasAuth(authCode, null);
    }

    public final boolean hasAuth(String authCode, Filter filter) {
        Resource resource = cache.get(authCode);
        if (resource != null) {
            if (filter == null) {
                return true;
            } else {
                Map<String, String> detail = resource.detail;
                for (Map.Entry<String, String> entry : detail.entrySet()) {
//                    boolean access = filter.access(entry.getKey(), entry.getValue());
                    boolean access = filter.access(entry.getValue(), entry.getKey());
                    if (access) {
                        return true;
                    }
                }
            }
        }
        return false /*false*/;
    }

    public final String getAuthDetail(String authCode) {
        return getAuthDetail(authCode, null);
    }

    public final String getAuthDetail(String authCode, String detailCode) {
        Resource resource = cache.get(authCode);
        if (resource != null && resource.detail != null && !TextUtils.isEmpty(detailCode)) {
            return resource.detail.get(detailCode);
        }
        return null;
    }

    public final void load(User user) {
        Map<String, Resource> map = new HashMap<>();
        onLoad(user, map);
        cache.clear();
        cache.putAll(map);
    }

    public final void release() {
        onRelease();
        cache.clear();
    }

    protected abstract void onLoad(User user, Map<String, Resource> map);

    protected void onRelease() {

    }

    protected class Resource {
        public final String code;
        public final Map<String, String> detail;

        public Resource(String code, Map<String, String> detail) {
            this.code = code;
            this.detail = detail;
        }
    }
}
