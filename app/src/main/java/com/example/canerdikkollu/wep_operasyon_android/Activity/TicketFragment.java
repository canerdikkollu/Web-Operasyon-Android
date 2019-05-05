package com.example.canerdikkollu.wep_operasyon_android.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.example.canerdikkollu.wep_operasyon_android.Adapter.TicketAdapter;
import com.example.canerdikkollu.wep_operasyon_android.Interface.ILoadMore;
import com.example.canerdikkollu.wep_operasyon_android.Model.TicketModel;
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

public class TicketFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, WaveSwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;
    ListView lstTicket;
    WaveSwipeRefreshLayout waveSwipeRefreshLayout;
    View view;
    List<TicketModel> items = new ArrayList<>();
    TicketAdapter adapter;
    OkHttpClient client;
    Request request;
    int i = 1;
    boolean isSearch = false;
    public static int ticketNo;
    public static String detay;

    public TicketFragment() {}

    public static TicketFragment newInstance(String param1, String param2) {
        TicketFragment fragment = new TicketFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ticket, container, false);
        initialization();
        swipeList();
        selectItem();
        return view;
    }

    private void selectItem() {
        lstTicket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                if (isSearch == true) {
                    intent.putExtra("ID", ticketNo);
                    intent.putExtra("Detay", detay);
                } else {
                    int selectID = items.get(i).getID();
                    String selectDetay = items.get(i).getACIKLAMA();
                    intent.putExtra("ID", selectID);
                    intent.putExtra("Detay", selectDetay);
                }
                startActivity(intent);
            }
        });
    }

    private void swipeList() {
        if (isSearch == true)
            waveSwipeRefreshLayout.setRefreshing(false);
        else {
            waveSwipeRefreshLayout.setRefreshing(true);
            waveSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    loadTicket(0);
                }
            });
            setupAdapter();
        }
    }

    private void setupAdapter() {
        if (isSearch == true) {
            items.clear();
            loadTicket(0);
        }
        adapter = new TicketAdapter(lstTicket, getActivity(), items);
        lstTicket.setAdapter(adapter);
        adapter.setiLoadMore(new ILoadMore() {
            @Override
            public void onLoadMore() {
                if (items.size() < 1000000) {
                    loadTicket(i * 10);
                    i++;
                }
            }
        });
    }

    private void initialization() {
        lstTicket = (ListView) view.findViewById(R.id.lstTicket);
        waveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        TextView txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);
        lstTicket.setEmptyView(txtEmpty);
        waveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        waveSwipeRefreshLayout.setOnRefreshListener(this);
        waveSwipeRefreshLayout.setWaveColor(Color.parseColor("#343a40"));
        registerForContextMenu(lstTicket);
    }

    private void loadTicket(final int index) {
        client = new OkHttpClient();
        request = new Request.Builder().url("/*Web Address*/")
                .addHeader("APIKey", "1nZ3IpnEb1bobqIuctYN5u2KHrXtQkIMX5klr8PKjyeg")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Session", "error in getting response using async okhttp call");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();
                if (response.code() == 200) {
                    final List<TicketModel> newItems = gson.fromJson(
                            body, new TypeToken<List<TicketModel>>() {
                            }.getType()
                    );
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                adapter.setLoaded();
                                adapter.updateData(newItems, index);
                                waveSwipeRefreshLayout.setRefreshing(false);
                            } catch (Exception e) {
                                Log.i("Session", e.getLocalizedMessage());
                            }
                        }
                    });
                } else {
                    Snackbar snackbar = Snackbar.make(waveSwipeRefreshLayout, "" + response.message(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        if (waveSwipeRefreshLayout.isRefreshing())
            waveSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        isSearch = true;
        if (s.length() == 0) {
            ticketNo = 0;
            detay = "";
            isSearch = false;
            items.clear();
            setupAdapter();
            loadTicket(0);
        }
        return false;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                waveSwipeRefreshLayout.setRefreshing(false);
                items.clear();
                loadTicket(0);
                setupAdapter();
            }
        }, 3000);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Açıklama");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index = info.position;
        if (item.getTitle() == "Açıklama") {
            final String detayAc = items.get(index).getACIKLAMA();
            final Dialog myDialog;
            myDialog = new Dialog(getActivity());
            myDialog.setContentView(R.layout.custom_pop_up);
            TextView txtClose = (TextView) myDialog.findViewById(R.id.txtClose);
            TextView txtAciklama = (TextView) myDialog.findViewById(R.id.txtAciklama);
            Button btnDetayAc = (Button) myDialog.findViewById(R.id.btnDetayAc);
            txtAciklama.setText(detayAc);
            txtClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDialog.dismiss();
                }
            });
            btnDetayAc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    if (isSearch == true) {
                        intent.putExtra("ID", ticketNo);
                        intent.putExtra("Detay", detay);
                    } else {
                        int selectID = items.get(i).getID();
                        String selectDetay = items.get(i).getACIKLAMA();
                        intent.putExtra("ID", selectID);
                        intent.putExtra("Detay", selectDetay);
                    }
                    startActivity(intent);
                }
            });
            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.show();
        }
        return true;
    }
}
