package com.kuliah.codegenerator.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kuliah.codegenerator.Datacode;
import com.kuliah.codegenerator.MainActivity;
import com.kuliah.codegenerator.R;
import com.kuliah.codegenerator.app.AppController;
import com.kuliah.codegenerator.database.Code;
import com.kuliah.codegenerator.edit_data;
import com.kuliah.codegenerator.viewCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CodeAdapter extends RecyclerView.Adapter<CodeAdapter.CodeViewHolder> {
    private ArrayList<Code> listData;

    public CodeAdapter(ArrayList<Code> listData) { this.listData = listData; }

    @Override
    public CodeAdapter.CodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutinf = LayoutInflater.from(parent.getContext());
        View view = layoutinf.inflate(R.layout.row_data_code,parent, false);
        return new CodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CodeViewHolder holder, int position) {
        String id,nm, tpe;
        id = listData.get(position).getId();
        nm = listData.get(position).getNama();
        tpe = listData.get(position).getTipe();

        holder.namaTxt.setText(nm);
        holder.tipeTxt.setText(tpe);

        holder.cardku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("kunci1",id);
                b.putString("kunci2",nm);
                b.putString("kunci3",tpe);

                ///butuh edit
                Intent intent = new Intent(v.getContext(), viewCode.class);
                intent.putExtras(b);
                v.getContext().startActivity(intent);
            }
        });

        holder.cardku.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu pm = new PopupMenu(view.getContext(), view);
                pm.inflate(R.menu.popup1);

                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.edit:
                                Bundle bendle = new Bundle();
                                bendle.putString("kunci1",id);
                                bendle.putString("kunci2",nm);
                                bendle.putString("kunci3",tpe);

                                    ///butuh edit
                                Intent intent = new Intent(view.getContext(), edit_data.class);
                                intent.putExtras(bendle);
                                view.getContext().startActivity(intent);
                                break;

                            case R.id.hapus:
                                AlertDialog.Builder alertdb = new AlertDialog.Builder(view.getContext());
                                alertdb.setTitle("Yakin " +nm+ " akan dihapus?");
                                alertdb.setMessage("Tekan Ya untuk menghapus");
                                alertdb.setCancelable(false);
                                alertdb.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        HapusData(id);
                                        Toast.makeText(view.getContext(), "Data "+id+" telah dihapus", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(view.getContext(), Datacode.class);
                                        view.getContext().startActivity(intent);
                                    }
                                });
                                alertdb.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog adlg= alertdb.create();
                                adlg.show();
                                break;
                        }
                        return true;
                    }
                });
                pm.show();
                return true;
            }
        });
    }

    private void HapusData(final String idx) {
        String url_update = "https://20200140003.praktikumtiumy.com/deletecd.php";
        final String TAG = edit_data.class.getSimpleName();
        final String TAG_SUCCES = "success";
        final int[] sukses = new int[1];

        StringRequest stringReq = new StringRequest(Request.Method.POST, url_update, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Respon: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject();
                    sukses[0] = jObj.getInt(TAG_SUCCES);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error :"+error.getMessage());
            }
        })
        {
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id", idx);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringReq);

    }

    @Override
    public int getItemCount() {
        return (listData != null)?listData.size() : 0;
    }

    public class CodeViewHolder extends RecyclerView.ViewHolder {
        private CardView cardku;
        private TextView namaTxt,tipeTxt;
        public CodeViewHolder(View view) {
            super(view);
            cardku = (CardView) view.findViewById(R.id.kartuku);
            namaTxt = (TextView) view.findViewById(R.id.txtCode);
            tipeTxt = (TextView) view.findViewById(R.id.txtTipe);
        }
    }

}
