/*
 * Copyright 2015 Soo [154014022@qq.com | sootracker@gmail.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.zhongmei.yunfu.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Help to find a view that was identified by the id and cast to the type which you want
 */
public class ViewFinder {

    /**
     * Find view by id in activity
     *
     * @param activity The view belong to
     * @param id       The view`s id
     * @param <V>      The type that view will be cast to
     * @return Instance of dest type
     * @throws ClassCastException          There is a view that can`t be cast to dest type
     * @throws Resources.NotFoundException There is no view has be found by id
     */
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

    /**
     * Find view by id in parent
     *
     * @param parent The view belong to
     * @param id     The view`s id
     * @param <V>    The type that view will be cast to
     * @return Instance of dest type
     * @throws ClassCastException          There is a view that can`t be cast to dest type
     * @throws Resources.NotFoundException There is no view has be found by id
     */
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

    /**
     * Find view by id in dialog
     *
     * @param dialog The view belong to
     * @param id     The view`s id
     * @param <V>    The type that view will be cast to
     * @return Instance of dest type
     * @throws ClassCastException          There is a view that can`t be cast to dest type
     * @throws Resources.NotFoundException There is no view has be found by id
     */
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

    /**
     * Find view by id in fragment
     *
     * @param fragment The view belong to
     * @param id       The view`s id
     * @param <V>      The type that view will be cast to
     * @return Instance of dest type
     * @throws ClassCastException          There is a view that can`t be cast to dest type
     * @throws Resources.NotFoundException There is no view has be found by id
     */
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