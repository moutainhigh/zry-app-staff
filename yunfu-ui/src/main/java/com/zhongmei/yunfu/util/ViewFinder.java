
package com.zhongmei.yunfu.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ViewFinder {


    @SuppressWarnings("unchecked")
    public static <V extends View> V findViewById(Activity activity, int id) {
        View view = activity.findViewById(id);
        if (view != null) {
            try {
                V v = (V) view;
                return v;
            } catch (Exception e) {
                throw new ClassCastException("The view （id=" + id + ") can`t be cast to dest class");
            }
        }
        throw new Resources.NotFoundException("No view has been found which id is:" + id);
    }


    @SuppressWarnings("unchecked")
    public static <V extends View> V findViewById(View parent, int id) {
        View view = parent.findViewById(id);
        if (view != null) {
            try {
                V v = (V) view;
                return v;
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassCastException("The view （id=" + id + ") can`t be cast to dest class");
            }
        }
        throw new Resources.NotFoundException("No view has been found which id is:" + id);
    }


    @SuppressWarnings("unchecked")
    public static <V extends View> V findViewById(Dialog dialog, int id) {
        View view = dialog.findViewById(id);
        if (view != null) {
            try {
                V v = (V) view;
                return v;
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassCastException("The view （id=" + id + ") can`t be cast to dest class");
            }
        }
        throw new Resources.NotFoundException("No view has been found which id is:" + id);
    }


    public static <V extends View> V findViewById(Fragment fragment, int id) {
        View view = fragment.getView();
        return findViewById(view, id);
    }

    public static <V extends View> V inflate(Context context, int resource, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflate(inflater, resource, viewGroup);
    }

    @SuppressWarnings("unchecked")
    public static <V extends View> V inflate(LayoutInflater inflater, int resource, ViewGroup viewGroup) {
        View view = inflater.inflate(resource, viewGroup);
        if (view != null) {
            V v = null;
            try {
                v = (V) view;
                return v;
            } catch (Exception e) {
                throw new IllegalArgumentException("The view which resource=" + resource + "can`t cast to dest class");
            }
        }
        throw new IllegalArgumentException("There is no view which resource=" + resource);
    }
}