package com.example.pdfreader3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreateActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 100;
    String folderPath = Environment.getDataDirectory().getAbsolutePath()
           + "/storage/emulated/0/your_folder_name";
    File directory = new File("/sdcard/your_folder_name/");

    Button camera,upload,convert;
     ImageView img;
     TextView title;
     ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        askPermisson();
        camera = findViewById(R.id.convertor_toll_camera);
        upload = findViewById(R.id.convertor_toll_upload);
        convert = findViewById(R.id.convertor_toll_convert);
        img = findViewById(R.id.convertor_toll_imageView);
        title = findViewById(R.id.convertor_toll_fileName);
        progressBar = findViewById(R.id.progressBar);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(directory.exists()) {
                    progressBar.setVisibility(View.VISIBLE);

                    //startActivityForResult(getCameraIntent(CreateActivity.this,System.currentTimeMillis()+""),REQUEST_CODE);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION & Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateActivity.this, "try again", Toast.LENGTH_SHORT).show();
                    createFolder();
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(directory.exists()){
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,REQUEST_CODE);

                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(CreateActivity.this,"try again",Toast.LENGTH_SHORT).show();
                    createFolder();
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.GONE);

        if(requestCode != REQUEST_CODE) return;
        if( resultCode == RESULT_CANCELED) return;

        if(data != null){

         Bitmap photo = (Bitmap) data.getExtras().get("data");

         Uri selectedImage = getImageUri(this,photo);
         String[] filePathColumn = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};
         Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,
                 null,null);
         cursor.moveToFirst();
         int filePath = cursor.getColumnIndex(filePathColumn[0]);
         int fileName = cursor.getColumnIndex(filePathColumn[1]);
         String path = cursor.getString(filePath);
         String name = cursor.getString(fileName);
         cursor.close();
         img.setImageURI(Uri.parse(path));

         title.setText(name);


        }
        else{
            Toast.makeText(this,"select a image",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



    private void askPermisson(){
        if ((ContextCompat.checkSelfPermission(CreateActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(CreateActivity.this,
                Manifest.permission.CAMERA)) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA},REQUEST_CODE);
        }
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            createFolder();
        }
    }
    private void createFolder(){
        File folder = new File(folderPath);
        if(!folder.exists()){
            folder.mkdir();
        }
        directory.mkdirs();
    }
    /*

    private boolean imageToPDF(String path,String name){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

        }else{
            return false;
        }
    }
    */

}