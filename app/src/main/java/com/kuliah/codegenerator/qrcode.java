package com.kuliah.codegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrcode extends AppCompatActivity {

    EditText edTxt;
    Button generateBtn, addlist;
    ImageView qrImage;
    String nm, tpe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        Generator gen = new Generator();

        edTxt = findViewById(R.id.edQrcode);
        qrImage = findViewById(R.id.qrImage);
        generateBtn = findViewById(R.id.qrbutton);
        addlist = findViewById(R.id.adListqr);


        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = edTxt.getText().toString();

                gen.makeQR(data, qrImage);
            }
        });

        addlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpanData();
            }
        });
    }

    private static String url_insert = "https://20200140003.praktikumtiumy.com/tambahcd.php";
    private static final String TAG = Barcode.class.getSimpleName();
    private static final String TAG_SUCCES = "success";
    int success;

    public void SimpanData() {
        nm = edTxt.getText().toString();
        tpe = "qrcode";

        if (nm.isEmpty()){
            Toast.makeText(qrcode.this, "Input data terlebih dahulu!", Toast.LENGTH_SHORT).show();
        }
        else {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

            StringRequest strReq = new StringRequest(Request.Method.POST, url_insert, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Response : " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);

                        success = jObj.getInt(TAG_SUCCES);
                        if (success == 1) {
                            Toast.makeText(qrcode.this, "Sukses simpan data", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(qrcode.this, "gagal", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Error : " + error.getMessage());

                    Toast.makeText(qrcode.this, "Gagal simpan data", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();

                    params.put("nama", nm);
                    params.put("tipe", tpe);

                    return params;
                }
            };
            requestQueue.add(strReq);
        }

    }
}