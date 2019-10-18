package com.zhongmei.beauty.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.beauty.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.Role;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.core.user.UserFunc;
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType;
import com.zhongmei.beauty.adapter.UserAdapter;
import com.zhongmei.beauty.entity.UserVo;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.view.recycler.RecyclerLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class BeautyWaiterView extends LinearLayout implements UserItemView.OnUserItemCheckListener {

    protected ViewGroup include_emptyStatus;

    protected RecyclerView lv_content;

    private UserAdapter mUserAdapter;

    private OnUserCheckedListener mOnUserCheckedListener;

    private int mIndetity = TradeUserType.SHOPOWER.value();

    private boolean isHasPonitView = false;

    private List<UserVo> mListUserVo;

    private Context mContext;

    public void setOnUserCheckedListener(OnUserCheckedListener onUserCheckedListener) {
        this.mOnUserCheckedListener = onUserCheckedListener;
    }

    public BeautyWaiterView(Context context) {
        super(context);
    }

    public BeautyWaiterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BeautyWaiterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeautyWaiterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public BeautyWaiterView(Context context, boolean isHasPointView) {
        super(context);
        this.mContext = context;
        this.isHasPonitView = isHasPointView;
        setupView();
    }

    private void setupView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.beauty_waiter_dialog_user, this, true);
        include_emptyStatus = (ViewGroup) view.findViewById(R.id.include_empty_status);
        lv_content = (RecyclerView) view.findViewById(R.id.lv_content);
        init();
    }

    protected void init() {
        LinearLayoutManager manager = new RecyclerLinearLayoutManager(getContext());
        lv_content.setLayoutManager(manager);
    }


    public void refreshView(Role role, UserVo users) {
        mListUserVo = getUserByIdentity(role, users);
        UserAdapter adapter = getAdapter();
        adapter.setItems(mListUserVo);
        lv_content.setAdapter(adapter);
        if (Utils.isEmpty(mListUserVo)) {
                        lv_content.setVisibility(View.GONE);
            include_emptyStatus.setVisibility(View.VISIBLE);
        } else {
            lv_content.setVisibility(View.VISIBLE);
            include_emptyStatus.setVisibility(View.GONE);
        }
    }


    protected List<UserVo> getUserByIdentity(Role role, UserVo userVo) {
        List<UserVo> listUserVo = new ArrayList<>();
        List<User> userList = Session.getFunc(UserFunc.class).getUsers();
        if (Utils.isEmpty(userList)) {
            return listUserVo;
        }
        for (User user : userList) {
            UserVo vo = new UserVo(user);
            if (userVo != null && user.getId().equals(userVo.getUser().getId()) && userVo.getTradeUserType() == mIndetity) {
                vo.setTradeUserType(mIndetity);
                vo.setChecked(true);
            }

            if (role != null && user.getRoleId() == role.getId()) {
                vo.getUser().setRoleName(role.getRoleName());
                listUserVo.add(vo);
            }

        }
        return listUserVo;
    }


    protected UserAdapter getAdapter() {
        if (mUserAdapter == null) {
            mUserAdapter = new UserAdapter(getContext(), isHasPonitView);
            mUserAdapter.setOnUserItemCheckListener(this);
        }
        return mUserAdapter;
    }

    @Override
    public void onCheckedChange(UserVo userVo, boolean isChecked) {
        updateItemChecked(userVo);
        userVo.setTradeUserType(mIndetity);
        if (mOnUserCheckedListener != null) {
            mOnUserCheckedListener.onUserCheckData(userVo);
        }
    }

    @Override
    public void updateAppoint(UserVo userVo, boolean isChcked) {

    }

    private void updateItemChecked(UserVo userVo) {
        if (mListUserVo == null) {
            return;
        }
        for (UserVo user : mListUserVo) {
            if (user.getUser().getId() != userVo.getUser().getId()) {
                user.setChecked(false);
                user.setOppoint(false);
            } else {
                user.setChecked(userVo.isChecked());
                user.setOppoint(userVo.isOppoint());
            }
        }
        mUserAdapter.notifyDataSetChanged();
    }

    public interface OnUserCheckedListener {
        void onUserCheckData(UserVo user);
    }


    public int getIdentity() {
        return mIndetity;
    }

}
