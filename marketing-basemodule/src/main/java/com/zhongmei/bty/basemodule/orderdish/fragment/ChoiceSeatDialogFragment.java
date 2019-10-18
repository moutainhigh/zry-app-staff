package com.zhongmei.bty.basemodule.orderdish.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.orderdish.adapter.TableSeatChoiceAdapter;
import com.zhongmei.bty.basemodule.orderdish.bean.DishQuantityBean;
import com.zhongmei.bty.basemodule.trade.enums.OperateType;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(resName = "trade_fragment_choiceseat_dialog")
public class ChoiceSeatDialogFragment extends CommonDialogFragment implements AdapterView.OnItemClickListener {

    @ViewById(resName = "tv_head_title")
    public TextView tv_headTitle;

    @ViewById(resName = "gv_seats")
    public GridView gv_seats;

    @ViewById(resName = "tv_operate_hint")
    public TextView tv_operateHint;

    private OperateType mOperateType;
    private List<TableSeat> mTableSeats;
    private DishQuantityBean mDishQuantityBean;
    private TableSeatChoiceAdapter mChoiceSeatAdapter;

    @AfterViews
    protected void initView() {
        super.initView();
        super.mNegativeButton.setText("");
        tv_operateHint.setText(getString(R.string.dinner_choice_seat_hint, mOperateType.desc()));
        mChoiceSeatAdapter = new TableSeatChoiceAdapter(getActivity().getApplicationContext(), mTableSeats, mDishQuantityBean.tableSeat);
        mPositiveButton.setTag(mChoiceSeatAdapter.getmCurTableSeat());
        gv_seats.setAdapter(mChoiceSeatAdapter);
        gv_seats.setOnItemClickListener(this);
        showHeaddTitle();
    }

    public void setmOperateType(OperateType mOperateType) {
        this.mOperateType = mOperateType;
    }


    public void setData(List<TableSeat> tableSeats, DishQuantityBean dishQuantityBean) {
        this.mTableSeats = tableSeats;
        this.mDishQuantityBean = dishQuantityBean;
    }

    private void showHeaddTitle() {
        if (tv_headTitle != null && !TextUtils.isEmpty(headTitle)) {
            tv_headTitle.setText(headTitle);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (mChoiceSeatAdapter != null) {
            mChoiceSeatAdapter.setCurTableSeat(mChoiceSeatAdapter.getItem(position));
            mPositiveButton.setTag(mChoiceSeatAdapter.getmCurTableSeat());
            mChoiceSeatAdapter.notifyDataSetChanged();
        }
    }

    public static class ChoiceSeatDialogFragmentBuilder extends CommonDialogFragmentBuilder {
        public ChoiceSeatDialogFragmentBuilder(Context context) {
            super(context);
        }

        @Override
        public CommonDialogFragment build() {
            ChoiceSeatDialogFragment fragment = new ChoiceSeatDialogFragment_();
            fragment.setArguments(mBundle);
            if (mNegativeListener != null) {
                fragment.setNegativeListener(mNegativeListener);
            }
            if (mpositiveLinstner != null) {
                fragment.setpositiveListener(mpositiveLinstner);
            }
            return fragment;
        }


    }


    @Override
    public void onClick(View view) {

        super.onClick(view);
    }
}
