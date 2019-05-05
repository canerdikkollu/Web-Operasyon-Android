package com.example.canerdikkollu.wep_operasyon_android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.example.canerdikkollu.wep_operasyon_android.Interface.ILoadMore;
import com.example.canerdikkollu.wep_operasyon_android.Model.TicketModel;
import com.example.canerdikkollu.wep_operasyon_android.R;
import com.example.canerdikkollu.wep_operasyon_android.Activity.TicketFragment;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends BaseAdapter implements Filterable {
    ValueFilter valueFilter;
    boolean isLoading;
    Activity activity;
    List<TicketModel> items;
    private LayoutInflater mInflater;
    ILoadMore iLoadMore;
    private ArrayList<TicketModel> modelList;
    int visibleThreshold = 5, lastVisibleItem, totalItemCount;

    public TicketAdapter(ListView listView, Activity activity, List<TicketModel> items) {
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

        View rowView = mInflater.inflate(R.layout.ticket_layout, null);
        TicketModel tmpitem = items.get(i);
        TextView txtID = (TextView) rowView.findViewById(R.id.txtID);
        TextView txtTarih = (TextView) rowView.findViewById(R.id.txtTarih);
        TextView txtCreator = (TextView) rowView.findViewById(R.id.txtCreator);
        TextView txtMusteri = (TextView) rowView.findViewById(R.id.txtMusteri);
        TextView txtHizmet = (TextView) rowView.findViewById(R.id.txtHizmet);
        TextView txtPriority = (TextView) rowView.findViewById(R.id.txtPriority);
        TextView txtAssignedTo = (TextView) rowView.findViewById(R.id.txtAssignedTo);
        TextView txtWhoIs = (TextView) rowView.findViewById(R.id.txtWhoIs);
        TextView txtLastModDate = (TextView) rowView.findViewById(R.id.txtLastModDate);
        TextView txtAciklama = (TextView) rowView.findViewById(R.id.txtAciklama);

        txtID.setText(Integer.valueOf(tmpitem.getID()) + "");
        txtTarih.setText("A.T: " + tmpitem.getTARIH() + "");
        txtCreator.setText("A: " + tmpitem.getCREATOR() + "");
        txtMusteri.setText(Integer.valueOf(tmpitem.getMUSTERI()) + "");
        txtHizmet.setText(Integer.valueOf(tmpitem.getHIZMET()) + "");
        txtPriority.setText("TP - " + Integer.valueOf(tmpitem.getPRIORITY()) + "");
        txtAssignedTo.setText("Atanan: " + tmpitem.getASSIGNEDTO() + "");
        txtWhoIs.setText("S.G: " + tmpitem.getWHOIS() + "  ");
        txtLastModDate.setText("S.G.T: " + tmpitem.getLASTMODIFIEDDATE() + "");
        txtAciklama.setText(tmpitem.getACIKLAMA() + "");

        return rowView;
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void updateData(List<TicketModel> tlModels, int index) {
        for (int i = index; i < index + 10; i++) {
            if (i == tlModels.size())
                break;
            else {
                items.add(tlModels.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public void setiLoadMore(ILoadMore iLoadMore) {
        this.iLoadMore = iLoadMore;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<TicketModel> filterList = new ArrayList<TicketModel>();
                for (int i = 0; i < items.size(); i++) {
                    if (String.valueOf(items.get(i).getID()).contains(constraint.toString())) {
                        TicketModel tlModel = new TicketModel(items.get(i).getID(),
                                items.get(i).getTARIH(),
                                items.get(i).getCREATOR(),
                                items.get(i).getMUSTERI(),
                                items.get(i).getHIZMET(),
                                items.get(i).getPRIORITY(),
                                items.get(i).getASSIGNEDTO(),
                                items.get(i).getWHOIS(),
                                items.get(i).getLASTMODIFIEDDATE(),
                                items.get(i).getACIKLAMA());
                        filterList.add(tlModel);
                        TicketFragment.ticketNo = tlModel.getID();
                        TicketFragment.detay = tlModel.getACIKLAMA();
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = items.size();
                results.values = items;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items = (ArrayList<TicketModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
