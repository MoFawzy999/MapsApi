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

public class EditActivity extends AppCompatActivity {

    private EditText latitude , longitude , title ;
    private Button edit ;
    private  Bundle extras ;
    private  int textnum = 0  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        title = findViewById(R.id.tittle);
        edit = findViewById(R.id.edit);

        extras = getIntent().getExtras();
        if (extras != null) {
            latitude.setText(String.valueOf(extras.getDouble("latitude")));
            longitude.setText(String.valueOf(extras.getDouble("longitude")));
            title.setText(extras.getString("title"));
            textnum = extras.getInt("id");
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editplace(textnum);
                finish();
                startActivity(new Intent(EditActivity.this,MapsActivity.class));
            }
        });

    }


      public void editplace(int id){
          final String token = SessionManager.getInstance(this).getToken().getToken();
          final String Latitude = latitude.getText().toString().trim();
          final String Longitude = longitude.getText().toString().trim();
          final String Title = title.getText().toString().trim();

          JSONObject postparams = new JSONObject();
          try {
              postparams.put("latitude",Latitude);
              postparams.put("longitude",Longitude);
              postparams.put("title",Title);
          } catch (JSONException e) {
              e.printStackTrace();
          }

          JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, Utils.ALL_DATA_URL+"/"+id,postparams
                  , new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                  try {
                      if (response.getBoolean("success")){
                          finish();
                          startActivity(new Intent(EditActivity.this,MapsActivity.class));
                      }
                  } catch (JSONException e) {
                      e.printStackTrace();
                  }

              }
          }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  Toast.makeText(EditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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








}