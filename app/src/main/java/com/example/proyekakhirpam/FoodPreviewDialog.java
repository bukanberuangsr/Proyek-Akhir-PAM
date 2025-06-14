package com.example.proyekakhirpam;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FoodPreviewDialog extends DialogFragment {
    private static final String ARG_IMAGE_URL = "image_url";
    private String imageUrl;

    public static FoodPreviewDialog newInstance(String imageUrl) {
        FoodPreviewDialog fragment = new FoodPreviewDialog();
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(ARG_IMAGE_URL);
        }
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_food_preview, null, false);
        ImageView imageView = view.findViewById(R.id.iv_full_image);
        Button btnClose = view.findViewById(R.id.btn_close);
        Button btnDownload = view.findViewById(R.id.btn_download);

        Glide.with(requireContext())
                .load(imageUrl)
                .into(imageView);

        btnClose.setOnClickListener(v -> dismiss());
        btnDownload.setOnClickListener(v -> {
            Glide.with(requireContext())
                    .asBitmap()
                    .load(imageUrl)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            String filename = "FoodImage_" + System.currentTimeMillis();
                            downloadImageToGallery(bitmap, filename);
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {}
                    });
        });

        Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(view);
        return dialog;
    }

    private void downloadImageToGallery(Bitmap bitmap, String filename) {
        Context context = requireContext();
        OutputStream fos;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename + ".jpg");
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/NamaFolderAplikasi");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                fos = resolver.openOutputStream(imageUri);
            } else {
                // Untuk Android 9 ke bawah
                String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/NamaFolderAplikasi";
                File file = new File(imagesDir);
                if (!file.exists()) file.mkdirs();
                File image = new File(file, filename + ".jpg");
                fos = new FileOutputStream(image);

                // Scan agar muncul di gallery
                MediaScannerConnection.scanFile(context, new String[]{image.getAbsolutePath()}, null, null);
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Toast.makeText(context, "Gambar berhasil disimpan ke Gallery", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Gagal menyimpan gambar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}