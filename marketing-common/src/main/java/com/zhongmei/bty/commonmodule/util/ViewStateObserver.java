package com.zhongmei.bty.commonmodule.util;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ViewStateObserver {
    private static ViewStateObserver defaultInstance;
    private HashMap<Object, List<View>> viewsMap = new HashMap();

    private ViewStateObserver() {

    }

    public static ViewStateObserver getDefault() {
        if (defaultInstance == null) {
            defaultInstance = new ViewStateObserver();
            EventBus.getDefault().register(defaultInstance);
        }
        return defaultInstance;
    }

    private void initState(View... views) {
        /*if (ServerHeartbeat.getInstance().getNetworkState() != ServerHeartbeat.NetworkState.NetworkAvailable) {
            for (View view : views) {
                ChangeViewStateUtil.setViewOffline(view);
            }
        }*/
    }

    public void register(Object object, View... views) {
        initState(views);
        if (viewsMap.containsKey(object)) {
            viewsMap.get(object).addAll(Arrays.asList(views));
        } else {
            List<View> viewList = new ArrayList<>();
            viewList.addAll(Arrays.asList(views));
            viewsMap.put(object, viewList);
        }
    }

    public void unregister(Object object) {
        viewsMap.remove(object);
    }

    public void onEventMainThread(EventServerAvailableStateChange event) {
        /*if (event.getState() == ServerHeartbeat.NetworkState.NetworkUnavailable) {
            for (List<View> viewList : viewsMap.values()) {
                for (View view : viewList) {
                    ChangeViewStateUtil.setViewOffline(view);
                }
            }
        } else {
            for (List<View> viewList : viewsMap.values()) {
                for (View view : viewList) {
                    ChangeViewStateUtil.setViewOnline(view);
                }
            }
        }*/
    }
}
