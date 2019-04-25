package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.enums.TradeUserType;
import com.zhongmei.beauty.utils.TradeUserUtil;
import com.zhongmei.beauty.adapter.UserAdapter;
import com.zhongmei.beauty.view.UserItemView;
import com.zhongmei.beauty.entity.UserVo;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.view.recycler.RecyclerLinearLayoutManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(resName = "beauty_viewgroup_user")
public class BeautyBaseWaiter extends LinearLayout implements UserItemView.OnUserItemCheckListener {

    @ViewById(resName = "include_empty_status")
    protected ViewGroup include_emptyStatus;

    @ViewById(resName = "layout_content")
    protected LinearLayout layout_content;

    @ViewById(resName = "lv_content")
    protected RecyclerView lv_content;

    @ViewById(resName = "beauty_user_title")
    protected TextView tv_title;

    private Map<Long, UserVo> mapCheckedUserVo = new HashMap<Long, UserVo>();

    private UserAdapter mUserAdapter;

    private OnUserCheckedListener onUserCheckedListener;
    private int mTitleResId;

    private boolean isHasPonitView = false;
    //针对产品或服务选择
    private IShopcartItemBase shopcartItemBase;
    List<TradeUser> tradeUserList;
    //是否是整单操作
    private boolean isDefine = false;

    private List<UserVo> mListUserVo;
    private ItemType mItemType;

    /**
     * @param context
     * @param titleResId     title resid
     * @param isHasPointView 是否有指定view
     */
    public BeautyBaseWaiter(Context context, int titleResId, boolean isHasPointView) {
        super(context);
        mTitleResId = titleResId;
        this.isHasPonitView = isHasPointView;
    }

    public BeautyBaseWaiter(Context context) {
        super(context);
    }

    public BeautyBaseWaiter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BeautyBaseWaiter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeautyBaseWaiter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @AfterViews
    protected void init() {
        tv_title.setText(getResources().getString(mTitleResId));

        LinearLayoutManager manager = new RecyclerLinearLayoutManager(getContext());
        lv_content.setLayoutManager(manager);

    }

    public void refreshView(final Long tradeId) {
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, List<UserVo>>() {
            @Override
            protected List<UserVo> doInBackground(Void... voids) {
                List<UserVo> userVos = TradeUserUtil.getUserVos(tradeId, isDefine, tradeUserList, shopcartItemBase, mItemType);
                return userVos;
            }

            @Override
            protected void onPostExecute(List<UserVo> userVos) {
                super.onPostExecute(userVos);
                mListUserVo = userVos;
                refreshView();
            }
        });

    }

    private void refreshView() {
        UserAdapter adapter = getAdapter();
        adapter.setItems(mListUserVo);
        lv_content.setAdapter(adapter);

        if (Utils.isEmpty(mListUserVo)) {
            //设置空态页
            layout_content.setVisibility(View.GONE);
            include_emptyStatus.setVisibility(View.VISIBLE);
        } else {
            layout_content.setVisibility(View.VISIBLE);
            include_emptyStatus.setVisibility(View.GONE);
        }
    }

    public void setOnUserCheckedListener(OnUserCheckedListener onUserCheckedListener) {
        this.onUserCheckedListener = onUserCheckedListener;
    }

    public void initItemData(IShopcartItemBase shopcartItemBase, ItemType itemType) {
        this.shopcartItemBase = shopcartItemBase;
        mItemType = itemType;
        isDefine = false;
    }

    public void initTradeuserData(List<TradeUser> tradeUserList) {
        this.tradeUserList = tradeUserList;
        isDefine = true;
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
        if (isDefine) {
            if (isChecked) {
                TradeUserUtil.addTradeUser(tradeUserList, userVo.getUser(), null);
            } else {
                TradeUserUtil.removeTradeUser(tradeUserList, userVo.getUser());
            }
        } else {
            if (isChecked) {
                TradeUserUtil.addTradeItemUsers(userVo.getUser(), shopcartItemBase, userVo.isOppoint());
            } else {
                TradeUserUtil.removeTradeItemusers(userVo.getUser(), shopcartItemBase);
            }
        }
        updateItemChecked(userVo);
        if (onUserCheckedListener != null) {
            onUserCheckedListener.onUserCheckedChange(isDefine);
        }
    }

    @Override
    public void updateAppoint(UserVo userVo, boolean isChcked) {
        if (!isDefine) {
            TradeUserUtil.updateTradeItemUsers(userVo.getUser(), shopcartItemBase, isChcked);
            updateItemChecked(userVo);
        }
        if (onUserCheckedListener != null) {
            onUserCheckedListener.onUserCheckedChange(isDefine);
        }
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
        void onUserCheckedChange(boolean isDefine);
    }


}
