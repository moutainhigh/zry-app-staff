package com.zhongmei.yunfu.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zhongmei.yunfu.ui.base.entity.FragmentVo;
import com.zhongmei.yunfu.ui.view.CalmLoadingDialogFragment;
import com.zhongmei.yunfu.context.UILoadingController;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 所有Fragment的父类
 *
 * @date 2014-8-18
 */
public class BasicFragment extends Fragment implements UILoadingController {

    protected static final String TAG = BasicFragment.class.getSimpleName();

    private Map<String, FragmentManager> fragmentMapTag = new HashMap<>();

    private boolean isDestroyView;

    public BasicFragment() {
        super();
    }

    protected boolean isDestroyView() {
        return isDestroyView;
    }

    public BaseActivity getRootActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        if (null != extras) {
            getBundleExtras(extras);
        }
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                Log.i("IdleHandler", "queueIdle");
                onInit();
                return false; //false 表示只监听一次IDLE事件,之后就不会再执行	这个函数了.
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initId();
        initViewsAndEvents();
        isDestroyView = false;
        if (view != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    /**
     * 初始化数据
     */
    protected void onInit() {
    }

    /**
     * 获取bundle数据
     *
     * @param extras
     */
    protected void getBundleExtras(Bundle extras) {
    }

    /**
     * 加载layout布局
     *
     * @return id of layout resource
     */
    protected int getContentViewLayoutID() {
        return 0;
    }

    /**
     * 初始化UI控件
     */
    protected void initId() {
    }

    /**
     * 初始化视图
     */
    protected void initViewsAndEvents() {
    }


    public <T extends View> T findViewById(int id) {
        if (getView() != null) {
            return (T) getView().findViewById(id);
        }
        return null;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//		if(AuthUserCache.getCurrentUser() == null){
//			 new CommonDialogFragmentBuilder().title(getResources().getString(R.string.invalidLogin))
//				.iconType(R.drawable.common_dialog_icon_warning)
//				.negativeText(R.string.reLogin)
//				.negativeLisnter(new OnClickListener() {
//
//					@Override
//					public void onClick(View arg0) {
////						解决anr后activity not attached 问题
//						if(getActivity()==null||getActivity().isDestroyed()){
//							return;
//						}
//			    		 Intent intent = new Intent();
//			    		 intent.setClassName("com.zhongmei.bty",
//			    		 "com.zhongmei.bty.splash.login.LoginActivity_");
//			    		 startActivity(intent);
//			    		 getActivity().finish();
//					}
//				})
//				.build()
//				.show(this.getFragmentManager(), TAG);
//
//		 }

    }

    public void addFragment(int containerViewId, Fragment fragment, String tag) {
        if (getFragmentManager() != null) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentMapTag.put(tag, getFragmentManager());

        }

    }

    /**
     * 获取 support fragment manager
     *
     * @return
     */
    protected FragmentManager getSupportFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void addChildFragment(int containerViewId, Fragment fragment, String tag) {
        if (getFragmentManager() != null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(containerViewId, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentMapTag.put(tag, getChildFragmentManager());
        }

    }

    /**
     * @Title: addFragment
     * @Description: 添加碎片
     * @Param mFragmentVo  碎片对象
     * @Param isAnim 是否有动画
     * @Return void 返回类型
     */
    public void addFragment(FragmentVo mFragmentVo, Boolean isAnim) {
        if (getFragmentManager() != null) {
            FragmentTransaction fragmentTransaction;
            if (isAnim) {
                fragmentTransaction = getFragmentTransaction(getFragmentManager());
            } else {
                fragmentTransaction = getFragmentManager().beginTransaction();
            }
            fragmentTransaction.add(mFragmentVo.getContainerViewId(), mFragmentVo.getFragment(), mFragmentVo.getTag());
            fragmentTransaction.commitAllowingStateLoss();
            fragmentMapTag.put(mFragmentVo.getTag(), getFragmentManager());

        }
    }

    /**
     * @Title: showFragment
     * @Description: 显示碎片
     * @Param mFragmentVo
     * @Param isAnim TODO
     * @Return void 返回类型
     */
    public void showFragment(Fragment mFragment, Boolean isAnim) {
        if (getFragmentManager() != null) {
            FragmentTransaction fragmentTransaction;
            if (isAnim) {
                fragmentTransaction = getFragmentTransaction(getFragmentManager());
            } else {
                fragmentTransaction = getFragmentManager().beginTransaction();
            }
            fragmentTransaction.show(mFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        try {
            if (!getActivity().isDestroyed() && getFragmentManager() != null) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(containerViewId, fragment, tag);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentMapTag.put(tag, getFragmentManager());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    protected void replaceChildFragment(int containerViewId, Fragment fragment, String tag) {
        if (getChildFragmentManager() != null) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentMapTag.put(tag, getChildFragmentManager());
        }
    }

    protected void replaceFragmentByActivity(int containerViewId, Fragment fragment, String tag) {
        if (null != getActivity()) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(containerViewId, fragment)
                    .commit();

            fragmentMapTag.put(tag, getActivity().getSupportFragmentManager());
        }
    }

    /**
     * @Title: replaceFragment
     * @Description: 当个替换碎片
     * @Param mFragmentVo
     * @Param isAnim TODO
     * @Return void 返回类型
     */
    protected void replaceFragment(FragmentVo mFragmentVo, Boolean isAnim) {
        FragmentTransaction fragmentTransaction;
        if (isAnim) {
            fragmentTransaction = getFragmentTransaction(getFragmentManager());
        } else {
            fragmentTransaction = getFragmentManager().beginTransaction();
        }
        fragmentTransaction.replace(mFragmentVo.getContainerViewId(), mFragmentVo.getFragment(), mFragmentVo.getTag());
        fragmentMapTag.put(mFragmentVo.getTag(), getFragmentManager());
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * @Title: replaceFragment
     * @Description: 当个替换子碎片
     * @Param mFragmentVo
     * @Param isAnim TODO
     * @Return void 返回类型
     */
    protected void replaceChildFragment(FragmentVo mFragmentVo, Boolean isAnim) {
        FragmentTransaction fragmentTransaction;
        if (isAnim) {
            fragmentTransaction = getFragmentTransaction(getChildFragmentManager());
        } else {
            fragmentTransaction = getChildFragmentManager().beginTransaction();
        }
        fragmentTransaction.replace(mFragmentVo.getContainerViewId(), mFragmentVo.getFragment(), mFragmentVo.getTag());
        fragmentMapTag.put(mFragmentVo.getTag(), getChildFragmentManager());
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * @Title: replaceFragment
     * @Description: 批量替换碎片
     * @Param @param listFragment：要替换的碎片列表
     * @Param @param isAnim 是否需要动画
     * @Return void 返回类型
     */
    protected void replaceFragment(List<FragmentVo> listFragment, Boolean isAnim) {
        FragmentTransaction fragmentTransaction;
        if (isAnim) {
            fragmentTransaction = getFragmentTransaction(getFragmentManager());
        } else {
            fragmentTransaction = getFragmentManager().beginTransaction();
        }
        for (FragmentVo mFragmentVo : listFragment) {
            fragmentTransaction.replace(mFragmentVo.getContainerViewId(), mFragmentVo.getFragment(), mFragmentVo.getTag());
            fragmentMapTag.put(mFragmentVo.getTag(), getFragmentManager());
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * @Title: removeFragment
     * @Description: 移除碎片
     * @Param TODO
     * @Return void 返回类型
     */
    protected void removeChileFragment(Fragment mFragment, String tag) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.remove(mFragment);
        fragmentMapTag.remove(tag);
        fragmentTransaction.commit();
    }


    /**
     * @Title: removeFragment
     * @Description: 移除碎片
     * @Param TODO
     * @Return void 返回类型
     */
    protected void removeFragment(Fragment mFragment, String tag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.remove(mFragment);
        fragmentMapTag.remove(tag);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroyView() {
        unregisterEventBus();
        isDestroyView = true;
        super.onDestroyView();
    }

    public FragmentTransaction getFragmentTransaction(FragmentManager mFragmentManager) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		/*fragmentTransaction.setCustomAnimations(R.animator.fragment_right_enter,
			R.animator.fragment_right_exit,
			R.animator.fragment_pop_right_exit,
			R.animator.fragment_pop_right_enter);*/
        return fragmentTransaction;
    }

    @Override
    public void onDestroy() {

        if (!getActivity().isFinishing()) {
            for (String key : fragmentMapTag.keySet()) {
                if (fragmentMapTag.get(key) != null) {
                    tryRemoveTargetFragment(fragmentMapTag.get(key), key);
                }
            }
        }
        fragmentMapTag.clear();

        dismissLoadingProgressDialog();
        super.onDestroy();
    }

    /**
     * 注册EventBus
     */
    protected void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    protected void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 用EventBus发送一个事件
     *
     * @param obj
     */
    public void post(Object obj) {
        EventBus.getDefault().post(obj);
    }

    /**
     * 用EventBus发送一个Sticky事件
     *
     * @param obj
     */
    public void postSticky(Object obj) {
        EventBus.getDefault().postSticky(obj);
    }

    /**
     * 显示一组View
     *
     * @param views
     */
    public void show(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 隐藏一组View
     *
     * @param views
     */
    public void hide(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 删除fragment的子fragment，确保程序的安全性(在fragment摧毁时调用)
     *
     * @param fm
     * @param tag
     */
    protected void tryRemoveTargetFragment(FragmentManager fm, String tag) {
        if (fm == null || fm.isDestroyed()) {
            return;
        }
        if (!getActivity().isFinishing()) {
            Fragment fragment = fm.findFragmentByTag(tag);
            if (fragment != null) {
                FragmentTransaction ft = fm.beginTransaction();
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    /**
     * 禁用软键盘
     *
     * @param editText 指定的输入框
     */
    protected void forbiddenSoftKeyboard(EditText editText) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(editText, false);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }

    }

    protected void showSoftKeyboard(EditText editText) {
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    @Override
    public void showLoadingDialog() {
        showLoadingProgressDialog();
    }

    @Override
    public void dismissLoadingDialog() {
        dismissLoadingProgressDialog();
    }

    public interface OnActivityTouchListener {
        public void onActivityTouched(MotionEvent event);
    }

    public void hideFragment(Fragment mFragment) {
        if (getFragmentManager() != null) {
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = getFragmentManager().beginTransaction();

            fragmentTransaction.hide(mFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private CalmLoadingDialogFragment mDialogFragment = null;

    public void showLoadingProgressDialog() {
        if (mDialogFragment == null) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                mDialogFragment = CalmLoadingDialogFragment.showByAllowingStateLoss(fragmentManager);
                fragmentManager.executePendingTransactions();
            }
        }
    }

    public void dismissLoadingProgressDialog() {
        if (mDialogFragment != null) {
            CalmLoadingDialogFragment.hide(mDialogFragment);
            mDialogFragment = null;
        }
    }

	/*@Override
	public void showToast(String msg) {
		((BaseActivity) getActivity()).showToast(msg);
	}*/
}
