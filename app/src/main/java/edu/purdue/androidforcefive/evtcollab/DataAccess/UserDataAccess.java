package edu.purdue.androidforcefive.evtcollab.DataAccess;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.User;

/**
 * Created by abuchmann on 20.11.2015.
 */
public class UserDataAccess extends SuperDataAccess implements IDataAccess<User> {
    private String ApiUrl = "http://192.168.109.128:3000/api/";
    private int ApiVersion = 1;

    @Override
    public List<User> getItems() throws MalformedURLException {
        List<User> users = new ArrayList<>();
        JSONArray usersJson = super.executeGet(ApiUrl + "v" + ApiVersion + "/users");

        try {
            for (int i = 0; i < usersJson.length(); i++) {
                JSONObject e = usersJson.getJSONObject(i);
                users.add(new User(e.getInt("id"),
                        e.getString("firstname"),
                        e.getString("lastname"),
                        e.getString("username"),
                        e.getString("password"),
                        e.getString("email")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public User getItem(int id) {
        return null;
    }

    @Override
    public User createItem(User item) {
        return null;
    }

    @Override
    public User updateItem(User item) {
        return null;
    }

    @Override
    public int removeItem(User item) {
        return 0;
    }
}
