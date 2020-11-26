package com.fawzy.mapsapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText name , email , password ;
    private Button register ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        register = findViewById(R.id.reg);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    public void register(){
         final String Name  = name.getText().toString().trim();
         final String Email = email.getText().toString().trim();
         final String Password = password.getText().toString().trim();

         if(TextUtils.isEmpty(Name)){
             name.setError("please enter your full name");
             name.requestFocus();
         } else if(TextUtils.isEmpty(Email)){
             email.setError("please enter your email");
             email.requestFocus();
         } else if(TextUtils.isEmpty(Password)){
             password.setError("please enter your password");
             password.requestFocus();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.REGISTER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // converting response to json object
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {
                        // getting the user from response
                        JSONObject userjson = jsonObject.getJSONObject("data");
                        // cretaing new user object name-email-pass
                        User user = new User(userjson.getString("token"));
                        // storing the user in shared prefernces
                        SessionManager.getInstance(getApplicationContext()).userlogin(user);
                        // startring profile activity
                        finish();
                        startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }, new Response.ErrorListener(){
                @Override
                 public void onErrorResponse(VolleyError error){
                    Toast.makeText(RegisterActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                }
                // send info ali hadkhlha
            }){
                  protected Map<String,String> getparams() throws AuthFailureError {
                  Map<String,String> params = new HashMap<>();
                  params.put("content-type","application/json");
                  params.put("name",Name);
                  params.put("email",Email);
                  params.put("password",Password);
                  params.put("c_password",Password);
                  return params ;
                }
            } ;
           VolleySingleTone.getInstance(this).addtorequest(stringRequest);
    }










   }