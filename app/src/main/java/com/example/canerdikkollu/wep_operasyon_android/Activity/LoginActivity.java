package com.example.canerdikkollu.wep_operasyon_android.Activity;

import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.canerdikkollu.wep_operasyon_android.R;
import org.json.JSONObject;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnLogin;
    LinearLayout loginLayout;
    CheckBox cbShowHide;
    ImageView imgLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initialization();
    }

    private void initialization() {
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        loginLayout = (LinearLayout) findViewById(R.id.loginLayout);
        cbShowHide = (CheckBox) findViewById(R.id.cbShowHidePass);
        imgLogin = (ImageView) findViewById(R.id.imgLogin);

        YoYo.with(Techniques.SlideInUp).duration(3000)
                .playOn(findViewById(R.id.imgLogin));

        cbShowHide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    cbShowHide.setText("Parolayı Gizle");
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// show password
                } else {
                    cbShowHide.setText("Parolayı Göster");
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());// hide password
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (username.equals("")) {
                    YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.edtUsername));
                    if (password.equals("")) {

                        YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.edtPassword));
                        Snackbar snackbar = Snackbar.make(loginLayout, "Bu alanlar boş bırakılamaz!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }else{
                        Snackbar snackbar = Snackbar.make(loginLayout, "Kullanıcı adı boş bırakılamaz!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }else if (password.equals("")) {
                    YoYo.with(Techniques.Shake).duration(700).playOn(findViewById(R.id.edtPassword));
                    Snackbar snackbar = Snackbar.make(loginLayout, "Parola alanı boş bırakılamaz!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    sendPostUser(username, password);
                }
            }
        });
    }

    private void sendPostUser(final String username, final String password) {
        runOnUiThread(new Runnable() {
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
                    jsonParam.put("username", username);
                    jsonParam.put("pass", password);

                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    os.close();
                    if (conn.getResponseCode() == 200) {

                        Snackbar snackbar = Snackbar.make(loginLayout, "Giriş İşlemi Başarılı!", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("currUser", username);
                        startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar.make(loginLayout, "Kullanıcı adı veya parola hatalı olabilir. Detay: " + conn.getResponseMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    Snackbar snackbar = Snackbar.make(loginLayout, "" + e.getLocalizedMessage(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }
}
