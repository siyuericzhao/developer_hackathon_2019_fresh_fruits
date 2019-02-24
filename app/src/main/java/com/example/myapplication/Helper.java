package com.example.myapplication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Helper {
    public static HttpURLConnection getConnection(String host, String path) throws IOException {
        String urlString = host + path;
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        return con;
    }

    public static HttpURLConnection getPostConnectionHelper(String host, String path) throws IOException {
        HttpURLConnection con = getConnection(host,path);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        return con;
    }

    public static String getBodyResponse(HttpURLConnection con) throws IOException {
        String bodyResponse = IOUtils.toString(con.getInputStream(), "UTF-8");
        return bodyResponse;
    }

    public static JsonObject parseJsonStringToJsonObject(String jsonStr) {
        try {
            JsonParser parser = new JsonParser();
            if(!parser.parse(jsonStr).isJsonObject()) {
                return null;
            }
            JsonObject jsonObj = parser.parse(jsonStr).getAsJsonObject();
            return jsonObj;
        } catch (JsonSyntaxException e) {
            return null;
        }
    }
}
