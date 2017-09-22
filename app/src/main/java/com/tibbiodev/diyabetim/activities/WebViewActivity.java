package com.tibbiodev.diyabetim.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tibbiodev.diyabetim.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String link = getIntent().getStringExtra("link");
        String title = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle(title);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Yükleniyor...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                progress.cancel();
            }
        });
        webView.loadUrl(link);

    }
}
