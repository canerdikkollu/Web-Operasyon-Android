package com.example.canerdikkollu.wep_operasyon_android.Activity;

import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.canerdikkollu.wep_operasyon_android.Adapter.DetailAdapter;
import com.example.canerdikkollu.wep_operasyon_android.Interface.ILoadMore;
import com.example.canerdikkollu.wep_operasyon_android.Model.DetailModel;
import com.example.canerdikkollu.wep_operasyon_android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity implements WaveSwipeRefreshLayout.OnRefreshListener {
    OkHttpClient client;
    Request request;
    DetailAdapter adapter;
    List<DetailModel> detailItems = new ArrayList<>();
    ListView lstDetail;
    TextView txtTicketNoDetay, txtEmpty, txtDetay;
    WaveSwipeRefreshLayout swipeRefreshLayout;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initialization();
    }

    private void initialization() {
        lstDetail = (ListView) findViewById(android.R.id.list);
        txtTicketNoDetay = (TextView) findViewById(R.id.txtTicketNoDetay);
        txtEmpty = (TextView) findViewById(android.R.id.empty);
        swipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.dtSwipeLayout);
        lstDetail.setEmptyView(txtEmpty);

        swipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(DetailActivity.this);
        swipeRefreshLayout.setWaveColor(Color.parseColor("#343a40"));

        Bundle value = getIntent().getExtras();
        final int ticketNo = value.getInt("ID");
        txtTicketNoDetay.setText(String.valueOf(ticketNo) + " Detayları");

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadDetail(ticketNo, 0);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDetail(ticketNo, i * 10);
                i++;
                setupAdapter(ticketNo);
            }
        });
        setupAdapter(ticketNo);
    }

    private void setupAdapter(final int ticketNo) {
        adapter = new DetailAdapter(lstDetail, DetailActivity.this, detailItems);
        lstDetail.setAdapter(adapter);
        adapter.setiLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if (detailItems.size() < 100000) {
                    loadDetail(ticketNo, i * 10);
                    i++;
                }
            }
        });
    }

    private void loadDetail(int ticketNo, final int index) {
        client = new OkHttpClient();
        request = new Request.Builder().url("/*Web address*/" + ticketNo)
                .addHeader("APIKey", "1nZ3IpnEb1bobqIuctYN5u2KHrXtQkIMX5klr8PKjyeg")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();
                if (response.code() == 200) {
                    final List<DetailModel> newItems = gson.fromJson(
                            body, new TypeToken<List<DetailModel>>() {
                            }.getType()
                    );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.updateData(newItems, index);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    Snackbar snackbar = Snackbar.make(swipeRefreshLayout, "" + response.message(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                detailItems.clear();
                initialization();
            }
        }, 3000);
    }
}
