package edu.purdue.androidforcefive.evtcollab.DataAccess;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by abuchmann on 20.11.2015.
 */
public class SuperDataAccess {
    protected URL url;

    protected JSONArray executeGet(String url) throws MalformedURLException {
        this.url = new URL(url);
        try {
            HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer jsonString = new StringBuffer(100000);
            String tmp;
            while ((tmp = bufferedReader.readLine()) != null)
                jsonString.append(tmp).append("\n");
            bufferedReader.close();
            return new JSONArray(jsonString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Return an empty object if nothing came back.
        return new JSONArray();
    }

}
