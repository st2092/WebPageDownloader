package bddevlabs.com.webpagedownloader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView webpageContentTextView;
    private EditText urlEditText;

    public class WebPageDownloader extends AsyncTask<String, Void, String> {

        protected MainActivity mainActivity;

        public WebPageDownloader(MainActivity mainActivityRef) {
            mainActivity = mainActivityRef;
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuffer webPageContentStringBuffer = new StringBuffer();
            if (urls.length <= 0) {
                return "Invalid URL";
            }

            try {
                String url = urls[0];
                URL webUrl = new URL(url);
                InputStream webPageDataStream = webUrl.openStream();
                InputStreamReader webPageDataReader = new InputStreamReader(webPageDataStream);
                int maxBytesToRead = 1024;
                char[] buffer = new char[maxBytesToRead];
                int bytesRead =  webPageDataReader.read(buffer);

                while(bytesRead != -1) {
                    webPageContentStringBuffer.append(buffer, 0, bytesRead);
                    bytesRead = webPageDataReader.read(buffer);
                }

                return webPageContentStringBuffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Failed to get webpage content";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mainActivity != null) {
                mainActivity.showProgressBar();
            }
        }

        @Override
        protected void onProgressUpdate(Void... progress) {
            super.onProgressUpdate();
        }

        @Override
        protected void onPostExecute(String webContent) {
            super.onPostExecute(webContent);
            if (mainActivity != null) {
                mainActivity.dismissProgressBar();
                mainActivity.setWebPageContent(webContent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        webpageContentTextView = findViewById(R.id.webpageContentTextView);
        urlEditText = findViewById(R.id.urlEditText);
    }

    public void getWebPageContent(View view) {
        WebPageDownloader webPageDownloader = new WebPageDownloader(this);
        String url = urlEditText.getText().toString();
        try {
            // note: don't use get() if you want your UI thread to keep running
            // 'get' will pause the UI thread until the task finishes executing
            webPageDownloader.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void log(String message) {
        Log.d("[Web Page Downloader] ", message);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setWebPageContent(String webPageContent) {
        webpageContentTextView.setText(webPageContent);
    }
}
