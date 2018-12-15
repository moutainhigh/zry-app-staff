package com.zhongmei.bty.commonmodule.view.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhongmei.yunfu.commonmodule.R;
import com.zhongmei.bty.commonmodule.view.calendar.CalendarView.OnItemClickListener;

/**
 * @date 2015年8月4日下午4:52:59
 */
public class CalendarDialog extends Dialog implements CalendarView.OnMonthChangeListener, OnKeyListener {
    private static final String TAG = CalendarDialog.class.getSimpleName();

    private ImageButton ibToday;

    private ImageButton calendarLeft;

    private TextView calendarCenter;

    private ImageButton calendarRight;

    private Context mContext;

    private OnItemClickListener itemListener;

    private List<Date> markDateList;

    private SimpleDateFormat formatMonth = new SimpleDateFormat(getContext().getString(R.string.year_month_format), Locale.getDefault());

    private Date defaultSeDate;

    CalendarViewAdapter<CalendarView> adapter;

    private CalendarViewPager mViewPager;

    private int mCurrentIndex = 498;

    private CalendarView[] mShowViews;

    private CalendarView[] calendarViews;

    private SildeDirection mDirection = SildeDirection.NO_SILDE;

    private boolean isShowBefore = true;

    private static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;

    private boolean isScrolling = false;
    private Date currentDate;

    enum SildeDirection {
        RIGHT, LEFT, NO_SILDE;
    }

    private Activity owner;

    public CalendarDialog(Context context, OnItemClickListener itemListener) {
        super(context, R.style.date_dialog);
        mContext = context;
        this.itemListener = itemListener;
        owner = (context instanceof Activity) ? (Activity) context : null;
    }

    public CalendarDialog(Context context, int theme, OnItemClickListener itemListener) {
        super(context, theme);
        mContext = context;
        this.itemListener = itemListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_main);
        getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
        setOnKeyListener(this);
        calendarViews = new CalendarView[3];
        mViewPager = (CalendarViewPager) findViewById(R.id.vp_calendar);

        for (int i = 0; i < 3; i++) {
            calendarViews[i] = new CalendarView(mContext) {
                @Override
                public boolean drawRedPoint(String key) {
                    return isShowRedPoint(key);
                }
            };
            calendarViews[i].setOnItemClickListener(itemListener, this);
            calendarViews[i].setDefaultSelected(defaultSeDate);
            calendarViews[i].isShowBefore(isShowBefore);
        }
        adapter = new CalendarViewAdapter<CalendarView>(calendarViews);
        setViewPager();

        ibToday = (ImageButton) findViewById(R.id.ib_today);
        calendarLeft = (ImageButton) findViewById(R.id.calendarLeft);
        calendarCenter = (TextView) findViewById(R.id.calendarCenter);
        calendarRight = (ImageButton) findViewById(R.id.calendarRight);

        ibToday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.OnItemClick(new Date());
                }
            }
        });

        // 获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
        calendarLeft.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 点击上一月 同样返回年月
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });

        calendarRight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 点击下一月
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            }
        });
    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(498);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.e("viewpager", mViewPager.getCurrentItem() + " onPageSelected");
                measureDirection(position);
                updateCalendarView(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Log.e("viewpager", mViewPager.getCurrentItem() + "onPageScrolled");
                if (isScrolling) {
                    CalendarView calendarView = calendarViews[mViewPager.getCurrentItem() % 3];
                    if (calendarView != null) {
                        calendarView.clearSelect();
                    }
                }
                // 扩展某些查询的功能需要
                if (queryListener != null && currentDate != null) {
                    queryListener.onQuery(currentDate);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    isScrolling = true;
                } else {
                    isScrolling = false;
                }
            }
        });
    }

    /**
     * 计算方向
     */
    private void measureDirection(int position) {

        if (position > mCurrentIndex) {
            mDirection = SildeDirection.RIGHT;

        } else if (position < mCurrentIndex) {
            mDirection = SildeDirection.LEFT;
        }
        mCurrentIndex = position;
    }

    // 更新日历视图
    private void updateCalendarView(int position) {
        mShowViews = adapter.getAllItems();
        if (mDirection == SildeDirection.RIGHT) {
            mShowViews[position % mShowViews.length].clickRightMonth();
        } else if (mDirection == SildeDirection.LEFT) {
            mShowViews[position % mShowViews.length].clickLeftMonth();
        }
        mDirection = SildeDirection.NO_SILDE;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void show() {
        super.show();
    }

    public void setMarkDates(List<Date> markDateList) {
        this.markDateList = markDateList;
        CalendarView calendarView = calendarViews[mCurrentIndex % calendarViews.length];
        if (defaultSeDate != null) {
            calendarView.setDefaultSelected(defaultSeDate);
        }
        if (markDateList != null) {
            calendarView.setMarkDate(markDateList);
        }
        calendarView.invalidate();
        defaultSeDate = null;
        markDateList = null;
    }

    /**
     * 设置默认选中的日期
     */
    public void setDefaultSelected(Date date) {
        this.defaultSeDate = date;
    }

    public void setIsShowBefore(boolean isShowBefore) {
        this.isShowBefore = isShowBefore;
    }

    public void showDialog() {
        this.show();
        CalendarView calendarView = calendarViews[mCurrentIndex % calendarViews.length];
        if (defaultSeDate != null) {
            calendarView.setDefaultSelected(defaultSeDate);
        }
        if (markDateList != null) {
            calendarView.setMarkDate(markDateList);
        }
        calendarView.invalidate();
        defaultSeDate = null;
        markDateList = null;
    }

    @Override
    public void onMonthChange(Date date) {
        calendarCenter.setText(formatMonth.format(date));
        currentDate = date;
        if (isShowBefore) {
            return;
        }
        if (isEarly(date)) {
            calendarLeft.setEnabled(false);
        } else {
            calendarLeft.setEnabled(true);
        }
    }

    /**
     * 是否比当前月早
     *
     * @return 早返回true
     */
    private boolean isEarly(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date today = calendar.getTime();
        try {
            Date t = formatMonth.parse(formatMonth.format(today));
            Date o = formatMonth.parse(formatMonth.format(date));
            if (o.compareTo(t) <= 0) {
                return true;
            }
        } catch (ParseException e) {
            Log.e(TAG, "", e);
        }
        return false;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_HOME) {
            this.dismiss();
            if (owner != null) {
                owner.onKeyDown(keyCode, event);
            }
            return false;
        }
        return false;
    }

    public void refresh() {
        if (calendarViews != null && calendarViews.length > 0) {
            for (CalendarView view : calendarViews) {
                view.postInvalidate();
            }
        }
    }

    public boolean isShowRedPoint(String key) {
        return false;
    }

    private OnQueryListener queryListener;

    public void setQueryListener(OnQueryListener queryListener) {
        this.queryListener = queryListener;
    }

    public interface OnQueryListener {
        void onQuery(Date month);
    }

}
