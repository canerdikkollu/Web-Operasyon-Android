package com.example.canerdikkollu.wep_operasyon_android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.canerdikkollu.wep_operasyon_android.Interface.ILoadMore;
import com.example.canerdikkollu.wep_operasyon_android.Model.DetailModel;
import com.example.canerdikkollu.wep_operasyon_android.R;
import java.util.List;

public class DetailAdapter extends BaseAdapter {
    boolean isLoading;
    Activity activity;
    List<DetailModel> items;
    private LayoutInflater mInflater;
    ILoadMore iLoadMore;
    int visibleThreshold = 5, lastVisibleItem, totalItemCount;

    public DetailAdapter(ListView listView, Activity activity, List<DetailModel> items) {
        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.items = items;
        this.activity = activity;
        final LinearLayoutManager layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.currentScrollState = scrollState;
                this.isScrollCompleted();
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;
                //this.isScrollCompleted();
            }
            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (iLoadMore != null)
                            iLoadMore.onLoadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.detail_layout, null);
        DetailModel tmpitem = items.get(i);
        TextView txtTarih = (TextView) rowView.findViewById(R.id.txtTarih);
        TextView txtOperator = (TextView) rowView.findViewById(R.id.txtOperator);
        TextView txtAciklama = (TextView) rowView.findViewById(R.id.txtAciklama);
        TextView txtCCO = (TextView) rowView.findViewById(R.id.txtCCO);
        txtTarih.setText(tmpitem.getTARIH() + "");
        txtAciklama.setText(tmpitem.getACIKLAMA() + "");
        txtCCO.setText(tmpitem.getCCO() + "");
        txtOperator.setText(tmpitem.getOPERATOR() + "");
        return rowView;
    }

    public void setLoaded(){ isLoading = false; }

    public void updateData(List<DetailModel> detailModels, int index) {

        for (int i = index; i < index +10; i++)
        {
            if (i == detailModels.size())
                break;
            else if(detailModels.size()<index)
                break;
            else
                items.add(detailModels.get(i));
        }
        notifyDataSetChanged();
    }

    public void setiLoadMore(ILoadMore iLoadMore) {
        this.iLoadMore = iLoadMore;
    }

}
