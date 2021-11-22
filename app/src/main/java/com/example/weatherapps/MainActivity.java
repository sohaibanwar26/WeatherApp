package com.example.weatherapps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.os.AsyncTask;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity {


    EditText cityName;
    TextView resultTextView;

    // when the user click on the button
    public void findWeather(View view) {


        // manages and controls the communication between the mobile apps and web services
        // get me the web services - web api
        // hide from the user screen , do not let the user see this interaction
        // work behind the scene
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        try{

            String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");
            // go to this URL, get the city name from the textbox, convert to string
            // opne the url, download the data based on the given URL below, use the api key given
            // or you can use the generic one for testing purposes

            DownloadTask task = new DownloadTask();  // defined later
            //task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName+ "&appid=b8e0d910d3cd9ce9eeabb9c1595719eb");

            task.execute("https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");
            // uses samples only actual API call not working


        }

        catch (UnsupportedEncodingException e) {

            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the UI controls from xml file
        cityName = (EditText) findViewById(R.id.cityName);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

    }

    // defined an inner class here , in a class definition, you can have a nested class
    // you can defined another java class here
    public class DownloadTask extends AsyncTask<String, Void, String> {

        // perform the download process in the background mode
        // background process
        // uses extends asynchTask keyword

        @Override
        protected String doInBackground(String... urls) {

            // set variables
            String result = "";  // no result yet
            URL url;  // create URL object , stored the web api URL
            HttpURLConnection urlConnection = null;  // http connection object


              try{

                  url = new URL(urls[0]);  // create URL object, use the first URL only

                  urlConnection = (HttpURLConnection) url.openConnection();  // open the URL link

                  InputStream in = urlConnection.getInputStream();  // read incoming data, download data

                  InputStreamReader reader = new InputStreamReader(in);  // put in buffer or memory

                  int data = reader.read();  // start reading

                  // use a while loop, to keep reading, while not end of file,
                  while (data != -1) {  // still got data inside the buffer or memory

                      char current = (char) data;  // convert the data into char data type

                      result += current;  // add the data to the result string

                      data = reader.read(); // read the next data

                  }

                  return result;  // all complete, return the string



              }

              catch (Exception e) {

                  Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

              }

            return null;


        }

        // after the background process completed, download process complete

        @Override
        protected void onPostExecute(String result) {

            try {

                String message = "";  // set message to empty, no message yet

                JSONObject jsonObject = new JSONObject(result);  // create the JSON object, convert string to JSON
                // the string result variables contains all the char data that you have downloaded from the website
                // now you need to do the conversion from raw data to json object

                String weatherInfo = jsonObject.getString("weather");  // inside the json object get me the weather data only, the rest no need

                JSONArray arr = new JSONArray(weatherInfo);// put the weather data inside the json object


                for (int i = 0; i < arr.length(); i++) {  // uses for loop to traverse the arrays

                    JSONObject jsonPart = arr.getJSONObject(i);  // get eh json object

                    String main = "";
                    String description = "";

                    main = jsonPart.getString("main");  // look for main part
                    description = jsonPart.getString("description");  // look for description

                    if (main != "" && description != "") {

                        message += main + ": " + description + "\r\n";  // add the main and description to the message

                    }

                }

                if (message != "") {  // if message is not empty

                    resultTextView.setText(message);  // display the contents of the message

                } else {

                    Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

                }


            }

            catch (JSONException e) {

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

            }
        }


    }

    }
