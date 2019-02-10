package com.example.weatherapp;

import android.app.DownloadManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.*;
import org.w3c.dom.Text;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.transform.ErrorListener;

public class MainActivity extends AppCompatActivity {

    private static String USER_AGENT = "Mozilla/5.0";
    private String JSONstring_returned;

    String Google_API = "AIzaSyASMFasDiMzu3xcRWY4I-rPiqjibX7hw5s";
    String DarkSky_API = "bd984ad3548b8ad1c9ea30f8ededb6af";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestQueue = Volley.newRequestQueue(this);
    }

    public void search(View view) {
        String JSONString;
        
        EditText editText = (EditText) findViewById(R.id.addressField);
        String address = editText.getText().toString();
        address.replace(" ", "+");

        // Luca is driving now (Reese was before)
        String geocode_API_call = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + Google_API;


        try {



            JSONString = testFunc(geocode_API_call);
            //requestQueue.start();


            System.out.println("cuck me");

            System.out.println(JSONString);
            System.out.println("I like pie");
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
        //System.out.println(JSONString);

        /*try {

            JSONObject jsonObj = new JSONObject(JSONnotString);
            System.out.println("HI: " + jsonObj.toString());



        } catch (Exception e) {
            System.out.println("well shit 2");
        }
        */



    }

    //this is Luca driving


    public static String testFunc(String input) {
            try {
                String finalStr = "";
                URL url = new URL(input);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String str = "";
                while (null != (str = br.readLine())) {
                    finalStr += str;
                }
                return finalStr;
            } catch (Exception ex) {
                ex.printStackTrace();
                return "didnt work";
            }
        }



    public String getHTML(String toRead) throws Exception {

        //final String[] toReturn = new String[1];

        //JsonObjectRequest jRequest = new JsonObjectRequest(Request.Method.GET,toRead,null,

        StringRequest stringRequest = new StringRequest(Request.Method.GET, toRead,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        //System.out.println(response);
                        //toReturn[0] = response;
                        //JSONstring_returned = response;
                        //System.out.println("success bitch");

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        );

        requestQueue.add(stringRequest);

        return "poo";
    }

}