package com.rebuild.dragonboy;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.*;
import android.view.*;
public class MainActivity extends Activity {
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().getDecorView().setSystemUiVisibility(5894|1024|512|4096);
  WebView w=new WebView(this);w.setBackgroundColor(0xff101827);w.getSettings().setJavaScriptEnabled(true);w.getSettings().setDomStorageEnabled(true);w.getSettings().setAllowFileAccess(true);w.setWebChromeClient(new WebChromeClient());w.loadUrl("file:///android_asset/client/index.html");setContentView(w);
 }
 @Override public void onBackPressed(){super.onBackPressed();}
}
