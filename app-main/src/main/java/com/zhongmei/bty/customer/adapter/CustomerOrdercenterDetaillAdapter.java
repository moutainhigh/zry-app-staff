package com.zhongmei.bty.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.bty.basemodule.devices.mispos.data.bean.CustomerSaleCardInfo;
import com.zhongmei.bty.customer.bean.CustomerOrdercenterDetailCommonBean;
import com.zhongmei.yunfu.R;

import java.util.List;

/**
 * @Date：2016年2月18日
 * @Description:售卡记录或储值记录列表adapter
 * @Version: 1.0
 */
public class CustomerOrdercenterDetaillAdapter extends BaseAdapter {

    private Context mContext;

    private List<?> dataList;

    private Class beanClass;

    private boolean showCheckbox = false;//是否显示选择框

    public CustomerOrdercenterDetaillAdapter(Context context, List<?> dataList, Class beanClass) {
        mContext = context;
        this.dataList = dataList;
        this.beanClass = beanClass;
    }

    @Override
    public int getCount() {
        if (dataList == null)
            return 0;
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            /*if (beanClass == CardSellCardsDetail.class) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.customer_ordercenter_detail_listitem, null);
                ViewHolderCardList viewHolder = new ViewHolderCardList();
                viewHolder.checkIv = (ImageView) convertView.findViewById(R.id.check_iv);
                viewHolder.serialNumber = (TextView) convertView.findViewById(R.id.serial_number);
                viewHolder.cardNumber = (TextView) convertView.findViewById(R.id.card_number);
                viewHolder.cardCategory = (TextView) convertView.findViewById(R.id.card_category);
                viewHolder.cardStatus = (TextView) convertView.findViewById(R.id.card_status);
                viewHolder.sellPrice = (TextView) convertView.findViewById(R.id.sell_price);
                viewHolder.dealStatus = (TextView) convertView.findViewById(R.id.deal_status);
                convertView.setTag(viewHolder);
            } else*/
            if (beanClass == CustomerOrdercenterDetailCommonBean.class) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.customer_ordercenter_detail_listitem2, null);
                ViewHolderCommon viewHolder = new ViewHolderCommon();
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.value = (TextView) convertView.findViewById(R.id.value);
                convertView.setTag(viewHolder);
            }
        }
        /*if (beanClass == CardSellCardsDetail.class) {
            ViewHolderCardList viewHolder = (ViewHolderCardList) convertView.getTag();
            final CardSellCardsDetail cardSellCardsDetail = (CardSellCardsDetail) dataList.get(position);
            viewHolder.serialNumber.setText(cardSellCardsDetail.getSerialNumber());
            viewHolder.cardNumber.setText(cardSellCardsDetail.getCardNumber());
            viewHolder.cardCategory.setText(cardSellCardsDetail.getCardCategory());
            viewHolder.cardStatus.setText(cardSellCardsDetail.getCardStatus());
            viewHolder.sellPrice.setText(cardSellCardsDetail.getSellPrice());
            viewHolder.dealStatus.setText(cardSellCardsDetail.getDealStatus());
            if (showCheckbox) {
                viewHolder.checkIv.setVisibility(View.VISIBLE);
                SelectType selectType = cardSellCardsDetail.getSelctType();
                if (selectType == SelectType.NOT_SELECT) {
                    viewHolder.checkIv.setImageResource(R.drawable.customer_ordercenter_detail_not_check);
                } else if (selectType == SelectType.SELECTED) {
                    viewHolder.checkIv.setImageResource(R.drawable.customer_ordercenter_detail_check);
                } else if (selectType == SelectType.SELECT_INVALIATE) {
                    viewHolder.checkIv.setImageResource(R.drawable.customer_ordercenter_detail_check_invaliate);
                }
            } else {
                viewHolder.checkIv.setVisibility(View.GONE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (showCheckbox) {//解决非选择模式时，item可点击的bug
                        if (cardSellCardsDetail.getSelctType() == SelectType.NOT_SELECT) {
                            cardSellCardsDetail.setSelctType(SelectType.SELECTED);
                        } else if (cardSellCardsDetail.getSelctType() == SelectType.SELECTED) {
                            cardSellCardsDetail.setSelctType(SelectType.NOT_SELECT);
                        }
                        showCheckBox(true);
                        updateCheckHeadBar();
                    }
                }
            });
        } else*/
        if (beanClass == CustomerOrdercenterDetailCommonBean.class) {
            ViewHolderCommon viewHolder = (ViewHolderCommon) convertView.getTag();
            CustomerOrdercenterDetailCommonBean customerOrdercenterDetailCommonBean = (CustomerOrdercenterDetailCommonBean) dataList.get(position);
            viewHolder.name.setText(customerOrdercenterDetailCommonBean.getName());
            viewHolder.value.setText(customerOrdercenterDetailCommonBean.getValue());
        }
        return convertView;
    }

    public void resetData(List<?> dataList, Class beanClass) {
        this.dataList = dataList;
        this.beanClass = beanClass;
        notifyDataSetChanged();
    }

    private class ViewHolderCardList {
        ImageView checkIv;
        TextView serialNumber;
        TextView cardNumber;
        TextView cardCategory;
        TextView cardStatus;
        TextView sellPrice;
        TextView dealStatus;
    }

    private class ViewHolderCommon {
        TextView name;
        TextView value;

    }

    public void showCheckBox(boolean show) {
        this.showCheckbox = show;
        notifyDataSetChanged();
    }

    /**
     * @param check
     * @Date 2016年4月4日
     * @Description: 全选或者全不选
     * @Return void
     */
    public void checkAll(boolean check) {
        /*if (beanClass != CardSellCardsDetail.class || dataList == null)
            return;
        int count = 0;
        for (Object o : dataList) {
            CardSellCardsDetail detail = (CardSellCardsDetail) o;
            if (detail.getSelctType() != SelectType.SELECT_INVALIATE) {
                detail.setSelctType(check ? SelectType.SELECTED : SelectType.NOT_SELECT);
                if (detail.getSelctType() == SelectType.SELECTED) {
                    count++;
                }
            }
        }*/
        //resetSelectCount(count);
        notifyDataSetChanged();

    }

    /**
     * @Date 2016年4月5日
     * @Description: 更新checkbox标题栏
     * @Return void
     */
    private void updateCheckHeadBar() {
        Activity activity = (Activity) mContext;
        CheckBox allCb = (CheckBox) activity.findViewById(R.id.allcheck_cb);
        //判断卡片是否全选
        boolean isAllSelect = false;
        /*for (Object o : dataList) {
            CardSellCardsDetail detail = (CardSellCardsDetail) o;
            if (detail.getSelctType() == SelectType.NOT_SELECT) {
                isAllSelect = false;
                break;
            } else if (detail.getSelctType() == SelectType.SELECTED) {
                isAllSelect = true;
            }
        }
//		CustomerOrdercenterDetailFragment.allCheckWatch=false;
        //获取选择数量
        allCb.setChecked(isAllSelect);
        int carNumber = 0;
        for (Object o : dataList) {
            CardSellCardsDetail detail = (CardSellCardsDetail) o;
            if (detail.getSelctType() == SelectType.SELECTED) {
                carNumber++;
            }
        }*/
        //resetSelectCount(carNumber);
    }

    private void resetSelectCount(int count) {
        Activity activity = (Activity) mContext;
        TextView checkNumberTv = (TextView) activity.findViewById(R.id.check_number_tv);
        checkNumberTv.setText(mContext.getString(R.string.customer_ordercenter_detail_checktip, count + ""));
    }

    /**
     * @return
     * @Date 2016年4月5日
     * @Description: 获取选中卡数据
     * @Return List<CardSaleInfo>
     */
    public List<CustomerSaleCardInfo> getSelectItems() {
        /*if (beanClass != CardSellCardsDetail.class || dataList == null)
            return null;
        List<CustomerSaleCardInfo> infos = new ArrayList<CustomerSaleCardInfo>();
        for (Object o : dataList) {
            CardSellCardsDetail detail = (CardSellCardsDetail) o;
            if (detail.getSelctType() == SelectType.SELECTED) {
                infos.add(detail.getCardSaleInfo());
            }
        }
        return infos;*/
        return null;
    }

    /**
     * @return
     * @Date 2016年4月5日
     * @Description: 判断是否是全选
     * @Return boolean
     */
    public boolean isSelectAll() {
        // 判断卡片是否全选
        boolean isAllSelect = false;
        /*if (dataList != null) {
            for (Object o : dataList) {
                CardSellCardsDetail detail = (CardSellCardsDetail) o;
                if (detail.getSelctType() == SelectType.SELECTED) {
                    isAllSelect = true;
                } else {
                    isAllSelect = false;
                    break;
                }
            }
        }*/
        return isAllSelect;
    }

    /**
     * @return
     * @Date 2016年4月11日
     * @Description: 获取可退货的卡数量
     * @Return byte
     */
    public byte getCanRefundNumber() {
        /*if (beanClass != CardSellCardsDetail.class || dataList == null)
            return 0;*/
        byte number = 0;
        /*for (Object o : dataList) {
            CardSellCardsDetail detail = (CardSellCardsDetail) o;
            if (detail.getSelctType() == SelectType.NOT_SELECT) {
                number++;
            }
        }*/
        return number;

    }

    /**
     * @return
     * @Date 2016年4月11日
     * @Description: 获取单卡
     * @Return CardSaleInfo
     */
    public CustomerSaleCardInfo getRefundSingleCard() {
        /*if (beanClass != CardSellCardsDetail.class || dataList == null)
            return null;*/
        /*for (Object o : dataList) {
            CardSellCardsDetail detail = (CardSellCardsDetail) o;
            if (detail.getSelctType() == SelectType.NOT_SELECT) {
                return detail.getCardSaleInfo();
            }
        }*/
        return null;
    }

    /**
     * @param selectedCards
     * @return
     * @Date 2016年4月12日
     * @Description: 是否是整单退
     * @Return boolean
     */
    public boolean isRefundAll(List<CustomerSaleCardInfo> selectedCards) {
        if (selectedCards.size() < dataList.size()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @Date 2016年4月12日
     * @Description: 取消选择状态
     * @Return void
     */
    public void resetSelectStatus() {
        /*if (beanClass != CardSellCardsDetail.class || dataList == null)
            return;
        for (Object o : dataList) {
            CardSellCardsDetail detail = (CardSellCardsDetail) o;
            if (detail.getSelctType() != SelectType.SELECT_INVALIATE) {
                detail.setSelctType(SelectType.NOT_SELECT);
            }
        }*/
    }
}
