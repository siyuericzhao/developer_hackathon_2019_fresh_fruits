package com.example.myapplication;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * This class uses Canon CCAPI to take photo and get the most recent photo
 */
public class CanonAPI {
    public static String url = "http://192.168.43.247:8080/ccapi";
    public static boolean init = false;

    public CanonAPI() {
        initialize();
    }

    public void initialize() {
        try {
            HttpURLConnection con = Helper.getConnection(url, "");
            int statusCode = con.getResponseCode();

            if(statusCode == 200) {
                init = true;
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
            HttpURLConnection con = Helper.getPostConnectionHelper(url, path);
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

    public boolean takePhoto() {
        try {
            String path = "/ver100/shooting/control/shutterbutton";
            HttpURLConnection con = Helper.getPostConnectionHelper(url, path);
            JsonObject json = new JsonObject();
            json.addProperty("af", true);
            OutputStream os = con.getOutputStream();
            os.write(json.toString().getBytes("UTF-8"));
            os.close();
            int statusCode = con.getResponseCode();
            System.out.println(statusCode);
            if(statusCode == 200) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    public String getRecentPhotoLink() {
        String path = "/ver100/contents/sd/100CANON";
        try {
            HttpURLConnection con = Helper.getConnection(url, path);
            int statusCode = con.getResponseCode();
            if(statusCode != 200) {
                return "";
            }
            String responseStr = Helper.getBodyResponse(con);
            JsonObject jsonObj = Helper.parseJsonStringToJsonObject(responseStr);
            JsonArray ar = jsonObj.getAsJsonArray("url");
            int size = ar.size();
            String lastPic = ar.get(size - 1).getAsString();
            return lastPic;
        } catch (IOException e) {
            System.out.println();
        }
        return "";
    }
}