package com.bagaswirapradana.berta_beritakita.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bagaswirapradana.berta_beritakita.R;

public class DetailBeritaActivity extends AppCompatActivity {

    WebView webView;
    ProgressBar loader;
    String urlBerita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);

        Intent intent = getIntent();
        urlBerita = intent.getStringExtra("url");
        loader = findViewById(R.id.loader);
        webView = findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.loadUrl(urlBerita);
        webView.canGoBack();

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    loader.setVisibility(View.GONE);
                } else {
                    loader.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
