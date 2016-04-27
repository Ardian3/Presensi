package com.joatsy.android.presensiandroid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class KeluarActivity extends AppCompatActivity {
    EditText txtAlasanKeluar;
    String fNip = Environment.getExternalStorageDirectory() +  "/" + "nip.txt";
    String Nip="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keluar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        txtAlasanKeluar = (EditText) findViewById(R.id.txAlasanKeluar);
        Nip= ReadNip();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Nip.length()<=5)
                {
                    Intent intent = new Intent(KeluarActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else {
                    if (isNetworkAvailable()) {
                        //Toast.makeText(MainActivity.this, "Network is Available", Toast.LENGTH_LONG).show();
                        String xUrl = "http://192.168.43.7/presensiandroid/keluar.php?n="+Nip+"&a="+txtAlasanKeluar.getText().toString();
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(xUrl.toString());
                    } else {
                        Toast.makeText(KeluarActivity.this, "Network is not Available", Toast.LENGTH_LONG).show();
                    }
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }
    private String ReadNip()
    {
        try {
            File myFile = new File(fNip);
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));
            String aDataRow = "";
            String aBuffer = "";
            while ((aDataRow = myReader.readLine()) != null) {
                aBuffer += aDataRow; //+ "\n";
            }
            myReader.close();
            //Toast.makeText(KeluarActivity.this, aBuffer, Toast.LENGTH_LONG).show();
            return aBuffer;

        } catch (Exception e) {
            Toast.makeText(KeluarActivity.this, "Failed to open file nip", Toast.LENGTH_LONG).show();
        }
        return null;
    }
    private boolean isNetworkAvailable(){
        boolean available = false;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if(networkInfo !=null && networkInfo.isAvailable())
            available = true;
        return available;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                return downloadContent(params[0]);
            } catch (IOException e) {
                return "Unable to retrieve data. URL may be invalid.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(KeluarActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }

    private String downloadContent(String myurl) throws IOException {
        InputStream is = null;
        int length = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = convertInputStreamToString(is, length);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }


    public String convertInputStreamToString(InputStream stream, int length) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[length];
        reader.read(buffer);
        return new String(buffer);
    }

}
