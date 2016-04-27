package com.joatsy.android.presensiandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class RegisterActivity extends AppCompatActivity {
    Button btnSetRegister;
    EditText txtNip;
    String fNip = Environment.getExternalStorageDirectory() +  "/" + "nip.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnSetRegister = (Button) findViewById(R.id.btnSetNip);
        txtNip = (EditText) findViewById(R.id.txNIP);
        btnSetRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveNip(txtNip.getText().toString());
                finish();
                System.exit(0);
            }
        });
    }
    private void SaveNip(String Nip)
    {
        try {
            File myFile = new File(fNip);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(Nip);
            myOutWriter.close();
            fOut.close();
            //
            //FileOutputStream fileout=openFileOutput(fNip, MODE_PRIVATE);
            //OutputStreamWriter outputWriter=new OutputStreamWriter(fileout);
            //outputWriter.write(Nip.toString());
           // outputWriter.close();

            //display file saved message
            Toast.makeText(getBaseContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
