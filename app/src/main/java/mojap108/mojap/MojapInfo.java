package mojap108.mojap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MojapInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infoview);
        Button infoView = (Button)findViewById(R.id.dialogButtonOK);
        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.loadData(String.format(Constants.htmlText, Constants.InfoText), "text/html", "utf-8");
        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
