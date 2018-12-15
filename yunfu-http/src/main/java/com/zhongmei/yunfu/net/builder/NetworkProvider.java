package com.zhongmei.yunfu.net.builder;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.zhongmei.yunfu.net.request.VolleyNetworkManager;

/**
 * Created by demo on 2018/12/15
 */
public class NetworkProvider {

    public static class Builder {
        private Context context;
        private Fragment fragment;
        private FragmentActivity activity;
        private NetworkRequest request;

        public void create() {
            if (getContext() == null) {
                return;
            }
            VolleyNetworkManager.getInstance().executeRequest(getContext(), request);
        }

        private Context getContext() {
            if (context != null) {
                return context;
            }

            if (fragment != null) {
                return fragment.getActivity();
            }

            if (activity != null) {
                return activity;
            }

            return null;
        }

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public Builder with(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }

        public Builder with(FragmentActivity activity) {
            this.activity = activity;
            return this;
        }

        public Builder request(NetworkRequest request) {
            this.request = request;
            return this;
        }
    }
}
