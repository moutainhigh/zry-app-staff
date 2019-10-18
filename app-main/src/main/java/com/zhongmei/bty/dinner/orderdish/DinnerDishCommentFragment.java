package com.zhongmei.bty.dinner.orderdish;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.zhongmei.bty.basemodule.customer.entity.TakeawayMemo;
import com.zhongmei.bty.basemodule.customer.operates.TakeawayMemoDal;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.dinner.table.view.CommentsView;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.dinner_dish_comment_fragment)
public class DinnerDishCommentFragment extends MobclickAgentFragment implements TextWatcher {
    @ViewById(R.id.close_iv)
    public ImageButton closeIv;
    @ViewById(R.id.content_edit)
    public EditText contentEditText;
    @ViewById(R.id.commentsview)
    public CommentsView commentsView;
    private String[] commentsList;

    private TextChangedLisener lisener;

    private ChangePageListener mChangePageListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @AfterViews
    public void initialViews() {
        contentEditText.requestFocus();
        contentEditText.addTextChangedListener(this);
        String memo = DinnerShoppingCart.getInstance().getOrder().getTrade().getTradeMemo();

        contentEditText.setText(memo);
        commentsView.setAdatper(commentsList);
        commentsView.initialView(getActivity(), DensityUtil.dip2px(getActivity(), 950));
        commentsView.setItemClickListener(itemClickListener);
    }

    public void setAdapter() {
        obtainDataFromDb();
    }

    private CommentsView.ItemClickListener itemClickListener = new CommentsView.ItemClickListener() {

        @Override
        public void onClick(String content) {
            if (TextUtils.isEmpty(contentEditText.getText().toString())) {
                contentEditText.append(content);
            } else {
                contentEditText.append("," + content);
            }

        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (lisener != null) {
            lisener.onTextChanged(s.toString());
        }
        if (s != null) {
            DinnerShoppingCart.getInstance().setDinnerRemarks(s.toString());
        }

    }

    public void setListener(TextChangedLisener lisener) {
        this.lisener = lisener;
    }

    public interface TextChangedLisener {
        void onTextChanged(String content);
    }

    @Click({R.id.close_iv, R.id.btn_print_memo})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_iv:
                mChangePageListener.changePage(ChangePageListener.ORDERDISHLIST, null);
                break;
            case R.id.btn_print_memo:
                TradeVo tradeVo = DinnerShoppingCart.getInstance().getOrder();
                if (tradeVo != null && tradeVo.getTrade() != null && !TextUtils.isEmpty(tradeVo.getTrade().getTradeMemo())) {
                                    } else {
                    ToastUtil.showShortToast(R.string.dinner_formcenter_no_print_data);
                }
                mChangePageListener.changePage(ChangePageListener.ORDERDISHLIST, null);
                break;
        }
        mChangePageListener.changePage(ChangePageListener.ORDERDISHLIST, null);
    }


    public void registerListener(ChangePageListener mChangePageListener) {
        this.mChangePageListener = mChangePageListener;
    }

    private void obtainDataFromDb() {
        if (commentsList == null) {
            try {
                TakeawayMemoDal takeawayMemoDal = OperatesFactory.create(TakeawayMemoDal.class);
                List<TakeawayMemo> orderMemos = takeawayMemoDal.getDataList();
                commentsList = new String[orderMemos.size()];

                for (int i = 0; i < orderMemos.size(); i++) {
                    commentsList[i] = orderMemos.get(i).getMemoContent();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
