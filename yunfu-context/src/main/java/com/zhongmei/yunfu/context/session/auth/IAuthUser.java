package com.zhongmei.yunfu.context.session.auth;

/**
 * Created by demo on 2018/12/15
 */
public interface IAuthUser {

    Long getId();

    String getName();

    final class Holder {
        private static final Object LOCK = new Object();
        private static IAuthUser defaultInstance = new IAuthUser() {
            @Override
            public Long getId() {
                return -1L;
            }

            @Override
            public String getName() {
                return "";
            }
        };

        private static IAuthUser currentInstance;

        public static IAuthUser get() {
            synchronized (LOCK) {
                return currentInstance == null ? defaultInstance : currentInstance;
            }
        }

        public static synchronized void refresh(IAuthUser authUser) {
            synchronized (LOCK) {
                currentInstance = authUser;
            }
        }

        public Holder() {
        }
    }
}
