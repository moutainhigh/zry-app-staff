package com.zhongmei.bty.mobilepay.views.indicator;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;



public interface Indicator {

    void setAdapter(Adapter adapter);

    void setOnItemClickListener(OnItemClickListener onItemClickListener);

    abstract class Adapter {

        private DataSetObservable observable = new DataSetObservable();


        public abstract View getPrevActionView(Indicator indicator, int orientation);

        public abstract View getNextActionView(Indicator indicator, int orientation);

        public abstract int getCount();

        public abstract Object getItem(int position);

        public abstract View getView(Indicator indicator, int position);

        public void notifyDataSetChanged() {
            observable.notifyChanged();
        }

        public void notifyDataSetInvalidate() {
            observable.notifyInvalidated();
        }

        public void registerDataSetObserver(DataSetObserver observer) {
            observable.registerObserver(observer);
        }


        public void unRegisterDataSetObserver(DataSetObserver observer) {
            observable.unregisterObserver(observer);
        }
    }

    interface OnItemClickListener {
        void onItemClick(Indicator indicator, Adapter adapter, int position);
    }
}
