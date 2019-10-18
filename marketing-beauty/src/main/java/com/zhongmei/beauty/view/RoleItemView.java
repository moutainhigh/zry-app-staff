package com.zhongmei.beauty.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.beauty.entity.UserVo;
import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.yunfu.context.session.core.user.Role;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;



@EViewGroup(resName = "beauty_item_role")
public class RoleItemView extends RelativeLayout {

    @ViewById(resName = "btn_name")
    protected Button btn_name;

    private Role mRole;

    private OnRoleCheckedListener onRoleCheckedListener;

    public RoleItemView(Context context) {
        super(context);
    }

    public RoleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnRoleCheckedListener(OnRoleCheckedListener onRoleCheckedListener) {
        this.onRoleCheckedListener = onRoleCheckedListener;
    }

    public RoleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoleItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void refreshUI(Role role, boolean isChecked) {
        this.mRole = role;
        btn_name.setText(mRole.getRoleName());
        btn_name.setSelected(isChecked);
        btn_name.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRoleCheckedListener != null) {
                    onRoleCheckedListener.onRoleChecked(mRole);
                }
            }
        });
    }


    public interface OnRoleCheckedListener {
        void onRoleChecked(Role role);
    }


}
