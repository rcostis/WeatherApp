package com.example.weatherapp;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.*;
import org.w3c.dom.Text;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.ErrorListener;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText addy;
    Button go;
    String Google_API = "AIzaSyASMFasDiMzu3xcRWY4I-rPiqjibX7hw5s";
    String DarkSky_API = "bd984ad3548b8ad1c9ea30f8ededb6af";
    String lati;
    String longi;
    MapView mMapView;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addy = (EditText)findViewById(R.id.addressField);
        go = (Button)findViewById(R.id.goButton);

        initGoogleMap(savedInstanceState);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetCoordinates().execute(addy.getText().toString().replace(" ","+"));
                //new GetWeather().execute();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        find_weather();
                        setMap();
                    }
                }, 2000);

            }
        });


    }

    public void setMap(){
        //mMapView.getMapAsync().moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(lati),Double.parseDouble(longi))));

    }

    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView = (MapView) findViewById(R.id.mappy);
        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(30.26, -97.7)).title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(30.26,-97.7)));

    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    public void find_weather(){
        String url2 = "https://api.darksky.net/forecast/85886cf21db9be843b512d5f3bfbebf4/"+lati+","+longi;
        JsonObjectRequest jOb = new JsonObjectRequest(Request.Method.GET, url2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    System.out.println(response.toString());
                    JSONObject mainObject = response.getJSONObject("currently");
                    String tempNum = String.valueOf(mainObject.getDouble("temperature")) + " degrees F";
                    System.out.println(tempNum);
                    String windNum = String.valueOf(mainObject.getDouble("windSpeed")) + " mph";
                    String humidNum = String.valueOf(mainObject.getDouble("humidity"));
                    String precipNum = String.valueOf(mainObject.getDouble("precipProbability"));
                    //String precipType = mainObject.getString("precipType");


                    TextView changeTemp = (TextView) findViewById(R.id.temp);
                    changeTemp.setText(tempNum);
                    TextView changeWind = (TextView) findViewById(R.id.windSpeed);
                    changeWind.setText(windNum);
                    TextView changeHum = (TextView) findViewById(R.id.humidity);
                    changeHum.setText(humidNum);
                    TextView changePrecip = (TextView) findViewById(R.id.precip);
                    changePrecip.setText(precipNum);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jOb);

    }

    private class GetCoordinates extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + Google_API;
                response = http.getHTTPData(url);
                return response;
            }
            catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s){
            try{
                System.out.println(s);
                JSONObject jDog =  new JSONObject(s);
                lati = ((JSONArray)jDog.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                longi = ((JSONArray)jDog.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();



                //System.out.println(latitude + " and the long is " + longitude);

            }
            catch (JSONException j){
                j.printStackTrace();
            }
        }
    }

    private class GetWeather extends AsyncTask<String,Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String response2;
            try{

                HttpDataHandler http2 = new HttpDataHandler();
                //String url2 = "https://api.darksky.net/forecast/85886cf21db9be843b512d5f3bfbebf4/" + lati + "," + longi;
                String url2 = "https://api.darksky.net/forecast/85886cf21db9be843b512d5f3bfbebf4/37.8267,-122.4233";
                response2 = http2.getHTTPData(url2);
                return response2;
            }
            catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s){
            try{
                System.out.println(s);
                JSONObject jDark =  new JSONObject(s);
                String windNum = ((JSONArray)jDark.get("currently")).getJSONObject(0).get("windSpeed").toString() + " mph";
                String tempNum = jDark.getJSONObject("").getString("") + " degrees F";
                String humidNum = jDark.getJSONObject("").getString("") + "%";
                String precipNum = jDark.getJSONObject("").getString("") + "%";
                String precipType = jDark.getJSONObject("").getString("");


                TextView changeTemp = (TextView) findViewById(R.id.temp);
                changeTemp.setText(tempNum);
                TextView changeWind = (TextView) findViewById(R.id.windSpeed);
                changeWind.setText(windNum);
                TextView changeHum = (TextView) findViewById(R.id.humidity);
                changeHum.setText(humidNum);
                TextView changePrecip = (TextView) findViewById(R.id.precip);
                changePrecip.setText(precipType + " " + precipNum);




                //System.out.println(latitude + " and the long is " + longitude);

            }
            catch (JSONException j){
                j.printStackTrace();
            }
        }
    }

    public void search(View view) {
        String JSONString = "";
        
        EditText editText = (EditText) findViewById(R.id.addressField);
        String address = editText.getText().toString();
        address.replace(" ", "+");

        // Luca is driving now (Reese was before)
        String geocode_API_call = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + Google_API;


        try {

            JSONObject jObj = new JSONObject(JSONString);


            //Reese is driving now
            //take in coordinates
            String latitude = jObj.getJSONObject("location").getString("lat");
            String longitude = jObj.getJSONObject("location").getString("lng");
            System.out.println(latitude + " and the long is " + longitude);

            //Map
            /*
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapView);

            //System.out.println("Reese's " + longitude);

            //Dark Sky
            String darkSky_API_call = "https://api.darksky.net/forecast/bd984ad3548b8ad1c9ea30f8ededb6af/" + longitude + "," + latitude;
            String dsResult = getHTML(darkSky_API_call);
            String windNum = dsResult.getJSONObject("").getString("") + " mph";
            String tempNum = dsResult.getJSONObject("").getString("") + " degrees F";
            String humidNum = dsResult.getJSONObject("").getString("") + "%";
            String precipNum = dsResult.getJSONObject("").getString("") + "%";
            String precipType = dsResult.getJSONObject("").getString("");


            TextView changeTemp = (TextView) findViewById(R.id.temp);
            changeTemp.setText(tempNum);
            TextView changeWind = (TextView) findViewById(R.id.windSpeed);
            changeWind.setText(windNum);
            TextView changeHum = (TextView) findViewById(R.id.humidity);
            changeHum.setText(humidNum);
            TextView changePrecip = (TextView) findViewById(R.id.precip);
            changePrecip.setText(precipType + " " + precipNum);
*/
            //implement a way to take in more inputs

        } catch (Exception e) {
            System.out.println("well shit");
        }

    }

}