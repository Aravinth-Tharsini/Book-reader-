package com.example.pdfbookreader;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
  private int topPx = 0, bottomPx = 0;

  public class Bars {
    @JavascriptInterface public int top() { return topPx; }
    @JavascriptInterface public int bottom() { return bottomPx; }
    @JavascriptInterface public void light(final boolean on) {
      runOnUiThread(() -> {
        WindowInsetsControllerCompat c = WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        c.setAppearanceLightStatusBars(on);
        c.setAppearanceLightNavigationBars(on);
      });
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
    getWindow().setStatusBarColor(Color.TRANSPARENT);
    getWindow().setNavigationBarColor(Color.TRANSPARENT);
    if (Build.VERSION.SDK_INT >= 29) {
      getWindow().setNavigationBarContrastEnforced(false);
    }
    getBridge().getWebView().addJavascriptInterface(new Bars(), "NativeBars");
    ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), (v, insets) -> {
      Insets s = insets.getInsets(WindowInsetsCompat.Type.systemBars());
      float d = getResources().getDisplayMetrics().density;
      topPx = Math.round(s.top / d);
      bottomPx = Math.round(s.bottom / d);
      return insets;
    });
  }
}
