package com.zhongmei.bty.customer.views;//package com.zhongmei.bty.customer.views;
//
//import android.content.Context;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.RecyclerView;
//import android.util.AttributeSet;
//
///**
// * 自定义顾客的recyclerview
// *

// * @date 2017/3/15 09:33
// */
//public class CustomerRecyclerView extends RecyclerView {
//    private OnBottomCallback mOnBottomCallback;
//
//    public interface OnBottomCallback {
//        void onBottom();
//    }
//
//    public void setOnBottomCallback(OnBottomCallback onBottomCallback) {
//        this.mOnBottomCallback = onBottomCallback;
//    }
//
//    public CustomerRecyclerView(Context context) {
//        this(context, null);
//    }
//
//    public CustomerRecyclerView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public CustomerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//    }
//
//    @Override
//    public void onScrolled(int dx, int dy) {
//        if (isSlideToBottom()) {
//            mOnBottomCallback.onBottom();
//        }
//    }
//
//    /**
//     * 其实就是它在起作用。
//     */
//    public boolean isSlideToBottom() {
//        return this != null
//                && this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
//                >= this.computeVerticalScrollRange();
//    }
//}