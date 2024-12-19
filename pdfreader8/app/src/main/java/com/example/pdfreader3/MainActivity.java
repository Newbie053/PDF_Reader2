package com.example.pdfreader3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.BasePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainAdapter adapter;
    private List<File>pdfList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EditText editTextSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        editTextSearch = findViewById(R.id.searchText);

        runtimePermisson();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterList(String.valueOf(editable));
            }
        });
    }

    private void runtimePermisson()
    {
        Dexter.withContext(MainActivity.this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displayPdf();
//                        if(multiplePermissionsReport.areAllPermissionsGranted()) {
//
//                        }
//                        else{
//                            runtimePermisson();
//                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
//                .withListener(new PermissionListener() {
//                    @Override
//                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                        permissionToken.continuePermissionRequest();
//
//                    }
//                })
                .check();
    }



    public ArrayList<File> findPdf (File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        if(files == null) {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }

        for(File singleFile : files)
        {
            if(singleFile.isDirectory() && !singleFile.isHidden())
            {
                arrayList.addAll(findPdf(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".pdf")){
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    public void displayPdf()
    {
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        File file = Environment.getExternalStorageDirectory();
        if(file == null){
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
            return;
        }
        pdfList.addAll(findPdf(file));

        adapter = new MainAdapter(this,pdfList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.GONE);

    }

    private void filterList(String key){
        List<File> filteredList = new ArrayList<>();
        for(File file : pdfList){
            if(file.getName().contains(key)){
                filteredList.add(file);
            }
        }
        adapter = new MainAdapter(this,filteredList);
        recyclerView.setAdapter(adapter);
    }

}
