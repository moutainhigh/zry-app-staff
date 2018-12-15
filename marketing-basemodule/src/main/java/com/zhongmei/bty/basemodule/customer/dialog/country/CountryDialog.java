package com.zhongmei.bty.basemodule.customer.dialog.country;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.util.ErpConstants;
import com.zhongmei.yunfu.context.util.CharacterParser;
import com.zhongmei.yunfu.context.util.SharedPreferenceUtil;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

/**
 * 国籍选择
 *
 * @date:2016年6月7日上午9:14:41
 */
public class CountryDialog extends Dialog implements SideBar.OnTouchingLetterChangedListener, CountryGridAdapter.OnItemSelectedListener {

    private StickyGridHeadersGridView mGridView;

    private List<CountryGridItem> mGirdList = new ArrayList<>();

    private Map<String, LetterData> sectionMap = new HashMap<>();

    private Context mContext;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    /**
     * 根据拼音来排列元数据
     */
    private PinyinComparator pinyinComparator;

    private SideBar sideBar;

    private CountryGridAdapter countryGridAdapter;

    private List<ErpCurrency> country = null;

    CountryGridAdapter.OnItemSelectedListener mListener;

    OnDialogCloseListener mCloseListener;

    private TextView tvTitle;

    private ImageButton btn_close;

    // 当前选择的user
    private ErpCurrency currentCountry;
    //gridview每行最多显示多少个元素
    private static final int GRID_NUM_COUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_country_dialog_layout);
        tvTitle = (TextView) findViewById(R.id.userlist_label);
        tvTitle.setText(R.string.customer_country_dialog_title);
        btn_close = (ImageButton) findViewById(R.id.btn_close);
        mGridView = (StickyGridHeadersGridView) findViewById(R.id.user_grid);
        sideBar = (SideBar) findViewById(R.id.sidebar);
        sideBar.setOnTouchingLetterChangedListener(this);
        initCountryData();
        countryGridAdapter = new CountryGridAdapter(mContext, mGirdList, this);
        mGridView.setAdapter(countryGridAdapter);

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

    public CountryDialog(Context context, List<ErpCurrency> countryList, ErpCurrency currentCountry, CountryGridAdapter.OnItemSelectedListener listener) {
        super(context, R.style.login_theme);
        mContext = context;
        this.country = countryList;
        mListener = listener;
        this.currentCountry = currentCountry;
    }

    @Override
    public void show() {
        super.show();
    }

    private void initCountryData() {
        if (country == null) {
            return;
        }
        characterParser = CharacterParser.getInstance();
        mGirdList = filledData(country);
        // 根据拼音首字母排序
        pinyinComparator = new PinyinComparator();
        Collections.sort(mGirdList, pinyinComparator);
        // 将首字母放到map中
        int section = 0;
        for (ListIterator<CountryGridItem> it = mGirdList.listIterator(); it.hasNext(); ) {
            CountryGridItem mGridItem = it.next();
            String letters = mGridItem.sortLetters;
            if (!sectionMap.containsKey(letters)) {
                mGridItem.section = section;
                LetterData position = new LetterData();
                position.setSection(section);
                position.setLineNum(1);
                sectionMap.put(letters, position);
                section++;
            } else {
                LetterData data = sectionMap.get(letters);
                mGridItem.section = data.getSection();
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
    private List<CountryGridItem> filledData(List<ErpCurrency> countryList) {
        List<CountryGridItem> mSortList = new ArrayList<>();

        for (int i = 0; i < countryList.size(); i++) {
            CountryGridItem gridItem = new CountryGridItem();
            ErpCurrency country = countryList.get(i);
            gridItem.erpCurrency = country;//add v8.2
            gridItem.countryEn = country.getCountryEn();
            gridItem.countryZh = country.getCountryZh();
            gridItem.areaCode = country.getAreaCode();
            String sortString;
            // 汉字转换成拼音
            if (ErpConstants.isChinese()) {
                sortString = characterParser.getSelling(country.getCountryZh()).substring(0, 1).toUpperCase(Locale.getDefault());
            } else {
                sortString = country.getCountryEn().substring(0, 1).toUpperCase(Locale.getDefault()); // 英文
            }
            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                gridItem.sortLetters = sortString.toUpperCase(Locale.getDefault());
            } else {
                gridItem.sortLetters = "#";
            }
            if (currentCountry != null && currentCountry.getId().equals(country.getId())) {
                gridItem.isSelected = true;
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
            int position = countryGridAdapter.getPositionForSection(s.charAt(0));
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
            CountryGridItem item = mGirdList.get(i);
            if (!item.sortLetters.equals(s)) {
                if (TextUtils.isEmpty(comparedS)) {
                    comparedS = item.sortLetters;
                }
//				当前的和前一次不相同，行数添加
                if (!item.sortLetters.equals(comparedS)) {
                    LetterData data = sectionMap.get(s);
                    tempLineNum = data.getLineNum();
                    totalLineNum = totalLineNum + tempLineNum;
                    comparedS = item.sortLetters;
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
    public void onSelected(Long countryId, String name, ErpCurrency erpCurrency) {
        if (mListener != null) {
            mListener.onSelected(countryId, name, erpCurrency);
        }
        this.dismiss();
    }

    public interface OnDialogCloseListener {
        void unSelected(Boolean unSelected);
    }

    /**
     * 拼音转换
     */
    public class PinyinComparator implements Comparator<CountryGridItem> {
        public int compare(CountryGridItem o1, CountryGridItem o2) {
            if (o1.sortLetters.equals("@")
                    || o2.sortLetters.equals("#")) {
                return -1;
            } else if (o1.sortLetters.equals("#")
                    || o2.sortLetters.equals("@")) {
                return 1;
            } else {
                return o1.sortLetters.compareTo(o2.sortLetters);
            }
        }
    }
}
