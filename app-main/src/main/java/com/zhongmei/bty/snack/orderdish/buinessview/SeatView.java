package com.zhongmei.bty.snack.orderdish.buinessview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.cashier.shoppingcart.vo.ChooseVo;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;


@EViewGroup(R.layout.experience_layout_seat)
public class SeatView extends LinearLayout {

    public interface SeatClickListener {
        void onSeatClick(ChooseVo<TableSeat> chooseVo);
    }

    private static final String TAG = SeatView.class.getSimpleName();

    @ViewById(R.id.experience_title)
    TextView experience_title;
    @ViewById(R.id.experience_layout)
    LinearLayout experience_layout;

    @ViewById(R.id.btn_ok)
    Button btnOk;

    private ArrayList<ChooseVo<TableSeat>> mPropertyGroupVos = new ArrayList<>();

    private SeatClickListener mListener;

    private boolean isBatchMode = false;

    public SeatView(Context context) {
        super(context);
    }

    public SeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SeatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SeatView(Context context, boolean isBatchMode) {
        super(context);
        this.isBatchMode = isBatchMode;
    }

    @AfterViews
    public void initView() {
        setOrientation(VERTICAL);

        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);

        if (isBatchMode) {
            btnOk.setVisibility(View.VISIBLE);
        } else {
            btnOk.setVisibility(View.GONE);
        }
    }

    public void setTitle(int stringResId) {
        experience_title.setText(stringResId);
    }

    public void setListener(SeatClickListener mListener) {
        this.mListener = mListener;
    }

    public void setList(List<TableSeat> tasteGroupVo, TradeItemExtraDinner tableSeatSelected) {
        mPropertyGroupVos.clear();
        if (tasteGroupVo != null) {
            for (TableSeat tableSeat : tasteGroupVo) {
                boolean isSelected = isItemSelected(tableSeatSelected, tableSeat);
                mPropertyGroupVos.add(new ChooseVo<>(tableSeat, isSelected));
            }
        }

        inflateRecipe();
    }

    private boolean isItemSelected(TradeItemExtraDinner tableSeatSelected, TableSeat tableSeat) {
        if (tableSeatSelected != null) {
            if (tableSeatSelected.getSeatId() != null
                    && tableSeatSelected.getSeatId().equals(tableSeat.getId())
                    && tableSeatSelected.getStatusFlag() == StatusFlag.VALID) {
                return true;
            }
        }
        return false;
    }

    private void inflateRecipe() {
        experience_layout.removeAllViews();
        if (mPropertyGroupVos != null && mPropertyGroupVos.size() > 0) {
                        int rowCount = mPropertyGroupVos.size();
            for (int i = 0; i < rowCount; i++) {
                View rootView =
                        LayoutInflater.from(getContext()).inflate(R.layout.experience_item_seat, experience_layout, false);

                ChooseVo<TableSeat> chooseVo = mPropertyGroupVos.get(i);
                setItemView(rootView, i, chooseVo);
                experience_layout.addView(rootView);
            }
        }
    }


    private void setItemView(View rootView, int rowIndex, ChooseVo<TableSeat> chooseVo) {
        TextView tvName = (TextView) rootView.findViewById(R.id.text_name);
        tvName.setText(chooseVo.getProperty().getSeatName());
        rootView.setOnClickListener(newItemClick(rowIndex));
        rootView.setTag(rowIndex);
        rootView.setBackgroundResource(R.drawable.ic_dish_property_item_bg);
        if (chooseVo.isSelected()) {
            rootView.setSelected(true);
            if (mListener != null) {
                mListener.onSeatClick(chooseVo);
            }
        } else {
            rootView.setSelected(false);
        }
    }

    private OnClickListener newItemClick(final int rowIndex) {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseVo<TableSeat> chooseVo = mPropertyGroupVos.get(rowIndex);
                if (chooseVo.isSelected()) {
                    chooseVo.setSelected(false);
                    setPropertySelectable(v, rowIndex, false);
                } else {
                    chooseVo.setSelected(true);
                    setPropertySelectable(v, rowIndex, true);
                }

                if (mListener != null) {
                    mListener.onSeatClick(chooseVo);
                }
            }
        };
    }



    private void setPropertySelectable(View v, int pos, boolean select) {
        int childCount = experience_layout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = experience_layout.getChildAt(i);
            child.setSelected(false);
            mPropertyGroupVos.get(i).setSelected(false);
        }

        View itemSelected = experience_layout.getChildAt(pos);
        if (itemSelected != null) {
            itemSelected.setSelected(select);
            mPropertyGroupVos.get(pos).setSelected(select);
        }
    }
}
