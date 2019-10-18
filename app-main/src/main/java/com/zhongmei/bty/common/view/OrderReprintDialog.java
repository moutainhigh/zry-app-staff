package com.zhongmei.bty.common.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.database.entity.PrinterCashierTicket;
import com.zhongmei.bty.constants.OCConstant;
import com.zhongmei.bty.dinner.action.ActionReprintType;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.ordercenter_reprint_dialog)
public class OrderReprintDialog extends BasicDialogFragment implements OnItemClickListener, OnClickListener {
    private static final String TAG = OrderReprintDialog.class.getSimpleName();

    private final static String CUSTOMER = "customer";

    private final static String PRECASH = "precash";

    private final static String CASH = "cash";

    private final static String CANCEL = "cancel";

    private final static String REFUND = "refund";

    private final static String RECEIPT = "Receipt";

    private final static String LABEL = "Label";

    private final static String KITCHEN_ALL = "kitchenAll";

    private final static String KITCHEN_CELL = "kitchenCell";

    private static final String DEPOSIT = "deposit";

    private final static String CREDIT = "credit";
    @ViewById(R.id.ordercenter_reprint_listview)
    ListView mListView;

    @ViewById(R.id.ordercenter_reprint_selectall)
    CheckBox mSelectAll;

    @ViewById(R.id.ordercenter_reprint_cancel)
    ImageButton mCancle;

    private List<TicketType> mDatas;

    private LongSparseArray<PrinterCashierTicket> mKitchenCellArray;

    private Map<String, String> printTypes = new HashMap<String, String>();

    private DinnerOrderCenterReprintAdapter adapter;

    private Trade trade;

    private String printTag;

    private int mFromType = -1000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        trade = (Trade) getArguments().getSerializable("trade");
        printTag = getArguments().getString("printTag");
        mFromType = getArguments().getInt("from_type");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_LAYOUT_FLAGS | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        Window window = dialog.getWindow();
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(uiOptions);

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }

    @AfterViews
    protected void initViews() {
        initPrintTypes();
        initDatas();
        mListView.setOnItemClickListener(this);
        mSelectAll.setOnClickListener(this);
    }

    private void initPrintTypes() {
        printTypes.put(CUSTOMER, getString(R.string.order_reprint_customer));
        printTypes.put(PRECASH, getString(R.string.order_reprint_precash));
        printTypes.put(CASH, getString(R.string.order_reprint_cash));
        printTypes.put(CANCEL, getString(R.string.order_reprint_cancel));
        printTypes.put(REFUND, getString(R.string.order_reprint_refund));
        printTypes.put(RECEIPT, getString(R.string.order_reprint_receipt));

        if (mFromType != OCConstant.FromType.FROM_TYPE_RETAIL) {
            printTypes.put(KITCHEN_ALL, getString(R.string.order_reprint_kitchen_all));
        }
        printTypes.put(DEPOSIT, getString(R.string.order_reprint_deposit));
        printTypes.put(CREDIT, getString(R.string.order_reprint_credit));
        printTypes.put(LABEL, getString(R.string.label));
    }

    @Click(R.id.ordercenter_reprint_reprint)
    protected void reprint() {
        int selectCount = getSelectCount();
        if (selectCount <= 0) {
            ToastUtil.showShortToast(R.string.reprint_no_bill_tip);
            return;
        }

        EventBus.getDefault().post(makeReprintEvent());
        dismiss();
    }

    private ActionReprintType makeReprintEvent() {
        ActionReprintType action = new ActionReprintType(trade, printTag);
        for (TicketType ticketType : mDatas) {
            if (ticketType.isSelect) {
                if (TextUtils.equals(ticketType.getKey(), CUSTOMER)) {
                    action.setPrintCustomer(true);
                } else if (TextUtils.equals(ticketType.getKey(), PRECASH)) {
                    action.setPrintPrecash(true);
                } else if (TextUtils.equals(ticketType.getKey(), CASH)) {
                    action.setPrintCash(true);
                } else if (TextUtils.equals(ticketType.getKey(), CANCEL)) {
                    action.setPrintCancel(true);
                } else if (TextUtils.equals(ticketType.getKey(), REFUND)) {
                    action.setPrintRefund(true);
                } else if (TextUtils.equals(ticketType.getKey(), RECEIPT)) {
                    action.setPrintReceipt(true);
                } else if (TextUtils.equals(ticketType.getKey(), KITCHEN_ALL)) {
                    action.setPrintKitchenAll(true);
                } else if (TextUtils.equals(ticketType.getKey(), KITCHEN_CELL)) {
                    PrinterCashierTicket printerCashierTicket = mKitchenCellArray.get(ticketType.getId());
                    action.addPrintKitchenCellList(printerCashierTicket);
                } else if (TextUtils.equals(ticketType.getKey(), DEPOSIT)) {
                    action.setPrintDeposit(true);
                } else if (TextUtils.equals(ticketType.getKey(), CREDIT)) {
                    action.setPrintCredit(true);
                } else if (TextUtils.equals(ticketType.getKey(), LABEL)) {
                    action.setPrintLabel(true);
                }
            }
        }

        return action;
    }

    @Click(R.id.ordercenter_reprint_cancel)
    protected void cancel() {
                dismiss();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ordercenter_reprint_selectall) {
            boolean selectState = false;
            if (mSelectAll.isChecked()) {
                selectState = true;
            } else {
                selectState = false;
            }
            for (int i = 0; i < mDatas.size(); i++) {
                TicketType ticketType = mDatas.get(i);
                ticketType.setSelect(selectState);
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TicketType ticketType = mDatas.get(position);
        ticketType.setSelect(!ticketType.isSelect);
        adapter.notifyDataSetChanged();
        handleSelctAllState();
    }

    public void initDatas() {
        try {
            mDatas = new ArrayList<TicketType>();
            getKitchenCellArray();
            TradeVo tradeVo = OperatesFactory.create(TradeDal.class).findTrade(trade);
            addPrintTicket(tradeVo.getTrade());
            addDepositTicket(tradeVo);
            addCreditTicket(tradeVo);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        initAdapter();
    }

    public void initAdapter() {
        adapter = new DinnerOrderCenterReprintAdapter(getActivity(), mDatas);
        mListView.setAdapter(adapter);
    }


    private void getKitchenCellArray() {
        List<PrinterCashierTicket> printerDocuments = null;
        try {
                    } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        mKitchenCellArray = new LongSparseArray<>();
        if (Utils.isNotEmpty(printerDocuments)) {
            for (PrinterCashierTicket printerCashierTicket : printerDocuments) {
                mKitchenCellArray.put(printerCashierTicket.getId(), printerCashierTicket);
            }
        }
    }

    private void addPrintTicket(Trade trade) {
        if (trade.getBusinessType() == BusinessType.BEAUTY) {            if (trade.getTradePayStatus() == TradePayStatus.PAID) {
                mDatas.add(new TicketType(CASH, printTypes.get(CASH), false));
            }
        } else if (trade.getBusinessType() == BusinessType.DINNER || trade.getBusinessType() == BusinessType.BUFFET || trade.getBusinessType() == BusinessType.GROUP) {
            addDinnerNativePrintTicket(trade);
        } else {
            addNativePrintTicket(trade);
            addBeautyPrintTIckety(trade);
        }

        if (mFromType != OCConstant.FromType.FROM_TYPE_RETAIL && mFromType != OCConstant.FromType.FROM_TYPE_BEAUTY) {            addKitchenPrintTicket();
        }
        addLabel(trade);
    }

    private void addBeautyPrintTIckety(Trade trade) {
        if (trade.getTradePayStatus() == TradePayStatus.PAID) {
            mDatas.add(new TicketType(CASH, printTypes.get(CASH), false));
                    }
    }

    private void addDinnerNativePrintTicket(Trade trade) {
                if (trade.getTradeStatus() == TradeStatus.RETURNED || trade.getTradePayStatus() == TradePayStatus.REFUND_FAILED
                || trade.getTradePayStatus() == TradePayStatus.REFUNDED
                || trade.getTradePayStatus() == TradePayStatus.REFUNDING) {
            mDatas.add(new TicketType(REFUND, printTypes.get(REFUND), false));
                    } else if (trade.getTradeStatus() == TradeStatus.INVALID) {
            mDatas.add(new TicketType(CANCEL, printTypes.get(CANCEL), false));
                    } else if (trade.getTradePayStatus() == TradePayStatus.PAID) {
            mDatas.add(new TicketType(CUSTOMER, printTypes.get(CUSTOMER), false));
            mDatas.add(new TicketType(CASH, printTypes.get(CASH), false));
                    } else if (trade.getTradePayStatus() == TradePayStatus.UNPAID) {
            mDatas.add(new TicketType(CUSTOMER, printTypes.get(CUSTOMER), false));
            mDatas.add(new TicketType(PRECASH, printTypes.get(PRECASH), false));
        }
    }

    private void addNativePrintTicket(Trade trade) {
                if (trade.getTradeStatus() == TradeStatus.RETURNED) {
            mDatas.add(new TicketType(REFUND, printTypes.get(REFUND), false));
                    } else if (trade.getTradeStatus() == TradeStatus.INVALID) {
            mDatas.add(new TicketType(CANCEL, printTypes.get(CANCEL), false));
        } else {
            mDatas.add(new TicketType(RECEIPT, printTypes.get(RECEIPT), false));
        }
    }


    private void addLabel(Trade trade) {
        if ((trade.getBusinessType() == BusinessType.SNACK || trade.getBusinessType() == BusinessType.TAKEAWAY || trade.getBusinessType() == BusinessType.DINNER)
                && (trade.getTradeStatus() == TradeStatus.CONFIRMED || trade.getTradeStatus() == TradeStatus.FINISH)) {
            mDatas.add(new TicketType(LABEL, printTypes.get(LABEL), false));
        }
    }

    private void addKitchenPrintTicket() {
        mDatas.add(new TicketType(KITCHEN_ALL, printTypes.get(KITCHEN_ALL), false));
        for (int i = 0; i < mKitchenCellArray.size(); i++) {
            PrinterCashierTicket printerCashierTicket = mKitchenCellArray.valueAt(i);
            String name = getString(R.string.order_reprint_kitchen_cell, printerCashierTicket.getName());
            TicketType tickectType = new TicketType(KITCHEN_CELL, name, false);
            tickectType.setId(printerCashierTicket.getId());
            mDatas.add(tickectType);
        }
    }


    private void addDepositTicket(TradeVo tradeVo) {
        if (showDepositTicket(tradeVo)) {
            mDatas.add(new TicketType(DEPOSIT, printTypes.get(DEPOSIT), false));
        }
    }


    private void addCreditTicket(TradeVo tradeVo) {
        if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getTradeStatus() == TradeStatus.CREDIT) {
            mDatas.add(new TicketType(CREDIT, printTypes.get(CREDIT), false));
        }
    }


    private boolean showDepositTicket(TradeVo tradeVo) {
        if (tradeVo != null) {
            if (tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET && tradeVo.isPaidTradeposit()) {
                return true;
            }

            Trade trade = tradeVo.getTrade();
            TradeDeposit tradeDeposit = tradeVo.getTradeDeposit();
                        if (trade.getTradePayStatus() == TradePayStatus.PAID
                    && tradeDeposit != null
                    && tradeDeposit.getDepositPay() != null
                    && tradeDeposit.getDepositRefund() == null) {

                return true;
            }
        }

        return false;
    }

    public class TicketType {

        private long id;

        private String key;

        private String name;

        private boolean isSelect;

        public TicketType(String key, String name, boolean isSelect) {
            this.key = key;
            this.name = name;
            this.isSelect = isSelect;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean isSelect) {
            this.isSelect = isSelect;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getId() {
            return id;
        }
    }

    public void handleSelctAllState() {
        int selectCount = getSelectCount();
        if (selectCount == mDatas.size()) {
            mSelectAll.setChecked(true);
        } else {
            if (mSelectAll.isChecked()) {
                mSelectAll.setChecked(false);

            }
        }
    }

    public int getSelectCount() {
        int selectCount = 0;
        for (TicketType ticketType : mDatas) {
            if (ticketType.isSelect) {
                selectCount++;
            }
        }
        return selectCount;
    }

    public class DinnerOrderCenterReprintAdapter extends BaseAdapter {
        private List<TicketType> mDatas;

        private Context mContext;

        public DinnerOrderCenterReprintAdapter(Context context, List<TicketType> datas) {
            this.mContext = context;
            this.mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView =
                        LayoutInflater.from(mContext).inflate(R.layout.ordercenter_reprint_listview_item, parent, false);
                holder = new ViewHolder();
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.ordercenter_reprint_selecttype);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TicketType ticketType = mDatas.get(position);
            holder.checkbox.setText(ticketType.getName());
            if (ticketType.isSelect()) {
                holder.checkbox.setChecked(true);
            } else {
                holder.checkbox.setChecked(false);
            }

            return convertView;
        }

        class ViewHolder {
            CheckBox checkbox;
        }

    }

}
