package com.zhongmei.yunfu.ui.base.entity;

import java.io.Serializable;

import android.support.v4.app.Fragment;

public class FragmentVo implements Serializable {


    private static final long serialVersionUID = 1L;
    private int containerViewId;
    private Fragment fragment;
    private String tag;

    public FragmentVo(int containerViewId, Fragment fragment, String tag) {
        this.containerViewId = containerViewId;
        this.fragment = fragment;
        this.tag = tag;
    }

    public int getContainerViewId() {
        return containerViewId;
    }

    public void setContainerViewId(int containerViewId) {
        this.containerViewId = containerViewId;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
