package com.joatsy.android.presensiandroid;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Environment;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.BufferedReader;
        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.io.OutputStreamWriter;
        import java.io.Reader;
        import java.io.UnsupportedEncodingException;
        import java.net.HttpURLConnection;
        import java.net.URL;


public class MainActivity extends AppCompatActivity {
    ImageButton btnMasukHome;
    ImageButton btnKeluarHome;
    ImageButton btnCutiHome;
    ImageButton btnPulangHome;
    TextView txtNIPHome;
    String Nip="";
    String fNip = Environment.getExternalStorageDirectory() +  "/" + "nip.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Aplikasi Presensi Android", Toast.LENGTH_LONG).show();
        btnMasukHome = (ImageButton) findViewById(R.id.btnHomeMasuk);
        btnKeluarHome = (ImageButton) findViewById(R.id.btnHomeKeluar);
        btnCutiHome = (ImageButton) findViewById(R.id.btnHomeCuti);
        btnPulangHome = (ImageButton) findViewById(R.id.btnHomePulang);
        txtNIPHome = (TextView) findViewById(R.id.txNIPhome);
        //SaveNip("12");
        Nip= ReadNip();
        if (Nip.length()<=5)
        {
            txtNIPHome.setText("NIP Belum Terdaftar");
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
        else {
            txtNIPHome.setText(Nip);
        }

        btnMasukHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nip= ReadNip();
                if (Nip.length()<=5)
                {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else {
                    if (isNetworkAvailable()) {
                        //Toast.makeText(MainActivity.this, "Network is Available", Toast.LENGTH_LONG).show();
                        String xUrl = "http://192.168.43.7/presensiandroid/presensi.php?a=masuk&n="+Nip;
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(xUrl.toString());
                    } else {
                        Toast.makeText(MainActivity.this, "Network is not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnPulangHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nip= ReadNip();
                if (Nip.length()<=5)
                {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else {
                    if (isNetworkAvailable()) {
                        //Toast.makeText(MainActivity.this, "Network is Available", Toast.LENGTH_LONG).show();
                        String xUrl = "http://192.168.43.7/presensiandroid/presensi.php?a=pulang&n="+Nip;
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(xUrl.toString());
                    } else {
                        Toast.makeText(MainActivity.this, "Network is not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnCutiHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nip= ReadNip();
                if (Nip.length()<=5)
                {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else {
                    if (isNetworkAvailable()) {
                        Intent intent = new Intent(MainActivity.this, CutiActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Network is not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btnKeluarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nip= ReadNip();
                if (Nip.length()<=5)
                {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else {
                    if (isNetworkAvailable()) {
                        Intent intent = new Intent(MainActivity.this, KeluarActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Network is not Available", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
    private void SaveNip(String Nip)
    {
        try {
            FileOutputStream fileout=openFileOutput(fNip, MODE_PRIVATE);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            outputWriter.write(Nip.toString());
            outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean fileExists(Context context, String filename) {
        File file = context.getFileStreamPath(filename);
        if(file == null || !file.exists()) {
            return false;
        }
        return true;
    }
    private String ReadNip()
    {
        try {
            File myFile = new File(fNip);
            if(myFile == null || !myFile.exists()) {
                Toast.makeText(MainActivity.this, "Failed to open file nip", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
           else {
                FileInputStream fIn = new FileInputStream(myFile);
                BufferedReader myReader = new BufferedReader(
                        new InputStreamReader(fIn));
                String aDataRow = "";
                String aBuffer = "";
                while ((aDataRow = myReader.readLine()) != null) {
                    aBuffer += aDataRow; //+ "\n";
                }
                myReader.close();
                //Toast.makeText(MainActivity.this, aBuffer, Toast.LENGTH_LONG).show();
                return aBuffer;
            }


        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Failed to open file nip", Toast.LENGTH_LONG).show();
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
            String tidakTerdaftar="NIP anda tidak terdaftar";
            if (result.toString().equals(tidakTerdaftar))
            {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
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
