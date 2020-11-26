package com.fawzy.mapsapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowActivity extends AppCompatActivity {

    private TextView latitude , longitude , tittle ;
    private Button delete , edit , logout ;
    private Bundle extras ;
    private int num = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        tittle =  findViewById(R.id.name);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        logout = findViewById(R.id.logout);

        extras = getIntent().getExtras();
        if (extras != null) {
            latitude.setText(String.valueOf(extras.getDouble("latitude")));
            longitude.setText(String.valueOf(extras.getDouble("longitude")));
            tittle.setText(extras.getString("title"));

            float testnum = extras.getInt("id");
            num = Math.round(testnum);
        }
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteplace(num);
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (num != 0){
                        Intent intent =  new Intent(ShowActivity.this, EditText.class);
                        intent.putExtra("latitude",latitude.getText().toString());
                        intent.putExtra("longitude",longitude.getText().toString());
                        intent.putExtra("title",tittle.getText().toString());
                        startActivity(intent);
                    }
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SessionManager.getInstance(getApplicationContext()).logout();
                    finish();
                    startActivity(new Intent(ShowActivity.this,MapsActivity.class));
                }
            });

    }



    public void deleteplace(int id){

        String token = SessionManager.getInstance(this).getToken().getToken();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Utils.DELETE_URL+id,null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")){
                        finish();
                        startActivity(new Intent(ShowActivity.this,MapsActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            protected Map<String, String> getheaders() {
                Map<String, String> params = new HashMap<>();
                params.put("content-value", "application/json");
                params.put("Authorization", "Bearer" + token);
                return params;
            }
        };
        VolleySingleTone.getInstance(this).addtorequest(jsonObjectRequest);
    }


}