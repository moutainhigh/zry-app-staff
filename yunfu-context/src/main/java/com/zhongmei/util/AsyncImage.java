package com.zhongmei.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class AsyncImage {

    private Context context;
    private int errorResId;

    private AsyncImage(Context context) {
        this.context = context;
    }

    public static AsyncImage with(Context context) {
        return new AsyncImage(context);
    }

    public static void showImg(Context context, ImageView imageView, String imgUrl) {
        showImg(context, imageView, imgUrl, 0);
    }

    public static void showImg(Context context, ImageView imageView, String imgUrl, @DrawableRes int errorResId) {
        Glide.with(context)                 .load(imgUrl)                                                                                                .fitCenter()                                                                                                                .error(errorResId)                .into(imageView);
    }

    public static void showImg(Context context, ImageView imageView, @DrawableRes int imgResId, @DrawableRes int errorResId) {
        Glide.with(context)                 .load(imgResId)                                                                                                .fitCenter()                                                                                                                .error(errorResId)                .into(imageView);    }

    private static RequestListener<String, GlideDrawable> setRequestListener(final ImageView imageView) {
                RequestListener<String, GlideDrawable> errorListener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                Log.e("onException", e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);
                                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Log.e("onResourceReady", "isFromMemoryCache:" + isFromMemoryCache + "  model:" + model + " isFirstResource: " + isFirstResource);
                return false;
            }
        };
        return errorListener;
    }
}
