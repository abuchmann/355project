package edu.purdue.androidforcefive.evtcollab.Controller;

import android.os.AsyncTask;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by abuchmann on 29.11.2015.
 */
public class LogonController {
    private static LogonController instance;
    protected String token;
    protected String username;
    protected String password;
    private Semaphore semaphore = new Semaphore(0);

    private LogonController() {
    }

    private class TokenDownloader extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            System.out.println("executed1");
            URL url = null;
            try {
                url = new URL("http://192.168.109.128:3000/token");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //connection.setRequestMethod("GET");
                //
                //connection.setDoInput(true);
                //connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Authorization", "Basic " +
                        Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP));
                //connection.connect();
                //System.out.println(connection.getResponseMessage() + " " + connection.getResponseCode());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String output, jsonString = "";
                //System.out.println("Output from Server .... \n");
                while ((output = bufferedReader.readLine()) != null) {
                    //System.out.println(output);
                    jsonString += output;
                }
                connection.disconnect();
                JSONObject jsonToken = new JSONObject(jsonString);
                token = jsonToken.getString("token");

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            semaphore.release();
            super.onPostExecute(aVoid);
        }
    }

    public static LogonController getInstance() {
        if (instance == null)
            instance = new LogonController();
        return instance;
    }

    public void login(String username, String password) {
        this.username = username;
        this.password = password;
        (new TokenDownloader()).execute();
        try {
            semaphore.tryAcquire(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        semaphore.release();
    }

    public String getToken() {
        return token;
    }
}
