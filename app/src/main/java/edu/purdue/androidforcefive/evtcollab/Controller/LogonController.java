package edu.purdue.androidforcefive.evtcollab.Controller;

import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

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

import edu.purdue.androidforcefive.evtcollab.BusinessObjects.User;
import edu.purdue.androidforcefive.evtcollab.DataCollections.Interfaces.IDataCollectionChanged;
import edu.purdue.androidforcefive.evtcollab.DataCollections.UserCollection;

/**
 * Created by abuchmann on 29.11.2015.
 */
public class LogonController implements IDataCollectionChanged {
    private static LogonController instance;
    protected String token;
    protected User loggedInUser;
    private Semaphore semaphore;

    private LogonController() {


    }

    public static LogonController getInstance() {
        if (instance == null) {
            instance = new LogonController();
        }
        return instance;
    }

    public void login(String username, String password) {
        UserCollection.getInstance().addListener(instance);
        UserCollection.getInstance().initializeUsers();
        for (User user : UserCollection.getInstance().getUsers()) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                this.loggedInUser = user;
            }
        }
    }

    public boolean checkCredentialValidity(String username, String password) {
        UserCollection.getInstance().addListener(instance);
        semaphore = new Semaphore(1);
        UserCollection.getInstance().initializeUsers();
        System.out.println("Trying to aquire semaphore, current status: " + semaphore.availablePermits());
        System.out.println("Usercount: " + UserCollection.getInstance().getUsers().size());
        for (User user : UserCollection.getInstance().getUsers()) {
            if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean loggedIn() {
        return loggedInUser != null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    @Override
    public void onCollectionChanged() {

//        System.out.println("Task done, semaphore released. current status: " + semaphore.availablePermits());
    }
}
