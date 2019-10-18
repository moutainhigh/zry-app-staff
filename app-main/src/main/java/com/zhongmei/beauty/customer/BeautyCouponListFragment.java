package com.zhongmei.beauty.customer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.http.CalmNetWorkRequest;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.basemodule.customer.bean.coupon.CouponVo;
import com.zhongmei.bty.basemodule.customer.message.CustomerDirectCouponListV2Resp;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.yunfu.db.enums.CouponType;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.yunfu.monitor.CalmResponseListener;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.yunfu.net.builder.NetError;
import com.zhongmei.yunfu.net.builder.NetworkRequest;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.resp.data.TransferReq;
import com.zhongmei.bty.commonmodule.database.entity.local.CustomerArrivalShop;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.customer.event.CustomerFragmentReplaceListener;
import com.zhongmei.bty.customer.util.AppUtil;
import com.zhongmei.bty.data.operates.CustomerArrivalShopDal;
import com.zhongmei.bty.data.operates.message.content.CustomerSendCouponReq;
import com.zhongmei.bty.data.operates.message.content.CustomerSendCouponResp;
import com.zhongmei.bty.snack.orderdish.view.CouponsLayoutView;
import com.zhongmei.bty.snack.orderdish.view.CouponsLayoutView_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.beauty_customer_coupon_list_dialog)
public class BeautyCouponListFragment extends BasicFragment implements CouponsLayoutView.CouponSelectedCallback {


    public final static int LAUNCHMODE_NORMAL = 0;

    public final static int LAUNCHMODE_PRIVLIGES = 1;

    public final static int LAUNCHMODE_CUSTOMER = 2;

    public final static String KEY_LAUNCH_MODE = "launchMode";

    public final static String KEY_CUSTOMER = "customer";

    @ViewById(R.id.root_layout)
    LinearLayout rootLayout;

    @ViewById(R.id.btn_close)
    protected ImageView mBtnClose;

    @ViewById(R.id.btn_cancel)
    protected Button mBtnCancel;


    @ViewById(R.id.tvTitle_CouponList)
    protected TextView mTvTitle;

    @ViewById(R.id.tvTitleDesc_CouponList)
    protected TextView mTvTitleDesc;

    @ViewById(R.id.coupon_content)
    protected LinearLayout mCouponViews;

    @ViewById(R.id.mBtn_sendCoupons)
    protected Button mSendCoupon;

    @ViewById(R.id.tv_empty)
    protected TextView mEmptyTv;

    @ViewById(R.id.content_layout)
    protected LinearLayout mLayout;

    @ViewById(R.id.bottom_btn_layout)
    protected LinearLayout bottomBtnLayout;

    @ViewById(R.id.customer_bottom_layout)
    protected LinearLayout bottomBtnLayout2;

    private List<CouponVo> couponVoList;

    private List<CouponVo> cashList;

    private List<CouponVo> discountList;

    private List<CouponVo> rebateList;
    private List<CouponVo> giftList;
    private CouponsLayoutView cashView;
    private CouponsLayoutView discountView;
    private CouponsLayoutView rebateView;
    private CouponsLayoutView giftView;
    private Context context;

    public CustomerArrivalShop customer;

    public Long customerId;

    private int mFlag = LAUNCHMODE_NORMAL;

    private CustomerResp mCustomerNew;

    private CouponVo vo;

    private boolean isChanged = false;

    private CustomerFragmentReplaceListener replaceListener;

    public void setReplaceListener(CustomerFragmentReplaceListener replaceListener) {
        this.replaceListener = replaceListener;
    }

    public void setCloseCallback(SendCouponCallback closeCallback) {
        this.mCallback = closeCallback;
    }

    private SendCouponCallback mCallback;

    private CustomerArrivalShopDal dal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().isRegistered(this);
    }

    @AfterViews
    void init() {
        mFlag = getArguments().getInt(KEY_LAUNCH_MODE);

        if (mFlag == LAUNCHMODE_NORMAL) {
            dal = OperatesFactory.create(CustomerArrivalShopDal.class);
            customerId = customer != null ? customer.customerId : null;
        } else {
            mCustomerNew = (CustomerResp) getArguments().getSerializable(KEY_CUSTOMER);
            customerId = mCustomerNew != null ? mCustomerNew.customerId : null;
        }
        context = getActivity();
        couponVoList = new ArrayList<>();
        cashList = new ArrayList<>();
        discountList = new ArrayList<>();
        rebateList = new ArrayList<>();
        giftList = new ArrayList<>();
        isChanged = false;
        setupView();
        getDate();
    }


    public Bundle createArguments(int launchMode, CustomerResp customer) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LAUNCH_MODE, launchMode);
        bundle.putSerializable(KEY_CUSTOMER, customer);
        return bundle;
    }

    private void setupView() {
        if (mFlag == LAUNCHMODE_NORMAL) {
            mBtnCancel.setVisibility(View.VISIBLE);
        } else if (mFlag == LAUNCHMODE_CUSTOMER) {
            mBtnClose.setVisibility(View.VISIBLE);
            if (mCustomerNew != null) {
                mTvTitle.setText(mCustomerNew.customerName);
                mTvTitleDesc.setText(mCustomerNew.levelName + " " + getString(R.string.custoemr_member_phone) + "：" + AppUtil.getTel(mCustomerNew.mobile));
            }
            rootLayout.setBackgroundResource(R.drawable.customer_card_bg);
            bottomBtnLayout.setVisibility(View.GONE);
            bottomBtnLayout2.setVisibility(View.VISIBLE);
        } else {
            mBtnClose.setVisibility(View.VISIBLE);
            mTvTitle.setTextColor(getResources().getColor(R.color.color_FF7901));
            mTvTitleDesc.setTextColor(getResources().getColor(R.color.color_999999));
            mBtnCancel.setVisibility(View.GONE);
            mBtnCancel.setBackgroundResource(R.drawable.beauty_customer_detail_pink_order_btn1);
        }
    }

    @Click(R.id.btn_close)
    public void close() {
        if (mFlag == LAUNCHMODE_PRIVLIGES) {
            EventBus.getDefault().post(new ActionDinnerPrilivige(ActionDinnerPrilivige.DinnerPriviligeType.PRIVILIGE_ITEMS));
        } else if (mFlag == LAUNCHMODE_CUSTOMER) {
            if (replaceListener != null) {
                replaceListener.getCustomer(isChanged ? mCustomerNew : null);
            }
        } else {
            if (!ClickManager.getInstance().isClicked() && this.mCallback != null) {
                this.mCallback.cancel();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getDate() {
        this.vo = null;
        OperatesFactory.create(CustomerOperates.class).getSendCouponList(this.getActivity(), new CalmResponseListener<ResponseObject<CustomerDirectCouponListV2Resp>>() {
            @Override
            public void onError(NetError error) {
                mEmptyTv.setVisibility(View.VISIBLE);
                mLayout.setVisibility(View.GONE);
                mEmptyTv.setText(error.getVolleyError().getMessage());
            }

            @Override
            public void onSuccess(final ResponseObject<CustomerDirectCouponListV2Resp> data) {
                if (data != null && data.getContent() == null) {
                    mEmptyTv.setVisibility(View.VISIBLE);
                    mLayout.setVisibility(View.GONE);
                    mEmptyTv.setText(data.getMessage());
                    ToastUtil.showShortToast(data.getMessage());
                    return;
                }
                TaskContext.bindExecute(BeautyCouponListFragment.this, new SimpleAsyncTask<Void>() {
                    @Override
                    public Void doInBackground(Void... params) {
                        try {
                            CustomerDirectCouponListV2Resp resp = data.getContent();
                            couponVoList.clear();
                            List<CouponVo> temp = resp.getCouponVoList();
                            if (Utils.isNotEmpty(temp)) {
                                for (CouponVo vo : temp) {
                                    if (vo.getCoupon() != null) {
                                        couponVoList.add(vo);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        initView();
                    }
                });
            }
        });
    }

    void initView() {
        if (Utils.isNotEmpty(couponVoList)) {
            getCouponList();
            mCouponViews.removeAllViews();
            if (Utils.isNotEmpty(cashList)) {
                cashView = CouponsLayoutView_.build(context, BeautyCouponListFragment.this, CouponType.CASH);
                cashView.setData(cashList, mFlag == LAUNCHMODE_CUSTOMER ? 2 : 1);
                mCouponViews.addView(cashView);
            }
            if (Utils.isNotEmpty(discountList)) {
                discountView = CouponsLayoutView_.build(context, BeautyCouponListFragment.this, CouponType.DISCOUNT);
                discountView.setData(discountList, mFlag == LAUNCHMODE_CUSTOMER ? 2 : 1);
                mCouponViews.addView(discountView);
            }
            if (Utils.isNotEmpty(giftList)) {
                giftView = CouponsLayoutView_.build(context, BeautyCouponListFragment.this, CouponType.GIFT);
                giftView.setData(giftList, mFlag == LAUNCHMODE_CUSTOMER ? 2 : 1);
                mCouponViews.addView(giftView);
            }
            if (Utils.isNotEmpty(rebateList)) {
                rebateView = CouponsLayoutView_.build(context, BeautyCouponListFragment.this, CouponType.REBATE);
                rebateView.setData(rebateList, mFlag == LAUNCHMODE_CUSTOMER ? 2 : 1);
                mCouponViews.addView(rebateView);
            }
            mEmptyTv.setVisibility(View.GONE);
            mLayout.setVisibility(View.VISIBLE);
        } else {
            mEmptyTv.setVisibility(View.VISIBLE);
            mLayout.setVisibility(View.GONE);
            mEmptyTv.setText(getContext().getString(R.string.not_have_coupon_list));
        }
    }


    void sendCoupon(CouponVo vo) {

    }

    @Click({R.id.mBtn_sendCoupons, R.id.btn_cancel, R.id.btn_cancel_customer_type, R.id.mBtn_sendCoupons_customer_type})
    void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.mBtn_sendCoupons_customer_type:
            case R.id.mBtn_sendCoupons:
                if (!ClickManager.getInstance().isClicked()) {
                    if (vo == null) {
                        ToastUtil.showShortToast(getString(R.string.choose_coupon));
                    } else {
                        sendCoupon(vo);
                    }
                }
                break;
            case R.id.btn_cancel_customer_type:
            case R.id.btn_cancel:
                if (mFlag == LAUNCHMODE_NORMAL) {
                    if (this.mCallback != null) this.mCallback.cancel();
                } else if (mFlag == LAUNCHMODE_CUSTOMER) {
                    if (replaceListener != null)
                        replaceListener.getCustomer(isChanged ? mCustomerNew : null);
                }
                break;
        }
    }

    @Override
    public void onCouponSelected(CouponVo vo, CouponType type) {
        for (CouponVo v : couponVoList) {
            if (v != vo) {
                v.setSelected(false);
            }
        }
        this.vo = vo.isSelected() ? vo : null;
        if (discountView != null) discountView.updateViews();
        if (cashView != null) cashView.updateViews();
        if (giftView != null) giftView.updateViews();
        if (rebateView != null) rebateView.updateViews();
    }

    public void getCouponList() {
        cashList.clear();
        discountList.clear();
        giftList.clear();
        rebateList.clear();
        for (CouponVo vo : couponVoList) {
            if (vo.getCoupon().getCouponType() == CouponType.CASH) {
                cashList.add(vo);
            } else if (vo.getCoupon().getCouponType() == CouponType.DISCOUNT) {
                discountList.add(vo);
            } else if (vo.getCoupon().getCouponType() == CouponType.GIFT) {
                giftList.add(vo);
            } else if (vo.getCoupon().getCouponType() == CouponType.REBATE) {
                rebateList.add(vo);
            }
        }
    }

    public interface SendCouponCallback {
        void cancel();

        void onSendCoupon(int count);
    }
}
