package edu.purdue.androidforcefive.evtcollab.BusinessObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import edu.purdue.androidforcefive.evtcollab.DataCollections.UserCollection;

/**
 * Created by abuchmann on 20.11.2015.
 */
public class User extends SuperItem {

    protected String firstName;
    protected String lastName;
    protected String userName;
    protected String password;
    protected String eMail;
    protected List<Integer> permittedCalendars = new ArrayList<>();

    public User(int id, String firstName, String lastName, String userName, String password, String eMail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
    }

    public User(String firstName, String lastName, String userName, String password, String eMail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
    }

    public User() {

    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void addPermissionToAccessCalendar(int calendarId) {
        permittedCalendars.add(calendarId);
    }

    public void removePermissionToAccessCalendar(int calendarId) {
        permittedCalendars.remove(new Integer(calendarId));
    }

    public List<Integer> getPermittedCalendars() {
        return permittedCalendars;
    }

    @Override
    public void save() {
        if (id == 0) {
            // It's a new user, we have create it on the Api
            UserCollection.getInstance().addUser(this);
        } else {
            // It's an existing user, update it!
            UserCollection.getInstance().updateUser(this);
        }
    }

    @Override
    public void destroy() {
        UserCollection.getInstance().destroyUser(this);
    }

    @Override
    public boolean contentEquals(Object other) {
        if (this.firstName.equals(((User) other).getFirstName()) &&
                this.lastName.equals(((User) other).getLastName()) &&
                this.userName.equals(((User) other).getUserName()) &&
                this.password.equals(((User) other).getPassword()) &&
                this.eMail.equals(((User) other).geteMail()) &&
                this.permittedCalendars.size() == ((User) other).getPermittedCalendars().size()) {
            for (int permittedCalendar : permittedCalendars) {
                if (!((User) other).getPermittedCalendars().contains(permittedCalendar)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void applyJsonObject(JSONObject object) {
        try {
            if (object.has("id"))
                this.id = object.getInt("id");
            this.firstName = object.getString("firstname");
            this.lastName = object.getString("lastname");
            this.userName = object.getString("username");
            this.password = object.getString("password");
            this.eMail = object.getString("email");
            JSONArray calendars = object.getJSONArray("calendars");
            for (int i = 0; i < calendars.length(); i++) {
                this.addPermissionToAccessCalendar(calendars.getJSONObject(i).getInt("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject getItemAsJsonObject() {
        JSONObject userJson = new JSONObject();
        JSONArray permittedCalendarsJson = new JSONArray();
        try {
            for (int permittedCalendar : permittedCalendars) {
                permittedCalendarsJson.put(new JSONObject().put("id", permittedCalendar));
            }
            userJson.put("firstname", firstName);
            userJson.put("lastname", lastName);
            userJson.put("email", eMail);
            userJson.put("username", userName);
            userJson.put("password", password);
            userJson.put("calendars", permittedCalendarsJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userJson;
    }
}
