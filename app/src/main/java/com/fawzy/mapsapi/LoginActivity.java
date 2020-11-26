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
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password ;
    private Button login ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.mail);
        password = findViewById(R.id.pass);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               loginuser();
            }
        });

    }
    public void loginuser(){
        final String Email = email.getText().toString().trim();
        final String Password = password.getText().toString().trim();

        if (TextUtils.isEmpty(Email)){
            email.setError("please enter your email");
            email.requestFocus();
        }else{
            password.setError("please enter your password");
            password.requestFocus();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getJSONObject("success") != null) {
                        JSONObject userJson = jsonObject.getJSONObject("success");
                        User user = new User(
                                userJson.getString("token")
                        );
                        SessionManager.getInstance(getApplicationContext()).userlogin(user);
                        finish();
                        startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
        {
            protected Map<String,String> getparams() throws AuthFailureError{
                Map<String,String> params = new HashMap<>();
                params.put("content-type","application/json");
                params.put("email",Email);
                params.put("password",Password);
                return  params ;
            }
        } ;
        VolleySingleTone.getInstance(this).addtorequest(stringRequest);
    }

}