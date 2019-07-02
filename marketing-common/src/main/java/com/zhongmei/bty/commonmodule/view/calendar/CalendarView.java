package com.zhongmei.bty.commonmodule.view.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.zhongmei.yunfu.commonmodule.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.util.DensityUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日历控件 功能：获得点选的日期区间
 */
public class CalendarView extends View {
    private final static String TAG = "anCalendar";

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

    private Date selectedStartDate;

    private Date selectedEndDate;

    private static Date curDate; // 当前日历显示的月

    private Date today; // 今天的日期文字显示红色

    private static Date downDate; // 手指按下状态时临时日期

    private Date showFirstDate, showLastDate; // 日历显示的第一个日期和最后一个日期

    private int downIndex = -1; // 按下的格子索引

    private Calendar calendar;

    private Surface surface;

    // 最多显示多少个日期
    private int maxCell = 42;

    private int[] date = new int[maxCell]; // 日历显示数字

    private int curStartIndex, curEndIndex; // 当前显示的日历起始的索引

    /**
     * 标注日期
     */
    private final List<Date> markDates;

    private int touchSlop;

    // 给控件设置监听事件
    private OnItemClickListener onItemClickListener;

    private OnMonthChangeListener monthChangeListener;

    private float mDownX;

    private float mDownY;

    private boolean isShowBefore = false;
    private Paint mRedPointPaint;

    public CalendarView(Context context) {
        super(context);
        markDates = new ArrayList<Date>();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        markDates = new ArrayList<Date>();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        init();
    }

    /**
     * 设置标准日期
     *
     * @param markDates
     * @author daixj@shishike.com
     */
    public void setMarkDate(List<Date> markDates) {
        if (markDates != null) {
            this.markDates.clear();
            this.markDates.addAll(markDates);
        }
    }

    public void isShowBefore(boolean isShowBefore) {
        this.isShowBefore = isShowBefore;
    }

    private void init() {
        curDate = selectedStartDate = selectedEndDate = today = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        surface = new Surface();
        surface.density = getResources().getDisplayMetrics().density;
        setBackgroundColor(surface.bgColor);
        // 红点的相关设置
        mRedPointPaint = new Paint();
        mRedPointPaint.setAntiAlias(true);
        mRedPointPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // surface.width =
        // getResources().getDisplayMetrics().widthPixels *
        // 1 / 2;
        surface.height = (int) (getResources().getDisplayMetrics().heightPixels * 7 / 10);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            surface.width = MeasureSpec.getSize(widthMeasureSpec);
        }
        // if (View.MeasureSpec.getMode(heightMeasureSpec)
        // == View.MeasureSpec.EXACTLY) {
        // surface.height =
        // View.MeasureSpec.getSize(heightMeasureSpec);
        // }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(surface.width, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(surface.height, MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "[onLayout] changed:" + (changed ? "new size" : "not change") + " left:" + left + " top:" + top
                + " right:" + right + " bottom:" + bottom);
        if (changed) {
            surface.init();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int backHeight = DensityUtil.dip2px(BaseApplication.sInstance, 50);// 星期灰色背景的高度
        canvas.drawRect(0, surface.monthHeight + 1, surface.width, surface.monthHeight + backHeight, surface.backPaint);
        canvas.drawLine(0, surface.monthHeight + 1, surface.width, surface.monthHeight + 1, surface.linePaint);
        canvas.drawLine(0,
                surface.monthHeight + backHeight + 1,
                surface.width,
                surface.monthHeight + backHeight + 1,
                surface.linePaint);

        float weekTextY = surface.monthHeight + surface.weekHeight / 2f;
        for (int i = 0; i < surface.weekText.length; i++) {
            float weekTextX =
                    i * surface.cellWidth + (surface.cellWidth - surface.weekPaint.measureText(surface.weekText[i])) / 2f;
            canvas.drawText(surface.weekText[i], weekTextX, weekTextY, surface.weekPaint);
        }

        // 计算日期
        calculateDate();
        // 按下状态，选择状态背景色
        if (!isToday(downDate, new Date(System.currentTimeMillis()))) {
            drawDownOrSelectedBg(canvas);
        }
        // write date number
        // today index
        int todayIndex = -1;
        calendar.setTime(curDate);
        String curYearAndMonth = calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH);
        calendar.setTime(today);
        String todayYearAndMonth = calendar.get(Calendar.YEAR) + "" + calendar.get(Calendar.MONTH);
        if (curYearAndMonth.equals(todayYearAndMonth)) {
            int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
            todayIndex = curStartIndex + todayNumber - 1;
        }
        for (int i = 0; i < maxCell; i++) {
            int color = surface.textColor;
            // 上一个月 与下一个月的日期不画
            if (isLastMonth(i)) {
                color = surface.borderColor;
                continue;
            } else if (isNextMonth(i)) {
                color = surface.borderColor;
                continue;
            }
            // 将今天之前的日期置灰色
            if (!isShowBefore && curYearAndMonth.equals(todayYearAndMonth) && i < todayIndex) {
                color = surface.borderColor;
            }

            if (todayIndex != -1 && i == todayIndex) {
                color = surface.todayNumberColor;
                drawCellBg(canvas, todayIndex, surface.todayBg, true);
            }
            if (downIndex != -1 && (downIndex == i) && isMonthEqual(downDate, curDate)) {
                color = surface.todayNumberColor;
            }
            if (date[i] != 0) {
                drawCellText(canvas, i, date[i] + "", color);
            }
            drawMarkBg(canvas, i);
        }
        super.onDraw(canvas);
    }

    private void calculateDate() {
        calendar.setTime(curDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "day in week:" + dayInWeek);
        int monthStart = dayInWeek;
        if (monthStart == 1) {
            monthStart = 8;
        }
        monthStart -= 1; // 以日为开头-1，以星期一为开头-2
        curStartIndex = monthStart;
        date[monthStart] = 1;
        // last month
        if (monthStart > 0) {
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
            for (int i = monthStart - 1; i >= 0; i--) {
                date[i] = dayInmonth;
                dayInmonth--;
            }
            calendar.set(Calendar.DAY_OF_MONTH, date[0]);
        }

        showFirstDate = calendar.getTime();
        // this month
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i < monthDay; i++) {
            date[monthStart + i] = i + 1;
        }
        curEndIndex = monthStart + monthDay;
        // next month
        for (int i = monthStart + monthDay; i < maxCell; i++) {
            date[i] = i - (monthStart + monthDay) + 1;
        }

        if (curEndIndex < maxCell) {
            // 显示了下一月的
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, date[41]);
        showLastDate = calendar.getTime();
        updateMonth();
    }

    /**
     * @param canvas
     * @param index
     * @param text
     */
    private void drawCellText(Canvas canvas, int index, String text, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        surface.datePaint.setColor(color);
        float cellY =
                surface.monthHeight + surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 3 / 4f;
        float cellX = (surface.cellWidth * (x - 1)) + (surface.cellWidth - surface.datePaint.measureText(text)) / 2f;
        canvas.drawText(text, cellX, cellY, surface.datePaint);
        // 计算是否需要绘制红点
        if (drawRedPoint(monthFormat.format(curDate) + "-" + ( (text.length() == 1) ? ("0" + text) : text))){
            Rect rect = new Rect();
            surface.datePaint.getTextBounds(text, 0, text.length(), rect);
            int w = rect.width();
            int h = rect.height();
            canvas.drawCircle(cellX + w + 6, cellY - h, 4, mRedPointPaint);
        }
    }

    /**
     * @param canvas
     * @param index
     * @param color
     */
    private void drawCellBg(Canvas canvas, int index, int color, boolean isToday) {
        if (index == -1) {
            return;
        }
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        surface.cellBgPaint.setColor(color);

        float cellY = surface.monthHeight + surface.weekHeight + (float) ((y - 1.0 / 3) * surface.cellHeight);
        float cellX = (float) (surface.cellWidth * (x - 0.5));
        //如果日期大于10 修正圆心坐标
        if (x >= 10) {
            cellX += 12;
        }
        canvas.drawCircle(cellX, cellY, (float) (surface.cellHeight / 3), surface.cellBgPaint);
        if (isToday) {
            float tcellY = surface.monthHeight + surface.weekHeight + (float) ((y - 0.7) * surface.cellHeight);
            float tcellX = (float) (surface.cellWidth * (x - 0.3));
            canvas.drawText(getResources().getText(R.string.today).toString(), tcellX, tcellY, surface.cellBgPaint);
        }
    }

    /**
     * 画标准的背景
     *
     * @param canvas
     * @author daixj@shishike.com
     */
    private void drawMarkBg(Canvas canvas, int index) {
        /** 设置标注日期颜色 */
        if (markDates != null) {
            Date indexDate = getDateByIndex(index);
            if (indexDate == null) {
                return;
            }
            int x = getXByIndex(index);
            int y = getYByIndex(index);
            float cellY = surface.monthHeight + surface.weekHeight + (float) ((y - 0.7) * surface.cellHeight);
            float cellX = (float) (surface.cellWidth * (x - 0.3));
            for (Date date : markDates) {
                if (isDateEqual(indexDate, date) && !isDateEqual(today, date)) {
                    canvas.drawCircle(cellX, cellY, 4, surface.markPaint);
                    break;
                }
            }
        }
    }

    /**
     * 比较两个日期的日是否相等
     *
     * @param d1
     * @param d2
     * @return
     * @author daixj@shishike.com
     */
    private boolean isDateEqual(Date d1, Date d2) {
        return dateFormat.format(d1).equals(dateFormat.format(d2));
    }

    private boolean isMonthEqual(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        return monthFormat.format(d1).equals(monthFormat.format(d2));
    }

    private boolean isToday(Date d1, Date d2) {
        if (d1 == null || d2 == null) {
            return false;
        }
        return dateFormat.format(d1).equals(dateFormat.format(d2));
    }

    private void drawDownOrSelectedBg(Canvas canvas) {
        // down and not up
        if (downDate != null && isMonthEqual(downDate, curDate)) {
            if (downIndex == -1) {
                downIndex = getIndexByDate(downDate);
            }
            drawCellBg(canvas, downIndex, surface.cellDownColor, false);
        }
    }

    private void findSelectedIndex(int startIndex, int endIndex, Calendar calendar, int[] section) {
        for (int i = startIndex; i < endIndex; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, date[i]);
            Date temp = calendar.getTime();
            // Log.d(TAG, "temp:" + temp.toLocaleString());
            if (temp.compareTo(selectedStartDate) == 0) {
                section[0] = i;
            }
            if (temp.compareTo(selectedEndDate) == 0) {
                section[1] = i;
                return;
            }
        }
    }

    public Date getSelectedStartDate() {
        return selectedStartDate;
    }

    public Date getSelectedEndDate() {
        return selectedEndDate;
    }

    private boolean isLastMonth(int i) {
        if (i < curStartIndex) {
            return true;
        }
        return false;
    }

    private boolean isNextMonth(int i) {
        if (i >= curEndIndex) {
            return true;
        }
        return false;
    }

    private int getXByIndex(int i) {
        return i % 7 + 1; // 1 2 3 4 5 6 7
    }

    private int getYByIndex(int i) {
        if (curStartIndex >= 7) {
            return i / 7; // 1 2 3 4 5
        }
        return i / 7 + 1; // 1 2 3 4 5 6
    }

    // 获得当前应该显示的年月
    public String getYearAndmonth() {
        calendar.setTime(curDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + "-" + month;
    }

    // 上一月
    public String clickLeftMonth() {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, -1);
        curDate = calendar.getTime();

        invalidate();
        return getYearAndmonth();
    }

    // 下一月
    public String clickRightMonth() {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        curDate = calendar.getTime();
        invalidate();
        return getYearAndmonth();
    }

    private void updateMonth() {
        if (monthChangeListener == null) {
            return;
        }
        monthChangeListener.onMonthChange(curDate);
    }

    // 设置日历时间
    public void setCalendarData(Date date) {
        calendar.setTime(date);
        invalidate();
    }

    // 获取日历时间
    public void getCalendatData() {
        calendar.getTime();
    }

    // 给控件设置监听事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener, OnMonthChangeListener changeListener) {
        this.onItemClickListener = onItemClickListener;
        this.monthChangeListener = changeListener;
    }

    public void setDefaultSelected(Date date) {
        downIndex = -1;
        downDate = curDate = date;
        calendar.setTime(curDate);
    }

    private void setSelectedDateByCoor(float x, float y) {
        // change month
        // cell click down
        if (y > surface.monthHeight + surface.weekHeight) {
            int m = (int) (Math.floor(x / surface.cellWidth) + 1);
            int n =
                    (int) (Math.floor((y - (surface.monthHeight + surface.weekHeight)) / Float.valueOf(surface.cellHeight)) + 1);

            if (curStartIndex >= 7) {
                n += 1;
            }
            int tempIndex = (n - 1) * 7 + m - 1;
            downIndex = tempIndex;
            if (tempIndex < curStartIndex || tempIndex >= curEndIndex) {
                return;
            }

            Date tempDate = getDateByIndex(tempIndex);
            if (!isShowBefore && isEarly(tempDate)) {
                return;
            }
            downDate = tempDate;
            invalidate();
        }
    }

    private Date getDateByIndex(int index) {
        calendar.setTime(curDate);
        if (isLastMonth(index)) {
            calendar.add(Calendar.MONTH, -1);
            return null;
        } else if (isNextMonth(index)) {
            calendar.add(Calendar.MONTH, 1);
            return null;
        }
        calendar.set(Calendar.DAY_OF_MONTH, date[index]);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 根据日期获得索引
     *
     * @param date
     * @return
     * @author daixj@shishike.com
     */
    private int getIndexByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dateIndex = calendar.get(Calendar.DAY_OF_MONTH) + curStartIndex - 1;
        for (int i = curStartIndex; i <= curEndIndex; i++) {
            if (dateIndex == i) {
                return i;
            }
        }
        return -1;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                if (mDownY > (surface.monthHeight + surface.weekHeight) || curStartIndex > 7) {
                    setSelectedDateByCoor(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mDownY <= (surface.monthHeight + surface.weekHeight) || isLastMonth(downIndex)
                        || isNextMonth(downIndex)) {
                    break;
                }
                if (!isShowBefore && isEarlyByIndex(downIndex)) {
                    break;
                }
                float disX = event.getX() - mDownX;
                float disY = event.getY() - mDownY;
                if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
                    if (onItemClickListener != null && downDate != null) {
                        onItemClickListener.OnItemClick(downDate);
                        downIndex = -1;
                    }
                } else {
                    clearSelect();
                }

                break;
        }
        return true;
    }

    /**
     * 取消选中的日期
     *
     * @Author renlx@shishike.com
     * @Title: clearSelect
     * @Return void 返回类型
     */
    public void clearSelect() {
        if (downIndex == -1 && downDate == null) {
            return;
        }
        downIndex = -1;
        downDate = null;
        invalidate();
    }

    // 监听接口
    public interface OnItemClickListener {

        void OnItemClick(Date downDate);
    }

    public interface OnMonthChangeListener {
        void onMonthChange(Date date);
    }

    // 是否比当前早
    private boolean isEarlyByIndex(int index) {
        Date date = getDateByIndex(index);
        return isEarly(date);
    }

    /**
     * 是否比今天更早
     *
     * @param date
     * @return
     * @author daixj@shishike.com
     */
    private boolean isEarly(Date date) {
        if (date == null) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date today = calendar.getTime();
        try {
            Date t = dateFormat.parse(dateFormat.format(today));
            Date o = dateFormat.parse(dateFormat.format(date));
            if (o.compareTo(t) < 0) {
                return true;
            }
        } catch (ParseException e) {
            Log.e(TAG, "", e);
        }
        return false;
    }

    /**
     * 1. 布局尺寸 2. 文字颜色，大小 3. 当前日期的颜色，选择的日期颜色
     */
    private class Surface {
        public float density;

        public int width; // 整个控件的宽度

        public int height; // 整个控件的高度

        public float monthHeight; // 显示月的高度

        public float weekHeight; // 显示星期的高度

        public float cellWidth; // 日期方框宽度

        public float cellHeight; // 日期方框高度

        public float borderWidth;

        public int bgColor = Color.parseColor("#FFFFFF");

        private int textColor = Color.BLACK;

        private int btnColor = Color.parseColor("#666666");

        private int borderColor = Color.parseColor("#AEAFAF");

        public int todayNumberColor = Color.WHITE;

        public int todayBg = Color.parseColor("#FF7E60");

        public int cellDownColor = Color.parseColor("#36CEC5");

        public int cellSelectedColor = Color.parseColor("#227700");

        public Paint borderPaint;

        public Paint monthPaint;

        public Paint linePaint;// 星期的灰线

        public Paint backPaint;// 星期的灰色背景

        public Paint weekPaint;

        public Paint datePaint;

        public Paint markPaint;

        public Paint monthChangeBtnPaint;

        public Paint cellBgPaint;

        public Path boxPath; // 边框路径

        public String[] weekText = {
                getResources().getString(R.string.sunday),
                getResources().getString(R.string.monday),
                getResources().getString(R.string.tuesday),
                getResources().getString(R.string.wednesday),
                getResources().getString(R.string.thursday),
                getResources().getString(R.string.friday),
                getResources().getString(R.string.saturday)
        };

        public void init() {
            Resources res = getResources();

            width -= 10;
            height = height - 20;
            float temp = height / 7f;
            monthHeight = 0;
            weekHeight = (float) ((temp + temp * 0.3f) * 0.7);
            cellHeight = (height - monthHeight - weekHeight) / 6f - 8;//modify by goujiabo on 2019/5/14 考虑到第六行可能无法展示出来，所以将每行的高度缩小8px
            cellWidth = width / 7f;
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Style.STROKE);
            borderWidth = (float) (0.5 * density);
            borderWidth = borderWidth < 1 ? 1 : borderWidth;
            borderWidth = 0;
            borderPaint.setStrokeWidth(borderWidth);
            monthPaint = new Paint();
            monthPaint.setColor(textColor);
            monthPaint.setAntiAlias(true);
            float textSize = cellHeight * 0.4f;
            Log.d(TAG, "text size:" + textSize);
            monthPaint.setTextSize(textSize);
            monthPaint.setTypeface(Typeface.DEFAULT_BOLD);
            backPaint = new Paint();
            backPaint.setColor(res.getColor(R.color.gray_bg));
            backPaint.setStyle(Style.FILL);
            linePaint = new Paint();
            linePaint.setColor(res.getColor(R.color.line_gray));
            linePaint.setStrokeWidth(DensityUtil.dip2px(BaseApplication.sInstance, 2));
            weekPaint = new Paint();
            weekPaint.setColor(res.getColor(R.color.text_calendar_week_gray));
            weekPaint.setAntiAlias(true);
            weekPaint.setTextSize(DensityUtil.sp2px(BaseApplication.sInstance, 20));
            datePaint = new Paint();
            datePaint.setColor(textColor);
            datePaint.setAntiAlias(true);
            float cellTextSize = cellHeight * 0.3f;
            datePaint.setTextSize(cellTextSize);
            boxPath = new Path();
            boxPath.rLineTo(width, 0);
            boxPath.moveTo(0, monthHeight + weekHeight);
            boxPath.rLineTo(width, 0);
            for (int i = 1; i < 6; i++) {
                boxPath.moveTo(0, monthHeight + weekHeight + i * cellHeight);
                boxPath.rLineTo(width, 0);
                boxPath.moveTo(i * cellWidth, monthHeight);
                boxPath.rLineTo(0, height - monthHeight);
            }
            boxPath.moveTo(6 * cellWidth, monthHeight);
            boxPath.rLineTo(0, height - monthHeight);
            monthChangeBtnPaint = new Paint();
            monthChangeBtnPaint.setAntiAlias(true);
            monthChangeBtnPaint.setStyle(Style.FILL_AND_STROKE);
            monthChangeBtnPaint.setColor(btnColor);
            cellBgPaint = new Paint();
            cellBgPaint.setAntiAlias(true);
            cellBgPaint.setTextSize(12f);
            cellBgPaint.setStyle(Style.FILL);
            cellBgPaint.setColor(cellSelectedColor);

            markPaint = new Paint();
            markPaint.setAntiAlias(true);
            markPaint.setTextSize(12f);
            markPaint.setStyle(Style.FILL);
            markPaint.setColor(todayBg);
        }
    }

    public boolean drawRedPoint(String key) {
        return false;
    }
}
