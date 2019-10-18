package com.zhongmei.bty.mobilepay.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.basemodule.commonbusiness.adapter.ReasonAdapter;
import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.commonbusiness.operates.ReasonDal;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.view.MyEditText;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;




public class ReasonOptionDialog extends Dialog implements View.OnClickListener {
    private String TAG = ReasonOptionDialog.class.getSimpleName();
    private Context mContext;
    private List<ReasonSetting> reasonList;

    private ReasonSource mReasonSource = ReasonSource.ZHONGMEI;
    private ReasonType mCurrentType;

    private MyEditText inputMyET;
    private TextView title2;
    private ListView listView;
    private ReasonAdapter adapter;

    private int srollLocation = 0;
    private int srollBeforeLocation = 0;

    private InputMethodManager mInputMethodManager;

    public void setOperateListener(OperateListener operateListener) {
        this.mOperateListener = operateListener;
    }

    private OperateListener mOperateListener;

    private ReasonOptionDialog(Context context, int theme) {
        super(context, theme);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mContext = context;
        setContentView(R.layout.reason_operate_dialog_fragment);
    }

    public ReasonOptionDialog(Activity context, String title, String title2, ReasonType reasonType) {
        this(context, R.style.custom_alert_dialog);
        this.mCurrentType = reasonType;
        initView(title, title2, reasonType);
    }


        public void initView(String titleStr, String title2Str, ReasonType reasonType) {
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                this.mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        listView = (ListView) findViewById(R.id.listView);
        inputMyET = (MyEditText) findViewById(R.id.user_defined_text);
        TextView title = (TextView) findViewById(R.id.dialog_title);
        title2 = (TextView) findViewById(R.id.tv_tip);

        title.setText(titleStr);
        title2.setText(title2Str);

        View closeBT = findViewById(R.id.btn_close);
        if (closeBT != null) closeBT.setOnClickListener(this);

        View sureBT = findViewById(R.id.btn_ok);
        if (sureBT != null) sureBT.setOnClickListener(this);

        inputMyET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null || listView.getCount() != 0) {
                    setCurrentPosion(-1);
                }
                inputMyET.setFocusable(true);                inputMyET.setFocusableInTouchMode(true);                inputMyET.requestFocus();                inputMyET.findFocus();                mInputMethodManager.showSoftInput(inputMyET, InputMethodManager.SHOW_FORCED);            }
        });

        adapter = new ReasonAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setCurrentPosion(position);
                inputMyET.setText("");
                inputMyET.setHint(getContext().getString(R.string.reason_type_self_define_reason));
                inputMyET.setFocusable(false);
                if (mInputMethodManager.isActive()) {
                    mInputMethodManager.hideSoftInputFromWindow(inputMyET.getWindowToken(), 0);                }
            }
        });
        startAsyncTask();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_close) {
            dismiss();

        } else if (v.getId() == R.id.btn_ok) {
            if (this.mOperateListener != null)
                this.mOperateListener.onSuccess(getReason());
            dismiss();
        }
    }


    public void setDefinedGone() {
        if (inputMyET != null) {
            inputMyET.setVisibility(View.GONE);
        }
    }

    public void finishComposing() {
        Log.d(TAG, "finishComposing");
        inputMyET.setFocusable(true);
        inputMyET.setFocusableInTouchMode(true);
            }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        srollBeforeLocation = srollLocation;
        return super.dispatchTouchEvent(ev);
    }

    private int getListviewY() {
        Rect rect = new Rect();
        listView.getLocalVisibleRect(rect);
        return rect.bottom;
    }

    private void startAsyncTask() {
        new AsyncTask<Void, Void, List<ReasonSetting>>() {
            @Override
            protected List<ReasonSetting> doInBackground(Void... params) {
                List<ReasonSetting> list = null;
                try {
                    list = queryReason(mReasonSource, mCurrentType);
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
                return list;
            }

            protected void onPostExecute(List<ReasonSetting> data) {
                reasonList = data;
                resetDialogUI();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    private List<ReasonSetting> queryReason(ReasonSource source, ReasonType type) {
        ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
        try {
            return reasonDal.findReasonSetting(source, type);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return Collections.emptyList();
    }


    private boolean isReasonDataValid() {
        return reasonList != null && !reasonList.isEmpty();
    }

    private void collectionSort() {
        Collections.sort(reasonList, new Comparator<ReasonSetting>() {

            @Override
            public int compare(ReasonSetting lhs, ReasonSetting rhs) {
                if (lhs.getSort() > rhs.getSort()) {
                    return 1;

                } else if (lhs.getSort() < rhs.getSort()) {
                    return -1;
                }
                return 0;

            }
        });
    }


    private void resetDialogUI() {
        if (isReasonDataValid()) {
            collectionSort();
            initListview();
        } else {
            if (this.adapter != null)
                this.adapter.notifyDataSetChanged();
        }
    }


    private void setCurrentPosion(int position) {
        adapter.setCurrentCheckedItem(position);
    }

    private void initListview() {
        adapter.setData(reasonList);
        setListViewHeightBasedOnChildren(listView);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
                ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {             View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);             totalHeight += listItem.getMeasuredHeight();         }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
                        listView.setLayoutParams(params);
    }


    public Reason getReason() {
        ReasonSetting reason = null;
        boolean isSelfDifinedReason = false;
                ReasonSetting userDefinedReason = new ReasonSetting();
                if (isReasonDataValid() && adapter != null && adapter.getCurrentCheckedItem() != -1 && reasonList.size() > 0) {
            reason = (ReasonSetting) adapter.getItem(adapter.getCurrentCheckedItem());
        } else {
            if (adapter != null && adapter.getCurrentCheckedItem() == -1
                    && !TextUtils.isEmpty(inputMyET.getText().toString())
                    && reasonList.size() > 0
                    && !inputMyET.getText().toString()
                    .equals(mContext.getResources().getString(R.string.reason_type_self_define_reason))) {
                                reason = userDefinedReason;
                isSelfDifinedReason = true;
            } else {
                if (!TextUtils.isEmpty(inputMyET.getText().toString())
                        && !inputMyET.getText().toString()
                        .equals(mContext.getResources().getString(R.string.reason_type_self_define_reason))) {
                    reason = userDefinedReason;
                    isSelfDifinedReason = true;
                } else {
                    reason = null;
                }
            }
        }

        if (reason == null)
            return null;
        Reason resultReason = new Reason();
        if (isSelfDifinedReason) {
            resultReason.setId(Long.valueOf(mCurrentType.value()));
            resultReason.setContent(inputMyET.getText().toString());
        } else {
            resultReason.setId(reason.getId());
            resultReason.setContent(reason.getContent());
        }
        return resultReason;
    }

    public interface OperateListener {
        public boolean onSuccess(Reason reason);
    }
}
