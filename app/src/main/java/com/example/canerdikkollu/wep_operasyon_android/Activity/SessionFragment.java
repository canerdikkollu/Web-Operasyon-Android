package com.example.canerdikkollu.wep_operasyon_android.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.canerdikkollu.wep_operasyon_android.Adapter.SessionAdapter;
import com.example.canerdikkollu.wep_operasyon_android.Model.SessionModel;
import com.example.canerdikkollu.wep_operasyon_android.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SessionFragment extends Fragment implements WaveSwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1, mParam2;

    WaveSwipeRefreshLayout swipeRefreshLayout;
    ListView lstSession;
    SessionAdapter adapter;
    List<SessionModel> items = new ArrayList<>();
    View view;
    String currUser = "";
    boolean isDelete = false;

    public SessionFragment() {}

    public static SessionFragment newInstance(String param1, String param2) {
        SessionFragment fragment = new SessionFragment();
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
            currUser = getArguments().getString("currUser");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_session, container, false);
        initialization();
        swipeList();
        return view;
    }

    private void swipeList() {
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadSession();
            }
        });
        setupAdapter();
    }

    private void setupAdapter() {
        if (isDelete == true) {
            items.clear();
        }
        adapter = new SessionAdapter(lstSession, getActivity(), items);
        lstSession.setAdapter(adapter);
    }

    private void initialization() {
        swipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.sesSwipeLayout);
        lstSession = (ListView) view.findViewById(R.id.lstSession);
        swipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setWaveColor(Color.parseColor("#343a40"));
        registerForContextMenu(lstSession);
    }

    private void loadSession() {
        OkHttpClient httpClient = new OkHttpClient();
        String url = "/*Web Address*/";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("APIKey", "1nZ3IpnEb1bobqIuctYN5u2KHrXtQkIMX5klr8PKjyeg")
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Session", "error in getting response using async okhttp call");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Gson gson = new Gson();
                if (response.code() == 200) {
                    final List<SessionModel> newItems = gson.fromJson(
                            body,
                            new TypeToken<List<SessionModel>>() {
                            }.getType()
                    );

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                adapter.updateData(newItems);
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                Log.i("Session", e.getLocalizedMessage());
                            }
                        }
                    });
                } else {
                    Snackbar snackbar = Snackbar.make(swipeRefreshLayout, "Beklenmedik bir hata oluştu. Detay: " + response.message(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

    public void sendPost(final int sessionID, final String username) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                String urlSession = "/*Web Address*/";
                try {
                    URL url = new URL(urlSession);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setRequestProperty("APIKey", "1nZ3IpnEb1bobqIuctYN5u2KHrXtQkIMX5klr8PKjyeg");

                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("session_id", sessionID);
                    jsonParam.put("username", username);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    os.close();

                    if (conn.getResponseCode() == 200) {
                        Snackbar snackbar = Snackbar.make(swipeRefreshLayout, "" + conn.getResponseMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                        items.clear();
                        loadSession();
                        setupAdapter();
                        isDelete = true;
                    } else {
                        Snackbar snackbar = Snackbar.make(swipeRefreshLayout, "Beklenmedik Bir Hata Oluştu. Detay: " + conn.getResponseMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    Snackbar snackbar = Snackbar.make(swipeRefreshLayout,e.getLocalizedMessage()+"", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        setupAdapter();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                items.clear();
                loadSession();
                setupAdapter();
            }
        }, 3000);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Kill");
        menu.add("Information");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index = info.position;
        if (item.getTitle() == "Kill") {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            int selectSessionID = items.get(index).getSession_id();
                            sendPost(selectSessionID, currUser);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            loadSession();
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Bunu Yapmak İstediğine Emin Misin?").setPositiveButton("Evet", dialogClickListener)
                    .setNegativeButton("Hayır", dialogClickListener).show();
        } else if (item.getTitle() == "Information") {
            int selectBlockSesID = items.get(index).getBlocking_session_id();
            String selectCommand = items.get(index).getCommand();
            String selectSessionText = items.get(index).getText();
            Intent intent = new Intent(getActivity(), SessionDetailActivity.class);
            intent.putExtra("BlockID", selectBlockSesID);
            intent.putExtra("Command", selectCommand);
            intent.putExtra("Text", selectSessionText);
            startActivity(intent);
        }
        return true;
    }
}
