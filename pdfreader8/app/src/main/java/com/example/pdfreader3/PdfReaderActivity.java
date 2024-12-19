package com.example.pdfreader3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;

public class PdfReaderActivity extends AppCompatActivity {

    private PDFView pdfView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_reader);

        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);

        pdfView.fromFile(MainAdapter.selectedFile)
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                        progressBar.setVisibility(View.GONE);
                    }
                })
                .onError(t -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }).load();

    }

}
