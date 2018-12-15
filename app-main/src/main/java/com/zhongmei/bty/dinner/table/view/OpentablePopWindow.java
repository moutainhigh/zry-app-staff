package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.resp.UserActionEvent;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.dinner.table.manager.OpenTableManager;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.vo.DinnertableVo;

import de.greenrobot.event.EventBus;

/**
 * @Date 2016/7/21
 * @Description:桌台开台人数修改弹窗
 */
public class OpentablePopWindow extends PopupWindow implements View.OnTouchListener, View.OnClickListener {

    private static final String TAG = OpentablePopWindow.class.getSimpleName();

    public static int ADD_BUTTON_WIDTH = 56;
    public static int ADD_BUTTON_HEIGHT = 48;

    public static String QUICK_OPEN_TABLE = "quick_open_table";
    public static boolean isQuickOpentable = false;//是否是快速开台

    private Context context;

    private int contentWidth;

    private ImageView minusCustomerNumIv;

    private ImageView addCustomerNumIv;

    private EditText customerNumEdit;

    private Button opentableBtn;

    private static OpentablePopWindow opentablePopWindow;

    private int customerNum = 2;// 客人数

    private DinnertableModel model;

    private OpenTableManager openTableManager;//开台操作封装

    private BusinessType mBusinessType;

    public static OpentablePopWindow getInstance(Context context) {
        if (opentablePopWindow == null) {
            opentablePopWindow = new OpentablePopWindow(context);
        }
        opentablePopWindow.setContext(context);
        return opentablePopWindow;
    }

    public void setmBusinessType(BusinessType mBusinessType) {
        this.mBusinessType = mBusinessType;
    }

    public OpentablePopWindow(Context context/*, View parentView, int contentWidth*/) {
        super();
        this.context = context;
        initialView(context);
    }


    private void initialView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.opentable_pop_window, null);
        setContentView(view);
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setTouchable(true);

        setBackgroundDrawable(new ColorDrawable(0));
        setOutsideTouchable(true);

        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        addCustomerNumIv = (ImageView) view.findViewById(R.id.add_customer_iv);
        minusCustomerNumIv = (ImageView) view.findViewById(R.id.minus_customer_iv);
        customerNumEdit = (EditText) view.findViewById(R.id.customer_num_tv);
        opentableBtn = (Button) view.findViewById(R.id.open_table_btn);

        addCustomerNumIv.setOnClickListener(this);
        minusCustomerNumIv.setOnClickListener(this);
        opentableBtn.setOnClickListener(this);
        customerNumEdit.addTextChangedListener(customerNumWatcher);
        setFocusable(true);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setTouchListener(customerNumEdit);
//		setAnimationStyle(R.style.dinner_table_info_waiterwindow_style);
    }


    public void show(View parentView) {
//		hide();
        if (parentView != null) {
            if (!isShowing()) {
                customerNumEdit.setText(String.valueOf(model.getNumberOfSeats()));
                int xOff = DensityUtil.dip2px(context, 15);//-43
                int yOff = DensityUtil.dip2px(context, 70);//153
                showAsDropDown(parentView, xOff, -yOff);
            }

        }
    }

    public void hide() {
        if (isShowing()) {
            dismiss();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (inRangeOfView2(event)) {
            return false;
        } else {
            hide();
            return true;
        }

    }


    private boolean inRangeOfView(View view, MotionEvent ev) {
        View contentView = getContentView();
        int x = contentView.getPaddingLeft();
        int y = contentView.getPaddingTop();
        View contentView2 = contentView.findViewById(R.id.content_ll);
        int width = contentView2.getWidth();
        int height = contentView2.getHeight();
        if (ev.getX() < x || ev.getX() > (x + width) || ev.getY() < y || ev.getY() > (y + height)) {
            return false;
        }
        return true;
    }

    private boolean inRangeOfView2(MotionEvent ev) {
        View contentView = getContentView();
        int[] location = new int[2];
        contentView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        int width = contentView.getWidth();
        int height = contentView.getHeight();
        if (ev.getX() < x || ev.getX() > (x + width) || ev.getY() < y || ev.getY() > (y + height)) {
            return false;
        }
        return true;
    }

    /**
     * @Date 2016年4月25日
     * @Description:
     * @Param
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_customer_iv:
                if (TextUtils.isEmpty(customerNumEdit.getText().toString())) {
                    ToastUtil.showShortToast(R.string.dinner_table_info_customer_number_toast);
                    return;
                }

                if (customerNum < 9999) {//最多只能输入4位
                    customerNum++;
                    customerNumEdit.setText(String.valueOf(customerNum));
                }

                break;
            case R.id.minus_customer_iv:
                if (TextUtils.isEmpty(customerNumEdit.getText().toString())) {
                    ToastUtil.showShortToast(R.string.dinner_table_info_customer_number_toast);
                    return;
                }

                if (customerNum > 0)
                    customerNum--;
                customerNumEdit.setText(String.valueOf(customerNum));
                break;
            case R.id.open_table_btn:
                if (!ClickManager.getInstance().isClicked()) {
                    UserActionEvent.start(UserActionEvent.DINNER_TABLE_OPEN);
                    MobclickAgentEvent.onEvent(context, MobclickAgentEvent.dinnerTableClickQuickOpentableButton);
                    VerifyHelper.verifyAlert((FragmentActivity) context, DinnerApplication.PERMISSION_DINNER_CREATE,
                            new VerifyHelper.Callback() {
                                @Override
                                public void onPositive(User user, String code, Auth.Filter filter) {
                                    super.onPositive(user, code, filter);
                                    finishOpenTable();
                                }
                            });
                }
                break;
            default:
                break;
        }

    }

    public void post(Object obj) {
        EventBus.getDefault().post(obj);
    }

    private TextWatcher customerNumWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == null || TextUtils.isEmpty(s.toString())) {
                customerNum = 0;
                customerNumEdit.setText(String.valueOf(0));
                return;
            }

            customerNum = 0;
            try {
                customerNum = Integer.valueOf(s.toString());
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

        }
    };

    public void setModel(DinnertableModel model) {
        this.model = model;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 点击EditText不弹出软键盘（屏蔽软键盘）
     *
     * @param input
     */
    public void setTouchListener(final EditText input) {
        input.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                v.requestFocus();
                if (input.getText() != null) {
                    input.setSelection(input.getText().length());
                }
                return true;
            }
        });
    }

    private void finishOpenTable() {
        if (customerNum == 0) {
            ToastUtil.showLongToast(R.string.dinner_opentable_valide);
            return;
        }

//		TableInfoContentBean.needJumpToDishWindow=true;
        isQuickOpentable = true;
        OpenTableManager manager = new OpenTableManager(new DinnertableVo(model), getEmptyTradeDefaultCustomerNumber(), context, mBusinessType);
        manager.finishOpenTable();
    }

    public static void release() {
        if (opentablePopWindow != null && opentablePopWindow.isShowing()) {
            opentablePopWindow.hide();
        }
        opentablePopWindow = null;
    }

    private int getEmptyTradeDefaultCustomerNumber() {
        if (model != null && mBusinessType != BusinessType.BUFFET) {//自助餐默认人数为0
            return model.getNumberOfSeats();
        } else {
            return 0;
        }
    }

}
