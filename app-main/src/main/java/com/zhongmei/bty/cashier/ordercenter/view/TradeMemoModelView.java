package com.zhongmei.bty.cashier.ordercenter.view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.customer.entity.TakeawayMemo;
import com.zhongmei.bty.basemodule.customer.operates.TakeawayMemoDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.snack.orderdish.adapter.ExtraOrderAdapter;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.view_trade_memo_model)
public class TradeMemoModelView extends RecyclerView {

    private final static String TAG = TradeMemoModelView.class.getSimpleName();

    private final static int COL_COUNT = 2;

    @Bean
    ExtraOrderAdapter mExtraOrderAdapter;

    /**
     * 常用整单备注
     */
    private List<String> mCommonTradeRemarks;

    public TradeMemoModelView(Context context) {
        super(context);
    }

    public TradeMemoModelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TradeMemoModelView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @AfterViews
    void initViews() {
        setLayoutManager(new GridLayoutManager(getContext(), COL_COUNT));
        setAdapter(mExtraOrderAdapter);
        loadTradeRemark();
    }

    public void setOnItemClickListener(RecyclerViewBaseAdapter.OnRecyclerViewItemClickListener listener) {
        if (mExtraOrderAdapter != null) {
            mExtraOrderAdapter.setOnItemClickListener(listener);
        }
    }

    /**
     * 加载整单常用备注
     */
    private void loadTradeRemark() {
        AsyncTask<Void, Void, List<String>> asyncTask = new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... params) {
                List<String> commonTradeRemarks = new ArrayList<String>();
                try {
                    TakeawayMemoDal takeawayMemoDal = OperatesFactory.create(TakeawayMemoDal.class);
                    List<TakeawayMemo> orderMemos = takeawayMemoDal.getDataList();
                    if (Utils.isNotEmpty(orderMemos)) {
                        for (TakeawayMemo orderMemo : orderMemos) {
                            commonTradeRemarks.add(orderMemo.getMemoContent());
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                return commonTradeRemarks;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                mCommonTradeRemarks = strings;
                if (mExtraOrderAdapter != null) {
                    mExtraOrderAdapter.setItems(mCommonTradeRemarks);
                }
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncTask.execute();
        }
    }

}
