package com.zhongmei.bty.dinner.table.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.table.bean.AddItemBatchBean;
import com.zhongmei.bty.commonmodule.view.AutofitTextView;
import com.zhongmei.bty.commonmodule.util.SpendTimeFormater;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.add_item_batch_view)
public class AddItemBatchView extends LinearLayout {
    @ViewById(R.id.root_ll)
    public LinearLayout rootLL;//根目录

    @ViewById(R.id.content_ll)
    LinearLayout contentLL;//内容区

    List<AddItemBatchBean> addItemBatchBeans;

    public int choosePos;
    LayoutInflater inflater;

    private ItemClick itemClick;

    public AddItemBatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflater = LayoutInflater.from(getContext());
    }

    @AfterViews
    public void initialView() {

    }

    public void setData(List<AddItemBatchBean> addItemBatchBeans, final int choosePosition) {
        if (contentLL.getChildCount() != 0) {
            contentLL.removeAllViews();
        }

        this.addItemBatchBeans = addItemBatchBeans;
        this.choosePos = choosePosition;
        for (int i = 0; i < addItemBatchBeans.size(); i++) {
            final AddItemBatchBean bean = addItemBatchBeans.get(i);
            View itemView = inflater.inflate(R.layout.add_item_batch_item, null);
            contentLL.addView(itemView);

            ImageView tradeChooseIv = (ImageView) itemView.findViewById(R.id.trade_choose_iv);
            AutofitTextView snTv = (AutofitTextView) itemView.findViewById(R.id.sn);
            TextView timeTextView = (TextView) itemView.findViewById(R.id.time);
//            TextView unitTv = (TextView) itemView.findViewById(R.id.unit);
//            View timeLayout = itemView.findViewById(R.id.time_layout);


            //加菜单序号
            snTv.setTextColor(ViewUtils.COLOR_TRADE_UNISSUED);
            snTv.setText(bean.getSequenceNumber());


            //获取加菜单时间
            timeTextView.setBackgroundColor(ViewUtils.COLOR_TRADE_UNISSUED);
            int spendTime = (int) (System.currentTimeMillis() - bean.getTime()) / (60 * 1000);
            if (spendTime < 0) {
                spendTime = -1;
            }
            //设置时间ø
            timeTextView.setText(SpendTimeFormater.format(spendTime));
//            unitTv.setText(formater.unit);

            final int position = i;
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (choosePos != position) {
                        //选中处理
                        if (choosePos != -1) {
                            contentLL.getChildAt(choosePos).findViewById(R.id.trade_choose_iv).setVisibility(View.INVISIBLE);
                        }
                        contentLL.getChildAt(position).findViewById(R.id.trade_choose_iv).setVisibility(View.VISIBLE);

                        itemClick.onItemCick(position, bean);
                        choosePos = position;
                    }


                }
            });


            tradeChooseIv.setVisibility(choosePosition == i ? View.VISIBLE : View.INVISIBLE);

        }

    }

    public interface ItemClick {
        void onItemCick(int position, AddItemBatchBean bean);
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }
}