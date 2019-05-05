package com.example.canerdikkollu.wep_operasyon_android.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MultiAutoCompleteTextView;
import com.example.canerdikkollu.wep_operasyon_android.R;

public class SessionDetailActivity extends AppCompatActivity {

    MultiAutoCompleteTextView txtText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_detail);
        txtText=(MultiAutoCompleteTextView)findViewById(R.id.txtDetailText);
        Bundle value=getIntent().getExtras();
        int BlockID=value.getInt("BlockID");
        String Command=value.getString("Command");
        String Text =value.getString("Text");
        txtText.setText(BlockID+"\n"+Command+"\n"+Text+"\n");
    }
}
