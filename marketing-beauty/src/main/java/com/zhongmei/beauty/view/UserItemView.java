package com.zhongmei.beauty.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.beauty.entity.UserVo;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;



@EViewGroup(resName = "beauty_item_user")
public class UserItemView extends RelativeLayout {

    @ViewById(resName = "rl_parent")
    RelativeLayout rl_parent;

    @ViewById(resName = "cb_name")
    protected CheckBox cb_name;

    @ViewById(resName = "tv_status")
    protected TextView tv_status;

    @ViewById(resName = "tb_appoint")
    protected ToggleButton tb_appoint;

    private UserVo mUserVo;

    private OnUserItemCheckListener onUserCheckedListener;
    private boolean isHasPonitView = false;

    public UserItemView(Context context, boolean isHasPonitView) {
        super(context);
        this.isHasPonitView = isHasPonitView;
    }

    public UserItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UserItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public UserItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setOnUserCheckedListener(OnUserItemCheckListener onUserCheckedListener) {
        this.onUserCheckedListener = onUserCheckedListener;
    }

    public void refreshUI(UserVo userVo) {
        this.mUserVo = userVo;
        if (isHasPonitView) {
            tb_appoint.setVisibility(View.VISIBLE);
        } else {
            tb_appoint.setVisibility(View.GONE);
        }
        tb_appoint.setChecked(userVo.isOppoint());
        cb_name.setChecked(userVo.isChecked());
        cb_name.setText(userVo.getUser().getName());
        if (userVo.isFree()) {
            tv_status.setVisibility(View.GONE);
                        if (!userVo.isChild()) {
                setViewEnabled(true);
            } else {
                setViewEnabled(false);
            }
        } else {
            tv_status.setVisibility(View.VISIBLE);
        }
    }

    private void setViewEnabled(boolean enabled) {
        rl_parent.setEnabled(enabled);
        cb_name.setEnabled(enabled);
        tb_appoint.setEnabled(enabled);
    }

    @Click(resName = {"rl_parent", "cb_name", "tb_appoint"})
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.rl_parent) {
            mUserVo.setChecked(!cb_name.isChecked());
            if (!cb_name.isChecked()) {
                mUserVo.setOppoint(false);
            }
            if (onUserCheckedListener != null) {
                onUserCheckedListener.onCheckedChange(mUserVo, !cb_name.isChecked());
            }
        } else if (viewId == R.id.cb_name) {
            if (!cb_name.isChecked() && tb_appoint.isChecked()) {
                tb_appoint.setChecked(false);
                mUserVo.setOppoint(false);
            }
            mUserVo.setChecked(cb_name.isChecked());
            if (onUserCheckedListener != null) {
                onUserCheckedListener.onCheckedChange(mUserVo, cb_name.isChecked());
            }
        } else if (viewId == R.id.tb_appoint) {
            if (tb_appoint.isChecked()) {
                mUserVo.setOppoint(true);                mUserVo.setChecked(true);
                onUserCheckedListener.updateAppoint(mUserVo, true);

            } else {
                mUserVo.setOppoint(false);
                onUserCheckedListener.updateAppoint(mUserVo, false);
            }
        }
    }

    public interface OnUserItemCheckListener {
        void onCheckedChange(UserVo userVo, boolean isChecked);

                void updateAppoint(UserVo userVo, boolean isChcked);
    }
}
