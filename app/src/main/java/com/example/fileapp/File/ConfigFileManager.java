package com.example.fileapp.File;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ConfigFileManager {
    //JSONファイルの親キー
    private final String CONTROL_SETTING_KEY = "ControlSetting";
    private final String READER_SETTING_KEY = "ReaderSetting";
    private final String VEHICLE_SETTING_KEY = "VehicleSetting";

    String LOGIN_PASSWORD_JSON_KEY = "LoginPassword";
    String USER_CODE_JSON_KEY ="UserCode";
    String PAY_RESULT_DISP_TIME_JSON_KEY = "PayResultDispTime";
    String QR_BRIGHTNESS_JSON_KEY = "Brightness";
    String VEHICLE_NUM_JSON_KEY = "VehicleNum";

    String CONFIG_FILE_NAME = "configFile.json";


    private final ConfigFileData[] CONFIGFILE_DATA_ARRAY = new ConfigFileData[] {
            new ConfigFileData(1, LOGIN_PASSWORD_JSON_KEY, 0, CONTROL_SETTING_KEY),
            new ConfigFileData(2, USER_CODE_JSON_KEY, 0, CONTROL_SETTING_KEY),
            new ConfigFileData(3, PAY_RESULT_DISP_TIME_JSON_KEY, 1,  READER_SETTING_KEY),
            new ConfigFileData(4, QR_BRIGHTNESS_JSON_KEY, 1,  READER_SETTING_KEY),
            new ConfigFileData(5, VEHICLE_NUM_JSON_KEY, 1, VEHICLE_SETTING_KEY),
    };

    public ConfigFileData getConfigFileData(int ID) {
        for (ConfigFileData configFileData : CONFIGFILE_DATA_ARRAY) {
            if (configFileData.getId() == ID) {
                return configFileData;
            }
        }
        return null;
    }
    /**
     * 概要：設定ファイルに書き込むデータを更新し、更新後のデータを取得する
     * @param value:更新対象のJSONファイルのバリュー
     * @param object:設定ファイルに書き込む情報(更新前)
     * @return object:設定ファイルに書き込む情報(更新後)
     */
    private JSONObject getUpdateConfigInfo(String parent, String key, String value, int type, JSONObject object) {
        try {
            if (type == 1) {
                object.getJSONObject(parent).put(key, Integer.parseInt(value));
            } else {
                object.getJSONObject(parent).put(key, value);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 概要：設定ファイルへの書き込み処理
     * @param value:書き込む値
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void writeConfigFile(Context context, String value, ConfigFileData configFileData) {
        if (configFileData != null) {
            ExtStorageAccess storageAccess = new ExtStorageAccess(context);
            File file = storageAccess.getFile(CONFIG_FILE_NAME);

            String parent = configFileData.getParent();
            String key = configFileData.getKey();
            int type = configFileData.getValueType();

            String jsString = getUpdateConfigInfo(parent, key, value, type, readJsonFile(context)).toString();
            storageAccess.writeFile(jsString, file);
        }
    }

    /**
     * 概要：読み込んだ設定ファイルのJSONObject(設定情報:configInfo)を取得する
     * @return 設定情報(JSONObject)
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject readJsonFile(Context context) {
        ExtStorageAccess storageAccess = new ExtStorageAccess(context);
        File file = storageAccess.getFile(CONFIG_FILE_NAME);
        try {
            if (storageAccess.readFile(file) == null){
                storageAccess.createFile(storageAccess.getExtPath(), CONFIG_FILE_NAME);
            }
            return new JSONObject(storageAccess.readFile(file));
        } catch (JSONException e) {
            storageAccess.writeFile(makeInitialJson().toString(), file);
            e.printStackTrace();
        }
        return null;
    }




    public JSONObject makeInitialJson() {
        JSONObject ret = new JSONObject();
        JSONObject controlObj = new JSONObject();
        JSONObject readerObj = new JSONObject();
        JSONObject vehicleObj = new JSONObject();

        try {
            controlObj.put(LOGIN_PASSWORD_JSON_KEY, "00000000");
            controlObj.put(USER_CODE_JSON_KEY, "FF");

            readerObj.put(PAY_RESULT_DISP_TIME_JSON_KEY, 5);
            readerObj.put(QR_BRIGHTNESS_JSON_KEY, 0);

            vehicleObj.put(VEHICLE_NUM_JSON_KEY, "00000");


            ret.put(CONTROL_SETTING_KEY, controlObj);
            ret.put(READER_SETTING_KEY, readerObj);
            ret.put(VEHICLE_SETTING_KEY, vehicleObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }


}
