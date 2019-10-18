package com.zhongmei.bty.mobilepay.fragment;

import android.app.Dialog;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.yunfu.context.util.ArgsUtils;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ViewFinder;
import com.zhongmei.bty.mobilepay.views.InvoiceView;
import com.zhongmei.bty.basemodule.pay.bean.ElectronicInvoiceVo;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.bty.basemodule.trade.message.InvoiceQrcodeReq;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceSource;
import com.zhongmei.bty.basemodule.trade.enums.InvoiceType;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.commonmodule.view.ExpandPlusListView;
import com.zhongmei.bty.mobilepay.adapter.InvoiceAdapter;
import com.zhongmei.bty.mobilepay.bean.Invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class InvoiceFragment extends BasicDialogFragment implements View.OnClickListener {
    private final static String TAG = "tag_invoice";
    private static final String BUNDLE_TRADE = "com.zhongmei.bty.cashier.elecvoice.bundle_trade";
    private static final String BUNDLE_EINVOICE = "com.zhongmei.bty.cashier.elecvoice.bundle_einvoice";
    private static final String BUNDLE_AMOUNT = "com.zhongmei.bty.cashier.elecvoice.bundle_amount";

    public final static int SCHEMA_PREVIEW = 0xf001;
    public final static int SCHEMA_ADD = 0xf002;

    private RelativeLayout previewTitleRlyt;
    private TextView previewTitleAmountTxv;
    private TextView previewTitleTotalTxv;
    private ImageButton previewTitleDeleteIBtn;
    private RelativeLayout addTitleRlyt;
        private LinearLayout previewContentLlyt;
    private ExpandPlusListView previewContentLv;
    private InvoiceView previewContentInvoiceView;
    private LinearLayout previewContentAddBtn;
    private Button previewContentCommitBtn;
    private LinearLayout addContentLlyt;
    private InvoiceView addContentInvoiceView;
    private Button addContentCancelBtn;
    private Button addContentCommitBtn;

    private Animation alphaInAnimation;
    private Animation alphaOutAnimation;
    private Animation translateInLeftAnimation;
    private Animation translateInRightAnimation;
    private Animation translateOutLeftAnimation;
    private Animation translateOutRightAnimation;

    private TradeVo tradeVo;
    private ElectronicInvoiceVo electronicInvoiceVo;
    private BigDecimal actualAmount;
    private Callback callback;
    private int currentSchema;

    private InvoiceAdapter invoiceAdapter;


    public interface Callback {


        void callback(boolean flag, InvoiceQrcodeReq invoiceQrcodeReq, TradeVo tradeVo);


    }


    @Deprecated
    public static void show(FragmentManager fragmentManager,
                            TradeVo tradeVo,
                            ElectronicInvoiceVo electronicInvoiceVo,
                            Callback callback) {
        BigDecimal actualAmount = null;
        if (tradeVo != null) {
            actualAmount = tradeVo.getTrade().getTradeAmount();
        } else {
            ArgsUtils.notNull(actualAmount, "Not found amount of trade");
        }
        show(fragmentManager, tradeVo, electronicInvoiceVo, actualAmount, callback);
    }


    public static void show(FragmentManager fragmentManager,
                            TradeVo tradeVo,
                            ElectronicInvoiceVo electronicInvoiceVo,
                            BigDecimal actualAmount,
                            Callback callback) {
        ArgsUtils.notNull(fragmentManager, "FragmentManager");
        ArgsUtils.notNull(tradeVo, "TradeVo");
        ArgsUtils.notNull(electronicInvoiceVo, "ElectronicInVoiceVo");
        if (BigDecimal.ZERO.compareTo(actualAmount) == 0) {
            actualAmount = tradeVo.getTrade().getTradeAmount();
        } else {
            ArgsUtils.notNull(actualAmount, "BigDecimal");
        }
        ArgsUtils.notNull(callback, "Callback");
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_TRADE, tradeVo);
        bundle.putSerializable(BUNDLE_EINVOICE, electronicInvoiceVo);
        bundle.putSerializable(BUNDLE_AMOUNT, actualAmount);
        fragment.setArguments(bundle);
        fragment.setCallback(callback);
        fragment.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            tradeVo = (TradeVo) arguments.getSerializable(BUNDLE_TRADE);
            electronicInvoiceVo = (ElectronicInvoiceVo) arguments.getSerializable(BUNDLE_EINVOICE);
            actualAmount = (BigDecimal) arguments.getSerializable(BUNDLE_AMOUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invoice_fragment_invoice, container);
        previewTitleRlyt = ViewFinder.findViewById(view, R.id.fragment_invoice_title_preview);
        previewTitleAmountTxv = ViewFinder.findViewById(view, R.id.fragment_invoice_title_preview_amount);
        previewTitleTotalTxv = ViewFinder.findViewById(view, R.id.fragment_invoice_title_preview_total);
        previewTitleDeleteIBtn = ViewFinder.findViewById(view, R.id.fragment_invoice_title_preview_delete);
        addTitleRlyt = ViewFinder.findViewById(view, R.id.fragment_invoice_title_add);
                previewContentLlyt = ViewFinder.findViewById(view, R.id.fragment_invoice_content_preview);
        previewContentLv = ViewFinder.findViewById(view, R.id.fragment_invoice_content_preview_list);
        previewContentInvoiceView = ViewFinder.findViewById(view, R.id.fragment_invoice_content_preview_invoice);
        previewContentAddBtn = ViewFinder.findViewById(view, R.id.fragment_invoice_content_preview_add);
        previewContentCommitBtn = ViewFinder.findViewById(view, R.id.fragment_invoice_content_preview_commit);
        addContentLlyt = ViewFinder.findViewById(view, R.id.fragment_invoice_content_add);
        addContentInvoiceView = ViewFinder.findViewById(view, R.id.fragment_invoice_content_add_iv);
        addContentCancelBtn = ViewFinder.findViewById(view, R.id.fragment_invoice_content_add_cancel);
        addContentCommitBtn = ViewFinder.findViewById(view, R.id.fragment_invoice_content_add_commit);

        alphaInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_in);
        alphaOutAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_out);
        translateInLeftAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_in_left);
        translateInRightAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_in_right);
        translateOutLeftAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out_left);
        translateOutRightAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_out_right);

        previewTitleDeleteIBtn.setOnClickListener(this);
        previewContentCommitBtn.setOnClickListener(this);
        previewContentAddBtn.setOnClickListener(this);
        addContentCommitBtn.setOnClickListener(this);
        addContentCancelBtn.setOnClickListener(this);

        invoiceAdapter = new InvoiceAdapter(getActivity(), previewContentLv, previewContentInvoiceView, dataSetObserver);
        previewContentLv.setAdapter(invoiceAdapter);

        setCurrentSchema(SCHEMA_PREVIEW, false);

        addNewInvoice(new Invoice(tradeVo, electronicInvoiceVo, actualAmount));

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), R.style.custom_alert_dialog) {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Window window = getWindow();
                window.setLayout(DensityUtil.dip2px(getActivity(), 460), DensityUtil.dip2px(getActivity(), 588));
            }

            @Override
            public void onBackPressed() {
                previewTitleDeleteIBtn.performClick();
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_invoice_content_preview_commit) {
            BigDecimal totalAmount = ArgsUtils.returnIfNull(getInvoiceTotalAmount(), BigDecimal.valueOf(0));
            if (totalAmount.compareTo(actualAmount) > 0) {
                CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(getActivity());
                cb.iconType(CommonDialogFragment.ICON_WARNING)
                        .title(getString(R.string.invoice_amount_overflow))
                        .positiveText(R.string.pay_ok)
                        .build()
                        .show(getFragmentManager(), "TAG_common");
            } else if (totalAmount.compareTo(BigDecimal.valueOf(0)) < 1) {
                CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(getActivity());
                cb.iconType(CommonDialogFragment.ICON_WARNING)
                        .title(getString(R.string.invoice_amount_less_zero))
                        .positiveText(R.string.pay_ok)
                        .build()
                        .show(getFragmentManager(), "TAG_common");
            } else {
                if (callback != null) {
                    List<Invoice> invoiceList = invoiceAdapter.getInvoiceList();
                    InvoiceQrcodeReq invoiceQrcodeReq = createInvoiceQrcodeReq(invoiceList);
                    if (callback != null) {
                        callback.callback(true, invoiceQrcodeReq, tradeVo);
                    }
                    dismiss();
                }
            }
        } else if (v.getId() == R.id.fragment_invoice_content_preview_add) {
            BigDecimal invoiceTotalAmount = getInvoiceTotalAmount();
            BigDecimal tradeAmount = actualAmount;
            if (invoiceTotalAmount == null || tradeAmount == null) {
                return;
            }
            if (invoiceTotalAmount.compareTo(tradeAmount) > 0) {
                CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(getActivity());
                cb.iconType(CommonDialogFragment.ICON_WARNING)
                        .title(getString(R.string.invoice_amount_overflow))
                        .positiveText(R.string.pay_ok)
                        .build()
                        .show(getFragmentManager(), "TAG_common");
            } else {
                Invoice invoice = new Invoice(tradeVo, electronicInvoiceVo, actualAmount);
                invoice.setUnitPrice(tradeAmount.subtract(invoiceTotalAmount));
                addContentInvoiceView.setInvoice(invoice);
                setCurrentSchema(SCHEMA_ADD, true);
            }
        } else if (v.getId() == R.id.fragment_invoice_content_add_commit) {
            Invoice invoice = addContentInvoiceView.getInvoice();
            addNewInvoice(invoice);
        } else if (v.getId() == R.id.fragment_invoice_content_add_cancel) {
            setCurrentSchema(SCHEMA_PREVIEW, true);
        } else if (v.getId() == R.id.fragment_invoice_title_preview_delete) {
            if (callback != null) {
                List<Invoice> invoiceList = invoiceAdapter.getInvoiceList();
                InvoiceQrcodeReq invoiceQrcodeReq = createInvoiceQrcodeReq(invoiceList);
                if (callback != null) {
                    callback.callback(false, invoiceQrcodeReq, tradeVo);
                }
            }
            dismiss();
        }
    }


    private void addNewInvoice(Invoice invoice) {
        if (invoice == null) {
            return;
        }

        invoice.registerDataSetObserver(dataSetObserver);

        invoiceAdapter.addInvoice(invoice);
        setCurrentSchema(SCHEMA_PREVIEW, true);

        dataSetObserver.onChanged();
    }

    private void setCallback(Callback callback) {
        this.callback = callback;
    }


    private void setCurrentSchema(int model, boolean animationEable) {
        if (currentSchema == model) {
            return;
        }
        currentSchema = model;
        switch (currentSchema) {
            case SCHEMA_PREVIEW:
                makePreviewSchema(animationEable);
                break;
            case SCHEMA_ADD:
                makeAddSchema(animationEable);
                break;
            default:
                throw new IllegalArgumentException("Unknown value");
        }
    }


    private void makePreviewSchema(boolean animationEnable) {
        if (animationEnable) {
            previewTitleRlyt.startAnimation(alphaInAnimation);
            addTitleRlyt.startAnimation(alphaOutAnimation);
            previewContentLlyt.startAnimation(translateInLeftAnimation);
            addContentLlyt.startAnimation(translateOutRightAnimation);
        }

        previewTitleRlyt.setVisibility(View.VISIBLE);
        previewContentLlyt.setVisibility(View.VISIBLE);
        addTitleRlyt.setVisibility(View.GONE);
        addContentLlyt.setVisibility(View.GONE);
    }


    private void makeAddSchema(boolean animationEnable) {
        if (animationEnable) {
            previewTitleRlyt.startAnimation(alphaOutAnimation);
            addTitleRlyt.startAnimation(alphaInAnimation);
            previewContentLlyt.startAnimation(translateOutLeftAnimation);
            addContentLlyt.startAnimation(translateInRightAnimation);
        }

        previewTitleRlyt.setVisibility(View.GONE);
        previewContentLlyt.setVisibility(View.GONE);
        addTitleRlyt.setVisibility(View.VISIBLE);
        addContentLlyt.setVisibility(View.VISIBLE);
    }

    private DataSetObserver dataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updatePreviewTile();
            updateAdapter();
        }

        private void updateAdapter() {
            invoiceAdapter.notifyDataSetChanged();
        }

        private void updatePreviewTile() {
            String actualAmountStr = null;
            String totalAmountStr;


            actualAmountStr = actualAmount.toString();

            BigDecimal totalAmount = getInvoiceTotalAmount();

            updatePreviewAddBtnState(actualAmount, totalAmount);

            totalAmountStr = totalAmount.toString();

            previewTitleAmountTxv.setText(ShopInfoCfg.formatCurrencySymbol(
                    ArgsUtils.returnIfEmpty(actualAmountStr, "0.00")));
            previewTitleTotalTxv.setText(ShopInfoCfg.formatCurrencySymbol(
                    ArgsUtils.returnIfEmpty(totalAmountStr, "0.00")));
        }

        private void updatePreviewAddBtnState(BigDecimal tradeAmount, BigDecimal currentTotalAmount) {
            int compareValue = tradeAmount.compareTo(currentTotalAmount);
            if (compareValue > 0) {
                previewContentAddBtn.setEnabled(true);
            } else if (compareValue == 0) {
                previewContentAddBtn.setEnabled(false);

            } else {
                previewContentAddBtn.setEnabled(false);
                CommonDialogFragment.CommonDialogFragmentBuilder cb = new CommonDialogFragment.CommonDialogFragmentBuilder(getActivity());
                cb.iconType(CommonDialogFragment.ICON_WARNING)
                        .title(getString(R.string.invoice_amount_overflow))
                        .positiveText(R.string.pay_ok)
                        .build()
                        .show(getFragmentManager(), "TAG_common");
            }
        }
    };

    private BigDecimal getInvoiceTotalAmount() {
        List<Invoice> invoiceList = invoiceAdapter.getInvoiceList();
        BigDecimal totalAmount = null;
        for (Invoice invoice : invoiceList) {
            BigDecimal invoiceAmount = invoice.getAmount();
            if (totalAmount == null) {
                totalAmount = invoiceAmount;
            } else {
                totalAmount = totalAmount.add(invoiceAmount);
            }
        }
        if (totalAmount == null) {
            totalAmount = BigDecimal.valueOf(0.00);
        }
        return totalAmount;
    }


    private InvoiceQrcodeReq createInvoiceQrcodeReq(List<Invoice> invoiceList) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<InvoiceQrcodeReq.GoodsInfo> goods = new ArrayList<InvoiceQrcodeReq.GoodsInfo>();
        for (Invoice invoice : invoiceList) {
            InvoiceQrcodeReq.GoodsInfo goodsInfo = createInvoiceItem(invoice);
            goods.add(goodsInfo);
                        totalAmount = totalAmount.add(goodsInfo.getAmount());
        }
        InvoiceQrcodeReq invoiceQrcodeReq = new InvoiceQrcodeReq();
        invoiceQrcodeReq.setGoods(goods);
        invoiceQrcodeReq.setBrandIdenty(Utils.toLong(ShopInfoCfg.getInstance().commercialGroupId));
        invoiceQrcodeReq.setShopIdenty(Utils.toLong(ShopInfoCfg.getInstance().shopId));
        invoiceQrcodeReq.setType(InvoiceType.FOOD);
        invoiceQrcodeReq.setSource(InvoiceSource.UKNOWER);
        invoiceQrcodeReq.setUuid(SystemUtils.genOnlyIdentifier());
        invoiceQrcodeReq.setOrderId(tradeVo.getTrade().getId());
        AuthUser authUser = Session.getAuthUser();
        if (authUser != null) {
            invoiceQrcodeReq.setDrawerNo(String.valueOf(authUser.getId()));
            invoiceQrcodeReq.setDrawer(authUser.getName());
        }
        invoiceQrcodeReq.setTotalAmount(totalAmount);
        invoiceQrcodeReq.setTaxpayerName(electronicInvoiceVo.getElectronicInvoice().getTaxpayerName());
        invoiceQrcodeReq.setTaxpayerNo(electronicInvoiceVo.getElectronicInvoice().getTaxpayerNumber());
        invoiceQrcodeReq.setTaxpayerAddr(electronicInvoiceVo.getElectronicInvoice().getAddress());
        invoiceQrcodeReq.setTaxpayerTel(electronicInvoiceVo.getElectronicInvoice().getTelephone());
        invoiceQrcodeReq.setTaxpayerBankAccount(electronicInvoiceVo.getElectronicInvoice().getAccountNumber());
        return invoiceQrcodeReq;
    }


    private InvoiceQrcodeReq.GoodsInfo createInvoiceItem(Invoice invoice) {
        InvoiceQrcodeReq.GoodsInfo goodsInfo = new InvoiceQrcodeReq().new GoodsInfo();
        goodsInfo.setGoodsName(invoice.getInvoiceTaxRate().getInvoiceName());
        goodsInfo.setGoodsNo(invoice.getInvoiceTaxRate().getInvoiceCode());
        goodsInfo.setCount(invoice.getCount());
        goodsInfo.setUnitPrice(invoice.getUnitPrice());
        goodsInfo.setAmount(invoice.getUnitPrice().multiply(new BigDecimal(invoice.getCount())));
        goodsInfo.setTaxRate(invoice.getInvoiceTaxRate().getTaxRate());
        return goodsInfo;
    }
}
