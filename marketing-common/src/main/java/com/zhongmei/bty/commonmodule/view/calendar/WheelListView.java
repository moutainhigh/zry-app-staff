package com.zhongmei.bty.commonmodule.view.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.util.DensityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WheelListView extends ListView implements ListView.OnScrollListener, View.OnTouchListener,
        ViewTreeObserver.OnGlobalLayoutListener {
    public static final int SMOOTH_SCROLL_DURATION = 50;    public static final int SECTION_DELAY = 300;
    public static final int TEXT_SIZE = 18;    public static final float TEXT_ALPHA = 0.8f;
    public static final int TEXT_COLOR_FOCUS = 0XFF32ADF6;
    public static final int TEXT_COLOR_NORMAL = 0XFFBCBCBC;

    public static final int ITEM_OFF_SET = 2;
    public static final int ITEM_HEIGHT = 45;    public static final int ITEM_PADDING_TOP_BOTTOM = 5;    public static final int ITEM_PADDING_LEFT_RIGHT = 10;    public static final int ITEM_MARGIN = 5;    public static final int ITEM_TAG_IMAGE = 100;
    public static final int ITEM_TAG_TEXT = 101;

    private static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    private static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    private int itemHeightPixels = 0;     private int currentPosition = -1;        private WheelAdapter adapter = new WheelAdapter();
    private OnWheelChangeListener onWheelChangeListener;

    private int textSize = TEXT_SIZE;
    private int textColorNormal = TEXT_COLOR_NORMAL;
    private int textColorFocus = TEXT_COLOR_FOCUS;
    private LineConfig lineConfig = null;
    public WheelListView(Context context) {
        super(context);
        init();
    }

    public WheelListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WheelListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setVerticalScrollBarEnabled(false);
        setScrollingCacheEnabled(false);
        setCacheColorHint(Color.TRANSPARENT);
        setFadingEdgeLength(0);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        setDividerHeight(0);
        setOnScrollListener(this);
        setOnTouchListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
        }
        if (!isInEditMode()) {
            getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
        super.setAdapter(adapter);
    }


    private void changeBackground() {
        int wheelSize = adapter.getWheelSize();
        if (null == lineConfig) {
            lineConfig = new LineConfig();
        }
        lineConfig.setWidth(getWidth());
        lineConfig.setHeight(itemHeightPixels * wheelSize);
        lineConfig.setWheelSize(wheelSize);
        lineConfig.setItemHeight(itemHeightPixels);
        Drawable drawable;
        WheelDrawable holoWheelDrawable = new HoloWheelDrawable(lineConfig);
        if (lineConfig.isShadowVisible()) {
            WheelDrawable shadowWheelDrawable = new ShadowWheelDrawable(lineConfig);
            if (lineConfig.isVisible()) {
                drawable = new LayerDrawable(new Drawable[]{shadowWheelDrawable, holoWheelDrawable});
            } else {
                drawable = shadowWheelDrawable;
            }
        } else if (lineConfig.isVisible()) {
            drawable = holoWheelDrawable;
        } else {
            drawable = new WheelDrawable(lineConfig);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            super.setBackground(drawable);
        } else {
                        super.setBackgroundDrawable(drawable);
        }
    }

    private void _setItems(List<String> list) {
        if (null == list || list.size() == 0) {
            throw new IllegalArgumentException("data are empty");
        }
        currentPosition = -1;
        adapter.setData(list);
    }

    public void setItems(List<String> list) {
        _setItems(list);
                setSelectedIndex(0);
    }

    public void setItems(String[] list) {
        setItems(Arrays.asList(list));
    }

    public void setItems(List<String> list, int index) {
        _setItems(list);
        setSelectedIndex(index);
    }

    public void setItems(List<String> list, String item) {
        _setItems(list);
        setSelectedItem(item);
    }

    public void setItems(String[] list, int index) {
        setItems(Arrays.asList(list), index);
    }

    public void setItems(String[] list, String item) {
        setItems(Arrays.asList(list), item);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setUnSelectedTextColor(@ColorInt int unSelectedTextColor) {

        if (unSelectedTextColor != 0) {
            this.textColorNormal = unSelectedTextColor;
            refreshCurrentPosition();
        }
    }

    public void setSelectedTextColor(@ColorInt int selectedTextColor) {

        if (selectedTextColor != 0) {
            this.textColorFocus = selectedTextColor;
            refreshCurrentPosition();
        }
    }


    public void setOffset(@IntRange(from = 1, to = 3) int offset) {
        if (offset < 1 || offset > 3) {
            throw new IllegalArgumentException("Offset must between 1 and 3");
        }
        int wheelSize = offset * 2 + 1;
        adapter.setWheelSize(wheelSize);
    }


    public void setCanLoop(boolean canLoop) {
        adapter.setLoop(canLoop);
    }

    public int getSelectedIndex() {
        return getCurrentPosition();
    }

    public void setSelectedIndex(final int index) {
        final int realPosition = getRealPosition(index);
                postDelayed(new Runnable() {
            @Override
            public void run() {
                WheelListView.super.setSelection(realPosition);
                refreshCurrentPosition();
            }
        }, SECTION_DELAY);
    }


    @Override
    public String getSelectedItem() {
        return adapter.getItem(getCurrentPosition());
    }

    public void setSelectedItem(String item) {
        setSelectedIndex(adapter.getData().indexOf(item));
    }

   


    private int getRealPosition(int position) {
        int realCount = adapter.getRealCount();
        if (realCount == 0) {
            return 0;
        }
        if (adapter.isLoop()) {
            int d = Integer.MAX_VALUE / 2 / realCount;
            return position + d * realCount - adapter.getWheelSize() / 2;
        }
        return position;
    }


    public int getCurrentPosition() {
        if (currentPosition == -1) {
            return 0;
        }
        return currentPosition;
    }


    @Deprecated
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter != null && adapter instanceof WheelAdapter) {
            this.adapter = (WheelAdapter) adapter;
            super.setAdapter(this.adapter);
        } else {
            throw new IllegalArgumentException("please invoke setItems");
        }
    }

    private void onSelectedCallback() {
        int index = getSelectedIndex();
        String item = getSelectedItem();
        if (null != onWheelChangeListener) {
            onWheelChangeListener.onItemSelected(index, item);
        }
    }

    private int obtainSmoothDistance(float scrollDistance) {
        if (Math.abs(scrollDistance) <= 2) {
            return (int) scrollDistance;
        } else if (Math.abs(scrollDistance) < 12) {
            return scrollDistance > 0 ? 2 : -2;
        } else {
            return (int) (scrollDistance / 6);          }
    }

    private void refreshCurrentPosition() {
        if (getChildAt(0) == null || itemHeightPixels == 0) {
            return;
        }
        int firstPosition = getFirstVisiblePosition();
        if (adapter.isLoop() && firstPosition == 0) {
            Log.v("WheelListView","is loop and first visible position is 0");
            return;
        }
        int position;
        if (Math.abs(getChildAt(0).getY()) <= itemHeightPixels / 2) {
            position = firstPosition;
        } else {
            position = firstPosition + 1;
        }
                int offset = (adapter.getWheelSize() - 1) / 2;
        int curPosition = position + offset;
        refreshVisibleItems(firstPosition, curPosition, offset);
        if (adapter.isLoop()) {
            position = curPosition % adapter.getRealCount();
        }
        if (position == currentPosition) {
                        return;
        }
        currentPosition = position;
                onSelectedCallback();
    }

    private void refreshVisibleItems(int firstPosition, int curPosition, int offset) {
        for (int i = curPosition - offset; i <= curPosition + offset; i++) {
            View itemView = getChildAt(i - firstPosition);
            if (itemView == null) {
                continue;
            }
            TextView textView = (TextView) itemView.findViewWithTag(ITEM_TAG_TEXT);
            refreshTextView(i, curPosition, itemView, textView);
        }
    }
    private void refreshTextView(int position, int curPosition, View
            itemView, TextView textView) {
                if (curPosition == position) {             setTextView(itemView, textView, textColorFocus, textSize, 1.0f);
        } else {             int delta = Math.abs(position - curPosition);
            float alpha = (float) Math.pow(TEXT_ALPHA, delta);
            setTextView(itemView, textView, textColorNormal, textSize, alpha);
        }
    }

    private void setTextView(View itemView, TextView textView, int textColor, float textSize, float textAlpha) {
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        itemView.setAlpha(textAlpha);
    }

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        int childCount = getChildCount();
        if (childCount > 0 && itemHeightPixels == 0) {
            itemHeightPixels = getChildAt(0).getHeight();
            Log.v("WheelListView","itemHeightPixels=" + itemHeightPixels);
            if (itemHeightPixels == 0) {
                return;
            }
            int wheelSize = adapter.getWheelSize();
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = itemHeightPixels * wheelSize;
            refreshVisibleItems(getFirstVisiblePosition(),
                    getCurrentPosition() + wheelSize / 2, wheelSize / 2);
            changeBackground();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.v("WheelListView","press down: currentPosition=" + currentPosition);
                break;
            case MotionEvent.ACTION_UP:
                Log.v("WheelListView", "press up: currentItem=" + getSelectedItem());
                break;
        }
        return false;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState != SCROLL_STATE_IDLE) {
            return;
        }
        View itemView = getChildAt(0);
        if (itemView == null) {
            return;
        }
        float deltaY = itemView.getY();
                if ((int) deltaY == 0 || itemHeightPixels == 0) {
            return;
        }
        if (Math.abs(deltaY) < itemHeightPixels / 2) {
            int d = obtainSmoothDistance(deltaY);
            smoothScrollBy(d, SMOOTH_SCROLL_DURATION);
        } else {
            int d = obtainSmoothDistance(itemHeightPixels + deltaY);
            smoothScrollBy(d, SMOOTH_SCROLL_DURATION);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int
            visibleItemCount, int totalItemCount) {
        if (visibleItemCount != 0) {
            refreshCurrentPosition();
        }
    }
    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = getLayoutParams().width;
                if (width == WRAP_CONTENT) {
                        super.onMeasure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), heightSpec);
        } else {
                        super.onMeasure(widthSpec, heightSpec);
        }
    }

    public void setOnWheelChangeListener(OnWheelChangeListener onWheelChangeListener) {
        this.onWheelChangeListener = onWheelChangeListener;
    }

    public void setLineConfig(LineConfig lineConfig) {
        this.lineConfig = lineConfig;
    }

    public interface OnWheelChangeListener {

        void onItemSelected(int index, String item);
    }




    private static class ItemView extends LinearLayout {
        private ImageView imageView;
        private TextView textView;

        public ItemView(Context context) {
            super(context);
            init(context);
        }

        public ItemView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        private void init(Context context) {
            setOrientation(LinearLayout.HORIZONTAL);
            int paddingTopBottom = DensityUtil.dip2px(context, ITEM_PADDING_TOP_BOTTOM);
            int paddingLeftRight = DensityUtil.dip2px(context, ITEM_PADDING_LEFT_RIGHT);
            setPadding(paddingLeftRight, paddingTopBottom, paddingLeftRight, paddingTopBottom);
            setGravity(Gravity.CENTER);
            int height = DensityUtil.dip2px(context, ITEM_HEIGHT);
                        setLayoutParams(new AbsListView.LayoutParams(MATCH_PARENT, height));

            imageView = new ImageView(getContext());
            imageView.setTag(ITEM_TAG_IMAGE);
            imageView.setVisibility(View.GONE);
            LayoutParams imageParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            imageParams.rightMargin = DensityUtil.dip2px(context, ITEM_MARGIN);
            addView(imageView, imageParams);

            textView = new TextView(getContext());
            textView.setTag(ITEM_TAG_TEXT);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setSingleLine(true);
            textView.setIncludeFontPadding(false);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            LayoutParams textParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            addView(textView, textParams);
        }


        public void setText(CharSequence text) {
            textView.setText(text);
        }


        public void setImage(@DrawableRes int resId) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(resId);
        }

    }

    private static class WheelAdapter extends BaseAdapter {
        private List<String> data = new ArrayList<>();
        private boolean isLoop = false;
        private int wheelSize = 5;

        public final int getRealCount() {
            return data.size();
        }

        public final int getCount() {
            if (isLoop) {
                return Integer.MAX_VALUE;
            }
            return data.size() > 0 ? (data.size() + wheelSize - 1) : 0;
        }

        @Override
        public final long getItemId(int position) {
            if (isLoop) {
                return data.size() > 0 ? position % data.size() : position;
            }
            return position;
        }

        @Override
        public final String getItem(int position) {
            if (isLoop) {
                return data.size() > 0 ? data.get(position % data.size()) : null;
            }
            if (data.size() <= position) {
                position = data.size() - 1;
            }
            return data.get(position);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public final View getView(int position, View convertView, ViewGroup parent) {
            if (isLoop) {
                position = position % data.size();
            } else {
                if (position < wheelSize / 2) {
                    position = -1;
                } else if (position >= wheelSize / 2 + data.size()) {
                    position = -1;
                } else {
                    position = position - wheelSize / 2;
                }
            }
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                holder.itemView = new WheelListView.ItemView(parent.getContext());
                convertView = holder.itemView;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (!isLoop) {
                holder.itemView.setVisibility(position == -1 ? View.INVISIBLE : View.VISIBLE);
            }
            if (position == -1) {
                position = 0;
            }
            holder.itemView.setText(data.get(position));
                        return convertView;
        }

        public final WheelAdapter setLoop(boolean loop) {
            if (loop != isLoop) {
                isLoop = loop;
                super.notifyDataSetChanged();
            }
            return this;
        }

        public final WheelAdapter setWheelSize(int wheelSize) {

            if ((wheelSize & 1) == 0) {
                throw new IllegalArgumentException("wheel size must be an odd number.");
            }
            this.wheelSize = wheelSize;
            super.notifyDataSetChanged();
            return this;
        }

        public final WheelAdapter setData(List<String> list) {
            data.clear();
            if (null != list) {
                data.addAll(list);
            }
            super.notifyDataSetChanged();
            return this;
        }

        public List<String> getData() {
            return data;
        }

        public int getWheelSize() {
            return wheelSize;
        }

        public boolean isLoop() {
            return isLoop;
        }


        @Override
        @Deprecated
        public final void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }


        @Override
        @Deprecated
        public final void notifyDataSetInvalidated() {
            super.notifyDataSetInvalidated();
        }

        private static class ViewHolder {
            WheelListView.ItemView itemView;
        }
    }

    private static class WheelDrawable extends Drawable {
        protected int width;
        protected int height;
        private Paint bgPaint;

        public WheelDrawable(LineConfig config) {
            this.width = config.getWidth();
            this.height = config.getHeight();
            init();
        }

        private void init() {
            bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPaint.setColor(Color.TRANSPARENT);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.drawRect(0, 0, width, height, bgPaint);
        }

        @Override
        public void setAlpha(int alpha) {
        }

        @Override
        public void setColorFilter(ColorFilter colorFilter) {
        }

        @Override
        public int getOpacity() {
            return PixelFormat.UNKNOWN;
        }

    }

    private static class HoloWheelDrawable extends WheelDrawable {
        private Paint bgPaint, paint;
        private int wheelSize, itemHeight;
        private float ratio;
        private LineConfig lineConfig ;

        public HoloWheelDrawable(LineConfig config) {
            super(config);
            this.lineConfig = config;
            this.wheelSize = config.getWheelSize();
            this.itemHeight = config.getItemHeight();
            init(config);
        }

        private void init(LineConfig config) {
            bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPaint.setColor(Color.TRANSPARENT);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStrokeWidth(config.getThick());
            paint.setColor(config.getColor());
            paint.setAlpha(config.getAlpha());
        }

        @Override
        public void draw(Canvas canvas) {
                        canvas.drawRect(0, 0, width, height, bgPaint);
                        if(lineConfig.isVisible()){
                                if (itemHeight != 0) {
                    canvas.drawLine(width * ratio, itemHeight * (wheelSize / 2), width * (1 - ratio),
                            itemHeight * (wheelSize / 2), paint);
                    canvas.drawLine(width * ratio, itemHeight * (wheelSize / 2 + 1), width * (1 - ratio),
                            itemHeight * (wheelSize / 2 + 1), paint);
                }
            }

        }

    }

    private static class ShadowWheelDrawable extends WheelDrawable {
        private static final int[] SHADOWS_COLORS =
                {
                        0xFF999999,
                        0x00AAAAAA,
                        0x00AAAAAA
                };         private GradientDrawable topShadow = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                SHADOWS_COLORS);         private GradientDrawable bottomShadow = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                SHADOWS_COLORS);         private Paint bgPaint, paint, dividerPaint;
        private int wheelSize, itemHeight;

        public ShadowWheelDrawable(LineConfig config) {
            super(config);
            this.wheelSize = config.getWheelSize();
            this.itemHeight = config.getItemHeight();
            init();
        }

        private void init() {
            bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPaint.setColor(Color.TRANSPARENT);

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(0XF0CFCFCF);

            dividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            dividerPaint.setColor(0XFFB5B5B5);
            dividerPaint.setStrokeWidth(2);
        }

        @Override
        public void draw(Canvas canvas) {
                        canvas.drawRect(0, 0, width, height, bgPaint);

                        if (itemHeight != 0) {
                canvas.drawRect(0, itemHeight * (wheelSize / 2), width, itemHeight
                        * (wheelSize / 2 + 1), paint);
                canvas.drawLine(0, itemHeight * (wheelSize / 2), width, itemHeight
                        * (wheelSize / 2), dividerPaint);
                canvas.drawLine(0, itemHeight * (wheelSize / 2 + 1), width, itemHeight
                        * (wheelSize / 2 + 1), dividerPaint);

                                topShadow.setBounds(0, 0, width, itemHeight);
                topShadow.draw(canvas);

                bottomShadow.setBounds(0, height - itemHeight, width, height);
                bottomShadow.draw(canvas);
            }
        }

    }

}