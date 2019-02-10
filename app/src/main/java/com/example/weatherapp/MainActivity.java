package com.example.weatherapp;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import com.google.android.gms.maps.SupportMapFragment;

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

public class MainActivity extends AppCompatActivity {

    EditText addy;
    Button go;
    String Google_API = "AIzaSyASMFasDiMzu3xcRWY4I-rPiqjibX7hw5s";
    String DarkSky_API = "bd984ad3548b8ad1c9ea30f8ededb6af";
    String lati;
    String longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addy = (EditText)findViewById(R.id.addressField);
        go = (Button)findViewById(R.id.goButton);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetCoordinates().execute(addy.getText().toString().replace(" ","+"));
                new GetWeather().execute();
            }
        });


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
                String url2 = "https://api.darksky.net/forecast/bd984ad3548b8ad1c9ea30f8ededb6af/" + longi + "," + lati;
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
                String windNum = jDark.getJSONObject("").getString("") + " mph";
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