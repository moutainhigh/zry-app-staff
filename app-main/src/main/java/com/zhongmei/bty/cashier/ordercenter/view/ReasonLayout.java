package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.entity.ReasonSetting;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonSource;
import com.zhongmei.bty.basemodule.commonbusiness.enums.ReasonType;
import com.zhongmei.bty.basemodule.commonbusiness.operates.ReasonDal;
import com.zhongmei.bty.basemodule.trade.bean.Reason;
import com.zhongmei.bty.cashier.ordercenter.adapter.ReasonAdapter;
import com.zhongmei.bty.cashier.ordercenter.manager.KouBeiReasonManage;
import com.zhongmei.bty.cashier.ordercenter.view.MyEditText.OnFinishComposingListener;
import com.zhongmei.bty.cashier.ordercenter.view.ObservableScrollView.ScrollListener;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@SuppressWarnings("UnnecessaryUnboxing")
public class ReasonLayout extends LinearLayout implements OnFinishComposingListener {
    private static final String TAG = ReasonLayout.class.getName();
    private Context mContext;
    private List<ReasonSetting> reasonList;
    private RadioGroup radioGroup;
    private int mCurrentSource = 1;
    private int mCurrentType = -1;
    private String refuseMsg = "";
    private MyEditText user_defined_text;
    private TextView title;
    private ListView listView;
    private ReasonAdapter adapter;
    private ScrollView scrollView;
    private int srollLocation = 0;
    private int srollBeforeLocation = 0;
    private LinearLayout main_content;
    InputMethodManager mInputMethodManager;

    public ReasonLayout(Context context) {
        super(context);
    }

        public ReasonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUtil();
        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
                main_content = (LinearLayout) inflater.inflate(R.layout.order_center_operate_dialog_fragment_reason, null);
                listView = (ListView) main_content.findViewById(R.id.listView);
        user_defined_text = (MyEditText) main_content.findViewById(R.id.user_defined_text);
        user_defined_text.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null || listView.getCount() != 0) {
                    setCurrentPosion(-1);
                }
                user_defined_text.setFocusable(true);                user_defined_text.setFocusableInTouchMode(true);                user_defined_text.requestFocus();                user_defined_text.findFocus();                mInputMethodManager.showSoftInput(user_defined_text, InputMethodManager.SHOW_FORCED);            }
        });
        title = (TextView) main_content.findViewById(R.id.title);
        addView(main_content);
        reasonList = new ArrayList<ReasonSetting>();
            }

    private void initUtil() {
                mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public ReasonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
    }

    public void setTypeAndStart(int source, int type) {
        mCurrentSource = source;
        mCurrentType = type;

        this.setTitle(chooseTitle());
        startAsyncTask();
    }


    public void setDefinedGone() {
        if (user_defined_text != null) {
            user_defined_text.setVisibility(View.GONE);
        }
    }

    public void finishComposing() {
        Log.d(TAG, "finishComposing");
        user_defined_text.setFocusable(true);
        user_defined_text.setFocusableInTouchMode(true);
            }

    public void setScrollView(ObservableScrollView scrollView) {
        this.scrollView = scrollView;
        scrollView.setScrollListener(new ScrollListener() {
            @Override
            public void scrollYPostion(int position) {
                srollLocation = position;
            }
        });
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
        reasonList.clear();
        ReasonSource source = findReasonSource();
        ReasonType type = ReasonType.newReason(mCurrentType);
        reasonList = queryReason(source, type);
        loadData();
    }


    private ReasonSource findReasonSource() {
        if (mCurrentSource == ReasonSource.BAIDU_TAKEOUT.value().intValue()) {
            return ReasonSource.BAIDU_TAKEOUT;
        } else if (mCurrentSource == ReasonSource.KOUBEI.value()) {
            return ReasonSource.KOUBEI;
        }
        return ReasonSource.ZHONGMEI;
    }

    private List<ReasonSetting> queryReason(ReasonSource source, ReasonType type) {
        if (source == ReasonSource.KOUBEI) {
                        if (type == ReasonType.TRADE_REFUSED ||
                    type == ReasonType.TRADE_REPEATED ||
                    type == ReasonType.TRADE_RETURNED ||
                    type == ReasonType.REFUSE_RETURN) {
                return KouBeiReasonManage.newInstance().load(type);
            } else {
                source = ReasonSource.ZHONGMEI;
            }
        }
        ReasonDal reasonDal = OperatesFactory.create(ReasonDal.class);
        try {
            return reasonDal.findReasonSetting(source, type);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return Collections.emptyList();
    }

    private void loadData() {
        resetDialogUI();
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

    private void sort() {
        collectionSort();
    }

    private void setRaidoListener() {
    }

    private void resetDialogUI() {
        if (isReasonDataValid()) {
            sort();
                        initListview();
        } else {
                        main_content.removeView(listView);
        }
    }

    private void clearUserDifinedFocus() {

    }

    private void setCurrentPosion(int position) {
        adapter.setCurrentCheckedItem(position);
    }

    private void initListview() {
        adapter = new ReasonAdapter(mContext, reasonList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setCurrentPosion(position);
                user_defined_text.setText("");
                user_defined_text.setHint(getResources().getString(R.string.reason_type_self_define_reason));
                user_defined_text.setFocusable(false);
                if (mInputMethodManager.isActive()) {
                    mInputMethodManager.hideSoftInputFromWindow(user_defined_text.getWindowToken(), 0);                }
                            }
        });
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

    private int chargeId() {
        int id = 0;
        if (mCurrentSource == ReasonSource.ZHONGMEI.value().intValue()) {
            if (mCurrentType == ReasonType.TRADE_INVALID.value().intValue()) {
                id = 0 - ReasonType.TRADE_INVALID.value().intValue();
            } else if (mCurrentType == ReasonType.TRADE_REFUSED.value().intValue()) {
                id = 0 - ReasonType.TRADE_REFUSED.value().intValue();
            } else if (mCurrentType == ReasonType.TRADE_REPEATED.value().intValue()) {
                id = 0 - ReasonType.TRADE_REPEATED.value().intValue();
            } else if (mCurrentType == ReasonType.TRADE_FREE.value().intValue()) {
                id = 0 - ReasonType.TRADE_FREE.value().intValue();
            } else if (mCurrentType == ReasonType.ITEM_GIVE.value().intValue()) {
                id = 0 - ReasonType.ITEM_GIVE.value().intValue();
            } else if (mCurrentType == ReasonType.ITEM_RETURN_QTY.value().intValue()) {
                id = 0 - ReasonType.ITEM_RETURN_QTY.value().intValue();
            } else if (mCurrentType == ReasonType.TRADE_RETURNED.value().intValue()) {
                id = 0 - ReasonType.TRADE_RETURNED.value().intValue();
            } else if (mCurrentType == ReasonType.BOOKING_CANCEL.value().intValue()) {
                id = 0 - ReasonType.BOOKING_CANCEL.value().intValue();
            } else if (mCurrentType == ReasonType.BOOKING_REFUSED.value().intValue()) {
                id = 0 - ReasonType.BOOKING_REFUSED.value().intValue();
            } else if (mCurrentType == ReasonType.LAG_REASON.value().intValue()) {
                id = 0 - ReasonType.LAG_REASON.value().intValue();
            } else if (mCurrentType == ReasonType.DEPOSIT_RETURN.value().intValue()) {
                id = 0 - ReasonType.DEPOSIT_RETURN.value().intValue();
            } else if (mCurrentType == ReasonType.TRADE_DISCOUNT.value().intValue()) {
                id = 0 - ReasonType.TRADE_DISCOUNT.value().intValue();
            } else if (mCurrentType == ReasonType.ITEM_GIVE.value().intValue()) {
                id = 0 - ReasonType.ITEM_GIVE.value().intValue();
            } else if (mCurrentType == ReasonType.TRADE_BANQUET.value().intValue()) {
                id = 0 - ReasonType.TRADE_BANQUET.value().intValue();
            } else if (mCurrentType == ReasonType.INTEGRAL_MODIFY.value().intValue()) {
                id = 0 - ReasonType.INTEGRAL_MODIFY.value().intValue();
            }
        } else {
            id = -1;
        }

        return id;
    }

    private int chooseTitle() {
        int titleID = R.string.order_center_fragment_dialog_title;
        if (mCurrentSource == ReasonSource.ZHONGMEI.value().intValue()) {
            if (mCurrentType == ReasonType.TRADE_INVALID.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_Invalid;
            } else if (mCurrentType == ReasonType.TRADE_REFUSED.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_refuse;
            } else if (mCurrentType == ReasonType.TRADE_REPEATED.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title__anti_settlement;
            } else if (mCurrentType == ReasonType.TRADE_FREE.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_free;
            } else if (mCurrentType == ReasonType.ITEM_GIVE.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_part_free;
            } else if (mCurrentType == ReasonType.ITEM_RETURN_QTY.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_return_dishes;
            } else if (mCurrentType == ReasonType.TRADE_RETURNED.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_return;
            } else if (mCurrentType == ReasonType.BOOKING_CANCEL.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_call_off;
            } else if (mCurrentType == ReasonType.BOOKING_REFUSED.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_deny;
            } else if (mCurrentType == ReasonType.LAG_REASON.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_charge_account;
            } else if (mCurrentType == ReasonType.DEPOSIT_RETURN.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_part_of_the_return;
            } else if (mCurrentType == ReasonType.TRADE_DISCOUNT.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_discount;
            } else if (mCurrentType == ReasonType.ITEM_GIVE.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_part_discount;
            } else if (mCurrentType == ReasonType.TRADE_BANQUET.value().intValue()) {
                titleID = R.string.order_center_fragment_dialog_title_fete;
            }
        } else {
            titleID = R.string.order_center_fragment_dialog_title;
        }

        return titleID;
    }

    public Reason getReason() {
        ReasonSetting reason = null;
        boolean isSelfDifinedReason = false;
                ReasonSetting userDefinedReason = new ReasonSetting();
                        if (isReasonDataValid() && adapter != null && adapter.getCurrentCheckedItem() != -1 && reasonList.size() > 0) {
            reason = (ReasonSetting) adapter.getItem(adapter.getCurrentCheckedItem());
        } else {
            if (adapter != null && adapter.getCurrentCheckedItem() == -1
                    && !TextUtils.isEmpty(user_defined_text.getText().toString())
                    && reasonList.size() > 0
                    && !user_defined_text.getText().toString()
                    .equals(mContext.getResources().getString(R.string.reason_type_self_define_reason))) {
                                reason = userDefinedReason;
                isSelfDifinedReason = true;
            } else {
                if (adapter == null
                        && !TextUtils.isEmpty(user_defined_text.getText().toString())
                        && reasonList.size() == 0
                        && !user_defined_text.getText().toString()
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
            resultReason.setId(Long.valueOf(chargeId()));
            resultReason.setContent(user_defined_text.getText().toString());
            resultReason.setContentCode("OTHER_REASON");
        } else {
                        resultReason.setId(reason.getId());
            resultReason.setContent(reason.getContent());
            resultReason.setContentCode(reason.getContentCode());
        }
        isSelfDifinedReason = false;
        return resultReason;
    }

    public void setTitle(int titleID) {
        this.title.setText(titleID);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }


}
