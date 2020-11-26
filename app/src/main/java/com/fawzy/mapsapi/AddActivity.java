package com.fawzy.mapsapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    private EditText latitude , longitude , tittle ;
    private Button add ;
    private Bundle extras ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        tittle = findViewById(R.id.tittle);
        add = findViewById(R.id.saveButton);

        extras = getIntent().getExtras();
        if (extras != null) {
            latitude.setText(String.valueOf(extras.getDouble("latitude")));
            longitude.setText(String.valueOf(extras.getDouble("longitude")));
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                finish();
                startActivity(new Intent(AddActivity.this,MapsActivity.class));
            }
        });

    }

    public void  saveData(){
        final String token = SessionManager.getInstance(this).getToken().getToken();
        final String Latitude = latitude.getText().toString().trim();
        final String Longitude = longitude.getText().toString().trim();
        final String Title = tittle.getText().toString().trim();

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("latitude",Latitude);
            postparams.put("longitude",Longitude);
            postparams.put("title",Title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Utils.ALL_DATA_URL,postparams
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")){
                        finish();
                        startActivity(new Intent(AddActivity.this,MapsActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            protected Map<String, String> getheaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer" + token);
                return params;
            }
        };
        VolleySingleTone.getInstance(this).addtorequest(jsonObjectRequest);
    }



}