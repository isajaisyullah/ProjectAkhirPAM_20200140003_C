package com.kuliah.codegenerator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kuliah.codegenerator.adapter.CodeAdapter;
import com.kuliah.codegenerator.database.Code;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Datacode extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CodeAdapter adapter;
    private ArrayList<Code> codeArrayList = new ArrayList<>();

    private static String url_select = "https://20200140003.praktikumtiumy.com/bacacode.php";
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String TAG_ID   = "id";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_TIPE = "tipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datacode);

        recyclerView = findViewById(R.id.recylerView);
        BacaData();
        adapter = new CodeAdapter(codeArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Datacode.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void BacaData() {
        codeArrayList.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jArr = new JsonArrayRequest(url_select, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                //parsing json
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);

                        Code item = new Code();

                        item.setId(obj.getString(TAG_ID));
                        item.setNama(obj.getString(TAG_NAMA));
                        item.setTipe(obj.getString(TAG_TIPE));

                        //menambah item ke array
                        codeArrayList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(Datacode.this, "gagal", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jArr);
    }
}