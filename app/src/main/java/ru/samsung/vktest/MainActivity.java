package ru.samsung.vktest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btEnter;
    TextView tv_response, tv_access;
    final String id_client = "8136394";
    final String key = "1bVxokylhDQRgo0dTArP";
    static String TOKEN_URL = "https://oauth.vk.com/access_token";
    static String OAUTH_URL = "http://oauth.vk.com/authorize";
    static String VK_API_URL = "https://api.vk.com/method/users.get";
    static String REDIRECT_URI = "https://localhost/vk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){
        btEnter = findViewById(R.id.auth);
        btEnter.setOnClickListener(this);
        tv_access = findViewById(R.id.Access);
        tv_response = findViewById(R.id.response);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.auth:
                openOauth();
                break;
        }
    }

    private void openOauth(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.auth_dialog);
        WebView webView = dialog.findViewById(R.id.webv);
        webView.getSettings().setJavaScriptEnabled(true);
        String request = OAUTH_URL + "?client_id=" + id_client +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=" + "token";
        Log.d("My", "request: " + request);
        webView.loadUrl(OAUTH_URL + "?client_id=" + id_client +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=" + "token");
        webView.setWebViewClient(new WebViewClient() {
            boolean authComplete = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.contains("#access_token=") && authComplete == false){
                    authComplete = true;
                    url = url.replace("#", "?");
                    Uri uri = Uri.parse(url);
                    String token = uri.getQueryParameter("access_token");
                    Log.d("My", "access_token: " + token);
                    Log.d("My", "ответ: " + url);
                    dialog.dismiss();

                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                URL url1 = new URL("https://api.vk.com/method/users.get?fields=sex,bdate,photo_big,screen_name&v=5.131&access_token="+token);
                                URLConnection urlConnection = url1.openConnection();
                                Scanner scanner = new Scanner(urlConnection.getInputStream());
                                String request = scanner.nextLine();
                                Log.d("My", request);
                                Gson gson = new Gson();
                                VkUser user = gson.fromJson(new JSONObject(request).
                                        getJSONArray("response").getJSONObject(0).toString(), VkUser.class);
                                Log.d("My", "user: " + user);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();
                }
            }
        });
        dialog.show();
    }
}