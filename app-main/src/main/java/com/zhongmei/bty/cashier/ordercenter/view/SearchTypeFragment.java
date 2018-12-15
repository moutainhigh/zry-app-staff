package com.zhongmei.bty.cashier.ordercenter.view;

import android.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.cashier.ordercenter.adapter.SearchTypeAdapter;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.context.util.Utils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.order_center_search_type_layout)
public class SearchTypeFragment extends BasicDialogFragment {

    @ViewById(R.id.order_center_search_type_view)
    RecyclerView mRecylerView;

    @Bean
    SearchTypeAdapter mSearchTypeAdapter;

    View mRelativeView;
    private List<String> contents;
    private int selectPosition;
    private boolean isFromSnack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        isFromSnack = bundle.getBoolean("isFromSnack");
        setStyle(DialogFragment.STYLE_NORMAL, R.style.common_transparent_dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(DensityUtil.dip2px(MainApplication.getInstance(), 144), -2);
        if (mRelativeView != null) {
            int[] position = new int[2];
            mRelativeView.getLocationOnScreen(position);
            int x;
            if (isFromSnack) {
                x = DensityUtil.dip2px(MainApplication.getInstance(), 22);
            } else {
                x = DensityUtil.dip2px(MainApplication.getInstance(), 94);
            }
            int y = position[1] + mRelativeView.getHeight() + DensityUtil.dip2px(MainApplication.getInstance(), 14);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = x;
            wl.y = y;
            wl.gravity = Gravity.START | Gravity.TOP;
            window.setAttributes(wl);
        }
    }

    @AfterViews
    void intViews() {
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecylerView.setAdapter(mSearchTypeAdapter);
        mRecylerView.addItemDecoration(new SearchTypeItemDivider(getActivity(), R.drawable.divide_line));
        if (!Utils.isEmpty(contents)) {
            ViewGroup.LayoutParams params = mRecylerView.getLayoutParams();
            params.height = DensityUtil.dip2px(MainApplication.getInstance(), 42) * contents.size();
            mRecylerView.setLayoutParams(params);
        }
        mSearchTypeAdapter.setItems(contents);
        mSearchTypeAdapter.setSelectItem(selectPosition);
        mSearchTypeAdapter.setOnItemClickListener(new SearchTypeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(((SearchTypeView) view).getContent(), position);
                }
                dismiss();
            }
        });
    }

    public void showAsDown(FragmentTransaction transaction, String tag, View view) {
        mRelativeView = view;
        show(transaction, tag);
    }

    public void setContent(String[] contents, int selectPosition) {
        if (contents != null) {
            this.contents = Arrays.asList(contents);
        }
        this.selectPosition = selectPosition;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(String content, int position);
    }
}
