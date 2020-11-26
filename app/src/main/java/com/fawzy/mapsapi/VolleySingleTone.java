package com.fawzy.mapsapi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleTone {

    public static VolleySingleTone Instance ;
    private static Context mcontext ;
    private RequestQueue requestQueue ;

    public VolleySingleTone(Context context){
        mcontext = context ;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleTone getInstance(Context context){
        if (Instance == null){
            Instance = new VolleySingleTone(context);
        }
        return  Instance ;
    }

    // tnfiz al request aw al talb btaa3i
    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
           requestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());
        }
        return  requestQueue ;
    }

    // bi5od al request btaa3i w ywadi lal server 3shan ygbli response
    public <T> void addtorequest(Request<T> request){
        getRequestQueue().add(request);
    }












}
