package com.zhongmei.bty.dinner.orderdish;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.zhongmei.bty.basemodule.orderdish.bean.DishAndStandards;
import com.zhongmei.bty.basemodule.orderdish.bean.DishInfo;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.bty.basemodule.shoppingcart.listerner.ModifyShoppingCartListener;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CreateItemTool;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListener;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCartListerTag;
import com.zhongmei.bty.common.view.Keyboard26WithClear;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.commonmodule.view.EditTextWithDeleteIcon;
import com.zhongmei.bty.data.operates.DishDal;
import com.zhongmei.bty.dinner.adapter.DinnerDishSearchAdapter;
import com.zhongmei.bty.router.RouteIntent;
import com.zhongmei.bty.snack.orderdish.OrderDishClearStatusFragment;
import com.zhongmei.bty.snack.orderdish.OrderDishClearStatusFragment_;
import com.zhongmei.bty.snack.orderdish.adapter.OrderDishAdapter;
import com.zhongmei.bty.snack.orderdish.view.OnCloseListener;
import com.zhongmei.bty.snack.orderdish.view.QuantityEditPopupWindow;
import com.zhongmei.bty.snack.orderdish.view.QuantityEditPopupWindow.DataChangeListener;
import com.zhongmei.bty.snack.orderdish.view.TouchGridView;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.ui.base.MobclickAgentFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment.CommonDialogFragmentBuilder;
import com.zhongmei.yunfu.util.MobclickAgentEvent;
import com.zhongmei.yunfu.util.ToastUtil;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemLongClick;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@EFragment(R.layout.dinner_dish_search)
public class DinnerDishSearchFragment extends MobclickAgentFragment implements TouchGridView.ItemTouchListener {

    public static String TAG = DinnerDishSearchFragment.class.getSimpleName() + "_search_type";
    public static final int SEARCH_FROM_ORDER = 0;
    public static final int SEARCH_FROM_TABLE = 1;    public static final int ALL = 0;

    public static final int FIRST_LETTER = 1;

    public static final int PRICE = 2;

    public static final int CODE = 3;

    public static final int DISHNAME = 4;

    @ViewById(R.id.btn_close)
    protected ImageButton btnClose;
    @ViewById(R.id.et_search_content)
    protected EditTextWithDeleteIcon etSearchContent;
    @ViewById(R.id.gv_search_result)
    protected TouchGridView gvSearchResult;
    @ViewById(R.id.search_type)
    protected RadioGroup searchTypeGroup;

    @ViewById(R.id.type_code)
    protected RadioButton searchCode;

    @ViewById(R.id.type_first_letter)
    protected RadioButton searchFirstLetter;

    @ViewById(R.id.type_price)
    protected RadioButton searchPrice;

    @ViewById(R.id.type_dish_name)
    protected RadioButton searchDishName;

    @ViewById(R.id.keyboard_letter)
    protected Keyboard26WithClear keyboard;

    private int searchType;

    private DishManager mDishManager;

    private ChangePageListener mChangePageListener;

    protected OrderDishAdapter mAdapter;

    private DinnerShoppingCart mShoppingCart;

    private boolean isDishToChange = false;
    private SerachAsyncTask mTask;
    private QuantityEditPopupWindow mPopupWindow;

        public void setSearchFromListener(int searchFrom, ChooseSelectedDishListener listener) {
        this.mSearchFrom = searchFrom;
        this.mChooseSelectedDishListener = listener;
    }

    private int mSearchFrom;
    private ChooseSelectedDishListener mChooseSelectedDishListener;

            public boolean keyboardIsHide() {
        if (keyboard != null) {
            return keyboard.getVisibility() == View.GONE;
        } else {
            return true;
        }
    }

    public void hideKeyboard() {
        if (keyboard != null) {
            keyboard.setVisibility(View.GONE);
            MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerOrderDishSearchKeyboardPackUp);
        }
    }

        private ModifyShoppingCartListener mShoppingCartListener = new ShoppingCartListener() {
        @Override
        public void addToShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo, ShopcartItem mShopcartItem) {
            updateItemView(mShopcartItem);
        }

        public void updateDish(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }


        @Override
        public void clearShoppingCart() {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }


        @Override
        public void removeShoppingCart(List<IShopcartItem> listOrderDishshopVo, TradeVo mTradeVo,
                                       IShopcartItemBase mShopcartItemBase) {
            updateItemView(mShopcartItemBase);
        }

        @Override
        public void exception(String message) {
            new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(getResources().getString(R.string.invalidLogin))
                    .iconType(R.drawable.commonmodule_dialog_icon_warning)
                    .negativeText(R.string.reLogin)
                    .negativeLisnter(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            RouteIntent.startLogin(getActivity());
                        }
                    })
                    .build()
                    .show(getActivity().getSupportFragmentManager(), TAG);
        }
    };


    public void updateItemView(IShopcartItemBase iShopcartItemBase) {
        if (mAdapter != null) {
            List<DishVo> dishVos = mAdapter.getDishList();
            if (Utils.isNotEmpty(dishVos)) {
                int size = dishVos.size();
                for (int i = 0; i < size; i++) {
                    DishVo dishVo = dishVos.get(i);
                    if (dishVo != null && dishVo.isSameSeries(iShopcartItemBase.getSkuUuid())) {
                        mAdapter.updateView(i, gvSearchResult);
                        break;
                    }
                }
            }
        }
    }

    public void registerListener(ChangePageListener changePageListener) {
        this.mChangePageListener = changePageListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mShoppingCart = DinnerShoppingCart.getInstance();
        mShoppingCart.registerListener(ShoppingCartListerTag.DINNER_DISH_SEARCH, mShoppingCartListener);
    }

    @Override
    public void onDestroy() {
                if (!isDishToChange) {
            SharedPreferenceUtil.getSpUtil().remove(Constant.SP_SEARCH_KEYWORD);
        }

        unregisterEventBus();
        mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_DISH_SEARCH);
        super.onDestroy();
    }

    @AfterViews
    protected void initView() {
        mDishManager = new DishManager();

        initSearchResult();
        initSearchTypeGroup();
        initSearchContent();
        initKeyboard();
                if (this.mSearchFrom == SEARCH_FROM_TABLE) {
            btnClose.setVisibility(View.INVISIBLE);
        }
            }

    private void initSearchResult() {
        initAdapter();
        gvSearchResult.setNumColumns(4);
        gvSearchResult.setAdapter(mAdapter);
        gvSearchResult.setListener(this);
                if (this.mSearchFrom == SEARCH_FROM_TABLE) {
            mAdapter.setHidClearNumber(true);
            mAdapter.setCurrentSelected(-1);
        }
            }

    protected void initAdapter() {
        mAdapter = new DinnerDishSearchAdapter(getActivity(), new ArrayList<DishVo>(), 4);
    }

    private void initSearchContent() {
                String keyWord = SharedPreferenceUtil.getSpUtil().getString(Constant.SP_SEARCH_KEYWORD, "");
        if (!TextUtils.isEmpty(keyWord)) {
                        if (mTask != null) {
                mTask.cancel(true);
            }
            mTask = new SerachAsyncTask(keyWord.toString());
            mTask.execute();
        }
        resetSearchContentFocus();
        etSearchContent.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE)
                    return false;
                onHardwareKey(v, keyCode, event);
                return true;
            }
        });
        etSearchContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                                etSearchContent.setText("");
                if (mAdapter != null) {
                    mAdapter.setDishList(null);
                }
                switchSoftInput(searchType, etSearchContent);
                            }
        });
        etSearchContent.setOnClearListener(new EditTextWithDeleteIcon.OnClearListener() {
            @Override
            public void AfterTextClear() {
                etSearchContent.setText("");
                if (mAdapter != null) {
                    mAdapter.setDishList(null);
                }

                switchSoftInput(searchType, etSearchContent);
            }
        });
    }

    private void resetSearchContentFocus() {
        etSearchContent.setInputType(InputType.TYPE_NULL | InputType.TYPE_CLASS_TEXT);
        etSearchContent.setFocusable(true);
        etSearchContent.setFocusableInTouchMode(true);
        etSearchContent.requestFocus();
    }


    private void initKeyboard() {
        Keyboard26WithClear.ClickEventListener keyboardListener = new Keyboard26WithClear.ClickEventListener() {

            @Override
            public void onClear() {
                                keyboard.setVisibility(View.GONE);
                MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerOrderDishSearchKeyboardPackUp);
            }

            @Override
            public void onBackspace() {
                String text = etSearchContent.getText().toString();
                if (text.length() > 0) {
                    etSearchContent.setText(text.subSequence(0, text.length() - 1));
                }
            }

            @Override
            public void onClick(View v) {
                etSearchContent.append((String) v.getTag());
            }
        };
        keyboard.setListener(keyboardListener);
    }

    private void switchSoftInput(int searchType, EditText etTarget) {
        switch (searchType) {
            case CODE:
            case PRICE:
            case FIRST_LETTER:
                forbiddenSoftKeyboard(etTarget);
                keyboard.setVisibility(View.VISIBLE);
                etTarget.setOnKeyListener(new OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE)
                            return false;
                        onHardwareKey(v, keyCode, event);
                        return true;
                    }
                });
                break;
            case DISHNAME:
                showSoftKeyboard(etTarget);
                keyboard.setVisibility(View.GONE);
                etTarget.setOnKeyListener(null);
                break;
        }
    }


    private void initSearchTypeGroup() {
        searchType = SharedPreferenceUtil.getSpUtil().getInt(TAG, PRICE);
        switch (searchType) {
            case CODE:
                searchCode.setChecked(true);
                break;
            case FIRST_LETTER:
                searchFirstLetter.setChecked(true);
                break;
            case PRICE:
                searchPrice.setChecked(true);
                break;
            case DISHNAME:                searchDishName.setChecked(true);
                break;
            default:
                break;
        }

        searchTypeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.type_code:
                        searchType = CODE;
                        MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerOrderDishSearchCode);
                        break;
                    case R.id.type_first_letter:
                        searchType = FIRST_LETTER;
                        MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerOrderDishSearchFirstLetter);
                        break;
                    case R.id.type_price:
                        searchType = PRICE;
                        MobclickAgentEvent.onEvent(getActivity(), MobclickAgentEvent.dinnerOrderDishSearchPrice);
                        break;
                    case R.id.type_dish_name:
                        searchType = DISHNAME;
                        break;
                }
                switchSoftInput(searchType, etSearchContent);
                searchDish(etSearchContent.getText());
                SharedPreferenceUtil.getSpUtil().putInt(TAG, searchType);
            }
        });

        switchSoftInput(searchType, etSearchContent);
    }

    private void onHardwareKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            int currentSelected = mAdapter.getCurrentSelected();
            switch (keyCode) {
                                case 160:
                case 66:
                    if (mAdapter == null || mAdapter.getCount() <= 0)
                        return;
                    if (currentSelected < 0 || currentSelected >= mAdapter.getCount())
                        return;
                    DishVo dishVo = (DishVo) mAdapter.getItem(currentSelected);
                    if (dishVo.isClear() && !mShoppingCart.getIsSalesReturn()) {
                        return;
                    }
                    if (!dishVo.isCombo() && !dishVo.isContainProperties() && !dishVo.isChangePrice()) {
                        ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
                        mShoppingCart.addDishToShoppingCart(shopcartItem, true);
                        QuantityEditPopupWindow mPopupWindow = new QuantityEditPopupWindow(getActivity(), shopcartItem, mListener);
                        mPopupWindow.showAtLocation(etSearchContent, Gravity.NO_GRAVITY, 0, 0);

                                                etSearchContent.setText("");
                    } else
                        onTouch(currentSelected);
                    break;
                                case 19:                     keyboard.setVisibility(View.GONE);
                    if (currentSelected >= gvSearchResult.getNumColumns()) {
                        currentSelected = currentSelected - gvSearchResult.getNumColumns();
                    }
                    setSelected(currentSelected);
                    break;
                case 20:                     keyboard.setVisibility(View.GONE);
                    if (currentSelected + gvSearchResult.getNumColumns() < gvSearchResult.getCount()) {
                        currentSelected = currentSelected + gvSearchResult.getNumColumns();
                    }
                    setSelected(currentSelected);
                    break;
                case 21:                     keyboard.setVisibility(View.GONE);
                    if (currentSelected > 0) {
                        currentSelected--;
                    }
                    setSelected(currentSelected);
                    break;
                case 22:                     keyboard.setVisibility(View.GONE);
                    if (currentSelected < gvSearchResult.getCount() - 1) {
                        currentSelected++;
                    }
                    setSelected(currentSelected);
                    break;
                                case 144:
                case 145:
                case 146:
                case 147:
                case 148:
                case 149:
                case 150:
                case 151:
                case 152:
                case 153:
                    etSearchContent.append((char) (keyCode - 144 + '0') + "");
                    break;
                                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                    etSearchContent.append((char) (keyCode - 29 + 'A') + "");
                    break;
                                case 67:
                    String text = etSearchContent.getText().toString();
                    if (text.length() > 0) {
                        etSearchContent.setText(text.subSequence(0, text.length() - 1));
                    }
                    break;
                                case 156:
                    break;
                                case 157:
                    break;
                                case 158:
                    break;
            }
        }
    }

    private DataChangeListener mListener = new DataChangeListener() {

        @Override
        public void dataChange(ShopcartItem shopcartItem, boolean isContainProperties) {

            if (shopcartItem.getOrderDish().isCombo()) {                saveInputKeyword();

                if (mChangePageListener != null) {
                    mShoppingCart.addDishToShoppingCart(shopcartItem, false);

                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, shopcartItem.getUuid());
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.SEARCH);
                    bundle.putBoolean(Constant.NONEEDCHECK, true);
                    mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                }
            } else if (isContainProperties || shopcartItem.getIsChangePrice() == Bool.YES) {                saveInputKeyword();

                if (mChangePageListener != null) {
                    mShoppingCart.addDishToShoppingCart(shopcartItem, false);

                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, shopcartItem.getUuid());
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.SEARCH);
                    bundle.putBoolean(Constant.NONEEDCHECK, true);
                    mChangePageListener.changePage(ChangePageListener.DISHPROPERTY, bundle);
                }
            } else {
                isDishToChange = false;

                mShoppingCart.addDishToShoppingCart(shopcartItem, true);
            }

        }
    };

    public void setSelected(int selected) {
                        gvSearchResult.smoothScrollToPosition(selected);
        mAdapter.setCurrentSelected(selected);
    }


    @AfterTextChange(R.id.et_search_content)
    protected void searchDish(Editable s) {
                if (TextUtils.isEmpty(s)) {
            return;
        }

        etSearchContent.setSelection(s.length());

        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new SerachAsyncTask(s.toString());
        mTask.execute();
    }


    class SerachAsyncTask extends AsyncTask<Void, Void, DishInfo> {

        String keyWord;

        public SerachAsyncTask(String kerword) {
            this.keyWord = kerword;
        }

        @Override
        protected DishInfo doInBackground(Void... params) {
            DishInfo temp;
            if (TextUtils.isEmpty(keyWord)) {
                temp = new DishInfo(null, null, false);
            } else {
                temp = mDishManager.search(keyWord, searchType);
            }
            return temp;
        }

        @Override
        protected void onPostExecute(DishInfo dishInfo) {
            super.onPostExecute(dishInfo);
            if (dishInfo != null && dishInfo.dishType == null && !dishInfo.scanResult && mAdapter != null) {
                mAdapter.setDishList(dishInfo.dishList);
                                if (mSearchFrom == SEARCH_FROM_TABLE) {
                    mAdapter.setCurrentSelected(-1);
                }
                            }
        }
    }

    @Click(R.id.btn_close)
    protected void clickCloseButton() {
        if (mChangePageListener != null) {
            mChangePageListener.changePage(ChangePageListener.ORDERDISHLIST, null);
        }
    }

    @Override
    public void onTouch(int postion) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }

        if (mAdapter != null) {
            final DishVo dishVo = (DishVo) mAdapter.getItem(postion);
            if (dishVo != null) {
                                                if (this.mSearchFrom == SEARCH_FROM_TABLE) {
                    if (this.mChooseSelectedDishListener != null) {
                        this.mChooseSelectedDishListener.onSelectedDish(dishVo);
                        mAdapter.setCurrentSelected(postion);
                    }
                } else {
                    if (dishVo.isClear()) {
                        return;
                    }
                    dealDishVo(dishVo);
                }
                                                etSearchContent.setText("");
                            }
        }
    }


    private void dealDishVo(DishVo dishVo) {
                if (dishVo.isCombo() || dishVo.isContainProperties() || dishVo.isChangePrice()) {
            saveInputKeyword();
        } else {
            isDishToChange = false;
        }

                boolean isUnionMainTrade = mShoppingCart.getOrder().isUnionMainTrade();
                if (dishVo.isContainProperties() && dishVo.getDishShop().getClearStatus() == ClearStatus.CLEAR) {
            DishShop dishShop;
            if (isUnionMainTrade) {
                                dishShop = dishVo.getLeastUnweighResidueFromOtherDishs();
            } else {
                dishShop = dishVo.getLeastResidueFromOtherDishs();
            }

            addOtherStandardSingleDish(dishVo, dishShop);
        } else {
            if (dishVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
                if (isUnionMainTrade) {
                                        DishShop dishShop = dishVo.getLeastUnweighResidueFromOtherDishs();
                    if (dishShop != null) {
                        addOtherStandardSingleDish(dishVo, dishShop);
                    } else {
                        ToastUtil.showShortToast(R.string.union_main_trade_not_allow_weighing_dish);
                    }
                } else {
                    addSingleDish(dishVo);
                }
            } else {
                if (dishVo.isCombo()) {                    addUnweighCombo(dishVo);
                } else {                    addSingleDish(dishVo);
                }
            }
        }
    }


    private void addSingleDish(DishVo dishVo) {
        if (dishVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
            ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
            QuantityEditPopupWindow popupWindow = new QuantityEditPopupWindow(getActivity(), shopcartItem, dishVo.isContainProperties(), mListener);
            popupWindow.setDinner(true);
            popupWindow.showAtLocation(etSearchContent, Gravity.NO_GRAVITY, 0, 0);
        } else {
            final ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
            mShoppingCart.addDishToShoppingCart(shopcartItem, true);
        }
    }


    private void addUnweighCombo(DishVo dishVo) {
        if (mChangePageListener != null) {
                        ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishVo);
            shopcartItem.setSetmealItems(new ArrayList<SetmealShopcartItem>());
            mShoppingCart.addDishToShoppingCart(shopcartItem, false);

            Bundle bundle = new Bundle();
            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, shopcartItem.getUuid());
            bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
            bundle.putBoolean(Constant.NONEEDCHECK, true);
            mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
        }
    }


    private void addOtherStandardSingleDish(DishVo dishVo, DishShop dishShop) {
        if (dishShop != null) {
            return;
        }

        Set<DishProperty> standards = DishManager.filterStandards(dishShop);
        DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
        DishAndStandards dishAndStandards = new DishAndStandards(dishShop, standards, unit);
        if (dishShop.getSaleType() == SaleType.WEIGHING) {
            ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishAndStandards);
            mPopupWindow = new QuantityEditPopupWindow(getActivity(), shopcartItem, dishVo.isContainProperties(), mListener);
            mPopupWindow.setDinner(true);
            mPopupWindow.showAtLocation(etSearchContent, Gravity.NO_GRAVITY, 0, 0);
        } else {
            ShopcartItem shopcartItem = CreateItemTool.createShopcartItem(dishAndStandards);
            mShoppingCart.addDishToShoppingCart(shopcartItem, true);
        }
    }

    private void addShoppingCart(ShopcartItem shopcartItem, Boolean isTempDish) {
        TradeVo tradeVo = mShoppingCart.getOrder();
        if (tradeVo != null && tradeVo.getTrade() != null && tradeVo.getTrade().getBusinessType() == BusinessType.BUFFET) {

            boolean lastMode = DinnerShoppingCart.getInstance().getShoppingCartVo().isGroupMode();

            DinnerShoppingCart.getInstance().getShoppingCartVo().setGroupMode(false);
                        mShoppingCart.addDishToShoppingCart(shopcartItem, isTempDish);
            DinnerShoppingCart.getInstance().getShoppingCartVo().setGroupMode(lastMode);
        } else
            mShoppingCart.addDishToShoppingCart(shopcartItem, isTempDish);
    }


    private void saveInputKeyword() {
        isDishToChange = true;                String keyword = etSearchContent.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            SharedPreferenceUtil.getSpUtil().putString(Constant.SP_SEARCH_KEYWORD,
                    keyword);
        }
    }

    @ItemLongClick(R.id.gv_search_result)
    public void myGridItemLongClicked(final DishVo dishVo) {
        if (dishVo.isContainProperties()) {
                        OrderDishClearStatusFragment orderDishClearStatusFragment = new OrderDishClearStatusFragment_();
            orderDishClearStatusFragment.setData(dishVo);
            orderDishClearStatusFragment.setOnCloseListener(new OnCloseListener() {

                @Override
                public void onClose(boolean isEnsure, Object obj) {
                    String s = etSearchContent.getText().toString();
                    if (mTask != null) {
                        mTask.cancel(true);
                    }
                    mTask = new SerachAsyncTask(s.toString());
                    mTask.execute();
                }
            });
            orderDishClearStatusFragment.show(getFragmentManager(), "clearStatusFragment");

        } else {
            int title = dishVo.isClear() ? R.string.checkUClearDishStatus : R.string.checkClearDishStatus;
            new CommonDialogFragmentBuilder(MainApplication.getInstance()).title(title)
                    .iconType(CommonDialogFragment.ICON_WARNING)
                    .negativeText(R.string.common_cancel)
                    .positiveText(R.string.common_submit)
                    .positiveLinstner(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestClearStatus(dishVo, dishVo.isClear() ? ClearStatus.SALE : ClearStatus.CLEAR);
                        }
                    })
                    .build()
                    .show(getActivity().getSupportFragmentManager(), "clearDishStatus");
        }
    }


    private void requestClearStatus(final DishVo dishVo, final ClearStatus newValue) {
        List<String> dishUuids = new ArrayList<String>();
        dishUuids.add(dishVo.getDishShop().getUuid());


        ResponseListener<Boolean> listener = new ResponseListener<Boolean>() {

            @Override
            public void onResponse(ResponseObject<Boolean> response) {
                if (ResponseObject.isOk(response) && response.getContent()) {
                    dishVo.getDishShop().setClearStatus(newValue);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatus);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatus);
                    }
                } else if (1100 == response.getStatusCode()) {
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatusNeedSync);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatusNeedSync);
                    }
                } else {
                    if (newValue == ClearStatus.SALE) {
                        ToastUtil.showLongToast(R.string.nClearstatusFailure);
                    } else {
                        ToastUtil.showLongToast(R.string.yClearstatusFailure);
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                if (newValue == ClearStatus.SALE) {
                    ToastUtil.showLongToast(BaseApplication.sInstance.getString(R.string.nClearstatusFailure) + error.getMessage());
                } else {
                    ToastUtil.showLongToast(BaseApplication.sInstance.getString(R.string.yClearstatusFailure) + error.getMessage());
                }
            }

        };

        DishDal dishDal = OperatesFactory.create(DishDal.class);
        dishDal.clearStatus(newValue, dishUuids, LoadingResponseListener.ensure(listener, getFragmentManager()));
    }

    public static interface ChooseSelectedDishListener {
        public void onSelectedDish(DishVo dishVo);
    }
}
