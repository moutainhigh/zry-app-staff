package com.zhongmei.bty.commonmodule.view.calendar;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhongmei.yunfu.commonmodule.R;
import com.zhongmei.yunfu.context.util.DateTimeUtils;

import java.util.ArrayList;

public class TimePickerView extends RelativeLayout {


    private static final String TAG = TimePickerView.class.getSimpleName();

    protected int textSize = WheelListView.TEXT_SIZE;
    protected int textColorNormal = WheelListView.TEXT_COLOR_NORMAL;
    protected int textColorFocus = WheelListView.TEXT_COLOR_FOCUS;
    protected int offset = WheelListView.ITEM_OFF_SET;
    protected boolean canLoop = true;
    protected boolean wheelModeEnable = false;
    protected boolean weightEnable = false;
    protected boolean canLinkage = false;//是否联动

    private ArrayList<String> hours = new ArrayList<>();
    private ArrayList<String> minutes = new ArrayList<>();
    private ArrayList<String> hoursAndUnit = new ArrayList<>();
    private ArrayList<String> minutesAndUnit = new ArrayList<>();
    private int selectedHourIndex = 0, selectedMinuteIndex = 0;
    //不带单位
    private String selectedHour = "", selectedMinute = "";
    protected LineConfig lineConfig;
    private OnWheelListener onWheelListener;
    private int startHour, startMinute = 0;
    private int endHour, endMinute = 59;
    private int stepMinute = 1, stepHour = 1;//时间间隔


    private WheelListView hourView;
    private WheelListView minuteView;

    public TimePickerView(Context context) {
        super(context);
        initView(context);
    }

    public TimePickerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimePickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initData() {
        startHour = 0;
        endHour = 23;
        setTimeRangeStart(0, 0);
        setTimeRangeEnd(23, 0);
        if (hours.size() == 0) {
            Log.v(TAG, "init hours before make view");
            initHourData();
        }
        if (minutes.size() == 0) {
            Log.v(TAG, "init minutes before make view");
            changeMinuteData(DateTimeUtils.trimZero(selectedHour));
        }
    }

    public void setSelectedTime(String hour, String minute) {
        selectedHour = hour;
        selectedMinute = minute;
        hourView.setSelectedItem(selectedHour+getContext().getString(R.string.hour_unit));
        minuteView.setSelectedItem(selectedMinute+getContext().getString(R.string.minute_unit));
    }


    private void initView(Context context) {
        initData();
        View view = View.inflate(context, R.layout.layout_time_picker_view, null);
        hourView = (WheelListView) view.findViewById(R.id.wv_hour);
        minuteView = (WheelListView) view.findViewById(R.id.wv_minute);

        hourView.setCanLoop(canLoop);
        hourView.setTextSize(textSize);//must be called before setDateList
        hourView.setSelectedTextColor(textColorFocus);
        hourView.setUnSelectedTextColor(textColorNormal);
        hourView.setLineConfig(lineConfig);
        hourView.setItems(hoursAndUnit, selectedHour+getContext().getString(R.string.hour_unit));
        hourView.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
            @Override
            public void onItemSelected(int index, String item) {
                selectedHourIndex = index;
                selectedMinuteIndex = 0;
                selectedHour = hours.get(index);
                if (onWheelListener != null) {
                    item = hours.get(index);
                    onWheelListener.onHourWheeled(index, item);
                }
                if (!canLinkage) {
                    return;
                }
//                        LogUtils.verbose(this, "change minutes after hour wheeled");
                changeMinuteData(DateTimeUtils.trimZero(item));
                minuteView.setItems(minutes, selectedMinuteIndex);
            }
        });


        //分钟
        minuteView.setCanLoop(canLoop);
        minuteView.setTextSize(textSize);//must be called before setDateList
        minuteView.setSelectedTextColor(textColorFocus);
        minuteView.setUnSelectedTextColor(textColorNormal);
        minuteView.setLineConfig(lineConfig);
        minuteView.setItems(minutesAndUnit, selectedMinute+getContext().getString(R.string.minute_unit));
        minuteView.setOnWheelChangeListener(new WheelListView.OnWheelChangeListener() {
            @Override
            public void onItemSelected(int index, String item) {
                selectedMinuteIndex = index;
                selectedMinute = minutes.get(index);
                if (onWheelListener != null) {
                    item = minutes.get(index);
                    onWheelListener.onMinuteWheeled(index, item);
                }
            }
        });

        addView(view);

    }


    private void changeMinuteData(int selectedHour) {
        if (startHour == endHour) {
            if (startMinute > endMinute) {
                int temp = startMinute;
                startMinute = endMinute;
                endMinute = temp;
            }
            for (int i = startMinute; i <= endMinute; i += stepMinute) {
                minutes.add(DateTimeUtils.fillZero(i));
                minutesAndUnit.add(DateTimeUtils.fillZero(i) + getContext().getString(R.string.minute_unit));
            }
        } else if (selectedHour == startHour) {
            for (int i = startMinute; i <= 59; i += stepMinute) {
                minutes.add(DateTimeUtils.fillZero(i));
                minutesAndUnit.add(DateTimeUtils.fillZero(i) + getContext().getString(R.string.minute_unit));
            }
        } else if (selectedHour == endHour) {
            for (int i = 0; i <= endMinute; i += stepMinute) {
                minutes.add(DateTimeUtils.fillZero(i));
                minutesAndUnit.add(DateTimeUtils.fillZero(i) + getContext().getString(R.string.minute_unit));
            }
        } else {
            for (int i = 0; i <= 59; i += stepMinute) {
                minutes.add(DateTimeUtils.fillZero(i));
                minutesAndUnit.add(DateTimeUtils.fillZero(i) + getContext().getString(R.string.minute_unit));
            }
        }
        if (minutes.indexOf(selectedMinute) == -1) {
            //当前设置的分钟不在指定范围，则默认选中范围开始的分钟
            selectedMinute = minutes.get(0);
        }
    }


    /**
     * 设置范围：开始的时分
     */
    public void setTimeRangeStart(int startHour, int startMinute) {
        boolean illegal = false;
        if (startHour < 0 || startMinute < 0 || startMinute > 59) {
            illegal = true;
        }
        if (startHour >= 24) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("Time out of range");
        }
        this.startHour = startHour;
        this.startMinute = startMinute;
    }

    /**
     * 设置范围：结束的时分
     */
    public void setTimeRangeEnd(int endHour, int endMinute) {
        boolean illegal = false;
        if (endHour < 0 || endMinute < 0 || endMinute > 59) {
            illegal = true;
        }
        if (endHour >= 24) {
            illegal = true;
        }
        if (illegal) {
            throw new IllegalArgumentException("Time out of range");
        }
        this.endHour = endHour;
        this.endMinute = endMinute;
        initHourData();
    }

    private void initHourData() {
        for (int i = startHour; i <= endHour; i += stepHour) {
            hours.add(DateTimeUtils.fillZero(i));
            String hour = DateTimeUtils.fillZero(i) + getContext().getString(R.string.hour_unit);
            hoursAndUnit.add(hour);
        }
        if (hours.indexOf(selectedHour) == -1) {
            //当前设置的小时不在指定范围，则默认选中范围开始的小时
            selectedHour = hours.get(0);
        }
    }


    /**
     * 设置文字大小
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置未选中文字颜色
     */
    public void setUnSelectedTextColor(@ColorInt int unSelectedTextColor) {
        this.textColorNormal = unSelectedTextColor;
    }

    /**
     * 设置选中文字颜色
     */
    public void setSelectedTextColor(@ColorInt int selectedTextColor) {
        this.textColorFocus = selectedTextColor;
    }

    /**
     * 设置分隔线是否可见
     */
    public void setLineVisible(boolean lineVisible) {
        if (null == lineConfig) {
            lineConfig = new LineConfig();
        }
        lineConfig.setVisible(lineVisible);
    }

    /**
     * 设置分隔阴影是否可见
     * 暂时去掉此功能
     */
//    public void setShadowVisible(boolean shadowVisible) {
//        if (null == lineConfig) {
//            lineConfig = new LineConfig();
//        }
//        lineConfig.setShadowVisible(shadowVisible);
//    }

    /**
     * 设置是否自动联动
     */
    public void setCanLinkage(boolean canLinkage) {
        this.canLinkage = canLinkage;
    }

    /**
     * 设置分隔线颜色
     */
    public void setLineColor(@ColorInt int lineColor) {
        if (null == lineConfig) {
            lineConfig = new LineConfig();
        }
        lineConfig.setVisible(true);
        lineConfig.setColor(lineColor);
    }

    /**
     * 设置分隔线配置项，设置null将隐藏分割线及阴影
     */
    public void setLineConfig(@Nullable LineConfig config) {
        if (null == config) {
            lineConfig = new LineConfig();
            lineConfig.setVisible(false);
            lineConfig.setShadowVisible(false);
        } else {
            lineConfig = config;
        }
    }

    /**
     * 设置选项偏移量，可用来要设置显示的条目数，范围为1-3。
     * 1显示3条、2显示5条、3显示7条
     */
    public void setOffset(@IntRange(from = 1, to = 3) int offset) {
        this.offset = offset;
    }

    /**
     * 设置是否禁用循环
     * true 循环 false 不循环
     */
    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    /**
     * 设置是否启用ios滚轮模式
     */
    public void setWheelModeEnable(boolean wheelModeEnable) {
        this.wheelModeEnable = wheelModeEnable;
    }

    /**
     * 线性布局设置是否启用权重
     * true 启用 false 自适应width
     */
    public void setWeightEnable(boolean weightEnable) {
        this.weightEnable = weightEnable;
    }


    public interface OnWheelListener {

        void onHourWheeled(int index, String hour);

        void onMinuteWheeled(int index, String minute);

    }

    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }
}
