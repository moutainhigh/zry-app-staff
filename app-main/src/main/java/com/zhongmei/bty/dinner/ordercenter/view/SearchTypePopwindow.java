package com.zhongmei.bty.dinner.ordercenter.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;

public class SearchTypePopwindow extends PopupWindow implements OnTouchListener {
    public static final byte ALL = 0;
    public static final byte TRADE_NUMBER = 1;
    public static final byte SERAIL_NUMBER = 2;
    public static final byte TABLE_NUMBER = 3;
    public static final byte PHONE_NUMBER = 4;

    private String[] contents;
    private Context context;
    private ChooseListener chooseListener;
    private ListView listView;

    public SearchTypePopwindow(Context context) {
        super(context);
        this.context = context;
        initial();
    }

    public void setContent(String[] contents, int choosePosition) {
        this.contents = contents;
        SearchTypeAdapter adapter = new SearchTypeAdapter(context, contents);
        adapter.setChoosePosition(choosePosition);

        int totalHeight = DensityUtil.dip2px(context, 60);
        totalHeight *= adapter.getCount();
        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        params.height += 5;
        listView.setLayoutParams(params);
        listView.setAdapter(adapter);
    }

    private void initial() {
        final LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dinner_ordercenter_searchtype_popwindow, null);
        setContentView(view);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setTouchable(true);
        setTouchInterceptor(this);
        setBackgroundDrawable(new ColorDrawable(128));
        setAnimationStyle(R.style.dinner_table_info_waiterwindow_style);


        listView = (ListView) view.findViewById(R.id.searchtype_lv);
    }


    private class SearchTypeAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        private Context context;

        private String[] contents;

        private int choosePosition = -1;
        public SearchTypeAdapter(Context context, String[] contents) {
            this.context = context;
            this.contents = contents;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return contents.length;
        }

        @Override
        public Object getItem(int position) {
            return contents[position];
        }

        public String getChooseItem() {
            if (choosePosition == -1) {
                return "";
            }
            return contents[choosePosition];
        }

        public void setChoosePosition(int choosePosition) {
            this.choosePosition = choosePosition;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView =
                        inflater.inflate(R.layout.dinner_ordercenter_searchtype_item, null);
            }
            convertView.setLayoutParams(new android.widget.AbsListView.LayoutParams(
                    android.widget.AbsListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 60)));
            final TextView searchTypeTv = (TextView) convertView.findViewById(R.id.searchtype_tv);
            searchTypeTv.setText(contents[position]);
            if (position == choosePosition) {
                searchTypeTv.setTextColor(Color.parseColor("#ff7901"));
            } else {
                searchTypeTv.setTextColor(Color.BLACK);
            }

            convertView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    chooseListener.choose(searchTypeTv.getText().toString(), (byte) position);
                    hide();

                }
            });
            return convertView;
        }

        public void resetAdapter() {
            setChoosePosition(-1);
            notifyDataSetChanged();
        }

    }

    public void setChooseListener(ChooseListener chooseListener) {
        this.chooseListener = chooseListener;

    }

    public interface ChooseListener {
        void choose(String content, int position);
    }


    public void hide() {
        if (isShowing()) {
            dismiss();
        }
    }

    public void show(View parentView) {
        if (parentView != null) {
            if (!isShowing()) {
                showAtLocation(parentView, Gravity.LEFT | Gravity.TOP, 0, 0);
            }

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!inRangeOfView(event)) {
                hide();
            }

        }
        return false;
    }

    private boolean inRangeOfView(MotionEvent ev) {
        View contentView = getContentView();
        View contentView2 = contentView.findViewById(R.id.content_ll);

        int[] location = new int[2];
        contentView2.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        Log.i("zhubo", "x，y:" + x + "," + y);
        int width = contentView2.getWidth();
        int height = contentView2.getHeight();
        if (ev.getX() < x || ev.getX() > (x + width) || ev.getY() < y || ev.getY() > (y + height)) {
            return false;
        }
        return true;
    }


}
