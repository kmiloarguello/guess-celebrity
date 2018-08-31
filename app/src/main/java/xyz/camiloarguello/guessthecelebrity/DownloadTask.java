package xyz.camiloarguello.guessthecelebrity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTask extends AsyncTask<String,Void,String > {
    @Override
    protected String doInBackground(String... strings) {

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {

            // Convert the string to URL
            url = new URL(strings[0]);

            // setup a URL connection with casting
            urlConnection = (HttpURLConnection) url.openConnection();

            // Hold input of data
            InputStream inputStream = urlConnection.getInputStream();

            // Read input data or read URL content
            InputStreamReader reader = new InputStreamReader(inputStream);

            // One character at the time
            // keep track the location of html content
            int data = reader.read();

            // Read all HTML data when exists (!= -1)
            while (data != -1){
                // current character being read
                char current = (char) data;

                // Append to result
                result += current;

                // data move on to the next character
                data = reader.read();
            }

            // When success return result HTML code
            return result;

        }catch (Exception e){
            e.printStackTrace();

            return "Failed X( X( X( X(";

        }

    }
}
