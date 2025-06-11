package com.example.proyekakhirpam;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Helper class for Cloudinary unsigned upload.
 * Requires OkHttp dependency in build.gradle:
 * implementation 'com.squareup.okhttp3:okhttp:4.9.3'
 */
public class CloudinaryManager {
    private static final String CLOUD_NAME = "donwrrlnv";
    private static final String UPLOAD_PRESET = "asset-pam";

    public interface UploadCallback {
        void onSuccess(String imageUrl);
        void onError(Exception e);
    }

    /**
     * Upload image file to Cloudinary unsigned preset.
     * @param file The image file to upload.
     * @param callback Callback for result (success: url, error: exception)
     */
    public static void uploadImage(File file, UploadCallback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("image/*")))
                .addFormDataPart("upload_preset", UPLOAD_PRESET)
                .build();

        String url = "https://api.cloudinary.com/v1_1/" + CLOUD_NAME + "/image/upload";

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> callback.onError(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> callback.onError(new IOException("Unexpected code " + response)));
                    return;
                }
                try {
                    JSONObject res = new JSONObject(response.body().string());
                    String imageUrl = res.getString("secure_url");
                    runOnUiThread(() -> callback.onSuccess(imageUrl));
                } catch (JSONException e) {
                    runOnUiThread(() -> callback.onError(e));
                }
            }
        });
    }

    public static File uriToFile(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        String fileName = "temp_" + System.currentTimeMillis() + ".jpg";
        File tempFile = new File(context.getCacheDir(), fileName);
        OutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return tempFile;
    }

    // Utility to run code on UI thread
    private static void runOnUiThread(Runnable runnable) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }
}
