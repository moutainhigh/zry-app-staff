package com.zhongmei.bty.cashier.ordercenter.presenter;

import android.support.v4.app.FragmentManager;
import android.text.TextWatcher;

import com.zhongmei.bty.snack.orderdish.presenter.Presenter;
import com.zhongmei.bty.snack.orderdish.view.MvpView;

/**
 * 家消费Presenter
 *
 * @version v8.1
 * @since 2017.09.19.
 */
public class IAddFeeContract {

    public interface IAddFeeView extends MvpView {

        void setFeeValue(String value);

        void showDialog(FragmentManager fragmentManager);

        void dismissDialog();

        void setPlusEnable(boolean enable);

        void setMinusEnable(boolean enable);

        FragmentManager getViewFragmentManager();
    }

    public interface IAddFeePresenter extends Presenter<IAddFeeView> {

        void minusClick();

        void plusClick();

        void sureClick(Long deliveryOrderId, Integer deliveryPlatform);

        void numClick(int num);

    }
}
