package com.kuliah.codegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class edit_data extends AppCompatActivity {
    ImageView imgview;
    EditText edNama;
    Button editBtn;
    Spinner spTipe;
    String id, nm, tpe, namaEd = "", tipeEd = "";
    int sukses;

    private static String url_update = "https://20200140003.praktikumtiumy.com/updatecd.php";
    private static final String TAG = edit_data.class.getSimpleName();
    private static final String TAG_SUCCES = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        imgview = findViewById(R.id.imgedt);
        edNama = findViewById(R.id.editNm);
        editBtn = findViewById(R.id.buttonEdit);
        spTipe = findViewById(R.id.sptype);

        String[] items = new String[]{"qrcode", "barcode"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spTipe.setAdapter(adapter);
        spTipe.setSelection(0);


        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("kunci1");
        nm = bundle.getString("kunci2");
        tpe = bundle.getString("kunci3");

        showCode(nm, tpe);

        edNama.setText(nm);
        if (tpe.equals("qrcode")){
            spTipe.setSelection(0);
        }
        else if(tpe.equals("barcode")){
            spTipe.setSelection(1);
        }


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditData();
            }
        });
    }

    public void showCode(String namaEd, String tipeEd){
        Generator gen = new Generator();
        switch (tipeEd){
            case "barcode":
                gen.makeBarcode(namaEd, imgview);
                break;
            case "qrcode":
                gen.makeQR(namaEd, imgview);
                break;
        }
    }

    public void EditData(){
        namaEd = edNama.getText().toString();
        tipeEd = spTipe.getSelectedItem().toString();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon:" + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    sukses = jObj.getInt(TAG_SUCCES);
                    if (sukses == 1) {
                        Toast.makeText(edit_data.this, "Sukses mengedit data", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(edit_data.this, "gagal", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error : "+error.getMessage());
                Toast.makeText(edit_data.this, "Gagal Edit data", Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                params.put("nama",namaEd);
                params.put("tipe",tipeEd);

                return params;
            }
        };
        requestQueue.add(stringReq);
        CallHomeActivity();
    }

    private void CallHomeActivity() {
        Intent inten = new Intent(getApplicationContext(),Datacode.class);
        startActivity(inten);
        finish();
    }

}