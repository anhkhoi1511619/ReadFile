package com.example.fileapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fileapp.File.ConfigFileManager;
import com.example.fileapp.File.ExtStorageAccess;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private Button btnTakeFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initial();

    }

    private void initial() {
        btnTakeFile = findViewById(R.id.btnTake);
        btnTakeFile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                ConfigFileManager configFileManager = new ConfigFileManager();
                JSONObject jsonObject = configFileManager.readJsonFile(getApplicationContext());
                configFileManager.writeConfigFile(getApplicationContext(), "1234567", configFileManager.getConfigFileData(4));
                jsonObject = configFileManager.readJsonFile(getApplicationContext());
            }
        });
    }


}