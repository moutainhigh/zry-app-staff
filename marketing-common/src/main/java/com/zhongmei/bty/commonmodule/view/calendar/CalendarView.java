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


public class CalendarView extends View {
    private final static String TAG = "anCalendar";

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    final SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());

    private Date selectedStartDate;

    private Date selectedEndDate;

    private static Date curDate;
    private Date today;
    private static Date downDate;
    private Date showFirstDate, showLastDate;
    private int downIndex = -1;
    private Calendar calendar;

    private Surface surface;

        private int maxCell = 42;

    private int[] date = new int[maxCell];
    private int curStartIndex, curEndIndex;

    private final List<Date> markDates;

    private int touchSlop;

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
                mRedPointPaint = new Paint();
        mRedPointPaint.setAntiAlias(true);
        mRedPointPaint.setColor(Color.RED);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                                surface.height = (int) (getResources().getDisplayMetrics().heightPixels * 7 / 10);
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            surface.width = MeasureSpec.getSize(widthMeasureSpec);
        }

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
        int backHeight = DensityUtil.dip2px(BaseApplication.sInstance, 50);        canvas.drawRect(0, surface.monthHeight + 1, surface.width, surface.monthHeight + backHeight, surface.backPaint);
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

                calculateDate();
                if (!isToday(downDate, new Date(System.currentTimeMillis()))) {
            drawDownOrSelectedBg(canvas);
        }
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
                        if (isLastMonth(i)) {
                color = surface.borderColor;
                continue;
            } else if (isNextMonth(i)) {
                color = surface.borderColor;
                continue;
            }
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
        monthStart -= 1;         curStartIndex = monthStart;
        date[monthStart] = 1;
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
                calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i < monthDay; i++) {
            date[monthStart + i] = i + 1;
        }
        curEndIndex = monthStart + monthDay;
                for (int i = monthStart + monthDay; i < maxCell; i++) {
            date[i] = i - (monthStart + monthDay) + 1;
        }

        if (curEndIndex < maxCell) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, date[41]);
        showLastDate = calendar.getTime();
        updateMonth();
    }


    private void drawCellText(Canvas canvas, int index, String text, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        surface.datePaint.setColor(color);
        float cellY =
                surface.monthHeight + surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 3 / 4f;
        float cellX = (surface.cellWidth * (x - 1)) + (surface.cellWidth - surface.datePaint.measureText(text)) / 2f;
        canvas.drawText(text, cellX, cellY, surface.datePaint);
                if (drawRedPoint(monthFormat.format(curDate) + "-" + ( (text.length() == 1) ? ("0" + text) : text))){
            Rect rect = new Rect();
            surface.datePaint.getTextBounds(text, 0, text.length(), rect);
            int w = rect.width();
            int h = rect.height();
            canvas.drawCircle(cellX + w + 6, cellY - h, 4, mRedPointPaint);
        }
    }


    private void drawCellBg(Canvas canvas, int index, int color, boolean isToday) {
        if (index == -1) {
            return;
        }
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        surface.cellBgPaint.setColor(color);

        float cellY = surface.monthHeight + surface.weekHeight + (float) ((y - 1.0 / 3) * surface.cellHeight);
        float cellX = (float) (surface.cellWidth * (x - 0.5));
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


    private void drawMarkBg(Canvas canvas, int index) {

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
        return i % 7 + 1;     }

    private int getYByIndex(int i) {
        if (curStartIndex >= 7) {
            return i / 7;         }
        return i / 7 + 1;     }

        public String getYearAndmonth() {
        calendar.setTime(curDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + "-" + month;
    }

        public String clickLeftMonth() {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, -1);
        curDate = calendar.getTime();

        invalidate();
        return getYearAndmonth();
    }

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

        public void setCalendarData(Date date) {
        calendar.setTime(date);
        invalidate();
    }

        public void getCalendatData() {
        calendar.getTime();
    }

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


    public void clearSelect() {
        if (downIndex == -1 && downDate == null) {
            return;
        }
        downIndex = -1;
        downDate = null;
        invalidate();
    }

        public interface OnItemClickListener {

        void OnItemClick(Date downDate);
    }

    public interface OnMonthChangeListener {
        void onMonthChange(Date date);
    }

        private boolean isEarlyByIndex(int index) {
        Date date = getDateByIndex(index);
        return isEarly(date);
    }


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


    private class Surface {
        public float density;

        public int width;
        public int height;
        public float monthHeight;
        public float weekHeight;
        public float cellWidth;
        public float cellHeight;
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

        public Paint linePaint;
        public Paint backPaint;
        public Paint weekPaint;

        public Paint datePaint;

        public Paint markPaint;

        public Paint monthChangeBtnPaint;

        public Paint cellBgPaint;

        public Path boxPath;
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
            cellHeight = (height - monthHeight - weekHeight) / 6f - 8;            cellWidth = width / 7f;
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
