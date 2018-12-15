package com.zhongmei.bty.dinner.table.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.cashier.util.TradeSourceUtils;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.bty.dinner.table.DinnertableFragment;
import com.zhongmei.bty.dinner.table.TableInfoContentBean;
import com.zhongmei.bty.dinner.table.TableInfoFragment;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.bty.basemodule.trade.bean.DinnertableTradeVo;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.trade_list_layout)
public class TradeGridView extends LinearLayout/* implements AdapterView.OnItemClickListener*/ {
    @ViewById(R.id.trade_gv)
    GridView tradeGridView;
    TradeAdapter adapter;
    Activity activity;

    List<DinnertableTradeVo> data;

    ItemClickLisenter itemClickLisenter;

    public TradeGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void initial() {
        adapter = new TradeAdapter(getContext());
        tradeGridView.setAdapter(adapter);
//        tradeGridView.setOnItemClickListener(this);
    }


    public void setData(Activity activity, List<DinnertableTradeVo> data, DinnertableTradeVo tableTradeVo) {
        this.activity = activity;
        adapter.setData(data, tableTradeVo);
    }

    /**
     * @Date 2016/9/28
     * @Description:选择位置刷新
     * @Param
     * @Return
     */
    public void choosePosition(int position) {
        adapter.curTableTradeVo = (DinnertableTradeVo) adapter.getItem(position);
        adapter.notifyDataSetChanged();
    }


    public void setItemClickListener(ItemClickLisenter listener) {
        itemClickLisenter = listener;
    }

    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        refreshTradeInWindow(position);
    }*/

    private class TradeAdapter extends BaseAdapter {
        List<DinnertableTradeVo> data;
        LayoutInflater inflater;
        DinnertableTradeVo curTableTradeVo;// 记录选中位置

        public TradeAdapter(Context context) {
            inflater = LayoutInflater.from(context);

        }

        public void setData(List<DinnertableTradeVo> data, DinnertableTradeVo tradeTableVo) {
            this.data = data;
            this.curTableTradeVo = tradeTableVo;
            notifyDataSetChanged();
        }


        public List<DinnertableTradeVo> getData() {
            return data;
        }

        @Override
        public int getCount() {
            if (data != null) {
                return data.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int i) {
            if (data != null) {
                return data.get(i);
            } else {
                return null;
            }

        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View contentView, ViewGroup viewGroup) {

            ViewHolder viewHolder = null;
            if (contentView == null) {
                contentView = (ViewGroup) inflater.inflate(R.layout.dinner_table_info_order_item, null);
                viewHolder = new ViewHolder();
                contentView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) contentView.getTag();
            viewHolder.tradeView = (DinnertableTradeView) contentView.findViewById(R.id.trade_view2);
            viewHolder.tradeTypeTv = (ImageView) contentView.findViewById(R.id.trade_type_iv);
            viewHolder.indicatorView = (ImageView) contentView.findViewById(R.id.indicator_iv);
            viewHolder.tradeChooseIv = (ImageView) contentView.findViewById(R.id.trade_choose_iv);
            viewHolder.indicatorView.setVisibility(View.GONE);
            bindData(position, viewHolder);
            return contentView;
        }

        private class ViewHolder {
            DinnertableTradeView tradeView;
            ImageView tradeTypeTv;
            ImageView indicatorView;
            ImageView tradeChooseIv;
        }

        private void bindData(final int postion, ViewHolder holder) {
            DinnertableTradeVo model = data.get(postion);
            holder.tradeView.enablePreCashDisplay(false);

            if (curTableTradeVo != null && curTableTradeVo.getTradeId() == model.getTradeId()) {
                holder.tradeView.setEnableTransparent(false);
                holder.tradeChooseIv.setVisibility(View.VISIBLE);
            } else {
                holder.tradeView.setEnableTransparent(false);
                holder.tradeChooseIv.setVisibility(View.INVISIBLE);
            }

            holder.tradeView.setModel(model.getDinnertableTrade());
            holder.tradeView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    itemClickLisenter.clickItem(postion);
                }
            });

            //modify by zhubo已支付的大众点评订单，不允许拖动
            if (model.getStatus() == DinnertableStatus.EMPTY
                    || TradeSourceUtils.isTradeUnProcessed(model, SourceId.WECHAT)
                    || TradeSourceUtils.isTradePayedAndConfirmed(model, SourceId.DIANPING)
                    || TradeSourceUtils.isTradePayedAndConfirmed(model, SourceId.XIN_MEI_DA)
                    || TradeSourceUtils.isTradePayedAndConfirmed(model, SourceId.KOU_BEI)
                    || TradeSourceUtils.isTradePayedUnAcceptFromBAIDURICE(model)
                    || TradeSourceUtils.isTradePayedUnAcceptFromFAMILIAR(model)
                    || TradeSourceUtils.isTradePayedUnAcceptFromOpenPlatform(model)
                    || TradeSourceUtils.isTradePayedFromShuKe(model)
                    || TableInfoContentBean.isAsyncHttpExcuting(model)) {
                holder.tradeView.setEnabledDrag(false);
            } else {
                holder.tradeView.setEnabledDrag(true);
            }


            // 微信订单标识 增加大众点评图标显示
            if (TradeSourceUtils.isTradeUnProcessed(model, SourceId.WECHAT)) {
                holder.tradeTypeTv.setVisibility(View.VISIBLE);
                holder.tradeTypeTv.setImageResource(R.drawable.dinner_table_wechat);
            } else if (TradeSourceUtils.isTradePayedAndConfirmed(model, SourceId.DIANPING)) {
                holder.tradeTypeTv.setVisibility(View.VISIBLE);
                holder.tradeTypeTv.setImageResource(R.drawable.dinner_table_dianping);
            } else if (TradeSourceUtils.isTradePayedAndConfirmed(model, SourceId.XIN_MEI_DA)) {
                holder.tradeTypeTv.setVisibility(View.VISIBLE);
                holder.tradeTypeTv.setImageResource(R.drawable.dinner_table_xinmeida);
            } else if (TradeSourceUtils.isTradeUnProcessed(model, SourceId.KOU_BEI)) {
                holder.tradeTypeTv.setVisibility(View.VISIBLE);
                holder.tradeTypeTv.setImageResource(R.drawable.dinner_table_koubei);
            } else if (TradeSourceUtils.isTradePayedUnAcceptFromBAIDURICE(model) || TradeSourceUtils.isTradePayedAcceptedFromBAIDURICE(model)) {
                //百度糯米订单图标显示
                holder.tradeTypeTv.setVisibility(View.VISIBLE);
                holder.tradeTypeTv.setImageResource(R.drawable.dinner_table_nuomi);
            } else if (TradeSourceUtils.isTradePayedFromShuKe(model)) {
                holder.tradeTypeTv.setVisibility(View.VISIBLE);
                holder.tradeTypeTv.setImageResource(R.drawable.dinner_table_shuke);
            } else if (TableInfoFragment.hasAddDish(model)) {//是否有加菜
                holder.tradeTypeTv.setVisibility(View.VISIBLE);
                holder.tradeTypeTv.setImageResource(R.drawable.add_dish_icon);
            } else if (model.getDinnertableTrade().getPreCashPrintStatus() == YesOrNo.YES && SharedPreferenceUtil.getSpUtil().getBoolean(DinnertableFragment.SHOW_PRECASH_KEY, false)) {
                holder.tradeTypeTv.setVisibility(View.VISIBLE);
                holder.tradeTypeTv.setImageResource(R.drawable.dinner_table_print_precash_icon);
            } else {
                holder.tradeTypeTv.setVisibility(View.GONE);
            }
        }

    }

    public interface ItemClickLisenter {
        void clickItem(int postion);
    }


}
