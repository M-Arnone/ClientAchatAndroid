package com.example.clientachatandroid.model;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private final Properties properties;

    public ConfigReader(Context context) {
        properties = new Properties();
        loadProperties(context);
    }

    private void loadProperties(Context context) {
        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open("config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("port"));
    }

    public String getServerIP() {
        return properties.getProperty("server-ip");
    }
}
