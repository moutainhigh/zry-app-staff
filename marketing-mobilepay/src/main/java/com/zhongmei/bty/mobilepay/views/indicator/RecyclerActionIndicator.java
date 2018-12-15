/*
package com.zhongmei.bty.mobilepay.views.indicator;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

*/
/**
 * Created by demo on 2018/12/15
 * <p>
 * 这是基于RecyclerView自定义指示器(目前这个视图，还未正式用于项目，功能还不完全)
 *
 * @see ViewPagerIndicator
 * RecyclerActionIndicator的适配器
 * @see RecyclerView.Adapter
 * RecyclerActionIndicator的适配器
 * @see RecyclerView.Adapter
 *//*


@Deprecated
public class RecyclerActionIndicator extends LinearLayout {

    private View prevActionView;
    private View nextActionView;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private Adapter adapter;
    private OnItemClickListener onItemClickListener;

    public RecyclerActionIndicator(Context context) {
        this(context, null);
    }

    public RecyclerActionIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerActionIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (getOrientation() == HORIZONTAL) {
            setGravity(Gravity.CENTER_VERTICAL);
        } else {
            setGravity(Gravity.CENTER_HORIZONTAL);
        }
        recyclerView = new RecyclerView(context, attrs, defStyleAttr);
    }

    @Override
    public void setOrientation(int orientation) {
        super.setOrientation(orientation);
        prepareViews();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (adapter != null) {
            adapter.listenerDelegate = onItemClickListener;
        }
        this.onItemClickListener = onItemClickListener;
    }

    public void setAdapter(Adapter adapter) {
        if (adapter == null) {
            throw new NullPointerException("The adapter must not be null");
        }
        if (this.adapter == adapter) {
            return;
        }
        this.adapter = adapter;
        this.adapter.actionIndicator = this;
        this.adapter.listenerDelegate = onItemClickListener;
        prepareViews();
    }

    private void prepareViews() {
        if (adapter == null) {
            return;
        }
        removeAllViews();

        int orientation = getOrientation();
        prevActionView = adapter.getPreviousActionView(this, orientation);
        nextActionView = adapter.getNextActionView(this, orientation);
        layoutManager = new LinearLayoutManager(getContext(), orientation, false);
        recyclerView.setLayoutManager(layoutManager);

        int index = 0;

        if (prevActionView != null) {
            ViewGroup.LayoutParams layoutParams = prevActionView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }

            super.addView(prevActionView, index, layoutParams);
            prevActionView.setOnClickListener(actionClickListener);
            index++;
        }
        ViewGroup.LayoutParams recyclerViewLayoutParams;
        if (orientation == HORIZONTAL) {
            recyclerViewLayoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        } else {
            recyclerViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
        }
        super.addView(recyclerView, index, recyclerViewLayoutParams);
        index++;
        if (nextActionView != null) {
            ViewGroup.LayoutParams layoutParams = nextActionView.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            super.addView(nextActionView, index, layoutParams);
            nextActionView.setOnClickListener(actionClickListener);
            index++;
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        throw new RuntimeException("Can`t support add view");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void nextPage() {
    }

    private void prevPage() {

    }

    private OnClickListener actionClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int fvcp = layoutManager.findFirstCompletelyVisibleItemPosition();
            int fvp = layoutManager.findFirstVisibleItemPosition();
            int lvcp = layoutManager.findLastCompletelyVisibleItemPosition();
            int lvp = layoutManager.findLastVisibleItemPosition();

            Log.i("--->", "fvcp:" + fvcp + "   fvp:" + fvp + "   lvcp:" + lvcp + "   lvp:" + lvp);


        }
    };

    */
/**RecyclerActionIndicator的适配器
 *
 * @see RecyclerView.Adapter
 *//*

    public static abstract class Adapter<T, VH extends Adapter.ViewHolder<T>> extends RecyclerView.Adapter<VH> {

        protected Context context;
        RecyclerActionIndicator actionIndicator;
        private OnItemClickListener listenerProxy;
        private OnItemClickListener listenerDelegate;

        public Adapter(Context context) {
            this.context = context;
            this.listenerProxy = new OnItemClickListener() {
                @Override
                public void onItemClick(RecyclerActionIndicator indicator, Adapter<?, ?> adapter, int position) {
                    if (listenerDelegate != null) {
                        listenerDelegate.onItemClick(indicator, adapter, position);
                    }
                }
            };
        }

        @Override
        public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
            VH vh = getViewHolder(parent, viewType);
            if (vh == null) {
                throw new NullPointerException("ViewHolder is null");
            }
            vh.actionIndicator = actionIndicator;
            vh.onItemClickListener = listenerProxy;
            vh.adapter = this;
            return vh;
        }

        @Override
        public final void onBindViewHolder(VH holder, int position) {
            T data = getData(position);
            holder.position = position;
            holder.onBindData(data, position);
        }

        public abstract VH getViewHolder(ViewGroup parent, int viewType);

        public abstract T getData(int position);

        public abstract View getPreviousActionView(RecyclerActionIndicator indicator, int orientation);

        public abstract View getNextActionView(RecyclerActionIndicator indicator, int orientation);

        public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder implements OnClickListener {
    
            RecyclerActionIndicator actionIndicator;
            Adapter<?, ?> adapter;
            int position;
            OnItemClickListener onItemClickListener;

            public ViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
            }

            public abstract void onBindData(T data, int position);

            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(actionIndicator, adapter, position);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerActionIndicator indicator, Adapter<?, ?> adapter, int position);
    }
}
*/
