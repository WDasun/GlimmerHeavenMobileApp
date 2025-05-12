package com.example.glimmerheaven.data.repository.supabase;

import com.example.glimmerheaven.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class SupabaseImageUploader {

    private static final String SUPABASE_URL = "https://ysluwlqnpugcebnxvjgj.supabase.co";
    private static final String BUCKET_NAME = "profile-images";

    public static void uploadImage(File file, String fileName, UploadCallback callback) {
        String SUPABASE_API_KEY = BuildConfig.SUPAASE_API_KEY;
        new Thread(() -> {
            try {
                String urlStr = SUPABASE_URL + "/storage/v1/object/" + BUCKET_NAME + "/" + fileName;

                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + SUPABASE_API_KEY);
                connection.setRequestProperty("Content-Type", "image/jpeg");
                connection.setRequestProperty("x-upsert", "true"); // Overwrite if exists
                connection.setDoOutput(true);

                // Write file data
                OutputStream outputStream = connection.getOutputStream();
                FileInputStream inputStream = new FileInputStream(file);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == 200 || responseCode == 201) {
                    callback.onSuccess(urlStr);
                } else {
                    callback.onFailure("Upload failed with response code: " + responseCode);
                }

                connection.disconnect();
            } catch (IOException e) {
                callback.onFailure("Exception: " + e.getMessage());
            }
        }).start();
    }

    public interface UploadCallback {
        void onSuccess(String savedUrl);
        void onFailure(String error);
    }
}

