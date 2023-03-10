package com.example.fileapp.File;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExtStorageAccess {

    private String extPath; // 外部ストレージのパス
    private boolean extStorageFlg;// 外部ストレージ有無 true:あり, false:なし

    public ExtStorageAccess (Context context) {
        File[] externalStorageVolumes = ContextCompat.getExternalFilesDirs(context.getApplicationContext(), null);

        if (externalStorageVolumes.length >= 1) {
            extPath = externalStorageVolumes[externalStorageVolumes.length - 1].getPath();
            extStorageFlg = true;
        } else {
            extPath = context.getFilesDir().getPath();
            extStorageFlg = false;
        }
        Log.d(TAG, "ExtStorageAccess: " + extPath);
    }

    /**
     * 概要:引数に指定したファイルを取得.
     * @return : 引数に指定したファイル
     */
    public File getFile(String fileName) {
        File file = new File(extPath+"/"+fileName);
        return file.getAbsoluteFile();
    }
    /**
     * 概要：fileの読み込み処理
     * @param file :読み込むファイル
     * @return str:読み込んだファイルの文字列
     */
    public String readFile(File file) {
        String ret = null;
        String str = null;
        FileReader fr = null;
        BufferedReader br;
        StringBuilder data;

        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        br = new BufferedReader(fr);
        data = new StringBuilder();

        try {
            str = br.readLine();
            while (str!= null) {
                data.append(str);
                str = br.readLine();
            }
            ret = data.toString();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    /**
     * 概要：新規ファイルの作成
     * @param extPath:新規ファイルを置くパス
     * @param fileName:新規ファイルの名称
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createFile(String extPath, String fileName) {
        Path paths = Paths.get(extPath + "/" + fileName);
        try {
            Files.createFile(paths);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 概要：ストレージのパス取得
     * @return ストレージのパス
     */
    public String getExtPath() {
        return extPath;
    }


    public void writeFile(String str, File file) {
        changeWriteAuthority(file);

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(str);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 概要：ファイルの書き込み権限がなければ書き込み権限ありに変更する
     */
    private void changeWriteAuthority(File file){
        if(!file.canWrite()){
            file.setWritable(true);
        }
    }

}
