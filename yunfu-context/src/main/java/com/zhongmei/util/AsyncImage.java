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
        Glide.with(context) //this 是上下文 activity/fragment
                .load(imgUrl)//根据地址下载图片
                //.listener(setRequestListener(imageView))//设置监听
                //.override(600, 600)//Glide加载图片大小是自动调整的，他根据ImageView的尺寸自动调整加载的图片大小，
                // 并且缓存的时候也是按图片大小缓存，每种尺寸都会保留一份缓存，如果图片不会自动适配到 ImageView，调用 override(horizontalSize, verticalSize) 。
                // 这将在图片显示到 ImageView之前重新改变图片大小
                //.dontAnimate()//不使用glide默认的渐入渐出的动画
                .fitCenter()//缩放
                //.transform(new GlideRoundTransform(this,20))//显示圆角图片
                //.transform(new GlideRotateTransform(this,90))//显示旋转后的图片
                //.transform(new GlideCircleTransform(this))//显示圆形图片
                //.transform(new GlideRoundTransform(this,20),new GlideRotateTransform(this,90))//圆角且旋转后的显示
                //.animate(R.anim.sacle_rotate_anim)//以自定义动画的方式显示
                //.placeholder(R.mipmap.icon_default)//默认显示图片
                .error(errorResId)//图片加载错误显示的图片
                .into(imageView);//显示

    }

    public static void showImg(Context context, ImageView imageView, @DrawableRes int imgResId, @DrawableRes int errorResId) {
        Glide.with(context) //this 是上下文 activity/fragment
                .load(imgResId)//根据地址下载图片
                //.listener(setRequestListener(imageView))//设置监听
                //.override(600, 600)//Glide加载图片大小是自动调整的，他根据ImageView的尺寸自动调整加载的图片大小，
                // 并且缓存的时候也是按图片大小缓存，每种尺寸都会保留一份缓存，如果图片不会自动适配到 ImageView，调用 override(horizontalSize, verticalSize) 。
                // 这将在图片显示到 ImageView之前重新改变图片大小
                //.dontAnimate()//不使用glide默认的渐入渐出的动画
                .fitCenter()//缩放
                //.transform(new GlideRoundTransform(this,20))//显示圆角图片
                //.transform(new GlideRotateTransform(this,90))//显示旋转后的图片
                //.transform(new GlideCircleTransform(this))//显示圆形图片
                //.transform(new GlideRoundTransform(this,20),new GlideRotateTransform(this,90))//圆角且旋转后的显示
                //.animate(R.anim.sacle_rotate_anim)//以自定义动画的方式显示
                //.placeholder(R.mipmap.icon_default)//默认显示图片
                .error(errorResId)//图片加载错误显示的图片
                .into(imageView);//显示
    }

    private static RequestListener<String, GlideDrawable> setRequestListener(final ImageView imageView) {
        //设置错误监听
        RequestListener<String, GlideDrawable> errorListener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                //图片加载异常的回调
                Log.e("onException", e.toString() + "  model:" + model + " isFirstResource: " + isFirstResource);
                //imageView.setImageResource(R.mipmap.icon_error);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                //图片加载成功的回调
                Log.e("onResourceReady", "isFromMemoryCache:" + isFromMemoryCache + "  model:" + model + " isFirstResource: " + isFirstResource);
                return false;
            }
        };
        return errorListener;
    }
}
