package com.zhongmei.bty.common.view;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;

import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

public class DatePickerDialogFragment extends BasicDialogFragment implements
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = DatePickerDialogFragment.class.getSimpleName();

    public static String YEAR = "YEAR";
    public static String MONTH = "MONTH";
    public static String DAY = "DAY_OF_MONTH";
    DateSetListener mListener;

    static class MyDatePickerDialog extends DatePickerDialog {

        public MyDatePickerDialog(Context context, OnDateSetListener callBack,
                                  int year, int monthOfYear, int dayOfMonth) {
            super(context, callBack, year, monthOfYear, dayOfMonth);
            // TODO Auto-generated constructor stub
        }

        public MyDatePickerDialog(Context context, int theme,
                                  OnDateSetListener callBack, int year, int monthOfYear,
                                  int dayOfMonth) {
            super(context, theme, callBack, year, monthOfYear, dayOfMonth);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onStop() {
            // wo should not call the onDateSet when we cancel.
            // super.onStop();
        }
    }

    public static interface DateSetListener {
        void OnDateSet(String date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = getArguments().getInt(YEAR, c.get(Calendar.YEAR));
        int month = getArguments().getInt(MONTH, c.get(Calendar.MONTH));
        int day = getArguments().getInt(DAY, c.get(Calendar.DAY_OF_MONTH));

        DatePickerDialog dialog = new MyDatePickerDialog(getActivity(),
                AlertDialog.THEME_HOLO_LIGHT, this, year, month, day);
        return dialog;
    }

    public static void show(FragmentManager ft, String date,
                            DateSetListener dateSetListener) {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(date)) {
            String[] dates = date.split("-");
            if (dates.length == 3) {
                try {
                    bundle.putInt(YEAR, Integer.parseInt(dates[0]));
                    bundle.putInt(MONTH, Integer.parseInt(dates[1]) - 1);
                    bundle.putInt(DAY, Integer.parseInt(dates[2]));
                } catch (NumberFormatException e) {
                    Log.e(TAG, e.getMessage(), e);
                    bundle.clear();
                }

            }
        }
        DatePickerDialogFragment dialogFragment = new DatePickerDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.setListener(dateSetListener);
        dialogFragment.show(ft, dialogFragment.getClass().getSimpleName());
    }

    private void setListener(DateSetListener dateSetListener) {
        mListener = dateSetListener;

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear,
                          int dayOfMonth) {
        if (mListener != null) {
            mListener.OnDateSet("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
        }
    }
}