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

/**
 * @Date：2015年7月8日 下午2:16:01
 * @Description: 菜品搜索界面
 * @Version: 5.8
 * <p>
 * rights reserved.
 */
@EFragment(R.layout.dinner_dish_search)
public class DinnerDishSearchFragment extends MobclickAgentFragment implements TouchGridView.ItemTouchListener {

    public static String TAG = DinnerDishSearchFragment.class.getSimpleName() + "_search_type";
    public static final int SEARCH_FROM_ORDER = 0;//点单搜索

    public static final int SEARCH_FROM_TABLE = 1;//查找桌台搜索
    public static final int ALL = 0;

    public static final int FIRST_LETTER = 1;

    public static final int PRICE = 2;

    public static final int CODE = 3;

    public static final int DISHNAME = 4;

    @ViewById(R.id.btn_close)
    protected ImageButton btnClose;// 关闭界面按钮

    @ViewById(R.id.et_search_content)
    protected EditTextWithDeleteIcon etSearchContent;// 搜索按钮

    @ViewById(R.id.gv_search_result)
    protected TouchGridView gvSearchResult;// 搜索结果

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

    private boolean isDishToChange = false;//是否因为点菜导致的跳转（套餐、变价和多规格商品等）

    private SerachAsyncTask mTask;
    private QuantityEditPopupWindow mPopupWindow;

    //add 20170608 begin
    public void setSearchFromListener(int searchFrom, ChooseSelectedDishListener listener) {
        this.mSearchFrom = searchFrom;
        this.mChooseSelectedDishListener = listener;
    }

    private int mSearchFrom;
    private ChooseSelectedDishListener mChooseSelectedDishListener;

    //add 20170608 end
    //add begin 20170615
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

    //add end 20170615
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

        /**
         * 清空购物车
         */
        @Override
        public void clearShoppingCart() {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }

        /**
         * 删除购物车中某一项
         */
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

    /**
     * 刷新指定菜品
     *
     * @param iShopcartItemBase 当前操作的菜品
     */
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
        //不是因为点菜导致的页面切换，移除keyword
        if (!isDishToChange) {
            SharedPreferenceUtil.getSpUtil().remove(Constant.SP_SEARCH_KEYWORD);
        }

        unregisterEventBus();
        mShoppingCart.unRegisterListenerByTag(ShoppingCartListerTag.DINNER_DISH_SEARCH);
        super.onDestroy();
    }

    @AfterViews
    protected void initView() {
//		mAdapter = new DinnerDishSearchAdapter(getActivity(), new ArrayList<DishVo>());
//		gvSearchResult.setAdapter(mAdapter);
//		gvSearchResult.setListener(this);
//		
//		mDishManager = new DishManager();
//		mDishManager.loadData();
//		
//		String keyWord = SharedPreferenceUtil.getSpUtil().getString(Constant.SP_SEARCH_KEYWORD, "");
//		if (!TextUtils.isEmpty(keyWord)) {
//			etSearchContent.setText(keyWord);
//			SharedPreferenceUtil.getSpUtil().remove(Constant.SP_SEARCH_KEYWORD);
//		}
//		
//		etSearchContent.setFocusable(true);
//		etSearchContent.setFocusableInTouchMode(true);
//		etSearchContent.requestFocus();
//		etSearchContent.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					etSearchContent.setText("");
//				}
//			}
//		});
        mDishManager = new DishManager();

        initSearchResult();
        initSearchTypeGroup();
        initSearchContent();
        initKeyboard();
        //modify begin 20170608
        if (this.mSearchFrom == SEARCH_FROM_TABLE) {
            btnClose.setVisibility(View.INVISIBLE);
        }
        //modify end 20170608
    }

    private void initSearchResult() {
        initAdapter();
        gvSearchResult.setNumColumns(4);
        gvSearchResult.setAdapter(mAdapter);
        gvSearchResult.setListener(this);
        //modify begin 20170608
        if (this.mSearchFrom == SEARCH_FROM_TABLE) {
            mAdapter.setHidClearNumber(true);
            mAdapter.setCurrentSelected(-1);
        }
        //modify end 20170608
    }

    protected void initAdapter() {
        mAdapter = new DinnerDishSearchAdapter(getActivity(), new ArrayList<DishVo>(), 4);
    }

    private void initSearchContent() {
        //套餐或者规格界面返回时，获取对应关键字
        String keyWord = SharedPreferenceUtil.getSpUtil().getString(Constant.SP_SEARCH_KEYWORD, "");
        if (!TextUtils.isEmpty(keyWord)) {
//            etSearchContent.setText(keyWord);
            //使用之前关键字进行搜索
            if (mTask != null) {
                mTask.cancel(true);
            }
            mTask = new SerachAsyncTask(keyWord.toString());
            mTask.execute();
//            SharedPreferenceUtil.getSpUtil().remove(Constant.SP_SEARCH_KEYWORD);//不再这里直接移除，以免再次点击套餐返回时，查询的关键字为空
        }
//        forbiddenSoftKeyboard(etSearchContent);
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
                //if (keyboard.getVisibility() != View.VISIBLE) {
//                keyboard.setVisibility(View.VISIBLE);//dzb
                etSearchContent.setText("");
                if (mAdapter != null) {
                    mAdapter.setDishList(null);
                }
                switchSoftInput(searchType, etSearchContent);
                //}
            }
        });
        etSearchContent.setOnClearListener(new EditTextWithDeleteIcon.OnClearListener() {
            @Override
            public void AfterTextClear() {
//                keyboard.setVisibility(View.VISIBLE);//dzb
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
                //etSearchContent.setText("");
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
            case DISHNAME://呼出手写键盘
                searchDishName.setChecked(true);
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
                //enter
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

                        //选中菜品后，清空文字，但不清空已经查询出来的菜品，并打开键盘
                        etSearchContent.setText("");
//                        keyboard.setVisibility(View.VISIBLE);//dzb
                    } else
                        onTouch(currentSelected);
                    break;
                //上下左右的事件
                case 19: //上
                    keyboard.setVisibility(View.GONE);
                    if (currentSelected >= gvSearchResult.getNumColumns()) {
                        currentSelected = currentSelected - gvSearchResult.getNumColumns();
                    }
                    setSelected(currentSelected);
                    break;
                case 20: //下
                    keyboard.setVisibility(View.GONE);
                    if (currentSelected + gvSearchResult.getNumColumns() < gvSearchResult.getCount()) {
                        currentSelected = currentSelected + gvSearchResult.getNumColumns();
                    }
                    setSelected(currentSelected);
                    break;
                case 21: //左
                    keyboard.setVisibility(View.GONE);
                    if (currentSelected > 0) {
                        currentSelected--;
                    }
                    setSelected(currentSelected);
                    break;
                case 22: //右
                    keyboard.setVisibility(View.GONE);
                    if (currentSelected < gvSearchResult.getCount() - 1) {
                        currentSelected++;
                    }
                    setSelected(currentSelected);
                    break;
                //1-9的事件 144为0的keyCode
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
                //a-z字母处理   29为a的keyCode
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
                //退格键
                case 67:
                    String text = etSearchContent.getText().toString();
                    if (text.length() > 0) {
                        etSearchContent.setText(text.subSequence(0, text.length() - 1));
                    }
                    break;
                //-
                case 156:
                    break;
                //+
                case 157:
                    break;
                //.
                case 158:
                    break;
            }
        }
    }

    private DataChangeListener mListener = new DataChangeListener() {

        @Override
        public void dataChange(ShopcartItem shopcartItem, boolean isContainProperties) {
            /*if (shopcartItem.getDishShop().getClearStatus() == ClearStatus.CLEAR && !mShoppingCart.getIsSalesReturn()) {
                ToastUtil.showShortToast(R.string.order_dish_sold_out);
                return;
            }
            */
            if (shopcartItem.getOrderDish().isCombo()) {// 套餐
                saveInputKeyword();

                if (mChangePageListener != null) {
                    mShoppingCart.addDishToShoppingCart(shopcartItem, false);

                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, shopcartItem.getUuid());
                    bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.SEARCH);
                    bundle.putBoolean(Constant.NONEEDCHECK, true);
                    mChangePageListener.changePage(ChangePageListener.DISHCOMBO, bundle);
                }
            } else if (isContainProperties || shopcartItem.getIsChangePrice() == Bool.YES) {// 有属性
                saveInputKeyword();

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
            /*mShoppingCart.addDishToShoppingCart(shopcartItem, true);*/
        }
    };

    public void setSelected(int selected) {
        //gvSearchResult.getChildAt(mAdapter.getCurrentSelected()).setBackgroundColor(Color.TRANSPARENT);
        //gvSearchResult.getChildAt(selected).setBackgroundResource(R.drawable.search_selected);
        gvSearchResult.smoothScrollToPosition(selected);
        mAdapter.setCurrentSelected(selected);
    }

    /**
     * @Title: searchDish
     * @Description: 搜索商品
     * @Param @param s TODO
     * @Return void 返回类型
     */
    @AfterTextChange(R.id.et_search_content)
    protected void searchDish(Editable s) {
        //输入框清空后，不在这里处理，因为需要不同的处理
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

    /**
     * 搜索AsyncTask
     */
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
                //modify begin 20170608
                if (mSearchFrom == SEARCH_FROM_TABLE) {
                    mAdapter.setCurrentSelected(-1);
                }
                //modify end 20170608
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

//        keyboard.setVisibility(View.GONE);
        if (mAdapter != null) {
            final DishVo dishVo = (DishVo) mAdapter.getItem(postion);
            if (dishVo != null) {
                //如果是查找桌台
                //modify begin 20170608
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
                //modify end 20170608
                //选中菜品后，清空文字，但不清空已经查询出来的菜品，并打开键盘
                etSearchContent.setText("");
                // keyboard.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * @Title: dealDish
     * @Description: 处理菜品
     * @Param @param dishVo TODO
     * @Return void 返回类型
     */
    private void dealDishVo(DishVo dishVo) {
        //套餐、多规格和变价商品会导致页面跳转，需要缓存keyword信息，此处不区别是否称重商品
        if (dishVo.isCombo() || dishVo.isContainProperties() || dishVo.isChangePrice()) {
            saveInputKeyword();
        } else {
            isDishToChange = false;
        }

        //是否是联台主单
        boolean isUnionMainTrade = mShoppingCart.getOrder().isUnionMainTrade();
        //如果多规格菜品默认菜品被估清，那么从系列菜品中找到一个未被估清的菜品加入到购物车
        if (dishVo.isContainProperties() && dishVo.getDishShop().getClearStatus() == ClearStatus.CLEAR) {
            DishShop dishShop;
            if (isUnionMainTrade) {
                //联台主单只能找非称重的商品
                dishShop = dishVo.getLeastUnweighResidueFromOtherDishs();
            } else {
                dishShop = dishVo.getLeastResidueFromOtherDishs();
            }

            addOtherStandardSingleDish(dishVo, dishShop);
        } else {
            if (dishVo.getDishShop().getSaleType() == SaleType.WEIGHING) {
                if (isUnionMainTrade) {
                    //联台主单获取其他规格的非称重商品，商品无多规格或者其他规格内找不到，提示联台主单不能添加称重商品
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
                if (dishVo.isCombo()) {// 套餐
                    addUnweighCombo(dishVo);
                } else {// 直接添加购物车
                    addSingleDish(dishVo);
                }
            }
        }
    }

    /**
     * 添加单菜
     *
     * @param dishVo
     */
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

    /**
     * 添加非称重套餐
     *
     * @param dishVo
     */
    private void addUnweighCombo(DishVo dishVo) {
        if (mChangePageListener != null) {
            // 构建一个套餐壳购物车
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

    /**
     * 添加dishvo里其他规格的单菜菜品
     *
     * @param dishVo
     * @param dishShop
     */
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
            //DishCarte dishCarte = BuffetManager.getInstance().getCurComboVo(BuffetManager.getInstance().getBuffetComboList(tradeVo), tradeVo).getDishCarte();
            //List<DishShop> shopList = DishCache.getDishCarteDetailHolder().getDishShopsByCarte(dishCarte.getUuid());

            boolean lastMode = DinnerShoppingCart.getInstance().getShoppingCartVo().isGroupMode();
            /*if (shopList != null && shopList.contains(shopcartItem.getDishShop())) {
                DinnerShoppingCart.getInstance().getShoppingCartVo().setGroupMode(true);
            } else {*/
            DinnerShoppingCart.getInstance().getShoppingCartVo().setGroupMode(false);
            //}
            mShoppingCart.addDishToShoppingCart(shopcartItem, isTempDish);
            DinnerShoppingCart.getInstance().getShoppingCartVo().setGroupMode(lastMode);
        } else
            mShoppingCart.addDishToShoppingCart(shopcartItem, isTempDish);
    }


    private void saveInputKeyword() {
        isDishToChange = true;//标示是因为点菜导致的页面跳转，在本页面销毁时，不会移除关键字
        //保存关键字
        String keyword = etSearchContent.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            SharedPreferenceUtil.getSpUtil().putString(Constant.SP_SEARCH_KEYWORD,
                    keyword);
        }
    }

    @ItemLongClick(R.id.gv_search_result)
    public void myGridItemLongClicked(final DishVo dishVo) {
        if (dishVo.isContainProperties()) {
            // 弹出估清界面
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

    /**
     * @Title: requestClearStatus
     * @Description: 请求更改估清状态
     * @Param @param dishShop TODO
     * @Return void 返回类型
     */
    private void requestClearStatus(final DishVo dishVo, final ClearStatus newValue) {
        List<String> dishUuids = new ArrayList<String>();
        dishUuids.add(dishVo.getDishShop().getUuid());

        /*
         * 估清请求结果
         */
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
