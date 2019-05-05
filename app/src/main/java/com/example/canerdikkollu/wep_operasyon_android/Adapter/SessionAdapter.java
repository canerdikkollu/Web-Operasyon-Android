package com.example.canerdikkollu.wep_operasyon_android.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.canerdikkollu.wep_operasyon_android.Interface.ILoadMore;
import com.example.canerdikkollu.wep_operasyon_android.Model.SessionModel;
import com.example.canerdikkollu.wep_operasyon_android.R;

import java.util.List;

public class SessionAdapter extends BaseAdapter {
    boolean isLoading;
    Activity activity;
    List<SessionModel> items;
    LayoutInflater mInflater;
    ILoadMore iLoadMore;
    int visibleThreshold = 5, lastVisibleItem, totalItemCount;

    public SessionAdapter(ListView listView, Activity activity, List<SessionModel> items) {
        this.activity = activity;
        this.items = items;
        this.mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = mInflater.inflate(R.layout.session_layout, null);
        SessionModel sessionModel = items.get(i);
        TextView txtSessionID = (TextView) rowView.findViewById(R.id.txtSessionID);
        TextView txtRunningTime = (TextView) rowView.findViewById(R.id.txtRunningTime);
        TextView txtBlockSessID = (TextView) rowView.findViewById(R.id.txtBlockingSesID);
        TextView txtHostName = (TextView) rowView.findViewById(R.id.txtHostname);
        TextView txtProgramName = (TextView) rowView.findViewById(R.id.txtProgName);
        TextView txtLoginName = (TextView) rowView.findViewById(R.id.txtLoginName);
        TextView txtCommand = (TextView) rowView.findViewById(R.id.txtCommand);
        TextView txtStatus = (TextView) rowView.findViewById(R.id.txtStatus);
        TextView txtText = (TextView) rowView.findViewById(R.id.txtText);
        TextView txtStartTime = (TextView) rowView.findViewById(R.id.txtStartTime);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

        txtSessionID.setText(Integer.valueOf(sessionModel.getSession_id()) + "");
        txtRunningTime.setText(sessionModel.getRunningTime() + "");
        txtBlockSessID.setText(Integer.valueOf(sessionModel.getBlocking_session_id()) + "");
        txtHostName.setText(sessionModel.getHostName() + "");
        txtProgramName.setText(sessionModel.getProgram_name() + "");
        txtLoginName.setText(sessionModel.getLoginName() + "");
        txtCommand.setText(sessionModel.getCommand() + "");
        txtStatus.setText(sessionModel.getStatus() + "");
        txtText.setText(sessionModel.getText() + "");
        txtStartTime.setText(sessionModel.getStartTime() + "");
        //Load image
        String imageName = sessionModel.getCommand().toLowerCase().toString();
        imageName = imageName.replace(" ", "");
        Glide.with(activity)
                .load(getImage(imageName))
                .into(imageView);
        return rowView;
    }

    public int getImage(String imageName) {
        int drawableResourceId = activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());
        return drawableResourceId;
    }

    public void setLoaded() {
        isLoading = false;
    }
    public void updateData(List<SessionModel> sesModels) {
        items.addAll(sesModels);
        notifyDataSetChanged();
    }
    public void setiLoadMore(ILoadMore iLoadMore) {
        this.iLoadMore = iLoadMore;
    }
}
