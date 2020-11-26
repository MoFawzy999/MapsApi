package com.fawzy.mapsapi;

import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener,
              GoogleMap.OnMapLongClickListener
             , GoogleMap.OnCameraIdleListener {

    private GoogleMap mMap;
    private String token;
    private List<Place> placelist;
    private List<Marker> markerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (SessionManager.getInstance(this).isLoggedin()) {
            if (SessionManager.getInstance(this).getToken() != null) {
                token = SessionManager.getInstance(this).getToken().getToken();
            } else {
                finish();
                startActivity(new Intent(MapsActivity.this, LoginActivity.class));
                // 3shan maykmlsh al code
                return;
            }
        }


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);

        markerList = new ArrayList<>();
        placelist = new ArrayList<>();
        placelist = getData();

        for (Place p : placelist) {
            markerList.add(mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(p.getLatitude())
                    , Double.parseDouble(p.getLongitude()))).title(p.getName()).zIndex(p.getId())));
        }

        for (Marker m : markerList) {
            LatLng latLng = new LatLng(m.getPosition().latitude, m.getPosition().longitude);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }

    // lma agi ahrak al camera w agi aw2af y3ml ay ta7dis
    @Override
    public void onCameraIdle() {
        placelist = getData();
        for (Place p : placelist) {
            markerList.add(mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(p.getLatitude())
                    , Double.parseDouble(p.getLongitude()))).title(p.getName()).zIndex(p.getId())));
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent intent = new Intent(MapsActivity.this, AddActivity.class);
        intent.putExtra("latitude", latLng.latitude);
        intent.putExtra("longitude", latLng.longitude);
        startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this, ShowActivity.class);
        intent.putExtra("latitude", marker.getPosition().latitude);
        intent.putExtra("longitude", marker.getPosition().longitude);
        intent.putExtra("title", marker.getTitle());
        intent.putExtra("id", marker.getZIndex());
        startActivity(intent);
        return false;
    }

    public List<Place> getData() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Utils.ALL_DATA_URL,null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                  JSONArray  jsonArray = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject placeobject = jsonArray.getJSONObject(i);
                        Place place = new Place();
                        place.setId(placeobject.getInt("id"));
                        place.setLatitude(placeobject.getString("latitude"));
                        place.setLongitude(placeobject.getString("longitude"));
                        place.setName(placeobject.getString("name"));
                        placelist.add(place);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        return  placelist ;
    }




}