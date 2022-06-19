package com.kuliah.codegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Arrays;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class viewCode extends AppCompatActivity {

    private String  id, nm, tpe;
    private TextView text;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        text = findViewById(R.id.textlist);
        image = findViewById(R.id.imgResult);

        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("kunci1");
        nm = bundle.getString("kunci2");
        tpe = bundle.getString("kunci3");

        text.setText(nm);

        showCode(nm, tpe);
    }

    public void showCode(String namaEd, String tipeEd){
        Generator gen = new Generator();
        switch (tipeEd){
            case "barcode":
                gen.makeBarcode(namaEd, image);
                break;
            case "qrcode":
                gen.makeQR(namaEd, image);
                break;
        }

    }
}