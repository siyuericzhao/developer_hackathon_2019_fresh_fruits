package com.example.myapplication;

import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.*;


import org.apache.commons.io.IOUtils;
import org.json.JSONObject;


public class CanonAPI {
//    public static String url = "http://192.168.1.2:8080/ccapi";
//    public static String url = "http://10.3.19.27:8080/ccapi";
    public static String url = "http://192.168.43.247:8080/ccapi";
    public static boolean init = false;

    public CanonAPI() {
        initialize();
    }

    public void initialize() {
        try {
            HttpURLConnection con = getConnection(url, "");
            int statusCode = con.getResponseCode();

            if(statusCode == 200) {
                init = true;
                System.out.println("helllo");
                halfPress();

            } else {
                System.out.println("fail to initalize");
            }
        } catch(Exception e) {
            System.out.println("fail to initialize");
        }
    }
    public void halfPress() {
        String path = "/ver100/shooting/control/shutterbutton/manual";
        try {
            HttpURLConnection con = getPostConnectionHelper(url, path);
            JsonObject json = new JsonObject();
            json.addProperty("action", "half_press");
            json.addProperty("af", true);
            OutputStream out = con.getOutputStream();
            out.write(json.toString().getBytes());
            out.close();
            int statusCode = con.getResponseCode();
            System.out.println(statusCode);
            if(statusCode == 200) {
                System.out.println("Half press works");
            } else {
                System.out.println("Half press fails");
            }
        } catch (IOException e) {
            System.out.println("Half press not working");
        }
    }

    public HttpURLConnection getConnection(String host, String path) throws IOException{
        String urlString = host + path;
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        return con;
    }

    private HttpURLConnection getPostConnectionHelper(String host, String path) throws IOException {
        HttpURLConnection con = getConnection(host,path);
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        return con;
    }

    public boolean takePhoto() {
        try {
            String path = "/ver100/shooting/control/shutterbutton";
            HttpURLConnection con = getPostConnectionHelper(url, path);
            System.out.println("helo3");
            JsonObject json = new JsonObject();
            json.addProperty("af", true);
            System.out.println("helo2");
            OutputStream os = con.getOutputStream();
            System.out.println("helo");
            os.write(json.toString().getBytes("UTF-8"));
            os.close();
            System.out.println("helo4");
            int statusCode = con.getResponseCode();
            System.out.println(statusCode);
            if(statusCode == 200) {
                return true;
            }
            System.out.println("helo5");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }


    public String getRecentPhotoLink() {
        String path = "/ver100/contents/sd/100CANON";
        try {
            HttpURLConnection con = getConnection(url, path);
            int statusCode = con.getResponseCode();
            if(statusCode != 200) {
                return "";
            }
            String responseStr = getBodyResponse(con);
            JsonObject jsonObj = parseJsonStringToJsonObject(responseStr);
            JsonArray ar = jsonObj.getAsJsonArray("url");
            int size = ar.size();
            String lastPic = ar.get(size - 1).getAsString();
            return lastPic;
        } catch (IOException e) {
            System.out.println();
        }
        return "";
    }

    public String getBodyResponse(HttpURLConnection con) throws IOException {
        String bodyResponse = IOUtils.toString(con.getInputStream(), "UTF-8");
        return bodyResponse;
    }

    public JsonObject parseJsonStringToJsonObject(String jsonStr) {
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