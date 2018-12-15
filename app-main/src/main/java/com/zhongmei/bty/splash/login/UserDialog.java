package com.zhongmei.bty.splash.login;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.customer.dialog.country.LetterData;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.zhongmei.bty.basemodule.customer.dialog.country.SideBar;
import com.zhongmei.bty.splash.login.adapter.UserGridAdapter;
import com.zhongmei.yunfu.context.util.CharacterParser;
import com.zhongmei.bty.common.util.PinyinComparator;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @date:2016年6月7日上午9:14:41
 */
public class UserDialog extends Dialog implements SideBar.OnTouchingLetterChangedListener, UserGridAdapter.OnItemClikListener {

    private StickyGridHeadersGridView mGridView;

    private List<UserGridItem> mGirdList = new ArrayList<UserGridItem>();

    private Map<String, LetterData> sectionMap = new HashMap<String, LetterData>();

    protected Context mContext;

    /**
     * 汉字转换成拼音的类
     */
    protected CharacterParser characterParser;

    /**
     * 根据拼音来排列元数据
     */
    private PinyinComparator pinyinComparator;

    private SideBar sideBar;

    protected UserGridAdapter userAdapter;

    protected List<User> userList = null;

    UserGridAdapter.OnUserSelectedListener mListener;

    OnDialogCloseListener mCloseListener;

    private TextView tvTitle;

    protected ImageButton btn_close;

    // 当前选择的user
    private User currentUser;
    //gridview每行最多显示多少个元素
    private static final int GRID_NUM_COUNT = 5;

    protected int titleResId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dialog_layout);
        tvTitle = (TextView) findViewById(R.id.userlist_label);
        if (SharedPreferenceUtil.getSpUtil().getBoolean("IsLoginActivity", true)) {
            if (titleResId > 0) {
                tvTitle.setText(titleResId);
            }
        } else {
            if (titleResId > 0) {
                tvTitle.setText(titleResId);
            } else {
                tvTitle.setText(R.string.select_waiter_label);
            }
        }

        btn_close = (ImageButton) findViewById(R.id.btn_close);
        mGridView = (StickyGridHeadersGridView) findViewById(R.id.user_grid);
        sideBar = (SideBar) findViewById(R.id.sidebar);
        sideBar.setOnTouchingLetterChangedListener(this);
        initUserData();
        userAdapter = new UserGridAdapter(mContext, mGirdList, this);
        mGridView.setAdapter(userAdapter);

        btn_close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!SharedPreferenceUtil.getSpUtil().getBoolean("IsLoginActivity", true)) {
                    if (mCloseListener != null) mCloseListener.unSelected(true);
                }
                dismiss();
            }
        });
    }

    public UserDialog(Context context, List<User> userList, User currentUser, UserGridAdapter.OnUserSelectedListener listener) {
        super(context, R.style.login_theme);
        mContext = context;
        this.userList = userList;
        mListener = listener;
        this.currentUser = currentUser;
    }

    public UserDialog(Context context, int titleResId, List<User> userList, User currentUser, UserGridAdapter.OnUserSelectedListener listener) {
        this(context, userList, currentUser, listener);
        this.titleResId = titleResId;
    }

    @Override
    public void show() {
        super.show();
    }

    private void initUserData() {
        if (userList == null) {
            return;
        }
        characterParser = CharacterParser.getInstance();
        mGirdList = filledData(userList);
        // 根据拼音首字母排序
        pinyinComparator = new PinyinComparator();
        Collections.sort(mGirdList, pinyinComparator);
        // 将首字母放到map中
        int section = 0;
        for (ListIterator<UserGridItem> it = mGirdList.listIterator(); it.hasNext(); ) {
            UserGridItem mGridItem = it.next();
            String letters = mGridItem.getSortLetters();
            if (!sectionMap.containsKey(letters)) {
                mGridItem.setSection(section);
                LetterData position = new LetterData();
                position.setSection(section);
                position.setLineNum(1);
                sectionMap.put(letters, position);
                section++;
            } else {
                LetterData data = sectionMap.get(letters);
                mGridItem.setSection(data.getSection());
                int count = data.getCount() + 1;
                data.setCount(count);
                //向上取整
                int lineNum = (int) Math.ceil(count / GRID_NUM_COUNT);
                data.setLineNum(lineNum);
            }
        }
    }

    /**
     * 为ListView填充数据
     *
     * @param
     * @return
     */
    protected List<UserGridItem> filledData(List<User> userList) {
        List<UserGridItem> mSortList = new ArrayList<UserGridItem>();

        for (int i = 0; i < userList.size(); i++) {
            UserGridItem gridItem = new UserGridItem();
            User authUser = userList.get(i);
            gridItem.setUser(authUser);//add v8.2
            gridItem.setUserName(authUser.getDisplayName());
            gridItem.setUserId(authUser.getId());
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(authUser.getDisplayName());
            String sortString = pinyin.substring(0, 1).toUpperCase(Locale.getDefault());

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                gridItem.setSortLetters(sortString.toUpperCase(Locale.getDefault()));
            } else {
                gridItem.setSortLetters("#");
            }
            if (currentUser != null && currentUser.getId().equals(authUser.getId())) {
                gridItem.setSelected(true);
            }
            mSortList.add(gridItem);
        }
        return mSortList;
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        Integer selction = 0;
        LetterData itemPosition = sectionMap.get(s);
        if (selction != null && selction != -1 && itemPosition != null) {
            selction = itemPosition.getSection();
            int position = userAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                selction = getPreLine(s) * GRID_NUM_COUNT + selction * GRID_NUM_COUNT;
                mGridView.setSelection(selction);

            }
        }
    }

    // 前面有好多行,一个字母前面有多少行,不包括head
    private int getPreLine(String s) {
        // 已经比较的首字母
        String comparedS = "";
        // 总行数
        int totalLineNum = 0;
        // 临时存储的行数,由于第一行是0开始，初始值为0
        int tempLineNum = 0;
        for (int i = 0; i < mGirdList.size(); i++) {
            UserGridItem item = mGirdList.get(i);
            if (!item.getSortLetters().equals(s)) {
                if (TextUtils.isEmpty(comparedS)) {
                    comparedS = item.getSortLetters();
                }
//				当前的和前一次不相同，行数添加
                if (!item.getSortLetters().equals(comparedS)) {
                    LetterData data = sectionMap.get(s);
                    tempLineNum = data.getLineNum();
                    totalLineNum = totalLineNum + tempLineNum;
                    comparedS = item.getSortLetters();
                }

            } else {
                return totalLineNum;
            }
        }
        return totalLineNum;
    }

    public void setDialogCloseListener(OnDialogCloseListener closeListener) {
        this.mCloseListener = closeListener;
    }


    @Override
    public void onItemClick(UserGridItem item) {
        if (mListener != null) {
            mListener.onSelected(item.getUser(), item.getUserId(), item.getUserName());
        }
        this.dismiss();
    }

    public interface OnDialogCloseListener {
        public void unSelected(Boolean unSelected);
    }

}
